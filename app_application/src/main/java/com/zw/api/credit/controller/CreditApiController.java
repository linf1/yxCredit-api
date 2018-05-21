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
import com.zhiwang.zwfinance.app.jiguang.util.api.EApiSourceEnum;
import com.zw.web.base.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.jsp.tagext.TryCatchFinally;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    /**
     * 个人征信验证API调用入口
     * @param request 请求参数
     * @return 最终数据
     */
    @PostMapping("/validateCreditApi")
    @ResponseBody
    public ResultVO validateCreditApi(@RequestBody CreditRequest request, @RequestHeader String token) throws IOException {
        if (null == request || StringUtils.isBlank(request.getOrderId())) {
            LOGGER.info("请求参数异常或不存在");
            return ResultVO.error("请求参数异常或不存在");
        }
        //根据订单id获取当前用户是否存在征信报告
        try {
            ApiResult result = new ApiResult();
            result.setSourceCode(EApiSourceEnum.CREDIT.getCode());
            result.setOnlyKey(request.getOrderId());
            result.setState(ApiConstants.STATUS_CODE_STATE);//获取有效数据
            List<Map> apiResultMap = apiResultServer.selectApiResult(result);
            if(CollectionUtils.isEmpty(apiResultMap)) {
                LOGGER.info("个人征信--获取报告信息 API调用开始.");
                request.setToken(token);
                CreditResultAO creditResultAO = creditApiService.validateAccount(request);
                if("processing".equals(creditResultAO.getTaskStatus())) {
                    try {
                        Thread.sleep(8000);
                        LOGGER.info("个人征信--获取报告信息 API调用参数：{}", request.toString());
                        //从数据库中获取数据
                        apiResultMap = apiResultServer.selectApiResult(result);
                        if(!CollectionUtils.isEmpty(apiResultMap)) {
                            LOGGER.info("个人征信-获取报告信息结束.");
                            return ResultVO.error(apiResultMap.get(0).get(ApiConstants.API_CODE_KEY).toString(), apiResultMap.get(0).get(ApiConstants.API_MESSAGE_KEY).toString());
                        } else {
                            return ResultVO.error(ApiConstants.STATUS_CREDIT_INFO_ERROR, ApiConstants.STATUS_CREDIT_INFO_MSG);
                        }
                    } catch (Exception e) {
                        LOGGER.info("个人征信-获取数据出错" + e);
                        return ResultVO.error(ApiConstants.STATUS_DATASOURCE_INTERNAL_ERROR,ApiConstants.STATUS_DATASOURCE_INTERNAL_ERROR_MSG);
                    }
                } else {
                    return ResultVO.error(ApiConstants.STATUS_SYS_ERROR,"需要补充任务信息");
                }
            } else {
                LOGGER.info("个人征信-存在.");
                return ResultVO.error(apiResultMap.get(0).get(ApiConstants.API_CODE_KEY).toString(), apiResultMap.get(0).get(ApiConstants.API_MESSAGE_KEY).toString());
            }
        } catch (Exception e) {
            LOGGER.info("个人征信-获取数据出错" + e);
            return ResultVO.error(ApiConstants.STATUS_DATASOURCE_INTERNAL_ERROR,ApiConstants.STATUS_DATASOURCE_INTERNAL_ERROR_MSG);
        }
    }

    /**
     * 获取个人征信信息回调入口
     * @param request 请求参数
     */
    @RequestMapping("/getCreditApiInfo/{orderId}")
    public void getCreditApiInfo(@RequestBody String request, @PathVariable String  orderId) throws Exception {
        LOGGER.info(request.toString());
        LOGGER.info("--------------------------------回调成功   ------------------------");
        JSONObject jsonObject = JSONObject.parseObject(request);
        Map<String,Object> map = new HashMap<>();
        ApiResult result = new ApiResult();
        result.setState(ApiConstants.STATUS_CODE_STATE);
        result.setResultData("");
        result.setIdentityCode("");
        result.setRealName("");
        result.setUserMobile("");
        result.setUserName("");
        try {
            if(jsonObject.getString(ApiConstants.API_TASK_STATUS_KEY).equals(ApiConstants.API_SUCCESS_KEY)){
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
                result.setState(ApiConstants.STATUS_CODE_NO_STATE);
                map.put(ApiConstants.API_MESSAGE_KEY,jsonObject.getString(ApiConstants.API_MESSAGE_KEY));
            }
            result.setId(GeneratePrimaryKeyUtils.getUUIDKey());
            result.setOnlyKey(orderId);
            result.setCode(map.get(ApiConstants.API_CODE_KEY).toString());
            result.setMessage(map.get(ApiConstants.API_MESSAGE_KEY).toString());
            result.setSourceName(EApiSourceEnum.CREDIT.getName());
            result.setSourceCode(EApiSourceEnum.CREDIT.getCode());
            result.setSourceChildName(EApiSourceEnum.CREDIT.getName());
            result.setSourceChildCode(EApiSourceEnum.CREDIT.getCode());
            LOGGER.info("个人征信--获取报告信息{}", result);
            apiResultServer.insertApiResult(result);
        } catch (Exception e) {
            LOGGER.info("个人征信-返回数据解析出错" + e);
        }
    }
}
