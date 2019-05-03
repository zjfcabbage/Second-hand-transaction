package com.zjf.transaction.msg;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseAdapter;
import com.zjf.transaction.base.BaseViewHolder;
import com.zjf.transaction.msg.model.MsgItem;
import com.zjf.transaction.util.ImageUtil;
import com.zjf.transaction.util.TimeUtil;

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
                    // TODO: 2019/4/3 跳转到聊天界面
                }
            });
        }

        @Override
        public void onBind(MsgItem data, int position) {
            if (data != null) {
                ImageUtil.loadImage(ivUserPic, data.getUserPic());
                tvUserName.setText(data.getUserName());
                tvNewMsg.setText(data.getNewMsg());
                tvFinalTime.setText(TimeUtil.formatTime(data.getTimestamp()));
            }
        }
    }

}
