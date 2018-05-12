package com.zw.api.sms;

import com.api.service.sortmsg.IMessageServer;
import com.base.util.RandomUtil;
import com.base.util.StringUtils;
import com.zw.app.util.AppConstant;
import com.api.model.sortmsg.MsgRequest;
import com.zw.web.base.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信接口控制器
 * @author 陈清玉
 */
@RestController
@RequestMapping("/api/sms")
public class SmsApiController {

    private final Logger LOGGER = LoggerFactory.getLogger(SmsApiController.class);

    @Autowired
    private IMessageServer messageServer;

    @RequestMapping("/sendMsg")
    public void sendMsg(HttpServletRequest request, MsgRequest msgRequest){
        //生成6位数随机数
        final String smsCode = RandomUtil.createRandomCharData(6);
        Map<String,String> parameters = new HashMap<>(2);
        parameters.put("company","碧友信");
        parameters.put("smsCode",smsCode);
        request.getSession().setAttribute(AppConstant.SMS_KEY,smsCode);
        try {
            final String result =  messageServer.sendSms(msgRequest,parameters);
            if (result != null) {
                LOGGER.info("接口发送成功",result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/checkMsg/{smsCode}")
    public ResultVO checkMsg(HttpServletRequest request,@PathVariable String smsCode){
        final Object smsKey = request.getSession().getAttribute(AppConstant.SMS_KEY);
        if(StringUtils.isEmpty(smsCode)) {
            return ResultVO.error("参数为空");
        }
        if(!smsCode.equals(smsKey)){
            return ResultVO.error("手机验证码发送失败或超时间");
        }
        return ResultVO.ok("验证通过");
    }


}
