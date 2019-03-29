package com.zjf.transaction.main;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseAdapter;
import com.zjf.transaction.base.BaseViewHolder;
import com.zjf.transaction.main.model.Commodity;
import com.zjf.transaction.util.ImageLoaderUtil;
import com.zjf.transaction.util.TextUtil;
import com.zjf.transaction.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengjiafeng on 2019/3/15
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class MainAdapter extends BaseAdapter<Commodity> {


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main_page_item, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends BaseViewHolder<Commodity> {

        private ImageView ivCommodity;
        private ViewGroup msgLayout;
        private RoundImageView ivUserPic;
        private TextView tvCommodity, tvRMB;

        @Override
        public void onBind(Commodity data) {
            ImageLoaderUtil.loadImage(ivCommodity, data.getImage());
            tvCommodity.setText(data.getMsg());
            tvRMB.setText(TextUtil.createPrice(data.getPrice()));
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCommodity = itemView.findViewById(R.id.iv_commodity);
            msgLayout = itemView.findViewById(R.id.layout_msg);
            ivUserPic = msgLayout.findViewById(R.id.iv_user_pic);
            tvCommodity = msgLayout.findViewById(R.id.tv_commodity);
            tvRMB = msgLayout.findViewById(R.id.tv_RMB);
            ImageLoaderUtil.loadImage(ivUserPic, R.drawable.cat);
        }
    }
}
