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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.modle.glide.GlideRoundTransform;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.FailureActivity;
import com.app.whoot.ui.activity.SuccessActivity;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.view.custom.TimerTextView;
import com.app.whoot.util.FormatDuration;
import com.app.whoot.util.QRCodeUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.UrlUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Sunny on 2018/1/8.
 */

public class ExchangeDialog extends Dialog {
    /**
     * 上下文对象 *
     */
    Activity context;

    private Button btn_save;


    private View.OnClickListener mClickListener;
    private ImageButton vox_finsh;
    private ImageView box_img;
    private ImageView exc_pl;
    private TextView exc_txt;
    private TextView exc_num;
    private TextView exc_number;
    private ImageView exc_img;
    private List<String> list = new ArrayList<>();
    private LinearLayout exc_ly,exc_ly_to;
    private TimerTextView exc_time;
    private int fg = 0;
    private Timer timer;
    private Task task;
    private Long aLong;

    Handler mHandler = new Handler();
    private String id;
    private TextView exc_text;
    private long expiredTm;
    private int zuan=0,jin=0,yin=0; //钻石   金   银
    private TextView card_item_exc;
    private String shopImg;
    private String shopId_item;
    private String couponId_item;
    private String userId;
    private String commodityId_item;

    private String urlUtil;

    public ExchangeDialog(Activity context) {
        super(context);
        this.context = context;
    }

