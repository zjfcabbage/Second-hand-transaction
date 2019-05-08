package com.zjf.transaction.msg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjf.transaction.R;
import com.zjf.transaction.app.AppConfig;
import com.zjf.transaction.base.BaseAdapter;
import com.zjf.transaction.base.BaseConstant;
import com.zjf.transaction.base.BaseFragment;
import com.zjf.transaction.database.TransactionDatabase;
import com.zjf.transaction.msg.model.MsgItem;
import com.zjf.transaction.util.LogUtil;
import com.zjf.transaction.util.ScreenUtil;

import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhengjiafeng on 2019/3/13
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class MsgFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private BaseAdapter<MsgItem> msgAdapter;
    private LocalBroadcastReceiver receiver;

    class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BaseConstant.ACTION_ADD_CHAT_LIST_ITEM.equalsIgnoreCase(intent.getAction())) {
                LogUtil.d("MsgFragment 更新数据");
                Bundle bundle = intent.getBundleExtra(BaseConstant.BUNDLE_MSG_ITEM);
                MsgItem item = null;
                if (bundle != null) {
                    item = bundle.getParcelable(BaseConstant.KEY_MSG_ITEM);
                    final String userId = item.getUserId();
                    Iterator<MsgItem> iterator = msgAdapter.getDataList().iterator();
                    while (iterator.hasNext()) {
                        MsgItem msgItem = iterator.next();
                        if (userId.equals(msgItem.getUserId())) {
                            iterator.remove();//如果列表中已存在此用户，则将此用户移除，再添加到最前面
                            break;
                        }
                    }
                    msgAdapter.appendData(0, item);
                    //更新数据库里的表
                    updateMsgItemTable(item);
                }
            }
        }
    }

    private void updateMsgItemTable(MsgItem item) {
        TransactionDatabase.getInstance().getMsgItemDao()
                .insert(item)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtil.d("update msgItem table success, line index -> %d", aLong);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("update msgItem table error, throwable -> %s", throwable.getMessage());
                    }
                });
    }

    @Override
    public View onCreateContent(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        receiver = new LocalBroadcastReceiver();
        AppConfig.getManager().registerReceiver(receiver, new IntentFilter(BaseConstant.ACTION_ADD_CHAT_LIST_ITEM));
        initView(view);
        return view;
    }


    private void initView(View view) {
        final ViewGroup titleLayout = view.findViewById(R.id.layout_msg_title);
        ((TextView) titleLayout.findViewById(R.id.tv_common_title)).setText(R.string.activity_main_message);
        titleLayout.setPadding(0, ScreenUtil.getStatusBarHeight(), 0, 0);
        recyclerView = view.findViewById(R.id.rv_msg);
        msgAdapter = new MsgAdapter();
        initData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(msgAdapter);
        recyclerView.addItemDecoration(new MsgDecoration(ContextCompat.getDrawable(getContext(), R.drawable.msg_item_decoration), 16));
    }

    private void initData() {
        TransactionDatabase.getInstance().getMsgItemDao()
                .getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MsgItem>>() {
                    @Override
                    public void accept(List<MsgItem> msgItems) throws Exception {
                        LogUtil.d("get msgItem success when init data");
                        msgAdapter.setDataList(msgItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("get msgItem error, throwable -> %s", throwable.getMessage());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppConfig.getManager().unregisterReceiver(receiver);
    }
}
