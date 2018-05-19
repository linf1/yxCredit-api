package com.api.model.shujumohe;

import java.io.Serializable;

/**
 * 魔盒参数请求对象
 * @author 陈清玉
 */
public class ShujumoheRequest implements Serializable {

    private static final long serialVersionUID = 3699837676057820037L;
    private String task_id;

    private String all_submit;

    private String userName;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getAll_submit() {
        return all_submit;
    }

    public void setAll_submit(String all_submit) {
        this.all_submit = all_submit;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "ShujumoheRequest{" +
                "task_id='" + task_id + '\'' +
                ", all_submit='" + all_submit + '\'' +
                '}';
    }
}
