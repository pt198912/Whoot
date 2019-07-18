package com.app.whoot.util;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by Sunrise on 6/14/2018.
 */

public class FormatDuration {

    private static StringBuilder mFormatBuilder;
    private static Formatter mFormatter;

    public FormatDuration(){
        //转换成字符串的时间
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    private static String getString(int t) {
        String m = "";
        if (t > 0) {
            if (t < 10) {
                m = "0" + t;
            } else {
                m = t + "";
            }
        } else {
            m = "00";
        }
        return m;
    }


    public static String format(int t) {
        if (t < 60000) {
            return "0:"+"0:"+(t % 60000) / 1000 + "";
        } else if ((t >= 60000) && (t < 3600000)) {
            return "0:"+getString((t % 3600000) / 60000) + ":" + getString((t % 60000) / 1000)+"";
        } else {
            return getString(t / 3600000) + ":" + getString((t % 3600000) / 60000) + ":" + getString((t % 60000) / 1000)+"";
        }
    }

    /**
     * 把毫秒转换成：1：20：30这样的形式
     * @param timeMs
     * @return
     */
    public static String stringForTime(int timeMs){
        int totalSeconds = timeMs/1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds/60)%60;
        int hours = totalSeconds/3600;
        mFormatBuilder.setLength(0);
        if(hours>0){
            return mFormatter.format("%d:%02d:%02d",hours,minutes,seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d",minutes,seconds).toString();
        }
    }
}
