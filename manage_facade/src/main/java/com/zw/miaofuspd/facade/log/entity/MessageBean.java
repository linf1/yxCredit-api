package com.zw.miaofuspd.facade.log.entity;

import java.io.Serializable;

public class MessageBean implements Serializable {
	private String id="";
	private String user_id="";//订单编号
	private String title="";//产品ID
	private String content="";//产品名
	private String create_time="";//分期期数
	private String update_time="";//商户ID
	private String state="";
	private String push_state="";//极光推送是否成功，0成功，1失败
	private String update_state="";//是否可以更新
	private String order_state="";
	private String msgType;
	
	public void setUpdateState(String update_state){
		this.update_state=update_state;
	}
	
	public String getUpdateState(){
		return this.update_state;
	}
	
	public void setUpdateTime(String update_time){
		this.update_time=update_time;
	}	
	public String getUpdateTime(){
		return this.update_time;
	}
	
	public void setCreateTime(String create_time){
		this.create_time=create_time;
	}	
	public String getCreateTime(){
		return this.create_time;
	}
	
	public void setId(String id){
		this.id=id;
	}	
	public String getId(){
		return this.id;
	}
	public String getUserId() {
		return user_id;
	}
	public void setUserId(String user_id) {
		this.user_id = user_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPushState() {
		return push_state;
	}
	public void setPushState(String push_state) {
		this.push_state = push_state;
	}

	public String getOrder_state() {
		return order_state;
	}

	public void setOrder_state(String order_state) {
		this.order_state = order_state;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
}
