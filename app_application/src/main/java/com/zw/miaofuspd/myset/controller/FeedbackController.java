package com.zw.miaofuspd.myset.controller;

import com.base.util.AppRouterSettings;
import com.zw.miaofuspd.facade.myset.service.FeedBackService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**************************************************碧友信***************************************************************/

/**
 * 意见反馈
 */
@Controller
@RequestMapping(AppRouterSettings.VERSION+"/feedback")
public class FeedBackController extends AbsBaseController {
    @Autowired
    private FeedBackService feedBackServiceImpl;


    /**
     *保存用户反馈信息
     * @author 仙海峰
     * @param userId
     * @param content
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveFeedback")
    @ResponseBody
    public ResultVO save(String userId, String content) throws Exception{
        ResultVO resultVO = new ResultVO();

        //调用反馈信息接口
        Map outMap = feedBackServiceImpl.feedbackAdd(userId, content);
        resultVO.setRetData(outMap);
        return resultVO;
    }
}