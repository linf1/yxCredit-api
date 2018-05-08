package com.zw.miaofuspd.openaccount.service;

import com.base.util.DateUtils;
import com.base.util.DoubleUtils;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.openaccount.service.IRepayPlanService;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.service.base.AbsServiceBase;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("repayPlanServiceImpl")
public class RepayPlanServiceImpl extends AbsServiceBase implements IRepayPlanService {

    @Autowired
    ISystemDictService iSystemDictService;
    @Autowired
    private AppOrderService appOrderService;

    /**
     * 查询还款计划
     * @param userInfo
     * @return
     * @throws Exception
     */
    @Override
    public Map getRepayplanList(AppUserInfo userInfo) throws Exception {
        Map returnMap = new HashMap();//需返回的数据存入此map中
        List repayInfoList = new ArrayList();//还款信息list包括商品的信息
        double unpaidAllAmount = 0.0;//当前未还总金额
        double unpaidAmount = 0.0;//当前本期待还金额
        double overdueAllAmount = 0.0;//逾期待还金额
        double derateAllAmount = 0.0;//逾期减免后总金额
        String derateAllCode = "0";//所有逾期订单中是否有减免
//        boolean confirmFlag = false;
        //查询待还款订单
        String orderId = "";
        List<Map> orderList = sunbmpDaoSupport.findForList("select id,periods,rate,order_no as orderNo,amount,state,merchant_id as merchantId,merchant_name as merchantName,merchandise_type as merchandiseType," +
                "merchandise_type_id as merchandiseTypeId,merchandise_brand as merchandiseBrand,merchandise_brand_id as merchandiseBrandId,merchandise_model as merchandiseModel,diy_type,diy_days," +
                "merchandise_version as merchandiseVersion,merchandise_version_id as merchandiseVersionId,applay_money as applayMoney,predict_price as downPayMoney,merchandise_url as merchandiseUrl,state,alter_time as alterTime " +
                "from mag_order where CUSTOMER_ID='" + userInfo.getCustomer_id() + "' and state='5' and order_type='2' ORDER BY alter_time asc");
        if (orderList != null && orderList.size() > 0) {
            //获取当前月
            String month = DateUtils.formatDate("yyyyMM");
            int orderNum = 0;
            for(int i =0 ;i<orderList.size();i++){
                double alreadyNum = 0;//用于判断还了多少次
                //获取有无有效的提前权限
                Map settleMap = sunbmpDaoSupport.findForMap("select settle_type as settleType, settle_fee as settleFee, settle_amount as settleAmount," +
                        "effective_time as effectiveTime from mag_settle_record where state!=0 and order_id='" + orderList.get(i).get("id") + "'");
                Double settleRepayMoney = 0.00;

                Map resultMap = new HashMap();
                Map umap = orderList.get(i);
                orderId = (String) umap.get("id");//订单id
                String merchandiseUrl = umap.get("merchandiseUrl").toString();//商品缩略图
                String merchandiseVersion = umap.get("merchandiseVersion").toString();//商品版本
                String merchandiseBrand = umap.get("merchandiseBrand").toString();//商品品牌
                String merchandiseModel = umap.get("merchandiseModel").toString();//商品型号
                String periods1 = umap.get("periods").toString();//商品期数
                //算出每月的还款金额
                double fenqiPay1 = 0.00;
                Map retureMap=appOrderService.calculationRepayment(umap);
                fenqiPay1=(Double) retureMap.get("monthPay");
                String fenqiPay = String.format("%.2f", fenqiPay1);
              /*  String rate = umap.get("rate").toString();//利率
                double monthRate= com.base.util.StringUtils.toDouble(rate)/100 ;
                double merchandPeriods = Double.valueOf(periods1);
                double contractAmount = Double.valueOf(umap.get("applayMoney").toString());*/
                //double fenqiPay1 =(contractAmount/merchandPeriods)+contractAmount*monthRate;
              /*  String fenqiPay=String.format("%.2f",fenqiPay1);*/
               /* double fenqiPay = AverageCapitalPlusInterestUtils.getPerMonthPrincipalInterest(contractAmount,monthRate,merchandPeriods);*/
                resultMap.put("merchandiseBrand",merchandiseBrand);//商品品牌
                resultMap.put("merchandiseModel",merchandiseModel);//商品型号
                resultMap.put("merchandiseVersion",merchandiseVersion);//商品版本
                resultMap.put("merchandiseUrl",merchandiseUrl);//商品缩略图
                resultMap.put("orderId",orderId);//订单id
                resultMap.put("periods",periods1);//订单id
                resultMap.put("fenqiPay",fenqiPay);//订单id
                List repayList = new ArrayList();//还款计划list
                String unpaid = "0";//当期是否已还
                String timeSql = "select pay_time as payTime from mag_repayment where order_id = '"+orderId+"' and state = '1' order by pay_time asc LIMIT 1";
                Map timeMap = sunbmpDaoSupport.findForMap(timeSql);
                String currentTime = "";
                if(timeMap!=null ){//存在未还的计划
                    currentTime = timeMap.get("payTime").toString();//当期还款时间
                }
                //查询还款计划详情
                List<Map> repaidList = sunbmpDaoSupport.findForList("select id as repaymentId ,pay_count as periods,actual_amount,repayment_amount as repaymentAmount,amount," +
                        "DATE_FORMAT(pay_time,'%Y%m%d') as repaymentTime,DATE_FORMAT( actual_time,'%Y%m%d') as actualTime,state,penalty ,default_interest as defaultInterest, red_amount as redMoney from mag_repayment where order_id = '"+orderId+"' and state !=0 order by pay_time asc");
                List<Map> unpayList = new ArrayList<Map>();
                List<Map> payedList = new ArrayList<Map>();
                String settleRemark = "";
                if(repaidList!=null && repaidList.size()>0){
                    for(int j=0;j<repaidList.size();j++){
                        Map raymentMap = new HashMap();
                        Map rayMap = repaidList.get(j);
                        String state = rayMap.get("state").toString();//还款计划状态
                        String penalty = (rayMap.get("penalty") == null || rayMap.get("penalty").toString().equals(""))?"0":rayMap.get("penalty").toString();//逾期利息
                        String repaymentAmount = rayMap.get("repaymentAmount").toString();//每月还款额
                        String repaymentTime = rayMap.get("repaymentTime").toString();//还款时间
                        String periods = rayMap.get("periods").toString();//期数
                        String repaymentId= rayMap.get("repaymentId").toString();
                        String nowTime = DateUtils.getDateString(new Date()).substring(0,8);
                        double yuMoney = 0.00;//逾期金额
                        double jianMoney = 0.00;//减免金额
                        double fuwuMoney = 0.00;//服务包金额

                        String remark = "";//说明
                        int endTime = Integer.parseInt(repaymentTime.substring(0,8));//还款结束时间

                        //每期所有服务包的钱
                        String sql = "select id,repayment_id as repaymentId,package_name as packageName,period,`month`," +
                                "type,amount, state from service_package_repayment where repayment_id = '"+repaymentId+"' order by period";
                        List serPcgList = sunbmpDaoSupport.findForList(sql);
                        if(serPcgList != null&& serPcgList.size() > 0){
                            for (int k = 0; k < serPcgList.size();k++){
                                Map map = (Map)serPcgList.get(k);
                                double amount = Double.valueOf(map.get("amount").toString());
                                //服务包费用
                                fuwuMoney = DoubleUtils.add(fuwuMoney, amount);
                            }
                        }

                        if("1".equals(state)){//未还
                            if (null != settleMap && "0".equals(settleMap.get("settleType")))
                            {//提前结清是否存在
                                settleRepayMoney = DoubleUtils.add(DoubleUtils.add(settleRepayMoney, Double.valueOf(rayMap.get("amount").toString())), fuwuMoney);
                            }

                            remark = repaymentTime.substring(4,6)+"月"+repaymentTime.substring(6,8)+"日"+"到期";
                        }else if("2".equals(state)){ //已还
                            alreadyNum ++;
                            if (rayMap.get("actual_amount") == null || "".equals(rayMap.get("actual_amount")))
                            {
                                settleRemark = "已结清";
                                if (null != settleMap && "0".equals(settleMap.get("settleType")))
                                {//提前结清是否存在
                                    settleRepayMoney = DoubleUtils.add(DoubleUtils.add(settleRepayMoney, Double.valueOf(rayMap.get("amount").toString())), fuwuMoney);
                                }
                            }
                            remark = "已结清";
                            if (Double.valueOf(penalty) != 0)
                            {//存在逾期时
                                double defaultInterest = Double.valueOf(rayMap.get("defaultInterest") == null ? "0" : rayMap.get("defaultInterest").toString());
                                yuMoney = DoubleUtils.add(Double.valueOf(penalty), defaultInterest);
                            }
                        }else if("3".equals(state)){ //逾期
                            if (null != settleMap && "0".equals(settleMap.get("settleType")))
                            {//正常结清
                                settleRepayMoney = DoubleUtils.add(DoubleUtils.add(settleRepayMoney, Double.valueOf(rayMap.get("money").toString())), fuwuMoney);
                            }

                            double defaultInterest = Double.valueOf((rayMap.get("defaultInterest") == null || "".equals(rayMap.get("defaultInterest"))) ? "0" : rayMap.get("defaultInterest").toString());
                            if (null == settleMap )
                            {
                                unpaid ="1";
                            }
                            remark = "逾期";
                            yuMoney = DoubleUtils.add(Double.valueOf(penalty), defaultInterest);
                        }else if("4".equals(state)) { //还款确认中
                            alreadyNum++;
                            remark = "还款确认中";
                            if (Double.valueOf(penalty) != 0)
                            {//存在逾期时
                                double defaultInterest = Double.valueOf(rayMap.get("defaultInterest") == null ? "0" : rayMap.get("defaultInterest").toString());
                                yuMoney = DoubleUtils.add(Double.valueOf(penalty), defaultInterest);
                            }
                            if (rayMap.get("actual_amount") == null || "".equals(rayMap.get("actual_amount")))
                            {
                                settleRemark = "还款确认中";
                                if (null != settleMap && "0".equals(settleMap.get("settleType")))
                                {//提前结清是否存在
                                    settleRepayMoney = DoubleUtils.add(DoubleUtils.add(settleRepayMoney, Double.valueOf(rayMap.get("amount").toString())), fuwuMoney);
                                }
                            }
                        }

                        //查询每期还款是否有减免
                        String derateSql = "select id,derate_amount as derateAmount,effective_data as  effectiveData,state from mag_derate where repayment_id = '"+repaymentId+"' and ((approval_state = '1' and effective_data > '"+DateUtils.getDateString(new Date())+"' and state='1') or state='3')";
                        Map derateMap = sunbmpDaoSupport.findForMap(derateSql);
                        if (derateMap != null && !"2".equals(state) && !"4".equals(state)){//未还或逾期时
                            double derateAmount = Double.valueOf(derateMap.get("derateAmount").toString());
                            jianMoney = derateAmount;
                            raymentMap.put("derateCode","1");
                            raymentMap.put("derate",derateMap);
                            derateAllCode = "1";
                        }else{//已还或确认中
                            if (null != derateMap && derateMap.get("state") != null && "3".equals(derateMap.get("state")))
                            {//已使用的减免
                                jianMoney = Double.valueOf(derateMap.get("derateAmount").toString());
                            }
                            raymentMap.put("derateCode","0");
                        }

                        Double total = DoubleUtils.add(DoubleUtils.sub(DoubleUtils.add(Double.valueOf(repaymentAmount), yuMoney), jianMoney), fuwuMoney);
                        String repayMoney = String.valueOf(total);//还款明细
                        if ("2".equals(state) || "4".equals(state))
                        {//已还或确认中时
                            if (null != rayMap.get("redMoney") && !"".equals(rayMap.get("redMoney")) && !"0".equals(rayMap.get("redMoney")))
                            {//存在红包时
                                Double  actualAmount= (rayMap.get("actual_amount") == null || "".equals(rayMap.get("actual_amount"))) ? 0 : Double.valueOf(rayMap.get("actual_amount").toString());//实际还款金额
                                if (DoubleUtils.sub(total, actualAmount) < Double.valueOf(rayMap.get("redMoney").toString()))
                                {//红包金额大于还款金额
                                    repayMoney += "(红包抵扣" + DoubleUtils.sub(total, actualAmount)+")";
                                }
                                else
                                {//红包金额小于还款金额
                                    repayMoney += "(红包抵扣" + Double.valueOf(rayMap.get("redMoney").toString()) + ")";
                                }
                            }

                        }
                        else
                        {
                            repayMoney = String.valueOf(DoubleUtils.add(total, jianMoney));
                        }

                        raymentMap.put("periodsNum",periods);
                        raymentMap.put("monthPay",repayMoney);//每月还款额
                        if ("1".equals(raymentMap.get("derateCode")))
                        {
                            raymentMap.put("unpaidDerateAmount",DoubleUtils.sub(Double.valueOf(repayMoney), jianMoney));
                        }
                        raymentMap.put("repaymentTime",repaymentTime);//每期到期日期
                        raymentMap.put("remark",remark);//还款说明
                        raymentMap.put("repaymentId",repaymentId);//还款id
                        raymentMap.put("state",state);//期数状态

//                        if(beginTime < timeNow && timeNow <=endTime || periods.equals("1")&&"1".equals(state)||/*periods.equals("1")&&*/ "2".equals(state)||"3".equals(state)||"4".equals(state)){
                        //已还、逾期和本月或最近的未还返回
                        if("2".equals(state) || "3".equals(state) || "4".equals(state) || !"".equals(currentTime) && endTime == Integer.parseInt(currentTime.substring(0,8)) || repaymentTime.substring(0,6).equals(month) ){
                            if ("2".equals(state) || "4".equals(state))
                            {
                                if (null != rayMap.get("actual_amount") && !"".equals(rayMap.get("actual_amount")))//非提前结清
                                    payedList.add(raymentMap);
                            }
                            else
                            {
                                unpayList.add(raymentMap);
                                if ("1".equals(state))
                                {//未还
                                    unpaidAmount = DoubleUtils.add(Double.valueOf(raymentMap.get("monthPay").toString()), unpaidAmount);
                                }
                                else if ("3".equals(state))
                                {//逾期
                                    overdueAllAmount = DoubleUtils.add(Double.valueOf(raymentMap.get("monthPay").toString()), overdueAllAmount);
                                    if ("1".equals(raymentMap.get("derateCode")))
                                    {
                                        derateAllAmount = DoubleUtils.add(Double.valueOf(raymentMap.get("unpaidDerateAmount").toString()), derateAllAmount);
                                    }
                                    else
                                    {
                                        derateAllAmount = DoubleUtils.add(Double.valueOf(raymentMap.get("monthPay").toString()), derateAllAmount);
                                    }
                                }
                                unpaid = "1";
                            }
                        }
                    }
                }else{
                    returnMap.put("flag", false);
                }

                if (null != settleMap)
                {//提前还款时
                    Map raymentMap = new HashMap();
                    raymentMap.put("repaymentId","");//还款id
                    raymentMap.put("derateCode","0");
                    if ("0".equals(settleMap.get("settleType")))
                    {//正常结清
                        raymentMap.put("periodsNum","-1");
                        raymentMap.put("monthPay",String.format("%.2f",DoubleUtils.add(Double.valueOf(settleMap.get("settleFee").toString()), Double.valueOf(settleRepayMoney))));//每月还款额
                        raymentMap.put("repaymentTime","");//每期到期日期
                        raymentMap.put("remark","".equals(settleRemark) ? "未结清" : settleRemark);//还款说明
                    }
                    else
                    {//非正常结清
                        raymentMap.put("periodsNum","0");
                        raymentMap.put("state","1");//期数状态
                        raymentMap.put("monthPay",String.format("%.2f",Double.valueOf(settleMap.get("settleAmount").toString())));//每月还款额

                        if (null != settleMap.get("effectiveTime"))
                        {//非正常结清的有效时间
                            raymentMap.put("remark","".equals(settleRemark) ? settleMap.get("effectiveTime").toString().substring(4,6)+"月"+settleMap.get("effectiveTime").toString().substring(6,8)+"日"+"到期" : settleRemark);//还款说明
                            raymentMap.put("repaymentTime", settleMap.get("effectiveTime").toString().substring(4,6)+"月"+settleMap.get("effectiveTime").toString().substring(6,8)+"日"+"到期");
                        }
                        else
                        {
                            raymentMap.put("remark","".equals(settleRemark) ? "" : settleRemark);//每期到期日期
                            raymentMap.put("repaymentTime","");//每期到期日期
                        }
                    }
                    repayList.add(raymentMap);
                    unpaid = "0";
                    derateAllCode += "0";
                    unpaidAllAmount = DoubleUtils.add(unpaidAllAmount, Double.valueOf(raymentMap.get("monthPay").toString()));
//                    derateAllAmount += 0.00;
                    unpaidAmount = DoubleUtils.add(Double.valueOf(raymentMap.get("monthPay").toString()), unpaidAmount);
//                    overdueAllAmount += 0.00;

                }
                else
                {//无提前还款时
                    repayList.addAll(unpayList);
                }
                repayList.addAll(payedList);
                resultMap.put("unpaid",unpaid);//是否当前月有还款,获取以前有逾期
                resultMap.put("alreadyNum",String.format("%.0f",alreadyNum));//已还多少期
                resultMap.put("repayList",repayList);//还款计划列表
                if (repayList.size() > 0){
                    repayInfoList.add(resultMap);//将商品信息和还款计划放到集合里面
                    orderNum++;
                }
            }

            unpaidAllAmount = DoubleUtils.add(unpaidAmount, derateAllAmount);
            returnMap.put("derateAllAmount",String.format("%.2f",derateAllAmount));//减免后逾期总金额
            returnMap.put("derateAllCode",derateAllCode);//所有逾期中是否有减免
            returnMap.put("unpaidAmount",String.format("%.2f",unpaidAmount));//当月未还总额
            returnMap.put("overdueAllAmount",String.format("%.2f",overdueAllAmount));//逾期待还
            returnMap.put("unpaidAllAmount",String.format("%.2f",unpaidAllAmount));//当前未还总额
            returnMap.put("repayInfoList",repayInfoList);//还款信息列表
            if (orderNum > 0)
            {
                returnMap.put("flag", true);
            }
            else
            {
                returnMap.put("flag", false);
            }
        }else{
            returnMap.put("flag", false);
        }
        return returnMap;
    }

