package com.api.model.credit;

import java.io.Serializable;

/**
 *  征信验证结果AO
 * @author luochaofang
 */
public class CreditResultAO implements Serializable {
    private static final long serialVersionUID = -3721527294663526053L;

    /**
     * 任务编号
     */
    private String taskNo;

    /**
     * 任务状态
     */
    private String taskStatus;

    /**
     * 结果代码
     */
    private String code;

    /**
     * 结果原因描述
     */
    private String message;

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
