package com.zw.miaofuspd.facade.entity;

import java.io.Serializable;

/**
 * <strong>Title : <br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2017年02月21日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:Administrator <br>
 *         email: <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
public class CustomerLinkmanBean implements Serializable {
    private String id="";//ID号
    private String link_name="";//联系人姓名
    private String relationship="";//关系
    private String known_loan="";//是否知晓此贷款
    private String contact="";//联系方式
    private String idcard_num="";//身份证号码
    private String work_company="";//工作单位
    private String main_sign="";//是否主要联系人，1为是，0为否
    private String customer_id="";//客户
    private String create_time="";
    private String update_time="";
    private String state="";
    private String crm_linkman_id="";
    private String relationship_name="";
    private String known_loan_name="";

    public void setKnownLoanName(String known_loan_name){
        this.known_loan_name=known_loan_name;
    }

    public String getKnownLoanName(){
        return this.known_loan_name;
    }

    public void setRelationshipName(String relationship_name){
        this.relationship_name=relationship_name;
    }

    public String getRelationshipName(){
        return this.relationship_name;
    }

    public void setCrmLinkmanid(String crm_linkman_id){
        this.crm_linkman_id=crm_linkman_id;
    }

    public String getCrmLinkmanid(){
        return this.crm_linkman_id;
    }

    public void setLinkName(String link_name){
        this.link_name=link_name;
    }

    public String getLinkName(){
        return this.link_name;
    }


    public void setState(String state){
        this.state=state;
    }

    public String getState(){
        return this.state;
    }

    public void setRelationShip(String relationship){
        this.relationship=relationship;
    }

    public String getRelationShip(){
        return this.relationship;
    }

    public void setKnownLoan(String known_loan){
        this.known_loan=known_loan;
    }

    public String getKnownLoan(){
        return this.known_loan;
    }

    public void setContact(String contact){
        this.contact=contact;
    }

    public String getContact(){
        return this.contact;
    }

    public void setIdcardNum(String idcard_num){
        this.idcard_num=idcard_num;
    }

    public String getIdcardNum(){
        return this.idcard_num;
    }

    public void setWorkCompany(String work_company){
        this.work_company=work_company;
    }

    public String getWorkCompany(){
        return this.work_company;
    }

    public void setMainSign(String main_sign){
        this.main_sign=main_sign;
    }

    public String getMainSign(){
        return this.main_sign;
    }

    public void setCustomerId(String customer_id){
        this.customer_id=customer_id;
    }
    public String getCustomerId(){
        return this.customer_id;
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
}

