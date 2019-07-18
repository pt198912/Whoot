package com.app.whoot.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

    private DateUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /*
     * 根据不同的dateType 返回不同的日期格式
     *
     * @param dateType
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static DateFormat getDateFormat(int dateType) {
        switch (dateType) {
            case 0:
                return new SimpleDateFormat("yyyyMMdd");
            case 1:
                return new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            case 2:
                return new SimpleDateFormat("yyyy-MM-dd");
            case 3:
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            case 4:
                return new SimpleDateFormat("yyyy/MM/dd");
            case 5:
                return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        }
        return SimpleDateFormat.getDateTimeInstance();
    }

    /*
     * 获取当前的日期 20171118
     *
     * @return
     */
    public static String getCurrentDayDate(DateFormat dateFormat) {
        return dateFormat.format(new Date());
    }

    /*
     * 获取前一天的日期 20171117  2017-11-17  2017/11/17
     *
     * @param currentDayDate
     * @param dateFormat
     */
    public static String getPreDayDate(String currentDayDate, DateFormat dateFormat) {
        if (TextUtils.isEmpty(currentDayDate)) {
            currentDayDate = getCurrentDayDate(dateFormat);
        }
        Calendar calendar = Calendar.getInstance();
        if (currentDayDate.contains("/") || currentDayDate.contains("-")) {
            calendar.set(Integer.parseInt(currentDayDate.substring(0, 4)),
                    Integer.parseInt(currentDayDate.substring(5, 7)),
                    Integer.parseInt(currentDayDate.substring(8, 10)));
        } else {
            calendar.set(Integer.parseInt(currentDayDate.substring(0, 4)),
                    Integer.parseInt(currentDayDate.substring(4, 6)),
                    Integer.parseInt(currentDayDate.substring(6, 8)));
        }
        return dateFormat.format(calendar.getTime());
    }



    /**
     * 描述：判断网络是否有效.
     *
     * @return true, if is network available
     */
    public static boolean isNetworkAvailable(Context context) {
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();

            if (info != null && info.isAvailable()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            // TODO: handle exception

            return false;
        }
    }
  //将毫秒转换为小时：分钟：秒格式
    public static String ms2HMS(int _ms){
        String HMStime;
        _ms/=1000;
        int hour=_ms/3600;
        int mint=(_ms%3600)/60;
        int sed=_ms%60;
        String hourStr=String.valueOf(hour);
        if(hour<10){
            hourStr="0"+hourStr;
        }
        String mintStr=String.valueOf(mint);
        if(mint<10){
            mintStr="0"+mintStr;
        }
        String sedStr=String.valueOf(sed);
        if(sed<10){
            sedStr="0"+sedStr;
        }
        HMStime=hourStr+":"+mintStr+":"+sedStr;
        return HMStime;
    }
    public static long getMinutess(String time){
        long minutes =0;
        Date d2 = new Date(System.currentTimeMillis());//你也可以获取当前时间
        long diff = d2.getTime()/1000- Long.parseLong(time);
        minutes =diff/(60);
        return minutes ;
    }
    //去除字符串中空格制表符换行
    public static String replaceBlank(String src) {
        String dest = "";
        if (src != null) {
            Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
            Matcher matcher = pattern.matcher(src);
            dest = matcher.replaceAll("");
        }
        return dest;
    }
    /**
     * 获取小数点后一位
     * */
    public static String formateRate(String rateStr){
        if(rateStr.indexOf(".") != -1){
            //获取小数点的位置
            int num = 0;
            //找到小数点在字符串中的位置,找到返回一个int类型的数字,不存在的话返回 -1
            num = rateStr.indexOf(".");

            String dianAfter = rateStr.substring(0,num+1);//输入100.30,dianAfter = 100.
            String afterData = rateStr.replace(dianAfter,"");//把原字符(rateStr)串包括小数点和小数点前的字符替换成"",最后得到小数点后的字符(不包括小数点)

            //判断小数点后字符的长度并做不同的操作,得到小数点后两位的字符串
            if(afterData.length() < 2){
                afterData = afterData + "0" ;
            }else{
                afterData = afterData;
            }
            //返回元字符串开始到小数点的位置 + "." + 小数点后两位字符
            //return rateStr.substring(0,num) + "." + afterData.substring(0,2);

            //返回
            return afterData.substring(0,1);
        }else{
           return "";
        }
    }
    /**
     * 将double格式化为指定小数位的String，不足小数位用0补全
     *
     * @param v     需要格式化的数字
     * @param scale 小数点后保留几位
     * @return
     */
    public static String roundByScale(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        if(scale == 0){

            return new DecimalFormat("0").format(v);
        }
        String formatStr = "0.";
        for(int i=0;i<scale;i++){
            formatStr = formatStr + "0";
        }
        return new DecimalFormat(formatStr).format(v);
    }
     /**
      *
      * 时间加减
      * */
     //Day:日期字符串例如 2015-3-10  Num:需要减少的天数例如 7
     public static String getDateStr(String day,int Num) {
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         Date nowDate = null;
         try {
             nowDate = df.parse(day);
         } catch (ParseException e) {
             e.printStackTrace();
         }
         //如果需要向后计算日期 -改为+
         Date newDate2 = new Date(nowDate.getTime() + (long)Num * 24 * 60 * 60 * 1000);
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
         String dateOk = simpleDateFormat.format(newDate2);
         return dateOk;
     }


}
