package com.zjf.transaction.pages.api;

import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.pages.model.OrderInfo;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderApi {

    @POST("/order/add")
    Single<DataResult<String>> addOrder(@Body OrderInfo order);

    @DELETE("/order/delete")
    Single<DataResult<String>> deleteOrder(@Query("orderId") String orderId, @Query("userId")String userId);

    @GET("/order/get")
    Single<DataResult<List<OrderInfo>>> getOrder(@Query("userId") String userId, @Query("pageNum") int pageNum);
}
