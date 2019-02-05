package com.zjf.transaction.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by zhengjiafeng on 2019/2/4
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class LogUtil {
    private static final String LOGUTIL = LogUtil.class.getSimpleName();


    public static void d(String format, Object... args) {
        debug(LOGUTIL, format, args);
    }

    public static void i(String format, Object... args) {
        info(LOGUTIL, format, args);
    }

    public static void w(String format, Object... args) {
        warn(LOGUTIL, format, args);
    }

    public static void e(String format, Object... args) {
        error(LOGUTIL, format, args);
    }

    private static void debug(String tag, String format, Object... args) {
        final String msg = formatMsg(format, args);
        Log.d(tag, msg);
    }

    private static void info(String tag, String format, Object... args) {
        final String msg = formatMsg(format, args);
        Log.i(tag, msg);
    }

    private static void warn(String tag, String format, Object... args) {
        final String msg = formatMsg(format, args);
        Log.w(tag, msg);
    }

    private static void error(String tag, String format, Object... args) {
        final String msg = formatMsg(tag, args);
        Log.e(tag, msg);
    }

    private static String formatMsg(String format, Object... args) {
        if (TextUtils.isEmpty(format)) {
            return arrayToString(args);
        }
        try {
            return String.format(format, args);
        } catch (Exception e) {
            Log.e(LOGUTIL, "log format = " + format + "格式化失败 -> error = " + Log.getStackTraceString(e));
        }
        return format;
    }

    private static String arrayToString(Object... args) {
        if (args == null || args.length == 0) {
            return "";
        }
        final StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            builder.append(arg);
        }
        return builder.toString();
    }
}
