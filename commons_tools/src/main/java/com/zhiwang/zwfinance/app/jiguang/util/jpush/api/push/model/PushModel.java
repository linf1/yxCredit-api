package com.zhiwang.zwfinance.app.jiguang.util.jpush.api.push.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public interface PushModel {

    public static Gson gson = new Gson();
    public JsonElement toJSON();
    
}
