package com.app.whoot.ui.activity.login;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.base.activity.dagger2.BaseDaggerActivity;
import com.app.whoot.bean.TokenExchangeBean;
import com.app.whoot.bean.TouristsBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.FailureActivity;
import com.app.whoot.ui.activity.ForgetActivity;
import com.app.whoot.ui.activity.LoginMeActivity;
import com.app.whoot.ui.activity.ProfileActivity;
import com.app.whoot.ui.activity.SignActivity;
import com.app.whoot.ui.activity.SuccessActivity;
import com.app.whoot.ui.activity.WebViewActivity;
import com.app.whoot.ui.activity.WindowActivity;
import com.app.whoot.ui.activity.WriteActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.ui.activity.main.TransActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.ui.view.dialog.TokenDialog;
import com.app.whoot.util.AppUtil;
import com.app.whoot.util.BitmapUtil;
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
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

import static com.app.whoot.util.Constants.EVENT_TYPE_SIGN;
import static com.app.whoot.util.Constants.EVENT_TYPE_TOKEN_EXCHANGE;


public class LoginActivity extends BaseDaggerActivity {


    @BindView(R.id.login_button)
    LoginButton loginButton;
    @BindView(R.id.login_facce)
    LinearLayout loginFacce;
    @BindView(R.id.login_no)
    TextView loginNo;
    @BindView(R.id.login_email)
    EditText login_email;
    @BindView(R.id.login_pass)
    EditText login_pass;
    @BindView(R.id.login_deng)
    LinearLayout login_deng;
    @BindView(R.id.login_invalid)
    TextView login_invalid;
    @BindView(R.id.login_sign)
    TextView login_sign;
    @BindView(R.id.login_forget)
    TextView login_forget;
    @BindView(R.id.login_whoot)
    TextView login_whoot;
    @BindView(R.id.title_back_title)
    TextView title_back_title;
    @BindView(R.id.title_back_fl)
    LinearLayout title_back_fl;


    private CallbackManager callbackManager;

    private static final int BAIDU_READ_PHONE_STATE = 100;

