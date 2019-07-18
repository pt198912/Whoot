package com.app.whoot.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.view.dialog.BackToDialog;
import com.app.whoot.ui.view.dialog.CustomDialog;
import com.app.whoot.ui.view.dialog.GiftReOneDialog;
import com.app.whoot.ui.view.dialog.ResetDialog;
import com.app.whoot.util.AppUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

/**
 * Created by Sunrise on 1/4/2019.
 * 忘记密码
 */

public class ForgetActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.forget_conf_pass)
    EditText forgetConfPass;
    @BindView(R.id.forget_txt)
    TextView forgetTxt;
    @BindView(R.id.forget_deng)
    LinearLayout forgetDeng;

    private String urlUtil;
    private Context mContext;

    @Override
    public int getLayoutId() {
        return R.layout.forget_activi;
    }

    @Override
    public void onCreateInit() {
        titleBackTitle.setText(getResources().getString(R.string.forget));
        urlUtil = (String) SPUtil.get(this, "URL", "");
        mContext = this;
        forgetDeng.getBackground().setAlpha(100);
        setTextonclicked();
    }

    private void setTextonclicked() {
        String trim = forgetConfPass.getText().toString().trim();
        if (trim.length()>0){
            forgetDeng.getBackground().setAlpha(255);
            forgetDeng.setClickable(true);
        }else {
            forgetDeng.getBackground().setAlpha(100);
            forgetDeng.setClickable(false);
        }
        forgetConfPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length>0){
                    forgetDeng.getBackground().setAlpha(255);
                    forgetDeng.setClickable(true);
                }else {
                    forgetDeng.getBackground().setAlpha(100);
                    forgetDeng.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.title_back_fl, R.id.forget_deng})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.forget_deng:
                String trim = forgetConfPass.getText().toString().trim();
                if (!AppUtil.isEmail(trim)){
                    ToastUtil.showgravity(this, getResources().getString(R.string.invalid_no));
                    return;
                }
                loading(getResources().getString(R.string.listview_loading));
                if (trim.length() > 0) {

                    Http.OkHttpGet(ForgetActivity.this, urlUtil + UrlUtil.isemailreg + "?email=" + trim, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            dissLoad();
                            Log.d("dgdfhfgjty", result);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getString("code").equals("0")) {

                                    boolean data = jsonObject.getBoolean("data");
                                    if (data) {
                                        Http.OkHttpGet(ForgetActivity.this, urlUtil + UrlUtil.resetPwdLink + "?email=" + trim, new Http.OnDataFinish() {
                                            @Override
                                            public void OnSuccess(String result) {
                                                try {
                                                    JSONObject jsonObject1 = new JSONObject(result);
                                                    if (jsonObject1.getString("code").equals("0")) {
                                                        ResetDialog dialog = new ResetDialog(ForgetActivity.this, R.layout.custom_dialog, onClickListener);
                                                        dialog.show();

                                                    } else {

                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onError(Exception e) {

                                            }
                                        });
                                    } else {
                                        ToastUtil.showgravity(ForgetActivity.this, getResources().getString(R.string.logo_null));
                                    }

                                } else {
                                    ToastUtil.showgravity(ForgetActivity.this, getResources().getString(R.string.fan));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }else {
                    ToastUtil.showgravity(ForgetActivity.this, getResources().getString(R.string.olease));
                }
                break;
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.reset_my_login:  //确认
                    finish();
                    break;

            }
        }
    };
}
