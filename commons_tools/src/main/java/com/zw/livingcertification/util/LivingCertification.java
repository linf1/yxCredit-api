package com.zw.livingcertification.util;

import com.base.util.HttpClientUtil;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/5/2 0002.
 */
public class LivingCertification {
    public static String sign(List<String> values, String ticket) {
        if (values == null) {
            throw new NullPointerException("values is null");
        }
        values.removeAll(Collections.singleton(null));// remove null
        values.add(ticket);
        java.util.Collections.sort(values);

        StringBuilder sb = new StringBuilder();
        for (String s : values) {
            sb.append(s);
        }
        System.out.println(sb);
        return Hashing.sha1().hashString(sb, Charsets.UTF_8).toString().toUpperCase();

    }


    public static String getAccessToken(String url,String app_id,String secret,String grant_type,String version) throws Exception{
        url = url+"?app_id="+app_id+"&secret="+secret+"&grant_type="+grant_type+"&version="+version;
        String data = HttpClientUtil.getInstance().sendHttpGet(url);
        return data;
    }

    public static String getNonceTicket(String url,String app_id,String access_token,String type,String version,String user_id) throws Exception{
        url = url+"?app_id="+app_id+"&access_token="+access_token+"&type="+type+"&version="+version+"&user_id="+user_id;
        String data = HttpClientUtil.getInstance().sendHttpGet(url);
        return data;
    }

    public static String getSignTicket(String url,String app_id,String access_token,String type,String version) throws Exception{
        url = url+"?app_id="+app_id+"&access_token="+access_token+"&type="+type+"&version="+version;
        String data = HttpClientUtil.getInstance().sendHttpGet(url);
        return data;
    }
    public static void main(String[] args) throws  Exception{
        List list = new ArrayList();
        list.add("TIDA0001");
        list.add("userID19959248596551");
        list.add("kHoSxvLZGxSoFsjxlbzEoUzh5PAnTU7T");
        list.add("1.0.0");
        list.add("XO99Qfxlti9iTVgHAjwvJdAZKN3nMuUhrsPdPlPVKlcyS50N6tlLnfuFBPIucaMS");
        System.out.print(sign(list,"XO99Qfxlti9iTVgHAjwvJdAZKN3nMuUhrsPdPlPVKlcyS50N6tlLnfuFBPIucaMS"));
    }

}
