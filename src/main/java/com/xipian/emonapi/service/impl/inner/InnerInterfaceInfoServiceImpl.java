package com.xipian.emonapi.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xipian.emonapi.common.ErrorCode;
import com.xipian.emonapi.exception.BusinessException;
import com.xipian.emonapi.mapper.InterfaceInfoMapper;
import com.xipian.emonapicommon.model.entity.InterfaceInfo;
import com.xipian.emonapicommon.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/nobitaxipian">XIPIAN</a>
 * @date 2023/10/12
 */
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    /**
     * 根据url和请求方法获取接口信息
     * @param path
     * @param method
     * @return
     */
    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        if (StringUtils.isAnyBlank(path,method)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url",path);
        queryWrapper.eq("method",method);

        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
