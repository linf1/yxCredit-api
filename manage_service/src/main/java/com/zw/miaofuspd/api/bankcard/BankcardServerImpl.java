package com.zw.miaofuspd.api.bankcard;

import com.alibaba.fastjson.JSONObject;
import com.api.model.BYXSettings;
import com.api.model.bankcard.BankcardRequest;
import com.api.model.bankcard.BankcardSettings;
import com.api.model.common.BYXRequest;
import com.api.service.bankcard.IBankcardServer;
import com.zhiwang.zwfinance.app.jiguang.util.api.CryptoTools;
import com.zw.api.HttpUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * 银行卡四要素
 * @author  陈清玉
 */
@Service(IBankcardServer.BEAN_KEY)
public class BankcardServerImpl implements IBankcardServer{

    private BankcardSettings bankcardSettings;

    private BYXSettings byxSettings;


    @Override
    public String authsms(BankcardRequest bankcardRequest) throws Exception {
        Map<String,String> param  = new HashMap<>(7);
        param.put("merchantNumber",bankcardRequest.getMerchantNumber());
        param.put("merchantOrder",bankcardRequest.getMerchantOrder());
        param.put("cardId",bankcardRequest.getCardId());
        param.put("realName",bankcardRequest.getMerchantNumber());
        param.put("bankCardNo",bankcardRequest.getBankCardNo());
        param.put("reservePhone",bankcardRequest.getReservePhone());
        param.put("userId",bankcardRequest.getUserId());
        final String byxRequestJson = BYXRequest.getBYXRequest(param, byxSettings);
        final Map<String, Object> headRequestMap = byxSettings.getHeadRequest();
        //发起请求
        return HttpUtil.sendHttpPostByJson(bankcardSettings.getRequestUrl(),byxRequestJson,headRequestMap);
    }

    @Override
    public String authconfirm(BankcardRequest bankcardRequest) throws Exception {
        Map<String,String> param  = new HashMap<>(3);
        param.put("merchantNumber",bankcardRequest.getMerchantNumber());
        param.put("merchantOrder",bankcardRequest.getMerchantOrder());
        param.put("smsCode",bankcardRequest.getCardId());
        final String byxRequestJson = BYXRequest.getBYXRequest(param, byxSettings);
        final Map<String, Object> headRequestMap = byxSettings.getHeadRequest();
        //发起请求
        return HttpUtil.sendHttpPostByJson(bankcardSettings.getRequestUrl(),byxRequestJson,headRequestMap);
    }

    @Override
    public String getBankList() throws IOException {
        return HttpUtil.doPost(bankcardSettings.getBankListUrl());
    }

    @Override
    public String getSubBankList(String regionId,String bankCode) throws IOException {
        Map<String,Object> paramMap = new HashMap<>(1);
        paramMap.put("regionId",regionId);
        paramMap.put("bankCode",bankCode);
        return HttpUtil.doPost(bankcardSettings.getBankListUrl(),paramMap);
    }

    @Override
    public String getCityList(String provinceId) throws IOException {
        Map<String,Object> paramMap = new HashMap<>(1);
        paramMap.put("provinceId",provinceId);
        return  HttpUtil.doPost(bankcardSettings.getBankListUrl(),paramMap);
    }

    @Override
    public String getProvinceList() throws IOException {
        return HttpUtil.doPost(bankcardSettings.getProvinceListUrl());
    }
}
