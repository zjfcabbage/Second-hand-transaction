package com.zjf.transaction.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.zjf.transaction.app.AppConfig;

/**
 * Created by zhengjiafeng on 2019/3/12
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class UserPreference {
    private static final String USER_PREFERENCE = "user_preference";
    private static final String USER_ACCOUNT = "user_account";
    private static final String USER_PASSWORD = "user_password";
    private SharedPreferences preferences;
    private static volatile UserPreference instance;

    private UserPreference(){
        preferences = AppConfig.getApplication().getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static UserPreference inst() {
        if (instance == null) {
            synchronized (UserPreference.class) {
                if (instance == null) {
                    instance = new UserPreference();
                }
            }
        }
        return instance;
    }

    public void setUserAccount(String account) {
        preferences.edit().putString(USER_ACCOUNT, account).apply();
    }

    public String getUserAccount() {
        return preferences.getString(USER_ACCOUNT, null);
    }

    public void setUserPassword(String password) {
        preferences.edit().putString(USER_PASSWORD, password).apply();
    }

    public String getUserPassword() {
        return preferences.getString(USER_PASSWORD, null);
    }


}
