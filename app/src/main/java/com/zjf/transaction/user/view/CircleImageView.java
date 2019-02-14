package com.zjf.transaction.user.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.zjf.transaction.R;

/**
 * Created by zhengjiafeng on 2019/2/14
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class CircleImageView extends AppCompatImageView {
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_RECTANGLE = 1;
    private int type;
    private int leftTop, leftBottom, rightTop, rightBottom;
    private final Paint mPaint;
    private Bitmap mask;


    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        type = array.getInt(R.styleable.CircleImageView_type, TYPE_CIRCLE);
        if (type == TYPE_RECTANGLE) {
            setRadius();
        }
        leftTop = array.getDimensionPixelSize(R.styleable.CircleImageView_left_top, leftTop);
        leftBottom = array.getDimensionPixelSize(R.styleable.CircleImageView_left_bottom, leftBottom);
        rightBottom = array.getDimensionPixelSize(R.styleable.CircleImageView_right_bottom, rightBottom);
        rightTop = array.getDimensionPixelSize(R.styleable.CircleImageView_right_top, rightTop);

        mPaint = new Paint();
        mPaint.setFilterBitmap(false);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        array.recycle();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

    }

    private void setRadius() {
        leftTop = leftBottom = rightBottom = rightTop = dp(4);
    }

    private int dp(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }
}
