package com.zw.miaofuspd.login.controller;

import com.base.util.AppRouterSettings;
import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.miaofuspd.facade.user.service.ILoginService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(AppRouterSettings.VERSION + "/login")
public class LoginController extends AbsBaseController {
    @Autowired
    ILoginService loginService;

    @Autowired
    private AppBasicInfoService appBasicInfoService;

    public String getRemortIP() {
        if (getRequest().getHeader("x-forwarded-for") == null) {
            return getRequest().getRemoteAddr();
        }
        return getRequest().getHeader("x-forwarded-for");
    }

    /**
     * 秒付登录
     * @param phone 登录电话
     * @param type 电话类型
     * @param deviceCode 设备唯一吗
     * @return 用户信息
     * @throws Exception
     */
    @RequestMapping("/loginEntry")
    @ResponseBody
    public ResultVO loginEntry(@RequestParam String phone,@RequestParam String type, @RequestParam String deviceCode) throws Exception {
        //获取客户端的Ip地址
        String ipAddress = getRemortIP();
        ResultVO resultVO = this.createResultVO(null);
        Map map = loginService.login(phone,type,ipAddress,deviceCode);
        boolean flag = (boolean) map.get("success");
        String msg = (String) map.get("msg");
        Map returnMap = new HashMap(3);
        if (flag) {
            AppUserInfo user = new AppUserInfo();
            user.setId(map.get("phone") + "");
            user.setTel(map.get("id") + "");
            user.setImg_url(map.get("img_url") + "");
            Map map2 = new HashMap(4);
            String token = map.get("token")+"";
            map2.put ("token",token);
            map2.put("token_time",map.get("token_time"));
            getRequest().getSession().setAttribute(AppConstant.APP_USER_INFO, user);
            returnMap.put("userId",map.get("id"));
            returnMap.put("phone",phone);
            returnMap.put("imgUrl",map.get("img_url"));
            returnMap.put("token",token);
            returnMap.put("sessionId",getRequest().getSession().getId());
            resultVO.setRetData(returnMap);
        } else {
            resultVO.setErrorMsg(VOConst.FAIL, msg);
            resultVO.setRetData(map);
        }
        return resultVO;
    }
}