package com.zjf.transaction.util.qiniu;

import android.net.Uri;
import android.os.Build;
import android.util.Pair;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.zjf.transaction.app.AppConfig;
import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.image.api.ImageApi;
import com.zjf.transaction.util.HttpFactory;
import com.zjf.transaction.util.LogUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringJoiner;

import androidx.annotation.RequiresApi;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class QiNiuUtil {

    public interface ActionListener {
        void success(String url);
    }

    public static void upLoadImageWithSimpleToken(final String filePath, final String key, final ActionListener actionListener) {
        HttpFactory.getApi(ImageApi.class)
                .getSimpleToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataResult<String>>() {
                    @Override
                    public void accept(DataResult<String> stringDataResult) throws Exception {
                        if (stringDataResult.code == DataResult.CODE_SUCCESS) {
                            LogUtil.d("get simple token success");
                            String fileName = key + System.currentTimeMillis();
                            AppConfig.getUploadManager().put(filePath, fileName, stringDataResult.data, new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject response) {
                                    Uri uri = new Uri.Builder()
                                            .scheme(QiNiuConfig.HTTP_SCHEME)
                                            .authority(QiNiuConfig.BASE_AUTHORITY)
                                            .path(key)
                                            .build();
                                    LogUtil.d("qiniu upload url -> %s", uri.toString());
                                    actionListener.success(uri.toString());
                                }
                            }, null);
                        } else {
                            LogUtil.e("get simple token failed, msg -> %s", stringDataResult.msg);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("get simple token failed, throwable -> %s", throwable.getMessage());
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void uploadImageList(final ArrayList<Pair<String, String>> picList, final ActionListener actionListener) {
        if (picList.isEmpty()) {
            return;
        }
        final int[] count = {0};
        final StringJoiner joiner = new StringJoiner("@@@");
        for (int i = 0; i < picList.size(); i++) {
            Pair<String, String> pair = picList.get(i);
            final String key = pair.first;
            final String filePath = pair.second;
            HttpFactory.getApi(ImageApi.class)
                    .getSimpleToken()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<DataResult<String>>() {
                        @Override
                        public void accept(final DataResult<String> stringDataResult) throws Exception {
                            if (stringDataResult.code == DataResult.CODE_SUCCESS) {
                                AppConfig.getUploadManager()
                                        .put(filePath, key, stringDataResult.data, new UpCompletionHandler() {
                                            @Override
                                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                                Uri uri = new Uri.Builder()
                                                        .scheme(QiNiuConfig.HTTP_SCHEME)
                                                        .authority(QiNiuConfig.BASE_AUTHORITY)
                                                        .path(key)
                                                        .build();
                                                synchronized (QiNiuUtil.class) {
                                                    joiner.add(uri.toString());
                                                    count[0]++;
                                                    if (count[0] == picList.size()) {
                                                        actionListener.success(joiner.toString());
                                                    }
                                                }
                                            }
                                        }, null);

                            } else {
                                LogUtil.e("upload pic error, msg -> %s", stringDataResult.msg);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtil.e("upload pic error, throwable -> %s", throwable.getMessage());
                        }
                    });
        }
    }
}
