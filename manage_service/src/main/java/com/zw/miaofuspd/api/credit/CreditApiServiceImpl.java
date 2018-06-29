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
import com.zw.service.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.RequestingUserName;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 征信服务
 * @author luochaofang
 */
@Service(ICreditApiService.BEAN_ID)
public class CreditApiServiceImpl implements ICreditApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreditApiServiceImpl.class);

    @Autowired
    private CreditSettings creditSettings;

    @Autowired
    private RedisService redisService;

    @Override
    public CreditResultAO getCreditToken(CreditRequest request){
        Map<String,Object> params = new LinkedHashMap<>(1);
        params.put("returnUrl", request.getCallbackUrl());
        String bodyJson = JSONObject.toJSONString(params);
        TokenHelper tokenHelper = new TokenHelper(creditSettings.getAk(), creditSettings.getSk());
        String queryParam = "appKey="+creditSettings.getAppKey()+"&taskType="+creditSettings.getTaskType();
        int expireTime = new Long(new Date().getTime()/1000).intValue() + 3600;
        String token = tokenHelper.generateToken(creditSettings.getCreditTokenUrl(), creditSettings.getRequestType(),queryParam , bodyJson, expireTime);
        Map<String, Object> headMap = new HashMap<>(1);
        headMap.put("X-IbeeAuth-Token",token);
        String requestUrl = creditSettings.getCreditHost()+ creditSettings.getCreditTokenUrl()+"?"+ queryParam;
        String result = HttpUtil.doPostHeadSSL(requestUrl, params, headMap);
        if(StringUtils.isNotBlank(result)) {
            CreditResultAO creditResultAO = JSONObject.parseObject(result,CreditResultAO.class);
            redisService.set(creditResultAO.getToken(), request.getCustomerId(), 1, TimeUnit.DAYS);
            LOGGER.info("个人征信--------- redis数据获取成功---------");
            creditResultAO.setCreditH5Url(creditSettings.getCreditHost()+"?token="+creditResultAO.getToken()+"&pbc_token="+creditResultAO.getPbc_token());
            return creditResultAO;
        }
        return null;
    }

}
