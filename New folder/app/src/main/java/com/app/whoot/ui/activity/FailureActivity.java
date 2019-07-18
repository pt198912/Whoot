package com.app.whoot.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.ui.view.popup.StorePopupWindow;
import com.app.whoot.util.PopupWindowUtil;
import com.app.whoot.util.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sunrise on 5/22/2018.
 * 失败页面
 */

public class FailureActivity extends BaseActivity {


      //扫码时该优惠券信息不存在
      public static final int ERROR_SHOP_SCAN_NOTFOUND_COUPON = 1044;
      //扫码时该优惠券有效期过期
      public static final int ERROR_SHOP_SCAN_OVERDUE_COUPON = 1045;
      //扫码时该用户信息不存在
      public static final int ERROR_SHOP_SCAN_USED_COUPON = 1046;
      //该账户优惠券使用处于冷却状态中
      public static final int ERROR_SHOP_SCAN_CD_COUPON = 1047;
      //兑换失败（钻石优惠券受限）！
      public static final int ERROR_SHOP_SCAN_DIAMOND_COUPON = 1048;



    @BindView(R.id.fail_img)
    ImageView failImg;
    @BindView(R.id.fail_txt)
    TextView failTxt;
    @BindView(R.id.fail_fa)
    LinearLayout failFa;
    @BindView(R.id.fail_top)
    TextView fail_top;

    @Override
    public int getLayoutId() {
        return R.layout.activi_failur;
    }

    @Override
    public void onCreateInit() {
        Intent intent = getIntent();
        int code = intent.getIntExtra("code", 0);
        if (code==ERROR_SHOP_SCAN_NOTFOUND_COUPON){
            failTxt.setText(getResources().getString(R.string.coup_one));
        }else if (code==ERROR_SHOP_SCAN_OVERDUE_COUPON){
            failTxt.setText(getResources().getString(R.string.coup_twe));
        }else if (code==ERROR_SHOP_SCAN_USED_COUPON){
            failTxt.setText(getResources().getString(R.string.coup_three));
        }else if (code==ERROR_SHOP_SCAN_CD_COUPON){
            failTxt.setText(getResources().getString(R.string.coup_fore));
        }else if (code==ERROR_SHOP_SCAN_DIAMOND_COUPON){
            failTxt.setText(getResources().getString(R.string.coup_five));
            fail_top.setVisibility(View.VISIBLE);
        }else {
            fail_top.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.fail_img, R.id.fail_fa,R.id.fail_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fail_img:
                break;
            case R.id.fail_fa:
                SPUtil.put(FailureActivity.this,"fai","fai");
                finish();
                break;
            case R.id.fail_top:
                StorePopupWindow popup = new StorePopupWindow(FailureActivity.this);
                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        PopupWindowUtil.setWindowTransparentValue(FailureActivity.this, 1.0f);
                    }
                });
                PopupWindowUtil.setWindowTransparentValue(FailureActivity.this, 0.5f);
                //设置PopupWindow中的位置
                popup.showAtLocation(FailureActivity.this.findViewById(R.id.off_ly), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
        }
    }
}
