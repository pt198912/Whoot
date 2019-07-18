package com.app.whoot.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
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
import com.app.whoot.bean.TokenExchangeBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.ui.view.custom.ClearEditText;
import com.app.whoot.util.AppUtil;
import com.app.whoot.util.FireBaseEventKey;
import com.app.whoot.util.FirebaseReportUtils;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

import static com.app.whoot.util.Constants.EVENT_TYPE_SHOW_FIND_FRAG;
import static com.app.whoot.util.Constants.EVENT_TYPE_SIGN;

/**
 * Created by Sunrise on 6/4/2018.
 */

public class LoginMeActivity extends BaseActivity {
    @BindView(R.id.login_me_img)
    LinearLayout loginMeImg;
    @BindView(R.id.frag_me_facce)
    LinearLayout fragMeFacce;
    @BindView(R.id.login_me_button)
    LoginButton LoginButton;
    @BindView(R.id.log_login)
    TextView log_login;
    @BindView(R.id.log_sign_up)
    TextView log_sign_up;
    @BindView(R.id.log_login_google)
    LinearLayout log_login_google;
    @BindView(R.id.gift_login_txt)
    TextView gift_login_txt;
    @BindView(R.id.login_login)
    TextView loginLogin;
    @BindView(R.id.login_sign_up)
    TextView loginSignUp;
    @BindView(R.id.login_login_facce)
    LinearLayout loginLoginFacce;
    @BindView(R.id.login_phone_new)
    ClearEditText loginPhoneNew;
    @BindView(R.id.login_pas_new)
    ClearEditText loginPasNew;
    @BindView(R.id.login_new_img)
    ImageView loginNewImg;
    @BindView(R.id.login_time_new)
    TextView loginTimeNew;
    @BindView(R.id.login_psw_new)
    TextView loginPswNew;
    @BindView(R.id.login_sign_new)
    TextView loginSignNew;
    @BindView(R.id.login_face_new)
    LinearLayout loginFaceNew;
    @BindView(R.id.frag_ly)
    LinearLayout fragLy;

    private CallbackManager callbackManager;
    private String gen;
    private String urlUtil;
    private int RC_SIGN_IN = 2;
    private FirebaseAuth mAuth;
    public static LoginMeActivity instance;
    public int flag_img=0;

    private String name;
    private String email1;
    private String gender;
    private String imgUrl;
    private String tel;
    private int login_flag;
    private int card_flag;
    private int my_flag;
    private FormBody body;

