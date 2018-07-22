package com.zw.miaofuspd.service;

import com.base.util.DateUtils;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.facade.pojo.MagOrder;
import com.zw.miaofuspd.mapper.BusinessRepaymentMapper;
import com.zw.pojo.BusinessRepayment;
import com.zw.service.IBusinessRepaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(IBusinessRepaymentService.BEAN_KEY)
public class BusinessRepaymentServiceImpl implements IBusinessRepaymentService {

    @Autowired
    private BusinessRepaymentMapper businessRepaymentMapper;

    @Autowired
    private AppOrderService appOrderService;

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
    public Map getRepaymentByOrderId(BusinessRepayment record) {
        HashMap map =  new HashMap();
        List<BusinessRepayment> businessRepaymentList = businessRepaymentMapper.findListByOrderId(record);
        if(null != businessRepaymentList) {
            Date currentDate = new Date();
            for(BusinessRepayment businessRepayment : businessRepaymentList) {
                //起息日小于当前时间
                if(DateUtils.getDifferenceDays(businessRepayment.getInterestStartTime(), currentDate) < 0
                        && businessRepayment.getRepaymentType() == 5) {
                    map.put("repaymentTime", businessRepayment.getRepaymentTime());
                    map.put("repaymentAccount",businessRepayment.getRepaymentAccount());
                    map.put("repaymentTime",businessRepayment.getRepaymentTime());
                    map.put("statue",businessRepayment.getStatus());//还款状态  1、还款中、2还款处理中、3已还款
                    map.put("repaymentType",businessRepayment.getRepaymentType());//还款类型，0未还款,1 正常还款; 2 提前还款;3 部分提前还款;4逾期还款，5逾期未还;
                }
            }
            if(map.isEmpty()) {
                //起息日小于当前时间
                for(BusinessRepayment businessRepayment : businessRepaymentList)
                    if (DateUtils.getDifferenceDays(businessRepayment.getInterestStartTime(), currentDate) < 0) {
                        map = new HashMap();
                        map.put("repaymentTime", businessRepayment.getRepaymentTime());
                        map.put("repaymentAccount",businessRepayment.getRepaymentAccount());
                        map.put("repaymentTime",businessRepayment.getRepaymentTime());
                        map.put("status",businessRepayment.getStatus());//还款状态  1、还款中、2还款处理中、3已还款
                        map.put("repaymentType",businessRepayment.getRepaymentType());//还款类型，0未还款,1 正常还款; 2 提前还款;3 部分提前还款;4逾期还款，5逾期未还;
                        map.put("repaymentTime",businessRepayment.getRepaymentTime());
                    }
            }
        }
        MagOrder magOrder = appOrderService.getOrderByNo(record.getOrderNo());
        map.put("loanTime",magOrder.getLoanTime());
        map.put("payBackUser",magOrder.getPayBackUser());
        map.put("payBackCard",magOrder.getPayBackCard());
        return map;
    }

    @Override
    public List<Map> findListByOrderId(BusinessRepayment record) {
        List<BusinessRepayment> businessRepaymentList = businessRepaymentMapper.findListByOrderId(record);
        if(null != businessRepaymentList && businessRepaymentList.size() > 0) {
            List<Map> listMap = new ArrayList<>();
            for (BusinessRepayment businessRepayment : businessRepaymentList){
                Map map = new HashMap();
                map.put("period", businessRepayment.getPeriod());//当前期数
                map.put("repaymentAccount", businessRepayment.getRepaymentAccount());//应还金额
                map.put("repaymentTime", businessRepayment.getRepaymentTime());//还款日
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
    public List<BusinessRepayment> findRepaymentInfoByStatus(String status) {
        return businessRepaymentMapper.findRepaymentInfoByStatus(status);
    }
}
