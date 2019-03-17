package com.zjf.transaction.main;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhengjiafeng on 2019/3/16
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class DividerDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "DividerDecoration";

    private int padding;

    public DividerDecoration(int padding) {
        this.padding = padding;
    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position % 2 == 0) {
            //view在第一列
            outRect.left = parent.getPaddingLeft() + padding;
            outRect.right = padding / 2;
        } else if (position % 2 == 1){
            //view在第二列
            outRect.left = padding /2;
            outRect.right = parent.getPaddingRight() + padding;
        }
        outRect.top = padding / 2;
        outRect.bottom = padding / 2;
    }
}
