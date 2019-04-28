package com.zjf.transaction.msg.model;

/**
 * Created by zhengjiafeng on 2019/4/3
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class MsgItem {
    private String userName;
    private String userPic;
    private String newMsg;
    private long timestamp;

    public MsgItem() {
    }

    public MsgItem(String userName, String userPicId, String newMsg, long timestamp) {
        this.userName = userName;
        this.userPic = userPicId;
        this.newMsg = newMsg;
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
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
                ", userPic=" + userPic +
                ", newMsg='" + newMsg + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
