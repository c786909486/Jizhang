package com.example.vuandroidadsdk.model;

import com.alibaba.fastjson.JSON;

/**
 * Created by CKZ on 2017/6/26.
 */

public class ResponseToModel {

    public static Object parseJsonToModule(String response,Class<Object> clazz){
        return JSON.parseObject(response,clazz);
    }
}
