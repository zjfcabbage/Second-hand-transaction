package com.zjf.transaction.user.model;

import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("provinceId")
    private int provinceId;
    @SerializedName("cityId")
    private int cityId;
    @SerializedName("cityName")
    private String cityName;

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
