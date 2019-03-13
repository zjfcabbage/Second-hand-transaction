package com.zjf.transaction.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.zjf.transaction.app.AppConfig;

/**
 * Created by zhengjiafeng on 2019/3/12
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class UserConfig {
    private static final String USER_PREFERENCE = "user_preference";
    private static final String USER_ACCOUNT = "user_account";
    private static final String USER_PASSWORD = "user_password";
    private static final String USER_PROVINCE = "user_province";
    private static final String USER_CITY = "user_city";
    private static final String USER_UNIVERSITY = "user_university";
    private SharedPreferences preferences;
    private static volatile UserConfig instance;

    private UserConfig() {
        preferences = AppConfig.getApplication().getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static UserConfig inst() {
        if (instance == null) {
            synchronized (UserConfig.class) {
                if (instance == null) {
                    instance = new UserConfig();
                }
            }
        }
        return instance;
    }

    public void setUserAccount(String account) {
        preferences.edit().putString(USER_ACCOUNT, account).apply();
    }

    public String getUserAccount() {
        return preferences.getString(USER_ACCOUNT, "");
    }

    public void setUserPassword(String password) {
        preferences.edit().putString(USER_PASSWORD, password).apply();
    }

    public String getUserPassword() {
        return preferences.getString(USER_PASSWORD, "");
    }

    public String getUserProvince() {
        return preferences.getString(USER_PROVINCE, "");
    }

    public String getUserCity() {
        return preferences.getString(USER_CITY, "");
    }

    public String getUserUniversity() {
        return preferences.getString(USER_UNIVERSITY, "");
    }

    public void setUserProvince(String province) {
        preferences.edit().putString(USER_PROVINCE, province).apply();
    }

    public void setUserCity(String city) {
        preferences.edit().putString(USER_CITY, city).apply();
    }

    public void setUserUniversity(String university){
        preferences.edit().putString(USER_UNIVERSITY, university).apply();
    }
}
