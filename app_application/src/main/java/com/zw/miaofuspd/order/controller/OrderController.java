package com.zw.miaofuspd.order.controller;

import com.base.util.AppRouterSettings;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Administrator on 2017/11/27 0027.
 */
@Controller
@RequestMapping(AppRouterSettings.VERSION+"/order")
public class  OrderController extends AbsBaseController {
    @Autowired
    private AppOrderService appOrderService;
    /****************************************************碧友信**********************************************/
    /**
     * 获取该用户下全部订单
     * @author 仙海峰
     * @param userId
     * @return
     */
    @RequestMapping("/getAllOrder")
    @ResponseBody
    public ResultVO getAllOrder(String userId,String pageNumber,String pageSize,String orderType) {
        ResultVO resultVO = new ResultVO();

        //根据userId获取全部订单
        Map map  = appOrderService.getAllOrderByUserId(userId,pageNumber,pageSize,orderType);

        resultVO.setRetData(map);

        return resultVO;
    }

    /**
     * 获取订单所有信息详情
     * @author 仙海峰
     * @param orderId
     * @return
     */
    @RequestMapping("/getOrderInFo")
    @ResponseBody
    public ResultVO getOrderInFo(String orderId) {
        ResultVO resultVO = new ResultVO();

        //获取此订单全部信息记录
        Map map  = appOrderService.getOrderAllInFoByOrderId(orderId);
        resultVO.setRetData(map);

        return resultVO;
    }

    /**
     * 根据订单ID修改订单状态
     * @author 仙海峰
     * @param orderId
     * @param state
     * @return
     */
    @RequestMapping("/updateOrderStatus")
    @ResponseBody
    public ResultVO updateOrderStatus(String orderId,String state) throws Exception {
        ResultVO resultVO = new ResultVO();

        //根据订单ID修改订单状态
        Map map  = appOrderService.updateOrderStatusByOrderId(orderId,state);
        resultVO.setRetData(map);

        return resultVO;
    }


    /**
     * 根据订单ID完成签约
     * @author 仙海峰
     * @param orderId
     * @return
     */
    @RequestMapping("/contractForSubmission")
    @ResponseBody
    public ResultVO contractForSubmission(String orderId ,String userId) throws Exception{
        ResultVO resultVO = new ResultVO();

        //根据orderId，userId完成签约提交
        Map map  = appOrderService.contractForSubmissionByOrderId(orderId,userId);
        resultVO.setRetData(map);

        return resultVO;
    }



}
