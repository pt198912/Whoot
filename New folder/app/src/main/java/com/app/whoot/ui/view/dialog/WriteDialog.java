package com.app.whoot.ui.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.app.whoot.R;
import com.app.whoot.ui.activity.WebViewActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.util.SPUtil;

public class WriteDialog extends Dialog {
    /**
     * 上下文对象 *
     */
    Activity context;

    private ImageButton vox_finsh;

    public WriteDialog(Activity context) {
        super(context);
        this.context = context;
    }
    public WriteDialog(Activity context, int theme) {
        super(context, theme);
        this.context = context;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.write_dialog);
        vox_finsh = findViewById(R.id.vox_finsh);
        LinearLayout token_dialog_ch = findViewById(R.id.write_dialog_ch);

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


        this.setCancelable(true);
        token_dialog_ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("su_token",1);
                context.startActivity(intent);
                dismiss();
            }
        });

        vox_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

    }

}
