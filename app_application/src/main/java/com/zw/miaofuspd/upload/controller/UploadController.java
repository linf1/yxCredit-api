package com.zw.miaofuspd.upload.controller;
import com.base.util.FileNewName;
import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.personal.service.AppUserService;
import com.zw.util.UploadFileUtil;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.*;

/**
 * Created by Administrator on 2017/11/14 0014.
 */
@Controller
@RequestMapping("/oss")
public class UploadController extends AbsBaseController {
    @Autowired
    private AppUserService appUserServiceImpl;
    @Autowired
    private ISystemDictService iSystemDictService;
    /**
     * 上传头像到阿里云
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/upload")
    public ResultVO upload() throws Exception {
        //获取用户的信息
        AppUserInfo userInfo = (AppUserInfo) getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        //获取图片路劲
        String imgPath = getHttpSession().getServletContext().getRealPath("/fintecher_file");
        //生成新的文件名
        String fileName = FileNewName.gatFileName();
        String id = UUID.randomUUID().toString();
        String url = "image";
        //获得头像路劲
        String catalog="fintecher_file"+"/"+url+"/";
        String bucket = iSystemDictService.getInfo("oss.bucket");
        String endpoint = iSystemDictService.getInfo("oss.endpoint");
        String accessKeyId = iSystemDictService.getInfo("oss.accessKeyId");
        String accessKeySecret = iSystemDictService.getInfo("oss.accessKeySecret");
        Map<String, Object> map = UploadFileUtil.getFileOSS(getRequest(), imgPath+ File.separator + url, id,catalog,bucket,endpoint,accessKeyId,accessKeySecret);
        appUserServiceImpl.updateUrl(map,userInfo);
        Map map1 = new HashMap();
        map1.put("fileList",map.get("fileList"));
        return this.createResultVO(map1);
    }

    /**
     * 获取我的页面的头像路径和姓名
     * @return
     * @throws Exception
     */
    @RequestMapping("/getPage")
    @ResponseBody
    public ResultVO getPage() throws Exception {
        //获取用户的信息
        AppUserInfo userInfo = (AppUserInfo) getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        List list = appUserServiceImpl.getImgInfo(userInfo);
        ResultVO resultVO = new ResultVO();
        if (list != null && list.size() > 0) {
            resultVO.setRetCode("SUCCESS");
            Map map = (Map) list.get(0);
            resultVO.setRetData(map);
        } else {
            resultVO.setRetCode("FAIL");
        }
        return resultVO;
    }
}
