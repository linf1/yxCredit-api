package com.zw.miaofuspd.myset.controller;

import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.personal.service.AppUserService;
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
 * 获取合作平台
 */
@Controller
@RequestMapping("/platform")
public class CooperationController extends AbsBaseController {
    @Autowired
    private AppUserService appUserServiceImpl;
    @Autowired
    private ISystemDictService systemDictServiceImpl;

    @RequestMapping("/getCooperation")
    @ResponseBody
    public ResultVO getCooperation() throws Exception {
        ResultVO resultVO = new ResultVO();
        String host = systemDictServiceImpl.getInfo("background.url");
        List list = appUserServiceImpl.getCooperation();
        Map map = new HashMap<>();
        map.put("host",host);
        map.put("list",list);
        if (list!=null&&list.size()>0){
            resultVO.setRetData(map);
        }else{
            resultVO.setRetMsg(VOConst.FAIL);
        }
        return resultVO;
    }
}
