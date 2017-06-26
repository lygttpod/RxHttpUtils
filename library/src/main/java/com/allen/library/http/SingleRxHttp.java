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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by allen on 2017/6/22.
 * 网络请求-----可以对每个请求单独配置参数
 */

public class SingleRxHttp {

    private static SingleRxHttp instance;

    private String baseUrl;

    private Map<String, Object> headerMaps = new HashMap<>();

    private boolean isShowLog = true;
    private boolean cache = false;
    private boolean saveCookie = true;

    private String cachePath;
    private long cacheMaxSize;

    private long readTimeout;
    private long writeTimeout;
    private long connectTimeout;

    private SSLUtils.SSLParams sslParams;

    private OkHttpClient okClient;

    public static SingleRxHttp getInstance() {
        if (instance == null) {
            synchronized (SingleRxHttp.class) {
                if (instance == null) {
                    instance = new SingleRxHttp();
                }
            }

        }
        return instance;
    }

    public SingleRxHttp baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public SingleRxHttp addHeaders(Map<String, Object> headerMaps) {
        this.headerMaps = headerMaps;
        return this;
    }

    public SingleRxHttp log(boolean isShowLog) {
        this.isShowLog = isShowLog;
        return this;
    }

    public SingleRxHttp cache(boolean cache) {
        this.cache = cache;
        return this;
    }

    public SingleRxHttp saveCookie(boolean saveCookie) {
        this.saveCookie = saveCookie;
        return this;
    }

    public SingleRxHttp cachePath(String cachePath, long maxSize) {
        this.cachePath = cachePath;
        this.cacheMaxSize = maxSize;
        return this;
    }

    public SingleRxHttp readTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public SingleRxHttp writeTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public SingleRxHttp connectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public SingleRxHttp sslSocketFactory(InputStream[] certificates, InputStream bksFile, String password) {
        sslParams = SSLUtils.getSslSocketFactory(certificates, bksFile, password);
        return this;
    }

    public SingleRxHttp client(OkHttpClient okClient) {
        this.okClient = okClient;
        return this;
    }

    /**
     * 使用自己自定义参数创建请求
     *
     * @param cls
     * @param <K>
     * @return
     */
    public <K> K createSApi(Class<K> cls) {
        return getSingleRetrofitBuilder().build().create(cls);
    }


    /**
     * 单个RetrofitBuilder
     *
     * @return
     */
    public Retrofit.Builder getSingleRetrofitBuilder() {

        Retrofit.Builder singleRetrofitBuilder = new Retrofit.Builder();
        singleRetrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient == null ? getSingleOkHttpBuilder().build() : okClient);

        if (!TextUtils.isEmpty(baseUrl)) {
            singleRetrofitBuilder.baseUrl(baseUrl);
        }
        return singleRetrofitBuilder;
    }

    /**
     * 获取单个 OkHttpClient.Builder
     *
     * @return
     */
    public OkHttpClient.Builder getSingleOkHttpBuilder() {

        OkHttpClient.Builder singleOkHttpBuilder = new OkHttpClient.Builder();

        singleOkHttpBuilder.retryOnConnectionFailure(true);

        singleOkHttpBuilder.addInterceptor(new HeaderInterceptor(headerMaps));

        if (cache) {
            CacheInterceptor cacheInterceptor = new CacheInterceptor();
            Cache cache;
            if (!TextUtils.isEmpty(cachePath) && cacheMaxSize > 0) {
                cache = new Cache(new File(cachePath), cacheMaxSize);
            } else {
                cache = new Cache(new File(Environment.getExternalStorageDirectory().getPath() + "/rxHttpCacheData")
                        , 1024 * 1024 * 100);
            }
            singleOkHttpBuilder.addInterceptor(cacheInterceptor)
                    .addNetworkInterceptor(cacheInterceptor)
                    .cache(cache);
        }
        if (isShowLog) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.e("RxHttpUtils",message);

                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            singleOkHttpBuilder.addInterceptor(loggingInterceptor);
        }

        if (saveCookie) {
            singleOkHttpBuilder
                    .addInterceptor(new AddCookiesInterceptor())
                    .addInterceptor(new ReceivedCookiesInterceptor());
        }

        singleOkHttpBuilder.readTimeout(readTimeout > 0 ? readTimeout : 10, TimeUnit.SECONDS);

        singleOkHttpBuilder.writeTimeout(writeTimeout > 0 ? writeTimeout : 10, TimeUnit.SECONDS);

        singleOkHttpBuilder.connectTimeout(connectTimeout > 0 ? connectTimeout : 10, TimeUnit.SECONDS);


        if (sslParams != null) {
            singleOkHttpBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        }


        return singleOkHttpBuilder;
    }
}
