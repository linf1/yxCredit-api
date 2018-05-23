package com.zw.api.shujumohe.controller;

import com.alibaba.fastjson.JSONObject;
import com.api.model.result.ApiResult;
import com.api.model.shujumohe.ShujumoheRequest;
import com.api.service.result.IApiResultServer;
import com.api.service.shujumohe.IShujumoheServer;
import com.base.util.AppRouterSettings;
import com.base.util.GeneratePrimaryKeyUtils;
import com.base.util.StringUtils;
import com.constants.ApiConstants;
import com.zhiwang.zwfinance.app.jiguang.util.api.EApiSourceEnum;
import com.zw.web.base.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 魔盒服务控制
 * @author 陈清玉
 */
@RestController
@RequestMapping(AppRouterSettings.VERSION +"/shujumohe" )
public class ShujumoheController {

    private final Logger LOGGER = LoggerFactory.getLogger(ShujumoheController.class);

    @Autowired
    private IShujumoheServer shujumoheServerImpl;

    @Autowired
    private IApiResultServer apiResultServerImpl;

    /**
     * 数据魔盒回调接口
     * @param request 数据
     * @return
     */
    @RequestMapping("/callBackShujumohe")
    public ResultVO callBackShujumohe(ShujumoheRequest request){
        LOGGER.info("=======数据魔盒回调接参数request:{}",request.toString());
        String result = null;
        try {
            ApiResult resultParameter = new ApiResult();
            resultParameter.setUserName(request.getPhone());
            resultParameter.setSourceCode(EApiSourceEnum.MOHE.getCode());
            //查询是否有报告
            final List<Map> mapList = apiResultServerImpl.selectApiResult(resultParameter);
            if(CollectionUtils.isEmpty(mapList)){
                //如果数据库没有证明第一次生成报告则调用远程API获取报告
                result = shujumoheServerImpl.callbackShujumohe(request);
            }
            if (StringUtils.isNotEmpty(result)) {
                final JSONObject jsonObject = JSONObject.parseObject(result);
                final String code = jsonObject.getString("code");
                if(ApiConstants.STATUS_SUCCESS.equals(code)){
                    final Map data = (Map) jsonObject.get("data");
                    if(data != null) {
                            ApiResult apiResult = new ApiResult();
                            apiResult.setId(GeneratePrimaryKeyUtils.getUUIDKey());
                            apiResult.setCode(code);
                            apiResult.setIdentityCode((String)data.get("identity_code"));
                            apiResult.setMessage(ApiConstants.STATUS_SUCCESS_MSG);
                            apiResult.setSourceChildName(ApiConstants.API_MOHE_YYS);
                            apiResult.setSourceChildCode((String)data.get("channel_type"));
                            apiResult.setOnlyKey(request.getOrderId());
                            apiResult.setRealName((String)data.get("real_name"));
                            apiResult.setSourceName(EApiSourceEnum.MOHE.getName());
                            apiResult.setSourceCode(EApiSourceEnum.MOHE.getCode());
                            apiResult.setUserMobile((String)data.get("user_mobile"));
                            apiResult.setUserName(request.getPhone());
                            apiResult.setResultData(data.get("task_data").toString());
                            apiResult.setState(1);
                            apiResultServerImpl.insertApiResult(apiResult);
                    }
                    return  ResultVO.ok("验证成功！",null);
                }
                return  ResultVO.error(jsonObject.get("message").toString());
            }else{
                return  ResultVO.error("重复验证！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResultVO.error();
        }
        return ResultVO.error("验证失败！");
    }
}
