package com.zw.wechat.page.controller;
import com.zw.web.base.AbsBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/wechat/page")
public class PageController extends AbsBaseController {
    @Autowired
    /**
     * 跳转到商品分期页
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toCommodity",method = RequestMethod.GET)
    public String toCommodity() throws Exception {
        return "merch-wechat/customer/commodity";
    }

    /**
     * 跳转到分期订单详情
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toDetailsOrder",method = RequestMethod.GET)
    public String toDetailsOrder() throws Exception {
        return "merch-wechat/customer/DetailsOrder";
    }

    /**
     * 跳转到重新绑卡
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toBuildingCardAgain",method = RequestMethod.GET)
    public String toBuildingCardAgain() throws Exception {
        return "merch-wechat/customer/bindingCardAgain";
    }

    /**
     * 跳转到还款
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toRefundOne",method = RequestMethod.GET)
    public String toRefundOne() throws Exception {
        return "merch-wechat/customer/refundOne";
    }


    /**
     * 跳转到还款成功
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toRepaySuccess",method = RequestMethod.GET)
    public String toRepaySuccess() throws Exception {
        return "merch-wechat/customer/repaySuccess";
    }


    /**
     * 跳转到还款明细
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toRefundTwo",method = RequestMethod.GET)
    public String toRefundTwo() throws Exception {
        return "merch-wechat/customer/refundTwo";
    }
    /**
     * 跳转到支付
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toPay",method = RequestMethod.GET)
    public String toPay() throws Exception {
        return "merch-wechat/customer/pay";
    }

    /**
     * 跳转到支付成功
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toPaySuccess",method = RequestMethod.GET)
    public String toPaySuccess() throws Exception {
        return "merch-wechat/customer/paySuccess";
    }

    /**
     * 跳转到常见问题
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toComQuestions",method = RequestMethod.GET)
    public String toComQuestions() throws Exception {
        return "merch-wechat/mine/comQuestions";
    }
    /**
     * 跳转到我的分期申请
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toGoodsApply",method = RequestMethod.GET)
    public String toGoodsApply() throws Exception {
        return "merch-wechat/customer/goodsApply";
    }
    /**
     * 跳转到申请信息
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toPersonInfoHome",method = RequestMethod.GET)
    public String toPersonInfoHome() throws Exception {
        return "merch-wechat/personInfo/personInfoHome";
    }
    /**
     * 跳转到申请成功
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toApplySuccess",method = RequestMethod.GET)
    public String toApplySuccess() throws Exception {
        return "merch-wechat/customer/applySuccess1";
    }
    /**
     * 还款中
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toRepayOrder",method = RequestMethod.GET)
    public String toRepayOrder() throws Exception{
        return "merch-wechat/customer/DetailsOrderRepay";
    }

    /**
     * 账单明细
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toBillingDetails",method = RequestMethod.GET)
    public String toBillingDetails() throws Exception{
        return "merch-wechat/customer/billingDetails";
    }
    /**
     * 关于我们
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/aboutWe",method = RequestMethod.GET)
    public String aboutWe() throws Exception{
        return "merch-wechat/CommonAgreement";
    }
    /**
     * 注册页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String register() throws Exception{
        return "../../register/register";
    }
    /**
     * 邀请好友
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/friend",method = RequestMethod.GET)
    public String friend() throws Exception{
        return "merch-wechat/mine/InviteFriends";
    }
    /**
     * 邀请好友明细
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/friendDetail",method = RequestMethod.GET)
    public String friendDetail() throws Exception{
        return "merch-wechat/mine/InviteFriendsSuccess";
    }



    /**
     * 跳转到分期订单详情(带还款历史明细详情的分期订单详情)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toHistoryDetailsOrder",method = RequestMethod.GET)
    public String toDetailsOrderAndHistory() throws Exception {
        return "merch-wechat/customer/historyDetailsOrder";
    }


    /**
     * 确认合同 新增
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/confirmContract",method = RequestMethod.GET)
    public String confirmContract() throws Exception{
        return "merch-wechat/contract/contract_pdf";
    }
    /**
     * 示例
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/demo",method = RequestMethod.GET)
    public String demo() throws Exception{
        return "compensatory/demo";
    }
}