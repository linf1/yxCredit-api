package com.api.service.bankcard;

import com.api.model.bankcard.BankcardRequest;
import com.api.model.common.BYXResponse;

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
    BYXResponse authsms(BankcardRequest bankcardRequest) throws Exception;
    /**
     * 银行卡四要素认证短信确认
     * @return 确认结果
     */
    BYXResponse authconfirm(BankcardRequest bankcardRequest) throws Exception;

    /**
     * 获取银行列表
     * @return 营业银行json字符串
     * @exception  IOException 请求异常
     */
    BYXResponse getBankList() throws Exception;
    /**
     * 获取银行列表
     * @param regionId 市id
     * @param bankCode 银行行别代码
     * @return 支营业银行列表json字符串
     * @exception  IOException 请求异常
     */
    BYXResponse getSubBankList(String regionId,String bankCode) throws Exception;
    /**
     * 获取城市列表
     * @param provinceId 省ID
     * @return 城市列表json字符串
     * @exception  IOException 请求异常
     */
    BYXResponse getCityList(String provinceId) throws Exception;
    /**
     * 获取省列表
     * @return 省列表json字符串
     * @exception  IOException 请求异常
     */
    BYXResponse getProvinceList() throws Exception;
}
