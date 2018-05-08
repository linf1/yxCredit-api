package com.zw.miaofuspd.employee.controller;

import com.zw.miaofuspd.facade.ratescheme.service.RateSchemeService;
import com.zw.miaofuspd.facade.serpackage.service.SerPackageService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/18 0018.
 */
@Controller
@RequestMapping("/serPackage")
public class SerPackageController extends AbsBaseController {
    @Autowired
    private SerPackageService serPackageService;
    @Autowired
    private RateSchemeService rateSchemeService;
    /**
     * 获取利率方案接口
     * @return
     */
    @RequestMapping("/getSerPackage")
    @ResponseBody
    public ResultVO getSerPackage(String productId) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = rateSchemeService.getFeeInfo(productId);
        String periods = String.valueOf(map.get("periods").toString());
        List list = serPackageService.getSerPackage(periods);
        resultVO.setRetData(list);
        return resultVO;
    }
    /**
     * 获取利率方案接口
     * @return
     */
    @RequestMapping("/getMonthPackage")
    @ResponseBody
    public ResultVO getMonthPackage(String afterMonth,String productId) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = rateSchemeService.getFeeInfo(productId);
        String periods = String.valueOf(map.get("periods").toString());
        List list = serPackageService.getMonthPackage(afterMonth,periods);
        resultVO.setRetData(list);
        return resultVO;
    }
}
