package com.zw.miaofuspd.timedTask.service;

import com.base.util.DateUtils;
import com.base.util.StringUtils;
import com.base.util.TraceLoggerUtil;
import com.zhiwang.zwfinance.app.jiguang.util.JiGuangUtils;
import com.zw.api.SendSmsApi;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.log.entity.OrderLogBean;
import com.zw.miaofuspd.facade.user.service.IUserService;
import com.zw.service.task.abs.AbsTask;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/12/11.
 */
public class InterestReductionTask extends AbsTask {
    /**
     * 定时利息减免计算
     *
     * @throws Exception
     */

    @Autowired
    private ISystemDictService iSystemDictService;

    @Autowired
    private IDictService iDictService;

    @Override
    public void doWork() throws Exception {
        //逾期计算
        overdueCalculationUpdate();
        //减免数据更新
        derateUpdate();
        //结清数据更新
        settleUpdate();
        //已还 未还 减免 罚息 统计
        sunUpdate();
    }

        //已还 未还 减免 罚息 统计
    public   void sunUpdate(){
        TraceLoggerUtil.info("统计计算跑批----------------------------------------------------开始");
        String orderSql=" select id from mag_order where commodity_state='20' and order_type='2' ";
        List orders = sunbmpDaoSupport.findForList(orderSql);
        double penaltyTotal=0;//罚息总额
        double derateAmount=0;//减免金额
        double returnedAmount=0;//已还款金额
        double unpaidAmount=0;//未还款金额
        double redAmount=0;//抵扣红包金额
        double servicePackageAmount=0;//服务包金额
        double settleAmount=0;//非正常结清金额
        String orderId="";
        String repaymentId="";
        DecimalFormat df  = new DecimalFormat("######0.00");
        try {
            for (int i=0;i<orders.size();i++){
                Map<String,Object> order=(Map) orders.get(i);
                orderId=order.get("id").toString();
                penaltyTotal=0;
                derateAmount=0;
                returnedAmount=0;
                unpaidAmount=0;
                String repaymentSql="SELECT  mr.id,mr.loan_id AS loanId ,mr.pay_count AS payCount,mr.fee,mr.amount,mr.rate,mr.repayment_amount AS repaymentAmount ,mr.order_id AS orderId,mr.manageFee,mr.quickTrialFee,mr.loan_time AS loanTime,mr.pay_time AS payTime,mr.overdue_days AS overdueDays,mr.penalty,mr.create_time AS createTime,mr.state,mr.remark,mr.settle_type as settleType, md.id,md.derate_amount,md.approval_date,md.approval_state,mr.actual_amount AS actualAmount,mr.actual_time AS  actualTime,md.derate_amount AS derateAmount,mr.settle_type  FROM mag_repayment mr LEFT JOIN mag_derate md ON md.repayment_id=mr.id AND  md.state IN ('1','3')    WHERE order_id ='"+orderId+"' and mr.state !='0'";
                List  repaymentList=sunbmpDaoSupport.findForList(repaymentSql);
                Map<String,Object> repayment=new HashedMap();
                //LoanManage loanManage=new LoanManage();
                //获取已经使用的非正常结清数据
                String settleSql="select settle_amount,settle_fee  from mag_settle_record where settle_type='1' and state='2' and order_id='"+orderId+"'";
                List  settleList=sunbmpDaoSupport.findForList(settleSql);
                for(int j=0;j<repaymentList.size();j++){
                    try {
                        repayment=(Map) repaymentList.get(j);
                        repaymentId=repayment.get("id").toString();
                        String settleType=repayment.get("settleType").toString();//结清状态
                        //非正常结清
                        if ("2".equals(settleType)){
                            if(settleList.size()>0){
                                settleAmount= Double.valueOf(((Map)(settleList.get(0))).get("settle_amount").toString());
                            }
                        }else {
                            //获取服务包金额
                            String servicePackage="select sum(amount) as amount from service_package_repayment where repayment_id='"+repayment.get("id")+"'";
                            List servicePackageList=sunbmpDaoSupport.findForList(servicePackage);
                            if (servicePackageList.size()>0){
                                if(servicePackageList.get(0)!=null) {
                                    servicePackageAmount = Double.valueOf(((Map) (servicePackageList.get(0))).get("amount").toString());
                                }
                            }
                            //未还  逾期
                            if ("1".equals(repayment.get("state").toString()) || "3".equals(repayment.get("state").toString())) {
                                //应还金额（本金+利息）+服务包
                                unpaidAmount = unpaidAmount + Double.valueOf(repayment.get("repaymentAmount").toString())+servicePackageAmount;
                            }//已还
                            else if ("2".equals(repayment.get("state").toString())) {
                                //已还金额（本金+利息）+服务包
                                returnedAmount = returnedAmount + Double.valueOf(repayment.get("repaymentAmount").toString())+servicePackageAmount;
                                derateAmount = derateAmount + Double.valueOf(repayment.get("derateAmount")==""?"0":(repayment.get("derateAmount").toString()));
                                penaltyTotal = penaltyTotal + Double.valueOf(repayment.get("penalty").toString());
                                //获取减免抵扣的红包金额
                                String redSql="select money  from mag_red_info where  repayment_id='"+repayment.get("id")+"'";
                                List redList=sunbmpDaoSupport.findForList(redSql);
                                if (redList.size()>0){
                                    for(int k=0;k<redList.size();k++){
                                        Map redMap=(Map)redList.get(k);
                                        redAmount+=Double.valueOf(redMap.get("money").toString());
                                    }
                                }
                            }
                        }
                    }catch (Exception e){
                        TraceLoggerUtil.info("还款计划统计，订单编号"+orderId+"还款计划表编号"+repayment.get("id")+"详细信息"+e.toString());
                    }
                }
                //已还金额+提前结清金额
                returnedAmount=returnedAmount+settleAmount;
                String loanManageSql="update mag_loan set penalty='"+df.format(penaltyTotal)+"', returned_amount='"+df.format(returnedAmount)+"',unpaid_amount='"+df.format(unpaidAmount)+"',derate_amount='"+df.format(derateAmount)+"',red_amount='"+df.format(redAmount)+"'  where  order_id='"+orderId+"'" ;
                sunbmpDaoSupport.exeSql(loanManageSql);
            }
        }catch (Exception e){
            TraceLoggerUtil.info("还款计划统计失败，订单编号"+orderId+"还款计划表编号"+repaymentId+"详细信息"+e.toString());
        }
        TraceLoggerUtil.info("统计计算跑批----------------------------------------------------结束");
    }

