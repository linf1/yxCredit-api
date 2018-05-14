package com.zw.miaofuspd.personnal.controller;

import com.alibaba.fastjson.JSONObject;
import com.base.util.AppRouterSettings;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.miaofuspd.facade.user.service.IUserService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by 韩梅生 on 2018/05/09 0028.
 */
@RestController
@RequestMapping(AppRouterSettings.BASIC_MODUAL)
public class BasicInfoController extends AbsBaseController {
    @Autowired
    AppBasicInfoService appBasicInfoService;
    @Autowired
    private IUserService userService;

    /**
     * @author 韩梅生
     * 保存用户申请的基本信息
     * @param data
     * @return
     */
    @RequestMapping("/addApplyInfo")
    public ResultVO addApplyInfo(String data) throws Exception{
        Map map = JSONObject.parseObject(data);
        ResultVO resultVO = new ResultVO();
        Map resultMap = appBasicInfoService.addApplyInfo(map);
        if(!(Boolean)(resultMap.get("flag"))){
            resultVO.setErrorMsg(VOConst.FAIL,(String)(resultMap.get("msg")));
        }
        resultVO.setRetData(resultMap);
        return resultVO;
    }

    /**
     * @author 韩梅生
     * 获取用户申请的基本信息
     * @param
     * @return
     */
    @RequestMapping("/getApplyInfo")
    public ResultVO getApplyInfo(String orderId) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = appBasicInfoService.getApplyInfo(orderId);
        resultVO.setRetData(map);
        return resultVO;
    }

    /**
     * @author:韩梅生
     * @Description  保存用户的个人信息
     * @Date 14:07 2018/5/12
     * @param
     * @return
     */
    @RequestMapping("/addBasicInfo")
    public ResultVO addBasicInfo(String data) throws Exception{
        Map map = JSONObject.parseObject(data);
        ResultVO resultVO = new ResultVO();
        Map resultMap = appBasicInfoService.addBasicInfo(map);
        if(!(Boolean)(resultMap.get("flag"))){
            resultVO.setErrorMsg(VOConst.FAIL,(String)(resultMap.get("msg")));
        }
        resultVO.setRetData(resultMap);
        return resultVO;
    }




    /**
     * @author:韩梅生
     * @Description 获取用户的个人信息
     * @Date 14:06 2018/5/12
     * @param
     * @return
     */
    @RequestMapping("/getBasicInfo")
    public ResultVO getBasicInfo(String customerId) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = appBasicInfoService.getBasicInfo(customerId);
        resultVO.setRetData(map);
        return resultVO;
    }




    /**
     * @author:韩梅生
     * @Description  获取省份信息
     * @Date 13:40 2018/5/12
     * @param
     * @return com.zw.web.base.vo.ResultVO
     */
    @RequestMapping("/getProvinceList")
    public ResultVO getProvinceList() throws Exception{
        ResultVO resultVO = new ResultVO();
        List list = appBasicInfoService.getProvinceList();
        resultVO.setRetData(list);
        return resultVO;
    }

    /**
     * @author:韩梅生
     * @Description  获取市的信息
     * @Date 13:54 2018/5/12
     * @param  provinceId 省id
     * @return
     */
    @RequestMapping("/getCityList/{provinceId}")
    public ResultVO getCityList(@PathVariable String provinceId) throws Exception{
        ResultVO resultVO = new ResultVO();
        List list = appBasicInfoService.getCityList(provinceId);
        resultVO.setRetData(list);
        return resultVO;
    }

    /**
     * @author:韩梅生
     * @Description  获取区的信息
     * @Date 13:54 2018/5/12
     * @param  cityId 市的id
     * @return
     */
    @RequestMapping("/getDistrictList")
    public ResultVO getDistrictList(String cityId) throws Exception{
        ResultVO resultVO = new ResultVO();
        List list = appBasicInfoService.getDistrictList(cityId);
        resultVO.setRetData(list);
        return resultVO;
    }

}
