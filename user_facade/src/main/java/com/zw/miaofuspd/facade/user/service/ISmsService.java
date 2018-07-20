package com.zw.miaofuspd.facade.user.service;

import com.api.model.sortmsg.MsgRequest;

import java.util.Map;

public interface ISmsService {
    Map checkSms(Map inMap) throws Exception;//根据验证码验证
    /**
     * 保存验证码信息到数据库 便于验证
     * @param request
     * @return
     */
    boolean saveSms(MsgRequest request);
    /**
     * 更新验证码信息到数据库 便于验证
     * @param request
     * @return
     */
    boolean updateSms(MsgRequest request);


    /**
     * 保存验证码信息到Redis 便于验证
     * @param request
     * @return
     */
    boolean saveSmsForRedis(MsgRequest request);

    /**
     * 根据验证码验证
     * @param inMap
     * @return
     */
    Map checkSmsForRedis(Map inMap) ;


}