    private List<TouristsBean> list = new ArrayList<>();
    private String gen;
    private String urlUtil;
    private String name;
    private String email1;
    private String gender;
    private String imgUrl;
    public static LoginActivity instance;
    private int wind_flag;
    private TokenDialog browsDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    /**
     * 是否应该检查权限
     *
     * @return
     */
    public boolean showCheckPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        } else {
            return false;
        }
    }

    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //   Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, BAIDU_READ_PHONE_STATE);
        } else {

        }
    }

    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    init();
                } else {
                    // 没有获取到权限，做特殊处理
                    //   Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityInject() {

        instance=this;
        urlUtil = (String) SPUtil.get(LoginActivity.this, "URL", "");

        title_back_title.setText(getResources().getString(R.string.log_in));

        login_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        boolean b = showCheckPermissions();
        if (b) {
            showContacts();
        }
        Intent intent = getIntent();
        wind_flag = intent.getIntExtra("wind_flag", 0);
        getActivityComponent().inject(this);
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
        login_whoot.setText(getResources().getString(R.string.login_by));
        SpannableString clickString = new SpannableString("  "+getResources().getString(R.string.login_terms)+"  ");
        login_whoot.setMovementMethod(LinkMovementMethod.getInstance());
        clickString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // startActivity(new Intent(MainActivity.this, FirstActivity.class));
                Intent intent3 = new Intent(LoginActivity.this, WebViewActivity.class);
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
        login_whoot.append(clickString);
        login_whoot.append(new SpannableString(getResources().getString(R.string.and)));
        SpannableString click = new SpannableString("  "+getResources().getString(R.string.yinsi)+"  ");
        click.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // startActivity(new Intent(MainActivity.this, FirstActivity.class));
                Intent intent4 = new Intent(LoginActivity.this, WebViewActivity.class);
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
        login_whoot.append(click);

    }

    private void init() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                Log.d("LoginActivity", "facebook登录成功了" + loginResult.getAccessToken().getToken());
                //获取登录信息
                getLoginInfo(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                //Toast.makeText(LoginActivity.this, getResources().getString(R.string.facebook), Toast.LENGTH_SHORT).show();
                ToastUtil.showgravity(LoginActivity.this, getResources().getString(R.string.facebook));

            }

            @Override
            public void onError(FacebookException error) {
                //Toast.makeText(LoginActivity.this, getResources().getString(R.string.facebook_one), Toast.LENGTH_SHORT).show();
                ToastUtil.showgravity(LoginActivity.this, getResources().getString(R.string.facebook_one));

            }
        });
    }

    @Override
    protected void onInitPageAndData() {

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
                    SPUtil.put(LoginActivity.this, "email", emali);

                    //获取用户头像
                    JSONObject object_pic = object.optJSONObject("picture");
                    JSONObject object_data = object_pic.optJSONObject("data");
                    String photo = object_data.optString("url");
                    // SPUtil.put(LoginActivity.this,"photo",photo);
                    //获取地域信息
                    String locale = object.optString("locale");   //zh_CN 代表中文简体


                    Log.d("hhahha", object.toString());
                    String url = (String) SPUtil.get(LoginActivity.this, "URL", "");
                    if (url.length() > 0) {


                        String token = accessToken.getToken();
                    /*token="EAAdMphXU5hIBAAp7Xcxu3rc7oiLpgvcQDKswCkb5UXA2CdmvvBBV7EUk7KUKytmXfM11mncfbjJf86MYkjVjc1Qa8aCUSZBalTQxMkpGdCgJvjj4zmDfH5ghV0SH8sIxKAcCV0nZ" +
                            "AthpOW3a9Iqdz98yxtbwt4WAYSSwYiGgpArZBZBJg5oapdgxEKuNfPa5c05JNSHthuhLtMacHouz";*/
                        FormBody body = new FormBody.Builder()
                                .add("type", String.valueOf(2))
                                .add("accessToken", token)
                                .add("notifyToken", FirebaseInstanceId.getInstance().getToken())
                                .build();
                        Http.OkHttpPost(LoginActivity.this, url + UrlUtil.login, body, new Http.OnDataFinish() {
                            @Override
                            public void OnSuccess(String result) {
                                Log.d("loginsfdgfh", result);
                                // SPUtil.remove(LoginActivity.this,"accToken");
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String code = jsonObject.getString("code");
                                    if (code.equals("0")) {
                                        JSONObject data = jsonObject.getJSONObject("data");
                                        JSONObject account = data.getJSONObject("account");
                                        try {
                                            gen = account.getString("gender");
                                        } catch (Exception e) {
                                            gen = "";
                                        }

                                        String name_one = account.getString("name");
                                        String Token = data.getString("token");
                                        String photo1 = account.getString("photo");
                                        String userid = account.getString("id");
                                  /*  try{
                                        String email = account.getString("email");
                                        SPUtil.put(LoginActivity.this,"email",email);
                                    }catch (Exception e){

                                    }*/
                                        try {
                                            long couponCd = account.getLong("couponCd");

                                            SPUtil.put(LoginActivity.this, "downco", String.valueOf(couponCd));
                                        } catch (Exception e) {

                                        }

                                        SPUtil.put(LoginActivity.this, "Token", Token);
                                        SPUtil.put(LoginActivity.this, "photo", photo1); //头像
                                        SPUtil.put(LoginActivity.this, "gen", gen); //性别
                                        SPUtil.put(LoginActivity.this, "name_one", name_one); //名字
                                        SPUtil.put(LoginActivity.this, "you_usid", userid + "");

                                        try {
                                            JSONArray coupons = data.getJSONArray("coupons");
                                            for (int i = 0; i < coupons.length(); i++) {
                                                if (i == 1) {
                                                    break;
                                                }
                                                JSONObject jsonObject1 = new JSONObject((String) coupons.get(i));
                                                String id1 = jsonObject1.getString("id");
                                                String couponSn = jsonObject1.getString("couponSn");
                                                String couponId = jsonObject1.getString("couponId");

                                                SPUtil.put(LoginActivity.this, "you_id", id1 + "");
                                                SPUtil.put(LoginActivity.this, "you_Sn", couponSn + "");
                                                SPUtil.put(LoginActivity.this, "you_type", couponId + "");
                                            }

                                        } catch (Exception e) {

                                        }

                                        try{boolean showUpgradeToToken = data.getBoolean("showUpgradeToToken");
                                            SPUtil.put(LoginActivity.this,"showUpgradeToToken",showUpgradeToToken);
                                        }catch (Exception e){

                                        }
                                        /**
                                         * 发消息调签到
                                         * */
                                        EventBusCarrier carrier=new EventBusCarrier();
                                        TokenExchangeBean bean=new TokenExchangeBean();
                                        bean.setSuToken(1);
                                        bean.setFlagToken(1);
                                        bean.setFlagCoupn(1);
                                        carrier.setEventType(EVENT_TYPE_SIGN);
                                        carrier.setObject(bean);
                                        EventBus.getDefault().post(carrier);
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        //Toast.makeText(LoginActivity.this,getResources().getString(R.string.lo),Toast.LENGTH_SHORT).show();
                                        ToastUtil.showgravity(LoginActivity.this, getResources().getString(R.string.lo));
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
                    } else {
                        ToastUtil.showgravity(LoginActivity.this, getResources().getString(R.string.fan));
                    }
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


    @OnClick({R.id.login_facce, R.id.login_no, R.id.login_forget, R.id.login_sign, R.id.login_deng, R.id.title_back_fl})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.title_back_fl:
                finish();
                break;
            case R.id.login_facce:
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));

                break;
            case R.id.login_no:
                finish();
                break;
            case R.id.login_deng:
                String email = login_email.getText().toString().trim();
                String password = login_pass.getText().toString().trim();
                String token = FirebaseInstanceId.getInstance().getToken();
                if (email.equals("")) {
                    ToastUtil.showgravity(LoginActivity.this, getResources().getString(R.string.logo_null));
                } else {
                   Http.OkHttpGet(LoginActivity.this, urlUtil + UrlUtil.isemailreg + "?email=" + email, new Http.OnDataFinish() {
                       @Override
                       public void OnSuccess(String result) {
                           try {
                               JSONObject jsonObject = new JSONObject(result);
                               if (jsonObject.getString("code").equals("0")){

                                   if (password.equals("")) {
                                       ToastUtil.showgravity(LoginActivity.this, getResources().getString(R.string.logo_pass));
                                       return;
                                   }
                                   String md5_password = AppUtil.MD5(password);
                                   FormBody body = new FormBody.Builder()
                                           .add("type", "4")
                                           .add("email", email)
                                           .add("password", md5_password)
                                           .add("notifyToken",token==null?"":token)
                                           .build();
                                   Http.OkHttpPost(LoginActivity.this, urlUtil + UrlUtil.login, body, new Http.OnDataFinish() {
                                       @Override
                                       public void OnSuccess(String result) {
                                           Log.d("Logining", result + urlUtil + UrlUtil.login+"=="+token);
                                           try {
                                               JSONObject jsonObject2 = new JSONObject(result);
                                               if (jsonObject2.getString("code").equals("0")) {
                                                   login_invalid.setVisibility(View.GONE);
                                                   JSONObject data2 = jsonObject2.getJSONObject("data");
                                                   JSONObject account = data2.getJSONObject("account");
                                                   try {
                                                       name = account.getString("name");
                                                   } catch (Exception e) {
                                                       name = "";
                                                   }
                                                   try {
                                                       long couponCd = account.getLong("couponCd");

                                                       SPUtil.put(LoginActivity.this, "downco", String.valueOf(couponCd));
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
                                                   SPUtil.put(LoginActivity.this, "photo", imgUrl);
                                                   SPUtil.put(LoginActivity.this, "Token", token);
                                                   SPUtil.put(LoginActivity.this, "name_one", name); //名字
                                                   SPUtil.put(LoginActivity.this, "you_usid", id);
                                                   SPUtil.put(LoginActivity.this, "email", email1);
                                                   SPUtil.put(LoginActivity.this, "gen", gender);
                                                   finish();
                                                   try{
                                                       boolean showUpgradeToToken = data2.getBoolean("showUpgradeToToken");
                                                       SPUtil.put(LoginActivity.this,"showUpgradeToToken",showUpgradeToToken);
                                                   }catch (Exception e){}
                                                   if (LoginMeActivity.instance!=null){
                                                       LoginMeActivity.instance.finish();
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

                                               } else if (jsonObject2.getString("code").equals("1006")) {
                                                   login_invalid.setVisibility(View.VISIBLE);
                                                   login_invalid.setText(getResources().getString(R.string.no_password));
                                               } else if (jsonObject2.getString("code").equals("1001")) {
                                                   ToastUtil.showgravity(LoginActivity.this, getResources().getString(R.string.logo_null));
                                               } else if (jsonObject2.getString("code").equals("5001")){
                                                   ToastUtil.showgravity(LoginActivity.this, getResources().getString(R.string.logo_null));
                                               }else {
                                                   ToastUtil.showgravity(LoginActivity.this, getResources().getString(R.string.fan));
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
                                   ToastUtil.showgravity(LoginActivity.this, getResources().getString(R.string.logo_null));
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
            case R.id.login_sign:
                Intent intent = new Intent(LoginActivity.this, SignActivity.class);
                startActivity(intent);
                break;
            case R.id.login_forget:
                Intent intent1 = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
