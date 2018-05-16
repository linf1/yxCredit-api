package com.zw.miaofuspd.facade.user.service;

import com.api.model.sortmsg.MsgRequest;

import java.util.Map;

public interface ISmsService {
    boolean insertSms(Map inMap) throws Exception;//向数据库中插入验证码
    Map checkSms(Map inMap) throws Exception;//根据验证码验证

    /**
     * 保存验证码信息到数据库 便于验证
     * @param request
     * @return
     */
    boolean saveSms(MsgRequest request);



}
