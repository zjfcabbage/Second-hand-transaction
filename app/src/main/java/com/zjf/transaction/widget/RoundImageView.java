package com.zjf.transaction.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.zjf.transaction.R;
import com.zjf.transaction.util.LogUtil;

/**
 * Created by zhengjiafeng on 2019/2/14
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class RoundImageView extends AppCompatImageView {
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_RECTANGLE = 1;
    private int type;
    private int leftTop, leftBottom, rightTop, rightBottom, radius;
    private final Paint mPaint;
    private float[] radii = new float[8];
    private Bitmap dstBitmap;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        type = array.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);
        leftTop = array.getDimensionPixelSize(R.styleable.RoundImageView_left_top_radius, leftTop);
        leftBottom = array.getDimensionPixelSize(R.styleable.RoundImageView_left_bottom_radius, leftBottom);
        rightBottom = array.getDimensionPixelSize(R.styleable.RoundImageView_right_bottom_radius, rightBottom);
        rightTop = array.getDimensionPixelSize(R.styleable.RoundImageView_right_top_radius, rightTop);

        radius = array.getDimensionPixelOffset(R.styleable.RoundImageView_radius, 0);

        array.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        setLayerType(View.LAYER_TYPE_HARDWARE, null); //取消硬件加速
        setScaleType(ScaleType.FIT_XY);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dstBitmap == null || dstBitmap.isRecycled()) {
            dstBitmap = createDst();
        }
        if (dstBitmap != null) {
            canvas.drawBitmap(dstBitmap, 0, 0, mPaint);
        }
    }

    private void setRoundRectRadius() {
        if (radius != 0) {
            for (int i = 0; i < 8; i++) {
                radii[i] = radius;
            }
            return;
        }
        radii[0] = leftTop;
        radii[1] = leftTop;
        radii[2] = rightTop;
        radii[3] = rightTop;
        radii[4] = rightBottom;
        radii[5] = rightBottom;
        radii[6] = rightTop;
        radii[7] = rightTop;
    }

    private void clearDst() {
        if (dstBitmap != null) {
            dstBitmap.recycle();
            dstBitmap = null;
        }
        invalidate();
    }

    private Bitmap createDst() {
        Bitmap dst = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dst);
        canvas.drawARGB(0, 0, 0, 0); //透明背景色，很重要
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        if (type == TYPE_CIRCLE) {
            drawCircleDst(canvas, paint);
        } else if (type == TYPE_RECTANGLE) {
            drawRectDst(canvas, paint);
        }
        return dst;
    }

    private void drawRectDst(Canvas canvas, Paint paint) {
        setRoundRectRadius();
        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        Path path = new Path();
        path.addRoundRect(rectF, radii, Path.Direction.CW); //CW 顺时针
        canvas.drawPath(path, paint);
    }

    private void drawCircleDst(Canvas canvas, Paint paint) {
        int radius = Math.min(getWidth(), getHeight()) / 2;
        canvas.drawCircle(radius, radius, radius, paint);
    }

    public void setRadius(int radius) {
        setRadius(radius, radius, radius, radius);
    }

    public void setRadius(int leftTop, int rightTop, int rightBottom, int leftBottom) {
        this.leftTop = leftTop;
        this.rightTop = rightTop;
        this.rightBottom = rightBottom;
        this.leftBottom = leftBottom;
        setRoundRectRadius();
        clearDst();
    }

    private int dp(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }
}
