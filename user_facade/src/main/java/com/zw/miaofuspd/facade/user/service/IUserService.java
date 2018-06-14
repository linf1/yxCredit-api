package com.zw.miaofuspd.facade.user.service;

import java.util.Map;

/**
 * Created by zh-pc on 2017/2/16.
 */
public interface IUserService {

    /**
     * customerId 获取客户信息
     * @param customerId
     * @return
     */
    Map getCustomerInfoByCustomerId(String customerId);
}
