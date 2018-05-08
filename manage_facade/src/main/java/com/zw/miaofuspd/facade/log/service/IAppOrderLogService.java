package com.zw.miaofuspd.facade.log.service;


import com.zw.miaofuspd.facade.log.entity.MessageBean;
import com.zw.miaofuspd.facade.log.entity.OrderLogBean;

/**
 * 订单日志接口
 */
public interface IAppOrderLogService {
    OrderLogBean getOrderById(String order_id) throws Exception;//获取订单ID

    OrderLogBean getOrderByCrmId(String crm_apply_id) throws Exception;//获取申请Id

    String setAppMessage(MessageBean message, String order_id) throws Exception;//插入站内信crm_apply_id

    String setOrderLog(OrderLogBean orderLog) throws Exception;//向数据库插入订单日志信息
}
