package com.zw.miaofuspd.api.sortmsg;

import com.api.model.BYXSettings;
import com.api.model.common.BYXRequest;
import com.api.model.common.BYXResponse;
import com.api.model.sortmsg.MsgRequest;
import com.api.model.sortmsg.MsgSettings;
import com.api.service.sortmsg.IMessageServer;
import com.base.util.DateUtils;
import com.base.util.TemplateUtils;
import com.zw.api.HttpClientUtil;
import com.zw.miaofuspd.mapper.AppMessageMapper;
import com.zw.pojo.AppMessage;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信服务实现
 * @author 陈清玉
 */
@Service(IMessageServer.BEAN_KEY)
public class MessageServerImpl extends AbsServiceBase implements IMessageServer {

    @Autowired
    private MsgSettings msgSettings;

    @Autowired
    private BYXSettings byxSettings;

    @Autowired
    private AppMessageMapper messageMapper;

    @Override
    public BYXResponse sendSms(MsgRequest request, Map<String,String> smsParam) throws Exception {
        Map<String,Object> parameter = new HashMap<>(5);
        parameter.put("phone",request.getPhone());
        parameter.put("type",msgSettings.getType());
        parameter.put("channelUniqId",msgSettings.getChannelUniqId());
        final String content = TemplateUtils.getContent(smsParam.get("content"), smsParam);
        parameter.put("content",content);
        final String result = HttpClientUtil.post(msgSettings.getRequestUrl(),BYXRequest.getBYXRequest(parameter, byxSettings), byxSettings.getHeadRequest());
        return  BYXResponse.getBYXResponse(result,byxSettings);

    }
    @Override
    public BYXResponse sendSms(MsgRequest request) throws Exception {
        Map<String,Object> parameter = new HashMap<>(5);
        parameter.put("phone",request.getPhone());
        parameter.put("type",msgSettings.getType());
        parameter.put("channelUniqId",msgSettings.getChannelUniqId());
        parameter.put("content",request.getContent());
        final String result = HttpClientUtil.post(msgSettings.getRequestUrl(),BYXRequest.getBYXRequest(parameter, byxSettings), byxSettings.getHeadRequest());
        return  BYXResponse.getBYXResponse(result,byxSettings);

    }

    @Override
    public Boolean sendLetter(AppMessage appMessage) {
        appMessage.setCreatTime(DateUtils.getCurrentTime());
        //未读状态
        appMessage.setState("1");
        appMessage.setPushState("0");
        appMessage.setJpushState("0");
        return messageMapper.insertSelective(appMessage) > 0;
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
