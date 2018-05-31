package com.zw.miaofuspd.personnal.controller;

import com.api.model.common.BYXResponse;
import com.api.service.ds.IDSMoneyBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 异步借款人及放款账户数据同步
 * @author 陈清玉
 */
public class DSMoneyRunable implements Runnable {

    private final Logger LOGGER = LoggerFactory.getLogger(DSMoneyRunable.class);

    private IDSMoneyBusiness idsMoneyBusiness;

    private String orderId;
    /**
     * 是否完成
     */
    private boolean done = false;

    public DSMoneyRunable(IDSMoneyBusiness idsMoneyBusiness,String orderId) {
        this.idsMoneyBusiness = idsMoneyBusiness;
        this.orderId = orderId;
    }

    /**
     * 获取魔盒数据
     * @throws Exception 异常
     */
    private void doRun() throws Exception {
        BYXResponse byxResponse = idsMoneyBusiness.syncData(this.orderId);
        if(byxResponse != null) {
            if (BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
                    //数据获取成功就不在轮询
                    this.done = true;
                    LOGGER.info("----异步借款人及放款账户数据同步成功----");
                }
            }
        if(!this.done) {
            //每次如果没有查询到数据就休眠
            doSleep();
        }
    }

    /**
     * 休眠
     * @throws InterruptedException 休眠异常
     */
    private void doSleep() throws InterruptedException {
        //休眠时间
        final int sleepMillis  = 5000;
        Thread.sleep(sleepMillis);
    }

    @Override
    public void run()  {
        //重试次数
        final int countNum = 5;
        int i = 0;
        //如果没有获得数据就继续查询数据
        do {
            LOGGER.info("----异步借款人及放款账户数据同步：{}次----", (i+1));
            if(i == countNum){return;}
            try {
                doRun();
            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }while (!done);
    }
}
