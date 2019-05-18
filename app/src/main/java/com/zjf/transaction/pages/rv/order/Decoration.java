package com.zjf.transaction.pages.rv.order;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import com.zjf.transaction.util.ScreenUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Decoration extends RecyclerView.ItemDecoration {
    private int padding;
    public Decoration(Context context, int padding) {
        super();
        this.padding = ScreenUtil.dp2px(context, padding);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); //获取子项的位置
        int type = parent.getAdapter().getItemViewType(position);
        if (position == 0) {
            outRect.top = padding;
        }
        outRect.left = padding;
        outRect.right = padding;
        if (type == OrderAdapter.TYPE_BOTTOM) {
            outRect.bottom = outRect.top + padding;
        }
    }
}
