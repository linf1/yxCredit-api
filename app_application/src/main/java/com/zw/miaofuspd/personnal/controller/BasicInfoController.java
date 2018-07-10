package com.zw.miaofuspd.personnal.controller;

import com.alibaba.fastjson.JSONObject;
import com.api.model.common.BYXResponse;
import com.api.model.ds.PFLoanRequest;
import com.api.service.ds.IDSMoneyServer;
import com.api.service.ds.IPFLoanServer;
import com.base.util.AppRouterSettings;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by 韩梅生 on 2018/05/09 0028.
 */
@RestController
@RequestMapping(AppRouterSettings.VERSION+AppRouterSettings.BASIC_MODUAL)
public class BasicInfoController extends AbsBaseController {

    private final  Logger LOGGER =  LoggerFactory.getLogger(BasicInfoController.class);

    @Autowired
    AppBasicInfoService appBasicInfoService;

    @Autowired
    private AppBasicInfoService appBasicInfoServiceImpl;


    @Autowired
    private IPFLoanServer ipfLoanServer;



    /**
     * @author 韩梅生
     * 保存用户申请的基本信息
     * @param data
     * @return
     */
    @RequestMapping(value = "/addApplyInfo")
    public ResultVO addApplyInfo(String data) throws Exception{
        Map map = JSONObject.parseObject(data);
        ResultVO resultVO = new ResultVO();
        Map resultMap = appBasicInfoService.addApplyInfo(map);
        if(!(Boolean)(resultMap.get("flag"))){
            resultVO.setErrorMsg(VOConst.FAIL,(String)(resultMap.get("msg")));
        }
        resultVO.setRetData(resultMap);
        return resultVO;
    }

