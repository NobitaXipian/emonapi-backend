package com.xipian.emonapiinterface.controller;

import com.xipian.emonapiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 名称API
 *
 * @author <a href="https://github.com/nobitaxipian">XIPIAN</a>
 * @date 2023/10/3
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public String getNameByGet(String name) {
        return "GET 你的名字是" +name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是" +name;
    }

    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest request) {
        // String accessKey = request.getHeader("accessKey");
        // String nonce = request.getHeader("nonce");
        // String timestamp = request.getHeader("timestamp");
        // String sign = request.getHeader("sign");
        // String body = request.getHeader("body");
        //
        // // 应该到数据库查是否accessKey是否已分配，并把对应的secretKey取出
        // //模拟
        // if (! accessKey.equals("gaomu")) {
        //     throw new RuntimeException("无权限");
        // }
        // // 应该查询随机数是否已经使用，可以使用Redis存储？内存压力可能会很大
        // //模拟校验随机数
        // if (Long.parseLong(nonce) > 10000){
        //     throw new RuntimeException("无权限");
        // }
        //
        // //时间戳和当前时间不能超过5分钟
        // long expirationTime = Long.parseLong(timestamp) + 300;
        // if (System.currentTimeMillis() / 1000 > expirationTime){
        //     throw new RuntimeException("无权限");
        // }
        //
        // // 实际情况中是从数据库中查出secretKey
        // String serverSign = SignUtils.getSign(body, "abcdef");
        // if (!sign.equals(serverSign)){
        //     throw new RuntimeException("无权限");
        // }
        return "让我猜一猜，你的名字肯定是" +user.getUsername();
    }
}
