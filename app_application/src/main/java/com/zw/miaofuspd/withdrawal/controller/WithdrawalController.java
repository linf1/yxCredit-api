package com.zw.miaofuspd.withdrawal.controller;

import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.openaccount.service.IAppWithdrawalsService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


@Controller
@RequestMapping("/withdrawal")
public class WithdrawalController extends AbsBaseController {
    @Autowired
    private IAppWithdrawalsService appWithdrawalsService;
    @Autowired
    private IDictService iDictService;
    /**
     * 获取开户信息
     * @return
     * @throws Exception
     */
    @RequestMapping("/getYbWithdrawal")
    @ResponseBody
    public ResultVO getYbWithdrawal() throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) this.getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        String customer_id = userInfo.getCustomer_id();
        String phone = iDictService.getDictInfo("联系客服","0");
        Map map = appWithdrawalsService.getOpenAccountInfo(customer_id);
        if (!(Boolean) map.get("flag")) {
            resultVO.setErrorMsg(VOConst.FAIL, map.get("msg").toString());
        }
        resultVO.setErrorMsg(VOConst.SUCCESS, map.get("msg").toString());
        map.put("phone",phone);
        resultVO.setRetData(map);
        return resultVO;
    }

    /**
     * 提现接口
     * @return
     * @throws Exception
     */
    @RequestMapping("/ybWithdrawal")
    @ResponseBody
    public ResultVO ybWithdrawal(String red_money, String repay_money, String idJson) throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) this.getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        Map map = appWithdrawalsService.ybWithdrawal(red_money,repay_money,userInfo,idJson);
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     * 获取提现明细
     * @return
     * @throws Exception
     */
    @RequestMapping("/getYbWithdrawalList")
    @ResponseBody
    public ResultVO getYbWithdrawalList() throws Exception {
        ResultVO resultVO = new ResultVO();
       AppUserInfo userInfo = (AppUserInfo) this.getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        Map map = appWithdrawalsService.getYbWithdrawalList(userInfo);
        resultVO.setRetData(map);
        return resultVO;
    }

}

