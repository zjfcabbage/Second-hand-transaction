package com.zjf.transaction.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zjf.transaction.app.AppConfig;

/**
 * Created by zhengjiafeng on 2019/2/7
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class ScreenUtil {
    public static int dp2px(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static float dp2px(Context context, float dp) {
        return  (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int px2dp(Context context, int px) {
        return (int) (px / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static void hideStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void hideStatusBarLight(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static int getStatusBarHeight() {
        int height = 0;
        int resourceId = AppConfig.context().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = AppConfig.context().getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }


    /**
     * 沉浸式状态栏
     * @param activity
     */
    public static void immersiveStatusBar(Activity activity) {
        if (activity == null) {
            return;
        }
        View decorView = activity.getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            option |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        decorView.setSystemUiVisibility(option);
        if (activity instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }

}
