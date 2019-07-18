package com.app.whoot.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.util.SPUtil;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sunrise on 5/16/2018.
 * 产品详情
 */

public class DishesActivity extends BaseActivity {
    @BindView(R.id.dish_img)
    ImageView dishImg;
    @BindView(R.id.dish_txt)
    TextView dishTxt;
    @BindView(R.id.dish_pic)
    TextView dishPic;
    @BindView(R.id.dish_tx)
    TextView dishTx;
    @BindView(R.id.dish_dis)
    TextView dishDis;
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;
    @BindView(R.id.dish_xiang)
    TextView dish_xiang;
    @BindView(R.id.dish_mon)
    LinearLayout dish_mon;

    private String cur_os;


    @Override
    public int getLayoutId() {
        return R.layout.dish_activity;
    }

    @Override
    public void onCreateInit() {
        Intent intent = getIntent();
        String describe = intent.getStringExtra("describe");
        String imgUrl = intent.getStringExtra("imgUrl");
        String name = intent.getStringExtra("name");
        double price = intent.getDoubleExtra("price", 0);

        if (describe.length()==0||describe==null){
            dish_xiang.setVisibility(View.GONE);
        }
        if (price==0.0){
            dish_mon.setVisibility(View.GONE);
        }
        String currency = (String) SPUtil.get(this, "currency", "");

        if (currency.equals("1")){
            cur_os="HKD";
        }else if (currency.equals("2")){
            cur_os="RMB";
        }else if (currency.equals("3")){
            cur_os="USD";
        }else if (currency.equals("4")){
            cur_os="EUR";
        }

        Glide.with(DishesActivity.this)
                .load(imgUrl)
                .centerCrop()
                .skipMemoryCache(true)
                .into(dishImg);
        dishTxt.setText(name);
        dishTx.setText(" "+cur_os+" "+price);
        dishDis.setText(describe);

        titleBackTitle.setText(getResources().getString(R.string.product));

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
