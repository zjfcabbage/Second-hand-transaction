package com.zjf.transaction.msg;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseAdapter;
import com.zjf.transaction.base.BaseFragment;
import com.zjf.transaction.msg.model.MsgItem;
import com.zjf.transaction.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengjiafeng on 2019/3/13
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class MsgFragment extends BaseFragment {
    private List<MsgItem> msgItemList;
    private RecyclerView recyclerView;
    private BaseAdapter<MsgItem> msgAdapter;

    @Override
    public View onCreateContent(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        initData();
        initView(view);
        return view;
    }

    private void initData() {
        msgItemList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            MsgItem item = new MsgItem("傻逼", null, "竹鼠怪，嘤嘤怪", System.currentTimeMillis());
            msgItemList.add(item);
        }
    }

    private void initView(View view) {
        final ViewGroup titleLayout = view.findViewById(R.id.layout_msg_title);
        ((TextView) titleLayout.findViewById(R.id.tv_common_title)).setText(R.string.activity_main_message);
        titleLayout.setPadding(0, ScreenUtil.getStatusBarHeight(), 0, 0);

        recyclerView = view.findViewById(R.id.rv_msg);
        msgAdapter = new MsgAdapter();
        msgAdapter.setDataList(msgItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(msgAdapter);
        recyclerView.addItemDecoration(new MsgDecoration(ContextCompat.getDrawable(getContext(), R.drawable.msg_item_decoration), 16));
    }
}
