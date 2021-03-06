package com.zw.miaofuspd.repayment.controller;

import com.activemq.entity.respose.LoanDetailResponse;
import com.activemq.entity.respose.RepaymentResponse;
import com.activemq.service.IRepaymentBusiness;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.model.common.BYXResponse;
import com.api.service.repayment.IRepaymentServer;
import com.base.util.AppRouterSettings;
import com.base.util.DateUtils;
import com.enums.RepaymentStatusEnum;
import com.enums.RepaymentTypeEnum;
import com.zw.pojo.BusinessRepayment;
import com.zw.pojo.Order;
import com.zw.service.IBusinessRepaymentService;
import com.zw.service.IOrderService;
import com.zw.web.base.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 预算提前还款
 * @author 韩梅生
 */
@RestController
@RequestMapping(AppRouterSettings.VERSION + "/repayment")
public class RepaymentController {
    @Autowired
    private IRepaymentServer repaymentServer;

    @Autowired
    private IBusinessRepaymentService businessRepaymentService;

    @Autowired
    private IRepaymentBusiness repaymentBusiness;

    @Autowired
    private IOrderService orderService;


    private final Logger LOGGER = LoggerFactory.getLogger(RepaymentController.class);

    /**
     * 预算提前还款
     * @param data  repayId 还款ID
     *              repayType 1 正常还款； 2 提前还款； 3 部分提前还款
     *              interestYesTime 结息时间
     * @return 成功失败
     */
    @PostMapping("/trialRepayment")
    public ResultVO trialRepayment(String data) {
        if(data == null){
            return ResultVO.error("参数异常");
        }
        LOGGER.info("进入预算提前还款,参数为：{}",data);
        Map<String,Object> map = JSONObject.parseObject(data);
        map.put("interestYesTime",DateUtils.getDateString(DateUtils.getSpecifyDate(1),DateUtils.STYLE_1));
        try {
            BYXResponse  byxResponse = repaymentServer.trialRepayment(map);
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
     * 确认提前还款
     * @param data  repayId 还款ID
     *              repayType 1 正常还款； 2 提前还款； 3 部分提前还款
     *              interestYesTime 结息时间
     * @return 成功失败
     */
    @PostMapping("/prepaymentRecode")
    public ResultVO prepaymentRecode(String data) {
        if(data == null){
            return ResultVO.error("参数异常");
        }
        LOGGER.info("进入确定提前还款,参数为：{}",data);
        Map<String,Object> map = JSONObject.parseObject(data);
        map.put("interestYesTime",DateUtils.getDateString(DateUtils.getSpecifyDate(1),DateUtils.STYLE_1));
        try {
            BYXResponse  byxResponse = repaymentServer.prepaymentRecode(map);
            if (BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
                boolean isSuccess = repaymentServer.updateRepayment(map.get("repayId").toString());
                if(isSuccess){
                    return ResultVO.ok(byxResponse.getRes_data());
                }
            }
            return ResultVO.error(byxResponse.getRes_msg());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage());
        }
    }

    /**
     * 查询还款账号
     * @param orderId 订单编号
     * @return 成功失败
     */
    @PostMapping("/getLoan")
    public ResultVO getLoan(String orderId) {
        if(orderId == null){
            return ResultVO.error("参数异常");
        }
        LOGGER.info("进入查询还款账号,参数为：{}",orderId);
        Map<String,String> map = new HashMap<>(1);
        map.put("businessId",orderId);
        try {
            BYXResponse  byxResponse = repaymentServer.getLoan(map);
            if (BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
                Map resData = (Map)byxResponse.getRes_data();
                LoanDetailResponse loanDetail = JSONObject.toJavaObject((JSON) resData,LoanDetailResponse.class);
                //更新订单放款信息
                boolean isLoanMoney = repaymentBusiness.loanMoney(loanDetail);
                if(isLoanMoney){
                    //发送短信
                    Order order = orderService.getOrderByNo(orderId);
                    repaymentBusiness.sendMessage(order);
                    return ResultVO.ok(byxResponse.getRes_msg());
                }
            }
            LOGGER.info("查询还款账号异常：{}",byxResponse.getRes_msg());
            return ResultVO.error(byxResponse.getRes_msg());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage());
        }
    }

