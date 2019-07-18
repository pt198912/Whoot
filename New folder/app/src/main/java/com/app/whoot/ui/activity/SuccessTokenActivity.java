package com.app.whoot.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.CardBean;
import com.app.whoot.bean.ExchBean;
import com.app.whoot.bean.TokenExchangeBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.app.whoot.util.Constants.EVENT_TYPE_TOKEN_EXCHANGE;

public class SuccessTokenActivity extends BaseActivity {
    @BindView(R.id.succ_token_img)
    ImageView succTokenImg;
    @BindView(R.id.succ_token_num)
    TextView succTokenNum;
    @BindView(R.id.succ_token_name)
    TextView succTokenName;
    @BindView(R.id.succ_token_l)
    View succTokenL;
    @BindView(R.id.ll_write_review)
    View writeReviewLayout;
    private int flag_token=0;
    private int flag_coupon=0;
    private String mShopId;
    private static final String TAG = "SuccessTokenActivity";
    @Override
    public int getLayoutId() {
        return R.layout.succ_token;
    }

    @Override
    public void onCreateInit() {
        Intent intent = getIntent();
        init(intent);
        
    }

    public void init(Intent intent){
        String couponId_item = intent.getStringExtra("couponId_item");
        String costs_item = intent.getStringExtra("costs_item");
        String commodityName_item = intent.getStringExtra("commodityName_item");
        String couponCd = intent.getStringExtra("couponCd");
        String regainCouponMap = intent.getStringExtra("regainCouponMap");
        String couponType = intent.getStringExtra("couponType");
        mShopId=intent.getStringExtra("shopId");
        if (couponType.equals("1")){
            succTokenImg.setBackgroundResource(R.drawable.token_icon_bronze);
        }else if (couponType.equals("2")){
            succTokenImg.setBackgroundResource(R.drawable.token_icon_silver);
        }else if (couponType.equals("3")){
            succTokenImg.setBackgroundResource(R.drawable.token_icon_gold);
        }else if (couponType.equals("4")){
            succTokenImg.setBackgroundResource(R.drawable.token_icon_diamond);
        }
        if (!regainCouponMap.equals("{}")){
            flag_token=1;
        }
        Log.d("SuccessTo666666666666",couponId_item+"=="+regainCouponMap+"===");

        succTokenName.setText(commodityName_item);
        if (couponId_item.equals("1")){
            flag_coupon=1;
            succTokenImg.setBackgroundResource(R.drawable.token_icon_bronze);
        }else if (couponId_item.equals("2")){
            flag_coupon=2;
            succTokenImg.setBackgroundResource(R.drawable.token_icon_silver);
        }else if (couponId_item.equals("3")){
            flag_coupon=3;
            succTokenImg.setBackgroundResource(R.drawable.token_icon_gold);
        }else if (couponId_item.equals("4")){

            succTokenImg.setBackgroundResource(R.drawable.token_icon_diamond);
        }
        succTokenNum.setText("-"+costs_item);
        if (couponCd != null) {
            if (couponCd.length() > 0) {
                SPUtil.put(this, "downco", couponCd.toString());
            }
        }
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ");
        init(intent);
    }
    List<ExchBean> mTokenHistory=new ArrayList<>();
    private static final int REQUEST_CODE_WRIRE_REVIEW=0x4433;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_WRIRE_REVIEW){
            if(resultCode==RESULT_OK){
                writeReviewLayout.setVisibility(View.GONE);
            }
        }
    }

    public void getCommentInfo() {
        String token = (String) SPUtil.get(this, "Token", "");
        String  urlUtil = (String) SPUtil.get(this, "URL", "");
        String url = urlUtil+ UrlUtil.history + "?start=0" + "&rows=" + 5;
        if (token.length() != 0) {
            Http.OkHttpGet(this, url, new Http.OnDataFinish() {
                @Override
                public void OnSuccess(String result) {
                    dissLoad();
                    Log.d(TAG, "token history:"+result + "==" + url);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        if (code.equals("0")) {
                            mTokenHistory.clear();
                            JSONArray data = jsonObject.getJSONArray("data");
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    ExchBean bean = GSonUtil.parseGson(data.getString(i), ExchBean.class);
                                    mTokenHistory.add(bean);
                                }
                                if(mTokenHistory.size()>0){
                                    ExchBean exchBean=mTokenHistory.get(0);
                                    Intent intent1 = new Intent(SuccessTokenActivity.this, WriteActivity.class);
                                    intent1.putExtra("shopid", String.valueOf(exchBean.getShopId()));
                                    intent1.putExtra("commodityId", String.valueOf(exchBean.getCommodityId()));
                                    intent1.putExtra("shopName", exchBean.getShopName());
                                    intent1.putExtra("commodityName", exchBean.getCommodityName());
                                    intent1.putExtra("couponHistoryId",exchBean.getId());
                                    intent1.putExtra("couponId", String.valueOf(exchBean.getCouponId()));
                                    intent1.putExtra("useCount", String.valueOf(exchBean.getUseCount()));
                                    intent1.putExtra("from_success_token_page",true);
                                    startActivityForResult(intent1,REQUEST_CODE_WRIRE_REVIEW);
                                }
                            }

                        } else {
                            ToastUtil.showgravity(SuccessTokenActivity.this, getResources().getString(R.string.jia));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Exception e) {
                    ToastUtil.showgravity(SuccessTokenActivity.this, getResources().getString(R.string.jia));
                    dissLoad();

                }
            });
        }
    }

    @OnClick({R.id.succ_token_name, R.id.succ_token_l,R.id.ll_write_review})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_write_review:
                loading(getString(R.string.loading));
                getCommentInfo();
                break;
            case R.id.succ_token_l:
//                Intent intent = new Intent(this, MainActivity.class);
//                intent.putExtra("su_token",1);
//                intent.putExtra("flag_token",flag_token);
//                intent.putExtra("flag_coupon",flag_coupon);
//                startActivity(intent);
                EventBusCarrier carrier=new EventBusCarrier();
                TokenExchangeBean bean=new TokenExchangeBean();
                bean.setSuToken(1);
                bean.setFlagToken(flag_token);
                bean.setFlagCoupn(flag_coupon);
                carrier.setEventType(EVENT_TYPE_TOKEN_EXCHANGE);
                carrier.setObject(bean);
                EventBus.getDefault().post(carrier);
                finish();
                if(StoreDetailActivity.instance!=null) {
                    StoreDetailActivity.instance.finish();
                }
                if (CoupDetaiActivity.instance!=null){
                    CoupDetaiActivity.instance.finish();
                }
                if (SearchActivity.instance!=null){
                    SearchActivity.instance.finish();
                }
                break;
            case R.id.succ_token_name:

                finish();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        CoupDetaiActivity.instance=null;
        StoreDetailActivity.instance=null;
        ResultsActivity.instance=null;
    }
}
