package com.zjf.transaction.main.api.impl;

import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.main.api.MainApi;
import com.zjf.transaction.main.model.Commodity;
import com.zjf.transaction.util.HttpFactory;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;

public class MainApiImpl {
    private static MainApi mainApi = HttpFactory.getApi(MainApi.class);

    public static Single<DataResult<List<Commodity>>> getAllCommodity(int pageNum) {
        return mainApi.getAllCommodity(pageNum);
    }

    public static Single<DataResult<String>> publish(Commodity commodity) {
        return mainApi.publish(commodity);
    }

    public static Single<DataResult<String>> upload(String id, List<MultipartBody.Part> pics) {
        return mainApi.upload(id, pics);
    }
}
