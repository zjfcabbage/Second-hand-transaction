package com.zjf.transaction.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zjf.transaction.app.AppNavigtor;
import com.zjf.transaction.base.BaseActivity;

/**
 * Created by zhengjiafeng on 2019/2/7
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //模拟开屏
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                AppNavigtor.startLoginActivity(SplashActivity.this);
                finish();
            }
        }).start();
    }
}
