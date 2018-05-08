package com.zw.miaofuspd.personnal.controller;

import com.alibaba.fastjson.JSONObject;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.miaofuspd.facade.user.service.IUserService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Administrator on 2017/11/28 0028.
 */
@Controller
@RequestMapping("/basic")
public class BasicInfoController extends AbsBaseController {
    @Autowired
    AppBasicInfoService appBasicInfoService;
    @Autowired
    private IUserService userService;
    /**
     * 办单员端，信息完善中的-保存用户基本信息
     * @param data
     * @return
     */
    @RequestMapping("/addBasicInfo")
    @ResponseBody
    public ResultVO addBasicInfo(String data) throws Exception{
        Map map = JSONObject.parseObject(data);
        ResultVO resultVO = new ResultVO();
        String orderId = map.get("orderId").toString();
        AppUserInfo userInfo = userService.getUserByOrderId(orderId);
        map.put("customerId",userInfo.getCustomer_id());
        map.put("tel",userInfo.getTel());
        map.put("card",userInfo.getCard());
        map.put("realname",userInfo.getName());
        map.put("userId",userInfo.getId());
        Map resultMap = appBasicInfoService.addBasicInfo(map);
        if(!(Boolean)(resultMap.get("flag"))){
            resultVO.setErrorMsg(VOConst.FAIL,(String)(resultMap.get("msg")));
        }
        resultVO.setRetData(resultMap);
        return resultVO;
    }
    /**
     * 办单员端，信息完善中的-获取用户基本信息
     * @param
     * @return
     */
    @RequestMapping("/getBasicInfo")
    @ResponseBody
    public ResultVO getBasicInfo(String orderId) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = appBasicInfoService.getBasicInfo(orderId);
        resultVO.setRetData(map);
        return resultVO;
    }
}
