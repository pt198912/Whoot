package com.app.whoot.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.base.fragment.BaseFragment;
import com.app.whoot.bean.TokenExchangeBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.BuxActivity;
import com.app.whoot.ui.activity.ChangeActivity;
import com.app.whoot.ui.activity.CommentActivity;
import com.app.whoot.ui.activity.CouponActivity;
import com.app.whoot.ui.activity.FavourActivity;
import com.app.whoot.ui.activity.InViteActivity;
import com.app.whoot.ui.activity.LoginMeActivity;
import com.app.whoot.ui.activity.ProfileActivity;
import com.app.whoot.ui.activity.RegisterActivity;
import com.app.whoot.ui.activity.RewardActivity;
import com.app.whoot.ui.activity.SettingActivity;
import com.app.whoot.ui.activity.SignActivity;
import com.app.whoot.ui.activity.WebViewActivity;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.ui.activity.main.TransActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.ui.view.custom.ClearEditText;
import com.app.whoot.ui.view.custom.ShapeImageView;
import com.app.whoot.ui.view.dialog.LoginCoupDialog;
import com.app.whoot.ui.view.dialog.TokenDialog;
import com.app.whoot.ui.view.popup.LoginPopupWindow;
import com.app.whoot.util.AppUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.bumptech.glide.Glide;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.FormBody;

import static com.app.whoot.util.Constants.EVENT_TYPE_SIGN;

/**
 * Created by Sunrise on 4/2/2018.
 * 我的
 */

public class MyFragment extends BaseFragment {

    @BindView(R.id.my_name)
    TextView myName;
    @BindView(R.id.exchange)
    LinearLayout exchange;
    @BindView(R.id.coupon)
    LinearLayout coupon;
    @BindView(R.id.comment)
    LinearLayout comment;
    @BindView(R.id.frag_my_ly)
    LinearLayout fragMyLy;
    @BindView(R.id.my_round_too)
    ShapeImageView ShapeImageView;
    @BindView(R.id.mylogin_ly)
    LinearLayout mylogin_ly;
    @BindView(R.id.my_login)
    TextView my_login;
    @BindView(R.id.my_sign_up)
    TextView my_sign_up;
    @BindView(R.id.my_login_button)
    LoginButton my_login_button;
    @BindView(R.id.my_login_facce)
    LinearLayout my_login_facce;
    @BindView(R.id.my_login_google)
    LinearLayout my_login_google;
    @BindView(R.id.my_login_txt)
    TextView my_login_txt;
    @BindView(R.id.my_ly)
    LinearLayout my_ly;
    @BindView(R.id.favourite)
    LinearLayout favourite;
    @BindView(R.id.bux)
    LinearLayout bux;
    @BindView(R.id.frag_my_bux)
    TextView frag_my_bux;
    @BindView(R.id.my_round_too_ph)
    ImageView my_round_too_ph;
    @BindView(R.id.my_set_ly)
    LinearLayout my_set_ly;
    @BindView(R.id.my_ques)
    LinearLayout my_ques;
    @BindView(R.id.my_phone_new)
    ClearEditText myPhoneNew;
    @BindView(R.id.my_pas_new)
    ClearEditText myPasNew;
    @BindView(R.id.frag_new_img)
    ImageView fragNewImg;
    @BindView(R.id.frag_time_new)
    TextView fragTimeNew;
    @BindView(R.id.frag_psw_new)
    TextView fragPswNew;
    @BindView(R.id.frag_sign_new)
    TextView fragSignNew;
    @BindView(R.id.frag_face_new)
    ImageView fragFaceNew;
    @BindView(R.id.set_invite)
    LinearLayout set_invite;
    @BindView(R.id.invite)
    LinearLayout invite;



    private String token = "";
    private LoginPopupWindow popup;
    private String url;
    private int RC_SIGN_IN = 2;

    private FirebaseAuth mAuth;

    public GoogleSignInOptions gso;
    public GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    protected boolean isCreated = false;
    private LoginCoupDialog loginCoupDialog;
    private TokenDialog browsDialog;

    private static final String TAG = "MyFragment";
    private int flag_img=0;

