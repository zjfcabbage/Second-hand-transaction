package com.zjf.transaction.pages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseAdapter;
import com.zjf.transaction.base.BaseViewHolder;
import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.database.entity.Msg;
import com.zjf.transaction.user.UserConfig;
import com.zjf.transaction.user.api.impl.UserApiImpl;
import com.zjf.transaction.user.model.User;
import com.zjf.transaction.util.ImageUtil;
import com.zjf.transaction.widget.RoundImageView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChatAdapter extends BaseAdapter<Msg> {

    private static int TYPE_SEND = 0;
    private static int TYPE_RECEIVE = 1;
    private User fromUser;
    private String userPicUrl;


    @NonNull
    @Override
    public BaseViewHolder<Msg> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_RECEIVE) {
            return new ReceiveViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_msg_left, parent, false));
        } else {
            return new SendViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_msg_right, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Msg msg = getDataList().get(position);
        String userId = UserConfig.inst().getUserId();
        if (userId.equals(msg.getFromId())) {
            //自己发送的消息
            return TYPE_SEND;
        } else {
            return TYPE_RECEIVE;
        }
    }

    class SendViewHolder extends BaseViewHolder<Msg> {
        private RoundImageView ivUser;
        private TextView tvMsg;

        @Override
        public void onBind(Msg data, int position) {
            ImageUtil.loadImage(ivUser, UserConfig.inst().getUserPicUrl());
            tvMsg.setText(data.getMessage());
        }

        public SendViewHolder(View itemView) {
            super(itemView);
            ivUser = itemView.findViewById(R.id.iv_user_pic);
            tvMsg = itemView.findViewById(R.id.tv_msg);
        }

    }

    class ReceiveViewHolder extends BaseViewHolder<Msg> {

        private RoundImageView ivUser;
        private TextView tvMsg;

        @Override
        public void onBind(Msg data, int position) {
            ImageUtil.loadImage(ivUser, userPicUrl);
            tvMsg.setText(data.getMessage());
        }

        public ReceiveViewHolder(View itemView) {
            super(itemView);
            ivUser = itemView.findViewById(R.id.iv_user_pic);
            tvMsg = itemView.findViewById(R.id.tv_msg);
        }

    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }

}
