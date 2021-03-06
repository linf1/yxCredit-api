package com.zw.miaofuspd.api.repayment;

import com.api.model.BYXSettings;
import com.api.model.common.BYXRequest;
import com.api.model.common.BYXResponse;
import com.api.model.repayment.LoanSettings;
import com.api.model.repayment.PrepaymentRecodeSettings;
import com.api.model.repayment.RepaymentSettings;
import com.api.model.repayment.TrialRepaymentSettings;
import com.api.service.repayment.IRepaymentServer;
import com.enums.RepaymentStatusEnum;
import com.zw.api.HttpClientUtil;
import com.zw.pojo.BusinessRepayment;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 还款接口实现
 * @author  韩梅生
 */
@Service(IRepaymentServer.BEAN_KEY)
public class RepaymentServerImpl extends AbsServiceBase implements IRepaymentServer {

    @Autowired
    private TrialRepaymentSettings trialRepaymentSettings;

    @Autowired
    private PrepaymentRecodeSettings prepaymentRecodeSettings;

    @Autowired
    private LoanSettings loanSettings;

    @Autowired
    private RepaymentSettings repaymentSettings;

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

    @Override
    public BYXResponse getRepaymentListByProjectId(BusinessRepayment record) throws Exception {
        Map<String,Object> paramMap = new HashMap<>(2);
        if(record != null){
            paramMap.put("businessId",record.getOrderNo());
            paramMap.put("repaymentType",record.getRepaymentType());
            final String result = HttpClientUtil.post(repaymentSettings.getRequestUrl(), BYXRequest.getBYXRequest(paramMap, byxSettings), byxSettings.getHeadRequest());
            return BYXResponse.getArrayBYXResponse(result, byxSettings);
        }
        return null;
    }

    @Override
    public void deleteRepayment(String orderNo) throws Exception {
        String delSQL = "delete from business_repayment where order_no = '"+orderNo+"'";
        sunbmpDaoSupport.exeSql(delSQL);
    }

    @Override
    public boolean updateRepayment(String repayId) {
        try {
            String updateSQL = "update business_repayment set status = '"+ RepaymentStatusEnum.REPAYMENT_PROCESSING.getCode() +"' where repayment_id = '"+repayId+"'";
            sunbmpDaoSupport.exeSql(updateSQL);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
