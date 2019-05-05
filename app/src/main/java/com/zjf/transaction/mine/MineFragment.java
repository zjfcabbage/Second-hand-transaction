package com.zjf.transaction.mine;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseFragment;
import com.zjf.transaction.user.UserConfig;
import com.zjf.transaction.util.ImageUtil;
import com.zjf.transaction.widget.RoundImageView;

/**
 * Created by zhengjiafeng on 2019/3/13
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class MineFragment extends BaseFragment {

    private ViewGroup userInfoLayout, publishLayout;
    private UserConfig userConfig = UserConfig.inst();

    @Override
    public View onCreateContent(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initUserInfoLayout(view);
        initPublishLayout(view);
        return view;
    }

    private void initPublishLayout(View view) {
        publishLayout = view.findViewById(R.id.layout_publish);
        publishLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2019/4/5 跳转到发布界面
            }
        });
    }

    private void initUserInfoLayout(View view) {
        userInfoLayout = view.findViewById(R.id.layout_user_info);
        final RoundImageView ivUserPic = userInfoLayout.findViewById(R.id.iv_user_pic);
        final TextView tvUserName = userInfoLayout.findViewById(R.id.tv_user_name);
        final TextView tvProvinceAndCity = userInfoLayout.findViewById(R.id.tv_province_city);
        final TextView tvUniversity = userInfoLayout.findViewById(R.id.tv_university);

        ImageUtil.loadImage(ivUserPic, userConfig.getUserPicUrl());
        tvUserName.setText(userConfig.getUserName());
        tvProvinceAndCity.setText(userConfig.getUserProvince() + userConfig.getUserCity());
        tvUniversity.setText(userConfig.getUserUniversity());
    }
}
