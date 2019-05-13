package com.zjf.transaction.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zjf.transaction.R;
import com.zjf.transaction.app.AppConfig;
import com.zjf.transaction.base.BaseAdapter;
import com.zjf.transaction.base.BaseConstant;
import com.zjf.transaction.base.BaseFragment;
import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.main.api.impl.MainApiImpl;
import com.zjf.transaction.main.model.Commodity;
import com.zjf.transaction.util.LogUtil;
import com.zjf.transaction.util.ScreenUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhengjiafeng on 2019/3/13
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class MainFragment extends BaseFragment {

    private static final int DEFAULT_PAGE_NUM = 1;

    private List<Commodity> commodityList = new ArrayList<>();
    private BaseAdapter<Commodity> adapter;
    private Disposable disposable;
    private int pageNum = DEFAULT_PAGE_NUM;
    private Receiver receiver = new Receiver();


    class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BaseConstant.ACTION_MAIN.equals(intent.getAction())) {
                ArrayList<String> commodityIdList = intent.getBundleExtra(BaseConstant.KEY_MAIN_BUNDLE)
                        .getStringArrayList(BaseConstant.KEY_MAIN_DELETE);
                if (commodityIdList == null) {
                    return;
                }
                if (!commodityIdList.isEmpty()) {
                    List<Commodity> list = adapter.getDataList();
                    Iterator<Commodity> iterator = list.iterator();
                    for (int i = 0; i < commodityIdList.size(); i++) {
                        final String id = commodityIdList.get(i);
                        while (iterator.hasNext()) {
                            Commodity commodity = iterator.next();
                            if (id.equals(commodity.getId())) {
                                iterator.remove();
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public View onCreateContent(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        AppConfig.getLocalBroadcastManager().registerReceiver(receiver, new IntentFilter(BaseConstant.ACTION_MAIN));
        initView(view);
        return view;
    }

    private void initView(View view) {
        ViewGroup searchLayout = view.findViewById(R.id.layout_search);
        searchLayout.requestFocus();
        searchLayout.setPadding(0, ScreenUtil.getStatusBarHeight(), 0, 0); //下移状态栏的高度
        final EditText etSearch = searchLayout.findViewById(R.id.et_search);
        final ImageView ivPublish = searchLayout.findViewById(R.id.iv_publish);
        ivPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishActivity.start(getContext(), PublishActivity.class);
            }
        });

        RefreshLayout refreshLayout = view.findViewById(R.id.layout_refresh);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@androidx.annotation.NonNull final RefreshLayout refreshLayout) {
                MainApiImpl.getAllCommodity(DEFAULT_PAGE_NUM)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<DataResult<List<Commodity>>>() {
                            @Override
                            public void accept(DataResult<List<Commodity>> listDataResult) throws Exception {
                                if (listDataResult.code == DataResult.CODE_SUCCESS) {
                                    LogUtil.d("refresh success");
                                    adapter.setDataList(listDataResult.data);
                                    pageNum = DEFAULT_PAGE_NUM;
                                    refreshLayout.finishRefresh(true);
                                } else {
                                    LogUtil.e("refresh failed, msg -> %s", listDataResult.msg);
                                    refreshLayout.finishRefresh(false);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogUtil.e("refresh failed, throwable -> %s", throwable.getMessage());
                                refreshLayout.finishRefresh(false);
                            }
                        });
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@androidx.annotation.NonNull final RefreshLayout refreshLayout) {
                MainApiImpl.getAllCommodity(pageNum + 1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<DataResult<List<Commodity>>>() {
                            @Override
                            public void accept(DataResult<List<Commodity>> listDataResult) throws Exception {
                                if (listDataResult.code == DataResult.CODE_SUCCESS) {
                                    LogUtil.d("load more success");
                                    adapter.appendDataList(listDataResult.data);
                                    pageNum++;
                                    refreshLayout.finishLoadMore(true);
                                } else {
                                    LogUtil.e("load more failed, msg -> %s", listDataResult.msg);
                                    refreshLayout.finishLoadMore(false);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogUtil.e("load more failed, thrwoable -> %s", throwable.getMessage());
                                refreshLayout.finishLoadMore(false);
                            }
                        });
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        adapter = new MainAdapter();
        adapter.setDataList(commodityList);
        recyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        MainPageDecoration decoration = new MainPageDecoration(10);
        recyclerView.addItemDecoration(decoration);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (disposable == null) {
            disposable = MainApiImpl.getAllCommodity(DEFAULT_PAGE_NUM)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<DataResult<List<Commodity>>>() {
                        @Override
                        public void accept(DataResult<List<Commodity>> listDataResult) throws Exception {
                            if (listDataResult.code == DataResult.CODE_SUCCESS) {
                                adapter.setDataList(listDataResult.data);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtil.e("throwable -> %s", throwable.getMessage());
                        }
                    });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppConfig.getLocalBroadcastManager().unregisterReceiver(receiver);
    }
}
