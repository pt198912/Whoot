package com.app.whoot.util;

/**
 * Created by Sunrise on 3/29/2019.
 * 全局捕获异常
 */

import android.content.Context;
import android.util.Log;

public class KqwException implements Thread.UncaughtExceptionHandler {
    private static KqwException myCrashHandler;

    private Context mContext;

    private KqwException(Context context) {
        mContext = context;
    }

    public static synchronized KqwException getInstance(Context context) {
        if (null == myCrashHandler) {
            myCrashHandler = new KqwException(context);
        }
        return myCrashHandler;
    }

    public void uncaughtException(Thread thread, Throwable throwable) {
        long threadId = thread.getId();
        String message = throwable.getMessage();
        String localizedMessage = throwable.getLocalizedMessage();
        Log.i("KqwException", "------------------------------------------------------");
        Log.i("KqwException", "threadId = " + threadId);
        Log.i("KqwException", "message = " + message);
        Log.i("KqwException", "localizedMessage = " + localizedMessage);
        Log.i("KqwException", "------------------------------------------------------");
        throwable.printStackTrace();

        // 重启应用
        mContext.startActivity(mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName()));
        //KO当前程序
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}