package com.zjf.transaction.shopcart.api.impl;

import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.shopcart.api.ShopcartApi;
import com.zjf.transaction.shopcart.model.ShopcartItem;
import com.zjf.transaction.util.HttpFactory;

import java.util.List;

import io.reactivex.Single;

public class ShopcartApiImpl {
    private static ShopcartApi shopcartApi = HttpFactory.getApi(ShopcartApi.class);

    public static Single<DataResult<String>> add(String userId, String commodityId) {
        return shopcartApi.add(userId, commodityId);
    }

    public static Single<DataResult<List<ShopcartItem>>> getShopcartItem(String userId, int pageNum) {
        return shopcartApi.getShopcartItem(userId, pageNum);
    }

    public static Single<DataResult<String>> delete(String userId, String commodityId) {
        return shopcartApi.delete(userId, commodityId);
    }
}
