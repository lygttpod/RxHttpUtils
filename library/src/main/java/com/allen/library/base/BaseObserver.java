package com.allen.library.base;

import android.widget.Toast;


import com.allen.library.exception.ApiException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;


/**
 * Created by Allen on 2017/5/3.
 * <p>
 * 基类BaseObserver
 */

public abstract class BaseObserver<T extends BaseResponse> implements Observer<T>, ISubscriber<T> {

    private Toast mToast;

    protected void doOnNetError() {
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        doOnSubscribe(d);
    }

    @Override
    public void onNext(@NonNull T t) {
        doOnNext(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (e instanceof SocketTimeoutException) {
            setError(ApiException.errorMsg_SocketTimeoutException);
        } else if (e instanceof ConnectException) {
            setError(ApiException.errorMsg_ConnectException);
        } else if (e instanceof UnknownHostException) {
            setError(ApiException.errorMsg_UnknownHostException);
        } else {

            String error = handleError(e);
            showToast(error);
            doOnError(error);
        }
    }


    @Override
    public void onComplete() {
        doOnCompleted();
    }


    private void setError(String errorMsg) {
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
            mToast = Toast.makeText(BaseRxHttpApplication.getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    /**
     * 错误处理
     *
     * @param e
     * @return
     */
    private String handleError(Throwable e) {
        String error = null;
        try {
            ResponseBody errorBody = ((HttpException) e).response().errorBody();
            error = errorBody.string();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return error;
    }
}
