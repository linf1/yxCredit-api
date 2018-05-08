package com.zw.miaofuspd.facade.personal.service;

import java.util.Map;

/**
 * Created by Administrator on 2017/2/28.
 */

public interface AppBasicInfoService {
    Map getMiaofuBasicInfo(String customerId) throws Exception;

    /**
     * 获取联系人信息
     * @param orderId 订单id
     * @param userId 用户id
     * @param map1 联系人信息集合
     * @return
     * @throws Exception
     */
    Map updateLinkManInfo(String orderId, String userId, Map map1) throws Exception;

    /**
     * 添加用户基本信息
     * @param paramMap
     * @return
     * @throws Exception
     */
    Map addBasicInfo(Map<String, String> paramMap) throws Exception;

    /**
     * 获取用户基本信息
     * @param orderId 订单id
     * @return
     * @throws Exception
     */
    Map getBasicInfo(String orderId) throws Exception;
    /**
     * 获取用户基本信息
     * @param customerId 客户id
     * @return
     * @throws Exception
     */
    Map getBasicCustomerInfo(String customerId) throws Exception;
    Map getLinkMan(String customerId) throws Exception;
    void saveTongXunLu(String customerId,String data) throws Exception;

}
