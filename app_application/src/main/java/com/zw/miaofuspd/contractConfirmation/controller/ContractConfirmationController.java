
package com.zw.miaofuspd.contractConfirmation.controller;

import com.api.model.common.BYXResponse;
import com.api.model.contractsign.ContractSignRequest;
import com.api.model.contractsign.ByxUserModel;
import com.api.service.contractsign.IContractSignService;
import com.junziqian.service.JunziqianService;
import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.contractConfirmation.service.ContractConfirmationService;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.facade.user.service.IMsgService;
import com.zw.util.ContextToPdf;
import com.zw.util.UploadFileUtil;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
/**
 * <strong>Title : <br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2018年03月12日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:Win7 <br>
 *         email: <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */


@Controller
@RequestMapping("/contractConfirmation")
public class ContractConfirmationController extends AbsBaseController {
    private static Logger logger = LoggerFactory.getLogger(ContractConfirmationController.class);
    public static final String TOP_TYPE = "ThirdPartner:ContractConfirmationController:";

    @Autowired
    private ContractConfirmationService contractConfirmationService;

    @Autowired
    private JunziqianService junziqianService;

    @Autowired
    private IDictService iDictService;
    @Autowired
    private IMsgService iMsgService;
    @Autowired
    private ISystemDictService iSystemDictService;

    @Autowired
    private AppOrderService appOrderService;

    @Autowired
    private IContractSignService iContractSignService;

    /**
     * 获取未签订合同
     *
     * @param request
     * @return
     */

