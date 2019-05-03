package com.zjf.transaction.main;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
        int width = ScreenUtil.dp2px(parent.getContext(), 180);
        int screenWidth = ScreenUtil.getScreenWidthInPx(parent.getContext());
        int averageWidth = (screenWidth - 2 * width) / 3;
        if (position % 2 == 0) {
            //view在第一列
            outRect.left = averageWidth;
//            outRect.right = averageWidth / 2;
        } else if (position % 2 == 1){
//            //view在第二列
            outRect.left = averageWidth;
//            outRect.right = parent.getPaddingRight() + averageWidth;
        }
        outRect.top = averageWidth;
        outRect.bottom = averageWidth;
    }
}
