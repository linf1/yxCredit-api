package com.zw.api.tongdun;

import com.api.model.common.ApiCommonResponse;
import com.api.model.common.ApiConstants;
import com.api.model.common.ResultModel;
import com.api.model.common.ResultModelUtil;
import com.api.model.tongdun.TongDunRequest;
import com.api.service.tongdun.ITongDunApiService;
import com.api.service.tongdun.TongDunProxy;
import com.base.util.AppRouterSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 同盾API控制器
 *
 * @author 陈清玉
 */
@RestController
@RequestMapping(value = AppRouterSettings.VERSION +AppRouterSettings.API_MODULE + "/tongDun")
public class TongDunApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TongDunApiController.class);

    @Autowired
    private ITongDunApiService tongDunApiService;

    /**
     * 贷前审核调用同盾API调用入口
     * @param request 请求参数
     * @return 最终数据
     */
    @GetMapping("/invokeTongDunApi")
    public ResultModel invokeTongDunApi(TongDunRequest request){
        if (request == null) {
            LOGGER.info("请求参数异常或不存在");
            return new  ResultModel(ResultModel.R.FAIL,"请求参数异常或不存在");
        }
        TongDunProxy tongDunProxy = new TongDunProxy(tongDunApiService);
        final ApiCommonResponse apiCommonResponse = tongDunProxy.invokeTongDunApi(request);
        return ResultModelUtil.formatResult(apiCommonResponse, ApiConstants.API_TONGDUN_TITLE,"贷前审核");
    }

}
