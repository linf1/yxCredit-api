package com.zw.api.tongdun.controller;

import com.api.model.common.ApiCommonResponse;
import com.api.service.result.IApiResultServer;
import com.constants.ApiConstants;
import com.api.model.common.ResultModel;
import com.api.model.common.ResultModelUtil;
import com.api.model.tongdun.TongDunRequest;
import com.api.service.tongdun.ITongDunApiService;
import com.api.service.tongdun.TongDunProxy;
import com.base.util.AppRouterSettings;
import com.zw.web.base.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 同盾API控制器
 *
 * @author 陈清玉
 */
@RestController
@RequestMapping(value = AppRouterSettings.VERSION + "/tongDun")
public class TongDunApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TongDunApiController.class);

    @Autowired
    private ITongDunApiService tongDunApiService;

    @Autowired
    private IApiResultServer apiResultServer;


    /**
     * 贷前审核调用同盾API调用入口
     * @param request 请求参数 idNo - 身份证号码
     *                       name - 姓名
     *                       phone - 电话
     *                       orderId - 订单号
     *                                 
     * @return 最终数据
     */
    @PostMapping("/invokeTongDunApi")
    public ResultVO invokeTongDunApi(TongDunRequest request){
        if (request == null) {
            LOGGER.info("请求参数异常或不存在");
            return ResultVO.error("请求参数异常或不存在");
        }
        try {
        TongDunProxy tongDunProxy = new TongDunProxy(tongDunApiService,apiResultServer);
         final ApiCommonResponse apiCommonResponse = tongDunProxy.invokeTongDunApi(request);
             if(apiCommonResponse != null){
                 return  ResultVO.ok(apiCommonResponse.getResponseMsg());
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultVO.error();
    }

}