    //逾期计算
    private void overdueCalculationUpdate() {
        TraceLoggerUtil.info("逾期计算跑批----------------------------------------------------开始");
        //获取当前日期 并设置时分为0
        Date Today=new Date();
        Today.setHours(0);
        Today.setMinutes(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String orderSql=" select id,product_detail_code,Yuqi_fee from mag_order where  order_type='2' ";
        List orders = sunbmpDaoSupport.findForList(orderSql);
        String dictSql="select code,name from zw_sys_dict where parent_id IN (select id from zw_sys_dict where CODE='yqsfgz')";
        List dict=sunbmpDaoSupport.findForList(dictSql);
        double drfdfx=0;//单日罚息封顶金额
        double fxjefxzz=0;//罚息计算区间罚息种子
        double fxjezz=0;//罚息计算金额区间种子
        double yqfyfd=0;//逾期费最低额
        int yfts=0;//逾期费、罚息优惠免收天数

        try {
            for(int i=0;i<dict.size();i++){
                Map<String,Object> map=(Map<String,Object> )dict.get(i);
                if("drfdfx".equals(map.get("code").toString())){
                    drfdfx=Double.parseDouble(map.get("name").toString());
                }
                if("fxjefxzz".equals(map.get("code").toString())){
                    fxjefxzz=Double.parseDouble(map.get("name").toString());
                }
                if("fxjezz".equals(map.get("code").toString())){
                    fxjezz=Double.parseDouble(map.get("name").toString());
                }
                if("yqfyfd".equals(map.get("code").toString())){
                    yqfyfd=Double.parseDouble(map.get("name").toString());
                }
                if("yfts".equals(map.get("code").toString())){
                    yfts=Integer.parseInt(map.get("name").toString());
                }
            }
        }catch (Exception e){
            TraceLoggerUtil.info("罚息配置数据出现问题，请重新配置数据");
            return;
        }
        double penaltyTotal=0;//罚息总额
        int OverdueDays=0;//逾期最大天数
        double penaltyCost=0;//单笔逾期费
        double penalty=0;//单日罚息
        double total=0;//应还总额
        double servicePackMoney=0;
        String orderId="";
        String repaymentId="";
        for (int i=0;i<orders.size();i++){
            Map<String,Object> order=(Map) orders.get(i);
            orderId=order.get("id").toString();
           //新加的部分
            String packageSql="select amount_collection from mag_servicepag_order where order_id='"+orderId+"' and state ='0' and type='2'";
            Map packageMap=sunbmpDaoSupport.findForMap(packageSql);
            if(packageMap!=null){
                servicePackMoney=Double.valueOf(packageMap.get("amount_collection").toString());
            }else{
                servicePackMoney=0;
            }
            penaltyTotal=0;
            OverdueDays=0;
            String repaymentSql="SELECT  mr.id,mr.loan_id AS loanId ,mr.pay_count AS payCount,mr.fee,mr.amount,mr.rate,mr.repayment_amount AS repaymentAmount ,mr.order_id AS orderId,mr.manageFee,mr.quickTrialFee,mr.loan_time AS loanTime," +
                    "mr.pay_time AS payTime,mr.overdue_days AS overdueDays,mr.penalty,mr.create_time AS createTime,mr.state,mr.remark,md.id,md.approval_date,md.approval_state,mr.actual_amount AS actualAmount,mr.actual_time AS  actualTime,md.derate_amount AS derateAmount  FROM mag_repayment mr LEFT JOIN mag_derate md ON md.repayment_id=mr.id AND  md.state IN ('1','3')    WHERE mr.order_id ='"+orderId+"' and mr.state !='0'";
            List  repaymentList=sunbmpDaoSupport.findForList(repaymentSql);
            Map<String,Object> repayment=new HashedMap();
            repaymentId="";
            try {
                for(int j=0;j<repaymentList.size();j++){
                    repayment=(Map) repaymentList.get(j);
                    if ("2".equals(repayment.get("state").toString()) || "4".equals(repayment.get("state").toString()))
                    {
                        continue;
                    }
                    try {
                        penaltyCost=0;
                        penalty=0;
                        repaymentId=repayment.get("id").toString();
                        String payTime=repayment.get("payTime").toString();
                        payTime =payTime.substring(0, 4) + "-" + payTime.substring(4, 6) + "-" + payTime.substring(6, 8);
                        Date strDate=sdf.parse(payTime);
                        int days = (int) ((Today.getTime() - strDate.getTime()) / (1000*3600*24));
                        if (days==1){
                            try {
                                Map smsMap = new HashMap();//短信参数
                                String host = iSystemDictService.getInfo("sms.app_sms_url");//短信地址
                                String account = iSystemDictService.getInfo("sms.app_sms_account");//短信模板。数据字典拿
                                String password = iSystemDictService.getInfo("sms.app_sms_password");//短信模板。数据字典拿
                                smsMap.put("account",account);
                                smsMap.put("password",password);
                                String customerId="";
                                String tel="";
                                String backCard = "";
                                String customerSql="select id,tel from  mag_customer where id IN (select CUSTOMER_ID from mag_order  where id='"+orderId+"')";
                                List customerList=sunbmpDaoSupport.findForList(customerSql);
                                if (customerList!=null&&customerList.size()>0){
                                    Map customerMap = (Map) customerList.get(0);
                                    customerId=customerMap.get("id").toString();
                                    tel=customerMap.get("tel").toString();
                                }
                                String accountSql = "select SUBSTRING(bank_card,-4) as bankCard from mag_customer_account where channel='1'and state='0' and customer_id ='"+customerId+"'";
                                List accountList = sunbmpDaoSupport.findForList(accountSql);
                                if(accountList!=null && accountList.size()>0){
                                    Map accountMap = (Map) accountList.get(0);
                                    backCard = accountMap.get("bankCard").toString();//银行卡号
                                }
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String dateString = dateFormat.format(new Date());
                                String smsSql = "select sms_content,sms_name from sms_manage where sms_rule='10' and platform_type='1' and sms_state='1'";
                                Map map = sunbmpDaoSupport.findForMap(smsSql);
                                String content = map.get("sms_content").toString().replace("$date$",dateString).replace("$days$",days+"").replace("$backCode$",""+backCard+"");
                                smsMap.put("tel",tel);
                                smsMap.put("msg",content);
                                SendSmsApi.sendSms(host,smsMap);
                            }catch (Exception e){
                                TraceLoggerUtil.info("短息发送异常---------");
                            }
                        }
                        //逾期计算
                        if (days>0&&(!"2".equals(repayment.get("state").toString()))){
                            total=total+Double.valueOf(repayment.get("repaymentAmount").toString());
                            //逾期费=应还总额*Yuqi_fee（百分比）*0.01
                           // penaltyCost=Double.valueOf(repayment.get("amount").toString())*0.01*Double.valueOf(order.get("Yuqi_fee").toString());
                            penaltyCost=(Double.valueOf(repayment.get("repaymentAmount").toString())+servicePackMoney)*0.01*Double.valueOf(order.get("Yuqi_fee").toString());
                            //逾期费最低额限制
                            if (penaltyCost<yqfyfd){
                                penaltyCost=yqfyfd;
                            }
                            //单日罚息
                            int count=(int)(Double.valueOf((Double.valueOf(repayment.get("repaymentAmount").toString())+servicePackMoney))/fxjezz);
                            if ((Double.valueOf(Double.valueOf(repayment.get("repaymentAmount").toString())+servicePackMoney)%fxjezz)>0){
                                count++;
                            }
                            //单日罚息
                            penalty=fxjefxzz*count;
                            //单日罚息最高额限制
                            if(penalty>drfdfx){
                                penalty=drfdfx;
                            }
                            //逾期减免天数
                            if (days<=yfts){
                                penalty=0;
                                penaltyCost=0;
                            }
                            penalty=penalty*days;
                            String penaltySql="insert  into  mag_repayment_penalty(id,order_id,repayment_id,penalty,penalty_formula,create_time) values('"+ UUID.randomUUID().toString()+"','"+orderId+"','"+repaymentId+"','"+penalty+"','应还款金额:"+repayment.get("amount").toString()+";罚息金额种子fxjezz："+fxjezz+";罚息种子fxjefxzz："+fxjefxzz+";减免天数yfts："+yfts+";当前逾期天数days："+days+";逾期费最低限额yqfyfd："+yqfyfd+";fxjefxzz："+fxjefxzz+"','"+ DateUtils.getDateString(new Date())+"')";
                            sunbmpDaoSupport.exeSql(penaltySql);
                            //Map<String,Object> map=(Map<String,Object>)sunbmpDaoSupport.findForMap("select SUM(penalty) as sum from mag_repayment_penalty  where  repayment_id='"+repaymentId+"'");
//                            if (map.size()>0){
//                                penalty+=Double.valueOf(map.get("sum").toString());
//                            }
                           // penaltyCost=penaltyCost+penalty;
                            DecimalFormat df  = new DecimalFormat("######0.00");
                            //penaltyTotal=penaltyTotal+penaltyCost+penalty;

//                            if (days>OverdueDays){
//                                OverdueDays=days;
//                            }
                            String repaymentUpdateSql="update mag_repayment set state='3',overdue_days='"+Integer.toString(days)+"',penalty='"+df.format(penaltyCost)+"',default_interest='"+df.format(penalty)+"'   where id='"+repayment.get("id")+"'";
                            sunbmpDaoSupport.exeSql(repaymentUpdateSql);
                        }
                    }catch (Exception e){
                        TraceLoggerUtil.info("还款计划表更新失败，订单编号"+orderId+"还款计划表编号"+repaymentId+"详细信息"+e.toString());
                    }
                }
            }catch (Exception e){
                TraceLoggerUtil.info("还款计划表更新失败，订单编号"+orderId+"还款计划表编号"+repaymentId+"详细信息"+e.toString());
            }
        }
        TraceLoggerUtil.info("逾期计算跑批----------------------------------------------------结束");
    }
    //减免数据更新
    private void derateUpdate() {
        TraceLoggerUtil.info("减免数据更新跑批----------------------------------------------------开始");
        //获取当前日期 并设置时分为0
        Date Today=new Date();
        Today.setHours(0);
        Today.setMinutes(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,Object> map=new HashedMap();
        String magDerateListSql=" SELECT id,repayment_id AS repaymentId,orderId,derate_amount AS derateAmount,derate_link AS derateLink,derate_state AS derateState,derate_personnel AS deratePersonnel,derate_date AS derateDate,derate_opinion AS derateOpinion ,effective_data AS   effectiveData     FROM mag_derate       WHERE  approval_state='1' AND state='1'";
        List magDerateList=sunbmpDaoSupport.findForList(magDerateListSql);
        try{
            for (int i=0;i<magDerateList.size();i++){
                Map magDerate=(Map) magDerateList.get(i);
                String effectiveData=magDerate.get("effectiveData").toString();
                effectiveData =effectiveData.substring(0, 4) + "-" + effectiveData.substring(4, 6) + "-" + effectiveData.substring(6, 8);
                Date strDate=sdf.parse(effectiveData);
                strDate.setHours(0);
                strDate.setMinutes(0);
                int days = (int) ((Today.getTime() - strDate.getTime()) / (1000*3600*24));
                //审批时间超过一天无效
                if(days>0){
                    String magDerateUpdateSql="update mag_derate set state='2'   where id='"+magDerate.get("id").toString()+"'";
                    sunbmpDaoSupport.exeSql(magDerateUpdateSql);
                    insertMessage((String)magDerate.get("orderId"), "1");
                }
            }
        }catch (Exception e){

        }
        TraceLoggerUtil.info("减免数据更新跑批----------------------------------------------------结束");
    }

    //提前结清数据更新
    private void settleUpdate() {
        TraceLoggerUtil.info("提前结清数据更新跑批----------------------------------------------------开始");
        //获取当前日期 并设置时分为0
        Date Today=new Date();
        Today.setHours(0);
        Today.setMinutes(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,Object> map=new HashedMap();
        //获取非正常结清数据
        String settleRecordListSql=" SELECT id,order_id,loan_id,settle_type,settle_fee,settle_amount,effective_time,state,create_time  FROM mag_settle_record       WHERE  state='1' and settle_type='1'";
        List recordList=sunbmpDaoSupport.findForList(settleRecordListSql);
        try{
            for (int i=0;i<recordList.size();i++){
                Map record=(Map) recordList.get(i);
                String repaySql = "select count(1) as num from mag_repayment where settle_id='"+record.get("id").toString()+"' and (state='2' || state='4')";
                Map repayMap = sunbmpDaoSupport.findForMap(repaySql);
                if (null != repayMap && Integer.valueOf(repayMap.get("num").toString()) > 0)
                {
                    continue;
                }
                String approvalDate=record.get("effective_time").toString();
                approvalDate =approvalDate.substring(0, 4) + "-" + approvalDate.substring(4, 6) + "-" + approvalDate.substring(6, 8);
                Date strDate=sdf.parse(approvalDate);
                strDate.setHours(0);
                strDate.setMinutes(0);
                int days = (int) ((Today.getTime() - strDate.getTime()) / (1000*3600*24));
                //审批时间超过一天无效
                if(days>0){
                    String magRecordUpdateSql="update mag_settle_record set state='0'   where id='"+record.get("id").toString()+"'";
                    sunbmpDaoSupport.exeSql(magRecordUpdateSql);
                    insertMessage((String)record.get("order_id"), "2");
                    //正常结清数据
                    String normalSettleRecordListSql="select * from mag_settle_record   where settle_type='0' and  state in ('1','2') and loan_id='"+record.get("loan_id").toString()+"'";
                    List normalRecordList=sunbmpDaoSupport.findForList(normalSettleRecordListSql);
                    //没有正常结清数据 更新放款表
                    if(normalRecordList.size()==0){
                        String magLoanUpdateSql="update mag_loan set settle_state='0'   where id='"+record.get("loan_id").toString()+"'";
                        sunbmpDaoSupport.exeSql(magLoanUpdateSql);
                    }
                }
            }
        }catch (Exception e){

        }
        TraceLoggerUtil.info("提前结清数据更新跑批----------------------------------------------------结束");
    }


    /**
     * 绑卡成功，向订单日志中，加一条订单消息
     * @param orderId 订单id
     * @param type 类型(1:减免 2：非正常结清)
     * @throws Exception
     */
    public void  insertMessage(String orderId, String type) throws Exception {
        String sql = " SELECT app.id as userId, app.registration_id as registrationId, cus.ID as customerId, cus.PERSON_NAME as CustomerName, cus.tel as tel, cus.card as card " +
                "FROM mag_order ord LEFT JOIN mag_customer cus ON cus.ID = ord.CUSTOMER_ID LEFT JOIN app_user app " +
                "on app.id = ord.USER_ID WHERE ord.id = '"+orderId+"'";
        Map map = sunbmpDaoSupport.findForMap(sql);
        AppUserInfo userInfo = new AppUserInfo();
        userInfo.setId(map.get("userId").toString());
        userInfo.setTel(map.get("tel").toString());
        userInfo.setCustomer_id(map.get("customerId").toString());
        userInfo.setCard(map.get("card").toString());
        userInfo.setName(map.get("CustomerName").toString());
        userInfo.setRegistration_id(map.get("registrationId").toString());
        if (null == userInfo)
        {
            return;
        }
        String messageId = UUID.randomUUID().toString();
        String title = "";
        String content = "";
        String date = DateUtils.getDateString(new Date());
        String user_id = userInfo.getId();//用户ID
        String jpush_state = "0";
        String regId = userInfo.getRegistration_id();

        if("1".equals(type))
        {
            title= "费用减免";
            content = iDictService.getDictInfo("消息内容", "jmsx");
        }
        else if ("2".equals(type))
        {
            title= "非正常结清";
            content = iDictService.getDictInfo("消息内容", "fzcjqsx");
        }
        if(StringUtils.isNotEmpty(regId)){
            if(!JiGuangUtils.alias(title, content, userInfo.getRegistration_id())){
                jpush_state="1";
            }
        }
        String messageinsertSql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,jpush_state,state,update_state,msg_type,push_state," +
                "order_id,order_type)values('"+messageId+"','"+user_id+"','"+title+"','"+content+"','"+date+"','"+date+"','"+jpush_state+"','0','0','2','"+jpush_state+"','"+orderId+"','2')";
        sunbmpDaoSupport.exeSql(messageinsertSql);
    }
}
