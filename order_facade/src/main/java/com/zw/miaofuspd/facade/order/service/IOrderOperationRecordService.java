package com.zw.miaofuspd.facade.order.service;

import com.zw.miaofuspd.facade.pojo.OrderOperationRecord;

/**
 * 操作记录表服务
 * @author 陈淸玉 create on 2018-07-20
 */
public interface IOrderOperationRecordService {
    String BEAN_KEY = "orderOperationRecordServiceImpl";

    /**
     * 保存操作记录表数据
     * @param orderOperationRecord 操作记录实体
     * @return 是否成功
     */
    boolean insetOrderOperationRecord(OrderOperationRecord orderOperationRecord);
}
