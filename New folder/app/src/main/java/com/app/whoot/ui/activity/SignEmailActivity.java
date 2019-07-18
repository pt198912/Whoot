package com.app.whoot.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.SignBean;
import com.app.whoot.bean.TokenExchangeBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.ui.view.custom.ClearEditText;
import com.app.whoot.ui.view.dialog.InviteDialog;
import com.app.whoot.ui.view.dialog.TokenDialog;
import com.app.whoot.util.AppUtil;
import com.app.whoot.util.FireBaseEventKey;
import com.app.whoot.util.FirebaseReportUtils;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.google.firebase.iid.FirebaseInstanceId;

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
import static com.app.whoot.util.Constants.TOKEN_FROM_COMMENT;
import static com.app.whoot.util.Constants.TOKEN_FROM_DAILY_SIGN;
import static com.app.whoot.util.Constants.TOKEN_FROM_EVENT;
import static com.app.whoot.util.Constants.TOKEN_FROM_EXCHANGE;
import static com.app.whoot.util.Constants.TOKEN_FROM_INVITED;
import static com.app.whoot.util.Constants.TOKEN_FROM_REFERRAL;
import static com.app.whoot.util.Constants.TOKEN_FROM_REGISTER;
import static com.app.whoot.util.Constants.TOKEN_FROM_VERSION_UPGRADE;

/**
 * 邮箱注册
 */