    //获取当期初始时间
    public static String getTime (String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(time));
            calendar.add(Calendar.MONTH,-1);
            calendar.add(Calendar.DAY_OF_MONTH,1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dangqi =sdf.format(calendar.getTime());
        return dangqi;
    }


    /**
     * 秒付金服-获取一笔还款计划的详情
     * repaymentId 还款计划id
     * orderId  订单id
     * @return
     * @throws Exception
     */
    @Override
    public Map getRepayDetails(String repaymentId, String orderId,String customerId) {
        //查询该客户的开户信息
        String accountSql = "select bank_card as bankCard,account_bank as bankName from mag_customer_account where CUSTOMER_ID = '"+customerId+"'";
        Map map = sunbmpDaoSupport.findForMap(accountSql);
        String bankCard = map.get("bankCard").toString();//银行卡号
        String bankName = map.get("bankName").toString();//开户行
        double alreadyNum = 0;//已还订单数
        Map merchandiseMap = new HashMap();
        String orderSql = "select id,order_no as orderNo,amount,state,merchant_id as merchantId,merchant_name as merchantName,merchandise_type as merchandiseType," +
                "merchandise_type_id as merchandiseTypeId,merchandise_brand as merchandiseBrand,merchandise_brand_id as merchandiseBrandId,merchandise_model as merchandiseModel," +
                "merchandise_version as merchandiseVersion,merchandise_version_id as merchandiseVersionId,merchandise_url as merchandiseUrl,state,alter_time as alterTime " +
                "from mag_order where ='" + orderId+ "' and order_type='2' ORDER BY alter_time asc";
        Map orderMap = sunbmpDaoSupport.findForMap(orderSql);
        String merchandiseUrl = orderMap.get("merchandiseUrl").toString();//商品缩略图
        String merchandiseVersion = orderMap.get("merchandiseVersion").toString();//商品版本
        String merchandiseBrand = orderMap.get("merchandiseBrand").toString();//商品品牌
        String merchandiseModel = orderMap.get("merchandiseModel").toString();//商品型号
        String merchantName = orderMap.get("merchantName").toString();//商户名字
        merchandiseMap.put("merchandiseBrand",merchandiseBrand);//商品品牌
        merchandiseMap.put("merchandiseModel",merchandiseModel);//商品型号
        merchandiseMap.put("merchandiseVersion",merchandiseVersion);//商品版本
        merchandiseMap.put("merchandiseUrl",merchandiseUrl);//商品缩略图
        merchandiseMap.put("merchantName",merchantName);//商户名字
        merchandiseMap.put("bankCard",bankCard);//银行卡号
        merchandiseMap.put("bankName",bankName);//开户行
        List repayList = new ArrayList();//还款计划list
        List<Map> repaidList = sunbmpDaoSupport.findForList("select id as repaymentId ,pay_count as periods,repayment_amount as repaymentAmount," +
                "DATE_FORMAT(pay_time,'%Y%m%d') as repaymentTime,state,penalty,overdue_days as overdueDays from mag_repayment,default_interest as defaultInterest where order_id = '"+orderId+"' and state !=0 and state !=4 order by pay_time desc");
        if(repaidList!=null && repaidList.size()>0){
            int repaidSize = repaidList.size();
            for(int i=0;i<repaidSize;i++){
                Map raymentMap = new HashMap();
                Map rayMap = repaidList.get(i);
                String repayId = rayMap.get("id").toString();
                String findSvcPcgSql = "select amount from service_package_repayment where repayment_id = '"+repayId+"'";
                List svcPcgList = sunbmpDaoSupport.findForList(findSvcPcgSql);
                double svcAmount = 0.00;
                if(svcPcgList !=null && svcPcgList.size()>0){
                    int svcPcgSize = svcPcgList.size();
                    for(int j=0;j<svcPcgSize;j++){
                        Map svcPcgMap =(Map) svcPcgList.get(j);
                        svcAmount += svcPcgMap.get("amount")==null?0.00:Double.valueOf(svcPcgMap.get("amount").toString());
                    }
                }
                String state = rayMap.get("state").toString();//还款计划状态
                String penalty = rayMap.get("penalty").toString();//逾期利息
                String repaymentAmount = rayMap.get("repaymentAmount").toString();//每月还款额
                String repaymentTime = rayMap.get("repaymentTime").toString();//还款时间
                String periods = rayMap.get("periods").toString();//期数
                String overdueDays = rayMap.get("overdue_days").toString();//逾期天数
                double monthPay = 0.00;
                String remark = "";//说明
                if("1".equals(state)){//未还
                    monthPay = Double.valueOf(repaymentAmount);
                    remark = repaymentTime.substring(4,6)+"月"+repaymentTime.substring(6,8)+"日"+"到期";
                }else if("2".equals(state)){//已还
                    alreadyNum++;
                }else if("3".equals(state)){//逾期
                    double defaultInterest = Double.valueOf(rayMap.get("defaultInterest").toString());
                    monthPay = Double.valueOf(repaymentAmount) + Double.valueOf(penalty)+defaultInterest;
                    remark = "逾期"+overdueDays+"天";
                }else if("4".equals(state)){//确认中
                    alreadyNum++;
                }
                //查询每期还款是否有减免
                String derateSql = "select id,derate_amount as derateAmount,effective_data as  effectiveData from mag_derate where repayment_id = '"+repaymentId+"' and approval_state = '1' and effective_data > '"+DateUtils.getDateString(new Date())+"'";
                Map derateMap = sunbmpDaoSupport.findForMap(derateSql);
                if (derateMap != null){
                    double derateAmount = Double.valueOf(derateMap.get("derateAmount").toString());
                    String unpaidDerateAmount = String.format("%.2f",monthPay-derateAmount);//当前期数减免后的金额
                    raymentMap.put("derateCode","1");
                    raymentMap.put("unpaidDerateAmount",unpaidDerateAmount);
                    raymentMap.put("derate",derateMap);
                }else{
                    raymentMap.put("derateCode","0");
                }

                raymentMap.put("alreadyNum",alreadyNum);//已还期数
                raymentMap.put("svcAmount",svcAmount);//每月服务包费用
                raymentMap.put("monthPay",String.format("%.2f",monthPay));//每月还款额
                raymentMap.put("repaymentTime",repaymentTime);//每期到期日期
                raymentMap.put("overdueM",String.format("%.2f",Double.valueOf(penalty)));//逾期利息

                raymentMap.put("remark",remark);//还款说明
                raymentMap.put("periods",periods);//还款期数
                raymentMap.put("state",state);//期数状态
                repayList.add(i,raymentMap);
            }
            merchandiseMap.put("repayList", repayList);
            merchandiseMap.put("flag", true);
        }else{
            merchandiseMap.put("flag", false);
        }
        return merchandiseMap;
    }

