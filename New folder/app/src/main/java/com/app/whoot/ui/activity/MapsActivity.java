package com.app.whoot.ui.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.util.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;
    @BindView(R.id.title_back_shou)
    ImageView titleBackShou;
    @BindView(R.id.title_back_no)
    ImageView titleBackNo;
    @BindView(R.id.title_back_img)
    ImageView titleBackImg;
    @BindView(R.id.title_del_img)
    ImageView titleDelImg;
    @BindView(R.id.title_review_img)
    ImageView titleReviewImg;
    @BindView(R.id.map_img)
    ImageView mapImg;
    @BindView(R.id.map_ly)
    LinearLayout mapLy;
    @BindView(R.id.map_img1)
    ImageView engImg1;
    @BindView(R.id.map_ch_ly)
    LinearLayout mapChLy;
    @BindView(R.id.map_img0)
    ImageView mapimg0;
    @BindView(R.id.map_china_ly)
    LinearLayout mapChinaLy;
    @BindView(R.id.map_set_up)
    TextView mapSetUp;

    private boolean isGaoDe, isBAidu, isGOOGLE;
    private int map_f=0; //标记


    //高德地图应用包名
    public static final String AMAP_PACKAGENAME = "com.autonavi.minimap";
    //百度地图应用包名
    public static final String BAIDUMAP_PACKAGENAME = "com.baidu.BaiduMap";
    //google地图应用包名
    public static final String GOOGLE_PACKAGENAME = "com.google.android.apps.maps";
    private int flag_map;
    private int flag=0;
    private boolean flagGaoDe, flagBAidu, flagGOOGLE;

    @Override
    public int getLayoutId() {
        return R.layout.activi_map;
    }

    @Override
    public void onCreateInit() {
        titleBackTxt.setVisibility(View.VISIBLE);
        titleBackTitle.setText(getResources().getString(R.string.maps));
        isGaoDe = isApp(MapsActivity.this, AMAP_PACKAGENAME);
        isBAidu = isApp(MapsActivity.this, BAIDUMAP_PACKAGENAME);
        isGOOGLE = isApp(MapsActivity.this, GOOGLE_PACKAGENAME);
       if (isGaoDe){
           mapChinaLy.setVisibility(View.VISIBLE);
           flag++;
           flagGaoDe=true;
       }else{
           mapChinaLy.setVisibility(View.GONE);
       }
        if (isBAidu){
            mapLy.setVisibility(View.VISIBLE);
            flag++;
            flagBAidu=true;
        }else{
            mapLy.setVisibility(View.GONE);
        }
        if (isGOOGLE){
            mapChLy.setVisibility(View.VISIBLE);
            flag++;
            flagGOOGLE=true;
        }else{
            mapChLy.setVisibility(View.GONE);
        }
        flag_map = (int) SPUtil.get(MapsActivity.this, "flag_map", 0);
        map_f=flag_map;
        if (flag_map ==1){
            mapImg.setVisibility(View.VISIBLE);
        }else if (flag_map ==2){
            engImg1.setVisibility(View.VISIBLE);
        }else if (flag_map ==3){
            mapimg0.setVisibility(View.VISIBLE);
        }
        if (flag==1){
            if (flagGaoDe){
                SPUtil.put(MapsActivity.this,"flag_map",3);
            }else if (flagGOOGLE){
                SPUtil.put(MapsActivity.this,"flag_map",2);
            }else if (flagBAidu){
                SPUtil.put(MapsActivity.this,"flag_map",1);
            }
        }

    }

    @OnClick({R.id.title_back_fl,R.id.map_ly, R.id.map_ch_ly, R.id.map_china_ly,R.id.title_back_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.map_ly:
                mapImg.setVisibility(View.VISIBLE);
                engImg1.setVisibility(View.INVISIBLE);
                mapimg0.setVisibility(View.INVISIBLE);
                flag_map=1;
                break;
            case R.id.map_ch_ly:
                mapImg.setVisibility(View.INVISIBLE);
                engImg1.setVisibility(View.VISIBLE);
                mapimg0.setVisibility(View.INVISIBLE);
                flag_map=2;
                break;
            case R.id.map_china_ly:
                mapImg.setVisibility(View.INVISIBLE);
                engImg1.setVisibility(View.INVISIBLE);
                mapimg0.setVisibility(View.VISIBLE);
                flag_map=3;
                break;
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.title_back_txt:
                if (map_f==flag_map){

                }else {
                    SPUtil.put(MapsActivity.this,"flag_map",flag_map);
                    finish();
                }
                break;
        }
    }

    private boolean isApp(Context context, String packname) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo != null) {
            return true;
        } else {
            return false;
        }
    }

}
