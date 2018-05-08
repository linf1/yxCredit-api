package com.zw.miaofuspd.msg.controller;

import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.myset.service.IAppMessageService;
import com.zw.miaofuspd.facade.personal.service.AppUserService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/25 0025.
 */
@Controller
@RequestMapping("/msg")
public class MsgController extends AbsBaseController {
    @Autowired
    IAppMessageService appMessageService;
    @Autowired
    AppUserService appUserService;

    /**
     * 获取系统消息和订单消息
     * @return
     * @throws Exception
     */
    @RequestMapping("/getMsgList")
    @ResponseBody
    public ResultVO getMsgList()throws Exception {
        //获取session中的用户数据
        AppUserInfo userInfo = (AppUserInfo)getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        Map map = appMessageService.getMessageList(userInfo.getId());//获取系统消息和订单消息
        ResultVO resultVO = this.createResultVO(map);
        return resultVO;
    }

    /**
     * 将消息设置成已读
     * @return
     * @throws Exception
     */
    @RequestMapping("/setReadyMsg")
    @ResponseBody
    public ResultVO setReadyMsg()throws Exception {
        //获取session中的用户数据
        AppUserInfo userInfo = (AppUserInfo)getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        appMessageService.setReadyMsg(userInfo.getId());
        ResultVO resultVO = new ResultVO();
        return resultVO;
    }
}

