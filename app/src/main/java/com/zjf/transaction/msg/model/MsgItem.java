package com.zjf.transaction.msg.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by zhengjiafeng on 2019/4/3
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
@Entity
public class MsgItem implements Parcelable {

    @PrimaryKey
    @NonNull
    private String userId;
    @ColumnInfo(name = "user_name")
    private String userName;
    @ColumnInfo(name = "user_pic_url")
    private String userPicUrl;
    @ColumnInfo(name = "new_msg")
    private String newMsg;
    @ColumnInfo(name = "timestamp")
    private long timestamp;

    public MsgItem() {
    }

    @Ignore
    public MsgItem(String userId, String userName, String userPicUrl, String newMsg, long timestamp) {
        this.userId = userId;
        this.userName = userName;
        this.userPicUrl = userPicUrl;
        this.newMsg = newMsg;
        this.timestamp = timestamp;
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

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }

    public String getNewMsg() {
        return newMsg;
    }

    public void setNewMsg(String newMsg) {
        this.newMsg = newMsg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(userPicUrl);
        dest.writeString(newMsg);
        dest.writeLong(timestamp);
    }

    protected MsgItem(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        userPicUrl = in.readString();
        newMsg = in.readString();
        timestamp = in.readLong();
    }

    public static final Creator<MsgItem> CREATOR = new Creator<MsgItem>() {
        @Override
        public MsgItem createFromParcel(Parcel in) {
            return new MsgItem(in);
        }

        @Override
        public MsgItem[] newArray(int size) {
            return new MsgItem[size];
        }
    };
}
