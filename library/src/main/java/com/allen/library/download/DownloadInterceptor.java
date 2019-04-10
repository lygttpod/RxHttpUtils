package com.allen.library.download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *      @author : Allen
 *      e-mail  : lygttpod@163.com
 *      date    : 2019/04/09
 *      desc    : 来取消Gzip压缩，Content-Length便是正常数据,否则有的接口通过Gzip压缩Content-Length返回为-1
 * </pre>
 */
public class DownloadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request.newBuilder()
                .addHeader("Accept-Encoding", "identity")
                .build());
        return response;
    }
}
