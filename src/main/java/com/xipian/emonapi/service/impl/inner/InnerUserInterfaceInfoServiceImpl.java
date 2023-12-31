package com.xipian.emonapi.service.impl.inner;

import com.xipian.emonapi.service.UserInterfaceInfoService;
import com.xipian.emonapicommon.model.entity.UserInterfaceInfo;
import com.xipian.emonapicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/nobitaxipian">XIPIAN</a>
 * @date 2023/10/12
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }

    @Override
    public boolean validLeftNum(long interfaceInfoId, long userId) {
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getUserInterfaceInfo(interfaceInfoId, userId);
        if (userInterfaceInfo == null || userInterfaceInfo.getLeftNum() <= 0){
            return false;
        }
        return true;
    }


}
