package com.app.whoot.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcel;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.util.AppUtil;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sunrise on 4/12/2018.
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;
    @BindView(R.id.about_txt)
    TextView aboutTxt;
    @BindView(R.id.about_web)
    TextView about_web;

    @Override
    public int getLayoutId() {
        return R.layout.about;
    }

    @Override
    public void onCreateInit() {
        titleBackTitle.setText(R.string.about);

        String versionName = AppUtil.getVersionName(this);
        aboutTxt.setText(getResources().getString(R.string.version)+"  v"+versionName);
        String html = getResources().getString(R.string.youtubi_web);
        about_web.setAutoLinkMask(Linkify.ALL);
        about_web.setText(html);
        NoUnderLineSpan noUnderLineSpan = new NoUnderLineSpan();
        if (about_web.getText() instanceof Spannable) {
            Spannable s = (Spannable) about_web.getText();
            s.setSpan(noUnderLineSpan, 0, s.length(), Spanned.SPAN_MARK_MARK);
        }
    }
    @SuppressLint("ParcelCreator")
    public static class NoUnderLineSpan extends UnderlineSpan {
        public NoUnderLineSpan() {
        }

        public NoUnderLineSpan(Parcel src) {
            super(src);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.setColor(R.color.title);
        }
    }
    @OnClick(R.id.title_back_fl)
    public void onViewClicked() {
        finish();
    }

}
