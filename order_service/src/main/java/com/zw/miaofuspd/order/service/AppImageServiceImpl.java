package com.zw.miaofuspd.order.service;

import com.base.util.DateUtils;
import com.zw.miaofuspd.facade.order.service.AppImageService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author xiahaiyang
 * @Create 2017年12月19日21:30:00
 **/
@Service("appImageServiceImpl")
public class AppImageServiceImpl extends AbsServiceBase implements AppImageService{
    /**
     *图片查询,当type为0时传merchandiseId,当type不为0时传orderId
     * @param id
     * @param type 0商品图片,1客户手持身份证图片,2办单员合影图片,3发货图片,4合同图片
     * @return
     */
    @Override
    public List getImagesByType(String id, String type) {
        if (!"0".equals(type)){
            String sql ="select id,img_url as imagSrc,type from mag_customer_image where type = '"+type+"' and order_id = '"+id+"' ORDER BY creat_time asc";
            List list = sunbmpDaoSupport.findForList(sql);
            return list;
        }else{
            String sql ="select id,img_url as imagSrc,type from mag_customer_image where type = '0' and merchantdise_id = '"+id+"' ORDER BY creat_time asc";
            List list = sunbmpDaoSupport.findForList(sql);
            return list;
        }
    }


    /**
     * 添加图片到相应类型,当type为0时传merchandiseId,当type不为0时传orderId
     * @param customerId
     * @param id
     * @param type
     * @return
     */
    @Override
    public Map addImageByType(String customerId,String id,String type,String imgUrl) {
        String dateString = DateUtils.getDateString(new Date());
        String uuid = UUID.randomUUID().toString();
        //商品图片，参数id对应商品的Id
        if (!"0".equals(type)){
            if("5".equals(type)) {//上传手签
                //先做删除然后在做插入
                String deleteSql = "delete from mag_customer_image where order_id='" + id + "' and type='5'";
                sunbmpDaoSupport.exeSql(deleteSql);
            }
            String sql = "insert into mag_customer_image (id,img_url,type,order_id,customer_id,state,creat_time,alter_time) " +
                    "values ('" + uuid + "','" + imgUrl + "','" + type + "','" + id + "','" + customerId + "','0','" + dateString + "','" + dateString + "');";
            sunbmpDaoSupport.exeSql(sql);
        }else{
            String sql ="insert into mag_customer_image (id,img_url,type,merchantdise_id,customer_id,state,creat_time,alter_time) " +
                    "values ('"+uuid+"','"+imgUrl+"','"+type+"','"+id+"','"+customerId+"','0','"+dateString+"','"+dateString+"');";
            sunbmpDaoSupport.exeSql(sql);
        }
        Map result = new HashMap();
        result.put("flag",true);
        result.put("msg","图片添加成功!");
        return result;
    }
    /**
     * 查看影像资料,手签资料
     * @param orderId
     */
   public Map showImageData(String orderId,String type){
       Map map =new HashMap();
       String sql ="SELECT id,img_url AS imgUrl FROM mag_customer_image WHERE order_id ='"+orderId+"' AND\n";
       StringBuffer sb1=new StringBuffer(sql);
       StringBuffer sb2 =new StringBuffer(sql);
       //影像资料
       if("yxzl".equals(type)){
           //客户手持身份证照片
           sb1.append("type ='1'");
           List  list =sunbmpDaoSupport.findForList(sb1.toString());
           if(list.size()>0){
               map.put("sczpList",list);
           }
           //办单员合影照片
           sb2.append("type='2'");
           List  list2 =sunbmpDaoSupport.findForList(sb2.toString());
           if(list.size()>0){
               map.put("khhyList",list2);
           }
       }
       //发货照片
       if("fhzp".equals(type)){
           //发货照片
           sb1.append("type='3'");
           List  list =sunbmpDaoSupport.findForList(sb1.toString());
           if(list.size()>0){
               map.put("fhzpList",list);
           }
           //合同照片
           sb2.append("type='4'");
           List  list2 =sunbmpDaoSupport.findForList(sb2.toString());
           if(list2.size()>0){
               map.put("htzpList",list2);
           }
       }
       //手签照片
       if("sqzp".equals(type)){
           sb1.append("type='5'");
           List  list =sunbmpDaoSupport.findForList(sb1.toString());
           map.put("sqzpList",list);
       }
       return map;
    }
    /**
     * 办单员端,上传保存客户合影和手持身份证照片
     * @param customerId
     * @param orderId
     * @param firstUrl
     * @param secondUrl
     * @return
     */
    @Override
    public Map addCustomerImage(String customerId, String urlType,String orderId,String firstUrl,String secondUrl) {
        Map map = new HashMap();
        if("".equals(firstUrl) || "".equals(secondUrl)){//如果为空,直接返回失败
            map.put("flag",false);
            map.put("msg","保存失败");
            return map;
        }
        if("yxzl".equals(urlType)){
            //保存之前先做一遍删除操作;
            String deleteSql = "delete from mag_customer_image where order_id='"+orderId+"' and type in(1,2)";
            sunbmpDaoSupport.exeSql(deleteSql);
            if(!"".equals(firstUrl)){//保存客户合影
                saveImage(firstUrl,"2",orderId,customerId);
            }
            if(!"".equals(secondUrl)){//保存客户手持
                saveImage(secondUrl,"1",orderId,customerId);
            }
        }
        if("fhtp".equals(urlType)){
            //保存之前先做一遍删除操作;
            String deleteSql = "delete from mag_customer_image where order_id='"+orderId+"' and type in(3,4)";
            sunbmpDaoSupport.exeSql(deleteSql);
            if(!"".equals(firstUrl)){//保存发货照片
                saveImage(firstUrl,"3",orderId,customerId);
            }
            if(!"".equals(secondUrl)){//保存合同照片
                saveImage(secondUrl,"4",orderId,customerId);
            }
        }
        map.put("flag",true);
        map.put("msg","保存成功");
        return map;
    }
    //循环保存影像资料
    public void saveImage(String customerUrl,String type,String orderId,String customerId){
        String url[] = customerUrl.split(",");
        for(int i=0;i<url.length;i++){
            String uuid = UUID.randomUUID().toString();
            String dateString = DateUtils.getDateString(new Date());
            String imgUrl = url[i];
            String sql ="insert into mag_customer_image (id,img_url,type,order_id,customer_id,state,creat_time,alter_time) " +
                    "values ('"+uuid+"','"+imgUrl+"','"+type+"','"+orderId+"','"+customerId+"','0','"+dateString+"','"+dateString+"');";
            sunbmpDaoSupport.exeSql(sql);
        }
    }
}
