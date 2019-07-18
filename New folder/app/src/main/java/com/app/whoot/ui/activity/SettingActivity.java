package com.app.whoot.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.fragment.MyFragment;
import com.app.whoot.ui.view.dialog.ClearlDialog;
import com.app.whoot.ui.view.dialog.CreaDialog;
import com.app.whoot.ui.view.dialog.GifDialog;
import com.app.whoot.ui.view.popup.LoginPopupWindow;
import com.app.whoot.util.DataCleanManager;
import com.app.whoot.util.PopupWindowUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.UrlUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

/**
 * Created by Sunrise on 4/3/2018.
 * 设定
 */

public class SettingActivity extends BaseActivity implements LoginPopupWindow.OnItemClickListener {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.exchange)
    LinearLayout exchange;
    @BindView(R.id.set_help)
    LinearLayout setHelp;
    @BindView(R.id.set_about)
    LinearLayout setAbout;
    @BindView(R.id.set_clear)
    TextView setClear;
    @BindView(R.id.set_clear_ly)
    LinearLayout setClearLy;
    @BindView(R.id.set_out)
    LinearLayout setOut;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;
    @BindView(R.id.title_back_img)
    ImageView titleBackImg;
    @BindView(R.id.set_fuwu)
    LinearLayout setFuwu;
    @BindView(R.id.set_yinsi)
    LinearLayout setYinsi;
    @BindView(R.id.set_ly)
    LinearLayout setLy;
    @BindView(R.id.set_map)
    LinearLayout set_map;
    @BindView(R.id.set_security)
    LinearLayout set_security;

    private LoginPopupWindow popup;
    private static final String TAG = "SettingActivity";
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int arg1 = msg.arg1;


            boolean destroyed = isFinishing();
            if (!destroyed){
                setClear.setText("");
                creaDia = new ClearlDialog(SettingActivity.this, R.style.box_dialog);
                creaDia.show();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        Looper.prepare();
                        try {

                            Thread.sleep(1000);
                            creaDia.dismiss();


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Looper.loop();
                    }
                }.start();
            }


        }
    };
    private ClearlDialog creaDia;
    private CreaDialog creaDialog;
    private String token;
    private String urlUtil;
    public GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;


    @Override
    public int getLayoutId() {
        return R.layout.set_activi;
    }

    @Override
    public void onCreateInit() {


        //初始化谷歌登录服务
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()   //获取邮箱
                .requestId()      //获取id 号
                .requestIdToken("456212545785")  //获取token
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        titleBackTitle.setText(R.string.setting);
        try {
            String cacheSize = DataCleanManager.getTotalCacheSize(this);
            if (cacheSize.equals("0K")) {
                setClear.setText("");
            } else {
                setClear.setText(cacheSize);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        token = (String) SPUtil.get(SettingActivity.this, "Token", "");
        urlUtil = (String) SPUtil.get(SettingActivity.this, "URL", "");
        if (token.equals("")) {
            setOut.setVisibility(View.GONE);

        } else {
            setOut.setVisibility(View.VISIBLE);

        }

    }


    @OnClick({R.id.exchange, R.id.set_help, R.id.set_about, R.id.set_clear_ly, R.id.set_out, R.id.title_back_fl,R.id.set_fuwu,R.id.set_yinsi,R.id.set_map,R.id.set_security})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exchange:
                Intent intent2 = new Intent(SettingActivity.this, LuchangeActivity.class);
                startActivity(intent2);
                break;
            case R.id.set_help:
                Intent intent1 = new Intent(SettingActivity.this, HelpActivity.class);
                startActivity(intent1);
                break;
            case R.id.set_about:  //关于
                Intent intent = new Intent(SettingActivity.this, WebViewActivity.class);
                intent.putExtra("html",4);
                startActivity(intent);
                break;
            case R.id.set_clear_ly:

                creaDialog = new CreaDialog(this, R.style.box_dialog);
                creaDialog.show();
                DataCleanManager.clearAllCache(this);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        Looper.prepare();
                        try {
                            Thread.sleep(3000);
                            creaDialog.dismiss();

                            Message message = handler.obtainMessage(3);
                            handler.sendMessage(message);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Looper.loop();
                    }
                }.start();
                /*ClearlDialog creaDia= new ClearlDialog(this, R.style.box_dialog);
                creaDia.show();*/

                break;
            case R.id.set_out:

                popup = new LoginPopupWindow(SettingActivity.this);
                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        PopupWindowUtil.setWindowTransparentValue(SettingActivity.this, 1.0f);
                    }
                });
                PopupWindowUtil.setWindowTransparentValue(SettingActivity.this, 0.5f);
                //设置PopupWindow中的位置
                popup.showAtLocation(SettingActivity.this.findViewById(R.id.set_ly), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                popup.setOnItemClickListener(this);
                break;
            case R.id.title_back_fl:
                finish();
                /*GifDialog gifDialog = new GifDialog(this,R.style.dialog_one);
                gifDialog.show();*/
                break;
            case R.id.set_fuwu:
                Intent intent3 = new Intent(SettingActivity.this, WebViewActivity.class);
                intent3.putExtra("html", 2);
                startActivity(intent3);
                break;
            case R.id.set_yinsi:
                Intent intent4 = new Intent(SettingActivity.this, WebViewActivity.class);
                intent4.putExtra("html", 1);
                startActivity(intent4);
                break;
            case R.id.set_map:  //地图
                Intent intent5 = new Intent(SettingActivity.this, MapsActivity.class);
                startActivity(intent5);
                break;
            case R.id.set_security:
                Intent intent6 = new Intent(SettingActivity.this, SecurityActivity.class);
                startActivity(intent6);
                break;
        }
    }

    @Override
    public void setOnItemClick(View v) {
        switch (v.getId()) {
            case R.id.login_pop_out:

                FormBody build = new FormBody.Builder()
                        .add("accessToken", token)
                        .build();
                Http.OkHttpPost(SettingActivity.this, urlUtil + UrlUtil.logout, build, new Http.OnDataFinish() {
                    @Override
                    public void OnSuccess(String result) {
                        try {
                            JSONObject object = new JSONObject(result);
                            Log.d("setting11111111111111",result);
                            if (object.getString("code").equals("0")) {
                                signOut();
                                SPUtil.remove(SettingActivity.this, "Token");
                                SPUtil.remove(SettingActivity.this,"accToken");
                                SPUtil.remove(SettingActivity.this, "photo1");
                                SPUtil.remove(SettingActivity.this, "photo");
                                SPUtil.remove(SettingActivity.this, "gen");
                                SPUtil.remove(SettingActivity.this, "name_one");
                                SPUtil.remove(SettingActivity.this, "downco");// 清除倒计时
                                SPUtil.remove(SettingActivity.this,"My_tong");
                                SPUtil.remove(SettingActivity.this,"My_yin");
                                SPUtil.remove(SettingActivity.this,"My_jin");
                                SPUtil.remove(SettingActivity.this,"My_zuan");
                                SPUtil.remove(SettingActivity.this,"you_usid");
                                SPUtil.remove(SettingActivity.this,"you_tel");
                                SPUtil.put(SettingActivity.this, "deng", false);
                                popup.dismiss();
                                finish();
                                Intent login=new Intent(SettingActivity.this,LoginMeActivity.class);
                                login.putExtra("startFromSetingsAct",true);
                                startActivity(login);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

                break;
            case R.id.login_pop_cr:
                popup.dismiss();
                break;
        }
    }
    /**
     * 退出google 登录
     * */
    public void signOut() {
        // Firebase sign out
        mAuth.signOut();
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });

    }

    private void updateUI(FirebaseUser user){

        if (user != null) {
            user.getEmail();
            user.getUid();
        } else {

        }
    }
}
