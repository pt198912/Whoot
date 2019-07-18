package com.app.whoot.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.ConfiguraBean;
import com.app.whoot.bean.TouristsBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.ui.view.PermissonCode;
import com.app.whoot.util.AppUtil;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.FireBaseEventKey;
import com.app.whoot.util.FirebaseReportUtils;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.PermissionUtils;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

//引导页
public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.text_min)
    TextView textMin;
    @BindView(R.id.welcome_ly)
    LinearLayout welcome_ly;

    private int time = 2;
    private int flag = 0;   //第一次启动的标记
    private String token = "";
    private String news = "ceshi";
    private List<TouristsBean> list = new ArrayList<>();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {


            try {
                time--;
                handler.postDelayed(this, 1000);
                textMin.setText("");
            } catch (Exception e) {

            }

            if (time == 0) {
                //判断是否是第一次启动
                Log.d("isFirstUse", String.valueOf(isFirstUse));
                if (isFirstUse) {
                    //跳转引导页
                    Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
                    startActivity(intent);
                    getRemovehand();
                    //实例化Editor对象
                    SharedPreferences.Editor editor = preferences.edit();
                    //存入数据
                    editor.putBoolean("isFirstUse", false);
                    Log.d("Welcome", "save isFirstUse: false");
                    //提交修改
                    editor.commit();
                } else {
                    //跳转主页 || accToken.length()!=0
                  /*  if (versionName.equals("2.0.0")){
                        Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
                        startActivity(intent);

                    }else {

                    }*/
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    getRemovehand();
                }


                finish();
            } else {

            }
        }
    };
    private String token1;
    private String accToken;

    //是否是第一次使用
    private boolean isFirstUse;
    private String versionName;
    private SharedPreferences preferences;
    private String url1;


    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    public void getRemovehand() {
        //结束线程
        handler.removeCallbacks(runnable);
    }


    @Override
    public void onCreateInit() {

        int confirm = (int) SPUtil.get(WelcomeActivity.this, "confirm", 2);//默认中文
        /*if (isZh(this)) {
            confirm = 3;
        } else {
            confirm = 2;
        }*/

        SPUtil.put(this, "luchang", confirm + "");

        if (confirm == 1) {  //简中
            changeLocal(Locale.SIMPLIFIED_CHINESE);
            welcome_ly.setBackgroundResource(R.drawable.startpage_fan);
        } else if (confirm == 2) { //繁中
            changeLocal(Locale.TAIWAN);
            welcome_ly.setBackgroundResource(R.drawable.startpage_fan);
        } else if (confirm == 3) { //英语
            changeLocal(Locale.ENGLISH);
            welcome_ly.setBackgroundResource(R.drawable.startpage_en);
        }
        handler.postDelayed(runnable, 1000);

        preferences = getSharedPreferences("isFirstUse", MODE_PRIVATE);
        isFirstUse = preferences.getBoolean("isFirstUse", true);
        Log.d("ppt", "onCreateInit: isFirstUse "+isFirstUse);
        versionName = AppUtil.getVersionName(this);

        //用户登录
        token1 = (String) SPUtil.get(WelcomeActivity.this, "Token", "");
        //游客登录
        accToken = (String) SPUtil.get(WelcomeActivity.this, "accToken", "");


        SPUtil.remove(this, "arg_right_name");
        SPUtil.remove(this, "arg_lefte");
        /**
         * 获取facebook
         *
         * */
        try {
            int i = 0;
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                i++;
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String KeyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                //KeyHash 就是你要的，不用改任何代码  复制粘贴 ;   测试： U+JaifQVKv2cL2rKN9xU+IL/UvQ=  正式：3wf32T32kn1sdZyFJ4Fht53hn8M=
                Log.d("womenbuyiy", KeyHash);
                //  Toast.makeText(WelcomeActivity.this,KeyHash.toString(),Toast.LENGTH_SHORT).show();
            }


        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        Http.OkHttpGet(this, UrlUtil.Release, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray versions = data.getJSONArray("Versions");
                    String newestVersion = data.getString("NewestVersion");
                    boolean upgradeNew = data.getBoolean("UpgradeNew");
                    SPUtil.put(WelcomeActivity.this,"newestVersion",newestVersion);
                    SPUtil.put(WelcomeActivity.this,"upgradeNew",upgradeNew);
                    for (int i = 0; i < versions.length(); i++) {
                        JSONObject jsonObject1 = versions.getJSONObject(i);
                        String version = jsonObject1.getString("Version");
                        if (versionName.equals(version)) {
                            url1 = jsonObject1.getString("URL");
                            String cfgURL = jsonObject1.getString("CfgURL");
                            SPUtil.put(WelcomeActivity.this, "URL", url1);
                            SPUtil.put(WelcomeActivity.this,"cfgURL",cfgURL);
                            break;
                        }
                    }
                    //url = "http://172.16.47.75:8888/";  http://172.16.0.211:8082/
                    //url="https://qc.whoot.com/";
                    //url="https://hk.whoot.com/";
                    if(TextUtils.isEmpty(url1)){
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }

        });
        String url = (String) SPUtil.get(this, "URL", "");
        if (url.equals("")) {
            SPUtil.put(this, "URL", "https://hk.whoot.com/");
        }


        reportPageShown();
    }

    private void reportPageShown() {
        Bundle bundle = new Bundle();
        bundle.putString("type","LAUNCH");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.EVENT_LAUNCH,bundle);
    }


    private void changeLocal(Locale locale) {
        setAppLocal(locale);

    }

    //语言切换
    private void setAppLocal(Locale locale) {
        Resources resources = this.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);
    }


}
