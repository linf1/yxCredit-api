package com.zw.api.bankcard;

import com.api.model.bankcard.BankcardRequest;
import com.api.service.bankcard.IBankcardServer;
import com.base.util.AppRouterSettings;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


/**
 * 银行四要素
 * @author 陈清玉
 */
@RestController
@RequestMapping(AppRouterSettings.VERSION +AppRouterSettings.API_MODULE + "/bankcard")
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


    /**
     * 获取银行列表
     * @return 营业银行json字符串
     */
    @PostMapping("/getBankList")
    public ResultVO getBankList() {
        try {
            return ResultVO.ok(bankcardServer.getBankList());
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage());
        }
    }
    /**
     * 获取银行列表
     * @param regionId 市id
     * @param bankCode 银行行别代码
     * @return 支营业银行列表json字符串
     */
    @PostMapping("/getSubBankList/{regionId}/{bankCode}")
    public ResultVO getSubBankList(@PathVariable String regionId,@PathVariable String bankCode) {
        try {
            return ResultVO.ok(bankcardServer.getSubBankList(regionId,bankCode));
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage());
        }
    }
    /**
     * 获取城市列表
     * @param provinceId 省ID
     * @return 城市列表json字符串
     * @exception  IOException 请求异常
     */
    @PostMapping("/getCityList")
    public ResultVO getCityList(@PathVariable String provinceId) {
        try {
            return ResultVO.ok(bankcardServer.getCityList(provinceId));
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage());
        }
    }
    /**
     * 获取省列表
     * @return 省列表json字符串
     */
    @PostMapping("/getProvinceList")
    public ResultVO getProvinceList(){
        try {
            return ResultVO.ok(bankcardServer.getProvinceList());
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVO.error(e.getMessage());
        }
    }

}
