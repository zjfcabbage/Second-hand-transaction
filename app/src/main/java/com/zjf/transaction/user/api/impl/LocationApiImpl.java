package com.zjf.transaction.user.api.impl;

import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.user.api.LocationApi;
import com.zjf.transaction.user.model.City;
import com.zjf.transaction.user.model.Province;
import com.zjf.transaction.user.model.University;
import com.zjf.transaction.util.HttpFactory;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class LocationApiImpl{

    private static LocationApi locationApi = HttpFactory.getApi(LocationApi.class);

    public static Single<DataResult<List<Province>>> getProvince() {
        return locationApi.getProvince();
    }

    public static Observable<DataResult<List<City>>> getCityByProvinceId(int provinceId) {
        return locationApi.getCityByProvinceId(provinceId);
    }

    public static Single<DataResult<List<University>>> getUniversityByCityId(int cityId) {
        return locationApi.getUniversityByCityId(cityId);
    }
}
