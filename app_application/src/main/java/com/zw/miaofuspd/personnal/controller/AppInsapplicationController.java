package com.zw.miaofuspd.personnal.controller;

import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.entity.CreditResponse;
import com.zw.miaofuspd.facade.merchant.service.MerchantService;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.facade.order.service.IAppInsapplicationService;
import com.zw.miaofuspd.facade.order.service.RedPacketService;
import com.zw.miaofuspd.facade.personal.service.AppUserService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 *秒付金服-进件接口
 */
@Controller
@RequestMapping("/miaofuOrder")
public class AppInsapplicationController extends AbsBaseController {
    @Autowired
    private IAppInsapplicationService iAppInsapplicationService;
    @Autowired
    private AppOrderService appOrderService;
    @Autowired
    private RedPacketService redPacketService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private AppUserService appUserService;
    /**
     * 秒付金服提交进件接口
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping("/subOrder")
    @ResponseBody
    public Object makeOrder(String orderId) throws Exception{
        AppUserInfo userInfo=(AppUserInfo)getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        ResultVO resultVO =new ResultVO();
        //获取续贷的状态
        String overdue_state = appOrderService.getOverdueState(orderId);
        if("0".equals(overdue_state)){
            resultVO.setErrorMsg("overdue_state","上笔订单逾期！");
            return resultVO;
        }
        //根据订单id获取订单信息
        Map orderMap= appOrderService.getOrderById(orderId);
        //查询改商户下是否超过单笔限额以及每日进件笔数
        Map merMap = merchantService.checkMerchantQuota(orderMap.get("merchantId").toString(),orderMap.get("amount").toString(),orderMap.get("predictPrice").toString());
        if(!(Boolean)(merMap.get("flag"))){
            resultVO.setErrorMsg(VOConst.FAIL,(String)(merMap.get("msg")));
            return resultVO;
        }

        Map outMap=iAppInsapplicationService.makeOrder(userInfo,orderId);
        if(!(Boolean)(outMap.get("flag"))){
            resultVO.setErrorMsg(VOConst.FAIL,(String)(outMap.get("msg")));
            return resultVO;
        }
        redPacketService.getRedPacket(userInfo.getId(),orderId);
        resultVO.setRetMsg((String)(outMap.get("msg")));
        return resultVO;
    }

    /**
     * 287规则回调地址
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/zx")
    @ResponseBody
    public ResultVO txrCode(String response) throws Exception{
        ResultVO resultVO =new ResultVO();
        iAppInsapplicationService.zx(response);
        return  resultVO;
    }

    /**
     * 调取91征信回调地址
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/callback")
    @ResponseBody
    public ResultVO callback(String response,String idNo) throws Exception{
        ResultVO resultVO =new ResultVO();
        iAppInsapplicationService.callback(response,idNo);
        return  resultVO;
    }
}
