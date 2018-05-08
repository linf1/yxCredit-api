package com.zw.miaofuspd.employee.controller;

import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.openaccount.service.IPayTreasureBindCardService;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.facade.personal.service.AppUserService;
import com.zw.miaofuspd.facade.serpackage.service.SerPackageService;
import com.zw.miaofuspd.facade.user.service.IMsgService;
import com.zw.miaofuspd.facade.user.service.IUserService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/7 0007.
 */
@Controller
@RequestMapping("/payTreasure")
public class PayTreasureCardController extends AbsBaseController {
    @Autowired
    private IPayTreasureBindCardService iPayTreasureBindCardService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IMsgService iMsgService;
    @Autowired
    private IDictService iDictService;
    @Autowired
    private AppOrderService appOrderService;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private SerPackageService serPackageService;

    /**
     * 宝付鉴权绑卡请求接口
     * @return
     * @throws Exception
     */
    @RequestMapping("/bindingCard")
    @ResponseBody
    public ResultVO bindingCard(String name, String idNo, String cardNo, String bankName, String bankCode, String tel,String orderId) throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = null;
        if (null == orderId || "".equals(orderId))
        {
            userInfo = (AppUserInfo) this.getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        }
        else
        {
            userInfo = iUserService.getUserByOrderId(orderId);
        }
        Map map = iPayTreasureBindCardService.payTreasureBindingCard(name,idNo,cardNo,bankName,bankCode,tel,userInfo);
        boolean b = (boolean)map.get("flag");
        if(!b){
            resultVO.setErrorMsg(VOConst.FAIL,map.get("msg").toString());
        }
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     * 宝付鉴权绑卡请求接口
     * @return
     * @throws Exception
     */
    @RequestMapping("/bindingCardConfirm")
    @ResponseBody
    public ResultVO bindingCardConfirm(String name, String idNo, String cardNo,
                                       String bankName, String bankCode, String tel, String smsCode, String transId,String orderId) throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = null;
        if (null == orderId || "".equals(orderId))
        {
            userInfo = (AppUserInfo) this.getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        }
        else
        {
            userInfo = iUserService.getUserByOrderId(orderId);
        }
        Map map = iPayTreasureBindCardService.payTreasureConfimCard(name,idNo,cardNo,bankName,smsCode,bankCode,transId,tel,userInfo);
        boolean b = (boolean)map.get("flag");
        if(!b){
            resultVO.setErrorMsg(VOConst.FAIL,map.get("msg").toString());
        }
       /* appOrderService.updateOrderStateBeforeSubmit(orderId,"17");//绑卡卡成功以后,修改订单的小状态*/
       if (null != orderId && !"".equals(orderId))
       {
           appOrderService.updateOrderStateBeforeSubmit(orderId,"16.5");//绑卡卡成功以后,修改订单的小状态(合同确认)
       }
//        iMsgService.insertOrderMsg("6",orderId);
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     * 宝付主动还款接口
     * @param amount 还款金额
     * @param derateAmount 减免金额
     * @param cardNo 卡号
     * @param derateId 减免id
     * @return
     * @throws Exception
     */
    @RequestMapping("/payTreasureCardPay")
    @ResponseBody
    public ResultVO payTreasureCardPay(String amount, String derateAmount, String cardNo, String derateId) throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) this.getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        Map map = iPayTreasureBindCardService.payTreasureCardPay(amount,derateAmount,cardNo,userInfo,derateId);
        resultVO.setRetData(map);
        return resultVO;
    }


    /**
     * 宝付支付前置费用
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping("/payServicePackageCardPay")
    @ResponseBody
    public ResultVO payServicePackageCardPay(String orderId) throws Exception{
        //获取用户信息
        AppUserInfo userInfo = (AppUserInfo)getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        Map map = iPayTreasureBindCardService.payServicePackageCardPay(userInfo,orderId);
        return this.createResultVO(map);
    }

    /**
     * 宝付支付前置服务包结果
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping("/payServicePackageResult")
    @ResponseBody
    public ResultVO payServicePackageResult(String orderId) throws  Exception{
        //获取用户信息
        Map map = iPayTreasureBindCardService.payServicePackageResult(orderId);
        return  this.createResultVO(map);
    }

    /**
     * 获取开户用户信息  姓名,身份证,开户行列表
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping("/getOpenAccountInfo")
    @ResponseBody
    public ResultVO getOpenAccountInfo(String orderId) throws Exception{
        Map map = new HashMap();
        //获取用户信息
        AppUserInfo userInfo = null;
        if (null == orderId || "".equals(orderId))
        {
            userInfo = (AppUserInfo) this.getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        }
        else
        {
            userInfo = iUserService.getUserByOrderId(orderId);
        }
        List bankNameList = iDictService.getDictJson("开户行");
        map.put("userName",userInfo.getName());//姓名
        map.put("IdNo",userInfo.getCard());//卡号
        map.put("bankNameList",bankNameList);//支持的开户行
        return this.createResultVO(map);
    }

    /**
     * 获取立即支付前置提前还款包的信息
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping("/getPaySerPcgInfo")
    @ResponseBody
    public ResultVO getPaySerPcgInfo(String orderId) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = new HashMap();
        //获取用户信息
        AppUserInfo userInfo = (AppUserInfo)getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        Map accountInfo = appUserService.getAccountInfo(userInfo);
        Map serPcgInfo = serPackageService.getSerPcgByOderId(orderId);
        if ((boolean)accountInfo.get("flag")&&(boolean)serPcgInfo.get("flag")){
            map.putAll(accountInfo);
            map.putAll(serPcgInfo);
            resultVO.setRetData(map);
        }else{
            resultVO.setErrorMsg(VOConst.FAIL,"查询支付信息有误");
        }
        return resultVO;
    }
}
