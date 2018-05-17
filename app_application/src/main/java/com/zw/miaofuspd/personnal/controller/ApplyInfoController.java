package com.zw.miaofuspd.personnal.controller;

import com.alibaba.fastjson.JSONObject;
import com.base.util.AppRouterSettings;
import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.miaofuspd.facade.user.service.IAppIdentityService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by 韩梅生
 */
@Controller
@RequestMapping(AppRouterSettings.APPLY_MODULE)
public class ApplyInfoController extends AbsBaseController {
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
        if (CollectionUtils.isEmpty(personMap)) {
            //说明尚未填写申请信息
            resultVO.setRetCode("1");
        } else {
                Map homeApplyMap = appBasicInfoService.getHomeApplyInfo(id,productName);
                resultVO.setRetCode("2");
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
        Map map = JSONObject.parseObject(data);
        ResultVO resultVO = new ResultVO();
        Map resultMap = appBasicInfoService.addBasicCustomerInfo(map);
        if(!(Boolean)(resultMap.get("flag"))){
            resultVO.setErrorMsg(VOConst.FAIL,(String)(map.get("msg")));
        }
        resultVO.setRetMsg((String)map.get("msg"));
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
        Map resultMap = appBasicInfoService.checkCustomerInfo((String) map.get("userId"),(String) map.get("card"));
        if(!(Boolean)(resultMap.get("flag"))){
            resultVO.setErrorMsg(VOConst.FAIL,(String)(resultMap.get("msg")));
        }
        resultVO.setRetMsg((String)resultMap.get("msg"));
        return resultVO;
    }




}
