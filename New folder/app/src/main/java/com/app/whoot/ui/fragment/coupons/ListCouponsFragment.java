package com.app.whoot.ui.fragment.coupons;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;


import android.support.annotation.NonNull;


import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.fragment.BaseFragment;
import com.app.whoot.bean.ActivityBean;
import com.app.whoot.bean.CheckCodeBean;
import com.app.whoot.bean.ConfiguraBean;
import com.app.whoot.bean.CouponBean;
import com.app.whoot.bean.CycleRecord;
import com.app.whoot.bean.ExchangeCodeBean;
import com.app.whoot.bean.GainTokenBean;
import com.app.whoot.bean.TokenExchangeBean;
import com.app.whoot.manager.ConfigureInfoManager;
import com.app.whoot.modle.http.Http;

import com.app.whoot.ui.activity.WebViewActivity;

import com.app.whoot.ui.activity.RewardActivity;

import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.ui.activity.main.TransActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.ui.view.MyScrollView;
import com.app.whoot.ui.view.RoundProgressBar;
import com.app.whoot.ui.view.custom.TimerTextView;
import com.app.whoot.ui.view.dialog.ListTokenDialog;
import com.app.whoot.ui.view.dialog.LoginCoupDialog;
import com.app.whoot.ui.view.dialog.OfferDialog;
import com.app.whoot.ui.view.dialog.TokenDialog;
import com.app.whoot.ui.view.popup.StorePopupWindow;
import com.app.whoot.util.AnimationUtils;
import com.app.whoot.util.Constants;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.FireBaseEventKey;
import com.app.whoot.util.FirebaseReportUtils;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.KeyboardUtils;
import com.app.whoot.util.PopupWindowUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.TimerUtils;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.app.whoot.util.Util;
import com.donkingliang.banner.CustomBanner;
import com.facebook.CallbackManager;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.FormBody;


import static com.app.whoot.util.Constants.ACTIVITY_CODE_MAX;
import static com.app.whoot.util.Constants.ACTIVITY_CODE_NOTFOUND;
import static com.app.whoot.util.Constants.ACTIVITY_CODE_USED;
import static com.app.whoot.util.Constants.COMMODITY_IDS_WORLD_ERROR;
import static com.app.whoot.util.Constants.EVENT_TYPE_GAIN_TOKEN;
import static com.app.whoot.util.Constants.EVENT_TYPE_TOKEN_ANIM;
import static com.app.whoot.util.Constants.INVITER_ACTIVITY_CODE_LENGTH_ERROR;
import static com.app.whoot.util.Constants.INVITER_CODE_NOTFOUND;
import static com.app.whoot.util.Constants.INVITER_CODE_SELF;
import static com.app.whoot.util.Constants.INVITER_CODE_WORLD_ERROR;
import static com.app.whoot.util.Constants.SP_KEY_KOL_JOIN_FROM;
import static com.app.whoot.util.Constants.TOKEN_FROM_COMMENT;
import static com.app.whoot.util.Constants.TOKEN_FROM_DAILY_SIGN;
import static com.app.whoot.util.Constants.TOKEN_FROM_EVENT;
import static com.app.whoot.util.Constants.TOKEN_FROM_EXCHANGE;
import static com.app.whoot.util.Constants.TOKEN_FROM_INVITED;
import static com.app.whoot.util.Constants.TOKEN_FROM_REFERRAL;
import static com.app.whoot.util.Constants.TOKEN_FROM_REGISTER;
import static com.app.whoot.util.Constants.TOKEN_FROM_VERSION_UPGRADE;
import static com.app.whoot.util.Constants.TYPE_BROZEN;
import static com.app.whoot.util.Constants.TYPE_DIAMOND;
import static com.app.whoot.util.Constants.TYPE_GOLD;
import static com.app.whoot.util.Constants.TYPE_SILVER;

/**
 * Created by Sunrise on 1/15/2019.
 */

public class ListCouponsFragment extends BaseFragment implements MainActivity.OnDispatchTouchListener {

