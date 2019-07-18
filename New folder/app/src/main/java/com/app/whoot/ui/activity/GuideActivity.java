package com.app.whoot.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.adapter.ViewPagerAdatper;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.TouristsBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.ui.view.custom.DepthPageTransformer;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Sunrise on 4/8/2018.
 * 引导页
 */

public class GuideActivity extends BaseActivity {
    @BindView(R.id.in_viewpager)
    ViewPager inViewpager;
    @BindView(R.id.bt_next)
    Button btNext;
    @BindView(R.id.in_ll)
    LinearLayout inLl;
    @BindView(R.id.iv_light_dots)
    ImageView ivLightDots;

    private List<View> mViewList;
    private ImageView mOne_dot;
    private ImageView mTwo_dot;
    private ImageView mThree_dot;
    private int mDistance;
    private List<TouristsBean> list = new ArrayList<>();
    private View view1;
    private View view2;
    private View view3;

    @Override
    public int getLayoutId() {
        return R.layout.guide_activi;
    }

    @Override
    public void onCreateInit() {
        initData();
        inViewpager.setAdapter(new ViewPagerAdatper(mViewList));
        addDots();
        moveDots();
        inViewpager.setPageTransformer(true, new DepthPageTransformer());
        Location();
    }

    private void moveDots() {
        ivLightDots.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获得两个圆点之间的距离
                mDistance = inLl.getChildAt(1).getLeft() - inLl.getChildAt(0).getLeft();
                ivLightDots.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
            }
        });
        inViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面滚动时小白点移动的距离，并通过setLayoutParams(params)不断更新其位置
                float leftMargin = mDistance * (position + positionOffset);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivLightDots.getLayoutParams();
                params.leftMargin = (int) leftMargin;
                ivLightDots.setLayoutParams(params);
                if (position == 2) {
                    btNext.setVisibility(View.VISIBLE);
                }
                if (position != 2 && btNext.getVisibility() == View.VISIBLE) {
                    btNext.setVisibility(View.GONE);
                }
            }
            @Override
            public void onPageSelected(int position) {
                //页面跳转时，设置小圆点的margin
                float leftMargin = mDistance * position;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivLightDots.getLayoutParams();
                params.leftMargin = (int) leftMargin;
                ivLightDots.setLayoutParams(params);
                if (position == 2) {
                    btNext.setVisibility(View.VISIBLE);
                }
                if (position != 2 && btNext.getVisibility() == View.VISIBLE) {
                    btNext.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addDots() {
        mOne_dot = new ImageView(this);
        mOne_dot.setImageResource(R.drawable.gray_dot);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 40, 0);
        inLl.addView(mOne_dot, layoutParams);
        mTwo_dot = new ImageView(this);
        mTwo_dot.setImageResource(R.drawable.gray_dot);
        inLl.addView(mTwo_dot, layoutParams);
        mThree_dot = new ImageView(this);
        mThree_dot.setImageResource(R.drawable.gray_dot);
        inLl.addView(mThree_dot, layoutParams);
        setClickListener();
    }

    private void setClickListener() {
        mOne_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inViewpager.setCurrentItem(0);
            }
        });
        mTwo_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inViewpager.setCurrentItem(1);
            }
        });
        mThree_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inViewpager.setCurrentItem(2);
            }
        });
    }

    private void initData() {
        mViewList = new ArrayList<View>();
        LayoutInflater lf = getLayoutInflater().from(GuideActivity.this);

        String  luchang= (String) SPUtil.get(this, "luchang", "");
        boolean zh = TimeUtil.isZh(this);
        if (luchang.equals("0")){
            if (zh){
                luchang="3";
            }else {
                luchang="2";
            }
        }
        if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
            view1 = lf.inflate(R.layout.we_fan_indicator, null);
            view2 = lf.inflate(R.layout.we_fan_indicator1, null);
            view3 = lf.inflate(R.layout.we_fan_indicator2, null);
        }else if (luchang.equals("3")){
            view1 = lf.inflate(R.layout.we_indicator1, null);
            view2 = lf.inflate(R.layout.we_indicator2, null);
            view3 = lf.inflate(R.layout.we_indicator3, null);
        }


        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
    }
    private LocationManager lm;//【位置管理】
    private static final int BAIDU_READ_PHONE_STATE = 100;
    /**
     * 获取定位权限  GPS_PROVIDER  NETWORK_PROVIDER
     */
    public void Location() {
        lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean providerEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (ok == false && providerEnabled == false) {//没有开了定位服务

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // 没有权限，申请权限。
                //                    Toast.makeText(getActivity(), "没有权限", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, BAIDU_READ_PHONE_STATE);
            } else {







            }
            // Toast.makeText(getActivity(), "系统检测到未开启GPS定位服务", Toast.LENGTH_SHORT).show();
            /*Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 1315);*/
        }
    }
    @OnClick(R.id.bt_next)
    public void onViewClicked() {

        //跳转主页
         Intent intent = new Intent(GuideActivity.this, MainActivity.class);
         startActivity(intent);
         finish();



    }
}
