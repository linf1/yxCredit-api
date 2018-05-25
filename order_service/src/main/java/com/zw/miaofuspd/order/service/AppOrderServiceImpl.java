package com.zw.miaofuspd.order.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.*;
import com.zhiwang.zwfinance.app.jiguang.util.api.util.OrderStateEnum;
import com.zw.api.HttpUtil;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.BranchInfo;
import com.zw.miaofuspd.facade.log.entity.OrderLogBean;
import com.zw.miaofuspd.facade.log.service.IAppOrderLogService;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.facade.order.service.IAppInsapplicationService;
import com.zw.miaofuspd.facade.user.service.IMsgService;
import com.zw.service.base.AbsServiceBase;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/******************************************************
 *Copyrights @ 2016，zhiwang  Co., Ltd.
 *         项目名称 秒付金服
 *All rights reserved.
 *
 *Filename：
 *	    订单方法
 *Description：
 ********************************************************/
@Service("appOrderServiceImpl")
public class AppOrderServiceImpl extends AbsServiceBase implements AppOrderService {
    @Autowired
    private IDictService dictService;
    @Autowired
    private IAppOrderLogService iAppOrderLogService;
    @Autowired
    private IMsgService msgService;
    @Autowired
    private IAppInsapplicationService iAppInsapplicationService;
    @Autowired
    private ISystemDictService iSystemDictService;
    @Autowired
    private AppOrderService appOrderService;

    /******************************************************
     *Copyrights @ 2016，zhiwang  Co., Ltd.
     *         项目名称 秒付金服
     *All rights reserved.
     *
     *Filename：
     *	    判断拒单之后客户是否为30天之后再次申请
     *Description：
     ********************************************************/
    public Map refusedTime(String id) throws Exception {
        String sql = "select order_refused_time from app_user where id = '" + id + "'";
        Map map = sunbmpDaoSupport.findForMap(sql);
        String date = (String) map.get("order_refused_time");//是否存在拒单
        Map isMap = new HashMap();
        isMap.put("flag", true);
        if (StringUtils.isNotEmpty(date)) {
            Date date1 = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");//时间转换为年月日格式
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");//时间转换为年月日格式
            Date date2 = sdf1.parse(date);
            date = sdf.format(date2);
            String date3 = sdf.format(date1);
            date2 = sdf.parse(date);
            date1 = sdf.parse(date3);
            long time = date1.getTime() - date2.getTime() - (long) 30 * 24 * 60 * 60 * 1000;
            System.out.println(date1.getTime());
            System.out.println(date2.getTime());
            System.out.println((long) 30 * 24 * 60 * 60 * 1000);
            if (time < 0) {//判断是否为30天之后
                int sum = (int) (-time / ((long) 24 * 60 * 60 * 1000));
                int n = (int) (time % ((long) 24 * 60 * 60 * 1000));
                if (n != 0) {
                    sum += 1;
                }
                isMap.put("flag", false);
                isMap.put("msg", "抱歉！由于您的上笔订单审批拒绝！请在" + sum + "天后再次尝试！");
            }
        }
        return isMap;
    }

    /******************************************************
     *Copyrights @ 2016，zhiwang  Co., Ltd.
     *         项目名称 秒付金服
     *All rights reserved.
     *
     *Filename：
     *	    判断是否存在未完成订单
     *Description：
     ********************************************************/
    public Map isTrue(String id) {
        String sql = "select id,state,USER_ID from mag_order where state = '0'" +
                " and order_type='2' and USER_ID = '" + id + "'";
        Map map = sunbmpDaoSupport.findForMap(sql);
        Map isMap = new HashMap();
        if (map != null) {
            isMap.put("order_id", map.get("id"));
            isMap.put("flag", false);
            return isMap;
        }
        isMap.put("flag", true);
        return isMap;
    }


    public Map getPeriods(String periods_id) {
        Map map = new HashMap();
        String sql = "SELECT periods,multiple_rate from pro_working_product_detail where id = '" + periods_id + "'";
        List<Map> list = sunbmpDaoSupport.findForList(sql);
        if (list != null && list.size() > 0) {
            String periods = (String) list.get(0).get("periods");
            String multiple_rate = (String) list.get(0).get("multiple_rate");
            map.put("periods", periods);
            map.put("multipleRate", multiple_rate);
            map.put("flag", true);
            return map;
        } else {
            map.put("flag", false);
            return map;
        }
    }

    public Map getProduct(String product_id) {
        String sql = "SELECT pro_name FROM pro_crm_product where id ='" + product_id + "'";
        Map map = sunbmpDaoSupport.findForMap(sql);
        if (map == null) {
            map = new HashMap();
            map.put("flag", false);
            map.put("msg", "当前产品已停用");
            return map;
        }
        Map map1 = new HashMap();
        map1.put("flag", true);
        map1.put("data", map);
        return map1;
    }

    /******************************************************
     *Copyrights @ 2016，zhiwang  Co., Ltd.
     *         项目名称 秒付金服
     *All rights reserved.
     *
     *Filename：
     *		生成订单编号
     *Description：
     ********************************************************/
    public String getOrderNo() {
        Random r = new Random();
        String date = DateUtils.getDateString(new Date());
        return date += r.nextInt(10000) + "";
    }


    /**
     * 根据产品id获取产品属于哪个系列
     *
     * @param product_id
     * @return
     */
    public Map getProductSeries(String product_id) {
        Map map = new HashMap();
        String sql = "SELECT pro_name,pro_number from pro_crm_product where id = (select parent_id from pro_crm_product where id ='" + product_id + "') and status = '1'";
        List<Map> list = sunbmpDaoSupport.findForList(sql);
        if (list != null && list.size() > 0) {
            String productType = (String) list.get(0).get("pro_number");
            String productTypeName = (String) list.get(0).get("pro_name");
            map.put("productType", productType);
            map.put("productTypeName", productTypeName);
            map.put("flag", true);
            return map;
        } else {
            map.put("flag", false);
            return map;
        }
    }

