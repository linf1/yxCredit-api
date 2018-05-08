package com.zw.miaofuspd.repayment.controller;

import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.openaccount.service.IPayTreasureBindCardService;
import com.zw.miaofuspd.facade.openaccount.service.IRepayPlanService;
import com.zw.miaofuspd.facade.red.service.RedService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 查询还款计划
 */
@Controller
@RequestMapping("/repay")
public class RepaymentController extends AbsBaseController {
    @Autowired
    private IRepayPlanService iRepayPlanService;
    @Autowired
    private IDictService iDictService;
    @Autowired
    private IPayTreasureBindCardService payTreasureBindCardService;
    @Autowired
    private RedService redService;

    /**
     * 秒付金服-获取多笔订单的还款计划
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getRepayList")
    @ResponseBody
    public ResultVO getRepayList() throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        //AppUserInfo userInfo = new AppUserInfo();
        //userInfo.setCustomer_id("73844c2f-3149-46e1-b2b4-fa558f441b60");
        Map map = iRepayPlanService.getRepayplanList(userInfo);
        if(!(boolean)map.get("flag")){
            resultVO.setErrorMsg(VOConst.FAIL,"当前无还款计划");
        }
        map.remove("flag");
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     * 秒付金服-获取一笔还款计划的详情
     * repaymentId 还款计划id
     * orderId  订单id
     * @return
     * @throws Exception
     */
    @RequestMapping("/getRepayDetails")
    @ResponseBody
    public ResultVO getRepayDetails(String repaymentId,String orderId) throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
//        AppUserInfo userInfo = new AppUserInfo();
//        userInfo.setCustomer_id("40e43836-70a5-411d-b9ce-98010c5f31b4");
        Map map = iRepayPlanService.getRepayDetails(repaymentId,orderId,userInfo.getCustomer_id());
        if(!(boolean)map.get("flag")){
            resultVO.setErrorMsg(VOConst.FAIL,"获取信息失败");
        }
        map.remove("flag");
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     *秒服金服-逾期还款说明
     * @return
     * @throws Exception
     */
    @RequestMapping("/getOverDueRemrak")
    @ResponseBody
    public ResultVO getOverDueRemrak() throws Exception {
        Map map = iRepayPlanService.getOverDueRemrak();
        ResultVO resultVO = new ResultVO();
        resultVO.setRetData(map);
        return resultVO;
    }

    /**
     *秒服金服-生成还款计划
     * @return
     * @throws Exception
     */
    @RequestMapping("/addRepayment")
    @ResponseBody
    public ResultVO addRepayment(String orderId) throws Exception {
        iRepayPlanService.addRepayment(orderId);//测试生产还款计划
        ResultVO resultVO = new ResultVO();
        return resultVO;
    }

    /**
     *全部结清
     * @return
     * @throws Exception
     */
    @RequestMapping("/settleAll")
    @ResponseBody
    public ResultVO settleAll(String orderId) throws Exception {
        ResultVO resultVO = new ResultVO();
        Map map = iRepayPlanService.settleAll(orderId);
        Boolean flag =(Boolean) map.get("flag");
        if(flag){
            resultVO.setRetData(map);
        }else{
            resultVO.setErrorMsg(VOConst.FAIL,"无还款信息！");
        }
        return resultVO;
    }

    /**
     *还款明细
     * @return
     * @throws Exception
     */
    @RequestMapping("/getRepayDetailList")
    @ResponseBody
    public ResultVO getRepayDetailList(String orderId) throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        Map redMap = redService.getRedList(userInfo);
        Map map = iRepayPlanService.getRepayDetailList(orderId);
        map.put("redList",redMap.get("redList"));
        resultVO.setRetData(map);
        return resultVO;
    }

    /**
     *账单明细
     * @return
     * @throws Exception
     */
    @RequestMapping("/getBillingDetails")
    @ResponseBody
    public ResultVO getBillingDetails(String orderId) throws Exception {
        ResultVO resultVO = new ResultVO();
        List list = iRepayPlanService.getBillingDetails(orderId);
        resultVO.setRetData(list);
        return resultVO;
    }

    /**
     *确认还款
     * @return
     * @throws Exception
     */
    @RequestMapping("/confirmPayByIds")
    @ResponseBody
    public ResultVO confirmPayByIds(String repayIds,String redIds) throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        Map map =payTreasureBindCardService.confirmPayByIds(userInfo,repayIds,redIds);
        resultVO.setRetData(map);
        return resultVO;
    }

    /**
     *提前结清确认还款
     * @return
     * @throws Exception
     */
    @RequestMapping("/confirmPaySettle")
    @ResponseBody
    public ResultVO confirmPaySettle(String orderId) throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        Map map =payTreasureBindCardService.earlyClearance(userInfo,orderId);
        resultVO.setRetData(map);
        return resultVO;
    }

    /**
     *获取提前结清权限
     * @return
     * @throws Exception
     */
    @RequestMapping("/getSettleAuth")
    @ResponseBody
    public ResultVO getSettleAuth(String orderId) throws Exception {
        ResultVO resultVO = new ResultVO();
        Map map = iRepayPlanService.getSettleAuth(orderId);
        resultVO.setRetData(map);
        return resultVO;
    }
}
