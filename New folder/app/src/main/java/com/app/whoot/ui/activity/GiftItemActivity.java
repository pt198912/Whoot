package com.app.whoot.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.view.custom.TimerTextView;
import com.app.whoot.ui.view.dialog.GiftDialog;
import com.app.whoot.ui.view.dialog.OfferDialog;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sunrise on 7/30/2018.
 * 礼包详情页
 */

public class GiftItemActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;
    @BindView(R.id.title_back_img)
    ImageView titleBackImg;
    @BindView(R.id.giftitem_img)
    ImageView giftitemImg;
    @BindView(R.id.giftitem_next)
    Button giftitemNext;
    @BindView(R.id.giftitem_txt)
    TextView giftitemTxt;
    @BindView(R.id.giftitem_add)
    TextView giftitemAdd;
    @BindView(R.id.giftitem_pro)
    TextView giftitemPro;
    @BindView(R.id.giftitem_date)
    TextView giftitemDate;
    @BindView(R.id.gift_item_ly)
    LinearLayout gift_item_ly;
    @BindView(R.id.gift_exc_time)
    TimerTextView TimerTextView;
    @BindView(R.id.gift_item_txt)
    TextView gift_item_txt;


    private GiftDialog offerDialog;
    private String giftId;
    private String giftUseId;
    private String userId;
    private String shopid;
    private String giftSn;
    private String commodityId;
    private String productImg;
    private String shopimg;
    private String shopName;
    private String urlUtil;
    private String timela;


    @Override
    public int getLayoutId() {
        return R.layout.giftitem;
    }

    @Override
    public void onCreateInit() {
        titleBackTitle.setText(getResources().getString(R.string.gift_detail));

        urlUtil = (String) SPUtil.get(GiftItemActivity.this, "URL", "");
        timela = (String) SPUtil.get(GiftItemActivity.this, "timeLag", "0");

        Intent intent = getIntent();
        giftSn = intent.getStringExtra("giftSn");
        shopid = intent.getStringExtra("shopid");
        String expiredTm = intent.getStringExtra("expiredTm");
        userId = intent.getStringExtra("userId");
        giftUseId = intent.getStringExtra("giftUseId");
        String type = intent.getStringExtra("type");
        giftId = intent.getStringExtra("giftId");

        try{
            if (expiredTm!=null){
                String timedate = TimeUtil.timeUTCdate(Long.parseLong(expiredTm)+Long.parseLong(timela));
                giftitemDate.setText(" "+timedate);
            }
        }catch (Exception e){
            giftitemDate.setText("");
        }



        Http.OkHttpGet(this, urlUtil + UrlUtil.detail+"?shopId="+ shopid +"&type="+type, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    long serverTime = jsonObject.getLong("serverTime");
                    if (jsonObject.getString("code").equals("0")){
                         JSONObject data = jsonObject.getJSONObject("data");
                         String address = data.getString("address");
                         productImg = data.getString("productImg");
                         commodityId = data.getString("commodityId");
                         String productName = data.getString("productName");
                         shopName = data.getString("shopName");
                         shopimg = data.getString("shopImg");
                         giftitemTxt.setText(" "+ shopName);
                         giftitemAdd.setText(" "+address);
                         giftitemPro.setText(" "+productName);



                         Glide.with(GiftItemActivity.this)
                                 .load(productImg)
                                 .centerCrop()
                                 .skipMemoryCache(true)
                                 .error(R.drawable.scanning_success_pic_sharegift)
                                 .into(giftitemImg);

                     }else {
                         //Toast.makeText(GiftItemActivity.this, getResources().getString(R.string.fan), Toast.LENGTH_SHORT).show();
                         ToastUtil.showgravity(GiftItemActivity.this, getResources().getString(R.string.fan));
                     }

                    /**
                     * 判断礼包是否可用
                     * */
                    String downco = (String) SPUtil.get(GiftItemActivity.this, "downco", "0");
                    long aLong = Long.parseLong(downco);
                    long l2 = aLong - serverTime;

                    if (l2>0){
                        giftitemNext.setBackgroundResource(R.drawable.giftdetail_btn_unavailable);
                        gift_item_ly.setVisibility(View.VISIBLE);
                        giftitemNext.setClickable(false);
                        String format = DateUtil.ms2HMS((int) l2);
                        String[] split = format.split(":");
                        String s = split[0];
                        String s1 = split[1];
                        String s2 = split[2];
                        long[] times = {0, Long.parseLong(s), Long.parseLong(s1), Long.parseLong(s2)};
                        TimerTextView.setTimes(l2 / 1000);
                        TimerTextView.beginRun();

                    }else {
                        giftitemNext.setBackgroundResource(R.drawable.bootpage_btn);
                        giftitemNext.setClickable(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

        TimerTextView.setListener(new TimerTextView.onListener() {
            @Override
            public void OnListener(String code) {
                if (code.equals("0")){
                    giftitemNext.setBackgroundResource(R.drawable.bootpage_btn);
                    giftitemNext.setClickable(true);
                    gift_item_ly.setVisibility(View.GONE);
                }
            }
        });
    }



    @OnClick({R.id.title_back_fl, R.id.giftitem_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.giftitem_next:
                SPUtil.put(this,"giftId",giftId);
                SPUtil.put(this,"giftSn",giftSn);
                SPUtil.put(this,"giftUseId",giftUseId);
                SPUtil.put(this,"giftCommodityId",commodityId);
                SPUtil.put(this,"giuserId",userId);
                SPUtil.put(this,"giftshopid",shopid);
                SPUtil.put(this,"productImg",shopimg);
                SPUtil.put(this,"shopname",shopName);

                offerDialog = new GiftDialog(this, R.style.box_dialog, onClickListener);
                offerDialog.show();

                break;
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.offer_img:  //确认

                    offerDialog.dismiss();

                    break;

            }
        }
    };
}
