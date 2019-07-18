package com.app.whoot.util;

import android.os.Bundle;
import android.util.Log;

import com.app.whoot.app.MyApplication;
import com.app.whoot.manager.ConfigureInfoManager;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * pt198912
 */
public class FirebaseReportUtils {

    private static volatile FirebaseReportUtils instance;
    private static FirebaseAnalytics mFirebaseAnalytics;
    private String Form;
    private static final String TAG = "FirebaseReportUtils";

    public static FirebaseReportUtils getInstance() {
        if (instance == null) {
            instance = new FirebaseReportUtils();
        }
        return instance;
    }

    private FirebaseReportUtils() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(MyApplication.getInstance());
    }

    /**
     * 埋点
     *
     * @param key
     * @param value
     */
    private void report(String key, Bundle value) {
        if (value == null) {
            value = new Bundle();
        }
        mFirebaseAnalytics.logEvent(key, value);
    }
    public void reportEventWithBaseData(String key, Bundle value) {
        if (value == null) {
            value = new Bundle();
        }
        String Usid = (String) SPUtil.get(MyApplication.getInstance(), "you_usid", "0");
        int type_login = (int) SPUtil.get(MyApplication.getInstance(), "type_login", 0);
        if (type_login==2){
            Form="FB";
        }else if (type_login==3){
            Form="MAIL";
        }else if (type_login==6){
            Form="MOBILE";
        }else{
            Form="";
        }
        value.putString("version",MyApplication.getInstance().getVersionName());
        value.putString("platform","AND");
        value.putString("userId",Usid);
        value.putString("registerFrom",Form);
        long duration=System.currentTimeMillis()-ConfigureInfoManager.getInstance().getLastMobileTime();
        long currentTime=ConfigureInfoManager.getInstance().getLastServerTime()+duration;
        Log.d(TAG, "reportEventWithBaseData: getLastServerTime "+ConfigureInfoManager.getInstance().getLastServerTime());
        value.putLong("createAt",currentTime);
        mFirebaseAnalytics.logEvent(key, value);
    }

    /**
     * 埋点
     *
     * @param key
     */
    public void report(String key) {
        report(key, null);
    }

}
