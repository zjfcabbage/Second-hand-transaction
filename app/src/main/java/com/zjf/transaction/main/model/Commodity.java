package com.zjf.transaction.main.model;

import android.support.annotation.DrawableRes;

/**
 * Created by zhengjiafeng on 2019/3/15
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class Commodity {
    private String id; //商品id应该用用户名和时间戳保证唯一性
    private String imageUrl;
    private String msg;
    private float price;
    private long publishTime;

    public Commodity() {
    }

    public Commodity(String id, String imageUrl, String msg, float price, long publishTime) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.msg = msg;
        this.price = price;
        this.publishTime = publishTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }
}
