package com.zw.miaofuspd.employee.controller;

import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.ratescheme.service.RateSchemeService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 获取利率方案接口
 */
@Controller
@RequestMapping("/rate")
public class RateSchemeController extends AbsBaseController {
    @Autowired
    private RateSchemeService rateSchemeService;
    @Autowired
    private IDictService iDictService;
    /**
     * 获取利率方案接口
     * @return
     */
    @RequestMapping("/getRateScheme")
    @ResponseBody
    public ResultVO getRateScheme() throws Exception{
        ResultVO resultVO = new ResultVO();
        List list = rateSchemeService.getRateScheme();
        resultVO.setRetData(list);
        return resultVO;
    }

    /**
     * 获取线上线下订单
     * @return
     */
    @RequestMapping("/getOfflineOrder")
    @ResponseBody
    public ResultVO getOfflineOrder() throws Exception{
        ResultVO resultVO = new ResultVO();
          List orderTypeList = iDictService.getDictJson("订单类型");
        resultVO.setRetData(orderTypeList);
        return resultVO;
    }
}
