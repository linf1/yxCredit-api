package com.api.service.bankcard;

import com.api.model.bankcard.BankcardRequest;

import java.io.IOException;

/**
 * 银行卡四要素
 * @author  陈清玉
 */
public interface IBankcardServer {

    String BEAN_KEY = "bankcardServerImpl";

    /**
     * 银行卡四要素认证发送短信
     * @return 认证结果
     */
    String authsms(BankcardRequest bankcardRequest) throws Exception;
    /**
     * 银行卡四要素认证短信确认
     * @return 确认结果
     */
    String authconfirm(BankcardRequest bankcardRequest) throws Exception;
}
