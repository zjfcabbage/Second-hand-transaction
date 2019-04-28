package com.zjf.transaction.shopcart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zjf.transaction.MainActivity;
import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseAdapter;
import com.zjf.transaction.base.BaseFragment;
import com.zjf.transaction.main.model.Commodity;
import com.zjf.transaction.shopcart.model.ShopcartItem;
import com.zjf.transaction.user.model.User;
import com.zjf.transaction.util.ScreenUtil;

import java.util.ArrayList;
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
    private ViewGroup shopcartBottomLayout, titleLayout;
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
        shopcartItemList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User("傻逼", null, "广东", "深圳", "傻逼大学");
            Commodity commodity = new Commodity("0", null, "竹鼠一只三块，三只十块，傻逼的快来买", 1999, 0);
            shopcartItemList.add(new ShopcartItem(user, commodity));
        }
    }

    private void initView(View view) {
        titleLayout = view.findViewById(R.id.layout_shopcart_title);
        titleLayout.setPadding(0, ScreenUtil.getStatusBarHeight(), 0, 0);
        ((TextView) titleLayout.findViewById(R.id.tv_common_title)).setText(getArguments().getString(MainActivity.KEY_TITLE));


        recyclerView = view.findViewById(R.id.rv_commodity);

        shopcartBottomLayout = view.findViewById(R.id.layout_shopcart_bottom);
        cbChooseAll = shopcartBottomLayout.findViewById(R.id.cb_choose_all);
        tvAllMoney = shopcartBottomLayout.findViewById(R.id.tv_all_money);
        tvPay = shopcartBottomLayout.findViewById(R.id.tv_pay);

        shopcartAdapter = new ShopcartAdapter();
        shopcartAdapter.setDataList(shopcartItemList);
        recyclerView.setAdapter(shopcartAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new ShopcartItemDecoration(10));

        cbChooseAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < shopcartItemList.size(); i++) {
                    shopcartItemList.get(i).setChecked(isChecked);
                }
                shopcartAdapter.setDataList(shopcartItemList);
            }
        });
    }


}
