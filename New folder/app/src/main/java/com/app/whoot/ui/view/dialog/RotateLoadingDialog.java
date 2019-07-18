package com.app.whoot.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.whoot.R;


public class RotateLoadingDialog extends Dialog {

//    private ImageView ivImage;
    private TextView tvMsg;
    private Context context;
//    private Animation animation;

    public RotateLoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
        this.context = context;
        init();
    }

    private void init() {
        setContentView(R.layout.common_dialog_loading_layout);

        tvMsg = findViewById(R.id.tvMsg);
        WindowManager.LayoutParams wm=(WindowManager.LayoutParams) getWindow().getAttributes();
        wm.alpha=0.8f;
        getWindow().setAttributes(wm);
//        ivImage = findViewById(R.id.ivImage);

//        initAnim();
    }

//    private void initAnim() {
//        animation = AnimationUtils.loadAnimation(context, R.anim.loading_anim1);
//        LinearInterpolator lin = new LinearInterpolator();
//        animation.setInterpolator(lin);
//    }


    @Override
    public void show() {
        super.show();
//        ivImage.startAnimation(animation);
    }

    @Override
    public void hide() {
        super.hide();

    }

    @Override
    public void dismiss() {
        super.dismiss();
//        ivImage.clearAnimation();
    }


    @Override
    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title) && null != tvMsg) {
            tvMsg.setText(title);
        }
    }


}