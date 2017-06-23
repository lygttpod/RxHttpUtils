package com.allen.library.download;

import android.widget.Toast;

import com.allen.library.base.BaseRxHttpApplication;
import com.allen.library.exception.ApiException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;

/**
 * Created by allen on 2017/6/13.
 *
 */

public abstract class BaseDownloadObserver implements Observer<ResponseBody> {

    private Toast mToast;

    /**
     * 失败回调
     *
     * @param errorMsg
     */
    protected abstract void doOnError(String errorMsg);


    @Override
    public void onError(@NonNull Throwable e) {
        if (e instanceof SocketTimeoutException) {
            setError(ApiException.errorMsg_SocketTimeoutException);
        } else if (e instanceof ConnectException) {
            setError(ApiException.errorMsg_ConnectException);
        } else if (e instanceof UnknownHostException) {
            setError(ApiException.errorMsg_UnknownHostException);
        } else {
            doOnError(e.getMessage().toString());
        }
    }

    private void setError(String errorMsg) {
        showToast(errorMsg);
        doOnError(errorMsg);
    }

    /**
     * Toast提示
     *
     * @param msg 提示内容
     */
    protected void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseRxHttpApplication.getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
