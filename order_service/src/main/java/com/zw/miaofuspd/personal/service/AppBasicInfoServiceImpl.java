package com.zw.miaofuspd.personal.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.DateUtils;
import com.base.util.GeneratePrimaryKeyUtils;
import com.base.util.TraceLoggerUtil;
import com.constants.ApiConstants;
import com.constants.CommonConstant;
import com.enums.EIsIdentityEnum;
import com.zhiwang.zwfinance.app.jiguang.util.api.EApiSourceEnum;
import com.zw.api.HttpUtil;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.CustomerLinkmanBean;
import com.zw.miaofuspd.facade.entity.CustomerLogBean;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.facade.order.service.IAppInsapplicationService;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.service.base.AbsServiceBase;
import com.zw.service.exception.DAOException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;


@Service
public class AppBasicInfoServiceImpl extends AbsServiceBase implements AppBasicInfoService {
    @Autowired
    private IDictService dictServiceImpl;
    @Autowired
    private IAppInsapplicationService iAppInsapplicationService;
    @Autowired
    private AppOrderService appOrderService;
    @Autowired
    private ISystemDictService iSystemDictService;

    @Override
    public Map updateLinkManInfo( String customer_id, Map map1) throws Exception {
        Map retMap = new HashMap();
        insertOrUpdate((CustomerLinkmanBean) map1.get("customerLinkmanBean"), customer_id);
        insertOrUpdate((CustomerLinkmanBean) map1.get("customerLinkmanBean2"), customer_id);
        insertOrUpdate((CustomerLinkmanBean) map1.get("customerLinkmanBean3"), customer_id);
        insertOrUpdate((CustomerLinkmanBean) map1.get("customerLinkmanBean4"), customer_id);
        insertOrUpdate((CustomerLinkmanBean) map1.get("customerLinkmanBean5"), customer_id);
        insertOrUpdate((CustomerLinkmanBean) map1.get("customerLinkmanBean6"), customer_id);
        String magSql = "update mag_customer set link_man_complete = '100' where id = '" + customer_id + "'";
        sunbmpDaoSupport.exeSql(magSql);
        retMap.put("success", true);
        retMap.put("msg", "保存成功！");
        return retMap;
    }

    //新增或修改联系人信息
    public void insertOrUpdate(CustomerLinkmanBean customerLinkmanBean, String customerId) {
        String complete = "100";
        String id = null;
        if (customerLinkmanBean.getRelationShip() != null) {
            if (!("").equals(customerLinkmanBean.getId())) {
//                //根据Id获取原先的联系人信息
//                String selectsql = "select id,relationship,relationship_name,contact,link_name from mag_customer_linkman " +
//                        "where CUSTOMER_ID = '" + customerId + "'";
//                Map map = sunbmpDaoSupport.findForMap(selectsql);
//                id = map.get("id").toString();
                String sql = "update mag_customer_linkman set complete = '" + complete + "',relationship = '" + customerLinkmanBean.getRelationShip() + "',relationship_name = '" + customerLinkmanBean.getRelationshipName() + "',contact = '" + customerLinkmanBean.getContact() + "',link_name='" + customerLinkmanBean.getLinkName() + "' where id = '" + customerLinkmanBean.getId()+"'" ;
                sunbmpDaoSupport.exeSql(sql);
            } else {
                    id = GeneratePrimaryKeyUtils.getUUIDKey();
                    String sql = "insert into mag_customer_linkman(ID,relationship,contact,CREAT_TIME,complete,CUSTOMER_ID,relationship_name,link_name) " +
                            "values('" + id + "','" + customerLinkmanBean.getRelationShip() + "','" + customerLinkmanBean.getContact() + "','" + DateUtils.getDateString(new Date()) + "','" + complete + "','" + customerId + "','" + customerLinkmanBean.getRelationshipName() + "','" + customerLinkmanBean.getLinkName() + "') ";
                    sunbmpDaoSupport.exeSql(sql);
            }
        }
    }






    //根据传来的值来新增或修改联系人
    public void insertOrUpdate(CustomerLinkmanBean customerLinkmanBean, String userId, String orderId) {
        if (customerLinkmanBean.getRelationShip() != null) {
            if (!("").equals(customerLinkmanBean.getId())) {
                //根据Id获取原先的联系人信息
                String selectsql = "select id,relationship,relationship_name AS relationshipname,contact,link_name AS linkname " +
                        "from mag_customer_linkman where id = '" + customerLinkmanBean.getId() + "'";
                Map map = sunbmpDaoSupport.findForMap(selectsql);
                String sql = "update mag_customer_linkman set relationship = '" + customerLinkmanBean.getRelationShip() + "'," +"relationship_name =' "+customerLinkmanBean.getRelationshipName()+
                        "',contact = '" + customerLinkmanBean.getContact() + "'," + "link_name='" + customerLinkmanBean.getLinkName() + "' where ID = '" + customerLinkmanBean.getId() + "'";
                sunbmpDaoSupport.exeSql(sql);
                //插入修改日志
                CustomerLogBean customer = new CustomerLogBean();
                StringBuffer value = new StringBuffer();
                //关系
                String serialNo = customerLinkmanBean.getId();
                String link_name = map.get("linkname") + "";
                if (!link_name.equals(customerLinkmanBean.getLinkName())) {
                    value.append(" ");
                    value.append("联系人" + serialNo + "名称变更 原：" + link_name + " 新：" + customerLinkmanBean.getLinkName());
                }
                String relationship_name = map.get("relationshipname") + "";
                if (!relationship_name.equals(customerLinkmanBean.getRelationshipName())) {
                    value.append(" ");
                    value.append("联系人" + serialNo + "关系变更 原：" + relationship_name + " 新：" + customerLinkmanBean.getRelationshipName());
                }
                //联系方式
                String contact = map.get("contact") + "";
                if (!contact.equals(customerLinkmanBean.getContact())) {
                    value.append(" ");
                    value.append("联系人" + serialNo + "联系方式变更 原：" + contact + " 新：" + customerLinkmanBean.getContact());
                }
            } else {
                if (!("").equals(customerLinkmanBean.getLinkName()) && !("").equals(customerLinkmanBean.getContact())) {
                    String sql = "insert into mag_customer_linkman(ID,relationship,contact,CREAT_TIME,CUSTOMER_ID,relationship_name,link_name) " +
                            "values('" + UUID.randomUUID() + "','" + customerLinkmanBean.getRelationShip() + "','" + customerLinkmanBean.getContact() + "','" + DateUtils.getDateString(new Date()) + "','" + userId + "','" + customerLinkmanBean.getRelationshipName() + "','" + customerLinkmanBean.getLinkName() + "') ";
                    sunbmpDaoSupport.exeSql(sql);
                }
            }
        }
    }

   /**
    * @author:hanmeisheng
    * @Date 13:07 2018/5/12
    * @param
    * @return java.util.Map
    */

