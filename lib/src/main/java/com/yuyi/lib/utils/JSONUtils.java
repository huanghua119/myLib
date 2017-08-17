  package com.yuyi.lib.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author tony amin
 */
public class JSONUtils {


    /**
     * String 转换 JSONObject对象
     *
     * @param source JSON资源
     * @return JSONObject 对象
     * @throws JSONException 返回JSON异常
     */
    public static JSONObject stringToJSONObject(String source) throws JSONException {
        JSONObject jsonObject = new JSONObject(source);
        return jsonObject;
    }

    /**
     * 根据name 获取 JSONArray 子对象
     *
     * @param jsonObject JSON对象
     * @param name       JSON 字段名
     * @return JSONArray对象
     * @throws JSONException 返回JSON异常
     */
    public static JSONArray getJSONArray(JSONObject jsonObject, String name) throws JSONException {
        if (jsonObject.has(name)) {
            return jsonObject.getJSONArray(name);
        }
        return null;
    }

    /**
     * 根据name 获取 json 资源
     *
     * @param jsonObject JSON 对象
     * @param name       JSON 字段名
     * @return JSON 资源
     * @throws JSONException 返回JSON异常
     */
    public static String getJSONString(JSONObject jsonObject, String name) throws JSONException {
        if (jsonObject.has(name)) {
            return jsonObject.getString(name);
        }
        return null;
    }

    public static boolean isJson(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        try {
            stringToJSONObject(str);
            return true;
        } catch (JSONException exception) {
        }
        return false;
    }
    public static JSONObject getJSONObject(JSONObject jsonObject, String name) throws JSONException {
        return jsonObject.getJSONObject(name);
    }
    public static double getJSONDouble(JSONObject jsonObject, String name) throws JSONException {
        if (jsonObject.has(name)) {
            return jsonObject.getDouble(name);
        }
        return 0;
    }
    public static int getJSONInt(JSONObject jsonObject, String name) throws JSONException {
        if (jsonObject.has(name)) {
            return jsonObject.getInt(name);
        }
        return -1;
    }

    public static long getJSONLong(JSONObject jsonObject, String name) throws JSONException {
        if (jsonObject.has(name)) {
            return jsonObject.getLong(name);
        }
        return -1;
    }

    public static boolean getJSONBoolean(JSONObject jsonObject, String name) throws JSONException {
        if (jsonObject.has(name)) {
            return jsonObject.getBoolean(name);
        }
        return false;
    }
}
