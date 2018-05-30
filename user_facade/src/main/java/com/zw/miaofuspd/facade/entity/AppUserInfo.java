package com.zw.miaofuspd.facade.entity;

import java.io.Serializable;

/**
 * <strong>Title : <br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2018年05月10日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:hanmeisheng <br>
 *         email: <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
public class AppUserInfo implements Serializable {

    //申请金额
    private String applayMoney;

    //申请期限
    private String periods;

    //试算合同金额
    private String budgetContractAmount;

    //剩余合同金额
    private String surplusContractAmount;

    //借款用途
    private String loanPurpose;


    public String getApplayMoney() {
        return applayMoney;
    }

    public void setApplayMoney(String applayMoney) {
        this.applayMoney = applayMoney;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getBudgetContractAmount() {
        return budgetContractAmount;
    }

    public void setBudgetContractAmount(String budgetContractAmount) {
        this.budgetContractAmount = budgetContractAmount;
    }

    public String getSurplusContractAmount() {
        return surplusContractAmount;
    }

    public void setSurplusContractAmount(String surplusContractAmount) {
        this.surplusContractAmount = surplusContractAmount;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    private String tel;//手机号
    //主键id
    private String id;
    private String card_type;//身份证的类型
    private String card_type_id;//从数据字典中读取
    //客户身份证号
    private String card;
    //客户id
    private String customer_id;

    private String occupation_type;//职业类型
    //和用户绑定的业务员id
    private String emp_id;
    private String bg_cust_info_id;
    private String crm_cust_info_id;
    //在erp中的bg_customer_id
    private String bg_customer_id;
    private String name;//姓名
    private String img_url;
    //性别 code
    private String sex;

    //性别
    private String sex_name;
    //person表主键id
    private String person_id;
    //要推送手机的唯一标示
    private String registration_id;
    //银行卡号
    private String card_no;

    private String temporary_id;

    private String isBlack;
    // 用户昵称
    private String nick_name;
    //用户标识
    private String identityid;
    //用户标识类型
    private String identitytype;

    public String getIdentityid() {
        return identityid;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public void setIdentityid(String identityid) {
        this.identityid = identityid;
    }

    public String getIdentitytype() {
        return identitytype;
    }

    public void setIdentitytype(String identitytype) {
        this.identitytype = identitytype;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getCard_type_id() {
        return card_type_id;
    }

    public void setCard_type_id(String card_type_id) {
        this.card_type_id = card_type_id;
    }

    public String getSex_name() {
        return sex_name;
    }

    public void setSex_name(String sex_name) {
        this.sex_name = sex_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getOccupation_type() {
        return occupation_type;
    }

    public void setOccupation_type(String occupation_type) {
        this.occupation_type = occupation_type;
    }

    public String getTemporary_id() {
        return temporary_id;
    }
    public void setTemporary_id(String temporary_id) {
        this.temporary_id = temporary_id;
    }
    public String getSexName() {
        return sex_name;
    }
    public void setSexName(String sex_name) {
        this.sex_name = sex_name;
    }

    public String getRegistration_id() {
        return registration_id;
    }
    public void setRegistration_id(String registration_id) {
        this.registration_id = registration_id;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCardTypeId() {
        return card_type_id;
    }
    public void setCardTypeId(String card_type_id) {
        this.card_type_id = card_type_id;
    }

    public String getCard() {
        return card;
    }
    public void setCard(String card) {
        this.card = card;
    }

    public String getCardType() {
        return card_type;
    }
    public void setCardType(String card_type) {
        this.card_type = card_type;
    }

    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBg_cust_info_id() {
        return bg_cust_info_id;
    }
    public String getCrm_cust_info_id() {
        return crm_cust_info_id;
    }
    public String getBg_customer_id() {
        return bg_customer_id;
    }
    public void setBg_cust_info_id(String bg_cust_info_id) {
        this.bg_cust_info_id = bg_cust_info_id;
    }
    public void setCrm_cust_info_id(String crm_cust_info_id) {
        this.crm_cust_info_id = crm_cust_info_id;
    }
    public void setBg_customer_id(String bg_customer_id) {
        this.bg_customer_id = bg_customer_id;
    }
    public String getCustomer_id() {
        return customer_id;
    }
    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
    public String getEmp_id() {
        return emp_id;
    }
    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImg_url() {
        return img_url;
    }
    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
    public String getPerson_id() {
        return person_id;
    }
    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getIsBlack() {
        return isBlack;
    }

    public void setIsBlack(String isBlack) {
        this.isBlack = isBlack;
    }
}

