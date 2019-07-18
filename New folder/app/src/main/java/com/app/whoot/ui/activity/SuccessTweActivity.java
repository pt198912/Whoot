package com.app.whoot.ui.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.view.dialog.GifDialog;
import com.app.whoot.ui.view.dialog.SuccDialog;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by Sunrise on 5/22/2018.
 * 成功页面
 */

public class SuccessTweActivity extends BaseActivity {
    @BindView(R.id.succ_img_one)
    ImageView succImgOne;
    @BindView(R.id.succ_ly)
    LinearLayout succLy;
    @BindView(R.id.suss_activi)
    LinearLayout sussActivi;
    @BindView(R.id.succ_txt)
    TextView succ_txt;
    @BindView(R.id.succ_type_img)
    ImageView succTypeImg;
    @BindView(R.id.succ_type_txt)
    TextView succTypeTxt;
    @BindView(R.id.succ_type_img_one)
    ImageView succTypeImgOne;
    @BindView(R.id.succ_type_txt_one)
    TextView succTypeTxtOne;
    @BindView(R.id.succ_type_ly_one)
    LinearLayout succTypeLyOne;
    @BindView(R.id.succ_type_img_twe)
    ImageView succTypeImgTwe;
    @BindView(R.id.succ_type_txt_twe)
    TextView succTypeTxtTwe;
    @BindView(R.id.succ_type_ly_twe)
    LinearLayout succTypeLyTwe;
    @BindView(R.id.succ_type_ly)
    LinearLayout succTypeLy;

    private GifDrawable gifDrawDrawable;
    private SuccDialog browsDialog;
    private String shopId;
    private String couponId;

    private int flag = 0;
    private boolean award;
    private String commodityName;
    private String type;
    private String shopid;
    private String giftSn;
    private String expiredTm;
    private String giftUseId;
    private String userId;
    private String id;
    private String shopName;
    private String urlUtil;
    private String timedate;
    private long gifttime;
    private String shopGiftCfgId;

    private String couponCd1;

