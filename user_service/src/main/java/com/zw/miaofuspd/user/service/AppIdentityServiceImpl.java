package com.zw.miaofuspd.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.DateUtils;
import com.base.util.HttpClientUtil;
import com.base.util.StringUtils;
import com.base.util.TraceLoggerUtil;
import com.zw.api.HttpUtil;
import com.zw.api.mobilepwd.MD5;
import com.zw.api.mobilepwd.MobilePwdApi;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.entity.BranchInfo;
import com.zw.miaofuspd.facade.entity.Contact;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.facade.order.service.IAppInsapplicationService;
import com.zw.miaofuspd.facade.user.service.IAppIdentityService;
import com.zw.miaofuspd.util.TaoBaoConstant;
import com.zw.service.base.AbsServiceBase;
import org.apache.http.client.config.RequestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AppIdentityServiceImpl extends AbsServiceBase implements IAppIdentityService {
    //转换日期格式
    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
    private static SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    @Autowired
    private ISystemDictService iSystemDictService;
    @Autowired
    private IDictService iDictService;
    @Autowired
    private IAppInsapplicationService iAppInsapplicationService;
    @Autowired
    private AppOrderService appOrderService;
    /**
     *现金贷三码验证
     * @throws Exception
     */
    public Map sanMaIdentity(String host,Map map) throws Exception {
        //调用规则引擎验证接口
        String customerId = map.get("customer_id").toString();
        String orderId = map.get("orderId").toString();
        Map orderMap = appOrderService.getOrderById(orderId);
        String empId=orderMap.get("empId").toString();
        Map saleMap=appOrderService.getSaleInfo(empId);
        String branchId=saleMap.get("branch").toString();
        BranchInfo branchInfo=appOrderService.getGongSiName(appOrderService.getBranchInfo(branchId));
        Map resultMap = new HashMap();
        String threeCodeError = iDictService.getDictInfo("三码错误次数","0");
        if(StringUtils.isEmpty(threeCodeError)){
            threeCodeError = "5";
        }
        int errorNum = Integer.valueOf(threeCodeError);//总共多少次
        int threeCodeNum = 0;
        //走数据库查出来
        String threeCodeSql = "SELECT three_error_spdNum FROM mag_customer where id = '"+customerId+"'";
        Map threeCodeMap = sunbmpDaoSupport.findForMap(threeCodeSql);
        String three_code_num = threeCodeMap.get("three_error_spdNum")+"";
        if(!"".equals(three_code_num)){
            threeCodeNum = Integer.valueOf(threeCodeMap.get("three_error_spdNum").toString());
        }
        Map<String , Object> param = new HashMap<String , Object>();
        param.put("idNo",map.get("card").toString());
        param.put("phone",map.get("tel").toString());
        param.put("name",map.get("realname").toString());
        param.put("companyName",branchInfo.getDeptName());
        param.put("companyId",branchInfo.getId());
        param.put("busType","2");
        param.put("pid", UUID.randomUUID().toString());
        JSONObject json = trinityTecternCertification(host,param);
        if(json!=null){
            String  responseCode= json.getString("responseCode");
            if(responseCode!=null){
                if("0".equals(responseCode)){//代表操作成功
                    String  authentication= json.getString("authentication");
                    if("1".equals(authentication)){//代表实名认证失败
                        threeCodeNum = threeCodeNum +1;
                        if(threeCodeNum>=errorNum){
                            resultMap.put("msg","您的信息填写超过"+errorNum+"次不匹配,您当天已被拒绝进件");
                            resultMap.put("threeCode","refuse");
                            String msssageSql = "UPDATE app_message SET update_state = '"+0+"' where order_id = '"+orderId+"'";//为了在我的消息那块操作
                            sunbmpDaoSupport.exeSql(msssageSql);
                        }else{
                            int num = errorNum-threeCodeNum;//剩余多少次机会
                            resultMap.put("msg","您的信息不匹配,您还有"+num+"次机会");
                        }
                        //更新客户表的二码错误次数
                        String sql = "UPDATE mag_customer SET three_error_spdNum = '"+threeCodeNum+"' where id = '"+customerId+"'";
                        sunbmpDaoSupport.exeSql(sql);
                        resultMap.put("flag",false);
                    }else if("0".equals(authentication)){//代表实名认证成功
                        //成功以后将数据库的次数改为0
                        List list = new ArrayList();
                        String sql = "UPDATE mag_customer SET three_error_spdNum = '"+0+"' where id = '"+customerId+"'";
                        String msssageSql = "UPDATE app_message SET update_state = '"+1+"' where order_id = '"+orderId+"'";//为了在我的消息那块操作
                        list.add(sql);
                        list.add(msssageSql);
                        sunbmpDaoSupport.exeSql(list);
                        resultMap.put("flag",true);
                    }
                }else{
                    resultMap.put("msg", "系统内部错误,请联系客服！");
                    resultMap.put("flag", false);
                }
            }else {
            resultMap.put("msg", "系统内部错误,请联系客服！");
            resultMap.put("flag", false);
            }
        }else{
            resultMap.put("msg","系统内部错误,请联系客服！");
            resultMap.put("flag",false);
        }
        return resultMap;
    }
    /**
     * 调用优分三码接口
     *
     */
    private JSONObject trinityAndNameCertification(String host, Map map) throws Exception {
        Map sanMaMap = new HashMap();
        sanMaMap.put("feature", "name+idcard+cellphone");
        sanMaMap.put("name",map.get("realname").toString());
        sanMaMap.put("cellphone",map.get("tel").toString());
        sanMaMap.put("idcard",map.get("card").toString());
        sanMaMap.put("account", "miaofujftest");
        try {
            String url = host + "/szt/youfen/validation3";
            TraceLoggerUtil.info("三码认证接口发送参数--->" + sanMaMap);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(60000).build();
            HttpClientUtil.setRequestConfig(requestConfig);
            String data = HttpClientUtil.getInstance().sendHttpPost(url, sanMaMap);
            TraceLoggerUtil.info("三码认证接口接收结果--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            throw ex;
        }
    }
    /**
     * 调用腾讯天御三码接口
     *
     */
    private JSONObject trinityTecternCertification(String host, Map<String , Object> param) throws Exception {
        try {
            String url = host + "/szt/tencent/antiFraud";
            TraceLoggerUtil.info("三码认证接口发送参数--->" + param);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(60000).build();
            HttpClientUtil.setRequestConfig(requestConfig);
            String data =  HttpUtil.doPost(url, param);
            TraceLoggerUtil.info("三码认证接口接收结果--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            throw ex;
        }
    }
    /**
     * 调用蜂巢淘宝授权接口
     *
     */
    private JSONObject taoBaoShouQuan(String url, Map<String , Object> param) throws Exception {
        try {
            TraceLoggerUtil.info("淘宝授权接口发送参数--->" + param);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(60000).build();
            HttpClientUtil.setRequestConfig(requestConfig);
            String data =  HttpUtil.doGet(url, param);
            TraceLoggerUtil.info("淘宝授权接口接收结果--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            throw ex;
        }
    }
    /**
     *秒付金服身份认证信息回显
     */
    public Map getIdentityInfoByCustomerId(String customerId) throws Exception {
        Map map = new HashMap();
        String sql = "SELECT tel as customerTel,PERSON_NAME as realname,sex_name as sexName,card as cardNo,nation,birth,card_register_address as cardRegissterAddress,card_effective_time as cardEffectiveTime,sign_address as signAddress,Fcard_src_base64 as FcardSrcBase64,Zcard_src_base64 as ZcardSrcBase64 from mag_customer WHERE id = '"+customerId+"'";
        List list = sunbmpDaoSupport.findForList(sql);
        if (list!=null&&list.size()>0){
            return (Map) list.get(0);
        }else {
            return map;
        }
    }

    /**
     *秒付金服身份认证信息保存
     */
    public Map saveIdentityInfo(Map map) throws Exception {
        Map resultMap = new HashMap();
        String Zcard_src_base64 = (String) map.get("Zcard_src_base64");//身份证正面图片src64码
        String Fcard_src_base64 = (String) map.get("Fcard_src_base64");//身份证反面图片src64码
        String customerSql = "UPDATE mag_customer\n" +
                "SET PERSON_NAME = '" + map.get("realname") + "',\n" +
                " sex_name = '" + map.get("sex_name") + "',\n" +
                " Zcard_src_base64 = '" + Zcard_src_base64 + "',\n" +
                " Fcard_src_base64 = '" + Fcard_src_base64 + "',\n" +
                " card = '" + map.get("card") + "',\n" +
                " birth = '" + map.get("birth") + "',\n" +
                " card_register_address = '" + map.get("card_register_address") + "',\n" +
                " is_identity_spd = '" + 1 + "',\n" +
                " ALTER_TIME = '" + DateUtils.getCurrentTime(DateUtils.STYLE_10) + "'" +
                "WHERE\n" +
                "\tid = '" + map.get("customer_id") + "'";
        sunbmpDaoSupport.exeSql(customerSql);
        String orderSql = "UPDATE mag_order\n" +
                "SET CUSTOMER_ID = '" + map.get("customer_id") + "',\n" +
                " CUSTOMER_NAME = '" + map.get("realname") + "',\n" +
                " sex_name = '" + map.get("sex_name") + "',\n" +
                " CARD = '" + map.get("card") + "',\n" +
                " ALTER_TIME = '" + DateUtils.getCurrentTime(DateUtils.STYLE_10) + "'" +
                "WHERE\n" +
                "\tid = '" + map.get("orderId") + "'";
        sunbmpDaoSupport.exeSql(orderSql);
        String source= map.get("SOURCE").toString();//区分是哪个app过来的实名认证
        String orderId = map.get("orderId").toString();
        Map orderMap = appOrderService.getOrderById(orderId);
       //根据业务员获取对应的部门，获取分公司以及总公司
     //   String empId=orderMap.get("empId").toString();
     //   Map saleMap=appOrderService.getSaleInfo(empId);
      //  String branchId=saleMap.get("branch").toString();
    //    BranchInfo branchInfo=appOrderService.getGongSiName(appOrderService.getBranchInfo(branchId));
      //  String overdue_state = appOrderService.getOverdueState(orderId);
        if("1".equals(source)){//如果是办单员端过来的,,那么就需要做天御规则验证
         //   if (!"1".equals(overdue_state)) {
                try {
                    Map ruleMap = new HashMap();
                    ruleMap.put("type", "0");
                    ruleMap.put("orderId", orderId);
                    JSONObject jsonResult = iAppInsapplicationService.CreditForRisk(ruleMap);
                    if("D00000".equals(jsonResult.getString("responseCode"))){
                        String  result = jsonResult.getString("result");
                        if ("拒绝".equals(result)) {
                            String sql = "UPDATE mag_order set identry_rule_state = '0' where id = '" +orderId+ "' and order_type='2'";
                            sunbmpDaoSupport.exeSql(sql);
                            String usersql = "update app_user set order_refused_time='"+ DateUtils.getDateString(new Date())+"' where id ='"+orderMap.get("userId")+"'";
                            sunbmpDaoSupport.exeSql(usersql);
                        }else if ("通过".equals(result)) {
                            String sql1 = "UPDATE mag_order set identry_rule_state = '1' where id = '" +orderId+ "' and order_type='2'";
                            sunbmpDaoSupport.exeSql(sql1);
                        }
                    }
                } catch (Exception e) {
                }
          //  }
        }
        return resultMap;
    }

    /**
     * 获取信用认证状态
     * @param customerId
     * @return
     * @throws Exception
     */
    @Override
    public Map getIdentityState(String customerId) throws Exception {
        //获取人脸和实名认证状态
        String customerSql = "select mc.taobao_state_spd as taoBaoState,mc.basic_complete_spd as basicCompleteSpd,mc.face_state_spd as faceState,mc.is_identity_spd as isIdentitySpd,mc.link_man_complete AS linkManComplete,authorization_complete_spd as authorizationComplete,phone_state_spd as phoneState from mag_customer mc where mc.id ='"+customerId+"'";
        Map map = sunbmpDaoSupport.findForMap(customerSql);
        return map;
    }
    /**
     * 保存人脸识别完成度
     * @param customerId
     * @param face_state_spd
     * @throws Exception
     */
    @Override
    public void saveFaceComplete(String customerId,String face_state_spd,String faceSrc) throws Exception {
        String sql = "update mag_customer set face_src = '"+faceSrc+"',face_state_spd = '"+face_state_spd+"' where id= '"+customerId+"'";
        sunbmpDaoSupport.exeSql(sql);
    }
    /**
     * 淘宝授权登录获取短信验证码接口
     * @param taobaoAccount 淘宝账号
     * @param password 淘宝账号密码
     * @param userInfo
     * @return
     * @throws Exception
     */
    @Override
    public Map taobaoGetSmsCode(String taobaoAccount,String password,AppUserInfo userInfo) throws Exception {
        Map returnMap = new HashMap();
        String host = iSystemDictService.getInfo("rule.url");
        host = host+"/szt/fcTaoBao/login";
        Map<String , Object> param = new HashMap<String , Object>();
        param.put("name",userInfo.getName());
        param.put("idNo",userInfo.getCard());
        param.put("username",taobaoAccount);
        param.put("password",password);
        param.put("pid", UUID.randomUUID().toString());
        param.put("busType","2");
//        param.put("companyName","秒付金服");
        JSONObject jsonObject = taoBaoShouQuan(host,param);
        String sid = "";
        String base64String = "";
        if(jsonObject!=null){
            String responseCode = jsonObject.getString("responseCode");
            if("0".equals(responseCode)){
                String result = (String) jsonObject.get("data");
                JSONObject data = JSON.parseObject(result);
                String code = data.getString("code");
                String type = data.getString("type");//验证码类型
                String msg = TaoBaoConstant.getTaoBaoMsg(code);
                if("0".equals(code)|| "7003".equals(code)){
                    base64String = data.getString("data");
                    sid = data.getString("sid");
                }
                returnMap.put("type",type);
                returnMap.put("base64String",base64String);
                returnMap.put("sid",sid);
                returnMap.put("msg",msg);
                returnMap.put("code",code);
                returnMap.put("flag",true);
            }else{
                returnMap.put("flag",false);
                returnMap.put("msg","请求失败!");
            }
        }else{
            returnMap.put("flag",false);
            returnMap.put("msg","请求失败!");
        }
        return returnMap;
    }

    /**
     * 淘宝授权验证短信验证码接口
     * @param smsCode 短信验证码
     * @param sid 本次任务抓取任务的编码
     * @param userInfo
     * @return
     * @throws Exception
     */
    @Override
    public Map taobaoCheckSmsCode(String smsCode, String sid, AppUserInfo userInfo) throws Exception {
        Map returnMap = new HashMap();
        String host = iSystemDictService.getInfo("rule.url");
        host = host+"/szt/fcTaoBao/subCode";
        Map<String , Object> param = new HashMap<String , Object>();
        param.put("checkcode",smsCode);
        param.put("sid",sid);
        param.put("pid", UUID.randomUUID().toString());
        param.put("busType","2");
//        param.put("companyName","合肥小指");
        JSONObject jsonObject = taoBaoShouQuan(host,param);
        if(jsonObject!=null){
            String responseCode = jsonObject.getString("responseCode");
            if("0".equals(responseCode)){
                String result = (String) jsonObject.get("data");
                JSONObject data = JSON.parseObject(result);
                String code = data.getString("code");
                String type = data.getString("type");//验证码类型
                String msg = TaoBaoConstant.getTaoBaoMsg(code);
                String base64String = "";
                if("0".equals(code)){
                    base64String = data.getString("data");//获取二维码
                }
                returnMap.put("type",type);
                returnMap.put("base64String",base64String);
                returnMap.put("sid",sid);
                returnMap.put("msg",msg);
                returnMap.put("code",code);
                returnMap.put("flag",true);
            }else{
                returnMap.put("flag",false);
                returnMap.put("msg","请求失败!");
            }
        }else{
            returnMap.put("flag",false);
            returnMap.put("msg","请求失败!");
        }
        return returnMap;
    }
    /**
     * 淘宝授权短信验证码超时重新获取接口
     * @param sid
     * @param userInfo
     * @return
     * @throws Exception
     */
    @Override
    public Map taobaoRefreshCode(String sid, AppUserInfo userInfo) throws Exception {
        Map returnMap = new HashMap();
        String host = iSystemDictService.getInfo("rule.url");
        host = host+"/szt/fcTaoBao/refreshCode";
        Map<String , Object> param = new HashMap<String , Object>();
        param.put("sid",sid);
        JSONObject jsonObject = taoBaoShouQuan(host,param);
        String base64String = "";
        if(jsonObject!=null){
            String responseCode = jsonObject.getString("responseCode");
            if("0".equals(responseCode)){
                String result = (String) jsonObject.get("data");
                JSONObject data = JSON.parseObject(result);
                String code = data.getString("code");
                String type = data.getString("type");//验证码类型
                String msg = TaoBaoConstant.getTaoBaoMsg(code);
                if("0".equals(code)){
                    if("0".equals(type)){//代表图形验证码
                        base64String = data.getString("data");
                    }
                    sid = data.getString("sid");
                }
                returnMap.put("type",type);
                returnMap.put("code",code);
                returnMap.put("base64String",base64String);
                returnMap.put("sid",sid);
                returnMap.put("msg",msg);
                returnMap.put("flag",true);
            }else{
                returnMap.put("flag",false);
                returnMap.put("msg","请求失败!");
            }
        }else{
            returnMap.put("flag",false);
            returnMap.put("msg","请求失败!");
        }
        return returnMap;
    }

    /**
     * 淘宝登录授权查询授权状态接口,拉取报告
     * @param taobaoAccount
     * @param sid
     * @param userInfo
     * @return
     * @throws Exception
     */
    @Override
    public Map taobaoQuery(String taobaoAccount, String sid,AppUserInfo userInfo) throws Exception {
        Map returnMap = new HashMap();
        String url = iSystemDictService.getInfo("rule.url");
        String host = url+"/szt/fcTaoBao/getReport";
        Map<String , Object> param = new HashMap<String , Object>();
        param.put("sid",sid);
        param.put("username",taobaoAccount);
        param.put("pid", UUID.randomUUID().toString());
        JSONObject jsonObject = taoBaoShouQuan(host,param);
        if(jsonObject!=null){
            String responseCode = jsonObject.getString("responseCode");
            if("0".equals(responseCode)){
                String result = (String) jsonObject.get("data");
                JSONObject data = JSON.parseObject(result);
                String code = data.getString("code");
                String searchKey = data.getString("searchKey");
                String repotcode="";
                if("0".equals(code)){
                    Map<String , Object> map = new HashMap<String , Object>();
                    map.put("searchKey",searchKey);
                    String host1 = url+"/szt/fcTaoBao/getResult";
                    while (true){
                        JSONObject json = taoBaoShouQuan(host1,map);
                        String repCode = json.getString("responseCode");
                        if("0".equals(repCode)){
                            String repotResult = (String) json.get("data");
                            JSONObject repotData = JSON.parseObject(repotResult);
                            repotcode = repotData.getString("code");
                            if("7002".equals(repotcode)){
                                continue;
                            }if("7004".equals(repotcode)){
                                String id = UUID.randomUUID().toString();
                                String insertSql = "insert into mag_authorization(id,task_id,customer_id,platform_account,result_json)values('"+id+"','"+sid+"','"+userInfo.getCustomer_id()+"','"+taobaoAccount+"','"+repotResult+"')";
                                sunbmpDaoSupport.exeSql(insertSql);
                                String updateSql ="update mag_customer set taobao_state_spd='1' where id='"+userInfo.getCustomer_id()+"'";
                                sunbmpDaoSupport.exeSql(updateSql);
                                break;
                            }else{
                                break;
                            }
                        }
                    }
                }
                String msg = TaoBaoConstant.getTaoBaoMsg(repotcode);
                returnMap.put("msg",msg);
                returnMap.put("code",repotcode);
                returnMap.put("flag",true);
            }else{
                returnMap.put("flag",false);
                returnMap.put("msg","请求失败!");
            }
        }else{
            returnMap.put("flag",false);
            returnMap.put("msg","请求失败!");
        }
        return returnMap;
    }
    /**
     * 淘宝登录授权检查二维码状态接口
     * @param sid
     * @param userInfo
     * @return
     * @throws Exception
     */
    @Override
    public Map tabaoCheckQrCode(String sid, AppUserInfo userInfo) throws Exception {
        Map returnMap = new HashMap();
        String url = iSystemDictService.getInfo("rule.url");
        String host = url+"/szt/fcTaoBao/qrcode";
        Map<String , Object> param = new HashMap<String , Object>();
        param.put("sid",sid);
        param.put("pid", UUID.randomUUID().toString());
        JSONObject jsonObject = taoBaoShouQuan(host,param);
        if(jsonObject!=null){
            String responseCode = jsonObject.getString("responseCode");
            if("0".equals(responseCode)){
                String result = (String) jsonObject.get("data");
                JSONObject data = JSON.parseObject(result);
                String code = data.getString("code");
                String type = data.getString("type");//验证码类型
                String msg = TaoBaoConstant.getTaoBaoMsg(code);
                String base64String = "";
                if("0".equals(code)){
                    base64String = data.getString("data");//获取二维码
                }
                returnMap.put("sid",sid);
                returnMap.put("msg",msg);
                returnMap.put("code",code);
                returnMap.put("flag",true);
                returnMap.put("type",type);
                returnMap.put("base64String",base64String);
            }else{
                returnMap.put("flag",false);
                returnMap.put("msg","请求失败!");
            }
        }else{
            returnMap.put("msg","请求失败!");
            returnMap.put("flag",false);
        }
        return returnMap;
    }

    /**
     * 聚立信获取手机运营商信息，获取token接口
     * @param userInfo
     * @return
     * @throws Exception
     */
    public Map jxlApplyForm(AppUserInfo userInfo) throws Exception {
        Map returnMap = new HashMap();
        Map<String , Object> param = new HashMap<String , Object>();
        param.put("name", userInfo.getName());
        param.put("idNo", userInfo.getCard());
        param.put("phone",userInfo.getTel());
        //查询
        String contactSql = "select main_sign,link_name,relationship,contact from mag_customer_linkman where customer_id = '"+userInfo.getCustomer_id()+"' and type ='1'";
        List contactList = sunbmpDaoSupport.findForList(contactSql);
        Map contactMap = new HashMap();
        if(contactList!=null && contactList.size()>0){
            ArrayList<Contact> list = new ArrayList<>();
            for(int i=0;i<contactList.size();i++){
                String contactType = "0";
                contactMap = (Map) contactList.get(i);
                if("0".equals(contactMap.get("main_sign").toString())){
                    if("0".equals(contactMap.get("relationship").toString())){
                        contactType = "1";
                    }else if("1".equals(contactMap.get("relationship").toString())){
                        contactType = "1";
                    }else if("2".equals(contactMap.get("relationship").toString())){
                        contactType = "2";
                    } else if("4".equals(contactMap.get("relationship").toString())){
                        contactType = "2";
                    }else if("3".equals(contactMap.get("relationship").toString())){
                        contactType = "0";
                    }else if("5".equals(contactMap.get("relationship").toString())){
                        contactType = "3";
                    }else if("6".equals(contactMap.get("relationship").toString())){
                        contactType = "6";
                    }
                }else{
                    if("0".equals(contactMap.get("relationship").toString())){
                        contactType = "4";
                    }else if("1".equals(contactMap.get("relationship").toString())){
                        contactType = "6";
                    }else if("2".equals(contactMap.get("relationship").toString())){
                        contactType = "5";
                    }else if("3".equals(contactMap.get("relationship").toString())){
                        contactType = "6";
                    }
                }
                Contact contact = new Contact();
                contact.setContact_name(contactMap.get("link_name").toString());
                contact.setContact_tel(contactMap.get("contact").toString());
                contact.setContact_type(contactType);
                list.add(contact);
            }
            param.put("contacts", list);
        }
        String host = iSystemDictService.getInfo("rule.url");
        if(org.apache.commons.lang.StringUtils.isEmpty(host)){
            host = "http://192.168.102.103:9999";
        }
        String data = MobilePwdApi.jxlApplyForm(host,param);
        JSONObject json = JSONObject.parseObject(data);
        if(json!=null){
            if("100".equals(json.getString("resultCode"))){
                String token = json.getString("token");
                JSONObject jsonObject = (JSONObject) json.get("obj");
                if(jsonObject!=null){
                    String website = jsonObject.getString("website");
                    returnMap.put("website",website);
                    returnMap.put("flag",true);
                }else{
                    returnMap.put("msg","系统异常,请联系客服人员");
                    returnMap.put("flag",false);
                }
                returnMap.put("token",token);
            }else if("300".equals(json.getString("resultCode"))){
                returnMap.put("msg",json.get("resultMsg"));
                returnMap.put("flag",false);
            }
        }else{
            returnMap.put("msg","系统异常,请联系客服人员");
            returnMap.put("flag",false);
        }
        returnMap.put("state",json.get("resultCode"));
        return returnMap;
    }
    /**
     * 聚立信获取手机运营商短信验证码接口
     * @throws Exception
     */
    @Override
    public Map jxlGetSmsCode(Map<String , Object> map,AppUserInfo userInfo) throws Exception {
        Map returnMap = new HashMap();
        String host = iSystemDictService.getInfo("rule.url");
        if(org.apache.commons.lang.StringUtils.isEmpty(host)){
            host = "http://192.168.102.103:9999";
        }
        Map applyFormMap = jxlApplyForm(userInfo);
        if("100".equals(applyFormMap.get("state").toString())){
            //获取短信参数
            String token = applyFormMap.get("token").toString();
            String website = applyFormMap.get("website").toString();
            map.put("token",token);
            map.put("website",website);
            map.put("type","");
            String data = MobilePwdApi.jxlGetSmsCode(host,map);
            JSONObject json = JSONObject.parseObject(data);
            if(json!=null){
                JSONObject jsonObject = (JSONObject) json.get("obj");
                if(jsonObject!=null){
                    if("10008".equals(jsonObject.getString("process_code"))){
                        returnMap.put("flag",true);
                        returnMap.put("msg", json.getString("resultMsg"));
                        //手机运营商授权成功将状态改为1
                        String updateSql = "update mag_customer set phone_state_spd = '1',authorization_complete_spd = '1' where id = '"+userInfo.getCustomer_id()+"'";
                        sunbmpDaoSupport.exeSql(updateSql);
                    }else if("1000".equals(jsonObject.getString("process_code"))){
                        returnMap.put("msg", json.getString("resultMsg"));
                        returnMap.put("flag", false);
                    }else{
                        returnMap.put("msg", json.getString("resultMsg"));
                        returnMap.put("flag", false);
                    }
                }else{
                    returnMap.put("msg","系统异常,请联系客服人员");
                    returnMap.put("flag",false);
                }
                returnMap.put("state",jsonObject.getString("process_code"));
            }else{
                returnMap.put("msg","系统异常,请联系客服人员");
                returnMap.put("flag",false);
            }
            returnMap.put("website",website);
            returnMap.put("token",token);
        }else{
            returnMap.put("msg",applyFormMap.get("msg"));
            returnMap.put("flag",false);
        }
        return returnMap;
    }
    /**
     * 聚立信验证手机运营商短信验证码接口
     * @throws Exception
     */
    @Override
    public Map jxlCheckSmsCode(Map<String , Object> map,AppUserInfo userInfo) throws Exception {
        Map returnMap = new HashMap();
        String host = iSystemDictService.getInfo("rule.url");
        if(org.apache.commons.lang.StringUtils.isEmpty(host)){
            host = "http://192.168.102.103:9999";
        }
        //获取短信参数
        map.put("type","SUBMIT_CAPTCHA");
        String data = MobilePwdApi.jxlGetSmsCode(host,map);
        JSONObject json = JSONObject.parseObject(data);
        if(json!=null){
            JSONObject jsonObject = (JSONObject) json.get("obj");
            if(jsonObject!=null){
                if("10008".equals(jsonObject.getString("process_code"))){
                    returnMap.put("flag",true);
                    //手机运营商授权成功将状态改为1
                    String updateSql = "update mag_customer set phone_state_spd = '1',authorization_complete_spd = '1' where id = '"+userInfo.getCustomer_id()+"'";
                    sunbmpDaoSupport.exeSql(updateSql);
                }else if("1000".equals(jsonObject.getString("process_code"))){
                    returnMap.put("msg", json.getString("resultMsg"));
                    returnMap.put("flag", false);
                }else{
                    returnMap.put("msg", json.getString("resultMsg"));
                    returnMap.put("flag", false);
                }
            }else{
                returnMap.put("msg","系统异常,请联系客服人员");
                returnMap.put("flag",false);
            }
            returnMap.put("state",jsonObject.getString("process_code"));
        }else{
            returnMap.put("msg","系统异常,请联系客服人员");
            returnMap.put("flag",false);
        }
        return returnMap;
    }

    /**
     * 同盾创建任务，获取taskId
     * @return
     */
    public Map tongdunGetTaskId(Map map,String host,AppUserInfo userInfo) throws Exception {
        Map returnMap = new HashMap();
        String taskData = new MobilePwdApi().tongdunGetTaskId(host, map);
        String taskId = "";
        if (taskData != null) {
            JSONObject jsonTask = JSONObject.parseObject(taskData);
            if (jsonTask != null) {
                String responseCode = jsonTask.getString("responseCode");
                if ("0".equals(responseCode)) {
                    taskId = jsonTask.getString("taskId");
                    String updateSql = "update mag_customer set phone_task_id = '"+taskId+"' where id = '"+userInfo.getCustomer_id()+"'";
                    sunbmpDaoSupport.exeSql(updateSql);
                    returnMap.put("taskId",taskId);
                    returnMap.put("flag","success");
                }else {
                    returnMap.put("flag","fail");
                    returnMap.put("msg",jsonTask.getString("responseMsg"));
                }
            }else{
                returnMap.put("flag","fail");
                returnMap.put("msg","系统异常,请联系客服人员");
            }
        } else{
            returnMap.put("flag","fail");
            returnMap.put("msg","系统异常,请联系客服人员");
        }
        return returnMap;
    }
    /**
     * 同盾获取，验证短信验证码提交接口，获取系统异常，造成的数据丢失
     * @return
     */
    public Map tongdunSubmit(Map map,String host) throws Exception {
        map.put("requestType", "submit");//先提交，获取短信，验证短信，请求类型。
        Map returnMap = new HashMap();
        String taskData = new MobilePwdApi().tongdunSubmitQuery(host, map);
        if (taskData != null) {
            JSONObject jsonTask = JSONObject.parseObject(taskData);
            if (jsonTask != null) {
                returnMap.put("flag","success");
            }else{
                returnMap.put("flag","fail");
                returnMap.put("msg","系统异常,请联系客服人员");
            }
        } else{
            returnMap.put("flag","fail");
            returnMap.put("msg","系统异常,请联系客服人员");
        }
        return returnMap;
    }
    //同盾获取信息
    public JSONObject tongdunQuery(Map map,String host) throws Exception{
        map.put("requestType", "query");//第二步，获取的时候该为query
        String smsData = new MobilePwdApi().tongdunSubmitQuery(host,map);
        JSONObject jsonSmsCode = JSON.parseObject(smsData);

        return jsonSmsCode;
    }
    /**
     * 获取同盾手机运营商短信验证码，或者图形验证码
     * @param userInfo
     * @return
     */
    @Override
    public Map tongdunGetSmsCode(String password,String phone,String host,AppUserInfo userInfo) throws Exception {
        Map returnMap = new HashMap();//返回接口Map
        Map taskMap = new HashMap();//获取任务map
        Map smsMap = new HashMap();//获取短信验证码map
        taskMap.put("name",userInfo.getName());
        taskMap.put("idNo",userInfo.getCard());
        taskMap.put("phone",phone);
        //第一步先创建任务，获取taskId
        Map map = tongdunGetTaskId(taskMap,host,userInfo);
        String flag = map.get("flag").toString();
        if("success".equals(flag)){
            smsMap.put("taskId", map.get("taskId").toString());//任务创建成功，获取taskId
        }else{
            returnMap.put("msg",map.get("msg").toString());//任务创建失败，直接返回消息
            return returnMap;
        }
        //第二步，先提交，然后获取
        smsMap.put("taskStage", "INIT");
        smsMap.put("phone", phone);//手机号码
        smsMap.put("userPass", password);//运营商服务密码
        smsMap.put("smsCode", "");//短信验证码
        smsMap.put("authCode", "");//图形验证码
        Map submitMap = tongdunSubmit(smsMap,host);//提交任务
        if("success".equals(submitMap.get("flag"))){
            JSONObject jsonObject = null;
            while (true){
                jsonObject = tongdunQuery(smsMap,host);
                if (jsonObject != null) {
                    String responseCode = jsonObject.getString("responseCode");
                    if("100".equals(responseCode)){
                        continue;
                    }else if("137".equals(responseCode) || "2007".equals(responseCode)){
                        //手机运营商授权成功将状态改为1
                        String updateSql = "update mag_customer set phone_host_state = '"+1+"',authorization_complete = '"+100+"' where id = '"+userInfo.getCustomer_id()+"'";
                        sunbmpDaoSupport.exeSql(updateSql);
                        break;
                    }else{
                        break;
                    }
                }
            }
            returnMap.put("smsData",jsonObject.get("data"));
            returnMap.put("responseCode",jsonObject.get("responseCode"));
            returnMap.put("responseMsg",jsonObject.get("responseMsg"));
            returnMap.put("taskId",map.get("taskId").toString());
            returnMap.put("type","frist");
        }else{
            returnMap.put("msg",submitMap.get("msg").toString());//任务创建失败，直接返回消息
            return returnMap;
        }
        return returnMap;
    }

    /**
     * 调用同盾获取手机运营运营商短信重发接口
     * @param taskId
     * @param host
     * @return
     * @throws Exception
     */
    @Override
    public String tongdunRetrySmsCode(String taskId,String host) throws Exception {
        Map smsMap = new HashMap();//获取短信验证码map
        smsMap.put("taskId", taskId);
        String data = new MobilePwdApi().tongdunSubmitRetry(host,smsMap);
        return data;
    }

    /**
     * 验证同盾手机运营商短信验证码，或者图形验证码
     * @param userInfo
     * @return
     */
    @Override
    public Map tongdunCheckSmsCode(String password,String taskId,String taskStage,String smsCode,String authCode, String phone,String host,AppUserInfo userInfo) throws Exception{
        Map returnMap = new HashMap();//返回接口Map
        Map smsMap = new HashMap();//获取短信验证码map
        //第二步，先提交，然后获取
        smsMap.put("taskId", taskId);//图形验证码
        smsMap.put("taskStage", taskStage);
        smsMap.put("phone", phone);//手机号码
        smsMap.put("userPass", password);//运营商服务密码
        smsMap.put("smsCode", smsCode);//短信验证码
        smsMap.put("authCode", authCode);//图形验证码
        Map submitMap = tongdunSubmit(smsMap,host);//提交任务
        if("success".equals(submitMap.get("flag"))){
            JSONObject jsonObject = null;
            while (true){
                jsonObject = tongdunQuery(smsMap,host);
                if (jsonObject != null) {
                    String responseCode = jsonObject.getString("responseCode");
                    if("100".equals(responseCode)){
                        continue;
                    }else if("137".equals(responseCode) || "2007".equals(responseCode)){
                        //手机运营商授权成功将状态改为1
                        String updateSql = "update mag_customer set phone_host_state = '"+1+"',authorization_complete = '"+100+"' where id = '"+userInfo.getCustomer_id()+"'";
                        sunbmpDaoSupport.exeSql(updateSql);
                        break;
                    }
                    else{
                        break;
                    }
                }
            }
            returnMap.put("smsData",jsonObject.get("data"));
            returnMap.put("responseCode",jsonObject.get("responseCode"));
            returnMap.put("responseMsg",jsonObject.get("responseMsg"));
        }else{
            returnMap.put("msg",submitMap.get("msg").toString());//任务创建失败，直接返回消息
            return returnMap;
        }
        return returnMap;
    }
    @Override
    public String mxPrepare(String host, String idNo, String name, String password, String phone, String userId) throws Exception{
        String d = String.valueOf(System.currentTimeMillis());
        String sign = MD5.getSign(phone, d);
        return MobilePwdApi.mxPrepare(host, d, idNo, name, password, phone, sign, userId);
    }

    @Override
    public String mxLunxun(String host, String phone, String smsCode) throws Exception{
        String d = String.valueOf(System.currentTimeMillis());
        String sign = MD5.getSign(phone, d);
        return MobilePwdApi.mxLunxun(host,d,phone,sign,smsCode);
    }
    @Override
    public String reGetCode(String host, String phone, String type) {
        String d = String.valueOf(System.currentTimeMillis());
        String sign = MD5.getSign(phone, d);
        return MobilePwdApi.reGetCode(host,d,phone,sign,type);
    }
}

