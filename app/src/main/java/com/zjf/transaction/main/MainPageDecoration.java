package com.zjf.transaction.main;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zjf.transaction.app.AppConfig;
import com.zjf.transaction.util.ScreenUtil;

/**
 * Created by zhengjiafeng on 2019/3/16
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class MainPageDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "MainPageDecoration";

    private int padding;

    public MainPageDecoration(int padding) {
        this.padding = ScreenUtil.dp2px(AppConfig.context(), padding);
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
