package com.allen.library.base;

import android.app.Dialog;
import android.util.Log;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by allen on 2016/12/21.
 * <p>
 * 请求结果统一处理基类  用户可以继承此类实现自己需要的方法
 */

public abstract class BaseSubscriber<T extends BaseResponse> extends Subscriber<T> implements ISubscriber<T> {

    private static final String TAG = "allen";

    private static final String errorMsg_SocketTimeoutException = "网络链接超时，请检查您的网络状态，稍后重试！";
    private static final String errorMsg_ConnectException = "网络链接异常，请检查您的网络状态";
    private static final String errorMsg_UnknownHostException = "网络异常，请检查您的网络状态";

    private Toast mToast;

    private Dialog mLoadingDialog;

    public BaseSubscriber(Dialog mLoadingDialog) {
        this.mLoadingDialog = mLoadingDialog;
    }

    public BaseSubscriber() {
    }

    protected void doOnNetError() {
    }

    @Override
    public void onCompleted() {
        cancelLoadingDialog();
        doOnCompleted();
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            Log.e(TAG, "onError: SocketTimeoutException----" + errorMsg_SocketTimeoutException);

            setOnError(errorMsg_SocketTimeoutException);

        } else if (e instanceof ConnectException) {
            Log.e(TAG, "onError: ConnectException-----" + errorMsg_ConnectException);

            setOnError(errorMsg_ConnectException);

        } else if (e instanceof UnknownHostException) {
            Log.e(TAG, "onError: UnknownHostException-----" + errorMsg_UnknownHostException);

            setOnError(errorMsg_UnknownHostException);

        } else {
            Log.e(TAG, "onError:----" + e.getMessage());
            showToast(e.getMessage());
            doOnError(e.getMessage());
        }
        doOnCompleted();
        cancelLoadingDialog();
    }


    @Override
    public void onNext(T t) {
        if (t.getCode() == 0) {
            doOnNext(t);
        } else if (t.getCode() == 409) {
            showToast(t.getMsg());
        } else {
            Log.e(TAG, "onNext: ----" + t.getMsg());
            doOnError(t.getMsg());
        }

    }

    /**
     * 设置错误处理
     *
     * @param errorMsg 错误信息
     */
    private void setOnError(String errorMsg) {
        showToast(errorMsg);
        doOnError(errorMsg);
        doOnNetError();
    }

    /**
     * Toast提示
     *
     * @param msg 提示内容
     */
    protected void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    /**
     * 显示loading对话框
     */
    public void showLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.show();
        }
    }

    /**
     * 取消loading对话框
     */
    private void cancelLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}
