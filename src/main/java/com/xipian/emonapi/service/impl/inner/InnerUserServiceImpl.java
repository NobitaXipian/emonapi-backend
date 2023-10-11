package com.xipian.emonapi.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xipian.emonapi.common.ErrorCode;
import com.xipian.emonapi.exception.BusinessException;
import com.xipian.emonapi.mapper.UserMapper;
import com.xipian.emonapicommon.model.entity.User;
import com.xipian.emonapicommon.service.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/nobitaxipian">XIPIAN</a>
 * @date 2023/10/12
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 根据 accessKey 查询用户信息
     * @param accessKey
     * @return
     */
    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //创建查询条件包装器
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("accessKey",accessKey);

        return userMapper.selectOne(userQueryWrapper);
    }
}
