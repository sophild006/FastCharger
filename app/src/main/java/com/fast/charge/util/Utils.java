package com.fast.charge.util;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.provider.Settings;

import java.lang.reflect.Method;

/**
 * Created by bruce on 14-11-6.
 */
public final class Utils {

    private Utils() {
    }

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static Boolean isMobleDataOpend() {
        ConnectivityManager connectivityManager = (ConnectivityManager) GlobalContext.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Method declaredMethod = Class.forName(connectivityManager.getClass().getName()).getDeclaredMethod("getMobileDataEnabled", new Class[0]);
            declaredMethod.setAccessible(true);
            return (Boolean) declaredMethod.invoke(connectivityManager, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean isAirPlaneOpend(Context context) {
        return Settings.System.getInt(context.getContentResolver(), "airplane_mode_on", 0) != 0;
    }

}