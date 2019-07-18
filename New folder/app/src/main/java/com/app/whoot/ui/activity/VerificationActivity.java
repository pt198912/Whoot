package com.app.whoot.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.view.PassWordLayout;
import com.app.whoot.ui.view.custom.CodeEditText;
import com.app.whoot.util.CountDownTimerUtils;
import com.app.whoot.util.FireBaseEventKey;
import com.app.whoot.util.FirebaseReportUtils;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.app.whoot.util.VerifyCodeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

/**
 * 验证
 */
public class VerificationActivity extends BaseActivity {

    @BindView(R.id.verif_code)
    PassWordLayout verif_code;
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.verif_time)
    TextView verifTime;
    @BindView(R.id.verif_number)
    TextView verif_number;
    private int forget=0;
    private String url;
    private String languageType;
    private String nationCode;
    private String number;
    private CountDownTimerUtils mCountDownTimerUtils;
    private boolean verif_res;
    private int card_flag;
    private int my_flag;

    @Override
    public int getLayoutId() {
        return R.layout.verif_activi;
    }

    @Override
    public void onCreateInit() {
        VerifyCodeUtils.reset();
        url = (String) SPUtil.get(this, "URL", "");
        Intent intent = getIntent();
        forget = intent.getIntExtra("Forget", 0);
        number = intent.getStringExtra("Number");
        nationCode = intent.getStringExtra("nationCode");
        languageType = intent.getStringExtra("languageType");
        card_flag = intent.getIntExtra("card_flag", 0);
        my_flag = intent.getIntExtra("my_flag", 0);
        if (forget ==1){
            titleBackTitle.setText(getResources().getString(R.string.forget_pas));
        }else if (forget ==2){
            titleBackTitle.setText(getResources().getString(R.string.sign));
        }else if (forget ==3){
            titleBackTitle.setText(getResources().getString(R.string.phone_number));
        }else if(forget ==4){
            titleBackTitle.setText(getResources().getString(R.string.phone_number));
        }
        verif_number.setText(nationCode +" "+ number);

        verif_code.setPwdChangeListener(new PassWordLayout.pwdChangeListener() {
            @Override
            public void onChange(String pwd) {

            }

            @Override
            public void onNull() {

            }

            @Override
            public void onFinished(String code) {
                Log.d("fdjfjdjgdfg","==="+verif_res);
                    if(verif_res) {
                        ToastUtil.showgravity(VerificationActivity.this, getResources().getString(R.string.code_wu));
                        return;
                    }

                FormBody body = new FormBody.Builder()
                        .add("mobile", number)
                        .add("code",code)
                        .build();
                loading(getResources().getString(R.string.listview_loading));
                if (forget==1){  //忘记密码
                    Http.OkHttpPost(VerificationActivity.this, url + UrlUtil.Forgot_Verify, body, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            dissLoad();
                            Log.d("验证码",result+"=="+url + UrlUtil.Verify);
                            try {
                                JSONObject object = new JSONObject(result);
                                if (object.getString("code").equals("0")){
                                    Intent intent = new Intent(VerificationActivity.this, ResetPaswActivity.class);
                                    intent.putExtra("verify_Code",code);
                                    intent.putExtra("mobile", number);
                                    startActivity(intent);
                                    finish();
                                }else if (object.getString("code").equals("5015")){
                                    verif_res = VerifyCodeUtils.checkFailTooManyTimesInOneMinute(VerificationActivity.this);
                                    ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.code_yan));
                                }else if (object.getString("code").equals("5014")){
                                    verif_res = VerifyCodeUtils.checkFailTooManyTimesInOneMinute(VerificationActivity.this);
                                    ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.code_xiao));
                                }else {
                                    ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.fan));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }else if (forget==2){
                    Http.OkHttpPost(VerificationActivity.this, url + UrlUtil.Verify, body, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            dissLoad();
                            Log.d("验证码",result+"=="+url + UrlUtil.Verify);
                            try {
                                JSONObject object = new JSONObject(result);
                                if (object.getString("code").equals("0")){
                                    Intent intent = new Intent(VerificationActivity.this, SignMenActivity.class);
                                    intent.putExtra("verify_Code",code);
                                    intent.putExtra("mobile", number);
                                    intent.putExtra("card_flag", card_flag);
                                    intent.putExtra("my_flag", my_flag);
                                    startActivity(intent);
                                    finish();
                                }else if (object.getString("code").equals("5015")){
                                    verif_res = VerifyCodeUtils.checkFailTooManyTimesInOneMinute(VerificationActivity.this);
                                    ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.code_yan));
                                }else if (object.getString("code").equals("5014")){
                                    verif_res = VerifyCodeUtils.checkFailTooManyTimesInOneMinute(VerificationActivity.this);
                                    ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.code_xiao));
                                }else {
                                    ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.fan));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }else if (forget==3){
                    Http.OkHttpPost(VerificationActivity.this, url + UrlUtil.Verify, body, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            dissLoad();
                            Log.d("验证码",result+"=="+url + UrlUtil.Verify);
                            try {
                                JSONObject object = new JSONObject(result);
                                if (object.getString("code").equals("0")){
                                    BingNumber(number,code);

                                }else if (object.getString("code").equals("5015")){
                                    verif_res = VerifyCodeUtils.checkFailTooManyTimesInOneMinute(VerificationActivity.this);
                                    ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.code_yan));
                                }else if (object.getString("code").equals("5014")){
                                    verif_res = VerifyCodeUtils.checkFailTooManyTimesInOneMinute(VerificationActivity.this);
                                    ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.code_xiao));
                                }else {
                                    ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.fan));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }else if (forget==4){
                    Http.OkHttpPost(VerificationActivity.this, url + UrlUtil.Verify, body, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            dissLoad();
                            Log.d("验证码",result+"=="+url + UrlUtil.Verify);
                            try {
                                JSONObject object = new JSONObject(result);
                                if (object.getString("code").equals("0")){
                                    BingNumber(number,code);
                                }else if (object.getString("code").equals("5015")){
                                    verif_res = VerifyCodeUtils.checkFailTooManyTimesInOneMinute(VerificationActivity.this);
                                    ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.code_yan));
                                }else if (object.getString("code").equals("5014")){
                                    verif_res = VerifyCodeUtils.checkFailTooManyTimesInOneMinute(VerificationActivity.this);
                                    ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.code_xiao));
                                }else {
                                    ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.fan));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
            }
        });



        //倒计时1分钟
        mCountDownTimerUtils = new CountDownTimerUtils(this,verifTime, 60000, 1000);
        mCountDownTimerUtils.start();

        reportPageShown();
    }

    private void reportPageShown() {
        Bundle b=new Bundle();
        b.putString("type","SHOW_RMOBILE_VERIFY");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.SHOW_REGISTER_CELLPHONE_VERIFYCODE,b);
    }


    @OnClick({R.id.title_back_fl,R.id.verif_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.verif_time:
                loading(getResources().getString(R.string.listview_loading));
                if(mCountDownTimerUtils.OnStopTime()){
                    VerifyCodeUtils.reset();
                    verif_res=false;
                }
                Log.d("获取验证码",url + UrlUtil.Mobile_code+"?mobile="+number+"&nationCode="+nationCode+"&languageType="+languageType+"==");
                if (forget==1){

                    Http.OkHttpGet(this, url + UrlUtil.Mobile_resetPwdCode+"?mobile="+number+"&nationCode="+nationCode+"&languageType="+languageType, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            Log.d("获取验证码",url + UrlUtil.Mobile_code+"?mobile="+number+"&nationCode="+nationCode+"&languageType="+languageType+"=="+result);
                            dissLoad();
                            try {
                                JSONObject object = new JSONObject(result);
                                if (object.getString("code").equals("0")){
                                    mCountDownTimerUtils.start();
                                }else if (object.getString("code").equals("1025")){
                                    ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.code_xian));
                                }else if (object.getString("code").equals("5103")){

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }else if (forget==2){
                    Http.OkHttpGet(this, url + UrlUtil.Mobile_code+"?mobile="+number+"&nationCode="+nationCode+"&languageType="+languageType, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            dissLoad();
                            Log.d("获取验证码",url + UrlUtil.Mobile_code+"?mobile="+number+"&nationCode="+nationCode+"&languageType="+languageType+"=="+result);
                            try {
                                JSONObject object = new JSONObject(result);
                                if (object.getString("code").equals("0")){
                                    mCountDownTimerUtils.start();
                                }else if (object.getString("code").equals("1025")){
                                    ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.code_xian));
                                }else if (object.getString("code").equals("5103")){

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }else if (forget==3){
                    Http.OkHttpGet(this, url + UrlUtil.Mobile_code+"?mobile="+number+"&nationCode="+nationCode+"&languageType="+languageType, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            dissLoad();
                            Log.d("获取验证码",url + UrlUtil.Mobile_code+"?mobile="+number+"&nationCode="+nationCode+"&languageType="+languageType+"=="+result);
                            try {
                                JSONObject object = new JSONObject(result);
                                if (object.getString("code").equals("0")){
                                    mCountDownTimerUtils.start();
                                }else if (object.getString("code").equals("1025")){
                                    ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.code_xian));
                                }else if (object.getString("code").equals("5103")){

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }else if (forget==4){
                    Http.OkHttpGet(this, url + UrlUtil.Mobile_code+"?mobile="+number+"&nationCode="+nationCode+"&languageType="+languageType, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            dissLoad();
                            Log.d("获取验证码",url + UrlUtil.Mobile_code+"?mobile="+number+"&nationCode="+nationCode+"&languageType="+languageType+"=="+result);
                            try {
                                JSONObject object = new JSONObject(result);
                                if (object.getString("code").equals("0")){
                                    mCountDownTimerUtils.start();
                                }else if (object.getString("code").equals("1025")){
                                    ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.code_xian));
                                }else if (object.getString("code").equals("5103")){

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
                break;
        }
    }

    public void BingNumber(String mobile,String code){
        FormBody body = new FormBody.Builder()
                .add("mobile", mobile)
                .add("code", code)
                .build();
        Http.OkHttpPost(VerificationActivity.this, url + UrlUtil.Bind_Http, body, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.d("shfoiijdfs",result+"=="+url + UrlUtil.Bind_Http);
                    if (jsonObject.getString("code").equals("0")){
                        ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.phone_succ));
                        SPUtil.put(VerificationActivity.this,"you_tel",number);
                        if (RegisterActivity.instance!=null){
                            RegisterActivity.instance.finish();
                        }
                        finish();
                    }else {
                        ToastUtil.showgravity(VerificationActivity.this,getResources().getString(R.string.fan));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

}
