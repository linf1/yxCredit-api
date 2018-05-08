package com.zw.miaofuspd.AutomaticRepayment.task;

import com.zw.api.SendSmsApi;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.service.task.abs.AbsTask;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/29 0029.
 */
public class RepayRemindTask extends AbsTask {
    @Autowired
    private ISystemDictService iSystemDictService;

    @Override
    public void doWork() throws Exception {
        logger.info("定时跑批还款提醒执行开始");
        Map smsMap = new HashMap();//短信参数
        String host = iSystemDictService.getInfo("sms.app_sms_url");//短信地址
        String account = iSystemDictService.getInfo("sms.app_sms_account");//短信模板。数据字典拿
        String password = iSystemDictService.getInfo("sms.app_sms_password");//短信模板。数据字典拿
        smsMap.put("account",account);
        smsMap.put("password",password);
        //order_type 为2表示商品贷,为1表示金棒棒
        String remindSql = "SELECT o.order_no as orderNo,o.customer_id as customerId,o.tel,date_format(r.pay_time,'%Y-%m-%d')as payTime FROM " +
                "mag_order o JOIN mag_repayment r ON r.order_id = o.ID " +
                "WHERE r.state = '1' and r.pay_time<=(NOW()-INTERVAL 5 DAY) and o.state = '5' AND o.commodity_state IN ('19', '20') AND o.order_type = '2' ";
        List remindList = sunbmpDaoSupport.findForList(remindSql);
        if(remindList!=null && remindList.size()>0){
            for(int i=0;i<remindList.size();i++){
                Map remindMap =  (Map)remindList.get(i);
                String orderNo = (String) remindMap.get("orderNo");//订单号
                String payTime = (String) remindMap.get("payTime");//还款日期
                String customerId = (String) remindMap.get("customerId");//客户id
                String tel = (String) remindMap.get("tel");//客户手机号码
                //channel 为0,表示金棒棒,为1代表的是商品贷开户,state表示正常账户
                String accountSql = "select SUBSTRING(bank_card,-4) as bankCard from mag_customer_account where channel='1'and state='0' and customer_id = '"+customerId+"'";
                List accountList = sunbmpDaoSupport.findForList(accountSql);
                String backCard = "";
                if(accountList!=null && accountList.size()>0){
                    Map accountMap = (Map) accountList.get(0);
                    backCard = accountMap.get("bankCard").toString();//银行卡号
                }
                String smsSql = "select sms_content,sms_name from sms_manage where sms_rule='9' and platform_type='1' and sms_state='1'";
                Map map = sunbmpDaoSupport.findForMap(smsSql);
                String content = map.get("sms_content").toString().replace("$orderNo$",":"+orderNo+"").replace("$time$",""+payTime+"").replace("$backCode$",""+backCard+"");
                smsMap.put("tel",tel);
                smsMap.put("msg",content);
                SendSmsApi.sendSms(host,smsMap);
            }
        }
    }
}
