package com.app.whoot.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.activity.main.TransActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

/**
 * Created by Sunrise on 8/27/2018.
 */

public class ResultLoginActivity extends BaseActivity {
    @BindView(R.id.result_facce)
    LinearLayout resultFacce;
    @BindView(R.id.login_me_button)
    LoginButton loginMeButton;
    @BindView(R.id.result_lin)
    LinearLayout resultLin;

    private String shopId;
    private String couponId;


    private boolean award;
    private String commodityName;

    private CallbackManager callbackManager;
    private String gen;
    private String shopImg;
    private long useTm;
    private String shopName;
    private String urlUtil;
    private String shopGiftCfgId;
    private String giftExpiredTm;

    @Override
    public int getLayoutId() {
        return R.layout.login_result;
    }

    @Override
    public void onCreateInit() {

        urlUtil = (String) SPUtil.get(ResultLoginActivity.this, "URL", "");
        Intent intent = getIntent();
        shopId = intent.getStringExtra("shopId");
        award = intent.getBooleanExtra("award", false);
        shopImg = intent.getStringExtra("shopImg");
        couponId = intent.getStringExtra("couponId");
        shopName = intent.getStringExtra("shopName");
        commodityName = intent.getStringExtra("commodityName");
        shopGiftCfgId = intent.getStringExtra("shopGiftCfgId");
        giftExpiredTm = intent.getStringExtra("giftExpiredTm");
        useTm = intent.getLongExtra("couponCd", 0);
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
        init();
    }
    private void init() {
        loginMeButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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

                Log.d("LoginActivity","facebook登录失败了"+error.toString());
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //facebook回调
        callbackManager.onActivityResult(requestCode, resultCode, data);

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

                    SPUtil.put(ResultLoginActivity.this,"email",emali);

                    //获取用户头像
                    JSONObject object_pic = object.optJSONObject("picture");
                    JSONObject object_data = object_pic.optJSONObject("data");
                    String photo = object_data.optString("url");
                    SPUtil.put(ResultLoginActivity.this,"photo",photo);
                    //获取地域信息
                    String locale = object.optString("locale");   //zh_CN 代表中文简体


                    Log.d("hhahha",object.toString());
                    String token = accessToken.getToken();
                    /*token="EAAdMphXU5hIBAAp7Xcxu3rc7oiLpgvcQDKswCkb5UXA2CdmvvBBV7EUk7KUKytmXfM11mncfbjJf86MYkjVjc1Qa8aCUSZBalTQxMkpGdCgJvjj4zmDfH5ghV0SH8sIxKAcCV0nZ" +
                            "AthpOW3a9Iqdz98yxtbwt4WAYSSwYiGgpArZBZBJg5oapdgxEKuNfPa5c05JNSHthuhLtMacHouz";*/
                    FormBody body = new FormBody.Builder()
                            .add("type", String.valueOf(2))
                            .add("accessToken", token)
                            .build();
                    Http.OkHttpPost(ResultLoginActivity.this, urlUtil+ UrlUtil.login, body, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            Log.d("loginaaaaaaaaa",result);
                            // SPUtil.remove(LoginMeActivity.this,"accToken");
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String code = jsonObject.getString("code");
                                if (code.equals("0")){
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    JSONObject account = data.getJSONObject("account");

                                    try{
                                        gen = account.getString("gender");
                                    }catch (Exception e){
                                        gen="";
                                    }
                                    try {
                                        long couponCd = account.getLong("couponCd");

                                        SPUtil.put(ResultLoginActivity.this, "downco", String.valueOf(couponCd));
                                    } catch (Exception e) {

                                    }
                                    String name_one = account.getString("name");
                                    String Token = data.getString("token");
                                    String photo1 = account.getString("photo");
                                    String id1 = account.getString("id");
                                    /*try{
                                        String email = account.getString("email");
                                        SPUtil.put(LoginMeActivity.this,"email",email);
                                    }catch (Exception e){}*/


                                    SPUtil.put(ResultLoginActivity.this,"Token",Token);
                                    SPUtil.put(ResultLoginActivity.this,"photo1",photo1); //头像
                                    SPUtil.put(ResultLoginActivity.this,"gen",gen); //性别
                                    SPUtil.put(ResultLoginActivity.this,"name_one",name_one); //名字
                                    SPUtil.put(ResultLoginActivity.this,"you_usid",id1);
                                    Intent intent = new Intent(ResultLoginActivity.this, SuccessActivity.class);
                                    intent.putExtra("couponCd", useTm);
                                    intent.putExtra("award",award);
                                    intent.putExtra("shopId",shopId);
                                    intent.putExtra("shopImg",shopImg);
                                    intent.putExtra("couponId",couponId);
                                    intent.putExtra("shopName",shopName);
                                    intent.putExtra("giftExpiredTm",giftExpiredTm);
                                    intent.putExtra("shopGiftCfgId",shopGiftCfgId);
                                    startActivity(intent);

                                    finish();

                                }else {
                                    //Toast.makeText(ResultLoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                                    ToastUtil.showgravity(ResultLoginActivity.this,getResources().getString(R.string.facebook_one));
                                }

                                Log.d("登录成功",result);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                   /* SPUtil.put(LoginActivity.this,"Token","EAAEHUvrnWSQBAFC6DsAZCxMixNDAExKzVlSXWk1tBDHfF1aNonh5CvdjYoaJbLDP4PLuJ8OhOkiNjINIPhvLon4ROjyQJdW7IsEY0vJE7cgpsisUrpb" +
                            "Poh0OgrZCvdgLjuLkEdCanCVSNf1NBr8nRVE53EqUyb8wU3I8W5TsIqWVTgB2xkDKoQQRSYrYQwTT1TBh3bsWCZA58UWB8eb");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();*/

                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,picture,locale,updated_time,timezone,age_range,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }
    @OnClick({R.id.result_facce, R.id.result_lin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.result_facce:
                LoginManager.getInstance().logInWithReadPermissions(ResultLoginActivity.this, Arrays.asList("public_profile"));
                break;
            case R.id.result_lin:
                finish();
                break;
        }
    }
}
