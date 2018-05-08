package com.zw.miaofuspd.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.base.util.AverageCapitalPlusInterestUtils;
import com.base.util.DateUtils;
import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.merchant.service.MerchandiseService;
import com.zw.miaofuspd.facade.merchant.service.MerchantService;
import com.zw.miaofuspd.facade.order.service.AppImageService;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.facade.ratescheme.service.RateSchemeService;
import com.zw.miaofuspd.facade.serpackage.service.SerPackageService;
import com.zw.util.UploadFileUtil;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/11/27 0027.
 */
@Controller
@RequestMapping("/order")
public class OrderController extends AbsBaseController {
    @Autowired
    private AppOrderService appOrderService;
    @Autowired
    private MerchandiseService merchandiseService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private RateSchemeService rateSchemeService;
    @Autowired
    private SerPackageService serPackageService;
    @Autowired
    private AppImageService appImageService;
    @Autowired
    private ISystemDictService iSystemDictService;

    /**
     * 扫描完二维码-获取产品的信息
     * @return
     */
    @RequestMapping("/getMerdiseInfoStage")
    @ResponseBody
    public ResultVO getMerdiseInfoStage(String merchantId,String productId,String empId,
                                        String allMoney,String downPayMoney,String merchandiseId,String idJson,String createTime) throws Exception{
        ResultVO resultVO = new ResultVO();
        String now = DateUtils.getDateString(new Date());
        if ((Double.parseDouble(now)-Double.parseDouble(createTime))>600){
            resultVO.setErrorMsg(VOConst.FAIL,"该二维码已无效!");
            return resultVO;
        }
        Map returnMap = new HashMap();
        //获取服务包信息
        List serPackageList = serPackageService.getSerPackageInfoByIds(idJson);
        //获取产品信息
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
        //获取商户的name
        Map merchantMap = merchantService.getMerchantById(merchantId);
        /*******************************配置自然月和自定义天数的计算*********************************************/
        Map productMap =rateSchemeService.getFeeInfo(productId);
        double periods = Double.valueOf(productMap.get("periods").toString());
        double fenqiMoney = Double.valueOf(allMoney)- Double.valueOf(downPayMoney);//分期总金额
        double rate= com.base.util.StringUtils.toDouble(productMap.get("monthRate")) ;
        Map typeMap=rateSchemeService.getProductInfo(productId);
        String diyType=String.valueOf(typeMap.get("diyType"));
        if(StringUtils.isEmpty(diyType)){
            diyType="0";
        }
        //算出每期的还款金额
        String Money=String.valueOf(fenqiMoney/periods);
        String intNumber = Money.substring(0,Money.indexOf("."));
        double intMoney=Double.valueOf(intNumber);
        double monthPay =0.00;
        if("0".equals(diyType)){
            //自然月的费用获取产品的费率和期数信息
            double monthRate = Double.valueOf(rate) / 100;
            monthPay =intMoney+ fenqiMoney*monthRate;
        }else{   //自定义天数获取产品的费率和期数信息
            int diyDays=Integer.valueOf(typeMap.get("diyDays").toString()); //自定义天数
            double dayRate=Double.valueOf(rate)*12/365/100 ; //日利息
            monthPay =intMoney+ fenqiMoney*dayRate*diyDays;
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
       // double monthPay =(fenqiMoney/periods)+fenqiMoney*monthRate;
       /* double monthPay = AverageCapitalPlusInterestUtils.getPerMonthPrincipalInterest(fenqiMoney,monthRate,periods);*/
        returnMap.put("merchantName",merchantMap.get("merName"));
        returnMap.put("allMoney",String.format("%.2f",Double.valueOf(allMoney)));//商品总价
        returnMap.put("downPayMoney",String.format("%.2f",Double.valueOf(downPayMoney)));//首付金额
        returnMap.put("fenqiMoney",String.format("%.2f",fenqiMoney));//分期总金额
        returnMap.put("monthPay",String.format("%.2f",monthPay));//每月还款额
        returnMap.put("periods",String.format("%.0f",periods));//期数
        returnMap.put("idJson",idJson);//每月还款额
        returnMap.put("empId",empId);//业务员工号Id
        returnMap.put("merchandiseBrandName",merchandiseBrandName);//商品品牌
        returnMap.put("merchandiseModelName",merchandiseModelName);//商品型号
        returnMap.put("merchandiseVersionName",merchandiseVersionName);//商品版本
        returnMap.put("merchandiseUrl",merchandiseUrl);//商品缩略图
        if(serPackageList.size()==0){
            returnMap.put("servicePagNum","0");//获取服务包的数量
        }else{
            returnMap.put("servicePagNum",idJson.split(",").length);//获取服务包的数量
          }
        returnMap.put("serPackageList",serPackageList);//服务包集合
        resultVO.setRetData(returnMap);
        return resultVO;
    }
    /**
     * 分期订单详情-获取分期订单详情信息
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping("/getFenqiOrderInfo")
    @ResponseBody
    public ResultVO getFenqiOrderInfo(String orderId) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = appOrderService.getFenqiOrderInfo(orderId);
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     * 获取该用户下所有的订单
     * @return
     */
    @RequestMapping("/getAllPersonOrder")
    @ResponseBody
    public ResultVO getAllPersonOrder(){
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo)getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
//        根据userId获取该用户下所有的订单
//        Map map  = appOrderService.getAllPersonOrder("9d673527-590f-4df2-b52c-3024bebb4a09");
        Map map = appOrderService.getAllPersonOrder(userInfo.getCustomer_id());
        resultVO.setRetData(map);

        return resultVO;
    }



