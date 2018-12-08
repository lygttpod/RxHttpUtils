package com.allen.library.http;

import com.allen.library.config.OkHttpConfig;
import com.allen.library.config.RetrofitConfig;
import com.allen.library.gson.GsonAdapter;
import com.allen.library.interceptor.RxHttpLogger;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created on 2017/5/3.
 *
 * @author Allen
 * <p>
 * RetrofitClient工具类
 */

public class RetrofitClient {

    private static RetrofitClient instance;

    private Retrofit.Builder mRetrofitBuilder;

    private OkHttpClient okHttpClient;

    public RetrofitClient() {

        initDefaultOkHttpClient();

        mRetrofitBuilder = new Retrofit.Builder();

        CallAdapter.Factory[] callAdapterFactories = RetrofitConfig.getInstance().getCallAdapterFactory();
        Converter.Factory[] converterFactories = RetrofitConfig.getInstance().getConverterFactory();

        if (null != callAdapterFactories && callAdapterFactories.length > 0) {
            for (CallAdapter.Factory factory : callAdapterFactories) {
                mRetrofitBuilder.addCallAdapterFactory(factory);
            }
        } else {
            mRetrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        }
        if (null != converterFactories && converterFactories.length > 0) {
            for (Converter.Factory factory : converterFactories) {
                mRetrofitBuilder.addConverterFactory(factory);
            }
        } else {
            mRetrofitBuilder.addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(GsonAdapter.buildGson()));
        }
    }

    private void initDefaultOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(10, TimeUnit.SECONDS);

        SSLUtils.SSLParams sslParams = SSLUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new RxHttpLogger());
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);

        okHttpClient = builder.build();
    }


    public static RetrofitClient getInstance() {

        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }

        }
        return instance;
    }


    public Retrofit.Builder getRetrofitBuilder() {
        return mRetrofitBuilder;
    }

    public Retrofit getRetrofit() {
        if (null == OkHttpConfig.getOkHttpClient()) {
            return mRetrofitBuilder.client(okHttpClient).build();
        } else {
            return mRetrofitBuilder.client(OkHttpConfig.getOkHttpClient()).build();
        }
    }

}
