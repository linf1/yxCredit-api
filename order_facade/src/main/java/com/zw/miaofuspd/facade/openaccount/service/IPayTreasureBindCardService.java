package com.zw.miaofuspd.facade.openaccount.service;

import com.zw.miaofuspd.facade.entity.AppUserInfo;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/7 0007.
 */
public interface IPayTreasureBindCardService {
    /**
     * 宝付预支付绑卡接口
     * @param name 姓名
     * @param idNo 身份证
     * @param cardNo 卡号
     * @param bankName 银行
     * @param bankCode 银行code
     * @param tel 开户电话
     * @param userInfo
     * @return
     * @throws Exception
     */
    Map payTreasureBindingCard(String name, String idNo, String cardNo,
                               String bankName, String bankCode, String tel, AppUserInfo userInfo) throws Exception;

    /**
     *宝付支付绑卡确认接口
     * @param name 姓名
     * @param idNo 身份证
     * @param cardNo 卡号
     * @param bankName 银行
     * @param bankCode 银行code
     * @param tel 开户电话
     * @param userInfo
     * @return
     * @throws Exception
     */
    Map payTreasureConfimCard(String name, String idNo, String cardNo, String bankName, String smsCode,
                              String bankCode, String transId, String tel, AppUserInfo userInfo) throws Exception;

    /**
     * 宝付主动还款接口
     * @param amount 钱数
     * @param cardNo 银行 卡号
     * @param userInfo 实体类
     * @param derateAmount 减免金额
     * @param derateId 减免iD
     * @return
     * @throws Exception
     */
    Map payTreasureCardPay(String amount, String derateAmount, String cardNo, AppUserInfo userInfo, String derateId) throws Exception;

    /**
     * 宝付支付前置服务包费用
     * @param userInfo
     * @param orderId
     * @return
     * @throws Exception
     */
     Map payServicePackageCardPay(AppUserInfo userInfo,String orderId) throws Exception;

    /**
     * 宝付支付前置服务包结果查询
     * @return
     * @throws Exception
     */
    Map payServicePackageResult(String orderId) throws Exception;

    /**
     * 宝付主动还款接口
     * @param userInfo
     * @param repayIds
     * @return
     * @throws Exception
     */
    Map confirmPayByIds(AppUserInfo userInfo, String repayIds,String redIds) throws Exception;

    /**
     * 提前结清
     * @param userInfo
     * @param orderId
     * @return
     * @throws Exception
     */
    Map earlyClearance(AppUserInfo userInfo,String orderId) throws Exception;
}
