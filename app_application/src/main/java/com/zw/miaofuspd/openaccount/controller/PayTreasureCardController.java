//package com.zw.miaofuspd.openaccount.controller;
//
//import com.zw.app.util.AppConstant;
//import com.zw.miaofu.facade.entity.AppUserInfo;
//import com.zw.miaofu.facade.openaccount.service.IPayTreasureBindCardService;
//import com.zw.web.base.AbsBaseController;
//import com.zw.web.base.vo.ResultVO;
//import com.zw.web.base.vo.VOConst;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.util.Map;
//
///**
// * Created by Administrator on 2017/12/7 0007.
// */
//@Controller
//@RequestMapping("/payTreasure")
//public class PayTreasureCardController extends AbsBaseController {
//    @Autowired
//    public IPayTreasureBindCardService iPayTreasureBindCardService;
//    /**
//     * 宝付鉴权绑卡请求接口
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/bindingCard")
//    @ResponseBody
//    public ResultVO bindingCard(String name, String idNo, String cardNo, String bankName, String bankCode, String tel) throws Exception {
//        ResultVO resultVO = new ResultVO();
//        AppUserInfo userInfo = (AppUserInfo) this.getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
//        Map map = iPayTreasureBindCardService.payTreasureBindingCard(name,idNo,cardNo,bankName,bankCode,tel,userInfo);
//        boolean b = (boolean)map.get("flag");
//        if(!b){
//            resultVO.setErrorMsg(VOConst.FAIL,map.get("msg").toString());
//        }
//        resultVO.setRetData(map);
//        return resultVO;
//    }
//    /**
//     * 宝付鉴权绑卡请求接口
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/bindingCardConfirm")
//    @ResponseBody
//    public ResultVO bindingCardConfirm(String name, String idNo, String cardNo,
//                                       String bankName, String bankCode, String tel, String smsCode, String transId, String type) throws Exception {
//        ResultVO resultVO = new ResultVO();
//        AppUserInfo userInfo = (AppUserInfo) this.getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
//        Map map = iPayTreasureBindCardService.payTreasureConfimCard(name,idNo,cardNo,bankName,smsCode,bankCode,transId,tel,userInfo,type);
//        boolean b = (boolean)map.get("flag");
//        if(!b){
//            resultVO.setErrorMsg(VOConst.FAIL,map.get("msg").toString());
//        }
//        resultVO.setRetData(map);
//        return resultVO;
//    }
//    /**
//     * 宝付主动还款接口
//     * @param amount 还款金额
//     * @param derateAmount 减免金额
//     * @param cardNo 卡号
//     * @param derateId 减免id
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/payTreasureCardPay")
//    @ResponseBody
//    public ResultVO payTreasureCardPay(String amount, String derateAmount, String cardNo, String derateId) throws Exception {
//        ResultVO resultVO = new ResultVO();
//        AppUserInfo userInfo = (AppUserInfo) this.getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
////        AppUserInfo userInfo = new AppUserInfo();
////        userInfo.setId("f5ae0725-f5de-4bdc-83c8-49e0f4ddce56");
//        Map map = iPayTreasureBindCardService.payTreasureCardPay(amount,derateAmount,cardNo,userInfo,derateId);
//        resultVO.setRetData(map);
//        return resultVO;
//    }
//}
