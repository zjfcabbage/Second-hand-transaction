package com.zjf.transaction.user.model;

import com.google.gson.annotations.SerializedName;

public class Province {
    @SerializedName("provinceId")
    private int provinceId;

    @SerializedName("provinceName")
    private String provinceName;

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
