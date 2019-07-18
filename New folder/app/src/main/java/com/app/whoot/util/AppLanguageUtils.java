package com.app.whoot.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.app.whoot.app.MyApplication;

import java.util.Locale;




public class AppLanguageUtils {
    private static final String TAG = "AppLanguageUtils";
    public static void setLanguage(Context context,Locale language,String lanCode){
        forceLocale(context,language);
        MyApplication.getInstance().setLanguage(language);
        Log.d(TAG, "setLanguage: "+language.toString());
        MyApplication.getInstance().getLanguageHelper().setValue("lan",lanCode);
    }


    public static void forceLocale(Context context, Locale locale) {
        if(context==null){
            return;
        }
        Configuration conf = context.getResources().getConfiguration();
        updateConfiguration(conf, locale);
        context.getResources().updateConfiguration(conf,  context.getResources().getDisplayMetrics());

        Configuration systemConf = Resources.getSystem().getConfiguration();
        updateConfiguration(systemConf, locale);
        Resources.getSystem().updateConfiguration(conf,  context.getResources().getDisplayMetrics());

        Locale.setDefault(locale);

    }

    public static void updateConfiguration(Configuration conf, Locale locale) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            conf.setLocale(locale);
        }else {
            //noinspection deprecation
            conf.locale = locale;
        }
    }

}
