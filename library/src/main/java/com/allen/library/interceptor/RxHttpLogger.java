package com.allen.library.interceptor;

import android.util.Log;

import com.allen.library.utils.JsonUtil;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * <pre>
 *      @author : Allen
 *      date    : 2018/06/14
 *      desc    : 日志打印格式化处理  https://www.jianshu.com/p/e044cab4f530
 *      version : 1.0
 * </pre>
 */
public class RxHttpLogger implements HttpLoggingInterceptor.Logger {
    private StringBuffer mMessage = new StringBuffer();

    @Override
    public void log(String message) {
        // 请求或者响应开始
        if (message.startsWith("--> POST")) {
            mMessage.setLength(0);
            mMessage.append(" ");
            mMessage.append("\r\n");
        }
        if (message.startsWith("--> GET")) {
            mMessage.setLength(0);
            mMessage.append(" ");
            mMessage.append("\r\n");
        }
        // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
        if ((message.startsWith("{") && message.endsWith("}"))
                || (message.startsWith("[") && message.endsWith("]"))) {
            message = JsonUtil.formatJson(message);
        }
        mMessage.append(message.concat("\n"));
        // 请求或者响应结束，打印整条日志
        if (message.startsWith("<-- END HTTP")) {
            Log.e("RxHttpUtils", mMessage.toString());
        }
    }
}
