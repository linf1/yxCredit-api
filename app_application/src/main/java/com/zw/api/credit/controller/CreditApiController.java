package com.zw.api.credit.controller;

import com.alibaba.fastjson.JSONObject;
import com.api.model.credit.CreditRequest;
import com.api.model.credit.CreditResultAO;
import com.api.model.result.ApiResult;
import com.api.service.credit.ICreditApiService;
import com.api.service.result.IApiResultServer;
import com.base.util.AppRouterSettings;
import com.base.util.GeneratePrimaryKeyUtils;
import com.constants.ApiConstants;
import com.zhiwang.zwfinance.app.jiguang.util.api.EApiChildSourceEnum;
import com.zhiwang.zwfinance.app.jiguang.util.api.EApiSourceEnum;
import com.zw.miaofuspd.facade.user.service.IUserService;
import com.zw.service.redis.RedisService;
import com.zw.web.base.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.ognl.TokenMgrError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 征信API控制器
 *
 * @author luochaofang
 */
@RestController
@RequestMapping(value = AppRouterSettings.VERSION  + "/credit")
public class CreditApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreditApiController.class);

    @Autowired
    private ICreditApiService creditApiService;

    @Autowired
    private IApiResultServer apiResultServer;

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisService redisService;

    /**
     * 个人征信验证获取token
     * @return 客户端H5
     */
    @PostMapping("/getCreditToken")
    @ResponseBody
    public ResultVO getCreditToken(CreditRequest request) {
        try {
            if (null == request || StringUtils.isBlank(request.getCustomerId())) {
                LOGGER.info("请求参数异常或不存在");
                return ResultVO.error("请求参数异常或不存在");
            }
            CreditResultAO creditResultAO = creditApiService.getCreditToken(request);
            if(null != creditResultAO && StringUtils.isNotBlank(creditResultAO.getToken())) {
                return ResultVO.ok(ApiConstants.STATUS_SUCCESS_MSG,creditResultAO);
            }
        } catch (Exception e) {
            LOGGER.info("个人征信-获取token出错" + e);
            return ResultVO.error(ApiConstants.STATUS_DATASOURCE_INTERNAL_ERROR,ApiConstants.STATUS_DATASOURCE_INTERNAL_ERROR_MSG);
        }
        return  ResultVO.error(ApiConstants.STATUS_SIGN_ERROR, ApiConstants.STATUS_SIGN_ERROR_MSG);
    }

    /**
     * 获取个人征信信息回调入口
     * @param request 参数
     */
    @RequestMapping("/getCreditApiInfo")
    public void getCreditApiInfo(@RequestBody String request){
        if(StringUtils.isNotBlank(request)) {
            LOGGER.info(request);
            LOGGER.info("--------------------------------回调成功   ------------------------");
            JSONObject jsonObject = JSONObject.parseObject(request);
            if(jsonObject.containsKey(ApiConstants.API_TOKEN_KEY)) {
                String token = jsonObject.getString(ApiConstants.API_TOKEN_KEY);
                if(StringUtils.isNotBlank(token)) {
                    //验证token是否正确
                    String customerId = "";
                    try {
                        customerId = redisService.get(token);
                        LOGGER.info("redis客户存在：{}", customerId);
                    } catch (Exception e) {
                        LOGGER.info("个人征信-redis获取数据出错" + e);
                    }
                    if(StringUtils.isNotBlank(customerId)) {
                        Map<String,Object> map = new HashMap<>();
                        Map userMap = userService.getCustomerInfoByCustomerId(customerId);
                        if(null == userMap) {
                            LOGGER.info("该客户不存在：{}", customerId);
                        } else {
                            try {
                                //根据客户信息更新征信报告
                                ApiResult oldResult = new ApiResult();
                                oldResult.setSourceCode(EApiSourceEnum.CREDIT.getCode());
                                oldResult.setOnlyKey(customerId);//客户id
                                oldResult.setState(ApiConstants.STATUS_CODE_NO_STATE);//改为无效数据
                                apiResultServer.updateByOnlyKey(oldResult);

                                ApiResult result = new ApiResult();
                                result.setState(ApiConstants.STATUS_CODE_STATE);
                                result.setResultData("");
                                result.setRealName(userMap.get("customerName").toString());
                                result.setUserName(userMap.get("tel").toString());
                                result.setIdentityCode(userMap.get("card").toString());
                                result.setUserMobile(userMap.get("tel").toString());
                                if(ApiConstants.API_SUCCESS_KEY.equals(jsonObject.getString(ApiConstants.API_TASK_STATUS_KEY))){
                                    if(jsonObject.containsKey(ApiConstants.API_TASK_RESULT_KEY)){
                                        result.setResultData(jsonObject.getString(ApiConstants.API_TASK_RESULT_KEY));
                                    }
                                    map.put(ApiConstants.API_MESSAGE_KEY,ApiConstants.STATUS_SUCCESS_MSG);
                                    map.put(ApiConstants.API_CODE_KEY,ApiConstants.STATUS_SUCCESS);
                                } else {
                                    if(ApiConstants.API__PBC_1_CODE_KEY.equals(jsonObject.get(ApiConstants.API_CODE_KEY))) {
                                        map.put(ApiConstants.API_CODE_KEY,ApiConstants.STATUS_ACCOUNT_PASSWORD_ERROR);
                                    } else if(ApiConstants.API__PBC_2_CODE_KEY.equals(jsonObject.getString(ApiConstants.API_CODE_KEY))) {
                                        map.put(ApiConstants.API_CODE_KEY,ApiConstants.STATUS_VERIF_CODE_ERROR);
                                    } else {
                                        map.put(ApiConstants.API_CODE_KEY,ApiConstants.STATUS_CREDIT_INFO_ERROR);
                                    }
                                    map.put(ApiConstants.API_MESSAGE_KEY,jsonObject.getString(ApiConstants.API_MESSAGE_KEY));
                                }
                                result.setId(GeneratePrimaryKeyUtils.getUUIDKey());
                                result.setOnlyKey(customerId);
                                result.setCode(map.get(ApiConstants.API_CODE_KEY).toString());
                                result.setMessage(map.get(ApiConstants.API_MESSAGE_KEY).toString());
                                result.setSourceName(EApiSourceEnum.CREDIT.getName());
                                result.setSourceCode(EApiSourceEnum.CREDIT.getCode());
                                result.setSourceChildName(EApiSourceEnum.CREDIT.getName());
                                result.setSourceChildCode(EApiSourceEnum.CREDIT.getCode());
                                result.setApiReturnId("");
                                LOGGER.info("个人征信--获取报告信息成功{}", result);
                                apiResultServer.insertApiResult(result);
                                redisService.delete(token);
                            } catch (Exception e) {
                                LOGGER.info("个人征信-返回数据解析出错" + e);
                            }
                        }
                    }

                }
            }

        }

    }
}
