package com.zw.miaofuspd.api.bankcard;

import com.alibaba.fastjson.JSONObject;
import com.api.model.BYXSettings;
import com.api.model.bankcard.BankcardRequest;
import com.api.model.bankcard.BankcardSettings;
import com.api.model.common.BYXRequest;
import com.api.model.common.BYXResponse;
import com.api.service.bankcard.IBankcardServer;
import com.base.util.StringUtils;
import com.zw.api.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
/**
 * 银行卡四要素
 * @author  陈清玉
 */
@Service(IBankcardServer.BEAN_KEY)
public class BankcardServerImpl implements IBankcardServer{
    private final Logger LOGGER = LoggerFactory.getLogger(BankcardServerImpl.class);
    @Autowired
    private BankcardSettings bankcardSettings;

    @Autowired
    private BYXSettings byxSettings;


    @Override
    public BYXResponse authsms(BankcardRequest bankcardRequest) throws Exception {
        Map<String,String> param  = new HashMap<>(7);
        param.put("merchantNumber",bankcardSettings.getMerchantNumber());
        param.put("merchantOrder",bankcardRequest.getMerchantOrder());
        param.put("merchantNeqNo",bankcardRequest.getMerchantNeqNo());
        param.put("cardId",bankcardRequest.getCardId());
        param.put("realName",bankcardRequest.getRealName());
        param.put("bankCardNo",bankcardRequest.getBankCardNo());
        param.put("reservePhone",bankcardRequest.getReservePhone());
        if (StringUtils.isNotEmpty(bankcardRequest.getUserId())) {
            param.put("userId",bankcardRequest.getUserId());
        }
        final String result = HttpClientUtil.post(bankcardSettings.getRequestUrl(),BYXRequest.getBYXRequest(param, byxSettings), byxSettings.getHeadRequest());
        return BYXResponse.getBYXResponse(result, byxSettings);
    }

    @Override
    public BYXResponse authconfirm(BankcardRequest bankcardRequest) throws Exception {
        Map<String,String> param  = new HashMap<>(3);
        param.put("merchantNumber",bankcardSettings.getMerchantNumber());
        param.put("merchantOrder",bankcardRequest.getMerchantOrder());
        param.put("merchantNeqNo",bankcardRequest.getMerchantNeqNo());
        param.put("smsCode",bankcardRequest.getSmsCode());
        final String byxRequestJson = BYXRequest.getBYXRequest(param, byxSettings);
        //发起请求
        final String result = HttpClientUtil.post(bankcardSettings.getAuthConfirmUrl(),byxRequestJson,byxSettings.getHeadRequest());
        return BYXResponse.getBYXResponse(result, byxSettings);
    }

    @Override
    public BYXResponse getBankList() throws Exception {
        final String result = HttpClientUtil.post(bankcardSettings.getBankListUrl(),BYXRequest.getBYXRequest(new HashMap<>(), byxSettings),byxSettings.getHeadRequest());
        return BYXResponse.getBYXResponse(result,byxSettings);
    }

    @Override
    public BYXResponse getSubBankList(String regionId,String bankCode) throws Exception {
        Map<String,Object> paramMap = new HashMap<>(1);
        paramMap.put("regionId",regionId);
        paramMap.put("bankCode",bankCode);
        final String result = HttpClientUtil.post(bankcardSettings.getSubBankListUrl(), BYXRequest.getBYXRequest(paramMap, byxSettings),byxSettings.getHeadRequest());
        return BYXResponse.getBYXResponse(result,byxSettings);
    }

    @Override
    public BYXResponse getCityList(String provinceId) throws Exception {
        Map<String,Object> paramMap = new HashMap<>(1);
        paramMap.put("provinceId",provinceId);
        final String result = HttpClientUtil.post(bankcardSettings.getCityListUrl(),BYXRequest.getBYXRequest(paramMap, byxSettings),byxSettings.getHeadRequest());
        return BYXResponse.getBYXResponse(result,byxSettings);
    }

    @Override
    public BYXResponse getProvinceList() throws Exception {
        final String result = HttpClientUtil.post(bankcardSettings.getProvinceListUrl(),"",byxSettings.getHeadRequest());
        return BYXResponse.getBYXResponse(result,byxSettings);
    }
}
