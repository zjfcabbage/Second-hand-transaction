package com.zjf.transaction.util;


import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zhengjiafeng on 2019/4/3
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */

/**
 * 凌晨:3:00--6:00
 * 早晨:6:00---8:00
 * 上午:8:00--11:00
 * 中午:11:00--13:00
 * 下午:13:00--17:00
 * 傍晚:17:00--19:00
 * 晚上:19:00--23:00
 * 深夜:23:00--3:00
 */
public class TimeUtil {

    public static String formatTime(long time) {
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (isInThisYear(date)) { //在这一年，显示:MM月dd日
            if (isInToday(date)) { //在今天，显示上午/下午 HH:mm
                int minutes = formatToMin(time);
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                if (minutes <= 1) {
                    return "刚刚";
                } else if (minutes < 60) {
                    return minutes + "分钟前";
                } else if (hours >= 3 && hours < 6) {
                    return "凌晨" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
                } else if (hours >= 6 && hours < 8) {
                    return "早晨" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
                } else if (hours >= 8 && hours < 11) {
                    return "上午" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
                } else if (hours >= 11 && hours < 13) {
                    return "中午" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
                } else if (hours >= 13 && hours < 17) {
                    return "下午" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
                } else if (hours >= 17 && hours < 19) {
                    return "傍晚" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
                } else if (hours >= 19 && hours < 23) {
                    return "晚上" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
                } else {
                    return "深夜" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
                }
            } else {
                if (isInYesterday(date)) { //在昨天
                    return "昨天";
                } else if (isInWeek(date)) {  //在同一周
                    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        return "星期日";
                    } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                        return "星期一";
                    } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                        return "星期二";
                    } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                        return "星期三";
                    } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                        return "星期四";
                    } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                        return "星期五";
                    } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                        return "星期六";
                    }
                } else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M月d日", Locale.CHINA);
                    return simpleDateFormat.format(date);
                }
            }
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
            return simpleDateFormat.format(date);
        }
        return -1 + "";
    }

    private static boolean isInWeek(Date date) {
        if (isInMonth(date)) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setFirstDayOfWeek(Calendar.SUNDAY);
            int dayOfWeekFromNow = calendar1.get(Calendar.WEEK_OF_MONTH);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar2.setTime(date);
            int dayOfWeekFromDate = calendar2.get(Calendar.WEEK_OF_MONTH);
            return dayOfWeekFromDate == dayOfWeekFromNow;
        }
        return false;
    }

    private static boolean isInMonth(Date date) {
        return isEquals(date, "yyyy-MM");
    }

    private static int formatToMin(long time) {
        return (int) ((System.currentTimeMillis() - time) / 60000);
    }


    private static boolean isInToday(Date date) {
        return isEquals(date, null);
    }

    private static boolean isInYesterday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return isEquals(calendar.getTime(), null);
    }

    private static boolean isInThisYear(Date date) {
        return isEquals(date, "yyyy");
    }

    private static boolean isEquals(Date date, @Nullable String format) {
        if (date == null) {
            return false;
        }
        if (format == null) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        String day = simpleDateFormat.format(date);
        String now = simpleDateFormat.format(new Date());
        return now.equals(day);
    }
}

