package com.app.whoot.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.app.whoot.R;
import com.app.whoot.app.MyApplication;
import com.app.whoot.bean.ActivityBean;
import com.app.whoot.manager.ConfigureInfoManager;
import com.app.whoot.ui.activity.StoreDetailActivity;
import com.app.whoot.ui.view.dialog.NoExchangedTokenDialog;
import com.app.whoot.ui.view.dialog.VerifyCodeFailDialog;

import java.io.Closeable;
import java.util.List;

/**
 * Created by huangziwei on 16-3-8.
 */
public class Util {

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int dp2px(Context context, float dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    public static void saveProperty(SharedPreferences sharedPreferences, String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void saveProperty(SharedPreferences sharedPreferences, String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void saveProperty(SharedPreferences sharedPreferences, String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void clearProperties(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }
    /**
     * 地球半径,单位 km
     */
    private static final double EARTH_RADIUS = 6378.137;
    private static final String TAG = "Util";

    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        // 纬度
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        // 经度
        double lng1 = Math.toRadians(longitude1);
        double lng2 = Math.toRadians(longitude2);
        // 纬度之差
        double a = lat1 - lat2;
        // 经度之差
        double b = lng1 - lng2;
        // 计算两点距离的公式
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        // 弧长乘地球半径, 返回单位: 千米
        s =  s * EARTH_RADIUS;
        Log.d(TAG, "getDistance: "+s);
        return s;
    }
    public static void showErrorMsgDlg(Activity context, String errorMsg, Handler handler ){
        VerifyCodeFailDialog dlg = new VerifyCodeFailDialog(context, errorMsg);
        dlg.show();
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    dlg.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, 1500);
    }
    public static boolean isChineseLanguage(Context context){
        String luchang= (String) SPUtil.get(context, "luchang", "");
        boolean zh = TimeUtil.isZh(context);
        if (luchang.equals("0")) {
            if (zh) {
                luchang = "3";
            } else {
                luchang = "2";
            }
        }
        if (luchang.equals("1") || luchang.equals("2") || luchang.equals("0")) {

            return true;
        } else if (luchang.equals("3")) {
            return false;

        }
        return false;
    }
    public static boolean isEventStarted(long serverTime){
        List<ActivityBean> actList= ConfigureInfoManager.getInstance().getActList();
        boolean showEvent=false;
        if(actList!=null&&actList.size()>0){
            ActivityBean act=actList.get(0);
            if(serverTime<act.getActivityStartTm()||serverTime>=act.getActivityEndTm()){
                showEvent=false;
            }else{
                showEvent=true;
            }
        }else{
            showEvent=false;
        }
        return showEvent;
    }
    public static String getTokenType(int coupId){
        String type="";
        switch (coupId){
            case Constants.TYPE_BROZEN:
                type=MyApplication.getInstance().getString(R.string.Copper);

                break;
            case Constants.TYPE_SILVER:
                type=MyApplication.getInstance().getString(R.string.Silver);

                break;
            case Constants.TYPE_GOLD:
                type=MyApplication.getInstance().getString(R.string.Gold);

                break;
            case Constants.TYPE_DIAMOND:
                type=MyApplication.getInstance().getString(R.string.Diamond);

                break;
        }
        return type;
    }
}
