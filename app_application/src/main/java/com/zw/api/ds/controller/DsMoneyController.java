package com.zw.api.ds.controller;

import com.alibaba.fastjson.JSONObject;
import com.api.model.common.BYXResponse;
import com.api.model.ds.DSMoneyRequest;
import com.api.service.ds.IDSMoneyServer;
import com.base.util.AppRouterSettings;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 借款人及放款账户数据同步控制器
 * @author 陈清玉
 */
@RestController
@RequestMapping(AppRouterSettings.VERSION +AppRouterSettings.API_MODULE + "/ds")
public class DsMoneyController {

    @Autowired
    private IDSMoneyServer idsMoneyServer;

    /**
     *
     * @param request
     * @return
     */
    @PostMapping("/dsBorrowerAndAccountCard")
    public ResultVO dsBorrowerAndAccountCard(DSMoneyRequest request) {
        try {
            final BYXResponse byxResponse = idsMoneyServer.saveBorrowerAndAccountCard(request);
            if (BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
                return  ResultVO.ok(byxResponse.getRes_data());
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResultVO.error(e.getMessage());
        }
        return  ResultVO.error();
    }

}
