package com.allen.library.http;

import android.text.TextUtils;

import com.allen.library.RxHttpUtils;
import com.allen.library.cookie.CookieJarImpl;
import com.allen.library.cookie.store.CookieStore;
import com.allen.library.interceptor.HeaderInterceptor;
import com.allen.library.interceptor.NetCacheInterceptor;
import com.allen.library.interceptor.NoNetCacheInterceptor;
import com.allen.library.interceptor.RxHttpLogger;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by allen on 2017/6/22.
 *
 * @author Allen
 * 网络请求-----可以对每个请求单独配置参数
 */

public class SingleRxHttp {

    private static SingleRxHttp instance;

    private String baseUrl;

    private Map<String, Object> headerMaps = new HashMap<>();

    private boolean isShowLog = true;
    private boolean cache = false;
    private CookieStore cookieStore;

    private String cachePath;
    private long cacheMaxSize;

    private long readTimeout;
    private long writeTimeout;
    private long connectTimeout;

    private SSLUtils.SSLParams sslParams;

    private OkHttpClient okClient;

    private List<Converter.Factory> converterFactories = new ArrayList<>();
    private List<CallAdapter.Factory> adapterFactories = new ArrayList<>();

    /**
     * 不使用单利模式是因为单个请求的参数配置是单次有效的
     *
     * @return SingleRxHttp
     */
    public static SingleRxHttp getInstance() {
        instance = new SingleRxHttp();
        return instance;
    }

    public SingleRxHttp baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    /**
     * 局部设置Converter.Factory,默认GsonConverterFactory.create()
     */
    public SingleRxHttp addConverterFactory(Converter.Factory factory) {
        if (factory != null) {
            converterFactories.add(factory);
        }
        return this;
    }

    /**
     * 局部设置CallAdapter.Factory,默认RxJavaCallAdapterFactory.create()
     */
    public SingleRxHttp addCallAdapterFactory(CallAdapter.Factory factory) {
        if (factory != null) {
            adapterFactories.add(factory);
        }
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

    public SingleRxHttp cookieType(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
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

    /**
     * 信任所有证书,不安全有风险
     *
     * @return
     */
    public SingleRxHttp sslSocketFactory() {
        sslParams = SSLUtils.getSslSocketFactory();
        return this;
    }

    /**
     * 使用预埋证书，校验服务端证书（自签名证书）
     *
     * @param certificates
     * @return
     */
    public SingleRxHttp sslSocketFactory(InputStream... certificates) {
        sslParams = SSLUtils.getSslSocketFactory(certificates);
        return this;
    }

    /**
     * 使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
     *
     * @param bksFile
     * @param password
     * @param certificates
     * @return
     */
    public SingleRxHttp sslSocketFactory(InputStream bksFile, String password, InputStream... certificates) {
        sslParams = SSLUtils.getSslSocketFactory(bksFile, password, certificates);
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

        if (converterFactories.isEmpty()) {
            //获取全局的对象重新设置
            List<Converter.Factory> listConverterFactory = RetrofitClient.getInstance().getRetrofit().converterFactories();
            for (Converter.Factory factory : listConverterFactory) {
                singleRetrofitBuilder.addConverterFactory(factory);
            }
        } else {
            for (Converter.Factory converterFactory : converterFactories) {
                singleRetrofitBuilder.addConverterFactory(converterFactory);
            }
        }

        if (adapterFactories.isEmpty()) {
            //获取全局的对象重新设置
            List<CallAdapter.Factory> listAdapterFactory = RetrofitClient.getInstance().getRetrofit().callAdapterFactories();
            for (CallAdapter.Factory factory : listAdapterFactory) {
                singleRetrofitBuilder.addCallAdapterFactory(factory);
            }

        } else {
            for (CallAdapter.Factory adapterFactory : adapterFactories) {
                singleRetrofitBuilder.addCallAdapterFactory(adapterFactory);
            }
        }


        if (TextUtils.isEmpty(baseUrl)) {
            singleRetrofitBuilder.baseUrl(RetrofitClient.getInstance().getRetrofit().baseUrl());
        } else {
            singleRetrofitBuilder.baseUrl(baseUrl);
        }

        singleRetrofitBuilder.client(okClient == null ? getSingleOkHttpBuilder().build() : okClient);

        return singleRetrofitBuilder;
    }

    /**
     * 获取单个 OkHttpClient.Builder
     *
     * @return
     */
    private OkHttpClient.Builder getSingleOkHttpBuilder() {

        OkHttpClient.Builder singleOkHttpBuilder = new OkHttpClient.Builder();


        if (null != cookieStore) {
            singleOkHttpBuilder.cookieJar(new CookieJarImpl(cookieStore));
        }


        if (cache) {
            Cache cache = null;
            if (!TextUtils.isEmpty(cachePath) && cacheMaxSize > 0) {
                cache = new Cache(new File(cachePath), cacheMaxSize);
            } else {
                File externalCacheDir = RxHttpUtils.getContext().getExternalCacheDir();
                if (null != externalCacheDir) {
                    cache = new Cache(new File(externalCacheDir.getPath() + "/RxHttpCacheData")
                            , 1024 * 1024 * 100);
                }

            }
            if (null != cache) {
                singleOkHttpBuilder
                        .cache(cache)
                        .addInterceptor(new NoNetCacheInterceptor())
                        .addNetworkInterceptor(new NetCacheInterceptor());
            }
        }

        if (sslParams != null) {
            singleOkHttpBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        }

        singleOkHttpBuilder.readTimeout(readTimeout > 0 ? readTimeout : 10, TimeUnit.SECONDS);

        singleOkHttpBuilder.writeTimeout(writeTimeout > 0 ? writeTimeout : 10, TimeUnit.SECONDS);

        singleOkHttpBuilder.connectTimeout(connectTimeout > 0 ? connectTimeout : 10, TimeUnit.SECONDS);
        singleOkHttpBuilder.retryOnConnectionFailure(true);

        singleOkHttpBuilder.addInterceptor(new HeaderInterceptor(headerMaps));


        if (isShowLog) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new RxHttpLogger());
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            singleOkHttpBuilder.addInterceptor(logInterceptor);
        }


        return singleOkHttpBuilder;
    }

}
