package com.app.whoot.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.adapter.TabFragmentPagerAdapter;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.base.fragment.BaseFragment;
import com.app.whoot.bean.TokenExchangeBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.LoginMeActivity;
import com.app.whoot.ui.activity.RegisterActivity;
import com.app.whoot.ui.activity.SignActivity;
import com.app.whoot.ui.activity.WebViewActivity;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.ui.activity.main.TransActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.ui.fragment.coupons.BeCouponsFragment;
import com.app.whoot.ui.fragment.coupons.ExchangeFragment;
import com.app.whoot.ui.fragment.coupons.ListCouponsFragment;
import com.app.whoot.ui.view.custom.ClearEditText;
import com.app.whoot.ui.view.dialog.TokenDialog;
import com.app.whoot.util.AppUtil;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.FormBody;

import static com.app.whoot.util.Constants.EVENT_TYPE_SIGN;

/**
 * Created by Sunrise on 4/2/2018.
 * 卡包
 */

public class CardFragment extends BaseFragment {

    @BindView(R.id.frag_facce)
    LinearLayout fragFacce;
    @BindView(R.id.login_e_button)
    LoginButton loginButton;
    @BindView(R.id.card_wei)
    LinearLayout cardWei;
    @BindView(R.id.card_yi)
    LinearLayout cardYi;
    @BindView(R.id.card_guo)
    LinearLayout cardGuo;
    @BindView(R.id.card_clear)
    LinearLayout cardClear;
    @BindView(R.id.card_tab_ly)
    LinearLayout cardTabLy;
    @BindView(R.id.cardViewPager)
    ViewPager cardViewPager;
    @BindView(R.id.frag_card_ly)
    LinearLayout fragCardLy;
    @BindView(R.id.card_my_login)
    TextView cardMyLogin;
    @BindView(R.id.card_my_sign_up)
    TextView cardMySignUp;
    @BindView(R.id.card_my_login_button)
    LoginButton cardMyLoginButton;
    @BindView(R.id.card_my_login_facce)
    LinearLayout cardMyLoginFacce;
    @BindView(R.id.card_my_login_google)
    LinearLayout cardMyLoginGoogle;
    @BindView(R.id.card_my_login_txt)
    TextView cardMyLoginTxt;
    @BindView(R.id.card_mylogin_ly)
    LinearLayout cardMyloginLy;
    @BindView(R.id.card_clear_img)
    ImageView card_clear_img;

    @BindView(R.id.tv_uesd)
    TextView usedTv;
    @BindView(R.id.tv_unuesd)
    TextView unusedTv;
    @BindView(R.id.tv_useless)
    TextView uselessTv;
    @BindView(R.id.card_clear_txt)
    TextView cardClearTxt;
    @BindView(R.id.card_login)
    TextView cardLogin;
    @BindView(R.id.card_sign_up)
    TextView cardSignUp;
    @BindView(R.id.card_phone_new)
    ClearEditText cardPhoneNew;
    @BindView(R.id.card_pas_new)
    ClearEditText cardPasNew;
    @BindView(R.id.card_new_img)
    ImageView cardNewImg;
    @BindView(R.id.card_time_new)
    TextView cardTimeNew;
    @BindView(R.id.card_psw_new)
    TextView cardPswNew;
    @BindView(R.id.card_sign_new)
    TextView cardSignNew;
    @BindView(R.id.card_face_new)
    ImageView cardFaceNew;
    private CallbackManager callbackManager;

    private TabFragmentPagerAdapter adapter;

    private List<Fragment> list_frag;

    private String urlUtil;
    private TokenDialog browsDialog;

    private int RC_SIGN_IN = 2;
    private int aaa = 0;
    private String url;
    private String name;
    private String email1;
    private String gender;
    private String imgUrl;
    private String tel;


    private ListCouponsFragment listCouponsFragment;
    private ExchangeFragment exchangeFragment;
    BeCouponsFragment beCouponsFragment;
    private int flag_img=0;

    @Override
    public int getLayoutId() {
        return R.layout.frag_card;
    }


