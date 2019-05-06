package com.zjf.transaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.text.TextUtils;

import com.zjf.transaction.app.AppConfig;
import com.zjf.transaction.base.BaseActivity;
import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.user.LoginActivity;
import com.zjf.transaction.user.UserConfig;
import com.zjf.transaction.user.api.impl.UserApiImpl;
import com.zjf.transaction.user.model.User;
import com.zjf.transaction.util.LogUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhengjiafeng on 2019/3/12
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //应用首次启动
        if (AppConfig.isFirstStart()) {
            LoginActivity.start(this, LoginActivity.class);
            AppConfig.setFirstStart(false);
            finish();
            return;
        }

        String account = UserConfig.inst().getUserName();
        String password = UserConfig.inst().getUserPassword();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            LoginActivity.start(this, LoginActivity.class);
        } else {
            tryLogin();
        }
    }

    @SuppressLint("CheckResult")
    private void tryLogin() {
        final long fiveDayTime = 5 * 24 * 60 * 60 * 1000;
        if (UserConfig.inst().getLastLoginTime() - System.currentTimeMillis() > fiveDayTime) {
            LoginActivity.start(this, LoginActivity.class);
            finish();
            return;
        }
        final String userName = UserConfig.inst().getUserName();
        final String password = UserConfig.inst().getUserPassword();
        UserApiImpl.login(userName, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataResult<User>>() {
                    @Override
                    public void accept(DataResult<User> userDataResult) throws Exception {
                        if (userDataResult.code == DataResult.CODE_SUCCESS) {
                            if (userDataResult.data != null) {
                                final User currentUser = userDataResult.data;
                                LogUtil.d("splashActivity -> %s", currentUser.toString());
                                UserConfig.inst().setUserName(currentUser.getUserName());
                                UserConfig.inst().setUserPassword(currentUser.getPassword());
                                UserConfig.inst().setUserProvince(currentUser.getProvince());
                                UserConfig.inst().setUserCity(currentUser.getCity());
                                UserConfig.inst().setUserUniversity(currentUser.getUniversity());
                                UserConfig.inst().setUserId(currentUser.getUserId());
                                UserConfig.inst().setUserPicUrl(currentUser.getUserPicUrl());
                                UserConfig.inst().setUser(currentUser);
                                UserConfig.inst().setLastLoginTime(System.currentTimeMillis());
                                LogUtil.d("login success");
                                MainActivity.start(SplashActivity.this, MainActivity.class);
                            }
                        }else {
                            LoginActivity.start(SplashActivity.this, LoginActivity.class);
                            LogUtil.e("login failed, msg -> %s", userDataResult.msg);
                        }
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LoginActivity.start(SplashActivity.this, LoginActivity.class);
                        LogUtil.e("login error, throwable -> %s", throwable.getMessage());
                        finish();
                    }
                });
    }
}
