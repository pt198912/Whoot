package com.app.whoot.ui.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.app.whoot.R;
import com.app.whoot.ui.activity.ForgetActivity;
import com.app.whoot.ui.activity.login.LoginActivity;

/**
 * Created by Sunrise on 2/15/2019.
 */

public class ResetDialog extends Dialog {

    Activity context;
    private View.OnClickListener mClickListener;

    public ResetDialog(Activity context, int layoutId,View.OnClickListener clickListener) {

        //使用自定义Dialog样式
        super(context, R.style.custom_dialog);
        this.context = context;
        this.mClickListener = clickListener;


        //指定布局
        setContentView(layoutId);
        LinearLayout reset_my_login = findViewById(R.id.reset_my_login);

        reset_my_login.setOnClickListener(mClickListener);
        this.setCancelable(true);
        //点击外部不可消失
        //setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



          /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);

    }
}
