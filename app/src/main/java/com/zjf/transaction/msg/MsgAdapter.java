package com.zjf.transaction.msg;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseAdapter;
import com.zjf.transaction.base.BaseConstant;
import com.zjf.transaction.base.BaseViewHolder;
import com.zjf.transaction.database.TransactionDatabase;
import com.zjf.transaction.msg.model.MsgItem;
import com.zjf.transaction.pages.ChatActivity;
import com.zjf.transaction.pages.CommodityActivity;
import com.zjf.transaction.user.model.User;
import com.zjf.transaction.util.ImageUtil;
import com.zjf.transaction.util.LogUtil;
import com.zjf.transaction.util.TimeUtil;
import com.zjf.transaction.widget.CommonDialogBuilder;

/**
 * Created by zhengjiafeng on 2019/4/3
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class MsgAdapter extends BaseAdapter<MsgItem> {
    @NonNull
    @Override
    public BaseViewHolder<MsgItem> onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_msg_item, viewGroup, false);
        return new MsgHolder(view);
    }

    class MsgHolder extends BaseViewHolder<MsgItem> {

        private ImageView ivUserPic;
        private TextView tvUserName, tvNewMsg, tvFinalTime;

        public MsgHolder(View itemView) {
            super(itemView);
            ivUserPic = itemView.findViewById(R.id.iv_user_pic);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvNewMsg = itemView.findViewById(R.id.tv_new_msg);
            tvFinalTime = itemView.findViewById(R.id.tv_final_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //打开聊天界面
                    MsgItem item = getIndexData();
                    final String userId = item.getUserId();
                    final String userName = item.getUserName();
                    final String userPicUrl = item.getUserPicUrl();
                    Bundle bundle = new Bundle();
                    bundle.putString(BaseConstant.KEY_USER_Id, userId);
                    bundle.putString(BaseConstant.KEY_USER_NAME, userName);
                    bundle.putString(BaseConstant.KEY_USER_PIC_URL, userPicUrl);
                    ChatActivity.start(getContext(), bundle, ChatActivity.class);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new CommonDialogBuilder(getContext())
                            .setTitle("确定删除聊天记录吗？")
                            .setNegativeText("取消")
                            .setPositive("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TransactionDatabase.getInstance().getMsgItemDao()
                                            .delete(getIndexData())
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<Integer>() {
                                                @Override
                                                public void accept(Integer integer) throws Exception {
                                                    LogUtil.d("delete msg item success, line index -> %d", integer);
                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Exception {
                                                    LogUtil.e("delete msg item error, throwable -> %s", throwable.getMessage());
                                                }
                                            });
                                    // TODO: 2019/5/8 清空相应用户的聊天记录
                                    getDataList().remove(getIndexData());
                                    notifyDataSetChanged();
                                }
                            }).show();
                    return true;
                }
            });
        }

        @Override
        public void onBind(MsgItem data, int position) {
            if (data != null) {
                ImageUtil.loadImage(ivUserPic, data.getUserPicUrl());
                tvUserName.setText(data.getUserName());
                tvNewMsg.setText(data.getNewMsg());
                tvFinalTime.setText(TimeUtil.formatTime(data.getTimestamp()));
            }
        }
    }
}
