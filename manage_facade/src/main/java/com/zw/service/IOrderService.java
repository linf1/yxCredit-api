package com.zw.service;


import com.zw.pojo.Order;

/**
 * 陈淸玉 create on 2018-07-19
 */
public interface IOrderService {

    /**
     * 按id更新订单信息
     * @author 陈淸玉 create on 2018-07-19
     * @param order 订单实体
     * @return 影响行数
     */
    int updateOrderByNo(Order order);

    /**
     * 按id获取订单信息
     * @author 陈淸玉 create on 2018-07-19
     * @param id 订单编号
     * @return 一条订单数据
     */
    Order getOrderByNo(String id);


}
