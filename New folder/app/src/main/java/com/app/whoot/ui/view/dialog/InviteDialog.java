package com.app.whoot.ui.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;

public class InviteDialog extends Dialog {
    private String code;
    Activity context;
    private View.OnClickListener mClickListener;

    public InviteDialog(Activity context, int layoutId,String code, View.OnClickListener clickListener) {

        //使用自定义Dialog样式
        super(context, R.style.custom_dialog);
        this.context = context;
        this.mClickListener = clickListener;
        this.code=code;
        //指定布局
        setContentView(layoutId);
        Button invie_positiveButton = findViewById(R.id.invie_positiveButton);
        Button invie_negativeButton = findViewById(R.id.invie_negativeButton);
        TextView invite_message = findViewById(R.id.invite_message);
        if (code.equals("5900")){
            invite_message.setText(context.getResources().getString(R.string.reward_code));
        }else if (code.equals("5901")){
            invite_message.setText(context.getResources().getString(R.string.invite_limit));
        }else if (code.equals("5902")){
            invite_message.setText(context.getResources().getString(R.string.invite_one));
        }else if (code.equals("5903")){
            invite_message.setText(context.getResources().getString(R.string.invite_cannot));
        }else if (code.equals("5904")){
            invite_message.setText(context.getResources().getString(R.string.reward_code));
        }else if (code.equals("5905")){
            invite_message.setText(context.getResources().getString(R.string.fan));
        }else if (code.equals("5906")){
            invite_message.setText(context.getResources().getString(R.string.reward_code));
        }else if (code.equals("5907")){
            invite_message.setText(context.getResources().getString(R.string.invite_already));
        }else if (code.equals("6000")){
            invite_message.setText(context.getResources().getString(R.string.invite_phono));
            invie_positiveButton.setText(context.getResources().getString(R.string.cancel));
            invie_negativeButton.setText(context.getResources().getString(R.string.invite_bind));
        }if (code.equals("7000")){
            invite_message.setText(context.getResources().getString(R.string.invite_set));
            invie_positiveButton.setText(context.getResources().getString(R.string.invite_no));
            invie_negativeButton.setText(context.getResources().getString(R.string.invite_go));
            invie_positiveButton.setTextSize(12);
            invie_positiveButton.setTextSize(12);
        }
        invie_positiveButton.setOnClickListener(mClickListener);
        invie_negativeButton.setOnClickListener(mClickListener);
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
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);

    }
}
