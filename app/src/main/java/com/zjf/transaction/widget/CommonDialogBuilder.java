package com.zjf.transaction.widget;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.zjf.transaction.R;
import com.zjf.transaction.util.ScreenUtil;

/**
 * Created by zhengjiafeng on 2019/4/5
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class CommonDialogBuilder {
    private Context context;
    private String title;
    private String message;
    private String positiveText;
    private String negativeText;
    private View.OnClickListener positiveButtonListener;
    private View.OnClickListener negativeButtonListener;
    private ActionListener actionListener;
    private boolean isCancelable;
    private boolean isCancelOutside;
    private boolean dismissAuto;

    public interface ActionListener{
        void onPositive(View v);

        void onNegative(View v);
    }

    public CommonDialogBuilder(Context context) {
        this.context = context;
        title = "";
        message = "";
        positiveText = "确定";
        negativeText = "取消";
        isCancelable = true;
        isCancelOutside = true;
        dismissAuto = true;
    }

    public CommonDialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public CommonDialogBuilder setTitle(@StringRes int title) {
        this.title = context.getString(title);
        return this;
    }

    public CommonDialogBuilder setMsg(String message) {
        this.message = message;
        return this;
    }


    public CommonDialogBuilder setMsg(@StringRes int msg) {
        this.message = context.getString(msg);
        return this;
    }

    public CommonDialogBuilder setPositiveText(String positiveText) {
        this.positiveText = positiveText;
        return this;
    }

    public CommonDialogBuilder setPositiveText(@StringRes int positiveText) {
        this.positiveText = context.getString(positiveText);
        return this;
    }

    public CommonDialogBuilder setNegativeText(String negativeText) {
        this.negativeText = negativeText;
        return this;
    }

    public CommonDialogBuilder setNegativeText(@StringRes int negativeText) {
        this.negativeText = context.getString(negativeText);
        return this;
    }

    public CommonDialogBuilder setPositive(String positiveText, View.OnClickListener positiveButtonListener) {
        this.positiveText = positiveText;
        this.positiveButtonListener = positiveButtonListener;
        return this;
    }

    public CommonDialogBuilder setPositive(@StringRes int positiveText, View.OnClickListener positiveButtonListener) {
        this.positiveText = context.getString(positiveText);
        this.positiveButtonListener = positiveButtonListener;
        return this;
    }

    public CommonDialogBuilder setNegative(String negativeText, View.OnClickListener negativeButtonListener) {
        this.negativeText = negativeText;
        this.negativeButtonListener = negativeButtonListener;
        return this;
    }

    public CommonDialogBuilder setNegative(@StringRes int negativeText, View.OnClickListener negativeButtonListener) {
        this.negativeText = context.getString(negativeText);
        this.negativeButtonListener = negativeButtonListener;
        return this;
    }

    public CommonDialogBuilder setCancelable(boolean isCancelable) {
        this.isCancelable = isCancelable;
        return this;
    }

    public CommonDialogBuilder setCancelOutside(boolean isCancelOutside) {
        this.isCancelOutside = isCancelOutside;
        return this;
    }

    public CommonDialogBuilder setDismissAuto(boolean isDismissAuto){
        this.dismissAuto = isDismissAuto;
        return this;
    }

    public CommonDialogBuilder setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    public Dialog create() {
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialog.setContentView(R.layout.widget_common_dialog);
        final ViewGroup textLayout = dialog.findViewById(R.id.layout_text);
        final TextView tvTitle = textLayout.findViewById(R.id.dialog_title);
        final TextView tvMessage = textLayout.findViewById(R.id.dialog_message);
        final ViewGroup buttonLayout = dialog.findViewById(R.id.layout_button);
        final TextView btnNegative = buttonLayout.findViewById(R.id.dialog_negative);
        final TextView btnPositive = buttonLayout.findViewById(R.id.dialog_positive);
        final View btnLine = buttonLayout.findViewById(R.id.line_button);

        //两个button的话显示中间的分割线
        if (!TextUtils.isEmpty(negativeText) && !TextUtils.isEmpty(positiveText)) {
            btnLine.setVisibility(View.VISIBLE);
        }

        //设置title, message, negativeBtn, positiveBtn
        setTextWithVisibility(tvTitle, title);
        setTextWithVisibility(tvMessage, message);
        setTextWithVisibility(btnNegative, negativeText);
        setTextWithVisibility(btnPositive, positiveText);

        remeasureLayout(textLayout, tvTitle, tvMessage);

        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (negativeButtonListener != null) {
                    negativeButtonListener.onClick(v);
                } else if (actionListener != null) {
                    actionListener.onNegative(v);
                }
                if (dismissAuto) {
                    dialog.dismiss();
                }
            }
        });

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positiveButtonListener != null) {
                    positiveButtonListener.onClick(v);
                } else if (actionListener != null) {
                    actionListener.onPositive(v);
                }
                if (dismissAuto) {
                    dialog.dismiss();
                }
            }
        });
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelOutside);
        return dialog;
    }

    public void show() {
        final Dialog dialog = create();
        dialog.show();
    }

    private void setTextWithVisibility(TextView textView, String text) {
        if (textView == null) {
            return;
        }
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 重新布局
     *
     * @param parent
     * @param titleLineCount 为0 说明没有title
     * @param msgLineCount   为0 说明没有message
     */
    private void performChangeLayout(ViewGroup parent, int titleLineCount, int msgLineCount) {
        if (titleLineCount >= 2 && msgLineCount == 0) {
            updateLayoutParams(parent, 26, 26);
        } else if (titleLineCount == 1 && msgLineCount == 0) {
            updateLayoutParams(parent, 35, 35);
        } else if (titleLineCount == 1 && msgLineCount == 1) {
            updateLayoutParams(parent, 26, 26);
        } else if (titleLineCount == 1 && msgLineCount >= 2) {
            updateLayoutParams(parent, 23, 23);
        }
    }

    private void updateLayoutParams(View parent, float topPadding, float bottomPadding) {
        parent.setPadding(0, (int) ScreenUtil.dp2px(context, topPadding), 0, (int) ScreenUtil.dp2px(context, bottomPadding));
    }

    private void remeasureLayout(final ViewGroup parent, final TextView titleView, final TextView msgView) {
        if (parent == null) {
            return;
        }
        ViewTreeObserver.OnPreDrawListener onPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int titleLineCount = 0;
                int msgLineCount = 0;
                if (titleView != null && !TextUtils.isEmpty(title)) {
                    titleLineCount = titleView.getLineCount();
                }

                if (msgView != null && !TextUtils.isEmpty(message)) {
                    msgLineCount = msgView.getLineCount();
                }
                performChangeLayout(parent, titleLineCount, msgLineCount);
                parent.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        };
        parent.getViewTreeObserver().addOnPreDrawListener(onPreDrawListener);
    }
}
