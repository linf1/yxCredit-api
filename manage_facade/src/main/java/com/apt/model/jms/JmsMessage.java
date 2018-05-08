package com.apt.model.jms;

import java.io.Serializable;

public class JmsMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code; //标识操作类型的编号（预定义），具体请查看表格：code定义；【非空字段】
	
	private String id; //主键编号（若通知订单则对应的是订单编号，若通知对象是申请单，那对应的编号就是申请单编号，以此类推）【非空字段】
	
	private String time; //操作时间【非空字段】
	
	private String approver; //审核人sys_employee员工表主键即可【非空字段】
	
	private String status; //审核状态，可能会涉及扩展;0：失败;1：通过;20：退回;21：拒绝;22：黑名单;【非空字段】
	
	private String message; //一般性消息通知 ；审核信息【可选字段】
	
	private String data; //消息通知的一些具体数据信息，Json格式包装，具体内容需参见不同通知方法【可选字段】

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public JmsMessage(String code, String id, String time, String approver, String status, String message,
                      String data) {
		super();
		this.code = code;
		this.id = id;
		this.time = time;
		this.approver = approver;
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public JmsMessage() {
	}
}
