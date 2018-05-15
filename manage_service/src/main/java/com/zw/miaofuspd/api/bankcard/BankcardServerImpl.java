package com.zw.miaofuspd.api.bankcard;

import com.alibaba.fastjson.JSONObject;
import com.api.model.BYXSettings;
import com.api.model.bankcard.BankcardRequest;
import com.api.model.bankcard.BankcardSettings;
import com.api.model.common.BYXRequest;
import com.api.service.bankcard.IBankcardServer;
import com.base.util.GeneratePrimaryKeyUtils;
import com.base.util.StringUtils;
import com.zw.api.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private BankcardSettings bankcardSettings;

    @Autowired
    private BYXSettings byxSettings;


    @Override
    public String authsms(BankcardRequest bankcardRequest) throws Exception {
        Map<String,String> param  = new HashMap<>(7);
        param.put("merchantNumber",bankcardSettings.getMerchantNumber());
        param.put("cardId",bankcardRequest.getCardId());
        param.put("realName",bankcardRequest.getMerchantNumber());
        param.put("bankCardNo",bankcardRequest.getBankCardNo());
        param.put("reservePhone",bankcardRequest.getReservePhone());
        if (StringUtils.isNotEmpty(bankcardRequest.getUserId())) {
            param.put("userId",bankcardRequest.getUserId());
        }
        final String byxRequestJson = BYXRequest.getBYXRequest(param, byxSettings);
        return HttpClientUtil.post(bankcardSettings.getRequestUrl(),byxRequestJson,byxSettings.getHeadRequest());
    }

    @Override
    public String authconfirm(BankcardRequest bankcardRequest) throws Exception {
        Map<String,String> param  = new HashMap<>(3);
        param.put("merchantNumber",bankcardSettings.getMerchantNumber());
        param.put("merchantOrder",bankcardRequest.getMerchantOrder());
        param.put("merchantNeqNo",bankcardRequest.getBankCardNo());
        param.put("smsCode",bankcardRequest.getSmsCode());
        final String byxRequestJson = BYXRequest.getBYXRequest(param, byxSettings);
        //发起请求
        return HttpClientUtil.post(bankcardSettings.getRequestUrl(),byxRequestJson,byxSettings.getHeadRequest());
    }

    @Override
    public String getBankList() throws IOException {
        return HttpClientUtil.post(bankcardSettings.getBankListUrl(),"",byxSettings.getHeadRequest());
    }

    @Override
    public String getSubBankList(String regionId,String bankCode) throws IOException {
        Map<String,Object> paramMap = new HashMap<>(1);
        paramMap.put("regionId",regionId);
        paramMap.put("bankCode",bankCode);
        return HttpClientUtil.post(bankcardSettings.getBankListUrl(), JSONObject.toJSONString(paramMap),byxSettings.getHeadRequest());
    }

    @Override
    public String getCityList(String provinceId) throws IOException {
        Map<String,Object> paramMap = new HashMap<>(1);
        paramMap.put("provinceId",provinceId);
        return  HttpClientUtil.post(bankcardSettings.getBankListUrl(),JSONObject.toJSONString(paramMap),byxSettings.getHeadRequest());
    }

    @Override
    public String getProvinceList() throws IOException {
        return HttpClientUtil.post(bankcardSettings.getProvinceListUrl(),"",byxSettings.getHeadRequest());
    }
}
