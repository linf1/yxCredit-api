package com.zw.miaofuspd.merchant.service;

import com.base.util.DateUtils;
import com.zw.miaofuspd.facade.merchant.service.MerchantService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/22 0022.
 */
@Service
public class MerchantServiceImpl extends AbsServiceBase implements MerchantService {
    /**
     * 获取该业务下所有的商户列表
     * @param  salesmanId 办单员id
     * @return
     * @throws Exception
     */
    @Override
    public List getMerchantList(String salesmanId) throws Exception {
        /**
         * state : 0 的时候表示商户正常
         * 1:表示商户冻结
         */
        String sql="SELECT id,type,mer_code as merCode,mer_name as merchantName,mer_detail_address as merchantAddress,mer_tel as tel,state\n" +
                     " FROM  mag_merchant WHERE  mag_merchant.id IN" +
                          "(SELECT  b.merchant_id AS merchantId  FROM \n" +
                                "mag_merchant_saleman_rel b\n" +
                                 "LEFT JOIN  mag_salesman a\n" +
                                    "ON  b.employee_id=a.id\n" +
                                        "WHERE  b.employee_id='" +salesmanId+"' and b.state='0') and mag_merchant.state='1'";
        return sunbmpDaoSupport.findForList(sql);
    }

    @Override
    /**
     * 模糊查询符合条件的商户
     * @param salesmanId 办单员id
     * @param merchantSearchInfo  模糊搜索商店的内容
     * @return
     * @throws Exception
     */
    public List selectMerchantByInfo(String merchantSearchInfo,String salesmanId) throws Exception {
        String sql ="select id,type,mer_code as merCode,mer_name as merchantName,mer_detail_address as merchantAddress,mer_tel as tel,state  " +
                "from mag_merchant where mag_merchant.id IN" +
                "(SELECT  b.merchant_id as merchant_id  FROM \n" +
                "mag_merchant_saleman_rel b\n" +
                "LEFT JOIN  mag_salesman a\n" +
                "ON  b.employee_id=a.id\n" +
                "WHERE  b.employee_id='" +salesmanId+"' and b.state='0') and mag_merchant.state='1' and  mer_name like '%"+merchantSearchInfo+"%'";
        return sunbmpDaoSupport.findForList(sql);
    }