    @Override
    public Map getRepayDetailList(String orderId) {
        //查询该客户的开户信息
        String bankAndOrderSql = "select mo.periods,mo.merchandise_url as merchandiseUrl,mo.customer_id,mca.bank_card as bankCard,mca.account_bank as bankName,mo.merchant_name as merchantName,mo.merchandise_brand as merchandiseBrand,mo.merchandise_model as merchandiseModel,mo.merchandise_version as merchandiseVersion,mo.periods from mag_order mo LEFT JOIN mag_customer_account mca on mo.customer_id = mca.customer_id where mo.id = '"+orderId+"'";
        Map map = sunbmpDaoSupport.findForMap(bankAndOrderSql);
        String bankCard = map.get("bankCard").toString();//银行卡号
        String bankName = map.get("bankName").toString();//开户行
        String periods = map.get("periods").toString();//期数
        Map merchandiseMap = new HashMap();
        String orderSql = "select id,order_no as orderNo,amount,state,merchant_id as merchantId,merchant_name as merchantName,merchandise_type as merchandiseType," +
                "merchandise_type_id as merchandiseTypeId,merchandise_brand as merchandiseBrand,merchandise_brand_id as merchandiseBrandId,merchandise_model as merchandiseModel," +
                "merchandise_version as merchandiseVersion,merchandise_version_id as merchandiseVersionId,merchandise_url as merchandiseUrl,state,alter_time as alterTime " +
                "from mag_order where id='" + orderId+ "' and order_type='2' ORDER BY alter_time asc";
        Map orderMap = sunbmpDaoSupport.findForMap(orderSql);
        String merchandiseUrl = orderMap.get("merchandiseUrl").toString();//商品缩略图
        String merchandiseVersion = orderMap.get("merchandiseVersion").toString();//商品版本
        String merchandiseBrand = orderMap.get("merchandiseBrand").toString();//商品品牌
        String merchandiseModel = orderMap.get("merchandiseModel").toString();//商品型号
        String merchantName = orderMap.get("merchantName").toString();//商户名字
        merchandiseMap.put("merchandiseBrand",merchandiseBrand);//商品品牌
        merchandiseMap.put("merchandiseModel",merchandiseModel);//商品型号
        merchandiseMap.put("merchandiseVersion",merchandiseVersion);//商品版本
        merchandiseMap.put("merchandiseUrl",merchandiseUrl);//商品缩略图
        merchandiseMap.put("merchantName",merchantName);//商户名字
        merchandiseMap.put("bankCard",bankCard);//银行卡号
        merchandiseMap.put("bankName",bankName);//开户行
        merchandiseMap.put("periods",periods);//还款总期数
        String timeSql = "select pay_time as payTime from mag_repayment where order_id = '"+orderId+"' and state = '1' order by pay_time asc LIMIT 1";
        Map timeMap = sunbmpDaoSupport.findForMap(timeSql);
        String currentTime = "";
        if(timeMap!=null ){
            currentTime = timeMap.get("payTime").toString();//当期还款时间
        }
        String waitPaySql = "select id as waitpayId,repayment_amount as repaymentAmount,pay_count as payCount,pay_time as payTime from mag_repayment  where order_id = '"+orderId+"' and state='1' order by pay_time asc";
        List<Map> waitPayList = sunbmpDaoSupport.findForList(waitPaySql);
        double waitPayMoney = 0.00;
        String nowTime = DateUtils.getDateString(new Date());
        List<Map> waitPays = new ArrayList<Map>();
        if(waitPayList != null && waitPayList.size() > 0){
            //获取当前月
            Calendar cal = Calendar.getInstance();
            String month = DateUtils.formatDate("yyyyMM");
            for (Map waitPayMap : waitPayList)
            {
                String payTime = waitPayMap.get("payTime").toString();
                String payCount = waitPayMap.get("payCount").toString();
//                int beginTime = Integer.parseInt(getTime(payTime.substring(0,8)));//每期起始时间
                int endTime = Integer.parseInt(payTime.substring(0,8));//还款结束时间
//                int timeNow = Integer.parseInt(nowTime.substring(0,8));//当前时间
                String repaymentTime = waitPayMap.get("payTime").toString();//还款时间
                if (endTime == Integer.parseInt(currentTime.substring(0,8)) || repaymentTime.substring(0,6).equals(month) ) {//本期
                    String id = waitPayMap.get("waitpayId").toString();
                    double waitMoney = Double.valueOf(waitPayMap.get("repaymentAmount").toString());//本期待还金额
                    String waitPcgSql = "select amount from service_package_repayment where repayment_id = '" + id + "'";
                    List waitPcgList = sunbmpDaoSupport.findForList(waitPcgSql);
                    double waitPcgAmount = 0.00;//服务包费用
                    if (waitPcgList != null && waitPcgList.size() > 0) {
                        int waitPcgSize = waitPcgList.size();
                        for (int i = 0; i < waitPcgSize; i++) {
                            Map waitPcgMap = (Map) waitPcgList.get(i);
                            double amount = Double.valueOf(waitPcgMap.get("amount").toString());
                            waitMoney = DoubleUtils.add(waitMoney, amount);
                            waitPcgAmount = DoubleUtils.add(waitPcgAmount, amount);
                        }
                    }

                    waitPayMoney = DoubleUtils.add(waitPayMoney, waitMoney);
                    waitPayMap.put("waitMoney", String.format("%.2f", waitMoney));
                    waitPayMap.put("waitPcgAmount", String.format("%.2f", waitPcgAmount));
                    waitPays.add(waitPayMap);
                }
            }
            merchandiseMap.put("waitPayMap", waitPays);
            merchandiseMap.put("waitPayMoney", waitPayMoney);
        }
        String alreadySql = "select count(*) as alreadyNum from mag_repayment where order_id = '"+orderId+"' and (state = '2' or state='4')";
        Map alreadyMap = sunbmpDaoSupport.findForMap(alreadySql);
        String alreadyNum = alreadyMap.get("alreadyNum").toString();//已还期数
        merchandiseMap.put("alreadyNum",alreadyNum);
        String avgSql = "select repayment_amount as avgAmount from mag_repayment where order_id = '"+orderId+"' ORDER BY pay_time LIMIT 1";
        Map avgMap = sunbmpDaoSupport.findForMap(avgSql);
        String avgAmount = avgMap.get("avgAmount").toString();
        merchandiseMap.put("avgAmount",avgAmount);//每期应还
        List overList = new ArrayList();
        double overAllMoney = 0.00;//逾期总还金额
        String overPaySql = "select id,penalty,default_interest as defaultInterest,repayment_amount as repaymentAmount,pay_count as payCount,pay_time as payTime from mag_repayment where order_id = '"+orderId+"' and state='3' ORDER BY pay_time desc";
        List overPayList = sunbmpDaoSupport.findForList(overPaySql);
        if(overPayList!=null && overPayList.size()>0){
            int overPaySize = overPayList.size();
            for(int i=0;i<overPaySize;i++){
                Map overPayMap =(Map) overPayList.get(i);
                String id = overPayMap.get("id").toString();
                String overPayCount = overPayMap.get("payCount").toString();
                String deRateSql = "select id as deRateId,derate_amount as deRateAmount,effective_data as effectiveData from mag_derate where repayment_id = '"+id+"' and approval_state = '1'  and effective_data > '"+nowTime+"'";
                Map deRateMap = sunbmpDaoSupport.findForMap(deRateSql);
                double deRateAmount = 0.00;
                Map overMap = new HashMap();
                if(deRateMap!=null){
                    String derate_amount = deRateMap.get("deRateAmount")==null?"":deRateMap.get("deRateAmount").toString();//减免金额
                    String deRateId = deRateMap.get("deRateId")==null?"":deRateMap.get("deRateId").toString();//减免id
                    String effectiveData = deRateMap.get("effectiveData")==null?"":deRateMap.get("effectiveData").toString();//减免到期时间
                    deRateAmount = Double.valueOf(derate_amount.equals("")?"0.00":derate_amount);
                    overMap.put("deRateAmount",deRateAmount);//减免金额
                    overMap.put("deRateId",deRateId);
                    overMap.put("effectiveData",effectiveData);
                }
                overMap.put("overId",id);
                double repaymentAmount = Double.valueOf(overPayMap.get("repaymentAmount").toString());
                double penalty = Double.valueOf(overPayMap.get("penalty").toString());
                String default_interest = overPayMap.get("defaultInterest")==null?"":overPayMap.get("defaultInterest").toString();
                double defaultInterest = Double.valueOf(default_interest.equals("")?"0.00":default_interest);//罚息
                String overPcgSql = "select amount from service_package_repayment where repayment_id = '"+id+"'";
                List overPcgList = sunbmpDaoSupport.findForList(overPcgSql);
                double overPcgAmount = 0.00;//逾期服务包费
                if(overPcgList!=null && overPcgList.size()>0){
                    int overPcgSize = overPcgList.size();
                    for(int j=0;j<overPcgSize;j++){
                        Map overPcgMap =(Map) overPcgList.get(j);
                        double amount = Double.valueOf(overPcgMap.get("amount").toString());
                        overPcgAmount = DoubleUtils.add(overPcgAmount, amount);
                    }
                }
                double overMoney = DoubleUtils.sub(DoubleUtils.add(DoubleUtils.add(DoubleUtils.add(repaymentAmount, penalty), overPcgAmount),defaultInterest),deRateAmount);//当期逾期待还总金额
                overAllMoney = DoubleUtils.add(overAllMoney, overMoney);
                overMap.put("repaymentAmount",String.format("%.2f",repaymentAmount));//应还
                overMap.put("penalty",String.format("%.2f",penalty));//逾期利息
                overMap.put("payCount",overPayCount);//第几期
                overMap.put("overPcgAmount",String.format("%.2f",overPcgAmount));//服务包
                overMap.put("overMoney",String.format("%.2f",overMoney));//当期逾期总还金额
                overMap.put("defaultInterest",defaultInterest);//当期逾期总还金额
                overList.add(i,overMap);
            }
            merchandiseMap.put("overList",overList);//逾期list
            merchandiseMap.put("overAllMoney",String.format("%.2f",overAllMoney));//逾期总额
        }else{
            merchandiseMap.put("overList",null);//逾期list
            merchandiseMap.put("overAllMoney",String.format("%.2f",overAllMoney));//逾期总额
        }
        double allMoney = DoubleUtils.add(waitPayMoney, overAllMoney);
        merchandiseMap.put("allMoney",String.format("%.2f",allMoney));//总额
        return merchandiseMap;
    }

