package com.api.model.contractsign;

import java.io.Serializable;

public class ByxUserModel implements Serializable {

	private static final long serialVersionUID = 8227721755883157034L;

	//常量提供

	//个人用户
	public static final Integer USERTYPE_PERSON = 0;//个人用户
	public static final Integer USERTYPE_COMPANY = 1;//企业用户

	public static final Integer IDTYPE_ID = 0;//身份证
	public static final Integer IDTYPE_PASSPORT = 1;//护照
	public static final Integer IDTYPE_HK_MAC_PASS = 2;//港澳通行证

	public static final Integer AREA_MAINLAND = 0;//大陆
	public static final Integer AREA_HK = 1;//香港
	public static final Integer AREA_MAC = 2;//澳门
	public static final Integer AREA_TW = 3;//台湾
	public static final Integer AREA_FOREIGN = 4;//外籍

	//企业用户
	//企业类型
	public static final Integer COMPANY_TYPE_COMMON = 0;//普通企业
	public static final Integer COMPANY_TYPE_SOC = 1;//社会团体
	public static final Integer COMPANY_TYPE_INS = 2;//事业单位
	public static final Integer COMPANY_TYPE_PRI = 3;//民办非企业单位
	public static final Integer COMPANY_TYPE_GOV = 4;//党政及国家机构
	//企业注册类型
	public static final Integer COMPANY_REG_AGENT = 1;//代理人注册
	public static final Integer COMPANY_REG_LEGAL = 2;//法人注册

	//坐标
	public static final Integer POS_TYPE_COO = 0;//坐标定位
	public static final Integer POS_TYPE_KEY = 1;//关键字定位

	//是否默认企业
	public static final Integer DEFAULT_COMPANY_YES = 0;//默认企业
	public static final Integer DEFAULT_COMPANY_NO = 1;//非默认企业

	/**用户类型：0,个人用户;1,企业用户**/
	private Integer userType;

	/**签署方,0:甲,1:乙,2:丙,3:丁**/
	private Integer signatory;//常量待定，根据模板而定

	//个人用户--------------------------

	/**用户身份证件类型,0:身份证;1:护照;2:港澳通行证**/
	private Integer personIdType;
	/**用户身份证件号码**/
	private String personIdValue;
	/**用户姓名**/
	private String personName;
	/**个人归属地：0-大陆，1-香港，2-澳门，3-台湾，4-外籍，默认0**/
	private Integer personArea;

	//企业用户------------------------------
	//默认企业,byx平台
	private Integer defaultCompany;

	private String organizeName;//企业名称
	private Integer organizeType;//企业类型，0-普通企业，1-社会团体，2-事业单位，3-民办非企业单位，4-党政及国家机构，默认0
	private Integer organizeUserType;//企业注册类型，1-代理人注册，2-法人注册，默认1
	private String organizeRegCode;//企业工商注册号
	private String organizeOrganCode;//组织机构代码号
	private String organizeCreditCode;//统一社会信用代码
	private String organizeAgentName;//代理人姓名
	private String organizeAgentIdno;//代理人身份证号

	private String organizeLegalName;//法人姓名
	private String organizeLegalIdno;//法定代表身份证号
	private Integer organizeLegalArea;//法定代表人归属地,0-大陆，1-香港，2-澳门，3-台湾，4-外籍，默认0


	//坐标
	private Integer posType;//0-坐标定位，1-关键字定位，默认0
	private String posPage;//签署页码,若为坐标定位时，不可空
	private String posX;//签署位置X坐标，，若为关键字定位，相对亍关键字的X坐标偏移量，默认0
	private String posY;//签署位置Y坐标，，若为关键字定位，相对亍关键字的Y坐标偏移量，默认0
	private String posKey;//关键字，用于关键字定位
	private String width;//pdf上的签名区域显示宽度
	private String height;//pdf上的签名区域显示高度

	/** 企业印章的横向文内容 **/
	private String htext;
	/** 企业印章的下弦文内容 **/
	private String qtext;

