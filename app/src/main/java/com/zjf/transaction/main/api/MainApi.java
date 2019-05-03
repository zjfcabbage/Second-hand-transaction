package com.zjf.transaction.main.api;

import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.main.model.Commodity;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface MainApi {
    @GET("/main/getAll")
    Single<DataResult<List<Commodity>>> getAllCommodity(@Query("pageNum") int pageNum);

    @POST("/main/publish")
    Single<DataResult<String>> publish(@Body Commodity commodity);

    @Multipart
    @POST("/main/upload")
    Single<DataResult<String>> upload(@Query("id") String id, @Part List<MultipartBody.Part> pics);
}