    @RequestMapping("/getContractGreement")
    @ResponseBody
    public ResultVO getLoanAgreement(HttpServletRequest request) throws Exception {
     /*   ModelAndView modelAndView = new ModelAndView("merch-wechat/contract/contract");*/
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) this.getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        //获取合同所需信息
      /*  String id="6e78634a-9301-40b9-86c1-2312ae60c57c";*/
        Map map = contractConfirmationService.getContractUserInfo(userInfo.getId());
        try {
            String cjName = iDictService.getDictInfo("出借人", "cjrxm");
            String cjCard = iDictService.getDictInfo("出借人", "cjsfz");
            //防止合同底部签名变形
            while (cjName.length() < 8) {
                cjName += " ";
            }
            map.put("cjName", cjName);
            map.put("cjCard", cjCard);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取合同内容并将对应信息填入合同中
        Map loanMap = contractConfirmationService.getContractAgreement(map);
        //获取根目录
        String root = request.getSession().getServletContext().getRealPath("/loan_agreement");
        //文件名
        String filename = UUID.randomUUID().toString() + ".pdf";
        String url = "/" + "loan_agreement_pdf" + "/" + filename;
        //先创建文件夹，避免fileOutputStream报错
        File file = new File(root + File.separator + "loan_agreement_pdf");
        if (!file.exists()) {
            file.mkdirs();
        }

        loanMap.put("url", root + url);
        Map mapPdf = new HashMap();
        mapPdf.put("pdfUrl", "/loan_agreement" + url);
        mapPdf.put("orderId", map.get("orderId").toString());
       /* String host = iSystemDictService.getInfo("contract.host");
        mapPdf.put("host", host);*/
        try {
            //转换成pdf
            ContextToPdf.insertPDF(loanMap);
            resultVO.setRetData(mapPdf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("pdfUrl","/loan_agreement" + url);
//        String value=loanMap.get("value").toString();
//        map.put("value",value);
//
       /* modelAndView.addObject("info",map);*/

        return resultVO;
    }



/**
     * 获取签订合同结果
     *
     * @param
     * @return
     */

    @RequestMapping("/getLoanGreementResult")
    @ResponseBody
    public ResultVO getLoanAgreementResult(String pdfPath, String orderId, HttpServletRequest request) {
        ResultVO resultVO = new ResultVO();
        Map map = new HashMap();
        AppUserInfo userInfo = (AppUserInfo) this.getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
      /*  String id="de783ec5-2ce0-4829-830f-7c702695239c";*/
       map.put("userId",userInfo.getId());
      //  map.put("userId",id);
       /*  pdfPath="/loan_agreement/loan_agreement_pdf/52e06f43-a947-4fb0-ac1b-4b13ba27afe6.pdf";
         orderId="547fd38b-9bef-4bc8-bb24-73150f5671cd";*/
        Map accoutMap = contractConfirmationService.getContractUserInfo(userInfo.getId());
        String pdfUrl;
        //获取根目录
        String root = request.getSession().getServletContext().getRealPath("");
        try{
            String cusName= accoutMap.get("cusName").toString();
            String cusCard=accoutMap.get("cusCard").toString();
            String tel=accoutMap.get("TEL").toString();
            //甲方签章信息
            String email=accoutMap.get("email").toString();
            String company_name=accoutMap.get("company_name").toString();
            String phone=accoutMap.get("tel").toString();
            String number=accoutMap.get("num").toString();
           /* String cjName = iDictService.getDictInfo("出借人","cjrxm");
            String cjCard = iDictService.getDictInfo("出借人","cjsfz");
            map.put("cjName",cjName);
            map.put("cjCard",cjCard);*/
            map.put("cusName",cusName);
            map.put("cusCard",cusCard);
            map.put("tel",tel);
            map.put("email",email);
            map.put("company_name",company_name);
            map.put("phone",phone);
            map.put("num",number);
            map.put("pdfPath",root+pdfPath);
            String contractNo = junziqianService.printJunziqian(map);
           /* accoutMap.put("contract", contractNo); //合同确认后生成合同编号,替换模板中的编号字段
            Map loanMap = contractConfirmationService.getContractAgreement(accoutMap);
            String root1 = request.getSession().getServletContext().getRealPath("/loan_agreement");
            //文件名
            File filePdf1 = new File(root+ pdfPath);
            filePdf1.delete();
            String filename = UUID.randomUUID().toString() + ".pdf";
            String url = "/" + "loan_agreement_pdf" + "/" + filename;
            //先创建文件夹，避免fileOutputStream报错
            File file1 = new File(root1+ File.separator + "loan_agreement_pdf");
            if (!file1.exists()) {
                file1.mkdirs();
            }
            loanMap.put("url", root1 + url);
            ContextToPdf.insertPDF(loanMap);  //更新合同编号*/
            //打印电子签章后将之前的pdf删除
            File filePdf = new File(root+pdfPath);
            filePdf.delete();
            if (contractNo == null){
                resultVO.setRetCode("1");
                resultVO.setRetMsg("打印电子签章失败!");
                return resultVO;
            }
            map.put("contractNo",contractNo);
            //电子签章结束立即获取,电子签章还在初始化,这里延时一秒
            int num = 0;
            do {
                pdfUrl = junziqianService.getJunziqian(map);
                Thread.sleep(500);
                num++;
                if (pdfUrl != null){
                    break;
                }
            }while(num < 20);
//            String pdfUrl;
//            do {
//                pdfUrl = junziqianService.getJunziqian(map);
//            }while(pdfUrl == null);
            //先创建文件夹，避免fileOutputStream报错
            File saveDir = new File(root+"/loan_agreement" + File.separator + "loan_agreement_pdf_Result");
            //文件名
            String fileName = contractNo+ ".pdf";
            if (!saveDir.exists()){
                saveDir.mkdirs();
            }
            //获得输入流
            InputStream is = getIns(pdfUrl);
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
            //获得头像路劲
            String bucket = iSystemDictService.getInfo("oss.bucket");
            String endpoint = iSystemDictService.getInfo("oss.endpoint");
            String accessKeyId = iSystemDictService.getInfo("oss.accessKeyId");
            String accessKeySecret = iSystemDictService.getInfo("oss.accessKeySecret");
            //合同上传到oss 并获取地址
            Map uploadMap = UploadFileUtil.wechatPDFToOSS( file,bucket,endpoint,accessKeyId,accessKeySecret);
            List list = (List) uploadMap.get("fileList");
            Map map1 = (Map) list.get(0);
            String nameUrl = map1.get("Name").toString();
            contractConfirmationService.setAlreadyContractAgreement(orderId,nameUrl);
            Map mapResult = new HashMap();
            iMsgService.insertOrderMsg("10", orderId);
            appOrderService.updateOrderStateBeforeSubmit(orderId,"16.7");
            mapResult.put("contractNo", contractNo);
            resultVO.setRetData(mapResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }



/**
     * 通过orderId 获取已签约协议
     *
     * @param orderId
     * @param request
     * @return
     */

    @RequestMapping("/getAlreadyLoanGreementResult")
    @ResponseBody
    public ResultVO getAlreadyLoanGreementResult(String orderId, HttpServletRequest request) throws Exception {
     /*  orderId="80d8949a-ac68-4c7b-a65e-0bdb5672f50d";*/
        ResultVO resultVO = new ResultVO();
        //获取根目录
        String contractNo = contractConfirmationService.getAlreadyContractAgreement(orderId);
        String pdfUrl = "/loan_agreement/loan_agreement_pdf_Result/" + contractNo + ".pdf";
        String host = iSystemDictService.getInfo("contract.host");
        Map map = new HashMap();
        map.put("pdfUrl", pdfUrl);
        map.put("host", host);
        resultVO.setRetData(map);
        return resultVO;
    }


/**
     * 删除已签约或未签约的合同
     *
     * @param pdfPath
     * @param request
     */

    @RequestMapping("/deleteLoanGreement")
    @ResponseBody
    public void deleteLoanGreement(String pdfPath, HttpServletRequest request) {
        //获取根目录
        String root = request.getSession().getServletContext().getRealPath("/loan_agreement");
        //实际文件路径
        pdfPath = root + pdfPath;
        File file = new File(pdfPath);
        if (file.delete()) {
            System.out.println("删除单个文件" + pdfPath + "成功！");
        }
    }


/**
     * 获得输入流
     *
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
     *
     * @param inputStream
     * @return
     * @throws IOException
     */

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


/**
     * 跳转至合同展示页
     *
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "/goToContract", method = RequestMethod.GET)
    public String goToContract() throws Exception {
        return "cash-wechat/centerMsg/contract";
    }


    /*****************************碧有信合同模块开始***************************/

    /**
     * 生成未签订合同
     *
     * @param request
     * @return
     */
    @RequestMapping("/generateContract")
    @ResponseBody
    public ResultVO generateContract(HttpServletRequest request) throws Exception {
        ResultVO resultVO = new ResultVO();
        String orderId=request.getParameter("orderId");
        Map map = contractConfirmationService.getContractInfo(orderId);
        try {
            String cjName = iDictService.getDictInfo("出借人", "cjrxm");
            String cjCard = iDictService.getDictInfo("出借人", "cjsfz");
            //防止合同底部签名变形
            while (cjName.length() < 8) {
                cjName += " ";
            }
            map.put("cjName", cjName);
            map.put("cjCard", cjCard);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取合同内容并将对应信息填入合同中
        Map loanMap = contractConfirmationService.getContractAgreement(map);
        //获取根目录
        String root = request.getSession().getServletContext().getRealPath("/loan_agreement");
        //文件名
        String filename = UUID.randomUUID().toString() + ".pdf";
        String url = "/" + "loan_agreement_pdf" + "/" + filename;
        //先创建文件夹，避免fileOutputStream报错
        File file = new File(root + File.separator + "loan_agreement_pdf");
        if (!file.exists()) {
            file.mkdirs();
        }

        loanMap.put("url", root + url);
        Map mapPdf = new HashMap();
        mapPdf.put("pdfUrl", "/loan_agreement" + url);
        mapPdf.put("orderId", map.get("orderId").toString());
       /* String host = iSystemDictService.getInfo("contract.host");
        mapPdf.put("host", host);*/
        try {
            //转换成pdf
            ContextToPdf.insertPDF(loanMap);
            resultVO.setRetData(mapPdf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("pdfUrl","/loan_agreement" + url);
//        String value=loanMap.get("value").toString();
//        map.put("value",value);
//
       /* modelAndView.addObject("info",map);*/
        contractConfirmationService.insertContract(map);

        return resultVO;
    }

    /**
     * 通过orderId 获取合同
     *
     * @param orderId
     * @param request
     * @return
     */

    @RequestMapping("/getContract")
    @ResponseBody
    public ResultVO getContract(String orderId, HttpServletRequest request) throws Exception {
     /*  orderId="80d8949a-ac68-4c7b-a65e-0bdb5672f50d";*/
        ResultVO resultVO = new ResultVO();
        //获取根目录
        String contractSrc = contractConfirmationService.getContractByOrderId(orderId);
        String host = iSystemDictService.getInfo("contract.host");
        Map map = new HashMap();
        map.put("pdfUrl", contractSrc);
        map.put("host", host);
        resultVO.setRetData(map);
        return resultVO;
    }


    /**
     * 生成签订合同
     *
     * @param
     * @return
     */
    @RequestMapping("/signContract")
    @ResponseBody
    public ResultVO signContract() throws Exception{
        ResultVO resultVO = new ResultVO();

        //电子签章需要参数
        ContractSignRequest contractSignRequest = new ContractSignRequest();
        contractSignRequest.setIsOutSign(0);//外部系统调用
        contractSignRequest.setRealTemplateId("666666");
        contractSignRequest.setContractTitle("20180428测试合同模板");

        List<ByxUserModel> userModelList = new ArrayList<ByxUserModel>();
        contractSignRequest.setUserModelList(userModelList);

        ByxUserModel byxUserModel = new ByxUserModel();
        byxUserModel.setPersonArea(0);
        byxUserModel.setPersonIdType(ByxUserModel.IDTYPE_ID);
        byxUserModel.setPersonIdValue("410622198211180181");
        byxUserModel.setPersonName("张乐");
        //甲方
        byxUserModel.setSignatory(0);
        //个人
        byxUserModel.setUserType(ByxUserModel.USERTYPE_PERSON);
        //关键字定位
        byxUserModel.setPosType(ByxUserModel.POS_TYPE_KEY);
        userModelList.add(byxUserModel);

        String pdfPath = "/Users/van/work/zwkj/byx/test.pdf";//此路径为test.pdf文件本地存放的路径。
        contractSignRequest.setUnsignPath(pdfPath);

        BYXResponse response=iContractSignService.signContract(contractSignRequest);

        System.out.println("$$$$$doElecSign:code:"+response.getRes_code()+";msg:"+response.getRes_msg());

        String res_code=response.getRes_code();
        String res_msg=response.getRes_msg();
        /**签署成功**/
        if("1".equals(res_code)){
//            try {
//                response.getRes_data();
//                ResData resData = gson.fromJson(jsonEncodeString, ResData.class);
//                /**解密后的文件流**/
//                byte[] signedStream = resData.getSignedStream();
//                String orderNo = resData.getOrderNo();
//                /**订单编号**/
//                logger.info(TOP_TYPE + "签署成功:orderNo:"+orderNo);
//                /**测试输出文件是否成功**/
//                String pathname = "e:"+File.separator+"elecsign4out"+File.separator+"signedDir"+File.separator+"sign06.pdf";
//                File signFile = new File(pathname );
//                FileUtils.writeByteArrayToFile(signFile , signedStream);
//            } catch (Exception e) {
//                /**解析结果异常**/
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                logger.error(TOP_TYPE+":解析结果异常:",e);
//            }
        }else{
            /**签署失败**/
            logger.info(TOP_TYPE + "失败编码:res_code:"+res_code);
            logger.info(TOP_TYPE + "失败的原因:res_msg:"+res_msg);

//            if(StringUtils.isNotBlank(jsonEncodeString)){
//                ResData resData = gson.fromJson(jsonEncodeString, ResData.class);
//                String orderNo = resData.getOrderNo();
//                logger.info(TOP_TYPE + "失败订单编号为:orderNo:"+orderNo);
//            }
        }
        resultVO.setRetCode(res_code);
        resultVO.setRetMsg(res_msg);
        return resultVO;
    }

}


