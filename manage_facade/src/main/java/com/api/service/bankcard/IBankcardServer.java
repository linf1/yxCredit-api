package com.api.service.bankcard;

import com.api.model.bankcard.BankcardRequest;
import com.api.model.common.BYXResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    /**
     * 保存银行卡四要素信息
     * @param bankcardRequest 保存数据
     * @return 影响行数
     */
    Integer  saveBankcard(BankcardRequest bankcardRequest);

    /**
     * 查询银行卡四要素信息
     * @param bankcardRequest BankcardRequest 查询参数 ,
     *                           cardId - 身份证号码,
     *                           bankCardNo - 身份证号码,
     * @return 影响行数
     */
    List findBankcard(BankcardRequest bankcardRequest);
    /**
     * 更新银行卡四要素信息
     * @param bankcardRequest BankcardRequest 查询参数 ,
     *                           merchantOrder - 请求订单号,
     *                           merchantNeqNo - 请求流水号，每次请求唯一,
     * @return 影响行数
     */
    Integer updateState(BankcardRequest bankcardRequest);

    /**
     * 获取银行卡信息
     * @param custId 个人信息ID
     * @return 银行卡信息
     */
    List<Map> findSysBankcardInfoByCustId(String  custId);

    /**
     * 查询银行卡信息
     * @param bankcardRequest 查询实体
     *                        cardId - 身份证号
     * @return 返回结果集
     */
    List<Map> findSysBankCardInfo(BankcardRequest bankcardRequest);

}
