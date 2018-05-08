package com.zw.miaofuspd.facade.home.service;

import com.zw.miaofuspd.facade.entity.AppUserInfo;

import java.util.Map;

public interface HomeService {

    /**
     *根据userId获取我的页面信息
     * @param userId
     * @return
     */
    Map getMyPageInfo(String userId);
    /**
     *
     */
    Map checkOrder(AppUserInfo appUserInfo);

}
