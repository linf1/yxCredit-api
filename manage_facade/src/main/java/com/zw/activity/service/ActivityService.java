package com.zw.activity.service;

import java.util.Map;

public interface ActivityService {

    /**
     *获取已上架Banner列表
     * @return
     * @throws Exception
     */
    Map<String, String> getBannerList() throws Exception;


}
