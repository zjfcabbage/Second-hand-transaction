package com.zjf.transaction.user.model;

import com.google.gson.annotations.SerializedName;

public class University {
    @SerializedName("universityId")
    private int universityId;
    @SerializedName("cityId")
    private int cityId;
    @SerializedName("provinceId")
    private int provinceId;
    @SerializedName("universityName")
    private String universityName;
    @SerializedName("cityName")
    private String cityName;

    public int getUniversityId() {
        return universityId;
    }

    public void setUniversityId(int universityId) {
        this.universityId = universityId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