    @Override
    public Map getOverDueRemrak() {
        String sql = "SELECT content_no_bq,content FROM mag_template where template_type = '13' and platform_type = '1'";
        return sunbmpDaoSupport.findForMap(sql);
    }

    /**
     * 根据订单生成还款计划
     * @param orderId
     */
    public void addRepayment(String orderId){
        //查询出订单的所有信息
        String orderSql = "select id,user_id as userId,customer_id as customerId,order_no as orderNo,periods as periods,sex_name as sexName,customer_id as customerId,customer_name as customerName," +
                "merchant_id as merchantId,merchant_name as merchantName,merchandise_brand as merchandiseBrandName,merchandise_model as merchandiseModelName," +
                "predict_price as downPayMoney,emp_id as empId,amount as amount,diy_type,diy_days," +
                "rate,commodity_state as commodityState,merchandise_version as merchandiseVersionName,merchandise_url as merchandiseUrl,state as state ,is_sign as isSign " +
                "from mag_order where id = '"+orderId+"' and order_type='2'";
        Map orderMap = sunbmpDaoSupport.findForMap(orderSql);
        String userId = orderMap.get("userId").toString();
        String customerId = orderMap.get("customerId").toString();

        String downPayMoney = "";//首付金额
        String rate = "";//利率
        Double repayAmount = 0.0;//所有还款金额
        Double rateAmount = 0.0;//所有利息
        Double serAllAmount = 0.0;//所有服务包的钱
        String amount = "";//总金额
        String diyType = "";
        double repayment_amount1 = 0.00;
        double lixi = 0.00;
        if (orderMap != null) {
            diyType = String.valueOf(orderMap.get("diy_type"));
            if(StringUtils.isEmpty(diyType)){
                diyType="0";
            }
            rate = orderMap.get("rate") == null ? "" : orderMap.get("rate").toString();
            amount = orderMap.get("amount") == null ? "" : orderMap.get("amount").toString();
            downPayMoney = orderMap.get("downPayMoney") == null ? "" : orderMap.get("downPayMoney").toString();
        }
        //获取产品的费率和期数信息
        double periods = Double.valueOf(orderMap.get("periods").toString());//总期数
        double fenqiMoney = Double.valueOf(amount == "" ? "0" : amount) - Double.valueOf(downPayMoney == "" ? "0" : downPayMoney);//分期总金额
        //公用的方法
        Map returnMap = appOrderService.calculationRepayment(orderMap);
        repayment_amount1 = (Double) returnMap.get("monthPay");
        lixi = (Double) returnMap.get("lixi");
        double intMoney = (Double) returnMap.get("intMoney");
        double surplusMoney = fenqiMoney % periods;
        String repayment_amount = String.format("%.2f", Double.valueOf(repayment_amount1));
        String surplus_Money = String.format("%.2f", Double.valueOf(surplusMoney));
        double benjin = intMoney;

      /*  String downPayMoney = "";//首付金额
        String rate = "";//利率
        Double repayAmount = 0.0;//所有还款金额
        Double rateAmount = 0.0;//所有利息
        Double serAllAmount = 0.0;//所有服务包的钱
        String amount = "";//总金额
        if(orderMap!=null){
            rate = orderMap.get("rate")==null?"":orderMap.get("rate").toString();
            amount = orderMap.get("amount")==null?"":orderMap.get("amount").toString();
            downPayMoney = orderMap.get("downPayMoney")==null?"":orderMap.get("downPayMoney").toString();
        }
        //获取产品的费率和期数信息
        double monthRate=Double.valueOf(rate)/100 ;
        int periods = Integer.valueOf(orderMap.get("periods").toString());//总期数
        double contractAmount = Double.valueOf(amount.equals("")?"0":amount);//金额
        double fenqiMoney = Double.valueOf(amount.equals("")?"0":amount)- Double.valueOf(downPayMoney.equals("")?"0":downPayMoney);//分期总金额--放款总金额
        //算出每月应还款金额
        String Money=String.valueOf(fenqiMoney/periods);
        String intNumber = Money.substring(0,Money.indexOf("."));
        double intMoney=Double.valueOf(intNumber);
        double repayment_amount1 =intMoney+ fenqiMoney*monthRate;
        double surplusMoney=fenqiMoney%periods;
        String repayment_amount = String.format("%.2f",Double.valueOf(repayment_amount1));
        String surplus_Money = String.format("%.2f",Double.valueOf(surplusMoney));
        double benjin = intMoney;
        double lixi = fenqiMoney*monthRate;*/
        //double repayment_amount = AverageCapitalPlusInterestUtils.getPerMonthPrincipalInterest(fenqiMoney,monthRate,periods);
        //每期应还本金
       // Map<Integer,Double> amountMap = AverageCapitalPlusInterestUtils.getPerMonthPrincipal(fenqiMoney,monthRate*12,periods);
        //每期应还利息
       // Map<Integer,Double> feeMap = AverageCapitalPlusInterestUtils.getPerMonthInterest(fenqiMoney,monthRate*12,periods);
        String id = "";
        String payCount = "";//每期期数
        String pay_time = "";//应还款时间
        String createTime = DateUtils.getDateString(new Date());//创建时间
        String state = "1";//状态0无效,1未还，2已还，3逾期 4.还款确认中
        List list = new ArrayList();

        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        String serPcgSql = "select package_name as packageName,period_collection as period,month as month,type as type," +
                "amount_collection as amount,state as state from mag_servicepag_order where type != '1' and order_id" +
                " = '"+orderId+"'";
        List serPcgList = sunbmpDaoSupport.findForList(serPcgSql);
        DecimalFormat df = new DecimalFormat("#.00");
        String payTime = "";//放款表最后还款时间
        String uuId = UUID.randomUUID().toString();//放款表id
        for(int i=0;i<periods;i++){
            String avgAmount = String.format("%.2f",Double.valueOf(benjin));//df.format(amountMap.get(i+1));//本金
            String fee = String.format("%.2f",Double.valueOf(lixi));//利息
            rateAmount +=Double.valueOf(fee);
            payCount = i+1+"";
            int day = now.get(Calendar.DAY_OF_MONTH);//当前天
            if ("0".equals(diyType)) {
                month = month + 1;
                if (month == 13) {
                    year++;
                    month = 1;
                }
                if (day > 28) {
                    int last = getNextLastDay(year, month);//获取下月最大天数
                    if (day > last) {
                        day = last;
                    }
                }
                String payDay = day + "";
                if (day < 10) {
                    payDay = "0" + day;
                }
                if (month < 10) {
                    pay_time = year + "0" + month + "" + payDay;
                } else {
                    pay_time = year + "" + month + "" + payDay;
                }
                //取最后一期还款时间插入放款表
                if (i == periods - 1) {
                    payTime = pay_time;
                }
            } else {
                int diyDays = Integer.valueOf(orderMap.get("diy_days").toString()); //自定义天数
                now.add(Calendar.DAY_OF_MONTH, diyDays);
                Date date = now.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                pay_time = sdf.format(date);
                //取最后一期还款时间插入放款表
                if (i == periods - 1) {
                    payTime = pay_time;
                }
            }
            id = UUID.randomUUID().toString();//还款计划id
            if(serPcgList !=null && serPcgList.size()>0){
                int serSize = serPcgList.size();
                for(int j=0;j<serSize;j++){
                    Map serMap = (Map) serPcgList.get(j);
                    int period = Integer.valueOf(serMap.get("period").toString());
                    if(i<period){
                        String serviceId = UUID.randomUUID().toString();//服务包还款明细id
                        String packageName = serMap.get("packageName")==null?"":serMap.get("packageName").toString();//服务包名称
                        String serMonth = serMap.get("month")==null?"":serMap.get("month").toString();//提前还款月数
                        String type = serMap.get("type")==null?"":serMap.get("type").toString();//服务包类型(1.前置还款 2.月前置还款 3.其他服务包)
                        String serAmount = serMap.get("amount")==null?"":serMap.get("amount").toString();//每期还款金额
                        repayAmount += Double.valueOf(serAmount);
                        serAllAmount += Double.valueOf(serAmount);
                        String addSvcPcgSql = "insert into service_package_repayment(id,repayment_id,package_name,period,month,type,amount,state,order_id,creat_time) values('"+serviceId+"','"+id+"','"+packageName+"','"+period+"','"+serMonth+"','"+type+"','"+serAmount+"','0','"+orderId+"','"+createTime+"')";
                        this.sunbmpDaoSupport.exeSql(addSvcPcgSql);
                    }
                }
            }
            if(i==periods-1){
                double repayment_amount2 =surplusMoney+repayment_amount1;
                repayment_amount = String.format("%.2f",Double.valueOf(repayment_amount2));
                double avgMoney=benjin+surplusMoney;
                 avgAmount= String.format("%.2f",Double.valueOf(avgMoney));
            }
            String sql = "insert into mag_repayment(id,loan_id,pay_count,fee,amount,repayment_amount,order_id,pay_time,create_time,state) values ('"+id+"','"+uuId+"','"+payCount+"','"+fee+"','"+avgAmount+"','"+repayment_amount+"','"+orderId+"','"+pay_time+"','"+createTime+"','"+state+"')";
            list.add(sql);
        }
        sunbmpDaoSupport.exeSql(list);
        repayAmount += rateAmount;
        //修改order表中总金额,总利息
        String updateOrderSql = "update mag_order set repay_money = '"+String.format("%.2f",repayAmount)+"',fee = '"+String.format("%.2f",rateAmount)+"' where id = '"+orderId+"'";
        sunbmpDaoSupport.exeSql(updateOrderSql);
        //插入放款表信息
        String loanAllAmount = String.format("%.2f",repayment_amount1*periods+serAllAmount);//还款总金额
        String loadSql = "insert into mag_loan(id,user_id,createtime,order_id,cust_id,state," +
                "amount,des,payable_amount,expiration_date,type,red_amount,derate_amount,penalty,returned_amount,unpaid_amount) values " +
                "('"+uuId+"','"+userId+"','"+createTime+"','"+orderId+"','"+customerId+"','1','"+fenqiMoney+"','合同签订生成数据'" +
                ",'"+loanAllAmount+"','"+payTime+"','1','0.00','0.00','0.00','0.00','"+loanAllAmount+"')";
        sunbmpDaoSupport.exeSql(loadSql);
    }

//    public static void main(String arg[]){
//        Calendar now = Calendar.getInstance();
//        int year = now.get(Calendar.YEAR);
//        int month = now.get(Calendar.MONTH) + 1;
//        for(int i=0;i<18;i++){
//            //int day = now.get(Calendar.DAY_OF_MONTH);//当前天
//            int day = 4;
//            month = month+1;
//            if(month==13){
//                year++;
//                month = 1;
//            }
//            if(day>28){
//                int last = getNextLastDay(year,month);//获取下月最大天数
//                if(day>last){
//                    day = last;
//                }
//            }
//            String nowDay = day+"";
//            if(day<10){
//                nowDay = "0"+day;
//            }
//            if(month<10){
//                System.out.println(year+"0"+month+""+nowDay);
//            }else{
//                System.out.println(year+""+month+""+nowDay);
//            }
//        }
//    }

