package com.zw.miaofuspd.employee.controller;

import com.zw.miaofuspd.facade.merchant.service.MerchantService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/22 0022.
 */
@Controller
@RequestMapping("/merchant")
public class MerchantController extends AbsBaseController {
    @Autowired
    public MerchantService merchantService;
    /**
     *获取该业务员下所有的商户列表
     * @param salesmanId 办单员id
     * @return
     * @throws Exception
     */
    @RequestMapping("/getMerchantList")
    @ResponseBody
    //通过测试
    public ResultVO getMerchantList(String salesmanId) throws Exception{
        ResultVO resultVO = new ResultVO();
        List list = merchantService.getMerchantList(salesmanId);
        if(list!=null && list.size()>0){
            resultVO.setRetData(list);
        }else{
            resultVO.setRetCode(VOConst.FAIL);
            resultVO.setRetData(new ArrayList());
            resultVO.setRetMsg("没有查到激活的商户");
        }
        return resultVO;
    }

    /**
     * 根据商户名称，模糊查询出所有符合的商户
     * @param merchantName  模糊搜索店铺的内容
     * @param salesmanId 办单员id
     * @return
     * @throws Exception
     */
    @RequestMapping("/getMerchantInfo")
    @ResponseBody
    //通过测试
    public ResultVO getMerchantsByInfo(String merchantName, String salesmanId) throws Exception{
        ResultVO resultVO = new ResultVO();
        List list = merchantService.selectMerchantByInfo(merchantName,salesmanId);
        if(list!=null && list.size()>0){
            resultVO.setRetData(list);
        }else{
            resultVO.setRetMsg("没有搜索到符合条件的商户");
            resultVO.setRetCode(VOConst.FAIL);
            resultVO.setRetData(new ArrayList());
        }
        return resultVO;
    }
}
