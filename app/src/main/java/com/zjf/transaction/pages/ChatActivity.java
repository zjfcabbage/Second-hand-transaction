package com.zjf.transaction.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zjf.transaction.R;
import com.zjf.transaction.app.AppConfig;
import com.zjf.transaction.base.BaseActivity;
import com.zjf.transaction.base.BaseConstant;
import com.zjf.transaction.database.entity.Msg;
import com.zjf.transaction.msg.model.MsgItem;
import com.zjf.transaction.user.model.User;
import com.zjf.transaction.util.LogUtil;
import com.zjf.transaction.util.ScreenUtil;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatActivity extends BaseActivity {

    private String userId;
    private String userPicUrl;
    private String userName;
    private String currentMessage;
    private long currentTimestamp = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(KEY_BUNDLE);
        if (bundle != null) {
            userId = bundle.getString(BaseConstant.KEY_USER_Id);
            userName = bundle.getString(BaseConstant.KEY_USER_NAME);
            userPicUrl = bundle.getString(BaseConstant.KEY_USER_PIC_URL);
        }
        initTitle();
        initView();
    }

    private void initView() {
        final SmartRefreshLayout refreshLayout = findViewById(R.id.layout_refresh);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }
        });

        final RecyclerView recyclerView = findViewById(R.id.rv_chat);
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
            ((TextView)titleLayout.findViewById(R.id.tv_common_title)).setText(userName);
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
        AppConfig.getManager().sendBroadcast(intent);
    }
}
