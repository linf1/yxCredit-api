package com.zw.miaofuspd.personnal.controller;

import com.base.util.AppRouterSettings;
import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.entity.CustomerLinkmanBean;
import com.zw.miaofuspd.facade.personal.service.AppBasicInfoService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/13 0013.
 */
@Controller
@RequestMapping(AppRouterSettings.LINKMAN_MODUAL)
public class LinkManController extends AbsBaseController {
    @Autowired
    AppBasicInfoService appBasicInfoService;
    @Autowired
    IDictService dictServiceImpl;
    /**
     * 添加联系人
     * String orderId, 订单id
     * String linkId1,联系人1id String relationship1, 联系人关系
     * String relationshipname1,联系人关系名字 String contact1, 联系人联系方式
     * @return
     */
    @RequestMapping("/updateLinkManInfo")
    @ResponseBody
    public ResultVO updateLinkManInfo(String customerId, String linkId1, String relationship1,String relationshipName1, String linkName1, String contact1, String linkId2, String relationship2,String relationshipName2, String linkName2, String contact2, String linkId3, String relationship3,
                                      String relationshipName3, String linkName3, String contact3, String linkId4, String relationship4,String relationshipName4, String linkName4, String contact4, String linkId5, String relationship5,String relationshipName5, String linkName5, String contact5, String linkId6, String relationship6,String relationshipName6, String linkName6, String contact6
    ) throws Exception{
        ResultVO resultVO = new ResultVO();
        //AppUserInfo userInfo = (AppUserInfo) this.getHttpSession().getAttribute(AppConstant.APP_USER_INFO);
        CustomerLinkmanBean customerLinkmanBean = new CustomerLinkmanBean();
        customerLinkmanBean.setId(linkId1);
        customerLinkmanBean.setRelationShip(relationship1);
        customerLinkmanBean.setContact(contact1);
        customerLinkmanBean.setLinkName(linkName1);
        customerLinkmanBean.setRelationshipName(relationshipName1);

        CustomerLinkmanBean customerLinkmanBean2 = new CustomerLinkmanBean();
        customerLinkmanBean2.setId(linkId2);
        customerLinkmanBean2.setRelationShip(relationship2);
        customerLinkmanBean2.setContact(contact2);
        customerLinkmanBean2.setLinkName(linkName2);
        customerLinkmanBean2.setRelationshipName(relationshipName2);

        CustomerLinkmanBean customerLinkmanBean3 = new CustomerLinkmanBean();
        customerLinkmanBean3.setId(linkId3);
        customerLinkmanBean3.setRelationShip(relationship3);
        customerLinkmanBean3.setContact(contact3);
        customerLinkmanBean3.setLinkName(linkName3);
        customerLinkmanBean3.setRelationshipName(relationshipName3);

        CustomerLinkmanBean customerLinkmanBean4 = new CustomerLinkmanBean();
        customerLinkmanBean4.setId(linkId4);
        customerLinkmanBean4.setRelationShip(relationship4);
        customerLinkmanBean4.setContact(contact4);
        customerLinkmanBean4.setLinkName(linkName4);
        customerLinkmanBean4.setRelationshipName(relationshipName4);

        CustomerLinkmanBean customerLinkmanBean5 = new CustomerLinkmanBean();
        customerLinkmanBean5.setId(linkId5);
        customerLinkmanBean5.setRelationShip(relationship5);
        customerLinkmanBean5.setContact(contact5);
        customerLinkmanBean5.setLinkName(linkName5);
        customerLinkmanBean5.setRelationshipName(relationshipName5);

        CustomerLinkmanBean customerLinkmanBean6 = new CustomerLinkmanBean();
        customerLinkmanBean6.setId(linkId6);
        customerLinkmanBean6.setRelationShip(relationship6);
        customerLinkmanBean6.setContact(contact6);
        customerLinkmanBean6.setLinkName(linkName6);
        customerLinkmanBean6.setRelationshipName(relationshipName6);


        Map map1 = new HashMap();
        map1.put("customerLinkmanBean",customerLinkmanBean);
        map1.put("customerLinkmanBean2",customerLinkmanBean2);
        map1.put("customerLinkmanBean3",customerLinkmanBean3);
        map1.put("customerLinkmanBean4",customerLinkmanBean4);
        map1.put("customerLinkmanBean5",customerLinkmanBean5);
        map1.put("customerLinkmanBean6",customerLinkmanBean6);
        Map map = appBasicInfoService.updateLinkManInfo(customerId, map1);
        boolean flag = (boolean) map.get("success");
        String msg = map.get("msg").toString();
        if(!flag){
            resultVO.setErrorMsg(VOConst.FAIL,msg);
            return resultVO;
        }
        resultVO.setRetMsg(msg);
        return resultVO;
    }
    /**
     * 获取联系人
     * @author 韩梅生
     * @return
     */
    @RequestMapping("/getLinkMan")
    @ResponseBody
    public ResultVO getLinkMan() throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) this.getHttpSession().getAttribute(AppConstant.APP_USER_INFO);
        Map map = appBasicInfoService.getLinkMan(userInfo.getCustomer_id());
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     * 保存通讯录信息
     * @author 韩梅生
     * @return
     */
    @RequestMapping("/saveTongXunLu")
    @ResponseBody
    public ResultVO saveTongXunLu(String data) throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) this.getHttpSession().getAttribute(AppConstant.APP_USER_INFO);
        appBasicInfoService.saveTongXunLu(userInfo.getCustomer_id(),data);
        return resultVO;
    }
    /**
     * 获取直属关系
     * @return  ResultVO
     * **/
    @RequestMapping("/getDirectRelationShip")
    @ResponseBody
    public ResultVO getDirectRelationShip() throws Exception{
        List relLinkManList = dictServiceImpl.getDictJson("直系联系人");
        return this.createResultVO(relLinkManList);
    }

    /**
     * 获取其他关系
     * @return  ResultVO
     * **/
    @RequestMapping("/getOtherRelationShip")
    @ResponseBody
    public ResultVO getOtherRelationShip() throws Exception{
        List otherLinkManList = dictServiceImpl.getDictJson("其他联系人");
        return this.createResultVO(otherLinkManList);
    }
}
