package com.zjf.transaction.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseAdapter;
import com.zjf.transaction.base.BaseFragment;
import com.zjf.transaction.main.model.Commodity;
import com.zjf.transaction.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengjiafeng on 2019/3/13
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class MainFragment extends BaseFragment {

    private List<Commodity> commodityList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BaseAdapter<Commodity> adapter;
    private StaggeredGridLayoutManager manager;

    @Override
    public View onCreateContent(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        init();
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);

        adapter = new MainAdapter();
        adapter.setDataList(commodityList);
        recyclerView.setAdapter(adapter);
        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        DividerDecoration decoration = new DividerDecoration(ScreenUtil.dp2px(getActivity(), 10));
        recyclerView.addItemDecoration(decoration);


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void init() {
        for (int i = 0; i < 10; i++) {
            Commodity commodity = new Commodity();
            commodity.setImage(R.drawable.cat);
            commodity.setMsg("竹鼠一只三块，三只十块，傻逼的快来买");
            commodity.setPrice(1900);
            commodityList.add(commodity);
        }
    }
}
