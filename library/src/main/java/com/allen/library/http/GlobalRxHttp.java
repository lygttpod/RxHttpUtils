package com.allen.library.http;

import android.os.Environment;
import android.text.TextUtils;

import com.allen.library.interceptor.AddCookiesInterceptor;
import com.allen.library.interceptor.CacheInterceptor;
import com.allen.library.interceptor.HeaderInterceptor;
import com.allen.library.interceptor.ReceivedCookiesInterceptor;

import java.io.File;
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

    public GlobalRxHttp setBaseUrl(String baseUrl) {
        getGlobalRetrofitBuilder().baseUrl(baseUrl);
        return this;
    }

    public GlobalRxHttp setHeaders(Map<String, Object> headerMaps) {
        getGlobalOkHttpBuilder().addInterceptor(new HeaderInterceptor(headerMaps));
        return this;
    }

    public GlobalRxHttp setLog(boolean isShowLog) {
        if (isShowLog) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            getGlobalOkHttpBuilder().addInterceptor(loggingInterceptor);
        }
        return this;
    }

    public GlobalRxHttp setCache() {
        CacheInterceptor cacheInterceptor = new CacheInterceptor();
        Cache cache = new Cache(new File(Environment.getExternalStorageDirectory().getPath() + "/rxHttpCacheData")
                , 1024 * 1024 * 100);
        getGlobalOkHttpBuilder().addInterceptor(cacheInterceptor)
                .addNetworkInterceptor(cacheInterceptor)
                .cache(cache);
        return this;
    }

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

    public GlobalRxHttp setCookie(boolean saveCookie) {
        if (saveCookie) {
            getGlobalOkHttpBuilder()
                    .addInterceptor(new AddCookiesInterceptor())
                    .addInterceptor(new ReceivedCookiesInterceptor());
        }
        return this;
    }


    public GlobalRxHttp setReadTimeout(long second) {
        getGlobalOkHttpBuilder().readTimeout(second, TimeUnit.SECONDS);
        return this;
    }

    public GlobalRxHttp setWriteTimeout(long second) {
        getGlobalOkHttpBuilder().readTimeout(second, TimeUnit.SECONDS);
        return this;
    }

    public GlobalRxHttp setConnectTimeout(long second) {
        getGlobalOkHttpBuilder().readTimeout(second, TimeUnit.SECONDS);
        return this;
    }

    public GlobalRxHttp setCertificates(String certificateName) {
        if (!TextUtils.isEmpty(certificateName)) {
            SSLHelper.SSLParams sslParams = SSLHelper.getSSLParams(certificateName);
            if (sslParams.sSLSocketFactory != null && sslParams.trustManager != null) {
                getGlobalOkHttpBuilder().sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);//添加https ssl
            }
        }
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
