package com.zw.miaofuspd.facade.red.service;

import com.zw.miaofuspd.facade.entity.AppUserInfo;

import java.util.Map;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
public interface RedService {
    /**
     * 获取红包列表,红包总金额,可用金额
     * @param appUserInfo
     * @return
     * @throws Exception
     */
    Map getRedList(AppUserInfo appUserInfo) throws Exception;
    Map getUnUserList(AppUserInfo appUserInfo) throws Exception;
}