    @Override
    public Map addApplyInfo(Map<String, Object> paramMap) {
        Map resturnMap = new HashMap();

        String orderId = (String) paramMap.get("orderId");
        //申请金额
        //BigDecimal applayMoney = new BigDecimal( String.valueOf(paramMap.get("applyMoney")));
        Object applayMoney = paramMap.get("applyMoney");
        //申请期限
        String periods = paramMap.get("periods").toString();
        //借款用途
        String loanPurpose = (String) paramMap.get("loanPurpose");
        //获取订单的产品名称及编号
        String sql = "select t3.contractor_name as contractor_name,t1.product_name as product_name,t1.product_name_name as product_name_name from mag_order t1 left join byx_white_list t2 on t1.customer_name = t2.real_name and t1.card = t2.card left join byx_contractor t3 on t2.contractor_id = t3.id where t1.id='"+orderId+"'";
        Map map1 = sunbmpDaoSupport.findForMap(sql);
        //插入产品的利率
        String sql2 = "select t1.li_xi as lixi,t1.product_id as product_id,t1.zbs_jujian_fee as zbs_jujian_fee from mag_product_fee t1\n" +
                "inner join(\n" +
                "select id,product_term_min,product_term_max from pro_working_product_detail where crm_product_id =(\n" +
                "select id from  pro_crm_product where pro_name ='"+map1.get("product_name_name")+"' and pro_number = '"+map1.get("product_name")+"') and product_term_min*1 <= "+periods+" and product_term_max*1 >= "+periods+")t2 on t1.product_id = t2.id";
        Map map2 = sunbmpDaoSupport.findForMap(sql2);
        String lixi = map2.get("lixi")==null?"":map2.get("lixi").toString();
        String product_detail = map2.get("product_id")==null?"":map2.get("product_id").toString();
        String zbs_jujian_fee = map2.get("zbs_jujian_fee") == null?"":map2.get("zbs_jujian_fee").toString();
        String contractorName = map1.get("contractor_name") == null?"":map1.get("contractor_name").toString();
        double serviceFee = 0.0;
        if(StringUtils.isNotEmpty(zbs_jujian_fee)){
            serviceFee = Double.valueOf(getServiceFee(contractorName, zbs_jujian_fee))/100;
        }
        String sql3 = "update mag_order set applay_money = " + applayMoney + "," + "PERIODS = '" + periods + "'," +
                "loan_purpose = '" + loanPurpose + "',rate = '"+lixi+"',product_detail = '"+product_detail+"',Service_fee = '"+serviceFee+"'," +
                "complete = '100' where id = '" + orderId + "'  ";
        int count = sunbmpDaoSupport.executeSql(sql3);
        //sunbmpDaoSupport.exeSql(sql);
        if(count == 0){
            resturnMap.put("msg","保存客户基本信息失败");
            resturnMap.put("flag",false);
           return resturnMap;
        }
        resturnMap.put("msg", "保存客户基本信息成功");
        resturnMap.put("flag", true);
        return resturnMap;
    }

    /**
     * 获取居间服务费
     */
    private String  getServiceFee(String contractorName,String ServiceFee){
        String[] ServiceFeeArray =  ServiceFee.split(",");
        for(int i=0;i<ServiceFeeArray.length;i++){
            if(ServiceFeeArray[i].equals(contractorName)){
                return ServiceFeeArray[i+1] == null?"0":ServiceFeeArray[i+1];
            }
        }
        return "0";
    }


    /**
     * 办单员端-获取用户基本信息
     *
     * @param orderId 订单id
     * @return
     */
    @Override
    public Map getApplyInfo(String orderId) {
        //根据订单id获取客户基本信息
        String customerSql = "select t1.applay_money as apply_money,t1.PERIODS as PERIODS," +
                "t2.surplus_contract_amount as surplus_contract_amount,t3.latest_pay as latest_pay," +
                "t1.loan_purpose as loan_purpose from mag_order t1 left join mag_customer t2 on t1.CUSTOMER_ID = t2.id left join byx_white_list t3 on t3.real_name = t2.PERSON_NAME and t2.CARD = t3.card" +
                " where t1.id = '" + orderId + "'";
        Map resutMap = sunbmpDaoSupport.findForMap(customerSql);
        return resutMap;
    }

    /**
     * 申请信息三要素查询
     *
     * @param map 客户id
     * @return
     */
    @Override
    public Map addBasicCustomerInfo(Map<String, String> map) {
        String createTime = DateUtils.getCurrentTime(DateUtils.STYLE_10);
        Map<String,Object> resultMap = new HashMap<String,Object>(4);
        String id = map.get("id");
        String tel = map.get("tel");
        String card = map.get("card");
        String personName = map.get("personName");
        String productName = map.get("productName");
        String orderNo;
        String uuidKey;
        String orderId = GeneratePrimaryKeyUtils.getUUIDKey();
        try {
            //新增客户信息
            uuidKey = GeneratePrimaryKeyUtils.getUUIDKey();
            String sql1 = "insert into mag_customer (ID,USER_ID,PERSON_NAME,TEL,CARD,surplus_contract_amount,CREAT_TIME) values ('" + uuidKey + "','" + id + "','" + personName + "','" + tel + "','" + card + "',200000,'"+createTime+"')";
            sunbmpDaoSupport.exeSql(sql1);
            orderNo = String.valueOf(GeneratePrimaryKeyUtils.getOrderNum());
            //获取产品id
            String productSql = "select id from pro_crm_product where pro_name='"+productName+"' and pro_number = 'BYX0001'";
            Map proMap = sunbmpDaoSupport.findForMap(productSql);
            String product_id = proMap.get("id")==null?"":proMap.get("id").toString();
            //新增订单信息
            String sql2 = "insert into mag_order (ID,USER_ID,order_no,CUSTOMER_ID,order_state,product_id,product_name,product_name_name,CUSTOMER_NAME,TEL,CARD,CREAT_TIME) values ('"+orderId+"','"+id+"'" +
                        ",'"+orderNo+"','"+uuidKey+"','1','"+product_id+"','BYX0001','"+productName+"','"+personName+"','"+tel+"','"+card+"','"+createTime+"')";
            sunbmpDaoSupport.exeSql(sql2);
        } catch (DAOException e) {
            e.printStackTrace();
            resultMap.put("flag",false);
            resultMap.put("msg","创建用户或订单信息失败！");
            return resultMap;
        }
        resultMap.put("flag",true);
        resultMap.put("msg","创建用户和订单信息成功！");
        resultMap.put("orderId",orderId);
        resultMap.put("customerId",uuidKey);
        return resultMap;

    }

    //保存客户职业信息
    public String saveJobInfo(Map<String, String> paramMap) {

        return null;
    }

