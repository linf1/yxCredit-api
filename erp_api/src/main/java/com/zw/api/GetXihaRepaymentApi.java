package com.zw.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.HttpClientUtil;
import com.base.util.TraceLoggerUtil;

import java.util.Map;
import java.util.logging.Logger;

/**
 * <strong>Title : <br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2017年10月10日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:niudengfeng <br>
 *         email: <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
public class GetXihaRepaymentApi {

    private static Logger logger = Logger.getLogger("communication");

    /**
     * 获取还款首页列表
     *
     * @param map
     * @return
     * @throws Exception
     */
    public JSONObject getXihaRepaymentPlan(Map map) throws Exception {
        try {
            String url = map.get("host").toString()+"/service/rest/queryloanContract.json";
            TraceLoggerUtil.info("接口发送--->" + url);
            String param = map.get("param").toString();
            String data = HttpClientUtil.getInstance().sendHttpPost(url,param);
            TraceLoggerUtil.info("接口返回--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            TraceLoggerUtil.error("异常--->", ex);
            throw ex;
        }
    }

    /**
     * 根据合同编号查询还款明细
     *
     * @param map
     * @return
     * @throws Exception
     */
    public JSONObject getXihaRepaymentPlanByLoanNo(Map map) throws Exception {
        try {
            String url = map.get("host").toString()+"/service/rest/queryRepayPlan.json";
            TraceLoggerUtil.info("接口发送--->" + url);
            String param = map.get("param").toString();
            String data = HttpClientUtil.getInstance().sendHttpPost(url,param);
            TraceLoggerUtil.info("接口返回--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            TraceLoggerUtil.error("异常--->", ex);
            throw ex;
        }
    }
    /**
     * 单期还款试算接口
     *
     * @param map
     * @return
     * @throws Exception
     */
    public JSONObject getSinglePeriodsTry(Map map) throws Exception {
        try {
            String url = map.get("host").toString()+"/service/rest/singlePayCalc.json";
            TraceLoggerUtil.info("接口发送--->" + url);
            String param = map.get("param").toString();
            String data = HttpClientUtil.getInstance().sendHttpPost(url,param);
            TraceLoggerUtil.info("接口返回--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            TraceLoggerUtil.error("异常--->", ex);
            throw ex;
        }
    }

    /**
     * 单期还款
     *
     * @param map
     * @return
     * @throws Exception
     */
    public JSONObject singleRepay(Map map) throws Exception {
        try {
            String url = map.get("host").toString()+"/service/rest/singlePay.json";
            TraceLoggerUtil.info("接口发送--->" + url);
            String param = map.get("param").toString();
            String data = HttpClientUtil.getInstance().sendHttpPost(url,param);
            TraceLoggerUtil.info("接口返回--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            TraceLoggerUtil.error("异常--->", ex);
            throw ex;
        }
    }


    /**
     * 多期还款试算
     *
     * @param map
     * @return
     * @throws Exception
     */
    public JSONObject multiPayCalc(Map map) throws Exception {
        try {
            String url = map.get("host").toString()+"/service/rest/multiPayCalc.json";
            TraceLoggerUtil.info("接口发送--->" + url);
            String param = map.get("param").toString();
            String data = HttpClientUtil.getInstance().sendHttpPost(url,param);
            TraceLoggerUtil.info("接口返回--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            TraceLoggerUtil.error("异常--->", ex);
            throw ex;
        }
    }

    /**
     * 多期还款
     *
     * @param map
     * @return
     * @throws Exception
     */
    public JSONObject multiPay(Map map) throws Exception {
        try {
            String url = map.get("host").toString()+"/service/rest/multiPay.json";
            TraceLoggerUtil.info("接口发送--->" + url);
            String param = map.get("param").toString();
            String data = HttpClientUtil.getInstance().sendHttpPost(url,param);
            TraceLoggerUtil.info("接口返回--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            TraceLoggerUtil.error("异常--->", ex);
            throw ex;
        }
    }

    /**
     * 多期提前结清试算
     *
     * @param map
     * @return
     * @throws Exception
     */
    public JSONObject multiPrepayCalc(Map map) throws Exception {
        try {
            String url = map.get("host").toString()+"/service/rest/multiPrepayCalc.json";
            TraceLoggerUtil.info("接口发送--->" + url);
            String param = map.get("param").toString();
            String data = HttpClientUtil.getInstance().sendHttpPost(url,param);
            TraceLoggerUtil.info("接口返回--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            TraceLoggerUtil.error("异常--->", ex);
            throw ex;
        }
    }

    /**
     * 多期提前结清试算
     *
     * @param map
     * @return
     * @throws Exception
     */
    public JSONObject multiPrepay(Map map) throws Exception {
        try {
            String url = map.get("host").toString()+"/service/rest/multiPrepay.json";
            TraceLoggerUtil.info("接口发送--->" + url);
            String param = map.get("param").toString();
            String data = HttpClientUtil.getInstance().sendHttpPost(url,param);
            TraceLoggerUtil.info("接口返回--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            TraceLoggerUtil.error("异常--->", ex);
            throw ex;
        }
    }
    /**
     * 绑卡
     *
     * @param map
     * @return
     * @throws Exception
     */
    public JSONObject cardBind(Map map) throws Exception {
        try {
            String url = map.get("host").toString()+"/service/rest/cardBind.json";
            TraceLoggerUtil.info("接口发送--->" + url);
            String param = map.get("param").toString();
            String data = HttpClientUtil.getInstance().sendHttpPost(url,param);
            TraceLoggerUtil.info("接口返回--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            TraceLoggerUtil.error("异常--->", ex);
            throw ex;
        }
    }
    /**
     * 资金方路由
     *
     * @param map
     * @return
     * @throws Exception
     */
    public JSONObject routeInvestor(Map map) throws Exception {
        try {
            String url = map.get("host").toString()+"/service/rest/routeInvestor.json";
            TraceLoggerUtil.info("接口发送--->" + url);
            String param = map.get("param").toString();
            String data = HttpClientUtil.getInstance().sendHttpPost(url,param);
            TraceLoggerUtil.info("接口返回--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            TraceLoggerUtil.error("异常--->", ex);
            throw ex;
        }
    }
    /**
     * 默认变更卡
     *
     * @param map
     * @return
     * @throws Exception
     */
    public JSONObject defaultCardChange(Map map) throws Exception {
        try {
            String url = map.get("host").toString()+"/service/rest/defaultCardChange.json";
            TraceLoggerUtil.info("接口发送--->" + url);
            String param = map.get("param").toString();
            String data = HttpClientUtil.getInstance().sendHttpPost(url,param);
            TraceLoggerUtil.info("接口返回--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            TraceLoggerUtil.error("异常--->", ex);
            throw ex;
        }
    }
    /**
     * 放款
     *
     * @param map
     * @return
     * @throws Exception
     */
    public JSONObject singleLender(Map map) throws Exception {
        try {
            String url = map.get("host").toString()+"/service/rest/singleLender.json";
            TraceLoggerUtil.info("接口发送--->" + url);
            String param = map.get("param").toString();
            String data = HttpClientUtil.getInstance().sendHttpPost(url,param);
            TraceLoggerUtil.info("接口返回--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            TraceLoggerUtil.error("异常--->", ex);
            throw ex;
        }
    }





    public static void main(String[] args) throws Exception {
//        Map map = new HashMap();
//        Map reqHeadMap = new HashMap();
//        Map paramMap = new HashMap();
//        reqHeadMap.put("token","xxxxxxxx");
//        reqHeadMap.put("channel","xxxx");
//        map.put("userCustId","52010201611222770");
////        map.put("cardNo","78270165764000000");
////        map.put("cardType","0");
////        map.put("amount","2000");
//        map.put("reqHead",reqHeadMap);
//        String param = toJSONString(map);
//        paramMap.put("param",param);
//        paramMap.put("host","http://192.168.9.158:8080/core-server-app");
//        System.out.print(toJSONString(map));
//        Map resultmap = (Map) new GetXihaRepaymentApi().routeInvestor(paramMap);
//        Map dataMap = (Map) resultmap.get("data");
//        System.out.print(dataMap.get("channel"));




//        Map inMap = new HashMap();
//        String data = String.valueOf(System.currentTimeMillis());
//        inMap.put("systemSourceId","XHQB");
//        inMap.put("chnId","YEE4");
//        inMap.put("bizId",String.valueOf(System.currentTimeMillis()));
//        inMap.put("accountNo","78270165764000008");
//        inMap.put("accountName","test");
//        inMap.put("idCard","13082119870416331X");
//        inMap.put("cardKind","01");
//        inMap.put("accountType","0");
//        inMap.put("mobile","18612964521");
//        inMap.put("remark","测试绑卡");
//        String mima = "XHQB"+data;
//        String signInfo = com.zw.api.mobilepwd.MD5.getSign(mima,"123321");
//        inMap.put("signInfo",signInfo);
//        inMap.put("bankId","403");
//        inMap.put("accountFlag","0");
//        inMap.put("validateCode","");
//        inMap.put("bindCradType","4");
//        String url = "http://192.168.9.157:8080/settle/services/ceFinalPaymentWS/bindBankCard.json";
//        String respString = HttpClientUtil.getInstance().sendHttpPost(url, toJSONString(inMap));
//        System.out.println("respString::::" + respString);


//        CeBindBankCardReq req = new CeBindBankCardReq();
//        req.setBizId(String.valueOf(System.currentTimeMillis()));
//        req.setSystemSourceId("XHQB");
//        req.setBankId("403");
//        req.setAccountName("test");
//        req.setAccountNo("78270165764000008");
//        req.setCardKind("01");
//        req.setIdCard("13082119870416331X");
//        req.setMobile("18612964521");
//        req.setRemark("测试绑卡");
//        req.setSendSms(false);
//        req.setAccountFlag("0");
//        req.setAccountType("0");
//        req.setChnId("YEE4");
//        req.setBindCradType("4");
////		String signInfo = new Md5PasswordEncoder().encodePassword(req.getSystemSourceId() +req.getBizId(),
////				"123321");
//        //   req.setSignInfo("123456");
//        String signInfo1 = new Md5PasswordEncoder().encodePassword(req.getSystemSourceId() +"4821567816000",
//                "123321");
//        req.setSignInfo("1489aed54a1fb39dd799141860b1ae35");
////		System.out.println(signInfo1);
////		String aaString="fc13f5c48b236676921ce7b94bde646d";
//        //JSONArray aaArray=JSONArray.fromObject(req);
//        String reqString = JSON.toJSONString(req);
//        ;
//        System.out.println(reqString);
//        // 发送通知
//        String url = "http://192.168.9.157:8080/settle/services/ceFinalPaymentWS/bindBankCard.json";
//        String respString = HttpClientUtil.getInstance().sendHttpPost(url, reqString);
//        System.out.println("respString::::" + respString);
//
//        CeBindBankCardReq req = new CeBindBankCardReq();
//        req.setBizId(String.valueOf(System.currentTimeMillis()));
//        req.setSystemSourceId("XHQB");
//        req.setBankId("403");
//        req.setAccountName("test");
//        req.setAccountNo("78270165764000008");
//        req.setCardKind("01");
//        req.setIdCard("13082119870416331X");
//        req.setMobile("18612964521");
//        req.setRemark("测试绑卡");
//        req.setSendSms(false);
//        req.setAccountFlag("0");
//        req.setAccountType("0");
//        req.setChnId("YEE4");
//        req.setBindCradType("4");
////		String signInfo = new Md5PasswordEncoder().encodePassword(req.getSystemSourceId() +req.getBizId(),
////				"123321");
//        //   req.setSignInfo("123456");
//        String signInfo1 = new Md5PasswordEncoder().encodePassword(req.getSystemSourceId() +"4821567816000",
//                "123321");
//        req.setSignInfo("1489aed54a1fb39dd799141860b1ae35");
////		System.out.println(signInfo1);
////		String aaString="fc13f5c48b236676921ce7b94bde646d";
//        //JSONArray aaArray=JSONArray.fromObject(req);
//        String reqString = JSON.toJSONString(req);
//        ;
//        System.out.println(reqString);
//        // 发送通知
//        String url = "http://192.168.9.157:8080/settle/services/ceFinalPaymentWS/bindBankCard.json";
//        String respString = HttpClientUtil.getInstance().sendHttpPost(url, reqString);
//        System.out.println("respString::::" + respString);
//

        String paramMap = "{\"infoMessage\":[{\"custInfo\":{\"nowaddress\":\"哦所以\",\"certType\":\"001001\",\"address\":\"安徽省巢湖市中㘿镇蛟龙行政村大周村\",\"liveBuildType\":\"1\",\"loanNo\":\"\",\"sex\":\"019001\",\"creditPreAmount\":\"8000\",\"liveProv\":\"河北省\",\"custName\":\"周欢\",\"liveArea\":\"长安区\",\"certIssuOrg\":\"巢湖市公安局\",\"liveCity\":\"石家庄市\",\"certNo\":\"342601199310114311\",\"custType\":\"017001\",\"certValidDate\":\"2015.08.28-2025.08.28\",\"TEL\":\"18356092311\",\"customerSource\":\"XHQB\",\"creditAmount\":\"4500.0\",\"regType\":\"002002\",\"userCustId\":\"614beed9-4ab5-4804-8c89-123d5340e495\"},\"custWork\":{\"unitProv\":\"山东省\",\"address\":\"哦我主任\",\"loanNo\":\"201710161019089639\",\"workUtil\":\"有收获\",\"isValid\":\"1\",\"unitCity\":\"济南市\",\"industry\":\"销售行业\",\"workJob\":\"自营者\",\"industryId\":\"1\",\"payType\":\"\",\"unitArea\":\"历下区\",\"monthIncome\":\"5800\",\"unitTel\":\"0551-5421114\",\"complete\":\"100\"},\"loanContract\":{\"contractStatus\":\"0\",\"orderNo\":\"201710161019081590\",\"loanNo\":\"201710161019089639\",\"contractSource\":\"XHQB\",\"endDate\":\"2017-11-15\",\"channel\":\"\",\"custName\":\"周欢\",\"loanInstalments\":\"\",\"loanAmount\":\"500\",\"repayWay\":\"\",\"protocolName\":\"借款服务协议\",\"payMod\":\"THIRD\",\"contractName\":\"借款服务协议\",\"contractFlag\":\"T+0\",\"productInfoId\":\"\",\"startDate\":\"2017-10-16\"},\"couponInfo\":{\"couponAmount\":\"\",\"couponType\":\"\"},\"lenderCard\":{\"cardType\":\"1\",\"cardNo\":\"3333333333333333333\"},\"systemAttachment\":[{\"fileName\":\"借款服务协议\",\"loanNo\":\"201710161019089639\",\"fileStatus\":\"1\",\"fileTitle\":\"借款服务协议\",\"fileDirectory\":\"\",\"busiType\":\"0\",\"fileStoreType\":\"OSS\",\"fileType\":\"10\",\"fileExtName\":\".pdf\"}],\"custOtherInfo\":{\"income\":\"5800\",\"realName\":\"周欢\",\"education\":\"0\",\"loanNo\":\"201710161019089639\",\"isValid\":\"1\",\"marry\":\"1\"},\"salesInfo\":{\"marNo\":\"01XHQB001\",\"strNo\":\"010001\",\"salerNo\":\"0100001\"},\"custBankCards\":{\"custBankCards\":[{\"isOwn\":\"\",\"idType\":\"001001\",\"loanNo\":\"201710161019089639\",\"cardName\":\"\",\"cardBankName\":\"浦发银行\",\"cardFlag\":\"0\",\"isbind\":\"0\",\"cardType\":\"1\",\"cardMobile\":\"18356092311\",\"idNo\":\"342601199310114311\",\"cardNo\":\"1111111111111111111\",\"cardmark\":\"0\",\"isDefault\":\"1\",\"cardBankId\":\"0310\",\"userCustId\":\"614beed9-4ab5-4804-8c89-123d5340e495\"},{\"isOwn\":\"\",\"idType\":\"001001\",\"loanNo\":\"201710161019089639\",\"cardName\":\"\",\"cardBankName\":\"广发银行\",\"cardFlag\":\"0\",\"isbind\":\"0\",\"cardType\":\"1\",\"cardMobile\":\"18356092311\",\"idNo\":\"342601199310114311\",\"cardNo\":\"4444444444444444444\",\"cardmark\":\"0\",\"isDefault\":\"0\",\"cardBankId\":\"0306\",\"userCustId\":\"614beed9-4ab5-4804-8c89-123d5340e495\"},{\"isOwn\":\"\",\"idType\":\"001001\",\"loanNo\":\"201710161019089639\",\"cardName\":\"\",\"cardBankName\":\"深圳发展银行\",\"cardFlag\":\"0\",\"isbind\":\"0\",\"cardType\":\"1\",\"cardMobile\":\"18256564578\",\"idNo\":\"342601199310114311\",\"cardNo\":\"6214855493724922\",\"cardmark\":\"0\",\"isDefault\":\"0\",\"cardBankId\":\"0307\",\"userCustId\":\"614beed9-4ab5-4804-8c89-123d5340e495\"},{\"isOwn\":\"\",\"idType\":\"001001\",\"loanNo\":\"201710161019089639\",\"cardName\":\"\",\"cardBankName\":\"光大银行\",\"cardFlag\":\"0\",\"isbind\":\"0\",\"cardType\":\"1\",\"cardMobile\":\"18110900600\",\"idNo\":\"342601199310114311\",\"cardNo\":\"9999999999999999999\",\"cardmark\":\"0\",\"isDefault\":\"0\",\"cardBankId\":\"0303\",\"userCustId\":\"614beed9-4ab5-4804-8c89-123d5340e495\"},{\"isOwn\":\"\",\"idType\":\"001001\",\"loanNo\":\"201710161019089639\",\"cardName\":\"\",\"cardBankName\":\"广州银行\",\"cardFlag\":\"0\",\"isbind\":\"0\",\"cardType\":\"1\",\"cardMobile\":\"18654564654\",\"idNo\":\"342601199310114311\",\"cardNo\":\"8888888888888888888\",\"cardmark\":\"0\",\"isDefault\":\"0\",\"cardBankId\":\"0413\",\"userCustId\":\"614beed9-4ab5-4804-8c89-123d5340e495\"},{\"isOwn\":\"\",\"idType\":\"001001\",\"loanNo\":\"201710161019089639\",\"cardName\":\"\",\"cardBankName\":\"交通银行\",\"cardFlag\":\"0\",\"isbind\":\"0\",\"cardType\":\"1\",\"cardMobile\":\"18356092311\",\"idNo\":\"342601199310114311\",\"cardNo\":\"5555555555555555555\",\"cardmark\":\"0\",\"isDefault\":\"0\",\"cardBankId\":\"0301\",\"userCustId\":\"614beed9-4ab5-4804-8c89-123d5340e495\"},{\"isOwn\":\"\",\"idType\":\"001001\",\"loanNo\":\"201710161019089639\",\"cardName\":\"\",\"cardBankName\":\"招商银行\",\"cardFlag\":\"0\",\"isbind\":\"0\",\"cardType\":\"1\",\"cardMobile\":\"18356092311\",\"idNo\":\"342601199310114311\",\"cardNo\":\"2222222222222222222\",\"cardmark\":\"0\",\"isDefault\":\"0\",\"cardBankId\":\"0308\",\"userCustId\":\"614beed9-4ab5-4804-8c89-123d5340e495\"},{\"isOwn\":\"\",\"idType\":\"001001\",\"loanNo\":\"201710161019089639\",\"cardName\":\"\",\"cardBankName\":\"交通银行\",\"cardFlag\":\"0\",\"isbind\":\"0\",\"cardType\":\"1\",\"cardMobile\":\"18654654654\",\"idNo\":\"342601199310114311\",\"cardNo\":\"6666666666666666666\",\"cardmark\":\"0\",\"isDefault\":\"0\",\"cardBankId\":\"0301\",\"userCustId\":\"614beed9-4ab5-4804-8c89-123d5340e495\"},{\"isOwn\":\"\",\"idType\":\"001001\",\"loanNo\":\"201710161019089639\",\"cardName\":\"\",\"cardBankName\":\"交通银行\",\"cardFlag\":\"0\",\"isbind\":\"0\",\"cardType\":\"1\",\"cardMobile\":\"18356092311\",\"idNo\":\"342601199310114311\",\"cardNo\":\"7777777777777777777\",\"cardmark\":\"0\",\"isDefault\":\"0\",\"cardBankId\":\"0301\",\"userCustId\":\"614beed9-4ab5-4804-8c89-123d5340e495\"},{\"isOwn\":\"\",\"idType\":\"001001\",\"loanNo\":\"201710161019089639\",\"cardName\":\"\",\"cardBankName\":\"中国工商银行\",\"cardFlag\":\"0\",\"isbind\":\"0\",\"cardType\":\"1\",\"cardMobile\":\"18356092311\",\"idNo\":\"342601199310114311\",\"cardNo\":\"3333333333333333333\",\"cardmark\":\"0\",\"isDefault\":\"0\",\"cardBankId\":\"0102\",\"userCustId\":\"614beed9-4ab5-4804-8c89-123d5340e495\"}]},\"custContactInfo\":[{\"loanNo\":\"201710161019089639\",\"mainSign\":\"0\",\"name\":\"哦哟哟\",\"relationship\":\"3\",\"complete\":\"100\",\"mobileTel\":\"18256564578\"},{\"loanNo\":\"201710161019089639\",\"mainSign\":\"1\",\"name\":\"我五杀\",\"relationship\":\"2\",\"complete\":\"100\",\"mobileTel\":\"18547888669\"},{\"loanNo\":\"201710161019089639\",\"name\":\"周欢\",\"relationship\":\"0\",\"complete\":\"100\",\"mobileTel\":\"18356092311\"}]}]}";
        System.out.print(paramMap);

    }

}


