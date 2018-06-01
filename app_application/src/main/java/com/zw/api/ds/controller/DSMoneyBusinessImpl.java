package com.zw.api.ds.controller;

import com.api.model.common.BYXResponse;
import com.api.model.ds.DSMoneyRequest;
import com.api.service.bankcard.IBankcardServer;
import com.api.service.ds.IDSMoneyBusiness;
import com.api.service.ds.IDSMoneyServer;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.miaofuspd.facade.personal.service.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 借款人及放款账户数据同步而业务层实现
 * @author 陈清玉
 */
@Component(IDSMoneyBusiness.BEAN_KEY)
public class DSMoneyBusinessImpl implements IDSMoneyBusiness {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private AppBasicInfoService appBasicInfoService;

    @Autowired
    private IDSMoneyServer idsMoneyServer ;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private IBankcardServer bankcardServer;

    @Override
    public BYXResponse syncData(String orderId) {
        try {
            final Map customer = appBasicInfoService.findByOrderId(orderId);
            if (customer != null) {
                if( customer.get("syncUserId") != null  &&  customer.get("syncAccountId") != null ){
                    LOGGER.info("------借款人及放款账户数据已经同步过了------");
                    return BYXResponse.ok();
                }
                String custId = customer.get("custId").toString();
                DSMoneyRequest request = new DSMoneyRequest();
                request.setBorrowerType(0);
                request.setBorrowerCardType("1");
                //app登录手机号码
                request.setBorrowerUserName(appUserService.getPhoneById(customer.get("userId").toString()));
                request.setBorrowerName(customer.get("custName").toString());
                request.setBorrowerMobilePhone(customer.get("custPhone").toString());
                request.setBorrowerThirdId(custId);
                request.setBorrowerChannel("YXD");
                request.setBorrowerCardNo(customer.get("custCard").toString());
                request.setAddress(customer.get("jobAddress").toString());
                request.setAccountType("0");
                request.setAccountName(customer.get("custName").toString());
                request.setAccountIdCard(customer.get("custCard").toString());
                request.setAccountChannel("YXD");
                request.setAccountThirdId(custId);
                //获取银行卡信息
                setBankInfo(custId, request);
                BYXResponse byxResponse = idsMoneyServer.saveBorrowerAndAccountCard(request);
                //数据同步接口一个相同的实名只会同步一次，所以不用判断是否成功
                if(byxResponse != null) {
                    final Map resData = (Map) byxResponse.getRes_data();
                    if(resData != null) {
                        if (BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
                            //数据同步更新个人信息到数据库
                            appBasicInfoService.updateSynById(
                                    resData.get("userId").toString(),
                                    resData.get("accountId").toString(),
                                    custId
                            );
                        }
                        LOGGER.info("------借款人及放款账户数据同步更新个人信息到数据库成功------");
                       return BYXResponse.ok();
                    }
                }else{
                    LOGGER.info("------借款人及放款账户数据同步更新个人信息到数据库失败------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
           return BYXResponse.error(e.getMessage());
        }
        return BYXResponse.error();
    }

    /**
     * 获取银行卡信息 并返回数据
     * @param custId 个人信息ID
     * @param request 请求数据实体
     */
    private void setBankInfo(String custId, DSMoneyRequest request) {
        Map sysBankcardInfoMap = bankcardServer.findSysBankcardInfoByCustId(custId);
        if(sysBankcardInfoMap == null){
         LOGGER.info("----银行卡信息为录入同步数据失败----");
            return;
        }
        request.setProvinceCode(String.valueOf(sysBankcardInfoMap.get("prov_id")));
        request.setProvinceName(sysBankcardInfoMap.get("prov_name").toString());
        request.setCityCode(String.valueOf(sysBankcardInfoMap.get("city_id")));
        request.setCityName(sysBankcardInfoMap.get("city_name").toString());
        request.setBankCode(String.valueOf(sysBankcardInfoMap.get("bank_number")));
        request.setOtherFlag(getOtherFlag(sysBankcardInfoMap.get("bank_number").toString()));
        request.setBankName(sysBankcardInfoMap.get("bank_name").toString());
        request.setBankCardNo(sysBankcardInfoMap.get("card_number").toString());
        request.setBankBranchName(sysBankcardInfoMap.get("bank_subbranch").toString());
        request.setCnapsCode(sysBankcardInfoMap.get("bank_subbranch_id").toString());
    }

    /**
     * 根据银行code判断是否是他行（0：浙商；1：非浙商）
     * @param bankCode 银行code
     * @return （0：浙商；1：非浙商）
     */
    private String getOtherFlag(String bankCode){
        String code = "316";
        return code.equals(bankCode) ? "0" : "1";
    }
}
