package com.fast.common.utils.json;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    public static boolean contains(JSONArray jsonArray, Object value) {
        if (value == null) {
            return false;
        }
        if (jsonArray == null || jsonArray.length() == 0) {
            return false;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.opt(i).equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static JSONArray add(JSONArray jsonArray, int index, Object insertObj) {
        ArrayList result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            result.add(jsonArray.opt(i));
        }

        result.add(index, insertObj);

        JSONArray destJSONArray = new JSONArray();
        for (Object obj : result) {
            destJSONArray.put(obj);
        }

        return destJSONArray;
    }
    

    public static void addAll(JSONArray jsonArray, List list) {
        if (jsonArray == null || list == null) {
            return;
        }

        for (Object obj : list) {
            jsonArray.put(obj);
        }
    }

    public static JSONArray addAll(JSONArray jsonArray, JSONArray insertArray) {
        if (jsonArray == null) {
            return jsonArray;
        }
        if (insertArray == null) {
            return jsonArray;
        }
        for (int i = 0; i < insertArray.length(); i++) {
            jsonArray.put(insertArray.opt(i));
        }
        return jsonArray;
    }

    public static JSONArray addAll(int index, JSONArray originArray, JSONArray insertArray) {
        if (originArray == null) {
            return originArray;
        }
        if (insertArray == null) {
            return originArray;
        }
        ArrayList result = new ArrayList<>();
        for (int i = 0; i < originArray.length(); i++) {
            result.add(originArray.opt(i));
        }

        ArrayList insertTempArray = new ArrayList();
        for (int i = 0; i < insertArray.length(); i++) {
            insertTempArray.add(insertArray.opt(i));
        }

        result.addAll(index, insertTempArray);

        JSONArray jsonArray = new JSONArray();
        for (Object obj : result) {
            jsonArray.put(obj);
        }
        return jsonArray;

    }

    public static JSONArray subList(JSONArray jsonArray, int start, int end) throws IllegalArgumentException  {
        if (start < 0 || end < 0) {
            throw new IllegalArgumentException("start and end must greater than 0");
        }
        if (start > end) {
            throw new IllegalArgumentException("start must greater than end");
        }

        JSONArray result = new JSONArray();
        for (int i = start; i < end && i < jsonArray.length(); i++) {
            result.put(jsonArray.opt(i));
        }
        return result;
    }

}
