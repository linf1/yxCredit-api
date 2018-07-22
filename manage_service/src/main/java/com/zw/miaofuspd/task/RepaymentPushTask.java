package com.zw.miaofuspd.task;

import com.api.service.sortmsg.IMessageServer;
import com.base.util.DateUtils;
import com.base.util.TemplateUtils;
import com.enums.DictEnum;
import com.enums.RepaymentStatusEnum;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.facade.pojo.MagOrder;
import com.zw.pojo.AppMessage;
import com.zw.pojo.BusinessRepayment;
import com.zw.service.IBusinessRepaymentService;
import com.zw.service.task.abs.AbsTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 还款提醒推送任务
 * @author 陈清玉 create on 2018-07-17
 */
public class RepaymentPushTask extends AbsTask {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private IBusinessRepaymentService businessRepaymentService;

    @Autowired
    private IMessageServer messageServer;

    @Autowired
    private IDictService dictService;

    @Autowired
    private AppOrderService appOrderService;
    /**
     * 预计还款时间减去的天数（如：1号放款，借款期限30天，那么最后正常还款时间为（30-7），逾期的第一天是（30+1））
     */
    private final int day = 7;

    private MagOrder order ;

    @Override
    public void doWork(){
        try {
            String currentTime = DateUtils.getCurrentTime(DateUtils.STYLE_2);
            Date currentDate = DateUtils.strConvertToDate(currentTime,DateUtils.STYLE_2);
            List<BusinessRepayment> infoByStatus = businessRepaymentService.findRepaymentInfoByStatus(RepaymentStatusEnum.REPAYMENTS.getCode());
            if(!CollectionUtils.isEmpty(infoByStatus)){
                for (BusinessRepayment info : infoByStatus) {
                    //正常还款最后一天
                    Date normalDate = DateUtils.getSpecifyDate(info.getRepaymentTime(), -day);
                    //逾期还款第一天
                    Date exceedDate = DateUtils.getSpecifyDate(info.getRepaymentTime(), 1);

                    if (normalDate.getTime() == currentDate.getTime()) {
                        String normalTemplate  = dictService.getDictInfo(DictEnum.JJDQ.getName(),DictEnum.JJDQ.getCode());
                        order = appOrderService.getOrderByNo(info.getOrderNo());
                        Map<String,String> parameter = new HashMap<>(5);
                        parameter.put("lastDate",DateUtils.getDateString(info.getRepaymentTime(),DateUtils.STYLE_2));
                        parameter.put("title","正常还款提醒");
                        sendMessage(normalTemplate,parameter);
                        LOGGER.info("订单ID为:{}马上就到期：推送站内信息------------",info.getOrderNo());

                    }else if(exceedDate.getTime() == currentDate.getTime() ){
                        String exceedTemplate  = dictService.getDictInfo(DictEnum.YYQ.getName(),DictEnum.YYQ.getCode());
                        order = appOrderService.getOrderByNo(info.getOrderNo());
                        Map<String,String> parameter = new HashMap<>(5);
                        parameter.put("lastDate",DateUtils.getDateString(info.getRepaymentTime(),DateUtils.STYLE_2));
                        parameter.put("title","逾期提醒");
                        sendMessage(exceedTemplate,parameter);
                        LOGGER.info("订单ID为:{}已经逾期：推送站内信息------------",info.getOrderNo());
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendMessage(String template , Map<String,String> parameter){
            if(StringUtils.isEmpty(template)){
                LOGGER.info("--------缺少短信模板配置-----------");
                return;
            }
            parameter.put("applayMoney",order.getApplayMoney().toString());
            parameter.put("periods",order.getPeriods());
            parameter.put("productName","蓝领贷");
            String content = TemplateUtils.getContent(template, parameter);
            AppMessage appMessage = new AppMessage();
            appMessage.setCreatTime(DateUtils.getCurrentTime());
            appMessage.setContent(content);
            appMessage.setTitle(parameter.get("title"));
            appMessage.setUserId(order.getUserId());
            //发送站内信
            messageServer.sendLetter(appMessage);
    }
}
