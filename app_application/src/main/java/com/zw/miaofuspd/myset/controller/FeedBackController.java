package com.zw.miaofuspd.myset.controller;

import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.myset.service.FeedBackService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 意见反馈
 */
@Controller
@RequestMapping("/feedback")
public class FeedBackController extends AbsBaseController {
    @Autowired
    private FeedBackService feedBackServiceImpl;
    /**
     *保存用户反馈信息
     * @param content
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveFeedback")
    @ResponseBody
    public ResultVO save(String content) throws Exception{
        AppUserInfo userInfo = (AppUserInfo) getHttpSession().getAttribute(AppConstant.APP_USER_INFO);//获取用户信息
        ResultVO resultVO = new ResultVO();
        Map outMap = feedBackServiceImpl.feedbackAdd(userInfo, content);//调用反馈信息接口
        if (!(Boolean) (outMap.get("flag"))) {
            resultVO.setErrorMsg(VOConst.FAIL, (String) (outMap.get("msg")));
            return resultVO;
        }
        resultVO.setRetMsg((String) (outMap.get("msg")));
        return resultVO;
    }
}