    private String gen;
    private String urlUtil;
    private String name;
    private String email1;
    private String gender;
    private String imgUrl;
    private String tel;

    @Override
    public int getLayoutId() {
        return R.layout.frag_my;
    }

    @Override
    public void onViewCreatedInit() {

        fragTimeNew.getBackground().setAlpha(100);
        isCreated = true;
        //初始化谷歌登录服务
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()   //获取邮箱
                .requestId()      //获取id 号
                .requestIdToken("456212545785")  //获取token
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        url = (String) SPUtil.get(getActivity(), "URL", "");
        my_login_txt.setText(getResources().getString(R.string.login_by));
        SpannableString clickString = new SpannableString("  " + getResources().getString(R.string.login_terms) + "  ");
        my_login_txt.setMovementMethod(LinkMovementMethod.getInstance());
        clickString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent intent3 = new Intent(getActivity(), WebViewActivity.class);
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
        my_login_txt.append(clickString);
        my_login_txt.append(new SpannableString(getResources().getString(R.string.and)));
        SpannableString click = new SpannableString("  " + getResources().getString(R.string.yinsi) + "  ");
        click.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent intent4 = new Intent(getActivity(), WebViewActivity.class);
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
        my_login_txt.append(click);

        mAuth = FirebaseAuth.getInstance();

        setTextChangedListener();
    }
    public void setTextChangedListener(){
        if (myPasNew.getText().toString().trim().length()>0){

            fragTimeNew.getBackground().setAlpha(255);
            fragTimeNew.setClickable(true);
        }else {

            fragTimeNew.getBackground().setAlpha(100);
            fragTimeNew.setClickable(false);
        }
        myPhoneNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length>0){
                    if (myPasNew.getText().toString().trim().length()>0){

                        fragTimeNew.getBackground().setAlpha(255);
                        fragTimeNew.setClickable(true);
                    }else {

                        fragTimeNew.getBackground().setAlpha(100);
                        fragTimeNew.setClickable(false);
                    }
                }else {

                    fragTimeNew.getBackground().setAlpha(100);
                    fragTimeNew.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        myPasNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length>0){
                    if (myPhoneNew.getText().toString().trim().length()>0){
                        fragTimeNew.setBackgroundResource(R.drawable.login_deng);
                        fragTimeNew.setClickable(true);
                    }else {
                        fragTimeNew.setBackgroundResource(R.drawable.bg_btn_disabled);
                        fragTimeNew.setClickable(false);
                    }
                    fragNewImg.setVisibility(View.VISIBLE);
                }else {
                    fragNewImg.setVisibility(View.GONE);
                    fragTimeNew.setBackgroundResource(R.drawable.bg_btn_disabled);
                    fragTimeNew.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    @Override
    public void onStartInit() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isCreated) {
            return;
        }

