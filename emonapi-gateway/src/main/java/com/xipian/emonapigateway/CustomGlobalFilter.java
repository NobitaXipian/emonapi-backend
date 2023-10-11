package com.xipian.emonapigateway;

import com.xipian.emonapiclientsdk.utils.SignUtils;
import com.xipian.emonapicommon.model.entity.InterfaceInfo;
import com.xipian.emonapicommon.model.entity.User;
import com.xipian.emonapicommon.service.InnerInterfaceInfoService;
import com.xipian.emonapicommon.service.InnerUserInterfaceInfoService;
import com.xipian.emonapicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义全局过滤器
 * @author <a href="https://github.com/nobitaxipian">XIPIAN</a>
 * @date 2023/10/8
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://localhost:8123";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("custom global filter");
        // 请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + path);
        log.info("请求方法：" + method);
        log.info("请求参数：" + request.getQueryParams());
        log.info("请求服务地址："+request.getURI());
        log.info("请求接受地址：" + request.getLocalAddress().getHostString());
        String sourceAddr = request.getRemoteAddress().getHostString();
        log.info("请求来源地址：" + sourceAddr);

        ServerHttpResponse response = exchange.getResponse();
        // 2. 访问控制 - 黑白名单
        if (!IP_WHITE_LIST.contains(sourceAddr)){
            return handleNoAuth(response);
        }

        // 3.用户鉴权- AK/SK 是否合法
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");

        // 数据库查是否accessKey是否已分配，并把对应的secretKey取出
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        }catch (Exception e){
            log.error("getInvokeUser error",e);
        }
        if (invokeUser == null){
            return handleNoAuth(response);
        }

        // if (! "gaomu".equals(accessKey)) {
        //     return handleNoAuth(response);
        // }

        //TODO 应该查询随机数是否已经使用，可以使用Redis存储？内存压力可能会很大
        //模拟校验随机数
        if (Long.parseLong(nonce) > 10000){
            return handleNoAuth(response);
        }

        // 时间戳和当前时间不能超过5分钟
        final long FIVE_MIN = 60 * 5L;
        long currentTime = System.currentTimeMillis() / 1000;
        long expirationTime = Long.parseLong(timestamp) + FIVE_MIN;
        if ( currentTime > expirationTime){
            return handleNoAuth(response);
        }

        // 从数据库中查出secretKey
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.getSign(body, secretKey);
        if (sign == null || !sign.equals(serverSign)){
            return handleNoAuth(response);
        }

        // 4.请求的模拟接口是否存在
        InterfaceInfo interfaceInfo = null;
        try{
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path,method);
        }catch (Exception e){
            log.error("getInterfaceInfo error",e);
        }
        if (interfaceInfo == null){
            return handleNoAuth(response);
        }
        // 5.请求转发，调用模拟接口
        //Mono<Void> filter = chain.filter(exchange);

        // 6.调用成功后打印响应日志
        return handleResponse(exchange,chain,interfaceInfo.getId(),invokeUser.getId());

        // return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }
    public Mono<Void> handleInvokeError(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    /**
     * 处理响应
     * @param exchange ServerWebExchange对象，用于获取请求和响应相关信息
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            // 获取原始的响应对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 获取响应的状态码
            HttpStatus statusCode = originalResponse.getStatusCode();
            // 判断状态码是否为200 OK
            if (statusCode == HttpStatus.OK) {
                // 创建一个装饰器对象，用于修改响应的行为
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        // 判断响应体是否为Flux类型
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 7.调用成功，接口调用次数 + 1 invokeCount
                                        try {
                                            innerUserInterfaceInfoService.invokeCount(interfaceInfoId,userId);
                                        }catch (Exception e){
                                            log.error("invokeCount error",e);
                                        }
                                        // 读取响应体的内容并释放内存
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);
                                        // 构建日志信息
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8);
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        log.info(sb2.toString(), rspArgs.toArray());
                                        // 将修改后的响应体内容重新封装为DataBuffer对象
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            log.error("响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 使用装饰器对象替换原始的响应对象，并继续处理请求
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            // 返回原始的响应对象，进行降级处理
            return chain.filter(exchange);
        } catch (Exception e) {
            log.error("网关处理响应异常：" + e);
            return chain.filter(exchange);
        }
    }
}