package com.zw.miaofuspd.facade.user.service;

import java.util.Map;

public interface IMsgService {
    /**
     * 往数据库中插入站内信
     * map中必包含（user_id，title，content，registration_id）;
     */
    void insertMsg(Map map) throws Exception;

    /**
     * 往数据库中插入订单消息
     * @param state
     * @throws Exception
     */
    void insertOrderMsg(String state, String order_id)throws Exception;
}