    /**
     * @author 韩梅生
     * 获取用户申请的基本信息
     * @param
     * @return
     */
    @RequestMapping(value = "/getApplyInfo")
    public ResultVO getApplyInfo(String orderId) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = appBasicInfoService.getApplyInfo(orderId);
        resultVO.setRetData(map);
        return resultVO;
    }

    /**
     * @author 韩梅生
     * @Description  保存用户的个人信息
     * @Date 14:07 2018/5/12
     * @param
     * @return
     */
    @RequestMapping("/addBasicInfo")
    public ResultVO addBasicInfo(String data) throws Exception{
        Map map = JSONObject.parseObject(data);
        ResultVO resultVO = new ResultVO();
        Map resultMap = appBasicInfoService.addBasicInfo(map);
        if(!(Boolean)(resultMap.get("flag"))){
            resultVO.setErrorMsg(VOConst.FAIL,(String)(resultMap.get("msg")));
        }
        resultVO.setRetData(resultMap);
        return resultVO;
    }




    /**
     * @author:韩梅生
     * @Description 获取用户的个人信息
     * @Date 14:06 2018/5/12
     * @param
     * @return
     */
    @RequestMapping("/getBasicInfo")
    public ResultVO getBasicInfo(String customerId) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = appBasicInfoService.getBasicInfo(customerId);
        if(!(Boolean)(map.get("flag"))){
            resultVO.setErrorMsg(VOConst.FAIL,(String)(map.get("msg")));
            return resultVO;
        }
        resultVO.setRetData(map.get("data"));
        return resultVO;
    }


    /**
     * @author:韩梅生
     * @Description  获取省份信息
     * @Date 13:40 2018/5/12
     * @param
     * @return com.zw.web.base.vo.ResultVO
     */
    @RequestMapping("/getProvinceList")
    public ResultVO getProvinceList() throws Exception{
        ResultVO resultVO = new ResultVO();
        List list = appBasicInfoService.getProvinceList();
        resultVO.setRetData(list);
        return resultVO;
    }

    /**
     * @author 韩梅生
     * @date 15:38 2018/5/21
     * 获取市列表
     */

    @RequestMapping("/getCityList")
    public ResultVO getCityList(String provinceId) throws Exception{
        ResultVO resultVO = new ResultVO();
        List list = appBasicInfoService.getCityList(provinceId);
        resultVO.setRetData(list);
        return resultVO;
    }

    /**
     * @author:韩梅生
     * @Description  获取区的信息
     * @Date 13:54 2018/5/12
     * @param  cityId 市的id
     * @return
     */
    @RequestMapping("/getDistrictList")
    public ResultVO getDistrictList(String cityId) throws Exception{
        ResultVO resultVO = new ResultVO();
        List list = appBasicInfoService.getDistrictList(cityId);
        resultVO.setRetData(list);
        return resultVO;
    }

    /**
     * @author:韩梅生
     * @Description 获取实名认证信息
     * @Date 17:58 2018/5/16
     * @param
     */
    @RequestMapping("/getRealName")
    public ResultVO getRealName(String userId) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = appBasicInfoService.getRealName(userId);
        resultVO.setRetData(map);
        return resultVO;
    }


    /**
     * @author:韩梅生
     * @Description 保存实名认证信息
     * @Date 17:58 2018/5/16
     * @param
     */
    @RequestMapping("/saveRealName")
    public ResultVO saveRealName(String data) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = JSONObject.parseObject(data);
        Map resMap = appBasicInfoService.saveRealName(map);
        resultVO.setRetData(resMap);
        return resultVO;
    }

    /**
     * @author 韩梅生
     * @date 18:45 2018/5/23
     * 获取实名认证信息
     */
    @RequestMapping("/getAuthorStatus")
    public ResultVO getAuthorStatus(String userId) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = appBasicInfoService.getAuthorStatus(userId);
        resultVO.setRetData(map);
        return resultVO;
    }

    /**
     * @author 韩梅生
     * @date 11:17 2018/6/6
     * 获取申请期限
     */
    @RequestMapping("/getPeriods")
    @ResponseBody
    public  ResultVO getPeriods(String productName) throws  Exception{
        ResultVO resultVO = new ResultVO();
        List list = appBasicInfoService.getPeriods(productName);
        resultVO.setRetData(list);
        return resultVO;
    }

    /**
     * @author 韩梅生
     * @date 11:17 2018/6/6
     * 新增银行卡信息
     */
    @RequestMapping("/addBankInfo")
    @ResponseBody
    public  ResultVO addBankInfo(String data) throws  Exception{
        //ResultVO resultVO = new ResultVO();
        Map map = JSONObject.parseObject(data);
        ResultVO  resultVO = saveAccoutCard(map);
        if(VOConst.SUCCESS.equals(resultVO.getRetCode())){
            Map map1= JSONObject.parseObject(resultVO.getRetData().toString());
            map.put("accountId",map1.get("accountId"));
            Map resultMap = appBasicInfoService.addBankInfo(map);
            if(!(Boolean)resultMap.get("flag")){
                resultVO.setErrorMsg(VOConst.FAIL,(String)(resultMap.get("msg")));
                return resultVO;
            }
            resultVO.setRetMsg(resultMap.get("msg").toString());
        }
        return resultVO;
    }
    /**
     * @author 韩梅生
     * @date 11:17 2018/6/6
     * 获取银行卡信息
     */
    @RequestMapping("/getBankInfo")
    @ResponseBody
    public  ResultVO getBankInfo(String userId) throws  Exception{
        ResultVO resultVO = new ResultVO();
        List<Map> bankInfo = appBasicInfoService.getBankInfo(userId);
        resultVO.setRetData(bankInfo);
        return resultVO;
    }

    /**
     * 保存放款账户.
     * @author  韩梅生
     */
    private ResultVO saveAccoutCard(Map map){
        PFLoanRequest request = new PFLoanRequest();
        String customerId = appBasicInfoService.getCustomerIdByid(map.get("userId").toString()).get(0).get("id").toString();
        request.setAccountChannel("YXD");
        request.setAccountIdCard(map.get("card") == null ? "" : map.get("card").toString());
        request.setAccountName(map.get("cust_name") == null ? "" : map.get("cust_name").toString());
        request.setAccountThirdId(customerId);
        request.setAccountType(map.get("bank_type") == null ? "" : map.get("bank_type").toString());
        request.setBankBranchName(map.get("bank_subbranch") == null ? "" : map.get("bank_subbranch").toString());
        request.setBankCardNo(map.get("card_number") == null ? "" : map.get("card_number").toString());
        request.setBankCode(map.get("bank_number") == null ? "" : map.get("bank_number").toString());
        request.setBankName(map.get("bank_name") == null ? "" : map.get("bank_name").toString());
        request.setBorrowerThirdId(customerId);
        request.setCityCode(map.get("city_id") == null ? "" : map.get("city_id").toString());
        request.setCityName(map.get("city_name") == null ? "" : map.get("city_name").toString());
        request.setCnapsCode(map.get("bank_subbranch_id") == null ? "" : map.get("bank_subbranch_id").toString());
        request.setProvinceCode(map.get("prov_id") == null ? "" : map.get("prov_id").toString());
        request.setProvinceName(map.get("prov_name") == null ? "" : map.get("prov_name").toString());
        //request.setOtherFlag(getOtherFlag(map.get("bank_number").toString()));
        request.setUserBorrowerId(Long.valueOf(appBasicInfoService.getUserBorrowerId(map.get("userId").toString())));
        try {
            BYXResponse byxResponse = ipfLoanServer.saveLoanMoney(request);
            if (BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
                return ResultVO.ok(byxResponse.getRes_data());
            }
            return ResultVO.error(byxResponse.getRes_msg());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultVO.error();
    }


}
