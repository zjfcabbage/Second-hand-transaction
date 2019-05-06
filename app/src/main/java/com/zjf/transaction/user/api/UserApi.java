package com.zjf.transaction.user.api;

import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.user.model.User;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserApi {
    @GET("/user/get")
    Single<DataResult<User>> getUser(@Query("userId") String userId);

    @POST("/user/add")
    Single<DataResult<User>> register(@Body User user);

    @FormUrlEncoded
    @PUT("/user/update/password")
    Single<DataResult<String>> updateUserPassword(@Field("password") String password, @Field("userId") String userId);

    @FormUrlEncoded
    @PUT("/user/update/userName")
    Single<DataResult<String>> updateUserName(@Field("userName") String userName, @Field("userId") String userId);

    @FormUrlEncoded
    @POST("/user/login/")
    Single<DataResult<User>> login(@Field("userName") String userName, @Field("password") String password);

    @GET("/user/exist/")
    Single<DataResult<User>> isUserNameExisted(@Query("userName") String userName);

    @PUT("/user/update/image")
    Single<DataResult<String>> updateUserPicUrl(@Query("userId") String userId, @Query("userPicUrl") String userPicUrl);
}
