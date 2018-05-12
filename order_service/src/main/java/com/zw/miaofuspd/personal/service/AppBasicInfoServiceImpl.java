package com.zw.miaofuspd.personal.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.AbstractDateDeserializer;
import com.base.util.DateUtils;
import com.base.util.GeneratePrimaryKeyUtils;
import com.base.util.TraceLoggerUtil;
import com.zw.api.HttpUtil;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.CustomerLinkmanBean;
import com.zw.miaofuspd.facade.entity.CustomerLogBean;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.facade.order.service.IAppInsapplicationService;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        String magSql = "update mag_customer set link_man_complete = '1' where id = '" + customer_id + "'";
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

                id = GeneratePrimaryKeyUtils.getUUIDKey();;
                if (!("").equals(customerLinkmanBean.getRelationShip()) && !("").equals(customerLinkmanBean.getContact())) {
                    String sql = "insert into mag_customer_linkman(ID,relationship,contact,CREAT_TIME,complete,CUSTOMER_ID,relationship_name,link_name) " +
                            "values('" + id + "','" + customerLinkmanBean.getRelationShip() + "','" + customerLinkmanBean.getContact() + "','" + DateUtils.getDateString(new Date()) + "','" + complete + "','" + customerId + "','" + customerLinkmanBean.getRelationshipName() + "','" + customerLinkmanBean.getLinkName() + "') ";
                    sunbmpDaoSupport.exeSql(sql);
                }
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
    public Map addApplyInfo(Map<String, String> paramMap) {
        Map resturnMap = new HashMap();

        String orderId = paramMap.get("orderId");
        //申请金额
        String applayMoney = paramMap.get("applayMoney");
        //申请期限
        String periods = paramMap.get("periods");
        //合同金额
        String ContractAmount = paramMap.get("ContractAmount");
        //剩余合同金额
        String surplusContractAmount = paramMap.get("surplusContractAmount");
        //借款用途
        String loanPurpose = paramMap.get("loanPurpose");

        String sql = "update mag_order set applay_money = '" + applayMoney + "'," + "PERIODS = '" + periods + "',contract_amount = '" +
                "" + ContractAmount + "'," + "surplus_contract_amount = '" + surplusContractAmount + "',loan_purpose = '" + loanPurpose + "'," +
                "complete = '100' where order_id = '" + orderId + "'  ";
        int count = sunbmpDaoSupport.getCount(sql);
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
     * 办单员端-获取用户基本信息
     *
     * @param orderId 订单id
     * @return
     */
    @Override
    public Map getApplyInfo(String orderId) {
        //根据订单id获取客户基本信息
        String customerSql = "select applay_money,PERIODS,contract_amount,surplus_contract_amount," +
                "loan_purpose from mag_order where order_no = '" + orderId + "'";
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
    public void getBasicCustomerInfo(Map<String, String> map) {
        String id = map.get("id");
        String tel = map.get("tel");
        String card = map.get("card");
        String personName = map.get("personName");
        String productName = map.get("productName");
        //判断是新增还是更新
        String sql = "select ID from mag_customer where USER_ID = '" + id + "'";
        List<Map> list = sunbmpDaoSupport.findForList(sql);
        if (list.size() > 0) {
            //更新三要素
            String sql2 = "update mag_customer set TEL = '" + tel + "',PERSON_NAME = '" + personName + "',CARD = '" + card + "' where USER_ID = '" + id + "'";
            sunbmpDaoSupport.exeSql(sql2);
            //更新订单表中的三要素信息
            String sql6 = "update mag_order set TEL = '" + tel + "',CUSTOMER_NAME = '" + personName + "',CARD = '" + card + "' where USER_ID = '" + id + "'";
            sunbmpDaoSupport.exeSql(sql6);
        } else {
            //新增客户信息
            String uuidKey = GeneratePrimaryKeyUtils.getUUIDKey();
            String sql3 = "insert into mag_customer (ID,USER_ID,PERSON_NAME,TEL,CARD) values ('" + uuidKey + "','" + id + "','" + personName + "','" + tel + "','" + card + "')";
            sunbmpDaoSupport.exeSql(sql3);
            String orderid = String.valueOf(GeneratePrimaryKeyUtils.getOrderNum());
            //新增订单信息
            String sql4 = "insert into mag_order (ID,order_no,CUSTOMER_ID,state,product_name_name,CUSTOMER_NAME,TEL,CARD) values ('"+GeneratePrimaryKeyUtils.getUUIDKey()+"'," +
                    "'"+orderid+"','"+uuidKey+"','0','"+productName+"','"+personName+"','"+tel+"','"+card+"')";
            sunbmpDaoSupport.exeSql(sql4);

        }
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
    public Map getBasicInfo(String customerId){
        String  sql="select t1.marital_status as marital_status,t1.children_status as children_status,t1.Hometown_house_property as Hometown_house_property" +
                ",t2.province_name as jobProvince_name,t2.city_name as jobCity_name,t2.district_name as jobDistrict_name,t2.address as jobDetailAddress," +
                "t3.provinces as cardProvinces,t3.city as cardCity,t3.distric as cardDistric,t3.address_detail as cardDetailAddress from mag_customer t1" +
                " left join mag_customer_job t2 on t1.id = t2.customer_id" +
                " left join mag_customer_live t3 on t1.id = t3.customer_id where t1.id = '"+customerId+"'";
        Map map = sunbmpDaoSupport.findForMap(sql);
        return map;

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
        //老家住房性质
        String hometownHouseProperty = paramMap.get("hometownHouseProperty");
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
        String  cusSql = "update mag_customer set marital_status ='"+maritalSstatus+"',children_status='"+childrenStatus+"',Hometown_house_property='"+hometownHouseProperty+"'";
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
        //查询linkman表中的直系亲属前两个人
        String sql = "select id,relationship,relationship_name AS relationshipname,contact,link_name AS linkName from mag_customer_linkman where type='1' and customer_id = '" + customerId + "' and main_sign =0 order by CREAT_TIME LIMIT 0,2";
        List list = sunbmpDaoSupport.findForList(sql);
        retMap.put("linkmanlist", list);
        //查询linkman表中的其他亲属前两个人
        String sql2 = "select id,relationship,relationship_name AS relationshipname,contact,link_name AS linkName from mag_customer_linkman where type='1' and customer_id = '" + customerId + "' and main_sign =1 order by CREAT_TIME LIMIT 0,2";
        List list2 = sunbmpDaoSupport.findForList(sql2);
        retMap.put("olinkmanlist", list2);
        return retMap;
    }

    /**
     * 在申请前判断是否实名认证
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
        if (list.size() > 0 && list != null) {
            return (Map) list.get(0);
        }
        return null;
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

        //根据登录用户id获取客户信息表id
        String sql1 = "select id from mag_customer where USER_ID = '" + id + "'";
        Map map = sunbmpDaoSupport.findForMap(sql1);
        //获取申请主页面的基本信息及资料的完成状态
        String sql2 = "select t1.card as card,t1.tel as tel,t1.PERSON_NAME as personName,t1.Baseinfo_complete as baseinfoComplete" +
                ",t2.complete as applyComplete,t2.order_no as orderId,t3.complete as linkComplete  from mag_customer t1 left join  mag_order t2 on t1.id = t2.CUSTOMER_ID left join " +
                "mag_customer_linkman t3 on t1.id = t3.CUSTOMER_ID where t1.id = '" + map.get("id") + "'and t2.product_name_name= '"+productName+"'and t2.state='1'";
        resMap = sunbmpDaoSupport.findForMap(sql2);
        return resMap;
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
        String sql="select id,province_name from zw_sys_province";
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
        String sql="select id,city_name from zw_sys_city where province_id='"+privinceId+"'";
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
        String sql="select id,city_name from zw_sys_district where city_id='"+cityId+"'";
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
    public void oneClickApply(String orderId) {

        //查询订单相关的列表信息
        String sql = "select t2.customer_id as customer_id,t2.CUSTOMER_NAME as CUSTOMER_NAME,t2.product_name_name as product_name_name,t1.person_name as person_name,t1.card as card,t1.tel as tel,t1.marital_status as marital_status,t1.children_status as children_status," +
                "  t1.hometown_house_property as hometown_house_property,t1.residence_address as residence_address,t1.card_register_address as card_register_address," +
                "  t2.product_name as product_name,t2.applay_money as applay_money,t2.periods as periods,t2.contract_amount as contract_amount," +
                "  t2.loan_purpose as loan_purpose from mag_customer t1  left join mag_order t2 on t1.id = t2.CUSTOMER_ID" +
                "  where t2.order_no='"+orderId+"'";
        Map forMap = sunbmpDaoSupport.findForMap(sql);
        //保存订单信息到订单进件信息表
        String id = GeneratePrimaryKeyUtils.getUUIDKey();
        String sql2 = "insert into mag_order_entry values ('"+id+"','"+orderId+"','"+forMap.get("customer_id")+"','"+forMap.get("person_name")+"'" +
                ",'"+forMap.get("card")+"','"+forMap.get("tel")+"','"+forMap.get("marital_status")+"','"+forMap.get("children_status")+"','"+forMap.get("hometown_house_property")+"'" +
                ",'"+forMap.get("residence_address")+"','"+forMap.get("card_register_address")+"','"+forMap.get("product_name")+"','"+forMap.get("applay_money")+"'" +
                ",'"+forMap.get("periods")+"','"+forMap.get("contract_amount")+"','"+forMap.get("loan_purpose")+"','0','"+DateUtils.getDateString(new Date())+"')";
        sunbmpDaoSupport.exeSql(sql2);
        //修改订单状态为已提交
        String sql3 = "update mag_order set state = '2' where order_no = '"+orderId+"'";
        sunbmpDaoSupport.exeSql(sql3);
        //新增操作表订单信息
        String sql4 = "insert into order_operation_record (id,status,order_id,emp_id,emp_name) values ('"+GeneratePrimaryKeyUtils.getUUIDKey()+"'," +
                "'1','"+orderId+"','"+id+"','"+forMap.get("CUSTOMER_NAME")+"')";
        sunbmpDaoSupport.exeSql(sql4);

    }
}