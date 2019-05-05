package com.zjf.transaction.util;

import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.zjf.transaction.R;
import com.zjf.transaction.app.AppConfig;

/**
 * Created by zhengjiafeng on 2019/3/28
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class PriceUtil {

    public static CharSequence createPrice(String price) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        final Drawable drawable = ContextCompat.getDrawable(AppConfig.context(), R.drawable.icon_price);
        if (drawable != null) {
            builder.append("  ").append(price);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
            builder.setSpan(span, 1, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return builder;
    }

}
