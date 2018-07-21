package com.zw.miaofuspd.activemq.listener;


import com.api.model.BYXSettings;
import com.api.model.common.BYXResponse;
import com.api.service.ds.IAssetServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 接受有信贷资产信息消息队列监听
 * @author 韩梅生 create on 2018-07-21
 */
public class AssetQueueListener implements MessageListener {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private BYXSettings byxSettings;

    @Autowired
    private IAssetServer iAssetServer;

    @Override
    public void onMessage(Message message) {
        LOGGER.info("推送‘接受有信贷资产’返回信息--------------");
        try {
            if(message instanceof TextMessage) {
                TextMessage text = (TextMessage)message;
                LOGGER.info("推送‘接受有信贷资产'信息的返回值：--------------{}",text.getText());
                BYXResponse byxResponse = BYXResponse.getBYXResponseJson(text.getText(), byxSettings);
                iAssetServer.updateAssetStatus(byxResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
