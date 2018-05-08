package com.zw.miaofuspd.facade.user.service;

import java.util.Map;

public interface ISmsService {
    boolean insertSms(Map inMap) throws Exception;//向数据库中插入验证码
    Map checkSms(Map inMap) throws Exception;//根据验证码验证

}
