package com.zw.api.shujumohe.controller;

import com.api.model.shujumohe.ShujumoheRequest;
import com.api.service.shujumohe.IShujumoheServer;
import com.zw.web.base.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 魔盒服务控制
 * @author 陈清玉
 */
@RestController
@RequestMapping("/shujumohe" )
public class ShujumoheController {

    private final Logger LOGGER = LoggerFactory.getLogger(ShujumoheController.class);

    @Autowired
    private IShujumoheServer shujumoheServerImpl;

    @RequestMapping("callBackShujumohe")
    public ResultVO callBackShujumohe(ShujumoheRequest request){
        LOGGER.info("========request:{}",request.toString());
        return ResultVO.ok(shujumoheServerImpl.callbackShujumohe(request));
    }
}
