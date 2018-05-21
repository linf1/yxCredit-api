package com.api.model.contractsign;

import java.io.Serializable;
import java.util.List;

/**
 * 调用签章服务传参对象
 *
 */
public class ContractSignRequest implements Serializable {

	private static final long serialVersionUID = 3407669563620418172L;
	
	/**合同标题**/
	private String contractTitle;
	/**对应的模板id，没有则为空**/
//	private java.lang.Long templateId;
	/**业务系统里面的模板id标识**/
	private String realTemplateId;
	/**未签署合同位置**/
	private String unsignPath;
	
	private List<ByxUserModel> userModelList;
	
	/**文件流接收,待签署的合同流**/
	private byte[] unsignStream;
	/**文件流的来源文件名称**/
	private String unsignFileOrignName;
	
	/**是否外部系统调用,0:是;1:否**/
	private Integer isOutSign;
	
	
	public String getUnsignFileOrignName() {
		return unsignFileOrignName;
	}

	public void setUnsignFileOrignName(String unsignFileOrignName) {
		this.unsignFileOrignName = unsignFileOrignName;
	}

	public Integer getIsOutSign() {
		return isOutSign;
	}

	public void setIsOutSign(Integer isOutSign) {
		this.isOutSign = isOutSign;
	}

	public byte[] getUnsignStream() {
		return unsignStream;
	}

	public void setUnsignStream(byte[] unsignStream) {
		this.unsignStream = unsignStream;
	}

	public String getRealTemplateId() {
		return realTemplateId;
	}

	public void setRealTemplateId(String realTemplateId) {
		this.realTemplateId = realTemplateId;
	}

	public String getContractTitle() {
		return contractTitle;
	}

	public void setContractTitle(String contractTitle) {
		this.contractTitle = contractTitle;
	}

//	public java.lang.Long getTemplateId() {
//		return templateId;
//	}
//
//	public void setTemplateId(java.lang.Long templateId) {
//		this.templateId = templateId;
//	}

	public String getUnsignPath() {
		return unsignPath;
	}

	public void setUnsignPath(String unsignPath) {
		this.unsignPath = unsignPath;
	}

	public List<ByxUserModel> getUserModelList() {
		return userModelList;
	}

	public void setUserModelList(List<ByxUserModel> userModelList) {
		this.userModelList = userModelList;
	}

}
