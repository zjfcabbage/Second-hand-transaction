package com.zjf.transaction.msg.model;

import android.support.annotation.DrawableRes;

/**
 * Created by zhengjiafeng on 2019/4/3
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class MsgItem {
    private String userName;
    @DrawableRes private int userPicId;
    private String newMsg;
    private long timestamp;

    public MsgItem() {
    }

    public MsgItem(String userName, @DrawableRes int userPicId, String newMsg, long timestamp) {
        this.userName = userName;
        this.userPicId = userPicId;
        this.newMsg = newMsg;
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserPicId() {
        return userPicId;
    }

    public void setUserPicId(@DrawableRes int userPicId) {
        this.userPicId = userPicId;
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
    public String toString() {
        return "MsgItem{" +
                "userName='" + userName + '\'' +
                ", userPicId=" + userPicId +
                ", newMsg='" + newMsg + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
