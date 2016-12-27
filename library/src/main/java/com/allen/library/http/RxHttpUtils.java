package com.allen.library.http;


import java.util.Map;
import java.util.TreeMap;

/**
 * Created by allen on 2016/12/20.
 * <p>
 * 网络请求工具类封装
 */

public class RxHttpUtils {

    private static RxHttpUtils mRxHttpUtils;

    private static String mBaseUrl;
    private static Map<String, Object> mHeaderMaps = new TreeMap<>();

    public static RxHttpUtils getInstance() {
        mHeaderMaps.clear();
        mBaseUrl = "";
        if (mRxHttpUtils == null) {
            mRxHttpUtils = new RxHttpUtils();
        }
        return mRxHttpUtils;
    }

    public static RxHttpUtils getInstance(String baseUrl) {
        mHeaderMaps.clear();
        mBaseUrl = baseUrl;
        if (mRxHttpUtils == null) {
            mRxHttpUtils = new RxHttpUtils();
        }
        return mRxHttpUtils;
    }

    public <K> K createApi(final Class<K> cls) {
        if ("".equals(mBaseUrl) || mBaseUrl == null) {
            return RetrofitClient.getInstance(mHeaderMaps).create(cls);
        } else {
            return RetrofitClient.getInstance(mBaseUrl, mHeaderMaps).create(cls);
        }
    }

    public RxHttpUtils addHeader(Map<String, Object> headerMaps) {
        mHeaderMaps = headerMaps;
        return mRxHttpUtils;
    }

}
