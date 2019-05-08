package com.zjf.transaction.pages;

import android.view.View;
import android.view.ViewGroup;

import com.zjf.transaction.base.BaseAdapter;
import com.zjf.transaction.base.BaseViewHolder;
import com.zjf.transaction.database.entity.Msg;

import androidx.annotation.NonNull;

public class CharAdapter extends BaseAdapter<Msg> {

    @NonNull
    @Override
    public BaseViewHolder<Msg> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    class ViewHolder extends BaseViewHolder<Msg> {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(Msg data, int position) {

        }
    }

}
