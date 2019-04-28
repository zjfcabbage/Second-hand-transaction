package com.zjf.transaction.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by zjfcabbage on 2019/2/4
 *
 * @author 糟老头子 zjfcabbage
 */
public class LogUtil {
    private static final String LOGUTIL = LogUtil.class.getSimpleName();


    public static void d(String format, Object... args) {
        debug(format, args);
    }

    public static void i(String format, Object... args) {
        info(format, args);
    }

    public static void w(String format, Object... args) {
        warn(format, args);
    }

    public static void e(String format, Object... args) {
        error(format, args);
    }

    private static void debug(String format, Object... args) {
        final String msg = formatMsg(format, args);
        Log.d(LogUtil.LOGUTIL, msg);
    }

    private static void info(String format, Object... args) {
        final String msg = formatMsg(format, args);
        Log.i(LogUtil.LOGUTIL, msg);
    }

    private static void warn(String format, Object... args) {
        final String msg = formatMsg(format, args);
        Log.w(LogUtil.LOGUTIL, msg);
    }

    private static void error(String format, Object... args) {
        final String msg = formatMsg(format, args);
        Log.e(LogUtil.LOGUTIL, msg);
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
