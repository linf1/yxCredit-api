package com.zw.miaofuspd.repayment.controller;

import com.activemq.entity.respose.LoanDetailResponse;
import com.activemq.service.IRepaymentBusiness;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.api.model.common.BYXResponse;
import com.api.service.repayment.IRepaymentServer;
import com.base.util.AppRouterSettings;
import com.base.util.DateUtils;
import com.zw.pojo.BusinessRepayment;
import com.zw.service.IBusinessRepaymentService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
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
                return ResultVO.ok(byxResponse.getRes_data());
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
                LoanDetailResponse loanDetail = JSONObject.toJavaObject((JSON) resData.get("loanDetail"),LoanDetailResponse.class);
                repaymentBusiness.setLoanInfo(loanDetail);
                return ResultVO.ok(byxResponse.getRes_data());
            }
            return ResultVO.error(byxResponse.getRes_msg());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage());
        }
    }

    /**
     * 查询还款账单信息
     * @param orderId 订单编号
     * @return 成功失败
     */
    @PostMapping("/getRepaymentByOrderId")
    public ResultVO getRepaymentByOrderId(String orderId) {
        if(orderId == null){
            return ResultVO.error("参数异常");
        }
        BusinessRepayment businessRepayment = new BusinessRepayment();
        businessRepayment.setOrderNo(orderId);
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
    @GetMapping("/findListByOrderId")
    public ResultVO findListByOrderId(String orderNo) {
        if(orderNo == null){
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

}
