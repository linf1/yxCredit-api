package com.zw.miaofuspd.serpackage.service;

import com.base.util.DateUtils;
import com.zw.miaofuspd.facade.serpackage.service.SerPackageService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2017/12/18 0018.
 */
@Service
public class SerPackageServiceImpl extends AbsServiceBase implements SerPackageService {
    /**
     * 获取服务包数据
     * @return
     * @throws Exception
     */
    @Override
    public List getSerPackage(String periods) throws Exception {
        List list = new ArrayList();//根据利率方案选择过后的期数,筛选过后的服务包集合
        List frontList = getSerPackageInfo("1");//查询出前置还款包
        if(frontList!=null && frontList.size()>0){
            for(int i = 0 ;i<frontList.size();i++){
                Map frontMap = new HashMap<>();
                frontMap = (Map) frontList.get(i);
                String afterMonth = String.valueOf(frontMap.get("afterMonth"));//几期后可提前还款
                if(Double.valueOf(periods)>=Double.valueOf(afterMonth)){
                    frontMap.put("sqTypeRemark","");
                    frontMap.put("qsRemark",afterMonth+"期后可提前还款");
                    list.add(frontMap);
                }
            }
        }
        List monthList = getSerPackageInfo("2");//查询出月付提前还款包
        if(monthList!=null && monthList.size()>0){
            for (int i = 0;i<monthList.size();i++){
                Map monthMap = new HashMap<>();
                monthMap = (Map) monthList.get(i);
                String periodCollection = String.valueOf(monthMap.get("collectionPeriod"));//多少期
                String collectionType = String.valueOf(monthMap.get("collectionType"));//收取期数类型
                String afterMonth = String.valueOf(monthMap.get("afterMonth"));//几期后可提前还款
                if("0".equals(collectionType)){//代表随产品期数收取
                    if(Double.valueOf(periods)>=Double.valueOf(afterMonth)){
                        monthMap.put("sqTypeRemark","随产品期数收取");
                        monthMap.put("qsRemark",afterMonth+"期后可提前还款");
                        list.add(monthMap);
                    }
                }else{
                    if(Double.valueOf(periods)>=Double.valueOf(periodCollection)){
                        monthMap.put("sqTypeRemark","收取"+periodCollection+"期");
                        monthMap.put("qsRemark",afterMonth+"期后可提前还款");
                        list.add(monthMap);
                    }
                }
            }
        }
        List ortherList = getSerPackageInfo("3");//查询出其他还款包
        if(ortherList!=null && ortherList.size()>0){
            for (int i = 0;i<ortherList.size();i++){
                Map ortherMap = new HashMap<>();
                ortherMap = (Map) ortherList.get(i);
                String periodCollection = String.valueOf(ortherMap.get("collectionPeriod"))==null?"0":String.valueOf(ortherMap.get("collectionPeriod"));//多少期
                String collectionType = String.valueOf(ortherMap.get("collectionType"));//收取期数类型
                String forceCollection = String.valueOf(ortherMap.get("forceCollection"));//是否强制收取
              //  String afterMonth = String.valueOf(ortherMap.get("afterMonth"))==null?"0":String.valueOf(ortherMap.get("afterMonth"));//几期后可提前还款
                if("0".equals(collectionType)){ //代表随产品期数收取
                    periodCollection=periods;
                    if(Double.valueOf(periods)>=Double.valueOf(periodCollection)) {
                        ortherMap.put("sqTypeRemark", "随产品期数收取");
                        ortherMap.put("qsRemark", "");
                        list.add(ortherMap);
                    }
                }else{
                    if("0".equals(forceCollection)){ //代表强制收取,0代表不强制收取
                        if(Double.valueOf(periods)>=Double.valueOf(periodCollection)){
                            ortherMap.put("sqTypeRemark","收取"+periodCollection+"期");
                            ortherMap.put("qsRemark","");
                            list.add(ortherMap);
                        }
                    }else{
                        if(Double.valueOf(periods)>=Double.valueOf(periodCollection)) {
                            ortherMap.put("sqTypeRemark", "收取" + periodCollection + "期");
                            ortherMap.put("qsRemark","");
                            list.add(ortherMap);
                        }
                    }
                }
            }
        }
        return list;
    }
    //根据服务包类型,查询出服务包的信息
    public List getSerPackageInfo(String type){
        //type 1 前置服务包,2月付提前还款包,3,其他服务包
        String sql = "select id,package_id as packageType,package_name as packageName," +
                "period_collection as collectionPeriod,month as afterMonth,amount_collection as collectionAmount," +
                "period_collection_type as collectionType,force_collection as forceCollection " +
                "from mag_service_package where package_id in (select id from mag_service_package_type) " +
                "and state = '1' and package_id = '"+type+"' order by package_name asc";
        List serviceList = sunbmpDaoSupport.findForList(sql);//查询还款包集合
        return serviceList;
    }
    @Override
    public void setSerPackageInfoByIds(String orderId,String idJson) {
        //先删除当前订单下的所有服务包.
        String sql = "delete from mag_servicepag_order where order_id ='"+orderId+"'";
        sunbmpDaoSupport.exeSql(sql);
        if("''".equals(idJson) || idJson==null){
            return;
        }
        String a[] = idJson.split(",");
        String servicePagId = "";
        //再将服务包的id取出来放到服务包跟订单关联表,每一个服务包都将会生成一个数据
        for (int i = 0; i < a.length; i++) {
            String id = UUID.randomUUID().toString();
            servicePagId = a[i];
            String packgeSql = " SELECT id, package_id AS type, package_name AS packageName," +
                    " period_collection AS periodCollection, MONTH as `month`," +
                    "amount_collection AS amountCollection, state AS state," +
                    " period_collection_type AS periodCollectionType, force_collection AS forceCollection" +
                    " FROM mag_service_package WHERE id = "+servicePagId+"";
            Map map = sunbmpDaoSupport.findForMap(packgeSql);
            String type = map.get("type").toString();
            String packageName = map.get("packageName").toString();
            String periodCollection = map.get("periodCollection").toString();
            String month = map.get("month").toString();
            String periodCollectionType = map.get("periodCollectionType").toString();
            String forceCollection = map.get("forceCollection").toString();
            String insertSql = "insert into mag_servicepag_order(id,package_service_id,state,create_time,alter_time,order_id,type" +
                    ",package_name,period_collection,month,amount_collection,period_collection_type,force_collection)" +
                    "value('" + id + "'," + servicePagId + ",'0','" + DateUtils.getDateString(new Date()) + "'," +
                    "'" + DateUtils.getDateString(new Date()) + "','"+orderId+"','"+type+"','"+packageName+"'," +
                    "'"+periodCollection+"','"+month+"','"+map.get("amountCollection").toString()+"','"+periodCollectionType+"','"+forceCollection+"')";
            sunbmpDaoSupport.exeSql(insertSql);
        }
    }
    /**
     * 获取服务包的信息
     * @param idJson
     * @return
     */
    @Override
    public List getSerPackageInfoByIds(String idJson) {//服务包的idjson串
        List list = new ArrayList();
        if("''".equals(idJson) || idJson==null){
            return list;
        }
        String sql = "select id,package_id as packageType,package_name as packageName," +
                "period_collection as collectionPeriod,month as afterMonth,amount_collection as collectionAmount," +
                "period_collection_type as collectionType,force_collection as forceCollection " +
                "from mag_service_package where state = '1' and id in ("+idJson+") order by package_id asc";
        List serviceList = sunbmpDaoSupport.findForList(sql);//查询服务包集合
        if(serviceList!=null && serviceList.size()>0){
            for(int i=0;i<serviceList.size();i++){
                Map serviceMap = new HashMap();
                serviceMap = (Map) serviceList.get(i);
                String packageType = (String) serviceMap.get("packageType");//服务包类型,1前置提前还款,2月付还款包,3其他服务包
                String afterMonth = String.valueOf(serviceMap.get("afterMonth"));//几期后可提前还款
                String periodCollection = String.valueOf(serviceMap.get("collectionPeriod"));//多少期
                String collectionType = String.valueOf(serviceMap.get("collectionType"));//收取期数类型
                if("1".equals(packageType)){
                    serviceMap.put("sqTypeRemark","提货前收取");
                    serviceMap.put("qsRemark",afterMonth+"期后可提前还款");
                    list.add(serviceMap);
                }else if("2".equals(packageType) /*|| "3".equals(packageType)*/){
                    if("0".equals(collectionType)){//代表随产品期数收取
                        serviceMap.put("sqTypeRemark","随产品期数收取");
                        serviceMap.put("qsRemark",afterMonth+"期后可提前还款");
                        list.add(serviceMap);
                    }else{//代表自定义期数收取
                        serviceMap.put("sqTypeRemark","收取"+periodCollection+"期");
                        serviceMap.put("qsRemark",afterMonth+"期后可提前还款");
                        list.add(serviceMap);
                    }
                }else{
                    if("0".equals(collectionType)){//代表随产品期数收取
                        serviceMap.put("sqTypeRemark","随产品期数收取");
                        serviceMap.put("qsRemark","");
                        list.add(serviceMap);
                    }else{//代表自定义期数收取
                        serviceMap.put("sqTypeRemark","收取"+periodCollection+"期");
                        serviceMap.put("qsRemark","");
                        list.add(serviceMap);
                    }
                }
            }
        }
        return list;
    }
    /**
     * 根据前置服务包的提前几个月还的月数,查询出对应的月付还款服务包
     * @param afterMonth
     * @return
     */
    @Override
    public List getMonthPackage(String afterMonth,String periods) {
        List list = new ArrayList();
        List monthList = getSerPackageInfo("2");
        if(monthList!=null && monthList.size()>0){
            for(int i=0;i<monthList.size();i++){
                Map map = new HashMap();
                map = (Map) monthList.get(i);
                String month = map.get("afterMonth").toString();
                String periodCollection = String.valueOf(map.get("collectionPeriod"));//多少期
                String collectionType = String.valueOf(map.get("collectionType"));//收取期数类型
                if(afterMonth.equals(month)){
                    if("0".equals(collectionType)){//代表随产品期数收取
                        map.put("sqTypeRemark","随产品期数收取");
                        map.put("qsRemark",month+"期后可提前还款");
                        list.add(map);
                    }else{//代表自定义期数收取
                        if(Double.valueOf(periods)>=Double.valueOf(periodCollection)){
                            map.put("sqTypeRemark","收取"+periodCollection+"期");
                            map.put("qsRemark",month+"期后可提前还款");
                            list.add(map);
                        }
                    }
                }
            }
        }
        return list;
    }

    @Override
    public Map getSerPcgByOderId(String orderId) {
        String sql = "select amount_collection as amount,package_name as packageName from mag_servicepag_order where type= '1' and state != '1' and state != '3' and order_id = '"+orderId+"'";
        Map serPackMap = sunbmpDaoSupport.findForMap(sql);
        if (serPackMap != null && serPackMap.size() > 0){
            serPackMap.put("flag",true);
            return serPackMap;
        }else{
            serPackMap = new HashMap();
            serPackMap.put("flag",false);
        }
      return serPackMap;
    }
}
