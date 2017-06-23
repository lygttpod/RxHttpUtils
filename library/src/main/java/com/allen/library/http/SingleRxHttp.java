package com.allen.library.http;

import android.os.Environment;
import android.text.TextUtils;

import com.allen.library.download.DownloadRetrofit;
import com.allen.library.interceptor.AddCookiesInterceptor;
import com.allen.library.interceptor.CacheInterceptor;
import com.allen.library.interceptor.HeaderInterceptor;
import com.allen.library.interceptor.ReceivedCookiesInterceptor;
import com.allen.library.upload.UploadRetrofit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
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

    private String certificateName;


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

    public SingleRxHttp certificates(String certificateName) {
        this.certificateName = certificateName;
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
     * 下载文件
     *
     * @param fileUrl
     * @return
     */
    public static Observable<ResponseBody> downloadFile(String fileUrl) {
        return DownloadRetrofit.downloadFile(fileUrl);
    }

    /**
     * 上传单张图片
     *
     * @param uploadUrl
     * @param filePath
     * @return
     */
    public static Observable<ResponseBody> uploadImg(String uploadUrl, String filePath) {
        return UploadRetrofit.uploadImg(uploadUrl, filePath);

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
                .client(getSingleOkHttpBuilder().build());

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

        singleOkHttpBuilder.retryOnConnectionFailure(true)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS);

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
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            singleOkHttpBuilder.addInterceptor(loggingInterceptor);
        }

        if (saveCookie) {
            singleOkHttpBuilder
                    .addInterceptor(new AddCookiesInterceptor())
                    .addInterceptor(new ReceivedCookiesInterceptor());
        }

        if (readTimeout > 0) {
            singleOkHttpBuilder.readTimeout(readTimeout, TimeUnit.SECONDS);
        }
        if (writeTimeout > 0) {
            singleOkHttpBuilder.writeTimeout(writeTimeout, TimeUnit.SECONDS);
        }
        if (connectTimeout > 0) {
            singleOkHttpBuilder.connectTimeout(connectTimeout, TimeUnit.SECONDS);
        }

        if (!TextUtils.isEmpty(certificateName)) {
            if (!TextUtils.isEmpty(certificateName)) {
                SSLHelper.SSLParams sslParams = SSLHelper.getSSLParams(certificateName);
                if (sslParams.sSLSocketFactory != null && sslParams.trustManager != null) {
                    singleOkHttpBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
                }
            }
        }


        return singleOkHttpBuilder;
    }
}
