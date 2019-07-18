package com.app.whoot.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.CommodityBean;
import com.app.whoot.bean.TokenExchangeBean;
import com.app.whoot.bean.WorkingTmListBean;
import com.app.whoot.modle.glide.GlideRoundTransform;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.ui.view.custom.TimerTextView;
import com.app.whoot.ui.view.dialog.LoginCoupDialog;
import com.app.whoot.ui.view.dialog.TokenDialog;
import com.app.whoot.util.Constants;
import com.app.whoot.util.FireBaseEventKey;
import com.app.whoot.util.FirebaseReportUtils;
import com.app.whoot.util.FormatDuration;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.QRCodeUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.donkingliang.banner.CustomBanner;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

import static com.app.whoot.util.Constants.EVENT_TYPE_TOKEN_EXCHANGE;

/**
 * Created by Sunrise on 10/17/2018.
 * 优惠券兑换详情
 */

public class CoupDetaiActivity extends BaseActivity {
    @BindView(R.id.coup_head)
    ImageView coupHead;
    @BindView(R.id.frag_item_coup)
    TextView fragItemCoup;
    @BindView(R.id.frag_item_img)
    ImageView fragItemImg;
    @BindView(R.id.coup_txt_time)
    TextView coupTxtTime;
    @BindView(R.id.coup_img)
    ImageView coupImg;
    @BindView(R.id.coup_txt_num)
    TextView coupTxtNum;
    @BindView(R.id.coup_txt_descri)
    TextView coupTxtDescri;
    @BindView(R.id.coup_txt_term)
    TextView coupTxtTerm;
    @BindView(R.id.coup_exc_time)
    TimerTextView coupExcTime;
    @BindView(R.id.coup_exc_ly)
    LinearLayout coupExcLy;
    @BindView(R.id.coup_exc_txt)
    TextView coup_exc_txt;
    @BindView(R.id.coup_txt_tix)
    TextView coup_txt_tix;
    @BindView(R.id.frag_item_woke)
    ImageView frag_item_woke;
    @BindView(R.id.coupdata_head)
    CustomBanner coupdata_head;
    @BindView(R.id.frag_item_text)
    TextView frag_item_text;
    @BindView(R.id.deta_xiang)
    TextView deta_xiang;
    @BindView(R.id.deta_ze)
    TextView deta_ze;
    @BindView(R.id.tv_food_desc)
    TextView foodDescTv;
    @BindView(R.id.iv_expand_food_desc)
    ImageView expandFoodDescIv;
    @BindView(R.id.pb_exchange_progress)
    ProgressBar exchangeProgressPb;
    @BindView(R.id.tv_exchange_progress)
    TextView exchangeProgressTv;
    @BindView(R.id.ll_write_review)
    View writeReviewLayout;
    @BindView(R.id.ll_invite_friend)
    View inviteFriendLayout;
    private String id;

    private long expiredTm;
    private int tong=0,zuan = 0, jin = 0, yin = 0; //钻石   金   银
    private TextView card_item_exc;
    private String shopImg;
    private String shopId_item;
    private String couponId_item;
    private String userId;
    private String commodityId_item;
    private Timer timer;
    private Task task;
    private Long aLong;
    private int fg = 0;
    private int fgly = 0;

