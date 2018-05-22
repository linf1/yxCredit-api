package com.zw.miaofuspd.personnal.controller;

import com.alibaba.fastjson.JSONObject;
import com.base.util.AppRouterSettings;
import com.base.util.DateUtils;
import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;
import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.miaofuspd.facade.user.service.IAppIdentityService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by 韩梅生
 */
@Controller
@RequestMapping(AppRouterSettings.VERSION+AppRouterSettings.APPLY_MODULE)
public class ApplyInfoController extends AbsBaseController {

    private final Logger LOGGER = LoggerFactory.getLogger(ApplyInfoController.class);
    @Autowired
    AppBasicInfoService appBasicInfoService;
    @Autowired
    IAppIdentityService iAppIdentityService;
    @Autowired
    IDictService dictServiceImpl;

    /**
     * 业务端获取申请信息接口
     *
     * @return
     */
    @RequestMapping("/getApplyInfo")
    @ResponseBody
    public ResultVO getApplyInfo(String id,String productName) throws Exception {
        ResultVO resultVO = new ResultVO();
        //获取申请时的用户信息
        Map personMap = appBasicInfoService.getPersonInfo(id);
        if (personMap.get("code").equals("1")) {
            //说明尚未填写申请信息
            resultVO.setRetCode("1");
        } else {
                Map homeApplyMap = appBasicInfoService.getHomeApplyInfo(id,productName);
                resultVO.setRetCode(homeApplyMap.get("code").toString());
                resultVO.setRetMsg((String) homeApplyMap.get("msg"));
                resultVO.setRetData(homeApplyMap.get("resMap"));

        }
        return resultVO;
    }


    /**
     * 保存三要素
     * @author 韩梅生
     * @return
     */
    @RequestMapping("/addBasicInfo")
    @ResponseBody
    public ResultVO addBasicInfo(String data) throws Exception {
        LOGGER.info("---------三要素:"+data);
        Map map = JSONObject.parseObject(data);
        ResultVO resultVO = new ResultVO();
        Map resultMap = appBasicInfoService.addBasicCustomerInfo(map);
        if(!(Boolean)(resultMap.get("flag"))){
            resultVO.setErrorMsg(VOConst.FAIL,(String)(map.get("msg")));
        }
        resultVO.setRetMsg((String)map.get("msg"));
        resultVO.setRetData(resultMap);
        return resultVO;
    }

    /**
     * 业务端-客户认证状态
     * @author 韩梅生
     * @return
     */
    @RequestMapping("/getCustomerIdentityState")
    @ResponseBody
    public ResultVO getCustomerIdentityState() throws Exception {
        AppUserInfo userInfo = (AppUserInfo) this.getHttpSession().getAttribute(AppConstant.APP_USER_INFO);
        ResultVO resultVO = new ResultVO();
        Map map = iAppIdentityService.getIdentityState(userInfo.getCustomer_id());
        resultVO.setRetData(map);
        return resultVO;
    }

    /**
     * @author:韩梅生
     * @Description  一键申请
     * @Date 15:59 2018/5/12
     * @param
     * @return com.zw.web.base.vo.ResultVO
     */

    @RequestMapping("/oneClickApply")
    @ResponseBody
    public ResultVO oneClickApply(String orderId) throws Exception {
        ResultVO resultVO = new ResultVO();
        Map map = appBasicInfoService.oneClickApply(orderId);
        if(!(Boolean)(map.get("flag"))){
            resultVO.setErrorMsg(VOConst.FAIL,(String)(map.get("msg")));
        }
        resultVO.setRetMsg((String)map.get("msg"));
        return resultVO;
    }

    /**
     * @author:韩梅生
     * @Description 用户信息强规则
     * @Date 20:09 2018/5/14
     * @param
     */
    @RequestMapping("/checkCustomerInfo")
    @ResponseBody
    public ResultVO checkCustomerInfo(String data) throws Exception {
        Map map = JSONObject.parseObject(data);
        ResultVO resultVO = new ResultVO();
        Map resultMap = appBasicInfoService.checkCustomerInfo(map.get("customerId").toString(),map.get("card").toString());
        if(!(Boolean)(resultMap.get("flag"))){
            resultVO.setErrorMsg(VOConst.FAIL,(String)(resultMap.get("msg")));
        }
        resultVO.setRetMsg((String)resultMap.get("msg"));
        return resultVO;
    }

    /**
     * @author:韩梅生
     * @Description 取消订单
     * @Date 17:00 2018/5/19
     * @param
     */
    @RequestMapping("/cancelOrder")
    @ResponseBody
    public  ResultVO cancelOrder(String orderId){
        ResultVO resultVO = new ResultVO();
        int i = appBasicInfoService.cancelOrder(orderId);
        if(i == 0){
            resultVO.setErrorMsg(VOConst.FAIL,"取消失败");
        }
        resultVO.setRetMsg("取消成功");
        return resultVO;
    }

    /**
     * @author:韩梅生
     * @Description 获取申请信息主页面
     * @Date 18:46 2018/5/19
     * @param
     */
    @RequestMapping("/getHomeApplyInfo")
    @ResponseBody
    public  ResultVO getHomeApplyInfo(String id,String productName) throws  Exception{
        ResultVO resultVO = new ResultVO();
        Map homeApplyMap = appBasicInfoService.getHomeApplyInfo(id,productName);
        resultVO.setRetCode(homeApplyMap.get("code").toString());
        resultVO.setRetMsg((String) homeApplyMap.get("msg"));
        resultVO.setRetData(homeApplyMap.get("resMap"));
        return resultVO;
    }

    /**
     * @author 韩梅生
     * @date 19:43 2018/5/21
     * 获取授权状态
     */
    @RequestMapping("/getEmpowerStatus")
    @ResponseBody
    public  ResultVO getEmpowerStatus(String orderId,String customerId) throws  Exception{
        ResultVO resultVO = new ResultVO();
        Map map = appBasicInfoService.getEmpowerStatus(orderId,customerId);
        resultVO.setRetData(map);
        return resultVO;
    }

    public static void main(String[] args) {
        String ss="总包商01-测试,3,总包商0,4";
    }
}
