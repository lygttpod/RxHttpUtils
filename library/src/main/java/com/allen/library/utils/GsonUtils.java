package com.allen.library.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by allen on 2017/5/10.
 *
 */

public class GsonUtils {
    /**
     * 将Json数据转化为对象;
     *
     * @param jsonString Json数据;
     * @param cls        转换后的类;
     * @return
     */
    public static <T> T getObject(String jsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
        }
        return t;
    }

    /**
     * 将Json数据转化成List<Object>集合;
     *
     * @param jsonString Json数据;
     * @param cls        将要转化成集合的类;
     * @return
     */
    public static <T> List<T> getArray(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(
                    jsonString,
                    new TypeToken<List<T>>(){}.getType()
            );
        } catch (Exception e) {
        }
        return list;
    }


    /**
     * 将Json数据转化成List<Map<String, Object>>对象;
     *
     * @param jsonString Json数据;
     * @return
     */
    public static List<Map<String, Object>> listKeyMaps(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(
                    jsonString,
                    new TypeToken<List<Map<String, Object>>>(){}.getType()
            );
        } catch (Exception e) {
        }
        return list;
    }

    /**
     * 将Json数据转化成Map<String, Object>对象;
     *
     * @param jsonString Json数据;
     * @return
     */
    public static Map<String, Object> objKeyMaps(String jsonString) {
        Map<String, Object> map = new HashMap<>();
        try {
            Gson gson = new Gson();
            map = gson.fromJson(
                    jsonString,
                    new TypeToken<Map<String, Object>>(){}.getType()
            );
        } catch (Exception e) {
        }
        return map;
    }
}
