package com.xipian.emonapicommon.service;

import com.xipian.emonapicommon.model.entity.User;

/**
 * @author <a href="https://github.com/nobitaxipian">XIPIAN</a>
 * @date 2023/10/11
 */
public interface InnerUserService {

    /**
     * 数据库中查是否已分配给用户秘钥（根据 accessKey）
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);

}
