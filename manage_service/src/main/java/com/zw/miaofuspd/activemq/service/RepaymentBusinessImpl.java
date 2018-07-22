package com.zw.miaofuspd.activemq.service;

import com.activemq.entity.respose.LoanDetailResponse;
import com.activemq.entity.respose.RepaymentResponse;
import com.activemq.service.IRepaymentBusiness;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.model.common.BYXResponse;
import com.api.model.sortmsg.MsgRequest;
import com.api.service.sortmsg.IMessageServer;
import com.base.util.BeanMapperUtil;
import com.base.util.DateUtils;
import com.base.util.GeneratePrimaryKeyUtils;
import com.base.util.TemplateUtils;
import com.constants.SysConstant;
import com.enums.DictEnum;
import com.enums.OperationNodeEnum;
import com.enums.OperationResultEnum;
import com.zhiwang.zwfinance.app.jiguang.util.api.util.OrderStateEnum;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.facade.order.service.IOrderOperationRecordService;
import com.zw.miaofuspd.facade.pojo.MagOrder;
import com.zw.miaofuspd.facade.pojo.OrderOperationRecord;
import com.zw.pojo.AppMessage;
import com.zw.pojo.BusinessRepayment;
import com.zw.service.IBusinessRepaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service(IRepaymentBusiness.BEAN_KEY)
public class RepaymentBusinessImpl implements IRepaymentBusiness {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private AppOrderService appOrderService;

    @Autowired
    private IOrderOperationRecordService orderOperationRecordService;

    @Autowired
    private IBusinessRepaymentService businessRepaymentService;

    @Autowired
    private IDictService dictService;

    @Autowired
    private IMessageServer messageServer;

    private  MagOrder order ;

    @Override
    public boolean loanMoney(LoanDetailResponse loanDetailResponse) {
        String currentTime = DateUtils.getCurrentTime();
        order = appOrderService.getOrderByNo(loanDetailResponse.getBusinessId());
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
        return setLoanInfo(loanDetailResponse) && orderOperationRecordService.insetOrderOperationRecord(orderOperationRecord);
    }

    @Override
    public boolean setLoanInfo(LoanDetailResponse loanDetailResponse) {
        MagOrder magOrder = new MagOrder();
        magOrder.setId(loanDetailResponse.getBusinessId());
        //设置为未还款状态
        magOrder.setOrderState(String.valueOf(OrderStateEnum.PENDING_REPAYMENT.getCode()));
        magOrder.setPayBackCard(loanDetailResponse.getLoanNo());
        magOrder.setPayBackUser(loanDetailResponse.getLoanName());
        magOrder.setAlterTime(DateUtils.getCurrentTime());
        magOrder.setLoanTime(DateUtils.getCurrentTime());
        return appOrderService.updateOrderById(magOrder) > 0;
    }

    @Override
    public boolean saveRepaymentInfo(RepaymentResponse repaymentResponse) {
        BusinessRepayment businessRepayment = BeanMapperUtil.objConvert(repaymentResponse, BusinessRepayment.class);
        businessRepayment.setCreateUserId("admin");
        businessRepayment.setPeriods(0);
        businessRepayment.setId(GeneratePrimaryKeyUtils.getUUIDKey());
        businessRepayment.setUpdateUserId("");
        businessRepayment.setUpdateTime(new Date());
        return businessRepaymentService.saveRepayment(businessRepayment);
    }

    @Override
    public boolean updateRepaymentInfo(RepaymentResponse repaymentResponse) {
        BusinessRepayment businessRepayment = BeanMapperUtil.objConvert(repaymentResponse, BusinessRepayment.class);
        businessRepayment.setUpdateUserId("admin");
        businessRepayment.setUpdateTime(new Date());
        return businessRepaymentService.updateByOrderIdAndPeriod(businessRepayment) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean repaymentHandel(BYXResponse byxResponse) {
        if(BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
            Map resData = (Map)byxResponse.getRes_data();
            LoanDetailResponse loanDetail = JSONObject.toJavaObject((JSON) resData.get("loanDetail"),LoanDetailResponse.class);
            if(loanDetail != null ){
                loanDetail.setBusinessId("453601127108182016");
                loanMoney(loanDetail);
                JSONArray repaymentList = (JSONArray) resData.get("repaymentList");
                //批量生产还款计划（多期的情况）
                if( repaymentList != null ){
                    for (Object item : repaymentList) {
                        RepaymentResponse repayment = JSONObject.toJavaObject((JSON)item,RepaymentResponse.class);
                        if(repayment != null){
                            repayment.setOrderNo(loanDetail.getBusinessId());
                            saveRepaymentInfo(repayment);
                        }
                    }
                    return  true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean repaymentPushAssentHandel(BYXResponse byxResponse) {
        if(BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
            Map resData = (Map)byxResponse.getRes_data();
                JSONArray repaymentList = (JSONArray) resData.get("repaymentList");
                //批量生产还款计划（多期的情况）
                if( repaymentList != null){
                    for (Object item : repaymentList) {
                        RepaymentResponse repayment = JSONObject.toJavaObject((JSON)item,RepaymentResponse.class);
                        if(repayment != null){
                            repayment.setOrderNo("453601127108182016");
                            updateRepaymentInfo(repayment);
                        }
                    }
                    return  true;
                }
        }
        return false;
    }

    @Override
    public void sendMessage() throws Exception {
        String contentTemplate = dictService.getDictInfo(DictEnum.FKCG.getName(),DictEnum.FKCG.getCode());
        if(StringUtils.isEmpty(contentTemplate)){
            LOGGER.info("--------缺少放款短信模板配置-----------");
             return;
        }
        Map<String,String> parameter = new HashMap<>(2);
        parameter.put("applayMoney",order.getApplayMoney().toString());
        parameter.put("periods",order.getPeriods());
        String content = TemplateUtils.getContent(contentTemplate, parameter);

        AppMessage appMessage = new AppMessage();
        appMessage.setCreatTime(DateUtils.getCurrentTime());
        appMessage.setContent(content);
        appMessage.setTitle("放款成功");
        appMessage.setUserId(order.getUserId());
        //发送站内信
        messageServer.sendLetter(appMessage);

        MsgRequest msgRequest = new MsgRequest();
        msgRequest.setPhone(order.getTel());
        msgRequest.setContent(content);
        //发送短信
        messageServer.sendSms(msgRequest);
        LOGGER.info("--------发送短信及站内信成功-----------");
    }
}
