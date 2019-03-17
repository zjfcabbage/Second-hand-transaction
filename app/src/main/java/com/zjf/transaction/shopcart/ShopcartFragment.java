package com.zjf.transaction.shopcart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseFragment;

/**
 * Created by zhengjiafeng on 2019/3/13
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class ShopcartFragment extends BaseFragment {
    @Override
    public View onCreateContent(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopcart, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

    }
}
