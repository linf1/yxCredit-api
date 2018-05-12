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

    /**
     * 获取银行列表
     * @return 营业银行json字符串
     * @exception  IOException 请求异常
     */
    String getBankList() throws IOException;
    /**
     * 获取银行列表
     * @param regionId 市id
     * @param bankCode 银行行别代码
     * @return 支营业银行列表json字符串
     * @exception  IOException 请求异常
     */
    String getSubBankList(String regionId,String bankCode) throws IOException;
    /**
     * 获取城市列表
     * @param provinceId 省ID
     * @return 城市列表json字符串
     * @exception  IOException 请求异常
     */
    String getCityList(String provinceId) throws IOException;
    /**
     * 获取省列表
     * @return 省列表json字符串
     * @exception  IOException 请求异常
     */
    String getProvinceList() throws IOException;
}
