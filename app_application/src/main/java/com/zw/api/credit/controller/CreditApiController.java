package com.zw.api.credit.controller;

import com.alibaba.fastjson.JSONObject;
import com.api.model.credit.CreditRequest;
import com.api.model.credit.CreditResultAO;
import com.api.service.credit.ICreditApiService;
import com.base.util.AppRouterSettings;
import com.constants.ApiConstants;
import com.zw.web.base.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
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

    /**
     * 个人征信验证API调用入口
     * @param request 请求参数
     * @return 最终数据
     */
    @PostMapping("/validateCreditApi")
    @ResponseBody
    public ResultVO validateCreditApi(@RequestBody CreditRequest request) throws IOException {
        if (request == null) {
            LOGGER.info("请求参数异常或不存在");
            return ResultVO.error("请求参数异常或不存在");
        }

        CreditResultAO creditResultAO = creditApiService.validateAccount(request);
        return ResultVO.error(creditResultAO.getCode(), creditResultAO.getTaskStatus());
    }

    /**
     * 获取个人征信信息回调入口
     * @param request 请求参数
     */
    @PostMapping("/getCreditApiInfo")
    public void getCreditApiInfo(@RequestBody String request) throws Exception {
        LOGGER.info(request.toString());
        LOGGER.info("--------------------------------回调成功   ------------------------");
        JSONObject jsonObject = JSONObject.parseObject(request);
        String code = "1";
        Map<String,Object> map = new HashMap<>();
        if(jsonObject.get("taskStatus").equals("success")){
            if(jsonObject.containsKey("taskResult")){
                map.put("taskResult",jsonObject.get("taskResult"));
            }
            map.put("message",ApiConstants.STATUS_SUCCESS_MSG);
            map.put("code",ApiConstants.STATUS_SUCCESS);
        } else {
            if(jsonObject.get("code").equals("pbc_1")) {
                map.put("code",ApiConstants.STATUS_ACCOUNT_PASSWORD_ERROR);
            } else if(jsonObject.get("code").equals("pbc_2")) {
                map.put("code",ApiConstants.STATUS_VERIF_CODE_ERROR);
            } else {
                map.put("code",ApiConstants.STATUS_CREDIT_INFO_ERROR);
            }
            map.put("message",jsonObject.get("message"));
        }
        map.put("taskNo",jsonObject.get("taskNo"));
        map.put("taskStatus",jsonObject.get("taskStatus"));
        creditApiService.saveCreditInfo(map);
    }
}
