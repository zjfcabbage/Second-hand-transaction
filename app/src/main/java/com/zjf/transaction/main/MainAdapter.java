package com.zjf.transaction.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseAdapter;
import com.zjf.transaction.base.BaseConstant;
import com.zjf.transaction.base.BaseViewHolder;
import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.main.model.Commodity;
import com.zjf.transaction.pages.commodity.CommodityActivity;
import com.zjf.transaction.user.api.impl.UserApiImpl;
import com.zjf.transaction.user.model.User;
import com.zjf.transaction.util.ImageUtil;
import com.zjf.transaction.util.LogUtil;
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
        private User user;

        @Override
        public void onBind(Commodity data, int position) {
            ImageUtil.loadImage(ivCommodity, getFirstPic(data.getImageUrls()));
            bindUserPic(data.getUserId());
            tvCommodity.setText(data.getMsg());
            tvRMB.setText(PriceUtil.createPrice(data.getPrice()));
        }

        private String getFirstPic(String imageUrls) {
            if (imageUrls == null) {
                return null;
            }
            String[] urls = imageUrls.split("@@@");
            if (urls.length > 0) {
                return urls[0];
            }
            return null;
        }

        private void bindUserPic(String userId) {
            if (userId == null) {
                return;
            }
            UserApiImpl.getUser(userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<DataResult<User>>() {
                        @Override
                        public void accept(DataResult<User> userDataResult) throws Exception {
                            if (userDataResult.code == DataResult.CODE_SUCCESS) {
                                user = userDataResult.data;
                                ImageUtil.loadImage(ivUserPic, userDataResult.data.getUserPicUrl());
                            } else {
                                LogUtil.e("get user pic failed, msg -> %s", userDataResult.msg);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtil.e("get user pic error, throwable -> %s", throwable.getMessage());
                        }
                    });
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCommodity = itemView.findViewById(R.id.iv_commodity);
            msgLayout = itemView.findViewById(R.id.layout_msg);
            ivUserPic = msgLayout.findViewById(R.id.iv_user_pic);
            tvCommodity = msgLayout.findViewById(R.id.tv_commodity);
            tvRMB = msgLayout.findViewById(R.id.tv_RMB);
            ivAddToShopcart = msgLayout.findViewById(R.id.iv_add_shopcart);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    Commodity commodity = getIndexData();
                    if (commodity != null) {
                        bundle.putParcelable(BaseConstant.KEY_COMMODITY_ID, commodity);
                        bundle.putParcelable(BaseConstant.KEY_USER, user);
                    }
                    CommodityActivity.start(getContext(), bundle, CommodityActivity.class);
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
