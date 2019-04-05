package com.zjf.transaction.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhengjiafeng on 2019/3/15
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    private T data;

    /**
     * 传递数据给viewholder
     * @param data 数据
     */
    public void setData(T data) {
        this.data = data;
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
