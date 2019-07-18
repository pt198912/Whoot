package com.app.whoot.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.CouponBean;
import com.app.whoot.bean.ExchangeCodeBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.view.custom.ClearEditText;
import com.app.whoot.ui.view.dialog.LoginCoupDialog;
import com.app.whoot.util.AnimationUtils;
import com.app.whoot.util.Constants;
import com.app.whoot.util.FireBaseEventKey;
import com.app.whoot.util.FirebaseReportUtils;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.app.whoot.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

import static com.app.whoot.util.Constants.TOKEN_FROM_COMMENT;
import static com.app.whoot.util.Constants.TOKEN_FROM_DAILY_SIGN;
import static com.app.whoot.util.Constants.TOKEN_FROM_EVENT;
import static com.app.whoot.util.Constants.TOKEN_FROM_EXCHANGE;
import static com.app.whoot.util.Constants.TOKEN_FROM_INVITED;
import static com.app.whoot.util.Constants.TOKEN_FROM_REFERRAL;
import static com.app.whoot.util.Constants.TOKEN_FROM_REGISTER;
import static com.app.whoot.util.Constants.TOKEN_FROM_VERSION_UPGRADE;

/**
 * 邀請碼/優惠碼
 */

public class RewardActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;
    @BindView(R.id.reward_name)
    ClearEditText rewardName;
    @BindView(R.id.reward_time)
    TextView rewardTime;

    private String userId;
    private String urlUtil;
    Handler mHandler = new Handler();

    @Override
    public int getLayoutId() {
        return R.layout.reward_activi;
    }

    @Override
    public void onCreateInit() {
        titleBackTitle.setText(getResources().getString(R.string.reward_cl));
        titleBackTxt.setText(getResources().getString(R.string.skip));
        userId = (String) SPUtil.get(this, "you_usid", "");
        urlUtil = (String) SPUtil.get(this, "URL", "");
        rewardTime.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        rewardTime.getBackground().setAlpha(100);
        setTextChangedListener();
    }
    public void setTextChangedListener(){
        if (rewardName.getText().toString().trim().length()>0){

            rewardTime.getBackground().setAlpha(255);
            rewardTime.setClickable(true);
        }else {

            rewardTime.getBackground().setAlpha(100);
            rewardTime.setClickable(false);
        }
        rewardName.addTextChangedListener(new TextWatcher() {

            private int oldLength = 0;
            private boolean isChange = true;
            private int curLength = 0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(isChange){
                    isChange=false;
                    return;
                }
                curLength = s.length();
                int length = s.length();
                if(start<=s.length()) {
                    String newInsert = s.subSequence(start, s.length()).toString();
                }
                if (length>0){
                    if (rewardName.toString().trim().length()>0){

                        rewardTime.getBackground().setAlpha(255);
                        rewardTime.setClickable(true);
                    }else {
                        rewardTime.getBackground().setAlpha(100);
                        rewardTime.setClickable(false);
                    }

                }else {
                    rewardTime.getBackground().setAlpha(100);
                    rewardTime.setClickable(false);
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

    }

    @OnClick({R.id.title_back_fl, R.id.reward_time,R.id.title_back_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                reportPageShown();
                finish();
                break;
            case R.id.reward_time:
                String rewardNametrim = rewardName.getText().toString().trim().toUpperCase();
                FormBody body = new FormBody.Builder()
                        .add("code", rewardNametrim)
                        .add("userId", userId)
                        .build();
                Http.OkHttpPost(RewardActivity.this, urlUtil + UrlUtil.Check_Code, body, new Http.OnDataFinish() {
                    @Override
                    public void OnSuccess(String result) {
                        Log.d("yaoqingma",result);
                        try {
                            JSONObject object = new JSONObject(result);
                            if (object.getString("code").equals("0")){
                                getInvite(rewardNametrim);
                            }else if (object.getString("code").equals("5900")){
                                ToastUtil.showgravity(RewardActivity.this,getResources().getString(R.string.reward_code));
                            }else if (object.getString("code").equals("5901")){
                                ToastUtil.showgravity(RewardActivity.this,getResources().getString(R.string.invite_limit));
                            }else if (object.getString("code").equals("5902")){
                                ToastUtil.showgravity(RewardActivity.this,getResources().getString(R.string.invite_one));
                            }else if (object.getString("code").equals("5903")){
                                ToastUtil.showgravity(RewardActivity.this,getResources().getString(R.string.invite_cannot));
                            }else if (object.getString("code").equals("5904")){
                                ToastUtil.showgravity(RewardActivity.this,getResources().getString(R.string.reward_code));
                            }else if (object.getString("code").equals("5905")){
                                ToastUtil.showgravity(RewardActivity.this,getResources().getString(R.string.fan));
                            }else if (object.getString("code").equals("5906")){
                                ToastUtil.showgravity(RewardActivity.this,getResources().getString(R.string.reward_code));
                            }else if (object.getString("code").equals("5907")){
                                ToastUtil.showgravity(RewardActivity.this,getResources().getString(R.string.invite_already));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                reportPageGo();
                break;
            case R.id.title_back_txt:
                finish();
                break;
        }
    }
    //埋点
    private void reportPageGo() {
        Bundle b=new Bundle();
        b.putString("type","CLICK_FB_INVITE_ACTION");
        b.putString("btn","ENTER");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.FBLOGIN_GO,b);
    }

    private void reportPageShown() {
        Bundle b=new Bundle();
        b.putString("type","CLICK_FB_INVITE_ACTION");
        b.putString("btn","SKIP");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.FBLOGIN_SKIP,b);
    }

    LoginCoupDialog loginCoupDialog;
    /**
     * 邀请码/活动码兑换
     * */
    public void getInvite(String code){


        FormBody body = new FormBody.Builder()
                .add("code", code)
                .build();
        Http.OkHttpPost(RewardActivity.this, urlUtil + UrlUtil.TOKEN_REDEEM, body, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                Log.d("duihuan",result+urlUtil + UrlUtil.TOKEN_REDEEM);
                dissLoad();
                try {
                    JSONObject obj=new JSONObject(result);
                    int code=obj.optInt("code");
                    if(code==0) {
                        JSONArray data = obj.getJSONArray("data");
                        if(data!=null&&data.length()>0){
                            List<ExchangeCodeBean> beanList=new ArrayList<>();
                            for(int i=0;i<data.length();i++) {
                                ExchangeCodeBean exchangeCodeBean = GSonUtil.parseGson(data.get(i).toString(), ExchangeCodeBean.class);
                                initTokenFromDesc(exchangeCodeBean);
                                beanList.add(exchangeCodeBean);
                            }
                            writeReawrdInfo(beanList);
                            List<Integer> animTypeList=new ArrayList<>();
                            for(ExchangeCodeBean bean:beanList){
                                animTypeList.add(bean.getCouponId()+ Constants.TYPE_COUPON_GAIN_COIN_BASE);
                            }
                            finish();

                        }else{
                            Util.showErrorMsgDlg(RewardActivity.this,getString(R.string.verify_code_fail),mHandler);
                        }
                    }else{
                        Util.showErrorMsgDlg(RewardActivity.this,getString(R.string.verify_code_fail),mHandler);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showgravity(RewardActivity.this,getString(R.string.verify_code_fail));
                }
            }

            @Override
            public void onError(Exception e) {
                dissLoad();
                ToastUtil.showgravity(RewardActivity.this,getString(R.string.verify_code_fail));
            }
        });
    }
    private void writeReawrdInfo(List<ExchangeCodeBean> exchangeCodeBeanList){
        int brozenNum=0;
        int silverNum=0;
        int goldNum=0;
        int diamondNum=0;
        for(ExchangeCodeBean bean:exchangeCodeBeanList){
            switch (bean.getCouponId()) {
                case Constants.TYPE_BROZEN:
                    brozenNum++;
                    SPUtil.put(RewardActivity.this, "brozen_desc", bean.getDesc());
                    break;
                case Constants.TYPE_SILVER:
                    silverNum++;
                    SPUtil.put(RewardActivity.this, "silver_desc", bean.getDesc());
                    break;
                case Constants.TYPE_GOLD:
                    goldNum++;
                    SPUtil.put(RewardActivity.this, "gold_desc", bean.getDesc());
                    break;
                case Constants.TYPE_DIAMOND:
                    diamondNum++;
                    SPUtil.put(RewardActivity.this, "diamond_desc", bean.getDesc());
                    break;
            }
        }
        SPUtil.put(RewardActivity.this, "My_tong", brozenNum);
        SPUtil.put(RewardActivity.this, "My_yin", silverNum);
        SPUtil.put(RewardActivity.this, "My_jin", goldNum);
        SPUtil.put(RewardActivity.this, "My_zuan", diamondNum);
    }

    private void startCoinAnimList(List<Integer> animTypeList){
        if(animTypeList==null||animTypeList.size()==0){
            return;
        }
        List<Integer> filterList=new ArrayList<>();
        for(int i=0;i<animTypeList.size();i++){
            int animType=animTypeList.get(i);
            boolean exist=false;
            for(Integer tmp:filterList){
                if(tmp==animType){
                    exist=true;
                    break;
                }
            }
            if(!exist){
                filterList.add(animType);
            }
        }
        int delay=0;
        for(int i=0;i<filterList.size();i++){
            final int animType=filterList.get(i);
            try {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, delay);
            }catch (Exception e){
                e.printStackTrace();
            }
            delay+=3000;
        }
    }

    private void initTokenFromDesc(Object obj){
        if(obj==null){
            return;
        }
        if(obj instanceof ExchangeCodeBean){
            ExchangeCodeBean bean=(ExchangeCodeBean)obj;
            bean.setDesc(getDesc(bean.getTokenFrom()));
        }else if(obj instanceof CouponBean.DataBean.CouponsBean){
            CouponBean.DataBean.CouponsBean bean=(CouponBean.DataBean.CouponsBean)obj;
            bean.setTokenFromDesc(getDesc(bean.getTokenFrom()));
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
