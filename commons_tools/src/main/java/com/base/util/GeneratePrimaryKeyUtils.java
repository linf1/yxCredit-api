package com.base.util;

import java.util.UUID;

/**
 * 数据库主键生成帮助类（雪花，UUID,等）
 * @author 陈清玉
 */
public class GeneratePrimaryKeyUtils {

    /**
     * jdk提供{@link UUID#randomUUID} uuid 考虑到资源问题去掉"-"
     * @return uuid 唯一主键
     */
    public static String getUUIDKey(){
       return UUID.randomUUID().toString().replace("-","");
    }

    /**
     * Twitter_Snowflake 雪花主键
     * @return
     */
    public static long getSnowflakeKey(){
        return new SnowflakeIdWorker(0, 0).nextId();
    }

    /**
     * Twitter_Snowflake 雪花主键
     * @param workerId  工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     * @return
     */
    public static long getSnowflakeKey(long workerId, long datacenterId){
        return new SnowflakeIdWorker(workerId, datacenterId).nextId();
    }

}
