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
 * 推送放款及还款计划信息消息队列监听
 * @author 陈淸玉 create on 2018-07-19
 */
public class RepaymentQueueListener implements SessionAwareMessageListener {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private BYXSettings byxSettings;

    @Autowired
    private IRepaymentBusiness repaymentBusiness;


    @Override
    public void onMessage(Message message,Session session) {
        LOGGER.info("------------推送放款及还款计划信息消息队列处理开始--------------");
        try {
            if(message instanceof TextMessage) {
                TextMessage text = (TextMessage)message;
                LOGGER.info("推送放款及还款计划信息消息值：--------------{}",text.getText());
                BYXResponse byxResponse = BYXResponse.getBYXResponseJson(text.getText(), byxSettings);
                LOGGER.info("------------推送放款及还款计划信息消息值解析：{}--------------",byxResponse.getRes_data().toString());
                boolean isOk = repaymentBusiness.repaymentHandel(byxResponse);
                if(isOk){
                    LOGGER.info("------------推送放款及还款计划信息消息MQ处理成功--------------");
                }else{
                    //TODO 失败可把消息放到异常队列
                    //MessageProducer producer = session.createProducer(this.repaymentErrorQueue);
                    // producer.send(session.createTextMessage(text.getText()));
                    LOGGER.info("推送放款及还款计划信息消息MQ处理失败--------------");
                }


            }else{
                LOGGER.info("推送放款及还款计划信息消息:不支持的消息类型--------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("推送放款及还款计划信息消息异常信息:{}--------------",e.getMessage());
        }
    }

    /**
     * 处理异常队列
     */
//    private Destination repaymentErrorQueue;
//
//    public void setRepaymentErrorQueue(Destination repaymentErrorQueue) {
//        this.repaymentErrorQueue = repaymentErrorQueue;
//    }
}
