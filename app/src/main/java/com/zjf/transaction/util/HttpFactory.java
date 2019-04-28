package com.zjf.transaction.util;

import com.zjf.transaction.app.AppConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zjfcabbage on 2019/2/5
 *
 * @author 糟老头子 zjfcabbage
 */
public class HttpFactory {
    private static Retrofit initRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder().addInterceptor(new UrlLogInterceptor()).build())
                .build();
    }

    public static <T> T getApi(Class<T> clazz) {
        return initRetrofit().create(clazz);
    }

    static class UrlLogInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            LogUtil.d("url -> %s", request.url());
            return chain.proceed(request);
        }
    }
}
