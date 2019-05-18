package com.zjf.transaction.pages;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseActivity;
import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.pages.api.impl.OrderApiImpl;
import com.zjf.transaction.pages.model.OrderInfo;
import com.zjf.transaction.pages.model.OrderShow;
import com.zjf.transaction.pages.rv.order.Decoration;
import com.zjf.transaction.pages.rv.order.OrderAdapter;
import com.zjf.transaction.user.UserConfig;
import com.zjf.transaction.util.LogUtil;
import com.zjf.transaction.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OrderActivity extends BaseActivity {

    private static final int DEFAULT_PAGE_NUM = 1;
    private List<OrderInfo> orderList;
    private OrderAdapter adapter;
    private int pageNum = DEFAULT_PAGE_NUM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_order);
        ScreenUtil.hideStatusBar(this);
        initTitleLayout();
        initRefreshLayout();

    }

    private void initRefreshLayout() {
        final SmartRefreshLayout smartRefreshLayout = findViewById(R.id.layout_refresh);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                OrderApiImpl.getOrder(UserConfig.inst().getUserId(), pageNum)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<DataResult<List<OrderInfo>>>() {
                            @Override
                            public void accept(DataResult<List<OrderInfo>> listDataResult) throws Exception {
                                if (listDataResult.code == DataResult.CODE_SUCCESS) {

                                    adapter.appendDataList(createOrderShowList(listDataResult.data));
                                    refreshLayout.finishLoadMore(true);
                                    LogUtil.d("load more order success");
                                } else {
                                    refreshLayout.finishLoadMore(false);
                                    LogUtil.e("load more order failed, msg -> %s", listDataResult.msg);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                refreshLayout.finishLoadMore(false);
                                LogUtil.e("load more order error, throwable -> %s", throwable.getMessage());
                            }
                        });
            }

            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                OrderApiImpl.getOrder(UserConfig.inst().getUserId(), DEFAULT_PAGE_NUM)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<DataResult<List<OrderInfo>>>() {
                            @Override
                            public void accept(DataResult<List<OrderInfo>> listDataResult) throws Exception {
                                if (listDataResult.code == DataResult.CODE_SUCCESS) {
                                    adapter.setDataList(createOrderShowList(listDataResult.data));
                                    pageNum = DEFAULT_PAGE_NUM + 1;
                                    refreshLayout.finishRefresh(true);
                                    LogUtil.d("refresh order data success");
                                } else {
                                    refreshLayout.finishRefresh(false);
                                    LogUtil.e("refresh order data failed, msg -> %s", listDataResult.msg);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                refreshLayout.finishRefresh(false);
                                LogUtil.e("init order data error, throwable -> %s", throwable.getMessage());
                            }
                        });
            }
        });
        initRecyclerView();
    }

    private void initRecyclerView() {
        final RecyclerView recyclerView = findViewById(R.id.rv_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new Decoration(this, 10));
        adapter = new OrderAdapter();
        initData();
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        OrderApiImpl.getOrder(UserConfig.inst().getUserId(), DEFAULT_PAGE_NUM)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataResult<List<OrderInfo>>>() {
                    @Override
                    public void accept(DataResult<List<OrderInfo>> listDataResult) throws Exception {
                        if (listDataResult.code == DataResult.CODE_SUCCESS) {
                            adapter.setDataList(createOrderShowList(listDataResult.data));
                            pageNum++;
                            LogUtil.d("init order data success");
                        } else {
                            LogUtil.e("init order data failed, msg -> %s", listDataResult.msg);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("init order data error, throwable -> %s", throwable.getMessage());
                    }
                });
    }

    private void initTitleLayout() {
        final ViewGroup titleLayout = findViewById(R.id.layout_title);
        titleLayout.setPadding(0, ScreenUtil.getStatusBarHeight(), 0, 0);
        ((TextView) titleLayout.findViewById(R.id.tv_common_title)).setText("订单");
        ImageView imageView = titleLayout.findViewById(R.id.iv_common_back);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private List<OrderShow> createOrderShowList(List<OrderInfo> orderInfoList) {
        final List<OrderShow> list = new ArrayList<>();
        for (OrderInfo orderInfo : orderInfoList) {
            list.add(new OrderShow(OrderAdapter.TYPE_TOP, orderInfo.getOrderId(), ""));
            List<OrderInfo.Content> contentList = orderInfo.getContentList();
            for (OrderInfo.Content content : contentList) {
                list.add(new OrderShow(OrderAdapter.TYPE_CONTENT, orderInfo.getOrderId(), content.getUserName(),
                        content.getUserPicUrl(), content.getCommodityUrl(), content.getCommodityMsg(), content.getPrice()));
            }
            list.add(new OrderShow(OrderAdapter.TYPE_BOTTOM, orderInfo.getOrderId(), orderInfo.getOrderMoney()));
        }
        return list;
    }
}
