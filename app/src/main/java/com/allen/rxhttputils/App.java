package com.allen.rxhttputils;


import android.app.Application;

import com.allen.library.RxHttpUtils;
import com.allen.library.config.OkHttpConfig;
import com.allen.library.cookie.store.SPCookieStore;

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

//        initRxHttpUtils();
        initCustomRxHttpUtils();

    }


    /**
     * 快速上手，默认配置
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
                .Builder(this)
                //全局的请求头信息
                .setHeaders(headerMaps)
                //开启缓存策略(默认false)
                //1、在有网络的时候，先去读缓存，缓存时间到了，再去访问网络获取数据；
                //2、在没有网络的时候，去读缓存中的数据。
                .setCache(true)
                //全局持久话cookie,保存到内存（new MemoryCookieStore()）或者保存到本地（new SPCookieStore(this)）
                //不设置的话，默认不对cookie做处理
                .setCookieType(new SPCookieStore(this))
                //可以添加自己的拦截器(比如使用自己熟悉三方的缓存库等等)
                //.setAddInterceptor(null)
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
//                .init(this)
//                .config()
//                .setBaseUrl(BuildConfig.BASE_URL)
//                .setOkClient(builder.build());
    }


}
