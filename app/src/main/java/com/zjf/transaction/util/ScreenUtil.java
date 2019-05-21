package com.zjf.transaction.util;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.input.InputManager;
import android.os.Build;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.IBinder;
import android.os.TokenWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;

import com.zjf.transaction.R;
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
        return (dp * context.getResources().getDisplayMetrics().density + 0.5f);
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

    public static void setStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.colorAccent));
        }
    }

    public static void setStatusLight(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(Color.WHITE);
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

    public static int getScreenWidthInDp(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float density = metrics.density;
        return (int) (metrics.widthPixels / density + 0.5f);
    }


    public static int getScreenWidthInPx(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static void hideSoftInput(Context context, IBinder token) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 沉浸式状态栏
     *
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
