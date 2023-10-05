package com.xipian.emonapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xipian.emonapi.common.ErrorCode;
import com.xipian.emonapi.exception.BusinessException;
import com.xipian.emonapi.mapper.InterfaceInfoMapper;
import com.xipian.emonapi.model.entity.InterfaceInfo;
import com.xipian.emonapi.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author NOBITA
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2023-10-04 19:40:47
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
         String name = interfaceInfo.getName();
         String url = interfaceInfo.getUrl();

        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name,url) ) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
        if (StringUtils.isNotBlank(name) && name.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "url过长");
        }

    }

}




