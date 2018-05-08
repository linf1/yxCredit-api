package com.zw.miaofuspd.facade.order.service;

import java.util.List;
import java.util.Map;

public interface AppImageService {
    /**
     *图片查询,当type为0时传merchandiseId,当type不为0时传orderId
     * @param id
     * @param type 0商品图片,1客户手持身份证图片,2办单员合影图片,3发货图片,4合同图片
     * @return
     */
    List getImagesByType(String id, String type);

    /**
     * 添加图片到相应类型,当type为0时传merchandiseId,当type不为0时传orderId
     * @param customerId
     * @param id
     * @param type
     * @return
     */
    Map addImageByType(String customerId,String id,String type,String imgUrl);

    /**
     * 查看影像资料,手签资料
     * @param orderId
     */
    Map showImageData(String orderId,String type);

    /**
     * 添加图片到相应类型,当type为0时传merchandiseId,当type不为0时传orderId
     * @param customerId
     * @param id
     * @return
     */
    Map addCustomerImage(String customerId,String urlType,String id,String firstUrl,String secondUrl);

}
