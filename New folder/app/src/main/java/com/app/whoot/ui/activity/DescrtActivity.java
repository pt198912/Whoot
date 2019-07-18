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

/*
 * 兑换说明
 * */

public class DescrtActivity extends BaseActivity {
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

    @Override
    public int getLayoutId() {

        return R.layout.desc_activi;
    }

    @Override
    public void onCreateInit() {

        titleBackTitle.setText(getResources().getString(R.string.descri));
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
