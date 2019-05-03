package com.zjf.transaction.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zjf.transaction.util.LogUtil;

/**
 * Created by zjfcabbage on 2019/2/4
 *
 * @author 糟老头子 zjfcabbage
 */
public class BaseActivity extends AppCompatActivity {
    public static final String KEY_BUNDLE = "key_bundle";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("=== onCreate : %s ===", getLocalClassName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d("=== onStart : %s ===", getLocalClassName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d("=== onResume : %s ===", getLocalClassName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d("=== onPause : %s ===", getLocalClassName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d("=== onStop : %s ===", getLocalClassName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("=== onDestroy : %s ===", getLocalClassName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.d("=== onRestart : %s ===", getLocalClassName());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.d("=== onNewIntent : %s ===", getLocalClassName());
    }

    public static void start(Context context, Class<? extends BaseActivity> clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    public static void start(Context context, Bundle bundle, Class<? extends BaseActivity> clazz){
        Intent intent = new Intent(context, clazz);
        intent.putExtra(KEY_BUNDLE, bundle);
        context.startActivity(intent);
    }
}
