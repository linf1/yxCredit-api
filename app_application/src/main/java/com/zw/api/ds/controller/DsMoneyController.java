package com.zw.api.ds.controller;

import com.api.model.common.BYXResponse;
import com.api.model.ds.DSMoneyRequest;
import com.api.service.ds.IDSMoneyBusiness;
import com.base.util.AppRouterSettings;
import com.base.util.StringUtils;
import com.zw.web.base.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 借款人及放款账户数据同步控制器
 * @author 陈清玉
 */
@RestController
@RequestMapping(AppRouterSettings.VERSION + "/ds")
public class DsMoneyController {

    private final Logger LOGGER = LoggerFactory.getLogger(DsMoneyController.class);

    @Autowired
    private IDSMoneyBusiness idsMoneyBusiness;


    /**
     * 根据订单ID同步借款人信息
     * @param request 订单ID
     * @return {@link ResultVO}
     */
    @PostMapping("/dsBorrowerAndAccountCard")
    public ResultVO dsBorrowerAndAccountCard(DSMoneyRequest request) {
        LOGGER.info("==============借款人及放款账户数据同步开始========参数：{}",request.toString());
        if (StringUtils.isNotBlank(request.getAppUserId())) {
            try {
                BYXResponse  byxResponse = idsMoneyBusiness.syncData(request);
                if(byxResponse != null) {
                    if (BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())) {
                        return ResultVO.ok("借款人及放款账户数据同步成功");
                    }
                    ResultVO.error(byxResponse.getRes_msg());
                }
            } catch (Exception e) {
                e.printStackTrace();
                ResultVO.error(e.getMessage());
            }
        }
        return ResultVO.error();
    }

}
