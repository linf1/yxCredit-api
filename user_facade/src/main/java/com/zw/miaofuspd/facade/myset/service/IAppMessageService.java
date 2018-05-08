package com.zw.miaofuspd.facade.myset.service;

import java.util.List;
import java.util.Map;

/**
 * Created by Win7 on 2017/2/27.
 */
public interface IAppMessageService {
    /**
     * 根据用户id查询信息列表
     * @return
     */
    Map getMessageList(String id) throws Exception ;
    /**
     * 查询是否开户
     * @param channel
     * @param customer_id
     * @return
     */
    /**
     * 根据id获取未读消息及消息数
     * @param id
     * @return
     */
    Map getUnMessage(String id) throws Exception ;
    /**
     * 修改消息读取状态
     * @param id
     * @return
     */
    void setReadyMsg(String id) throws Exception ;
    List getPage() throws Exception;
    List getFaqList();
    List getPictureInfo(String type) throws Exception;
}
