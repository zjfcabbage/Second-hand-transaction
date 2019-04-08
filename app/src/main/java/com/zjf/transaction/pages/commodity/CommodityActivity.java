package com.zjf.transaction.pages.commodity;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseActivity;
import com.zjf.transaction.util.ImageLoaderUtil;
import com.zjf.transaction.util.ScreenUtil;
import com.zjf.transaction.widget.RoundImageView;

public class CommodityActivity extends BaseActivity {

    private ViewGroup titleLayout, topBarLayout, bottomBarLayout, commodityInfoLayout;
    private NestedScrollView scrollView;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity);
        ScreenUtil.hideStatusBar(this);
        initTitleLayout();
        initScrollView();
        initCommodityInfoLayout();
        initTopBarLayout();
        initBottomBarLayout();
    }

    private void initCommodityInfoLayout() {
        commodityInfoLayout = findViewById(R.id.layout_commodity_info);
        final TextView tvCommodityPrice = commodityInfoLayout.findViewById(R.id.tv_commodity_price);
        final TextView tvCommodityMsg = commodityInfoLayout.findViewById(R.id.tv_commodity_msg);
        // TODO: 2019/4/5 设置商品价格和描述信息
        addCommodityPicture(commodityInfoLayout);
    }

    /**
     * 创建展示商品图片的imageView然后添加到ViewGround
     *
     * @param commodityInfoLayout
     */
    private void addCommodityPicture(ViewGroup commodityInfoLayout) {
        // TODO: 2019/4/5 根据服务器返回的图片数组动态添加图片进布局
        final ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.dp2px(this, 300)));
        imageView.setPadding(0, 0, 0, ScreenUtil.dp2px(this, 12));
        // TODO: 2019/4/5 根据Uri设置imageView
        ImageLoaderUtil.loadImage(imageView, "");
        commodityInfoLayout.addView(imageView);
    }

    private void initBottomBarLayout() {
        bottomBarLayout = findViewById(R.id.layout_bottom_bar);
        final TextView tvAddShopcart = bottomBarLayout.findViewById(R.id.tv_add_shopcart);
        tvAddShopcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2019/4/5 将商品添加进购物车
            }
        });
    }

    private void initScrollView() {
        scrollView = findViewById(R.id.sv_commodity_info);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int x, int y, int oldX, int oldY) {
                if (y < 0) {
                    // TODO: 2019/4/5 当scrollView开始向上滑动的时候，设置title为用户名
                    title.setText(null);
                }
            }
        });
    }

    private void initTopBarLayout() {
        final ViewGroup commodityInfoLayout = scrollView.findViewById(R.id.layout_commodity_info);
        topBarLayout = commodityInfoLayout.findViewById(R.id.layout_top_bar);
        final RoundImageView ivUserPic = topBarLayout.findViewById(R.id.iv_user_pic);
        final TextView tvUserName = topBarLayout.findViewById(R.id.tv_user_name);
        final TextView tvPublishTime = topBarLayout.findViewById(R.id.tv_publish_time);
        // TODO: 2019/4/5 更新用户信息

        topBarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2019/4/5 跳转用户信息
            }
        });
    }

    private void initTitleLayout() {
        titleLayout = findViewById(R.id.commodity_info_title);
        titleLayout.setPadding(0, ScreenUtil.getStatusBarHeight(), 0, 0);
        final ImageView ivBack = titleLayout.findViewById(R.id.iv_common_back);
        title = titleLayout.findViewById(R.id.tv_common_title);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommodityActivity.this.finish();
            }
        });
    }
}
