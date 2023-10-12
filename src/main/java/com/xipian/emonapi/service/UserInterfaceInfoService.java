package com.xipian.emonapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xipian.emonapicommon.model.entity.UserInterfaceInfo;

/**
* @author NOBITA
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-10-07 01:53:24
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo,boolean add);

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 获取接口与用户的信息
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    public UserInterfaceInfo getUserInterfaceInfo(long interfaceInfoId, long userId);
}
