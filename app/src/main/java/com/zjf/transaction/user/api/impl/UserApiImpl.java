package com.zjf.transaction.user.api.impl;

import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.user.api.UserApi;
import com.zjf.transaction.user.model.User;
import com.zjf.transaction.util.HttpFactory;

import io.reactivex.Single;
import okhttp3.MultipartBody;

public class UserApiImpl {
    private static UserApi userApi = HttpFactory.getApi(UserApi.class);

    public static Single<DataResult<User>> getUser(String userId) {
        return userApi.getUser(userId);
    }

    public static Single<DataResult<User>> register(User user) {
        return userApi.register(user);
    }

    public static Single<DataResult<String>> updateUserPassword(String password, String userId) {
        return userApi.updateUserPassword(password, userId);
    }

    public static Single<DataResult<String>> updateUserName(String userName, String userId) {
        return userApi.updateUserName(userName, userId);
    }

    public static Single<DataResult<String>> uploadUserPic(MultipartBody.Part file) {
        return userApi.uploadUserPic(file);
    }

    public static Single<DataResult<User>> login(String userName, String password) {
        return userApi.login(userName, password);
    }

    public static Single<DataResult<String>> isUserNameExisted(String userName) {
        return userApi.isUserNameExisted(userName);
    }
}
