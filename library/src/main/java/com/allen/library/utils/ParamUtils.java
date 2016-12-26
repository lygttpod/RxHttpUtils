package com.allen.library.utils;


import com.allen.library.base.BaseApplication;

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

    private static final String KEY_SIGN = "sign";

    private Map<String, Object> params;


    public ParamUtils addParams(String key, Object value) {
        if (params == null) {
            params = new TreeMap<>();
        }
        if (params.containsKey(KEY_SIGN)) {
            params.remove(KEY_SIGN);
        }
        params.put(key, value);
        return this;
    }

    public Map getParams() {
        if (params == null) {
            return null;
        }
        if (params.containsKey(KEY_SIGN)) {
            params.remove(KEY_SIGN);
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey());
            sb.append('=');
            Object entryValue = entry.getValue();
            if (entryValue instanceof Boolean) {
                if (((Boolean) entryValue) == true) {
                    sb.append('1');
                } else if (((Boolean) entryValue) == false) {
                    sb.append('0');
                }
            } else {
                sb.append(entryValue);
            }
            sb.append('&');
        }


        String token = (String) SPUtils.get(BaseApplication.getContext(), "token", "");
        sb.append(token);


        String signValue = MD5.EncoderByMd5(String.valueOf(sb));
        params.put(KEY_SIGN, signValue);

        return params;
    }

    public void clearParams() {
        if (params != null)
            params.clear();
    }
}
