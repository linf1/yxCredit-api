package com.zw.miaofuspd.activemq.service;

import com.activemq.entity.respose.LoanDetailResponse;
import com.activemq.entity.respose.RepaymentResponse;
import com.activemq.service.IRepaymentService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.model.common.BYXResponse;
import com.base.util.BeanMapperUtil;
import com.base.util.DateUtils;
import com.base.util.GeneratePrimaryKeyUtils;
import com.constants.SysConstant;
import com.enums.OperationNodeEnum;
import com.enums.OperationResultEnum;
import com.zhiwang.zwfinance.app.jiguang.util.api.util.OrderStateEnum;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.facade.order.service.IOrderOperationRecordService;
import com.zw.miaofuspd.facade.pojo.MagOrder;
import com.zw.miaofuspd.facade.pojo.OrderOperationRecord;
import com.zw.pojo.BusinessRepayment;
import com.zw.service.IBusinessRepaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service(IRepaymentService.BEAN_KEY)
public class RepaymentServiceImpl implements IRepaymentService {

    @Autowired
    private AppOrderService appOrderService;

    @Autowired
    private IOrderOperationRecordService orderOperationRecordService;

    @Autowired
    private IBusinessRepaymentService businessRepaymentService;


    @Override
    public boolean loanMoney(LoanDetailResponse loanDetailResponse) {
        String currentTime = DateUtils.getCurrentTime();
        MagOrder magOrder = new MagOrder();
        magOrder.setId(loanDetailResponse.getBusinessId());
        //设置为未还款状态
        magOrder.setOrderState(String.valueOf(OrderStateEnum.PENDING_REPAYMENT.getCode()));
        magOrder.setPayBackCard(loanDetailResponse.getVirtualAccount());
        magOrder.setPayBackUser(loanDetailResponse.getVirtualAccName());
        magOrder.setAlterTime(currentTime);

        MagOrder order = appOrderService.getOrderById(loanDetailResponse.getBusinessId());
        OrderOperationRecord orderOperationRecord = new OrderOperationRecord();
        orderOperationRecord.setId(GeneratePrimaryKeyUtils.getUUIDKey());
        orderOperationRecord.setOperationTime(currentTime);
        orderOperationRecord.setDescription("已放款");
        //放款审核
        orderOperationRecord.setOperationNode(OperationNodeEnum.LOAN_AUDIT.getCode());
        //放款
        orderOperationRecord.setOperationResult(OperationResultEnum.LOAN.getCode());
        orderOperationRecord.setEmpId("admin");
        orderOperationRecord.setEmpName("admin");
        //订单id
        orderOperationRecord.setOrderId(loanDetailResponse.getBusinessId());
        orderOperationRecord.setAmount(order.getContractAmount());
        orderOperationRecord.setStatus(SysConstant.INVALID);

        //更新订单还款账户信息 && 新增操作记录
        return appOrderService.updateOrderById(magOrder) > 0 && orderOperationRecordService.insetOrderOperationRecord(orderOperationRecord);
    }

    @Override
    public boolean saveRepaymentInfo(RepaymentResponse repaymentResponse) {
        BusinessRepayment businessRepayment = BeanMapperUtil.objConvert(repaymentResponse, BusinessRepayment.class);
        businessRepayment.setCreateUserId("admin");
        businessRepayment.setPeriods(0);
        businessRepayment.setId(GeneratePrimaryKeyUtils.getUUIDKey());
        businessRepayment.setUpdateUserId("");
        return businessRepaymentService.saveRepayment(businessRepayment);
    }

    @Override
    public boolean updateRepaymentInfo(RepaymentResponse repaymentResponse) {
        BusinessRepayment businessRepayment = BeanMapperUtil.objConvert(repaymentResponse, BusinessRepayment.class);
        businessRepayment.setUpdateUserId("admin");
        return businessRepaymentService.updateByOrderIdAndPeriod(businessRepayment) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean repaymentHandel(BYXResponse byxResponse) {
        if(BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
            Map resData = (Map)byxResponse.getRes_data();
            LoanDetailResponse loanDetail = JSONObject.toJavaObject((JSON) resData.get("loanDetail"),LoanDetailResponse.class);
            if(loanDetail != null ){
                loanDetail.setBusinessId("0d8a7369b7ee454b993817456a65e421");
                loanMoney(loanDetail);
                JSONArray repaymentList = (JSONArray) resData.get("repaymentList");
                //批量生产还款计划（多期的情况）
                if( repaymentList != null){
                    for (Object item : repaymentList) {
                        RepaymentResponse repayment = JSONObject.toJavaObject((JSON)item,RepaymentResponse.class);
                        if(repayment != null){
                            repayment.setOrderId(loanDetail.getBusinessId());
                            saveRepaymentInfo(repayment);
                        }
                    }
                    return  true;
                }
            }
        }
        return false;
    }

}
