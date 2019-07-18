package com.app.whoot.ui.activity;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.adapter.MyPagerAdapter;
import com.app.whoot.adapter.TabFragmentPagerAdapter;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.ui.fragment.gift.GiftAlreadyFragment;
import com.app.whoot.ui.fragment.gift.GiftNotFragment;
import com.app.whoot.ui.fragment.gift.GiftOverdueFragment;
import com.app.whoot.util.SPUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sunrise on 1/8/2019.
 * 我的礼品
 */

public class BuxActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    String urlUtil;
    @BindView(R.id.gift_unuesd)
    TextView giftUnuesd;
    @BindView(R.id.gift_wei)
    LinearLayout giftWei;
    @BindView(R.id.gift_uesd)
    TextView giftUesd;
    @BindView(R.id.gift_yi)
    LinearLayout giftYi;
    @BindView(R.id.gift_useless)
    TextView giftUseless;
    @BindView(R.id.gift_guo)
    LinearLayout giftGuo;
    @BindView(R.id.gift_tab_ly)
    LinearLayout giftTabLy;
    private MyPagerAdapter pagerAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.bux_activi;
    }

    @Override
    public void onCreateInit() {
        titleBackTitle.setText(getResources().getString(R.string.bux));
        titleBackTxt.setText(getResources().getString(R.string.clear_all));
        urlUtil = (String) SPUtil.get(BuxActivity.this, "URL", "");
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> list_Title = new ArrayList<>();
        fragmentList.add(new GiftNotFragment());
        fragmentList.add(new GiftOverdueFragment());
        fragmentList.add(new GiftAlreadyFragment());

        viewPager.setOnPageChangeListener(new MyPagerChangeListener());
        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);  //初始化显示第一个页面
    }

    @OnClick({R.id.title_back_fl, R.id.title_back_title, R.id.title_back_txt,R.id.gift_wei, R.id.gift_yi, R.id.gift_guo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.title_back_title:
                break;
            case R.id.title_back_txt:
                EventBusCarrier eventBusCarrier = new EventBusCarrier();
                eventBusCarrier.setEventType("1");
                eventBusCarrier.setObject("我是gift_clear发布的事件");
                EventBus.getDefault().post(eventBusCarrier); //普通事件发布
                break;
            case R.id.gift_wei:
                viewPager.setCurrentItem(0);
                break;
            case R.id.gift_yi:
                viewPager.setCurrentItem(1);
                break;
            case R.id.gift_guo:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    /**
     * 设置一个ViewPager的侦听事件，当左右滑动ViewPager时菜单栏被选中状态跟着改变
     */
    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            Log.d("菜单栏被选中状态跟着改变", arg0 + "wo");
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    giftWei.setBackgroundColor(getResources().getColor(R.color.white));
                    giftYi.setBackgroundColor(getResources().getColor(R.color.card_time));
                    giftGuo.setBackgroundColor(getResources().getColor(R.color.card_time));
                    giftUnuesd.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    //设置不为加粗
                    giftUesd.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    giftUseless.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    break;
                case 1:
                    giftWei.setBackgroundColor(getResources().getColor(R.color.card_time));
                    giftYi.setBackgroundColor(getResources().getColor(R.color.white));
                    giftGuo.setBackgroundColor(getResources().getColor(R.color.card_time));

                    //设置不为加粗
                    giftUnuesd.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    giftUesd.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    giftUseless.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    break;
                case 2:
                    giftWei.setBackgroundColor(getResources().getColor(R.color.card_time));
                    giftYi.setBackgroundColor(getResources().getColor(R.color.card_time));
                    giftGuo.setBackgroundColor(getResources().getColor(R.color.white));
                    giftUesd.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    //设置不为加粗
                    giftUnuesd.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    giftUseless.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    break;
            }
        }
    }

}