    /**
     * @author:hanmeisheng
     * @Description 获取用户的个人信息
     * @Date 18:59 2018/5/12
     * @param
     * @return
     */
    @Override
    public Map getBasicInfo(String customerId){
        Map resultMap = new HashMap();
        String  sql="select t5.contractor_name as contractor_name,t1.marital_status as marital_status,t1.children_status as children_status" +
                ",t2.province_name as jobProvince_name,t2.province_id as jobProvince_id,t2.city_name as jobCity_name,t2.city_id as jobCity_id,t2.district_name as jobDistrict_name,t2.district_id as jobDistrict_id,t2.address as jobDetailAddress," +
                "t3.provinces as cardProvinces,t3.provinces_id as cardProvinces_id,t3.city as cardCity,t3.city_id as cardCity_id,t3.distric as cardDistric,t3.distric_id as cardDistric_id,t3.address_detail as cardDetailAddress from mag_customer t1" +
                " left join mag_customer_job t2 on t1.id = t2.customer_id" +
                " left join mag_customer_live t3 on t1.id = t3.customer_id" +
                " left join byx_white_list t4 on t1.person_name = t4.real_name and t1.card = t4.card" +
                " left join byx_contractor t5 on t5.id = t4.contractor_id where t1.id = '"+customerId+"'";

        List<Map> list = sunbmpDaoSupport.findForList(sql);
        if(list.size()==0){
            resultMap.put("flag",false);
            resultMap.put("msg","查询失败，没有该用户的信息");
            return  resultMap;
        }
        resultMap.put("flag",true);
        resultMap.put("msg","查询成功！");
        resultMap.put("data",list.get(0));
        return resultMap;

    }


    /**
     * @author:hanmeisheng
     * @Description 保存用户的个人信息
     * @Date 14:11 2018/5/12
     * @param
     * @return
     */
    @Override
    public Map addBasicInfo(Map<String, String> paramMap) {
        Map resturnMap = new HashMap();
        String customerId = paramMap.get("customerId");

        String alterTime = DateUtils.getDateString(new Date());
        //婚姻状况
        String maritalSstatus =  paramMap.get("maritalSstatus");
        //子女状况
        String  childrenStatus = paramMap.get("childrenStatus");
        //籍贯居住详细地址
        String cardRegisterDetailAddress = paramMap.get("cardRegisterDetailAddress");
        //籍贯省市区
        String cardRegisterAddress = paramMap.get("cardRegisterAddress");
        //籍贯省市区id
        String cardRegisterAddressCode = paramMap.get("cardRegisterAddressCode");
        //工作居住详细地址
        String jodDetailAddress = paramMap.get("jodDetailAddress");
        //工作省市区
        String jobAddress = paramMap.get("jobAddress");
        //工作省市区id
        String jobAddressCode = paramMap.get("jobAddressCode");

        //客户信息表更新个人信息
        String  cusSql = "update mag_customer set marital_status ='"+maritalSstatus+"',children_status='"+childrenStatus+"'";
        sunbmpDaoSupport.exeSql(cusSql);
        Map<String, String> jmap = getAdress(jobAddress, jobAddressCode, jodDetailAddress);
        Map<String, String> cmap = getAdress(cardRegisterAddress, cardRegisterAddressCode, cardRegisterDetailAddress);
        String id = null;
        String complete = "1";
        try {
            //检查是否已经存在工作地居住地址
            StringBuffer sql=new StringBuffer("select id from mag_customer_job where customer_id='" + customerId + "'");
            List<Map> list = sunbmpDaoSupport.findForList(sql.toString());
            //检查是否已经存在户籍居住地址
            StringBuffer sql2=new StringBuffer("select id from mag_customer_live where customer_id='" + customerId + "'");
            List<Map> list2 = sunbmpDaoSupport.findForList(sql2.toString());
            //更新工作地居住地址
            if (list.size() > 0) {
                Map map = (Map) list.get(0);
                id = map.get("id") + "";
                sql = new StringBuffer("update mag_customer_job set ");
                sql.append("address='" + jodDetailAddress);
                sql.append("',province_name='" + jmap.get("pname"));
                sql.append("',city_name='" + jmap.get("cname"));
                sql.append("',district_name='" + jmap.get("dname"));
                sql.append("',province_id='" + jmap.get("pid"));
                sql.append("',city_id='" + jmap.get("cid"));
                sql.append("',district_id='" + jmap.get("did"));
                sql.append("',company_address='" + jmap.get("fullAddress"));
                sql.append("',alter_time='" + alterTime);
                sql.append("',complete='" + complete);
                sql.append("' where  id='" + id + "'");
                sunbmpDaoSupport.exeSql(sql.toString());
            } else {
                id = GeneratePrimaryKeyUtils.getUUIDKey();
                sql = new StringBuffer("insert into mag_customer_job (id,customer_id,address,province_name,city_name,district_name,province_id,city_id,district_id,company_address,complete,alter_time) values ('");
                sql.append(id + "','" + customerId + "','"  + jodDetailAddress + "','" +
                        jmap.get("pname") + "','" + jmap.get("cname") + "','" + jmap.get("dname") + "','" + jmap.get("pid") + "','" + jmap.get("cid") + "','" + jmap.get("did") + "','" + jmap.get("fullAddress") + "','1','" + alterTime +
                        "')");

                sunbmpDaoSupport.exeSql(sql.toString());
            }
            //更新户籍居住地址
            if (list2.size() > 0) {
                Map map = (Map) list2.get(0);
                id = map.get("id") + "";
                sql2 = new StringBuffer("update mag_customer_live set ");
                sql2.append("address_detail='" + cardRegisterDetailAddress);
                sql2.append("',provinces ='" + cmap.get("pname"));
                sql2.append("',city='" + cmap.get("cname"));
                sql2.append("',distric='" + cmap.get("dname"));
                sql2.append("',provinces_id='" + cmap.get("pid"));
                sql2.append("',city_id='" + cmap.get("cid"));
                sql2.append("',distric_id='" + cmap.get("did"));
                sql2.append("',nowaddress='" + cmap.get("fullAddress"));
                sql2.append("',alter_time='" + alterTime);
                sql2.append("',complete='" + complete);
                sql2.append("' where  id='" + id + "'");
                sunbmpDaoSupport.exeSql(sql.toString());
            } else {
                id = GeneratePrimaryKeyUtils.getUUIDKey();
                sql2 = new StringBuffer("insert into mag_customer_live (id,customer_id,address_detail,provinces,city,distric,provinces_id,city_id,distric_id,nowaddress,complete,alter_time) values ('");
                sql2.append(id + "','" + customerId + "','"  + cardRegisterDetailAddress + "','" +
                        cmap.get("pname") + "','" + cmap.get("cname") + "','" + cmap.get("dname") + "','" + cmap.get("pid") + "','" + cmap.get("cid") + "','" + cmap.get("did") + "','" + cmap.get("fullAddress") + "','1','" + alterTime +
                        "')");

                sunbmpDaoSupport.exeSql(sql2.toString());
            }
        } catch (Exception e) {
            TraceLoggerUtil.error("保存客户职业表出错！", e);
            resturnMap.put("msg", "保存客户基本信息失败");
            resturnMap.put("flag", false);
        }
        //更新完成状态
        String completeSql = "update mag_customer set Baseinfo_complete = '100' where id ='"+customerId+"'";
        sunbmpDaoSupport.exeSql(completeSql);
        resturnMap.put("msg", "保存客户基本信息成功");
        resturnMap.put("flag", true);
        return resturnMap;

    }

