package com.zw.miaofuspd.merchant.service;

import com.base.util.AverageCapitalPlusInterestUtils;
import com.base.util.DateUtils;
import com.zw.miaofuspd.facade.merchant.service.MerchandiseService;
import com.zw.miaofuspd.facade.merchant.service.QrCodeService;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.facade.ratescheme.service.RateSchemeService;
import com.zw.service.base.AbsServiceBase;
import com.zw.miaofuspd.util.QRCodeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by Administrator on 2017/11/22 0022.
 */
@Service
public class QrCodeServiceImpl extends AbsServiceBase implements QrCodeService {
    @Autowired
    private MerchandiseService merchandiseService;
    @Autowired
    private RateSchemeService rateSchemeService;
    @Autowired
    private AppOrderService appOrderService;
    /**
     * 生成商户二维码带logo
     * @return
     */
    @Override
    public Map  createQRcode(Map map,String idJson) throws Exception {
        Map returnMap = new HashMap();
        String a[] = idJson.split(",");
        String idJson1 = "";
        for(int i=0;i<a.length;i++){
            if(i==a.length-1){//拼接成一个带单引号的串
                idJson1 += "'"+a[i]+"'";
            }else{
                idJson1 += "'"+a[i]+"',";
            }
        }
        String orderTypeValue = map.get("orderTypeValue").toString();//订单类型
        String merchantId = map.get("merchantId").toString();//商户id
        String merchandiseId= map.get("merchandiseId").toString();//商品具体版本id
        String empId = map.get("empId").toString();//业务员工号
        String allMoney = map.get("allMoney").toString();//商品总金额
        String productId = map.get("productId").toString();//分期期数Id
        String downPayMoney = map.get("downPayMoney").toString();//商品金额首付款
        //根据商品的id获取商品的图片,以及商品的信息
        Map merchantdiseMap = merchandiseService.getMerchandiseInfoById(merchandiseId);
        String merchandiseUrl = "";//商品缩略图
        String merchandiseBrandName= "";//商品品牌
        String merchandiseModelName = "";//商品型号
        String merchandiseVersionName = "";//商品版本
        if(merchantdiseMap!=null){
            merchandiseUrl = merchantdiseMap.get("imgUrl").toString();//获取商品缩略图
            merchandiseBrandName = String.valueOf(merchantdiseMap.get("merchandiseBrandName"));
            merchandiseModelName = String.valueOf(merchantdiseMap.get("merchandiseModelName"));
            merchandiseVersionName = String.valueOf(merchantdiseMap.get("merchandiseVersionName"));
        }
        File saveDir = new File(map.get("root").toString()+"/erweima" + File.separator);
        //文件名
        String fileName = UUID.randomUUID().toString()+ ".jpg";
        if (!saveDir.exists()){
            saveDir.mkdirs();
        }
        InputStream is = getIns(merchandiseUrl);
        //获取自己数组
        byte[] getData = readInputStream(is);
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(is!=null){
            is.close();
        }
        merchandiseUrl = saveDir+File.separator+fileName;
        String createTime= DateUtils.getDateString(new Date());
        StringBuffer sendRequest = new StringBuffer();
        sendRequest.append("{");
        sendRequest.append("\"empId\": \"");//业务员工号
        sendRequest.append(empId);
        sendRequest.append("\",");
        sendRequest.append("\"allMoney\": \"");//商品总价
        sendRequest.append(allMoney);
        sendRequest.append("\",");
        sendRequest.append("\"downPayMoney\": \"");//首付金额
        sendRequest.append(downPayMoney);
        sendRequest.append("\",");
        sendRequest.append("\"merchantId\": \"");//商户id
        sendRequest.append(merchantId);
        sendRequest.append("\",");
        sendRequest.append("\"productId\": \"");//利率id
        sendRequest.append(productId);
        sendRequest.append("\",");
        sendRequest.append("\"idJson\": \"");//服务包id集合;
        sendRequest.append(idJson1);
        sendRequest.append("\",");
        sendRequest.append("\"merchandiseId\": \"");//商品id
        sendRequest.append(merchandiseId);
        sendRequest.append("\",");
        sendRequest.append("\"orderTypeValue\": \"");//订单类型
        sendRequest.append(orderTypeValue);
        sendRequest.append("\",");
        sendRequest.append("\"createTime\":\"");
        sendRequest.append(createTime);
        sendRequest.append("\"");
        sendRequest.append("}");
        String data = sendRequest.toString();
        //生成二维码
        String base64String = QRCodeTool.encodeBase64(data,merchandiseUrl);
        try{
            File f = new File(merchandiseUrl);
            f.delete();
        }catch (Exception e){
        }
       /* //根据产品id获取产品的费率和期数信息
        Map productMap =rateSchemeService.getFeeInfo(productId);
        double monthRate= com.base.util.StringUtils.toDouble(productMap.get("monthRate"))/100 ;
        double periods = Double.valueOf(productMap.get("periods").toString());
        double fenqiMoney = Double.valueOf(allMoney)- Double.valueOf(downPayMoney);//分期总金额
        //算出每月的还款金额
        String Money=String.valueOf(fenqiMoney/periods);
        String intNumber = Money.substring(0,Money.indexOf("."));
        double intMoney=Double.valueOf(intNumber);
        double monthPay =intMoney+ fenqiMoney*monthRate;*/

        //根据产品id获取产品的费率和期数信息
        Map productMap =rateSchemeService.getFeeInfo(productId);
        Map typeMap=rateSchemeService.getProductInfo(productId);
        String diyType=String.valueOf(typeMap.get("diyType"));
        if(StringUtils.isEmpty(diyType)){
            diyType="0";
        }
        double periods = Double.valueOf(productMap.get("periods").toString());
        double rate= com.base.util.StringUtils.toDouble(productMap.get("monthRate")) ;
        String diyDays=typeMap.get("diyDays")==null ? "":typeMap.get("diyDays").toString();
        Map orderMap=new HashMap();
        orderMap.put("diy_type",diyType);
        orderMap.put("diy_days",diyDays);
        orderMap.put("rate",rate);
        orderMap.put("amount",allMoney);
        orderMap.put("downPayMoney",downPayMoney);
        orderMap.put("periods",periods);
        //公用的方法
        Map retureMap=appOrderService.calculationRepayment(orderMap);
        double monthPay=(Double) retureMap.get("monthPay");
        double fenqiMoney=(Double)retureMap.get("fenqiMoney");
        downPayMoney=retureMap.get("downPayMoney").toString();

       // double monthPay = (fenqiMoney/periods)+fenqiMoney*monthRate;
       /* double monthPay = AverageCapitalPlusInterestUtils.getPerMonthPrincipalInterest(fenqiMoney,monthRate,periods);*/
        returnMap.put("fenqiMoney",String.format("%.2f",fenqiMoney));//分期总金额
        returnMap.put("allMoney",String.format("%.2f",Double.valueOf(allMoney)));//分期总金额
        returnMap.put("downPayMoney",String.format("%.2f",Double.valueOf(downPayMoney)));//首付金额
        returnMap.put("monthPay",String.format("%.2f",monthPay));//每月还款额
        returnMap.put("periods",String.format("%.0f",periods));//分期期数
        returnMap.put("merchandiseBrandName",merchandiseBrandName);//商品品牌
        returnMap.put("merchandiseModelName",merchandiseModelName);//商品型号
        returnMap.put("merchandiseVersionName",merchandiseVersionName);//商品版本
        returnMap.put("base64String",base64String);//商品版本
        returnMap.put("flag",true);
        return returnMap;
    }
    /**
     * 获得输入流
     * @param url
     * @return
     */
    private static InputStream getIns(String url) {
        InputStream is = null;
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            http.connect();
            // 获取文件转化为byte流
            is = http.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            return is;
        }
        return is;
    }
    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
