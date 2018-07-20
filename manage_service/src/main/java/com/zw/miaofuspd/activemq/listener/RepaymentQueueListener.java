package com.zw.miaofuspd.activemq.listener;


import com.activemq.entity.respose.LoanDetailResponse;
import com.activemq.entity.respose.RepaymentListResponse;
import com.activemq.service.IRepaymentService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.api.model.BYXSettings;
import com.api.model.common.BYXResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 推送放款及还款计划信息消息队列监听
 * @author 陈淸玉 create on 2018-07-19
 */
public class RepaymentQueueListener implements MessageListener {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private BYXSettings byxSettings;

    @Autowired
    private IRepaymentService repaymentService;

    @Override
    public void onMessage(Message message) {
        LOGGER.info("推送放款及还款计划信息消息队列监听--------------");
        try {
            if(message instanceof TextMessage) {
                TextMessage text = (TextMessage)message;
                LOGGER.info("推送放款及还款计划信息消息值：--------------{}",text.getText());
                BYXResponse byxResponse = BYXResponse.getBYXResponseJson(text.getText(), byxSettings);
                repaymentService.repaymentHandel(byxResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
