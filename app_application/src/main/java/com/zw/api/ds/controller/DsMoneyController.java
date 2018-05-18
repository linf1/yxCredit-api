package com.zw.api.ds.controller;

import com.api.model.common.BYXResponse;
import com.api.model.ds.DSMoneyRequest;
import com.api.service.ds.IDSMoneyServer;
import com.base.util.AppRouterSettings;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * 借款人及放款账户数据同步控制器
 * @author 陈清玉
 */
@RestController
@RequestMapping(AppRouterSettings.VERSION +AppRouterSettings.API_MODULE + "/ds")
public class DsMoneyController {

    @Autowired
    private IDSMoneyServer idsMoneyServer;

    @Autowired
    private AppBasicInfoService appBasicInfoServiceImpl;

    /**
     *
     * @param request
     * @return
     */
    @PostMapping("/dsBorrowerAndAccountCard")
    public ResultVO dsBorrowerAndAccountCard(DSMoneyRequest request) {
        final Map customer = appBasicInfoServiceImpl.findById(request.getBorrowerThirdId());
        if (customer != null) {
            request.setBorrowerType(0);
            request.setBorrowerCardType("1");
            request.setBorrowerName(customer.get("PERSON_NAME").toString());
            request.setBorrowerMobilePhone(customer.get("TEL").toString());
            request.setBorrowerChannel("yxd");
            request.setBorrowerCardNo(customer.get("CARD").toString());
            request.setAddress(customer.get("company_address").toString());
            request.setAccountType("0");
            request.setAccountName(customer.get("PERSON_NAME").toString());
            request.setAccountIdCard(customer.get("CARD").toString());
            request.setOtherFlag("1");
            request.setAccountChannel("yxd");
            request.setAccountThirdId(request.getBorrowerThirdId());
            try {
                BYXResponse  byxResponse = idsMoneyServer.saveBorrowerAndAccountCard(request);
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
