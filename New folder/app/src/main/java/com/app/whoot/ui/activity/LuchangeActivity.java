package com.app.whoot.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.app.MyApplication;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.util.AppLanguageUtils;
import com.app.whoot.util.LanguageConfig;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ToastUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sunrise on 4/12/2018.
 */

public class LuchangeActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;
    @BindView(R.id.eng_ly)
    LinearLayout engLy;
    @BindView(R.id.eng_china_ly)
    LinearLayout engChinaLy;
    @BindView(R.id.eng_ch_ly)
    LinearLayout engChLy;
    @BindView(R.id.eng_img)
    ImageView engImg;
    @BindView(R.id.engimg0)
    ImageView engimg0;
    @BindView(R.id.eng_img1)
    ImageView engImg1;

    private int confirm=1;

    @Override
    public int getLayoutId() {
        return R.layout.luchang;
    }

    @Override
    public void onCreateInit() {
        titleBackTxt.setVisibility(View.VISIBLE);
        titleBackTitle.setText(getResources().getString(R.string.language));
       // engimg0.setVisibility(View.VISIBLE);

        int confirm = (int) SPUtil.get(LuchangeActivity.this, "confirm", 0);
        if (confirm==1){
            engImg.setVisibility(View.VISIBLE);
            engimg0.setVisibility(View.INVISIBLE);
            engImg1.setVisibility(View.INVISIBLE);
        }else if (confirm==2){
            engimg0.setVisibility(View.VISIBLE);
            engImg.setVisibility(View.INVISIBLE);
            engImg1.setVisibility(View.INVISIBLE);
        }else if (confirm==3){
            engImg.setVisibility(View.INVISIBLE);
            engimg0.setVisibility(View.INVISIBLE);
            engImg1.setVisibility(View.VISIBLE);
        }else {
            engImg.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.title_back_fl, R.id.title_back_txt, R.id.eng_ly, R.id.eng_china_ly, R.id.eng_ch_ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.title_back_txt:

                int conf = (int) SPUtil.get(LuchangeActivity.this, "confirm", 0);
                SPUtil.put(this,"luchang",conf+"");
                if (conf==1){  //简中
//                    changeLocal(Locale.TRADITIONAL_CHINESE);


                }else if (conf==2){ //繁中
//                    changeLocal(Locale.TRADITIONAL_CHINESE);
                    changeLocal(new Locale("zh","TW"), LanguageConfig.ID_TAIWAN);

                }else if (conf==3){ //英语
                    changeLocal(new Locale("en"), LanguageConfig.ID_EN);

                }else {
                    //Toast.makeText(LuchangeActivity.this,"请切换语言后保存",Toast.LENGTH_SHORT).show();
                    //ToastUtil.showgravity(LuchangeActivity.this,"请切换语言后保存");
                }
                break;
            case R.id.eng_ly:
                this.confirm =1;
                SPUtil.put(LuchangeActivity.this,"confirm", this.confirm);
                engImg.setVisibility(View.VISIBLE);
                engimg0.setVisibility(View.INVISIBLE);
                engImg1.setVisibility(View.INVISIBLE);
                break;
            case R.id.eng_china_ly:
                this.confirm =2;//中文
                SPUtil.put(LuchangeActivity.this,"confirm", this.confirm);
                engImg.setVisibility(View.INVISIBLE);
                engimg0.setVisibility(View.VISIBLE);
                engImg1.setVisibility(View.INVISIBLE);
                break;
            case R.id.eng_ch_ly:
                this.confirm =3;//英文
                SPUtil.put(LuchangeActivity.this,"confirm", this.confirm);
                engImg.setVisibility(View.INVISIBLE);
                engimg0.setVisibility(View.INVISIBLE);
                engImg1.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void changeLocal(Locale locale,String lanCode){
//        setAppLocal(locale);
        AppLanguageUtils.setLanguage(MyApplication.getInstance(),locale,lanCode);
        restartApp();
    }
    private void restartApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    //语言切换
//    private void setAppLocal(Locale locale) {
//        Resources resources = this.getResources();
//        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
//        Configuration configuration = resources.getConfiguration();
//        configuration.locale=locale;
//        resources.updateConfiguration(configuration,displayMetrics);
//    }
    public void setAppLocal(Locale locale) {
        Resources res = this.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            conf.setLocale(locale);
        } else {
            conf.locale =locale;
        }
        res.updateConfiguration(conf, dm);

    }
}