    //拼接省市区信息
    public Map<String,String> getAdress(String address,String addressCode,String detailAddress){
        String pname = "";//省名称
        String cname = "";//市名称
        String dname = "";//区名称

        if (address != null && !"".equals(address)) {
            String[] str = address.split("/");
            switch (str.length) {
                case 1:
                    pname = address.split("/")[0];
                    break;
                case 2:
                    pname = address.split("/")[0];
                    cname = address.split("/")[1];
                    break;
                case 3:
                    pname = address.split("/")[0];
                    cname = address.split("/")[1];
                    dname = address.split("/")[2];
                    break;
            }
        }
        String pid = "";//省code
        String cid = "";//市code
        String did = "";//区code
        if (addressCode != null && !"".equals(addressCode)) {
            String[] str = addressCode.split("/");
            switch (str.length) {
                case 1:
                    pid = addressCode.split("/")[0];
                    break;
                case 2:
                    pid = addressCode.split("/")[0];
                    cid = addressCode.split("/")[1];
                    break;
                case 3:
                    pid = addressCode.split("/")[0];
                    cid = addressCode.split("/")[1];
                    did = addressCode.split("/")[2];
                    break;
            }
        }
        String fullAddress = pname + cname + dname + detailAddress;
        Map<String,String> map = new HashMap<String, String>();
        map.put("fullAddress",fullAddress);
        map.put("pid",pid);
        map.put("cid",cid);
        map.put("did",did);
        map.put("pname",pname);
        map.put("cname",cname);
        map.put("dname",dname);
        return map;
    }


    /**
     * 获取该用户下所有的联系人
     *
     * @param customerId
     * @return
     */
    @Override
    public Map getLinkMan(String customerId) throws Exception {
        Map retMap = new HashMap();
        //查询linkman表
        String sql = "select id,relationship,relationship_name AS relationshipname,contact,link_name AS linkName from mag_customer_linkman where customer_id = '" + customerId + "' ";
        List list = sunbmpDaoSupport.findForList(sql);
        retMap.put("linkmanlist", list);
        //查询linkman表中的其他亲属前两个人
//        String sql2 = "select id,relationship,relationship_name AS relationshipname,contact,link_name AS linkName from mag_customer_linkman where type='1' and customer_id = '" + customerId + "' and main_sign =1 order by CREAT_TIME LIMIT 0,2";
//        List list2 = sunbmpDaoSupport.findForList(sql2);
//        retMap.put("olinkmanlist", list2);
        return retMap;
    }

    /**
     * 在申请前判断是否填过草稿信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Map getPersonInfo(String id) throws Exception {
        Map resMap = new HashMap();
        String sql = "select is_identity,person_name,tel,card from mag_customer where USER_ID = '" + id + "'";
        List list = sunbmpDaoSupport.findForList(sql);
        if (list.size()==0){
            resMap.put("code","1");
            return resMap;
        }
        resMap.put("code","");
        return resMap;
    }

    /**
     * 获取申请主页面信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Map getHomeApplyInfo(String id,String productName) throws Exception {
        Map resMap = new HashMap();
        Map resultMap = new HashMap(4);
        //根据登录用户id获取客户信息表id
        String sql1 = "select id,PERSON_NAME,TEL,CARD from mag_customer where USER_ID = '" + id + "'";
        Map cusmap = sunbmpDaoSupport.findForMap(sql1);
        //获取当前用户的所有订单状态
        String  sql2 = "select order_state as state from mag_order where user_id = '"+id+"' and product_name_name = '"+productName+"'";
        List<Map> staList = sunbmpDaoSupport.findForList(sql2);
        //申请主页面的相关信息
        String sql3 = "select date_format(str_to_date(t2.CREAT_TIME,'%Y%m%d%H%i%s'),'%Y-%m-%d %H:%i:%s') as creat_time,t1.id as customerId,t1.card as card,t2.applay_money as applay_money,t1.tel as tel,t1.PERSON_NAME as personName,t1.Baseinfo_complete as baseinfoComplete" +
                ",t2.complete as applyComplete,t2.id as orderId,t1.is_identity as is_identity,t1.authorization_complete as authorization_complete,t1.link_man_complete as link_man_complete  from mag_customer t1 left join  mag_order t2 on t1.id = t2.CUSTOMER_ID " +
                " where t1.user_id = '" +id+ "'and t2.product_name_name= '"+productName+"'and t2.order_state='1'";
        for(Map map:staList){
            //未完成订单
            if( CommonConstant.ORDER_STATE_PREAPPLY.equals(map.get("state"))){
                //获取申请主页面的未完成订单的基本信息及资料的完成状态
                resMap = sunbmpDaoSupport.findForMap(sql3);
                resultMap.put("code","2");
                resultMap.put("resMap",resMap);
                resultMap.put("msg","成功获取未完成订单！");
                return resultMap;

            }else if(CommonConstant.ORDER_STATE_REVIEW.equals(map.get("state") )){
                //当有未结清订单，不允许访问申请主页面
                resultMap.put("code","3");
                resultMap.put("msg","您有审核中的订单!");
                return resultMap;
            }else if(CommonConstant.ORDER_STATE_PRESIGNED.equals(map.get("state"))){
                resultMap.put("code","3");
                resultMap.put("msg","您有待签约的订单!");
                return resultMap;
            }else if(CommonConstant.ORDER_STATE_PRELOAN.equals(map.get("state"))){
                resultMap.put("code","3");
                resultMap.put("msg","您有待放款的订单!");
                return resultMap;
            }else if(CommonConstant.ORDER_STATE_PREREPAYMENT.equals(map.get("state"))){
                resultMap.put("code","3");
                resultMap.put("msg","您有待还款的订单!");
                return resultMap;
            }
        }
        //新增订单信息
        String sql4 = "insert into mag_order (ID,USER_ID,order_no,CUSTOMER_ID,order_state,product_name,product_name_name,CUSTOMER_NAME,TEL,CARD,CREAT_TIME) values ('"+GeneratePrimaryKeyUtils.getUUIDKey()+"','"+id+"'," +
                "'"+GeneratePrimaryKeyUtils.getOrderNum()+"','"+cusmap.get("id")+"','1','BYX0001','"+productName+"','"+cusmap.get("PERSON_NAME")+"','"+cusmap.get("TEL")+"','"+cusmap.get("CARD")+"','"+DateUtils.getCurrentTime(DateUtils.STYLE_10)+"')";
        sunbmpDaoSupport.exeSql(sql4);
        resMap = sunbmpDaoSupport.findForMap(sql3);
        resultMap.put("code","4");
        resultMap.put("msg","新增订单!");
        resultMap.put("resMap",resMap);
        return resultMap;
    }

    @Override
    public void saveTongXunLu(String customerId, String data) throws Exception {
        data = data.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "");
        String updateSql = "update mag_customer set phoneBookList='" + data + "' where id ='" + customerId + "'";
        sunbmpDaoSupport.exeSql(updateSql);
    }

    /**
     * 秒付金服获取个人信息
     *
     * @param customerId
     * @return
     * @throws Exception
     */
    @Override
    public Map getMiaofuBasicInfo(String customerId) throws Exception {
        List linkManList = dictServiceImpl.getDictJson("联系人关系");
        List otherLinkManList = dictServiceImpl.getDictJson("其他联系人");
        Map resutMap = new HashMap();
        //获取基本信息
        String basicSql = "SELECT marry,educational,educational_name,marry_name from mag_customer_person where customer_id='" + customerId + "'";
        Map basicMap = sunbmpDaoSupport.findForMap(basicSql);
        //获取职业信息
        String OccupationSql = "SELECT company_name,company_phone,address,province_name,city_name,district_name,province_id,city_id,district_id from mag_customer_job where customer_id='" + customerId + "'";
        Map OccupationMap = sunbmpDaoSupport.findForMap(OccupationSql);
        //获取联系人信息
        String linkManSql = "SELECT id,link_name,contact,relationship,relationship_name,main_sign from mag_customer_linkman where CUSTOMER_ID='" + customerId + "'";
        List olist = sunbmpDaoSupport.findForList(linkManSql);
        resutMap.put("linkManList", linkManList);
        resutMap.put("olist", olist);
        resutMap.put("basicMap", basicMap);
        resutMap.put("OccupationMap", OccupationMap);
        resutMap.put("otherLinkManList", otherLinkManList);
        resutMap.put("flag", true);
        return resutMap;
    }

