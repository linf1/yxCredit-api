package com.zw.miaofuspd.task;

import com.zw.service.task.abs.AbsTask;

/**
 * 还款提醒推送任务
 * @author 陈清玉 create on 2018-07-17
 */
public class RepaymentPushTask extends AbsTask {

    @Override
    public void doWork(){
        System.out.println("还款提醒及还款结果消息只推送站内信消息");
    }
}
