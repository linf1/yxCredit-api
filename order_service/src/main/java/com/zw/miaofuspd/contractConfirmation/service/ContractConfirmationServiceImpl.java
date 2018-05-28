package com.zw.miaofuspd.contractConfirmation.service;

import com.base.util.AverageCapitalPlusInterestUtils;
import com.base.util.DateUtils;
import com.zw.miaofuspd.facade.contractConfirmation.service.ContractConfirmationService;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.util.ChineseCharToEnUtil;
import com.zw.miaofuspd.util.NumberToChnUtil;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <strong>Title : <br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2018年03月12日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:Win7 <br>
 *         email: <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
@Service("contractConfirmationServiceImpl")
public class ContractConfirmationServiceImpl extends AbsServiceBase implements ContractConfirmationService {
    @Autowired
    private AppOrderService appOrderService;
    @Autowired
    private IDictService iDictService;

    @Override
    public Map getContractUserInfo(String userId) {
        Map map = new HashMap();
        //查询用户信息
        String sql = "SELECT id as customerId  , person_name as cusName , card as cusCard ,tel as cusPhone , card_register_address as cusAddress,TEL FROM  mag_customer where USER_ID = '"+userId+"'";
        Map userMap = sunbmpDaoSupport.findForMap(sql);
        map.putAll(userMap);
        //获取当前订单id
        String accountsql="select bank_card,account_bank from mag_customer_account where user_id='"+userId+"' and channel='1'";
        Map accountMap=sunbmpDaoSupport.findForMap(accountsql);
        map.putAll(accountMap);
        sql = "select id,merchandise_brand,merchandise_model,merchandise_version_id from mag_order where USER_ID = '"+userId+"' and CREAT_TIME =(SELECT MAX(creat_time) from mag_order where USER_ID = '"+userId+"')and order_type = '2'";
        Map orderMap = sunbmpDaoSupport.findForMap(sql);
        String orderId = orderMap.get("id").toString();
        map.put("orderId",orderId);
        String orSql="select COMPANY,BRANCH, rate,amount,predict_price as downPayMoney,periods,merchandise_version_id,merchandise_model,merchandise_brand,applay_money,predict_price,diy_type,diy_days,MERCHANT_ID from mag_order where id='"+orderId+"' and order_type='2'";
        Map merchandiseMap=sunbmpDaoSupport.findForMap(orSql);
        String merchandise_brand=merchandiseMap.get("merchandise_brand").toString();
        String merchandise_model=merchandiseMap.get("merchandise_model").toString();
        String merchantId=merchandiseMap.get("MERCHANT_ID").toString();
        String merchandiseVersionId=merchandiseMap.get("merchandise_version_id").toString();
        double applayMoney=Double.valueOf(merchandiseMap.get("applay_money").toString());
        double predictPrice=Double.valueOf(merchandiseMap.get("predict_price").toString());
        Double price=applayMoney+predictPrice;
       // String merchantSql="select merchandise_name from mag_merchantdise where id='"+merchandiseVersionId+"'";
        String merchantSql="select a.merchandise_name as brandName,b.merchandise_name as typeName,c.merchandise_name as modelName,d.merchandise_name as goodName from mag_merchantdise a\n" +
                " LEFT JOIN mag_merchantdise b ON b.id = a.parent_id LEFT JOIN mag_merchantdise c ON c.id = b.parent_id\n" +
                " LEFT JOIN mag_merchantdise d ON d.id = c.parent_id\n" +
                " LEFT JOIN mag_merchantdise e ON e.id = d.parent_id\n" +
                "where a.id='"+merchandiseVersionId+"'";
        Map merchantMap=sunbmpDaoSupport.findForMap(merchantSql);
        String merchandise_name=merchantMap.get("goodName").toString();
        String modelName=merchantMap.get("modelName").toString();
        String typeName=merchantMap.get("typeName").toString();
        String brandName=merchantMap.get("brandName").toString();
        String sql1="select mer_name,mer_email,mer_tel from mag_merchant where id='"+merchantId+"'";
        Map sqlMap=sunbmpDaoSupport.findForMap(sql1);
        String merchantName=merchandiseMap.get("COMPANY").toString();
        String merchantEmail=sqlMap.get("mer_email").toString();
        String tel=sqlMap.get("mer_tel").toString();
        map.put("merchandiseName",merchandise_name+modelName);
        map.put("email",merchantEmail);
        map.put("tel",tel);
        map.put("model",typeName+brandName); //型号
        map.put("price",price); //单价
        map.put("weight","1"); //数量
    //    map.put("merchantName",merchantName); //甲方
        map.put("type","2"); //方式
        map.put("day","3"); //天
        map.put("freight","乙"); //运费的支付方
        String deptSql="select company_abbreviation from zw_sys_department where dept_name ='"+merchantName+"'";
        Map deptmap=sunbmpDaoSupport.findForMap(deptSql);
        String deptname=deptmap.get("company_abbreviation").toString();
        String companysql="select email,company_name,tel,num from zw_sys_companyInfo where company_name ='"+deptname+"'";
        Map companyMap=sunbmpDaoSupport.findForMap(companysql);
        /*String email=companyMap.get("email").toString();
        String company_name=companyMap.get("company_name").toString();
        String phone=companyMap.get("tel").toString();
        String num=companyMap.get("tel").toString();*/
        map.putAll(companyMap);
        map.put("merchantName",deptname); //甲方
        String downPayMoney = "";//首付金额
        String rate = "";//利率
        String amount = "";//总金额
        if(orderMap!=null){
            rate = merchandiseMap.get("rate")==null?"":merchandiseMap.get("rate").toString();
            amount = merchandiseMap.get("amount")==null?"":merchandiseMap.get("amount").toString();
            downPayMoney = merchandiseMap.get("downPayMoney")==null?"":merchandiseMap.get("downPayMoney").toString();
        }
        /**查询已还款了几期**/
        String sqlForCount="select id as repaymentId,amount ,pay_count as periods,repayment_amount as repaymentAmount," +
                "DATE_FORMAT(pay_time,'%Y%m%d') as repaymentTime,state,penalty,overdue_days as overdueDays,default_interest as defaultInterest  " +
                "from mag_repayment where order_id = '"+orderId+"' and state ='2' order by pay_time desc";
        List<Map> repaidAlreadyList = sunbmpDaoSupport.findForList(sqlForCount);
        int repayNum = repaidAlreadyList.size();
/*

        //获取产品的费率和期数信息
        double monthRate=Double.valueOf(rate)/100 ;
        int periods = Integer.valueOf(merchandiseMap.get("periods").toString());//总期数
        double fenqiMoney = Double.valueOf(amount.equals("")?"0":amount)- Double.valueOf(downPayMoney.equals("")?"0":downPayMoney);//分期总金额--放款总金额
*/
        //获取产品的费率和期数信息 算出每月应还款金额
        String repayment_amount="";
        //公用的方法
        Map returnMap= appOrderService.calculationRepayment(merchandiseMap);
        double repayment_amount1=(Double) returnMap.get("monthPay");
        repayment_amount1= (double)Math.round(repayment_amount1*100)/100;
        double fenqiMoney=(Double)returnMap.get("fenqiMoney");
        double periods=(Double)returnMap.get("periods");
        double surplusMoney=fenqiMoney%periods;
        if(repayNum==periods){
            double repayment_amount2 =surplusMoney+repayment_amount1;
            repayment_amount = String.format("%.2f",Double.valueOf(repayment_amount2));
        }else{
            repayment_amount = String.format("%.2f",Double.valueOf(repayment_amount1));
        }
     /*   //算出每月应还款金额
        String Money=String.valueOf(fenqiMoney/periods);
        String intNumber = Money.substring(0,Money.indexOf("."));
        double intMoney=Double.valueOf(intNumber);
        double repayment_amount1 = intMoney+ fenqiMoney*monthRate;
        String repayment_amount = String.format("%.2f",Double.valueOf(repayment_amount1));
        double surplusMoney=fenqiMoney%periods;
        String surplus_Money = String.format("%.2f",Double.valueOf(surplusMoney));
        if(repayNum==periods){
            double repayment_amount2 =surplusMoney+repayment_amount1;
             repayment_amount = String.format("%.2f",Double.valueOf(repayment_amount2));
        }*/
        //获取用户借款信息
       /* sql = "SELECT id, order_id, pay_count,repayment_amount from mag_repayment where order_id = '"+orderId+"'";
        Map repaymentMap = sunbmpDaoSupport.findForMap(sql);
        String payDay=repaymentMap.get("pay_count").toString();//期数
        double money=Double.valueOf(repaymentMap.get("repayment_amount").toString()); //每期的还款金额
        map.put("payDay",payDay);
       /* //获取服务包的信息
        sql = "select package_name,period,type,amount from service_package_repayment where repayment_id='"+repaymentId+"'";
        List list=sunbmpDaoSupport.findForList(sql);
        double amount=0;
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                Map pageMap =(Map) list.get(i);
               *//* String packageName=pageMap.get("package_name").toString();
                String period=pageMap.get("period").toString();
                String type=pageMap.get("type").toString();*//*
                 amount +=Double.valueOf(pageMap.get("amount").toString()); //每期服务包的还款金额
            }
        }*/

        String payDay=merchandiseMap.get("periods").toString();
        map.put("payDay",payDay);
        map.put("repaymentAmount",repayment_amount);
        map.put("amountChn",repayment_amount);
        Calendar now = Calendar.getInstance();
        String yearSub=String.valueOf(now.get(Calendar.YEAR)).substring(2);
        String year=String.valueOf(now.get(Calendar.YEAR));
        String month=String.valueOf(now.get(Calendar.MONTH)+1);
        String days=String.valueOf(now.get(Calendar.DAY_OF_MONTH));
        map.put("yearSub",yearSub);
        map.put("year",year);
        map.put("month",month);
        map.put("days",days);
        if(month.length()==1){
            month='0'+month;
        }        if(days.length()==1){
            days="0"+days;
        }
        String time=year+month+days;
        String updateSql="update mag_order set contract_time='"+time+"' where id='"+orderId+"' and order_type='2'";
        sunbmpDaoSupport.exeSql(updateSql);
        return map;
    }

