package com.allen.library.base;

/**
 * Created by allen on 2016/12/21.
 * <p>
 * 请求结果基类   所有请求结果继承此类
 */

public class BaseResponse {

    /**
     * 错误码
     */
    private int code;
    /**
     * 错误描述
     */
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
