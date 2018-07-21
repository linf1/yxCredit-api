package com.api.service.sortmsg;

import com.api.model.common.BYXResponse;
import com.api.model.sortmsg.MsgRequest;
import com.zw.pojo.AppMessage;

import java.io.IOException;
import java.util.Map;

/**
 * 短信服务
 * @author 陈清玉
 */
public interface IMessageServer {

    String  BEAN_KEY = "messageServerImpl";
    /**
     * 短信发送
     * @param request 短信请求参数
     * @return json 字符串
     * @throws IOException 短信发送异常
     */
    BYXResponse sendSms(MsgRequest request, Map<String, String> smsParam) throws Exception;


    /**
     * 短信发送
     * @param request 短信请求参数
     * @return json 字符串
     * @throws IOException 短信发送异常
     */
    BYXResponse sendSms(MsgRequest request) throws Exception;

    /**
     * 发送站内信息
     * @param appMessage 信息对象
     * @return 结果
     */
    Boolean sendLetter(AppMessage appMessage);

}
