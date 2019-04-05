package com.zjf.transaction.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengjiafeng on 2019/3/15
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {

    private final List<T> dataList = new ArrayList<>();

    public void setDataList(List<T> data) {
        dataList.clear();
        if (data != null && !data.isEmpty()) {
            dataList.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void appendDataList(List<T> data) {
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData() {
        dataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<T> holder, int i) {
        final T data = dataList.get(i);
        holder.onBind(data, i);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
