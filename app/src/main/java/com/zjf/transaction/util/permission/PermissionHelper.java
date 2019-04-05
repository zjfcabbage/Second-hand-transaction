package com.zjf.transaction.util.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.zjf.transaction.util.ListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengjiafeng on 2019/4/4
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class PermissionHelper {
    public static Object requireNonNull(Object o, String message) {
        if (o == null) {
            throw new NullPointerException(message);
        }
        return o;
    }

    public static boolean isSDKVersionOver23() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean shouldShowRequestPermissionRationale(@NonNull Object context, String permission) {
        if (context instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission);
        } else if (context instanceof Fragment) {
            return ((Fragment) context).shouldShowRequestPermissionRationale(permission);
        }
        return false;
    }

    public static List<String> findDeniedPermission(@NonNull Context context, @NonNull String... permissions) {
        final List<String> deniedPermission = new ArrayList<>();
        if (isSDKVersionOver23()) {
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(context, permissions[i]) == PackageManager.PERMISSION_DENIED) {
                    deniedPermission.add(permissions[i]);
                }
            }
        }
        return deniedPermission;
    }

    public static boolean isEmpty(List list) {
        return ListUtil.isEmpty(list);
    }
}