    //获取下月最大天数
    public static int getNextLastDay(int year,int month){
        Calendar cal = Calendar.getInstance();
        cal.set(year,month - 1,1);
        int last = cal.getActualMaximum(Calendar.DATE);
        return last;
    }

    //还款中
    public Map repaying(String orderId){
        String orderSql = "select mo.merchant_name as merchantName,mo.merchandise_brand as merchandiseBrand,mo.merchandise_model as merchandiseModel,mo.merchandise_version as merchandiseVersion,mo.predict_price as predictPrice,mo.applay_money as applayMoney,mo.periods,mr.id as repaymentId,mr.repayment_amount as repaymentAmount from mag_order mo LEFT JOIN mag_repayment mr on mo.id = mr.order_id where mo.id = '"+orderId+"' LIMIT 0,1";
        Map orderMap = sunbmpDaoSupport.findForMap(orderSql);
        if(orderMap==null){
            orderMap.put("flag",false);
            return orderMap;
        }
        String alreadySql = "select id,actual_amount,actual_time from mag_repayment where  state = '2' and order_id = '"+orderId+"'";
        List alreadyList = sunbmpDaoSupport.findForList(alreadySql);
        if(alreadyList !=null && alreadyList.size()>0){
            orderMap.put("alreadyPeriods",alreadyList.size());
            orderMap.put("alreadyList",alreadyList);
        }
        orderMap.put("flag",true);
        return orderMap;
    }

