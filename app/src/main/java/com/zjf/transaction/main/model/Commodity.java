package com.zjf.transaction.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhengjiafeng on 2019/3/15
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class Commodity implements Parcelable {
    @SerializedName("id")
    private String id; //商品id应该用用户名和时间戳保证唯一性
    @SerializedName("userId")
    private String userId;
    @SerializedName("name")
    private String name;
    @SerializedName("imageUrl")
    private String imageUrls;  //用@@@来分隔每个url
    @SerializedName("msg")
    private String msg;
    @SerializedName("price")
    private String price;
    @SerializedName("publishTime")
    private long publishTime;

    public Commodity() {
    }

    public Commodity(String id, String userId, String name, String imageUrls, String msg, String price, long publishTime) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.imageUrls = imageUrls;
        this.msg = msg;
        this.price = price;
        this.publishTime = publishTime;
    }

    protected Commodity(Parcel in) {
        id = in.readString();
        userId = in.readString();
        name = in.readString();
        imageUrls = in.readString();
        msg = in.readString();
        price = in.readString();
        publishTime = in.readLong();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", imageUrls=" + imageUrls +
                ", msg='" + msg + '\'' +
                ", price=" + price +
                ", publishTime=" + publishTime +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(imageUrls);
        dest.writeString(msg);
        dest.writeString(price);
        dest.writeLong(publishTime);
    }

    public static final Creator<Commodity> CREATOR = new Creator<Commodity>() {
        @Override
        public Commodity createFromParcel(Parcel in) {
            return new Commodity(in);
        }

        @Override
        public Commodity[] newArray(int size) {
            return new Commodity[size];
        }
    };
}
