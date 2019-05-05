package com.zjf.transaction.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zjf.transaction.app.AppConfig;
import com.zjf.transaction.user.LoginActivity;
import com.zjf.transaction.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by zjfcabbage on 2019/2/4
 *
 * @author 糟老头子 zjfcabbage
 */
public class BaseActivity extends AppCompatActivity {
    public static final String KEY_BUNDLE = "key_bundle";
    private static ArrayList<Activity> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList.add(this);
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
        arrayList.remove(this);
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

    public static void logout() {
        if (arrayList.isEmpty()) {
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).finish();
        }
        arrayList.clear();
        start(AppConfig.context(), LoginActivity.class);
    }
}