    @Override
    public Map getContractAgreement(Map mapInfo) {
        Map map = new HashMap();
        String sql = "SELECT content_no_bq as context from mag_template t where t.template_type = '0'and platform_type='1' and state='1'";
        if(mapInfo.get("template_name")!=null){
            sql="SELECT content_no_bq as context from mag_template t where name='"+mapInfo.get("template_name")+"'";
        }
        Map mapContext = sunbmpDaoSupport.findForMap(sql);
        Iterator<Map.Entry> it = mapInfo.entrySet().iterator();
        String context = mapContext.get("context").toString();
        String kongGe=" ";
        for (int i = 0;i <80;i++){
             kongGe += " ";
        }
        while (it.hasNext()) {
            Map.Entry entry = it.next();
            //根据key值替换内容
            String key = "#"+entry.getKey()+"#";
            String value = String.valueOf(entry.getValue());
            if (entry.getKey().equals("amountChn")){
                //数字转大写
                value = NumberToChnUtil.numToChn(Double.parseDouble(value));
                map.put("value",value);
            }
            context =context.replace(key,value);
        }
        map.put("context",kongGe+context);
        return map;
    }

    @Override
    public void setAlreadyContractAgreement(String orderId, String nameUrl) {
        String sql ="select id,USER_ID,PERIODS,CUSTOMER_ID,CUSTOMER_NAME,CARD, applay_money,contract_no,order_no from mag_order where id = '"+orderId+"'";
        Map map = sunbmpDaoSupport.findForMap(sql);
        String amount = map.get("applay_money").toString();
        String userId = map.get("USER_ID").toString();
        String customerId = map.get("CUSTOMER_ID").toString();
        String customerName = map.get("CUSTOMER_NAME").toString();
        String card = map.get("CARD").toString();
        String contract_no = map.get("contract_no").toString();
        String uuId = UUID.randomUUID().toString();
        String createTime = DateUtils.getDateString(new Date());
        sql = "insert into mag_order_contract  (id,CUSTOMER_ID,customer_name,custID,user_id,contract_amount,order_no,contract_no,contract_name," +
                "order_id,contract_src,CREAT_TIME) values " +
                "('"+uuId+"','"+customerId+"','"+customerName+"','"+card+"','"+userId+"','"+amount+"','"+orderId+"','"+contract_no+"','申购协议','"+orderId+"','"+nameUrl+"'" +
                ",'"+createTime+"')";
        sunbmpDaoSupport.exeSql(sql);
    }

