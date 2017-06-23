package com.allen.library.utils;


import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Allen on 2016/12/8.
 * <p>
 * 根据不同后台要求对post请求进行加工处理
 * <p>
 * 用户根据自己需求编写自己的map操作类
 */
public class ParamUtils {

    private Map<String, Object> params;


    public ParamUtils addParams(String key, Object value) {
        if (params == null) {
            params = new TreeMap<>();
        }

        params.put(key, value);
        return this;
    }

    public Map getParams() {
        if (params == null) {
            return null;
        }

        return params;
    }

    public void clearParams() {
        if (params != null)
            params.clear();
    }
}
