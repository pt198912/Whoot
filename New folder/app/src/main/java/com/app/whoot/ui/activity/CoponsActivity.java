package com.app.whoot.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.util.TimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sunrise on 7/31/2018.
 * 兑换记录详情
 */

public class CoponsActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;
    @BindView(R.id.title_back_img)
    ImageView titleBackImg;
    @BindView(R.id.copps_type)
    TextView coppsType;
    @BindView(R.id.copps_sn)
    TextView coppsSn;
    @BindView(R.id.copps_shop)
    TextView coppsShop;
    @BindView(R.id.copps_time)
    TextView coppsTime;
    @BindView(R.id.copps_name)
    TextView coppsName;
    @BindView(R.id.copps_img)
    ImageView copps_img;
    @BindView(R.id.copps_ly)
    LinearLayout coppsLy;
    @BindView(R.id.gifts_type)
    TextView giftsType;
    @BindView(R.id.gifts_sn)
    TextView giftsSn;
    @BindView(R.id.gifts_shop)
    TextView giftsShop;
    @BindView(R.id.gifts_ly)
    LinearLayout giftsLy;

    @Override
    public int getLayoutId() {
        return R.layout.copons_activi;
    }

    @Override
    public void onCreateInit() {

        titleBackTitle.setText(getResources().getString(R.string.copp_de));
        Intent intent = getIntent();
        String couponId = intent.getStringExtra("couponId");
        String shopName = intent.getStringExtra("shopName");
        String useTm = intent.getStringExtra("useTm");
        String commodityName = intent.getStringExtra("commodityName");
        String difference = intent.getStringExtra("difference");
        if (difference.equals("1")){
            coppsLy.setVisibility(View.VISIBLE);
            giftsLy.setVisibility(View.GONE);
            int coupId = Integer.parseInt(couponId);
            if (coupId == 1) {
                copps_img.setBackgroundResource(R.drawable.rewards_icon_bronze);
                coppsType.setText(getResources().getString(R.string.Copper));
            } else if (coupId == 2) {
                copps_img.setBackgroundResource(R.drawable.rewards_icon_silver);
                coppsType.setText(getResources().getString(R.string.Silver));
            } else if (coupId == 3) {
                copps_img.setBackgroundResource(R.drawable.rewards_icon_gold);
                coppsType.setText(getResources().getString(R.string.Gold));
            } else if (coupId == 4) {
                copps_img.setBackgroundResource(R.drawable.rewards_icon_diamond);
                coppsType.setText(getResources().getString(R.string.Diamond));
            }
            coppsName.setText(commodityName);
            coppsShop.setText(shopName);
            long aLong = Long.parseLong(useTm);
            String timeFormatText = TimeUtil.timedate(aLong);
            coppsTime.setText(timeFormatText);
        }else if (difference.equals("2")){
            coppsLy.setVisibility(View.GONE);
            giftsLy.setVisibility(View.VISIBLE);
        }



    }

    @OnClick({R.id.title_back_fl, R.id.copps_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.copps_name:
                break;
        }
    }

}
