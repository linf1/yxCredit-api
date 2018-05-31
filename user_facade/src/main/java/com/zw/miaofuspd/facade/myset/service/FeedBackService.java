package com.zw.miaofuspd.facade.myset.service;


import com.zw.miaofuspd.facade.entity.AppUserInfo;

import java.util.Map;


public interface FeedBackService {
     /**
      * 添加客户意见反馈
      * @author 仙海峰
      * @param userId
      * @param content
      * @return
      * @throws Exception
      */
     Map<String, String> feedbackAdd(String userId, String content) throws Exception;
}
