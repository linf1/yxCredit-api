package com.zw.miaofuspd.my.controller;

import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.home.service.HomeService;
import com.zw.miaofuspd.personnal.controller.BasicInfoController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * @Author xiahaiyang
 * @Create 2017年12月21日10:03:19
 **/
@Controller
@RequestMapping("/home")
public class HomeController  extends BasicInfoController{

    @Autowired
    HomeService homeService;

    /**
     *获取我的页面信息
     * @return
     */
    @RequestMapping("/getMyPageInfo")
    @ResponseBody
    public ResultVO getMyPageInfo(){
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo)getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        Map map  = homeService.getMyPageInfo(userInfo.getId());
//        Map map  = homeService.getMyPageInfo("d2f2074e-66aa-430c-8c16-1ecf3804e119");
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     *判断是否可以分享好友
     * @return
     */
    @RequestMapping("/checkOrder")
    @ResponseBody
    public ResultVO checkOrder(){
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo)getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        Map map  = homeService.checkOrder(userInfo);
        if(!(boolean)map.get("flag")){
            resultVO.setErrorMsg(VOConst.FAIL,map.get("msg").toString());
        }
        map.remove("flag");
        resultVO.setRetData(map);
        return resultVO;
    }

}
