package com.zjf.transaction.pages.api.impl;

import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.pages.api.OrderApi;
import com.zjf.transaction.pages.model.OrderInfo;
import com.zjf.transaction.util.HttpFactory;

import java.util.List;

import io.reactivex.Single;

public class OrderApiImpl {

    private static OrderApi orderApi = HttpFactory.getApi(OrderApi.class);

    public static Single<DataResult<String>> addOrder(OrderInfo order) {
        return orderApi.addOrder(order);
    }

    public static Single<DataResult<String>> deleteOrder(String orderId, String userId) {
        return orderApi.deleteOrder(orderId, userId);
    }

    public static Single<DataResult<List<OrderInfo>>> getOrder(String userId, int pageNum) {
        return orderApi.getOrder(userId, pageNum);
    }
}
