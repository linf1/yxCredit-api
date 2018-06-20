package com.zw.miaofuspd.personnal.controller;import com.alibaba.fastjson.JSONObject;import com.api.model.common.BYXResponse;import com.api.model.sortmsg.MsgRequest;import com.api.service.ds.IDSMoneyBusiness;import com.api.service.sortmsg.IMessageServer;import com.base.util.AppRouterSettings;import com.base.util.ByxFileUploadUtils;import com.exception.TextProperties;import com.zw.miaofuspd.facade.dict.service.IDictService;import com.zw.miaofuspd.facade.dict.service.ISystemDictService;import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;import com.zw.web.base.AbsBaseController;import com.zw.web.base.vo.ResultVO;import com.zw.web.base.vo.VOConst;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Controller;import org.springframework.util.MultiValueMap;import org.springframework.web.bind.annotation.PostMapping;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.ResponseBody;import org.springframework.web.multipart.MultipartFile;import org.springframework.web.multipart.MultipartHttpServletRequest;import javax.servlet.http.HttpServletRequest;import java.io.File;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import java.util.Map;import java.util.concurrent.*;/** * Created by 韩梅生 */@Controller@RequestMapping(AppRouterSettings.VERSION+AppRouterSettings.APPLY_MODULE)public class ApplyInfoController extends AbsBaseController {    private final Logger LOGGER = LoggerFactory.getLogger(ApplyInfoController.class);    @Autowired    AppBasicInfoService appBasicInfoService;    @Autowired    IDictService dictServiceImpl;    @Autowired    private IDSMoneyBusiness idsMoneyBusiness;    @Autowired    private IMessageServer messageServer;    @Autowired    private ISystemDictService iSystemDictService;    /**     * 业务端获取申请信息接口     *     * @return     */    @RequestMapping("/getApplyInfo")    @ResponseBody    public ResultVO getApplyInfo(String id,String productName) throws Exception {        ResultVO resultVO = new ResultVO();        //获取申请时的用户信息        Map personMap = appBasicInfoService.getPersonInfo(id);        if (personMap.get("code").equals("1")) {            //说明尚未填写申请信息            resultVO.setRetCode("1");        } else {                appBasicInfoService.getEmpowerStatus(id);                Map homeApplyMap = appBasicInfoService.getHomeApplyInfo(id,productName);                resultVO.setRetCode(homeApplyMap.get("code").toString());                resultVO.setRetMsg((String) homeApplyMap.get("msg"));                resultVO.setRetData(homeApplyMap.get("resMap"));        }        return resultVO;    }    /**     * 保存三要素     * @author 韩梅生     * @return     */    @RequestMapping("/addBasicInfo")    @ResponseBody    public ResultVO addBasicInfo(String data) throws Exception {        LOGGER.info("---------三要素:"+data);        Map map = JSONObject.parseObject(data);        ResultVO resultVO = new ResultVO();        Map resultMap = appBasicInfoService.addBasicCustomerInfo(map);        if(!(Boolean)(resultMap.get("flag"))){            resultVO.setErrorMsg(VOConst.FAIL,(String)(map.get("msg")));        }        resultVO.setRetMsg((String)map.get("msg"));        resultVO.setRetData(resultMap);        return resultVO;    }    /**     * @author:韩梅生     * @Description  一键申请     * @Date 15:59 2018/5/12     * @param     * @return com.zw.web.base.vo.ResultVO     */    @RequestMapping("/oneClickApply")    @ResponseBody    public ResultVO oneClickApply(String orderId,String phone) throws Exception {        ResultVO resultVO = new ResultVO();        Map map = appBasicInfoService.oneClickApply(orderId);        Map orderMap = (Map) map.get("orderMap");        if(!(Boolean)(map.get("flag"))){            resultVO.setErrorMsg(VOConst.FAIL,(String)(map.get("msg")));        }else{            //一键申请成功的时候异步同步借款人信息到碧有信            asyncExecutor(orderId);            //判断是否发送短信            if(Boolean.valueOf(TextProperties.instance().get("order.isSend"))){                sendApplyMsg(orderMap,phone);            }        }        resultVO.setRetMsg((String)map.get("msg"));        return resultVO;    }    /**     * @author:韩梅生     * @Description 用户信息强规则     * @Date 20:09 2018/5/14     * @param     */    @RequestMapping("/checkCustomerInfo")    @ResponseBody    public ResultVO checkCustomerInfo(String data) throws Exception {        Map map = JSONObject.parseObject(data);        ResultVO resultVO = new ResultVO();        Map resultMap = appBasicInfoService.checkCustomerInfo(map.get("customerId").toString(),map.get("card").toString());        if(!(Boolean)(resultMap.get("flag"))){            resultVO.setErrorMsg(VOConst.FAIL,(String)(resultMap.get("msg")));        }        resultVO.setRetMsg((String)resultMap.get("msg"));        return resultVO;    }    /**     * @author:韩梅生     * @Description 取消订单     * @Date 17:00 2018/5/19     * @param     */    @RequestMapping("/cancelOrder")    @ResponseBody    public  ResultVO cancelOrder(String orderId){        ResultVO resultVO = new ResultVO();        int i = appBasicInfoService.cancelOrder(orderId);        if(i == 0){            resultVO.setErrorMsg(VOConst.FAIL,"取消失败");        }        resultVO.setRetMsg("取消成功");        return resultVO;    }    /**     * @author:韩梅生     * @Description 获取申请信息主页面     * @Date 18:46 2018/5/19     * @param     */    @RequestMapping("/getHomeApplyInfo")    @ResponseBody    public  ResultVO getHomeApplyInfo(String id,String productName) throws  Exception{        ResultVO resultVO = new ResultVO();        appBasicInfoService.getEmpowerStatus(id);        Map homeApplyMap = appBasicInfoService.getHomeApplyInfo(id,productName);        resultVO.setRetCode(homeApplyMap.get("code").toString());        resultVO.setRetMsg((String) homeApplyMap.get("msg"));        resultVO.setRetData(homeApplyMap.get("resMap"));        return resultVO;    }    /**     * @author 韩梅生     * @date 19:43 2018/5/21     * 获取授权状态     */    @RequestMapping("/getEmpowerStatus")    @ResponseBody    public  ResultVO getEmpowerStatus(String userId) throws  Exception{        ResultVO resultVO = new ResultVO();        Map map = appBasicInfoService.getEmpowerStatus(userId);        resultVO.setRetData(map);        return resultVO;    }    /**     * 异步借款人及放款账户数据同步     * @param orderId 请求参数对象     *                orderId - 订单id     *     */    private void asyncExecutor(String orderId){        final BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();        final ThreadFactory threadFactory = Thread::new;        final ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 1,                0L, TimeUnit.MILLISECONDS, blockingQueue, threadFactory);        poolExecutor.submit(new DSMoneyRunable(this.idsMoneyBusiness,orderId));        poolExecutor.shutdown();    }    /**     * @author 韩梅生     * @date 16:16 2018/6/11     * 一键申请成功同步发送短信     * @     */    private  void sendApplyMsg(Map orderMap,String phone){        MsgRequest msgRequest = new MsgRequest();        Map<String,String> parameters = new HashMap<>(2);        msgRequest.setPhone(phone);        msgRequest.setType("0");        parameters.put("applyMoney",orderMap.get("applay_money").toString());        parameters.put("periods",orderMap.get("periods").toString());        parameters.put("productName",orderMap.get("product_name_name").toString());        parameters.put("content",TextProperties.instance().get("order.audit"));        try {            final BYXResponse byxResponse = messageServer.sendSms(msgRequest, parameters);        } catch (Exception e) {            e.printStackTrace();        }    }    /**     * @author 韩梅生     * @date 19:43 2018/6/19     * 获取影像资料     */    @RequestMapping("/getImageInfos")    @ResponseBody    public  ResultVO getImageInfos(String customerId) throws  Exception{        ResultVO resultVO = new ResultVO();        List infos = appBasicInfoService.getImageInfos(customerId);        resultVO.setRetData(infos);        return resultVO;    }    /**     * @author 韩梅生     * @date 19:43 2018/6/19     * 上传影像资料     */    @PostMapping("/uploadImageInfos")    @ResponseBody    public  ResultVO uploadImageInfos(HttpServletRequest request) throws  Exception{        //Map map = JSONObject.parseObject(data);        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;        String customerId = request.getParameter("customerId");        //MultipartFile mFile = (MultipartFile) multipartRequest.getFile("uploadImg");        //多图片上传方法        List<MultipartFile> MultipartFileList = multipartRequest.getFiles("files");        //MultiValueMap<String, MultipartFile> multiFileMap = multipartRequest.getMultiFileMap();        //String filename=filedata.getOriginalFilename();        String filePath =null;        List resultList = new ArrayList();        ResultVO resultVO = new ResultVO();        File f = null;        for(MultipartFile mfile:MultipartFileList){            resultList.add(ByxFileUploadUtils.uploadFile(iSystemDictService.getInfo("upload.url"),mfile));        }        Map resultMap = appBasicInfoService.uploadImageInfos(customerId,resultList);        if(!(Boolean)(resultMap.get("flag"))){            resultVO.setErrorMsg(VOConst.FAIL,(String)(resultMap.get("msg")));        }        resultVO.setRetMsg((String)resultMap.get("msg"));        return resultVO;    }    /**     * @author 韩梅生     * @date 19:43 2018/6/20     * 获取站内信     */    @RequestMapping("/getInstationMsg")    @ResponseBody    public  ResultVO getInstationMsg(String userId) throws  Exception{        ResultVO resultVO = new ResultVO();        List infos = appBasicInfoService.getInstationMsg(userId);        resultVO.setRetData(infos);        return resultVO;    }    /**     * @author 韩梅生     * @date 19:43 2018/6/20     * 获取站内信读取状态     */    @RequestMapping("/getInstationStatus")    @ResponseBody    public  ResultVO getInstationStatus(String userId) throws  Exception{        ResultVO resultVO = new ResultVO();        boolean status = appBasicInfoService.getInstationStatus(userId);        resultVO.setRetData(status);        return resultVO;    }    /**     * @author 韩梅生     * @date 19:43 2018/6/20     * 更新站内信读取状态     */    @RequestMapping("/updateInstationStatus")    @ResponseBody    public  ResultVO updateInstationMsg(String userId) throws  Exception{        ResultVO resultVO = new ResultVO();        appBasicInfoService.updateInstationMsg(userId);        return resultVO;    }    /**     * @author 韩梅生     * @date 19:43 2018/6/20     * 删除影像资料     */    @RequestMapping("/deleteImageInfos")    @ResponseBody    public  ResultVO deleteImageInfos(String id) throws  Exception{        ResultVO resultVO = new ResultVO();        appBasicInfoService.deleteImageInfos(id);        return resultVO;    }}