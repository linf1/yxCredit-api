package com.zw.miaofuspd.employee.controller;

import com.alibaba.fastjson.JSONObject;
import com.zw.miaofuspd.facade.merchant.service.MerchantService;
import com.zw.miaofuspd.facade.merchant.service.QrCodeService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Administrator on 2017/11/22 0022.
 */
@Controller
@RequestMapping("/qrCode")
public class QRCodeController extends AbsBaseController {
    @Autowired
    public QrCodeService qrCodeService;
    @Autowired
    public MerchantService merchantService;
    /**
     * 生成商户商品二维码
     * @param idJson 服务包id的json集合
     * @return
     */
    @RequestMapping("/createQRCode")
    @ResponseBody
    public ResultVO createQRCode(String data,String idJson) throws Exception{
        Map<String,String> inMap = (Map) JSONObject.parse(data);
        String root = getRequest().getSession().getServletContext().getRealPath("/fintecher_file");
        ResultVO resultVO = new ResultVO();
        String merchantId = inMap.get("merchantId").toString();
        String allMoney= inMap.get("allMoney").toString();
        String downMoney= inMap.get("downPayMoney").toString();
        //先进行渠道商户限额判断和区域限额判断
        Map merMap = merchantService.checkMerchantQuota(merchantId,allMoney,downMoney);
        if(!(boolean)merMap.get("flag")){
            resultVO.setErrorMsg(VOConst.FAIL,merMap.get("msg").toString());
            return resultVO;
        }
        inMap.put("root",root);
        Map map = qrCodeService.createQRcode(inMap,idJson);
        map.remove("flag");
        resultVO.setRetData(map);
        return resultVO;
    }
}