public class SignEmailActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.semail_em)
    ClearEditText semailEm;
    @BindView(R.id.semail_name)
    ClearEditText semailName;
    @BindView(R.id.semail_pas)
    ClearEditText semailPas;
    @BindView(R.id.semail_pas_img)
    ImageView semailPasImg;
    @BindView(R.id.semail_pasword)
    ClearEditText semailPasword;
    @BindView(R.id.semail_pasword_img)
    ImageView semailPaswordImg;
    @BindView(R.id.semail_txt)
    TextView semailTxt;
    @BindView(R.id.semail_time)
    TextView semailTime;
    @BindView(R.id.semail_invite)
    ClearEditText semail_invite;

    private int img_flag=0;
    private int img_flag_psw=0;
    private int pas_length=0;
    private int pasword_length=0;
    private int email_length=0;
    private String url;
    private TokenDialog browsDialog;
    private List<SignBean> listean = new ArrayList<SignBean>();
    private String token_fire;
    private int card_flag;
    private int my_flag;
    private String userId;
    private InviteDialog dialog;


    @Override
    public int getLayoutId() {
        return R.layout.signemail_activi;
    }

    @Override
    public void onCreateInit() {
        semailTime.getBackground().setAlpha(100);
        titleBackTitle.setText(getResources().getString(R.string.email_logn));
        url = (String) SPUtil.get(this, "URL", "");
        getPolicy();
        setTextChangedListener();
        Intent intent = getIntent();
        card_flag = intent.getIntExtra("card_flag", 0);
        my_flag = intent.getIntExtra("my_flag", 0);
        semailName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        semail_invite.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        reportPageShown();
    }
    //埋点
    private void reportPageShown() {
        Bundle b=new Bundle();
        b.putString("type","SHOW_RMAIL_INFO");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.SHOW_REGISTER_EMAIL_INFO,b);
    }

    private static final String TAG = "SignEmailActivity";

    public void setTextChangedListener(){
        if (pasword_length>0&&semailName.getText().toString().trim().length()>0){

            semailTime.getBackground().setAlpha(255);
            semailTime.setClickable(true);
        }else {

            semailTime.getBackground().setAlpha(100);
            semailTime.setClickable(false);
        }
        semailPas.addTextChangedListener(new TextWatcher() {

            private int oldLength = 0;
            private boolean isChange = true;
            private int curLength = 0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG,"一次性输入的字符"+count);
                if(isChange){
                    isChange=false;
                    return;
                }
                curLength = s.length();
                int length = s.length();

                pas_length=length;
                if(start<=s.length()) {
                    String newInsert = s.subSequence(start, s.length()).toString();
                    Log.d(TAG, "onTextChanged: newInsert " + newInsert);
                    
                }
                Log.d(TAG, "onTextChanged: start "+start+",before "+before);
                if (length>0){
                    if (pasword_length>0&&semailName.getText().toString().trim().length()>0){

                        semailTime.getBackground().setAlpha(255);
                        semailTime.setClickable(true);
                    }else {

                        semailTime.getBackground().setAlpha(100);
                        semailTime.setClickable(false);
                    }
                    semailPasImg.setVisibility(View.VISIBLE);
                }else {
                    semailPasImg.setVisibility(View.GONE);

                    semailTime.getBackground().setAlpha(100);
                    semailTime.setClickable(false);
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
        semailPasword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                pasword_length=length;
                if (length>0){
                    if (pas_length>0&&semailName.getText().toString().trim().length()>0&&email_length>0){

                        semailTime.getBackground().setAlpha(255);
                        semailTime.setClickable(true);
                    }else {

                        semailTime.getBackground().setAlpha(100);
                        semailTime.setClickable(false);
                    }
                    semailPaswordImg.setVisibility(View.VISIBLE);
                }else {
                    semailPaswordImg.setVisibility(View.GONE);

                    semailTime.getBackground().setAlpha(100);
                    semailTime.setClickable(false);
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
        semailName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length>0){
                    if (pas_length>0&&pasword_length>0&&email_length>0){


                        semailTime.getBackground().setAlpha(255);
                        semailTime.setClickable(true);
                    }else {

                        semailTime.getBackground().setAlpha(100);
                        semailTime.setClickable(false);
                    }
                }else {

                    semailTime.getBackground().setAlpha(100);
                    semailTime.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        semailEm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                email_length=length;
                if (length>0){
                      if (pas_length>0&&pasword_length>0&&semailName.getText().toString().trim().length()>0){

                          semailTime.getBackground().setAlpha(255);
                          semailTime.setClickable(true);
                      }else {

                          semailTime.getBackground().setAlpha(100);
                          semailTime.setClickable(false);
                      }
                }else {

                    semailTime.getBackground().setAlpha(100);
                    semailTime.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void getPolicy(){
        semailTxt.setText(getResources().getString(R.string.sign_men));
        SpannableString clickString = new SpannableString("  "+getResources().getString(R.string.login_terms)+"  ");
        semailTxt.setMovementMethod(LinkMovementMethod.getInstance());
        clickString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent intent3 = new Intent(SignEmailActivity.this, WebViewActivity.class);
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
        semailTxt.append(clickString);
        semailTxt.append(new SpannableString(getResources().getString(R.string.and)));
        SpannableString click = new SpannableString("  "+getResources().getString(R.string.yinsi)+"  ");
        click.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent intent4 = new Intent(SignEmailActivity.this, WebViewActivity.class);
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
        semailTxt.append(click);
    }

    @OnClick({R.id.title_back_fl, R.id.semail_pas_img, R.id.semail_pasword_img, R.id.semail_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.semail_pas_img:
                if (img_flag==0){
                    img_flag=1;
                    semailPasImg.setBackgroundResource(R.drawable.login_icon_show);
                    semailPas.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else if (img_flag==1){
                    img_flag=0;
                    semailPasImg.setBackgroundResource(R.drawable.login_icon_hide);
                    semailPas.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.semail_pasword_img:
                if (img_flag_psw==0){
                    img_flag_psw=1;
                    semailPaswordImg.setBackgroundResource(R.drawable.login_icon_show);
                    semailPasword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else if (img_flag_psw==1){
                    img_flag_psw=0;
                    semailPaswordImg.setBackgroundResource(R.drawable.login_icon_hide);
                    semailPasword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.semail_time:

                String semailinvitetrim = semail_invite.getText().toString().trim();
                if (semailinvitetrim.length()>0){
                    isCheckCode(semailinvitetrim);
                }else {
                    getLogin("");
                }
                reportPageByemail();

                break;
        }
    }
    //埋点
    private void reportPageByemail() {
        Bundle b=new Bundle();
        b.putString("type","CLICK_REG_SUBMIT");
        b.putString("regType","MAIL");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.REGISTER_GO,b);
    }

    //判断email格式是否正确
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }
     //登录

    public void getLogin(String code){

        String email = semailEm.getText().toString().trim();
        String name = semailName.getText().toString().trim();
        String passw = semailPas.getText().toString().trim();
        token_fire = FirebaseInstanceId.getInstance().getToken();
        if (token_fire==null){
            token_fire="";
        }
        String confpass = semailPasword.getText().toString().trim();
        if (name.equals("")) {
            ToastUtil.showgravity(SignEmailActivity.this,getResources().getString(R.string.invalid_name));
        } else {
            if (name.length()<3){
                return;
            }
            if (isEmail(email)) {
                if (passw.equals("")){
                    ToastUtil.showgravity(SignEmailActivity.this,getResources().getString(R.string.invalid_null));
                    return;
                }
                if (passw.length()<6){
                    ToastUtil.showgravity(SignEmailActivity.this,getResources().getString(R.string.error_invalid_password));
                    return;
                }
                if (!AppUtil.isContainAll(passw)){
                    ToastUtil.showgravity(SignEmailActivity.this,getResources().getString(R.string.should));
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
                                        ToastUtil.showgravity(SignEmailActivity.this, getResources().getString(R.string.invalid_yong));
                                    } else {
                                        Http.OkHttpGet(SignEmailActivity.this, url + UrlUtil.check_name + "?userName=" + name, new Http.OnDataFinish() {
                                            @Override
                                            public void OnSuccess(String result) {
                                                Log.d("check_name", result.toString() + url + UrlUtil.check_name);
                                                try {
                                                    JSONObject jsonObject1 = new JSONObject(result);
                                                    if (jsonObject.getString("code").equals("0")) {
                                                        boolean data1 = jsonObject1.getBoolean("data");
                                                        if (data1) {
                                                            ToastUtil.showgravity(SignEmailActivity.this, getResources().getString(R.string.zhangha));
                                                        } else {
                                                            String md5_passw = AppUtil.MD5(passw);
                                                            FormBody body = new FormBody.Builder()
                                                                    .add("type", "3")
                                                                    .add("name", name)
                                                                    .add("email", email)
                                                                    .add("password", md5_passw)
                                                                    .add("notifyToken",token_fire)
                                                                    .add("invitationCode",code.toUpperCase())
                                                                    .build();
                                                            Http.OkHttpPost(SignEmailActivity.this, url + UrlUtil.login, body, new Http.OnDataFinish() {
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
                                                                            SPUtil.put(SignEmailActivity.this, "Token", token);
                                                                            SPUtil.put(SignEmailActivity.this, "email", email);
                                                                            SPUtil.put(SignEmailActivity.this, "name_one", name); //名字
                                                                            SPUtil.put(SignEmailActivity.this, "you_usid", id);
                                                                            JSONArray coupons = data2.getJSONArray("coupons");

                                                                            for (int i = 0; i < coupons.length(); i++) {
                                                                                SignBean bean = GSonUtil.parseGson(coupons.getString(i), SignBean.class);
                                                                                initTokenFromDesc(bean);
                                                                                listean.add(bean);
                                                                            }
                                                                            int tong = 0;
                                                                            int jin = 0;
                                                                            int yin = 0;
                                                                            int zuan = 0;
                                                                            for (int j = 0; j < listean.size(); j++) {
                                                                                int couponType = listean.get(j).getCouponType();
                                                                                if (couponType == 1) {
                                                                                    SPUtil.put(SignEmailActivity.this,"brozen_desc",listean.get(j).getDesc());
                                                                                    tong++;
                                                                                } else if (couponType == 2) {
                                                                                    SPUtil.put(SignEmailActivity.this,"silver_desc",listean.get(j).getDesc());
                                                                                    yin++;
                                                                                } else if (couponType == 3) {
                                                                                    SPUtil.put(SignEmailActivity.this,"gold_desc",listean.get(j).getDesc());
                                                                                    jin++;
                                                                                } else if (couponType == 4) {
                                                                                    SPUtil.put(SignEmailActivity.this,"diamond_desc",listean.get(j).getDesc());
                                                                                    zuan++;
                                                                                }
                                                                            }
                                                                            SPUtil.put(SignEmailActivity.this, "My_tong", tong);
                                                                            SPUtil.put(SignEmailActivity.this, "My_yin", yin);
                                                                            SPUtil.put(SignEmailActivity.this, "My_jin", jin);
                                                                            SPUtil.put(SignEmailActivity.this, "My_zuan", zuan);

                                                                            ToastUtil.showgravity(SignEmailActivity.this,getResources().getString(R.string.login_yes));
                                                                            finish();
                                                                            if(RegisterActivity.instance!=null) {

                                                                                RegisterActivity.instance.finish();//关闭上一个页面
                                                                            }
                                                                            boolean showUpgradeToToken = data2.getBoolean("showUpgradeToToken");
                                                                            if (showUpgradeToToken){
                                                                                browsDialog = new TokenDialog(SignEmailActivity.this, R.style.box_dialog);
                                                                                browsDialog.setCanceledOnTouchOutside(false);
                                                                                browsDialog.show();
                                                                                browsDialog.setCancelable(false);
                                                                                SPUtil.remove(SignEmailActivity.this,"showUpgradeToToken");
                                                                            }
                                                                            /**
                                                                             * 发消息签到
                                                                             * */
                                                                            EventBusCarrier carrier=new EventBusCarrier();
                                                                            TokenExchangeBean bean=new TokenExchangeBean();
                                                                            if (card_flag==1){
                                                                                bean.setSuToken(4);
                                                                            }else if (my_flag==2){
                                                                                bean.setSuToken(5);
                                                                            }
                                                                            bean.setSuToken(1);
                                                                            bean.setFlagToken(1);
                                                                            bean.setFlagCoupn(1);
                                                                            carrier.setEventType(EVENT_TYPE_SIGN);
                                                                            carrier.setObject(bean);
                                                                            EventBus.getDefault().post(carrier);
                                                                            if(LoginMeActivity.instance!=null) {

                                                                                LoginMeActivity.instance.finish();
                                                                            }

                                                                        } else if (jsonObject2.getString("code").equals("5008")){
                                                                            ToastUtil.showgravity(SignEmailActivity.this, getResources().getString(R.string.login_email));
                                                                        }else if (jsonObject2.getString("code").equals("5001")) {
                                                                            ToastUtil.showgravity(SignEmailActivity.this, getResources().getString(R.string.name_lo));
                                                                        } else {
                                                                            ToastUtil.showgravity(SignEmailActivity.this, getResources().getString(R.string.fan));
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
                                    ToastUtil.showgravity(SignEmailActivity.this, getResources().getString(R.string.fan));
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
    }

    private void initTokenFromDesc(SignBean bean) {
        if (bean==null){
            return;
        }
        bean.setDesc(getDesc(bean.getTokenFrom()));
    }
    private String getDesc(int tokenFrom){
        String tokenFromDesc="";
        switch (tokenFrom){
            case TOKEN_FROM_EXCHANGE:
                tokenFromDesc=getString(R.string.reward_from_exchange);
                break;
            case TOKEN_FROM_COMMENT:
                tokenFromDesc=getString(R.string.reward_from_comment);
                break;
            case TOKEN_FROM_REGISTER:
                tokenFromDesc=getString(R.string.reward_from_register);
                break;
            case TOKEN_FROM_REFERRAL:
                tokenFromDesc=getString(R.string.reward_from_reffer);
                break;
            case TOKEN_FROM_INVITED:
                tokenFromDesc=getString(R.string.reward_from_reffer);
                break;
            case TOKEN_FROM_EVENT:
                tokenFromDesc=getString(R.string.reward_from_event);
                break;
            case TOKEN_FROM_DAILY_SIGN:
                tokenFromDesc=getString(R.string.reward_from_sign);
                break;
            case TOKEN_FROM_VERSION_UPGRADE:
                tokenFromDesc=getString(R.string.reward_from_upgrade);
                break;
        }
        return tokenFromDesc;
    }

    //验证邀请码/活动码
    public void isCheckCode(String code){

        userId = (String) SPUtil.get(this, "you_usid", "");
        FormBody body = new FormBody.Builder()
                .add("code", code)
                .add("userId", userId)
                .build();
        Http.OkHttpPost(SignEmailActivity.this, url + UrlUtil.Check_Code, body, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                Log.d("Check_Code",result+"=="+url + UrlUtil.Check_Code);
                try {
                    JSONObject object = new JSONObject(result);
                    String code1 = object.getString("code");
                    if (code1.equals("0")){
                        getLogin(code);
                    }else {
                        dialog = new InviteDialog(SignEmailActivity.this,R.layout.invite_dingdiglog,code1,onClickListener);
                        dialog.show();
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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.invie_positiveButton:
                    dialog.dismiss();
                    break;
                case R.id.invie_negativeButton:

                    getLogin("");
                    dialog.dismiss();
                    break;

            }
        }
    };

}
