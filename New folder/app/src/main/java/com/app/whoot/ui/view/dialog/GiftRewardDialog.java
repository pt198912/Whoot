package com.app.whoot.ui.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.facebook.login.widget.LoginButton;


/**
 * Created by Sunrise on 8/13/2018.
 */

public class GiftRewardDialog extends Dialog{

    /**
     * 上下文对象 *
     */
    Activity context;

    private Button btn_save;



    private View.OnClickListener mClickListener;
    private ImageButton vox_finsh;
    private LinearLayout box_ly;
    private LinearLayout gift_rewar_ly;
    private TextView gift_rewar_tx;
    private TextView gift_rewar_txt;
    private LinearLayout gift_frag_facce;
    private LoginButton login_e_button;


    public GiftRewardDialog(Activity context) {
        super(context);
        this.context = context;
    }

    public GiftRewardDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.gift_reward_dialog);


        vox_finsh = findViewById(R.id.vox_finsh);
        gift_rewar_ly = findViewById(R.id.gift_rewar_ly); //背景图片
        gift_rewar_tx = findViewById(R.id.gift_rewar_tx);
        gift_rewar_txt = findViewById(R.id.gift_rewar_txt);
        gift_frag_facce = findViewById(R.id.gift_frag_facce);
        login_e_button = findViewById(R.id.login_e_button);

        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);


        // 为按钮绑定点击事件监听器

        vox_finsh.setOnClickListener(mClickListener);
        gift_frag_facce.setOnClickListener(mClickListener);
        this.setCancelable(true);


    }
}