    /**
     * 获取该用户下所有的订单
     * 专用于我的分期申请
     * @return
     */
    @RequestMapping("/getAllPersonOrder1")
    @ResponseBody
    public ResultVO getAllPersonOrder1(){
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo)getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
//        根据userId获取该用户下所有的订单
//       Map map  = appOrderService.getAllPersonOrder1("c0c824ab-49d0-44c6-aa35-45492acf6e67");
        Map map = appOrderService.getAllPersonOrder(userInfo.getCustomer_id());
        resultVO.setRetData(map);

        return resultVO;
    }




    /**
     * 根据orderId获取该订单下所有信息
     * @return
     */
    @RequestMapping("/getOrderInfoById")
    @ResponseBody
    public ResultVO getOrderInfoById(String orderId){
        ResultVO resultVO = new ResultVO();
        //根据orderId获取该订单下所有信息
        Map map  = appOrderService.getOrderById(orderId);
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     * 根据orderId修改订单状态
     * @param orderId
     * @param state
     * @return
     */
    @RequestMapping("/updateOrderState")
    @ResponseBody
    public ResultVO updateOrderState(String orderId,String state) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = appOrderService.updateOrderState(orderId,state);
        if (!(boolean)map.get("flag")){
            resultVO.setRetCode(VOConst.FAIL);
        }
        return resultVO;
    }
    /**
     * 扫描二维码生成订单
     * @param data
     * @return
     */
    @RequestMapping("/addOrder")
    @ResponseBody
    public ResultVO addOrder(String data) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = JSONObject.parseObject(data);
        //创建订单需要的信息存入orderMap中
        Map orderMap = new HashMap();
        //扫描二维码获得的参数
        String empId = map.get("empId").toString();
        orderMap.put("empId",empId);
        String allMoney = map.get("allMoney").toString();
        orderMap.put("allMoney",allMoney);
        String downPayMoney = map.get("downPayMoney").toString();
        orderMap.put("downPayMoney",downPayMoney);
        String merchantId = map.get("merchantId").toString();
        String productId = map.get("productId").toString();
        String idJson = map.get("idJson").toString();
        String merchandiseId = map.get("merchandiseId").toString();
        //获取用户信息
        AppUserInfo userInfo = (AppUserInfo)getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        orderMap.put("userId",userInfo.getId());
        orderMap.put("tel",userInfo.getTel());
        orderMap.put("customerId",userInfo.getCustomer_id());
        orderMap.put("customerName",userInfo.getName());
        orderMap.put("sexName",userInfo.getSex_name());
        orderMap.put("card",userInfo.getCard());
        //获取商户信息
        Map merchantMap = merchantService.getMerchantById(merchantId);
        orderMap.put("merchantId",merchantMap.get("id"));
        orderMap.put("merName",merchantMap.get("merName"));
        //获取商品信息
        Map merchandisMap = merchandiseService.getMerchandiseInfoById(merchandiseId);
        orderMap.put("merchandiseVersionId",merchandisMap.get("merchandiseVersionId"));
        orderMap.put("merchandiseVersionName",merchandisMap.get("merchandiseVersionName"));
        orderMap.put("merchandiseModelId",merchandisMap.get("merchandiseModelId"));
        orderMap.put("merchandiseModelName",merchandisMap.get("merchandiseModelName"));
        orderMap.put("merchandisBrandId",merchandisMap.get("merchandisBrandId"));
        orderMap.put("merchandiseBrandName",merchandisMap.get("merchandiseBrandName"));
        String orderTypeValue = map.get("orderTypeValue").toString();
        orderMap.put("orderTypeValue",orderTypeValue);
      /*  //获取利率信息
        Map rateMap = rateSchemeService.getFeeInfo(productId);
        orderMap.put("periods",rateMap.get("periods"));
        String nowTime = DateUtils.getDateString(new Date());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        calendar.setTime(sdf.parse(nowTime));
        calendar.add(Calendar.MONTH,+Integer.parseInt(rateMap.get("periods").toString()));
        String expirationTime = sdf.format(calendar.getTime());//到期时间
        //获取产品的费率和期数信息
        double monthRate=Double.valueOf(rateMap.get("monthRate").toString())/100 ;
        int periods = Integer.valueOf(orderMap.get("periods").toString());//总期数
        double fenqiMoney = Double.valueOf(allMoney)- Double.valueOf(downPayMoney);//分期总金额--放款总金额
        //每期应还利息
        double lixi=fenqiMoney*monthRate;
      *//*  Map<Integer,Double> feeMap = AverageCapitalPlusInterestUtils.getPerMonthInterest(fenqiMoney,monthRate*12,periods);*//*
        DecimalFormat df = new DecimalFormat("#.00");
        Double allFee = 0.0;*/
      /*  for(int i=0;i<periods;i++) {
            String fee = df.format(lixi);//利息
            allFee += Double.valueOf(fee);
        }*/

        /***********************************配置自然月和自定义天数的计算******************************************************************/
        //获取利率信息
        //12	24	2.0000	0.0658	0.067  商品贷	动感地带2
        Map rateMap = rateSchemeService.getFeeInfo(productId);
        orderMap.put("periods",rateMap.get("periods"));
        String nowTime = DateUtils.getDateString(new Date());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        calendar.setTime(sdf.parse(nowTime));
        //获取产品分期天数的方式和天数
        Map typeMap=rateSchemeService.getProductInfo(productId);
        String diyType=String.valueOf(typeMap.get("diyType"));
        if(StringUtils.isEmpty(diyType)){
            diyType="0";
        }
        String expirationTime="";
        String rate="";
        String diyDay="";
        if("0".equals(diyType)){
            //自然月的费用获取产品的费率和期数信息
            double monthRate=Double.valueOf(rateMap.get("monthRate").toString())/100 ;
            int periods = Integer.valueOf(orderMap.get("periods").toString());//总期数
            double fenqiMoney = Double.valueOf(allMoney)- Double.valueOf(downPayMoney);//分期总金额--放款总金额
            //每期应还利息
            double lixi=fenqiMoney*monthRate;
            Double allFee = 0.0;
            orderMap.put("allFee",String.format("%.2f",fenqiMoney*monthRate*periods));
            calendar.add(Calendar.MONTH,+Integer.parseInt(rateMap.get("periods").toString()));
            expirationTime = sdf.format(calendar.getTime());//到期时间
            rate=rateMap.get("monthRate").toString();
        }else{   //自定义天数获取产品的费率和期数信息
            diyDay=typeMap.get("diyDays").toString();
            int diyDays=Integer.valueOf(diyDay); //自定义天数
            double dayRate=Double.valueOf(rateMap.get("monthRate").toString())*12/365/100 ; //日利息
            int periods = Integer.valueOf(orderMap.get("periods").toString());//总期数
            double fenqiMoney = Double.valueOf(allMoney)- Double.valueOf(downPayMoney);//分期总金额--放款总金额
            //每期应还利息
            double lixi=fenqiMoney*dayRate*diyDays;
            Double allFee = 0.0;
            orderMap.put("allFee",String.format("%.2f",lixi*periods));
            int days=(Integer.parseInt(rateMap.get("periods").toString()))*diyDays;
            calendar.add(Calendar.DATE, +days);
            expirationTime = sdf.format(calendar.getTime());//到期时间
            rate=rateMap.get("monthRate").toString();
        }
        orderMap.put("diy_days",diyDay);
        orderMap.put("diy_type",diyType);
        orderMap.put("expirationTime",expirationTime);
        orderMap.put("rate",rate);
        orderMap.put("yuqiFee",rateMap.get("yuqiFee"));
        orderMap.put("proTypeName",rateMap.get("proTypeName"));
        orderMap.put("proNameName",rateMap.get("proNameName"));
        //生成订单
        Map orderResultMap = appOrderService.addOrder(orderMap);
        if (!(boolean)orderResultMap.get("flag")){
            resultVO.setErrorMsg(VOConst.FAIL,orderResultMap.get("msg").toString());
            return resultVO;
        }
        resultVO.setRetData(orderResultMap);
        String orderId = orderResultMap.get("orderId").toString();
        //设置该订单对应的服务包
        serPackageService.setSerPackageInfoByIds(orderId,idJson);
        return resultVO;
    }


    /**
     * 图片查询,当type为0时传merchandiseId,当type不为0时传orderId
     * @param id
     * @param type
     * @return
     */
    @RequestMapping("/getImages")
    @ResponseBody
    public ResultVO getImages(String id,String type){
        ResultVO resultVO = new ResultVO();
        List list = appImageService.getImagesByType(id,type);
        resultVO.setRetData(list);
        return resultVO;
    }

    /**
     * 客户合影,发货,客户手持身份证,合同,商品图片上传
     * @param customerId
     * @param id
     * @param type
     * @return
     */
    @RequestMapping("/addImage")
    @ResponseBody
    public ResultVO addImage(String customerId,String id,String type) throws Exception{
        ResultVO resultVO = new ResultVO();
        //获取图片路径
        String imgPath = getHttpSession().getServletContext().getRealPath("/fintecher_file");
        //生成新的文件名
        String uuid = UUID.randomUUID().toString();
        String url = "image";
        //获得图片路劲
        String catalog="fintecher_file"+"/"+url+"/";
        String bucket = iSystemDictService.getInfo("oss.bucket");
        String endpoint = iSystemDictService.getInfo("oss.endpoint");
        String accessKeyId = iSystemDictService.getInfo("oss.accessKeyId");
        String accessKeySecret = iSystemDictService.getInfo("oss.accessKeySecret");
        Map<String, Object> map = UploadFileUtil.getFileOSS(getRequest(), imgPath+ File.separator + url, uuid,catalog,bucket,endpoint,accessKeyId,accessKeySecret);
        List list = (List)map.get("fileList");
        Map map1 = (Map)list.get(0);
        //获取第一张图片存储在阿里云上的地址
        String imagUrl =map1.get("Name").toString();
        resultVO.setRetData(appImageService.addImageByType(customerId,id,type,imagUrl));
        return resultVO;
    }


    /**
     * 通过imgUrl删除图片
     * @param url
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteImage")
    @ResponseBody
    public ResultVO deleteImage(String url) throws Exception{
        ResultVO resultVO = new ResultVO();
        String bucket = iSystemDictService.getInfo("oss.bucket");
        String endpoint = iSystemDictService.getInfo("oss.endpoint");
        String accessKeyId = iSystemDictService.getInfo("oss.accessKeyId");
        String accessKeySecret = iSystemDictService.getInfo("oss.accessKeySecret");
        UploadFileUtil.deleteFile(bucket,url,endpoint,accessKeyId,accessKeySecret);
        resultVO.setRetCode(VOConst.SUCCESS);
        resultVO.setRetMsg("图片删除成功!");
        return resultVO;
    }


    /**
     * 查询当月当期是否已结清
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping("/checkNowMonthPay")
    @ResponseBody
    public ResultVO checkNowMonthPay(String orderId) throws Exception{
        ResultVO resultVO = new ResultVO();
        String state=appOrderService.checkNowMonthPay(orderId);
//        resultVO.setRetCode(state);
        resultVO.setRetData(state);
        return resultVO;
    }

}
