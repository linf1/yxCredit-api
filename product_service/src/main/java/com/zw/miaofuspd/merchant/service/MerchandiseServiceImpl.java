package com.zw.miaofuspd.merchant.service;

import com.base.util.DateUtils;
import com.zw.miaofuspd.facade.merchant.service.MerchandiseService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 查询商户-商品接口
 */
@Service
public class MerchandiseServiceImpl extends AbsServiceBase implements MerchandiseService {
//    /**
//     * 根据商户id，分页显示商品
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public List getMerchandiseList(String merchantId,String firstIndex,String pageSize) throws Exception {
//        String sql = "SELECT\n" +
//                "\ta.merchandise_id,\n" +
//                " \tb.id as merchandiseVersionId,\n" +
//                " \tb.merchandise_name as merchandiseVersionName,\n" +
//                "  b.merchandise_type as merchandiseVersionType,\n" +
//                "\tc.id as modelId,\n" +
//                "  c.merchandise_name as merchandiseModelName,\n" +
//                "  c.merchandise_type as merchandiseModelType,\n" +
//                "\td.id as brandId,\n" +
//                "  d.merchandise_name as merchandiseBrandName,\n" +
//                " \td.merchandise_type as merchandiseBrandType,\n" +
//                "\te.id as merchandiseTypeId,\n" +
//                "  e.merchandise_name as merchandiseName,\n" +
//                " \te.merchandise_type as merchandiseType\n" +
//                "FROM\n" +
//                "\tmag_merchant_rel a\n" +
//                "LEFT JOIN mag_merchantdise b ON b.id = a.merchandise_id\n" +
//                "LEFT JOIN mag_merchantdise c ON c.id = b.parent_id \n" +
//                "LEFT JOIN mag_merchantdise d ON d.id = c.parent_id\n" +
//                "LEFT JOIN mag_merchantdise e ON e.id = d.parent_id\n" +
//                "WHERE\n" +
//                "\tmerchant_id = '"+merchantId+"' and state = '1' ORDER BY c.id limit ('"+firstIndex+"'-1)*'"+pageSize+"','"+pageSize+"'";
//        List list = sunbmpDaoSupport.findForList(sql);
//        return list;
//    }

    /**
     * 查询商户的所有的商品
     *@param merchantId 商户id
     * @return
     */
    @Override
    public List getMerchandiseList(String merchantId) throws Exception {
        String sql = "SELECT\n" +
                "\ta.merchant_id,\n" +
                "\ta.merchandise_id,\n" +
                " \tb.id as merchandiseVersionId,\n" +
                "\tb.img_url as merchandiseImgUrl,\n"+
                " \tb.merchandise_name as merchandiseVersionName,\n" +
                "\tb.merchandise_type as merchandiseVersionType,\n" +
                "\tc.id as merchandiseModelId,\n" +
                "\tc.merchandise_name as merchandiseModelName,\n" +
                "\tc.merchandise_type as merchandiseModelType,\n" +
                "\td.id as merchandiseBrandId,\n" +
                "\td.merchandise_name as merchandiseBrandName,\n" +
                "\td.merchandise_type as merchandiseBrandType,\n" +
                "\te.id as merchandiseId,\n" +
                "\te.merchandise_name as merchandiseName,\n" +
                "\te.merchandise_type as merchandiseType\n" +
                "FROM\n" +
                "\tmag_merchant_rel a\n" +
                "LEFT JOIN mag_merchantdise b ON b.id = a.merchandise_id\n" +
                "LEFT JOIN mag_merchantdise c ON c.id = b.parent_id \n" +
                "LEFT JOIN mag_merchantdise d ON d.id = c.parent_id\n" +
                "LEFT JOIN mag_merchantdise e ON e.id = d.parent_id\n" +
                "WHERE\n" +
                "\tmerchant_id = '"+merchantId+"' and a.state='0' and  b.state = '1' and c.state ='1' and d.state='1' and e.state ='1' "+" ORDER BY a.CREATE_TIME DESC ";
        List list = sunbmpDaoSupport.findForList(sql);
        return list;
    }

    @Override
    /**
     * 模糊搜索商店里的商品
     * @return
     * @throws Exception
     */
    public List searchMerchandiseList(String merchantId,String salesmanId,String merchandiseSearch) throws Exception {
        //插入一条搜索商品的历史
        insertMerchandiseRecord(merchantId,salesmanId,merchandiseSearch);
        String sql = "SELECT t.* FROM (SELECT\n" +
                "\tconcat(b.merchandise_name,c.merchandise_name,d.merchandise_name,e.merchandise_name) as merchandiseInfo,\n" +
                "\ta.merchant_id,\n" +
                "\ta.merchandise_id,\n" +
                " \tb.id as merchandiseVersionId,\n" +
                "\tb.img_url as merchandiseImgUrl,\n"+
                " \tb.merchandise_name as merchandiseVersionName,\n" +
                "\tb.merchandise_type as merchandiseVersionType,\n" +
                "\tc.id as merchandiseModelId,\n" +
                "\tc.merchandise_name as merchandiseModelName,\n" +
                "\tc.merchandise_type as merchandiseModelType,\n" +
                "\td.id as merchandiseBrandId,\n" +
                "\td.merchandise_name as merchandiseBrandName,\n" +
                "\td.merchandise_type as merchandiseBrandType,\n" +
                "\te.id as merchandiseId,\n" +
                "\te.merchandise_name as merchandiseName,\n" +
                "\te.merchandise_type as merchandiseType\n" +
                "FROM\n" +
                "\tmag_merchant_rel a\n" +
                "LEFT JOIN mag_merchantdise b ON b.id = a.merchandise_id\n" +
                "LEFT JOIN mag_merchantdise c ON c.id = b.parent_id \n" +
                "LEFT JOIN mag_merchantdise d ON d.id = c.parent_id\n" +
                "LEFT JOIN mag_merchantdise e ON e.id = d.parent_id\n" +
                "WHERE\n" +
                "\tmerchant_id = '"+merchantId+"' and  a.state ='0' and  b.state = '1' and  c.state='1'  and  d.state='1' and e.state='1' \n"+
                "\tORDER BY c.id) t\n"+
                "\t WHERE merchandiseInfo Like '%"+merchandiseSearch+"%'";
        List list = sunbmpDaoSupport.findForList(sql);
        return list;
    }

    /**
     * 模糊搜索商品的时候在那之前
     * 往搜索历史表中增加一条搜索记录
     * @param merchantId
     * @param salesmanId
     * @param merchandiseSearch
     * @throws Exception
     */
   public void insertMerchandiseRecord(String merchantId,String salesmanId,String merchandiseSearch) throws  Exception{
       String searchTime =new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
       Map map1 = new HashMap();
       map1.put("id",UUID.randomUUID().toString());
       map1.put("searchTime",searchTime);
       map1.put("merchantId",merchantId);
       map1.put("salesmanId",salesmanId);
       map1.put("searchInfo",merchandiseSearch);
       if(merchandiseSearch!="" && merchandiseSearch!=null){
           List list =showSearchList(merchantId,salesmanId);
           //有搜索历史的情况
           if(list != null && list.size()>0){
               for(int i=0;i<list.size();i++){
                   String searchHistoryId =((Map)list.get(i)).get("id").toString();
                   String searchContent= ((Map)list.get(i)).get("searchContent").toString();
                   //先判断该搜索内容是否存在搜索历史表中，有就删掉
                   if(searchContent.equals(merchandiseSearch)){
                       deleteSearchHistoryById(searchHistoryId,merchantId,salesmanId);
                       break;
                   }
               }
               //在判断搜索历史的条数是不是等于7
               Map map2 = getSearchHistoryNum(merchantId,salesmanId);
               String searchHistoryNum =map2.get("searchHistoryNum").toString();
               if("7".equals(searchHistoryNum)){
                   //获取第一条搜索历史的id
                   String searchHistoryId =((Map)list.get(list.size()-1)).get("id").toString();
                   //删除第一条搜索历史
                  deleteSearchHistoryById(searchHistoryId,merchantId,salesmanId);
                   //插入一条新的搜索历史
                  insertSearchRecord(map1);
               }else{
                   insertSearchRecord(map1);
               }
           }else{
               //没有搜索历史情况，插入一条新的搜索历史
              insertSearchRecord(map1);
           }
       }
   }

    @Override
    /**
     * 向搜索记录表中插入一条记录
     * @return
     * @throws Exception
     */
    public void insertSearchRecord(Map map) throws  Exception{
        String id=map.get("id").toString();
        String searchTime=map.get("searchTime").toString();
        String merchantId=map.get("merchantId").toString();
        String salesmanId=map.get("salesmanId").toString();
        String searchInfo=map.get("searchInfo").toString();
        String sql="INSERT INTO search_history (id,search_time,search_content,merchant_id,salesman_id,type)"
                   +"VALUES('"+id+"','"+searchTime+"','"+searchInfo+"','"+merchantId+"','"+salesmanId+"','0')";
        sunbmpDaoSupport.exeSql(sql);
    }
    /**
     * 查找搜索历史表中的搜索历史条数
     * @param merchantId 商户Id
     * @param salesmanId 办单员Id
     * @return
     */
    @Override
    public Map getSearchHistoryNum(String merchantId,String salesmanId)throws Exception{
        String sql ="SELECT COUNT(id) AS searchHistoryNum FROM search_history WHERE salesman_id='"+salesmanId+"'\n"
                +"AND  merchant_id='"+merchantId+"' AND type='0'";
        Map map = sunbmpDaoSupport.findForMap(sql);
        return map;
    }

    @Override
    /**
     * 将搜索内容按照时间倒叙排序
     * @return
     * @throws Exception
     */
    public List showSearchList(String merchantId,String salesmanId) throws Exception{
          String sql= "SELECT id, search_content AS searchContent FROM  search_history WHERE  merchant_id='"+merchantId+"'\n" +
                  "AND salesman_id='"+salesmanId+"'\n " +
                  "AND type='0' ORDER BY  STR_TO_DATE(search_time,'%Y-%m-%d %H:%i:%s') DESC";
        List list = sunbmpDaoSupport.findForList(sql);
        return list;
    }

    /**
     * 根据搜索历史Id删除搜索历史
     * @param  id
     * @merchantId 商户id
     * @salesmanId 办单员id
     */
    @Override
    public void deleteSearchHistoryById(String id,String merchantId,String salesmanId) throws Exception{
        String sql ="DELETE FROM search_history WHERE id ='"+id+"' AND\n"
                +"merchant_id='"+merchantId+"' AND\n"+
                "salesman_id ='"+salesmanId+"' AND type='0'";
        sunbmpDaoSupport.exeSql(sql);
    }

    /**
     * 清空搜索历史
     * @merchantId 商户Id
     * @salesmanId 办单员Id
     * @return
     * @throws Exception
     */

    public void dropSerarchHistroy(String merchantId,String salesmanId) throws Exception {
        String sql ="DELETE FROM  search_history\n" +
                "WHERE merchant_id='"+merchantId+"' and salesman_id ='"+salesmanId+"' and type='0'";
        sunbmpDaoSupport.exeSql(sql);
    }

    /**
     * 查询所有的商品品牌
     *
     * @return
     * @throws Exception
     */
    public List getAllMerchantdiseType() throws  Exception{
        /**
         * type为1表示商品类型
         * state 为1表示商品启用
         */
        String sql="select id ,merchandise_name from mag_merchantdise where type='1' and state = '1'";
        List list =sunbmpDaoSupport.findForList(sql);
        return list;
    }
    public List getAllType(String parentId) throws  Exception{
        String sql="select id,merchandise_name from mag_merchantdise where  state = '1' and parent_id ='"+parentId+"'";
        List list =sunbmpDaoSupport.findForList(sql);
        return list;
    }
    @Override
    public List getMerchantdiseType(String id,String type) {
        String sql ="";
        if (type.equals("splx")){
            sql = "select id,merchandise_name,merchandise_type from mag_merchantdise where type = '1' and state = '1'";
            List list = sunbmpDaoSupport.findForList(sql);
            return list;
        }else if (type.equals("ppmc")){
            sql = "select id,merchandise_name,merchandise_type from mag_merchantdise where type = '2' and parent_id ='"+id+"' and state = '1'";
            List list = sunbmpDaoSupport.findForList(sql);
            return list;
        }else if (type.equals("jtxh")){
            sql = "select id,merchandise_name,merchandise_type from mag_merchantdise where type = '3' and parent_id ='"+id+"' and state = '1'";
            List list = sunbmpDaoSupport.findForList(sql);
            return list;
        }else if (type.equals("jtbb")){
            sql = "select id,merchandise_name,merchandise_type from mag_merchantdise where type = '4' and parent_id ='"+id+"' and state = '1'";
            List list = sunbmpDaoSupport.findForList(sql);
            return list;
        }
        return null;
    }
    /**
     * 根据商品名称查询，商户id,查询该商户下的品牌
     * @param merchandiseBrandName
     * @param merchantId
     * @return
     * @throws Exception
     */
    @Override
    public List getMerchandiseInfo(String merchandiseBrandName, String merchantId) throws Exception {
        String sql = "SELECT\n" +
                "\ta.merchandise_id AS merchandiseId,\n" +
                " \tb.id as merchandiseVersionId,\n" +
                " \tb.merchandise_name as merchandiseVersionName,\n" +
                "  b.merchandise_type as merchandiseVersionType,\n" +
                "\tc.id as modelId,\n" +
                "  c.merchandise_name as merchandiseModelName,\n" +
                "  c.merchandise_type as merchandiseModelType,\n" +
                "\td.id as brandId,\n" +
                "  d.merchandise_name as merchandiseBrandName,\n" +
                " \td.merchandise_type as merchandiseBrandType,\n" +
                "\te.id as merchandiseTypeId,\n" +
                "  e.merchandise_name as merchandiseName,\n" +
                " \te.merchandise_type as merchandiseType\n" +
                "FROM\n" +
                "\tmag_merchant_rel a\n" +
                "LEFT JOIN mag_merchantdise b ON b.id = a.merchandise_id\n" +
                "LEFT JOIN mag_merchantdise c ON c.id = b.parent_id \n" +
                "LEFT JOIN mag_merchantdise d ON d.id = c.parent_id\n" +
                "LEFT JOIN mag_merchantdise e ON e.id = d.parent_id\n" +
                "WHERE\n" +
                "\tmerchant_id = '"+merchantId+"' and state = '1' and d.merchandise_name like '%"+merchandiseBrandName+"%'";
        List list = sunbmpDaoSupport.findForList(sql);
        return list;
    }

    /**
     * 根据商品id查询商户名称
     * @param merchantId  商户Id
     * @return
     * @throws Exception
     */
    @Override
    public Map findMerchantNameById(String merchantId) throws Exception{
        String sql ="SELECT mer_name AS merName FROM  mag_merchant where id ='"+merchantId+"'";
        Map map  = sunbmpDaoSupport.findForMap(sql);
        return map;
    }

    /**
     * 添加商品,新商品
     * @param map
     */
    @Override
    public Map addMerchandiseType(Map map) {
        Map result = new HashMap();
        String parentId = map.get("parent_id").toString();
        String brandName = map.get("brandName").toString();
        String modelName = map.get("xinghaoName").toString();
        String versionName = map.get("versionName").toString();
        String imgUrl = map.get("imgUrl").toString();
        String sql = "";
        //添加新商品
        //查询是否拥有品牌名称
        sql = "select id,merchandise_name,merchandise_code,merchandise_type from mag_merchantdise  "
                + "where type = '2' and merchandise_name ='" + brandName + "' and parent_id ='" + parentId + "'";
        Map brandMap = sunbmpDaoSupport.findForMap(sql);
        if (brandMap == null) {
            String brandId = UUID.randomUUID().toString();//品牌名称id
            String modelId = UUID.randomUUID().toString();//具体型号id
            String versionId = UUID.randomUUID().toString();//版本号id
            String dateString = DateUtils.getDateString(new Date());
            //添加品牌名称
            sql = "insert into mag_merchantdise (id,parent_id,merchandise_name,state,merchandise_type,type,creat_time,alter_time) values(" +
                    "'" + brandId + "','" + parentId + "','" + brandName + "','1','品牌名称','2','"+dateString+"','"+dateString+"');";
            sunbmpDaoSupport.exeSql(sql);
            //添加具体型号
            sql = "insert into mag_merchantdise (id,parent_id,merchandise_name,state,merchandise_type,type,creat_time,alter_time) values(" +
                    "'" + modelId + "','" + brandId + "','" + modelName + "','1','具体型号','3','"+dateString+"','"+dateString+"');";
            sunbmpDaoSupport.exeSql(sql);
            //添加具体版本
            sql = "insert into mag_merchantdise (id,parent_id,merchandise_name,state,merchandise_type,type,img_url,creat_time,alter_time) values(" +
                    "'" + versionId + "','" + modelId + "','" + versionName + "','1','具体版本','4','" + imgUrl + "','"+dateString+"','"+dateString+"');";
            sunbmpDaoSupport.exeSql(sql);
            result.put("versionId", versionId);
        } else {
            Map map1 = insertModelAndVersion(result, modelName, versionName, brandMap, imgUrl);
            return map1;
        }
        result.put("flag", true);
        result.put("msg", "添加新商品成功!");
        return result;
    }



    private Map insertModelAndVersion(Map result, String modelName, String versionName, Map brandMap,String imgUrl) {
        String sql="";
        String brandId = brandMap.get("id").toString();
        String versionId = UUID.randomUUID().toString();
        sql ="select id,merchandise_name,merchandise_code,merchandise_type from mag_merchantdise  " +
                    "where type = '3' and merchandise_name ='"+modelName+"' and parent_id ='"+brandId+"'";
        //查询是否拥有型号名称
        Map modelMap = sunbmpDaoSupport.findForMap(sql);
        if (modelMap == null){
            String modelId = UUID.randomUUID().toString();
            String dateString = DateUtils.getDateString(new Date());
            //添加具体型号
            sql = "insert into mag_merchantdise (id,parent_id,merchandise_name,state,merchandise_type,type,creat_time,alter_time) values(" +
                    "'"+modelId+"','"+brandId+"','"+modelName+"','1','品牌型号','3','"+dateString+"','"+dateString+"');";
            sunbmpDaoSupport.exeSql(sql);
            //添加具体版本
            sql = "insert into mag_merchantdise (id,parent_id,merchandise_name,state,merchandise_type,type,img_url,creat_time,alter_time) values(" +
                    "'"+versionId+"','"+modelId+"','"+versionName+"','1','品牌版本','4','" + imgUrl + "','"+dateString+"','"+dateString+"');";
            sunbmpDaoSupport.exeSql(sql);
            result.put("flag",true);
            result.put("msg","添加成功!");
            result.put("versionId",versionId);
            return result;
        }else{
            String modelId = modelMap.get("id").toString();
            sql = "select id,merchandise_name,merchandise_code,merchandise_type from mag_merchantdise " +
                    "where type = '4' and merchandise_name ='"+versionName+"' and parent_id ='"+modelId+"'";
             //查询是否拥有版本名称
            Map versionMap = sunbmpDaoSupport.findForMap(sql);
            if (versionMap == null){
                //添加具体版本
                String dateString = DateUtils.getDateString(new Date());
                sql = "insert into mag_merchantdise (id,parent_id,merchandise_name,state,merchandise_type,type,img_url,creat_time,alter_time) values(" +
                        "'"+versionId+"','"+modelId+"','"+versionName+"','1','品牌版本','4','" + imgUrl + "','"+dateString+"','"+dateString+"');";
                sunbmpDaoSupport.exeSql(sql);
                result.put("flag",true);
                result.put("msg","添加成功!");
                result.put("versionId",versionId);
                return result;
            }else{
                result.put("flag",false);
                result.put("msg","该商品已存在!");
                return result;
            }
        }
    }

    /**
     * 将商品添加到商户
     * @param merchandiseId 商品id
     * @return
     * @throws Exception
     */
    @Override
    public void addMerchandiseToMerchant(String merchandiseId,String merchantId) {
        String sql ="insert into mag_merchant_rel (id,merchant_id,merchandise_id,create_time,alter_time,state) values ('"+UUID.randomUUID().toString()+"'," +
                "'"+merchantId+"','"+merchandiseId+"','"+ DateUtils.getDateString(new Date())+"','"+DateUtils.getDateString(new Date())+"','0')";
        sunbmpDaoSupport.exeSql(sql);
    }
    /**
     * 将商品添加到商户
     * @param merchandiseId 商品id
     * @return
     * @throws Exception
     */
    @Override
    public Map addMerchandiseRelMerchant(String merchandiseId, String merchantId, String imgUrl) {
        Map returnMap = new HashMap();
        //先做查询,看该商品是否存在于该商户下面
        String merRelSql = "select id from mag_merchant_rel where merchant_id = '"+merchantId+"' and merchandise_id='"+merchandiseId+"' and state='0'";
        Map map = sunbmpDaoSupport.findForMap(merRelSql);
        if(map!=null){
            returnMap.put("flag",false);
            returnMap.put("msg","该商品已存在");
            return returnMap;
        }
        String sql ="insert into mag_merchant_rel (id,merchant_id,merchandise_id,create_time,alter_time,state) values ('"+UUID.randomUUID().toString()+"'," +
                "'"+merchantId+"','"+merchandiseId+"','"+ DateUtils.getDateString(new Date())+"','"+DateUtils.getDateString(new Date())+"','0')";
        sunbmpDaoSupport.exeSql(sql);
//        //更新商品表的图片缩略图
        String updateSql = "update mag_merchantdise set img_url = '"+imgUrl+"' where id = '"+merchandiseId+"'";
        sunbmpDaoSupport.exeSql(updateSql);
        returnMap.put("flag",true);
        returnMap.put("msg","添加成功");
        return returnMap;
    }

    /**
     * 商品图片和商品id关联保存在mag_customer_image表中
     * @param src 商品图片
     * @param merchandiseId 商户Id
     * @return
     * @throws Exception
     */
    public void addMerchandiseImagesToMerchandise(String src,String merchandiseId) throws Exception{
        String id =UUID.randomUUID().toString();
        String sql ="insert into mag_customer_image(id,img_url,type,merchantdise_id) values('"+id+"','"+src+"','0','"+merchandiseId+"')";
        sunbmpDaoSupport.exeSql(sql);
    }

    /**
     * 根据商品的id获取到商品的信息
     * @param id
     * @return
     */
    @Override
    public Map getMerchandiseInfoById(String id) {
        String sql = "select\n" +
                "\ta.img_url AS imgUrl,\n" +
                "  a.id AS merchandiseVersionId,\n" +
                "\ta.merchandise_name AS merchandiseVersionName,\n" +
                "  b.id AS merchandiseModelId,\n" +
                "\tb.merchandise_name AS merchandiseModelName,\n" +
                "  c.id AS merchandisBrandId,\n" +
                "\tc.merchandise_name AS merchandiseBrandName "+
                "from mag_merchantdise\ta\n" +
                "LEFT JOIN mag_merchantdise b  ON b.id = a.parent_id\n" +
                "LEFT JOIN mag_merchantdise c  ON c.id = b.parent_id\n" +
                "where a.id = '"+id+"'\n" +
                "and a.state ='1' and b.state='1' and c.state='1'";
        Map merchantdiseMap = sunbmpDaoSupport.findForMap(sql);
        return merchantdiseMap;
    }
}
