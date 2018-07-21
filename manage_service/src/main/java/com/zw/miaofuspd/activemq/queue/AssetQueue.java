package com.zw.miaofuspd.activemq.queue;

import org.apache.activemq.command.ActiveMQQueue;

/**
 * 接受有信贷资产信息队列
 * @author 韩梅生 create on 2018-07-20
 */
public class AssetQueue extends ActiveMQQueue {
    public AssetQueue(String s) {
        super(s);
    }
}
