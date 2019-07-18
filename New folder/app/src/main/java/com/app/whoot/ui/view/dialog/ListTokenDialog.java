package com.app.whoot.ui.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.adapter.TokenDialogAdapter;
import com.app.whoot.bean.CouponBean;
import com.app.whoot.ui.view.custom.MyListView;

import java.util.ArrayList;
import java.util.List;

public class ListTokenDialog extends Dialog {
    private Activity context;
    private ListView token_list;
    private ImageButton list_finsh;
    private List<CouponBean.DataBean.CouponsBean> list=new ArrayList<>();
    private static final String TAG = "ListTokenDialog";
    public ListTokenDialog(Activity context, int theme) {
        super(context,theme);
        this.context=context;
        initView();
    }
    private void initView(){
        setContentView(R.layout.dialog_token);
        token_list = findViewById(R.id.token_list);
        list_finsh = findViewById(R.id.list_finsh);

        /*
         * 获取窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.18598048454
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.75); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.85); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);



        list_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
    public void setList(List<CouponBean.DataBean.CouponsBean> list){
        Log.d(TAG, "setList: "+list.size());
        this.list.clear();
        if(list!=null&&list.size()!=0){
            this.list.addAll(list);
        }
        TextView tokenType=findViewById(R.id.tv_token_type);
        if(list!=null&& list.size()>0){
            int coupnId=list.get(0).getCouponId();
            switch (coupnId){
                case 1:
                    tokenType.setText(R.string.label_brozen_token);
                    break;
                case 2:
                    tokenType.setText(R.string.label_silver_token);
                    break;
                case 3:
                    tokenType.setText(R.string.label_gold_token);
                    break;
                case 4:
                    tokenType.setText(R.string.label_diamond_token);
                    break;
            }
        }
        TokenDialogAdapter tokenDialogAdapter = new TokenDialogAdapter(context, list);
        token_list.setAdapter(tokenDialogAdapter);
    }




}
