package com.zw.api.bankcard.controller;

import com.api.model.bankcard.BankcardRequest;
import com.api.model.common.BYXResponse;
import com.api.service.bankcard.IBankcardServer;
import com.base.util.AppRouterSettings;
import com.base.util.GeneratePrimaryKeyUtils;
import com.base.util.StringUtils;
import com.constants.ApiConstants;
import com.zw.web.base.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * 银行四要素
 * @author 陈清玉
 */
@RestController
@RequestMapping(AppRouterSettings.VERSION + "/bankcard")
public class BankcardController {
    @Autowired
    private IBankcardServer bankcardServer;

    private final Logger LOGGER = LoggerFactory.getLogger(BankcardController.class);

    /**
     * 银行卡四要素认证发送短信
     * @param bankcardRequest 银行四要素请求参数
     *                         cardId	必要	string	身份证号（加密）
     *                         realName	必要	string	真实姓名（加密）
     *                         bankCardNo	必要	string	银行卡号必传（加密）
     *                         reservePhone	必要	string	预留手机号（加密）
     *                         userId	非必要	string	用户ID，有则传（加密）
     * @return 成功失败
     */
    @PostMapping("/authsms")
    public ResultVO authsms(BankcardRequest bankcardRequest) {
        if(bankcardRequest == null){
            return ResultVO.error("参数异常");
        }
        LOGGER.info("进入银行卡四要素认证发送短信,参数为：{}",bankcardRequest.toString());
        final String orderNum = "O" + GeneratePrimaryKeyUtils.getSnowflakeKey();
        final String orderNo = "N" + GeneratePrimaryKeyUtils.getSnowflakeKey();
        bankcardRequest.setMerchantNeqNo(orderNo);
        bankcardRequest.setMerchantOrder(orderNum);
        try {
            final BYXResponse authsms = bankcardServer.authsms(bankcardRequest);
            if (BYXResponse.resCode.success.getCode().equals(authsms.getRes_code())) {
                bankcardServer.saveBankcard(bankcardRequest);
                Object resData = authsms.getRes_data();
                if(resData != null){
                    Map resDataMap = (Map) resData;
                    if(ApiConstants.AUTH_SEND_STATUS_SUCCESS.equals(resDataMap.get("status").toString())){
                        return ResultVO.ok(resDataMap.get("msg").toString(),null);
                    }else{
                        return  ResultVO.error(resDataMap.get("msg").toString());
                    }
                }
            }
            return ResultVO.error(authsms.getRes_msg());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage());
        }
    }

    /**
     * 银行卡四要素认证短信确认
     * @param bankcardRequest 接口请求参数对象
     *            smsCode    必要	string	短信验证码  （加密）
     *            cardId	必要	string	身份证号（加密）
     *            bankCardNo	必要	string	银行卡号必传（加密）
     * @return
     */
    @PostMapping("/authconfirm")
    public ResultVO authconfirm(BankcardRequest bankcardRequest) {
        try {
            if (bankcardRequest == null) {
                return ResultVO.error("参数异常");
            }
            LOGGER.info("进入银行卡四要素认证短信确认,参数为：{}",bankcardRequest.toString());
            final List bankcardList = bankcardServer.findBankcard(bankcardRequest);
            if(CollectionUtils.isEmpty(bankcardList)){
                return ResultVO.error("验证码过期");
            }
            final Map o = (Map)bankcardList.get(0);
            bankcardRequest.setMerchantOrder(o.get("merchant_order").toString());
            bankcardRequest.setMerchantNeqNo(o.get("merchant_neq_no").toString());
            final BYXResponse authsms = bankcardServer.authconfirm(bankcardRequest);
                if (BYXResponse.resCode.success.getCode().equals(authsms.getRes_code())) {
                    Object resData = authsms.getRes_data();
                    if(resData != null){
                        Map resDataMap = (Map) resData;
                       if(ApiConstants.AUTH_FIRM_STATUS_SUCCESS.equals(resDataMap.get("status").toString())){
                           bankcardServer.updateState(bankcardRequest);
                           return ResultVO.ok(resDataMap.get("msg").toString(),null);
                       }else{
                           return ResultVO.error(resDataMap.get("msg").toString());
                       }
                    }
                }
                return ResultVO.error(authsms.getRes_msg());
            } catch (Exception e) {
                e.printStackTrace();
                return ResultVO.error(e.getMessage());
            }
    }


    /**
     * 获取银行列表
     * @return 营业银行json字符串
     */
    @PostMapping("/getBankList")
    public ResultVO getBankList() {
        try {
            final BYXResponse byxResponse = bankcardServer.getBankList();
            if (BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
                return ResultVO.ok(byxResponse.getRes_data());
            }
            return ResultVO.error(byxResponse.getRes_msg());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage());
        }
    }
    /**
     * 获取银行列表
     * @param regionId 市id
     * @param bankCode 银行行别代码
     * @return 支营业银行列表json字符串
     */
    @PostMapping("/getSubBankList/{regionId}/{bankCode}")
    public ResultVO getSubBankList(@PathVariable String regionId,@PathVariable String bankCode) {
        try {
            final BYXResponse subBankList = bankcardServer.getSubBankList(regionId, bankCode);
            if (BYXResponse.resCode.success.getCode().equals(subBankList.getRes_code())) {
                return ResultVO.ok(subBankList.getRes_data());
            }
            return ResultVO.error(subBankList.getRes_msg());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage());
        }
    }
    /**
     * 获取城市列表
     * @param provinceId 省ID
     * @return 城市列表json字符串
     * @exception  IOException 请求异常
     */
    @PostMapping("/getCityList/{provinceId}")
    public ResultVO getCityList(@PathVariable String provinceId) {
        try {
            final BYXResponse cityList = bankcardServer.getCityList(provinceId);
            if (BYXResponse.resCode.success.getCode().equals(cityList.getRes_code())) {
                return ResultVO.ok(cityList.getRes_data());
            }
            return ResultVO.error(cityList.getRes_msg());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage());
        }
    }
    /**
     * 获取省列表
     * @return 省列表json字符串
     */
    @PostMapping("/getProvinceList")
    public ResultVO getProvinceList(){
        try {
            final BYXResponse provinceList = bankcardServer.getProvinceList();
            if (BYXResponse.resCode.success.getCode().equals(provinceList.getRes_code())) {
                return ResultVO.ok(provinceList.getRes_data());
            }
            return ResultVO.error(provinceList.getRes_msg());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage());
        }
    }


    /**
     * 实名认证时检查银行卡是否进行过实名认证
     * @param bankcardRequest 银行卡请求参数
     *                         cardId	string	身份证号
     * @return 成功：可以进行实名认证 失败：不可进行实名认证
     */
    @GetMapping("/isRealName")
    public ResultVO isRealName(BankcardRequest bankcardRequest){
        if(bankcardRequest == null){
            return ResultVO.error("参数异常");
        }
        if(StringUtils.isBlank(bankcardRequest.getCardId())) {
            return ResultVO.error("缺少参数");
        }
        LOGGER.info("进入实名认证时检查银行卡是否进行过实名认证,参数为：{}",bankcardRequest.toString());
        List<Map> bankCardList = bankcardServer.findSysBankCardInfo(bankcardRequest);
        if(!CollectionUtils.isEmpty(bankCardList)){
            return ResultVO.error("该银行卡用户已被实名认证");
        }
        return ResultVO.ok("该银行卡未被实名认证",null);
    }

}
