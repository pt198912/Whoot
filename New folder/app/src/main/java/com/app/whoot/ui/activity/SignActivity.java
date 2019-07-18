package com.app.whoot.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.SignBean;
import com.app.whoot.bean.TokenExchangeBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.activity.main.TransActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.ui.view.dialog.TokenDialog;
import com.app.whoot.util.AppUtil;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

import static com.app.whoot.util.Constants.EVENT_TYPE_SIGN;

/**
 * Created by Sunrise on 1/3/2019.
 * <p>
 * 邮箱注册
 */

public class SignActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.sign_name)
    EditText signName;
    @BindView(R.id.sign_email)
    EditText signEmail;
    @BindView(R.id.sign_pass)
    EditText signPass;
    @BindView(R.id.sign_conf_pass)
    EditText signConfPass;
    @BindView(R.id.sign_deng)
    LinearLayout signDeng;
    @BindView(R.id.sign_txt)
    TextView signTxt;


    private String name = "";
    private String email = "";
    private String passw = "";
    private String confpass = "";
    private String url;
    private List<SignBean> listean = new ArrayList<SignBean>();
    private TokenDialog browsDialog;


    @Override
    public int getLayoutId() {
        return R.layout.sign_activi;
    }

    @Override
    public void onCreateInit() {


        signPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        signConfPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        titleBackTitle.setText(getResources().getString(R.string.sign_up));
        url = (String) SPUtil.get(this, "URL", "");
        signTxt.setText(getResources().getString(R.string.login_by));
        SpannableString clickString = new SpannableString("  "+getResources().getString(R.string.login_terms)+"  ");
        signTxt.setMovementMethod(LinkMovementMethod.getInstance());
        clickString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent3 = new Intent(SignActivity.this, WebViewActivity.class);
                intent3.putExtra("html", 2);
                startActivity(intent3);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.blue_one));//设置颜色
                ds.setUnderlineText(false);
            }
        }, 0, clickString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signTxt.append(clickString);
        signTxt.append(new SpannableString(getResources().getString(R.string.and)));
        SpannableString click = new SpannableString("  "+getResources().getString(R.string.yinsi)+"  ");
        click.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent4 = new Intent(SignActivity.this, WebViewActivity.class);
                intent4.putExtra("html", 1);
                startActivity(intent4);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.blue_one));//设置颜色
                ds.setUnderlineText(false);
            }
        }, 0, click.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signTxt.append(click);

    }

    @OnClick({R.id.title_back_fl, R.id.sign_deng})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.sign_deng:
                name = signName.getText().toString().trim();
                email = signEmail.getText().toString().trim();
                passw = signPass.getText().toString().trim();
                confpass = signConfPass.getText().toString().trim();
                if (name.equals("")) {
                     ToastUtil.showgravity(SignActivity.this,getResources().getString(R.string.invalid_name));
                } else {

                    if (isEmail(email)) {
                        if (passw.equals("")){
                            ToastUtil.showgravity(SignActivity.this,getResources().getString(R.string.invalid_null));
                            return;
                        }
                        if (passw.length()<6){
                            ToastUtil.showgravity(SignActivity.this,getResources().getString(R.string.error_invalid_password));
                            return;
                        }
                        if (passw.equals(confpass)) {

                            Http.OkHttpGet(this, url + UrlUtil.isemailreg + "?email=" + email, new Http.OnDataFinish() {
                                @Override
                                public void OnSuccess(String result) {
                                    Log.d("hahhh", result.toString() + url + UrlUtil.isemailreg);
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        if (jsonObject.getString("code").equals("0")) {
                                            boolean data = jsonObject.getBoolean("data");
                                            if (data) {
                                                ToastUtil.showgravity(SignActivity.this, getResources().getString(R.string.invalid_yong));
                                            } else {
                                                Http.OkHttpGet(SignActivity.this, url + UrlUtil.check_name + "?userName=" + name, new Http.OnDataFinish() {
                                                    @Override
                                                    public void OnSuccess(String result) {
                                                        Log.d("check_name", result.toString() + url + UrlUtil.check_name);
                                                        try {
                                                            JSONObject jsonObject1 = new JSONObject(result);
                                                            if (jsonObject.getString("code").equals("0")) {
                                                                boolean data1 = jsonObject1.getBoolean("data");
                                                                if (data1) {
                                                                    ToastUtil.showgravity(SignActivity.this, getResources().getString(R.string.bu_ya));
                                                                } else {
                                                                    String md5_passw = AppUtil.MD5(passw);
                                                                    FormBody body = new FormBody.Builder()
                                                                            .add("type", "3")
                                                                            .add("name", name)
                                                                            .add("email", email)
                                                                            .add("password", md5_passw)
                                                                            .add("notifyToken", FirebaseInstanceId.getInstance().getToken())
                                                                            .build();
                                                                    Http.OkHttpPost(SignActivity.this, url + UrlUtil.login, body, new Http.OnDataFinish() {
                                                                        @Override
                                                                        public void OnSuccess(String result) {
                                                                            Log.d("login1", result.toString() + url + UrlUtil.login);
                                                                            try {
                                                                                JSONObject jsonObject2 = new JSONObject(result);
                                                                                if (jsonObject2.getString("code").equals("0")) {
                                                                                    JSONObject data2 = jsonObject2.getJSONObject("data");
                                                                                    JSONObject account = data2.getJSONObject("account");

                                                                                    String name = account.getString("name");
                                                                                    String id = account.getString("id");
                                                                                    String email = account.getString("email");
                                                                                    String token = data2.getString("token");
                                                                                    SPUtil.put(SignActivity.this, "Token", token);
                                                                                    SPUtil.put(SignActivity.this, "email", email);
                                                                                    SPUtil.put(SignActivity.this, "name_one", name); //名字
                                                                                    SPUtil.put(SignActivity.this, "you_usid", id);
                                                                                    JSONArray coupons = data2.getJSONArray("coupons");

                                                                                    for (int i = 0; i < coupons.length(); i++) {
                                                                                        SignBean bean = GSonUtil.parseGson(coupons.getString(i), SignBean.class);
                                                                                        listean.add(bean);
                                                                                    }
                                                                                    int tong = 0;
                                                                                    int jin = 0;
                                                                                    int yin = 0;
                                                                                    int zuan = 0;
                                                                                    for (int j = 0; j < listean.size(); j++) {
                                                                                        int couponType = listean.get(j).getCouponType();
                                                                                        if (couponType == 1) {
                                                                                            tong++;
                                                                                        } else if (couponType == 2) {
                                                                                            yin++;
                                                                                        } else if (couponType == 3) {
                                                                                            jin++;
                                                                                        } else if (couponType == 4) {
                                                                                            zuan++;
                                                                                        }
                                                                                    }
                                                                                    SPUtil.put(SignActivity.this, "My_tong", tong);
                                                                                    SPUtil.put(SignActivity.this, "My_yin", yin);
                                                                                    SPUtil.put(SignActivity.this, "My_jin", jin);
                                                                                    SPUtil.put(SignActivity.this, "My_zuan", zuan);

                                                                                    finish();
                                                                                    if(LoginActivity.instance!=null) {

                                                                                        LoginActivity.instance.finish();//关闭上一个页面
                                                                                    }
                                                                                    if (LoginMeActivity.instance!=null){
                                                                                        LoginMeActivity.instance.finish();
                                                                                    }
                                                                                    boolean showUpgradeToToken = data2.getBoolean("showUpgradeToToken");
                                                                                    if (showUpgradeToToken){
                                                                                        browsDialog = new TokenDialog(SignActivity.this, R.style.box_dialog);
                                                                                        browsDialog.setCanceledOnTouchOutside(false);
                                                                                        browsDialog.show();
                                                                                        browsDialog.setCancelable(false);
                                                                                        SPUtil.remove(SignActivity.this,"showUpgradeToToken");
                                                                                        }
                                                                                    /**
                                                                                     * 发消息签到
                                                                                     * */
                                                                                    EventBusCarrier carrier=new EventBusCarrier();
                                                                                    TokenExchangeBean bean=new TokenExchangeBean();
                                                                                    bean.setSuToken(1);
                                                                                    bean.setFlagToken(1);
                                                                                    bean.setFlagCoupn(1);
                                                                                    carrier.setEventType(EVENT_TYPE_SIGN);
                                                                                    carrier.setObject(bean);
                                                                                    EventBus.getDefault().post(carrier);


                                                                                    } else if (jsonObject2.getString("code").equals("5001")) {
                                                                                    ToastUtil.showgravity(SignActivity.this, getResources().getString(R.string.user_no));
                                                                                } else {
                                                                                    ToastUtil.showgravity(SignActivity.this, getResources().getString(R.string.fan));
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

                                            }
                                        } else {
                                            ToastUtil.showgravity(SignActivity.this, getResources().getString(R.string.fan));
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                            FormBody body = new FormBody.Builder()
                                    .add("", "1")
                                    .add("name", name)
                                    .add("email", email)
                                    .add("password", passw)
                                    .build();


                        } else {
                            ToastUtil.showgravity(this, getResources().getString(R.string.invalid_yi));
                        }
                    } else {
                        ToastUtil.showgravity(this, getResources().getString(R.string.invalid_no));
                    }
                }
                break;
        }
    }

    //判断email格式是否正确
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

}
