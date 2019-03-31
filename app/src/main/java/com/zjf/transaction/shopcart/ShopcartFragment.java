package com.zjf.transaction.shopcart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseAdapter;
import com.zjf.transaction.base.BaseFragment;
import com.zjf.transaction.shopcart.model.ShopcartItem;

import java.util.List;

/**
 * Created by zhengjiafeng on 2019/3/13
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class ShopcartFragment extends BaseFragment {

    private List<ShopcartItem> shopcartItemList;
    private BaseAdapter<ShopcartItem> shopcartAdapter;
    private RecyclerView recyclerView;
    private ViewGroup shopcartBottomLayout;
    private CheckBox cbChooseAll;
    private TextView tvAllMoney;
    private TextView tvPay;

    @Override
    public View onCreateContent(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopcart, container, false);
        initData();
        initView(view);
        return view;
    }

    private void initData() {

    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.rv_commodity);

        shopcartBottomLayout = view.findViewById(R.id.layout_shopcart_bottom);
        cbChooseAll = shopcartBottomLayout.findViewById(R.id.cb_choose_all);
        tvAllMoney = shopcartBottomLayout.findViewById(R.id.tv_all_money);
        tvPay = shopcartBottomLayout.findViewById(R.id.tv_pay);

        shopcartAdapter = new ShopcartAdapter();
        shopcartAdapter.setDataList(shopcartItemList);
        recyclerView.setAdapter(shopcartAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.addItemDecoration();
    }
}