        if (isVisibleToUser) {
            String name_one = (String) SPUtil.get(getActivity(), "name_one", "");
            String url_bux = (String) SPUtil.get(getActivity(), "URL", "");
            //用户登录
            token = (String) SPUtil.get(getActivity(), "Token", "");
            if (token.length() != 0) {
                int my_tong = (int) SPUtil.get(getActivity(), "My_tong", 0);
                if (my_tong != 0) {
                    loginCoupDialog = new LoginCoupDialog(getActivity(), onClickListener);
                    loginCoupDialog.show();
                    SPUtil.remove(getActivity(), "My_tong");
                    SPUtil.remove(getActivity(), "My_yin");
                    SPUtil.remove(getActivity(), "My_jin");
                    SPUtil.remove(getActivity(), "My_zuan");

                }

                myName.setText(name_one);
                my_ly.setVisibility(View.VISIBLE);
                mylogin_ly.setVisibility(View.GONE);

                Http.OkHttpGet(getActivity(), url_bux + UrlUtil.bux_sum, new Http.OnDataFinish() {
                    @Override
                    public void OnSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("code").equals("0")) {
                                String data = jsonObject.getString("data");
                                frag_my_bux.setText(data);
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

                my_ly.setVisibility(View.GONE);
//                mylogin_ly.setVisibility(View.VISIBLE);

            }

            String photo = (String) SPUtil.get(getActivity(), "photo", "");
            if (photo.length() == 0) {
                ShapeImageView.setVisibility(View.GONE);
                my_round_too_ph.setVisibility(View.VISIBLE);

            } else {
                ShapeImageView.setVisibility(View.VISIBLE);
                my_round_too_ph.setVisibility(View.GONE);
                Glide.with(getActivity())
                        .load(photo)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .error(R.drawable.headimg)
                        .into(ShapeImageView);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String name_one = (String) SPUtil.get(getActivity(), "name_one", "");
        String url_bux = (String) SPUtil.get(getActivity(), "URL", "");
        //用户登录
        token = (String) SPUtil.get(getActivity(), "Token", "");
        Log.d(TAG, "onResume: Token " + token);
        String photo = (String) SPUtil.get(getActivity(), "photo", "");
        if (token.length() != 0) {

            int my_tong = (int) SPUtil.get(getActivity(), "My_tong", 0);
            int my_yin = (int) SPUtil.get(getActivity(), "My_yin", 0);
            int my_jin = (int) SPUtil.get(getActivity(), "My_jin", 0);
            int my_zuan = (int) SPUtil.get(getActivity(), "My_zuan", 0);
            if (my_tong != 0||my_yin != 0||my_jin != 0||my_zuan != 0) {
                loginCoupDialog = new LoginCoupDialog(getActivity(),onClickListener);
                loginCoupDialog.show();
                SPUtil.remove(getActivity(), "My_tong");
                SPUtil.remove(getActivity(), "My_yin");
                SPUtil.remove(getActivity(), "My_jin");
                SPUtil.remove(getActivity(), "My_zuan");
            }
            boolean firstLogin = (boolean) SPUtil.get(getActivity(), "firstLogin", false);
            if (firstLogin){
                int type_login = (int) SPUtil.get(getActivity(), "type_login", 0);
                if (type_login==2){
                    Intent intent = new Intent(getActivity(), RewardActivity.class);
                    startActivity(intent);
                    SPUtil.remove(getActivity(),"type_login");
                    SPUtil.remove(getActivity(),"firstLogin");
                }
            }
            boolean showUpgradeToToken = (boolean) SPUtil.get(getActivity(), "showUpgradeToToken", false);
            if (showUpgradeToToken) {

                browsDialog = new TokenDialog(getActivity(), R.style.box_dialog);
                browsDialog.setCanceledOnTouchOutside(false);
                browsDialog.show();
                browsDialog.setCancelable(false);
                SPUtil.remove(getActivity(), "showUpgradeToToken");

            }
            myName.setText(name_one);
            my_ly.setVisibility(View.VISIBLE);
            mylogin_ly.setVisibility(View.GONE);
            Http.OkHttpGet(getActivity(), url_bux + UrlUtil.bux_sum, new Http.OnDataFinish() {
                @Override
                public void OnSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("code").equals("0")) {
                            String data = jsonObject.getString("data");
                            frag_my_bux.setText(data);
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

            my_ly.setVisibility(View.GONE);
//            mylogin_ly.setVisibility(View.VISIBLE);
        }
        if (photo.length() > 0) {

            ShapeImageView.setVisibility(View.VISIBLE);
            my_round_too_ph.setVisibility(View.GONE);
            Glide.with(getActivity())
                    .load(photo)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .error(R.drawable.headimg)
                    .into(ShapeImageView);
        } else {
            ShapeImageView.setVisibility(View.GONE);
            my_round_too_ph.setVisibility(View.VISIBLE);
        }

        FragmentActivity activity = getActivity();

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                    case R.id.login_box_ly:
                    if(loginCoupDialog!=null) {
                        loginCoupDialog.dismiss();
                    }
                    Activity act = getActivity();
                    if (act != null && act instanceof MainActivity) {
                        ((MainActivity) act).changeFragment(MainActivity.TAB_INDEX_CARD);
                    }
                    break;

            }
        }
    };


    @OnClick({R.id.coupon, R.id.comment, R.id.frag_my_ly, R.id.my_login, R.id.my_sign_up, R.id.my_login_google, R.id.my_login_facce,
            R.id.favourite, R.id.bux, R.id.my_set_ly, R.id.my_ques,R.id.frag_time_new,R.id.frag_psw_new,R.id.frag_sign_new,
            R.id.frag_new_img,R.id.frag_face_new,R.id.invite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.coupon:

                Intent intent1 = new Intent(getActivity(), CouponActivity.class);
                startActivity(intent1);
                break;
            case R.id.comment:
                Intent intent2 = new Intent(getActivity(), CommentActivity.class);
                startActivity(intent2);
                break;
            case R.id.frag_my_ly:  //个人信息
                Intent intent4 = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent4);
                break;
            case R.id.my_login:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.my_sign_up:
                Intent intent6 = new Intent(getActivity(), SignActivity.class);
                startActivity(intent6);
                break;
            case R.id.my_login_google:
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestIdToken("516500673783-h0c5ggiod363hiu6e7fen2k1or9ftlmc.apps.googleusercontent.com")
                        .build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

                break;
            case R.id.my_login_facce:

                break;
            case R.id.favourite:   //我的收藏
                Intent intent3 = new Intent(getActivity(), FavourActivity.class);
                startActivity(intent3);
                break;
            case R.id.bux:   //我的礼品
                Intent intent7 = new Intent(getActivity(), BuxActivity.class);
                startActivity(intent7);
                break;
            case R.id.my_set_ly:   //设定
                Intent intent8 = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent8);
                break;
            case R.id.my_ques:  //常见问题
                Intent intent9 = new Intent(getActivity(), WebViewActivity.class);
                intent9.putExtra("html", 5);
                startActivity(intent9);
                break;
            case R.id.frag_time_new:  //登录
                String Phonenew = myPhoneNew.getText().toString().trim();
                String PasNew = myPasNew.getText().toString().trim();
                if (Phonenew.indexOf("@")!=-1){ //邮箱登录
                    SetEmailLogn(4);

                }else { //手机号登录 7
                    SetEmailLogn(7);
                }

                break;
            case R.id.frag_psw_new:  //忘记密码
                Intent intent11 = new Intent(getActivity(), RegisterActivity.class);
                intent11.putExtra("Forget",1);
                startActivity(intent11);
                break;
            case R.id.frag_sign_new:
                Intent intent10 = new Intent(getActivity(), RegisterActivity.class);
                intent10.putExtra("Forget",2);
                startActivity(intent10);
                break;
            case R.id.frag_new_img:
                if (flag_img==0){
                    flag_img=1;
                    fragNewImg.setBackgroundResource(R.drawable.login_icon_show);
                    myPasNew.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else if (flag_img==1){
                    flag_img=0;
                    fragNewImg.setBackgroundResource(R.drawable.login_icon_hide);
                    myPasNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.frag_face_new:
                Intent intent5 = new Intent(getActivity(), TransActivity.class);
                startActivity(intent5);
                break;
            case R.id.invite:  //InViteActivity  RewardActivity
                Intent intent12 = new Intent(getActivity(), InViteActivity.class);
                startActivity(intent12);
                break;
        }
    }
     /**
      * 邮箱or手机号登录
      * */
    private void SetEmailLogn(int type) {
        String email = myPhoneNew.getText().toString().trim();
        String password = myPasNew.getText().toString().trim();
        String token = FirebaseInstanceId.getInstance().getToken();
        if (email.equals("")) {
            ToastUtil.showgravity(getActivity(), getResources().getString(R.string.logo_null));
        } else {

            Http.OkHttpGet(getActivity(), url + UrlUtil.isemailreg + "?email=" + email, new Http.OnDataFinish() {
                @Override
                public void OnSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("code").equals("0")){

                            if (password.equals("")) {
                                ToastUtil.showgravity(getActivity(), getResources().getString(R.string.logo_pass));
                                return;
                            }
                            String md5_password = AppUtil.MD5(password);
                            FormBody body = new FormBody.Builder()
                                    .add("type", String.valueOf(type))
                                    .add("email", email)
                                    .add("password", md5_password)
                                    .add("mobile",email)
                                    .add("notifyToken",token==null?"":token)
                                    .build();
                            Http.OkHttpPost(getActivity(), url + UrlUtil.login, body, new Http.OnDataFinish() {
                                @Override
                                public void OnSuccess(String result) {
                                    Log.d("Logining", result + url + UrlUtil.login+"=="+token);
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

                                                SPUtil.put(getActivity(), "downco", String.valueOf(couponCd));
                                            } catch (Exception e) {
                                            }
                                            String id = account.getString("id");
                                            String token = data2.getString("token");

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
                                            SPUtil.put(getActivity(), "photo", imgUrl);
                                            SPUtil.put(getActivity(), "Token", token);
                                            SPUtil.put(getActivity(), "name_one", name); //名字
                                            SPUtil.put(getActivity(), "you_usid", id);
                                            SPUtil.put(getActivity(), "email", email1);
                                            SPUtil.put(getActivity(), "gen", gender);
                                            SPUtil.put(getActivity(),"you_tel",tel);

                                            try{
                                                boolean showUpgradeToToken = data2.getBoolean("showUpgradeToToken");
                                                SPUtil.put(getActivity(),"showUpgradeToToken",showUpgradeToToken);
                                            }catch (Exception e){}
                                            /**
                                             * 签到接口
                                             * */
                                            EventBusCarrier carrier=new EventBusCarrier();
                                            TokenExchangeBean bean=new TokenExchangeBean();
                                            bean.setSuToken(1);
                                            bean.setFlagToken(1);
                                            bean.setFlagCoupn(1);
                                            carrier.setEventType(EVENT_TYPE_SIGN);
                                            carrier.setObject(bean);
                                            EventBus.getDefault().post(carrier);
                                            onResume();
                                        } else if (jsonObject2.getString("code").equals("1006")) {
                                            ToastUtil.showgravity(getActivity(), getResources().getString(R.string.pasw_lo));
                                        } else if (jsonObject2.getString("code").equals("1001")) {
                                            ToastUtil.showgravity(getActivity(), getResources().getString(R.string.nofff));
                                        } else if (jsonObject2.getString("code").equals("5001")){
                                            ToastUtil.showgravity(getActivity(), getResources().getString(R.string.logo_null));
                                        }else if (jsonObject2.getString("code").equals("5010")){
                                            ToastUtil.showgravity(getActivity(), getResources().getString(R.string.phone_no_login));
                                        }else {
                                            ToastUtil.showgravity(getActivity(), getResources().getString(R.string.fan));
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
                            ToastUtil.showgravity(getActivity(), getResources().getString(R.string.logo_null));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                ToastUtil.showgravity(getActivity(), getResources().getString(R.string.lo) + e.toString());
                // ...
            }
        } else {

        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getIdToken());


        // [START_EXCLUDE silent]

        // [END_EXCLUDE]

        String idToken = acct.getIdToken();

        FormBody body = new FormBody.Builder()
                .add("type", String.valueOf(5))
                .add("idToken", idToken)
                .build();
        Http.OkHttpPostOne(getActivity(), url + UrlUtil.login, body, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                Log.d("dsfgdgdf", url + UrlUtil.login + "==" + result);
                try {
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

                        SPUtil.put(getActivity(), "Token", Token);
                        SPUtil.put(getActivity(), "photo", photo1); //头像
                        SPUtil.put(getActivity(), "name_one", name_one); //名字
                        SPUtil.put(getActivity(), "email", email);
                        SPUtil.put(getActivity(), "you_usid", id1);

                        mylogin_ly.setVisibility(View.GONE);
                        my_ly.setVisibility(View.VISIBLE);
                        setUserVisibleHint(true);


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

            }
        });
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("dgfg", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);

                        }

                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {
            user.getEmail();
            user.getUid();
        } else {

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            //登录成功做相应的处理
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String user_id = account.getId();
            String idToken = account.getIdToken();


            Log.d("google login", user_id + "====" + idToken);
        } catch (ApiException e) {
            //登录异常做相应的处理
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("google login", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
