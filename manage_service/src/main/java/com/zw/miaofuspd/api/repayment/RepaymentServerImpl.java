package com.zw.miaofuspd.api.repayment;

import com.api.model.BYXSettings;
import com.api.model.common.BYXRequest;
import com.api.model.common.BYXResponse;
import com.api.model.repayment.LoanSettings;
import com.api.model.repayment.PrepaymentRecodeSettings;
import com.api.model.repayment.TrialRepaymentSettings;
import com.api.service.repayment.IRepaymentServer;
import com.zw.api.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 还款接口实现
 * @author  韩梅生
 */
@Service(IRepaymentServer.BEAN_KEY)
public class RepaymentServerImpl implements IRepaymentServer {

    @Autowired
    private TrialRepaymentSettings trialRepaymentSettings;

    @Autowired
    private PrepaymentRecodeSettings prepaymentRecodeSettings;

    @Autowired
    private LoanSettings loanSettings;

    @Autowired
    private BYXSettings byxSettings;

    @Override
    public BYXResponse trialRepayment(Map map) throws Exception {
        if (map != null){
            final String result = HttpClientUtil.post(trialRepaymentSettings.getRequestUrl(), BYXRequest.getBYXRequest(map, byxSettings), byxSettings.getHeadRequest());
            return BYXResponse.getBYXResponse(result, byxSettings);
        }
        return  BYXResponse.error();
    }

    @Override
    public BYXResponse prepaymentRecode(Map map) throws Exception {
        if (map != null){
            final String result = HttpClientUtil.post(prepaymentRecodeSettings.getRequestUrl(), BYXRequest.getBYXRequest(map, byxSettings), byxSettings.getHeadRequest());
            return BYXResponse.getBYXResponse(result, byxSettings);
        }
        return  BYXResponse.error();
    }

    @Override
    public BYXResponse getLoan(Map map) throws Exception {
        if (map != null){
            final String result = HttpClientUtil.post(loanSettings.getRequestUrl(), BYXRequest.getBYXRequest(map, byxSettings), byxSettings.getHeadRequest());
            return BYXResponse.getBYXResponse(result, byxSettings);
        }
        return  BYXResponse.error();
    }
}
