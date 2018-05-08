package com.zw.miaofuspd.facade.living.service;

/**
 * Created by Administrator on 2017/5/4 0004.
 */
public interface IIvingService {
    String getNonceTicket(String user_id) throws Exception;

    boolean validation(String user_id, String nonceStr, String app_sign) throws Exception;

    String getAccessToken()throws Exception;
}
