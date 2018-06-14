package com.allen.library.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.allen.library.utils.NetUtils.isNetworkConnected;

/**
 * <pre>
 *      @author : Allen
 *      date    : 2018/06/14
 *      desc    : 网络缓存 参考 https://www.jianshu.com/p/cf59500990c7
 *      version : 1.0
 * </pre>
 */

public class NetCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        boolean connected = isNetworkConnected();
        if (connected) {
            //如果有网络，缓存60s
            Response response = chain.proceed(request);
            int maxTime = 60;
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxTime)
                    .build();
        }
        //如果没有网络，不做处理，直接返回
        return chain.proceed(request);
    }

}
