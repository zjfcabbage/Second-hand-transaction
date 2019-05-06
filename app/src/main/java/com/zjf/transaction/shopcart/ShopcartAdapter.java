package com.zjf.transaction.shopcart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseAdapter;
import com.zjf.transaction.base.BaseConstant;
import com.zjf.transaction.base.BaseViewHolder;
import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.main.model.Commodity;
import com.zjf.transaction.pages.commodity.CommodityActivity;
import com.zjf.transaction.shopcart.api.impl.ShopcartApiImpl;
import com.zjf.transaction.shopcart.model.ShopcartItem;
import com.zjf.transaction.user.UserConfig;
import com.zjf.transaction.user.model.User;
import com.zjf.transaction.util.ImageUtil;
import com.zjf.transaction.util.LogUtil;
import com.zjf.transaction.util.PriceUtil;
import com.zjf.transaction.widget.CommonDialogBuilder;

/**
 * Created by zhengjiafeng on 2019/3/28
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class ShopcartAdapter extends BaseAdapter<ShopcartItem> {

    private onItemCheckChangedListener onItemCheckChangedListener;

    interface onItemCheckChangedListener {
        void onItemCheckChanged(CompoundButton buttonView, boolean isChecked);
    }

    @NonNull
    @Override
    public BaseViewHolder<ShopcartItem> onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_shopcart_item, parent, false);
        return new ShopcartHolder(view);
    }

    class ShopcartHolder extends BaseViewHolder<ShopcartItem> {

        private ViewGroup commodityLayout;
        private ViewGroup userLayout;
        private ViewGroup commodityInfoLayout;
        private ImageView ivUserPic, ivCommodityPic;
        private TextView tvUserName, tvCommodityMsg, tvCommodityMoney;
        private CheckBox cbChooseCommodity;

        public ShopcartHolder(final View itemView) {
            super(itemView);
            commodityLayout = itemView.findViewById(R.id.layout_commodity_item);
            //user layout
            userLayout = commodityLayout.findViewById(R.id.layout_user);
            ivUserPic = userLayout.findViewById(R.id.iv_user_pic);
            tvUserName = userLayout.findViewById(R.id.tv_user_name);
            userLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2019/3/28 跳转到用户信息界面
                }
            });


            //commodity layout
            commodityInfoLayout = commodityLayout.findViewById(R.id.layout_commodity_info);
            ivCommodityPic = commodityInfoLayout.findViewById(R.id.iv_commodity_pic);
            tvCommodityMsg = commodityInfoLayout.findViewById(R.id.tv_commodity_msg);
            tvCommodityMoney = commodityInfoLayout.findViewById(R.id.tv_commodity_money);
            cbChooseCommodity = commodityInfoLayout.findViewById(R.id.cb_commodity_choose);

            commodityInfoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    ShopcartItem shopcartItem = getIndexData();
                    if (shopcartItem != null && shopcartItem.getCommodity() != null && shopcartItem.getUser() != null) {
                        bundle.putParcelable(BaseConstant.KEY_COMMODITY, shopcartItem.getCommodity());
                        bundle.putParcelable(BaseConstant.KEY_USER, shopcartItem.getUser());
                    }
                    CommodityActivity.start(getContext(), bundle, CommodityActivity.class);
                }
            });
        }


        @Override
        public void onBind(final ShopcartItem data, final int position) {
            User user = data.getUser();
            final Commodity commodity = data.getCommodity();
            if (user == null || commodity == null) {
                return;
            }
            ImageUtil.loadImage(ivUserPic, user.getUserPicUrl());
            tvUserName.setText(user.getUserName());
            ImageUtil.loadImage(ivCommodityPic, getFirstImageUrl(commodity.getImageUrls()));
            tvCommodityMsg.setText(commodity.getMsg());
            tvCommodityMoney.setText(PriceUtil.createPrice(commodity.getPrice()));
            cbChooseCommodity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    data.setChecked(isChecked);
                    if (onItemCheckChangedListener != null) {
                        onItemCheckChangedListener.onItemCheckChanged(buttonView, isChecked);
                    }
                }
            });
            cbChooseCommodity.setChecked(data.isChecked());

            commodityInfoLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new CommonDialogBuilder(getContext())
                            .setTitle("确定从购物车移除此商品吗？")
                            .setNegativeText("取消")
                            .setPositive("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Commodity commodity = getIndexData().getCommodity();
                                    if (commodity != null) {
                                        ShopcartApiImpl.delete(UserConfig.inst().getUserId(), commodity.getId())
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Consumer<DataResult<String>>() {
                                                    @Override
                                                    public void accept(DataResult<String> stringDataResult) throws Exception {
                                                        if (stringDataResult.code == DataResult.CODE_SUCCESS) {
                                                            getDataList().remove(position);
                                                            notifyDataSetChanged();
                                                            LogUtil.d("delete commodity success");
                                                            Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            LogUtil.e("delete commodity failed, msg -> %s", stringDataResult.msg);
                                                            Toast.makeText(getContext(), "删除失败，请检查网络后重试", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }, new Consumer<Throwable>() {
                                                    @Override
                                                    public void accept(Throwable throwable) throws Exception {
                                                        LogUtil.e("delete commodity error, throwable -> %s", throwable.getMessage());
                                                        Toast.makeText(getContext(), "删除失败，请检查网络后重试", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            })
                            .show();
                    return true;
                }
            });
        }

        private String getFirstImageUrl(String imageUrls) {
            if (imageUrls == null) {
                return null;
            }
            String[] strings = imageUrls.split("@@@");
            return strings[0];
        }
    }

    public void setOnItemCheckChangedListener(ShopcartAdapter.onItemCheckChangedListener onItemCheckChangedListener) {
        this.onItemCheckChangedListener = onItemCheckChangedListener;
    }
}
