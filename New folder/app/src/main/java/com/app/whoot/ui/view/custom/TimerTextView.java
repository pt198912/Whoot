package com.app.whoot.ui.view.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Sunrise on 6/14/2018.
 */

public class TimerTextView extends TextView implements Runnable {

    private View.OnClickListener mClickListener;
    private String strTime;

    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub

    }

    private long mday, mhour, mmin, msecond;//天，小时，分钟，秒
    private boolean run=false; //是否启动了
    private int flag=0;
    private long midTime;
    private int time=0;
    private static final String TAG = "TimerTextView";
   /* public void setTimes(long[] times) {
        mday = times[0];
        mhour = times[1];
        mmin = times[2];
        msecond = times[3];

    }*/
   public void setTimes(long Time) {
       this.midTime = Time;


   }

    /**
     * 倒计时计算
     */
    private void ComputeTime() {
     /*   msecond--;
        if (msecond < 0) {
            mmin--;
            msecond = 59;
            if (mmin < 0) {
                mmin = 59;
                mhour--;
                if (mhour < 0) {
                    // 倒计时结束，一天有24个小时
                    run=false;
                    //mday--;

                }
            }

        }*/
        if (midTime>0){
            midTime--;
            mhour= midTime / 60 / 60 % 60;
            mmin = midTime / 60 % 60;
            msecond = midTime % 60;

        }




    }

    public boolean isRun() {
        return run;
    }

    public void beginRun() {
        this.run = true;
        run();
    }

    public void stopRun(){
        this.run = false;
//        run();
        removeCallbacks(this);
    }
    public int endRun(){
        flag=1;
        return flag;
    }

    @Override
    public void run() {
        ComputeTime();
        Log.d(TAG, "run: ComputeTime");
        //标示已经启动
        if(run){

            Log.d(TAG, "run: true");

            if (mhour==0&&mmin==0&&msecond==0){
                String strTime= "00:"+"00:"+"00";
                Log.d("dabuguowoba0",strTime);
                time=1;
                this.setText(strTime);
                run=false;
            }else {
                if (msecond<10){

                    if (mmin>10){
                        strTime = "0"+mhour+":"+ mmin+":0"+msecond+"";
                    }else {
                        strTime= "0"+mhour+":0"+ mmin+":0"+msecond+"";
                    }

                    Log.d("dabuguowoba3",strTime);
                    this.setText(strTime);

                }else if (mmin<10){
                    String strTime= "0"+mhour+":0"+ mmin+":"+msecond+"";
                    Log.d("dabuguowoba2",strTime);
                    this.setText(strTime);
                }else if (mhour<10){

                    String strTime= "0"+mhour+":"+mmin+":"+msecond+"";
                    Log.d("dabuguowoba1",strTime);
                    this.setText(strTime);
                }else {
                    String strTime= mhour+":"+ mmin+":"+msecond+"";
                    Log.d("dabuguowoba4",strTime);
                    this.setText(strTime);
                }

            }

            postDelayed(this, 1000);


        }else {
            Log.d(TAG, "run: false");
            removeCallbacks(this);
            String strTime= "00:"+ "00:"+"00";
          //  this.setText(strTime);
            if (time==1){

                /**
                 * 在合适的位置给其调用接口，给其赋值
                 */
                if (listener!=null) {
                    listener.OnListener("0");
                }
            }

        }
    }

    /**
     * 定义一个接口
     */
    public interface   onListener{
        void OnListener(String code);
    }

    /**
     *定义一个变量储存数据
     */
    private  onListener listener;

    /**
     *提供公共的方法,并且初始化接口类型的数据
     */
    public void setListener( onListener listener){
        this.listener =  listener;
    }


}
