package com.app.whoot.ui.activity;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.CouponBean;
import com.app.whoot.bean.ExchangeCodeBean;
import com.app.whoot.bean.SignBean;
import com.app.whoot.bean.TokenExchangeBean;
import com.app.whoot.modle.http.Http;
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

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

public class SignMenActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.smen_name)
    ClearEditText smenName;
    @BindView(R.id.smen_pas)
    ClearEditText smenPas;
    @BindView(R.id.smen_pas_img)
    ImageView smenPasImg;
    @BindView(R.id.smen_pasword)
    ClearEditText smenPasword;
    @BindView(R.id.smen_pasword_img)
    ImageView smenPaswordImg;
    @BindView(R.id.smen_time)
    TextView verifTime;
    @BindView(R.id.smen_txt)
    TextView smen_txt;
    @BindView(R.id.smen_invate)
    ClearEditText smen_invate;


    private int img_flag=0;
    private int img_flag_psw=0;
    private int pas_length=0;
    private int pasword_length=0;
    private String verify_code;
    private String mobile;
    private String url;
    private List<SignBean> listean = new ArrayList<SignBean>();
    private TokenDialog browsDialog;
    private int card_flag;
    private int my_flag;
    private String userId;
    private InviteDialog dialog;

    @Override
    public int getLayoutId() {
        return R.layout.signmen_activi;
    }

    @Override
    public void onCreateInit() {
        titleBackTitle.setText(getResources().getString(R.string.sign_up));
        verifTime.getBackground().setAlpha(100);
        Intent intent = getIntent();
        verify_code = intent.getStringExtra("verify_Code");
        mobile = intent.getStringExtra("mobile");
        card_flag = intent.getIntExtra("card_flag", 0);
        my_flag = intent.getIntExtra("my_flag", 0);
        url = (String) SPUtil.get(this, "URL", "");
        smenName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        smen_invate.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        getPolicy();
        smenPas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                pas_length=length;
                if (length>0){
                    if (pasword_length>0&&smenName.toString().trim().length()>0){

                        verifTime.getBackground().setAlpha(255);
                        verifTime.setClickable(true);
                    }else {


                        verifTime.getBackground().setAlpha(100);
                        verifTime.setClickable(false);
                    }

                    smenPasImg.setVisibility(View.VISIBLE);
                }else {
                    smenPasImg.setVisibility(View.GONE);

                    verifTime.setClickable(false);
                    verifTime.getBackground().setAlpha(100);

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
        smenPasword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                pasword_length=length;
                if (length>0){

                    if (pas_length>0&&smenName.toString().trim().length()>0){

                        verifTime.getBackground().setAlpha(255);
                        verifTime.setClickable(true);
                    }else {


                        verifTime.getBackground().setAlpha(100);
                        verifTime.setClickable(false);
                    }
                    smenPaswordImg.setVisibility(View.VISIBLE);
                }else {
                    smenPaswordImg.setVisibility(View.GONE);

                    verifTime.setClickable(false);
                    verifTime.getBackground().setAlpha(100);
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
        smenName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length>0){
                    if (pas_length>0&&pasword_length>0){


                        verifTime.getBackground().setAlpha(255);
                        verifTime.setClickable(true);
                    }else {

                        verifTime.getBackground().setAlpha(100);
                        verifTime.setClickable(false);
                    }
                }else {

                    verifTime.getBackground().setAlpha(100);
                    verifTime.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        reportPageShown();
    }
    //埋点
    private void reportPageShown() {
        Bundle b=new Bundle();
        b.putString("type","SHOW_RMOBILE_INFO");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.SHOW_REGISTER_CELLPHONE_INFO,b);
    }

    public void getPolicy(){
        smen_txt.setText(getResources().getString(R.string.sign_men));
        SpannableString clickString = new SpannableString("  "+getResources().getString(R.string.login_terms)+"  ");
        smen_txt.setMovementMethod(LinkMovementMethod.getInstance());
        clickString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent intent3 = new Intent(SignMenActivity.this, WebViewActivity.class);
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
        smen_txt.append(clickString);
        smen_txt.append(new SpannableString(getResources().getString(R.string.and)));
        SpannableString click = new SpannableString("  "+getResources().getString(R.string.yinsi)+"  ");
        click.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent intent4 = new Intent(SignMenActivity.this, WebViewActivity.class);
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
        smen_txt.append(click);
    }

    @OnClick({R.id.title_back_fl, R.id.smen_pas_img, R.id.smen_pasword_img, R.id.smen_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.smen_pas_img:
                if (img_flag==0){
                    img_flag=1;
                    smenPasImg.setBackgroundResource(R.drawable.login_icon_show);
                    smenPas.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else if (img_flag==1){
                    img_flag=0;
                    smenPasImg.setBackgroundResource(R.drawable.login_icon_hide);
                    smenPas.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.smen_pasword_img:
                if (img_flag_psw==0){
                    img_flag_psw=1;
                    smenPaswordImg.setBackgroundResource(R.drawable.login_icon_show);
                    smenPasword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else if (img_flag_psw==1){
                    img_flag_psw=0;
                    smenPaswordImg.setBackgroundResource(R.drawable.login_icon_hide);
                    smenPasword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.smen_time:

                String invatetrim = smen_invate.getText().toString().trim().toUpperCase();
                isCheckCode(invatetrim);
                reportPageBymen();
                break;
        }
    }
    //埋点
    private void reportPageBymen() {
        Bundle b=new Bundle();
        b.putString("type","CLICK_REG_SUBMIT");
        b.putString("regType","MOBILE");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.REGISTER_GO,b);
    }

    //验证邀请码/活动码
    public void isCheckCode(String code){

        userId = (String) SPUtil.get(this, "you_usid", "");
        FormBody body = new FormBody.Builder()
                .add("code", code)
                .add("userId", userId)
                .build();
        Http.OkHttpPost(SignMenActivity.this, url + UrlUtil.Check_Code, body, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String code1 = object.getString("code");
                    if (code1.equals("0")){
                        getLogin(code);
                    }else {
                        dialog = new InviteDialog(SignMenActivity.this,R.layout.invite_dingdiglog,code1,onClickListener);
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
                case R.id.invie_positiveButton:  //确认
                    dialog.dismiss();
                    break;
                case R.id.invie_negativeButton:
                    getLogin("");
                    dialog.dismiss();
                    break;

            }
        }
    };

    private void getLogin(String code) {
        String Nametrim = smenName.getText().toString().trim();
        String Pastrim = smenPas.getText().toString().trim();
        String Paswordtrim = smenPasword.getText().toString().trim();
        if (!AppUtil.isContainAll(Paswordtrim)){
            ToastUtil.showgravity(SignMenActivity.this,getResources().getString(R.string.should));
            return;
        }
        if (!AppUtil.isContainAll(Pastrim)){
            ToastUtil.showgravity(SignMenActivity.this,getResources().getString(R.string.should));
            return;
        }
        if (Nametrim.length()>50){

        }
        if (Pastrim.equals(Paswordtrim)){

            Http.OkHttpGet(SignMenActivity.this, url + UrlUtil.check_name + "?userName=" + Nametrim, new Http.OnDataFinish() {
                @Override
                public void OnSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("code").equals("0")){
                            boolean data = jsonObject.getBoolean("data");
                            if (data){
                                ToastUtil.showgravity(SignMenActivity.this, getResources().getString(R.string.zhangha));
                                return;
                            }else {
                                FormBody body = new FormBody.Builder()
                                        .add("type", String.valueOf(6))
                                        .add("verifyCode",verify_code)
                                        .add("name",Nametrim)
                                        .add("password",AppUtil.MD5(Paswordtrim))
                                        .add("mobile",mobile)
                                        .add("invitationCode",code.toUpperCase())
                                        .build();
                                Http.OkHttpPost(SignMenActivity.this, url + UrlUtil.login, body, new Http.OnDataFinish() {
                                    @Override
                                    public void OnSuccess(String result) {
                                        Log.d("logining",result+"=="+url + UrlUtil.login);
                                        try {
                                            JSONObject object = new JSONObject(result);
                                            if (object.getString("code").equals("0")){
                                                JSONObject data2 = object.getJSONObject("data");
                                                JSONObject account = data2.getJSONObject("account");

                                                String name = account.getString("name");
                                                String id = account.getString("id");
                                                String token = data2.getString("token");
                                                String tel = account.getString("tel");
                                                SPUtil.put(SignMenActivity.this, "Token", token);
                                                SPUtil.put(SignMenActivity.this, "name_one", name); //名字
                                                SPUtil.put(SignMenActivity.this, "you_usid", id);
                                                SPUtil.put(SignMenActivity.this,"you_tel",tel);
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
                                                        SPUtil.put(SignMenActivity.this,"brozen_desc",listean.get(j).getDesc());
                                                        tong++;
                                                    } else if (couponType == 2) {
                                                        SPUtil.put(SignMenActivity.this,"silver_desc",listean.get(j).getDesc());
                                                        yin++;
                                                    } else if (couponType == 3) {
                                                        SPUtil.put(SignMenActivity.this,"gold_desc",listean.get(j).getDesc());
                                                        jin++;
                                                    } else if (couponType == 4) {
                                                        SPUtil.put(SignMenActivity.this,"diamond_desc",listean.get(j).getDesc());
                                                        zuan++;
                                                    }
                                                }
                                                SPUtil.put(SignMenActivity.this, "My_tong", tong);
                                                SPUtil.put(SignMenActivity.this, "My_yin", yin);
                                                SPUtil.put(SignMenActivity.this, "My_jin", jin);
                                                SPUtil.put(SignMenActivity.this, "My_zuan", zuan);

                                                ToastUtil.showgravity(SignMenActivity.this,getResources().getString(R.string.login_yes));
                                                finish();
                                                if(RegisterActivity.instance!=null) {

                                                    RegisterActivity.instance.finish();//关闭上一个页面
                                                }
                                                boolean showUpgradeToToken = data2.getBoolean("showUpgradeToToken");
                                                if (showUpgradeToToken){
                                                    browsDialog = new TokenDialog(SignMenActivity.this, R.style.box_dialog);
                                                    browsDialog.setCanceledOnTouchOutside(false);
                                                    browsDialog.show();
                                                    browsDialog.setCancelable(false);
                                                    SPUtil.remove(SignMenActivity.this,"showUpgradeToToken");
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
                                                bean.setFlagToken(1);
                                                bean.setFlagCoupn(1);
                                                carrier.setEventType(EVENT_TYPE_SIGN);
                                                carrier.setObject(bean);
                                                EventBus.getDefault().post(carrier);
                                                if(LoginMeActivity.instance!=null) {

                                                    LoginMeActivity.instance.finish();//关闭上一个页面
                                                }


                                            }else if (object.getString("code").equals("5016")){
                                                ToastUtil.showgravity(SignMenActivity.this,getResources().getString(R.string.phone_now));
                                            }else if (object.getString("code").equals("5001")) {
                                                ToastUtil.showgravity(SignMenActivity.this, getResources().getString(R.string.name_lo));
                                            } else if (object.getString("code").equals("5001")){

                                            }else {
                                                ToastUtil.showgravity(SignMenActivity.this, getResources().getString(R.string.fan));
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }else {
            ToastUtil.showgravity(SignMenActivity.this,getResources().getString(R.string.match));
        }


    }

    private void initTokenFromDesc(Object obj) {
        if(obj==null){
            return;
        }
        if(obj instanceof SignBean){
            SignBean bean=(SignBean)obj;
            bean.setDesc(getDesc(bean.getTokenFrom()));
        }
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
                tokenFromDesc=getString(R.string.reward_from_invited);
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

}
