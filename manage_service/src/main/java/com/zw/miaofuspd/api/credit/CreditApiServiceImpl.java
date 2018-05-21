package com.zw.miaofuspd.api.credit;

import com.alibaba.fastjson.JSONObject;
import com.api.model.credit.CreditRequest;
import com.api.model.credit.CreditResultAO;
import com.api.model.credit.CreditResultRequest;
import com.api.model.credit.CreditSettings;
import com.api.model.result.ApiResult;
import com.api.service.credit.ICreditApiService;
import com.base.util.DateUtils;
import com.base.util.TokenHelper;
import com.zw.api.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.RequestingUserName;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 征信服务
 * @author luochaofang
 */
@Service(ICreditApiService.BEAN_ID)
public class CreditApiServiceImpl implements ICreditApiService {

    @Autowired
    private CreditSettings creditSettings;

    @Override
    public CreditResultAO validateAccount(CreditRequest request) throws IOException {
        Map<String,Object> params = new LinkedHashMap<String,Object>(2);
        Map<String,Object> dataMap = new LinkedHashMap<String,Object>(3);
        dataMap.put("account",request.getAccount());
        dataMap.put("password",request.getPassword());
        dataMap.put("smsCode",request.getSmsCode());
        params.put("data", dataMap);
        params.put("callbackUrl", creditSettings.getCallbackUrl()+ request.getOrderId() + request.getToken());
        TokenHelper tokenHelper = new TokenHelper(creditSettings.getAk(), creditSettings.getSk());
        int expireTime = new Long(new Date().getTime()/1000).intValue() + 3600;
        String bodyJson = JSONObject.toJSONString(params);
        String queryParam = "appKey="+creditSettings.getAppKey()+"&taskType="+creditSettings.getTaskType();
        String urlPath = creditSettings.getCreditUrlPath();
        String token = tokenHelper.generateToken(urlPath, creditSettings.getRequestType(),queryParam , bodyJson, expireTime);
        Map<String, Object> headMap = new HashMap<>(1);
        headMap.put("X-IbeeAuth-Token",token);
        String result = HttpUtil.doPostHeadSSL(creditSettings.getUrl(), params, headMap);
        return  JSONObject.parseObject(result,CreditResultAO.class);
    }

}
