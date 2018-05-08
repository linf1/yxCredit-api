package com.zw.miaofuspd.facade.order.service;

import com.alibaba.fastjson.JSONObject;
import com.zw.miaofuspd.facade.entity.AppUserInfo;

import java.util.Map;

public interface IAppInsapplicationService {
    Map makeOrder(AppUserInfo userInfo, String order_id) throws Exception;//提交进件，生成订单
    JSONObject CreditForRisk(Map inMap)throws Exception;//获取额度调用风控
    String tongdunRule(Map inMap)throws Exception;//获取额度调用风控
    String zenxintongRule(Map inMap) throws Exception;//获取增信通调用风控
    JSONObject tencentRule(Map inMap) throws Exception;//获取天域调用风控
    void zx(String response) throws Exception;//287规则回调地址
    void callback(String response,String idNo) throws Exception;//91征信回调地址
}
