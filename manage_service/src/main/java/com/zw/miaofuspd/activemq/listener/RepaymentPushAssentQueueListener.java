package com.zw.miaofuspd.activemq.listener;


import com.activemq.service.IRepaymentBusiness;
import com.api.model.BYXSettings;
import com.api.model.common.BYXResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.*;

/**
 * 网贷发生还款，正常还款 or 提前还款消息队列监听
 * @author 陈淸玉 create on 2018-07-19
 */
public class RepaymentPushAssentQueueListener implements SessionAwareMessageListener {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String LOG_TITLE = "网贷发生还款，正常还款 or 提前还款";

    @Autowired
    private BYXSettings byxSettings;

    @Autowired
    private IRepaymentBusiness repaymentBusiness;

    @Override
    public void onMessage(Message message,Session session) {
        LOGGER.info("------------{}消息队列处理开始--------------",LOG_TITLE);
        try {
            if(message instanceof TextMessage) {
                TextMessage text = (TextMessage)message;
                LOGGER.info("{}消息值：{}--------------",LOG_TITLE,text.getText());
                BYXResponse byxResponse = BYXResponse.getBYXResponseJson(text.getText(), byxSettings);
                LOGGER.info("{}消息解析后值：{}--------------",LOG_TITLE,byxResponse.getRes_data().toString());
                boolean isOk = repaymentBusiness.repaymentPushAssentHandel(byxResponse);
                //如果失败放入异常队列
                if(!isOk){
                    //TODO 失败可把消息放到异常队列
                    //MessageProducer producer = session.createProducer(this.repaymentPushAssentErrorQueue);
                    //producer.send(session.createTextMessage(text.getText()));
                    LOGGER.info("{}消息MQ处理失败--------------",LOG_TITLE);
                }
            }else{
                LOGGER.info("{}:不支持的消息类型--------------",LOG_TITLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("{}异常信息:{}--------------",LOG_TITLE,e.getMessage());
        }
    }

    /**
     * 处理异常队列
     */
    private Destination repaymentPushAssentErrorQueue;

    public void setRepaymentPushAssentErrorQueue(Destination repaymentPushAssentErrorQueue) {
        this.repaymentPushAssentErrorQueue = repaymentPushAssentErrorQueue;
    }
}
