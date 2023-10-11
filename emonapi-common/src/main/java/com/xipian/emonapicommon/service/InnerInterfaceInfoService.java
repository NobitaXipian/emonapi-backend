package com.xipian.emonapicommon.service;

import com.xipian.emonapicommon.model.entity.InterfaceInfo;

/**
 * @author <a href="https://github.com/nobitaxipian">XIPIAN</a>
 * @date 2023/10/11
 */
public interface InnerInterfaceInfoService {
    /**
     * 从数据库中查询模拟接口是否存在（请求路径、请求方法,返回接口信息，为空表示不存在）
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path, String method);

}
