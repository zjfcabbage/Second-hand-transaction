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
    private SharedPreferences preferences;
    private static volatile UserConfig instance;
    private static User user;

    private UserConfig() {
        user = new User();
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
        if (user.getUserId().equals(userId)) {
            return;
        }
        user.setUserId(userId);
        preferences.edit().putString(USER_ID, userId).apply();
    }

    public String getUserId() {
        if (!user.getUserId().isEmpty()) {
            return user.getUserId();
        }
        return preferences.getString(USER_ID, "");
    }

    public void setUserName(String userName) {
        if (user.getUserName().equals(userName)) {
            return;
        }
        user.setUserName(userName);
        preferences.edit().putString(USER_NAME, userName).apply();
    }

    public String getUserName() {
        if (!user.getUserName().isEmpty()) {
            return user.getUserName();
        }
        return preferences.getString(USER_NAME, "");
    }

    public void setUserPassword(String password) {
        if (user.getPassword().equals(password)) {
            return;
        }
        user.setPassword(password);
        preferences.edit().putString(USER_PASSWORD, password).apply();
    }

    public String getUserPassword() {
        if (!user.getPassword().isEmpty()) {
            return user.getPassword();
        }
        return preferences.getString(USER_PASSWORD, "");
    }


    public void setUserPicUrl(String userPicUrl) {
//        if (user.getUserPicUrl().equals(userPicUrl)) {
//            return;
//        }
        user.setUserPicUrl(userPicUrl);
        preferences.edit().putString(USER_PIC_URL, userPicUrl).apply();
    }

    public String getUserPicUrl() {
        if (!user.getUserPicUrl().isEmpty()) {
            return user.getUserPicUrl();
        }
        return preferences.getString(USER_PIC_URL, "");
    }

    public void setUserProvince(String province) {
        if (user.getProvince().equals(province)) {
            return;
        }
        user.setProvince(province);
        preferences.edit().putString(USER_PROVINCE, province).apply();
    }

    public String getUserProvince() {
        if (!user.getProvince().isEmpty()) {
            return user.getProvince();
        }
        return preferences.getString(USER_PROVINCE, "");
    }

    public void setUserCity(String city) {
        if (user.getCity().equals(city)) {
            return;
        }
        user.setCity(city);
        preferences.edit().putString(USER_CITY, city).apply();
    }

    public String getUserCity() {
        if (!user.getCity().isEmpty()) {
            return user.getCity();
        }
        return preferences.getString(USER_CITY, "");
    }

    public String getUserUniversity() {
        if (!user.getUniversity().isEmpty()) {
            return user.getUniversity();
        }
        return preferences.getString(USER_UNIVERSITY, "");
    }

    public void setUserUniversity(String university) {
        if (user.getUniversity().equals(university)) {
            return;
        }
        user.setUniversity(university);
        preferences.edit().putString(USER_UNIVERSITY, university).apply();
    }

    public User getUser() {
        if (user == null) {
            String userJson = preferences.getString(USER, "");
            user = new Gson().fromJson(userJson, User.class);
        }
        return user;
    }

    public void setUser(User user) {
        if (UserConfig.user == user) {
            return;
        }
        UserConfig.user = user;
        preferences.edit().putString(USER, new Gson().toJson(user)).apply();
    }
}
