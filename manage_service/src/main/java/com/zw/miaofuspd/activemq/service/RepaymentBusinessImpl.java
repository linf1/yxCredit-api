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
import com.zw.service.IOrderOperationRecordService;
import com.zw.pojo.Order;
import com.zw.pojo.OrderOperationRecord;
import com.zw.pojo.AppMessage;
import com.zw.pojo.BusinessRepayment;
import com.zw.service.IBusinessRepaymentService;
import com.zw.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service(IRepaymentBusiness.BEAN_KEY)
public class RepaymentBusinessImpl implements IRepaymentBusiness {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderOperationRecordService orderOperationRecordService;

    @Autowired
    private IBusinessRepaymentService businessRepaymentService;

    @Autowired
    private IDictService dictService;

    @Autowired
    private IMessageServer messageServer;

    private Order order;

    @Override
    public boolean loanMoney(LoanDetailResponse loanDetailResponse) {
        order = orderService.getOrderByNo(loanDetailResponse.getBusinessId());
        OrderOperationRecord orderOperationRecord = new OrderOperationRecord();
        orderOperationRecord.setId(GeneratePrimaryKeyUtils.getUUIDKey());
        orderOperationRecord.setOperationTime(DateUtils.getDateString(loanDetailResponse.getReviewTime(), DateUtils.STYLE_10));
        orderOperationRecord.setDescription("已放款");
        //放款审核
        orderOperationRecord.setOperationNode(OperationNodeEnum.LOAN_AUDIT.getCode());
        //放款
        orderOperationRecord.setOperationResult(OperationResultEnum.LOAN.getCode());
        orderOperationRecord.setEmpId("admin");
        orderOperationRecord.setEmpName("admin");
        //订单id
        orderOperationRecord.setOrderId(order.getId());
        orderOperationRecord.setAmount(order.getContractAmount());
        orderOperationRecord.setStatus(SysConstant.INVALID);

        //更新订单还款账户信息 && 新增操作记录
        return setLoanInfo(loanDetailResponse) && orderOperationRecordService.insetOrderOperationRecord(orderOperationRecord);
    }

    @Override
    public boolean setLoanInfo(LoanDetailResponse loanDetailResponse) {
        Order order = new Order();
        order.setOrderNo(loanDetailResponse.getBusinessId());
        //设置为未还款状态
        order.setOrderState(String.valueOf(OrderStateEnum.PENDING_REPAYMENT.getCode()));
        order.setPayBackCard(loanDetailResponse.getLoanNo());
        order.setPayBackUser(loanDetailResponse.getLoanName());
        order.setAlterTime(DateUtils.getCurrentTime());
        //放款时间
        order.setLoanTime(DateUtils.getDateString(loanDetailResponse.getReviewTime(), DateUtils.STYLE_1));
        return orderService.updateOrderByNo(order) > 0;
    }

