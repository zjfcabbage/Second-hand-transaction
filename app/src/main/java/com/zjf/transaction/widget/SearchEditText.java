package com.zjf.transaction.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zjf.transaction.R;

import androidx.appcompat.widget.AppCompatEditText;

public class SearchEditText extends AppCompatEditText {

    private Drawable searchDrawable, clearDrawable;
    private OnClearTextListener listener;

    public interface OnClearTextListener {
        void onClearTextListener();
    }

    public SearchEditText(Context context) {
        this(context, null, 0);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        searchDrawable = getResources().getDrawable(R.drawable.icon_search);
        setCompoundDrawablesWithIntrinsicBounds(searchDrawable, getCompoundDrawables()[1], getCompoundDrawables()[2], getCompoundDrawables()[3]);
        clearDrawable = getResources().getDrawable(R.drawable.icon_clear);
        setFocusableInTouchMode(true);  //点击的时候弹出软键盘
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (clearDrawable != null) {
                final float left = getWidth() - getPaddingRight() - clearDrawable.getIntrinsicWidth();
                final float right = getWidth() - getPaddingRight();
                if (event.getX() >= left && event.getX() <= right) {
                    this.setText("");
                    if (listener != null) {
                        listener.onClearTextListener();
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setClearDrawableVisibility(text.length() > 0);
    }

//    @Override
//    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
//        super.onFocusChanged(focused, direction, previouslyFocusedRect);
//        setClearDrawableVisibility(hasFocus() && length() > 0);
//    }

    private void setClearDrawableVisibility(boolean visibile) {
        Drawable right = visibile ? clearDrawable : null;
        setCompoundDrawablesWithIntrinsicBounds(searchDrawable, getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    public void setOnClearTextListener(OnClearTextListener listener) {
        this.listener = listener;
    }
}
