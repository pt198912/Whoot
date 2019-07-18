package com.app.whoot.util;

import android.content.Context;
import android.util.Log;

import com.app.whoot.R;
import com.app.whoot.modle.http.Http;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Sunrise on 5/3/2018.
 */

public class TimeUtil {
    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年


    /**
     * 返回文字描述的日期
     *
     * @param date
     * @return
     */
    public static String getTimeFormatText(Context context,Date date) {
        if (date == null) {
            return null;
        }
        long time = date.getTime();
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
       /* if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }*/
        if (diff > day) {
            r = (diff / day);
            if (r == 1) {
                return context.getResources().getString(R.string.yest);
            } else if (r > 1) {
                return timedate(time);
            }

        }
        if (diff > hour) {
            r = (diff / hour);
            return r + context.getResources().getString(R.string.ago);
        }

        if (diff > minute) {
            r = (diff / minute);
            return r + context.getResources().getString(R.string.min);
        }
        return context.getResources().getString(R.string.just);
    }
    /**
     * 时间格式化
     * */
    public static String timedate(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        String times = sdr.format(new Date(time));
        return times;
    }

    public static String timedateNomm(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd");

        String times = sdr.format(new Date(time));
        return times;
    }
    /**
     * 时间格式化
     * 营业时间显示，从配置文件拿到时差，时间戳+时差--》0时区
     *
     * */
    public static String timedongba(long time) {




        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdr.setTimeZone(TimeZone.getTimeZone("gmt"));
        Date times = null;

        try {

            times = sdr.parse(sdr.format(new Date(time)));//将本地时间转换为转换时间为东八区
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  sdr.format(new Date(time));
    }

    /**
     * 当地时间---》转utc
     * */
    public static String timeUTCdate(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdr.setTimeZone(TimeZone.getTimeZone("gmt"));
        String times = sdr.format(new Date(time));
        return times;
    }
    /**
     * 当地时间---》转utc
     * */
    public static String timeUTCone(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        sdr.setTimeZone(TimeZone.getTimeZone("gmt"));
        String times = sdr.format(new Date(time));
        return times;
    }
    /**
     * UTC时间 ---> 当地时间
     * @param utcTime   UTC时间
     * @return
     */
    public static String utc2Local(String utcTime) {
        SimpleDateFormat utcFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//UTC时间格式
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//当地时间格式
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return localTime;
    }



    /**
     * 时间格式化
     * */
    public static String timedateone(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String times = sdr.format(new Date(time));
        return times;

    }
    public static String timedateone2(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        String times = sdr.format(new Date(time));
        return times;

    }
    /**
     * 时间格式化
     * */
    public static String timedaNo(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd");
        String times = sdr.format(new Date(time));
        return times;

    }
    /**
     * 与当前时间比较早晚
     *
     * @param time 需要比较的时间
     * @return 输入的时间比现在时间晚则返回true
     */
    public static boolean compareNowTime(String time,String endtime) {
        boolean isDayu = false;

        long time_one = Long.parseLong(time);
        long time_end = Long.parseLong(endtime);
        if (time_one-time_end>0){
            isDayu=true;
        }else {
            isDayu = false;
        }
     /*   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        long time_one = Long.parseLong(time);
        long time_end = Long.parseLong(endtime);
        try {
            long time_twe = time_one / 1000;
            long time_end_l = time_end / 1000;
            Date parse = dateFormat.parse(String.valueOf(time_twe));
            Date parse1 = dateFormat.parse(String.valueOf(time_end_l));

            long diff = parse1.getTime() - parse.getTime();
            if (diff <= 0) {
                isDayu = true;
            } else {
                isDayu = false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        return isDayu;
    }
    /**
     * 比较两个日期相差多久
     * */
    public long dateDiff(String startTime, String endTime, String format) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒

            if (day>=1) {
                return day;
            }else {
                if (day==0) {
                    return 1;
                }else {
                    return 0;
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;

    }

    //获取时分秒对应的秒数
    public static int getHourMinSecond(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);//小时
        int minute=calendar.get(Calendar.MINUTE);//分
        int second=calendar.get(Calendar.SECOND);//秒
        return hour*3600+minute*60+second;
    }
    /**
     * 判断系统语言
     */
    public static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("en"))
            return true;
        else
            return false;
    }

    /**
     * 获取当前时间
     * */
    public static String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
    static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final String TAG = "TimeUtil";
    public static long getExactTime(long originTime,long serverTime){
        try {
            Date date = sdf.parse("1970-01-01 00:00");
            long time = date.getTime();
            long duration = originTime - time;
            Date server = new Date(serverTime);
            String dest = (server.getYear() + 1900) + "-" + (server.getMonth() + 1) + "-" + server.getDate() + " 00:00";
            Date zeroClockTime = sdf.parse(dest);
            Date exactDate=new Date(zeroClockTime.getTime() + duration);
            String target = sdf.format(exactDate);
            return exactDate.getTime();
        }catch (ParseException e){
            e.printStackTrace();
            return -1;
        }
    }
}
