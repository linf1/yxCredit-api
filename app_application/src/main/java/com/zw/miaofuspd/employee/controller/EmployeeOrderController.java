package com.zw.miaofuspd.employee.controller;
import com.zw.miaofuspd.facade.openaccount.service.IRepayPlanService;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by Administrator on 2017/12/22.
 */
@Controller
@RequestMapping("/employeeOrder")

public class EmployeeOrderController extends AbsBaseController {
    @Autowired
    private AppOrderService appOrderService;
    @Autowired
    private IRepayPlanService iRepayPlanService;
    /**
     * 通过业务员编号获取该业务员下所有订单信息
     * @param employeeId 办单员Id
     * @return
     * @throws Exception
     */
    @RequestMapping("/getAllOrder")
    @ResponseBody
    public ResultVO getAllOrder(String employeeId,String type) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = appOrderService.getAllOrder(employeeId,type);
        boolean flag= (boolean) map.get("flag");
        if(!flag){
            resultVO.setErrorMsg(VOConst.FAIL,"当前无订单");
            return resultVO;
        }
        map.remove("flag");
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     * 办单员端模糊搜索订单
     * @param  employeeId 办单员Id
     * @param  searchContent 搜索内容
     * @param  state 订单的状态
     * @type  搜索历史类型
     *  state
     *  1: 全部订单  2:未提交订单
     *  3:审核中订单 4:内拒单
     *  5.审批通过的订单
     *  6.待发货订单
     *  7.已发货订单
     * */
    @RequestMapping("/searchOrder")
    @ResponseBody
    public ResultVO searchOrderLike(String employeeId,String merchantId,String searchContent,String state,String type) throws Exception {
        ResultVO resultVO = new ResultVO();
        Map map = appOrderService.findOrderLike(employeeId,merchantId,searchContent,state,type);
        boolean flag=(boolean) map.get("flag");
        if(!flag){
            resultVO.setErrorMsg(VOConst.FAIL,"没有符合条件的订单");
            return resultVO;
        }
        map.remove("flag");
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     * 清空订单搜索历史记录
     * @param employeeId 办单员Id
     * @param type 搜索历史记录类型
     * @return
     * @throws Exception
     */
    @RequestMapping("/dropOrderSearchHistory")
    @ResponseBody
    public ResultVO dropOrderSearchHistroy(String employeeId,String merchantId,String type) throws  Exception{
        ResultVO resultVO = new ResultVO();
        Map map = appOrderService.deleteOrderSearchHistory(employeeId,merchantId,type);
        Boolean flag = (Boolean) map.get("flag");
        if(flag){
            resultVO.setRetMsg("搜索历史已清空");
        }
        return resultVO;
    }
    /**
     * 展示订单搜索历史的前几条记录
     * @param employeeId 办单员Id
     */
    @RequestMapping("/showOrderSearchHistory")
    @ResponseBody
    public ResultVO getOrderSearchHistory(String employeeId,String merchantId,String type) throws  Exception{
        ResultVO resultVO =new ResultVO();
        Map map =new HashMap();
        List list =appOrderService.showOrderSearchHistory(employeeId,merchantId,type);
        if(list.size()>0){
            map.put("searchHistoryList",list);
            resultVO.setRetData(map);
        }else{
            map.put("searchHistoryList",new ArrayList());
            resultVO.setRetData(map);
        }
        return resultVO;
    }

    /**
     * 修改订单的状态，用于订单提交之前
     * 完善信息，上传影像资料，手签的环节
     * @param orderId 订单Id
     * @param state  订单状态
     */
    @RequestMapping("/updateOrderStateBeforeSubmit")
    @ResponseBody
    public ResultVO updateOrderStateBeforeSubmit(String orderId,String state) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map orderMap = appOrderService.getOrderById(orderId);//先查出原始订单的状态,防止在补充发货照片的时候改变状态
        String commodityState = (String) orderMap.get("commodityState");
        if(!"19".equals(commodityState) && !"20".equals(commodityState)){
            if("19".equals(state)){//提货,,此时生成还款计划
                try{
                    iRepayPlanService.addRepayment(orderId);//生成还款计划
                }catch (Exception e){
                }
            }
            Map map = appOrderService.updateOrderStateBeforeSubmit(orderId,state);
            if (!(boolean)map.get("flag")){
                resultVO.setRetCode(VOConst.FAIL);
            }
        }
        return resultVO;
    }
    /**
     * 订单提交修改状态
     * @param orderId 订单Id
     * @param state  订单状态
     * @param isSign
     */
    @RequestMapping("/submitUpdateState")
    @ResponseBody
    public ResultVO submitUpdateStatus(String orderId,String state,String isSign) throws Exception {
        ResultVO resultVO = new ResultVO();
        Map map = appOrderService.submitUpdateStatus(orderId,state,isSign);
        if (!(boolean)map.get("flag")){
            resultVO.setRetCode(VOConst.FAIL);
        }
        return resultVO;
    }

    /**
     * 提交商品串号码
     * @param orderId
     * @param merCode
     * @return
     */
    @RequestMapping("/submitMerchandiseCode")
    @ResponseBody
    public ResultVO submitMerchandiseCode(String orderId,String merCode,String state){
        ResultVO resultVO = new ResultVO();
        appOrderService.setMerchandiseCode(orderId,merCode,state);
        return resultVO;
    }
    /**
     *查询客户是否进行实名认证
     * @param orderId 订单Id
     * @return
     */
    @RequestMapping("/improveCustomerInformation")
    @ResponseBody
    public ResultVO improveCustomerInformation(String orderId) throws Exception {
        ResultVO resultVO = new ResultVO();
        Map map  =appOrderService.getCustomerIdByOrderId(orderId);
        //没有实名认证的情况
        if (!(boolean)map.get("flag")){
            resultVO.setRetCode(VOConst.FAIL);
        }
        return resultVO;
    }
}