    @Override
    public int getLayoutId() {
        return R.layout.login_me;
    }
    private boolean mStartFromSettingsAct;
    @Override
    public void onCreateInit() {
        mStartFromSettingsAct=getIntent().getBooleanExtra("startFromSetingsAct",false);
        loginTimeNew.getBackground().setAlpha(100);
        urlUtil = (String) SPUtil.get(LoginMeActivity.this, "URL", "");
        instance = this;
        gift_login_txt.setText(getResources().getString(R.string.login_policy));
        login_flag = getIntent().getIntExtra("Login_flag", 0);
        card_flag = getIntent().getIntExtra("card_flag", 0);
        my_flag = getIntent().getIntExtra("my_flag", 0);
        SpannableString clickString = new SpannableString("  " + getResources().getString(R.string.login_terms) + "  ");
        gift_login_txt.setMovementMethod(LinkMovementMethod.getInstance());
        clickString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent intent3 = new Intent(LoginMeActivity.this, WebViewActivity.class);
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
        gift_login_txt.append(clickString);
        gift_login_txt.append(new SpannableString(getResources().getString(R.string.and)));
        SpannableString click = new SpannableString("  " + getResources().getString(R.string.yinsi) + "  ");
        click.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent intent4 = new Intent(LoginMeActivity.this, WebViewActivity.class);
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
        gift_login_txt.append(click);

        //getActivityComponent().inject(this);
        callbackManager = CallbackManager.Factory.create();
        //　还要注册一个监听：
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getLoginInfo(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        mAuth = FirebaseAuth.getInstance();
        init();
        setTextChangedListener();

    }
    public void setTextChangedListener(){
        if (loginPasNew.getText().toString().trim().length()>=6){

            loginTimeNew.getBackground().setAlpha(255);
            loginTimeNew.setClickable(true);
        }else {

            loginTimeNew.getBackground().setAlpha(100);
            loginTimeNew.setClickable(false);
        }

        loginPhoneNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length>0){
                    if (loginPasNew.getText().toString().trim().length()>=6){

                        loginTimeNew.getBackground().setAlpha(255);
                        loginTimeNew.setClickable(true);
                    }else {

                        loginTimeNew.getBackground().setAlpha(100);
                        loginTimeNew.setClickable(false);
                    }
                }else {
                    loginTimeNew.getBackground().setAlpha(100);
                    loginTimeNew.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loginPasNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length>=6){
                    if (loginPhoneNew.getText().toString().trim().length()>0){
                        loginTimeNew.getBackground().setAlpha(255);
                        loginTimeNew.setClickable(true);
                    }else {
                        loginTimeNew.getBackground().setAlpha(100);
                        loginTimeNew.setClickable(false);
                    }
                    loginNewImg.setVisibility(View.VISIBLE);
                }else {
                    loginNewImg.setVisibility(View.GONE);
                    loginTimeNew.getBackground().setAlpha(100);
                    loginTimeNew.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void init() {
        LoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                Log.d("LoginActivity", "facebook登录成功了" + loginResult.getAccessToken().getToken());
                //获取登录信息
                getLoginInfo(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                //  Toast.makeText(LoginMeActivity.this, "facebook登录取消了", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onError(FacebookException error) {
                //  Toast.makeText(LoginMeActivity.this, "facebook登录失败了"+error.toString(), Toast.LENGTH_SHORT).show();

                Log.d("LoginActivity", "facebook登录失败了" + error.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            /*Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);*/
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("fhfhfhf", "Google sign in failed", e);
                ToastUtil.showgravity(this, getResources().getString(R.string.lo));
                // ...
            }
        } else {
            //facebook回调
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        // [START_EXCLUDE silent]

        // [END_EXCLUDE]
        String idToken = acct.getIdToken();
        FormBody body = new FormBody.Builder()
                .add("type", String.valueOf(5))
                .add("idToken", idToken)
                .build();
        loading(getResources().getString(R.string.listview_loading));
        Http.OkHttpPostOne(this, urlUtil + UrlUtil.login, body, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                Log.d("dsfgdgdf", urlUtil + UrlUtil.login + "==" + result);
                try {
                    dissLoad();
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("0")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject account = data.getJSONObject("account");

                        String name_one = account.getString("name");
                        String Token = data.getString("token");
                        String photo1 = account.getString("photo");
                        String id1 = account.getString("id");
                        String email = account.getString("email");

                        SPUtil.put(LoginMeActivity.this, "Token", Token);
                        SPUtil.put(LoginMeActivity.this, "photo", photo1); //头像
                        SPUtil.put(LoginMeActivity.this, "name_one", name_one); //名字
                        SPUtil.put(LoginMeActivity.this, "email", email);
                        SPUtil.put(LoginMeActivity.this, "you_usid", id1);
                        finish();
                    } else {
                        // Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
                        // ToastUtil.showgravity(getActivity(), getActivity().getResources().getString(R.string.facebook_one));
                    }

                    Log.d("登录成功", result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                dissLoad();
            }
        });
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginMeActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("dgfg", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            try {
                                updateUI(user);
                            } catch (ApiException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // If sign in fails, display a message to the user.


                            try {
                                updateUI(null);
                            } catch (ApiException e) {
                                e.printStackTrace();
                            }
                        }

                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
    }

    private void updateUI(FirebaseUser user) throws ApiException {

        if (user != null) {
            user.getEmail();
            user.getUid();


        } else {

        }
    }

    /**
     * 获取登录信息
     *
     * @param accessToken
     */
    public void getLoginInfo(AccessToken accessToken) {


        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object != null) {
                    String id = object.optString("id");   //比如:1565455221565
                    String name = object.optString("name");  //比如：Zhang San
                    String gender = object.optString("gender");  //性别：比如 male （男）  female （女）
                    String emali = object.optString("email");  //邮箱：比如：56236545@qq.com

                    SPUtil.put(LoginMeActivity.this, "email", emali);

                    //获取用户头像
                    JSONObject object_pic = object.optJSONObject("picture");
                    JSONObject object_data = object_pic.optJSONObject("data");
                    String photo = object_data.optString("url");
                    SPUtil.put(LoginMeActivity.this, "photo", photo);
                    //获取地域信息
                    String locale = object.optString("locale");   //zh_CN 代表中文简体

                    Log.d("hhahha", object.toString());

                    String token = accessToken.getToken();

                    SetEmailLogn(2,token);   //facebook 登录

                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,picture,locale,updated_time,timezone,age_range,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onBackPressed() {

    }

    @OnClick({R.id.login_me_img, R.id.frag_me_facce, R.id.log_login_google, R.id.log_login, R.id.log_sign_up,R.id.login_new_img,R.id.login_time_new,
            R.id.login_sign_new,R.id.login_face_new,R.id.login_psw_new})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_me_img:
                if (mStartFromSettingsAct) {
                    EventBusCarrier carrier = new EventBusCarrier();
                    carrier.setEventType(EVENT_TYPE_SHOW_FIND_FRAG);
                    EventBus.getDefault().post(carrier);
                    finish();
                } else {
                    finish();
                }
                break;
            case R.id.frag_me_facce:

                break;
            case R.id.log_login_google:
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestIdToken("516500673783-h0c5ggiod363hiu6e7fen2k1or9ftlmc.apps.googleusercontent.com")
                        .build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.log_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.log_sign_up:
                Intent intent6 = new Intent(this, SignActivity.class);
                startActivity(intent6);
                break;
            case R.id.login_new_img:
                if (flag_img==0){
                    flag_img=1;
                    loginNewImg.setBackgroundResource(R.drawable.login_icon_show);
                    loginPasNew.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else if (flag_img==1){
                    flag_img=0;
                    loginNewImg.setBackgroundResource(R.drawable.login_icon_hide);
                    loginPasNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.login_time_new:
                String Phonetrim = loginPhoneNew.getText().toString().trim();
                String Pastrim = loginPasNew.getText().toString().trim();
                if (Phonetrim.indexOf("@")!=-1){ //邮箱登录
                    SetEmailLogn(4,"");

                }else { //手机号登录 7
                    SetEmailLogn(7,"");
                }

                break;
            case R.id.login_psw_new:  //忘记密码
                Intent intent11 = new Intent(LoginMeActivity.this, RegisterActivity.class);
                intent11.putExtra("Forget",1);
                startActivity(intent11);
                break;
            case R.id.login_sign_new:  //注册
                Intent intent10 = new Intent(LoginMeActivity.this, RegisterActivity.class);
                intent10.putExtra("Forget",2);
                intent10.putExtra("card_flag",card_flag);
                intent10.putExtra("my_flag",my_flag);
                startActivity(intent10);

                getFirebaseEvent();
                break;
            case R.id.login_face_new:
                LoginManager.getInstance().logInWithReadPermissions(LoginMeActivity.this, Arrays.asList("public_profile"));

                getFirebaseEvent();
                break;

        }
    }

    private void getFirebaseEvent(){
        //埋点
        Bundle bundle1 = new Bundle();
        bundle1.putString("type","CLICK_REG");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.REGISTER_START,bundle1);
    }
    /**
     * 区分是facebook还是邮箱/手机号登录
     *
     * */
    private void SetEmailLogn(int type,String accToken) {
        String email = loginPhoneNew.getText().toString().trim();
        String password = loginPasNew.getText().toString().trim();

        if (type!=2){

            Http.OkHttpGet(LoginMeActivity.this, urlUtil + UrlUtil.isemailreg + "?email=" + email, new Http.OnDataFinish() {
                @Override
                public void OnSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("code").equals("0")){

                            if (password.equals("")) {
                                ToastUtil.showgravity(LoginMeActivity.this, getResources().getString(R.string.logo_pass));
                                return;
                            }
                            getLogin(type,password);

                        }else {
                            ToastUtil.showgravity(LoginMeActivity.this, getResources().getString(R.string.logo_null));
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
            getLogin(type,accToken);

        }




    }

    @Override
    protected void onResume() {
        super.onResume();
        clearTransparentStatusBar();
    }

    private void getLogin(int type, String Token){
        String md5_password = AppUtil.MD5(Token);
        String email = loginPhoneNew.getText().toString().trim();
        String token = FirebaseInstanceId.getInstance().getToken();
        if (type==2){
            body = new FormBody.Builder()
                    .add("type", String.valueOf(2))
                    .add("accessToken", Token)
                    .build();
        }else {
            body = new FormBody.Builder()
                    .add("type", String.valueOf(type))
                    .add("email", email)
                    .add("password", md5_password)
                    .add("mobile",email)
                    .add("notifyToken",token==null?"":token)
                    .build();
        }

        Http.OkHttpPost(LoginMeActivity.this, urlUtil + UrlUtil.login, body, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                Log.d("Logining", result + urlUtil + UrlUtil.login+"=="+token);
                try {
                    JSONObject jsonObject2 = new JSONObject(result);
                    if (jsonObject2.getString("code").equals("0")) {

                        JSONObject data2 = jsonObject2.getJSONObject("data");
                        JSONObject account = data2.getJSONObject("account");
                        try {
                            name = account.getString("name");
                        } catch (Exception e) {
                            name = "";
                        }
                        try {
                            long couponCd = account.getLong("couponCd");

                            SPUtil.put(LoginMeActivity.this, "downco", String.valueOf(couponCd));
                        } catch (Exception e) {
                        }
                        String id = account.getString("id");
                        int type1 = account.getInt("type");
                        //埋点
                        if (type1==2){
                            reportPageFb();
                        }else if (type1==3){
                            reportPageEmail();
                        }else if (type1==6){
                            reportPageMobile();
                        }
                        SPUtil.put(LoginMeActivity.this,"type_login",type1);
                        String token = data2.getString("token");
                        boolean isBindPwd = data2.getBoolean("isBindPwd");
                        SPUtil.put(LoginMeActivity.this,"bindpwd",isBindPwd);
                        boolean firstLogin = data2.getBoolean("firstLogin");
                        SPUtil.put(LoginMeActivity.this,"firstLogin",firstLogin);
                        try {
                            email1 = account.getString("email");
                        } catch (Exception e) {
                            email1 = "";
                        }
                        try {
                            imgUrl = account.getString("photo");
                        } catch (Exception e) {
                            imgUrl = "";
                        }
                        try {
                            gender = account.getString("gender");
                        } catch (Exception e) {
                            gender = "";
                        }
                        try {
                            tel = account.getString("tel");
                        } catch (Exception e) {
                            tel = "";
                        }
                        SPUtil.put(LoginMeActivity.this, "photo", imgUrl);
                        SPUtil.put(LoginMeActivity.this, "Token", token);
                        SPUtil.put(LoginMeActivity.this, "name_one", name); //名字
                        SPUtil.put(LoginMeActivity.this, "you_usid", id);
                        SPUtil.put(LoginMeActivity.this, "email", email1);
                        SPUtil.put(LoginMeActivity.this, "gen", gender);
                        SPUtil.put(LoginMeActivity.this,"you_tel",tel);

                        try{
                            boolean showUpgradeToToken = data2.getBoolean("showUpgradeToToken");
                            SPUtil.put(LoginMeActivity.this,"showUpgradeToToken",showUpgradeToToken);
                        }catch (Exception e){}
                        /**
                         * 签到接口
                         * */
                        EventBusCarrier carrier=new EventBusCarrier();
                        TokenExchangeBean bean=new TokenExchangeBean();
                        if (login_flag==1){
                            bean.setSuToken(2);
                        }else {
                            bean.setSuToken(1);
                        }
                        bean.setFlagToken(1);
                        bean.setFlagCoupn(1);
                        carrier.setEventType(EVENT_TYPE_SIGN);
                        carrier.setObject(bean);
                        EventBus.getDefault().post(carrier);
                        finish();
                    } else if (jsonObject2.getString("code").equals("1006")) {
                        ToastUtil.showgravity(LoginMeActivity.this, getResources().getString(R.string.pasw_lo));
                    } else if (jsonObject2.getString("code").equals("1001")) {
                        ToastUtil.showgravity(LoginMeActivity.this, getResources().getString(R.string.nofff));
                    } else if (jsonObject2.getString("code").equals("5001")){
                        ToastUtil.showgravity(LoginMeActivity.this, getResources().getString(R.string.logo_null));
                    }else if (jsonObject2.getString("code").equals("5017")){
                        ToastUtil.showgravity(LoginMeActivity.this, getResources().getString(R.string.phone_no_login));
                    }else if (jsonObject2.getString("code").equals("1036")){

                    }else {
                        ToastUtil.showgravity(LoginMeActivity.this, getResources().getString(R.string.fan));
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
    //埋点
    private void reportPageMobile() {
        Bundle b=new Bundle();
        b.putString("type","LOGIN");
        b.putString("loginType","FB");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.EVENT_LOGIN,b);
    }

    private void reportPageEmail() {
        Bundle b=new Bundle();
        b.putString("type","LOGIN");
        b.putString("loginType","MAIL");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.EVENT_LOGIN,b);
    }

    private void reportPageFb() {
        Bundle b=new Bundle();
        b.putString("type","LOGIN");
        b.putString("loginType","MOBILE");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.EVENT_LOGIN,b);
    }

}
