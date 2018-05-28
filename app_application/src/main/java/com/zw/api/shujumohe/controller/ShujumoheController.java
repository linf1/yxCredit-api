package com.zw.api.shujumohe.controller;

import com.api.model.result.ApiResult;
import com.api.model.shujumohe.ShujumoheRequest;
import com.api.service.result.IApiResultServer;
import com.api.service.shujumohe.IShujumoheServer;
import com.base.util.AppRouterSettings;
import com.base.util.GeneratePrimaryKeyUtils;
import com.constants.ApiConstants;
import com.zhiwang.zwfinance.app.jiguang.util.api.EApiSourceEnum;
import com.zw.web.base.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

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
        try {
            ApiResult resultParameter = new ApiResult();
            resultParameter.setUserName(request.getPhone());
            resultParameter.setSourceCode(EApiSourceEnum.MOHE.getCode());
            //查询是否有报告
            final List<Map> mapList = apiResultServerImpl.selectApiResult(resultParameter);
            if(CollectionUtils.isEmpty(mapList)){
                Map<String,Object>  param = new HashMap<>(5);
                param.put("identity_code","");
                param.put("channel_type","");
                param.put("real_name","");
                param.put("user_mobile","");
                param.put("task_data","");
                //默认数据成功
                saveMoheInfo(request,param);
                //异步更新数据
                syncExecutor(request);
            }else{
                final Map map = mapList.get(0);
                Map<String,Object>  param = new HashMap<>(5);
                param.put("identity_code",map.get("identity_code"));
                param.put("channel_type",map.get("source_child_code"));
                param.put("real_name",map.get("real_name"));
                param.put("user_mobile",map.get("user_mobile"));
                param.put("task_data",map.get("result_data"));
                //保存数据到数据库
                saveMoheInfo(request, param);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResultVO.error();
        }
        return  ResultVO.ok("验证成功！",null);
    }

    /**
     * 持久化魔盒数据
     * @param request 数据请求数据
     * @param data 魔盒数据
     * @throws Exception
     */
    private void saveMoheInfo(ShujumoheRequest request, Map data) throws Exception {
        ApiResult resultParameter = new ApiResult();
        resultParameter.setUserName(request.getPhone());
        resultParameter.setSourceCode(EApiSourceEnum.MOHE.getCode());
        resultParameter.setOnlyKey(request.getOrderId());
        //一个订单只会有一种风控数据 ，如果数据存在就不在继续添加
        if (apiResultServerImpl.validateData(resultParameter)) {
            return;
        }
        ApiResult apiResult = new ApiResult();
        apiResult.setId(GeneratePrimaryKeyUtils.getUUIDKey());
        apiResult.setCode(ApiConstants.STATUS_SUCCESS);
        apiResult.setIdentityCode(data.get("identity_code").toString());
        apiResult.setMessage(ApiConstants.STATUS_SUCCESS_MSG);
        apiResult.setSourceChildName(ApiConstants.API_MOHE_YYS);
        apiResult.setSourceChildCode(data.get("channel_type").toString());
        apiResult.setOnlyKey(request.getOrderId());
        apiResult.setRealName(data.get("real_name").toString());
        apiResult.setSourceName(EApiSourceEnum.MOHE.getName());
        apiResult.setSourceCode(EApiSourceEnum.MOHE.getCode());
        apiResult.setUserMobile(data.get("user_mobile").toString());
        apiResult.setUserName(request.getPhone());
        apiResult.setResultData(data.get("task_data").toString());
        apiResult.setState(1);
        apiResultServerImpl.insertApiResult(apiResult);
    }

    /**
     * 异步执行数据并且更新到数据库
     * @param request 请求参数对象
     *                orderId - 订单id
     *                phone - 手机号码
     *                taskId - 数据魔盒数据查询id
     *
     */
    private void syncExecutor(ShujumoheRequest request){
        final BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
        final ThreadFactory threadFactory = Thread::new;
        final ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS, blockingQueue, threadFactory);
        poolExecutor.submit(new ShujumoheRunable(this.apiResultServerImpl,this.shujumoheServerImpl,request));
        poolExecutor.shutdown();

    }
}
