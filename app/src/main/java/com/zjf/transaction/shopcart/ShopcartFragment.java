package com.zjf.transaction.shopcart;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zjf.transaction.MainActivity;
import com.zjf.transaction.R;
import com.zjf.transaction.app.AppConfig;
import com.zjf.transaction.base.BaseAdapter;
import com.zjf.transaction.base.BaseConstant;
import com.zjf.transaction.base.BaseFragment;
import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.main.api.impl.MainApiImpl;
import com.zjf.transaction.main.model.Commodity;
import com.zjf.transaction.shopcart.api.impl.ShopcartApiImpl;
import com.zjf.transaction.shopcart.model.ShopcartItem;
import com.zjf.transaction.user.UserConfig;
import com.zjf.transaction.util.LogUtil;
import com.zjf.transaction.util.ScreenUtil;
import com.zjf.transaction.widget.CommonDialogBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhengjiafeng on 2019/3/13
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class ShopcartFragment extends BaseFragment {

    private static final int DEFAULT_PAGE_NUM = 1;
    private int pageNum = DEFAULT_PAGE_NUM;
    private List<ShopcartItem> shopcartItemList;
    private BaseAdapter<ShopcartItem> shopcartAdapter;
    private CheckBox cbChooseAll;
    private TextView tvAllMoney;
    private TextView tvPay;
    private Disposable disposable;

    @Override
    public View onCreateContent(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopcart, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        ViewGroup titleLayout = view.findViewById(R.id.layout_shopcart_title);
        titleLayout.setPadding(0, ScreenUtil.getStatusBarHeight(), 0, 0);
        ((TextView) titleLayout.findViewById(R.id.tv_common_title)).setText(getArguments().getString(MainActivity.KEY_TITLE));

        initShopcartBottomLayout(view);

        initRefreshLayout(view);
    }

    private void initRefreshLayout(View view) {
        SmartRefreshLayout refreshLayout = view.findViewById(R.id.layout_refresh);
        RecyclerView recyclerView = refreshLayout.findViewById(R.id.rv_commodity);

        shopcartAdapter = new ShopcartAdapter();
        recyclerView.setAdapter(shopcartAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new ShopcartItemDecoration(10));

        ((ShopcartAdapter) shopcartAdapter).setOnItemCheckChangedListener(new ShopcartAdapter.onItemCheckChangedListener() {
            @Override
            public void onItemCheckChanged(CompoundButton buttonView, boolean isChecked) {
                final List<ShopcartItem> list = shopcartAdapter.getDataList();
                float sumPrice = 0;
                for (int i = 0; i < list.size(); i++) {
                    ShopcartItem item = list.get(i);
                    if (item.isChecked()) {
                        String price = item.getCommodity().getPrice();
                        try {
                            sumPrice += Float.valueOf(price);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (sumPrice == 0) {
                    tvAllMoney.setText("0");
                } else {
                    tvAllMoney.setText(sumPrice + "");
                }
            }
        });

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                //上拉加载更多
                ShopcartApiImpl.getShopcartItem(UserConfig.inst().getUserId(), pageNum + 1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<DataResult<List<ShopcartItem>>>() {
                            @Override
                            public void accept(DataResult<List<ShopcartItem>> listDataResult) throws Exception {
                                if (listDataResult.code == DataResult.CODE_SUCCESS && listDataResult.data != null) {
                                    shopcartAdapter.appendDataList(listDataResult.data);
                                    shopcartItemList.addAll(listDataResult.data);
                                    refreshLayout.finishLoadMore(true);
                                    pageNum++;
                                    LogUtil.d("load more shopcart item success");
                                } else {
                                    LogUtil.e("load more shopcart item failed, msg -> %s", listDataResult.msg);
                                    refreshLayout.finishLoadMore(false);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogUtil.e("load more shopcart item eror, throwable -> %s", throwable.getMessage());
                            }
                        });
            }

            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                //下拉刷新
                ShopcartApiImpl.getShopcartItem(UserConfig.inst().getUserId(), DEFAULT_PAGE_NUM)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<DataResult<List<ShopcartItem>>>() {
                            @Override
                            public void accept(DataResult<List<ShopcartItem>> listDataResult) throws Exception {
                                if (listDataResult.code == DataResult.CODE_SUCCESS && listDataResult.data != null) {
                                    LogUtil.d("refresh shopcart item success");
                                    shopcartAdapter.setDataList(listDataResult.data);
                                    pageNum = DEFAULT_PAGE_NUM;
                                    shopcartItemList = listDataResult.data;
                                    refreshLayout.finishRefresh(true);
                                } else {
                                    LogUtil.e("refresh shopcart item failed, msg -> %s", listDataResult.msg);
                                    refreshLayout.finishRefresh(false);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogUtil.e("refresh shopcart item error, throwable -> %s", throwable.getMessage());
                                refreshLayout.finishRefresh(false);
                            }
                        });
            }
        });
    }

    private void initShopcartBottomLayout(View view) {
        ViewGroup shopcartBottomLayout = view.findViewById(R.id.layout_shopcart_bottom);
        cbChooseAll = shopcartBottomLayout.findViewById(R.id.cb_choose_all);
        tvAllMoney = shopcartBottomLayout.findViewById(R.id.tv_all_money);
        tvPay = shopcartBottomLayout.findViewById(R.id.tv_pay);

        cbChooseAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < shopcartItemList.size(); i++) {
                    shopcartItemList.get(i).setChecked(isChecked);
                }
                shopcartAdapter.setDataList(shopcartItemList);
            }
        });

        tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float sumMoney = 0;
                try {
                    sumMoney = Float.valueOf(tvAllMoney.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (sumMoney == 0) {
                    Toast.makeText(getActivity(), "请选择想要购买的商品", Toast.LENGTH_SHORT).show();
                } else {
                    new CommonDialogBuilder(getActivity())
                            .setTitle("付款")
                            .setMsg("需要支付" + sumMoney + "元")
                            .setPositive("支付", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
                                    deletePayItem();
                                    tvAllMoney.setText("0");
                                }
                            })
                            .show();

                }
            }
        });
    }

    private void deletePayItem() {
        //商品已被购买，清除主页和购物车中对应的条目
        final ArrayList<String> commodityList = new ArrayList<>();
        Iterator<ShopcartItem> iterator = shopcartItemList.iterator();
        while (iterator.hasNext()) {
            ShopcartItem item = iterator.next();
            if (item.isChecked()) {
                commodityList.add(item.getCommodity().getId());
                iterator.remove();
            }
        }
        shopcartAdapter.setDataList(shopcartItemList);
        ShopcartApiImpl.deleteMore(UserConfig.inst().getUserId(), commodityList)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Function<DataResult<String>, SingleSource<DataResult<String>>>() {
                    @Override
                    public SingleSource<DataResult<String>> apply(DataResult<String> stringDataResult) throws Exception {
                        if (stringDataResult.code == DataResult.CODE_SUCCESS) {
                            LogUtil.d("shop item, update shopcart success");
                            return MainApiImpl.delete(commodityList);
                        } else {
                            LogUtil.d("shop item, update shopcart failed, msg -> %s", stringDataResult.msg);
                            return null;
                        }
                    }
                }).subscribe(new Consumer<DataResult<String>>() {
            @Override
            public void accept(DataResult<String> stringDataResult) throws Exception {
                if (stringDataResult.code == DataResult.CODE_SUCCESS) {
                    LogUtil.d("shop item, update main page success");
                    Intent intent = new Intent(BaseConstant.ACTION_MAIN);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(BaseConstant.KEY_MAIN_DELETE, commodityList);
                    intent.putExtra(BaseConstant.KEY_MAIN_BUNDLE, bundle);
                    AppConfig.getManager().sendBroadcast(intent);
                } else {
                    LogUtil.d("shop item, update main page failed, msg -> %s", stringDataResult.msg);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtil.d("shop item, update main page error, throwable -> %s", throwable.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (disposable == null) {
            disposable = ShopcartApiImpl.getShopcartItem(UserConfig.inst().getUserId(), DEFAULT_PAGE_NUM)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<DataResult<List<ShopcartItem>>>() {
                        @Override
                        public void accept(DataResult<List<ShopcartItem>> listDataResult) throws Exception {
                            if (listDataResult.code == DataResult.CODE_SUCCESS && listDataResult.data != null) {
                                LogUtil.d("refresh shopcart item success");
                                shopcartAdapter.setDataList(listDataResult.data);
                                shopcartItemList = listDataResult.data;
                            } else {
                                LogUtil.e("refresh shopcart item failed, msg -> %s", listDataResult.msg);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtil.e("refresh shopcart item error, throwable -> %s", throwable.getMessage());
                        }
                    });
        }
    }
}
