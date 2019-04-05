package com.zjf.transaction.msg;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zjf.transaction.app.AppConfig;
import com.zjf.transaction.util.ScreenUtil;

/**
 * Created by zhengjiafeng on 2019/4/4
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class MsgDecoration extends RecyclerView.ItemDecoration {

    private Drawable drawable;
    private int padding;
    private final Rect mBounds = new Rect();

    public MsgDecoration(Drawable drawable, int padding) {
        this.drawable = drawable;
        this.padding = ScreenUtil.dp2px(AppConfig.context(), padding);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        canvas.save();
        final int left;
        final int right;
        // 需要考虑clipToPadding的boolean值
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - drawable.getIntrinsicHeight();
            drawable.setBounds(left, top, right, bottom);
            drawable.draw(canvas);
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = parent.getPaddingLeft() + padding;
        outRect.bottom = (int) (outRect.top + ScreenUtil.dp2px(parent.getContext(), 0.5F));
        outRect.right = parent.getPaddingRight() + padding;
    }
}
