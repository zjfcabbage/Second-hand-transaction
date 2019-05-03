package com.zjf.transaction.app;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

/**
 * Created by zjfcabbage on 2019/2/5
 *
 * @author 糟老头子 zjfcabbage
 */
public class AppConfig {
    public static final String BASE_URL = "http://47.100.61.176:8080";
    public static final String FIRST_START = "first_start";
    private static SharedPreferences preferences;

    private static volatile Context application;

    public static void initApplication(Context appContext) {
        application = appContext;
        preferences = appContext.getSharedPreferences("app", Context.MODE_PRIVATE);
    }

    @NonNull
    public static synchronized Context context() {
        if (application == null) {
            throw new IllegalStateException("App may be not initialized!");
        }
        return application;
    }

    public static void setFirstStart(boolean y) {
        preferences.edit().putBoolean(FIRST_START, y).apply();
    }

    public static boolean isFirstStart() {
        return preferences.getBoolean(FIRST_START, true);
    }
}
