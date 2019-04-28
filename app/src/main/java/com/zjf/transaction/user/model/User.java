package com.zjf.transaction.user.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhengjiafeng on 2019/3/28
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class User implements Parcelable {

    @SerializedName("userId")
    private String userId;
    @SerializedName("userName")
    private String userName;
    @SerializedName("password")
    private String password;
    @SerializedName("userPicUrl")
    private String userPicUrl;
    @SerializedName("province")
    private String province;
    @SerializedName("city")
    private String city;
    @SerializedName("university")
    private String university;

    public User() {
    }

    public User(String userName, String userPic, String province, String city, String university) {
        this.userName = userName;
        this.userPicUrl = userPic;
        this.province = province;
        this.city = city;
        this.university = university;
    }

    public User(String userId, String userName, String password,
                String userPic, String province, String city, String university) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.userPicUrl = userPic;
        this.province = province;
        this.city = city;
        this.university = university;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
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
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", userPicUrl='" + userPicUrl + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", university='" + university + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeString(userPicUrl);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(university);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            User user = new User();
            user.userId = source.readString();
            user.userName = source.readString();
            user.password = source.readString();
            user.userPicUrl = source.readString();
            user.province = source.readString();
            user.city = source.readString();
            user.university = source.readString();
            return user;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
