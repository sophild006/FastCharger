package com.fast.charge.util;

import android.content.Context;

/**
 * Created by Administrator on 2017/5/19.
 */

public class GlobalContext {
    private GlobalContext() {
    }

    private static Context sAppContext;

    public static final void setAppContext(Context appContext) {
        if (appContext != null) {
            sAppContext = appContext.getApplicationContext();
        }
    }

    public static final Context getAppContext() {
        assert sAppContext != null;
        return sAppContext;
    }
}
