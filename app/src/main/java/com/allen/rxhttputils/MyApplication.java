package com.allen.rxhttputils;


import com.allen.library.base.BaseRxHttpApplication;
import com.allen.library.RxHttpUtils;


/**
 * Created by allen on 2016/12/21.
 * <p>
 * 需要继承 BaseRxHttpApplication
 */

public class MyApplication extends BaseRxHttpApplication {

    @Override
    public void onCreate() {
        super.onCreate();

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
                //.setHeaders(headerMaps)
                //全局持久话cookie,保存本地每次都会携带在header中
                .setCookie(false)
                //全局ssl证书认证
                //1、设置可访问所有的https网站----(null,null,null)
                //2、设置具体的证书----（证书的inputstream,null,null)
                //3、双向认证----(证书的inputstream,本地证书的inputstream,本地证书的密码)
                //.setSslSocketFactory(null, null, null)
                //全局超时配置
                .setReadTimeout(10)
                //全局超时配置
                .setWriteTimeout(10)
                //全局超时配置
                .setConnectTimeout(10)
                //全局是否打开请求log日志
                .setLog(true);



        // TODO: 2017/6/26 如果以上配置满足不了你，传入自己的 OkHttpClient 自行设置
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//
//        builder
////                .addInterceptor(log_interceptor)
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
