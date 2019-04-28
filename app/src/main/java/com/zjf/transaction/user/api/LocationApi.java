package com.zjf.transaction.user.api;

import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.user.model.City;
import com.zjf.transaction.user.model.Province;
import com.zjf.transaction.user.model.University;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LocationApi {

    @GET("/location/province")
    Single<DataResult<List<Province>>> getProvince();

    @GET("location/city")
    Observable<DataResult<List<City>>> getCityByProvinceId(@Query("provinceId") int provinceId);

    @GET("location/university")
    Single<DataResult<List<University>>> getUniversityByCityId(@Query("cityId") int cityId);
}
