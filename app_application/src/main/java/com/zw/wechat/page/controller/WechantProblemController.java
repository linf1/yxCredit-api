package com.zw.wechat.page.controller;

import com.zw.miaofuspd.facade.myset.service.IAppMessageService;
import com.zw.web.base.AbsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/25 0025.
 */
@Controller
@RequestMapping("/wechat/problem")
public class WechantProblemController extends AbsBaseController {
    @Resource
    public IAppMessageService appMessageService;
    //常见问题
    @RequestMapping("/getFaqList")
    public String getFaqList(ModelMap mv) throws Exception {
        List<Map> list = appMessageService.getFaqList();
        Map paramMap = new HashMap();
        List resultList = null;
        for (int i = 0;i<list.size();i++){
            resultList = new ArrayList();
            String problem_type_name = list.get(i).get("problem_type_name").toString();
            for (int j = i;j<list.size();j++,i++){
                if(list.get(j).get("problem_type_name").toString().equals(problem_type_name)){
                    resultList.add(list.get(j));
                }else{
                    break;
                }
            }
            paramMap.put(problem_type_name,resultList);
        }
        mv.put("data",paramMap);
        return "merch-wechat/mine/comQuestions";
    }
}
