package com.zw.api.ds.controller;

import com.api.model.common.BYXResponse;
import com.api.model.ds.AssetRequest;
import com.api.model.ds.DSMoneyRequest;
import com.api.service.ds.IAssetServer;
import com.api.service.ds.IDSMoneyServer;
import com.base.util.AppRouterSettings;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * 第三方资产接口同步控制器
 * @author 韩梅生
 */
@RestController
@RequestMapping(AppRouterSettings.VERSION + "/asset")
public class AssetController {

    @Autowired
    private IAssetServer iassetServer;

    @Autowired
    private AppBasicInfoService appBasicInfoServiceImpl;

    /**
     *
     * @param
     * @return
     */
    @PostMapping("/thirdAssetsReceiver")
    public ResultVO thirdAssetsReceiver(String orderId,String customerId,String contractorName) {
        //final Map resultMap = appBasicInfoServiceImpl.getOrderDetailById(request.getOrderId(),request.getCustomerId(),request.getContractorName());
        final Map resultMap = appBasicInfoServiceImpl.getOrderDetailById(orderId,customerId,contractorName);
        AssetRequest request = new AssetRequest();
        Map orderMap = (Map) resultMap.get("orderMap");
        Map customerMap = (Map) resultMap.get("customerMap");
        Map bankMap = (Map) resultMap.get("bankMap");
        if (!CollectionUtils.isEmpty(resultMap)) {
              request.setAssetName(orderMap.get("product_name_name") == null? "" : orderMap.get("product_name_name").toString());
              request.setAssetAmount(orderMap.get("loan_amount") == null?"":orderMap.get("loan_amount").toString());
              request.setAssetFinanceCost(orderMap.get("assetFinanceCost") == null?"":orderMap.get("assetFinanceCost").toString());
              request.setAssetServiceRate(orderMap.get("assetServiceRate") == null?"":orderMap.get("assetServiceRate").toString());
              request.setAssetBorrowTimeLimit(orderMap.get("periods") == null?"":orderMap.get("periods").toString());
              request.setAssetBorrowTimeType("1");
              request.setAssetBorrowCreateTime(orderMap.get("applay_time") == null?"":orderMap.get("applay_time").toString());
              request.setAssetBorrowApplication(orderMap.get("loan_purpose") == null?"":orderMap.get("loan_purpose").toString());
              request.setAssetPrepayment(orderMap.get("repayment") == null?"":orderMap.get("repayment").toString());
              request.setAssetPrepaymentTimeLimit(orderMap.get("repayment_days") == null?"":orderMap.get("repayment_days").toString());
              request.setAssetPrepaymentTimeType("1");
              request.setUserId(customerMap.get("sync_user_id") == null?"":customerMap.get("sync_user_id").toString());
              request.setAssetPersonIdcard(orderMap.get("card") == null?"":orderMap.get("card").toString());
              request.setAssetPersonMobilePhone(orderMap.get("tel") == null?"":orderMap.get("tel").toString());
              request.setAssetPersonAddress(customerMap.get("company_address") == null?"":customerMap.get("company_address").toString());
              request.setByxBankId(customerMap.get("sync_account_id") == null?"":customerMap.get("sync_account_id").toString());
              request.setAssetBankUserName(bankMap.get("cust_name") == null?"":bankMap.get("cust_name").toString());
              request.setAssetBankName(bankMap.get("bank_name") == null?"":bankMap.get("bank_name").toString());
              request.setAssetBankCnapsCode(bankMap.get("bank_subbranch_id") == null?"":bankMap.get("bank_subbranch_id").toString());
              request.setAssetBankNo(bankMap.get("card_number") == null?"":bankMap.get("card_number").toString());
              request.setAssetBankAddress(bankMap.get("prov_name").toString()+bankMap.get("city_name"));
              request.setAssetLoanCard(bankMap.get("card") == null?"":bankMap.get("card").toString());
              request.setAssetGuarantorName(contractorName);
              request.setContractId("4123421");
              request.setServiceContractId("4234242");
              request.setThirdAssetOrderNum(orderMap.get("order_no") == null?"1":orderMap.get("order_no").toString());
              request.setAssetCustomerName(orderMap.get("customer_name") == null?"1":orderMap.get("customer_name").toString());

            try {
                BYXResponse  byxResponse = iassetServer.thirdAssetsReceiver(request);
                if (BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
                    return ResultVO.ok(byxResponse.getRes_data());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResultVO.error();
   }

}