    @BindView(R.id.frag_time)
    LinearLayout fragTime;
    @BindView(R.id.frag_store)
    TextView fragStore;
    @BindView(R.id.frag_ly_to)
    LinearLayout fragLyTo;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.card_scro)
    MyScrollView card_scro;
    @BindView(R.id.card_dao)
    LinearLayout cardDao;
    @BindView(R.id.frag_time_t)
    TimerTextView frag_time_t;
    @BindView(R.id.parent)
    LinearLayout parent;
    @BindView(R.id.bronze_num)
    TextView bronzeNum;
    @BindView(R.id.bronze_img)
    ImageView bronzeImg;
    @BindView(R.id.branze_rxpiry)
    TextView branzeRxpiry;
    @BindView(R.id.bronze_use_ly)
    LinearLayout bronzeUseLy;
    @BindView(R.id.silver_number)
    TextView silverNumber;
    @BindView(R.id.silver_img)
    ImageView silverImg;
    @BindView(R.id.silver_expiry)
    TextView silverExpiry;
    @BindView(R.id.silver_use_ly)
    LinearLayout silverUseLy;
    @BindView(R.id.gold_number)
    TextView goldNumber;
    @BindView(R.id.gold_img)
    ImageView goldImg;
    @BindView(R.id.gold_expiry)
    TextView goldExpiry;
    @BindView(R.id.gold_use_ly)
    LinearLayout goldUseLy;
    @BindView(R.id.diamond_number)
    TextView diamondNumber;
    @BindView(R.id.diamond_img)
    ImageView diamondImg;
    @BindView(R.id.diamond_expiry)
    TextView diamondExpiry;
    @BindView(R.id.diamond_use_ly)
    LinearLayout diamondUseLy;
    @BindView(R.id.silver_ly)
    LinearLayout silver_ly;
    @BindView(R.id.gold_ly)
    LinearLayout gold_ly;
    @BindView(R.id.diamond_ly)
    LinearLayout diamond_ly;
    @BindView(R.id.silver_txet)
    TextView silver_txet;
    @BindView(R.id.gold_text)
    TextView gold_text;
    @BindView(R.id.diamond_text)
    TextView diamond_text;
    @BindView(R.id.no_network_view)
    View noNetworkView;


    @BindView(R.id.pb_gold)
    RoundProgressBar pbGold;
    @BindView(R.id.pb_diamond)
    RoundProgressBar pbDiamond;
    @BindView(R.id.tv_gold_use)
    TextView goldUseTv;
    @BindView(R.id.iv_arrow_gold)
    ImageView arrowGoldIv;
    @BindView(R.id.tv_gold_progress)
    TextView tvGoldProgres;
    @BindView(R.id.iv_diamond_coin)
    ImageView ivDiamondCoin;
    @BindView(R.id.tv_gold_progress_max)
    TextView tvGoldProgressMax;
    @BindView(R.id.iv_gold_coin)
    ImageView ivGoldCoin;
    @BindView(R.id.iv_arrow_silver)
    ImageView ivArrowSilver;
    @BindView(R.id.tv_silver_progress)
    TextView tvSilverProgress;
    @BindView(R.id.tv_silver_progress_max)
    TextView tvSilverProgressMax;
    @BindView(R.id.pb_silver)
    RoundProgressBar pbSilver;
    @BindView(R.id.iv_silver_coin)
    ImageView ivSilverCoin;
    @BindView(R.id.iv_arrow_brozen)
    ImageView ivArrowBrozen;
    @BindView(R.id.tv_brozen_progress)
    TextView tvBrozenProgress;
    @BindView(R.id.tv_brozen_progress_max)
    TextView tvBrozenProgressMax;
    @BindView(R.id.pb_bronze)
    RoundProgressBar pbBronze;
    @BindView(R.id.iv_bronze_coin)
    ImageView ivBronzeCoin;
    Unbinder unbinder;
    @BindView(R.id.tv_diamond_num)
    TextView tvDiamondNum;
    @BindView(R.id.tv_gold_num)
    TextView tvGoldNum;
    @BindView(R.id.tv_gold_expire_time)
    TextView tvGoldExpireTime;
    @BindView(R.id.tv_silver_num)
    TextView tvSilverNum;
    @BindView(R.id.tv_silver_expire_time)
    TextView tvSilverExpireTime;
    @BindView(R.id.tv_brozen_num)
    TextView tvBrozenNum;
    @BindView(R.id.tv_brozen_expire_time)
    TextView tvBrozenExpireTime;
    @BindView(R.id.tv_diamond_use)
    TextView tvDiamondUse;
    @BindView(R.id.tv_silver_use)
    TextView tvSilverUse;
    @BindView(R.id.tv_brozen_use)
    TextView tvBrozenUse;
    @BindView(R.id.iv_diamond_info)
    ImageView ivDiamondInfo;
    @BindView(R.id.ll_diamond_info)
    LinearLayout llDiamondInfo;
    @BindView(R.id.iv_gold_show_info)
    ImageView ivGoldShowInfo;
    @BindView(R.id.ll_gold_info)
    LinearLayout llGoldInfo;
    @BindView(R.id.iv_silver_info)
    ImageView ivSilverInfo;
    @BindView(R.id.ll_silver_info)
    LinearLayout llSilverInfo;
    @BindView(R.id.iv_brozen_info)
    ImageView ivBrozenInfo;
    @BindView(R.id.ll_brozen_info)
    LinearLayout llBrozenInfo;
    @BindView(R.id.tv_diamond_expire_time)
    TextView tvDiamonExpireTime;
    @BindView(R.id.tv_use_diamond_tip)
    TextView tvUseDiamondTip;
    @BindView(R.id.tv_exchange_gold_tip)
    TextView tvExchangeGoldTip;
    @BindView(R.id.tv_exchange_silver_tip)
    TextView tvExchangeSilverTip;
    @BindView(R.id.tv_exchange_brozen_tip)
    TextView tvExchangeBrozenTip;
    @BindView(R.id.iv_metal_coin)
    ImageView ivMetalCoin;
    @BindView(R.id.iv_effect)
    ImageView lightEffectView;
    @BindView(R.id.fl_bg)
    View flBgView;
    @BindView(R.id.ll_content)
    View contentLayout;
    @BindView(R.id.et_input_invite_code)
    EditText inputInviteCodeEt;
    @BindView(R.id.tv_confirm)
    TextView confirmTv;
    @BindView(R.id.iv_delete_icon)
    ImageView deleteIv;


    private List<CouponBean> couponlist = new ArrayList<CouponBean>();

    private List<ConfiguraBean> configuralist = new ArrayList<ConfiguraBean>();

    private OfferDialog browsDialog;

    /**
     * 两天转成毫秒
     */
    private long MS = 172800000;

    private int card_one = 0;
    private int card_twe = 0;
    private int card_three = 0;
    private int card_four = 0;
    private String couponSn;
    private int couponId;
    private int couponType;
    private int clickableSpan;

    private CallbackManager callbackManager;
    private Gson gson;
    private String couponSn_yin;
    private int id_tong;
    private int id_yin;
    private int couponType_yin;
    private String couponSn_jin;
    private int id_jin;
    private int couponType_jin;
    private String couponSn_zuan;
    private int id_zuan;
    private int couponType_zuan;
    private long aLong;
    Handler mHandler = new Handler();
    private long expiredTm_yin;
    private long expiredTm_jin;
    private long expiredTm_zuan;
    private int userId;
    private String urlUtil;
    protected boolean isCreated = false;
    private String cfgURL;
    private boolean mRegistered;
    private static final String TAG = "ListCouponsFragment";
    private TokenDialog browsDialog1;

    @Override
    public int getLayoutId() {
        return R.layout.coupon_list;
    }

    @Override
    public void onViewCreatedInit() {
        Log.d(TAG, "onViewCreatedInit: ");
        if (!mRegistered) {
            EventBus.getDefault().register(this);
            mRegistered = true;
        }
        cfgURL = (String) SPUtil.get(getActivity(), "cfgURL", "");
        urlUtil = (String) SPUtil.get(getActivity(), "URL", "");
        isCreated = true;
        //此行必须有
        // fragStore.setMovementMethod(LinkMovementMethod.getInstance());
        gson = new Gson();
        /**
         * 默认+时间格式2:DEFAULT_STYLE <--> TIME_STYLE_TWO = "HH时mm分ss秒"
         * */
        TextView tv1 = TimerUtils.getTimer(TimerUtils.DEFAULT_STYLE, getActivity(), 7000000, TimerUtils.TIME_STYLE_TWO, 0)
                .getmDateTv();
        parent.addView(tv1);
        setmLayoutParams(tv1);
        Http.OkHttpGet(getActivity(), cfgURL, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    ConfiguraBean configuraBean = gson.fromJson(result, ConfiguraBean.class);
                    configuralist.add(configuraBean);
                } catch (Exception e) {

                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        noNetworkView.findViewById(R.id.tv_retry_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading(getResources().getString(R.string.listview_loading));
                getLOdin();
            }
        });
        loading(getResources().getString(R.string.listview_loading));
//        getLOdin();
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                frag_time_t.stopRun();
                getLOdin();
            }
        });
