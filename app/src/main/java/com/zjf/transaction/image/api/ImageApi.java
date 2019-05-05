package com.zjf.transaction.image.api;

import com.zjf.transaction.base.DataResult;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ImageApi {

    @GET("/image/simpleToken")
    Single<DataResult<String>> getSimpleToken();

    @GET("/image/coverToken")
    Single<DataResult<String>> getCoverToken(@Query("fileName") String fileName);
}
