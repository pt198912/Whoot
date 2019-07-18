package com.app.whoot.ui.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.app.whoot.R;
import com.app.whoot.util.Constants;
import com.app.whoot.util.SPUtil;


public class NoExchangedTokenDialog extends Dialog {

    private String write;
    /**
     * 上下文对象 *
     */
    Activity context;
    private int mTokenType;

    Handler mHandler = new Handler();
    Runnable r = new Runnable() {

        @Override
        public void run() {


            /*getContext().startActivity(new Intent(getContext(),SuccessActivity.class));
            mHandler.removeCallbacks(r);
            dismiss();
            //do something
            //每隔1s循环执行run方法
            mHandler.postDelayed(this, 8000);*/
        }
    };


    public NoExchangedTokenDialog(Activity context, int tokenType, int theme) {
        super(context,theme);
        this.context = context;
        this.mTokenType=tokenType;
    }

    public NoExchangedTokenDialog(Activity context, String write, int theme) {
        super(context, theme);
        this.context = context;
        this.write = write;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.dlg_no_exchanged_token);
        TextView daily_ly = findViewById(R.id.daily_progress);
        TextView tokenIv=findViewById(R.id.iv_token);
//        daily_ly.getBackground().setAlpha(200);
//        TextView daily_txt = findViewById(R.id.daily_txt);
        LinearLayout daily = findViewById(R.id.daily_ly);
//        daily_txt.setText("+1");
       switch (mTokenType){
           case 1:
               daily_ly.setText(context.getResources().getString(R.string.label_not_have)+context.getResources().getString(R.string.label_brozen_token));
//               tokenIv.setImageResource(R.drawable.token_icon_bronze);
               break;
           case 2:
               daily_ly.setText(context.getResources().getString(R.string.label_not_have)+context.getResources().getString(R.string.label_silver_token));
//               tokenIv.setImageResource(R.drawable.token_icon_silver);
               break;
           case 3:
               daily_ly.setText(context.getResources().getString(R.string.label_not_have)+context.getResources().getString(R.string.label_gold_token));
//               tokenIv.setImageResource(R.drawable.token_icon_gold);
               break;
           case 4:
               daily_ly.setText(context.getResources().getString(R.string.label_not_have)+context.getResources().getString(R.string.label_diamond_token));
//               tokenIv.setImageResource(R.drawable.token_icon_diamond);
               break;
       }

        /*
         * 获取窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);


        // 为按钮绑定点击事件监听器


        this.setCancelable(true);
        mHandler.postDelayed(r, 100);//延时100毫秒

    }
}
