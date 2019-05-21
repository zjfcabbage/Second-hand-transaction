package com.zjf.transaction.main.api;

import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.main.model.Commodity;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MainApi {
    @GET("/main/getAll")
    Single<DataResult<List<Commodity>>> getAllCommodity(@Query("pageNum") int pageNum);

    @POST("/main/publish")
    Single<DataResult<String>> publish(@Body Commodity commodity);

    @GET("/main/getByName")
    Single<DataResult<List<Commodity>>> getByName(@Query("name") String name, @Query("pageNum") int pageNum);

    @HTTP(method = "DELETE", path = "/main/delete", hasBody = true)
    Single<DataResult<String>> delete(@Body List<String> list);

    @POST("/main/mark")
    Single<DataResult<String>> markCommodityIsSold(@Body List<String> list);
}
