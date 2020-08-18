package com.fast.common.utils.json;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JSON {

    public static Gson getGson() {
        return GSON.sInstance;
    }

    public static final String toJSONString(Object object) {
        return GSON.toJSONString(object);
    }

    public static final <T> T parseObject(String json, Class<T> clazz) {
        return GSON.parseObject(json, clazz);
    }

    public static final <T> T parseObject(String json, Type type) {
        return GSON.parseObject(json, type);
    }
    
    public static final JSONObject parseObject(String json) {
        return GSON.parseObject(json);
    }

    public static final JSONObject parse(Object object) {
        return GSON.parseObject(object);
    }

    public static final JSONObject toJSON(Object object) {
        return GSON.parseObject(object);
    }

    public static final JSONObject toJSON(String json) {
        return GSON.parseObject(json);
    }

    public static final JSONArray parseArray(String json) {
        return GSON.parseArray(json);
    }

    public static final <T> List<T> parseArray(String json, Class<T> clazz) {
        JSONArray jsonArray = GSON.parseArray(json);
        List list = new ArrayList();
        for (int i = 0; i < jsonArray.length(); i++) {
            T t = parseObject(jsonArray.optString(i), clazz);
            list.add(t);
        }
        return list;
    }

    public static final <T> T toJavaObject(JSONObject jsonObject, Type type) {
        return parseObject(toJSONString(jsonObject), type);
    }

    public static final <T> T toJavaObject(JSONObject jsonObject, Class<T> clazz) {
        return parseObject(toJSONString(jsonObject), clazz);
    }
}