    @Override
    public String getAlreadyContractAgreement(String orderId) {
        String sql = "select contract_no from mag_order where id = '"+orderId+"'";
        Map map = sunbmpDaoSupport.findForMap(sql);
        String contract_no = null;
        if (!map.get("contract_no").equals("")){
            contract_no = map.get("contract_no").toString();
        }
        return contract_no;
    }

    @Override
    public Map getOrderInfo(String orderId) {
        String orderSql ="select amount,repay_date as repayDate from mag_order where id ='"+orderId+"' and order_type ='1' ";
        return sunbmpDaoSupport.findForMap(orderSql);
    }


    /******************************碧有信*****************************/
    /**
     * 合同信息获取
     * @param orderId
     * @return
     */
    @Override
    public Map getContractInfo(String orderId) {
        Map map = new HashMap();
        //查询订单
        String orderSql="select id as orderId, order_no as orderNo, customer_id as customerId, amount, loan_amount as loanAmount, fee, periods as deadline, loan_purpose as useOfLoans from mag_order where id='"+orderId+"'";
        Map orderMap = sunbmpDaoSupport.findForMap(orderSql);
        map.putAll(orderMap);
        //查询客户
        String customerId=map.get("customerId").toString();
        String customerSql = "SELECT id as customerId  , user_id as userId, person_name as cusName , card as cusCard ,tel as cusTel , card_register_address as cusReceiveAddress, residence_address as cusAddress FROM  mag_customer where id = '"+customerId+"'";
        Map customerMap = sunbmpDaoSupport.findForMap(customerSql);
        map.putAll(customerMap);
        //查询银行卡信息
        String bankSql="select cust_name as cusAccountName, bank_name as cusAccountBank, card_number as cusBankCard from sys_bank_card where cust_id='"+customerId+"'";
        Map bankMap = sunbmpDaoSupport.findForMap(bankSql);
        map.putAll(customerMap);

        //查询该客户在今年的借款次数
        String currentDateStr=DateUtils.getCurrentTime(DateUtils.STYLE_3);
        String countSql="select count(*) as count from mag_order_contract where customer_id='"+customerId+"' and creat_time like '"+currentDateStr+"%'";
        Map countMap = sunbmpDaoSupport.findForMap(countSql);
        String subYear=currentDateStr.substring(2,4);
        int jkCount=Integer.parseInt(countMap.get("count").toString())+1;
        String jkCountStr=jkCount+"";
        if(jkCount/100==0){
            if(jkCount/10>0){
                jkCountStr="0"+jkCount;
            }else{
                jkCountStr="00"+jkCount;
            }
        }
        String cusName=map.get("cusName").toString();
        ChineseCharToEnUtil cte = new ChineseCharToEnUtil();
        String pinyinCustName=cte.getAllFirstLetter(cusName).toUpperCase();
        System.out.println("获取拼音首字母："+ cte.getAllFirstLetter(pinyinCustName));

        String contractNo=pinyinCustName+"-"+subYear+jkCountStr;
        map.put("contractNo",contractNo);

        Calendar now = Calendar.getInstance();
        String year=String.valueOf(now.get(Calendar.YEAR));
        String month=String.valueOf(now.get(Calendar.MONTH)+1);
        String day=String.valueOf(now.get(Calendar.DAY_OF_MONTH));
        map.put("year",year);
        map.put("month",month);
        map.put("day",day);
        if(month.length()==1){
            month='0'+month;
        }
        if(day.length()==1){
            day="0"+day;
        }
        String time=year+month+day;
        String updateSql="update mag_order set contract_time='"+time+"' where id='"+orderId+"'";
        sunbmpDaoSupport.exeSql(updateSql);

        try {
            String cjName = iDictService.getDictInfo("出借人", "cjrxm");
            String cjCard = iDictService.getDictInfo("出借人", "cjsfz");
            //防止合同底部签名变形
            while (cjName.length() < 8) {
                cjName += " ";
            }
            map.put("cjName", cjName);
            map.put("cjCard", cjCard);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * 添加合同
     * @param map
     * @return
     */
    @Override
    public int insertContract(Map map){
        String uuId = UUID.randomUUID().toString();
        String createTime = DateUtils.getDateString(new Date());
        String customerId=map.get("customerId").toString();
        String orderId=map.get("orderId").toString();
        String orderNo=map.get("orderNo").toString();
        String customerName=map.get("cusName").toString();
        String card=map.get("cusCard").toString();
        String userId=map.get("userId").toString();
        String amount=map.get("loanAmount").toString();
        String contractSrc=map.get("pdfUrl").toString();
        String contract_no=map.get("contractNo").toString();

        String insertContractSql = "insert into mag_order_contract  (id,CUSTOMER_ID,customer_name,custID,user_id,contract_amount,order_no,contract_no,contract_name," +
                "order_id,contract_src,CREAT_TIME, status) values " +
                "('"+uuId+"','"+customerId+"','"+customerName+"','"+card+"','"+userId+"','"+amount+"','"+orderNo+"','"+contract_no+"','申购协议','"+orderId+"','"+contractSrc+"'" +
                ",'"+createTime+"','0')";

        sunbmpDaoSupport.exeSql(insertContractSql);
        return 1;
    }

    @Override
    public Map getContractByOrderId(String orderId) {
        String sql = "select moc.id, moc.customer_id, moc.customer_name, moc.custId, moc.user_id, moc.contract_amount, moc.order_no, moc.contract_no, moc.contract_name,"+
                "moc.order_id, moc.contract_src, moc.creat_time, moc.status, mc.person_name,mc.card from mag_order_contract moc left join mag_customer mc on mc.id=moc.customer_id where moc.order_id = '"+orderId+"'";
        Map map = sunbmpDaoSupport.findForMap(sql);
        return map;
    }

    @Override
    public void updateOrderStatus(Map params) {
        String uuId = UUID.randomUUID().toString();
        String insertOpSql="insert into order_operation_record (id, operation_node,operation_result, status, amount, order_id, operation_time, emp_id, emp_name, description) values ('"+uuId+"', '4', '"+params.get("operationResult")+"', '1', amount, '"+params.get("orderId")+"', '"+DateUtils.getCurrentTime(DateUtils.STYLE_10)+"', '"+params.get("empId")+"', '"+params.get("empName")+"', '"+params.get("description")+"')";

        String updateStatusSql = "update mag_order set order_state='"+params.get("orderState")+"' where id = '"+params.get("orderId")+"'";
        sunbmpDaoSupport.exeSql(insertOpSql);
        sunbmpDaoSupport.exeSql(updateStatusSql);
    }

    @Override
    public void updateAssetStatus(String orderId, String assetState) {
        String updateStatusSql = "update mag_order set asset_state='"+assetState+"' where id = '"+orderId+"'";
        sunbmpDaoSupport.exeSql(updateStatusSql);
    }
}
