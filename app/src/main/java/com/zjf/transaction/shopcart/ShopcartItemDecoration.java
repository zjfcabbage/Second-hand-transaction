package com.zjf.transaction.shopcart;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zjf.transaction.app.AppConfig;
import com.zjf.transaction.util.ScreenUtil;

/**
 * Created by zhengjiafeng on 2019/4/2
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class ShopcartItemDecoration extends RecyclerView.ItemDecoration {
    private int padding;

    public ShopcartItemDecoration(int padding) {
        this.padding = ScreenUtil.dp2px(AppConfig.context(), padding);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = parent.getPaddingLeft() + padding;
        outRect.right = parent.getPaddingRight() + padding;
        outRect.bottom = outRect.top + padding;
        int position = parent.getChildAdapterPosition(view);
        if (position == 0) {
            outRect.top = parent.getPaddingTop() + padding;
        }
    }
}
