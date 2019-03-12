package com.zjf.transaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.zjf.transaction.base.BaseActivity;
import com.zjf.transaction.user.LoginActivity;
import com.zjf.transaction.user.UserPreference;

/**
 * Created by zhengjiafeng on 2019/3/12
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String account = UserPreference.inst().getUserAccount();
        String password = UserPreference.inst().getUserPassword();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            LoginActivity.start(this, LoginActivity.class);
        } else {
            MainActivity.start(this, MainActivity.class);
        }
    }
}
