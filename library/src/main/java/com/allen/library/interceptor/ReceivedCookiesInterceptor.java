package com.allen.library.interceptor;


import com.allen.library.constant.SPKeys;
import com.allen.library.utils.SPUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

import static java.util.Calendar.getInstance;

/**
 * Created by allen on 2017/5/11.
 *
 * 接受服务器发的cookie   并保存到本地
 */

public class ReceivedCookiesInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        //这里获取请求返回的cookie
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }
            SPUtils.put(SPKeys.COOKIE,cookies);
        }
        //获取服务器相应时间--用于计算倒计时的时间差
        if (!originalResponse.header("Date").isEmpty()){
            long date = dateToStamp(originalResponse.header("Date"));
            SPUtils.put(SPKeys.DATE,date);
        }

        return originalResponse;
    }

    /*
   * 将时间转换为时间戳
   */
    public static long dateToStamp(String s) throws android.net.ParseException {
        Date date = new Date(s); //转换为标准时间对象
        Calendar calendar= getInstance();
        calendar.setTime(date);
        long mTimeInMillis = calendar.getTimeInMillis();
        return mTimeInMillis;
    }
}