    @Override
    /**
     * @param merchantId 商户Id
     * 通过商户id获取商户的名字
     */
    public Map getMerchantById(String merchantId) {
        String sql = "select id,mer_name as merName from mag_merchant where id = '"+merchantId+"'";
        Map map = sunbmpDaoSupport.findForMap(sql);
        return map;
    }
    /**
     * 查询改商户下是否超过单笔限额以及每日进件笔数
     * @param merchantId 商户id
     * @param allMoney 商品总金额
     * @param downMoney 商户首付金额
     * @return
     * @throws Exception
     */
    @Override
    public Map checkMerchantQuota(String merchantId,String allMoney,String downMoney) throws Exception {
        Map returnMap = new HashMap();
        double fenqiMoney = Double.valueOf(allMoney) - Double.valueOf(downMoney);//分期总金额
        String singleQuato = "";//每日单笔限额额度
        //先根据商户id查询改商户的信息
        String levelSql = "select provinces,provinces_id as provincesId,city,city_id as cityId,distric,distric_id as districId,mer_grade as merLevelId from mag_merchant" +
                " where id = '"+merchantId+"' and state = '1'";
        Map levelMap = sunbmpDaoSupport.findForMap(levelSql);
        String merLevelId = levelMap.get("merLevelId").toString();//分级表的id
        String provincesId = levelMap.get("provincesId").toString();//商户所在的省id
        String cityId = levelMap.get("cityId").toString();//商户所在的市id

        //走商户分级表,查询出改商户的每日单笔限额
        String merGradeSql = "select number_day as numDay,number_week as numWeek,number_month as numMonth,quota_day as quotaDay,quota_week as quotaWeek," +
                "quota_month as quotaMonth,single_quota as quotaSingle from mag_merchant_grade where id = '"+merLevelId+"' and state = '1'";
        Map merGradeMap = sunbmpDaoSupport.findForMap(merGradeSql);
        String quotaSingle = "";//商户每笔单日限额
        String numDay ="";//商户每日限额进件量
        String numWeek="";//商户每周限额进件量
        String numMonth="";//商户每月限额进件量
        String quotaDay = "";//日限额
        String quotaWeek = "";//周限额
        String quotaMonth = "";//月限额
        if(merGradeMap!=null){
             quotaSingle = merGradeMap.get("quotaSingle").toString();
             numDay = merGradeMap.get("numDay").toString();
             numWeek = merGradeMap.get("numWeek").toString();
             numMonth = merGradeMap.get("numMonth").toString();
            quotaDay = merGradeMap.get("quotaDay").toString();
            quotaWeek = merGradeMap.get("quotaWeek").toString();
            quotaMonth = merGradeMap.get("quotaMonth").toString();
        }
        //走区域限额查询该商户所在的区域的每日单笔限额
        String areaQuotaSql = "select number_day as numDay,number_week as numWeek,number_month as numMonth,quota_day as quotaDay,quota_week as quotaWeek ," +
                "quota_month as quotaMonth,single_quota as quotaSingle from mag_area_quota" +
                " where province_id='"+provincesId+"' and city_id='"+cityId+"' and state ='1'";
        Map areaQuotaMap = sunbmpDaoSupport.findForMap(areaQuotaSql);
        String areaQuotaSingle ="";//区域单笔限额
        String areaNumDay="";//区域每日进件量限额
        String areaNumWeek="";//区域每周进件量限额
        String areaNumMonth="";//区域每月进件量限额
        String areaQuotaDay = "";//日限额
        String areaQuotaWeek = "";//周限额
        String areaQuotaMonth = "";//月限额
        if(areaQuotaMap!=null){
            areaQuotaSingle = areaQuotaMap.get("quotaSingle").toString();
            areaNumDay = areaQuotaMap.get("numDay").toString();
            areaNumWeek = areaQuotaMap.get("numWeek").toString();
            areaNumMonth = areaQuotaMap.get("numMonth").toString();
            areaQuotaDay = merGradeMap.get("quotaDay").toString();
            areaQuotaWeek = merGradeMap.get("quotaWeek").toString();
            areaQuotaMonth = merGradeMap.get("quotaMonth").toString();
        }
        //====1.判断每日单笔限额是否超过分期总金额
        //比较商户的每日单笔限额和区域的单笔限额,谁低取谁
        if(Double.valueOf(quotaSingle==""?"0":quotaSingle) >=Double.valueOf(areaQuotaSingle==""?"0":areaQuotaSingle)){
            singleQuato = areaQuotaSingle;
        }else{
            singleQuato = quotaSingle;
        }
        if(Double.valueOf(singleQuato==""?"0":singleQuato)!=0 && fenqiMoney>Double.valueOf(singleQuato==""?"0":singleQuato)*10000){
            returnMap.put("flag",false);
            returnMap.put("msg","当前分期金额大于单笔分期限额(单笔额度:"+String.format("%.2f",Double.valueOf(singleQuato))+"元),请重新输入");
            return returnMap;
        }
        //===2.判断每日进件量,每周进件量,每月进件量,是否超过商户限额
        //先查询出该商户
        String sql = "select applay_money from mag_order where merchant_id = '"+merchantId+"' and state!=0 and order_type='2'";
        StringBuffer orderDaySql = new StringBuffer(sql);//天进件量
        orderDaySql.append(" and DATE_FORMAT(CREAT_TIME,'%Y%m%d')='"+ DateUtils.getCurrentTime("yyyyMMdd")+"' order by creat_time asc");
        List orderDayList = sunbmpDaoSupport.findForList(orderDaySql.toString());
        Map orderDayMap= numMoney(orderDayList);
        int orderDayNum = Integer.valueOf(orderDayMap.get("orderNum").toString());;//日订单
        double orderDayMoney=Double.valueOf(orderDayMap.get("orderMoney").toString());;//日订单总金额

        StringBuffer orderWeekSql = new StringBuffer(sql);//周进件量
        orderWeekSql.append(" and DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(creat_time) order by creat_time asc");
        List orderWeekList = sunbmpDaoSupport.findForList(orderWeekSql.toString());//周进件量
        Map orderWeekMap= numMoney(orderWeekList);
        int orderWeekNum = Integer.valueOf(orderWeekMap.get("orderNum").toString());//周进件量
        double orderWeekMoney = Double.valueOf(orderWeekMap.get("orderMoney").toString());//周单子钱

        StringBuffer orderMonthSql = new StringBuffer(sql);//月进件量
        orderMonthSql.append(" and DATE_SUB(CURDATE(), INTERVAL 1 Month) <= date(creat_time) order by creat_time asc");
        List orderMonthList = sunbmpDaoSupport.findForList(orderMonthSql.toString());
        Map orderMonthMap= numMoney(orderMonthList);
        int orderMonthNum = Integer.valueOf(orderMonthMap.get("orderNum").toString());//月进件量
        if(orderMonthNum==0){
            returnMap.put("flag",true);
            return returnMap;
        }
        double orderMonthMoney = Double.valueOf(orderMonthMap.get("orderMoney").toString());//月单子钱

        //判断如果该商户达到了每日,每周,每月限定笔数,就不给进件了
        if(merGradeMap!=null){
            if(orderDayNum>Integer.valueOf(numDay)){
                returnMap.put("flag",false);
                returnMap.put("msg","该商户已达到每日进件数量(每日进件数量:"+numDay+"笔),请明日再试");
                return returnMap;
            }else if(orderWeekNum>Integer.valueOf(numWeek)){
                returnMap.put("flag",false);
                returnMap.put("msg","该商户已达到每周进件数量(每周进件数量:"+numWeek+"笔),请下周再试");
                return returnMap;
            }else if(orderMonthNum>Integer.valueOf(numMonth)){
                returnMap.put("flag",false);
                returnMap.put("msg","该商户已达到每月进件数量(每月进件数量:"+numMonth+"笔),请下月再试");
                return returnMap;
            }else if(orderDayMoney>Double.valueOf(quotaDay)*10000){
                returnMap.put("flag",false);
                returnMap.put("msg","该商户已达到每日限额(每日限额"+String.format("%.2f",Double.valueOf(quotaDay))+"元),请明日再试");
                return returnMap;
            }else if(orderWeekMoney>Double.valueOf(quotaWeek)*10000){
                returnMap.put("flag",false);
                returnMap.put("msg","该商户已达到每周限额(每周限额:"+String.format("%.2f",Double.valueOf(quotaWeek))+"元),请下周再试");
                return returnMap;
            }else if(orderMonthMoney>Double.valueOf(quotaMonth)*10000){
                returnMap.put("flag",false);
                returnMap.put("msg","该商户已达到每月限额(每月限额:"+String.format("%.2f",Double.valueOf(quotaMonth))+"元),请下月再试");
                return returnMap;
            }
        }
        //===3.查询该商户对应的区域下的所有进件量是否超过区域限额
        String areaSql = "select count(*) from mag_order where city_id = '"+cityId+"' and provinces_id = '"+provincesId+"' and state!=0 and order_type='2'";
        StringBuffer areaDaySql = new StringBuffer(areaSql);//天进件量
        areaDaySql.append(" and DATE_FORMAT(CREAT_TIME,'%Y%m%d')='"+ DateUtils.getCurrentTime("yyyyMMdd")+"' order by creat_time asc");
        int areaDayNum = sunbmpDaoSupport.getCount(areaDaySql.toString());//区域所有当天所有商户进件量

        StringBuffer areaWeekSql = new StringBuffer(areaSql);//周进件量
        areaWeekSql.append(" and DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(creat_time) order by creat_time asc");
        int areaWeekNum = sunbmpDaoSupport.getCount(areaWeekSql.toString());//区域所有一周所有商户进件量

        StringBuffer areaMonthSql = new StringBuffer(areaSql);//月进件量
        areaMonthSql.append(" and DATE_SUB(CURDATE(), INTERVAL 1 Month) <= date(creat_time) order by creat_time asc");
        int areaMonthNum = sunbmpDaoSupport.getCount(areaMonthSql.toString());//区域所有一月所有商户进件量

        //再判断该商户对应的区域每日,每周,每月限定笔数
        if(areaQuotaMap!=null){
            if(areaDayNum>Integer.valueOf(areaNumDay)){
                returnMap.put("flag",false);
                returnMap.put("msg","该商户所在区域已达到每日进件数量(每日进件数量:"+areaNumDay+"笔),请明日再试");
                return returnMap;
            }else if(areaWeekNum>Integer.valueOf(areaNumWeek)){
                returnMap.put("flag",false);
                returnMap.put("msg","该商户所在区域已达到每周进件数量(每周进件数量:"+areaNumWeek+"笔),请下周再试");
                return returnMap;
            }else if(areaMonthNum>Integer.valueOf(areaNumMonth)){
                returnMap.put("flag",false);
                returnMap.put("msg","该商户所在区域已达到每月进件数量(每月进件数量:"+areaNumWeek+"笔),请下月再试");
                return returnMap;
            }else if(orderDayMoney>Double.valueOf(areaQuotaDay)*10000){
                returnMap.put("flag",false);
                returnMap.put("msg","该商户所在区域已达到每日限额(每日限额"+String.format("%.2f",Double.valueOf(areaQuotaDay))+"元),请明日再试");
                return returnMap;
            }else if(orderWeekMoney>Double.valueOf(areaQuotaWeek)*10000){
                returnMap.put("flag",false);
                returnMap.put("msg","该商户所在区域已达到每周限额(每周限额:"+String.format("%.2f",Double.valueOf(areaQuotaWeek))+"元),请下周再试");
                return returnMap;
            }else if(orderMonthMoney>Double.valueOf(areaQuotaMonth)*10000){
                returnMap.put("flag",false);
                returnMap.put("msg","该商户所在区域已达到每月限额(每月限额:"+String.format("%.2f",Double.valueOf(areaQuotaMonth))+"元),请下月再试");
                return returnMap;
            }
        }
        returnMap.put("flag",true);
        return returnMap;
    }
    //获取订单总量和总金额
    public Map numMoney(List list){
        Map map = new HashMap();
        int orderNum = 0;//周订单
        double orderMoney=0;//周订单总金额
        if(list!=null && list.size()>0){
            for(int i=0;i<list.size();i++){
                Map orderMap = (Map) list.get(i);
                orderMoney +=Double.valueOf(orderMap.get("applay_money").toString());
            }
            orderNum = list.size();//天进件量
        }
        map.put("orderNum",orderNum);
        map.put("orderMoney",String.format("%.2f",orderMoney));
        return map;

    }
}
