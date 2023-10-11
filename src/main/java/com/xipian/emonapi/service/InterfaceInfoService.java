package com.xipian.emonapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xipian.emonapicommon.model.entity.InterfaceInfo;

/**
* @author NOBITA
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-10-04 19:40:47
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
