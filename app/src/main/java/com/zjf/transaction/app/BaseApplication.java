package com.zjf.transaction.app;

import android.app.Application;

import org.litepal.LitePal;

/**
 * Created by zjfcabbage on 2019/2/6
 *
 * @author 糟老头子 zjfcabbage
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        AppConfig.initApplication(this);
    }

}
