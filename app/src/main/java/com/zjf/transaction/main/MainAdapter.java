package com.zjf.transaction.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseAdapter;
import com.zjf.transaction.base.BaseConstant;
import com.zjf.transaction.base.BaseViewHolder;
import com.zjf.transaction.main.model.Commodity;
import com.zjf.transaction.pages.commodity.CommodityActivity;
import com.zjf.transaction.util.ImageUtil;
import com.zjf.transaction.util.PriceUtil;
import com.zjf.transaction.widget.RoundImageView;

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

        private ImageView ivCommodity, ivAddToShopcart;
        private ViewGroup msgLayout;
        private RoundImageView ivUserPic;
        private TextView tvCommodity, tvRMB;

        @Override
        public void onBind(Commodity data, int position) {
            ImageUtil.loadImage(ivCommodity, data.getImageUrl());
            tvCommodity.setText(data.getMsg());
            tvRMB.setText(PriceUtil.createPrice(data.getPrice()));
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCommodity = itemView.findViewById(R.id.iv_commodity);
            msgLayout = itemView.findViewById(R.id.layout_msg);
            ivUserPic = msgLayout.findViewById(R.id.iv_user_pic);
            tvCommodity = msgLayout.findViewById(R.id.tv_commodity);
            tvRMB = msgLayout.findViewById(R.id.tv_RMB);
            ivAddToShopcart = msgLayout.findViewById(R.id.iv_add_shopcart);
            ImageUtil.loadImage(ivUserPic, R.drawable.cat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    Commodity commodity = getIndexData();
                    if (commodity != null) {
                        bundle.putString(BaseConstant.KEY_COMMODITY_ID, commodity.getId());
                    }
                    CommodityActivity.start(getContext(), bundle,  CommodityActivity.class);
                }
            });

            ivAddToShopcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2019/4/2 加入购物车
                }
            });
        }
    }
}
