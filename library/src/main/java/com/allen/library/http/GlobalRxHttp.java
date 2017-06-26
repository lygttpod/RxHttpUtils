package com.allen.library.http;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.allen.library.interceptor.AddCookiesInterceptor;
import com.allen.library.interceptor.CacheInterceptor;
import com.allen.library.interceptor.HeaderInterceptor;
import com.allen.library.interceptor.ReceivedCookiesInterceptor;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by Allen on 2017/5/3.
 * <p>
 * 网络请求工具类---使用的是全局配置的变量
 */

public class GlobalRxHttp {

    private static GlobalRxHttp instance;

    public static GlobalRxHttp getInstance() {

        if (instance == null) {
            synchronized (GlobalRxHttp.class) {
                if (instance == null) {
                    instance = new GlobalRxHttp();
                }
            }

        }
        return instance;
    }

    /**
     * 设置baseUrl
     *
     * @param baseUrl
     * @return
     */
    public GlobalRxHttp setBaseUrl(String baseUrl) {
        getGlobalRetrofitBuilder().baseUrl(baseUrl);
        return this;
    }


    /**
     * 设置自己的client
     *
     * @param okClient
     * @return
     */
    public GlobalRxHttp setOkClient(OkHttpClient okClient) {
        getGlobalRetrofitBuilder().client(okClient);
        return this;
    }


    /**
     * 添加统一的请求头
     *
     * @param headerMaps
     * @return
     */
    public GlobalRxHttp setHeaders(Map<String, Object> headerMaps) {
        getGlobalOkHttpBuilder().addInterceptor(new HeaderInterceptor(headerMaps));
        return this;
    }

    /**
     * 是否开启请求日志
     *
     * @param isShowLog
     * @return
     */
    public GlobalRxHttp setLog(boolean isShowLog) {
        if (isShowLog) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.e("RxHttpUtils", message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            getGlobalOkHttpBuilder().addInterceptor(loggingInterceptor);
        }
        return this;
    }

    /**
     * 开启缓存，缓存到默认路径
     *
     * @return
     */
    public GlobalRxHttp setCache() {
        CacheInterceptor cacheInterceptor = new CacheInterceptor();
        Cache cache = new Cache(new File(Environment.getExternalStorageDirectory().getPath() + "/rxHttpCacheData")
                , 1024 * 1024 * 100);
        getGlobalOkHttpBuilder().addInterceptor(cacheInterceptor)
                .addNetworkInterceptor(cacheInterceptor)
                .cache(cache);
        return this;
    }

    /**
     * 设置缓存路径及缓存文件大小
     *
     * @param cachePath
     * @param maxSize
     * @return
     */
    public GlobalRxHttp setCache(String cachePath, long maxSize) {
        if (!TextUtils.isEmpty(cachePath) && maxSize > 0) {
            CacheInterceptor cacheInterceptor = new CacheInterceptor();
            Cache cache = new Cache(new File(cachePath), maxSize);
            getGlobalOkHttpBuilder()
                    .addInterceptor(cacheInterceptor)
                    .addNetworkInterceptor(cacheInterceptor)
                    .cache(cache);
        }

        return this;
    }

    /**
     * 持久化保存cookie保存到sp文件中
     *
     * @param saveCookie
     * @return
     */
    public GlobalRxHttp setCookie(boolean saveCookie) {
        if (saveCookie) {
            getGlobalOkHttpBuilder()
                    .addInterceptor(new AddCookiesInterceptor())
                    .addInterceptor(new ReceivedCookiesInterceptor());
        }
        return this;
    }


    /**
     * 设置读取超时时间
     *
     * @param second
     * @return
     */
    public GlobalRxHttp setReadTimeout(long second) {
        getGlobalOkHttpBuilder().readTimeout(second, TimeUnit.SECONDS);
        return this;
    }

    /**
     * 设置写入超时时间
     *
     * @param second
     * @return
     */
    public GlobalRxHttp setWriteTimeout(long second) {
        getGlobalOkHttpBuilder().readTimeout(second, TimeUnit.SECONDS);
        return this;
    }

    /**
     * 设置连接超时时间
     *
     * @param second
     * @return
     */
    public GlobalRxHttp setConnectTimeout(long second) {
        getGlobalOkHttpBuilder().readTimeout(second, TimeUnit.SECONDS);
        return this;
    }

    /**
     * 对https的支持，三种方式
     * 1、设置可访问所有的https网站----(null,null,null)
     * 2、设置具体的证书----（证书的inputstream,null,null)
     * 3、双向认证----(证书的inputstream,本地证书的inputstream,本地证书的密码)
     *
     * @param certificates
     * @param bksFile
     * @param password
     * @return
     */
    public GlobalRxHttp setSslSocketFactory(InputStream[] certificates, InputStream bksFile, String password) {
        SSLUtils.SSLParams sslParams = SSLUtils.getSslSocketFactory(certificates, bksFile, password);
        getGlobalOkHttpBuilder().sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }


    /**
     * 全局的 retrofit
     *
     * @return
     */
    public static Retrofit getGlobalRetrofit() {
        return RetrofitClient.getInstance().getRetrofit();
    }

    /**
     * 全局的 RetrofitBuilder
     *
     * @return
     */
    public Retrofit.Builder getGlobalRetrofitBuilder() {
        return RetrofitClient.getInstance().getRetrofitBuilder();
    }

    public OkHttpClient.Builder getGlobalOkHttpBuilder() {
        return HttpClient.getInstance().getBuilder();
    }

    /**
     * 使用全局变量的请求
     *
     * @param cls
     * @param <K>
     * @return
     */
    public static <K> K createGApi(final Class<K> cls) {
        return getGlobalRetrofit().create(cls);
    }


}
