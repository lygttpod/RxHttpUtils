package com.allen.rxhttputils;


import com.allen.library.base.BaseRxHttpApplication;
import com.allen.library.http.RxHttpUtils;
import com.allen.library.utils.AppUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by allen on 2016/12/21.
 * <p>
 * 需要继承 BaseRxHttpApplication
 */

public class MyApplication extends BaseRxHttpApplication {

    Map<String, Object> headerMaps = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();

        headerMaps.put("client", "android");
        headerMaps.put("version", AppUtils.getAppVersion());
        headerMaps.put("uuid", AppUtils.getUUID());
        headerMaps.put("Content-type", "application/json");

        /**
         * 全局请求的统一配置
         */
        RxHttpUtils
                .getInstance()
                //开启全局配置
                .config()
                //全局的BaseUrl
                .setBaseUrl(BuildConfig.BASE_URL)
                //开启缓存策略
                //.setCache()
                //全局的请求头信息
                .setHeaders(headerMaps)
                //全局持久话cookie,保存本地每次都会携带在header中
                .setCookie(true)
                //全局ssl证书认证
                //.setCertificates(BuildConfig.CertificatesName)
                //全局超时配置
                .setReadTimeout(10)
                //全局超时配置
                .setWriteTimeout(10)
                //全局超时配置
                .setConnectTimeout(10)
                //全局是否打开请求log日志
                .setLog(true);

    }
}
