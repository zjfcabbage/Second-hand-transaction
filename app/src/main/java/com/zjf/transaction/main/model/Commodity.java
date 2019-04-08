package com.zjf.transaction.main.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

/**
 * Created by zhengjiafeng on 2019/3/15
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class Commodity {
    private int id; //商品id应该用用户名和时间戳保证唯一性
    private int image;
    private String msg;
    private float price;

    public Commodity() {
    }

    public Commodity(int id, int image, String msg, float price) {
        this.id = id;
        this.image = image;
        this.msg = msg;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(@DrawableRes int image) {
        this.image = image;
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
}
