package com.zw.miaofuspd.api.ds;

import com.api.model.BYXSettings;
import com.api.model.common.BYXRequest;
import com.api.model.common.BYXResponse;
import com.api.model.ds.DSMoneyRequest;
import com.api.model.ds.DSMoneySettings;
import com.api.service.ds.IDSMoneyServer;
import com.zw.api.HttpClientUtil;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 借款人及放款账户数据同步服务实现
 * @author 陈清玉
 */
@Service(IDSMoneyServer.BEAN_KEY)
public class DSMoneyServerImpl implements IDSMoneyServer {

    @Autowired
    private DSMoneySettings dsMoneySettings;

    @Autowired
    private AppBasicInfoService appBasicInfoService;

    @Autowired
    private BYXSettings byxSettings;

    @Override
    public BYXResponse saveBorrowerAndAccountCard(DSMoneyRequest request) throws Exception {
        final Map customer = appBasicInfoService.findById(request.getBorrowerThirdId());
        if(customer != null){
            request.setBorrowerType(0);
            request.setBorrowerCardType("1");
            request.setBorrowerName(customer.get("person_name").toString());
            request.setBorrowerMobilePhone(customer.get("tel").toString());
            request.setBorrowerCardNo(customer.get("card").toString());
            request.setAddress(customer.get("company_address").toString());
            request.setAccountType("0");
            request.setAccountName(customer.get("person_name").toString());
            request.setAccountIdCard(customer.get("card").toString());
            request.setOtherFlag("1");
            request.setOtherFlag("yxd");
            request.setAccountIdCard(request.getBorrowerThirdId());
            final String result = HttpClientUtil.post(dsMoneySettings.getRequestUrl(),BYXRequest.getBYXRequest(request, byxSettings), byxSettings.getHeadRequest());
            return BYXResponse.getBYXResponse(result, byxSettings);
        }
        return  BYXResponse.error();
    }
}
