package com.app.whoot.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.view.custom.ClearEditText;
import com.app.whoot.util.AppUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

/**
 * 更改手机号密码
 */
public class ResetPaswActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.reset_new)
    ClearEditText resetNew;
    @BindView(R.id.reset_img)
    ImageView resetImg;
    @BindView(R.id.reset_pas_new)
    ClearEditText resetPasNew;
    @BindView(R.id.reset_new_img)
    ImageView resetNewImg;
    @BindView(R.id.reset_time_new)
    TextView resetTimeNew;
    private int img_flag=0;
    private int img_flag_psw=0;
    private String verify_code;
    private String mobile;
    private String url;

    @Override
    public int getLayoutId() {
        return R.layout.reset_activi;
    }

    @Override
    public void onCreateInit() {
        resetTimeNew.getBackground().setAlpha(100);
        titleBackTitle.setText(getResources().getString(R.string.reset_pa));
        setTextChangedListener();
        Intent intent = getIntent();
        verify_code = intent.getStringExtra("verify_Code");
        mobile = intent.getStringExtra("mobile");
        url = (String) SPUtil.get(this, "URL", "");
        resetNew.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        resetPasNew.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
    }

    private void setTextChangedListener() {

        if (resetNew.getText().toString().trim().length()>=6){
            if (resetPasNew.getText().toString().trim().length()>=6){
                resetTimeNew.getBackground().setAlpha(255);
                resetTimeNew.setClickable(true);
            }else {
                resetTimeNew.getBackground().setAlpha(100);
                resetTimeNew.setClickable(false);
            }
        }else {
            resetTimeNew.getBackground().setAlpha(100);
            resetTimeNew.setClickable(false);
        }


        resetNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length>0){
                    if (resetPasNew.getText().toString().trim().length()>=6){
                        if (resetNew.getText().toString().trim().length()>=6){
                            resetTimeNew.getBackground().setAlpha(255);
                            resetTimeNew.setClickable(true);
                        }
                    }else {
                        resetTimeNew.getBackground().setAlpha(100);
                        resetTimeNew.setClickable(false);
                    }
                    resetImg.setVisibility(View.VISIBLE);
                    resetNewImg.setVisibility(View.VISIBLE);
                }else {
                    resetImg.setVisibility(View.GONE);
                    resetNewImg.setVisibility(View.GONE);
                    resetTimeNew.getBackground().setAlpha(100);
                    resetTimeNew.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        resetPasNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length>0){
                    if (resetNew.getText().toString().trim().length()>=6){

                        if (resetPasNew.getText().toString().trim().length()>=6){
                            resetTimeNew.getBackground().setAlpha(255);
                            resetTimeNew.setClickable(true);
                        }
                    }else {
                        resetTimeNew.getBackground().setAlpha(100);
                        resetTimeNew.setClickable(false);
                    }
                    resetImg.setVisibility(View.VISIBLE);
                    resetNewImg.setVisibility(View.VISIBLE);
                }else {
                    resetImg.setVisibility(View.GONE);
                    resetNewImg.setVisibility(View.GONE);
                    resetTimeNew.getBackground().setAlpha(100);
                    resetTimeNew.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.title_back_fl, R.id.reset_img, R.id.reset_new_img, R.id.reset_time_new})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.reset_img:
                if (img_flag==0){
                    img_flag=1;
                    resetImg.setBackgroundResource(R.drawable.login_icon_show);
                    resetNew.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else if (img_flag==1){
                    img_flag=0;
                    resetImg.setBackgroundResource(R.drawable.login_icon_hide);
                    resetNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.reset_new_img:
                if (img_flag_psw==0){
                    img_flag_psw=1;
                    resetNewImg.setBackgroundResource(R.drawable.login_icon_show);
                    resetPasNew.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else if (img_flag_psw==1){
                    img_flag_psw=0;
                    resetNewImg.setBackgroundResource(R.drawable.login_icon_hide);
                    resetPasNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.reset_time_new:
                String resetPatrim = resetPasNew.getText().toString().trim();
                String resetNewtrim = resetNew.getText().toString().trim();
                boolean containAll = AppUtil.isContainAll(resetPatrim);
                if (!containAll){
                    ToastUtil.showgravity(ResetPaswActivity.this,getResources().getString(R.string.should));
                    return;
                }
                if (resetPatrim.equals(resetNewtrim)){
                    FormBody body = new FormBody.Builder()
                            .add("mobile", mobile)
                            .add("code",verify_code)
                            .add("password", AppUtil.MD5(resetPatrim))
                            .build();
                    Http.OkHttpPatc(this, url + UrlUtil.Mobile_password, body, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            Log.d("修改密码",url + UrlUtil.Mobile_password+result);
                            try {
                                JSONObject object = new JSONObject(result);
                                if (object.getString("code").equals("0")){
                                    ToastUtil.showgravity(ResetPaswActivity.this,getResources().getString(R.string.reset_pas));
                                    finish();
                                    if(RegisterActivity.instance!=null) {

                                        RegisterActivity.instance.finish();//关闭上一个页面
                                    }
                                }else {
                                    ToastUtil.showgravity(ResetPaswActivity.this,getResources().getString(R.string.fan));
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
                    ToastUtil.showgravity(ResetPaswActivity.this,getResources().getString(R.string.match));
                }

                break;
        }
    }
}
