
package com.zw.miaofuspd.contractConfirmation.controller;

import com.api.model.contractsign.ByxUserModel;
import com.api.model.contractsign.ContractSignRequest;
import com.api.model.contractsign.ContractSignResponse;
import com.api.model.contractsign.ResData;
import com.api.service.contractsign.IContractSignService;
import com.base.util.AppRouterSettings;
import com.google.gson.Gson;
import com.zw.api.ds.controller.AssetController;
import com.zw.miaofuspd.facade.contractConfirmation.service.ContractConfirmationService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.util.ContextToPdf;
import com.zw.util.PdfToHtml;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@RequestMapping(AppRouterSettings.VERSION+"/contractConfirmation")
public class ContractConfirmationController extends AbsBaseController {
    private static Logger logger = LoggerFactory.getLogger(ContractConfirmationController.class);
    public static final String TOP_TYPE = "ThirdPartner:ContractConfirmationController:";

    @Autowired
    private ContractConfirmationService contractConfirmationService;

    @Autowired
    private ISystemDictService iSystemDictService;

    @Autowired
    private IContractSignService iContractSignService;

    @Autowired
    private AssetController assetController;

    /*****************************碧有信合同模块开始***************************/

    /**
     * 生成未签订合同
     *
     * @param orderId
     * @return
     */
    public ResultVO generateContract(String orderId, HttpServletRequest request) throws Exception {
        ResultVO resultVO = new ResultVO();
        Map map = contractConfirmationService.getContractInfo(orderId);


        //获取合同内容并将对应信息填入合同中
        //填入合同1 借款协议
        map.put("template_name","碧有信借款协议");
        Map loanMap1 = contractConfirmationService.getContractAgreement(map);
        //生成合同1
        //文件名
        String fileName1 = orderId + "_jiekuanxieyi.pdf";
        String pdfUrl1=generatePdf(request,loanMap1,fileName1);
        map.put("pdfUrl",pdfUrl1);
        contractConfirmationService.insertContract(map);

        //填入合同2 居间协议
        map.put("template_name","碧有信居间服务协议");
        Map loanMap2 = contractConfirmationService.getContractAgreement(map);
        //生成合同2
        String fileName2 = orderId + "_jujianfuwuxieyi.pdf";
        String pdfUrl2=generatePdf(request,loanMap2,fileName2);
        map.put("pdfUrl",pdfUrl2);
        contractConfirmationService.insertContract(map);

        Map mapPdf = new HashMap();
        mapPdf.put("pdfUrl1", pdfUrl1);
        mapPdf.put("pdfUrl2", pdfUrl2);
        mapPdf.put("orderId", map.get("orderId").toString());

        resultVO.setRetData(mapPdf);

        return resultVO;
    }