    @Override
    public void onViewCreatedInit() {

        cardTimeNew.getBackground().setAlpha(100);
        url = (String) SPUtil.get(getActivity(), "URL", "");
        cardMyLoginTxt.setText(getResources().getString(R.string.login_by));
        SpannableString clickString = new SpannableString("  " + getResources().getString(R.string.login_terms) + "  ");
        cardMyLoginTxt.setMovementMethod(LinkMovementMethod.getInstance());
        clickString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // startActivity(new Intent(MainActivity.this, FirstActivity.class));
                aaa = 1;
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
        cardMyLoginTxt.append(clickString);
        cardMyLoginTxt.append(new SpannableString(getResources().getString(R.string.and)));
        SpannableString click = new SpannableString("  " + getResources().getString(R.string.yinsi) + "  ");
        click.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                aaa = 2;
                // startActivity(new Intent(MainActivity.this, FirstActivity.class));
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
        cardMyLoginTxt.append(click);
        urlUtil = (String) SPUtil.get(getActivity(), "URL", "");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile"));
            }
        });
        callbackManager = CallbackManager.Factory.create();

        loginButton.setFragment(this);
        init();
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


        //把Fragment添加到List集合里面
        list_frag = new ArrayList<>();
        listCouponsFragment = new ListCouponsFragment();

        exchangeFragment = new ExchangeFragment();
        beCouponsFragment = new BeCouponsFragment();
        list_frag.add(listCouponsFragment);
        list_frag.add(exchangeFragment);
        list_frag.add(beCouponsFragment);

        cardViewPager.setOnPageChangeListener(new MyPagerChangeListener());
        adapter = new TabFragmentPagerAdapter(getChildFragmentManager(), list_frag);
        cardViewPager.setAdapter(adapter);
        cardViewPager.setCurrentItem(0);  //初始化显示第一个页面

        setTextChangedListener();
    }


    public enum TAB_INDEX{
        UNUSED,USED,EXPIRED
    }
    public void setTabIndex(TAB_INDEX index){
        cardViewPager.setCurrentItem(index.ordinal());
    }
    public void setTextChangedListener(){
        if (cardPasNew.getText().toString().trim().length()>0){
            cardTimeNew.getBackground().setAlpha(255);
            cardTimeNew.setClickable(true);
        }else {
            cardTimeNew.getBackground().setAlpha(100);
            cardTimeNew.setClickable(false);
        }
        cardPhoneNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length>0){
                    if (cardPasNew.getText().toString().trim().length()>0){
                        cardTimeNew.getBackground().setAlpha(255);
                        cardTimeNew.setClickable(true);
                    }else {
                        cardTimeNew.getBackground().setAlpha(100);
                        cardTimeNew.setClickable(false);
                    }
                }else {
                    cardTimeNew.getBackground().setAlpha(100);
                    cardTimeNew.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cardPasNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length>0){
                    if (cardPhoneNew.getText().toString().trim().length()>0){
                        cardTimeNew.setBackgroundResource(R.drawable.login_deng);
                        cardTimeNew.setClickable(true);
                    }else {
                        cardTimeNew.setBackgroundResource(R.drawable.bg_btn_disabled);
                        cardTimeNew.setClickable(false);
                    }
                    cardNewImg.setVisibility(View.VISIBLE);
                }else {
                    cardNewImg.setVisibility(View.GONE);
                    cardTimeNew.setBackgroundResource(R.drawable.bg_btn_disabled);
                    cardTimeNew.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    private int Card_img = 0;

    /**
     * 设置一个ViewPager的侦听事件，当左右滑动ViewPager时菜单栏被选中状态跟着改变
     */
    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            Log.d("菜单栏被选中状态跟着改变", arg0 + "wo");
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    cardWei.setBackgroundColor(getResources().getColor(R.color.white));
                    cardYi.setBackgroundColor(getResources().getColor(R.color.card_time));
                    cardGuo.setBackgroundColor(getResources().getColor(R.color.card_time));
                    unusedTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    //设置不为加粗
                    usedTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    uselessTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    cardClear.setVisibility(View.VISIBLE);
                    card_clear_img.setVisibility(View.VISIBLE);
                    Card_img = 1;
                    break;
                case 1:
                    cardWei.setBackgroundColor(getResources().getColor(R.color.card_time));
                    cardYi.setBackgroundColor(getResources().getColor(R.color.white));
                    cardGuo.setBackgroundColor(getResources().getColor(R.color.card_time));

                    //设置不为加粗
                    unusedTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    usedTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    uselessTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    cardClear.setVisibility(View.VISIBLE);
                    Card_img = 0;
                    break;
                case 2:
                    cardWei.setBackgroundColor(getResources().getColor(R.color.card_time));
                    cardYi.setBackgroundColor(getResources().getColor(R.color.card_time));
                    cardGuo.setBackgroundColor(getResources().getColor(R.color.white));
                    usedTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    //设置不为加粗
                    unusedTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    uselessTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    cardClear.setVisibility(View.VISIBLE);
                    card_clear_img.setVisibility(View.VISIBLE);
                    Card_img = 0;
                    break;
            }
        }
    }


    private void init() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d("LoginActivity", "facebook登录成功了" + loginResult.getAccessToken().getToken());
                //获取登录信息  xLNf8EJ9HUiwCX1   HCBZUCIO3AOUBNMT
                getLoginInfo(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                //Toast.makeText(getActivity(), getResources().getString(R.string.facebook), Toast.LENGTH_SHORT).show();
                ToastUtil.showgravity(getActivity(), getResources().getString(R.string.facebook));


            }

            @Override
            public void onError(FacebookException error) {
                //Toast.makeText(getActivity(), getResources().getString(R.string.facebook_one), Toast.LENGTH_SHORT).show();
                ToastUtil.showgravity(getActivity(), getResources().getString(R.string.facebook_one));

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //facebook回调
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
                ToastUtil.showgravity(getActivity(), getResources().getString(R.string.lo));
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


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

                        cardViewPager.setVisibility(View.VISIBLE);
                        cardMyloginLy.setVisibility(View.GONE);
                        fragCardLy.setVisibility(View.VISIBLE);

                    } else {
                        // Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
                        ToastUtil.showgravity(getActivity(), getActivity().getResources().getString(R.string.facebook_one));
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

                    //获取用户头像
                    JSONObject object_pic = object.optJSONObject("picture");
                    JSONObject object_data = object_pic.optJSONObject("data");
                    String photo = object_data.optString("url");
                    SPUtil.put(getActivity(), "photo", photo);
                    //获取地域信息
                    String locale = object.optString("locale");   //zh_CN 代表中文简体
                    Log.d("hhahha", object.toString());
                    String token = accessToken.getToken();

                    FormBody body = new FormBody.Builder()
                            .add("type", String.valueOf(2))
                            .add("accessToken", token)
                            .build();
                    Http.OkHttpPost(getActivity(), urlUtil + UrlUtil.login, body, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            Log.d("login123456", result);
                            SPUtil.remove(getActivity(), "accToken");
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String code = jsonObject.getString("code");
                                if (code.equals("0")) {
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    JSONObject account = data.getJSONObject("account");


                                    String gen = account.getString("gender");
                                    String name_one = account.getString("name");
                                    String Token = data.getString("token");
                                    String photo1 = account.getString("photo");
                                    String id1 = account.getString("id");
                                    // String email = account.getString("email");


                                    SPUtil.put(getActivity(), "Token", Token);
                                    SPUtil.put(getActivity(), "photo1", photo1); //头像
                                    SPUtil.put(getActivity(), "gen", gen); //性别
                                    SPUtil.put(getActivity(), "name_one", name_one); //名字

                                    //  SPUtil.put(LoginActivity.this,"email",email);


                                    fragFacce.setVisibility(View.GONE);
                                    updateFragment();

                                } else {
                                    // Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
                                    ToastUtil.showgravity(getActivity(), getActivity().getResources().getString(R.string.facebook_one));
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


                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,picture,locale,updated_time,timezone,age_range,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void updateFragment() {
        for (int i = 0; i < list_frag.size(); i++) {
            list_frag.get(i).setUserVisibleHint(true);
        }
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        Log.d("pengtao", "onFragmentVisibleChange: ");
        super.onFragmentVisibleChange(isVisible);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {
                String token = (String) SPUtil.get(getActivity(), "Token", "");
                Log.d("执行了吗", "zhixingl11112 " + token);
                if (token.length() != 0) {
                    cardViewPager.setVisibility(View.VISIBLE);
                    cardMyloginLy.setVisibility(View.GONE);
                    fragCardLy.setVisibility(View.VISIBLE);
                } else {
                  //
                    //  cardMyloginLy.setVisibility(View.VISIBLE);
                    cardViewPager.setVisibility(View.GONE);
                    fragCardLy.setVisibility(View.GONE);
                }
                listCouponsFragment.setUserVisibleHint(isVisibleToUser);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        String token = (String) SPUtil.get(getActivity(), "Token", "");
        Log.d("执行了吗", "zhixingl11111 " + token);
        if (token.length() != 0) {

            cardViewPager.setVisibility(View.VISIBLE);
            cardMyloginLy.setVisibility(View.GONE);
            fragCardLy.setVisibility(View.VISIBLE);
        } else {
        //    cardMyloginLy.setVisibility(View.VISIBLE);
            cardViewPager.setVisibility(View.GONE);
            fragCardLy.setVisibility(View.GONE);
        }

        setUserVisibleHint(true);

    }

    @Override
    public void onStartInit() {

    }

    @OnClick({R.id.frag_facce, R.id.card_wei, R.id.card_yi, R.id.card_guo, R.id.card_clear, R.id.card_my_login, R.id.card_my_login_facce, R.id.card_my_sign_up,
            R.id.card_my_login_google, R.id.card_my_login_txt, R.id.card_clear_img,R.id.card_new_img,R.id.card_face_new,R.id.card_sign_new,R.id.card_psw_new,R.id.card_time_new})
    public void onViewClicked(View view) {
        String token = (String) SPUtil.get(getActivity(), "Token", "");
        switch (view.getId()) {

            case R.id.frag_facce:  //登录
                //LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile"));
                break;
            case R.id.card_wei:
                cardViewPager.setCurrentItem(0);
                cardClear.setVisibility(View.VISIBLE);
                break;
            case R.id.card_yi:
                if (token.length() != 0) {
                    cardViewPager.setCurrentItem(1);
                    cardClear.setVisibility(View.VISIBLE);
                } else {
                    ToastUtil.showgravity(getActivity(), getResources().getString(R.string.login_inj));
                }

                break;
            case R.id.card_guo:
                if (token.length() != 0) {
                    cardViewPager.setCurrentItem(2);
                    cardClear.setVisibility(View.VISIBLE);
                } else {
                    ToastUtil.showgravity(getActivity(), getResources().getString(R.string.login_inj));
                }

                break;
            case R.id.card_clear:
                EventBusCarrier eventBusCarrier = new EventBusCarrier();
                eventBusCarrier.setEventType("1");
                eventBusCarrier.setObject("我是TwoActivity发布的事件");
                EventBus.getDefault().post(eventBusCarrier); //普通事件发布
                break;
            case R.id.card_clear_img:
                Intent intent3 = new Intent(getActivity(), WebViewActivity.class);
                intent3.putExtra("html", 3);
                startActivity(intent3);
                break;
            case R.id.card_my_login:
                Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.card_my_sign_up:
                Intent intent2 = new Intent(getActivity(), SignActivity.class);
                startActivity(intent2);
                break;
            case R.id.card_my_login_facce:

                break;
            case R.id.card_my_login_google:
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestIdToken("516500673783-h0c5ggiod363hiu6e7fen2k1or9ftlmc.apps.googleusercontent.com")
                        .build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

                break;
            case R.id.card_my_login_txt:
                ToastUtil.showgravity(getActivity(), aaa + "");
                break;
            case R.id.card_time_new:
                String Phonenew = cardPhoneNew.getText().toString().trim();
                if (Phonenew.indexOf("@")!=-1){ //邮箱登录
                    SetEmailLogn(4);

                }else { //手机号登录 7
                    SetEmailLogn(7);
                }
                break;
            case R.id.card_psw_new:
                Intent intent11 = new Intent(getActivity(), RegisterActivity.class);
                intent11.putExtra("Forget",1);
                startActivity(intent11);
                break;
            case R.id.card_sign_new:
                Intent intent10 = new Intent(getActivity(), RegisterActivity.class);
                intent10.putExtra("Forget",2);
                startActivity(intent10);
                break;
            case R.id.card_face_new:
                Intent intent = new Intent(getActivity(), TransActivity.class);
                startActivity(intent);
                break;
            case R.id.card_new_img:
                if (flag_img==0){
                    flag_img=1;
                    cardNewImg.setBackgroundResource(R.drawable.login_icon_show);
                    cardPasNew.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else if (flag_img==1){
                    flag_img=0;
                    cardNewImg.setBackgroundResource(R.drawable.login_icon_hide);
                    cardPasNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;

        }
    }

    private void SetEmailLogn(int type) {
        String email = cardPhoneNew.getText().toString().trim();
        String password = cardPasNew.getText().toString().trim();
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

    class Clickable extends ClickableSpan implements View.OnClickListener {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener listener) {
            mListener = listener;
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view);
        }
    }


    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
