package com.zw.miaofuspd.service;

import com.zw.miaofuspd.mapper.BusinessRepaymentMapper;
import com.zw.pojo.BusinessRepayment;
import com.zw.service.IBusinessRepaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(IBusinessRepaymentService.BEAN_KEY)
public class BusinessRepaymentServiceImpl implements IBusinessRepaymentService {

    @Autowired
    private BusinessRepaymentMapper businessRepaymentMapper;

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
}
