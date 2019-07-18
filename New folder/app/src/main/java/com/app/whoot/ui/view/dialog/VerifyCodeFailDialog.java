package com.app.whoot.ui.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;


public class VerifyCodeFailDialog extends Dialog {
    /**
     * 上下文对象 *
     */
    Activity context;
    Handler mHandler = new Handler();
    private String mErrorMsg;

    public VerifyCodeFailDialog(Activity context, String errorMsg) {
        super(context,R.style.box_dialog);
        this.context = context;
        this.mErrorMsg = errorMsg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.dlg_verify_code_fail);
        TextView errorMsgTv = findViewById(R.id.tv_error_msg);
        errorMsgTv.setText(mErrorMsg);

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
        p.dimAmount=0;
        dialogWindow.setAttributes(p);


        // 为按钮绑定点击事件监听器


        this.setCancelable(true);


    }
}