    @Override
    public int getLayoutId() {
        return R.layout.activi_succ;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            switch (msg.what) {
                case 7:

                    break;
            }
        }
    };

    @Override
    public void onCreateInit() {

        urlUtil = (String) SPUtil.get(SuccessTweActivity.this, "URL", "");
        Intent intent = getIntent();
        shopId = intent.getStringExtra("shopId");
        award = intent.getBooleanExtra("award", false);
        String shopImg = intent.getStringExtra("shopImg");
        couponId = intent.getStringExtra("couponId");
        commodityName = intent.getStringExtra("commodityName");
        shopName = intent.getStringExtra("shopName");
        couponCd1 = intent.getStringExtra("couponCd");
        String giftExpiredTm = intent.getStringExtra("giftExpiredTm");
        shopGiftCfgId = intent.getStringExtra("shopGiftCfgId");
        try {
            gifttime = Long.parseLong(giftExpiredTm);
        } catch (Exception e) {
            gifttime = 0;
        }


        timedate = TimeUtil.timedate(gifttime);
        if (couponCd1 != null) {
            if (couponCd1.length() > 0) {
                SPUtil.put(this, "downco", couponCd1.toString());
            }
        }
        if (shopImg == null) {
            succ_txt.setText(getResources().getString(R.string.fenxiang));
        } else {
            succ_txt.setText(getResources().getString(R.string.share));
        }
        Log.d("susususu", couponId + "");

        GifDialog gifDialog = new GifDialog(this, R.style.dialog_one);
        gifDialog.show();

    }

    @OnClick({R.id.succ_img_one, R.id.succ_ly, R.id.suss_activi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.succ_img_one:

                finish();
                break;
            case R.id.succ_ly:

                andro();
                /*gifDrawSuc.setVisibility(View.VISIBLE);
                gifDrawDrawable = (GifDrawable) gifDrawSuc.getDrawable();
                gifDrawDrawable.start();
                gifDrawDrawable.setLoopCount(1);
                int duration = gifDrawDrawable.getDuration();//获取播放一次所需要的时间*/
               /* new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        Looper.prepare();
                        try {
                            Thread.sleep(*//*duration * *//*100);
                            //   gifDraw.setVisibility(View.GONE);
                            browsDialog = new SuccDialog(SuccessActivity.this, R.style.box_dialog, onClickListener);
                            browsDialog.show();

                            handler.sendEmptyMessage(7);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Looper.loop();
                    }
                }.start();*/
                break;
            case R.id.suss_activi:
                finish();
                break;
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.vox_finsh:  //确认

                    browsDialog.dismiss();

                    break;

                case R.id.box_ly:
                    browsDialog.dismiss();
                    Intent intent = new Intent(SuccessTweActivity.this, GiftItemActivity.class);
                    intent.putExtra("shopid", shopid + "");
                    intent.putExtra("giftSn", giftSn);
                    intent.putExtra("expiredTm", expiredTm + "");
                    intent.putExtra("giftUseId", giftUseId + "");
                    intent.putExtra("type", type + "");
                    intent.putExtra("userId", userId + "");
                    intent.putExtra("giftId", id + "");
                    startActivity(intent);
                    SPUtil.put(SuccessTweActivity.this, "fai", "fai");
                    finish();
                    break;

            }
        }
    };

    public void andro() {

        List<Intent> targetedShareIntents = new ArrayList<Intent>();


        for (int i = 0; i < 10; i++) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            if (award) {
                intent.putExtra(Intent.EXTRA_TEXT, "Check out: \n" +
                        shopName + " \n" + UrlUtil.Share + "shopId=" + shopId + "&locationId=1" + "&couponId=" + couponId + "&shopGiftCfgId=" + shopGiftCfgId + "\nGift will be expired in：" + timedate);
            } else {
                intent.putExtra(Intent.EXTRA_TEXT, "Check out: \n" +
                        shopName + " \n" + UrlUtil.Share + "shopId=" + shopId);
            }

            Log.d("分享的链接", "http://193.112.160.85/user/share.html?shopId=" + shopId + "&locationId=1" + "&couponId=" + couponId);

            intent.setType("text/plain");
            //intent.setType("image/jpeg");
            //intent.putExtra(Intent.EXTRA_TITLE, "标题");
            //intent.putExtra(Intent.EXTRA_SUBJECT, "内容");
            if (i == 0) {
                // intent.setPackage("com.whatsapp");
                intent.setClassName("com.whatsapp", "com.whatsapp.ContactPicker");
            } else if (i == 1) {
                //intent.setPackage("com.instagram.android");
                intent.setClassName("com.instagram.android", "com.instagram.direct.share.handler.DirectShareHandlerActivity");
            } else if (i == 2) {
                intent.setPackage("com.facebook.katana");
                //intent.setClassName("com.samsung.android.voc","com.samsung.android.community.ui.WebFacebookLaunchActivity");
            } else if (i == 3) {
                intent.setPackage("com.facebook.orca");
            } else if (i == 4) {
                //intent.setPackage("jp.naver.line.android");
                intent.setClassName("jp.naver.line.android", "jp.naver.line.android.activity.selectchat.SelectChatActivityLaunchActivity");
            } else if (i == 5) {
                intent.setPackage("com.tencent.mm");
            } else if (i == 6) {
                // intent.setPackage("com.google.android.gm");
                intent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
            } else if (i == 7) {
                intent.setPackage("com.samsung.android.email.provider");
            } else if (i == 8) {
                intent.setPackage("com.samsung.android.messaging");
            } else if (i == 9) {
                intent.setPackage("com.google.android.gm");

            }
            targetedShareIntents.add(intent);
        }
        Intent chooserIntent = null;
        try {
            chooserIntent = Intent.createChooser(Intent.getIntent(""), "Select app to share");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (chooserIntent == null) {
            return;
        }
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));

        // A Parcelable[] of Intent or LabeledIntent objects as set with
        // putExtra(String, Parcelable[]) of additional activities to place
        // a the front of the list of choices, when shown to the user with a
        // ACTION_CHOOSER.
        try {
            startActivity(chooserIntent);
            flag = 1;
        } catch (ActivityNotFoundException ex) {
            //Toast.makeText(this, "Can't find share component to share", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (flag == 1) {


        }

        Log.d("li111111", flag + "  " + award);


    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (award) {
            Http.OkHttpGet(SuccessTweActivity.this, urlUtil + UrlUtil.award + "?locationId=1" + "&shopId=" + shopId + "&couponId=" + couponId, new Http.OnDataFinish() {
                @Override
                public void OnSuccess(String result) {
                    try {

                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        if (code.equals("0")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            boolean giftReceived = data.getBoolean("giftReceived");
                            if (giftReceived) {

                            } else {
                                type = data.getString("type");
                                shopid = data.getString("shopid");
                                giftSn = data.getString("giftSn");
                                expiredTm = data.getString("expiredTm");
                                giftUseId = data.getString("giftUseId");
                                userId = data.getString("userId");
                                id = data.getString("id");

                                if (!giftReceived) {
                                    browsDialog = new SuccDialog(SuccessTweActivity.this, R.style.box_dialog, onClickListener);
                                    browsDialog.show();
                                }
                            }

                        } else {
                            // Toast.makeText(SuccessActivity.this, "失败", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("huodelibao", result + "==" +
                                urlUtil + UrlUtil.award + "?locationId=1" + "&shopId=" + shopId + "&couponId=" + couponId);
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
}
