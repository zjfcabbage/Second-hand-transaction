package com.zjf.transaction.mine;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseActivity;
import com.zjf.transaction.base.BaseFragment;
import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.main.PublishActivity;
import com.zjf.transaction.user.UserConfig;
import com.zjf.transaction.user.api.impl.UserApiImpl;
import com.zjf.transaction.util.ImageUtil;
import com.zjf.transaction.util.LogUtil;
import com.zjf.transaction.util.ScreenUtil;
import com.zjf.transaction.util.qiniu.QiNiuUtil;
import com.zjf.transaction.widget.CommonDialogBuilder;
import com.zjf.transaction.widget.RoundImageView;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zhengjiafeng on 2019/3/13
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class MineFragment extends BaseFragment {

    private UserConfig userConfig = UserConfig.inst();
    private RoundImageView ivUserPic;

    @Override
    public View onCreateContent(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        view.setPadding(ScreenUtil.dp2px(getActivity(), 25), ScreenUtil.getStatusBarHeight(), ScreenUtil.dp2px(getActivity(), 25), 0);
        ScreenUtil.hideStatusBarLight(getActivity());
        initUserInfoLayout(view);
        initPublishLayout(view);
        initLogout(view);
        return view;
    }

    private void initLogout(View view) {
        ViewGroup logoutLayout = view.findViewById(R.id.layout_logout);
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonDialogBuilder(getActivity())
                        .setTitle("确定注销此账号吗？")
                        .setNegativeText("取消")
                        .setPositive("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                BaseActivity.logout();
                            }
                        })
                .show();
            }
        });
    }

    private void initPublishLayout(View view) {
        ViewGroup publishLayout = view.findViewById(R.id.layout_publish);
        publishLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishActivity.start(getActivity(), PublishActivity.class);
            }
        });
    }

    private void initUserInfoLayout(View view) {
        ViewGroup userInfoLayout = view.findViewById(R.id.layout_user_info);
        ivUserPic = userInfoLayout.findViewById(R.id.iv_user_pic);
        final TextView tvUserName = userInfoLayout.findViewById(R.id.tv_user_name);
        final TextView tvProvinceAndCity = userInfoLayout.findViewById(R.id.tv_province_city);
        final TextView tvUniversity = userInfoLayout.findViewById(R.id.tv_university);

        LogUtil.d(userConfig.getUser().toString());
        ImageUtil.loadImage(ivUserPic, userConfig.getUserPicUrl());
        tvUserName.setText(userConfig.getUserName());
        tvProvinceAndCity.setText(userConfig.getUserProvince() + userConfig.getUserCity());
        tvUniversity.setText(userConfig.getUserUniversity());

        ivUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(MineFragment.this)
                        .openGallery(PictureMimeType.ofImage())
                        .previewImage(true)
                        .enableCrop(true)
                        .compress(true)
                        .circleDimmedLayer(true)
                        .freeStyleCropEnabled(true)
                        .circleDimmedLayer(true)
                        .selectionMode(PictureConfig.SINGLE)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (!selectList.isEmpty()) {
                        LocalMedia localMedia = selectList.get(0);
                        ImageUtil.loadImage(ivUserPic, localMedia.getPath());
                        updateUserPic(localMedia.getPath());
                    }
                    break;
            }
        }
    }

    private void updateUserPic(String path) {
        QiNiuUtil.upLoadImageWithSimpleToken(path, userConfig.getUserId(), new QiNiuUtil.ActionListener() {
            @Override
            public void success(String url) {
                UserApiImpl.updateUserPicUrl(userConfig.getUserId(), url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataResult<String>>() {
                    @Override
                    public void accept(DataResult<String> stringDataResult) throws Exception {
                        if (stringDataResult.code == DataResult.CODE_SUCCESS) {
                            LogUtil.d("update user pic success");
                        } else {
                            LogUtil.e("update user pic failed, msg -> %s", stringDataResult.msg);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("update user pic error, throwable -> %s", throwable.getMessage());
                    }
                });
            }
        });
    }
}
