package com.zw.miaofuspd.api.sortmsg;

import com.api.model.sortmsg.MsgRequest;
import com.api.model.sortmsg.MsgSettings;
import com.api.service.sortmsg.IMessageServer;
import com.base.util.TemplateUtils;
import com.zw.api.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信服务实现
 */
public class MessageServerImpl implements IMessageServer {

    @Autowired
    private MsgSettings msgSettings;

    @Override
    public String sendSms(MsgRequest request, Map<String,String> smsParam) throws IOException {
        Map<String,Object> parameter = new HashMap<>(5);
        parameter.put("phone",request.getPhone());
        parameter.put("content",getContent());
        parameter.put("type",msgSettings.getType());
        parameter.put("channelUniqId",msgSettings.getChannelUniqId());
        parameter.put("templateCode",msgSettings.getTemplateCode());
        TemplateUtils.getContent(msgSettings.getContent(),smsParam);
        return HttpUtil.doPost(msgSettings.getRequestUrl(),parameter);
    }

    /**
     * 根据模板生成短信内容
     * @return
     */
    private String getContent(){
        Map<String,String> parameters = new HashMap<>();
        parameters.put("company","碧友信");
        parameters.put("smsCode","1234");
        return TemplateUtils.getContent(msgSettings.getContent(),parameters);
    }

}
