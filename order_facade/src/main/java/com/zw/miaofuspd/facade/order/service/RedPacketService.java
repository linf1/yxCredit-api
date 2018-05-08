package com.zw.miaofuspd.facade.order.service;

import java.util.Map;

public interface RedPacketService {

    /**
     * 推荐人获取红包
     * @param userId
     * @return
     */
     Map getRedPacket(String userId, String orderId);
}
