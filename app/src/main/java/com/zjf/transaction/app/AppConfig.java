package com.zjf.transaction.app;

import android.app.Application;
import android.support.annotation.NonNull;

/**
 * Created by zjfcabbage on 2019/2/5
 *
 * @author 糟老头子 zjfcabbage
 */
public class AppConfig {
    public static final String BASE_URL = "http://";

    private static volatile Application application;

    public static void initApplication(Application appcontext) {
        application = appcontext;
    }

    @NonNull
    public static synchronized Application getApplication() {
        if (application == null) {
            throw new IllegalStateException("App may be not initialized!");
        }
        return application;
    }
}
