package com.zw.miaofuspd.activemq.queue;

import org.apache.activemq.command.ActiveMQQueue;

/**
 * 推送放款及还款计划信息队列
 * @author 陈淸玉 create on 2018-07-20
 */
public class RepaymentQueue extends ActiveMQQueue {
    public RepaymentQueue(String s) {
        super(s);
    }
}
