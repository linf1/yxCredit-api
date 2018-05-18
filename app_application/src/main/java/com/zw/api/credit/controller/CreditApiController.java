package com.zw.api.credit.controller;

import com.api.model.common.ResultModel;
import com.api.model.credit.CreditRequest;
import com.api.model.credit.CreditResultAO;
import com.api.service.credit.ICreditApiService;
import com.base.util.AppRouterSettings;
import com.zw.web.base.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 征信API控制器
 *
 * @author luochaofang
 */
@RestController
@RequestMapping(value = AppRouterSettings.VERSION +AppRouterSettings.API_MODULE + "/credit")
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
        ResultVO resultVO = new  ResultVO();
        if (request == null) {
            LOGGER.info("请求参数异常或不存在");
            return resultVO.error("请求参数异常或不存在");
        }
        CreditResultAO creditResultAO = creditApiService.validateAccount(request);
        return resultVO.error(creditResultAO.getCode(), creditResultAO.getMessage());
    }

    /**
     * 获取个人征信信息回调入口
     * @param request 请求参数
     */
    @PostMapping("/getCreditApiInfo")
    public void getCreditApiInfo(String request) throws IOException {
        LOGGER.info(request.toString());

        //CreditResultAO creditResultAO = creditApiService.validateAccount(request);
    }
}
