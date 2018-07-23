package com.zw.miaofuspd.activemq.service;

import com.zw.miaofuspd.mapper.OrderOperationRecordMapper;
import com.zw.pojo.OrderOperationRecord;
import com.zw.service.IOrderOperationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
* 操作记录表服务
* @author 陈淸玉 create on 2018-07-20
*/
@Service(IOrderOperationRecordService.BEAN_KEY)
public class OrderOperationRecordServiceImpl implements IOrderOperationRecordService {

    @Autowired
    private OrderOperationRecordMapper orderOperationRecordMapper;

    @Override
    public boolean insetOrderOperationRecord(OrderOperationRecord orderOperationRecord) {
        return orderOperationRecordMapper.insertOrderOperRecord(orderOperationRecord) > 0;
    }
}
