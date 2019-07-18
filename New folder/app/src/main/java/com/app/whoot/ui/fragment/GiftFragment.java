package com.app.whoot.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
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
import com.app.whoot.adapter.GiftAdapter;
import com.app.whoot.adapter.TabFragmentPagerAdapter;
import com.app.whoot.base.fragment.BaseFragment;
import com.app.whoot.bean.GiftListBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.ExchangGiftActivity;
import com.app.whoot.ui.activity.GiftItemActivity;
import com.app.whoot.ui.activity.SignActivity;
import com.app.whoot.ui.activity.WebViewActivity;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.activity.main.TransActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.ui.fragment.gift.GiftAlreadyFragment;
import com.app.whoot.ui.fragment.gift.GiftNotFragment;
import com.app.whoot.ui.fragment.gift.GiftOverdueFragment;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.NetworkUtils;
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
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.FormBody;


/**
 * Created by Sunrise on 7/26/2018.
 * 礼包
 */

public class GiftFragment extends BaseFragment {

    @BindView(R.id.frag_facce)
    LinearLayout fragFacce;
    @BindView(R.id.frag_ly)
    LinearLayout fragLy;
    @BindView(R.id.login_e_button)
    LoginButton loginEButton;
    @BindView(R.id.frag_title_ly)
    LinearLayout fragTitleLy;
    @BindView(R.id.gift_login)
    TextView giftLogin;
    @BindView(R.id.gift_sign_up)
    TextView giftSignUp;
    @BindView(R.id.gift_login_google)
    LinearLayout giftLoginGoogle;
    @BindView(R.id.gift_login_txt)
    TextView giftLoginTxt;
    @BindView(R.id.gift_tab_ly)
    LinearLayout gift_tab_ly;
    @BindView(R.id.gift_wei)
    LinearLayout gift_wei;
    @BindView(R.id.gift_yi)
    LinearLayout gift_yi;
    @BindView(R.id.gift_guo)
    LinearLayout gift_guo;
    @BindView(R.id.gift_clear)
    LinearLayout gift_clear;
    @BindView(R.id.myViewPager)
    ViewPager myViewPager;
    @BindView(R.id.gift_wei_img)
    ImageView gift_wei_img;
    @BindView(R.id.gift_yi_img)
    ImageView gift_yi_img;
    @BindView(R.id.gift_guo_img)
    ImageView gift_guo_img;





    private TabFragmentPagerAdapter adapter;

    private List<Fragment> list_frag;


    private int RC_SIGN_IN = 2;

    private int biao = 0;

    private String url = "";
    private int rows = 1;



    private CallbackManager callbackManager;

    private int faag_li = 0;  //是否登录的标记
    private String token;

    private int fac_num = 0;  //控制缺省页显示
    private String urlUtil;


    @Override
    public int getLayoutId() {

        return R.layout.gift_frag;
    }