    //全部结清
    @Override
    public Map settleAll(String orderId){
        //获取商品信息

        String bankAndOrderSql = "select mo.merchandise_url as merchandiseUrl,mo.customer_id,mca.bank_card as bankCard,mca.account_bank as bankName,mo.merchant_name as merchantName,mo.merchandise_brand as merchandiseBrand,mo.merchandise_model as merchandiseModel,mo.merchandise_version as merchandiseVersion,mo.periods from mag_order mo LEFT JOIN mag_customer_account mca on mo.customer_id = mca.customer_id where mo.id = '"+orderId+"'";
        Map retMap = sunbmpDaoSupport.findForMap(bankAndOrderSql);
        String settleType = "";//提前结清状态
        int repayNum = 0;//已还次数
        Map resultMap = new HashMap();
        resultMap.put("flag",true);
        /**查询一共几期**/
        String sqlForCount="select id as repaymentId,amount ,pay_count as periods,repayment_amount as repaymentAmount," +
                "DATE_FORMAT(pay_time,'%Y%m%d') as repaymentTime,state,penalty,overdue_days as overdueDays,default_interest as defaultInterest  " +
                "from mag_repayment where order_id = '"+orderId+"' order by pay_time asc";
        List<Map> repaymentList = sunbmpDaoSupport.findForList(sqlForCount);
        String everyRepay = "0";
        if (null != repaymentList && repaymentList.size() > 0)
        {
            for (Map m : repaymentList) {
                if ("2".equals(m.get("state")) || "4".equals(m.get("state")))
                    repayNum++;
            }
            everyRepay = String.valueOf(repaymentList.get(0).get("repaymentAmount"));
        }
        resultMap.put("everyRepay",String.format("%.2f",Double.valueOf(everyRepay)));//每期应还金额

        /*******************/
        //首先获取提前结清的数据
        String noSettleSql = "select id,settle_type as settleType ,settle_amount as amount,effective_time as effectiveTime,state,settle_fee as settleFee from mag_settle_record where" +
                " order_id = '"+orderId+"' and state = '1'";
        Map settleMap = sunbmpDaoSupport.findForMap(noSettleSql);

        if (null != settleMap)
        {
            String repaySql = "select count(1) as num from mag_repayment where settle_id='"+settleMap.get("id").toString()+"' and (state='2' or state='4')";
            Map repayMap = sunbmpDaoSupport.findForMap(repaySql);
            if (null != repayMap && Integer.valueOf(repayMap.get("num").toString()) > 0)
            {//提前结清中
                settleType = settleMap.get("settleType").toString();
                resultMap.put("amount",0);
                resultMap.put("repayed","1");
                resultMap.put("effectiveTime",settleMap.get("effectiveTime").toString());
                resultMap.put("settleType",settleType);
                resultMap.put("repayNum",String.valueOf(repayNum));
                resultMap.putAll(retMap);
                return resultMap;
            }
        }
        else
        {
            resultMap.put("amount",0);
            resultMap.put("repayed","1");
            resultMap.put("effectiveTime","");
            resultMap.put("settleType","");
            resultMap.put("repayNum",String.valueOf(repayNum));
            resultMap.putAll(retMap);
            return resultMap;
        }

        String nowTime = DateUtils.getDateString(new Date()).substring(0,8);
        //当存在非正常结清时
        if (settleMap != null && settleMap.size() > 0  && "1".equals(settleMap.get("settleType")) && Integer.parseInt(settleMap.get("effectiveTime").toString().substring(0,8))>=Integer.parseInt(nowTime)){
//            String countSql = "select count(id) as count,(select repayment_amount from mag_repayment where order_id = '"+orderId+"' group by repayment_amount ) " +
//                    "as repaymentAmount from mag_repayment where order_id ='"+orderId+"' and state = '2'";
//            Map countMap = sunbmpDaoSupport.findForMap(countSql);
//            resultMap.put("repayNum",countMap.get("count").toString());
//            resultMap.put("everyRepay",String.format("%.2f",Double.valueOf(countMap.get("repaymentAmount").toString())));
            settleType = settleMap.get("settleType").toString();
            resultMap.put("amount",String.format("%.2f",Double.valueOf(settleMap.get("amount").toString())));
            resultMap.put("effectiveTime",settleMap.get("effectiveTime").toString());
            resultMap.put("settleType",settleType);
            resultMap.put("repayNum",String.valueOf(repayNum));
            resultMap.putAll(retMap);
            return resultMap;
        }
        //正常结清
//        String settleSql = "select id,settle_type as settleType ,settle_fee as settleFee,effective_time as effectiveTime,state from mag_settle_record " +
//                "where order_id = '"+orderId+"' and settle_type = '0' and state = '1'";
//        Map settleMap = sunbmpDaoSupport.findForMap(settleSql);
        //如果正常结清有数据
        if (settleMap != null && settleMap.size() > 0){
            settleType = settleMap.get("settleType").toString();
            //获取所有还款计划
            List<Map> repaidList = sunbmpDaoSupport.findForList("select id as repaymentId,amount ,pay_count as periods,repayment_amount as repaymentAmount," +
                    "DATE_FORMAT(pay_time,'%Y%m%d') as repaymentTime,state,penalty,overdue_days as overdueDays,default_interest as defaultInterest  " +
                    "from mag_repayment where order_id = '"+orderId+"' and state !=0 and state !=4 and state !=2 order by pay_time desc");
            if(repaidList!=null && repaidList.size()>0) {
                Double  allPrincipalSum = 0.0;//所有未到期本金;
                Double  allPackSum = 0.0;//所有未到期本金;
                Double  nowRepay = 0.0;//当期待还总金额;
                Double  overdueMoney = 0.0;//逾期待还总额;
                Double settleFee =Double.valueOf(settleMap.get("settleFee").toString());//结清费用
                for (int i = 0; i <  repaidList.size(); i++) {
                    Map rayMap = repaidList.get(i);
                    String repayId = rayMap.get("repaymentId").toString();//还款计划Id
                    String payTime = rayMap.get("repaymentTime").toString();
                    String findSvcPcgSql = "select amount from service_package_repayment where repayment_id = '" + repayId + "'";
                    List svcPcgList = sunbmpDaoSupport.findForList(findSvcPcgSql);
                    double svcAmount = 0.00;
                    if (svcPcgList != null && svcPcgList.size() > 0) {
                        int svcPcgSize = svcPcgList.size();
                        for (int j = 0; j < svcPcgSize; j++) {
                            Map svcPcgMap = (Map) svcPcgList.get(j);
                            svcAmount = DoubleUtils.add(svcAmount, svcPcgMap.get("amount") == null ? 0.00 : Double.valueOf(svcPcgMap.get("amount").toString()));
                        }
                    }

                    int beginTime = Integer.parseInt(getTime(payTime.substring(0,8)));//每期起始时间
                    int endTime = Integer.parseInt(payTime.substring(0,8));//还款结束时间
                    int timeNow = Integer.parseInt(nowTime.substring(0,8));//当前时间
                    String state = rayMap.get("state").toString();//还款计划状态
                    String repaymentAmount = rayMap.get("repaymentAmount").toString();//每月还款额
                    String periods = rayMap.get("periods").toString();//期数
//                    if ("1".equals(state)) {//未还
//                        //当期待还
//                        if (beginTime < timeNow && timeNow < endTime || periods.equals("1")){
//                            nowRepay =Double.valueOf(repaymentAmount) + Double.valueOf(svcAmount);//当期金额 = 应还款金额+当期所有服务包的金额
//                        }else{
//                            Double principalSum =Double.valueOf(rayMap.get("amount").toString());
//                            allPrincipalSum += principalSum;//未到期总金额+=每期未到期本金
//                        }
//                        everyRepay = repaymentAmount;
//                    } else if ("3".equals(state)) {//逾期
//                        Double penalty = Double.valueOf(rayMap.get("penalty").toString());//逾期利息
//                        Double defaultInterest = Double.valueOf(rayMap.get("defaultInterest").toString());//罚息
//                        overdueMoney += penalty+defaultInterest+Double.valueOf(repaymentAmount)+svcAmount;
                    if ("1".equals(state) || "3".equals(state)) {
                        Double principalSum =Double.valueOf(rayMap.get("amount").toString());
                        allPrincipalSum = DoubleUtils.add(allPrincipalSum, principalSum);
                        allPackSum = DoubleUtils.add(allPackSum, svcAmount);

                    }else if ("2".equals(state) || "4".equals(state)){//已还
                        repayNum++;
                    }
                }
                Double allMoney = DoubleUtils.add(DoubleUtils.add(allPrincipalSum, settleFee), allPackSum);//合计应还款
                resultMap.put("everyRepay",everyRepay);//每期应还金额
                resultMap.put("allPrincipalMoney",String.format("%.2f",allMoney));//未到期应还金额
                resultMap.put("repayNum",String.valueOf(repayNum));
                resultMap.put("allMoney",String.format("%.2f",allMoney));//总还款金额
                resultMap.put("allPrincipalSum",String.format("%.2f",allPrincipalSum));//总本金
                resultMap.put("allPackSum",String.format("%.2f",allPackSum));//总服务包
//                resultMap.put("nowRepay",String.format("%.2f",nowRepay));//本期待还金额
//                resultMap.put("overdueMoney",String.format("%.2f",overdueMoney));//逾期待还金额
                resultMap.put("settleFee",String.format("%.2f",settleFee));//提前结清服务包金额
                resultMap.put("settleType",settleType);
                resultMap.putAll(retMap);
            }
            return resultMap;
        }else{
            resultMap.put("flag",false);
            return resultMap;
        }
    }

