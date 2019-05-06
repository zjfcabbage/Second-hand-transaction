package com.zjf.transaction.pages.commodity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseActivity;
import com.zjf.transaction.base.BaseConstant;
import com.zjf.transaction.main.model.Commodity;
import com.zjf.transaction.user.model.User;
import com.zjf.transaction.util.ImageUtil;
import com.zjf.transaction.util.PriceUtil;
import com.zjf.transaction.util.ScreenUtil;
import com.zjf.transaction.util.TimeUtil;
import com.zjf.transaction.widget.RoundImageView;

import androidx.core.widget.NestedScrollView;

public class CommodityActivity extends BaseActivity {

    private Commodity commodity;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(KEY_BUNDLE);
        if (bundle != null) {
            commodity = bundle.getParcelable(BaseConstant.KEY_COMMODITY_ID);
            user = bundle.getParcelable(BaseConstant.KEY_USER);
        }
        ScreenUtil.hideStatusBar(this);
        initTitleLayout();
        initCommodityInfoLayout();
        initTopBarLayout();
        initBottomBarLayout();
    }

    private void initCommodityInfoLayout() {
        ViewGroup commodityInfoLayout = findViewById(R.id.layout_commodity_info);
        final TextView tvCommodityPrice = commodityInfoLayout.findViewById(R.id.tv_commodity_price);
        final TextView tvCommodityMsg = commodityInfoLayout.findViewById(R.id.tv_commodity_msg);
        if (commodity != null) {
            tvCommodityPrice.setText(PriceUtil.createPrice(commodity.getPrice()));
            tvCommodityMsg.setText(commodity.getMsg());
        }
        addCommodityPicture(commodityInfoLayout);
    }

    /**
     * 创建展示商品图片的imageView然后添加到ViewGround
     *
     */
    private void addCommodityPicture(ViewGroup commodityInfoLayout) {
        String[] imageUrls = null;
        if (commodity != null) {
            imageUrls = commodity.getImageUrls().split("@@@");
        }
        if (imageUrls == null) {
            return;
        }
        for (String imageUrl : imageUrls) {
            final ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.dp2px(this, 300)));
            imageView.setPadding(0, 0, 0, ScreenUtil.dp2px(this, 12));
            ImageUtil.loadImage(imageView, imageUrl);
            commodityInfoLayout.addView(imageView);
        }
    }

    private void initBottomBarLayout() {
        ViewGroup bottomBarLayout = findViewById(R.id.layout_bottom_bar);
        final TextView tvAddShopcart = bottomBarLayout.findViewById(R.id.tv_add_shopcart);
        tvAddShopcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2019/4/5 将商品添加进购物车
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void initTopBarLayout() {
        ViewGroup topBarLayout = findViewById(R.id.layout_top_bar);
        final RoundImageView ivUserPic = topBarLayout.findViewById(R.id.iv_user_pic);
        final TextView tvUserName = topBarLayout.findViewById(R.id.tv_user_name);
        final TextView tvPublishTime = topBarLayout.findViewById(R.id.tv_publish_time);

        if (commodity != null) {
            tvPublishTime.setText(TimeUtil.formatTime(commodity.getPublishTime()));
        }
        if (user != null) {
            ImageUtil.loadImage(ivUserPic, user.getUserPicUrl());
            tvUserName.setText(user.getUserName());
        }

        topBarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2019/4/5 跳转用户信息
            }
        });
    }

    private void initTitleLayout() {
        ViewGroup titleLayout = findViewById(R.id.commodity_info_title);
        titleLayout.setPadding(0, ScreenUtil.getStatusBarHeight(), 0, 0);
        final ImageView ivBack = titleLayout.findViewById(R.id.iv_common_back);
        ivBack.setVisibility(View.VISIBLE);
        TextView title = titleLayout.findViewById(R.id.tv_common_title);
        if (commodity != null) {
            title.setText(commodity.getName());
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommodityActivity.this.finish();
            }
        });
    }
}
