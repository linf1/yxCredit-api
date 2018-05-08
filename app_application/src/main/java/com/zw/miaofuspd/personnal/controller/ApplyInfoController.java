package com.zw.miaofuspd.personnal.controller;

import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.miaofuspd.facade.user.service.IAppIdentityService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/23 0023.
 */
@Controller
@RequestMapping("/apply")
public class ApplyInfoController extends AbsBaseController {
    @Autowired
    AppBasicInfoService appBasicInfoService;
    @Autowired
    IAppIdentityService iAppIdentityService;
    @Autowired
    IDictService dictServiceImpl;
    /**
     * 业务端获取申请信息接口
     * @return
     */
    @RequestMapping("/getApplyInfo")
    @ResponseBody
    public ResultVO getApplyInfo() throws Exception{
        AppUserInfo userInfo = (AppUserInfo) this.getHttpSession().getAttribute(AppConstant.APP_USER_INFO);
        Map linkManMap  = appBasicInfoService.getLinkMan(userInfo.getCustomer_id());
        Map indentityMap = iAppIdentityService.getIdentityInfoByCustomerId(userInfo.getCustomer_id());
        ResultVO resultVO = new ResultVO();
        Map returnMap = new HashMap();
        returnMap.put("linkManMap",linkManMap);
        returnMap.put("indentityMap",indentityMap);
        resultVO.setRetData(returnMap);
        return resultVO;
    }
    /**
     * 业务端获取基本信息接口
     * @return
     */
    @RequestMapping("/getBasicInfo")
    @ResponseBody
    public ResultVO getBasicInfo() throws Exception{
        AppUserInfo userInfo = (AppUserInfo) this.getHttpSession().getAttribute(AppConstant.APP_USER_INFO);
        ResultVO resultVO = new ResultVO();
        Map returnMap =  appBasicInfoService.getBasicCustomerInfo(userInfo.getCustomer_id());
        resultVO.setRetData(returnMap);
        return resultVO;
    }
    /**
     * 业务端-客户认证状态
     * @return
     */
    @RequestMapping("/getCustomerIdentityState")
    @ResponseBody
    public ResultVO getCustomerIdentityState() throws Exception{
        AppUserInfo userInfo = (AppUserInfo) this.getHttpSession().getAttribute(AppConstant.APP_USER_INFO);
        ResultVO resultVO = new ResultVO();
        Map map = iAppIdentityService.getIdentityState(userInfo.getCustomer_id());
        resultVO.setRetData(map);
        return resultVO;
    }
}
