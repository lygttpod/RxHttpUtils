package com.allen.library.http;


import android.app.Dialog;


import com.allen.library.base.BaseObserver;
import com.allen.library.base.BaseResponse;

import io.reactivex.disposables.Disposable;

/**
 * Created by Allen on 2017/5/3.
 * 通用的Observer
 */

public abstract class CommonObserver<T extends BaseResponse> extends BaseObserver<T> {


    private Dialog mProgressDialog;

    public CommonObserver() {
    }

    public CommonObserver(Dialog progressDialog) {
        mProgressDialog = progressDialog;
    }

    /**
     * 获取disposable 在onDestroy方法中取消订阅disposable.dispose()
     */
    protected abstract void getDisposable(Disposable d);

    /**
     * 失败回调
     *
     * @param errorMsg
     */
    protected abstract void onError(String errorMsg);

    /**
     * 成功回调
     *
     * @param t
     */
    protected abstract void onSuccess(T t);


    @Override
    public void doOnSubscribe(Disposable d) {
        getDisposable(d);
    }

    @Override
    public void doOnError(String errorMsg) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        onError(errorMsg);
    }

    @Override
    public void doOnNext(T t) {
        onSuccess(t);
    }

    @Override
    public void doOnCompleted() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
