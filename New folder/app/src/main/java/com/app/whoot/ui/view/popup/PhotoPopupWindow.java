package com.app.whoot.ui.view.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.whoot.R;

import java.text.DecimalFormat;

import static com.app.whoot.base.BaseApplication.SCREEN_HEIGHT;


/**
 * Created by Sunrise on 4/2/2018.
 */

public class PhotoPopupWindow extends PopupWindow implements View.OnClickListener {

    private View mPopView;
    private OnItemClickListener mListener;

    private DecimalFormat df = new DecimalFormat("0.00");
    private LinearLayout photo_txt_p;
    private LinearLayout photo_txt_car;
    private LinearLayout photo_txt_no;

    public PhotoPopupWindow(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
        setPopupWindow();
        photo_txt_p.setOnClickListener(this);
        photo_txt_car.setOnClickListener(this);
        photo_txt_no.setOnClickListener(this);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(context);
        //绑定布局
        mPopView = inflater.inflate(R.layout.photo_popup_window, null);
        photo_txt_p = mPopView.findViewById(R.id.photo_txt_p);
        photo_txt_car = mPopView.findViewById(R.id.photo_txt_car);
        photo_txt_no = mPopView.findViewById(R.id.photo_txt_no);
    }

    /**
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        this.setContentView(mPopView);// 设置View
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高

        this.setFocusable(true);// 设置弹出窗口可
        this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明

        mPopView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int height = mPopView.findViewById(R.id.photo_pop_win).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {


                        dismiss();
                    }
                }
                return true;
            }
        });
    }



    /**
     * 定义一个接口，公布出去 在Activity中操作按钮的单击事件
     */
    public interface OnItemClickListener {
        void setOnItemClick(View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (mListener != null) {
            mListener.setOnItemClick(v);
        }
    }

}
