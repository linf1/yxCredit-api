package com.zw.miaofuspd.facade.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/1 0001.
 */
public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;

    private String contact_name;

    private String contact_tel;	//父id

    private String contact_type;	//组织机构名称

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_tel() {
        return contact_tel;
    }

    public void setContact_tel(String contact_tel) {
        this.contact_tel = contact_tel;
    }

    public String getContact_type() {
        return contact_type;
    }

    public void setContact_type(String contact_type) {
        this.contact_type = contact_type;
    }
}
