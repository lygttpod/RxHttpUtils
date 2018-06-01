package com.allen.rxhttputils;


import android.app.Application;

import com.allen.library.RxHttpUtils;
import com.allen.library.config.OkHttpConfig;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;


/**
 * Created by allen on 2016/12/21.
 * <p>
 *
 * @author Allen
 */

public class App extends Application {

    private Map<String, Object> headerMaps = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();

        initRxHttpUtils();

//        initCustomRxHttpUtils();

    }


    /**
     * 快速上手，简单配置
     */
    private void initRxHttpUtils() {
        RxHttpUtils
                .getInstance()
                .init(this)
                .config()
                //配置全局baseUrl
                .setBaseUrl("https://api.douban.com/");
    }


    /**
     * 全局请求的统一配置（以下配置根据自身情况选择性的配置即可）
     */
    private void initCustomRxHttpUtils() {

//        获取证书
//        InputStream cerInputStream = null;
//        InputStream bksInputStream = null;
//        try {
//            cerInputStream = getAssets().open("YourSSL.cer");
//            bksInputStream = getAssets().open("your.bks");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        OkHttpClient okHttpClient = new OkHttpConfig
                .Builder()
                //全局的请求头信息
                .setHeaders(headerMaps)
                //开启缓存策略(默认false)
                .setCache(false)
                //全局持久话cookie,保存本地每次都会携带在header中（默认false）
                .setSaveCookie(false)
                //全局ssl证书认证
                //1、信任所有证书,不安全有风险（默认信任所有证书）
                //.setSslSocketFactory()
                //2、使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(cerInputStream)
                //3、使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(bksInputStream,"123456",cerInputStream)
                //全局超时配置
                .setReadTimeout(10)
                //全局超时配置
                .setWriteTimeout(10)
                //全局超时配置
                .setConnectTimeout(10)
                //全局是否打开请求log日志
                .setDebug(true)
                .build();

        RxHttpUtils
                .getInstance()
                .init(this)
                .config()
                //配置全局baseUrl
                .setBaseUrl("https://api.douban.com/")
                //开启全局配置
                .setOkClient(okHttpClient);

//        TODO: 2018/5/31 如果以上OkHttpClient的配置满足不了你，传入自己的 OkHttpClient 自行设置
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//
//        builder
//                .addInterceptor(log_interceptor)
//                .readTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .connectTimeout(10, TimeUnit.SECONDS);
//
//        RxHttpUtils
//                .getInstance()
//                .config()
//                .setBaseUrl(BuildConfig.BASE_URL)
//                .setOkClient(builder.build());
    }


}