    public void push(String order_id) throws Exception {
        List list = new ArrayList();
        String id = UUID.randomUUID().toString();
        String userSql = "select USER_ID as userId from mag_order where order_type = '2' and id = '" + order_id + "'";
        Map userMap = sunbmpDaoSupport.findForMap(userSql);
        String user_id = userMap.get("userId").toString();
        String cont = dictService.getDictInfo("消息内容", "zxsq");
        String title = "在线申请";
        String date = DateUtils.getDateString(new Date());
        String push = "0";
        String sql = "update app_message set update_state = '0' where order_id = '" + order_id + "'";
        list.add(sql);
        sql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,state,update_state,msg_type,push_state,order_state,order_id,order_type)values('" + id + "','" + user_id + "','" + title + "','" + cont + "','" + date + "','" + date + "','0','1','1','" + push + "','0','" + order_id + "','2')";
        list.add(sql);
        OrderLogBean orderLog = new OrderLogBean();
        orderLog.setMessageId(id);
        //记录订单日志
        String orderInfoSql = "select order_no,USER_ID,PERIODS,product_type,CUSTOMER_ID,CUSTOMER_NAME,CARD,tel,product_type_name,product_name_name,AMOUNT from mag_order where id='" + order_id + "'";
        Map map1 = sunbmpDaoSupport.findForMap(orderInfoSql);
        orderLog.setUserId(map1.get("USER_ID").toString());
        orderLog.setCard(map1.get("CARD").toString());
        orderLog.setCustomerId(map1.get("CUSTOMER_ID").toString());
        orderLog.setPeriods(map1.get("PERIODS").toString());
        orderLog.setCustomerName(map1.get("CUSTOMER_NAME").toString());
        orderLog.setAmount(map1.get("AMOUNT").toString());
        orderLog.setTel(map1.get("tel").toString());
        orderLog.setProductTypeName(map1.get("product_type_name").toString());
        orderLog.setProductNameName(map1.get("product_name_name").toString());
        orderLog.setOrderNo(map1.get("order_no").toString());
        orderLog.setProductType(map1.get("product_type").toString());
        orderLog.setTache(title);
        orderLog.setChangeValue(title);
        orderLog.setOperatorId(map1.get("USER_ID").toString());
        orderLog.setOperatorType("0");
        orderLog.setOperatorName(map1.get("CUSTOMER_NAME").toString());
        orderLog.setTache(title);
        orderLog.setCreatTime(DateUtils.getDateString(new Date()));
        orderLog.setCreatTimeLog(DateUtils.getDateString(new Date()));
        orderLog.setAlterTime(DateUtils.getDateString(new Date()));
        orderLog.setOrderId(order_id);
        if (iAppOrderLogService.setOrderLog(orderLog) == null) {
            TraceLoggerUtil.error("记录订单日志失败!");
        }
        sunbmpDaoSupport.exeSql(list);
    }


    /**
     * 获取上一笔订单
     *
     * @param user_id
     * @return
     */
    public Map getLastOrder(String user_id) {
        String sql = "select id,order_no,card,customer_name,tel,product_type,state from mag_order where USER_ID='" + user_id + "'and state='9' and order_type='2' ORDER BY CREAT_TIME desc";
        List list = sunbmpDaoSupport.findForList(sql);
        Map map = null;
        if (list != null && list.size() > 0) {
            map = (Map) list.get(0);
        }
        return map;
    }

    public List getrepayOrder(String order_id) {
        String sql = "select overdue_days,pay_time,loan_time,create_time,pay_count from mag_repayment where order_id='" + order_id + "'";
        List list = sunbmpDaoSupport.findForList(sql);
        return list;
    }

    /**
     * 计算两个日期之间的天数
     *
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    public int getDateSpace(Date date1, Date date2)
            throws ParseException {
        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();
        calst.setTime(date1);
        caled.setTime(date2);
        //设置时间为0时
        calst.set(Calendar.HOUR_OF_DAY, 0);
        calst.set(Calendar.MINUTE, 0);
        calst.set(Calendar.SECOND, 0);
        caled.set(Calendar.HOUR_OF_DAY, 0);
        caled.set(Calendar.MINUTE, 0);
        caled.set(Calendar.SECOND, 0);
        //得到两个日期相差的天数
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600 / 24;
        return days;
    }

    /**
     * 获取续贷状态
     *
     * @param order_id
     * @return
     */
    public String getOverdueState(String order_id) {
        String sql = "select overdue_state from mag_order where id = '" + order_id + "'";
        Map map = sunbmpDaoSupport.findForMap(sql);
        if (map != null) {
            return map.get("overdue_state").toString();
        }
        return null;
    }

    @Override
    /**
     * 通过业务员编号获取该业务员下所有订单信息
     * @param employeeNum
     * @return
     * @throws Exception
     */
    public Map getAllOrder(String employeeId, String type) {
        Map map = new HashMap();
       /* String sql = "SELECT id,customer_id as customerId,amount,rate as monthRate,customer_name as customerName,sex_name as sexName,order_no AS orderNo,periods," +
                "DATE_FORMAT(alter_time,'%Y-%m-%d  %H:%i:%s') AS alterTime,merchandise_type AS merchandiseType,merchandise_version AS merchandiseVersion," +
                "merchandise_brand AS merchandiseBrand,merchandise_model AS merchandiseModel,commodity_state AS commodityState,predict_price AS predictPrice," +
                "state,is_sign as isSign FROM mag_order where  " +
                "emp_id ='" + employeeId + "' AND  order_type = '2' AND  ";*/
       String sql="SELECT mo.id,mo.customer_id as customerId,mo.amount,mo.rate as monthRate,mo.customer_name as customerName,mo.sex_name as sexName,mo.order_no AS orderNo,mo.periods,\n" +
               "   DATE_FORMAT(mo.alter_time,'%Y-%m-%d  %H:%i:%s') AS alterTime,mo.merchandise_type AS merchandiseType,mo.merchandise_version AS merchandiseVersion,mo.diy_type,mo.diy_days,\n" +
               "   mo.merchandise_brand AS merchandiseBrand,mo.merchandise_model AS merchandiseModel,mo.commodity_state AS commodityState,mo.predict_price AS predictPrice,\n" +
               "   mo.state,mo.is_sign as isSign,mso.state as status FROM mag_order mo LEFT JOIN (select order_Id, state, type from mag_servicepag_order where type=1) mso on mo.id=mso.order_Id \n" +
               "   where  mo.emp_id ='" + employeeId + "' AND  mo.order_type = '2' and ";
        StringBuffer allOrderSql = new StringBuffer(sql);
        StringBuffer uncomSql = new StringBuffer(sql);
        StringBuffer inReviewSql = new StringBuffer(sql);
        StringBuffer rejectSql = new StringBuffer(sql);
        StringBuffer approvalSql = new StringBuffer(sql);//查询全部审批通过
        StringBuffer deliverSql = new StringBuffer(sql);//待发货
        StringBuffer deliveredSql = new StringBuffer(sql);//已发货
        allOrderSql.append("mo.state != '9' and mo.state != '5' and mo.state != '8' order by mo.alter_time desc");
        uncomSql.append("mo.state = '0'").append("  order by mo.alter_time desc");
        inReviewSql.append("mo.state in('1','2')").append("  order by mo.alter_time desc");
        rejectSql.append("mo.state in('3','3.5','6')").append("  order by mo.alter_time desc");
        approvalSql.append("mo.state = '5' order by mo.alter_time desc");//查询审批通过的单子
        deliverSql.append("mo.state = '5' and mo.commodity_state in('16','17','18')  order by mo.alter_time desc");//待发货
        deliveredSql.append("mo.state = '5' and mo.commodity_state in('19','20')  order by mo.alter_time desc");//已发货
        if ("sqgl".equals(type)) {
            //所有的订单
            List orderAllList = new ArrayList();//获取所有订单
            List allOrderList = sunbmpDaoSupport.findForList(allOrderSql.toString());
            if (allOrderList == null) {
                map.put("flag", false);
                return map;
            }

            addMonthPaytoOrder(orderAllList, allOrderList, "orderAllList", map);
            //未提交的订单
            List listUncom = new ArrayList();//获取未提交的订单
            List listUncom1 = sunbmpDaoSupport.findForList(uncomSql.toString());
            addMonthPaytoOrder(listUncom, listUncom1, "listUncom", map);
            //审核中订单
            List listInReview = new ArrayList();//获取审核中的订单
            List listInReview1 = sunbmpDaoSupport.findForList(inReviewSql.toString());
            addMonthPaytoOrder(listInReview, listInReview1, "listInReview", map);
            //内拒单
            List listReject = new ArrayList();
            List listReject1 = sunbmpDaoSupport.findForList(rejectSql.toString());//获取内拒单
            addMonthPaytoOrder(listReject, listReject1, "listReject", map);
            map.put("listAll", orderAllList);
            map.put("listUncom", listUncom);
            map.put("listInReview", listInReview);
            map.put("listReject", listReject);
            map.put("flag", true);
        } else if ("ddgl".equals(type)) {
            //所有审批通过的单子
            List approvalList = new ArrayList();
            List approvalList1 = sunbmpDaoSupport.findForList(approvalSql.toString());//所有审批通过的单子
            if (approvalList1 == null) {
                map.put("flag", false);
                return map;
            }
            try{
                for(int i=0;i<approvalList1.size();i++){
                    Map serviceMap=(Map)approvalList1.get(i);
                    String orderId=serviceMap.get("id").toString();
                    String commodityState = serviceMap.get("commodityState").toString();//区分是否有前置服务包,页面做不同的显示
                    String serviceState=serviceMap.get("status").toString();
                    if("16.7".equals(commodityState) || "17".equals(commodityState)){
                        if(!"".equals(serviceState) && !"1".equals(serviceState)){
                            commodityState = "16.7";
                        }else{
                            commodityState = "17";
                        }
                        serviceMap.put("commodityState",commodityState);
                    }
                }
            }catch (Exception e){

            }
            addMonthPaytoOrder(approvalList, approvalList1, "approvalList", map);
            //待发货
            List deliverList = new ArrayList();
            List deliverList1 = sunbmpDaoSupport.findForList(deliverSql.toString());//待发货
            addMonthPaytoOrder(deliverList, deliverList1, "approvalList", map);
            //已发货
            List deliveredList = new ArrayList();
            List deliveredList1 = sunbmpDaoSupport.findForList(deliveredSql.toString());//已发货
            addMonthPaytoOrder(deliveredList, deliveredList1, "approvalList", map);
            map.put("approvalList", approvalList);//所有审批通过的单子
            map.put("deliverList", deliverList);//待发货
            map.put("deliveredList", deliveredList);//已发货
            map.put("flag", true);
        } else {
            map.put("flag", false);
        }
        return map;
    }

    /**
     * @param list               符合条件的订单集合，每条订单
     *                           包含分期还款金额
     * @param eligibleOrdersList 符合条件的订单的集合
     */
    public void addMonthPaytoOrder(List list, List eligibleOrdersList, String type, Map map) {

        if (eligibleOrdersList != null && eligibleOrdersList.size() > 0) {
            for (int i = 0; i < eligibleOrdersList.size(); i++) {
                Map orderMap = (Map) eligibleOrdersList.get(i);
                Map infoMap=new HashMap();
                infoMap.put("periods",orderMap.get("periods").toString());
                infoMap.put("amount",orderMap.get("amount").toString());
                infoMap.put("downPayMoney",orderMap.get("predictPrice").toString());
                infoMap.put("diy_days",orderMap.get("diy_days")==null ? "":orderMap.get("diy_days").toString());
                infoMap.put("diy_type",orderMap.get("diy_type").toString());
                infoMap.put("rate",orderMap.get("monthRate").toString());
                Map returnMap=appOrderService.calculationRepayment(infoMap);
                double monthPay = (Double) returnMap.get("monthPay");
              /*
                double monthRate = com.base.util.StringUtils.toDouble(orderMap.get("monthRate")) / 100;
                double periods = Double.valueOf(orderMap.get("periods").toString());
                double contractAmount = Double.valueOf(orderMap.get("amount").toString());
                double predictPrice = Double.valueOf(orderMap.get("predictPrice").toString());
                contractAmount = contractAmount - predictPrice;
                //算出每月的还款金额
                String Money=String.valueOf(contractAmount/periods);
                String intNumber = Money.substring(0,Money.indexOf("."));
                double intMoney=Double.valueOf(intNumber);
                double monthPay =intMoney+ contractAmount*monthRate;*/
                //double monthPay = (contractAmount/periods)+contractAmount*monthRate;
                /*double monthPay = AverageCapitalPlusInterestUtils.getPerMonthPrincipalInterest(contractAmount, monthRate, periods);*/
                orderMap.put("monthPay",String.format("%.2f",monthPay));
                list.add(i, orderMap);
            }
        }
    }

    /**
     * @param employeeId    员工Id
     * @param searchContent 搜索内容
     * @param type          1:申请管理中订单搜索历史
     *                      2:订单管理中订单搜索历史
     *                      插入搜索记录
     *                      在订单模糊搜索的时候使用
     */
    public void insertSearchRecord(String employeeId, String merchantId, String searchContent, String type) {
        String searchTime = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
        String id = UUID.randomUUID().toString();
        String searchSql = "SELECT id,search_content AS searchContent FROM  search_history WHERE " +
                "salesman_id='" + employeeId + "' AND merchant_id='" + merchantId + "'\n" +
                "AND type ='" + type + "' ORDER BY  STR_TO_DATE(search_time,'%Y-%m-%d %H:%i:%s') DESC";
        List list = sunbmpDaoSupport.findForList(searchSql);
        //有搜索历史的情况
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String searchHistoryId = ((Map) list.get(i)).get("id").toString();
                String Content = ((Map) list.get(i)).get("searchContent").toString();
                //先判断该搜索内容是否存在在搜索历史表中，有就删掉
                if (Content.equals(searchContent)) {
                    String sql = "DELETE FROM search_history WHERE id='" + searchHistoryId + "' AND type='" + type + "'";
                    sunbmpDaoSupport.exeSql(sql);
                    break;
                }
            }
            //在判断搜索订单历史的条数是不是等于7
            String sql = "SELECT COUNT(id) AS searchHistoryNum  FROM search_history WHERE salesman_id='" + employeeId + "'" +
                    "  AND type='" + type + "'  AND merchant_id='" + merchantId + "'";
            Map map2 = sunbmpDaoSupport.findForMap(sql);
            String searchHistoryNum = map2.get("searchHistoryNum").toString();
            if ("7".equals(searchHistoryNum)) {
                //获取第一条搜索历史的id
                String searchHistoryId = ((Map) list.get(list.size() - 1)).get("id").toString();
                //删除第一条搜索历史
                String firstSearchHistory = "DELETE FROM search_history WHERE id='" + searchHistoryId + "' AND type='" + type + "'";
                sunbmpDaoSupport.exeSql(firstSearchHistory);
                //插入一条新的搜索历史
                String insertNew = "INSERT INTO search_history(id,search_time,search_content,salesman_id,type,merchant_id) VALUES" +
                        "('" + id + "','" + searchTime + "','" + searchContent + "','" + employeeId + "','" + type + "','" + merchantId + "')";
                sunbmpDaoSupport.exeSql(insertNew);
            } else {
                String insertNew1 = "INSERT INTO search_history(id,search_time,search_content,salesman_id,type,merchant_id) VALUES" +
                        "('" + id + "','" + searchTime + "','" + searchContent + "','" + employeeId + "','" + type + "','" + merchantId + "')";
                sunbmpDaoSupport.exeSql(insertNew1);
            }
        } else {
            String insertNew2 = "INSERT INTO search_history(id,search_time,search_content,salesman_id,type,merchant_id) VALUES" +
                    "('" + id + "','" + searchTime + "','" + searchContent + "','" + employeeId + "','" + type + "','" + merchantId + "')";
            sunbmpDaoSupport.exeSql(insertNew2);
        }
    }


    /**
     * 办单员端模糊搜索订单
     *
     * @param employeeId    state 订单状态
     * @param searchContent
     */
    public Map findOrderLike(String employeeId, String merchantId, String searchContent, String state, String type) throws Exception {
        //插入搜索订单记录
        insertSearchRecord(employeeId, merchantId, searchContent, type);
        //该map用于封装各种类型的订单集合
        Map map = new HashMap();
        List orderAllList = new ArrayList();//所有订单
        String string = "SELECT id,amount,rate as monthRate,customer_name as customerName,periods,sex_name as sexName," +
                "order_no AS orderNo,periods,alter_time AS alterTime,merchandise_type AS merchandiseType,merchandise_version AS merchandiseVersion," +
                "merchandise_brand AS merchandiseBrand,merchandise_model AS merchandiseModel,commodity_state AS commodityState,predict_price AS predictPrice," +
                "state,is_sign FROM mag_order where emp_id ='" + employeeId + "' AND order_type = '2' AND\n";
        StringBuffer sb = new StringBuffer(string);
        //查询全部订单
        if ("1".equals(state)) {
            sb.append("state != '9' AND  state != '5' AND\n");
            sb.append("(customer_name like " + "'%" + searchContent + "%'\n" +
                    "OR  emp_id like" + "'%" + searchContent + "%'\n" +
                    "OR  merchandise_version like '%" + searchContent + "%'\n" +
                    "OR  merchandise_brand like " + "'%" + searchContent + "%'\n" +
                    "OR  merchandise_model like " + "'%" + searchContent + "%'\n" +
                    "OR CONCAT(merchandise_version,merchandise_brand,merchandise_model) like " + "'%" + searchContent + "%')"
            );
            sb.append(" ORDER BY alter_time ASC");
            List allOrderList = sunbmpDaoSupport.findForList(sb.toString());
            if (allOrderList != null && allOrderList.size() > 0) {
                addMonthPaytoOrder(orderAllList, allOrderList, "orderAllList", map);
                map.put("listAll", orderAllList);
                map.put("flag", true);
            } else {
                map.put("flag", false);
            }
        }
        //未提交的订单
        List listUncom = new ArrayList();
        if ("2".equals(state)) {
            sb.append("state = '0' AND\n");
            sb.append("(customer_name like " + "'%" + searchContent + "%'\n" +
                    "OR  emp_id like" + "'%" + searchContent + "%'\n" +
                    "OR  merchandise_version like '%" + searchContent + "%'\n" +
                    "OR  merchandise_brand like " + "'%" + searchContent + "%'\n" +
                    "OR  merchandise_model like " + "'%" + searchContent + "%'\n" +
                    "OR CONCAT(merchandise_version,merchandise_brand,merchandise_model) like " + "'%" + searchContent + "%')"
            );
            sb.append(" ORDER BY alter_time ASC");
            List listUncom1 = sunbmpDaoSupport.findForList(sb.toString());
            if (listUncom1 != null && listUncom1.size() > 0) {
                addMonthPaytoOrder(listUncom, listUncom1, "listUncom", map);
                map.put("listUncom", listUncom);
                map.put("flag", true);
            } else {
                map.put("flag", false);
            }
        }
        //审核中的订单
        List listInReview = new ArrayList();
        if ("3".equals(state)) {
            sb.append("state in('1','2') AND\n");
            sb.append("(customer_name like " + "'%" + searchContent + "%'\n" +
                    "OR  emp_id like" + "'%" + searchContent + "%'\n" +
                    "OR  merchandise_version like '%" + searchContent + "%'\n" +
                    "OR  merchandise_brand like " + "'%" + searchContent + "%'\n" +
                    "OR  merchandise_model like " + "'%" + searchContent + "%'\n" +
                    "OR CONCAT(merchandise_version,merchandise_brand,merchandise_model) like " + "'%" + searchContent + "%')"
            );
            sb.append(" ORDER BY alter_time ASC");
            List listInReview1 = sunbmpDaoSupport.findForList(sb.toString());
            if (listInReview1 != null && listInReview1.size() > 0) {
                addMonthPaytoOrder(listInReview, listInReview1, "listInReview", map);
                map.put("listInReview", listInReview);
                map.put("flag", true);
            } else {
                map.put("flag", false);
            }
        }
        List listReject = new ArrayList();//内拒单
        if ("4".equals(state)) {
            sb.append("state in('3','6')  AND\n");
            sb.append("(customer_name like " + "'%" + searchContent + "%'\n" +
                    "OR  emp_id like" + "'%" + searchContent + "%'\n" +
                    "OR  merchandise_version like '%" + searchContent + "%'\n" +
                    "OR  merchandise_brand like " + "'%" + searchContent + "%'\n" +
                    "OR  merchandise_model like " + "'%" + searchContent + "%'\n" +
                    "OR CONCAT(merchandise_version,merchandise_brand,merchandise_model) like " + "'%" + searchContent + "%')"
            );
            sb.append(" ORDER BY alter_time ASC");
            List listReject1 = sunbmpDaoSupport.findForList(sb.toString());
            if (listReject1 != null && listReject1.size() > 0) {
                addMonthPaytoOrder(listReject, listReject1, "listReject", map);
                map.put("listReject", listReject);
                map.put("flag", true);
            } else {
                map.put("flag", false);
            }
        }
        //全部审批通过的订单
        List allPassOrder = new ArrayList();
        if ("5".equals(state)) {
            sb.append("state = '5'  AND \n");
            sb.append("(customer_name like " + "'%" + searchContent + "%'\n" +
                    "OR  emp_id like" + "'%" + searchContent + "%'\n" +
                    "OR  merchandise_version like '%" + searchContent + "%'\n" +
                    "OR  merchandise_brand like " + "'%" + searchContent + "%'\n" +
                    "OR  merchandise_model like " + "'%" + searchContent + "%'\n" +
                    "OR CONCAT(merchandise_version,merchandise_brand,merchandise_model) like " + "'%" + searchContent + "%')"
            );
            sb.append(" ORDER BY alter_time ASC");
            List passOrderList = sunbmpDaoSupport.findForList(sb.toString());
            if (passOrderList != null && passOrderList.size() > 0) {
                addMonthPaytoOrder(allPassOrder, passOrderList, "allPassOrder", map);
                map.put("allPassOrder", allPassOrder);
                map.put("flag", true);
            } else {
                map.put("flag", false);
            }
        }
        //待发货
        List waitDeliveryOrder = new ArrayList();
        if ("6".equals(state)) {
            sb.append("state = '5' AND commodity_state in('16','17','18') AND \n");
            sb.append("(customer_name like " + "'%" + searchContent + "%'\n" +
                    "OR  emp_id like" + "'%" + searchContent + "%'\n" +
                    "OR  merchandise_version like '%" + searchContent + "%'\n" +
                    "OR  merchandise_brand like " + "'%" + searchContent + "%'\n" +
                    "OR  merchandise_model like " + "'%" + searchContent + "%'\n" +
                    "OR CONCAT(merchandise_version,merchandise_brand,merchandise_model) like " + "'%" + searchContent + "%')");
            sb.append(" ORDER BY alter_time ASC");
            List waitDeliveryList = sunbmpDaoSupport.findForList(sb.toString());
            if (waitDeliveryList != null && waitDeliveryList.size() > 0) {
                addMonthPaytoOrder(waitDeliveryOrder, waitDeliveryList, "waitDeliveryOrder", map);
                map.put("waitDeliveryOrder", waitDeliveryOrder);
                map.put("flag", true);
            } else {
                map.put("flag", false);
            }
        }
        //已发货
        List deliveryOrder = new ArrayList();
        if ("7".equals(state)) {
            sb.append("state = '5' and commodity_state in('19','20') AND \n");
            sb.append("(customer_name like " + "'%" + searchContent + "%'\n" +
                    "OR  emp_id like" + "'%" + searchContent + "%'\n" +
                    "OR  merchandise_version like '%" + searchContent + "%'\n" +
                    "OR  merchandise_brand like " + "'%" + searchContent + "%'\n" +
                    "OR  merchandise_model like " + "'%" + searchContent + "%'\n" +
                    "OR CONCAT(merchandise_version,merchandise_brand,merchandise_model) like " + "'%" + searchContent + "%')");
            sb.append(" ORDER BY alter_time ASC");
            List deliveryOrderList = sunbmpDaoSupport.findForList(sb.toString());
            if (deliveryOrderList != null && deliveryOrderList.size() > 0) {
                addMonthPaytoOrder(deliveryOrder, deliveryOrderList, "deliveryOrder", map);
                map.put("deliveryOrder", deliveryOrder);
                map.put("flag", true);
            } else {
                map.put("flag", false);
            }
        }
        return map;
    }

    /**
     * 清空订单搜索历史记录
     *
     * @param employeeId
     * @return
     * @throws Exception
     */
    public Map deleteOrderSearchHistory(String employeeId, String merchantId, String type) throws Exception {
        Map map = new HashMap();
        String sql = "DELETE FROM search_history WHERE salesman_id ='" + employeeId + "' AND type='" + type + "' AND merchant_id='" + merchantId + "'";
        sunbmpDaoSupport.exeSql(sql);
        map.put("flag", true);
        return map;
    }

    /**
     * 展示订单搜索历史的前几条记录
     *
     * @param employeeId
     */
    public List showOrderSearchHistory(String employeeId, String merchantId, String type) throws Exception {
        String sql = "SELECT id, search_content AS searchContent FROM  search_history WHERE  " +
                "salesman_id='" + employeeId + "'  AND merchant_id='" + merchantId + "'\n" +
                "AND type='" + type + "' ORDER BY  STR_TO_DATE(search_time,'%Y-%m-%d %H:%i:%s') DESC";
        List list = sunbmpDaoSupport.findForList(sql);
        return list;
    }

    /**
     * 根据id修改订单状态
     *
     * @param orderId
     * @param state
     * @return
     */
    @Override
    public Map updateOrderState(String orderId, String state) throws Exception {
        String dateString = DateUtils.getDateString(new Date());
        String sql = "update mag_order set state = '" + state + "',ALTER_TIME = '" + dateString + "' where id = '" + orderId + "'";
        sunbmpDaoSupport.exeSql(sql);
        if (state.equals("1")) {
            msgService.insertOrderMsg("2", orderId);
        }
        //push(orderId);
        Map map = new HashMap();
        map.put("flag", true);
        return map;
    }


    /**
     * 修改订单的状态
     * 用于完善信息，上传影像资料，手签的提交之后
     *
     * @param orderId 订单Id
     * @param state   订单状态
     */
    @Override
    public Map updateOrderStateBeforeSubmit(String orderId, String state) throws Exception {
        String dateString = DateUtils.getDateString(new Date());
        String sql = "update mag_order set commodity_state = '" + state + "',ALTER_TIME = '" + dateString + "' where id = '" + orderId + "' and order_type='2'";
        Map orderMap = getOrderById(orderId);
        String empId=orderMap.get("empId").toString();
        Map saleMap=appOrderService.getSaleInfo(empId);
        String branchId=saleMap.get("branch").toString();
        BranchInfo branchInfo=appOrderService.getGongSiName(appOrderService.getBranchInfo(branchId));

        String customerName = orderMap.get("customerName") == null ? "" : orderMap.get("customerName").toString();
        String overdue_state = getOverdueState(orderId);
        try {
            if ("11".equals(state)) {//提交办单员,如果在办单员端做过实名认证了,并做286天御规则验证,如果没有就不需要调了
                try {
                    msgService.insertOrderMsg("1", orderId);
                    if (!"".equals(customerName)) {//如果名字为空,那么就说明在办单员端没有做过ocr那么此时就不需要做天御了
                   //    if (!"1".equals(overdue_state)) {
                            try {
                                Map map = new HashMap();
                                map.put("type", "0");
                                map.put("orderId", orderId);
                                JSONObject jsonResult = iAppInsapplicationService.CreditForRisk(map);
                                if ("D00000".equals(jsonResult.getString("responseCode"))) {
                                    String result = jsonResult.getString("result");
                                    if ("拒绝".equals(result)) {
                                        String orderSql = "UPDATE mag_order set identry_rule_state = '0' where id = '" + map.get("orderId") + "' and order_type='2'";
                                        sunbmpDaoSupport.exeSql(orderSql);
                                        String usersql = "update app_user set order_refused_time='"+ DateUtils.getDateString(new Date())+"' where id ='"+orderMap.get("userId")+"'";
                                        sunbmpDaoSupport.exeSql(usersql);
                                    } else if ("通过".equals(result)) {
                                        String sql1 = "UPDATE mag_order set identry_rule_state = '1' where id = '" + map.get("orderId") + "' and order_type='2'";
                                        sunbmpDaoSupport.exeSql(sql1);
                                    }
                                }

                            } catch (Exception e) {
                            }
                    //    }
                    }
                } catch (Exception e) {
                }
            } else if ("12".equals(state)) { //办单员提交,并做287规则验证
                Map paramMap = new HashMap();
                String deviceSql = "select black_box,type from app_user where id = '" + orderMap.get("userId").toString() + "'";
                Map deviceMap = sunbmpDaoSupport.findForMap(deviceSql);
                paramMap.put("blackBox", deviceMap.get("black_box").toString());
                paramMap.put("type", deviceMap.get("type").toString());
                paramMap.put("tel", orderMap.get("tel"));
                paramMap.put("card", orderMap.get("card"));
                paramMap.put("realname", orderMap.get("customerName"));
                paramMap.put("userId", orderMap.get("userId"));
                paramMap.put("orderId", orderId);
                String host = iSystemDictService.getInfo("rule.url");
                //获取设备信息
                try {
                    paramMap.put("host", host);
                    paramMap.put("companyName",branchInfo.getDeptName());
                    paramMap.put("companyId",branchInfo.getId());
                    paramMap.put("busType","2");
                    deviceRule(paramMap);
                } catch (Exception e) {
                }
                //获取91信息
                try {
                    zx(orderMap.get("customerName").toString(), orderMap.get("card").toString(), orderMap.get("company").toString(), host);
                } catch (Exception e) {
                }
                //运营商授权（每次进件必须重新授权,清空之前的已授权state）
//                try {
//                    String customerId=orderMap.get("customerId").toString();
//                    String stateSql="update mag_customer set phone_state_spd='0' where id='"+customerId+"'";
//                    sunbmpDaoSupport.exeSql(stateSql);
//                } catch (Exception e) {
//                }

                //身份风控引擎状态
                if (orderMap != null && "1".equals(orderMap.get("identryRuleState").toString())) {  //身份认证通过"1"
                  //  if (!"1".equals(overdue_state)) {
                        try {
                            Map map = new HashMap();
                            map.put("type", "1");
                            map.put("tongduType", deviceMap.get("type").toString());
                            map.put("blackBox", deviceMap.get("black_box").toString());
                            map.put("orderId", orderId);
                            JSONObject jsonResult = iAppInsapplicationService.CreditForRisk(map);
                            TraceLoggerUtil.info("287L风控接口返回结果=====" + jsonResult);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
              //      }
                }
            } else if ("15".equals(state)) {
                msgService.insertOrderMsg("2", orderId);
            } else if ("16.7".equals(state)) {
                String servicePageSql = "select id from mag_servicepag_order where order_id = '" + orderId + "' and type ='1' and state in(0,2)";
                List list = sunbmpDaoSupport.findForList(servicePageSql);
                String commodityState = "";//区分是否有前置服务包,页面做不同的显示
                if (list != null && list.size() > 0) {//有前置还款包
                    commodityState = "16.7";
                } else {//无前置还款包
                    commodityState = "17";
                }
                sql = "update mag_order set commodity_state = '" + commodityState + "',ALTER_TIME = '" + dateString + "' where id = '" + orderId + "' and order_type='2'";
            } else if ("17".equals(state)) {
                String type = "6";
                String commodityState = "17";
                /*String servicePageSql = "select id from mag_servicepag_order where order_id = '" + orderId + "' and type ='1'and state in(0,2)";
                List list = sunbmpDaoSupport.findForList(servicePageSql);
                String type = "";
                String commodityState = "";//区分是否有前置服务包,页面做不同的显示
                if (list != null && list.size() > 0) {//有前置还款包
                    type = "5";
                    commodityState = "16.7";
                } else {//无前置还款包
                    type = "6";
                    commodityState = "17";
                }*/
                sql = "update mag_order set commodity_state = '" + commodityState + "',ALTER_TIME = '" + dateString + "' where id = '" + orderId + "' and order_type='2'";
                msgService.insertOrderMsg(type, orderId);
            } else if ("19".equals(state)) {
                String commodityState="19";
                sql = "update mag_order set commodity_state = '" + commodityState + "',ALTER_TIME = '" + dateString + "',APEX3 = '" + dateString + "' where id = '" + orderId + "' and order_type='2'"; //APEX3作为发货时间(报表展示)
                msgService.insertOrderMsg("7", orderId);
            }
        } catch (Exception e) {
        }
        sunbmpDaoSupport.exeSql(sql);
        Map map = new HashMap();
        map.put("flag", true);
        return map;
    }

    /**
     * 订单提交修改状态
     * 内拒单不予提交
     *
     * @param orderId 订单Id
     * @param state   订单状态
     * @param isSign
     */
    public Map submitUpdateStatus(String orderId, String state, String isSign) throws Exception {
        Map map = new HashMap();
        String dateString = DateUtils.getDateString(new Date());
        String sql = "update mag_order set commodity_state = '" + state + "',ALTER_TIME = '" + dateString + "',is_sign='" + isSign + "' where id = '" + orderId + "'";
        sunbmpDaoSupport.exeSql(sql);
        msgService.insertOrderMsg("2", orderId);
        map.put("flag", true);
        return map;
    }


    /**
     * 根据id修改商品订单状态
     *
     * @param orderId
     * @param state
     * @return
     */
    @Override
    public Map updateOrderCommState(String orderId, String state) throws Exception {
        String dateString = DateUtils.getDateString(new Date());
        String sql = "update mag_order set commodity_state = '" + state + "',ALTER_TIME = '" + dateString + "' where id = '" + orderId + "'";
        sunbmpDaoSupport.exeSql(sql);
        if (state.equals("11")) {
            msgService.insertOrderMsg("1", orderId);
        }
        Map map = new HashMap();
        map.put("flag", true);
        return map;
    }

    /**
     * 获取当前用户下所有订单(专用于我的分期申请)
     *
     * @param customerId
     * @return
     */
    public Map getAllPersonOrder1(String customerId) {
        List orderAllList = new ArrayList();
        //获取所有订单
        String sql = "select id,order_no as orderNo,periods as periods,CREAT_TIME as createTime,customer_id as customerId,CUSTOMER_NAME as customerName," +
                "merchant_id as merchantId,merchant_name as merchantName,diy_type,diy_days" +
                "merchandise_type as merchandiseType,merchandise_type_id as merchandiseTypeId,merchandise_brand as merchandiseBrand" +
                ",merchandise_brand_id as merchandiseBrandId,merchandise_model as merchandiseModel," +
                "applay_money as applayMoney,predict_price as predictPrice,emp_number as empNumber,emp_id as empId,amount as amount," +
                "rate as rate,DATE_FORMAT(alter_time,'%Y-%m-%d') as alterTime,merchandise_version as merchandiseVersion," +
                "merchandise_url as merchandiseUrl,merchandise_code as merchandiseCode,order_type as orderType,state as state ,is_sign as isSign, commodity_state as commodityState " +
                "from mag_order where customer_id = '" + customerId + "' and order_type = '2'  order by alter_time desc";
        List orderList = sunbmpDaoSupport.findForList(sql);
        if (orderList != null && orderList.size() > 0) {
            for (int i = 0; i < orderList.size(); i++) {
                Map orderMap = new HashMap();
                orderMap = (Map) orderList.get(i);
                if ("5".equals(orderMap.get("state"))) {
                    String svcPcgSql = "select package_name as packageName,amount_collection as svcPcgAmount from mag_servicepag_order " +
                            "where order_id = '" + orderMap.get("id").toString() + "' and type = '1' and state != '1'";
                    Map svcPcgMap = sunbmpDaoSupport.findForMap(svcPcgSql);//查询所有未付款或者付款未成功的前置服务包
                    String bankSql = "select account_bank as accountBank,bank_card as bankCard from mag_customer_account where " +
                            "CUSTOMER_ID = '" + orderMap.get("customerId") + "'";
                    Map bankMap = sunbmpDaoSupport.findForMap(bankSql);
                    if (svcPcgMap != null) {
                        orderMap.put("code", 1);
                        orderMap.put("packageName", svcPcgMap.get("packageName").toString());
                        orderMap.put("svcPcgAmount", String.format("%.2f", Double.valueOf(svcPcgMap.get("svcPcgAmount").toString())));
                    } else {
                        orderMap.put("code", 0);
                    }
                    if (bankMap != null) {
                        String bankCard = bankMap.get("bankCard").toString();
                        orderMap.put("isBank", "1");
                        orderMap.put("bankName", bankMap.get("accountBank").toString());
                        orderMap.put("bankCard", bankCard.substring((bankCard.length() - 4)));
                    } else {
                        orderMap.put("isBank", "0");
                    }
                }

                //获取产品的费率和期数信息 算出每月应还款金额
                //公用的方法
                String downPayMoney = orderMap.get("predictPrice") == null ? "" : orderMap.get("predictPrice").toString();
                orderMap.put("downPayMoney",downPayMoney);
                Map retureMap=appOrderService.calculationRepayment(orderMap);
                double monthPay=(Double) retureMap.get("monthPay");
              /*  rate = orderMap.get("rate") == null ? "" : orderMap.get("rate").toString();
                amount = orderMap.get("amount") == null ? "" : orderMap.get("amount").toString();
                downPayMoney = orderMap.get("downPayMoney") == null ? "" : orderMap.get("downPayMoney").toString();*/
               /* String rate = orderMap.get("rate") == null ? "" : orderMap.get("rate").toString();
                String amount = orderMap.get("amount") == null ? "" : orderMap.get("amount").toString();
                String applayMoney = orderMap.get("applayMoney") == null ? "" : orderMap.get("applayMoney").toString();
                //获取产品的费率和期数信息
                double monthRate = Double.valueOf(rate) / 100;
                double periods = Double.valueOf(orderMap.get("periods").toString());//期数
                double contractAmount = Double.valueOf(applayMoney == "" ? "0" : applayMoney);//分期金额
                //算出每月的还款金额
                String Money=String.valueOf(contractAmount/periods);
                String intNumber = Money.substring(0,Money.indexOf("."));
                double intMoney=Double.valueOf(intNumber);
                double monthPay =intMoney+ contractAmount*monthRate;*/
               // double monthPay = (contractAmount/periods)+contractAmount*monthRate;
               // double monthPay = AverageCapitalPlusInterestUtils.getPerMonthPrincipalInterest(contractAmount, monthRate, periods);
                orderMap.put("monthPay", String.format("%.2f",String.format("%.2f",monthPay)));//每月应还金额
                orderAllList.add(i, orderMap);
            }
        }
        Map map = new HashMap();
        map.put("allList", orderAllList);
        return map;
    }


    /**
     * 获取当前用户下所有订单
     *
     * @param customerId
     * @return
     */
    @Override
    public Map getAllPersonOrder(String customerId) {
        List orderAllList = new ArrayList();
        //获取所有订单
        String sql = "select id,order_no as orderNo,periods as periods,CREAT_TIME as createTime,customer_id as customerId,CUSTOMER_NAME as customerName," +
                "merchant_id as merchantId,merchant_name as merchantName,diy_type,diy_days," +
                "merchandise_type as merchandiseType,merchandise_type_id as merchandiseTypeId,merchandise_brand as merchandiseBrand" +
                ",merchandise_brand_id as merchandiseBrandId,merchandise_model as merchandiseModel," +
                "applay_money as applayMoney,predict_price as predictPrice,emp_number as empNumber,emp_id as empId,amount as amount," +
                "rate as rate,DATE_FORMAT(alter_time,'%Y-%m-%d') as alterTime,merchandise_version as merchandiseVersion," +
                "merchandise_url as merchandiseUrl,merchandise_code as merchandiseCode,order_type as orderType,state as state ,is_sign as isSign, commodity_state as commodityState " +
                "from mag_order where customer_id = '" + customerId + "' and order_type = '2' order by alter_time desc";
        List orderList = sunbmpDaoSupport.findForList(sql);
        if (orderList != null && orderList.size() > 0) {
            for (int i = 0; i < orderList.size(); i++) {
                Map orderMap = new HashMap();
                orderMap = (Map) orderList.get(i);
                if ("5".equals(orderMap.get("state"))) {
                    String svcPcgSql = "select package_name as packageName,amount_collection as svcPcgAmount from mag_servicepag_order " +
                            "where order_id = '" + orderMap.get("id").toString() + "' and type = '1' and state != '1'";
                    Map svcPcgMap = sunbmpDaoSupport.findForMap(svcPcgSql);
                    String bankSql = "select account_bank as accountBank,bank_card as bankCard from mag_customer_account where " +
                            "CUSTOMER_ID = '" + orderMap.get("customerId") + "'";
                    Map bankMap = sunbmpDaoSupport.findForMap(bankSql);
                    if (svcPcgMap != null) {
                        orderMap.put("code", 1);
                        orderMap.put("packageName", svcPcgMap.get("packageName").toString());
                        orderMap.put("svcPcgAmount", String.format("%.2f", Double.valueOf(svcPcgMap.get("svcPcgAmount").toString())));
                    } else {
                        orderMap.put("code", 0);
                    }
                    if (bankMap != null) {
                        String bankCard = bankMap.get("bankCard").toString();
                        orderMap.put("isBank", "1");
                        orderMap.put("bankName", bankMap.get("accountBank").toString());
                        orderMap.put("bankCard", bankCard.substring((bankCard.length() - 4)));
                    } else {
                        orderMap.put("isBank", "0");
                    }
                }
                //公用的方法
                String downPayMoney = orderMap.get("predictPrice") == null ? "" : orderMap.get("predictPrice").toString();
                orderMap.put("downPayMoney",downPayMoney);
                Map retureMap=appOrderService.calculationRepayment(orderMap);
                double monthPay=(Double) retureMap.get("monthPay");
//                String rate = orderMap.get("rate") == null ? "" : orderMap.get("rate").toString();
//                String amount = orderMap.get("amount") == null ? "" : orderMap.get("amount").toString();
//                String applayMoney = orderMap.get("applayMoney") == null ? "" : orderMap.get("applayMoney").toString();
//                //获取产品的费率和期数信息
//                double monthRate = Double.valueOf(rate) / 100;
//                double periods = Double.valueOf(orderMap.get("periods").toString());//期数
//                double contractAmount = Double.valueOf(applayMoney == "" ? "0" : applayMoney);//分期金额
//                //算出每月的还款金额
//                String Money=String.valueOf(contractAmount/periods);
//                String intNumber = Money.substring(0,Money.indexOf("."));
//                double intMoney=Double.valueOf(intNumber);
//                double monthPay =intMoney+ contractAmount*monthRate;
                //double monthPay =(contractAmount/periods)+contractAmount*monthRate;
               // double monthPay = AverageCapitalPlusInterestUtils.getPerMonthPrincipalInterest(contractAmount, monthRate, periods);
                orderMap.put("monthPay", String.format("%.2f", monthPay));//每月应还金额
                orderAllList.add(i, orderMap);
            }
        }
        Map map = new HashMap();
        map.put("allList", orderAllList);
        return map;
    }


    /**
     * 通过id获取该订单的信息
     *
     * @param orderId
     * @return
     */
    @Override
    public Map getOrderById(String orderId) {
        //根据orderId获取订单
        String sql = "select id,tel,identry_rule_state as identryRuleState,user_id as userId,card,order_no as orderNo,periods as periods,customer_id as customerId,customer_name as customerName," +
                "merchant_id as merchantId,merchant_name as merchantName," +
                "merchandise_type as merchandiseType,merchandise_type_id as merchandiseTypeId,merchandise_brand as merchandiseBrand" +
                ",merchandise_brand_id as merchandiseBrandId,merchandise_model as merchandiseModel," +
                "applay_money as applayMoney,company,branch,predict_price as predictPrice,emp_number as empNumber,emp_id as empId,amount as amount," +
                "rate as rate ,merchandise_version as merchandiseVersion,merchandise_url as merchandiseUrl,commodity_state as commodityState,order_type as orderType,state as state ,is_sign as isSign " +
                "from mag_order where id = '" + orderId + "' and order_type='2'";
        return sunbmpDaoSupport.findForMap(sql);
    }

    /**
     * 添加订单
     *
     * @param orderMap
     * @return
     * @throws Exception
     */
    @Override
    public Map addOrder(Map orderMap) throws Exception {
        String empId = orderMap.get("empId").toString();//业务员工号
        String allMoney = orderMap.get("allMoney").toString();//商品总价
        String downPayMoney = orderMap.get("downPayMoney").toString();//
        String applyMoney = Double.parseDouble(allMoney) - Double.parseDouble(downPayMoney) + "";//申请金额
        String customerName = orderMap.get("customerName").toString();//客户姓名
        String userId = orderMap.get("userId").toString();//用户id
        String tel = orderMap.get("tel").toString();//手机号
        String customerId = orderMap.get("customerId").toString();//客户id
        String merchantId = orderMap.get("merchantId").toString();//商户id
        String merName = orderMap.get("merName").toString();//商户姓名
        String merchandiseVersionName = orderMap.get("merchandiseVersionName").toString();//版本名称
        String merchandiseModelName = orderMap.get("merchandiseModelName").toString();//型号名称
        String merchandiseBrandName = orderMap.get("merchandiseBrandName").toString();//品牌名称
        String merchandiseVersionId = orderMap.get("merchandiseVersionId").toString();
        String periods = orderMap.get("periods").toString();//期数
        String rate = orderMap.get("rate").toString();//利率
        String yuqiFee = orderMap.get("yuqiFee").toString();//逾期罚息
        String proTypeName = orderMap.get("proTypeName").toString();//产品类型
        String proNameName = orderMap.get("proNameName").toString();//产品名称
        String allFee = orderMap.get("allFee").toString();//所有利息
        String expirationTime = orderMap.get("expirationTime").toString();//到期时间
        String sexName = orderMap.get("sexName").toString();
        String card = orderMap.get("card").toString();
        String orderTypeValue=orderMap.get("orderTypeValue").toString()==null?"0":orderMap.get("orderTypeValue").toString();
        String diy_type=orderMap.get("diy_type").toString();
        String diy_days=String.valueOf(orderMap.get("diy_days"));
        if(StringUtils.isEmpty(diy_days)){
            diy_days="";
        }
        Map map3 = refusedTime(userId);
        if (!(boolean) map3.get("flag")) {
            return map3;
        }
        //做一次查询，如果这个人三码验证失败超过三次，那么则拒绝进件
       /* String customer_id="1a24a094-5213-4f78-b8be-a680708d2f2a";*/
        String threeCodeSql = "SELECT three_code_num FROM mag_customer where id = '" + customerId + "'";
        Map threeCodeMap = sunbmpDaoSupport.findForMap(threeCodeSql);
        int num = 0;
        if (!"".equals(threeCodeMap.get("three_code_num").toString())) {
            num = Integer.valueOf(threeCodeMap.get("three_code_num").toString());

        }
        String threeCodeError = dictService.getDictInfo("三码错误次数", "0");
        if (com.base.util.StringUtils.isEmpty(threeCodeError)) {
            threeCodeError = "3";
        }
        int errorNum = Integer.valueOf(threeCodeError);//总共多少次
        if (num >= errorNum) {
            threeCodeMap.put("flag", false);
            threeCodeMap.put("msg", "抱歉！由于您的姓名身份证验证失败超过" + num + "次,当天无法继续进件");
            return threeCodeMap;
        }

        Map isMap = isTrue(userId);
        if ((boolean) isMap.get("flag")) {
            Map map1 = insertOrderInfo(card, sexName, empId, allMoney, customerName, userId, tel, customerId,
                    merchantId, merName, merchandiseVersionName, merchandiseModelName,
                    merchandiseBrandName, periods, rate, downPayMoney, merchandiseVersionId, yuqiFee, applyMoney, proTypeName, proNameName, allFee, expirationTime,orderTypeValue,diy_type,diy_days);

            try {
//                String customerId=orderMap.get("customerId").toString();
                String stateSql="update mag_customer set phone_state_spd='0' where id='"+customerId+"'";
                sunbmpDaoSupport.exeSql(stateSql);
            } catch (Exception e) {
            }
            return map1;
        } else {
            String orderId = isMap.get("order_id").toString();
            Map map1 = resetOrderInfo(sexName, orderId, empId, allMoney, customerName, userId, tel, customerId,
                    merchantId, merName, merchandiseVersionName, merchandiseModelName,
                    merchandiseBrandName, periods, rate, downPayMoney, merchandiseVersionId, yuqiFee, applyMoney, proTypeName, proNameName, allFee, expirationTime,orderTypeValue,diy_type,diy_days);
            return map1;
        }
    }


    /**
     * 插入订单信息
     *
     * @return
     */
    private Map insertOrderInfo(String card, String sexName, String empId, String allMoney, String customerName, String userId, String tel, String customerId,
                                String merchantId, String merName, String merchandiseVersionName, String merchandiseModelName,
                                String merchandiseBrandName, String periods, String rate, String downPayMoney,
                                String merchandiseVersionId, String yuqiFee, String applayMoney, String proTypeName, String proNameName, String allFee, String expirationTime,String orderTypeValue,String diy_type,String diy_days) throws Exception {
        Map map = new HashMap();
        //查询当前用户,是否拥存在订单
        String sql = "select USER_ID,id ,order_no,amount,state from mag_order where state !='3' " +
                "and state !='6' and state !='9' and state != '10' and order_type = '2' and USER_ID = '" + userId + "'";
        List list = sunbmpDaoSupport.findForList(sql);
        //如果只有一笔订单,查询当前订单是否逾期
        if (list != null && list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                Map listMap = (Map) list.get(j);
                String orderId = listMap.get("id").toString();
                String repaymentSql = "select order_id as orderId,state,overdue_days as overdueDays from mag_repayment where order_id ='" + orderId + "'";
                List repayList = sunbmpDaoSupport.findForList(repaymentSql);
                int num = 0;
                if (repayList != null) {
                    for (int i = 0; i < repayList.size(); i++) {
                        Map listRepayMap = (Map) repayList.get(i);
                        if ("3".equals(listRepayMap.get("state"))) {
                            map.put("flag", false);
                            map.put("msg", "当前用户下存在订单逾期。如需添加新的订单，请先结清当前订单。");
                            return map;
                        }
                        if ("2".equals(listRepayMap.get("state"))) {
                            num++;
                        }
                    }
                    if (num < 6) {
                        map.put("flag", false);
                        map.put("msg", "当前用户下存在订单还款未满足6个月。如需添加新的订单，请先累计还款6个月。");
                        return map;
                    }
                } else {
                    map.put("flag", false);
                    map.put("msg", "当前用户下存在订单还款未满足6个月。如需添加新的订单，请先累计还款6个月。");
                    return map;
                }
            }
        }
//        else{
//            map.put("flag", false);
//            map.put("msg", "当前用户下存在订单还款未满足6个月。如需添加新的订单，请先累计还款6个月。");
//            return map;
//        }
        //续贷判断
        Map orderMap = getLastOrder(userId);//获取上一笔订单信息
        String overdue_state = "";//判断设置续贷状态
        String overdays = dictService.getDictInfo("还款时间是否超过30天", "0");  //从数据字典拿
        if (com.base.util.StringUtils.isEmpty(overdays)) {
            overdays = "30";
        }
        int over_time = Integer.valueOf(overdays);
        String overdue_day = dictService.getDictInfo("逾期天数", "0");  //从数据字典拿
        if (com.base.util.StringUtils.isEmpty(overdue_day)) {
            overdue_day = "2";
        }
        int times = Integer.valueOf(overdue_day);
        if (orderMap != null) {
            if("9".equals(orderMap.get("state").toString())){ //上笔订单已结清
                overdue_state = "1";//放款时间未超过30天
            }else {
                int max = 0;
                String order_id = (String) orderMap.get("id");
                String loantime = "";
                List repayList = getrepayOrder(order_id); //获取上一笔订单还款信息
                if (repayList != null && repayList.size() > 0) {
                    Map maxMap = (Map) repayList.get(0);
                    max = Integer.valueOf(maxMap.get("overdue_days").toString() == "" ? "0" : maxMap.get("overdue_days").toString());//取第一个为最大值
                    loantime = (String) maxMap.get("create_time");//放款时间
                    for (int i = 0; i < repayList.size(); i++) {
                        Map repMap = (Map) repayList.get(i);
                        String overdueDay = repMap.get("overdue_days").toString();
                        if (max <= Integer.valueOf(overdueDay == "" ? "0" : overdueDay)) {
                            max = Integer.valueOf(overdueDay == "" ? "0" : overdueDay);
                        }
                    }
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                Date loadDate = sdf.parse(loantime);//放款时间
                Date nowDate = new Date();
                if (max > times) {
                    overdue_state = "0"; //逾期超过2天
                } else {
                    int dayNum = getDateSpace(loadDate, nowDate);
                    if (dayNum > over_time) {
                        overdue_state = "2"; //放款时间超过30天
                    } else {
                        overdue_state = "1";//放款时间未超过30天
                    }
                }
            }
        } else {
            //上笔订单不存在
            overdue_state = "3";
        }
        String orderId = UUID.randomUUID().toString();
        //添加新的order
        String orderNo = getOrderNo();
        String dateString = DateUtils.getDateString(new Date());
        String merSql = "select img_url as imgUrl from mag_merchantdise  where id = '" + merchandiseVersionId + "'";
        Map merMap = sunbmpDaoSupport.findForMap(merSql);
        String addrSql = "select provinces_id as provincesId,provinces,city_id as cityId,city,distric_id as districId,distric from mag_merchant where id = '" + merchantId + "'";
        Map addrMap = sunbmpDaoSupport.findForMap(addrSql);
        String empSql = "select company,branch from mag_salesman where id = '" + empId + "'";
        Map empMap = sunbmpDaoSupport.findForMap(empSql);
        String company = empMap.get("company").toString();//公司
        String branch = empMap.get("branch").toString();//部门
        String provincesId = addrMap.get("provincesId").toString();
        String provinces = addrMap.get("provinces").toString();
        String cityId = addrMap.get("cityId").toString();
        String city = addrMap.get("city").toString();
        String districId = addrMap.get("districId").toString();
        String distric = addrMap.get("distric").toString();
        String imgUrl = merMap.get("imgUrl").toString();
        String orderSql = "INSERT INTO mag_order (ID,sex_name,card,USER_ID,overdue_state,product_type_name,product_name_name, PERIODS, MERCHANT_ID," +
                " MERCHANT_NAME, CUSTOMER_ID, CUSTOMER_NAME, TEL, AMOUNT," +
                " state, rate, merchandise_brand, merchandise_model, merchandise_version," +
                " emp_id, order_type,predict_price,order_no,CREAT_TIME,ALTER_TIME,merchandise_version_id,merchandise_url,yuqi_fee,provinces_id," +
                "provinces,city_id,city,distric_id,distric,applay_money,COMPANY,BRANCH,fee,repay_date,offline_order,diy_type,diy_days)" +
                "values('" + orderId + "','" + sexName + "','" + card + "','" + userId + "','" + overdue_state + "','" + proTypeName + "','" + proNameName + "','" + periods + "','" + merchantId + "'," +
                "'" + merName + "','" + customerId + "','" + customerName + "','" + tel + "','" + allMoney + "'," +
                "'0','" + rate + "','" + merchandiseBrandName + "','" + merchandiseModelName + "','" + merchandiseVersionName + "'," +
                "'" + empId + "','2','" + downPayMoney + "','" + orderNo + "','" + dateString + "','" + dateString + "','" + merchandiseVersionId + "','" + imgUrl + "','" + yuqiFee + "'," +
                "'" + provincesId + "','" + provinces + "','" + cityId + "','" + city + "','" + districId + "','" + distric + "','" + applayMoney + "','" + company + "','" + branch + "','" + allFee + "','" + expirationTime + "','"+orderTypeValue+"','" + diy_type + "','"+diy_days+"')";
        sunbmpDaoSupport.exeSql(orderSql);
        msgService.insertOrderMsg("0", orderId);
        //push(orderId);
        map.put("flag", true);
        map.put("msg", "保存成功");
        map.put("orderId", orderId);
        return map;
    }

    /**
     * 修改订单信息
     *
     * @return
     */
    private Map resetOrderInfo(String sexName, String orderId, String empId, String allMoney, String customerName, String userId, String tel, String customerId, String merchantId, String merName, String merchandiseVersionName, String merchandiseModelName, String merchandiseBrandName, String periods,
                               String rate, String downPayMoney, String merchandiseVersionId, String yuqiFee, String applayMoney, String proTypeName, String proNameName, String allFee, String expirationTime,String orderTypeValue,String diy_type,String diy_days) {
        Map map = new HashMap();
        String merSql = "select img_url as imgUrl from mag_merchantdise  where id = '" + merchandiseVersionId + "'";
        Map merMap = sunbmpDaoSupport.findForMap(merSql);
        String imgUrl = merMap.get("imgUrl").toString();
        String dateString = DateUtils.getDateString(new Date());
        String addrSql = "select provinces_id as provincesId,provinces,city_id as cityId,city,distric_id as districId,distric from mag_merchant where id = '" + merchantId + "'";
        Map addrMap = sunbmpDaoSupport.findForMap(addrSql);
        String provincesId = addrMap.get("provincesId").toString();
        String provinces = addrMap.get("provinces").toString();
        String cityId = addrMap.get("cityId").toString();
        String city = addrMap.get("city").toString();
        String districId = addrMap.get("districId").toString();
        String distric = addrMap.get("distric").toString();
        String empSql = "select company,branch from mag_salesman where id = '" + empId + "'";
        Map empMap = sunbmpDaoSupport.findForMap(empSql);
        String company = empMap.get("company").toString();//公司
        String branch = empMap.get("branch").toString();//部门
        String sql = "UPDATE mag_order set sex_name='" + sexName + "',product_type_name='" + proTypeName + "',product_name_name='" + proNameName + "',USER_ID = '" + userId + "', PERIODS = '" + periods + "', MERCHANT_ID = '" + merchantId + "'," +
                " MERCHANT_NAME = '" + merName + "', CUSTOMER_ID = '" + customerId + "', CUSTOMER_NAME = '" + customerName + "'," +
                " TEL = '" + tel + "', AMOUNT = '" + allMoney + "', rate = '" + rate + "', merchandise_brand = '" + merchandiseBrandName + "'," +
                " merchandise_model = '" + merchandiseModelName + "', merchandise_version = '" + merchandiseVersionName + "', emp_id = '" + empId + "'," +
                " predict_price = '" + downPayMoney + "', ALTER_TIME = '" + dateString + "',merchandise_version_id = '" + merchandiseVersionId + "'," +
                "merchandise_url='" + imgUrl + "' ,yuqi_fee = '" + yuqiFee + "',provinces_id = '" + provincesId + "',provinces = '" + provinces + "'," +
                "city_id = '" + cityId + "',city = '" + city + "',distric_id = '" + districId + "',distric = '" + distric + "',applay_money = '" + applayMoney + "'," +
                "COMPANY='" + company + "',BRANCH = '" + branch + "',fee = '" + allFee + "',repay_date = '" + expirationTime + "',offline_order='"+orderTypeValue+"',diy_type = '" + diy_type + "',diy_days='"+diy_days+"' WHERE id = '" + orderId + "'";
        sunbmpDaoSupport.exeSql(sql);
        map.put("flag", true);
        map.put("msg", "保存成功");
        map.put("orderId", orderId);
        return map;
    }

    /**
     * 订单Id查找用户Id
     *
     * @param orderId 订单Id
     * @return
     * @throws Exception
     */
    public Map getCustomerIdByOrderId(String orderId) {
        Map returnMap = new HashMap();
        String customerSql = "SELECT is_identity_spd AS isIdentity " +
                "FROM  mag_customer WHERE  id =(SELECT customer_id AS customerId  FROM  mag_order WHERE  id ='" + orderId + "' and order_type='2')";
        Map map = sunbmpDaoSupport.findForMap(customerSql);
        String isIdentity = "";
        if (map != null) {
            isIdentity = map.get("isIdentity").toString();//是否已经认证
            if ("1".equals(isIdentity)) {
                returnMap.put("flag", true);
            } else {
                returnMap.put("flag", false);
            }
        } else {
            returnMap.put("flag", false);
        }
        returnMap.put("isIdentity", isIdentity);
        return returnMap;
    }

    /**
     * 分期订单详情-根据订单id获取订单的详细信息
     *
     * @param orderId
     * @return
     */
    public Map getFenqiOrderInfo(String orderId) {
        List list = new ArrayList();
        //查询服务包和订单关联表查询出所有服务包的id
        String serviceSql = "select type as packageType,month as afterMonth,period_collection as collectionPeriod," +
                "period_collection_type as collectionType,package_name as packageName,amount_collection as collectionAmount,package_service_id from mag_servicepag_order where order_id = '" + orderId + "'";
        List serviceList = sunbmpDaoSupport.findForList(serviceSql);
        if (serviceList != null && serviceList.size() > 0) {
            for (int i = 0; i < serviceList.size(); i++) {
                Map serviceMap = new HashMap();
                serviceMap = (Map) serviceList.get(i);
                String packageType = (String) serviceMap.get("packageType");//服务包类型,1前置提前还款,2月付还款包,3其他服务包
                String afterMonth = serviceMap.get("afterMonth") == null ? "" : serviceMap.get("afterMonth").toString();//几期后可提前还款
                String periodCollection = serviceMap.get("collectionPeriod") == null ? "" : serviceMap.get("collectionPeriod").toString();//多少期
                String collectionType = serviceMap.get("collectionType") == null ? "" : serviceMap.get("collectionType").toString();//收取期数类型
                if ("1".equals(packageType)) {
                    serviceMap.put("sqTypeRemark", "提货前收取");
                    serviceMap.put("qsRemark", afterMonth + "期后可提前还款");
                    list.add(serviceMap);
                } else if ("2".equals(packageType) /*|| "3".equals(packageType)*/) {
                    if ("0".equals(collectionType)) {//代表随产品期数收取
                        serviceMap.put("sqTypeRemark", "随产品期数收取");
                        serviceMap.put("qsRemark", afterMonth + "期后可提前还款");
                        list.add(serviceMap);
                    } else {//代表自定义期数收取
                        serviceMap.put("sqTypeRemark", "收取" + periodCollection + "期");
                        serviceMap.put("qsRemark", afterMonth + "期后可提前还款");
                        list.add(serviceMap);
                    }
                }else{
                    if ("0".equals(collectionType)) {//代表随产品期数收取
                        serviceMap.put("sqTypeRemark", "随产品期数收取");
                        serviceMap.put("qsRemark", "");
                        list.add(serviceMap);
                    } else {//代表自定义期数收取
                        serviceMap.put("sqTypeRemark", "收取" + periodCollection + "期");
                        serviceMap.put("qsRemark", "");
                        list.add(serviceMap);
                    }
                }
            }
        }
        //查询出订单的所有信息
        String sql = "select id,customer_id as customerId,DATE_FORMAT(alter_time,'%Y-%m-%d  %H:%i') AS alterTime,CREAT_TIME as createTime,order_no as orderNo,periods as periods,sex_name as sexName,customer_id as customerId,customer_name as customerName," +
                "merchant_id as merchantId,merchant_name as merchantName,merchandise_brand as merchandiseBrandName,merchandise_model as merchandiseModelName," +
                "predict_price as downPayMoney,emp_id as empId,amount as amount,diy_type,diy_days," +
                "rate,merchandise_code as merchandiseCode,commodity_state as commodityState,merchandise_version as merchandiseVersionName,merchandise_url as merchandiseUrl,state as state ,is_sign as isSign " +
                "from mag_order where id = '" + orderId + "' and order_type='2'";
        Map orderMap = sunbmpDaoSupport.findForMap(sql);
        String downPayMoney = "";//首付
        String rate = "";
        String amount = "";
        if (orderMap != null) {
            rate = orderMap.get("rate") == null ? "" : orderMap.get("rate").toString();
            amount = orderMap.get("amount") == null ? "" : orderMap.get("amount").toString();
            downPayMoney = orderMap.get("downPayMoney") == null ? "" : orderMap.get("downPayMoney").toString();
        }
        if ("5".equals(orderMap.get("state"))) {
            String bankSql = "select account_bank as accountBank,bank_card as bankCard from mag_customer_account where " +
                    "CUSTOMER_ID = '" + orderMap.get("customerId") + "'";
            Map bankMap = sunbmpDaoSupport.findForMap(bankSql);
            if (bankMap != null) {
                String bankCard = bankMap.get("bankCard").toString();
                orderMap.put("isBank", "1");
                orderMap.put("bankName", bankMap.get("accountBank").toString());
                orderMap.put("bankCard", bankCard.substring((bankCard.length() - 4)));
            } else {
                orderMap.put("isBank", "0");
            }
        }
        if ("16.7".equals(orderMap.get("commodityState"))) {
            String svcPcgSql = "select package_name as packageName,amount_collection as svcPcgAmount from mag_servicepag_order " +
                    "where order_id = '" + orderMap.get("id").toString() + "' and type = '1' and state != '1'";
            Map svcPcgMap = sunbmpDaoSupport.findForMap(svcPcgSql);
            if (svcPcgMap != null) {
                orderMap.put("code", 1);
                orderMap.put("packageName", svcPcgMap.get("packageName").toString());
                orderMap.put("svcPcgAmount", String.format("%.2f", Double.valueOf(svcPcgMap.get("svcPcgAmount").toString())));
            } else {
                orderMap.put("code", 0);
            }
        }
      /*  if ("5".equals(orderMap.get("state"))){
            String svcPcgSql ="select package_name as packageName,amount_collection as svcPcgAmount from mag_servicepag_order " +
                    "where order_id = '"+orderMap.get("id").toString()+"' and type = '1' and state != '1'";
            Map svcPcgMap = sunbmpDaoSupport.findForMap(svcPcgSql);
            String bankSql = "select account_bank as accountBank,bank_card as bankCard from mag_customer_account where " +
                    "CUSTOMER_ID = '"+orderMap.get("customerId")+"'";
            Map bankMap = sunbmpDaoSupport.findForMap(bankSql);
            if (svcPcgMap != null){
                orderMap.put("code",1);
                orderMap.put("packageName",svcPcgMap.get("packageName").toString());
                orderMap.put("svcPcgAmount",String.format("%.2f",Double.valueOf(svcPcgMap.get("svcPcgAmount").toString())));
            }else {
                orderMap.put("code",0);
            }
            if (bankMap != null){
                String bankCard =bankMap.get("bankCard").toString();
                orderMap.put("isBank","1");
                orderMap.put("bankName",bankMap.get("accountBank").toString());
                orderMap.put("bankCard",bankCard.substring((bankCard.length()-4)));
            }else{
                orderMap.put("isBank","0");
            }
        }*/
       /* //获取产品的费率和期数信息
        double monthRate = Double.valueOf(rate) / 100;
        double periods = Double.valueOf(orderMap.get("periods").toString());//期数
        double fenqiMoney = Double.valueOf(amount == "" ? "0" : amount) - Double.valueOf(downPayMoney == "" ? "0" : downPayMoney);//分期总金额
        //算出每月的还款金额
        String Money=String.valueOf(fenqiMoney/periods);
        String intNumber = Money.substring(0,Money.indexOf("."));
        double intMoney=Double.valueOf(intNumber);
        double monthPay =intMoney+ fenqiMoney*monthRate;*/
        double monthPay = 0.00;
        double fenqiMoney =0.00;
        double periods =0;
        if(orderMap!=null){
            //公用的方法
            Map returnMap=calculationRepayment(orderMap);
            monthPay=(Double) returnMap.get("monthPay");
            fenqiMoney=(Double)returnMap.get("fenqiMoney");
            periods=(Double)returnMap.get("periods");
            downPayMoney=returnMap.get("downPayMoney").toString();
        }else {
            orderMap.put("msg","您当前没有获取到订单信息");
        }

        //  double monthPay =(fenqiMoney/periods)+fenqiMoney*monthRate;
       /* double monthPay = AverageCapitalPlusInterestUtils.getPerMonthPrincipalInterest(fenqiMoney, monthRate, periods);*/
        //查询已还明细
        String alreadyPaySql = "select mr.actual_amount as actualAmount,mr.actual_time as actualTime,mr.repayment_amount as repaymentAmount,mr.penalty,mr.pay_count as payCount," +
                "mr.default_interest as defaultInterest,mr.derate_amount as derateAmount,mr.red_amount as redAmount,mr.settle_type as settleType, spr.amount as packAmount from mag_repayment mr " +
                "left join (select sum(amount) as amount,repayment_id from service_package_repayment where repayment_id !='' and repayment_id is not null group by repayment_id) spr on spr.repayment_id=mr.id " +
                "where mr.state='2' and mr.id !='' and mr.id is not null and mr.order_id = '" + orderId + "' ORDER BY mr.actual_time asc";
        List<Map> alreadyPays = sunbmpDaoSupport.findForList(alreadyPaySql);
        List<Map> alreadyPayList = new ArrayList<Map>();

        if (null != alreadyPays && alreadyPays.size() > 0)
        {
            int settleType = 0;
            for (Map pay : alreadyPays)
            {
                if (null != pay.get("actualAmount") && !"".equals(pay.get("actualAmount")))
                {
                    Map<String, Object> p = new HashMap<String, Object>();
                    p.put("payCount", pay.get("payCount"));
                    p.put("actualTime", pay.get("actualTime"));
                    if (null == pay.get("redAmount") || "".equals(pay.get("redAmount")) || "0".equals(pay.get("redAmount")))
                    {
                        p.put("actualAmount", pay.get("actualAmount"));
                    }
                    else
                    {
                        //应还
                        Double repayMoney = ("".equals(pay.get("repaymentAmount")) || pay.get("repaymentAmount") == null) ? 0.00 : Double.valueOf(pay.get("repaymentAmount").toString());

                        //逾期
                        Double defaultInterest = ("".equals(pay.get("defaultInterest")) || pay.get("defaultInterest") == null) ? 0.00 : Double.valueOf(pay.get("defaultInterest").toString());
                        Double penalty = ("".equals(pay.get("penalty")) || pay.get("penalty") == null) ? 0.00 : Double.valueOf(pay.get("penalty").toString());

                        //减免
                        Double derateAmount = ("".equals(pay.get("derateAmount")) || pay.get("derateAmount") == null) ? 0.00 : Double.valueOf(pay.get("derateAmount").toString());

                        //服务包
                        Double packAmount = ("".equals(pay.get("packAmount")) || pay.get("packAmount") == null) ? 0.00 : Double.valueOf(pay.get("packAmount").toString());

                        Double totalAmount = DoubleUtils.sub(DoubleUtils.add(DoubleUtils.add(DoubleUtils.add(repayMoney, defaultInterest), penalty), packAmount), derateAmount);
                        if (totalAmount < Double.valueOf(pay.get("redAmount").toString()))
                        {
                            p.put("redAmount", totalAmount);
                        }
                        else
                        {
                            p.put("redAmount", Double.valueOf(pay.get("redAmount").toString()));
                        }

                        p.put("actualAmount", totalAmount);
                    }

                    alreadyPayList.add(p);
                }
                else
                {
                    if (pay.get("settleType") != null && !"".equals(pay.get("settleType")))
                    {
                        settleType = Integer.valueOf(pay.get("settleType").toString());
                    }
                }
            }
            if (0 != settleType)
            {
                //获取提前结清还款金额
                String settleSql = "select amount as actualAmount, creat_time as actualTime from mag_transaction_details where (repayment_id = '' or repayment_id is null) and type = '2' and state = '1' and order_id='"+orderId+"'";
                List<Map> settles = sunbmpDaoSupport.findForList(settleSql);
                if (null != settles && settles.size() > 0)
                {
                    for (Map s : settles)
                    {//settleType为1 正常结清 2非正常结清
                        s.put("payCount", settleType == 1 ? "-1" : "-2");
                        alreadyPayList.add(s);
                    }
                }
            }
        }
        String payCountSql = "select count(1) as num from mag_repayment where order_id = '" + orderId + "' and state = '2'";
        Map payCount = sunbmpDaoSupport.findForMap(payCountSql);

        orderMap.put("monthPay", String.format("%.2f", monthPay));//每月还款额
        orderMap.put("fenqiMoney", String.format("%.2f", fenqiMoney));//分期金额
        orderMap.put("downPayMoney", String.format("%.2f", Double.valueOf(downPayMoney)));
        orderMap.put("periods", String.format("%.0f", periods));
        orderMap.put("serPackageList", list);
        orderMap.put("servicePagNum", list.size());
        orderMap.put("alreadyPayList", alreadyPayList);//已还
        orderMap.put("alreadyPayCount", payCount.get("num").toString());//已还
        return orderMap;
    }

    /**
     * 根据orderId设置对应订单商品串号
     *
     * @param orderId
     * @param merCode
     * @return
     */
    @Override
    public void setMerchandiseCode(String orderId, String merCode, String state) {
        String sql = "update mag_order set loan_state='1',merchandise_code = '" + merCode + "',commodity_state='" + state + "' where id = '" + orderId + "'";
        sunbmpDaoSupport.exeSql(sql);
    }

    //获取同盾设备信息接口
    public JSONObject deviceRule(Map map) {
        String result;
        JSONObject jsonResult;
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("name", map.get("realname"));
            param.put("phone", map.get("tel"));
            param.put("idNo", map.get("card"));
            param.put("bankNo", "");
            param.put("accountEmail", "");
            param.put("accountPhone", "");
            param.put("qqNumber", "");
            param.put("contactAddress", "");
            param.put("contact1Name", "");
            param.put("contact1Mobile", "");
            param.put("type", map.get("type"));
            param.put("companyName",map.get("companyName").toString());
            param.put("companyId",map.get("companyId").toString());
            param.put("busType","2");
            param.put("pid", UUID.randomUUID().toString());
            if ("WEB".equals(map.get("type"))) {
                param.put("tokenId", map.get("blackBox"));
                param.put("blackBox", "");
            } else {
                param.put("tokenId", "");
                param.put("blackBox", map.get("blackBox"));
            }
            String host = map.get("host").toString();
            String url = host + "/szt/tongdun/rule";

            result = HttpUtil.doPost(url, param);
            jsonResult = JSON.parseObject(result);
            JSONObject data = (JSONObject) jsonResult.get("data");
            System.out.println("=================================================" + data);
            JSONObject jsonResult1 = (JSONObject) data.get("INFOANALYSIS");
            JSONObject jsonResult2 = (JSONObject) jsonResult1.get("geoip_info");
            JSONObject jsonResult3 = (JSONObject) jsonResult1.get("device_info");//设备信息
            JSONObject jsonResult4 = (JSONObject) jsonResult1.get("address_detect");//
            String apply_address = jsonResult4.getString("true_ip_address");
            String operate_system = jsonResult3.getString("os");//操作系统
            String device_type = jsonResult3.getString("deviceName");//设备类型
            String tel_memory_run = jsonResult3.getString("availableMemory");//手机运行内存
            String tel_memory = jsonResult3.getString("totalMemory");//手机内存
            String tel_model = jsonResult3.getString("model");//手机型号
            String tel_brand = jsonResult3.getString("brand");//手机品牌
            String network_type = jsonResult3.getString("networkType");//网络类型
            String wifi_name = jsonResult3.getString("ssid");//wifi-名称
            String wifi_ssid = jsonResult3.getString("bssid");//wifi ssid
            String ip_address = jsonResult3.getString("trueIp");//Ip_地址
            String is_root = jsonResult3.getString("root");//是否root
            String id = UUID.randomUUID().toString();
            String deviceSql = "select id from customer_device_info where order_id ='" + map.get("orderId") + "'";
            List list = sunbmpDaoSupport.findForList(deviceSql);
            if (list != null && list.size() > 0) {
                String deleteSql = "delete from customer_device_info where order_id='" + map.get("orderId") + "'";
                sunbmpDaoSupport.exeSql(deleteSql);
            }
            jsonResult.getJSONObject("data").getJSONObject("INFOANALYSIS").getJSONObject("device_info").put("ssid","");
            String sql = "insert into customer_device_info (id,tongdun_json,order_id,apply_province,apply_city,apply_area,apply_address,imei_number," +
                    "operate_system,device_type,tel_memory_run,tel_memory,tel_model,tel_brand,network_type,wifi_name,wifi_ssid,ip_address," +
                    "ip_province,ip_city,ip_area,is_root,is_prison,is_moni_online,location_permission,create_time,alter_time,latitude,longitude,black_box,type,user_id) values" +
                    "('" + id + "','" + jsonResult + "','" + map.get("orderId") + "','','','','" + apply_address + "','','"
                    + operate_system + "','" + device_type + "','" + tel_memory_run + "','" + tel_memory + "','" + tel_model + "','"
                    + tel_brand + "','" + network_type + "','" + wifi_name + "','" + wifi_ssid + "','"
                    + ip_address + "','','','','" + is_root + "','','','','" + DateUtils.getDateString(new Date()) + "','" + DateUtils.getDateString(new Date()) + "','','','" + map.get("blackBox") + "','" + map.get("type") + "','" + map.get("userId") + "')";
            sunbmpDaoSupport.exeSql(sql);
            System.out.println("=================================================" + result);
            return jsonResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void zx(String name, String idNo, String companyName, String host) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("idNo", idNo);
        map.put("companyName", companyName);
        map.put("busType", "2");
        map.put("pid", UUID.randomUUID().toString());
        String url = host + "/szt/zhengXinLoanInfo/";
        try {
            String result = HttpUtil.doPost(url, map);
            System.out.println("=================================================");
            System.out.println(result);
            System.out.println("=================================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证当前月的还款计划的还款状态
     *
     * @param orderId
     * @return state
     */
    public String checkNowMonthPay(String orderId) {
        //获取提前结清
        String state = "";
        String settleSql = "select count(1) as num from mag_settle_record where state='1' and order_id='" + orderId + "'";
        Map settleMap = sunbmpDaoSupport.findForMap(settleSql);
        if (null != settleMap && !"0".equals(String.valueOf(settleMap.get("num"))))
        {
            return state;
        }

        String checkSql = "SELECT id ,state FROM mag_repayment WHERE date_format(pay_time, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 0 MONTH),'%Y %m') and order_id = '" + orderId + "'";
        List<Map> nowMonthPay = sunbmpDaoSupport.findForList(checkSql);
        if (nowMonthPay != null && nowMonthPay.size() > 0) {
            state = nowMonthPay.get(0).get("state").toString();
        }
        return state;

    }

    @Override
    public Map getSaleInfo(String empId) {
        String sql = "select branch from mag_salesman where id='" + empId + "'";
        return sunbmpDaoSupport.findForMap(sql);
    }

    @Override
    public BranchInfo getBranchInfo(String branchId) {
        String sql = "select id,type,pid,pname,dept_name AS deptName from zw_sys_department where id='" + branchId + "'";
        Map map = sunbmpDaoSupport.findForMap(sql);
        BranchInfo branchInfo = new BranchInfo();
        branchInfo.setId(map.get("id").toString());
        branchInfo.setType(map.get("type").toString());
        branchInfo.setPname(map.get("pname").toString());
        branchInfo.setDeptName(map.get("deptName").toString());
        branchInfo.setPid(map.get("pid").toString());
        return branchInfo;
    }

    @Override
    public BranchInfo getGongSiName(BranchInfo branchInfo) {
        if ("1".equals(branchInfo.getType())) {
            return branchInfo;
        } else {
            return getGongSiName(getBranchInfo(branchInfo.getPid()));
        }
    }

    @Override
    public Map getCompanyInfo(String company) {
        String sql = "select id from zw_sys_department where dept_name like '" + company + "' limit 1";
        return sunbmpDaoSupport.findForMap(sql);
    }

    public Map calculationRepayment(Map orderMap) {
        Map returnMap =new HashMap();
        String downPayMoney = "";//首付
        String rate = "";
        String amount = "";
        String diyType = "";
        if (orderMap != null) {
            diyType = String.valueOf(orderMap.get("diy_type"));
            if(StringUtils.isEmpty(diyType)){
                diyType="0";
            }
            rate = orderMap.get("rate") == null ? "" : orderMap.get("rate").toString();
            amount = orderMap.get("amount") == null ? "" : orderMap.get("amount").toString();
            downPayMoney = orderMap.get("downPayMoney") == null ? "" : orderMap.get("downPayMoney").toString();
        }
        double periods = Double.valueOf(orderMap.get("periods").toString());//总期数
        double fenqiMoney = Double.valueOf(amount == "" ? "0" : amount) - Double.valueOf(downPayMoney == "" ? "0" : downPayMoney);//分期总金额
        //算出每期的还款金额
        String Money = String.valueOf(fenqiMoney / periods);
        String intNumber = Money.substring(0, Money.indexOf("."));
        double intMoney = Double.valueOf(intNumber);
        double monthPay = 0.00;
        double lixi =0.00;
        if ("0".equals(diyType)) {
            //自然月的费用获取产品的费率和期数信息
            double monthRate = Double.valueOf(rate) / 100;
            monthPay = intMoney + fenqiMoney * monthRate;
            lixi = fenqiMoney*monthRate;
        } else {   //自定义天数获取产品的费率和期数信息
            int diyDays = Integer.valueOf(orderMap.get("diy_days").toString()); //自定义天数
            double dayRate = Double.valueOf(rate) * 12 / 365 / 100; //日利息
            monthPay = intMoney + fenqiMoney * dayRate * diyDays;
            //每期应还利息
            lixi=fenqiMoney*dayRate*diyDays;
        }
        returnMap.put("rate",rate);
        returnMap.put("periods",periods);
        returnMap.put("fenqiMoney",fenqiMoney);
        returnMap.put("intMoney",intMoney);
        returnMap.put("monthPay",monthPay);
        returnMap.put("downPayMoney",downPayMoney);
        returnMap.put("lixi",lixi);
        return returnMap;
    }
     public static void mian(String[] args){
         String dateString = DateUtils.getDateString(new Date());
        System.out.println(dateString);
     }

    /****************************************************碧友信**********************************************/

    /**
     * 根据用户ID和订单状态获取订单信息列表(2.审核中、3.待签约、4.待放款)
     * @author 仙海峰
     * @param userId
     * @return
     */
    @Override
    public Map getOrderListByUserId(String userId) {
        Map returnMap = new HashMap();
        String  states = OrderStateEnum.AUDIT.getCode() +","+OrderStateEnum.PENDING_CONTRACT.getCode()+","+OrderStateEnum.PENDING_LOAN.getCode();
        String sql ="SELECT ID AS orderId , product_name_name AS productName , applay_money AS applayMoney , PERIODS AS periods , CREAT_TIME AS creatTime , Order_state AS orderState " +
                    "FROM mag_order WHERE USER_ID='"+userId+"' AND Order_state IN("+states+")";
        List list =sunbmpDaoSupport.findForList(sql);
        if(userId.isEmpty()){
            returnMap.put("success",false);
            returnMap.put("msg","未登录用户，请先登录");
            return returnMap;
        }else {
            returnMap.put("OrderList",list);
            return returnMap;
        }


    }


    /**
     * 根据订单ID获取订单信息
     * @author 仙海峰
     * @param orderId
     * @return
     */
    @Override
    public Map getOrderInfoByOrderId(String orderId){

        Map returnMap = new HashMap();
        String sql ="SELECT ID AS orderId , product_name_name AS productName , applay_money AS applayMoney , PERIODS AS periods , CREAT_TIME AS creatTime , Order_state AS orderState  " +
                    "FROM mag_order WHERE ID='"+orderId+"'";
        Map map =sunbmpDaoSupport.findForMap(sql);
        returnMap.put("orderInfo",map);
        return returnMap;
    }

    /**
     * 根据订单ID获取订单签约信息
     * @author 仙海峰
     * @param orderId
     * @return
     */
    @Override
    public Map getPendingContractOrderInfoByOrderId(String orderId) {
        Map returnMap = new HashMap();
        String sql =" SELECT ID AS orderId , product_name_name AS productName ," +
                    " applay_money AS applayMoney , loan_amount AS loanAmount , contract_amount AS contractAmount , " +
                    " PERIODS AS periods , CREAT_TIME AS creatTime , Order_state AS orderState ,repay_type AS repayType  " +
                    " FROM mag_order WHERE ID='"+orderId+"' ";

        Map map =sunbmpDaoSupport.findForMap(sql);

        returnMap.put("PendingContractOrderInfo",map);
        return returnMap;
    }

    /**
     * 根据订单ID修改订单状态 并插入操作流程表(用户提交签约操作)
     * @author 仙海峰
     * @param orderId
     * @return
     */
    @Override
    public Map contractForSubmissionByOrderId(String orderId ,String userId) throws Exception {
        Map returnMap = new HashMap();
        String operationTime = DateUtils.getCurrentTime(DateUtils.STYLE_11);

        String checkSql="SELECT o.ID AS orderId , o.`CUSTOMER_NAME` AS customerName  ,o.loan_amount AS loanAmount " +
                    "FROM mag_order o WHERE o.`ID`='"+orderId+"' ";
        Map map =sunbmpDaoSupport.findForMap(checkSql);

        //获取批复金额
        String StringAmount = map.get("loanAmount").toString();
        BigDecimal amount=new BigDecimal(StringAmount);

        //获取用户姓名
        String customerName= map.get("customerName").toString();


        String insertSql="INSERT INTO order_operation_record (id,operation_node,operation_result,amount,order_id,operation_time,emp_id,emp_name,description) " +
                    "VALUES ('"+ GeneratePrimaryKeyUtils.getUUIDKey()+"',4,5,'"+amount+"',"+orderId+",'"+operationTime+"',"+userId+",'"+customerName+"','客户已签约完成')";

        String updateSql="UPDATE mag_order SET Order_state=4 WHERE ID='"+orderId+"'";

        if(!map.isEmpty()){
            int count = sunbmpDaoSupport.executeSql(insertSql);
            if(count !=0 ){
                int count2= sunbmpDaoSupport.executeSql(updateSql);
                if(count2 !=0){
                    returnMap.put("res_code",1);
                    returnMap.put("res_msg", "信息已提交，签约成功！");
                    return  returnMap;
                }else {
                    returnMap.put("res_code",0);
                    returnMap.put("res_msg", "签约失败！");
                }

            }
        }


        return returnMap;
    }

    /**
     * 根据UserId获取全部订单信息
     * @author 仙海峰
     * @param userId
     * @return
     */
    @Override
    public Map getAllOrderByUserId(String userId ,String pageNumber,String pageSize) {
        Map returnMap = new HashMap();


            String sql ="SELECT ID AS orderId ,  product_name_name AS productName , applay_money AS applayMoney , " +
                                "PERIODS AS periods , " +
                                "date_format(str_to_date(applay_time,'%Y%m%d%H%i%s'),'%Y-%m-%d %H:%I:%S') AS applayTime , " +
                                "Order_state AS orderState  " +
                        "FROM mag_order WHERE USER_ID='"+userId+"' ORDER BY applay_time DESC  limit "+pageNumber+","+pageSize;
        List allOrderList = sunbmpDaoSupport.findForList(sql);

        returnMap.put("allOrderList",allOrderList);
        return returnMap;
    }

    /**
     * 根据orderId获取待放款订单信息
     * @author 仙海峰
     * @param orderId
     * @return
     */
    @Override
    public Map getPendingMoneyInfoByOrderId(String orderId) {
        Map returnMap = new HashMap();

        String orderSql="SELECT o.ID AS orderId , o.`product_name_name` AS productName , o.`applay_money` AS applayMoney ," +
                                "o.`PERIODS` AS periods , o.`Order_state` AS orderStatus " +
                        "FROM mag_order o WHERE  o.`ID`='"+orderId+"' ";
        Map orderMap = sunbmpDaoSupport.findForMap(orderSql);
        String operationSql="SELECT operation_time AS operationTime , amount AS amount , operation_node AS operationNode  " +
                "FROM order_operation_record " +
                "WHERE  order_id='"+orderId+"' AND operation_node IN(1,4,5) ";

        List operationList = sunbmpDaoSupport.findForList(operationSql);
        returnMap.put("orderInfo",orderMap);
        returnMap.put("operationInfo",operationList);
        return  returnMap;

    }


    /**
     * 根据订单ID获取订单全部信息（包括订单操作流程信息）
     * @author 仙海峰
     * @param orderId
     * @return
     */
    @Override
    public Map getOrderAllInFoByOrderId(String orderId) {
        Map returnMap = new HashMap();
        Map operationMap = new HashMap();
        String orderSql = "SELECT   o.ID AS orderId ,  o.CUSTOMER_NAME AS customerName ,  o.TEL AS tel ,  o.CARD AS card , " +
                                    "product_name_name AS productName , applay_money AS applayMoney , " +
                                    "date_format(str_to_date( o.applay_time,'%Y%m%d%H%i%s'),'%Y-%m-%d %H:%I:%S') AS applayTime , " +
                                    " o.loan_amount AS loanAmount ," +
                                    "date_format(str_to_date( o.Examine_time,'%Y%m%d%H%i%s'),'%Y-%m-%d %H:%I:%S') AS examineTime , " +
                                    " o.contract_amount AS contractAmount , " +
                                    " o.repay_type AS repayType ,  o.Job AS job ,  o.Service_fee AS serviceFee ,  o.loan_purpose AS loanPurpose , " +
                                    " o.PERIODS AS periods ,  o.Order_state AS orderStatus  ,wpd.payment AS payment " +
                            "FROM mag_order o " +
                            "LEFT JOIN mag_product_fee pf ON o.product_detail = pf.id " +
                            "LEFT JOIN pro_working_product_detail wpd ON pf.product_id=wpd.id " +
                            "WHERE  o.ID='"+orderId+"' ";
        Map orderMap = sunbmpDaoSupport.findForMap(orderSql);

        String operationSql="SELECT id AS operationId , order_id AS orderId , emp_id AS empId , emp_name AS empName," +
                                    "date_format(str_to_date(operation_time,'%Y%m%d%H%i%s'),'%Y-%m-%d %H:%I:%S') AS operationTime , " +
                                    "amount AS amount , STATUS AS status , operation_node AS operationNode , " +
                                    "operation_result AS operationResult , description AS description " +
                            "FROM order_operation_record " +
                            "WHERE order_id='"+orderId+"' AND  operation_node NOT IN (2)  ORDER BY operation_time DESC";
        List operationList = sunbmpDaoSupport.findForList(operationSql);
        if (operationList!=null){
            //获取订单状态
            String orderStatus= orderMap.get("orderStatus").toString();

            operationMap.put("operationId","");
            operationMap.put("operationNode","");
            operationMap.put("operationResult","");
            operationMap.put("status","");
            operationMap.put("amount","");
            operationMap.put("orderId",orderId);
            operationMap.put("operationTime","");
            operationMap.put("empId","");
            operationMap.put("empName","");
            operationMap.put("description","");
            operationMap.put("orderStatus",orderStatus);

            //添加到operationList 索引为0的位置
            operationList.add(0,operationMap);
          ;
        }
        returnMap.put("orderInfo",orderMap);
        returnMap.put("operationInfo",operationList);
        return  returnMap;
    }


    /**
     * 根据订单ID修改订单状态
     * @author 仙海峰
     * @param orderId
     * @return
     */
    @Override
    public Map updateOrderStatusByOrderId(String orderId,String state) throws Exception {
        Map returnMap = new HashMap();
        String updateSql="UPDATE mag_order SET Order_state='"+state+"'  WHERE ID='"+orderId+"'";
        int count= sunbmpDaoSupport.executeSql(updateSql);
        if(count != 0){
            returnMap.put("res_code",1);
            returnMap.put("res_msg","订单状态修改成功！");
        }else {
            returnMap.put("res_code",0);
            returnMap.put("res_msg","订单状态修改失败！");
        }

        return returnMap;

    }
}