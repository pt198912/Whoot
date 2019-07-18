package com.app.whoot.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.bumptech.glide.Glide;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sunrise on 8/9/2018.
 * 礼包扫码成功页面
 */

public class GiftSuccessActivity extends BaseActivity {

    @BindView(R.id.giftsuaa)
    ImageView giftsuaa;
    @BindView(R.id.succ_ly_l)
    LinearLayout succLyL;
    @BindView(R.id.giftsu_activi)
    LinearLayout giftsuActivi;
    private String giftshopid;

    @Override
    public int getLayoutId() {
        return R.layout.giftsucce_activi;
    }

    @Override
    public void onCreateInit() {
        Intent intent = getIntent();
        String productImg = intent.getStringExtra("productImg");
        giftshopid = intent.getStringExtra("giftshopid");
        String giftCd = intent.getStringExtra("giftCd");
        SPUtil.put(this,"downco",giftCd);
        Glide.with(GiftSuccessActivity.this)
                .load(productImg)
                .centerCrop()
                .skipMemoryCache(true)
                .error(R.drawable.headimg)
                .into(giftsuaa);
    }

    @OnClick({R.id.succ_ly_l, R.id.giftsu_activi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.succ_ly_l:
                andro();
                break;
            case R.id.giftsu_activi:
                finish();
                break;
        }
    }

    public void andro(){

        List<Intent> targetedShareIntents = new ArrayList<Intent>();

        String shopname = (String) SPUtil.get(GiftSuccessActivity.this, "shopname", "");
        for (int i = 0; i < 10; i++) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Check out: \n" +
                    shopname+"\n"+ UrlUtil.Share+"shopId="+giftshopid);
            intent.setType("text/plain");
            //intent.setType("image/jpeg");
            //intent.putExtra(Intent.EXTRA_TITLE, "标题");
            //intent.putExtra(Intent.EXTRA_SUBJECT, "内容");
            if (i == 0) {
                // intent.setPackage("com.whatsapp");
                intent.setClassName("com.whatsapp","com.whatsapp.ContactPicker");
            } else if (i == 1) {
                //intent.setPackage("com.instagram.android");
                intent.setClassName("com.instagram.android","com.instagram.direct.share.handler.DirectShareHandlerActivity");
            } else if (i == 2) {
                intent.setPackage("com.facebook.katana");
                //intent.setClassName("com.samsung.android.voc","com.samsung.android.community.ui.WebFacebookLaunchActivity");
            }else if (i == 3) {
                intent.setPackage("com.facebook.orca");
            }else if (i == 4) {
                //intent.setPackage("jp.naver.line.android");
                intent.setClassName("jp.naver.line.android","jp.naver.line.android.activity.selectchat.SelectChatActivityLaunchActivity");
            }else if (i == 5) {
                intent.setPackage("com.tencent.mm");
            }else if (i == 6) {
                // intent.setPackage("com.google.android.gm");
                intent.setClassName("com.tencent.mm","com.tencent.mm.ui.tools.ShareToTimeLineUI");
            } else if (i == 7) {
                intent.setPackage("com.samsung.android.email.provider");
            }else if (i == 8) {
                intent.setPackage("com.samsung.android.messaging");
            }else if (i == 9) {
                intent.setPackage("com.google.android.gm");

            }
            targetedShareIntents.add(intent);
        }
        Intent chooserIntent = null;
        try {
            chooserIntent = Intent.createChooser(Intent.getIntent(""), "Select app to share");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (chooserIntent == null) {
            return;
        }
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));

        // A Parcelable[] of Intent or LabeledIntent objects as set with
        // putExtra(String, Parcelable[]) of additional activities to place
        // a the front of the list of choices, when shown to the user with a
        // ACTION_CHOOSER.
        try {
            startActivity(chooserIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            //Toast.makeText(this, "Can't find share component to share", Toast.LENGTH_SHORT).show();
            ToastUtil.showgravity(this, "Can't find share component to share");
        }
    }

}
