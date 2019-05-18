package com.zjf.transaction.pages.rv.order;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseAdapter;
import com.zjf.transaction.base.BaseViewHolder;
import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.pages.api.impl.OrderApiImpl;
import com.zjf.transaction.pages.model.OrderInfo;
import com.zjf.transaction.pages.model.OrderShow;
import com.zjf.transaction.user.UserConfig;
import com.zjf.transaction.util.ImageUtil;
import com.zjf.transaction.util.LogUtil;
import com.zjf.transaction.util.PriceUtil;
import com.zjf.transaction.widget.CommonDialogBuilder;

import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OrderAdapter extends BaseAdapter<OrderShow> {
    public static final int TYPE_TOP = 0;
    public static final int TYPE_CONTENT = 1;
    public static final int TYPE_BOTTOM = 2;
    private Dialog dialog;

    @NonNull
    @Override
    public BaseViewHolder<OrderShow> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TOP) {
            return new TopViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_order_item_top, parent, false));
        } else if (viewType == TYPE_CONTENT) {
            return new ContentViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_order_item_content, parent, false));
        } else {
            return new BottomViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_order_item_bottom, parent, false));
        }
    }


    class TopViewHolder extends BaseViewHolder<OrderShow> {

        private TextView tvOrderId;

        public TopViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDialog(getContext(), getIndexData().getOrderId());
                    return true;
                }
            });
        }

        @Override
        public void onBind(OrderShow data, int position) {
            tvOrderId.setText(data.getOrderId());
        }
    }

    class ContentViewHolder extends BaseViewHolder<OrderShow> {

        private ImageView ivUserPic, ivCommodityPic;
        private TextView tvUserName, tvMsg, tvMoney;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ivUserPic = itemView.findViewById(R.id.iv_user_pic);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            ivCommodityPic = itemView.findViewById(R.id.iv_commodity_pic);
            tvMsg = itemView.findViewById(R.id.tv_commodity_msg);
            tvMoney = itemView.findViewById(R.id.tv_commodity_money);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDialog(getContext(), getIndexData().getOrderId());
                    return true;
                }
            });
        }

        @Override
        public void onBind(OrderShow data, int position) {
            ImageUtil.loadImage(ivUserPic, data.getUserPicUrl());
            tvUserName.setText(data.getUserName());
            ImageUtil.loadImage(ivCommodityPic, data.getCommodityUrl());
            tvMsg.setText(data.getCommodityMsg());
            tvMoney.setText(PriceUtil.createPrice(data.getPrice()));
        }
    }

    class BottomViewHolder extends BaseViewHolder<OrderShow> {
        private TextView tvOrderMoney;

        public BottomViewHolder(View itemView) {
            super(itemView);
            tvOrderMoney = itemView.findViewById(R.id.tv_order_money);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDialog(getContext(), getIndexData().getOrderId());
                    return true;
                }
            });
        }

        @Override
        public void onBind(OrderShow data, int position) {
            tvOrderMoney.setText(PriceUtil.createPrice(data.getOrderMoney()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getDataList().get(position).getType() == TYPE_TOP) {
            return TYPE_TOP;
        } else if (getDataList().get(position).getType() == TYPE_CONTENT) {
            return TYPE_CONTENT;
        } else {
            return TYPE_BOTTOM;
        }
    }

    private void deleteOrder(final String orderId) {
        OrderApiImpl.deleteOrder(orderId, UserConfig.inst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataResult<String>>() {
                    @Override
                    public void accept(DataResult<String> stringDataResult) throws Exception {
                        if (stringDataResult.code == DataResult.CODE_SUCCESS) {
                            LogUtil.d("delete order success");
                            final List<OrderShow> orderShowList = getDataList();
                            Iterator<OrderShow> iterator = orderShowList.iterator();
                            while (iterator.hasNext()) {
                                if (orderId.equals(iterator.next().getOrderId())) {
                                    iterator.remove();
                                }
                            }
                            notifyDataSetChanged();
                            dialog.dismiss();
                        } else {
                            LogUtil.e("delete order failed, msg -> %s", stringDataResult.msg);
                            dialog.dismiss();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("delete prder error, throwable -> %s", throwable.getMessage());
                        dialog.dismiss();
                    }
                });
    }

    private void showDialog(Context context, final String orderId) {
        dialog = new CommonDialogBuilder(context)
                .setTitle("确定删除此订单吗？")
                .setPositive("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOrder(orderId);
                    }
                })
                .create();
        dialog.show();
    }
}