    @Override
    public void onViewCreatedInit() {


        giftLoginTxt.setText(getResources().getString(R.string.login_by));
        SpannableString clickString = new SpannableString("  "+getResources().getString(R.string.login_terms)+"  ");
        giftLoginTxt.setMovementMethod(LinkMovementMethod.getInstance());
        clickString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // startActivity(new Intent(MainActivity.this, FirstActivity.class));
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
        giftLoginTxt.append(clickString);
        giftLoginTxt.append(new SpannableString(getResources().getString(R.string.and)));
        SpannableString click = new SpannableString("  "+getResources().getString(R.string.yinsi)+"  ");
        click.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
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
        giftLoginTxt.append(click);
        urlUtil = (String) SPUtil.get(getActivity(), "URL", "");
        loginEButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile"));
            }
        });
        callbackManager = CallbackManager.Factory.create();

        loginEButton.setFragment(this);

        //把Fragment添加到List集合里面
        list_frag = new ArrayList<>();
        list_frag.add(new GiftNotFragment());
        list_frag.add(new GiftOverdueFragment());
        list_frag.add(new GiftAlreadyFragment());
        myViewPager.setOnPageChangeListener(new MyPagerChangeListener());
        adapter = new TabFragmentPagerAdapter(getChildFragmentManager(), list_frag);
        myViewPager.setAdapter(adapter);
        myViewPager.setCurrentItem(0);  //初始化显示第一个页面

    }
    /**
     * 设置一个ViewPager的侦听事件，当左右滑动ViewPager时菜单栏被选中状态跟着改变
     */
    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:

                    gift_wei.setBackgroundColor(getResources().getColor(R.color.white));
                    gift_yi.setBackgroundColor(getResources().getColor(R.color.card_time));
                    gift_guo.setBackgroundColor(getResources().getColor(R.color.card_time));
                    gift_wei_img.setBackgroundResource(R.drawable.gifts_tab_icon_gift_big);
                    gift_yi_img.setBackgroundResource(R.drawable.gifts_tab_icon_gift_used);
                    gift_guo_img.setBackgroundResource(R.drawable.gifts_tab_icon_gift_expired);
                    gift_clear.setVisibility(View.GONE);
                    break;
                case 1:
                    gift_wei.setBackgroundColor(getResources().getColor(R.color.card_time));
                    gift_yi.setBackgroundColor(getResources().getColor(R.color.white));
                    gift_guo.setBackgroundColor(getResources().getColor(R.color.card_time));
                    gift_wei_img.setBackgroundResource(R.drawable.gifts_tab_icon_gift);
                    gift_yi_img.setBackgroundResource(R.drawable.gifts_tab_icon_gift_used_big);
                    gift_guo_img.setBackgroundResource(R.drawable.gifts_tab_icon_gift_expired);
                    gift_clear.setVisibility(View.GONE);
                    break;
                case 2:
                    gift_wei.setBackgroundColor(getResources().getColor(R.color.card_time));
                    gift_yi.setBackgroundColor(getResources().getColor(R.color.card_time));
                    gift_guo.setBackgroundColor(getResources().getColor(R.color.white));
                    gift_wei_img.setBackgroundResource(R.drawable.gifts_tab_icon_gift);
                    gift_yi_img.setBackgroundResource(R.drawable.gifts_tab_icon_gift_used);
                    gift_guo_img.setBackgroundResource(R.drawable.gifts_tab_icon_gift_expired_big);
                    gift_clear.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
    @Override
    public void onStartInit() {

    }


    @OnClick({R.id.gift_ly, R.id.frag_facce,R.id.gift_login,R.id.gift_sign_up,R.id.gift_login_google,R.id.gift_wei,R.id.gift_yi,R.id.gift_guo,R.id.gift_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.gift_ly:
                Intent intent = new Intent(getActivity(), ExchangGiftActivity.class);
                startActivity(intent);
                break;
            case R.id.frag_facce:
                Intent intent1 = new Intent(getActivity(), TransActivity.class);
                startActivity(intent1);
                break;
            case R.id.gift_login:
                Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent2);
                break;
            case R.id.gift_sign_up:
                Intent intent3 = new Intent(getActivity(), SignActivity.class);
                startActivity(intent3);
                break;
            case R.id.gift_login_google:
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestIdToken("516500673783-h0c5ggiod363hiu6e7fen2k1or9ftlmc.apps.googleusercontent.com")
                        .build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.gift_wei:
                myViewPager.setCurrentItem(0);
                gift_clear.setVisibility(View.GONE);
                break;
            case R.id.gift_yi:
                myViewPager.setCurrentItem(1);
                gift_clear.setVisibility(View.GONE);
                break;
            case R.id.gift_guo:
                myViewPager.setCurrentItem(2);
                gift_clear.setVisibility(View.VISIBLE);
                break;
            case R.id.gift_clear:

                break;

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            token = (String) SPUtil.get(getActivity(), "Token", "");
            if (token.length() != 0) {
                gift_tab_ly.setVisibility(View.VISIBLE);
                myViewPager.setVisibility(View.VISIBLE);
                fragLy.setVisibility(View.GONE);
                fragTitleLy.setVisibility(View.VISIBLE);

                rows = 1;
                if (biao == 1) {  //控制第一次进入不执行
                    Log.d("lkjhg", url);
                    // requestData();
                }
                if (faag_li == 1) {

                }
                url = urlUtil + UrlUtil.giftlist + "?start=0" + "&rows=" + 10;
                Log.d("pppppppppppppp", "mmp");

            } else {
                fragLy.setVisibility(View.VISIBLE);       //登录
                gift_tab_ly.setVisibility(View.GONE);
                myViewPager.setVisibility(View.GONE);
                fragTitleLy.setVisibility(View.GONE);

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        token = (String) SPUtil.get(getActivity(), "Token", "");
        if (token.length() != 0) {
            gift_tab_ly.setVisibility(View.VISIBLE);
            myViewPager.setVisibility(View.VISIBLE);
            fragLy.setVisibility(View.GONE);
            fragTitleLy.setVisibility(View.VISIBLE);

            rows = 1;
            if (biao == 1) {  //控制第一次进入不执行
                Log.d("lkjhg", url);
                // requestData();
            }
            if (faag_li == 1) {

            }
            url = urlUtil + UrlUtil.giftlist + "?start=0" + "&rows=" + 10;
            Log.d("pppppppppppppp", "mmp");
        } else {
            fragLy.setVisibility(View.VISIBLE);       //登录
            gift_tab_ly.setVisibility(View.GONE);
            myViewPager.setVisibility(View.GONE);
            fragTitleLy.setVisibility(View.GONE);

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
                ToastUtil.showgravity(getActivity(),getResources().getString(R.string.lo));
                // ...
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        // [START_EXCLUDE silent]

        // [END_EXCLUDE]
        String idToken = account.getIdToken();

        FormBody body = new FormBody.Builder()
                .add("type", String.valueOf(5))
                .add("idToken", idToken)
                .build();
        Http.OkHttpPostOne(getActivity(), urlUtil + UrlUtil.login, body, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                Log.d("dsfgdgdf",urlUtil + UrlUtil.login+"=="+result);
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
                        SPUtil.put(getActivity(),"email",email);
                        SPUtil.put(getActivity(),"you_usid",id1);

                        gift_tab_ly.setVisibility(View.VISIBLE);
                        myViewPager.setVisibility(View.VISIBLE);
                        fragLy.setVisibility(View.GONE);
                        fragTitleLy.setVisibility(View.VISIBLE);

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

    @Override
    public void onPause() {
        super.onPause();
        faag_li = 1;
    }


}
