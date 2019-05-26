package com.zjf.transaction.main;

import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.zjf.transaction.app.AppConfig;
import com.zjf.transaction.util.LogUtil;
import com.zjf.transaction.util.ScreenUtil;

/**
 * Created by zhengjiafeng on 2019/3/16
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class MainPageDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "MainPageDecoration";

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int width = ScreenUtil.dp2px(parent.getContext(), 180);
        int screenWidth = ScreenUtil.getScreenWidthInPx(parent.getContext());
        int averageWidth = (screenWidth - 2 * width) / 3;
        if (position == 0 || position == 1) {
            outRect.top = averageWidth;
        }
        if (position % 2 == 0) {
            outRect.left = averageWidth;
            LogUtil.d("outrecf 0 left -> %d", outRect.left);
            LogUtil.d("outrecf 0 right -> %d", outRect.right);
        }
        if (position % 2 == 1) {
            outRect.left = averageWidth - 13;
            LogUtil.d("outrecf 1 left -> %d", outRect.left);
            LogUtil.d("outrecf 1 right -> %d", outRect.right);
        }
        outRect.bottom = averageWidth;
    }
}
