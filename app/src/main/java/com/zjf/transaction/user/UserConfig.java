package com.zjf.transaction.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.zjf.transaction.app.AppConfig;
import com.zjf.transaction.user.model.City;
import com.zjf.transaction.user.model.User;

/**
 * Created by zhengjiafeng on 2019/3/12
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class UserConfig {
    private static final String USER_PREFERENCE = "user_preference";
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_account";
    private static final String USER_PASSWORD = "user_password";
    private static final String USER_PIC_URL = "user_pic_url";
    private static final String USER_PROVINCE = "user_province";
    private static final String USER_CITY = "user_city";
    private static final String USER_UNIVERSITY = "user_university";
    private static final String USER = "user";
    private static final String LAST_LOGIN_TIME = "last_login_time";
    private SharedPreferences preferences;
    private static volatile UserConfig instance;

    private UserConfig() {
        preferences = AppConfig.context().getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
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

    public void setUserId(String userId) {
        preferences.edit().putString(USER_ID, userId).apply();
    }

    public String getUserId() {
        return preferences.getString(USER_ID, "");
    }

    public void setUserName(String userName) {
        preferences.edit().putString(USER_NAME, userName).apply();
    }

    public String getUserName() {
        return preferences.getString(USER_NAME, "");
    }

    public void setUserPassword(String password) {
        preferences.edit().putString(USER_PASSWORD, password).apply();
    }

    public String getUserPassword() {
        return preferences.getString(USER_PASSWORD, "");
    }


    public void setUserPicUrl(String userPicUrl) {
        preferences.edit().putString(USER_PIC_URL, userPicUrl).apply();
    }

    public String getUserPicUrl() {
        return preferences.getString(USER_PIC_URL, "");
    }

    public void setUserProvince(String province) {
        preferences.edit().putString(USER_PROVINCE, province).apply();
    }

    public String getUserProvince() {
        return preferences.getString(USER_PROVINCE, "");
    }

    public void setUserCity(String city) {
        preferences.edit().putString(USER_CITY, city).apply();
    }

    public String getUserCity() {
        return preferences.getString(USER_CITY, "");
    }

    public String getUserUniversity() {
        return preferences.getString(USER_UNIVERSITY, "");
    }

    public void setUserUniversity(String university) {
        preferences.edit().putString(USER_UNIVERSITY, university).apply();
    }

    public User getUser() {
        String userJson = preferences.getString(USER, "");
        return new Gson().fromJson(userJson, User.class);
    }

    public void setUser(User user) {
        preferences.edit().putString(USER, new Gson().toJson(user)).apply();
    }

    public void setLastLoginTime(long lastLoginTime) {
        preferences.edit().putLong(LAST_LOGIN_TIME, lastLoginTime).apply();
    }

    public long getLastLoginTime() {
        return preferences.getLong(LAST_LOGIN_TIME, System.currentTimeMillis());
    }
}
