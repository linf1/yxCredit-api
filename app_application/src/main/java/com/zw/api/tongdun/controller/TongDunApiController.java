package com.zw.api.tongdun.controller;

import com.api.model.common.ApiCommonResponse;
import com.api.model.tongdun.TongDunRequest;
import com.api.service.result.IApiResultServer;
import com.api.service.tongdun.ITongDunApiService;
import com.api.service.tongdun.TongDunProxy;
import com.base.util.AppRouterSettings;
import com.zhiwang.zwfinance.app.jiguang.util.api.EApiSourceEnum;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.web.base.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private AppBasicInfoService appBasicInfoService;

    /**
     * 调用同盾API调用入口
     * @param request 请求参数 idNo - 身份证号码
     *                       name - 姓名
     *                       phone - 电话
     *                       custId - 个人信息ID
     *                                 
     * @return 最终数据
     */
    @PostMapping("/invokeTongDunApi")
    public ResultVO invokeTongDunApi(TongDunRequest request){
        if (request == null) {
            LOGGER.info("请求参数异常或不存在");
            return ResultVO.error("请求参数异常或不存在");
        }
        LOGGER.info("=====同盾数据调用====请求参数：{}",request.toString());
        try {
            //如果个人同盾报告已经失效（过期）或是未认证
            if (isLoseEfficacy(request.getCustomerId())) {
                TongDunProxy tongDunProxy = new TongDunProxy(tongDunApiService,apiResultServer);
                final ApiCommonResponse apiCommonResponse = tongDunProxy.invokeTongDunApi(request);
                if(apiCommonResponse == null){
                    return  ResultVO.error();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.ok("同盾调用失败:" + e.getMessage());
        }
        return ResultVO.ok("同盾调用成功");
    }

    /**
     *  查询风控数据是否过期，过去需要重新生产新同盾报告
     * @param custId 用户ID
     * @return false 没过期 true 过期
     */
    private boolean isLoseEfficacy(String custId){
        boolean isLoseEfficacy  = false;
        //获取提交人的同盾风控信息
        List<Map> tongDunInfo = appBasicInfoService.findEmpowerStatus(EApiSourceEnum.TODONG.getCode(),custId);
        //如果有同盾风控信息
        if(!CollectionUtils.isEmpty(tongDunInfo)){
            Map tongDunMap = tongDunInfo.get(0);
            //获取配置的时间期限（天）
            Map expireDays = appBasicInfoService.getExpireDays(EApiSourceEnum.TODONG.getCode());
            if (expireDays != null) {
                try {
                    int days = Integer.valueOf(tongDunMap.get("days").toString());
                    int expireDay = Integer.valueOf(expireDays.get("value") == "" ? "9999" : expireDays.get("value").toString());
                    //如果这条风控数据已经过期
                    if(days > expireDay){
                        isLoseEfficacy =true;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }else{
            //如果数据库查询不到证明是未认证
            isLoseEfficacy = true;
        }
        return isLoseEfficacy;
    }


}
