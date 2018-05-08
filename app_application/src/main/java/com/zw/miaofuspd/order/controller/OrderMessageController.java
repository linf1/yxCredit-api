package com.zw.miaofuspd.order.controller;

import com.zw.miaofuspd.facade.user.service.IMsgService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后台做审批通过，和放款的时候，向手机端，推送消息
 */
@Controller
@RequestMapping("/orderMessage")
public class OrderMessageController extends AbsBaseController {
    @Autowired
    private IMsgService iMsgService;
    /**
     * 后台做审批通过，和放款的时候，向手机端，推送消息
     * @return
     * @throws Exception
     */
    @RequestMapping("/orderPushMessage")
    @ResponseBody
    public ResultVO orderPushMessage(String orderId, String state) throws Exception{
        ResultVO resultVO = new ResultVO();
        iMsgService.insertOrderMsg(state,orderId);
        return resultVO;
    }
}
