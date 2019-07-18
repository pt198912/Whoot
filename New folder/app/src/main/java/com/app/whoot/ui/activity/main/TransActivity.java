package com.app.whoot.ui.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.TokenExchangeBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.LoginMeActivity;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.ui.view.dialog.BoxDialog;
import com.app.whoot.ui.view.dialog.TokenDialog;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import okhttp3.FormBody;

import static com.app.whoot.util.Constants.EVENT_TYPE_SIGN;

/**
 * Created by Sunrise on 6/6/2018.
 */

public class TransActivity extends Activity{


    private LoginButton LoginButton;
    private CallbackManager callbackManager;
    private String urlUtil;
    private String gen;
    private TokenDialog browsDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_activi);
        LoginButton = findViewById(R.id.login_me_button);
        callbackManager = CallbackManager.Factory.create();

        urlUtil = (String) SPUtil.get(TransActivity.this, "URL", "");

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
        LoginManager.getInstance().logInWithReadPermissions(TransActivity.this, Arrays.asList("public_profile"));
        init();

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
              //  Toast.makeText(TransActivity.this, "facebook登录取消了", Toast.LENGTH_SHORT).show();

             getCancel();
            }

            @Override
            public void onError(FacebookException error) {
               // Toast.makeText(TransActivity.this, "facebook登录失败了", Toast.LENGTH_SHORT).show();

               getError();
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

                    SPUtil.put(TransActivity.this,"email",emali);

                    //获取用户头像
                    JSONObject object_pic = object.optJSONObject("picture");
                    JSONObject object_data = object_pic.optJSONObject("data");
                    String photo = object_data.optString("url");
                    SPUtil.put(TransActivity.this,"photo",photo);
                    //获取地域信息
                    String locale = object.optString("locale");   //zh_CN 代表中文简体


                    String token = accessToken.getToken();
                    Log.d("hhahha",object.toString()+"\n"+token);

                    FormBody body = new FormBody.Builder()
                            .add("type", String.valueOf(2))
                            .add("accessToken", token)
                            .add("notifyToken", FirebaseInstanceId.getInstance().getToken())
                            .build();
                    Http.OkHttpPost(TransActivity.this, urlUtil + UrlUtil.login, body, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            Log.d("login_wwwwwwwwwwwwwwwww",result);
                            // SPUtil.remove(TransActivity.this,"accToken");
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

                                        SPUtil.put(TransActivity.this, "downco", String.valueOf(couponCd));
                                    } catch (Exception e) {

                                    }
                                    String name_one = account.getString("name");
                                    String Token = data.getString("token");
                                    String photo1 = account.getString("photo");
                                    String id1 = account.getString("id");
                                    try{
                                        String email = account.getString("email");
                                        SPUtil.put(TransActivity.this,"email",email);
                                    }catch (Exception e){}
                                    SPUtil.put(TransActivity.this,"Token",Token);
                                    SPUtil.put(TransActivity.this,"photo1",photo1); //头像
                                    SPUtil.put(TransActivity.this,"gen", gen); //性别
                                    SPUtil.put(TransActivity.this,"name_one",name_one); //名字
                                    SPUtil.put(TransActivity.this,"you_usid",id1);
                                    finish();
                                    try{boolean showUpgradeToToken = data.getBoolean("showUpgradeToToken");
                                        SPUtil.put(TransActivity.this,"showUpgradeToToken",showUpgradeToToken);
                                    }catch (Exception e){

                                    }
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

                                }else {
                                    //Toast.makeText(TransActivity.this,getResources().getString(R.string.fail),Toast.LENGTH_SHORT).show();
                                    ToastUtil.showgravity(TransActivity.this,getResources().getString(R.string.fail));
                                    finish();
                                }

                                Log.d("登录成功",result);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            finish();
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

    public void getError() {
        finish();
    }

    public void getCancel() {
        finish();
    }
}