    public String generatePdf(HttpServletRequest request, Map loanMap, String fileName) throws Exception{
        Map returnMap=new HashMap();
        //获取根目录
        String root = iSystemDictService.getInfo("file.path");
        //文件名
        String url = "/" + "loan_agreement_pdf" + "/" + fileName;
        //先创建文件夹，避免fileOutputStream报错
        File file = new File(root + File.separator + "loan_agreement_pdf");
        if (!file.exists()) {
            file.mkdirs();
        }

        loanMap.put("url", root + url);
        try {
            //转换成pdf
            ContextToPdf.insertPDF(loanMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
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
        List<Map> contracts = contractConfirmationService.getContractByOrderId(orderId);
        if(contracts==null||contracts.size()==0){
            this.generateContract(orderId, request);
        }
        contracts = contractConfirmationService.getContractByOrderId(orderId);
        Map map = new HashMap();
        String contractId1=contracts.get(0).get("id").toString();
        String contractId2=contracts.get(1).get("id").toString();
        String contractName1=contracts.get(0).get("contract_name").toString().trim();
        if("碧有信借款协议".equals(contractName1)){
            map.put("contractId1", contractId1);
            map.put("contractId2", contractId2);
        }else{
            map.put("contractId1", contractId2);
            map.put("contractId2", contractId1);
        }

        resultVO.setRetData(map);
        return resultVO;
    }

    @RequestMapping("/previewContract")
    @ResponseBody
    public ResultVO previewContract(String contractId, HttpServletRequest request) throws Exception {
     /*  orderId="80d8949a-ac68-4c7b-a65e-0bdb5672f50d";*/
        ResultVO resultVO = new ResultVO();

        Map contract = contractConfirmationService.getContractById(contractId);

        String host = iSystemDictService.getInfo("contract.host");
        String root = iSystemDictService.getInfo("file.path");
        String url=contract.get("contract_src").toString();

//        if(contract.get("contract_no")!=null&&!"".equals(contract.get("contract_no"))){
//            url=url.replace(".pdf","_signed.pdf");
//        }

        String htmlUrl=url.replace(".pdf", ".html");

        File contractHtmlFile=new File(root+htmlUrl);
        if(!contractHtmlFile.exists()){
            PdfToHtml.PdfToImage(new File(root+url), host, url);
        }

        Map map = new HashMap();
        map.put("pdfUrl", htmlUrl);
        map.put("host", host);
        resultVO.setRetData(map);
        return resultVO;
    }

    /**
     * 放弃签约更新订单状态
     *
     * @param
     * @return
     */
    @RequestMapping("/getupSignContract")
    @ResponseBody
    public ResultVO getupContract(String orderId, HttpServletRequest request) throws Exception{
        Map params=new HashMap();
        params.put("orderId", orderId);
        params.put("empId",request.getParameter("empId"));
        params.put("empName",request.getParameter("empName"));
        params.put("amount",request.getParameter("amount"));
        params.put("operationResult", 6);
        params.put("orderState", 9);
        params.put("description", request.getParameter("description"));
        contractConfirmationService.updateOrderStatus(params);

        return ResultVO.ok(params);
    }

    /**
     * 确认签约更新订单状态
     *
     * @param
     * @return
     */
    @RequestMapping("/signContract")
    @ResponseBody
    public ResultVO signContract(String orderId, HttpServletRequest request) throws Exception{
        List<Map> contracts = contractConfirmationService.getContractByOrderId(orderId);

        String root = iSystemDictService.getInfo("file.path");
        String host = iSystemDictService.getInfo("contract.host");

        Map contract1=null;
        Map contract2=null;
        if("碧有信借款协议".equals(contracts.get(0).get("contract_name").toString().trim())){
            contract1=contracts.get(0);
            contract2=contracts.get(1);
        }else{
            contract1=contracts.get(1);
            contract2=contracts.get(0);
        }

        //签章1
        String pdfUrl1=contract1.get("contract_src").toString();
        Map map1=signSingleContract(contract1, "借款协议", root+pdfUrl1);
        if("0".equals(map1.get("res_code"))){
            return ResultVO.error(map1.get("res_msg").toString());
        }
        PdfToHtml.PdfToImage(new File(root+pdfUrl1), host, pdfUrl1);
        //签章2
        String pdfUrl2=contract2.get("contract_src").toString();
        Map map2=signSingleContract(contract2, "居间服务协议", root+pdfUrl2);
        if("0".equals(map2.get("res_code"))){
            return ResultVO.error(map2.get("res_msg").toString());
        }
        PdfToHtml.PdfToImage(new File(root+pdfUrl2), host, pdfUrl2);

        Map map=new HashMap();
        map.put("result1",map1);
        map.put("result2",map2);

        String customerId=request.getParameter("empId");
        String customerName=request.getParameter("empName");
        String amount=contract1.get("contract_amount").toString();
        String contractNo=contract1.get("contract_no").toString();
        String description=request.getParameter("description");
        Map params=new HashMap();
        params.put("orderId", orderId);
        params.put("empId",customerId);
        params.put("empName",customerName);
        params.put("amount",amount);
        params.put("operationResult", 5);
        params.put("orderState", 4);
        params.put("description", description);
        params.put("contract_no", contractNo);
        contractConfirmationService.updateOrderStatus(params);

        ResultVO resultVO = assetController.thirdAssetsReceiver(orderId,customerId);
        if("SUCCESS".equals(resultVO.getRetCode())){
            contractConfirmationService.updateAssetStatus(orderId,"1");
        }

        return ResultVO.ok(map);
    }

    private Map signSingleContract(Map contract, String title, String pdfPath) throws Exception{
        Map returnMap=new HashMap();

        ContractSignRequest contractSignRequest = new ContractSignRequest();

        //电子签章需要参数
        contractSignRequest.setIsOutSign(0);//外部系统调用
        if("借款协议".equals(title)){
            contractSignRequest.setRealTemplateId("3f4072ea-73fa-4df3-8b37-986e89abde7a");
        }else if("居间服务协议".equals(title)){
            contractSignRequest.setRealTemplateId("f86a9ff0-51c8-47aa-b61d-7a74d0dfccd7");
        }

        contractSignRequest.setContractTitle(title);

        List<ByxUserModel> userModelList = new ArrayList<ByxUserModel>();
        contractSignRequest.setUserModelList(userModelList);

        /**甲方**/
        ByxUserModel byxUserModel = new ByxUserModel();
        byxUserModel.setPersonArea(0);
        byxUserModel.setPersonIdType(ByxUserModel.IDTYPE_ID);
        byxUserModel.setPersonIdValue(contract.get("card").toString());
        byxUserModel.setPersonName(contract.get("person_name").toString());
        //甲方
        byxUserModel.setSignatory(0);
        //个人
        byxUserModel.setUserType(ByxUserModel.USERTYPE_PERSON);
        //关键字定位
        byxUserModel.setPosType(ByxUserModel.POS_TYPE_KEY);

        userModelList.add(byxUserModel);

        if("居间服务协议".equals(title)){
            contractSignRequest.setRealTemplateId("f86a9ff0-51c8-47aa-b61d-7a74d0dfccd7");
            /**乙方**/
            ByxUserModel byxUserModel2 = new ByxUserModel();
//            byxUserModel2.setDefaultCompany();
            byxUserModel2.setOrganizeName("华惠金服信息科技（北京）有限公司");
            byxUserModel2.setOrganizeType(0);
            byxUserModel2.setOrganizeUserType(2);
            byxUserModel2.setOrganizeOrganCode("MA004T5A-1");
            byxUserModel2.setOrganizeCreditCode("91110107MA004T5A1G");

            byxUserModel2.setOrganizeLegalArea(0);
            byxUserModel2.setOrganizeLegalIdno("430903198605190359");
            byxUserModel2.setOrganizeLegalName("周煜琛");

            //乙方
            byxUserModel2.setSignatory(1);
            //企业
            byxUserModel2.setUserType(ByxUserModel.USERTYPE_COMPANY);
            //关键字定位
            byxUserModel2.setPosType(ByxUserModel.POS_TYPE_KEY);

            userModelList.add(byxUserModel2);
        }

        contractSignRequest.setUnsignPath(pdfPath);

        ContractSignResponse response=iContractSignService.signContract(contractSignRequest);

        System.out.println("$$$$$doElecSign:code:"+response.getRes_code()+";msg:"+response.getRes_msg());

        String res_code=response.getRes_code();
        String res_msg=response.getRes_msg();
        /**签署成功**/
        if("1".equals(res_code)){
            String resDataStr = response.getRes_data();
            Gson gson=new Gson();
            ResData resData = gson.fromJson(resDataStr, ResData.class);
            byte[] signedStream=resData.getSignedStream();
            String pathname =  pdfPath;;
            File signFile = new File(pathname );
            FileUtils.writeByteArrayToFile(signFile , signedStream);

        }else{
            /**签署失败**/
            logger.info(TOP_TYPE + "失败编码:res_code:"+res_code);
            logger.info(TOP_TYPE + "失败的原因:res_msg:"+res_msg);

        }
        returnMap.put("res_code",res_code);
        returnMap.put("res_msg",res_msg);
        return returnMap;
    }

}


