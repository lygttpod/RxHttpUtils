package com.allen.library.http;

import com.allen.library.config.RetrofitConfig;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Allen on 2017/5/3.
 * <p>
 *
 * @author Allen
 * 网络请求工具类---使用的是全局配置的变量
 */

public class GlobalRxHttp {

    private static GlobalRxHttp instance;

    /**
     * 缓存retrofit针对同一个ApiService不会重复创建retrofit对象
     */
    private static HashMap<String, Object> retrofitServiceCache;

    public GlobalRxHttp() {
        retrofitServiceCache = new HashMap<>();
    }

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
     * 为Retrofit设置CallAdapterFactory
     * 注意：需在调用SetBaseUrl之前调用
     *
     * @param factories callAdapterFactory
     * @return CallAdapterFactory[]
     */
    public GlobalRxHttp setCallAdapterFactory(CallAdapter.Factory... factories) {
        if (null != factories) {
            RetrofitConfig.getInstance().addCallAdapterFactory(factories);
        }
        return this;
    }

    /**
     * 为Retrofit设置ConverterFactory
     * 注意：需在调用SetBaseUrl之前调用
     *
     * @param factories converterFactory
     * @return ConverterFactory[]
     */
    public GlobalRxHttp setConverterFactory(Converter.Factory... factories) {
        if (null != factories) {
            RetrofitConfig.getInstance().addConverterFactory(factories);
        }
        return this;
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
    private Retrofit.Builder getGlobalRetrofitBuilder() {
        return RetrofitClient.getInstance().getRetrofitBuilder();
    }


    /**
     * 使用全局变量的请求
     *
     * @param cls
     * @param <K>
     * @return
     */
    public static <K> K createGApi(final Class<K> cls) {
        if (retrofitServiceCache == null) {
            retrofitServiceCache = new HashMap<>();
        }
        K retrofitService = (K) retrofitServiceCache.get(cls.getCanonicalName());
        if (retrofitService == null) {
            retrofitService = getGlobalRetrofit().create(cls);
            retrofitServiceCache.put(cls.getCanonicalName(), retrofitService);
        }
        return retrofitService;
    }


}
