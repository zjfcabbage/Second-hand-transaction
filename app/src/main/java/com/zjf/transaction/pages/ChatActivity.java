package com.zjf.transaction.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zjf.transaction.R;
import com.zjf.transaction.app.AppConfig;
import com.zjf.transaction.base.BaseActivity;
import com.zjf.transaction.base.BaseConstant;
import com.zjf.transaction.database.TransactionDatabase;
import com.zjf.transaction.database.entity.Msg;
import com.zjf.transaction.msg.model.MsgItem;
import com.zjf.transaction.pages.websocket.ChatWebSocketListener;
import com.zjf.transaction.pages.websocket.WebSocketConnectUtil;
import com.zjf.transaction.user.UserConfig;
import com.zjf.transaction.util.LogUtil;
import com.zjf.transaction.util.ScreenUtil;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;
import okhttp3.WebSocket;

public class ChatActivity extends BaseActivity {

    private String userId;
    private String userPicUrl;
    private String userName;
    private String currentMessage;
    private long currentTimestamp = System.currentTimeMillis();
    private ChatAdapter adapter;
    private WebSocket webSocket;
    private RecyclerView recyclerView;
    private ChatWebSocketListener.OnWebSocketListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        webSocket = WebSocketConnectUtil.getInstance().getWebSocket();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(KEY_BUNDLE);
        if (bundle != null) {
            userId = bundle.getString(BaseConstant.KEY_USER_Id);
            userName = bundle.getString(BaseConstant.KEY_USER_NAME);
            userPicUrl = bundle.getString(BaseConstant.KEY_USER_PIC_URL);
            currentMessage = bundle.getString(BaseConstant.KEY_NEW_MSG);
        }
        initTitle();
        initView();
        initBottomLayout();

        addOnWebSocketListener();
    }

    private void addOnWebSocketListener() {
        listener = new ChatWebSocketListener.OnWebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
            }

            @Override
            public void onMessage(final String text) {
                LogUtil.d("get data");
                final Msg msg = new Gson().fromJson(text, Msg.class);
                currentMessage = msg.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.appendData(adapter.getDataList().size(), msg);
                        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    }
                });
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
            }
        };
        WebSocketConnectUtil.getInstance().addOnWebSocketListener(listener);
    }

    private void initBottomLayout() {
        final ViewGroup bottomLayout = findViewById(R.id.layout_bottom);
        final EditText etMsg = bottomLayout.findViewById(R.id.et_msg);
        final Button btnSend = bottomLayout.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMsg.getText().toString().isEmpty()) {
                    Toast.makeText(ChatActivity.this, "不能发送空白文本", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentMessage = etMsg.getText().toString();
                final long currentTimestamp = System.currentTimeMillis();
                Msg msg = new Msg();
                msg.setFromId(UserConfig.inst().getUserId());
                msg.setToId(userId);
                msg.setMessage(currentMessage);
                msg.setTimestamp(currentTimestamp);
                adapter.appendData(adapter.getDataList().size(), msg);
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                insertMsg(msg);
                if (webSocket != null) {
                    String msgJson = new Gson().toJson(msg);
                    webSocket.send(msgJson);
                    LogUtil.d("webSocket send msg json -> %s", msgJson);
                }
                etMsg.setText("");
            }
        });
    }

    private void insertMsg(Msg msg) {
        TransactionDatabase.getInstance().getMsgDao()
                .insert(msg)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<List<Long>>() {
                    @Override
                    public void accept(List<Long> line) throws Exception {
                        LogUtil.d("insert msg into database success");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("insert msg into database error, throwable -> %s", throwable.getMessage());
                    }
                });
    }

    private void initView() {
        recyclerView = findViewById(R.id.rv_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter();
        adapter.setUserPicUrl(userPicUrl);
        initData();
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(adapter.getDataList().size() - 1);
    }

    private void initData() {
        TransactionDatabase.getInstance().getMsgDao()
                .getAll(userId, UserConfig.inst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Msg>>() {
                    @Override
                    public void accept(List<Msg> msgs) throws Exception {
                        if (msgs.isEmpty()) {
                            LogUtil.d("init chat history failed, list is empty or null");
                        } else {
                            LogUtil.d("init chat history success");
                            adapter.setDataList(msgs);
                            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("init chat history error, throwable -> %s", throwable.getMessage());
                    }
                });
    }

    private void initTitle() {
        final ViewGroup titleLayout = findViewById(R.id.layout_title);
        ScreenUtil.setStatus(this);
        ImageView ivBack = titleLayout.findViewById(R.id.iv_common_back);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                notifyAddChatListItem();
            }
        });
        if (userName != null) {
            ((TextView) titleLayout.findViewById(R.id.tv_common_title)).setText(userName);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        notifyAddChatListItem();
    }

    //在聊天记录列表中增加一条item
    private void notifyAddChatListItem() {
        Intent intent = new Intent(BaseConstant.ACTION_ADD_CHAT_LIST_ITEM);
        Bundle bundle = new Bundle();
        MsgItem item = new MsgItem();
        item.setUserId(userId);
        item.setUserName(userName);
        item.setUserPicUrl(userPicUrl);
        item.setNewMsg(currentMessage);
        item.setTimestamp(currentTimestamp);
        bundle.putParcelable(BaseConstant.KEY_MSG_ITEM, item);
        intent.putExtra(BaseConstant.BUNDLE_MSG_ITEM, bundle);
        AppConfig.getLocalBroadcastManager().sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketConnectUtil.getInstance().removeOnWebSocketListener(listener);
    }
}
