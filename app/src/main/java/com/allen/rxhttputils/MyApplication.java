package com.allen.rxhttputils;


import com.allen.library.base.BaseRxHttpApplication;
import com.allen.library.http.RxHttpUtils;
import com.allen.library.utils.AppUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by allen on 2016/12/21.
 * <p>
 * 需要继承 BaseApplication
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

        RxHttpUtils.getInstance().config()
                .setBaseUrl(BuildConfig.BASE_URL)
                .setCache()
                .setHeaders(headerMaps)
                .setCookie(false)
                .setLog(true);

    }
}
