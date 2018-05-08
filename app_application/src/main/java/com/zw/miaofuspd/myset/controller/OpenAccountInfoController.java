package com.zw.miaofuspd.myset.controller;

import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.personal.service.AppUserService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/13 0013.
 */
@Controller
@RequestMapping("/getOpenAccountInfo")
public class OpenAccountInfoController extends AbsBaseController {
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private IDictService iDictService;
    @Autowired
    private ISystemDictService iSystemDictService;
    /**
     *查询客户开户信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getOpenAccount")
    @ResponseBody
    public ResultVO getOpenAccountInfo() throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo)getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        Map map = appUserService.getAccountInfo(userInfo);
        String phone = iSystemDictService.getInfo("service.tel");
        if(!(boolean)map.get("flag")){
            resultVO.setErrorMsg(VOConst.FAIL,map.get("msg").toString());
        }
        map.remove("flag");
        map.put("phone",phone);
        resultVO.setRetData(map);
        return resultVO;
    }

}