    public ExchangeDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.exc_dialog);

        urlUtil = (String) SPUtil.get(context, "URL", "");

        String commodityPic_item = (String) SPUtil.get(context, "commodityPic_item", "");
        String commodityName_item = (String) SPUtil.get(context, "commodityName_item", "");
        String detail_item = (String) SPUtil.get(context, "detail_item", "");
        couponId_item = (String) SPUtil.get(context, "couponId_item", "");
        String maxUseAmout_item = (String) SPUtil.get(context, "maxUseAmout_item", "");
        String usedAmout_item = (String) SPUtil.get(context, "usedAmout_item", "");
        String costs_item = (String) SPUtil.get(context, "costs_item", "");
        shopId_item = (String) SPUtil.get(context, "shopId_item", "");
        commodityId_item = (String) SPUtil.get(context, "commodityId_item", "");
        userId = (String) SPUtil.get(context, "you_usid", "");


        vox_finsh = findViewById(R.id.vox_finsh);
        box_img = findViewById(R.id.box_img);
        exc_pl = findViewById(R.id.exc_pl);
        exc_txt = findViewById(R.id.exc_txt);
        exc_num = findViewById(R.id.exc_num);
        exc_number = findViewById(R.id.exc_number);
        exc_img = findViewById(R.id.exc_img);
        exc_ly = findViewById(R.id.exc_ly);
        exc_ly_to=findViewById(R.id.exc_ly_to);
        exc_time = findViewById(R.id.exc_time);
        exc_text = findViewById(R.id.exc_text);
        card_item_exc = findViewById(R.id.card_item_exc);

        exc_number.setText(usedAmout_item + "/" + maxUseAmout_item);
        exc_num.setText("X" + costs_item);
        exc_txt.setText(detail_item);
        card_item_exc.setText(commodityName_item);


        getExchang();
        exc_time.setListener(new TimerTextView.onListener() {
            @Override
            public void OnListener(String code) {
                if (code.equals("0")){


                    getExch();
                }
            }
        });


        Glide.with(context)
                .load(commodityPic_item)
                .dontAnimate()

                .transform(new CenterCrop(context), new GlideRoundTransform(context, 10))
                .into(exc_pl);


        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.7); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);


        // 为按钮绑定点击事件监听器

        vox_finsh.setOnClickListener(mClickListener);
        this.setCancelable(true);

        timer = new Timer();
        task = new Task();

        vox_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Timenull();
                zuan=0;
                jin=0;
                yin=0;
            }
        });
        //schedule 计划安排，时间表
        timer.schedule(task, 3 * 1000, 5 * 1000);
    }

    public class Task extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Http.OkHttpGet(context, urlUtil + UrlUtil.couponId + id, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                Log.d("saomiao", result + "===" + urlUtil + UrlUtil.couponId + id);
                                String code = jsonObject.getString("code");
                                if (code.equals("0")) {
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    String commodityName = data.getString("commodityName");
                                    int couponCost = data.getInt("couponCost");
                                    int couponLevel = data.getInt("couponLevel");
                                    String shopName = data.getString("shopName");
                                    long useTm = data.getLong("useTm");
                                    boolean award = data.getBoolean("award");
                                    if (award){

                                    }else {
                                        try{
                                            shopImg = data.getString("shopImg");
                                        }catch (Exception e){
                                            shopImg="";
                                        }


                                    }
                                    Intent intent = new Intent(getContext(), SuccessActivity.class);
                                    intent.putExtra("commodityName", commodityName);
                                    intent.putExtra("couponCost", couponCost);
                                    intent.putExtra("couponLevel", couponLevel);
                                    intent.putExtra("shopName", shopName);
                                    intent.putExtra("useTm", useTm);
                                    intent.putExtra("award",award);
                                    intent.putExtra("shopId",shopId_item);
                                    intent.putExtra("shopImg",shopImg);
                                    intent.putExtra("couponId",id);
                                    getContext().startActivity(intent);

                                    SPUtil.put(getContext(),"exchange","1");
                                    Timenull();
                                    dismiss();
                                } else if (code.equals("1044")) { //扫码时该优惠券信息不存在
                                    Intent intent = new Intent(getContext(), FailureActivity.class);
                                    intent.putExtra("code", 1044);
                                    getContext().startActivity(intent);
                                    Timenull();
                                    dismiss();

                                } else if (code.equals("1045")) {  //扫码时该优惠券信息不存在
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

    private void getExchang(){
        Http.OkHttpGet(context, urlUtil+ UrlUtil.coupon, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                Log.d("sdfsdfdg",result);
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject data = object.getJSONObject("data");
                    JSONArray coupons = data.getJSONArray("coupons");
                    long serverTime = object.getLong("serverTime");

                    String downco = (String) SPUtil.get(context, "downco", "0");
                    aLong = Long.parseLong(downco);
                    /**
                     * 2小时倒计时
                     * */
                    long time_len = 7200000;
                    long l = serverTime - aLong;
                    long l2 = time_len - l;
                    for (int i = 0; i < coupons.length(); i++) {

                        JSONObject o = (JSONObject) coupons.get(i);
                        id = o.getString("id");
                        String couponId1 = o.getString("couponId");
                        if (couponId1.equals("1")){
                            expiredTm=4102373532000L;
                        }else {
                            expiredTm = o.getLong("expiredTm");
                        }
                        boolean nowTime = TimeUtil.compareNowTime(String.valueOf(expiredTm), String.valueOf(serverTime));
                        if (nowTime) {
                            String couponSn = o.getString("couponSn");
                            if (couponId1.equals("2")){
                                yin++;
                            }else if (couponId1.equals("3")){
                                jin++;
                            }else if (couponId1.equals("4")){
                                zuan++;
                            }
                            if (fg == 1) {
                                break;
                            }
                            if (l2 > 0) {
                                Log.d("oojojo", "zhixing2");
                                exc_ly.setVisibility(View.VISIBLE);
                                exc_time.setVisibility(View.VISIBLE);
                                exc_text.setVisibility(View.VISIBLE);
                                String format = FormatDuration.format((int) l2);
                                String[] split = format.split(":");
                                String s = split[0];
                                String s1 = split[1];
                                String s2 = split[2];
                                long[] times = {0, Long.parseLong(s), Long.parseLong(s1), Long.parseLong(s2)};
                                exc_time.setTimes(l2 / 1000);
                                exc_time.beginRun();
                                exc_ly_to.setBackgroundResource(R.mipmap.sao_img);
                                fg = 1;
                                break;
                            } else {

                                exc_ly.setVisibility(View.GONE);
                                exc_text.setVisibility(View.GONE);
                            }
                            if (couponId1.equals(couponId_item)) {
                                Log.d("oojojo", "zhixing1");
                                box_img.setVisibility(View.VISIBLE);
                                exc_ly.setVisibility(View.GONE);
                                exc_time.setVisibility(View.GONE);
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("userId", userId);
                                jsonObject.put("couponId", id);
                                jsonObject.put("sn", couponSn);
                                jsonObject.put("couponType", couponId1);
                                jsonObject.put("shopId", shopId_item);
                                String s = jsonObject.toString();
                                Log.d("youhuijuan", s);
                                String a = "userId:" + userId + "&commodityId:" + commodityId_item + "&shopId:" + shopId_item + "&couponType:" + couponId1 + "&Sn:" + couponSn + "&couponId:" + id;
                                Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(s, (int) context.getResources().getDimension(R.dimen.px_250), (int) context.getResources().getDimension(R.dimen.px_250));
                                box_img.setImageBitmap(mBitmap);
                                fg = 1;
                                break;
                            } else {

                                /*box_img.setVisibility(View.GONE);        //二维码图片
                                exc_time.setVisibility(View.VISIBLE);
                                fg = 0;*/
                            }
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (couponId_item.equals("1")) {
                    exc_img.setBackgroundResource(R.drawable.icon_bronze);

                } else if (couponId_item.equals("2")) {
                    exc_img.setBackgroundResource(R.drawable.icon_silver);
                    if (yin==0){
                        box_img.setVisibility(View.GONE);        //二维码图片
                        exc_time.setVisibility(View.VISIBLE);
                        exc_ly.setVisibility(View.VISIBLE);
                        exc_ly_to.setBackgroundResource(R.drawable.sao_img);
                    }

                } else if (couponId_item.equals("3")) {
                    exc_img.setBackgroundResource(R.drawable.icon_gold);
                    if (jin==0){
                        box_img.setVisibility(View.GONE);        //二维码图片
                        exc_time.setVisibility(View.VISIBLE);
                        exc_ly.setVisibility(View.VISIBLE);
                        exc_ly_to.setBackgroundResource(R.drawable.sao_img);
                    }

                } else if (couponId_item.equals("4")) {
                    exc_img.setBackgroundResource(R.drawable.icon_diamond);
                    if (zuan==0){
                        box_img.setVisibility(View.GONE);        //二维码图片
                        exc_time.setVisibility(View.VISIBLE);
                        exc_ly.setVisibility(View.VISIBLE);
                        exc_ly_to.setBackgroundResource(R.drawable.sao_img);
                    }

                }



            }

            @Override
            public void onError(Exception e) {

            }
        });

    }
    private void getExch(){
        Http.OkHttpGet(context, urlUtil+ UrlUtil.coupon, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                Log.d("sdfsdfdg",result);

                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject data = object.getJSONObject("data");
                    JSONArray coupons = data.getJSONArray("coupons");
                    long serverTime = object.getLong("serverTime");

                    String downco = (String) SPUtil.get(context, "downco", "0");
                    aLong = Long.parseLong("0");
                    long time_len = 7200000;
                    long l = serverTime - aLong;
                    long l2 = time_len - l;
                    for (int i = 0; i < coupons.length(); i++) {

                        JSONObject o = (JSONObject) coupons.get(i);
                        id = o.getString("id");
                        String couponId1 = o.getString("couponId");
                        if (couponId1.equals("1")){
                            expiredTm=4102373532000L;
                        }else {
                            expiredTm = o.getLong("expiredTm");
                        }
                        boolean nowTime = TimeUtil.compareNowTime(String.valueOf(expiredTm), String.valueOf(serverTime));
                        if (nowTime) {
                            String couponSn = o.getString("couponSn");
                            if (couponId1.equals("2")){
                                yin++;
                            }else if (couponId1.equals("3")){
                                jin++;
                            }else if (couponId1.equals("4")){
                                zuan++;
                            }

                            if (l2 > 0) {
                                Log.d("oojojo", "zhixing2");
                                exc_ly.setVisibility(View.VISIBLE);
                                exc_time.setVisibility(View.VISIBLE);
                                exc_text.setVisibility(View.VISIBLE);
                                String format = FormatDuration.format((int) l2);
                                String[] split = format.split(":");
                                String s = split[0];
                                String s1 = split[1];
                                String s2 = split[2];
                                long[] times = {0, Long.parseLong(s), Long.parseLong(s1), Long.parseLong(s2)};
                                exc_time.setTimes(l2 / 1000);
                                exc_time.beginRun();
                                exc_ly_to.setBackgroundResource(R.mipmap.sao_img);
                                fg = 1;
                                break;
                            } else {

                                exc_ly.setVisibility(View.GONE);
                                exc_text.setVisibility(View.GONE);
                            }
                            if (couponId1.equals(couponId_item)) {
                                Log.d("oojojo", "zhixing1");
                                box_img.setVisibility(View.VISIBLE);
                                exc_ly.setVisibility(View.GONE);
                                exc_time.setVisibility(View.GONE);
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("userId", userId);
                                jsonObject.put("couponId", id);
                                jsonObject.put("sn", couponSn);
                                jsonObject.put("couponType", couponId1);
                                jsonObject.put("shopId", shopId_item);
                                String s = jsonObject.toString();
                                Log.d("youhuijuan", s);
                                String a = "userId:" + userId + "&commodityId:" + commodityId_item + "&shopId:" + shopId_item + "&couponType:" + couponId1 + "&Sn:" + couponSn + "&couponId:" + id;
                                Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(s, (int) context.getResources().getDimension(R.dimen.px_250), (int) context.getResources().getDimension(R.dimen.px_250));
                                box_img.setImageBitmap(mBitmap);
                                fg = 1;
                                break;
                            } else {

                                /*box_img.setVisibility(View.GONE);        //二维码图片
                                exc_time.setVisibility(View.VISIBLE);
                                fg = 0;*/
                            }
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (couponId_item.equals("1")) {
                    exc_img.setBackgroundResource(R.drawable.icon_bronze);

                } else if (couponId_item.equals("2")) {
                    exc_img.setBackgroundResource(R.drawable.icon_silver);
                    if (yin==0){
                        box_img.setVisibility(View.GONE);        //二维码图片
                        exc_time.setVisibility(View.VISIBLE);
                        exc_ly.setVisibility(View.VISIBLE);
                        exc_ly_to.setBackgroundResource(R.drawable.sao_img);
                    }

                } else if (couponId_item.equals("3")) {
                    exc_img.setBackgroundResource(R.drawable.icon_gold);
                    if (jin==0){
                        box_img.setVisibility(View.GONE);        //二维码图片
                        exc_time.setVisibility(View.VISIBLE);
                        exc_ly.setVisibility(View.VISIBLE);
                        exc_ly_to.setBackgroundResource(R.drawable.sao_img);
                    }

                } else if (couponId_item.equals("4")) {
                    exc_img.setBackgroundResource(R.drawable.icon_diamond);
                    if (zuan==0){
                        box_img.setVisibility(View.GONE);        //二维码图片
                        exc_time.setVisibility(View.VISIBLE);
                        exc_ly.setVisibility(View.VISIBLE);
                        exc_ly_to.setBackgroundResource(R.drawable.sao_img);
                    }

                }



            }

            @Override
            public void onError(Exception e) {

            }
        });

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
