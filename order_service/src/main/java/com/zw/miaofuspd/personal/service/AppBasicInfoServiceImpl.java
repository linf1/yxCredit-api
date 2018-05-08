package com.zw.miaofuspd.personal.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.DateUtils;
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
    public Map updateLinkManInfo(String orderId,String customer_id,Map map1) throws Exception {
        Map retMap = new HashMap();
        if(((CustomerLinkmanBean)map1.get("customerLinkmanBean")).getId().equals("")){
            String sql = "DELETE FROM mag_customer_linkman WHERE customer_id = '"+customer_id+"' and type='1'";
            sunbmpDaoSupport.exeSql(sql);
        }
        insertOrUpdate((CustomerLinkmanBean)map1.get("customerLinkmanBean"),customer_id,orderId);
        insertOrUpdate((CustomerLinkmanBean)map1.get("customerLinkmanBean2"),customer_id,orderId);
        insertOrUpdate((CustomerLinkmanBean)map1.get("customerLinkmanBean3"),customer_id,orderId);
        insertOrUpdate((CustomerLinkmanBean)map1.get("customerLinkmanBean4"),customer_id,orderId);
        String magSql = "update mag_customer set link_man_complete = '1' where id = '"+customer_id+"'";
        sunbmpDaoSupport.exeSql(magSql);
        retMap.put("success",true);
        retMap.put("msg","保存成功！");
        return retMap;
    }
    //根据传来的值来新增或修改联系人
    public void insertOrUpdate(CustomerLinkmanBean customerLinkmanBean,String userId,String orderId){
        if(customerLinkmanBean.getRelationShip()!=null){
            if(!("").equals(customerLinkmanBean.getId())){
                //根据Id获取原先的联系人信息
                String selectsql = "select id,relationship,relationship_name AS relationshipname,contact,link_name AS linkname " +
                        "from mag_customer_linkman where id = '"+customerLinkmanBean.getId()+"' and type='1'";
                Map map=sunbmpDaoSupport.findForMap(selectsql);
                String sql = "update mag_customer_linkman set relationship = '"+customerLinkmanBean.getRelationShip()+"',relationship_name = '"+customerLinkmanBean.getRelationshipName()+"',contact = '"+customerLinkmanBean.getContact()+"',link_name='"+customerLinkmanBean.getLinkName()+"' where type='1' and ID = '"+customerLinkmanBean.getId()+"'";
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
            }else {
                if (!("").equals(customerLinkmanBean.getRelationShip()) && !("").equals(customerLinkmanBean.getContact())) {
                    String sql = "insert into mag_customer_linkman(ID,type,relationship,contact,CREAT_TIME,CUSTOMER_ID,relationship_name,link_name,main_sign) " +
                            "values('" + UUID.randomUUID() + "','1','" + customerLinkmanBean.getRelationShip() + "','" + customerLinkmanBean.getContact() + "','" + DateUtils.getDateString(new Date()) + "','" + userId + "','" + customerLinkmanBean.getRelationshipName() + "','" + customerLinkmanBean.getLinkName() + "','" + customerLinkmanBean.getMainSign() + "') ";
                    sunbmpDaoSupport.exeSql(sql);
                }
            }
        }
    }


    @Override
    public Map addBasicInfo(Map<String, String> paramMap) throws Exception {
        Map resturnMap = new HashMap();
        String  liveInfoId = saveLiveInfo(paramMap);//保存客户基本信息
        if (liveInfoId == null) {
            resturnMap.put("msg","保存客户居住信息失败");
            resturnMap.put("flag",false);
            return resturnMap;
        }
        String jobInfoId = saveJobInfo(paramMap);//保存客户职业信息
        if (jobInfoId == null) {
            resturnMap.put("msg","保存客户职业信息失败");
            resturnMap.put("flag",false);
            return resturnMap;
        }
        String magSql = "update mag_customer set basic_complete_spd = '1' where id = '"+paramMap.get("customerId")+"'";
        sunbmpDaoSupport.exeSql(magSql);
        resturnMap.put("msg","保存客户信息成功");
        resturnMap.put("flag",true);
        return resturnMap;
    }
    /**
     * 办单员端-获取用户基本信息
     * @param orderId 订单id
     * @return
     * @throws Exception
     */
    @Override
    public Map getBasicInfo(String orderId) throws Exception {
        Map resutMap = new HashMap();
        //根据订单id获取客户id
        String customerSql = "select customer_id from mag_order where id = '"+orderId+"' and order_type='2'";
        List list = sunbmpDaoSupport.findForList(customerSql);
        String customer_id = "";
        if(list!=null && list.size()>0){
            Map map = (Map) list.get(0);
            customer_id = map.get("customer_id").toString();
        }
        //查询客户的居住信息
        String liveSql = "select provinces,city,distric,provinces_id as provincesId,city_id as cityId,distric_id as districId ,address_detail as addressDetail,complete from mag_customer_live where customer_id='"+customer_id+"' and type ='1'";
        Map liveMap = sunbmpDaoSupport.findForMap(liveSql);
        resutMap.put("liveMap",liveMap);
        //查询客户的职业信息
        String jobSql = "select id,jobType_id as jobTypeId,address as detailsAddress,company_address as companyAddress,company_name as companyName,company_phone as companyPhone,province_name as provinceName,city_name as cityName,district_name as districtName," +
                "province_id as provinceId,city_id as cityId,district_id as districtId,pos_level as posLevel,type,company_code as companyCode from mag_customer_job where customer_id='"+customer_id+"' and type='1'";
        Map jobMap = sunbmpDaoSupport.findForMap(jobSql);
        resutMap.put("jobMap",jobMap);
        return resutMap;
    }
    /**
     * 业务端-获取用户基本信息
     * @param customerId 客户id
     * @return
     * @throws Exception
     */
    @Override
    public Map getBasicCustomerInfo(String customerId) throws Exception {
        Map resutMap = new HashMap();
        //查询客户的居住信息
        String liveSql = "select provinces,city,distric,provinces_id as provincesId,city_id as cityId,distric_id as districId,address_detail as addressDetail,complete from mag_customer_live where customer_id='"+customerId+"' and type ='1'";
        Map liveMap = sunbmpDaoSupport.findForMap(liveSql);
        resutMap.put("liveMap",liveMap);
        //查询客户的职业信息
        String jobSql = "select id,jobType_id,customer_id as customerId,company_address as companyAddress,company_name as companyName,company_phone as companyPhone,address,province_name as provinceName,city_name as cityName,district_name as districtName," +
                "province_id as provinceId,city_id as cityId,district_id as districtId,pos_level as posLevel,complete,type,company_code as companyCode from mag_customer_job where customer_id='"+customerId+"' and type='1'";
        Map jobMap = sunbmpDaoSupport.findForMap(jobSql);
        resutMap.put("jobMap",jobMap);
        return resutMap;
    }

    //保存客户职业信息
    public String saveJobInfo (Map<String,String> paramMap){
        String customerId = paramMap.get("customerId");
        String alterTime = DateUtils.getDateString(new Date());
        //客户职业信息
        String companyName = paramMap.get("companyName");//公司名称
        String linkNumber = paramMap.get("linkNumber");//联系电话
//        String companyCode = paramMap.get("companyCode");//区号
        String detailAddress = paramMap.get("detailAddress");//详细地址
        String address = paramMap.get("companyAddress");//省市区
        String address_code = paramMap.get("companyAddressId");//省市区code
        String pos_level = paramMap.get("posLevel");//z职位
        String jobType = "";//工作类型name
        String jobTypeId = paramMap.get("jobTypeId");//工作类型Id
        if("0".equals(jobTypeId)){
            jobType = "工薪者";
        }else{
            jobType = "自营者";
        }
        String pname = "";//省名称
        String cname = "";//市名称
        String dname = "";//区名称
        if (address != null && !"".equals(address)) {
            String []str = address.split("/");
            switch (str.length){
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
        if (address_code != null && !"".equals(address_code)) {
            String []str = address_code.split("/");
            switch (str.length){
                case 1:
                    pid = address_code.split("/")[0];
                    break;
                case 2:
                    pid = address_code.split("/")[0];
                    cid = address_code.split("/")[1];
                    break;
                case 3:
                    pid = address_code.split("/")[0];
                    cid = address_code.split("/")[1];
                    did = address_code.split("/")[2];
                    break;
            }
        }
        String company_address = pname + cname +dname +detailAddress;
        String id = null;
        String complete = "1";
        try {
            //检查是否已经存在客户资料
            StringBuffer sql = new StringBuffer("select id,company_name,company_phone,province_name,address,pos_level,complete from mag_customer_job where customer_id='" + customerId + "' and type='1'");
            List<Map> list = sunbmpDaoSupport.findForList(sql.toString());
            if (list.size() > 0) {
                //更新证件资料
                Map map = (Map) list.get(0);
                id = map.get("id") + "";
                sql = new StringBuffer("update mag_customer_job set ");
                sql.append("company_name='" + companyName);
                sql.append("',company_phone='" + linkNumber);
                sql.append("',address='" + detailAddress);
                sql.append("',province_name='" + pname);
                sql.append("',city_name='" + cname);
                sql.append("',district_name='" + dname);
                sql.append("',province_id='" + pid);
                sql.append("',city_id='" + cid);
                sql.append("',district_id='" + did);
                sql.append("',company_address='" + company_address);
                sql.append("',pos_level='" + pos_level);
                sql.append("',alter_time='" + alterTime);
                sql.append("',complete='" + complete);
                sql.append("',jobType='" + jobType);
                sql.append("',jobType_id='" + jobTypeId);
                sql.append("' where  id='" + id + "'");
                sunbmpDaoSupport.exeSql(sql.toString());
            } else {
                id = UUID.randomUUID() + "";
                sql = new StringBuffer("insert into mag_customer_job (id,customer_id,jobType,jobType_id,company_address,company_name,company_phone,address,province_name,city_name,district_name,province_id,city_id,district_id,pos_level,complete,type,alter_time) VALUES('");
                sql.append(id + "','" + customerId + "','" + jobType+ "','" + jobTypeId+ "','" + company_address+ "','" + companyName+ "','" + linkNumber + "','" +detailAddress+ "','" +
                        pname + "','" + cname + "','" + dname + "','" + pid + "','" + cid + "','" + did + "','"+pos_level+"','"+complete+"','1','" + alterTime +
                        "')");
                sunbmpDaoSupport.exeSql(sql.toString());
            }
        } catch (Exception e) {
            TraceLoggerUtil.error("保存客户职业表出错！", e);
        }
        return id;
    }

    /**
     * 获取该用户下所有的联系人
     * @param customerId
     * @return
     */
    @Override
    public Map getLinkMan(String customerId) throws Exception {
        Map retMap = new HashMap();
        //查询linkman表中的直系亲属前两个人
        String sql = "select id,relationship,relationship_name AS relationshipname,contact,link_name AS linkName from mag_customer_linkman where type='1' and customer_id = '"+customerId+"' and main_sign =0 order by CREAT_TIME LIMIT 0,2";
        List list=sunbmpDaoSupport.findForList(sql);
        retMap.put("linkmanlist",list);
        //查询linkman表中的其他亲属前两个人
        String sql2 = "select id,relationship,relationship_name AS relationshipname,contact,link_name AS linkName from mag_customer_linkman where type='1' and customer_id = '"+customerId+"' and main_sign =1 order by CREAT_TIME LIMIT 0,2";
        List list2=sunbmpDaoSupport.findForList(sql2);
        retMap.put("olinkmanlist",list2);
        return retMap;
    }

    @Override
    public void saveTongXunLu(String customerId,String data) throws Exception {
        data = data.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "");
        String updateSql = "update mag_customer set phoneBookList='"+data+"' where id ='"+customerId+"'";
        sunbmpDaoSupport.exeSql(updateSql);
    }

    /**
     * 秒付金服获取个人信息
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
        String basicSql = "SELECT marry,educational,educational_name,marry_name from mag_customer_person where customer_id='"+customerId+"'";
        Map basicMap = sunbmpDaoSupport.findForMap(basicSql);
        //获取职业信息
        String OccupationSql ="SELECT company_name,company_phone,address,province_name,city_name,district_name,province_id,city_id,district_id from mag_customer_job where customer_id='"+customerId+"'";
        Map OccupationMap = sunbmpDaoSupport.findForMap(OccupationSql);
        //获取联系人信息
        String linkManSql = "SELECT id,link_name,contact,relationship,relationship_name,main_sign from mag_customer_linkman where CUSTOMER_ID='"+customerId+"'";
        List olist = sunbmpDaoSupport.findForList(linkManSql);
        resutMap.put("linkManList",linkManList);
        resutMap.put("olist",olist);
        resutMap.put("basicMap",basicMap);
        resutMap.put("OccupationMap",OccupationMap);
        resutMap.put("otherLinkManList",otherLinkManList);
        resutMap.put("flag",true);
        return resutMap;
    }
    //保存客户居住信息
    public String saveLiveInfo(Map<String,String> paramMap){
        String customerId = paramMap.get("customerId");
        String alterTime = DateUtils.getDateString(new Date());
        //客户职业信息
        String address = paramMap.get("liveAddress");//居住地址
        String address_code = paramMap.get("liveAddressId");//省市区code
        String pname = "";//省名称
        String cname = "";//市名称
        String dname = "";//区名称
        if (address != null && !"".equals(address)) {
            String []str = address.split("/");
            switch (str.length){
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
        if (address_code != null && !"".equals(address_code)) {
            String []str = address_code.split("/");
            switch (str.length){
                case 1:
                    pid = address_code.split("/")[0];
                    break;
                case 2:
                    pid = address_code.split("/")[0];
                    cid = address_code.split("/")[1];
                    break;
                case 3:
                    pid = address_code.split("/")[0];
                    cid = address_code.split("/")[1];
                    did = address_code.split("/")[2];
                    break;
            }
        }
        String addressDetail = paramMap.get("addressDetail");//居住详细地址
        String id="";
        String complete = "1";
        try {
            //检查是否已经存在客户资料
            StringBuffer sql = new StringBuffer("select ID,nowaddress,address_detail,complete from mag_customer_live where customer_id='" + customerId + "' and type ='1'");
            List<Map> list = sunbmpDaoSupport.findForList(sql.toString());
            if (list.size() > 0) {
                //更新证件资料
                Map map = (Map) list.get(0);
                id = map.get("ID") + "";
                sql = new StringBuffer("update mag_customer_live set ");
                sql.append("provinces='" + pname);
                sql.append("',city='" + cname);
                sql.append("',distric='" + dname);
                sql.append("',provinces_id='" + pid);
                sql.append("',city_id='" + cid);
                sql.append("',distric_id='" + did);
                sql.append("',address_detail='" + addressDetail);
                sql.append("',ALTER_TIME='" + alterTime);
                sql.append("',complete='" + complete);
                sql.append("' where  id='" + id + "'");
                sunbmpDaoSupport.exeSql(sql.toString());
            } else {
                id = UUID.randomUUID() + "";
                sql = new StringBuffer("insert into mag_customer_live (ID,type,customer_id,provinces,city,distric,provinces_id,city_id,distric_id,address_detail,complete,CREAT_TIME,ALTER_TIME) VALUES('");
                sql.append(id + "','1','" + customerId + "','" + pname+ "','" + cname+ "','" + dname+ "','" + pid+ "','" + cid+ "','" + did+ "','" + addressDetail + "','" + complete + "','" + alterTime + "','" + alterTime +
                        "')");
                sunbmpDaoSupport.exeSql(sql.toString());
            }
        } catch (Exception e) {
            TraceLoggerUtil.error("保存客户信息表出错！", e);
        }
        return id;
    }

    //保存客户联系人信息
    public Map saveLinkManInfo(Map<String,String> paramMap){
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
        map.put("customerLinkmanBean",customerLinkmanBean);
        map.put("customerLinkmanBean",customerLinkmanBean1);
        map.put("customerLinkmanBean2",customerLinkmanBean2);
        map.put("customerLinkmanBean",customerLinkmanBean3);
        String relId = insertOrUpdate((CustomerLinkmanBean)map.get("customerLinkmanBean"),customerId);
        String relId1 = insertOrUpdate((CustomerLinkmanBean)map.get("customerLinkmanBean"),customerId);
        String otherId = insertOrUpdate((CustomerLinkmanBean)map.get("customerLinkmanBean2"),customerId);
        String otherId1 = insertOrUpdate((CustomerLinkmanBean)map.get("customerLinkmanBean2"),customerId);
        outMap.put("relId",relId);
        outMap.put("relId1",relId1);
        outMap.put("otherId",otherId);
        outMap.put("otherId1",otherId1);
        return outMap;
    }
    public String insertOrUpdate(CustomerLinkmanBean customerLinkmanBean, String customerId){
        String complete="100";
        String id = null;
        if(customerLinkmanBean.getRelationShip()!=null){
            if(!("").equals(customerLinkmanBean.getId())){
                //根据Id获取原先的联系人信息
                String selectsql = "select id,relationship,relationship_name,contact,link_name from mag_customer_linkman " +
                        "where CUSTOMER_ID = '"+customerId+"' and main_sign = '"+customerLinkmanBean.getMainSign()+"' and type = '1'";
                Map map=sunbmpDaoSupport.findForMap(selectsql);
                id = map.get("id").toString();
                String sql = "update mag_customer_linkman set complete = '"+complete+"',relationship = '"+customerLinkmanBean.getRelationShip()+"',relationship_name = '"+customerLinkmanBean.getRelationshipName()+"',contact = '"+customerLinkmanBean.getContact()+"',link_name='"+customerLinkmanBean.getLinkName()+"' where CUSTOMER_ID = '"+customerId+"' and main_sign = '"+customerLinkmanBean.getMainSign()+"' and type='1'";
                sunbmpDaoSupport.exeSql(sql);
            }else {
                id = UUID.randomUUID() + "";
                if (!("").equals(customerLinkmanBean.getRelationShip()) && !("").equals(customerLinkmanBean.getContact())) {
                    String sql = "insert into mag_customer_linkman(ID,relationship,contact,CREAT_TIME,complete,CUSTOMER_ID,relationship_name,link_name,main_sign,type) " +
                            "values('" + id + "','" + customerLinkmanBean.getRelationShip() + "','" + customerLinkmanBean.getContact() + "','"+complete+"','" + DateUtils.getDateString(new Date()) + "','" + customerId + "','" + customerLinkmanBean.getRelationshipName() + "','" + customerLinkmanBean.getLinkName() + "','" + customerLinkmanBean.getMainSign() + "','1') ";
                    sunbmpDaoSupport.exeSql(sql);
                }
            }
        }
        return id;
    }
    //获取同盾设备信息接口
    public JSONObject deviceRule(Map map) {
        String result;
        JSONObject jsonResult;
        try{
            Map<String , Object> param = new HashMap<String , Object>();
            param.put("name",map.get("realname"));
            param.put("phone", map.get("tel"));
            param.put("idNo", map.get("card"));
            param.put("bankNo", "");
            param.put("accountEmail", "");
            param.put("accountPhone", "");
            param.put("qqNumber", "");
            param.put("contactAddress", "");
            param.put("contact1Name","");
            param.put("contact1Mobile", "");
            param.put("type", map.get("type"));
            if("WEB".equals(map.get("type"))){
                param.put("tokenId", map.get("blackBox"));
                param.put("blackBox", "");
            }else{
                param.put("tokenId", "");
                param.put("blackBox", map.get("blackBox"));
            }
            String host = map.get("host").toString();
            String url = host+"/szt/tongdun/rule";

            result  = HttpUtil.doPost(url, param);
            jsonResult = JSON.parseObject(result);
            JSONObject data = (JSONObject) jsonResult.get("data");
            System.out.println("================================================="+data);
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
            String is_root =  jsonResult3.getString("root");//是否root
            String id = UUID.randomUUID().toString();
            String deviceSql = "select id from customer_device_info where order_id ='"+map.get("orderId")+"'";
            List list = sunbmpDaoSupport.findForList(deviceSql);
            if(list!=null && list.size()>0){
                String deleteSql = "delete from customer_device_info where order_id='"+map.get("orderId")+"'";
                sunbmpDaoSupport.exeSql(deleteSql);
            }
            String sql = "insert into customer_device_info (id,order_id,apply_province,apply_city,apply_area,apply_address,imei_number," +
                    "operate_system,device_type,tel_memory_run,tel_memory,tel_model,tel_brand,network_type,wifi_name,wifi_ssid,ip_address," +
                    "ip_province,ip_city,ip_area,is_root,is_prison,is_moni_online,location_permission,create_time,alter_time,latitude,longitude,black_box,type,user_id) values" +
                    "('" + id + "','" +map.get("orderId")+ "','','','','"+apply_address+"','','"
                    + operate_system + "','" + device_type + "','" +tel_memory_run + "','"+tel_memory+"','" + tel_model + "','"
                    + tel_brand + "','" + network_type + "','" + wifi_name + "','" + wifi_ssid + "','"
                    + ip_address + "','','','','" + is_root + "','','','','"+DateUtils.getDateString(new Date())+"','"+DateUtils.getDateString(new Date())+"','','','" + map.get("blackBox") + "','" + map.get("type") + "','"+ map.get("userId") + "')";
            sunbmpDaoSupport.exeSql(sql);
            System.out.println("================================================="+result);
            return jsonResult;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void zx(String name,String idNo,String companyName,String host){
        Map<String , Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("idNo", idNo);
        map.put("companyName",companyName);
        map.put("busType", "2");
        String url = host+"/szt/zhengXinLoanInfo/";
        try{
            String result  = HttpUtil.doPost(url, map);
            System.out.println("=================================================");
            System.out.println(result);
            System.out.println("=================================================");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}