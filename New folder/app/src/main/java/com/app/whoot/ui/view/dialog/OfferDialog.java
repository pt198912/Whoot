package com.app.whoot.ui.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.bean.TokenExchangeBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.FailureActivity;
import com.app.whoot.ui.activity.SuccessActivity;
import com.app.whoot.ui.activity.SuccessTokenActivity;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.ui.view.custom.RoundImage;
import com.app.whoot.util.Constants;
import com.app.whoot.util.DensityTool;
import com.app.whoot.util.QRCodeUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.UrlUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static com.app.whoot.util.Constants.EVENT_TYPE_MESSAGING;
import static com.app.whoot.util.Constants.EVENT_TYPE_SIGN;
import static com.app.whoot.util.Constants.EVENT_TYPE_TOKEN_EXCHANGE;


/**
 * Created by Sunny on 2018/1/8.
 */

public class OfferDialog extends Dialog {
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
    private String shopImg;
    private String urlUtil;
    private String giftExpiredTm="";
    private String shopGiftCfgId;
    private float mCurrentExchangeProgress;
    private String replaceCouponMap;
    private String couponType;
    private String shopId;

    public OfferDialog(Activity context) {
        super(context);
        this.context = context;
    }

    public OfferDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;

    }

    public void setCurrentExchangeProgress(float currentExchangeProgress) {
        this.mCurrentExchangeProgress = currentExchangeProgress;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.offer_dialog);

        urlUtil = (String) SPUtil.get(context, "URL", "");
        offer_img = findViewById(R.id.offer_img);
        offer_i = findViewById(R.id.offer_i);
        offre_ok = findViewById(R.id.offre_ok);
        offer_i_on = findViewById(R.id.offer_i_on);

        String userId = (String) SPUtil.get(context, "you_usid", "");
        String couponSn = (String) SPUtil.get(context, "couponSn", "");
        couponId = (String) SPUtil.get(context, "couponId", "");
        couponType = (String) SPUtil.get(context, "couponType", "");
        shopId = (String) SPUtil.get(context, "shopId", "");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("couponId", couponId);
            jsonObject.put("sn", couponSn);
            jsonObject.put("couponType", couponType);
            jsonObject.put("shopId",shopId);
            ab = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SPUtil.remove(context,"shopId");

        WindowManager m_wind = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        m_wind.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels - DensityTool.dp2px(context, 10f) * 2; //乘以2是因为左右两侧的宽度
        int height = (int) (width / 280f * 136); //280*136
        Log.d("youyouyou", ab);
        String s = "userId:" + userId + "&couponId:" + couponId + "&couponSn:" + couponSn + "&couponType:" + couponType;
        Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(ab,width, height);
        offer_i_on.setImageBitmap(mBitmap);
        offre_ok.setText(couponSn);
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
       // timer.schedule(task, 3 * 1000, 1000);
        EventBus.getDefault().register(this);

    }
    // 普通事件的处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventBusCarrier carrier) {
        String eventType=carrier.getEventType();
        if(EVENT_TYPE_MESSAGING.equals(eventType)){
            if (couponId==null){
                return;
            }
            Log.d("saomiao1111","9999999999976565");
            Http.OkHttpGet(context, urlUtil + UrlUtil.couponId + couponId, new Http.OnDataFinish() {
                @Override
                public void OnSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        Log.d("saomiao", result + "===" + urlUtil + UrlUtil.couponId + couponId);
                        String code = jsonObject.getString("code");
                        if (code.equals("0")) {
                            Log.d("saomiao1111",result);
                            JSONObject data = jsonObject.getJSONObject("data");
                            String commodityName = data.getString("commodityName");
                            int couponCost = data.getInt("couponCost");
                            int couponLevel = data.getInt("couponLevel");
                            String shopName = data.getString("shopName");
                            long useTm = data.getLong("useTm");
                            long couponCd = data.getLong("couponCd");
                            try{
                                shopGiftCfgId = data.getString("shopGiftCfgId");
                            }catch (Exception e){
                                shopGiftCfgId="";
                            }
                            try{

                                String regainCouponMap = data.getString("regainCouponMap");
                                replaceCouponMap = regainCouponMap.replace("\"", "");
                            }catch (Exception e){

                                replaceCouponMap="{}";
                            }
                            boolean award = data.getBoolean("award");
                            if (award){  //有礼包
                                giftExpiredTm = data.getString("giftExpiredTm");
                            }else {
                                try{
                                    shopImg = data.getString("shopImg");
                                }catch (Exception e){
                                    shopImg="";
                                }
                            }
                            String shopId = data.getString("shopId");
                            Intent intent = new Intent(getContext(), SuccessTokenActivity.class);
                            intent.putExtra("commodityName", commodityName);
                            intent.putExtra("couponCost", couponCost+"");
                            intent.putExtra("couponLevel", couponLevel+"");
                            intent.putExtra("shopName", shopName);
                            intent.putExtra("couponCd", couponCd+"");
                            intent.putExtra("award",award);
                            intent.putExtra("shopId",shopId+"");
                            intent.putExtra("shopImg",shopImg);
                            intent.putExtra("couponId",couponId+"");
                            intent.putExtra("giftExpiredTm",giftExpiredTm);
                            intent.putExtra("shopGiftCfgId",shopGiftCfgId);

                            intent.putExtra("regainCouponMap",replaceCouponMap);
                            intent.putExtra("couponId_item",couponType+"");
                            intent.putExtra("costs_item",couponCost+"");
                            intent.putExtra("commodityName_item",commodityName);
                            intent.putExtra("couponCd", couponCd+"");
                            intent.putExtra("couponType",couponType);


                            Log.d("susususu",couponId+"");
                            getContext().startActivity(intent);
                            Timenull();
                            dismiss();
                            getEventBus();
                        } else if (code.equals("1044")) { //扫码时该优惠券信息不存在
                            Intent intent = new Intent(getContext(), FailureActivity.class);
                            intent.putExtra("code", 1044);
                            getContext().startActivity(intent);
                            Timenull();
                            dismiss();
                            getEventBus();

                        } else if (code.equals("1045")) {  //扫码时该优惠券有效期过期
                            Intent intent = new Intent(getContext(), FailureActivity.class);
                            intent.putExtra("code", 1045);
                            getContext().startActivity(intent);
                            Timenull();
                            dismiss();
                            getEventBus();

                        } else if (code.equals("1046")) {  //扫码时该用户信息不存在
                            Intent intent = new Intent(getContext(), FailureActivity.class);
                            intent.putExtra("code", 1046);
                            getContext().startActivity(intent);
                            Timenull();
                            dismiss();
                            getEventBus();
                        } else if (code.equals("1047")) {  //该账户优惠券使用处于冷却状态中
                            Intent intent = new Intent(getContext(), FailureActivity.class);
                            intent.putExtra("code", 1047);
                            getContext().startActivity(intent);
                            Timenull();
                            dismiss();
                            getEventBus();
                        } else if (code.equals("1048")) {  //兑换失败（钻石优惠券受限）！

                            Intent intent = new Intent(getContext(), FailureActivity.class);
                            intent.putExtra("code", 1048);
                            getContext().startActivity(intent);
                            Timenull();
                            dismiss();
                            getEventBus();
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

    }
    public void getEventBus(){
        EventBus.getDefault().unregister(this);
    }


    public class Task extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (couponId==null){
                        return;
                    }
                    Http.OkHttpGet(context, urlUtil + UrlUtil.couponId + couponId, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                Log.d("saomiao", result + "===" + urlUtil + UrlUtil.couponId + couponId);
                                String code = jsonObject.getString("code");
                                if (code.equals("0")) {
                                    Log.d("saomiao1111",result);
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    String commodityName = data.getString("commodityName");
                                    int couponCost = data.getInt("couponCost");
                                    int couponLevel = data.getInt("couponLevel");
                                    String shopName = data.getString("shopName");
                                    long useTm = data.getLong("useTm");
                                    long couponCd = data.getLong("couponCd");
                                    try{
                                        shopGiftCfgId = data.getString("shopGiftCfgId");
                                    }catch (Exception e){
                                        shopGiftCfgId="";
                                    }
                                    try{

                                        String regainCouponMap = data.getString("regainCouponMap");
                                        replaceCouponMap = regainCouponMap.replace("\"", "");
                                    }catch (Exception e){

                                        replaceCouponMap="{}";
                                    }
                                    boolean award = data.getBoolean("award");
                                    if (award){  //有礼包
                                        giftExpiredTm = data.getString("giftExpiredTm");
                                    }else {
                                        try{
                                            shopImg = data.getString("shopImg");
                                        }catch (Exception e){
                                            shopImg="";
                                        }
                                    }
                                    String shopId = data.getString("shopId");
                                    Intent intent = new Intent(getContext(), SuccessTokenActivity.class);
                                    intent.putExtra("commodityName", commodityName);
                                    intent.putExtra("couponCost", couponCost+"");
                                    intent.putExtra("couponLevel", couponLevel+"");
                                    intent.putExtra("shopName", shopName);
                                    intent.putExtra("couponCd", couponCd+"");
                                    intent.putExtra("award",award);
                                    intent.putExtra("shopId",shopId+"");
                                    intent.putExtra("shopImg",shopImg);
                                    intent.putExtra("couponId",couponId+"");
                                    intent.putExtra("giftExpiredTm",giftExpiredTm);
                                    intent.putExtra("shopGiftCfgId",shopGiftCfgId);

                                    intent.putExtra("regainCouponMap",replaceCouponMap);
                                    intent.putExtra("couponId_item",couponType+"");
                                    intent.putExtra("costs_item",couponCost+"");
                                    intent.putExtra("commodityName_item",commodityName);
                                    intent.putExtra("couponCd", couponCd+"");
                                    intent.putExtra("couponType",couponType);


                                    Log.d("susususu",couponId+"");
                                    getContext().startActivity(intent);
                                    Timenull();
                                    dismiss();
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
