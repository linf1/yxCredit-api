package com.zw.miaofuspd.employee.controller;

import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.miaofuspd.facade.user.service.IAppIdentityService;
import com.zw.miaofuspd.facade.user.service.IUserService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/11.
 */
@Controller
@RequestMapping("/employeeApply")
public class PersonnalInfoController extends AbsBaseController {
    @Autowired
    AppBasicInfoService appBasicInfoService;
    @Autowired
    IAppIdentityService iAppIdentityService;
    @Autowired
    private IUserService userService;
    @Autowired
    private AppOrderService appOrderService;
    /**
     * 办单员端,信息完善中-获取用户的个人信息
     * @param orderId
     * @return
     */
    @RequestMapping("/getApplyInfo")
    @ResponseBody
    public ResultVO getBasicInfo(String orderId) throws Exception{
        Map  resultMap =new HashMap();
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = userService.getUserByOrderId(orderId);
        String customerId=userInfo.getCustomer_id();
        Map linkManMap  = appBasicInfoService.getLinkMan(customerId);//查询联系人信息
        Map indentyMap = iAppIdentityService.getIdentityInfoByCustomerId(customerId);//查询身份认证信息
        Map indetityStateMap = iAppIdentityService.getIdentityState(customerId);//获取客户信息完成度
        Map orderMap = appOrderService.getOrderById(orderId);
        resultMap.put("commodityState",orderMap.get("commodityState").toString());
        resultMap.put("linkManMap",linkManMap);
        resultMap.put("indentyMap",indentyMap);
        resultMap.put("indetityStateMap",indetityStateMap);
        resultVO.setRetData(resultMap);
        return resultVO;
    }
}