	public Integer getPersonIdType() {
		return personIdType;
	}
	public void setPersonIdType(Integer personIdType) {
		this.personIdType = personIdType;
	}
	public String getPersonIdValue() {
		return personIdValue;
	}
	public void setPersonIdValue(String personIdValue) {
		this.personIdValue = personIdValue;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public Integer getPersonArea() {
		return personArea;
	}
	public void setPersonArea(Integer personArea) {
		this.personArea = personArea;
	}
	public Integer getPosType() {
		return posType;
	}
	public void setPosType(Integer posType) {
		this.posType = posType;
	}
	public String getPosPage() {
		return posPage;
	}
	public void setPosPage(String posPage) {
		this.posPage = posPage;
	}
	public String getPosX() {
		return posX;
	}
	public void setPosX(String posX) {
		this.posX = posX;
	}
	public String getPosY() {
		return posY;
	}
	public void setPosY(String posY) {
		this.posY = posY;
	}
	public String getPosKey() {
		return posKey;
	}
	public void setPosKey(String posKey) {
		this.posKey = posKey;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getOrganizeName() {
		return organizeName;
	}
	public void setOrganizeName(String organizeName) {
		this.organizeName = organizeName;
	}
	public Integer getOrganizeType() {
		return organizeType;
	}
	public void setOrganizeType(Integer organizeType) {
		this.organizeType = organizeType;
	}
	public Integer getOrganizeUserType() {
		return organizeUserType;
	}
	public void setOrganizeUserType(Integer organizeUserType) {
		this.organizeUserType = organizeUserType;
	}
	public String getOrganizeRegCode() {
		return organizeRegCode;
	}
	public void setOrganizeRegCode(String organizeRegCode) {
		this.organizeRegCode = organizeRegCode;
	}
	public String getOrganizeOrganCode() {
		return organizeOrganCode;
	}
	public void setOrganizeOrganCode(String organizeOrganCode) {
		this.organizeOrganCode = organizeOrganCode;
	}
	public String getOrganizeCreditCode() {
		return organizeCreditCode;
	}
	public void setOrganizeCreditCode(String organizeCreditCode) {
		this.organizeCreditCode = organizeCreditCode;
	}
	public String getOrganizeAgentName() {
		return organizeAgentName;
	}
	public void setOrganizeAgentName(String organizeAgentName) {
		this.organizeAgentName = organizeAgentName;
	}
	public String getOrganizeAgentIdno() {
		return organizeAgentIdno;
	}
	public void setOrganizeAgentIdno(String organizeAgentIdno) {
		this.organizeAgentIdno = organizeAgentIdno;
	}
	public Integer getDefaultCompany() {
		return defaultCompany;
	}
	public void setDefaultCompany(Integer defaultCompany) {
		this.defaultCompany = defaultCompany;
	}

	public Integer getSignatory() {
		return signatory;
	}
	public void setSignatory(Integer signatory) {
		this.signatory = signatory;
	}

	public String getOrganizeLegalName() {
		return organizeLegalName;
	}
	public void setOrganizeLegalName(String organizeLegalName) {
		this.organizeLegalName = organizeLegalName;
	}
	public String getOrganizeLegalIdno() {
		return organizeLegalIdno;
	}
	public void setOrganizeLegalIdno(String organizeLegalIdno) {
		this.organizeLegalIdno = organizeLegalIdno;
	}
	public Integer getOrganizeLegalArea() {
		return organizeLegalArea;
	}
	public void setOrganizeLegalArea(Integer organizeLegalArea) {
		this.organizeLegalArea = organizeLegalArea;
	}
	public String getHtext() {
		return htext;
	}
	public void setHtext(String htext) {
		this.htext = htext;
	}
	public String getQtext() {
		return qtext;
	}
	public void setQtext(String qtext) {
		this.qtext = qtext;
	}
	public ByxUserModel() {
		super();
		// TODO Auto-generated constructor stub
	}
		
}
