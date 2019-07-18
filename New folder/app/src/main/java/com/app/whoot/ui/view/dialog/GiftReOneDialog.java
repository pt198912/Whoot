package com.app.whoot.ui.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.facebook.login.widget.LoginButton;

/**
 * Created by Sunrise on 8/13/2018.
 */

public class GiftReOneDialog extends Dialog {
    /**
     * 上下文对象 *
     */
    Activity context;

    private View.OnClickListener mClickListener;
    private ImageButton vox_finsh;


    public GiftReOneDialog(Activity context) {
        super(context);
        this.context = context;
    }

    public GiftReOneDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.gift_one_dialog);


        vox_finsh = findViewById(R.id.vox_finsh);


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

        this.setCancelable(true);
    }
}
