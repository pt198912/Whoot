package com.app.whoot.ui.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.FailureActivity;
import com.app.whoot.ui.activity.GiftSuccessActivity;
import com.app.whoot.ui.activity.SuccessActivity;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.view.custom.RoundImage;
import com.app.whoot.util.QRCodeUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Sunny on 2018/1/8.
 */

public class GiftDialog extends Dialog {
    private String a;
    private int b;
    private int c;
    /**
     * 上下文对象 *
     */
    Activity context;

    private Button btn_save;


    private View.OnClickListener mClickListener;
    private ImageButton offer_img;
    private ImageView offer_i;
    private TextView offre_ok;


    Handler mHandler = new Handler();
    private String ab;
    private String couponId;
    private Timer timer;
    private Task task;
    private RoundImage offer_i_on;
    private String giftId;
    private String productImg;
    private String giftshopid;
    private String urlUtil;


    public GiftDialog(Activity context) {
        super(context);
        this.context = context;
    }

    public GiftDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.gift_dialog);

        urlUtil = (String) SPUtil.get(context, "URL", "");
        offer_img = findViewById(R.id.offer_img);
        offer_i = findViewById(R.id.offer_i);
        offre_ok = findViewById(R.id.offre_ok);
        offer_i_on = findViewById(R.id.offer_i_on);

        giftId = (String) SPUtil.get(context, "giftId", "");
        String giftSn = (String) SPUtil.get(context, "giftSn", "");
        String giftUseId = (String) SPUtil.get(context, "giftUseId", "");
        String giftCommodityId = (String) SPUtil.get(context, "giftCommodityId", "");
        String giuserId = (String) SPUtil.get(context, "giuserId", "");
        giftshopid = (String) SPUtil.get(context, "giftshopid", "");
        productImg = (String) SPUtil.get(context, "productImg", "");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("giftId", giftId);
            jsonObject.put("giftSn",giftSn);
            jsonObject.put("giftUseId",giftUseId);
            jsonObject.put("giftCommodityId",giftCommodityId);
            jsonObject.put("userId",giuserId);
            jsonObject.put("shopId", giftshopid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(s, 500, 500);
        offer_i_on.setImageBitmap(mBitmap);

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
        dialogWindow.setAttributes(p);


        // 为按钮绑定点击事件监听器

        offer_img.setOnClickListener(mClickListener);
        this.setCancelable(true);

        timer = new Timer();
        task = new Task();

        offer_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                Timenull();

            }
        });


        //schedule 计划安排，时间表
        timer.schedule(task, 3 * 1000, 5* 1000);

    }

    public class Task extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Http.OkHttpGet(context, urlUtil + UrlUtil.resultgift_new + giftId, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                Log.d("saomiao", result + "===" + urlUtil + UrlUtil.resultgift + giftId);
                                String code = jsonObject.getString("code");

                                if (code.equals("0")) {

                                    JSONObject data = jsonObject.getJSONObject("data");
                                    long giftCd = data.getLong("giftCd");

                                    Intent intent = new Intent(getContext(), GiftSuccessActivity.class);
                                    intent.putExtra("productImg",productImg);
                                    intent.putExtra("giftshopid",giftshopid);
                                    intent.putExtra("giftCd",giftCd+"");
                                    getContext().startActivity(intent);
                                    Timenull();
                                    dismiss();
                                    context.finish();
                                } else if (code.equals("1044")) { //扫码时该优惠券信息不存在
                                    Intent intent = new Intent(getContext(), FailureActivity.class);
                                    intent.putExtra("code", 1044);
                                    getContext().startActivity(intent);
                                    Timenull();
                                    dismiss();

                                } else if (code.equals("1045")) {  //扫码时该优惠券有效期过期
                                    Intent intent = new Intent(getContext(), FailureActivity.class);
                                    intent.putExtra("code", 1045);
                                    getContext().startActivity(intent);
                                    Timenull();
                                    dismiss();

                                } else if (code.equals("1046")) {  //扫码时该用户信息不存在
                                    Intent intent = new Intent(getContext(), FailureActivity.class);
                                    intent.putExtra("code", 1046);
                                    getContext().startActivity(intent);
                                    Timenull();
                                    dismiss();
                                } else if (code.equals("1047")) {  //该账户优惠券使用处于冷却状态中
                                    Intent intent = new Intent(getContext(), FailureActivity.class);
                                    intent.putExtra("code", 1047);
                                    getContext().startActivity(intent);
                                    Timenull();
                                    dismiss();
                                } else if (code.equals("1048")) {  //兑换失败（钻石优惠券受限）！

                                    Intent intent = new Intent(getContext(), FailureActivity.class);
                                    intent.putExtra("code", 1048);
                                    getContext().startActivity(intent);
                                    Timenull();
                                    dismiss();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
            });
        }
    }

    private void Timenull() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
}
