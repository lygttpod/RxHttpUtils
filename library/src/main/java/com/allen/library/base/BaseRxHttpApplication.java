package com.allen.library.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by allen on 2016/12/21.
 * <p>
 * Application基类
 */

public class BaseRxHttpApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
