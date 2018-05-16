package com.zw.api.sms.controller;

import com.api.model.common.BYXResponse;
import com.api.service.sortmsg.IMessageServer;
import com.base.util.AppRouterSettings;
import com.base.util.RandomUtil;
import com.base.util.StringUtils;
import com.api.model.sortmsg.MsgRequest;
import com.zw.miaofuspd.facade.user.service.ISmsService;
import com.zw.web.base.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信接口控制器
 * @author 陈清玉
 */
@RestController
@RequestMapping(AppRouterSettings.VERSION + AppRouterSettings.API_MODULE + "/sms")
public class SmsApiController  {

    private final Logger LOGGER = LoggerFactory.getLogger(SmsApiController.class);

    @Autowired
    private IMessageServer messageServer;

    @Autowired
    private ISmsService smsService;


    /**
     * 短信发送接口
     * @param msgRequest 请求参数
     * @return
     */
    @PostMapping("/sendMsg")
    public ResultVO sendMsg (MsgRequest msgRequest){
        //生成6位数随机数
        final String smsCode = RandomUtil.createRandomVcode(6);
        Map<String,String> parameters = new HashMap<>(2);
        parameters.put("smsCode",smsCode);
        msgRequest.setSmsCode(smsCode);
        try {
            final BYXResponse byxResponse = messageServer.sendSms(msgRequest, parameters);
            if (byxResponse != null) {
                smsService.saveSms(msgRequest);
                LOGGER.info("短信发送成功",byxResponse.toString());
                return ResultVO.ok(byxResponse.getRes_msg(),null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResultVO.error(e.getMessage());
        }
       return ResultVO.error();
    }

    /**
     * 验证手机验证码
     * @param msgRequest
     * @return
     */
    @PostMapping("/checkMsg")
    public ResultVO checkMsg(MsgRequest msgRequest){
        try {
            if(msgRequest == null || StringUtils.isEmpty(msgRequest.getSmsCode())) {
                return ResultVO.error("参数为空");
            }
            //先查询验证码和用户是否正确
            Map inMap=new HashMap();
            inMap.put("tel",msgRequest.getPhone());
            inMap.put("smsCode",msgRequest.getSmsCode());
            inMap.put("errortime","600");
            final Map resData  = smsService.checkSms(inMap);
            if(resData != null){
                if((Boolean) resData.get("flag")){
                    return   ResultVO.ok("验证成功",null);
                }else{
                    return   ResultVO.error(resData.get("msg").toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage(),null);
        }
        return ResultVO.error("验证失败");
    }
//    @RequestMapping("captcha.jpg")
//    public void captcha(HttpServletResponse response,HttpServletRequest request)throws ServletException, IOException {
//        response.setHeader("Cache-Control", "no-store, no-cache");
//        response.setContentType("image/jpeg");
//
//        //生成文字验证码
//        String text = producer.createText();
//        //生成图片验证码
//        BufferedImage image = producer.createImage(text);
//        //保存到shiro session
//        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, text);
//        ServletOutputStream out = response.getOutputStream();
//        ImageIO.write(image, "jpg", out);
//        out.flush();
//    }

}
