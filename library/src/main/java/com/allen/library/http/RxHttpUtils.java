package com.allen.library.http;


/**
 * Created by allen on 2016/12/20.
 * <p>
 * 网络请求工具类封装
 */

public class RxHttpUtils {

    private static RxHttpUtils mRxHttpUtils;

    private static String mBaseUrl;

    public static RxHttpUtils getInstance() {
        mBaseUrl = "";
        if (mRxHttpUtils == null) {
            mRxHttpUtils = new RxHttpUtils();
        }
        return mRxHttpUtils;
    }

    public static RxHttpUtils getInstance(String baseUrl) {
        mBaseUrl = baseUrl;
        if (mRxHttpUtils == null) {
            mRxHttpUtils = new RxHttpUtils();
        }
        return mRxHttpUtils;
    }

    public <K> K createApi(final Class<K> cls) {
        if ("".equals(mBaseUrl) || mBaseUrl == null) {
            return RetrofitClient.getInstance().create(cls);
        } else {
            return RetrofitClient.getInstance(mBaseUrl).create(cls);
        }
    }

}
