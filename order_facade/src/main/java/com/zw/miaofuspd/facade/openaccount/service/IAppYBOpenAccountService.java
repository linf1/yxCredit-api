package com.zw.miaofuspd.facade.openaccount.service;


import com.zw.miaofuspd.facade.entity.AppUserInfo;

import java.util.Map;

public interface IAppYBOpenAccountService {
    /**
     * 易宝鉴权绑卡请求接口
     * @param authbindcardreqUri
     * @param cardno
     * @param merchantno
     * @param userInfo
     * @return
     * @throws Exception
     */
    Map bindingCard(String authbindcardreqUri, String tel, String host, String cardno, String merchantno, AppUserInfo userInfo) throws Exception;

    /**
     * 易宝鉴权绑卡确认接口
     * @return
     * @throws Exception
     */
    Map bindingCardConfirm(String authbindcardconfirmUri, String smsCode, String merchantno, AppUserInfo userInfo, String cardNo, String tel, String accountBankName) throws Exception;

    /**
     * 易宝鉴权绑卡请求重发接口
     * @param authbindcardresendUri
     * @param merchantno
     * @param userInfo
     * @return
     * @throws Exception
     */
    Map bindingCardResend(String authbindcardresendUri, String merchantno, AppUserInfo userInfo)throws Exception;

    /**
     * 易宝鉴权绑卡查询绑卡记录接口，在出现FAIL状态的情况下，调此接口
     * @param authListqueryUri
     * @param merchantno
     * @param userInfo
     * @return
     * @throws Exception
     */
    Map bindingCardCheck(String cardNo, String authListqueryUri, String merchantno, AppUserInfo userInfo)throws Exception;

    Map getOpenAccountInfo(AppUserInfo userInfo) throws Exception;
    Map openAccountCallBack(String response) throws Exception;
}
