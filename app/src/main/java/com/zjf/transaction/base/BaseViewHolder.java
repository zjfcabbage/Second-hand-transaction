package com.zjf.transaction.base;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhengjiafeng on 2019/3/15
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    private T indexData;

    /**
     * 传递数据给viewholder
     * @param data 数据
     */
    public void setIndexData(T data) {
        this.indexData = data;
    }

    public T getIndexData() {
        return indexData;
    }

    /**
     * viewholder绑定到view
     * @param data 数据
     */
    public abstract void onBind(T data, int position);

    public Context getContext() {
        return itemView.getContext();
    }

    public BaseViewHolder(View itemView) {
        super(itemView);
    }
}
