package com.zjf.transaction.app;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by zjfcabbage on 2019/2/5
 *
 * @author 糟老头子 zjfcabbage
 */
public class AppConfig {
    public static final String BASE_URL = "http://";

    private static volatile Context application;

    public static void initApplication(Context appContext) {
        application = appContext;
    }

    @NonNull
    public static synchronized Context context() {
        if (application == null) {
            throw new IllegalStateException("App may be not initialized!");
        }
        return application;
    }
}
