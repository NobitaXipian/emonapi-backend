package com.xipian.emonapicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xipian.emonapicommon.model.entity.UserInterfaceInfo;

/**
* @author NOBITA
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-10-07 01:53:24
*/
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}
