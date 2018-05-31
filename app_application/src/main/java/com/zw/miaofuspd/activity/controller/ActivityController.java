package com.zw.miaofuspd.activity.controller;

import com.zw.activity.service.ActivityService;
import com.base.util.AppRouterSettings;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by Administrator on 2018/5/30
 */
@Controller
@RequestMapping(AppRouterSettings.VERSION + "/activity")
public class ActivityController extends AbsBaseController {

    @Autowired
    private ActivityService activityService;



    /****************************************************碧友信**********************************************/



    /**
     * 获取已上架Banner列表
     * @author 仙海峰
     * @return
     */
    @RequestMapping("/getBannerList")
    @ResponseBody
    public ResultVO getBannerList() throws Exception{
        ResultVO resultVO = new ResultVO();

        Map map  = activityService.getBannerList();
        resultVO.setRetData(map);

        return resultVO;
    }





}
