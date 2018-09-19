package com.allen.library.exception;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.io.NotSerializableException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * @author Allen
 * Created by Allen on 2017/10/23.
 * 异常类型
 */

public class ApiException extends Exception {

    private final int code;
    private String message;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
        this.message = throwable.getMessage();
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ApiException(httpException, httpException.code());
            ex.message = httpException.getMessage();
        } else if (e instanceof SocketTimeoutException) {
            ex = new ApiException(e, ERROR.TIMEOUT_ERROR);
            ex.message = "网络连接超时，请检查您的网络状态，稍后重试！";
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, ERROR.TIMEOUT_ERROR);
            ex.message = "网络连接异常，请检查您的网络状态，稍后重试！";
        } else if (e instanceof ConnectTimeoutException) {
            ex = new ApiException(e, ERROR.TIMEOUT_ERROR);
            ex.message = "网络连接超时，请检查您的网络状态，稍后重试！";
        } else if (e instanceof UnknownHostException) {
            ex = new ApiException(e, ERROR.TIMEOUT_ERROR);
            ex.message = "网络连接异常，请检查您的网络状态，稍后重试！";
        } else if (e instanceof NullPointerException) {
            ex = new ApiException(e, ERROR.NULL_POINTER_EXCEPTION);
            ex.message = "空指针异常";
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ApiException(e, ERROR.SSL_ERROR);
            ex.message = "证书验证失败";
        } else if (e instanceof ClassCastException) {
            ex = new ApiException(e, ERROR.CAST_ERROR);
            ex.message = "类型转换错误";
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof JsonSerializer
                || e instanceof NotSerializableException
                || e instanceof ParseException) {
            ex = new ApiException(e, ERROR.PARSE_ERROR);
            ex.message = "解析错误";
        } else if (e instanceof IllegalStateException) {
            ex = new ApiException(e, ERROR.ILLEGAL_STATE_ERROR);
            ex.message = e.getMessage();
        } else {
            ex = new ApiException(e, ERROR.UNKNOWN);
        }
        return ex;
    }

    /**
     * 约定异常
     */
    public static class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1001;
        /**
         * 空指针错误
         */
        public static final int NULL_POINTER_EXCEPTION = 1002;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1003;

        /**
         * 类转换错误
         */
        public static final int CAST_ERROR = 1004;

        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1005;

        /**
         * 非法数据异常
         */
        public static final int ILLEGAL_STATE_ERROR = 1006;

    }
}
