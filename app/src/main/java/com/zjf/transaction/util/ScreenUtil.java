package com.zjf.transaction.util;

import android.content.Context;

/**
 * Created by zhengjiafeng on 2019/2/7
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class ScreenUtil {
    public static int dp2px(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int px2dp(Context context, int px) {
        return (int) (px / context.getResources().getDisplayMetrics().density + 0.5f);
    }
}
