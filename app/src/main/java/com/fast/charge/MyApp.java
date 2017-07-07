package com.fast.charge;

import android.app.Application;
import android.provider.Settings;

import com.fast.charge.util.GlobalContext;

/**
 * Created by Administrator on 2017/7/7.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GlobalContext.setAppContext(this);
    }
}
