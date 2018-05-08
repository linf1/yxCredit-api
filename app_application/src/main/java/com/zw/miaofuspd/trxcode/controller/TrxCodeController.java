package com.zw.miaofuspd.trxcode.controller;

import com.zw.miaofuspd.facade.entity.TrxCode;
import com.zw.miaofuspd.facade.trxcode.service.TrxCodeService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5 0005.
 */
@Controller
@RequestMapping("trxCode")
public class TrxCodeController extends AbsBaseController {
    @Autowired
    public TrxCodeService trxCodeService;
    @RequestMapping("trxCode")
    @ResponseBody
    public ResultVO selectTrxcodeByCard(String card) throws Exception {
        ResultVO resultVO=new ResultVO();
        List<TrxCode> list = trxCodeService.selectTrxcodeByCard(card);
        resultVO.setRetData(list);
        return resultVO;
    }
}
