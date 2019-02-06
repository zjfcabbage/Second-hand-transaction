package com.zjf.transaction.util;

import com.zjf.transaction.app.AppConfig;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zjfcabbage on 2019/2/5
 *
 * @author 糟老头子 zjfcabbage
 */
public class HttpFactory<T> {
    private static Retrofit initRestrofit() {
        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public T getApi(Class<T> clazz) {
        return initRestrofit().create(clazz);
    }

}
