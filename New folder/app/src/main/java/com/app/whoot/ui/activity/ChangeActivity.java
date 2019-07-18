package com.app.whoot.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

/**
 * 更改密码
 */
public class ChangeActivity extends BaseActivity {
    private static final String TAG = "ChangeActivity";
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.change_new)
    ClearEditText changeNew;
    @BindView(R.id.change_img)
    ImageView changeImg;
    @BindView(R.id.change_pas_nw)
    ClearEditText changePasNw;
    @BindView(R.id.change_new_g)
    ImageView changeNewG;
    @BindView(R.id.change_pas_new)
    ClearEditText changePasNew;
    @BindView(R.id.change_new_img)
    ImageView changeNewImg;
    @BindView(R.id.change_time_new)
    TextView changeTimeNew;
    @BindView(R.id.change_ly)
    LinearLayout change_ly;

    private int img_flag=0;
    private int img_flag_psw=0;
    private int img_flag_new=0;
    private String url;

    @Override
    public int getLayoutId() {
        return R.layout.change_activi;
    }

    @Override
    public void onCreateInit() {
        changeTimeNew.getBackground().setAlpha(100);
        url = (String) SPUtil.get(this, "URL", "");

        setTextChangedListener();
        changeNew.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        changePasNw.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        changePasNew.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        Intent intent = getIntent();
        int set_password = intent.getIntExtra("set_password", 0);
        if (set_password==1){
            titleBackTitle.setText(getResources().getString(R.string.set_paws));
            change_ly.setVisibility(View.GONE);
            SpannableString s_password = new SpannableString(getResources().getString(R.string.password));
            changePasNw.setHint(s_password);
            SpannableString s_confirm = new SpannableString(getResources().getString(R.string.confirm));
            changePasNew.setHint(s_confirm);
        }else {
            titleBackTitle.setText(getResources().getString(R.string.change));
            change_ly.setVisibility(View.VISIBLE);

        }
    }

    private void setTextChangedListener() {
        if (changePasNw.getText().toString().trim().length()>0&&changePasNew.getText().toString().trim().length()>0){
            changeTimeNew.getBackground().setAlpha(255);
            changeTimeNew.setClickable(true);
        }else {
            changeTimeNew.getBackground().setAlpha(100);
            changeTimeNew.setClickable(false);
        }

        changeNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length>0){
                    if (changePasNw.getText().toString().trim().length()>=6&&changePasNew.getText().toString().trim().length()>=6){


                        changeTimeNew.getBackground().setAlpha(255);
                        changeTimeNew.setClickable(true);
                    }else {

                        changeTimeNew.getBackground().setAlpha(100);
                        changeTimeNew.setClickable(false);
                    }
                    changeImg.setVisibility(View.VISIBLE);
                }else {
                    changeImg.setVisibility(View.GONE);

                    changeTimeNew.getBackground().setAlpha(100);
                    changeTimeNew.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    for (int i = 0; i <s.length() ; i++) {
                        char c = s.charAt(i);
                        if (c>=0x4e00&&c<=0X9fff){
                            s.delete(i,i+1);
                        }
                    }
                }
            }
        });
        changePasNw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length>0){
                    if (changeNew.getText().toString().trim().length()>=6&&changePasNew.getText().toString().trim().length()>=6){

                        changeTimeNew.getBackground().setAlpha(255);
                        changeTimeNew.setClickable(true);
                    }else {

                        changeTimeNew.getBackground().setAlpha(100);
                        changeTimeNew.setClickable(false);
                    }
                    changeNewG.setVisibility(View.VISIBLE);
                }else {
                    changeNewG.setVisibility(View.GONE);

                    changeTimeNew.getBackground().setAlpha(100);
                    changeTimeNew.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    for (int i = 0; i <s.length() ; i++) {
                        char c = s.charAt(i);
                        if (c>=0x4e00&&c<=0X9fff){
                            s.delete(i,i+1);
                        }
                    }
                }
            }
        });
        changePasNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length>0){
                    if (changeNew.getText().toString().trim().length()>=6&&changePasNw.getText().toString().trim().length()>=6){

                        changeTimeNew.getBackground().setAlpha(255);
                        changeTimeNew.setClickable(true);
                    }else {

                        changeTimeNew.getBackground().setAlpha(100);
                        changeTimeNew.setClickable(false);
                    }
                    changeNewImg.setVisibility(View.VISIBLE);
                }else {
                    changeNewImg.setVisibility(View.GONE);

                    changeTimeNew.getBackground().setAlpha(100);
                    changeTimeNew.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    for (int i = 0; i <s.length() ; i++) {
                        char c = s.charAt(i);
                        if (c>=0x4e00&&c<=0X9fff){
                            s.delete(i,i+1);
                        }
                    }
                }
            }
        });
    }

    @OnClick({R.id.title_back_fl, R.id.change_img, R.id.change_new_g, R.id.change_new_img, R.id.change_time_new})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.change_img:
                if (img_flag==0){
                    img_flag=1;
                    changeImg.setBackgroundResource(R.drawable.login_icon_show);
                    changeNew.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else if (img_flag==1){
                    img_flag=0;
                    changeImg.setBackgroundResource(R.drawable.login_icon_hide);
                    changeNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.change_new_g:
                if (img_flag_psw==0){
                    img_flag_psw=1;
                    changeNewG.setBackgroundResource(R.drawable.login_icon_show);
                    changePasNw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else if (img_flag_psw==1){
                    img_flag_psw=0;
                    changeNewG.setBackgroundResource(R.drawable.login_icon_hide);
                    changePasNw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.change_new_img:
                if (img_flag_new==0){
                    img_flag_new=1;
                    changeNewImg.setBackgroundResource(R.drawable.login_icon_show);
                    changePasNew.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else if (img_flag_new==1){
                    img_flag_new=0;
                    changeNewImg.setBackgroundResource(R.drawable.login_icon_hide);
                    changePasNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.change_time_new:

                String changetrim = changeNew.getText().toString().trim();
                String changePastrim = changePasNw.getText().toString().trim();
                String changePatrim = changePasNew.getText().toString().trim();
                if (!AppUtil.isContainAll(changePatrim)){
                    ToastUtil.showgravity(ChangeActivity.this,getResources().getString(R.string.should));
                    return;
                }
                String md5_passw = AppUtil.MD5(changetrim);
                String md5_changePatr = AppUtil.MD5(changePatrim);
                if (changePatrim.equals(changePastrim)){
                    loading(getResources().getString(R.string.listview_loading));
                    FormBody body = new FormBody.Builder()
                            .add("oldPwd", md5_passw)
                            .add("newPwd", md5_changePatr)
                            .build();
                    Http.OkHttpPatc(this, url + UrlUtil.ChangePwd, body, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            dissLoad();
                            Log.d("safddgfjhkjh",result+"=="+url + UrlUtil.ChangePwd);
                            try {
                                JSONObject object = new JSONObject(result);
                                if (object.getString("code").equals("0")){
                                    finish();
                                    ToastUtil.showgravity(ChangeActivity.this,getResources().getString(R.string.successfully));
                                }else if (object.getString("code").equals("1023")){
                                    ToastUtil.showgravity(ChangeActivity.this,getResources().getString(R.string.incorrect));
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
                    ToastUtil.showgravity(ChangeActivity.this,getResources().getString(R.string.match));
                }
                break;
        }
    }
}
