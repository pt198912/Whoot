package com.app.whoot.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.adapter.MyFragmentAdapter;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.ui.fragment.CouponsFragment;
import com.app.whoot.ui.fragment.GiftsFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sunrise on 7/31/2018.
 * 兑换记录改版
 */

public class RecordActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;
    @BindView(R.id.title_back_img)
    ImageView titleBackImg;
    @BindView(R.id.record_tab)
    TabLayout recordTab;
    @BindView(R.id.record_pager)
    ViewPager recordPager;


    //private final String[]sTitle=new String[]{"1","2"};
    private List<String> sTitle=new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.record_act;
    }

    @Override
    public void onCreateInit() {
        titleBackTitle.setText(getResources().getString(R.string.exchange));

        sTitle.add(getResources().getString(R.string.copp));
        sTitle.add(getResources().getString(R.string.gift));

        recordTab.addTab(recordTab.newTab().setText(getResources().getString(R.string.copp)));
        recordTab.addTab(recordTab.newTab().setText(getResources().getString(R.string.gift)));

        recordTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        recordTab.setupWithViewPager(recordPager);
        ArrayList<Fragment> list_frag = new ArrayList<>();
        list_frag.add(CouponsFragment.newInstance());
        list_frag.add(GiftsFragment.newInstance());

        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), list_frag, sTitle);
        recordPager.setAdapter(myFragmentAdapter);

        recordPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @OnClick({R.id.title_back_fl, R.id.title_back_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.title_back_title:
                break;
        }
    }
}
