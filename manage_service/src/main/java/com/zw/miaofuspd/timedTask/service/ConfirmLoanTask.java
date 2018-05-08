package com.zw.miaofuspd.timedTask.service;


import com.base.util.TraceLoggerUtil;
import com.yeepay.g3.utils.common.json.JSONObject;
import com.yeepay.g3.utils.common.json.XML;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.*;
import com.zw.service.task.abs.AbsTask;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/11.
 */
public class ConfirmLoanTask extends AbsTask {
    @Autowired
    private ISystemDictService iSystemDictService;
    @Autowired
    private IDictService iDictService;

    /**
     * 定时放款查询
     *
     * @throws Exception
     */
    @Override
    public void doWork() throws Exception {
        Map<String, Object> map = new HashedMap();
        String pfxpath = iSystemDictService.getInfo("payTreasure.pfxName");//商户私钥
        String cerpath = iSystemDictService.getInfo("payTreasure.cerName");//宝付公钥
        String memberId = iSystemDictService.getInfo("payTreasure.memberId");//商户号
        String terminalId = iSystemDictService.getInfo("payTreasurePay.terminalId");//终端号
        String loanQueryUrl = iSystemDictService.getInfo("payTreasure.loanQueryUrl");//放款查询url
        String keyStorePassword = iSystemDictService.getInfo("payTreasure.pfxPwd");//商户私钥密码
        map.put("pfxPath", pfxpath);
        map.put("cerPath", cerpath);
        map.put("memberId", memberId);
        map.put("terminalId", terminalId);
        map.put("loanQueryUrl", loanQueryUrl);
        map.put("keyStorePassword", keyStorePassword);
       // map.put("transNo", "120150020180102133544830");
       String batchSql = "select md.id,md.order_id, md.batch_no from mag_transaction_details md left join mag_order mo on md.order_id=mo.id  where  md.type='0' and md.state='0' and mo.order_type='2' order by ALTER_TIME desc";
        List list = sunbmpDaoSupport.findForList(batchSql);
        try {
            Map confrimMap = new HashMap();
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    confrimMap = (Map) list.get(i);
                    map.put("transNo", confrimMap.get("batch_no").toString());
                    String orderId = confrimMap.get("order_id").toString();
                    String id = confrimMap.get("id").toString();
                    Map infoMap = confirmationMerchant(map);
                    TraceLoggerUtil.info("----------->结果" + infoMap);
                   if ("0401".equals(infoMap.get("returnCode").toString())||"401".equals(infoMap.get("returnCode").toString())) {
                        //代付交易查证信息不存在(0401)更新mag_transaction_details和mag_order表
                        String datailSql = "update mag_transaction_details set state='2' where id='" + id + "'";
                        sunbmpDaoSupport.exeSql(datailSql);
                        String orderSql = "update mag_order set loan_state='4' where id='" + orderId + "'";
                        sunbmpDaoSupport.exeSql(orderSql);
                    } else {
                       //TraceLoggerUtil.info("----------->结果更新失败");
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private static Map<String, Object> confirmationMerchant(Map map) throws Exception {
        Map<String, Object> resultMap = new HashedMap();
        TransContent<TransReqBF0040002> transContent = new TransContent<TransReqBF0040002>(
                TransConstant.data_type_xml);

        List<TransReqBF0040002> trans_reqDatas = new ArrayList<TransReqBF0040002>();
        TransReqBF0040002 transReqData = new TransReqBF0040002();
        transReqData.setTrans_no(map.get("transNo").toString());
        trans_reqDatas.add(transReqData);
        transContent.setTrans_reqDatas(trans_reqDatas);
        String bean2XmlString = transContent.obj2Str(transContent);
        System.out.println("报文：" + bean2XmlString);
        String keyStorePath = map.get("pfxPath").toString();
        String keyStorePassword = map.get("keyStorePassword").toString();
        String pub_key = map.get("cerPath").toString();
        String origData = bean2XmlString;
        //origData = Base64.encode(origData);
        /**
         * 加密规则：项目编码UTF-8
         * 第一步：BASE64 加密
         * 第二步：商户私钥加密
         */
        try {
            origData = new String(SecurityUtil.Base64Encode(origData));//Base64.encode(origData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String encryptData = RsaCodingUtil.encryptByPriPfxFile(origData, keyStorePath, keyStorePassword);
        //String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, pfxpath, pfxPwd);

        TraceLoggerUtil.info("----------->放款【私钥加密-结果】" + encryptData);

        // 发送请求
        String requestUrl = map.get("loanQueryUrl").toString();
        String memberId = map.get("memberId").toString(); // 商户号
        String terminalId = map.get("terminalId").toString(); // 终端号
        String dataType = TransConstant.data_type_xml; // 数据类型 xml/json

        RequestParams params = new RequestParams();
        params.setMemberId(Integer.parseInt(memberId));
        params.setTerminalId(Integer.parseInt(terminalId));
        params.setDataType(dataType);
        params.setDataContent(encryptData);// 加密后数据
        params.setVersion("4.0.0");
        params.setRequestUrl(requestUrl);
        SimpleHttpResponse response = null;
        response = BaofooClient.doRequest(params);
        //TraceLoggerUtil.info("放款-------宝付请求返回结果：" + response.getEntityString());

        TransContent<TransRespBF0040002> str2Obj = new TransContent<TransRespBF0040002>(dataType);

        String reslut = response.getEntityString();

        /**
         * 在商户终端正常的情况下宝付同步返回会以密文形式返回,如下：
         *
         * 此时要先宝付提供的公钥解密：RsaCodingUtil.decryptByPubCerFile(reslut, pub_key)
         *
         * 再次通过BASE64解密：new String(new Base64().decode(reslut))
         *
         * 在商户终端不正常或宝付代付系统异常的情况下宝付同步返回会以明文形式返回
         */
        //System.out.println(reslut);
        TraceLoggerUtil.info("放款宝付请求返回结果未解密：" + reslut);
        if (reslut.contains("trans_content")) {
            // 我报文错误处理
            str2Obj = (TransContent<TransRespBF0040002>) str2Obj
                    .str2Obj(reslut, TransRespBF0040002.class);
            //业务逻辑判断
            TraceLoggerUtil.info("报文头参数不正确");
        } else {
            reslut = RsaCodingUtil.decryptByPubCerFile(reslut, pub_key);
            reslut = SecurityUtil.Base64Decode(reslut);
            str2Obj = (TransContent<TransRespBF0040002>) str2Obj
                    .str2Obj(reslut, TransRespBF0040002.class);
            //业务逻辑判断
        }
        TraceLoggerUtil.info("放款宝付请求返回结果已解密：" + reslut);
        //将xml转为json
        JSONObject xmlJSONObj = XML.toJSONObject(reslut);
        String returnCode = xmlJSONObj.getJSONObject("trans_content").getJSONObject("trans_head").get("return_code").toString();
        String returnMsg = xmlJSONObj.getJSONObject("trans_content").getJSONObject("trans_head").getString("return_msg");
        resultMap.put("returnCode", returnCode);
        resultMap.put("returnMsg", returnMsg);
        return resultMap;

    }

    public static void main(String [] args) throws Exception{
        String reslut="<?xml version=\"1.0\" encoding=\"UTF-8\" ?><trans_content><trans_head><return_code>0401</return_code><return_msg>代付交易查证信息不存在（未知）</return_msg></trans_head></trans_content>";
        JSONObject xmlJSONObj = XML.toJSONObject(reslut);
        String returnCode = xmlJSONObj.getJSONObject("trans_content").getJSONObject("trans_head").get("return_code").toString();
        String returnMsg = xmlJSONObj.getJSONObject("trans_content").getJSONObject("trans_head").getString("return_msg");
        System.out.println(returnCode+"________"+returnMsg);
    }
}