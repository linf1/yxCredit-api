package com.zw.miaofuspd.facade.myset.service;


import com.zw.miaofuspd.facade.entity.AppUserInfo;

import java.util.Map;

/**
 * Created by Administrator on 2017/2/28.
 */
public interface FeedBackService {
     Map<String, String> feedbackAdd(AppUserInfo userinfo, String content) throws Exception;
}