    @Override
    public boolean saveRepaymentInfo(RepaymentResponse repaymentResponse) {
        BusinessRepayment businessRepayment = BeanMapperUtil.objConvert(repaymentResponse, BusinessRepayment.class);
        businessRepayment.setCreateUserId("admin");
        businessRepayment.setPeriods(0);
        businessRepayment.setId(GeneratePrimaryKeyUtils.getUUIDKey());
        businessRepayment.setUpdateUserId("");
        businessRepayment.setUpdateTime(new Date());
        businessRepayment.setCreateTime(new Date());
        initRepayment(businessRepayment);
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
    public boolean repaymentHandel(BYXResponse byxResponse) throws Exception {
        if (BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
            Map resData = (Map) byxResponse.getRes_data();
            LoanDetailResponse loanDetail = JSONObject.toJavaObject((JSON) resData.get("loanDetail"), LoanDetailResponse.class);
            if (loanDetail != null) {
                loanMoney(loanDetail);
                JSONArray repaymentList = (JSONArray) resData.get("repaymentList");
                //批量生成还款计划（多期的情况）
                if (repaymentList != null) {
                    for (Object item : repaymentList) {
                        RepaymentResponse repayment = JSONObject.toJavaObject((JSON) item, RepaymentResponse.class);
                        if (repayment != null) {
                            repayment.setOrderNo(loanDetail.getBusinessId());
                            saveRepaymentInfo(repayment);
                        }
                    }
                    //成功发送站内信息及短信
                    sendMessage(this.order);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean repaymentPushAssentHandel(BYXResponse byxResponse) {
        if (BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
            Map resData = (Map) byxResponse.getRes_data();
            JSONArray repaymentList = (JSONArray) resData.get("repaymentList");
            //订单编号
            String businessId = resData.get("businessId").toString();
            //批量更新还款计划（多期的情况）
            if (repaymentList != null) {
                for (Object item : repaymentList) {
                    RepaymentResponse repayment = JSONObject.toJavaObject((JSON) item, RepaymentResponse.class);
                    if (repayment != null) {
                        repayment.setOrderNo(businessId);
                        updateRepaymentInfo(repayment);
                    }
                }
                //如果用户款项已经结清就更新订单状态为已结清
                 settleOrder(businessId);
                return true;
            }
        }
        return false;
    }

    @Override
    public void settleOrder(String businessId) {
        Integer countNum = businessRepaymentService.countRepaymentByStatus(businessId);
        if(countNum == 0){
            Order order = new Order();
            order.setOrderNo(businessId);
            order.setOrderState(String.valueOf(OrderStateEnum.ALREADY_SETTLED.getCode()));
            orderService.updateOrderByNo(order);
            LOGGER.info("--------订单编号为{}的订单用户已经款项已经全部结清--------",businessId);
        }
    }

    @Override
    public void sendMessage(Order order) throws Exception {
        String contentTemplate = dictService.getDictInfo(DictEnum.FKCG.getName(), DictEnum.FKCG.getCode());
        if (StringUtils.isEmpty(contentTemplate)) {
            LOGGER.info("--------缺少放款短信模板配置-----------");
            return;
        }
        Map<String, String> parameter = new HashMap<>(2);
        parameter.put("applayMoney", order.getApplayMoney().toString());
        parameter.put("periods", order.getPeriods());
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

    /**
     * 远程调用获取还款信息 如果字段为空 为对应字段赋初始值
     *
     * @param businessRepayment 还款信息实体
     */
    private void initRepayment(BusinessRepayment businessRepayment) {
        if (businessRepayment.getRepaymentAccount() == null) {
            businessRepayment.setRepaymentAccount(new BigDecimal(0.0000));
        }
        if (businessRepayment.getRepaymentYesAccount() == null) {
            businessRepayment.setRepaymentYesAccount(new BigDecimal(0.0000));
        }
        if (businessRepayment.getYesCapital() == null) {
            businessRepayment.setYesCapital(new BigDecimal(0.0000));
        }
        if (businessRepayment.getRate() == null) {
            businessRepayment.setRate(new BigDecimal(0.0000));
        }
        if (businessRepayment.getInterest() == null) {
            businessRepayment.setInterest(new BigDecimal(0.0000));
        }
        if (businessRepayment.getRepaymentYesInterest() == null) {
            businessRepayment.setRepaymentYesInterest(new BigDecimal(0.0000));
        }
        if (businessRepayment.getIsRepayment() == null) {
            businessRepayment.setIsRepayment(0);
        }
        if (businessRepayment.getRepaymentType() == null) {
            businessRepayment.setRepaymentType(0);
        }
        if (businessRepayment.getLateDays() == null) {
            businessRepayment.setLateDays(0);
        }
        if (businessRepayment.getLateRate() == null) {
            businessRepayment.setLateRate(new BigDecimal(0.0000));
        }
        if (businessRepayment.getLateInterest() == null) {
            businessRepayment.setLateInterest(new BigDecimal(0.0000));
        }
        if (businessRepayment.getDerateAmount() == null) {
            businessRepayment.setDerateAmount(new BigDecimal(0.0000));
        }
        if (businessRepayment.getRemark() == null) {
            businessRepayment.setRemark("");
        }
        if (businessRepayment.getChannelType() == null) {
            businessRepayment.setChannelType("");
        }
    }
}
