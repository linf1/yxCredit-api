package com.zw.miaofuspd.myset.controller;

import com.zw.miaofuspd.facade.myset.service.IAppMessageService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 获取首页轮播图
 */
@Controller
@RequestMapping("/carousel")
public class CarouselController extends AbsBaseController {
    @Autowired
    public IAppMessageService appMessageService;

    @RequestMapping("/getPictureInfo")
    @ResponseBody
    public ResultVO getPictureInfo(String type) throws Exception {
        ResultVO resultVO = new ResultVO();
        List list = appMessageService.getPictureInfo(type);
        if (list!=null&&list.size()>0){
            resultVO.setRetData(list);
        }else{
            resultVO.setRetMsg(VOConst.FAIL);
        }
        return resultVO;
    }

}
