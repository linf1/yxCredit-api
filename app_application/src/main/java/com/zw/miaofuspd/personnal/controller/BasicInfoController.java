package com.zw.miaofuspd.personnal.controller;

import com.alibaba.fastjson.JSONObject;
import com.api.model.common.BYXResponse;
import com.api.model.ds.DSMoneyRequest;
import com.api.service.ds.IDSMoneyServer;
import com.base.util.AppRouterSettings;
import com.base.util.DateUtils;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.miaofuspd.facade.user.service.IUserService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private IUserService userService;

    @Autowired
    private AppBasicInfoService appBasicInfoServiceImpl;

    @Autowired
    private IDSMoneyServer idsMoneyServer;

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
    public ResultVO getRealName(String customerId) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = appBasicInfoService.getRealName(customerId);
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
        DSMoneyRequest request = new DSMoneyRequest();
        try {
            final Map customer = appBasicInfoServiceImpl.findById((String) map.get("customerId"));
            if (customer != null) {
                request.setBorrowerType(0);
                request.setBorrowerCardType("1");
                //app登录手机号码
                request.setBorrowerUserName((String) map.get("phone"));
                request.setBorrowerName(customer.get("PERSON_NAME").toString());
                request.setBorrowerMobilePhone(customer.get("TEL").toString());
                request.setBorrowerThirdId((String) map.get("customerId"));
                request.setBorrowerChannel("yxd");
                request.setBorrowerCardNo(customer.get("CARD").toString());
                request.setAddress(customer.get("company_address").toString());
                request.setAccountType("0");
                request.setAccountName(customer.get("PERSON_NAME").toString());
                request.setAccountIdCard(customer.get("CARD").toString());
                request.setOtherFlag("1");
                request.setAccountChannel("yxd");
                request.setAccountThirdId(map.get("customerId").toString());
                request.setProvinceCode(String.valueOf(map.get("prov_id")));
                request.setProvinceName(map.get("prov_name").toString());
                request.setCityCode(String.valueOf(map.get("city_id")));
                request.setCityName(map.get("city_name").toString());
                request.setBankCode(String.valueOf(map.get("bank_number")));
                request.setBankName(map.get("bank_name").toString());
                request.setBankCardNo(map.get("card_number").toString());
                request.setBankBranchName(map.get("bank_subbranch").toString());
                request.setCnapsCode(map.get("bank_subbranch_id").toString());
                BYXResponse byxResponse = idsMoneyServer.saveBorrowerAndAccountCard(request);
                if (BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
                    final Map resData = (Map) byxResponse.getRes_data();
                    //数据同步更新个人信息到数据库
                    appBasicInfoService.updateSynById(
                             resData.get("userId").toString(),
                             resData.get("accountId").toString(),
                             map.get("customerId").toString()
                            );
                    LOGGER.info("------借款人及放款账户数据同步更新个人信息到数据库成功------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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


}