//        final PtrMDHeader header = new PtrMDHeader(getContext());
//        mPtrFrame.setHeaderView(header);
//        mPtrFrame.addPtrUIHandler(header);
//        mPtrFrame.setPtrHandler(new PtrHandler() {
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, card_scro, header);
//            }
//
//            @Override
//            public void onRefreshBegin(final PtrFrameLayout frame) {
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        frag_time_t.stopRun();
//                        getLOdin();
//                        boolean refreshResult = true;
//                        header.refreshComplete(refreshResult, frame);
//                    }
//                }, 3000);
//            }
//        });
        frag_time_t.setListener(new TimerTextView.onListener() {
            @Override
            public void OnListener(String code) {
                if (code.equals("0")) {

                    fragTime.setVisibility(View.GONE);
                    bronzeUseLy.setClickable(true);
                    silverUseLy.setClickable(true);
                    goldUseLy.setClickable(true);
                    diamondUseLy.setClickable(true);
                    loading(getString(R.string.loading));
                    getLOdin();
                }


            }
        });
        inputInviteCodeEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        inputInviteCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    deleteIv.setVisibility(View.VISIBLE);
                    deleteIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            inputInviteCodeEt.setText("");
                        }
                    });
                    confirmTv.setBackgroundResource(R.drawable.login_deng);
                    confirmTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle b=new Bundle();
                            b.putString("type","CLICK_CODE_ENTER");
                            b.putString("code",inputInviteCodeEt.getText().toString());
                            FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.CLICK_TOKEN_UNUESD_INPUTBOX_GO,b);
                            mHandler.removeCallbacksAndMessages(null);
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    KeyboardUtils.hideSoftKeyboard(inputInviteCodeEt);
                                    checkCode();
                                }
                            },500);

                        }
                    });
                }else{
                    deleteIv.setVisibility(View.GONE);
                    confirmTv.setBackgroundResource(R.drawable.bg_btn_disabled);
                    confirmTv.setOnClickListener(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        card_scro.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                inputInviteCodeEt.setCursorVisible(false);
                KeyboardUtils.hideSoftKeyboard(inputInviteCodeEt);
            }
        });
        inputInviteCodeEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String Ed_txt = inputInviteCodeEt.getText().toString().trim();
                if (Ed_txt.length()==0){

                }else {
                    if (actionId == EditorInfo.IME_ACTION_SEND
                            || actionId == EditorInfo.IME_ACTION_DONE
                            || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                        KeyboardUtils.hideSoftKeyboard(inputInviteCodeEt);
                        checkCode();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    LoginCoupDialog loginCoupDialog;
    private void checkCode(){
        loading(getString(R.string.loading));
        String userId=(String)SPUtil.get(getContext(),"you_usid","");
        FormBody body = new FormBody.Builder()
                .add("code", inputInviteCodeEt.getText().toString().toUpperCase())
                .add("userId",userId)
                .build();
        Http.OkHttpPost(getContext(), urlUtil + UrlUtil.check_code, body,new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                Log.d("check_code_one",result+"urlUtil + UrlUtil.check_code");
                if(isDetached()){
                    return;
                }
                try {
                    JSONObject obj=new JSONObject(result);
                    int code=obj.optInt("code");
                    if(code==0) {
                        redeemInviteCode();
                    }else{
                        dissLoad();
                        switch (code){
                            case ACTIVITY_CODE_NOTFOUND:
                                Util.showErrorMsgDlg(getActivity(),getString(R.string.activity_code_not_exist),mHandler);
//                                ToastUtil.showgravity(getContext(),getString(R.string.activity_code_not_exist));
                                break;
                            case ACTIVITY_CODE_MAX:
                                Util.showErrorMsgDlg(getActivity(),getString(R.string.event_code_no_more_than_5),mHandler);
//                                ToastUtil.showgravity(getContext(),getString(R.string.event_code_no_more_than_5));
                                break;
                            case ACTIVITY_CODE_USED:
                                Util.showErrorMsgDlg(getActivity(),getString(R.string.event_code_have_used),mHandler);
//                                ToastUtil.showgravity(getContext(),getString(R.string.event_code_have_used));
                                break;
                            case INVITER_CODE_SELF:
                                Util.showErrorMsgDlg(getActivity(),getString(R.string.event_code_not_use_yourself),mHandler);
//                                ToastUtil.showgravity(getContext(),getString(R.string.event_code_not_use_yourself));
                                break;
                            case INVITER_CODE_NOTFOUND:
                                Util.showErrorMsgDlg(getActivity(),getString(R.string.event_code_not_exist),mHandler);
//                                ToastUtil.showgravity(getContext(),getString(R.string.event_code_not_exist));
                                break;
                            case INVITER_CODE_WORLD_ERROR:
                                Util.showErrorMsgDlg(getActivity(),getString(R.string.event_code_server_error),mHandler);
//                                ToastUtil.showgravity(getContext(),getString(R.string.event_code_server_error));
                                break;
                            case INVITER_ACTIVITY_CODE_LENGTH_ERROR:
                                Util.showErrorMsgDlg(getActivity(),getString(R.string.event_code_error_length),mHandler);
//                                ToastUtil.showgravity(getContext(),getString(R.string.event_code_error_length));
                                break;
                            case COMMODITY_IDS_WORLD_ERROR:
                                Util.showErrorMsgDlg(getActivity(),getString(R.string.event_code_only_invite_once),mHandler);
//                                ToastUtil.showgravity(getContext(),getString(R.string.event_code_only_invite_once));
                                break;
                            default:
                                Util.showErrorMsgDlg(getActivity(),getString(R.string.verify_code_fail),mHandler);
//                                    ToastUtil.showgravity(getContext(),getString(R.string.verify_code_fali));
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dissLoad();
                    ToastUtil.showgravity(getContext(),getString(R.string.verify_code_fail));
                }
            }

            @Override
            public void onError(Exception e) {
                if(isDetached()){
                    return;
                }
                dissLoad();
                ToastUtil.showgravity(getContext(),getString(R.string.verify_code_fail));
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
                case TYPE_BROZEN:
                    brozenNum++;
                    SPUtil.put(getContext(), "brozen_desc", bean.getDesc());
                    break;
                case Constants.TYPE_SILVER:
                    silverNum++;
                    SPUtil.put(getContext(), "silver_desc", bean.getDesc());
                    break;
                case Constants.TYPE_GOLD:
                    goldNum++;
                    SPUtil.put(getContext(), "gold_desc", bean.getDesc());
                    break;
                case Constants.TYPE_DIAMOND:
                    diamondNum++;
                    SPUtil.put(getContext(), "diamond_desc", bean.getDesc());
                    break;
            }
        }
        SPUtil.put(getContext(), "My_tong", brozenNum);
        SPUtil.put(getContext(), "My_yin", silverNum);
        SPUtil.put(getContext(), "My_jin", goldNum);
        SPUtil.put(getContext(), "My_zuan", diamondNum);
    }
    private void initTokenFromDesc(Object obj){
        if(obj==null){
            return;
        }
        if(obj instanceof ExchangeCodeBean){
            ExchangeCodeBean bean=(ExchangeCodeBean)obj;
            if(bean.getCouponId()==TYPE_BROZEN){
                if(bean.getTokenFrom()==0){
                    bean.setTokenFrom(TOKEN_FROM_DAILY_SIGN);
                }
            }
            bean.setDesc(getDesc(bean.getTokenFrom()));
        }else if(obj instanceof CouponBean.DataBean.CouponsBean){
            CouponBean.DataBean.CouponsBean bean=(CouponBean.DataBean.CouponsBean)obj;
            if(bean.getCouponId()==TYPE_BROZEN){
                if(bean.getTokenFrom()==0){
                    bean.setTokenFrom(TOKEN_FROM_DAILY_SIGN);
                }
            }
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
                tokenFromDesc=getString(R.string.reward_from_reffer);
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
    private void redeemInviteCode(){
        FormBody body = new FormBody.Builder()
                .add("code", inputInviteCodeEt.getText().toString().toUpperCase())
                .build();
        Http.OkHttpPost(getContext(), urlUtil + UrlUtil.TOKEN_REDEEM, body,new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                Log.d("TOKEN_REDEEM",result+urlUtil + UrlUtil.TOKEN_REDEEM);
                if(isDetached()){
                    return;
                }
                dissLoad();
                getLOdin();
                try {
                    JSONObject obj=new JSONObject(result);
                    int code=obj.optInt("code");
                    if(code==0) {
                        JSONArray data = obj.getJSONArray("data");
                        long serverTime=obj.optLong("serverTime");
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
                                animTypeList.add(bean.getCouponId()+Constants.TYPE_COUPON_GAIN_COIN_BASE);
                            }
                            SPUtil.put(getContext(),SP_KEY_KOL_JOIN_FROM,Constants.KOL_JOIN_FROM_INPUT);
                            loginCoupDialog = new LoginCoupDialog(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startCoinAnimList(animTypeList);
                                }
                            },getActivity());
                            loginCoupDialog.show();
                            SPUtil.remove(getContext(), "My_tong");
                            SPUtil.remove(getContext(), "My_yin");
                            SPUtil.remove(getContext(), "My_jin");
                            SPUtil.remove(getContext(), "My_zuan");
                        }else{
                            Util.showErrorMsgDlg(getActivity(),getString(R.string.verify_code_fail),mHandler);
                        }
                    }else{
                        Util.showErrorMsgDlg(getActivity(),getString(R.string.verify_code_fail),mHandler);
//                        switch (code){
//                            case ACTIVITY_CODE_NOTFOUND:
//                                Util.showErrorMsgDlg(getActivity(),getString(R.string.activity_code_not_exist),mHandler);
//                                break;
//                            case ACTIVITY_CODE_MAX:
//                                Util.showErrorMsgDlg(getActivity(),getString(R.string.event_code_no_more_than_5),mHandler);
//                                break;
//                            case ACTIVITY_CODE_USED:
//                                Util.showErrorMsgDlg(getActivity(),getString(R.string.event_code_have_used),mHandler);
//                                break;
//                            case INVITER_CODE_SELF:
//                                Util.showErrorMsgDlg(getActivity(),getString(R.string.event_code_not_use_yourself),mHandler);
//                                break;
//                            case INVITER_CODE_NOTFOUND:
//                                Util.showErrorMsgDlg(getActivity(),getString(R.string.event_code_not_exist),mHandler);
//                                break;
//                            case INVITER_CODE_WORLD_ERROR:
//                                Util.showErrorMsgDlg(getActivity(),getString(R.string.event_code_server_error),mHandler);
//                                break;
//                            case INVITER_ACTIVITY_CODE_LENGTH_ERROR:
//                                Util.showErrorMsgDlg(getActivity(),getString(R.string.event_code_error_length),mHandler);
//                                break;
//                            case COMMODITY_IDS_WORLD_ERROR:
//                                Util.showErrorMsgDlg(getActivity(),getString(R.string.event_code_only_invite_once),mHandler);
//                                break;
//                                default:
//                                    Util.showErrorMsgDlg(getActivity(),getString(R.string.verify_code_fail),mHandler);
//                                    break;
//                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showgravity(getContext(),getString(R.string.verify_code_fail));
                }
            }

            @Override
            public void onError(Exception e) {
                if(isDetached()){
                    return;
                }
                dissLoad();
                ToastUtil.showgravity(getContext(),getString(R.string.verify_code_fail));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mRegistered = false;
        cancelArrowTransAnim();
        AnimationUtils.stopCircleProgressAnim();
        if(frag_time_t!=null){
            frag_time_t.stopRun();
        }
    }

    private void cancelArrowTransAnim() {
        if (arrowGoldIv != null && arrowGoldIv.getAnimation() != null) {
            arrowGoldIv.getAnimation().cancel();
        }
        if (ivArrowSilver != null && ivArrowSilver.getAnimation() != null) {
            ivArrowSilver.getAnimation().cancel();
        }
        if (ivArrowBrozen != null && ivArrowBrozen.getAnimation() != null) {
            ivArrowBrozen.getAnimation().cancel();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }


    @Override
    public void onStartInit() {

    }

    private void setmLayoutParams(TextView tv) {
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv.getLayoutParams();
        params.setMargins(20, 20, 20, 20);
        tv.setLayoutParams(params);
    }



    @OnClick({R.id.et_input_invite_code,R.id.tv_exchange_silver_tip,R.id.tv_exchange_brozen_tip,R.id.tv_exchange_gold_tip,R.id.tv_diamond_use, R.id.tv_gold_use, R.id.tv_silver_use, R.id.tv_brozen_use, R.id.iv_brozen_info, R.id.iv_silver_info, R.id.iv_gold_show_info, R.id.frag_time, R.id.frag_store, R.id.diamond_use_ly, R.id.gold_use_ly, R.id.silver_use_ly, R.id.bronze_use_ly, R.id.iv_diamond_info})

    public void onViewClicked(View view) {
        String token = (String) SPUtil.get(getActivity(), "Token", "");
        ListTokenDialog listTokenDialog = null;
        switch (view.getId()) {

            case R.id.et_input_invite_code:
                Bundle b=new Bundle();
                b.putString("type","FOCUS_CODE");
                FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.CLICK_TOKEN_UNUESD_INPUTBOX,b);
                inputInviteCodeEt.setCursorVisible(true);
                KeyboardUtils.showSoftKeyboard(inputInviteCodeEt);
                break;
            case R.id.tv_exchange_silver_tip:
//                startExchangeAnim(0);
                break;

            case R.id.tv_exchange_brozen_tip:
//                startExchangeAnim(Constants.TYPE_COUPON_EXCHANGE_BROZEN);
                break;
            case R.id.tv_exchange_gold_tip:
//                startExchangeAnim(Constants.TYPE_COUPON_EXCHANGE_GOLDEN);
                break;

            case R.id.frag_time:

                break;
            case R.id.frag_store:
//                StorePopupWindow popup = new StorePopupWindow(getActivity());
//                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        PopupWindowUtil.setWindowTransparentValue(getActivity(), 1.0f);
//                    }
//                });
//                PopupWindowUtil.setWindowTransparentValue(getActivity(), 0.5f);
//                //设置PopupWindow中的位置
//                popup.showAtLocation(getActivity().findViewById(R.id.card_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.tv_diamond_use:  //钻石
                Bundle b1=new Bundle();
                b1.putString("type","SHOW_QRCODE");
                b1.putString("tokenType",Util.getTokenType(TYPE_DIAMOND));
                FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.CLICK_TOKEN_UNUSED_USE,b1);
                SPUtil.put(getActivity(), "couponSn", couponSn_zuan);
                SPUtil.put(getActivity(), "couponId", String.valueOf(id_zuan));
                SPUtil.put(getActivity(), "couponType", String.valueOf(couponType_zuan));
                browsDialog = new OfferDialog(getActivity(), R.style.box_dialog, onClickListener);
                browsDialog.setCanceledOnTouchOutside(false);
                browsDialog.setCancelable(false);
                browsDialog.show();

                break;
            case R.id.tv_gold_use: //金
                Bundle b2=new Bundle();
                b2.putString("type","SHOW_QRCODE");
                b2.putString("tokenType",Util.getTokenType(TYPE_GOLD));
                FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.CLICK_TOKEN_UNUSED_USE,b2);
                SPUtil.put(getActivity(), "couponSn", couponSn_jin);
                SPUtil.put(getActivity(), "couponId", String.valueOf(id_jin));
                SPUtil.put(getActivity(), "couponType", String.valueOf(couponType_jin));
                browsDialog = new OfferDialog(getActivity(), R.style.box_dialog, onClickListener);
                browsDialog.setCurrentExchangeProgress(mGoldExchangeProgress);
                browsDialog.setCanceledOnTouchOutside(false);
                browsDialog.setCancelable(false);
                browsDialog.show();
//                startExchangeAnim(Constants.TYPE_COUPON_EXCHANGE_GOLDEN);
                break;
            case R.id.tv_silver_use:  //银
                Bundle b3=new Bundle();
                b3.putString("type","SHOW_QRCODE");
                b3.putString("tokenType",Util.getTokenType(TYPE_SILVER));
                FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.CLICK_TOKEN_UNUSED_USE,b3);
                SPUtil.put(getActivity(), "couponSn", couponSn_yin);
                SPUtil.put(getActivity(), "couponId", String.valueOf(id_yin));
                SPUtil.put(getActivity(), "couponType", String.valueOf(couponType_yin));
                browsDialog = new OfferDialog(getActivity(), R.style.box_dialog, onClickListener);
                browsDialog.setCurrentExchangeProgress(mSilverExchangeProgress);
                browsDialog.setCanceledOnTouchOutside(false);
                browsDialog.setCancelable(false);
                browsDialog.show();
//                startExchangeAnim(Constants.TYPE_COUPON_EXCHANGE_SILVER);

                break;
            case R.id.tv_brozen_use:  //铜
                Bundle b4=new Bundle();
                b4.putString("type","SHOW_QRCODE");
                b4.putString("tokenType",Util.getTokenType(TYPE_BROZEN));
                FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.CLICK_TOKEN_UNUSED_USE,b4);
                SPUtil.put(getActivity(), "couponSn", couponSn);
                SPUtil.put(getActivity(), "couponId", String.valueOf(couponId));
                SPUtil.put(getActivity(), "couponType", String.valueOf(couponType));
                browsDialog = new OfferDialog(getActivity(), R.style.box_dialog, onClickListener);
                browsDialog.setCurrentExchangeProgress(mBrozenExchangeProgress);
                browsDialog.setCanceledOnTouchOutside(false);
                browsDialog.setCancelable(false);
                browsDialog.show();
//                startExchangeAnim(Constants.TYPE_COUPON_EXCHANGE_BROZEN);
                break;
            case R.id.frag_facce:  //登录
                Intent intent = new Intent(getActivity(), TransActivity.class);
                startActivity(intent);

                break;
            case R.id.iv_diamond_info:
                listTokenDialog = new ListTokenDialog(getActivity(), R.style.box_dialog);
                listTokenDialog.setList(diamons);
                listTokenDialog.show();
                break;
            case R.id.iv_gold_show_info:
                listTokenDialog = new ListTokenDialog(getActivity(), R.style.box_dialog);
                listTokenDialog.setList(golds);
                listTokenDialog.show();
                break;
            case R.id.iv_silver_info:
                listTokenDialog = new ListTokenDialog(getActivity(), R.style.box_dialog);
                listTokenDialog.setList(silvers);
                listTokenDialog.show();
                break;
            case R.id.iv_brozen_info:
                listTokenDialog = new ListTokenDialog(getActivity(), R.style.box_dialog);
                listTokenDialog.setList(brozens);
                listTokenDialog.show();
                break;
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.offer_img:  //确认

                    browsDialog.dismiss();

                    break;

            }
        }
    };

    // 普通事件的处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventBusCarrier carrier) {
        String eventType=carrier.getEventType();
        if(EVENT_TYPE_TOKEN_ANIM.equals(eventType)){
            TokenExchangeBean bean = (TokenExchangeBean) carrier.getObject();
            Log.d(TAG, "handleEvent: EVENT_TYPE_TOKEN_ANIM ");
            if(bean!=null) {
                boolean half = bean.getFlagToken() != 1;
                Log.d(TAG, "handleEvent: half " + half);
                startExchangeAnim(bean.getFlagCoupn(), half);
                loading(getString(R.string.loading));
                getLOdin();
            }
        }else if(EVENT_TYPE_GAIN_TOKEN.equals(eventType)){
            GainTokenBean bean = (GainTokenBean) carrier.getObject();
            Log.d(TAG, "handleEvent: EVENT_TYPE_GAIN_TOKEN ");
            if(bean!=null) {
                List<Integer> animTypeList = bean.getAnimTypeList();
                startCoinAnimList(animTypeList);
                loading(getString(R.string.loading));
                getLOdin();
            }
        }

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
                        startCoinAnim(animType);
                    }
                }, delay);
            }catch (Exception e){
                e.printStackTrace();
            }
            delay+=3000;
        }
    }
    private void startCoinAnim(int animType) {
        switch (animType) {
            case Constants.TYPE_COUPON_GAIN_BROZEN:
                ivMetalCoin.setImageResource(R.drawable.token_icon_bronze);
                AnimationUtils.startMetalCoinAnim(ivBronzeCoin, ivMetalCoin, lightEffectView, flBgView);
                break;
            case Constants.TYPE_COUPON_GAIN_SILVER:
                ivMetalCoin.setImageResource(R.drawable.token_icon_silver);
                AnimationUtils.startMetalCoinAnim(ivSilverCoin, ivMetalCoin, lightEffectView, flBgView);
                break;
            case Constants.TYPE_COUPON_GAIN_GOLD:
                ivMetalCoin.setImageResource(R.drawable.token_icon_gold);
                AnimationUtils.startMetalCoinAnim(ivGoldCoin, ivMetalCoin, lightEffectView, flBgView);
                break;
            case Constants.TYPE_COUPON_GAIN_DIAMOND:
                ivMetalCoin.setImageResource(R.drawable.token_icon_diamond);
                AnimationUtils.startMetalCoinAnim(ivDiamondCoin, ivMetalCoin, lightEffectView, flBgView);
                break;
        }
    }
    private void startExchangeAnim(int type,boolean half) {
        switch (type) {
            case Constants.TYPE_COUPON_EXCHANGE_BROZEN:
                ivMetalCoin.setImageResource(R.drawable.token_icon_silver);
                AnimationUtils.transArrowAnim(ivArrowBrozen, ivSilverCoin,ivMetalCoin,lightEffectView,flBgView, pbSilver, tvBrozenProgress, half);
                break;
            case Constants.TYPE_COUPON_EXCHANGE_SILVER:
                ivMetalCoin.setImageResource(R.drawable.token_icon_gold);
                AnimationUtils.transArrowAnim(ivArrowSilver, ivGoldCoin,ivMetalCoin,lightEffectView, flBgView,pbGold, tvSilverProgress, half);
                break;
            case Constants.TYPE_COUPON_EXCHANGE_GOLDEN:
                ivMetalCoin.setImageResource(R.drawable.token_icon_diamond);
                AnimationUtils.transArrowAnim(arrowGoldIv, ivDiamondCoin, ivMetalCoin,lightEffectView,flBgView,pbDiamond, tvGoldProgres, half);
                break;
        }
    }

    private float mBrozenExchangeProgress;
    private float mSilverExchangeProgress;
    private float mGoldExchangeProgress;
    List<CouponBean.DataBean.CouponsBean> brozens = new ArrayList<>();
    List<CouponBean.DataBean.CouponsBean> silvers = new ArrayList<>();
    List<CouponBean.DataBean.CouponsBean> golds = new ArrayList<>();
    List<CouponBean.DataBean.CouponsBean> diamons = new ArrayList<>();

    public void getLOdin() {


        String token = (String) SPUtil.get(getActivity(), "Token", "");
        if (token.length()==0){
            dissLoad();
            return;
        }

        Http.OkHttpGet(getActivity(), urlUtil + UrlUtil.coupon, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                if(isDetached()){
                    return;
                }
                dissLoad();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                Log.d(TAG, "getLOdin: "+result);
                noNetworkView.setVisibility(View.GONE);

                mRefreshLayout.setVisibility(View.VISIBLE);
                tvUseDiamondTip.setText(getResources().getString(R.string.use_diamond_tip));
                SpannableString clickString = new SpannableString("  "+getResources().getString(R.string.use_diamond_tip_dou)+"  ");
                tvUseDiamondTip.setMovementMethod(LinkMovementMethod.getInstance());
                clickString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        StorePopupWindow popup = new StorePopupWindow(getActivity());
                        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                PopupWindowUtil.setWindowTransparentValue(getActivity(), 1.0f);
                            }
                        });
                        PopupWindowUtil.setWindowTransparentValue(getActivity(), 0.5f);
                        //设置PopupWindow中的位置
                        popup.showAtLocation(contentLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(getResources().getColor(R.color.color_five));//设置颜色
                        ds.setUnderlineText(false);
                    }
                }, 0, clickString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvUseDiamondTip.append(clickString);

                tvExchangeGoldTip.setText(Html.fromHtml(getResources().getString(R.string.exchange_bsg)+"<font color='#FC5D34'> "+getResources().getString(R.string.token_number)+" </font>"+getResources().getString(R.string.exchange_gold)+" <font color='#FC5D34'> "+getResources().getString(R.string.token_num)+" </font>"+getResources().getString(R.string.exchange_gold_dou)));
                tvExchangeSilverTip.setText(Html.fromHtml(getResources().getString(R.string.exchange_bsg)+"<font color='#FC5D34'> "+getResources().getString(R.string.token_number)+" </font>"+getResources().getString(R.string.exchange_brozen)+" <font color='#FC5D34'> "+getResources().getString(R.string.token_num)+" </font>"+getResources().getString(R.string.exchange_brozen_dou)));
                tvExchangeBrozenTip.setText(Html.fromHtml(getResources().getString(R.string.exchange_bsg)+"<font color='#FC5D34'> "+getResources().getString(R.string.token_number)+" </font>"+getResources().getString(R.string.exchange_silver)+" <font color='#FC5D34'> "+getResources().getString(R.string.token_num)+" </font>"+getResources().getString(R.string.exchange_silver_dou)));

                try {

                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    long serverTime = jsonObject.getLong("serverTime");

                    if (code.equals("0")) {
                        if (couponlist.size() > 0) {
                            couponlist.clear();
                        }
                        CouponBean couponBean = gson.fromJson(result, CouponBean.class);
                        couponlist.add(couponBean);
                        List<CouponBean.DataBean.CouponsBean> coupons = couponlist.get(0).getData().getCoupons();
                        List<CouponBean.DataBean.CouponPiecesBean> couponPieces = couponlist.get(0).getData().getCouponPieces();
                        List<CycleRecord> cycleRecords = couponBean.getData().getCycleRecords();
                        if (cycleRecords != null && cycleRecords.size() > 0) {
                            for (CycleRecord record : cycleRecords) {
                                switch (record.getType()) {
                                    case Constants.TYPE_COUPON_EXCHANGE_BROZEN:
                                        tvBrozenProgress.setText((int)(record.getProgress() * 2) + "");
                                        if (record.getProgress() == 0.5) {
                                            ivArrowBrozen.setVisibility(View.VISIBLE);
                                        } else {
                                            ivArrowBrozen.setVisibility(View.GONE);
                                        }
                                        mBrozenExchangeProgress = record.getProgress();
                                        break;
                                    case Constants.TYPE_COUPON_EXCHANGE_SILVER:
                                        tvSilverProgress.setText((int)(record.getProgress() * 2) + "");
                                        if (record.getProgress() == 0.5) {
                                            ivArrowSilver.setVisibility(View.VISIBLE);
                                        } else {
                                            ivArrowSilver.setVisibility(View.GONE);
                                        }
                                        mSilverExchangeProgress = record.getProgress();
                                        break;
                                    case Constants.TYPE_COUPON_EXCHANGE_GOLDEN:
                                        tvGoldProgres.setText((int)(record.getProgress() * 2) + "");
                                        if (record.getProgress() == 0.5) {
                                            arrowGoldIv.setVisibility(View.VISIBLE);
                                        } else {
                                            arrowGoldIv.setVisibility(View.GONE);
                                        }
                                        mGoldExchangeProgress = record.getProgress();
                                        break;
                                }

                            }
                        }

                        brozens.clear();
                        silvers.clear();
                        golds.clear();
                        diamons.clear();

                        int e = 0, b = 0, c = 0, d = 0;
                        for (int i = 0; i < coupons.size(); i++) {
                            CouponBean.DataBean.CouponsBean bean=coupons.get(i);
                            initTokenFromDesc(bean);
                            if (coupons.get(i).getCouponId() == 1) {    //铜

                                if (e == 0) {
                                    couponSn = couponlist.get(0).getData().getCoupons().get(i).getCouponSn();
                                    couponId = couponlist.get(0).getData().getCoupons().get(i).getId();
                                    couponType = couponlist.get(0).getData().getCoupons().get(i).getCouponId();
                                    e = 1;
                                }
                                brozens.add(bean);

                            } else if (coupons.get(i).getCouponId() == 2) {  //银

                                long expiredTm1 = coupons.get(i).getExpiredTm();
                                boolean nowTime = TimeUtil.compareNowTime(String.valueOf(expiredTm1), String.valueOf(serverTime));
                                if (nowTime) {
                                    if (b == 0) {
                                        couponSn_yin = couponlist.get(0).getData().getCoupons().get(i).getCouponSn();
                                        id_yin = couponlist.get(0).getData().getCoupons().get(i).getId();
                                        couponType_yin = couponlist.get(0).getData().getCoupons().get(i).getCouponId();
                                        expiredTm_yin = couponlist.get(0).getData().getCoupons().get(i).getExpiredTm();
                                        b=1;
                                    }
                                    silvers.add(bean);
                                }

                            } else if (coupons.get(i).getCouponId() == 3) {  //金

                                long expiredTm1 = coupons.get(i).getExpiredTm();
                                boolean nowTime = TimeUtil.compareNowTime(String.valueOf(expiredTm1), String.valueOf(serverTime));
                                if (nowTime) {
                                    if (c == 0) {
                                        couponSn_jin = couponlist.get(0).getData().getCoupons().get(i).getCouponSn();
                                        id_jin = couponlist.get(0).getData().getCoupons().get(i).getId();
                                        couponType_jin = couponlist.get(0).getData().getCoupons().get(i).getCouponId();
                                        expiredTm_jin = couponlist.get(0).getData().getCoupons().get(i).getExpiredTm();
                                        c = 1;
                                    }
                                    golds.add(bean);
                                }

                            } else if (coupons.get(i).getCouponId() == 4) {  //钻
                                long expiredTm1 = coupons.get(i).getExpiredTm();
                                boolean nowTime = TimeUtil.compareNowTime(String.valueOf(expiredTm1), String.valueOf(serverTime));
                                if (nowTime) {
                                    if (d == 0) {
                                        couponSn_zuan = couponlist.get(0).getData().getCoupons().get(i).getCouponSn();
                                        id_zuan = couponlist.get(0).getData().getCoupons().get(i).getId();
                                        couponType_zuan = couponlist.get(0).getData().getCoupons().get(i).getCouponId();
                                        expiredTm_zuan = couponlist.get(0).getData().getCoupons().get(i).getExpiredTm();
                                        d = 1;
                                    }
                                    diamons.add(bean);
                                }

                            } else {

                            }

                        }
                        SPUtil.put(getActivity(),"brozens_num",brozens.size());
                        tvBrozenNum.setText("⨯ " + brozens.size());
                        tvSilverNum.setText("⨯ " + silvers.size());
                        tvGoldNum.setText("⨯ " + golds.size());
                        tvDiamondNum.setText("⨯ " + diamons.size());
                        if (brozens.size() == 0) {
                            tvBrozenNum.setTextColor(getResources().getColor(R.color.main_tab_defaule));
                            tvBrozenExpireTime.setVisibility(View.GONE);
                            ivBrozenInfo.setVisibility(View.GONE);
                            tvBrozenUse.setClickable(false);
                            tvBrozenUse.setBackgroundResource(R.drawable.bg_btn_disabled);
                        } else if (brozens.size() >= 5) {
                            tvBrozenNum.setTextColor(getResources().getColor(R.color.nh_percent));
                            tvBrozenExpireTime.setVisibility(View.VISIBLE);
                            ivBrozenInfo.setVisibility(View.VISIBLE);
                            tvBrozenUse.setClickable(true);
                            tvBrozenUse.setBackgroundResource(R.drawable.login_deng);
                        } else {
                            tvBrozenNum.setTextColor(getResources().getColor(R.color.text_color_black_v1));
                            tvBrozenExpireTime.setVisibility(View.GONE);
                            ivBrozenInfo.setVisibility(View.VISIBLE);
                            tvBrozenUse.setClickable(true);
                            tvBrozenUse.setBackgroundResource(R.drawable.login_deng);
                        }
                        if (silvers.size() == 0) {
                            tvSilverNum.setTextColor(getResources().getColor(R.color.main_tab_defaule));
                            ivSilverInfo.setVisibility(View.GONE);
                            tvSilverUse.setClickable(false);
                            tvSilverUse.setBackgroundResource(R.drawable.bg_btn_disabled);
                        }else {
                            tvSilverNum.setTextColor(getResources().getColor(R.color.text_color_black_v1));
                            ivSilverInfo.setVisibility(View.VISIBLE);
                            tvSilverUse.setClickable(true);
                            tvSilverUse.setBackgroundResource(R.drawable.login_deng);
                        }
                        if (golds.size() == 0) {
                            tvGoldNum.setTextColor(getResources().getColor(R.color.main_tab_defaule));
                            ivGoldShowInfo.setVisibility(View.GONE);
                            goldUseTv.setClickable(false);
                            goldUseTv.setBackgroundResource(R.drawable.bg_btn_disabled);
                        } else {
                            tvGoldNum.setTextColor(getResources().getColor(R.color.text_color_black_v1));
                            ivGoldShowInfo.setVisibility(View.VISIBLE);
                            goldUseTv.setClickable(true);
                            goldUseTv.setBackgroundResource(R.drawable.login_deng);
                        }
                        if (diamons.size() == 0) {
                            tvDiamondNum.setTextColor(getResources().getColor(R.color.main_tab_defaule));
                            ivDiamondInfo.setVisibility(View.GONE);
                            tvDiamondUse.setClickable(false);
                            tvDiamondUse.setBackgroundResource(R.drawable.bg_btn_disabled);
                        }else {
                            tvDiamondNum.setTextColor(getResources().getColor(R.color.text_color_black_v1));
                            ivDiamondInfo.setVisibility(View.VISIBLE);
                            tvDiamondUse.setClickable(true);
                            tvDiamondUse.setBackgroundResource(R.drawable.login_deng);
                        }
                        if (silvers.size() > 0) {
                            long silverExpiredTm = silvers.get(0).getExpiredTm();
                            if (silverExpiredTm != 0) {
                                String timeFormatZx = TimeUtil.timedateone(silverExpiredTm);
                                tvSilverExpireTime.setVisibility(View.VISIBLE);
                                tvSilverExpireTime.setText(getResources().getString(R.string.expired_ri) + ":" + timeFormatZx);
                            } else {
                                tvSilverExpireTime.setVisibility(View.GONE);
                            }


                        }else{
                            tvSilverExpireTime.setVisibility(View.GONE);
                        }
                        if (golds.size() > 0) {
                            long goldExpiredTm = golds.get(0).getExpiredTm();
                            if (goldExpiredTm != 0) {
                                String timeFormatZx = TimeUtil.timedateone(goldExpiredTm);
                                tvGoldExpireTime.setVisibility(View.VISIBLE);
                                tvGoldExpireTime.setText(getResources().getString(R.string.expired_ri) + ":" + timeFormatZx);
                            } else {
                                tvGoldExpireTime.setVisibility(View.GONE);
                            }

                        }else{
                            tvGoldExpireTime.setVisibility(View.GONE);
                        }
                        if (diamons.size() > 0) {
                            long diamondExpiredTm = diamons.get(0).getExpiredTm();
                            if (diamondExpiredTm != 0) {
                                String timeFormatZx = TimeUtil.timedateone(diamondExpiredTm);
                                tvDiamonExpireTime.setVisibility(View.VISIBLE);
                                tvDiamonExpireTime.setText(getResources().getString(R.string.expired_ri) + ":" + timeFormatZx);
                            } else {
                                tvDiamonExpireTime.setVisibility(View.GONE);
                            }

                        }else{
                            tvDiamonExpireTime.setVisibility(View.GONE);
                        }
                        try {
                            String downco = (String) SPUtil.get(getActivity(), "downco", "0");
                            aLong = Long.parseLong(downco);
                        } catch (Exception exception) {

                        }

                        long l2 = aLong - serverTime;
                        if (l2 > 0) {
                            if (fragTime != null) {
                                fragTime.setVisibility(View.VISIBLE);
                            }
                            String format = DateUtil.ms2HMS((int) l2);
                            String[] split = format.split(":");
                            String s = split[0];
                            String s1 = split[1];
                            String s2 = split[2];
                            long[] times = {0, Long.parseLong(s), Long.parseLong(s1), Long.parseLong(s2)};
                            Log.d(TAG, "OnSuccess: "+frag_time_t.isRun());
                            if(!frag_time_t.isRun()) {
                                frag_time_t.setTimes(l2 / 1000);
                                frag_time_t.beginRun();
                            }
                            tvBrozenUse.setClickable(false);
                            tvSilverUse.setClickable(false);
                            goldUseTv.setClickable(false);
                            tvDiamondUse.setClickable(false);
                            tvBrozenUse.setBackgroundResource(R.drawable.bg_btn_disabled);
                            tvSilverUse.setBackgroundResource(R.drawable.bg_btn_disabled);
                            goldUseTv.setBackgroundResource(R.drawable.bg_btn_disabled);
                            tvDiamondUse.setBackgroundResource(R.drawable.bg_btn_disabled);
                            Log.d("wuuwuwu", format);
                        } else {
                            fragTime.setVisibility(View.GONE);
                            if(brozens.size()>0) {
                                tvBrozenUse.setClickable(true);
                                tvBrozenUse.setBackgroundResource(R.drawable.login_deng);
                            }
                            if(silvers.size()>0) {
                                tvSilverUse.setClickable(true);
                                tvSilverUse.setBackgroundResource(R.drawable.login_deng);
                            }
                            if(golds.size()>0) {
                                goldUseTv.setClickable(true);
                                goldUseTv.setBackgroundResource(R.drawable.login_deng);
                            }
                            if(diamons.size()>0) {
                                tvDiamondUse.setClickable(true);
                                tvDiamondUse.setBackgroundResource(R.drawable.login_deng);
                            }

                        }
//                        e = 0;
////                        b = 0;
////                        c = 0;
////                        d = 0;
                        //印花
//                        for (int a = 0; a < couponPieces.size(); a++) {
//                            int amount = couponPieces.get(a).getAmount();
//                            if (couponPieces.get(a).getCouponId() == 1) {
//
//                                Log.d("hahqqq1", amount + "");
//                                if (amount == 1) {
//                                    bronzeImg.setBackgroundResource(R.drawable.coupons_progressbar_1);
//
//                                } else if (amount == 2) {
//                                    bronzeImg.setBackgroundResource(R.drawable.coupons_progressbar_2);
//
//
//                                } else if (amount == 3) {
//                                    bronzeImg.setBackgroundResource(R.drawable.coupons_progressbar_3);
//
//                                } else if (amount == 4) {
//                                    bronzeImg.setBackgroundResource(R.drawable.coupons_progressbar_4);
//
//                                } else if (amount == 5) {
//
//                                } else {
//                                    bronzeImg.setBackgroundResource(R.drawable.coupons_progressbar_0);
//                                }
//
//
//                            } else if (couponPieces.get(a).getCouponId() == 2) {
//                                silverNumber.setText("x" + amount);
//                                Log.d("hahqqq2", amount + "");
//                                if (amount == 1) {
//                                    silverImg.setBackgroundResource(R.drawable.coupons_progressbar_1);
//
//                                } else if (amount == 2) {
//                                    silverImg.setBackgroundResource(R.drawable.coupons_progressbar_2);
//
//                                } else if (amount == 3) {
//                                    silverImg.setBackgroundResource(R.drawable.coupons_progressbar_3);
//
//                                } else if (amount == 4) {
//                                    silverImg.setBackgroundResource(R.drawable.coupons_progressbar_4);
//
//                                } else if (amount == 5) {
//
//                                } else {
//                                    silverImg.setBackgroundResource(R.drawable.coupons_progressbar_0);
//
//                                }
//
//                            } else if (couponPieces.get(a).getCouponId() == 3) {
//                                goldNumber.setText("x" + amount);
//                                Log.d("hahqqq3", amount + "");
//                                if (amount == 1) {
//                                    goldImg.setBackgroundResource(R.drawable.coupons_progressbar_1);
//
//
//                                } else if (amount == 2) {
//                                    goldImg.setBackgroundResource(R.drawable.coupons_progressbar_2);
//
//
//                                } else if (amount == 3) {
//                                    goldImg.setBackgroundResource(R.drawable.coupons_progressbar_3);
//
//
//                                } else if (amount == 4) {
//                                    goldImg.setBackgroundResource(R.drawable.coupons_progressbar_4);
//
//
//                                } else if (amount == 5) {
//
//                                } else {
//                                    goldImg.setBackgroundResource(R.drawable.coupons_progressbar_0);
//
//
//                                }
//
//
//                            } else if (couponPieces.get(a).getCouponId() == 4) {
//                                diamondNumber.setText("x" + amount);
//                                Log.d("hahqqq4", amount + "");
//                                if (amount == 1) {
//                                    diamondImg.setBackgroundResource(R.drawable.coupons_progressbar_1);
//
//                                } else if (amount == 2) {
//                                    diamondImg.setBackgroundResource(R.drawable.coupons_progressbar_2);
//
//
//                                } else if (amount == 3) {
//                                    diamondImg.setBackgroundResource(R.drawable.coupons_progressbar_3);
//
//
//                                } else if (amount == 4) {
//                                    diamondImg.setBackgroundResource(R.drawable.coupons_progressbar_4);
//
//                                } else if (amount == 5) {
//
//                                } else {
//                                    diamondImg.setBackgroundResource(R.drawable.coupons_progressbar_0);
//
//                                }
//                            }
//                        }
//                        Log.d("优惠卷数量", card_one + "==" + card_twe + "==" + card_three + "==" + card_four);
//                        bronzeNum.setText("x" + card_one);
//
//                        diamond_ly.setBackgroundResource(R.drawable.coupons_icon_diamond);
//                        gold_ly.setBackgroundResource(R.drawable.coupons_icon_gold);
//                        silver_ly.setBackgroundResource(R.drawable.coupons_icon_silver);
//                        silverImg.setVisibility(View.VISIBLE);
//                        goldImg.setVisibility(View.VISIBLE);
//                        diamondImg.setVisibility(View.VISIBLE);
//                        silver_txet.setText(getResources().getString(R.string.card_use));
//                        gold_text.setText(getResources().getString(R.string.card_use));
//                        diamond_text.setText(getResources().getString(R.string.card_use));
//                        silverNumber.setText("x" + card_twe);
//                        goldNumber.setText("x" + card_three);
//                        diamondNumber.setText("x" + card_four);
//                        if (card_one == 0) { //铜
//
//                            bronzeUseLy.setBackgroundResource(R.drawable.coupons_btn_unavailable);
//                            bronzeUseLy.setClickable(false);
//
//                        } else {
//                            if (l2 < 0) {
//                                bronzeUseLy.setBackgroundResource(R.drawable.coupons_btn);
//                            }
//                        }
//                        if (card_twe == 0) {
//                            silverExpiry.setText("");
//                            silverUseLy.setBackgroundResource(R.drawable.coupons_btn_unavailable);
//                            silverUseLy.setClickable(false);
//                        } else {
//                            if (l2 < 0) {
//                                silverUseLy.setBackgroundResource(R.drawable.coupons_btn);
//                            }
//                        }
//                        if (card_three == 0) {
//                            goldExpiry.setText("");
//                            goldUseLy.setBackgroundResource(R.drawable.coupons_btn_unavailable);
//                            goldUseLy.setClickable(false);
//                        } else {
//                            if (l2 < 0) {
//                                goldUseLy.setBackgroundResource(R.drawable.coupons_btn);
//                            }
//                        }
//                        if (card_four == 0) {
//                            diamondExpiry.setText("");
//                            diamondUseLy.setBackgroundResource(R.drawable.coupons_btn_unavailable);
//                            diamondUseLy.setClickable(false);
//                        } else {
//                            if (l2 < 0) {
//                                diamondUseLy.setBackgroundResource(R.drawable.coupons_btn);
//                            }
//                        }
                        //   }


                    } else if (code.equals("5007")) {
                        SPUtil.remove(getActivity(), "Token");
                        SPUtil.remove(getActivity(), "photo1");
                        SPUtil.remove(getActivity(), "photo");
                        SPUtil.remove(getActivity(), "gen");
                        SPUtil.remove(getActivity(), "name_one");
                        SPUtil.put(getActivity(), "deng", false);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        //失败
                        //Toast.makeText(getActivity(), getResources().getString(R.string.fan), Toast.LENGTH_SHORT).show();
                        ToastUtil.showgravity(getActivity(), getResources().getString(R.string.fan));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Exception e) {
                if(isDetached()){
                    return;
                }
                dissLoad();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                noNetworkView.setVisibility(View.VISIBLE);
                mRefreshLayout.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void dispatchTouchEvent(MotionEvent ev) {

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isCreated) {
            return;
        }
        if (isVisibleToUser) {
            urlUtil = (String) SPUtil.get(getActivity(), "URL", "");
            checkTokenToLoadData();
        }
    }

    private void checkTokenToLoadData(){
        try {
            String token = (String) SPUtil.get(getActivity(), "Token", "");
            Log.d(TAG, "onResume token:" + token);
            if (token.length() != 0) {
                getLOdin();
                boolean showUpgradeToToken = (boolean) SPUtil.get(getActivity(), "showUpgradeToToken", false);
                if (showUpgradeToToken){

                        browsDialog1 = new TokenDialog(getActivity(), R.style.box_dialog);
                        browsDialog1.setCanceledOnTouchOutside(false);
                        browsDialog1.show();
                        browsDialog1.setCancelable(false);
                    SPUtil.remove(getActivity(),"showUpgradeToToken");

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
            }else{
                dissLoad();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        urlUtil = (String) SPUtil.get(getActivity(), "URL", "");
        checkTokenToLoadData();
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);

    }


    @Override
    public void onStop() {
        super.onStop();
        // frag_time_t.stopRun();

    }

    @Override
    public void onStart() {
        super.onStart();

    }

}
