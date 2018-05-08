package com.zw.miaofuspd.facade.personal.service;

import  java.util.List;
/**
 * Created by Administrator on 2017/12/20.
 */
public interface AppAuthorizationInfoService {
    /**
     * 通过订单Id获取客户的授权信息
     * @param orderId 订单Id
     */
    public List AuthorizationInfo(String orderId) throws  Exception;

    /** 依据授权Id 更改授权状态
     * @parm taskId 授权id
     */
    public void perfectingAuthorizationInfo(String taskId)  throws  Exception;
}
