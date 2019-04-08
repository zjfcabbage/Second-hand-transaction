package com.zjf.transaction.user.model;

import android.support.annotation.DrawableRes;

/**
 * Created by zhengjiafeng on 2019/3/28
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class UserInfo {

    private String userName;
    private String password;
    @DrawableRes
    private int userPic;
    private String province;
    private String city;
    private String university;

    public UserInfo(String userName, @DrawableRes int userPic, String province, String city, String university) {
        this.userName = userName;
        this.userPic = userPic;
        this.province = province;
        this.city = city;
        this.university = university;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserPic() {
        return userPic;
    }

    public void setUserPic(@DrawableRes int userPic) {
        this.userPic = userPic;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userName='" + userName + '\'' +
                ", userPic='" + userPic + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", university='" + university + '\'' +
                '}';
    }
}