    //保存客户居住信息
    public String saveLiveInfo(Map<String, String> paramMap) {
//        String customerId = paramMap.get("customerId");
//        String alterTime = DateUtils.getDateString(new Date());
//        //客户职业信息
//        String address = paramMap.get("liveAddress");//居住地址
//        String address_code = paramMap.get("liveAddressId");//省市区code
//        String pname = "";//省名称
//        String cname = "";//市名称
//        String dname = "";//区名称
//        if (address != null && !"".equals(address)) {
//            String[] str = address.split("/");
//            switch (str.length) {
//                case 1:
//                    pname = address.split("/")[0];
//                    break;
//                case 2:
//                    pname = address.split("/")[0];
//                    cname = address.split("/")[1];
//                    break;
//                case 3:
//                    pname = address.split("/")[0];
//                    cname = address.split("/")[1];
//                    dname = address.split("/")[2];
//                    break;
//            }
//        }
//        String pid = "";//省code
//        String cid = "";//市code
//        String did = "";//区code
//        if (address_code != null && !"".equals(address_code)) {
//            String[] str = address_code.split("/");
//            switch (str.length) {
//                case 1:
//                    pid = address_code.split("/")[0];
//                    break;
//                case 2:
//                    pid = address_code.split("/")[0];
//                    cid = address_code.split("/")[1];
//                    break;
//                case 3:
//                    pid = address_code.split("/")[0];
//                    cid = address_code.split("/")[1];
//                    did = address_code.split("/")[2];
//                    break;
//            }
//        }
//        String addressDetail = paramMap.get("addressDetail");//居住详细地址
//        String id = "";
//        String complete = "1";
//        try {
//            //检查是否已经存在客户资料
//            StringBuffer sql = new StringBuffer("select ID,nowaddress,address_detail,complete from mag_customer_live where customer_id='" + customerId + "' and type ='1'");
//            List<Map> list = sunbmpDaoSupport.findForList(sql.toString());
//            if (list.size() > 0) {
//                //更新证件资料
//                Map map = (Map) list.get(0);
//                id = map.get("ID") + "";
//                sql = new StringBuffer("update mag_customer_live set ");
//                sql.append("provinces='" + pname);
//                sql.append("',city='" + cname);
//                sql.append("',distric='" + dname);
//                sql.append("',provinces_id='" + pid);
//                sql.append("',city_id='" + cid);
//                sql.append("',distric_id='" + did);
//                sql.append("',address_detail='" + addressDetail);
//                sql.append("',ALTER_TIME='" + alterTime);
//                sql.append("',complete='" + complete);
//                sql.append("' where  id='" + id + "'");
//                sunbmpDaoSupport.exeSql(sql.toString());
//            } else {
//                id = UUID.randomUUID() + "";
//                sql = new StringBuffer("insert into mag_customer_live (ID,type,customer_id,provinces,city,distric,provinces_id,city_id,distric_id,address_detail,complete,CREAT_TIME,ALTER_TIME) VALUES('");
//                sql.append(id + "','1','" + customerId + "','" + pname + "','" + cname + "','" + dname + "','" + pid + "','" + cid + "','" + did + "','" + addressDetail + "','" + complete + "','" + alterTime + "','" + alterTime +
//                        "')");
//                sunbmpDaoSupport.exeSql(sql.toString());
//            }
//        } catch (Exception e) {
//            TraceLoggerUtil.error("保存客户信息表出错！", e);
//        }
        return null;
    }

