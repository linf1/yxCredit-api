package com.zw.miaofuspd.red.controller;

import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.red.service.RedService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
@Controller
@RequestMapping("/red")
public class RedController extends AbsBaseController {
    @Autowired
    public RedService redService;
    /**
     * @return
     * @throws Exception
     */
    @RequestMapping("/getRedList")
    @ResponseBody
    public ResultVO getRedList() throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) this.getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        Map map = redService.getRedList(userInfo);
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     * 秒付金服红包未提现
     * @return
     * @throws Exception
     */
    @RequestMapping("/getUnUserList")
    @ResponseBody
    public ResultVO getUnUserList() throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) this.getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        Map map = redService.getUnUserList(userInfo);
        resultVO.setRetData(map);
        return resultVO;
    }
}
