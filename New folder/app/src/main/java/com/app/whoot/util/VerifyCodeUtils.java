package com.app.whoot.util;

import android.content.Context;
import android.util.Log;

import com.app.whoot.R;

public class VerifyCodeUtils {
    private static int MAX_FAIL_COUNT=5;
    private static int mRetryCount=0;
    private static long mFirstTimeStamp;
    private static long mLastTimeStamp;
    private static final String TAG = "VerifyCodeUtils";

    public static void reset(){
        mFirstTimeStamp=0;
        mRetryCount=0;
    }
    public static boolean checkFailTooManyTimesInOneMinute(Context context){
        long currentTime=System.currentTimeMillis();
        boolean res=false;
        if(mRetryCount==0){
            mFirstTimeStamp=currentTime;
        }else if(mRetryCount==MAX_FAIL_COUNT-1){
            mLastTimeStamp=currentTime;
            Log.d(TAG, "checkFailTooManyTimesInOneMinute: mLastTimeStamp "+mLastTimeStamp+",mFirstTimeStamp "+mFirstTimeStamp);
            Log.d(TAG, "checkFailTooManyTimesInOneMinute: mRetryCount "+mRetryCount);
            if(mLastTimeStamp-mFirstTimeStamp<=60*1000){
                res=true;
            }else{
                res=false;
            }
            mRetryCount=0;
        }
        if (res){
            ToastUtil.showgravity(context,context.getResources().getString(R.string.code_wu));
        }
        Log.d(TAG, "checkFailTooManyTimesInOneMinute: res "+res);
        mRetryCount++;
        return res;
    }
}