    //保存客户联系人信息
    public Map saveLinkManInfo(Map<String, String> paramMap) {
        Map outMap = new HashMap();
        String customerId = paramMap.get("customerId");
        //直系联系人
        String linkDirectId = paramMap.get("linkDirectId");
        String link_name = paramMap.get("linealRel");//联系人姓名
        String contact = paramMap.get("PersonDirectRelativesPhone");//手机号码
        String relationship_name = paramMap.get("relationship");//关系名字
        String relationship = paramMap.get("relationValue");//关系code
        CustomerLinkmanBean customerLinkmanBean = new CustomerLinkmanBean();
        customerLinkmanBean.setId(linkDirectId);
        customerLinkmanBean.setLinkName(link_name);
        customerLinkmanBean.setContact(contact);
        customerLinkmanBean.setRelationshipName(relationship_name);
        customerLinkmanBean.setRelationShip(relationship);
        customerLinkmanBean.setMainSign("0");

        //直系联系人1
        String linkDirectId1 = paramMap.get("linkDirectId1");
        String link_name1 = paramMap.get("linealRel1");//联系人姓名
        String contact1 = paramMap.get("PersonDirectRelativesPhone1");//手机号码
        String relationship_name1 = paramMap.get("relationship1");//关系名字
        String relationship1 = paramMap.get("relationValue1");//关系code

        CustomerLinkmanBean customerLinkmanBean1 = new CustomerLinkmanBean();
        customerLinkmanBean1.setId(linkDirectId1);
        customerLinkmanBean1.setLinkName(link_name1);
        customerLinkmanBean1.setContact(contact1);
        customerLinkmanBean1.setRelationshipName(relationship_name1);
        customerLinkmanBean1.setRelationShip(relationship1);
        customerLinkmanBean1.setMainSign("0");

        //其他联系人
        String ortherId = paramMap.get("linkOtherId");
        String link_name2 = paramMap.get("otherRel");//联系人姓名
        String contact2 = paramMap.get("otherRelativesPhone");//手机号码
        String relationship_name2 = paramMap.get("otherRelationship");//关系名字
        String relationship2 = paramMap.get("relationOtherValue");//关系code

        CustomerLinkmanBean customerLinkmanBean2 = new CustomerLinkmanBean();
        customerLinkmanBean2.setId(ortherId);
        customerLinkmanBean2.setLinkName(link_name2);
        customerLinkmanBean2.setContact(contact2);
        customerLinkmanBean2.setRelationshipName(relationship_name2);
        customerLinkmanBean2.setRelationShip(relationship2);
        customerLinkmanBean2.setMainSign("1");

        //其他联系人
        String ortherId3 = paramMap.get("linkOtherId1");
        String link_name3 = paramMap.get("otherRel1");//联系人姓名
        String contact3 = paramMap.get("otherRelativesPhone1");//手机号码
        String relationship_name3 = paramMap.get("otherRelationship1");//关系名字
        String relationship3 = paramMap.get("relationOtherValue1");//关系code

        CustomerLinkmanBean customerLinkmanBean3 = new CustomerLinkmanBean();
        customerLinkmanBean3.setId(ortherId3);
        customerLinkmanBean3.setLinkName(link_name3);
        customerLinkmanBean3.setContact(contact3);
        customerLinkmanBean3.setRelationshipName(relationship_name3);
        customerLinkmanBean3.setRelationShip(relationship3);
        customerLinkmanBean3.setMainSign("1");


        Map map = new HashMap();
        map.put("customerLinkmanBean", customerLinkmanBean);
        map.put("customerLinkmanBean", customerLinkmanBean1);
        map.put("customerLinkmanBean2", customerLinkmanBean2);
        map.put("customerLinkmanBean", customerLinkmanBean3);
//        String relId = insertOrUpdate((CustomerLinkmanBean) map.get("customerLinkmanBean"), customerId);
//        String relId1 = insertOrUpdate((CustomerLinkmanBean) map.get("customerLinkmanBean"), customerId);
//        String otherId = insertOrUpdate((CustomerLinkmanBean) map.get("customerLinkmanBean2"), customerId);
//        String otherId1 = insertOrUpdate((CustomerLinkmanBean) map.get("customerLinkmanBean2"), customerId);
//        outMap.put("relId", relId);
//        outMap.put("relId1", relId1);
//        outMap.put("otherId", otherId);
//        outMap.put("otherId1", otherId1);
        return outMap;
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
            String sql = "insert into customer_device_info (id,order_id,apply_province,apply_city,apply_area,apply_address,imei_number," +
                    "operate_system,device_type,tel_memory_run,tel_memory,tel_model,tel_brand,network_type,wifi_name,wifi_ssid,ip_address," +
                    "ip_province,ip_city,ip_area,is_root,is_prison,is_moni_online,location_permission,create_time,alter_time,latitude,longitude,black_box,type,user_id) values" +
                    "('" + id + "','" + map.get("orderId") + "','','','','" + apply_address + "','','"
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
     * @author:hanmeisheng
     * @Description  获取省份信息
     * @Date 13:37 2018/5/12
     * @param
     * @return java.util.List<java.util.Map>
     */

    @Override
    public List<Map> getProvinceList() {
        String sql="select id,province_name as name from zw_sys_province";
        List<Map> list = sunbmpDaoSupport.findForList(sql);
        return list;
    }

    /**
     * @author:hanmeisheng
     * @Description  获取市信息
     * @Date 13:37 2018/5/12
     * @param privinceId 省id
     * @return java.util.List<java.util.Map>
     */

    @Override
    public List<Map> getCityList(String privinceId) {
        String sql="select id,city_name as name from zw_sys_city where province_id='"+privinceId+"'";
        List<Map> list = sunbmpDaoSupport.findForList(sql);
        return list;
    }

    /**
     * @author:hanmeisheng
     * @Description  获取区信息
     * @Date 13:37 2018/5/12
     * @param cityId 市的id
     * @return java.util.List<java.util.Map>
     */

    @Override
    public List<Map> getDistrictList(String cityId) {
        String sql="select id,district_name as name from zw_sys_district where city_id='"+cityId+"'";
        List<Map> list = sunbmpDaoSupport.findForList(sql);
        return list;
    }

    /**
     * @author:hanmeisheng
     * @Description 一键申请
     * @Date 16:06 2018/5/12
     * @param
     * @return
     */
    @Override
    public Map oneClickApply(String orderId) {
        Map resultMap = new HashMap();
        try {
            //查询订单相关的列表信息
            String sql = "select t2.order_no as order_no,t2.customer_id as customer_id,t1.user_id as user_id,t2.CUSTOMER_NAME as CUSTOMER_NAME,t2.product_name_name as product_name_name,t1.person_name as person_name,t1.card as card,t1.tel as tel,t1.marital_status as marital_status,t1.children_status as children_status," +
                    "  t4.company_address as residence_address,t3.nowaddress as card_register_address," +
                    "  t2.product_name as product_name,t2.applay_money as applay_money,t2.periods as periods,t2.contract_amount as contract_amount," +
                    "  t2.loan_purpose as loan_purpose from mag_customer t1  left join mag_order t2 on t1.id = t2.CUSTOMER_ID LEFT JOIN mag_customer_live t3 on t1.id = t3.customer_id" +
                    "  left join mag_customer_job t4 on t1.id = t4.customer_id " +
                    "  where t2.id='"+orderId+"'";
            Map forMap = sunbmpDaoSupport.findForMap(sql);
            //查询联系人信息
            String linkSql = "select link_name,contact,relationship_name from mag_customer_linkman where customer_id='"+forMap.get("customer_id")+"'";
            List<Map> list = sunbmpDaoSupport.findForList(linkSql);
            Object linkjson = JSONObject.toJSON(list);
            //保存订单信息到订单进件信息表
            String id = GeneratePrimaryKeyUtils.getUUIDKey();
            BigDecimal applayMoney = new BigDecimal( forMap.get("applay_money").toString());
            BigDecimal contractAmount = new BigDecimal(forMap.get("contract_amount").toString());
            String sql2 = "insert into mag_order_entry values ('"+id+"','"+forMap.get("order_no")+"','"+forMap.get("customer_id")+"','"+forMap.get("person_name")+"'" +
                    ",'"+forMap.get("card")+"','"+forMap.get("tel")+"','"+forMap.get("marital_status")+"','"+forMap.get("children_status")+"',''" +
                    ",'"+forMap.get("residence_address")+"','"+forMap.get("card_register_address")+"','"+forMap.get("product_name")+"','"+applayMoney+"'" +
                    ",'"+forMap.get("periods")+"','"+contractAmount+"','"+forMap.get("loan_purpose")+"','"+linkjson+"','"+DateUtils.getDateString(new Date())+"')";
            sunbmpDaoSupport.exeSql(sql2);
            //申请时间
            String applyTime = DateUtils.getCurrentTime(DateUtils.STYLE_10);
            //修改订单状态为已提交
            String sql3 = "update mag_order set Order_state = '2',applay_time = '"+applyTime+"' where id = '"+orderId+"'";
            sunbmpDaoSupport.exeSql(sql3);

            //根据订单编号获取订单id
//            String sql4 = "select id,applay_money from mag_order where id = '"+orderId+"'";
//            Map orderMap = sunbmpDaoSupport.findForMap(sql4);
            //新增操作表订单信息
            String sql5 = "insert into order_operation_record (id,operation_node,operation_result,order_id,operation_time,amount,emp_id,emp_name,description) values ('"+GeneratePrimaryKeyUtils.getUUIDKey()+"'," +
                    "1,1,'"+orderId+"','"+applyTime+"','"+forMap.get("applay_money")+"','"+forMap.get("user_id")+"','"+forMap.get("CUSTOMER_NAME")+"','已申请')";
            sunbmpDaoSupport.exeSql(sql5);
        } catch (DAOException e) {
            e.printStackTrace();
            resultMap.put("flag",false);
            resultMap.put("msg","一键申请提交失败");
            return resultMap;
        }
        resultMap.put("flag",true);
        resultMap.put("msg","一键申请提交成功");
        return  resultMap;
    }

    /**
     * @author:韩梅生
     * @Description 用户信息强规则
     * @Date 20:16 2018/5/14
     * @param
     */
    @Override
    public Map checkCustomerInfo(String costomerId,String card)  {
        Map resultMap = new HashMap(3);
        Map workMap = null;
        long workTimeDiff = -1L;

        try {
            //验证用户年龄
            Date age = DateUtils.strConvertToDateByType(card.substring(6,14),DateUtils.STYLE_3);
            Date now =  new Date();
            long  days = DateUtils.getDifferenceDays(now,age);
            long year = days/365L;
            if(year < CommonConstant.MIN_AGE || year > CommonConstant.MAX_AGE){
                resultMap.put("flag",false);
                resultMap.put("msg","您的年龄不符合申请条件");
                return  resultMap;
            }
            //验证工作时间是否满一个月
            String sql = "select t2.contract_start_date as contract_start_date,t2.job as job from mag_customer t1 " +
                    "left join byx_white_list t2 on t1.PERSON_NAME = t2.real_name and t1.card = t2.card where t1.ID = '"+costomerId+"'";
            workMap = sunbmpDaoSupport.findForMap(sql);
            String contractStartDate = workMap.get("contract_start_date") == null?"":workMap.get("contract_start_date").toString();
            if(StringUtils.isNotEmpty(contractStartDate)){
                Date workTime = DateUtils.strConvertToDateByType(contractStartDate,DateUtils.STYLE_3);
                workTimeDiff = DateUtils.getDifferenceDays(now,workTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("flag",false);
            resultMap.put("msg","系统繁忙，请稍后重试");
            return  resultMap;
        }
        if(workMap.get("job").toString().equals("3")){
            resultMap.put("flag",false);
            resultMap.put("msg","工地临时工不符合申请条件");
            return  resultMap;
        }
        if(workTimeDiff < 30 && workTimeDiff != -1L){
            resultMap.put("flag",false);
            resultMap.put("msg","所在工地工作未满1个月");
            return  resultMap;
        }
        resultMap.put("flag",true);
        resultMap.put("msg","信息验证通过");
        return  resultMap;
    }

    /**
     * @author:韩梅生
     * @Description 获取用户的实名信息
     * @Date 18:18 2018/5/16
     * @param
     */
    @Override
    public Map getRealName(String userId){
        Map resultMap = new HashMap();
        String sql = "select t1.PERSON_NAME as cust_name,t1.card as card,t1.tel as tel,t2.bank_name as bank_name,t2.bank_number as bank_number,t2.bank_subbranch_id as bank_subbranch_id," +
                "t2.bank_subbranch as bank_subbranch,t2.card_number as card_number,t2.prov_id as prov_id,t2.prov_name as prov_name,t2.city_id as city_id," +
                "t2.city_name as city_name from mag_customer t1 left join sys_bank_card t2 on t1.id = t2.cust_id where t1.user_id = '"+userId+"'";
        resultMap =  sunbmpDaoSupport.findForMap(sql);
        return resultMap;
    }

    /**
     * @author:韩梅生
     * @Description 保存用户的实名信息
     * @Date 18:48 2018/5/16
     * @param
     */
    @Override
    public  Map saveRealName(Map map){
        Map resulMap = new HashMap(3);
        //根据userId获取customerId
        String cusSql  = "select id from mag_customer where user_id = '"+map.get("userId")+"'";
        Map forMap = sunbmpDaoSupport.findForMap(cusSql);

        //新增或修改银行卡信息
        String sql1 = "select * from sys_bank_card where cust_id = '"+forMap.get("id")+"'";
        List<Map> list = sunbmpDaoSupport.findForList(sql1);
        try {
            if(list.size()==0){
                //新增银行卡信息
                String sql2 = "insert into sys_bank_card values ('"+GeneratePrimaryKeyUtils.getUUIDKey()+"','"+map.get("bank_name")+"'," +
                        "'"+map.get("bank_number")+"','"+map.get("bank_subbranch_id")+"','"+map.get("bank_subbranch")+"','"+map.get("card_number")+"','"+forMap.get("id")+"','"+map.get("cust_name")+"'," +
                        "'"+map.get("prov_id")+"','"+map.get("prov_name")+"','"+map.get("city_id")+"','"+map.get("city_name")+"','"+DateUtils.getNowDate()+"','"+DateUtils.getNowDate()+"')";
                sunbmpDaoSupport.exeSql(sql2);
            }else {
                String sql3 = "update sys_bank_card set bank_name = '"+map.get("bank_name")+"',bank_number = '"+map.get("bank_number")+"',bank_subbranch_id ='"+map.get("bank_subbranch_id")+"',bank_subbranch = '"+map.get("bank_subbranch")+"',card_number = '"+map.get("card_number")+"'," +
                        "cust_name = '"+map.get("cust_name")+"',prov_id = '"+map.get("prov_id")+"',prov_name = '"+map.get("prov_name")+"',city_id = '"+map.get("city_id")+"'," +
                        "city_name = '"+map.get("city_name")+"',update_time = '"+DateUtils.getNowDate()+"' where cust_id = '"+forMap.get("id")+"'";
                sunbmpDaoSupport.exeSql(sql3);
            }
            String card = map.get("card").toString();
            String sexNo = card.substring(16,17);
            if(Integer.valueOf(sexNo)%2==0){
                sexNo = "女";
            }else {
                sexNo = "男";
            }
            String birth = card.substring(6,14);
            String birthName = birth.substring(0,4)+"年"+birth.substring(4,6)+"月"+birth.substring(6,8)+"日";

            String sql4 = "update mag_customer set PERSON_NAME='"+map.get("cust_name")+"',sex_name = '"+sexNo+"',birth = '"+birthName+"',tel = '"+map.get("tel")+"',card = '"+card+"',is_identity = '1' where id = '"+forMap.get("id")+"'";
            sunbmpDaoSupport.exeSql(sql4);
        } catch (DAOException e) {
            e.printStackTrace();
            resulMap.put("flag",false);
            resulMap.put("msg","保存失败");
            return resulMap;
        }
        resulMap.put("flag",true);
        resulMap.put("msg","保存成功");
        return resulMap;
    }

    @Override
    public Map findById(String id)  {
        StringBuilder sql = new StringBuilder("select id from mag_customer where user_id = '").append(id).append("'");
        Map forMap = sunbmpDaoSupport.findForMap(sql.toString());
        sql = new StringBuilder("select cust.id,cust.PERSON_NAME,cust.TEL,cust.CARD,job.company_address ");
        sql.append("from mag_customer cust INNER JOIN mag_customer_job job on cust.id = job.customer_id ")
                .append("where cust.id = '").append(forMap.get("id")).append("'");
        return sunbmpDaoSupport.findForMap(sql.toString());
    }

    @Override
    public int updateSynById(String userID, String accountId,String custId) {
        StringBuilder sql = new StringBuilder("update mag_customer set  sync_user_id = '"+userID+"', sync_account_id ='"+accountId+"'  WHERE ID = '"+ custId +"'");
        return sunbmpDaoSupport.executeSql(sql.toString());
    }

    @Override
    public int cancelOrder(String orderId) {
        Map resultMap = new HashMap();
        //取消订单将申请时间改为已取消
        String sql = "update mag_order set order_state = '7',ALTER_TIME='"+DateUtils.getNowDate()+"' where id = '"+orderId+"'";
        return sunbmpDaoSupport.executeSql(sql);
    }
    @Override
    public Map getEmpowerStatus(String orderId,String customerId){
        Map<String,String> resultMap = new HashMap<String, String>(2);
        resultMap.put("mohe","0");
        resultMap.put("zhengxin","0");
        Map customerMap = getThreeItems(customerId);
        //获取魔盒授权状态
        int moheCount = findEmpowerStatus(EApiSourceEnum.MOHE.getCode(),customerMap);
        //获取个人征信授权状态
        int creditCount = findEmpowerStatus(EApiSourceEnum.CREDIT.getCode(),customerMap);
        if(moheCount == 1){
            resultMap.put("mohe","1");
        }
        if(creditCount == 1){
            resultMap.put("zhengxin","1");
        }
        String sql2;
        if(moheCount + creditCount == 2 ){
            sql2 = "update mag_customer set authorization_complete = '100' where id = '"+customerId+"'";
        }else {
            sql2 = "update mag_customer set authorization_complete = '0' where id = '"+customerId+"'";
        }
        sunbmpDaoSupport.exeSql(sql2);
        return resultMap;
    }

    /**
     * 获取授权状态
     */
    private  int findEmpowerStatus(String sourceCode,Map customerMap){
        String sql = "SELECT count(1) from zw_api_result where source_code = '"+sourceCode+"' and state = "+ApiConstants.STATUS_CODE_STATE+" and code = "+ApiConstants.STATUS_SUCCESS+" and  created_time >= date_add(NOW(), interval -1 MONTH) and real_name = '"+customerMap.get("PERSON_NAME")+"' and user_mobile = '"+customerMap.get("TEL")+"' and identity_code = '"+customerMap.get("CARD")+"' ORDER BY created_time desc LIMIT 1";
        return sunbmpDaoSupport.getCount(sql);
    }

    /**
     * 获取用户三要素信息
     * @param customerId
     * @return
     */
    public Map getThreeItems(String customerId){
        StringBuilder sql = new StringBuilder("select PERSON_NAME,TEL,CARD from mag_customer where id = '").append(customerId).append("'");
        return  sunbmpDaoSupport.findForMap(sql.toString());
    }


    @Override
    public Map getOrderDetailById(String orderId,String customerId) {
        Map reslutMap = new HashMap(3);
        String sql1 = "select t2.order_no as order_no,t2.product_name_name as product_name_name,t2.loan_amount as loan_amount,t2.rate as rate,t2.PERIODS as periods,date_format(str_to_date(t2.applay_time,'%Y%m%d%H%i%s'),'%Y-%m-%d %H:%i:%s') as applay_time," +
                "t2.loan_purpose as loan_purpose,t2.customer_name as customer_name,t2.card as card ,t2.tel as tel,t1.zbs_jujian_fee  as zbs_jujian_fee,t1.li_xi as lixi,t3.repayment as repayment,t3.repayment_days as repayment_days" +
                " from mag_product_fee t1 left join mag_order t2 on t1.product_id = t2.product_detail left join pro_working_product_detail t3 on t1.product_id = t3.id where t2.id = '"+orderId+"'";
        Map orderMap =sunbmpDaoSupport.findForMap(sql1);

        //根据用户id获取用户相关信息
        String sql2 = "select t1.user_id as user_id,t4.contractor_name as contractor_name,t1.sync_user_id as sync_user_id,t1.sync_account_id as sync_account_id,t2.company_address as company_address  " +
                "from mag_customer t1 left join mag_customer_job t2 on t1.id=t2.customer_id left join byx_white_list t3 on t1.person_name = t3. real_name " +
                "left join byx_contractor t4 on t3.contractor_id = t4.id where t1.id = '"+customerId+"'";
        Map customerMap =sunbmpDaoSupport.findForMap(sql2);
        String zbs_jujian_fee = orderMap.get("zbs_jujian_fee")==null?"":orderMap.get("zbs_jujian_fee").toString();
        String  lixi = orderMap.get("lixi")==null?"0":orderMap.get("lixi").toString();
        String jujian_fee="0";
        if(StringUtils.isNotEmpty(zbs_jujian_fee)){
            String[] zbs_jujian =  zbs_jujian_fee.split(",");
            String contractorName = customerMap.get("contractor_name") == null?"":customerMap.get("contractor_name").toString();
            for(int i=0;i<zbs_jujian.length;i++){
                if(zbs_jujian[i].equals(contractorName)){
                    jujian_fee = zbs_jujian[i+1];
                }
            }
        }
        DecimalFormat df = new DecimalFormat("#0.0000");
        //融资成本
        String assetFinanceCost = df.format(Double.parseDouble(jujian_fee)/100+Double.parseDouble(lixi)/100);
        //居间服务费年化率
        String assetServiceRate = df.format(Double.parseDouble(jujian_fee)*365/100);
        orderMap.put("assetFinanceCost",assetFinanceCost);
        orderMap.put("assetServiceRate",assetServiceRate);

        //获取银行卡信息
        Map bankMap = getRealName(customerMap.get("user_id").toString());
        reslutMap.put("orderMap",orderMap);
        reslutMap.put("customerMap",customerMap);
        reslutMap.put("bankMap",bankMap);
        return reslutMap;
    }

    @Override
    public Map getAuthorStatus(String userId) {
        Map resultMap = new HashMap();
        String sql = "select count(1) from mag_customer where user_id = '"+userId+"' and is_identity = '1'";
        int i = sunbmpDaoSupport.getCount(sql);
        if(i==0){
            resultMap.put("msg","该用户未实名认证！");
            resultMap.put("code","0");
            return  resultMap;
        }
        resultMap.put("msg","该用户已实名认证！");
        resultMap.put("code","1");
        return resultMap;
    }

    @Override
    public Boolean isAuthentication(String userId) {
        StringBuilder sql  = new StringBuilder("select is_identity from mag_customer where USER_ID = '");
        sql.append(userId).append("' ");
        final Map forMap = sunbmpDaoSupport.findForMap(sql.toString());
        if(forMap != null){
            final Object identity = forMap.get("is_identity");
            return identity != null && EIsIdentityEnum.CERTIFIED.getCode().equals(identity.toString());
        }
        return false;
    }

    @Override
    public void updateAssetStatus(String orderId,boolean flag) {
        StringBuilder sql = new StringBuilder("update mag_order set asset_state =");
        if(flag){
            sql.append("'1'");
        }else{
            sql.append("'2'");
        }
        sql.append(" where id = '");
        sql.append(orderId).append("' ");
        sunbmpDaoSupport.exeSql(sql.toString());
    }
}