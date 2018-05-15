package com.zw.api.ds.controller;

import com.alibaba.fastjson.JSONObject;
import com.api.model.common.BYXResponse;
import com.api.model.ds.DSMoneyRequest;
import com.api.service.ds.IDSMoneyServer;
import com.base.util.AppRouterSettings;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 借款人及放款账户数据同步控制器
 * @author 陈清玉
 */
@RestController
@RequestMapping(AppRouterSettings.VERSION +AppRouterSettings.API_MODULE + "/ds")
public class DSMoneyController {

    private IDSMoneyServer idsMoneyServer;

    @PostMapping("/synchroBorrowerAndAccountCard")
    public BYXResponse synchroBorrowerAndAccountCard() {
        DSMoneyRequest request = new DSMoneyRequest();
        String jsonString = idsMoneyServer.saveBorrowerAndAccountCard(request);
        if (jsonString != null) {
            BYXResponse.ok(JSONObject.parseObject(jsonString, BYXResponse.class));
        }
        return  BYXResponse.error();
    }

}
