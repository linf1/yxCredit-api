package com.zw.miaofuspd.util;


public class UserLogToSqlUtils {
	/**
	 * 返回写入app_user_log表中最详细的sql
	 * @param id 用户的id	
	 * @param tel 用户的手机号
	 * @param handle 描述
	 * @param ago	之前的值
	 * @param current 现在的值
	 * @param described	描述
	 * @param creat_time 插入时间
	 * @param alter_time 修改时间
	 * @param status 状态 0  正常  1 失效
	 * @return
	 */
	public static String userLog(String id,String tel,String handle,String ago,String current,String described,String creat_time,String alter_time,String status){
		String sql = "INSERT INTO app_user_log (id,tel,handle,ago,current,described,creat_time,alter_time,state) "
				+ "VALUES('"+id+"','"+tel+"','"+handle+"','"+ago+"','"+current+"','"+described+"','"+creat_time+"','"+alter_time+"','"+status+"')";
		return sql;
	}
	/**
	 * 返回写入app_user_log表中sql
	 * @param id 用户的id	
	 * @param tel 用户的手机号
	 * @param handle 描述
	 * @param current 现在的值
	 * @param described	描述
	 * @param creat_time 插入时间
	 * @param alter_time 修改时间
	 * @return
	 */
	public static String userLog(String id,String tel,String handle,String current,String described,String creat_time,String alter_time){
		String sql = "INSERT INTO app_user_log (id,tel,handle,current,described,creat_time,alter_time) VALUES("
				+ "'"+id+"','"+tel+"','"+handle+"','"+current+"','"+described+"','"+creat_time+"','"+alter_time+"')";
		return sql;
	}
	/**
	 * 返回写入app_user_log表中的sql
	 * @param id 用户的id	
	 * @param tel 用户的手机号
	 * @param handle 描述
	 * @param ago	之前的值
	 * @param current 现在的值
	 * @param described	描述
	 * @param creat_time 插入时间
	 * @param alter_time 修改时间
	 * @return
	 */
	public static String userLog(String id,String tel,String handle,String ago,String current,String described,String creat_time,String alter_time){
		String sql = "INSERT INTO app_user_log (id,tel,handle,ago,current,described,creat_time,alter_time) VALUES"
				+ "('"+id+"','"+tel+"','"+handle+"','"+ago+"','"+current+"','"+described+"','"+creat_time+"','"+alter_time+"')";
		return sql;
	}
}
