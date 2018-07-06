package com.zw.miaofuspd.contractConfirmation.service;

import com.api.model.common.BYXResponse;
import com.api.model.sortmsg.MsgRequest;
import com.api.service.sortmsg.IMessageServer;
import com.base.util.DateUtils;
import com.base.util.GeneratePrimaryKeyUtils;
import com.base.util.TemplateUtils;
import com.enums.DictEnum;
import com.zw.miaofuspd.facade.contractConfirmation.service.ContractConfirmationService;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.util.ChineseCharToEnUtil;
import com.zw.miaofuspd.util.NumberToChnUtil;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
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
        String orderSql="select id as orderId, order_no as orderNo, customer_id as customerId, amount, loan_amount as loanAmount, fee, service_fee as serviceFee, periods as deadline, loan_purpose as useOfLoans from mag_order where id='"+orderId+"'";
        Map orderMap = sunbmpDaoSupport.findForMap(orderSql);
        map.putAll(orderMap);
        double fee=Double.parseDouble(orderMap.get("fee").toString());
        double serviceFee=Double.parseDouble(orderMap.get("serviceFee").toString())*100*365;
        NumberFormat nbf=NumberFormat.getInstance();
        nbf.setMinimumFractionDigits(3);
        map.put("fee", nbf.format(fee));
        map.put("serviceFee", nbf.format(serviceFee));
        map.put("totalFee", nbf.format(fee+serviceFee));
        //查询客户
        String customerId=map.get("customerId").toString();
        String customerSql = "SELECT mc.id as customerId  , mc.user_id as userId, mc.person_name as cusName , mc.card as cusCard , " +
                "mc.tel as cusTel , mcl.nowaddress as cusAddress, mcj.company_address as cusCompanyAddress " +
                "FROM  mag_customer mc left join mag_customer_live mcl on mc.id=mcl.customer_id " +
                "left join mag_customer_job mcj on mc.id=mcj.customer_id where mc.id = '"+customerId+"'";
        Map customerMap = sunbmpDaoSupport.findForMap(customerSql);
        map.putAll(customerMap);
        //查询银行卡信息
        String bankSql="select cust_name as cusAccountName, bank_name as cusAccountBank, card_number as cusBankCard from sys_bank_card where cust_id='"+customerId+"' and is_authcard ='1'";
        Map bankMap = sunbmpDaoSupport.findForMap(bankSql);
        map.putAll(bankMap);

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
        map.put("amountChn", NumberToChnUtil.numToChn(Double.parseDouble(map.get("loanAmount").toString())));
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
        String contract_name=map.get("template_name").toString();

        String insertContractSql = "insert into mag_order_contract  (id,CUSTOMER_ID,customer_name,custID,user_id,contract_amount,order_no,contract_no,contract_name," +
                "order_id,contract_src,CREAT_TIME, status) values " +
                "('"+uuId+"','"+customerId+"','"+customerName+"','"+card+"','"+userId+"','"+amount+"','"+orderNo+"','"+contract_no+"','"+contract_name+"','"+orderId+"','"+contractSrc+"'" +
                ",'"+createTime+"','0')";

        sunbmpDaoSupport.exeSql(insertContractSql);
        return 1;
    }

    @Override
    public List<Map> getContractByOrderId(String orderId) {
        String sql = "select moc.id, moc.customer_id, moc.customer_name, moc.custId, moc.user_id, moc.contract_amount, moc.order_no, moc.contract_no, moc.contract_name,"+
                "moc.order_id, moc.contract_src, moc.creat_time, moc.status, mc.person_name,mc.card from mag_order_contract moc left join mag_customer mc on mc.id=moc.customer_id where moc.order_id = '"+orderId+"' order by CREAT_TIME";

        List<Map> contracts = sunbmpDaoSupport.findForList(sql);
        return contracts;
    }

    @Override
    public Map getContractById(String contractId) {
        String sql = "select moc.id, moc.customer_id, moc.customer_name, moc.custId, moc.user_id, moc.contract_amount, moc.order_no, moc.contract_no, moc.contract_name,"+
                "moc.order_id, moc.contract_src, moc.creat_time, moc.status, mc.person_name,mc.card from mag_order_contract moc left join mag_customer mc on mc.id=moc.customer_id where moc.id = '"+contractId+"' order by CREAT_TIME";
        Map contract = sunbmpDaoSupport.findForMap(sql);
        return contract;
    }

    @Override
    public void updateOrderStatus(Map params) {
        String uuId = UUID.randomUUID().toString();
        String insertOpSql="insert into order_operation_record (id, operation_node,operation_result, status, amount, order_id, operation_time, emp_id, emp_name, description) values ('"+uuId+"', '4', '"+params.get("operationResult")+"', '1', '"+params.get("amount")+"', '"+params.get("orderId")+"', '"+DateUtils.getCurrentTime(DateUtils.STYLE_10)+"', '"+params.get("empId")+"', '"+params.get("empName")+"', '"+params.get("description")+"')";

        String updateStatusSql = "update mag_order set contract_no='"+params.get("contract_no")+"',order_state='"+params.get("orderState")+"' where id = '"+params.get("orderId")+"'";
        sunbmpDaoSupport.exeSql(insertOpSql);
        sunbmpDaoSupport.exeSql(updateStatusSql);
    }

    @Override
    public void updateAssetStatus(String orderId, String assetState) {
        String updateStatusSql = "update mag_order set asset_state='"+assetState+"' where id = '"+orderId+"'";
        sunbmpDaoSupport.exeSql(updateStatusSql);
    }
    @Override
    public Map getByxOrderInfo(String orderId){
        //查询订单
        String orderSql="select mo.id as order_id, mo.user_id, mc.tel, mo.applay_money, mo.periods, mo.product_name_name, mo.loan_amount from mag_order mo left join mag_customer mc on mo.customer_id=mc.id where mo.id='"+orderId+"'";
        Map orderMap = sunbmpDaoSupport.findForMap(orderSql);
        return orderMap;
    }
    public void insertAppMsg(Map orderMap, String msgCode){
        String sql = "SELECT name,description  from  mag_dict_detail where code = '"+ msgCode +"' and dict_name = '消息内容' and state = '1'";
        Map msgTemplateMap = sunbmpDaoSupport.findForMap(sql);

        String creatTime = DateUtils.getNowDate();
        String title = msgTemplateMap.get("description")==null?"":msgTemplateMap.get("description").toString();
        String content = msgTemplateMap.get("name")==null?"":msgTemplateMap.get("name").toString();
        content = TemplateUtils.getContent(content, orderMap);
        String sql7 = "insert into app_message (id,user_id,title,content,creat_time,alter_time,state,order_state,order_id) values ('"+ GeneratePrimaryKeyUtils.getUUIDKey()+"','"+orderMap.get("user_id")+"','"+title+"','"+content+"','"+creatTime+"','"+creatTime+"','0','2','"+orderMap.get("order_id")+"')";
        sunbmpDaoSupport.exeSql(sql7);
    }

}
