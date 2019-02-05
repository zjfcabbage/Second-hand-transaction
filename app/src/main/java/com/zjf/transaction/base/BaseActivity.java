package com.zjf.transaction.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zjf.transaction.util.LogUtil;

/**
 * Created by zhengjiafeng on 2019/2/4
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("=== onCreate : " + getLocalClassName() + " ===");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d("=== onStart : " + getLocalClassName() + " ===");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d("=== onResume : " + getLocalClassName() + " ===");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d("=== onPause : " + getLocalClassName() + " ===");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d("=== onStop : " + getLocalClassName() + " ===");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("=== onDestroy : " + getLocalClassName() + " ===");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.d("=== onRestart : " + getLocalClassName() + " ===");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.d("=== onNewIntent : " + getLocalClassName() + " ===");
    }
}
