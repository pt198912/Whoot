package com.app.whoot.ui.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.FailureActivity;
import com.app.whoot.ui.activity.SuccessActivity;
import com.app.whoot.util.QRCodeUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Sunny on 2018/1/8.
 */

public class CreaDialog extends Dialog {
    private String a;
    private int b;
    private int c;
    /**
     * 上下文对象 *
     */
    Activity context;

    private Button btn_save;




    private Animation mAnimation;


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




    public CreaDialog(Activity context) {
        super(context);
        this.context = context;
    }

    public CreaDialog(Activity context, int theme) {
        super(context, theme);
        this.context = context;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.clearldialog);
        TextView iv_text = findViewById(R.id.iv_text);
        ImageView iv_progress = findViewById(R.id.iv_progress);
        mAnimation = AnimationUtils.loadAnimation(context, R.anim.set_rorate_loading);//给图片添加动态效果
        Animation animation = iv_progress.getAnimation();
        if (animation != null) {
            animation.start();
        } else {
            iv_progress.startAnimation(mAnimation);
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

        String creaDia = (String) SPUtil.get(context, "proDia", "");
        if (creaDia.equals("1")){
            iv_text.setText(context.getResources().getString(R.string.pro_saving));
        }else if (creaDia.equals("2")){
            iv_text.setText(context.getResources().getString(R.string.write_one));
        }
        SPUtil.remove(context,"proDia");

        // 为按钮绑定点击事件监听器


        this.setCancelable(true);
        mHandler.postDelayed(r, 100);//延时100毫秒

    }
}
