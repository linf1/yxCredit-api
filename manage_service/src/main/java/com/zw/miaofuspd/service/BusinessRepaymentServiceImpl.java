package com.zw.miaofuspd.service;

import com.base.util.DateUtils;
import com.enums.RepaymentStatusEnum;
import com.enums.RepaymentTypeEnum;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.mapper.BusinessRepaymentMapper;
import com.zw.pojo.BusinessRepayment;
import com.zw.pojo.Order;
import com.zw.service.IBusinessRepaymentService;
import com.zw.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(IBusinessRepaymentService.BEAN_KEY)
public class BusinessRepaymentServiceImpl implements IBusinessRepaymentService {

    @Autowired
    private BusinessRepaymentMapper businessRepaymentMapper;

    @Autowired
    private IOrderService orderService;

    @Override
    public boolean saveRepayment(BusinessRepayment repayment) {
        return businessRepaymentMapper.insert(repayment) > 0;
    }

    @Override
    public BusinessRepayment selectById(String id) {
        return businessRepaymentMapper.selectByPrimaryKey(id);
    }


    @Override
    public BusinessRepayment getByOrderIdAndPeriod(BusinessRepayment record) {
        return businessRepaymentMapper.getByOrderIdAndPeriod(record);
    }

    @Override
    public int updateByOrderIdAndPeriod(BusinessRepayment record) {
        return businessRepaymentMapper.updateByOrderIdAndPeriod(record);
    }

    @Override
    public int updateRepaymentById(BusinessRepayment record) {
        return businessRepaymentMapper.updateRepaymentById(record);
    }

    @Override
    public Map getRepaymentByOrderId(BusinessRepayment record) {
        List<BusinessRepayment> businessRepaymentList = businessRepaymentMapper.findListByOrderId(record);
        if(null != businessRepaymentList && businessRepaymentList.size() > 0) {
            Map<String,Object> map =  new HashMap<String,Object>();
            String currentTime = DateUtils.getCurrentTime(DateUtils.STYLE_2);
            Date currentDate = DateUtils.strConvertToDate(currentTime,DateUtils.STYLE_2);
            for(BusinessRepayment businessRepayment : businessRepaymentList) {
                //起息日小于当前时间
                if(currentDate.getTime() >= businessRepayment.getInterestStartTime().getTime()
                        && RepaymentTypeEnum.OVERDUE.getCode().equals(businessRepayment.getRepaymentType())) {
                    map.put("id", businessRepayment.getId());
                    map.put("repaymentId",businessRepayment.getRepaymentId());
                    map.put("period", businessRepayment.getPeriod());//当前期数
                    map.put("repaymentTime", businessRepayment.getRepaymentTime());
                    map.put("repaymentAccount", businessRepayment.getRepaymentAccount());
                    map.put("repaymentTime", businessRepayment.getRepaymentTime());
                    map.put("status", businessRepayment.getStatus());//还款状态  1、还款中、2还款处理中、3已还款
                    map.put("repaymentType", businessRepayment.getRepaymentType());//还款类型，0未还款,1 正常还款; 2 提前还款;3 部分提前还款;4逾期还款，5逾期未还;
                    map.put("repaymentTime", DateUtils.getDateString(businessRepayment.getRepaymentTime(),DateUtils.STYLE_1));
                }
            }
            if(map.isEmpty()) {
                //起息日小于当前时间
                for(BusinessRepayment businessRepayment : businessRepaymentList)
                    if (currentDate.getTime() >= businessRepayment.getInterestStartTime().getTime()) {
                        map.put("id",businessRepayment.getId());
                        map.put("repaymentId",businessRepayment.getRepaymentId());
                        map.put("period", businessRepayment.getPeriod());//当前期数
                        map.put("repaymentTime", businessRepayment.getRepaymentTime());
                        map.put("repaymentAccount",businessRepayment.getRepaymentAccount());
                        map.put("repaymentTime",businessRepayment.getRepaymentTime());
                        map.put("status",businessRepayment.getStatus());//还款状态  1、还款中、2还款处理中、3已还款
                        map.put("repaymentType",businessRepayment.getRepaymentType());//还款类型，0未还款,1 正常还款; 2 提前还款;3 部分提前还款;4逾期还款，5逾期未还;
                        map.put("repaymentTime",DateUtils.getDateString(businessRepayment.getRepaymentTime(),DateUtils.STYLE_1));
                        break;
                    }
            }
            Order order = orderService.getOrderByNo(record.getOrderNo());
            map.put("loanTime",order.getLoanTime());
            map.put("payBackUser",order.getPayBackUser());
            map.put("payBackCard",order.getPayBackCard());
            map.put("isRepayment", order.getIsRepayment());//是否提前还款，0否，1是
            return map;
        }
        return null;
    }

    @Override
    public List<Map> findListByOrderId(BusinessRepayment record) {
        List<BusinessRepayment> businessRepaymentList = businessRepaymentMapper.findListByOrderId(record);
        if(null != businessRepaymentList && businessRepaymentList.size() > 0) {
            List<Map> listMap = new ArrayList<>();
            for (BusinessRepayment businessRepayment : businessRepaymentList){
                Map<String,Object> map =  new HashMap<String,Object>();
                map.put("id", businessRepayment.getId());
                map.put("openDetails",false);
                map.put("period", businessRepayment.getPeriod());//当前期数
                map.put("repaymentAccount", businessRepayment.getRepaymentAccount());//应还金额
                map.put("repaymentTime",DateUtils.getDateString(businessRepayment.getRepaymentTime(),DateUtils.STYLE_1));
                map.put("capital", businessRepayment.getCapital());//本金
                map.put("interest", businessRepayment.getInterest());//利息
                map.put("repaymentType", businessRepayment.getRepaymentType());
                map.put("status", businessRepayment.getStatus());
                listMap.add(map);
            }
            return listMap;
        }
        return null;
    }

    @Override
    public List<Map> findListRecordByOrderNo(BusinessRepayment record) {
        List<BusinessRepayment> businessRepaymentList = businessRepaymentMapper.findListByOrderId(record);
        if(null != businessRepaymentList && businessRepaymentList.size() > 0) {
            List<Map> listMap = new ArrayList<>();
            for (BusinessRepayment businessRepayment : businessRepaymentList){
                Map<String,Object> map =  new HashMap<String,Object>();
                map.put("id", businessRepayment.getId());
                map.put("repaymentAccount", businessRepayment.getRepaymentAccount());//应还金额
                map.put("repaymentTime",DateUtils.getDateString(businessRepayment.getRepaymentYesTime(),DateUtils.STYLE_1));
                listMap.add(map);
            }
            return listMap;
        }
        return null;
    }

    @Override
    public List<BusinessRepayment> findRepaymentInfoByStatus(String status) {
        return businessRepaymentMapper.findRepaymentInfoByStatus(status);
    }

    @Override
    public Integer countRepaymentByStatus(String orderNo) {
        return businessRepaymentMapper.countRepaymentByStatus(orderNo);
    }
}
