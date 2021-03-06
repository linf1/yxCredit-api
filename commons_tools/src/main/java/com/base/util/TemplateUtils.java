package com.base.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 内容模板解析器
 * @author  陈清玉
 */
public class TemplateUtils {
    private final static  String srg = "(\\$\\{([a-zA-Z]+)\\})";

    /**
     * 根据模板及参数获得短信内容
     * @return
     */
    public static String getContent(String tempalte, Map<String,String> parameters){
        Pattern p = Pattern.compile(srg);
        Matcher m = p.matcher(tempalte);
        StringBuffer stringBuffer = new StringBuffer();
        while (m.find()){
            String key = m.group(2);
            String value = null;
            if (parameters.containsKey(key)){
                value = parameters.get(key);
            }
            value = (value == null) ? "" : value;
            m.appendReplacement(stringBuffer,value);
        }
        m.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        String tempalte = "${name}你好,今年${age}岁！";
        Map<String,String> parameters = new HashMap<String, String>();
        parameters.put("name", "chris");
        parameters.put("age", "22");
        System.out.println(getContent(tempalte, parameters));

    }
}
