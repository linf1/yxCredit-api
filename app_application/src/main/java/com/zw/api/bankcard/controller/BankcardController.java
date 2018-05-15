package com.zw.api.bankcard.controller;

import com.api.model.bankcard.BankcardRequest;
import com.api.model.common.BYXResponse;
import com.api.service.bankcard.IBankcardServer;
import com.base.util.GeneratePrimaryKeyUtils;
import com.base.util.StringUtils;
import com.zw.app.util.AppConstant;
import com.zw.web.base.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * 银行四要素
 * @author 陈清玉
 */
@RestController
@RequestMapping("/bankcard")
public class BankcardController {
    @Autowired
    private IBankcardServer bankcardServer;

    private final Logger LOGGER = LoggerFactory.getLogger(BankcardController.class);

    /**
     * 银行卡四要素认证发送短信
     * @param bankcardRequest 银行四要素请求参数
     * @return 成功失败
     */
    @PostMapping("/authsms")
    public ResultVO authsms(HttpServletRequest request,BankcardRequest bankcardRequest) {
        if(bankcardRequest == null){
            return ResultVO.error("参数异常");
        }
        final String orderNum = "O" + GeneratePrimaryKeyUtils.getSnowflakeKey();
        final String orderNo = "N" + GeneratePrimaryKeyUtils.getSnowflakeKey();
        bankcardRequest.setMerchantNeqNo(orderNo);
        bankcardRequest.setMerchantOrder(orderNum);
        request.getSession().setAttribute(AppConstant.MERCHANT_ORDER,orderNum);
        request.getSession().setAttribute(AppConstant.MERCHANT_NEQNO,orderNo);
        try {
            final BYXResponse authsms = bankcardServer.authsms(bankcardRequest);
            if (BYXResponse.resCode.success.getCode().equals(authsms.getRes_code())) {
                return  ResultVO.ok(authsms.getRes_data());
            }
            return ResultVO.error(authsms.getRes_msg());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage());
        }
    }

    @GetMapping("/authconfirm/{smsCode}")
    public ResultVO authconfirm(HttpServletRequest request,@PathVariable String smsCode) {
        try {
                if (StringUtils.isEmpty(smsCode)) {
                    return ResultVO.error("参数异常");
                }
                BankcardRequest bankcardRequest  = new BankcardRequest();
                bankcardRequest.setSmsCode(smsCode);
                final HttpSession session = request.getSession();
                bankcardRequest.setMerchantOrder((String)session.getAttribute(AppConstant.MERCHANT_ORDER));
                bankcardRequest.setMerchantNeqNo((String)session.getAttribute(AppConstant.MERCHANT_NEQNO));
                final BYXResponse authsms = bankcardServer.authconfirm(bankcardRequest);
                if (BYXResponse.resCode.success.getCode().equals(authsms.getRes_code())) {
                    return ResultVO.ok(authsms.getRes_data());
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

}
