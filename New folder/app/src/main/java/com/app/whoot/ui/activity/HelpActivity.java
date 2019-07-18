package com.app.whoot.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sunrise on 4/12/2018.
 * 帮助
 */

public class HelpActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;
    @BindView(R.id.help_ly_img)
    ImageView helpLyImg;
    @BindView(R.id.help_ly_one)
    LinearLayout helpLyOne;
    @BindView(R.id.help_txt)
    LinearLayout helpTxt;
    @BindView(R.id.help_img)
    ImageView helpImg;
    @BindView(R.id.help_ly_twe)
    LinearLayout helpLyTwe;
    @BindView(R.id.help_txt_one)
    LinearLayout helpTxtOne;

    private int flag_a=0;
    private int flag_b=0;

    @Override
    public int getLayoutId() {
        return R.layout.help;
    }

    @Override
    public void onCreateInit() {
        titleBackTitle.setText(R.string.help);
    }


    @OnClick({R.id.title_back_fl, R.id.title_back_title,R.id.help_ly_one, R.id.help_ly_twe})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.title_back_title:
                break;
            case R.id.help_ly_one:
                if (flag_a==0){
                    helpTxt.setVisibility(View.VISIBLE);
                    flag_a=1;
                    helpLyImg.setBackgroundResource(R.drawable.arrow_down);
                }else {
                    helpTxt.setVisibility(View.GONE);
                    flag_a=0;
                    helpLyImg.setBackgroundResource(R.drawable.storedetail_icon_arrow);
                }

                break;
            case R.id.help_ly_twe:
                if (flag_b==0){
                    helpTxtOne.setVisibility(View.VISIBLE);
                    flag_b=1;
                    helpImg.setBackgroundResource(R.drawable.arrow_down);
                }else {
                    helpTxtOne.setVisibility(View.GONE);
                    flag_b=0;
                    helpImg.setBackgroundResource(R.drawable.storedetail_icon_arrow);

                }
                break;
        }
    }
}
