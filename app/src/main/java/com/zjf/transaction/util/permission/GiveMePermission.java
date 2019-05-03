package com.zjf.transaction.util.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.zjf.transaction.widget.CommonDialogBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengjiafeng on 2019/4/4
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class GiveMePermission {
    private static Context context;
    private int requestCode;
    private String[] permissions;
    private String rationaleText, positiveText, negativeText;

    public interface OnRequestPermissionCallback {
        void onPermissionGranted(int requestCode, String[] permissions);

        void onPermissionDenied(int requestCode, String[] deniedPermissions);
    }

    private GiveMePermission() {
    }

    public static GiveMePermission with(Activity activity) {
        PermissionHelper.requireNonNull(activity, "activity is null");
        context = activity;
        return new GiveMePermission();
    }

    public static GiveMePermission with(Fragment fragment) {
        Context fragmentContext = fragment.getContext();
        PermissionHelper.requireNonNull(fragmentContext, "fragment is null");
        context = fragmentContext;
        return new GiveMePermission();
    }

    public GiveMePermission requestCode(int requestCode) {
        PermissionHelper.requireNonNull(requestCode, "request code is null");
        this.requestCode = requestCode;
        return this;
    }

    public GiveMePermission permissions(String[] permissions) {
        PermissionHelper.requireNonNull(permissions, "permission array is null");
        if (permissions.length == 0) {
            throw new IllegalArgumentException("the length of permission array is 0");
        }
        this.permissions = permissions;
        return this;
    }

    public GiveMePermission permission(String permission) {
        PermissionHelper.requireNonNull(permission, "permission is null");
        this.permissions = new String[]{permission};
        return this;
    }

    public GiveMePermission rationale(String rationaleText) {
        PermissionHelper.requireNonNull(rationaleText, "text of rationale is null");
        this.rationaleText = rationaleText;
        return this;
    }

    public GiveMePermission rationale(@StringRes int rationaleText) {
        return rationale(context.getString(rationaleText));
    }

    public GiveMePermission positiveText(String positiveText) {
        PermissionHelper.requireNonNull(positiveText, "text of positive button is null");
        this.positiveText = positiveText;
        return this;
    }

    public GiveMePermission positiveText(@StringRes int positiveText) {
        return positiveText(context.getString(positiveText));
    }

    public GiveMePermission negativeText(String negativeText) {
        PermissionHelper.requireNonNull(negativeText, "text of negative button is null");
        this.negativeText = negativeText;
        return this;
    }

    public GiveMePermission negativeText(@StringRes int negativeText) {
        return negativeText(context.getString(negativeText));
    }

    public void request() {
        final OnRequestPermissionCallback callback = (OnRequestPermissionCallback) context;

        if (!PermissionHelper.isSDKVersionOver23()) { //版本<android 6.0
            callback.onPermissionGranted(requestCode, permissions);
            return;
        }

        //寻找未授权的权限
        final List<String> deniedPermission = PermissionHelper.findDeniedPermission(context, permissions);
        //已全部授权
        if (deniedPermission == null) {
            return;
        }

        if (PermissionHelper.isEmpty(deniedPermission)) {//权限已经全部授权
            callback.onPermissionGranted(requestCode, permissions);
        } else {
            //判断某些权限是否被拒绝过
            boolean shouldShowRationale = false;
            for (String s : deniedPermission) {
                shouldShowRationale = shouldShowRationale | PermissionHelper.shouldShowRequestPermissionRationale(context, s);
            }
            final String[] deniedPermissionArray = deniedPermission.toArray(new String[0]);
            if (shouldShowRationale) {
                new CommonDialogBuilder(context)
                        .setTitle(rationaleText)
                        .setNegativeText(negativeText)
                        .setPositiveText(positiveText)
                        .setActionListener(new CommonDialogBuilder.ActionListener() {
                            @Override
                            public void onPositive(View v) { //请求权限
                                performRequestPermission(context, requestCode, deniedPermissionArray);
                            }

                            @Override
                            public void onNegative(View v) { //拒绝权限
                                callback.onPermissionDenied(requestCode, deniedPermissionArray);
                            }
                        })
                        .setCancelOutside(true)
                        .setCancelable(true)
                        .show();
            } else {
                performRequestPermission(context, requestCode, deniedPermissionArray);
            }
        }
    }

    public static void onRequestPermissionResult(Object object, int requestCode, String[] permissions, int[] grantResults) {
        OnRequestPermissionCallback callback = null;
        if (object instanceof OnRequestPermissionCallback) {
            callback = (OnRequestPermissionCallback) object;
        } else {
            throw new IllegalArgumentException("the class which requests permission must implements OnRequestPermissionCallback");
        }
        final List<String> grantedPermissions = new ArrayList<>();
        final List<String> deniedPermissions = new ArrayList<>();
        if (grantResults != null && grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i]);
                } else {
                    grantedPermissions.add(permissions[i]);
                }
            }
        }
        if (!PermissionHelper.isEmpty(deniedPermissions)) {
            callback.onPermissionDenied(requestCode, deniedPermissions.toArray(new String[0]));
        }
        if (!PermissionHelper.isEmpty(grantedPermissions)) {
            callback.onPermissionGranted(requestCode, grantedPermissions.toArray(new String[0]));
        }
    }


    /**
     * 请求权限
     *
     * @param context
     * @param requestCode
     * @param deniedPermissionArray
     */
    @TargetApi(23)
    private void performRequestPermission(Object context, int requestCode, String[] deniedPermissionArray) {
        if (context instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) context, deniedPermissionArray, requestCode);
        } else if (context instanceof Fragment) {
            ((Fragment) context).requestPermissions(permissions, requestCode);
        }
    }
}