    /**
     * 远程获取还款计划并持久化到本地数据库 create on 陈淸玉
     * @param orderNo 订单编号
     * @throws Exception http 异常
     */
    @PostMapping("/addRepayment")
    public ResultVO addRepayment(String orderNo) throws Exception {
        BusinessRepayment repayment = new BusinessRepayment();
        repayment.setOrderNo(orderNo);
        //未还款的状态
        repayment.setRepaymentType(RepaymentTypeEnum.REPAYMENT_ALL.getCode());
        //调远程API获取还款计划
        LOGGER.info("远程获取还款计划参数：{}",repayment.toString());
        BYXResponse repaymentInfo = repaymentServer.getRepaymentListByProjectId(repayment);
        if (BYXResponse.resCode.success.getCode().equals(repaymentInfo.getRes_code())) {
            //先删除数据库还款计划
            repaymentServer.deleteRepayment(orderNo);
            //多期产品会有多期还款计划
            JSONArray repaymentList = (JSONArray)repaymentInfo.getRes_data();
            for (Object item : repaymentList) {
                RepaymentResponse repaymentResponse = JSONObject.toJavaObject((JSON)item,RepaymentResponse.class);
                if(repaymentResponse != null){
                    repaymentResponse.setOrderNo(orderNo);
                    repaymentBusiness.saveRepaymentInfo(repaymentResponse);
                }
            }
            //如果用户款项已经结清就更新订单状态为已结清
            repaymentBusiness.settleOrder(orderNo);
          return   ResultVO.ok(repaymentInfo.getRes_msg());
        }
        LOGGER.info("远程获取还款计划失败：{}",repaymentInfo.getRes_msg());
     return   ResultVO.error(repaymentInfo.getRes_msg());
    }

    /**
     * 查询还款账单信息
     * @param orderNo 订单编号
     * @return 成功失败
     */
    @GetMapping("/getRepaymentByOrderNo")
    public ResultVO getRepaymentByOrderId(String orderNo) {
        if(StringUtils.isBlank(orderNo)){
            return ResultVO.error("参数异常");
        }
        BusinessRepayment businessRepayment = new BusinessRepayment();
        businessRepayment.setOrderNo(orderNo);
        try {
            Map map = businessRepaymentService.getRepaymentByOrderId(businessRepayment);
            return ResultVO.ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage());
        }
    }

    /**
     * 查询还款期数列表
     * @param orderNo 订单编号
     * @return 成功失败
     */
    @GetMapping("/findListByOrderNo")
    public ResultVO findListByOrderId(String orderNo) {
        if(StringUtils.isBlank(orderNo)){
            return ResultVO.error("参数异常");
        }
        BusinessRepayment businessRepayment = new BusinessRepayment();
        businessRepayment.setOrderNo(orderNo);
        try {
            List<Map> businessRepaymentList = businessRepaymentService.findListByOrderId(businessRepayment);
            return ResultVO.ok(businessRepaymentList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage());
        }
    }
    /**
     * 查询还款计划
     * @param  businessId 订单编号
     *         repaymentType 还款类型
     * @return 成功失败
     */
    @PostMapping("/getRepaymentListByProjectId")
    public ResultVO getRepaymentListByProjectId(String businessId,Integer repaymentType) {
        if(businessId == null || repaymentType == null){
            return ResultVO.error("参数异常");
        }
        LOGGER.info("进入查询还款计划,参数为：{}",businessId,repaymentType);
        BusinessRepayment businessRepayment = new BusinessRepayment();
        businessRepayment.setOrderNo(businessId);
        businessRepayment.setRepaymentType(repaymentType);
        try {
            BYXResponse  byxResponse = repaymentServer.getRepaymentListByProjectId(businessRepayment);
            if (BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
                LOGGER.info("查询还款计划成功----------------------------");
                return ResultVO.ok(byxResponse.getRes_data());
            }
            return ResultVO.error(byxResponse.getRes_msg());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage());
        }
    }

    /**
     * 更新还款计划表状态
     * @param data 还款数据
     * @return 成功失败
     */
    @PostMapping("/updateRepaymentStatus")
    public ResultVO updateRepaymentStatus(String data) {
        try {
           if(StringUtils.isNotBlank(data)){
                Map map = JSONObject.parseObject(data);
                if(map.containsKey("id") && null != map.get("id")
                        && map.containsKey("repayType") && null != map.get("repayType")) {
                    BusinessRepayment  businessRepayment = new BusinessRepayment();
                    businessRepayment.setId(map.get("id").toString());
                    businessRepayment.setStatus(RepaymentStatusEnum.REPAYMENT_PROCESSING.getCode());
                    businessRepayment.setRepaymentType(Integer.parseInt(map.get("repayType").toString()));
                    int repaymentNum = businessRepaymentService.updateRepaymentById(businessRepayment);
                    if(repaymentNum > 0) {
                        return ResultVO.ok("更新还款计划成功",null);
                    }
                    return ResultVO.error("更新还款计划失败");
                }
            }
            return ResultVO.error("参数异常");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error("系统异常,请联系管理员。");
        }
    }

    /**
     * 查询还款记录列表
     * @param orderNo 订单编号
     * @return 成功失败
     */
    @GetMapping("/findListRecordByOrderNo")
    public ResultVO findListRecordByOrderNo(String orderNo) {
        if(StringUtils.isBlank(orderNo)){
            return ResultVO.error("参数异常");
        }
        BusinessRepayment businessRepayment = new BusinessRepayment();
        businessRepayment.setOrderNo(orderNo);
        businessRepayment.setStatus(RepaymentStatusEnum.REPAYMENT.getCode());
        try {
            List<Map> businessRepaymentList = businessRepaymentService.findListRecordByOrderNo(businessRepayment);
            return ResultVO.ok(businessRepaymentList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error("系统异常,请联系管理员。");
        }
    }

}
