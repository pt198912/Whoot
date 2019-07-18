package com.app.whoot.ui.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.app.whoot.R;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Sunrise on 8/13/2018.
 */

public class GifDialog extends Dialog {
    /**
     * 上下文对象 *
     */
    Activity context;

    private View.OnClickListener mClickListener;

    private GifImageView openDoorGif;
    private GifDrawable gifDrawDrawable;
    private ProgressBar gif_gif;


    public GifDialog(Activity context, int theme) {
        super(context,theme);
        this.context = context;
    }

    public GifDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.activi_gif);

        openDoorGif = findViewById(R.id.gif_acti);

        gif_gif = findViewById(R.id.gif_gif);


        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();



        gifDrawDrawable = (GifDrawable) openDoorGif.getDrawable();
        gifDrawDrawable.start();
        gifDrawDrawable.setLoopCount(1);
        int duration = gifDrawDrawable.getDuration();//获取播放一次所需要的时间

        mHandler.postDelayed(r, 1650);//延时100毫秒



        this.setCancelable(true);
    }

    Handler mHandler = new Handler();
    Runnable r = new Runnable() {

        @Override
        public void run() {
            //do something
            //每隔1s循环执行run方法
            //mHandler.postDelayed(this, 1000);
           // openDoorGif.setVisibility(View.GONE);
            dismiss();
        }
    };
}
