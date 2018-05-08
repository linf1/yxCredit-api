package com.zw.miaofuspd.facade.trxcode.service;

import com.zw.miaofuspd.facade.entity.TrxCode;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5 0005.
 */
public interface TrxCodeService {
    List<TrxCode> selectTrxcodeByCard(String card) throws Exception;
}
