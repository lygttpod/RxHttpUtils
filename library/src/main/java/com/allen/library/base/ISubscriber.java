package com.allen.library.base;

/**
 * Created by allen on 2016/12/21.
 * <p>
 * 定义请求结果处理接口
 */

public interface ISubscriber<T extends BaseResponse> {

    //      void doOnError(Throwable e);

    void doOnError(String errorMsg);

    void doOnNext(T t);

    void doOnCompleted();
}