    private String urlUtil;
    Handler mHandler = new Handler();
    private String giftExpiredTm = "";
    private String shopGiftCfgId;
    private long expired;
    private String costs_item;
    private String commodityName_item;
    private String regainCouponMap;
    private String couponType;
    private String coupon_id;
    private LoginCoupDialog loginCoupDialog;
    public static CoupDetaiActivity instance;
    private String token;
    private static final String TAG = "CoupDetaiActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activi_deta;
    }

    @Override
    public void onCreateInit() {
        urlUtil = (String) SPUtil.get(this, "URL", "");
        instance=this;


        Intent intent = getIntent();
        ArrayList<String> image = intent.getStringArrayListExtra("image");
        /**
         * 需求暂时取消
         * */
        //setBean(image);
//        getExchang();

        timer = new Timer();
        task = new Task();
        //schedule 计划安排，时间表
        timer.schedule(task, 3 * 1000, 1000);

        coupExcTime.setListener(new TimerTextView.onListener() {
            @Override
            public void OnListener(String code) {
                if (code.equals("0")) {
                    getExch();
                }
            }
        });
        expandFoodDescIv.setTag("collapse");
        expandFoodDescIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag=(String)view.getTag();
                if("expand".equals(tag)){
                    expandFoodDescIv.setImageResource(R.drawable.arrow_down);
                    foodDescTv.setSingleLine(true);
                    view.setTag("collapse");
                }else {
                    expandFoodDescIv.setImageResource(R.drawable.storedetail_arrow_up);
                    foodDescTv.setSingleLine(false);
                    view.setTag("expand");
                }
            }
        });
        writeReviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homePage=new Intent(CoupDetaiActivity.this,MainActivity.class);
                homePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                homePage.putExtra(Constants.EXTRA_KEY_START_MAIN_ACTIVITY, Constants.ACTION_START_MAIN_ACTIVITY_TO_USED_TOKEN_PAGE);
                startActivity(homePage);
            }
        });

    }
    private int mMaxAmount;
    private int mUsedAmount;
    private void updateExchangeProgress(){
        int spareAmount=mMaxAmount-mUsedAmount;
        exchangeProgressPb.setMax(mMaxAmount);
        exchangeProgressPb.setProgress(spareAmount);
        exchangeProgressTv.setText(spareAmount+"/"+mMaxAmount);
    }
    //设置普通指示器
    private void setBean(final ArrayList<String> beans) {
        coupdata_head.setPages(new CustomBanner.ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String entity) {
                Glide.with(context).load(entity).into((ImageView) view);
            }
        }, beans)
                //                //设置指示器为普通指示器
                .setIndicatorStyle(CustomBanner.IndicatorStyle.ORDINARY)
                //                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setIndicatorRes(R.drawable.shape_point_select, R.drawable.shape_point_unselect)
                //                //设置指示器的方向
                .setIndicatorGravity(CustomBanner.IndicatorGravity.CENTER)
                //                //设置指示器的指示点间隔
                .setIndicatorInterval(20)
                //设置自动翻页
                .startTurning(5000);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        getExhah();
    }

    public class Task extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    Log.d("ewrytu", urlUtil + UrlUtil.couponId + id);
                    if (coupon_id == null) {
                        return;
                    }
                    if (token.length() != 0) {
                    Http.OkHttpGet(CoupDetaiActivity.this, urlUtil + UrlUtil.couponId + coupon_id, new Http.OnDataFinish() {
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
                                    try {

                                        regainCouponMap = data.getString("regainCouponMap");
                                    } catch (Exception e) {
                                        shopGiftCfgId = "";
                                        regainCouponMap = "";
                                    }
                                    try {
                                        shopGiftCfgId = data.getString("shopGiftCfgId");
                                    } catch (Exception e) {

                                    }
                                    long useTm = data.getLong("useTm");
                                    long couponCd = data.getLong("couponCd");
                                    boolean award = data.getBoolean("award");
                                    if (award) {
                                        giftExpiredTm = data.getString("giftExpiredTm");
                                    } else {
                                        try {
                                            shopImg = data.getString("shopImg");
                                        } catch (Exception e) {
                                            shopImg = "";
                                        }
                                    }
                                    Intent intent = new Intent(CoupDetaiActivity.this, SuccessTokenActivity.class);

                                    intent.putExtra("commodityName", commodityName);
                                    intent.putExtra("couponCost", couponCost);
                                    intent.putExtra("couponLevel", couponLevel);
                                    intent.putExtra("shopName", shopName);
                                    intent.putExtra("couponCd", couponCd + "");
                                    intent.putExtra("award", award);
                                    intent.putExtra("shopId", shopId_item);
                                    intent.putExtra("shopImg", shopImg);
                                    intent.putExtra("couponId", id);
                                    intent.putExtra("giftExpiredTm", giftExpiredTm + "");
                                    intent.putExtra("shopGiftCfgId", shopGiftCfgId);
                                    intent.putExtra("regainCouponMap", regainCouponMap);

                                    intent.putExtra("couponId_item", couponId_item);
                                    intent.putExtra("costs_item", costs_item);
                                    intent.putExtra("commodityName_item", commodityName_item);
                                    intent.putExtra("couponCd", couponCd + "");
                                    intent.putExtra("couponType", couponType);
                                    startActivity(intent);
                                    SPUtil.put(CoupDetaiActivity.this, "exchange", "1");
                                    Timenull();

                                } else if (code.equals("1044")) { //扫码时该优惠券信息不存在
                                    Intent intent = new Intent(CoupDetaiActivity.this, FailureActivity.class);
                                    intent.putExtra("code", 1044);
                                    startActivity(intent);
                                    Timenull();


                                } else if (code.equals("1045")) {  //扫码时该优惠券信息不存在
                                    Intent intent = new Intent(CoupDetaiActivity.this, FailureActivity.class);
                                    intent.putExtra("code", 1045);
                                    startActivity(intent);
                                    Timenull();


                                } else if (code.equals("1046")) {  //扫码时该用户信息不存在
                                    Intent intent = new Intent(CoupDetaiActivity.this, FailureActivity.class);
                                    intent.putExtra("code", 1046);
                                    startActivity(intent);
                                    Timenull();


                                } else if (code.equals("1047")) {  //该账户优惠券使用处于冷却状态中
                                    Intent intent = new Intent(CoupDetaiActivity.this, FailureActivity.class);
                                    intent.putExtra("code", 1047);
                                    startActivity(intent);
                                    Timenull();


                                } else if (code.equals("1048")) {  //兑换失败（钻石优惠券受限）！
                                    Intent intent = new Intent(CoupDetaiActivity.this, FailureActivity.class);
                                    intent.putExtra("code", 1048);
                                    startActivity(intent);
                                    Timenull();


                                }/*else {
                                    Intent intent = new Intent(CoupDetaiActivity.this, SuccessTokenActivity.class);
                                    intent.putExtra("couponId_item",couponId_item);
                                    intent.putExtra("costs_item",costs_item);
                                    intent.putExtra("commodityName_item",commodityName_item);
                                    intent.putExtra("couponCd", "");
                                    startActivity(intent);
                                    Timenull();

                                }*/
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
            });
        }
    }

    private void getExchang() {
        if (token.length() != 0) {
        Http.OkHttpGet(this, urlUtil + UrlUtil.coupon, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                Log.d("sdfsdfdg2222", result);
                loadCoupDetailInfo();
                long l2 = 0;
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject data = object.getJSONObject("data");
                    JSONArray coupons = data.getJSONArray("coupons");
                    long serverTime = object.getLong("serverTime");

                    String downco = (String) SPUtil.get(CoupDetaiActivity.this, "downco", "0");
                    Log.d("daojishi", downco);
                    aLong = Long.parseLong(downco);
                    //aLong=1541409961000L;
                    /**
                     * 2小时倒计时
                     * */
                    l2 = aLong - serverTime;
                    for (int a = 0; a < coupons.length(); a++) {
                        JSONObject o = (JSONObject) coupons.get(a);
                        String couponId1 = o.getString("couponId");
                        if (couponId1.equals("1")) {
                            expired = 4102373532000L;
                        } else {
                            expired = o.getLong("expiredTm");
                        }
                        //比较过期时间是否过期
                        boolean nowTime = TimeUtil.compareNowTime(String.valueOf(expired), String.valueOf(serverTime));
                        if (nowTime) {
                            if (couponId1.equals("1")) {
                                tong++;
                            } else if (couponId1.equals("2")) {
                                yin++;

                            } else if (couponId1.equals("3")) {
                                jin++;

                            } else if (couponId1.equals("4")) {
                                zuan++;

                            }
                        }
                    }
                    for (int i = 0; i < coupons.length(); i++) {

                        JSONObject o = (JSONObject) coupons.get(i);
                        id = o.getString("id");
                        String couponId1 = o.getString("couponId");
                        if (couponId1.equals("1")) {
                            expiredTm = 4102373532000L;
                        } else {
                            expiredTm = o.getLong("expiredTm");
                        }
                        boolean nowTime = TimeUtil.compareNowTime(String.valueOf(expiredTm), String.valueOf(serverTime));
                        if (nowTime) {
                            String couponSn = o.getString("couponSn");
                            if (l2 > 0) {
                                Log.d("oojojo", "zhixing2" + l2 + "::" + aLong);
                                frag_item_woke.setVisibility(View.GONE);
                                fragItemImg.setVisibility(View.VISIBLE);
                                coupExcTime.setVisibility(View.VISIBLE);
                                coupTxtTime.setVisibility(View.VISIBLE);
                                coupTxtTime.setText(getResources().getString(R.string.shi));
                                String format = FormatDuration.format((int) l2);
                                String[] split = format.split(":");
                                String s = split[0];
                                String s1 = split[1];
                                String s2 = split[2];
                                long[] times = {0, Long.parseLong(s), Long.parseLong(s1), Long.parseLong(s2)};
                                if (!coupExcTime.isRun()) {
                                    coupExcTime.setTimes(l2 / 1000);
                                    coupExcTime.beginRun();
                                }
                                fragItemImg.setBackgroundResource(R.drawable.storedetail_pic_qrcode_none);
                                fg = 1;

                                break;
                            } else {

                            }
                            if (fg == 1) {
                                break;
                            }
                            //判断是否登录
                            String token = (String) SPUtil.get(CoupDetaiActivity.this, "Token", "");

                            if (token.equals("")) {
                                coup_txt_tix.setVisibility(View.GONE);
                                fragItemImg.setBackgroundResource(R.drawable.storedetail_pic_qrcode_none);
                            } else {

                                if (couponId1.equals(couponId_item)) {
                                    Log.d("oojojo", "zhixing1");
                                    fragItemImg.setVisibility(View.VISIBLE);
                                    coupExcLy.setVisibility(View.GONE);
                                    JSONObject jsonObject = new JSONObject();
                                    coupon_id = id;
                                    jsonObject.put("userId", userId);
                                    jsonObject.put("couponId", id);
                                    jsonObject.put("sn", couponSn);
                                    jsonObject.put("couponType", couponId1);
                                    jsonObject.put("shopId", shopId_item);
                                    String s = jsonObject.toString();
                                    Log.d("youhuijuan", s);
                                    coup_txt_tix.setVisibility(View.VISIBLE);
                                    fragItemImg.setBackgroundResource(0);
                                    couponType = couponId1;
//                                    String a = "userId:" + userId + "&commodityId:" + commodityId_item + "&shopId:" + shopId_item + "&couponType:" + couponId1 + "&Sn:" + couponSn + "&couponId:" + id;
                                    Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(s, (int) CoupDetaiActivity.this.getResources().getDimension(R.dimen.px_400), (int) CoupDetaiActivity.this.getResources().getDimension(R.dimen.px_400));
                                    fragItemImg.setImageBitmap(mBitmap);
                                    fg = 1;
                                    break;
                                } else {
                                    coup_txt_tix.setVisibility(View.GONE);
                                /*box_img.setVisibility(View.GONE);        //二维码图片
                                exc_time.setVisibility(View.VISIBLE);
                                fg = 0;*/
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //判断是否登录
                String token = (String) SPUtil.get(CoupDetaiActivity.this, "Token", "");
                if (token.length() > 0) {
                    frag_item_text.setVisibility(View.GONE);
                    if (couponId_item.equals("1")) {
                        coupImg.setBackgroundResource(R.drawable.token_icon_bronze);
                        if (tong == 0) {
                            coupExcTime.setVisibility(View.GONE);
                            coupTxtTime.setText(getResources().getString(R.string.no_bronze));
                            fragItemImg.setBackgroundResource(R.drawable.storedetail_pic_qrcode_none);
                            writeReviewLayout.setVisibility(View.VISIBLE);
                            inviteFriendLayout.setVisibility(View.GONE);

                        }

                    } else if (couponId_item.equals("2")) {
                        coupImg.setBackgroundResource(R.drawable.token_icon_silver);
                        if (yin == 0) {
                            //box_img.setVisibility(View.GONE);        //二维码图片
                            coupExcTime.setVisibility(View.GONE);
                            writeReviewLayout.setVisibility(View.GONE);
                            inviteFriendLayout.setVisibility(View.GONE);
                            coupTxtTime.setText(getResources().getString(R.string.no_silver));
                            fragItemImg.setBackgroundResource(R.drawable.storedetail_pic_qrcode_none);
                        }

                    } else if (couponId_item.equals("3")) {
                        coupImg.setBackgroundResource(R.drawable.token_icon_gold);
                        if (jin == 0) {
                            //box_img.setVisibility(View.GONE);        //二维码图片
                            writeReviewLayout.setVisibility(View.GONE);
                            inviteFriendLayout.setVisibility(View.VISIBLE);
                            inviteFriendLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(CoupDetaiActivity.this,InViteActivity.class));
                                }
                            });
                            coupExcTime.setVisibility(View.GONE);
                            coupTxtTime.setText(getResources().getString(R.string.no_gold));
                            fragItemImg.setBackgroundResource(R.drawable.storedetail_pic_qrcode_none);
                        }

                    } else if (couponId_item.equals("4")) {
                        coupImg.setBackgroundResource(R.drawable.token_icon_diamond);
                        if (zuan == 0) {
                            //box_img.setVisibility(View.GONE);        //二维码图片
                            coupExcTime.setVisibility(View.GONE);
                            writeReviewLayout.setVisibility(View.GONE);
                            inviteFriendLayout.setVisibility(View.GONE);
                            coupTxtTime.setText(getResources().getString(R.string.no_diamond));
                            fragItemImg.setBackgroundResource(R.drawable.storedetail_pic_qrcode_none);
                        }

                    }

                } else {
                    coup_txt_tix.setVisibility(View.GONE);
                    frag_item_text.setVisibility(View.VISIBLE);
                    writeReviewLayout.setVisibility(View.GONE);
                    inviteFriendLayout.setVisibility(View.GONE);
                    fragItemImg.setBackgroundResource(R.drawable.storedetail_pic_qrcode_none);
                    if (couponId_item.equals("1")) {
                        coupImg.setBackgroundResource(R.drawable.token_icon_bronze);

                    } else if (couponId_item.equals("2")) {
                        coupImg.setBackgroundResource(R.drawable.token_icon_silver);
                    } else if (couponId_item.equals("3")) {
                        coupImg.setBackgroundResource(R.drawable.token_icon_gold);
                    } else if (couponId_item.equals("4")) {
                        coupImg.setBackgroundResource(R.drawable.token_icon_diamond);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                loadCoupDetailInfo();
            }
        });
    }else {
            loadCoupDetailInfo();
            coup_txt_tix.setVisibility(View.GONE);
            frag_item_text.setVisibility(View.VISIBLE);
            fragItemImg.setBackgroundResource(R.drawable.storedetail_pic_qrcode_none);
            if (couponId_item!=null&&couponId_item.equals("1")) {
                coupImg.setBackgroundResource(R.drawable.token_icon_bronze);

            } else if (couponId_item.equals("2")) {
                coupImg.setBackgroundResource(R.drawable.token_icon_silver);
            } else if (couponId_item.equals("3")) {
                coupImg.setBackgroundResource(R.drawable.token_icon_gold);
            } else if (couponId_item.equals("4")) {
                coupImg.setBackgroundResource(R.drawable.token_icon_diamond);
            }
        }

    }

    private void getExhah() {
        if (token.length() != 0) {
        Http.OkHttpGet(this, urlUtil + UrlUtil.coupon, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                Log.d("sdfsdfdg", result);

                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject data = object.getJSONObject("data");
                    JSONArray coupons = data.getJSONArray("coupons");
                    long serverTime = object.getLong("serverTime");

                    String downco = (String) SPUtil.get(CoupDetaiActivity.this, "downco", "0");
                    aLong = Long.parseLong(downco);

                    long l2 = aLong - serverTime;
                    for (int i = 0; i < coupons.length(); i++) {

                        JSONObject o = (JSONObject) coupons.get(i);
                        id = o.getString("id");
                        String couponId1 = o.getString("couponId");
                        if (couponId1.equals("1")) {
                            expiredTm = 4102373532000L;
                        } else {
                            expiredTm = o.getLong("expiredTm");
                        }
                        boolean nowTime = TimeUtil.compareNowTime(String.valueOf(expiredTm), String.valueOf(serverTime));
                        if (nowTime) {

                            if (l2 > 0) {
                                Log.d("oojojo", "zhixing298" + l2);

                                fragItemImg.setVisibility(View.GONE);
                                frag_item_woke.setVisibility(View.VISIBLE);
                                coupExcTime.setVisibility(View.VISIBLE);
                                coup_txt_tix.setVisibility(View.GONE);
                                coupTxtTime.setVisibility(View.VISIBLE);
                                coupExcLy.setVisibility(View.VISIBLE);
                                //                                coupExcTime.setText("");
                                coupTxtTime.setText(getResources().getString(R.string.shi));
                                String format = FormatDuration.format((int) l2);
                                String[] split = format.split(":");
                                String s = split[0];
                                String s1 = split[1];
                                String s2 = split[2];
                                fragItemImg.setBackgroundResource(0);
                                long[] times = {0, Long.parseLong(s), Long.parseLong(s1), Long.parseLong(s2)};
                                //                                coupExcTime.setTimes(l2 / 1000);
                                //                                coupExcTime.beginRun();
                                frag_item_woke.setBackgroundResource(R.drawable.storedetail_pic_qrcode_none);

                                break;
                            }

                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }else {
            coup_txt_tix.setVisibility(View.GONE);
            frag_item_text.setVisibility(View.VISIBLE);
            fragItemImg.setBackgroundResource(R.drawable.storedetail_pic_qrcode_none);
        }

    }

    private void loadCoupDetailInfo(){
        FormBody body=new FormBody.Builder()
                .add("shopId", shopId_item)
                .add("couponLevel", couponId_item)
                .build();
        Http.OkHttpPost(this, urlUtil + UrlUtil.COMMODITY_INFO, body,new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                Log.d("COMMODITY_INFO",result+"urlUtil + UrlUtil.COMMODITY_INFO");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    CommodityBean commodityBean=GSonUtil.parseGson(data.toString(), CommodityBean.class);
                    String commodityPic_item = commodityBean.getCommodityPic();
                    Bundle b=new Bundle();
                    b.putString("type","SHOW_REDEEM_INFO");
                    b.putString("from",(String)SPUtil.get(CoupDetaiActivity.this,Constants.SP_KEY_COUP_DETAIL_FROM,""));
                    b.putString("pid",commodityBean.getCommodityId()+"");
                    b.putString("pname",commodityBean.getCommodityName());
                    b.putString("redeemRemain",(commodityBean.getMaxUseAmout()-commodityBean.getUsedAmout())+"");
                    String tokenName="";
                    boolean hasToken=false;
                    switch (couponId_item){
                        case Constants.TYPE_BROZEN+"":
                            tokenName=getString(R.string.Copper);
                            if(tong>0){
                                hasToken=true;
                            }else{
                                hasToken=false;
                            }
                            break;
                        case Constants.TYPE_SILVER+"":
                            tokenName=getString(R.string.Silver);
                            if(yin>0){
                                hasToken=true;
                            }else{
                                hasToken=false;
                            }
                            break;
                        case Constants.TYPE_GOLD+"":
                            tokenName=getString(R.string.Gold);
                            if(jin>0){
                                hasToken=true;
                            }else{
                                hasToken=false;
                            }
                            break;
                        case Constants.TYPE_DIAMOND+"":
                            tokenName=getString(R.string.Diamond);
                            if(zuan>0){
                                hasToken=true;
                            }else{
                                hasToken=false;
                            }
                            break;
                    }
                    b.putString("redeemToken",tokenName);
                    b.putString("redeemCount",commodityBean.getCosts()+"");
                    b.putString("hasToken",hasToken?"TRUE":"FALSE");
                    FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.SHOW_PRODUCT_DETAIL_REDEEM,b);
                    String luchang= (String) SPUtil.get(CoupDetaiActivity.this, "luchang", "");
                    boolean zh = TimeUtil.isZh(CoupDetaiActivity.this);
                    if (luchang.equals("0")) {
                        if (zh) {
                            luchang = "3";
                        } else {
                            luchang = "2";
                        }
                    }
                    if (luchang.equals("1") || luchang.equals("2") || luchang.equals("0")) {
                        commodityName_item = commodityBean.getCommodityName();
                    }else if (luchang.equals("3")) {
                        commodityName_item = commodityBean.getCommodityNameEn();
                    }

                    String detail_item =commodityBean.getDetail();

                    Log.d(TAG, "onResume: couponId_item "+couponId_item);
//                    costs_item = (String) SPUtil.get(this, "costs_item", "");
                    commodityDescribe = commodityBean.getCommodityDescribe();
                    Glide.with(CoupDetaiActivity.this)
                            .load(commodityPic_item)
                            .centerCrop()
                            .skipMemoryCache(true)
                            .into(coupHead);
                    coupTxtNum.setText(" X" + commodityBean.getCosts());
                    if (detail_item!=null&&detail_item.length()>0){
                        deta_ze.setVisibility(View.VISIBLE);
                        coupTxtTerm.setText(detail_item);
                    }else {
                        deta_ze.setVisibility(View.GONE);
                    }
                    coup_exc_txt.setText(commodityName_item);
                    mMaxAmount=commodityBean.getMaxUseAmout();
                    mUsedAmount=commodityBean.getUsedAmout();
                    updateExchangeProgress();
                    updateFoodDescView();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showgravity(CoupDetaiActivity.this,getString(R.string.fail));
            }

        });
    }
    private void getExch() {
        if (token.length() != 0) {
        Http.OkHttpGet(this, urlUtil + UrlUtil.coupon, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                Log.d("sdfsdfdg", result);
                yin = 0;
                jin = 0;
                zuan = 0;
                long l2 = 0;
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject data = object.getJSONObject("data");
                    JSONArray coupons = data.getJSONArray("coupons");
                    long serverTime = object.getLong("serverTime");

                    String downco = (String) SPUtil.get(CoupDetaiActivity.this, "downco", "0");
                    aLong = Long.parseLong(downco);

                    l2 = aLong - serverTime;

                    for (int a = 0; a < coupons.length(); a++) {
                        JSONObject o = (JSONObject) coupons.get(a);
                        String couponId1 = o.getString("couponId");
                        if (couponId1.equals("1")) {
                            expired = 4102373532000L;
                        } else {
                            expired = o.getLong("expiredTm");
                        }
                        boolean nowTime = TimeUtil.compareNowTime(String.valueOf(expired), String.valueOf(serverTime));
                        if (nowTime) {

                            if (couponId1.equals("2")) {
                                yin++;

                            } else if (couponId1.equals("3")) {
                                jin++;

                            } else if (couponId1.equals("4")) {
                                zuan++;

                            }
                        }
                    }

                    for (int i = 0; i < coupons.length(); i++) {

                        JSONObject o = (JSONObject) coupons.get(i);
                        id = o.getString("id");
                        String couponId1 = o.getString("couponId");
                        if (couponId1.equals("1")) {
                            expiredTm = 4102373532000L;
                        } else {
                            expiredTm = o.getLong("expiredTm");
                        }
                        boolean nowTime = TimeUtil.compareNowTime(String.valueOf(expiredTm), String.valueOf(serverTime));
                        if (nowTime) {
                            String couponSn = o.getString("couponSn");


                            if (l2 > 0) {
                                Log.d("oojojo", "zhixing2");
                                frag_item_woke.setVisibility(View.GONE);
                                fragItemImg.setVisibility(View.VISIBLE);
                                coupExcTime.setVisibility(View.VISIBLE);
                                coupTxtTime.setVisibility(View.VISIBLE);
                                coupTxtTime.setText(getResources().getString(R.string.shi));
                                String format = FormatDuration.format((int) l2);
                                String[] split = format.split(":");
                                String s = split[0];
                                String s1 = split[1];
                                String s2 = split[2];
                                long[] times = {0, Long.parseLong(s), Long.parseLong(s1), Long.parseLong(s2)};
                                coupExcTime.setTimes(l2 / 1000);
                                coupExcTime.beginRun();
                                fragItemImg.setBackgroundResource(R.drawable.storedetail_pic_qrcode_none);
                                fg = 1;
                                break;
                            } else {


                            }
                            if (couponId1.equals(couponId_item)) {

                                coupTxtTime.setVisibility(View.GONE);
                                coupExcTime.setVisibility(View.GONE);
                                fragItemImg.setVisibility(View.VISIBLE);
                                frag_item_woke.setVisibility(View.GONE);
                                coup_txt_tix.setVisibility(View.VISIBLE);
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("userId", userId);
                                jsonObject.put("couponId", id);
                                jsonObject.put("sn", couponSn);
                                jsonObject.put("couponType", couponId1);
                                jsonObject.put("shopId", shopId_item);
                                String s = jsonObject.toString();
                                fragItemImg.setBackgroundResource(0);
                                coupon_id = id;
                                couponType = couponId1;
                                String a = "userId:" + userId + "&commodityId:" + commodityId_item + "&shopId:" + shopId_item + "&couponType:" + couponId1 + "&Sn:" + couponSn + "&couponId:" + id;
                                Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(s, (int) CoupDetaiActivity.this.getResources().getDimension(R.dimen.px_400), (int) CoupDetaiActivity.this.getResources().getDimension(R.dimen.px_400));
                                fragItemImg.setImageBitmap(mBitmap);
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
                Log.d("ppt", "OnSuccess: ");
                if (couponId_item.equals("1")) {
                    coupImg.setBackgroundResource(R.drawable.token_icon_bronze);


                } else if (couponId_item.equals("2")) {
                    coupImg.setBackgroundResource(R.drawable.token_icon_silver);
                    if (yin == 0) {

                        coupExcTime.setVisibility(View.GONE);
                        coupTxtTime.setText(getResources().getString(R.string.no_silver));
                        fragItemImg.setBackgroundResource(R.drawable.storedetail_pic_qrcode_none);
                    }

                } else if (couponId_item.equals("3")) {
                    coupImg.setBackgroundResource(R.drawable.token_icon_gold);
                    if (jin == 0) {

                        coupExcTime.setVisibility(View.GONE);
                        coupTxtTime.setText(getResources().getString(R.string.no_gold));
                        fragItemImg.setBackgroundResource(R.drawable.storedetail_pic_qrcode_none);
                    }

                } else if (couponId_item.equals("4")) {
                    coupImg.setBackgroundResource(R.drawable.token_icon_diamond);
                    if (zuan == 0) {

                        coupExcTime.setVisibility(View.GONE);
                        coupTxtTime.setText(getResources().getString(R.string.no_diamond));
                        fragItemImg.setBackgroundResource(R.drawable.storedetail_pic_qrcode_none);
                    }

                }


            }

            @Override
            public void onError(Exception e) {

            }
        });
    }else{
            coup_txt_tix.setVisibility(View.GONE);
            frag_item_text.setVisibility(View.VISIBLE);
            fragItemImg.setBackgroundResource(R.drawable.storedetail_pic_qrcode_none);
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
    private String commodityDescribe;
    private void updateFoodDescView(){
        if (commodityDescribe!=null&&commodityDescribe.length()>0){
            foodDescTv.setText(commodityDescribe);
        }else {
            expandFoodDescIv.setVisibility(View.INVISIBLE);
        }
        foodDescTv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(commodityDescribe!=null&&commodityDescribe.length()>0) {
                    TextPaint textPaint = foodDescTv.getPaint();
                    textPaint.setTextSize(foodDescTv.getTextSize());
                    int textViewWidth = (int) textPaint.measureText(commodityDescribe);
                    if (textViewWidth > foodDescTv.getWidth()) {//超出一行
                        expandFoodDescIv.setVisibility(View.VISIBLE);
                    } else {
                        expandFoodDescIv.setVisibility(View.INVISIBLE);
                    }
                }else{
                    expandFoodDescIv.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        couponId_item = (String) SPUtil.get(CoupDetaiActivity.this, "couponId_item", "");
        shopId_item = (String) SPUtil.get(CoupDetaiActivity.this, "shopId_item", "");
        userId = (String) SPUtil.get(CoupDetaiActivity.this, "you_usid", "");
        token = (String) SPUtil.get(CoupDetaiActivity.this, "Token", "");
//        loadCoupDetailInfo();
        int my_tong = (int) SPUtil.get(CoupDetaiActivity.this, "My_tong", 0);
        if (my_tong!=0){
            loginCoupDialog = new LoginCoupDialog(CoupDetaiActivity.this, R.style.box_dialog, onClickListener);
            loginCoupDialog.show();
            SPUtil.remove(this,"My_tong");
            SPUtil.remove(this,"My_yin");
            SPUtil.remove(this,"My_jin");
            SPUtil.remove(this,"My_zuan");
        }

        boolean showUpgradeToToken = (boolean) SPUtil.get(CoupDetaiActivity.this, "showUpgradeToToken", false);
        if (showUpgradeToToken){

            TokenDialog browsDialog = new TokenDialog(CoupDetaiActivity.this, R.style.box_dialog);
            browsDialog.setCanceledOnTouchOutside(false);
            browsDialog.show();
            browsDialog.setCancelable(false);
            SPUtil.remove(this,"showUpgradeToToken");
        }
        getExchang();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(coupExcTime!=null){
            coupExcTime.stopRun();
        }
        Timenull();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_box_ly:
                    loginCoupDialog.dismiss();
                    EventBusCarrier carrier=new EventBusCarrier();
                    TokenExchangeBean bean=new TokenExchangeBean();
                    bean.setSuToken(1);
                    bean.setFlagToken(2);
                    bean.setFlagCoupn(0);
                    carrier.setEventType(EVENT_TYPE_TOKEN_EXCHANGE);
                    carrier.setObject(bean);
                    EventBus.getDefault().post(carrier);
                    finish();
                    if (StoreDetailActivity.instance!=null){
                        StoreDetailActivity.instance.finish();
                    }
                    break;

            }
        }
    };

    @OnClick({R.id.frag_item_coup, R.id.frag_item_img,R.id.frag_item_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.frag_item_coup:
                Timenull();
                zuan = 0;
                jin = 0;
                yin = 0;
                finish();
                break;
            case R.id.frag_item_img:
                break;
            case R.id.frag_item_text:
                Intent intent = new Intent(CoupDetaiActivity.this, LoginMeActivity.class);
                intent.putExtra("Login_flag",1);
                startActivity(intent);
                break;
        }
    }

}
