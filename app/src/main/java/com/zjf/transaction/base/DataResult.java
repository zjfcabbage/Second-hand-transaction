package com.zjf.transaction.base;

import com.google.gson.annotations.SerializedName;

public class DataResult<T> {
    public static final int CODE_SUCCESS = 1;
    public static final int CODE_ERROR = 0;

    @SerializedName("code")
    public int code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public T data;

}
