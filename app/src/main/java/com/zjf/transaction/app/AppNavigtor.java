package com.zjf.transaction.app;

import android.content.Context;
import android.content.Intent;

import com.zjf.transaction.user.LoginActivity;
import com.zjf.transaction.user.RegisterActivity;

/**
 * Created by zhengjiafeng on 2019/2/7
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class AppNavigtor {
    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void startRegisterActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }
}


