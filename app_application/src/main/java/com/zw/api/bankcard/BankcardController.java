package com.zw.api.bankcard;

import com.api.model.bankcard.BankcardRequest;
import com.api.service.bankcard.IBankcardServer;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 银行四要素
 * @author 陈清玉
 */
@RestController
@RequestMapping("/api/bankcard")
public class BankcardController {
    @Autowired
    private IBankcardServer bankcardServer;

    @PostMapping("/authsms")
    public ResultVO authsms(BankcardRequest bankcardRequest) {
        if(bankcardRequest == null){
            return ResultVO.error("参数异常");
        }
        try {
            final String authsms = bankcardServer.authsms(bankcardRequest);
            return ResultVO.ok(authsms);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage());
        }
    }

    @PostMapping("/authconfirm")
    public ResultVO authconfirm(BankcardRequest bankcardRequest) {
        try {
                if (bankcardRequest == null) {
                    return ResultVO.error("参数异常");
                }

                final String authconfirm = bankcardServer.authconfirm(bankcardRequest);
                return ResultVO.ok(authconfirm);
            } catch (Exception e) {
                e.printStackTrace();
                return ResultVO.error(e.getMessage());
            }
    }

}
