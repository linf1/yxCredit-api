package com.zw.miaofuspd.activemq.service;

import com.zw.miaofuspd.mapper.OrderMapper;
import com.zw.pojo.Order;
import com.zw.service.IOrderService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
    * 陈淸玉 create on 2018-07-19
    */
@Service("orderServiceImpl")
public class OrderServiceImpl extends AbsServiceBase implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public int updateOrderByNo(Order order) {
        return  orderMapper.updateOrderByNo(order);
    }

    @Override
    public Order getOrderByNo(String id) {
        return orderMapper.getOrderInfoByNo(id);
    }

}