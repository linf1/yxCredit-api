package com.zw.miaofuspd.personnal.controller;

import com.alibaba.fastjson.JSONObject;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
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
     *
     * @return
     */
    @RequestMapping("/getApplyInfo")
    @ResponseBody
    public ResultVO getApplyInfo(String id,String productName) throws Exception {
        //AppUserInfo userInfo = (AppUserInfo) this.getHttpSession().getAttribute(AppConstant.APP_USER_INFO);
        ResultVO resultVO = new ResultVO();
        Map returnMap = new HashMap();
        //获取申请时的用户信息
        Map personMap = appBasicInfoService.getPersonInfo(id);
        if (personMap == null) {
            //说明尚未填写申请信息
            resultVO.setRetCode("2");
        } else {
            if (personMap.get("is_identity") == "1") {
                //说明已完成身份认证
                Map homeApplyMap = appBasicInfoService.getHomeApplyInfo(id,productName);
                resultVO.setRetCode("1");
                resultVO.setRetData(homeApplyMap);
            } else {
                //填写过申请信息
                Map basInfoMap = appBasicInfoService.getPersonInfo(id);
                resultVO.setRetCode("3");
                resultVO.setRetData(basInfoMap);

            }

        }
        return resultVO;
    }


    /**
     * 保存三要素
     *
     * @return
     */
    @RequestMapping("/addBasicInfo")
    @ResponseBody
    public ResultVO getBasicInfo(String data) throws Exception {
        Map map = JSONObject.parseObject(data);
        ResultVO resultVO = new ResultVO();
        appBasicInfoService.getBasicCustomerInfo(map);
        Map homeApplyMap = appBasicInfoService.getHomeApplyInfo((String) map.get("id"),(String) map.get("productName"));
        resultVO.setRetData(homeApplyMap);
        return resultVO;
    }

    /**
     * 业务端-客户认证状态
     *
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
     * @author:hanmeisheng
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


}