    //账单明细
    public List getBillingDetails(String orderId){
        List retList = new ArrayList();
        String repaySql = "select id,state,amount,default_interest as defaultInterest,repayment_amount as repaymentAmount,pay_time as payTime,pay_count as payCount,penalty,actual_amount as actualAmount,red_amount as redMoney from mag_repayment where order_id = '"+orderId+"' order by pay_time asc";
        List<Map> repays = sunbmpDaoSupport.findForList(repaySql);
        //获取有无有效的提前权限
        Map settleMap = sunbmpDaoSupport.findForMap("select settle_type as settleType, settle_fee as settleFee, settle_amount as settleAmount," +
                "effective_time as effectiveTime from mag_settle_record where state!=0 and order_id='" + orderId + "'");
        Double settleRepayMoney = 0.00;
        Double settlePackMoney = 0.00;
        String settleAllState = "";

        String timeSql = "select pay_time as payTime from mag_repayment where order_id = '"+orderId+"' and state = '1' order by pay_time asc LIMIT 1";
        Map timeMap = sunbmpDaoSupport.findForMap(timeSql);
        String currentTime = "";
        if(timeMap!=null ){//存在未还的计划
            currentTime = timeMap.get("payTime").toString();//当期还款时间
        }
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH )+1;
        List payList = new ArrayList();
        List unpayList = new ArrayList();
        if(repays!=null && repays.size()>0){
            int repaySize = repays.size();
            for(int i=0;i<repaySize;i++){
                String settleState = "";
                Map repayMap = repays.get(i);
                String now = "0";
                String state = repayMap.get("state").toString();//还款计划状态
                String repaymentTime = repayMap.get("payTime").toString();//还款时间
                int endTime = Integer.parseInt(repaymentTime.substring(0,8));//还款结束时间
                String penalty = (repayMap.get("penalty") == null || repayMap.get("penalty").toString().equals(""))?"0":repayMap.get("penalty").toString();//逾期利息
                String repaymentAmount = repayMap.get("repaymentAmount").toString();//每月还款额
                double monthPay = 0.00;//每期实际还款金额
                double yuMoney = 0.00;//逾期金额
                double interestMoney = 0.00;//罚息金额
                double penaltyMoney = 0.00;//逾期利息金额
                double jianMoney = 0.00;//减免金额
                double fuwuMoney = 0.00;//服务包金额

                //每期所有服务包的钱
                String sql = "select id,repayment_id as repaymentId,package_name as packageName,period,`month`," +
                        "type,amount, state from service_package_repayment where repayment_id = '"+repayMap.get("id")+"' order by period";
                List serPcgList = sunbmpDaoSupport.findForList(sql);
                if(serPcgList != null&& serPcgList.size() > 0){
                    for (int k = 0; k < serPcgList.size();k++){
                        Map map = (Map)serPcgList.get(k);
                        double amount = Double.valueOf(map.get("amount").toString());
                        //服务包费用
                        fuwuMoney = DoubleUtils.add(fuwuMoney, amount);
                    }
                }

                if("1".equals(state)){//未还
                    if (null != settleMap && "0".equals(settleMap.get("settleType")))
                    {//提前结清是否存在
                        settleRepayMoney = DoubleUtils.add(settleRepayMoney, Double.valueOf(repayMap.get("amount").toString()));
                        settlePackMoney = DoubleUtils.add(settlePackMoney, fuwuMoney);
                    }
                    if (null != settleMap)
                    {
                        settleState = "1";
                        settleAllState = "1";
                    }

                    //本月或最近一期未还
                    if (null == settleMap )
                    {
                        if(endTime == Integer.parseInt(currentTime.substring(0,8)) || Integer.valueOf(repaymentTime.substring(4,6)) == month){
                            now = "1";
                        }
                    }
                }else if("2".equals(state)){ //已还
                    if (Double.valueOf(penalty) != 0)
                    {//存在逾期时
                        double defaultInterest = Double.valueOf(repayMap.get("defaultInterest") == null ? "0" : repayMap.get("defaultInterest").toString());
                        yuMoney = DoubleUtils.add(Double.valueOf(penalty), defaultInterest);
                        interestMoney = defaultInterest;
                        penaltyMoney = Double.valueOf(penalty);
                    }
                    if (repayMap.get("actualAmount") == null || "".equals(repayMap.get("actualAmount")))
                    {
                        settleState = "2";
                        settleAllState = "2";
                        if (null != settleMap && "0".equals(settleMap.get("settleType")))
                        {//提前结清是否存在
                            settleRepayMoney = DoubleUtils.add(settleRepayMoney, Double.valueOf(repayMap.get("amount").toString()));
                            settlePackMoney = DoubleUtils.add(settlePackMoney, fuwuMoney);
                        }
                    }
                }else if("3".equals(state)){ //逾期
                    if (null != settleMap && "0".equals(settleMap.get("settleType")))
                    {//正常结清
                        settleRepayMoney = DoubleUtils.add(settleRepayMoney, Double.valueOf(repayMap.get("money").toString()));
                        settlePackMoney = DoubleUtils.add(settlePackMoney, fuwuMoney);
                    }
                    if (null != settleMap)
                    {
                        settleState = "1";
                        settleAllState = "1";
                    }

                    double defaultInterest = Double.valueOf((repayMap.get("defaultInterest") == null || "".equals(repayMap.get("defaultInterest"))) ? "0" : repayMap.get("defaultInterest").toString());
                    yuMoney = DoubleUtils.add(Double.valueOf(penalty), defaultInterest);
                    interestMoney = defaultInterest;
                    penaltyMoney = Double.valueOf(penalty);
                }else if("4".equals(state)) { //还款确认中
                    if (Double.valueOf(penalty) != 0)
                    {//存在逾期时
                        double defaultInterest = Double.valueOf(repayMap.get("defaultInterest") == null ? "0" : repayMap.get("defaultInterest").toString());
                        yuMoney = DoubleUtils.add(Double.valueOf(penalty), defaultInterest);
                        interestMoney = defaultInterest;
                        penaltyMoney = Double.valueOf(penalty);
                    }
                    if (repayMap.get("actualAmount") == null || "".equals(repayMap.get("actualAmount")))
                    {
                        settleState = "4";
                        settleAllState = "4";
                        if (null != settleMap && "0".equals(settleMap.get("settleType")))
                        {//提前结清是否存在
                            settleRepayMoney = DoubleUtils.add(settleRepayMoney, Double.valueOf(repayMap.get("amount").toString()));
                            settlePackMoney = DoubleUtils.add(settlePackMoney, fuwuMoney);
                        }
                    }
                }

                //查询每期还款是否有减免
                String derateSql = "select id,derate_amount as derateAmount,effective_data as  effectiveData,state from mag_derate where repayment_id = '"+repayMap.get("id")+"' and ((approval_state = '1' and effective_data > '"+DateUtils.getDateString(new Date())+"') or state='3')";
                Map derateMap = sunbmpDaoSupport.findForMap(derateSql);
                if (derateMap != null && !"2".equals(state) && !"4".equals(state)){//未还或逾期时
                    if (!"3".equals(derateMap.get("state")))
                    {
                        double derateAmount = Double.valueOf(derateMap.get("derateAmount").toString());
                        jianMoney = derateAmount;
                    }

                }else{//已还或确认中
                    if (null != derateMap && derateMap.get("state") != null && "3".equals(derateMap.get("state")))
                    {//已使用的减免
                        jianMoney = Double.valueOf(derateMap.get("derateAmount").toString());
                    }
                }

                Double total = DoubleUtils.add(DoubleUtils.sub(DoubleUtils.add(Double.valueOf(repaymentAmount), yuMoney), jianMoney), fuwuMoney);
                Double redMoney = 0.00;//还款明细
                if ("2".equals(state) || "4".equals(state))
                {//已还或确认中时
                    if (null != repayMap.get("redMoney") && !"".equals(repayMap.get("redMoney")) && !"0".equals(repayMap.get("redMoney")))
                    {//存在红包时
                        Double  actualAmount= (repayMap.get("actualAmount") == null || "".equals(repayMap.get("actualAmount"))) ? 0 : Double.valueOf(repayMap.get("actualAmount").toString());//实际还款金额
                        if (DoubleUtils.sub(total, actualAmount) < Double.valueOf(repayMap.get("redMoney").toString()))
                        {//红包金额大于还款金额
                            redMoney = DoubleUtils.sub(total, actualAmount);
                        }
                        else
                        {//红包金额小于还款金额
                            redMoney = Double.valueOf(repayMap.get("redMoney").toString());
                        }
                    }

                }
                monthPay = DoubleUtils.sub(total, redMoney);
                repayMap.put("monthPay",String.format("%.2f",monthPay));
                repayMap.put("derateAmount",String.format("%.2f",jianMoney));
                repayMap.put("redAmount",String.format("%.2f",redMoney));
                repayMap.put("defaultInterest",String.format("%.2f",interestMoney));
                repayMap.put("penalty",String.format("%.2f",penaltyMoney));
                repayMap.put("pcgAmount",String.format("%.2f",fuwuMoney));
                repayMap.put("now",now);
                if (null == settleMap)
                {
                    if ("2".equals(state) || "4".equals(state))
                    {
                        payList.add(repayMap);
                    }
                    else
                    {
                        unpayList.add(repayMap);
                    }
                }
                else if (("2".equals(state) || "4".equals(state)) && "".equals(settleState))
                {
                    payList.add(repayMap);
                }
            }
            if (null != settleMap)
            {
                Map raymentMap = new HashMap();
                raymentMap.put("state", settleAllState);//还款状态
                raymentMap.put("now",1);
                if ("0".equals(settleMap.get("settleType")))
                {//正常结清
                    raymentMap.put("payCount","-1");
                    raymentMap.put("monthPay",String.format("%.2f",DoubleUtils.add(DoubleUtils.add(Double.valueOf(settleMap.get("settleFee").toString()), Double.valueOf(settleRepayMoney)),settlePackMoney)));//每月还款额
                    raymentMap.put("repaymentTime","");//每期到期日期
                    raymentMap.put("pcgAmount",String.format("%.2f",settlePackMoney));
                    raymentMap.put("settleAmount",String.format("%.2f",Double.valueOf(settleRepayMoney)));//每月还款额
                    raymentMap.put("settleFee",String.format("%.2f",Double.valueOf(settleMap.get("settleFee").toString())));//每月还款额
                }
                else
                {//非正常结清
                    raymentMap.put("payCount","0");
                    raymentMap.put("monthPay",String.format("%.2f",Double.valueOf(settleMap.get("settleAmount").toString())));//每月还款额
                    raymentMap.put("repaymentAmount", String.format("%.2f",Double.valueOf(settleMap.get("settleAmount").toString())));

                    if (null != settleMap.get("effectiveTime") && "1".equals(settleAllState))
                    {//非正常结清的有效时间
                        raymentMap.put("payTime", settleMap.get("effectiveTime").toString());
                    }
                }
                retList.add(raymentMap);
            }
            else
            {
                retList.addAll(unpayList);
            }
            retList.addAll(payList);
        }
        return retList;
    }

    /**
     *根据orderId获取是否有提前还款权限
     * @param orderId
     * @return
     */
    @Override
    public Map getSettleAuth(String orderId) throws Exception{
        //获取服务包提前还款期数
        String sevPcgOrderSql = "select id,type,amount_collection as amount,package_name as packageName,month from " +
                "mag_servicepag_order where order_id = '"+orderId+"' order by type desc";
        List sevPcgOrderList = sunbmpDaoSupport.findForList(sevPcgOrderSql);
        Map map = new HashMap();
        String tel = iSystemDictService.getInfo("service.tel");
        map.put("tel",tel);
        String state = "";//提前结清状态
        String settleSql = "select id,effective_time as effectiveTime,settle_type from mag_settle_record where order_id = '"+orderId+"'  and state = '1'";
        List settleList = sunbmpDaoSupport.findForList(settleSql);
        if (settleList != null && settleList.size() > 0){
            Map settleMap =  (Map) settleList.get(0);
            String now = DateUtils.getDateString(new Date());
            String effectiveTime = "";
            if("1".equals(settleMap.get("settle_type").toString())){//非正常结清
                effectiveTime = settleMap.get("effectiveTime").toString();
                if(Double.parseDouble(now)-Double.parseDouble(effectiveTime)>0){//非正常结清失效了
                    state = "0";
                    map.put("servicePackage",sevPcgOrderList);
                }else{//非正常结清未失效
                    state = "2";
                }
            }else{
                state = "2";
            }
            map.put("state",state);
            return map;
        }

        Integer month=0;
        if(sevPcgOrderList==null || sevPcgOrderList.size()==0){
            state = "1";//还未拥有提前还款资格,请按时还款
            map.put("state",state);
            //    map.put("servicePackage",sevPcgOrderList);
            return map;
        }else{
            for(int i=0;i<sevPcgOrderList.size();i++){
                Map sevPcgMap = (Map)sevPcgOrderList.get(i);
                if ("1".equals(sevPcgMap.get("type").toString())) {
                    if (!org.springframework.util.StringUtils.isEmpty(sevPcgMap.get("month"))) {
                        Integer month1 = Integer.valueOf(sevPcgMap.get("month").toString());
                        if (month == 0) {
                            month = month1;
                        } else if (month < month1) {
                            month = month1;
                        }
                    }
                } else if ("2".equals(sevPcgMap.get("type").toString())) {
                    if (!org.springframework.util.StringUtils.isEmpty(sevPcgMap.get("month"))) {
                        Integer month2 = Integer.valueOf(sevPcgMap.get("month").toString());
                        if (month == 0) {
                            month = month2;
                        } else if (month < month2) {
                            month = month2;
                        }
                    }
                }
                //获取订单已还款期数
                String paySql = "select count(id) as payNum from mag_repayment where order_id = '" + orderId + "' and state = '2'";
                Map payMap = sunbmpDaoSupport.findForMap(paySql);
                //已还款期数
                Integer payNum = Integer.valueOf(payMap.get("payNum")==null?"0":payMap.get("payNum").toString());
                if (month <= payNum) {
                    state = "1";//已拥有提前还款资格,可打客服电话开启
                    map.put("state", state);
                    map.put("tel", tel);
                } else {
                    state = "0";//还未拥有提前还款资格,请按时还款
                    map.put("state", state);
                }
            }
        }
        map.put("servicePackage", sevPcgOrderList);
        return map;
    }


    public static void main(String[] args) {
      /* double a=1000;
        int b=12;
        String c=String.valueOf(a/b);
        String c1=String.valueOf(a%b);
        String intNumber = c.substring(0,c.indexOf("."));
        double intMoney=Double.valueOf(intNumber);
        double repayment_amount =intMoney+ a*0.02;
        System.out.println(repayment_amount);
        System.out.println(c1);*/
        String a="1.01";
        double b=Double.valueOf(a);
        System.out.println(b);
    }
}