/**
 * 将XML转JSON String、将JSON转Map
 *
 * @author 宝付（大圣）
 */
package com.zw.miaofuspd.util;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.util.*;

public class JXMConvertUtil{

    /**
     * 将XML String转成JSON String
     * @param XMLString
     * @return
     */
    public static String XmlConvertJson(String XMLString) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        JSON jsobj = (JSON) xmlSerializer.read(XMLString);
        return jsobj.toString();

    }

    /**
     * @截取XML节点值
     * @param XMLString
     * @param KeyName
     * @return
     * @throws Exception
     */

    public static String CutXMLStr(String XMLString,String KeyName) throws Exception{
        SAXReader sax = new SAXReader();
        Document document = sax.read(new ByteArrayInputStream(XMLString.getBytes("UTF-8")));//reader为定义的一个字符串，可以转换为xml
        Element root = document.getRootElement();
        return getElementErgodic(root.element(KeyName));
    }

    /**
     * @遍历Element供CutXMLStr使用
     * @param element
     * @return  返回XML节点
     */

    @SuppressWarnings("rawtypes")
    private static String getElementErgodic(Element element)
    {
        StringBuilder XMLStr = new StringBuilder();
        for(Iterator it=element.elementIterator();it.hasNext();){
            Element elementTmp = (Element) it.next();
            XMLStr.append("<").append(elementTmp.getName()).append(">");
            XMLStr.append(elementTmp.getText());
            XMLStr.append("</"+elementTmp.getName()+">");
        }
        return XMLStr.toString();
    }



    /**
     * 将JSON转换成Map
     * @param object
     * @return
     *
     */

    public static Map<String, Object> JsonConvertHashMap(Object object)
    {
        Map<String, Object> RMap = new HashMap<String, Object>();
        // 将json字符串转换成jsonObject
        JSONObject jsonObject = JSONObject.fromObject(object);
        RMap=IteratorHash(jsonObject);

        return RMap;
    }


    /**
     * 递归遍历JSON对象。
     *
     * @param JsonToMap
     * @return
     *
     *
     */
    public static Map<String,Object> IteratorHash(JSONObject JsonToMap){
        Iterator<?> it = JsonToMap.keys();
        HashMap<String, Object> RMap = new HashMap<String, Object>();
        while(it.hasNext()){
            String key = String.valueOf(it.next());
            if(JsonToMap.get(key).getClass() == JSONArray.class){//判是否为列表

                if(JsonToMap.getJSONArray(key).isEmpty()){//判列表是否为空
                    RMap.put(key,null);
                }else{

                    List<Map<String,Object>> MapListObj=new ArrayList<Map<String,Object>>();
                    for(Object JsonArray : JsonToMap.getJSONArray(key)){
                        HashMap<String, Object> TempMap = new HashMap<String, Object>();
                        TempMap.putAll(IteratorHash(JSONObject.fromObject(JsonArray)));
                        MapListObj.add(TempMap);
                    }
                    RMap.put(key, (Object) MapListObj);
                }
            }else if(JsonToMap.get(key).getClass() == JSONObject.class){

                RMap.putAll(IteratorHash(JsonToMap.getJSONObject(key)));

            }else if(JsonToMap.get(key).getClass() == String.class){

                RMap.put(key, JsonToMap.get(key));

            }
        }

        return RMap;
    }

}