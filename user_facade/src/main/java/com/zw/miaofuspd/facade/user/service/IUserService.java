package com.zw.miaofuspd.facade.user.service;

import com.zw.miaofuspd.facade.entity.AppUserInfo;

import java.util.Map;

/**
 * Created by zh-pc on 2017/2/16.
 */
public interface IUserService {

    int test(String sql) throws Exception;


    Map getUserByToken(String token) throws Exception;

    /**
     * 通过orderId 获取用户信息
     * @param orderId
     * @return
     */
    AppUserInfo getUserByOrderId(String orderId);

    /**
     * orderId 获取客户信息
     * @param orderId
     * @return
     */
    Map getCustomerInfoByCustomerId(String orderId);
}
