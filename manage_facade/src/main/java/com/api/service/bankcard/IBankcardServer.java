package com.api.service.bankcard;

import com.api.model.bankcard.BankcardRequest;

/**
 * 银行卡四要素
 * @author  陈清玉
 */
public interface IBankcardServer {

    /**
     * 银行卡四要素认证发送短信
     * @return 认证结果
     */
    String authsms(BankcardRequest bankcardRequest);
    /**
     * 银行卡四要素认证短信确认
     * @return 确认结果
     */
    String authconfirm(BankcardRequest bankcardRequest);
}
