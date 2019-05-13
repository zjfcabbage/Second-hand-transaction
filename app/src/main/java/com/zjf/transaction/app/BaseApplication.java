package com.zjf.transaction.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import com.google.gson.Gson;
import com.scwang.smartrefresh.header.PhoenixHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.zjf.transaction.base.BaseConstant;
import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.database.TransactionDatabase;
import com.zjf.transaction.database.entity.Msg;
import com.zjf.transaction.msg.model.MsgItem;
import com.zjf.transaction.pages.websocket.ChatWebSocketListener;
import com.zjf.transaction.pages.websocket.WebSocketConnectUtil;
import com.zjf.transaction.user.api.impl.UserApiImpl;
import com.zjf.transaction.user.model.User;
import com.zjf.transaction.util.LogUtil;

import org.litepal.LitePal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;
import okhttp3.WebSocket;

/**
 * Created by zjfcabbage on 2019/2/6
 *
 * @author 糟老头子 zjfcabbage
 */
public class BaseApplication extends Application {
    //用于保存每个userId对应的userPicUrl和userName
    private Map<String, Pair<String, String>> userMap = new HashMap<>();

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new PhoenixHeader(context);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        AppConfig.initApplication(this);

        WebSocketConnectUtil.getInstance().connectWebSocket();
        WebSocketConnectUtil.getInstance().addOnWebSocketListener(new ChatWebSocketListener.OnWebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                WebSocketConnectUtil.getInstance().setWebSocket(webSocket); //保存webSocket
            }

            @Override
            public void onMessage(final String text) {
                Msg[] msg;
                if (text.contains("[")) {
                    //说明是json数组
                    msg = new Gson().fromJson(text, Msg[].class);
                    notifyMsgItemUpdate(msg[msg.length - 1]);
                } else {
                    msg = new Msg[1];
                    msg[0] = new Gson().fromJson(text, Msg.class);
                    if (userMap.containsKey(msg[0].getFromId())) {
                        //判断此用户的信息是否已缓存
                        Pair<String, String> userPair = userMap.get(msg[0].getFromId());
                        MsgItem item = new MsgItem();
                        item.setUserName(userPair.first);
                        item.setUserPicUrl(userPair.second);
                        item.setUserId(msg[0].getFromId());
                        item.setTimestamp(msg[0].getTimestamp());
                        item.setNewMsg(msg[0].getMessage());
                        sendBroadcast(item);
                    } else {
                        getUserInfoAndSendBroadcast(msg[0]);
                    }
                }

                TransactionDatabase.getInstance().getMsgDao()
                        .insert(msg)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Long>>() {
                            @Override
                            public void accept(List<Long> aLong) throws Exception {
                                LogUtil.d("update chat history from internet success");
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogUtil.e("update chat history from internet error, throwable -> %s", throwable.getMessage());
                            }
                        });
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
            }
        });
    }

    private void sendBroadcast(MsgItem item) {
        Intent intent = new Intent(BaseConstant.ACTION_ADD_CHAT_LIST_ITEM);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BaseConstant.KEY_MSG_ITEM, item);
        intent.putExtra(BaseConstant.BUNDLE_MSG_ITEM, bundle);
        AppConfig.getLocalBroadcastManager().sendBroadcast(intent);
    }

    private void getUserInfoAndSendBroadcast(final Msg msg) {
        UserApiImpl.getUser(msg.getFromId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataResult<User>>() {
                    @Override
                    public void accept(DataResult<User> userDataResult) throws Exception {
                        if (userDataResult.code == DataResult.CODE_SUCCESS) {
                            final User user = userDataResult.data;
                            userMap.put(msg.getFromId(), new Pair<>(user.getUserName(), user.getUserPicUrl()));
                            MsgItem item = new MsgItem();
                            item.setUserId(msg.getFromId());
                            item.setUserName(user.getUserName());
                            item.setUserPicUrl(user.getUserPicUrl());
                            item.setNewMsg(msg.getMessage());
                            item.setTimestamp(msg.getTimestamp());
                            sendBroadcast(item);
                        }
                    }
                });
    }


    //用于更新应用没启动时，用户发送过来的消息，因为应用刚启动时，msgFragment没初始化，无法接收广播，所以直接存进数据库
    private void notifyMsgItemUpdate(final Msg msg) {
        final String fromId = msg.getFromId();
        UserApiImpl.getUser(fromId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataResult<User>>() {
                    @Override
                    public void accept(DataResult<User> userDataResult) throws Exception {
                        if (userDataResult.code == DataResult.CODE_SUCCESS) {
                            final User user = userDataResult.data;
                            MsgItem item = new MsgItem();
                            item.setUserId(user.getUserId());
                            item.setUserName(user.getUserName());
                            item.setUserPicUrl(user.getUserPicUrl());
                            item.setNewMsg(msg.getMessage());
                            item.setTimestamp(msg.getTimestamp());
                            TransactionDatabase.getInstance().getMsgItemDao()
                                    .insert(item)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.io())
                                    .subscribe(new Consumer<Long>() {
                                        @Override
                                        public void accept(Long aLong) throws Exception {
                                            LogUtil.d("update msg item fragment in application success");
                                        }
                                    }, new Consumer<Throwable>() {
                                        @Override
                                        public void accept(Throwable throwable) throws Exception {
                                            LogUtil.d("update msg item fragment in application error, throwable -> %s", throwable.getMessage());
                                        }
                                    });
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }
}
