package com.app.whoot.ui.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.FailureActivity;
import com.app.whoot.ui.activity.ResultLoginActivity;
import com.app.whoot.ui.activity.SuccessActivity;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.util.QRCodeUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Sunny on 2018/1/8.
 */

public class BoxDialog extends Dialog {
    /**
     * 上下文对象 *
     */
    Activity context;





    private View.OnClickListener mClickListener;
    private ImageButton vox_finsh;




    private String urlUtil;
    private LinearLayout box_update;
    private LinearLayout box_img_ly;


    public BoxDialog(Activity context) {
        super(context);
        this.context = context;
    }

    public BoxDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.box_dialog);
        urlUtil = (String) SPUtil.get(context, "URL", "");

        vox_finsh = findViewById(R.id.vox_finsh);
        box_update = findViewById(R.id.box_update);
        box_img_ly=findViewById(R.id.box_img_ly);

        boolean upgradeNew = (boolean) SPUtil.get(context, "upgradeNew", false);
        upgradeNew=false;
        if (upgradeNew){
            vox_finsh.setVisibility(View.INVISIBLE);
        }else {
            vox_finsh.setVisibility(View.VISIBLE);
        }

        String  luchang= (String) SPUtil.get(context, "luchang", "");
        boolean zh = TimeUtil.isZh(context);
        if (luchang.equals("0")){
            if (zh){
                luchang="3";
            }else {
                luchang="2";
            }
        }
        if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
            box_img_ly.setBackgroundResource(R.drawable.notice_update_fan);
        }else if (luchang.equals("3")){
            box_img_ly.setBackgroundResource(R.drawable.notice_update_en);
        }
        box_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
                if (intent.resolveActivity(context.getPackageManager())!=null){
                    context.startActivity(intent);
                }else {
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+ context.getPackageName()));
                    context.startActivity(intent);
                }
            }
        });



        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度设置为屏幕的0.6
        p.width = WindowManager.LayoutParams.WRAP_CONTENT; // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);


        // 为按钮绑定点击事件监听器

        vox_finsh.setOnClickListener(mClickListener);
        this.setCancelable(true);






    }


}
