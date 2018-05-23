package com.api.model.contractsign;

import com.alibaba.fastjson.JSONObject;
import com.api.model.BYXSettings;
import com.api.model.common.BYXRequest;
import com.google.gson.Gson;
import com.zhiwang.zwfinance.app.jiguang.util.api.CryptoTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * 调用签章服务传参对象
 *
 */
public class ContractSignRequest implements Serializable {
	private static final Logger LOGGER = LoggerFactory.getLogger(ContractSignRequest.class);

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

	/**
	 * 返回json对象参数
	 * @param param 返回参数
	 * @param byxSettings 碧友信配置
	 * @return 请求参数json字符串
	 * @throws Exception 加密异常
	 */
	public static String getBYXRequest(Object param,BYXSettings byxSettings) throws Exception {
		CryptoTools cryptoTools = new CryptoTools(byxSettings.getDesKey(),byxSettings.getVi());
		BYXRequest byxRequest  = new BYXRequest();
		if(param == null){
			byxRequest.setData("");
		}else{
			//参数转化JSON
			Gson gson=new Gson();
			final String paramJson = gson.toJson(param);
			LOGGER.info("请求参数JSON字符串：{}",paramJson);
			//碧友信参数加密
			final String encodesStr = cryptoTools.encode(paramJson);
			byxRequest.setData(encodesStr);
		}
		byxRequest.setRequestTime(System.currentTimeMillis());
		return JSONObject.toJSONString(byxRequest);
	}
}
