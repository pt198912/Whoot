package com.app.whoot.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.app.whoot.R;
import com.app.whoot.adapter.MyFragmentPager;
import com.app.whoot.adapter.MyReAdapter;
import com.app.whoot.adapter.ShopsLVAdapter;
import com.app.whoot.adapter.StoreImageAdapter;
import com.app.whoot.app.Constant;
import com.app.whoot.app.MyApplication;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.CardBean;
import com.app.whoot.bean.CardItemTopBean;
import com.app.whoot.bean.CouponBean;
import com.app.whoot.bean.CycleRecord;
import com.app.whoot.bean.DishReviewBean;
import com.app.whoot.bean.PriceScopeBean;
import com.app.whoot.bean.RewardItemBean;
import com.app.whoot.bean.ShopTagsBean;
import com.app.whoot.bean.StoreTagBean;
import com.app.whoot.bean.TokenExchangeBean;
import com.app.whoot.bean.WorkingTmListBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.ui.fragment.DishReviewFragment;
import com.app.whoot.ui.view.CustomRefreshHeader;
import com.app.whoot.ui.view.IndicatorDrawable;
import com.app.whoot.ui.view.custom.MyListView;
import com.app.whoot.ui.view.dialog.CustomDialog;
import com.app.whoot.ui.view.dialog.GiftReOneDialog;
import com.app.whoot.ui.view.dialog.GiftReTweDialog;
import com.app.whoot.ui.view.dialog.GiftRewardDialog;
import com.app.whoot.ui.view.dialog.GiftThreeReDialog;
import com.app.whoot.ui.view.dialog.LoginCoupDialog;
import com.app.whoot.ui.view.popup.StorePopupWindow;
import com.app.whoot.util.AppUtil;
import com.app.whoot.util.Constants;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.FireBaseEventKey;
import com.app.whoot.util.FirebaseReportUtils;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.PopupWindowUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ScreenUtils;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.dl7.tag.TagLayout;
import com.dl7.tag.TagView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;


import com.jchou.imagereview.ui.ImagePagerActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import okhttp3.FormBody;

import static com.app.whoot.ui.fragment.FindFragment.AMAP_PACKAGENAME;
import static com.app.whoot.ui.fragment.FindFragment.BAIDUMAP_PACKAGENAME;
import static com.app.whoot.ui.fragment.FindFragment.GOOGLE_PACKAGENAME;
import static com.app.whoot.util.Constants.EVENT_TYPE_TOKEN_EXCHANGE;

/**
 * 门店详情
 * */

public class StoreDetailActivity extends BaseActivity implements View.OnClickListener,AppBarLayout.OnOffsetChangedListener{
    @BindView(R.id.title_carditem_fl)
    LinearLayout titleCarditemFl;
    @BindView(R.id.title_carditem_title)
    TextView titleCarditemTitle;
    @BindView(R.id.title_carditem_shou)
    ImageView titleCarditemShou;
    @BindView(R.id.title_carditem_no)
    ImageView titleCarditemNo;
    @BindView(R.id.title_carditem_img)
    ImageView titleCarditemImg;

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_name_en)
    TextView tvNameEn;
    @BindView(R.id.divider_show_detail_info)
    View showDetailInfoDivider;
    @BindView(R.id.car_item_score)
    TextView carItemScore;
    @BindView(R.id.card_item_img0)
    ImageView cardItemImg0;
    @BindView(R.id.card_item_img1)
    ImageView cardItemImg1;
    @BindView(R.id.card_item_img2)
    ImageView cardItemImg2;
    @BindView(R.id.card_item_img3)
    ImageView cardItemImg3;
    @BindView(R.id.card_item_img4)
    ImageView cardItemImg4;
    @BindView(R.id.car_item_score_one)
    TextView carItemScoreOne;
    @BindView(R.id.tv_dish_style)
    TextView tvDishStyle;
    @BindView(R.id.tv_open_status)
    TextView tvOpenStatus;
    @BindView(R.id.tv_price_level)
    TextView tvPriceLevel;
    @BindView(R.id.tv_average_consumption)
    TextView tvAverageConsumption;
    @BindView(R.id.tv_store_address)
    TextView tvStoreAddress;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.tv_store_distance)
    TextView tvStoreDistance;
    @BindView(R.id.tv_store_phone)
    TextView tvStorePhone;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.layout_detail_info)
    View detailInfoLayout;
    @BindView(R.id.ll_detail_info)
    LinearLayout llDetailInfo;
    @BindView(R.id.iv_expand_collapse)
    ImageView ivExpandCollapse;
    @BindView(R.id.collapsingtoolbar)
    CollapsingToolbarLayout collapsingtoolbar;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    @BindView(R.id.login_e_button)
    LoginButton loginButton;
    @BindView(R.id.no_network_view)
    View noNetworkView;
    @BindView(R.id.tag_container)
    TagLayout tagContainer;
    @BindView(R.id.card_qi)
    TextView card_qi;
    @BindView(R.id.card_time)
    TextView card_time;
    @BindView(R.id.time_divider)
    TextView card_time_l;
    @BindView(R.id.card_time_cl)
    TextView card_time_cl;
    @BindView(R.id.crd_ly)
    LinearLayout crdLy;
    @BindView(R.id.car_item_list)
    MyListView car_item_list;
    @BindView(R.id.rv_store_images)
    RecyclerView rvStoreImages;
    @BindView(R.id.tv_store_desc)
    TextView tvStoreDesc;
    @BindView(R.id.tv_retry_now)
    TextView tvRetryNow;


    @BindView(R.id.ll_tag_label)
    LinearLayout llTagLabel;
    @BindView(R.id.ll_open_time_label)
    LinearLayout llOpenTimeLabel;
    @BindView(R.id.ll_store_desc_label)
    LinearLayout llStoreDescLabel;
    @BindView(R.id.ll_store_detail_header)
    LinearLayout llStoreDetailHeader;
    @BindView(R.id.tv_store_close_status)
    TextView storeCloseStatusTv;
    @BindView(R.id.swipe_container)
    SmartRefreshLayout refreshLayout;
    private List<String> titles = new ArrayList<String>();
    private CollapsingToolbarLayout mCollapsingtoolbar;
    private AppBarLayout mAppBarLayout;
    private DishReviewFragment mDishReviewFrag;
    private int mCurrentTabIndex;
    private int id;
    private String locationId1 = "undefined";
    private String couponId1 = "undefined";
    private String shopId1 = "undefined";
    private String url1 = "";
    private String urlUtil;
    public static StoreDetailActivity instance;
    private String cfgURL;
    private String url;
    private long timeLag;
    private int favour;
    private String distances;
    CallbackManager callbackManager;
    private String distance;
    private String shopGiftCfgId1;
    private String token;
    private GiftRewardDialog browsDialog;
    private boolean isGaoDe, isBAidu, isGOOGLE;
    private List<RewardItemBean> Toplist = new ArrayList<RewardItemBean>();
    private List<CardItemTopBean> readlist = new ArrayList<CardItemTopBean>();
    private List<WorkingTmListBean> Tmlist = new ArrayList<WorkingTmListBean>(); //营业时间
    private List<ShopTagsBean> sags = new ArrayList<ShopTagsBean>();
    private long closeTm;
    private long openingTm;
    private long closeTmSecond;
    private long openingTmSecond;
    private GiftReOneDialog OneDialog;   //过期
    private GiftReTweDialog TweDialog;   //已经领取
    private GiftThreeReDialog ThreeDialog;  //未领取
    ArrayList<String> images = new ArrayList<>();
    private String giftSn;
    private String shopid;
    private String userId;
    private String giftUseId;
    private String type;
    private String giftId;
    private String expiredTm;
    private String tags;
    private DishReviewBean mData = new DishReviewBean();
    private List<StoreTagBean> mStoreTags = new ArrayList<>();
    private String describe;
    private String nameCh;
    private static final String TAG = "StoreDetailActivity";
    private LoginCoupDialog loginCoupDialog;
    private Bundle bundle;
    private boolean flag_sto;

    @Override
    public int getLayoutId() {
        return R.layout.activity_store_detail;
    }

    @Override
    public void onCreateInit() {
        instance = this;
        String versionName = AppUtil.getVersionName(this);
        noNetworkView.findViewById(R.id.tv_retry_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading(getResources().getString(R.string.listview_loading));
                initData();
            }
        });

        Http.OkHttpGet(this, UrlUtil.Release, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray versions = data.getJSONArray("Versions");
                    String newestVersion = data.getString("NewestVersion");
                    boolean upgradeNew = data.getBoolean("UpgradeNew");
                    SPUtil.put(StoreDetailActivity.this, "newestVersion", newestVersion);
                    SPUtil.put(StoreDetailActivity.this, "upgradeNew", upgradeNew);
                    for (int i = 0; i < versions.length(); i++) {
                        JSONObject jsonObject1 = versions.getJSONObject(i);
                        String version = jsonObject1.getString("Version");
                        if (versionName.equals(version)) {
                            url = jsonObject1.getString("URL");
                            cfgURL = jsonObject1.getString("CfgURL");
                            SPUtil.put(StoreDetailActivity.this, "URL", url);
                            SPUtil.put(StoreDetailActivity.this, "cfgURL", cfgURL);
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

        String url2 = (String) SPUtil.get(this, "URL", "");
        if (url2.equals("")) {
            SPUtil.put(StoreDetailActivity.this, "URL", "https://hk.whoot.com/");
        }
        urlUtil = (String) SPUtil.get(StoreDetailActivity.this, "URL", "");
        String timela = (String) SPUtil.get(StoreDetailActivity.this, "timeLag", "0");
        timeLag = Long.parseLong(timela);
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

        //获取Scheme跳转的参数
        Intent intent = getIntent();
        //以此为标记判断是否从链接进去

        favour = intent.getIntExtra("favour", 0);
        String scheme = intent.getScheme();
        String dataString = intent.getDataString();
//        distances = intent.getStringExtra("distances");
//        Log.d(TAG, "onCreateInit: distance "+distances);
//        if (distances != null) {
//            double dis = Double.parseDouble(distances);
//            if (dis >= 1) {
//                String s = DateUtil.roundByScale(dis, 1);
//                tvStoreDistance.setText(s + getResources().getString(R.string.km));
//            } else {
//                double v = dis * 1000;
//                String of = String.valueOf(v);
//                String[] split = of.split("\\.");
//                tvStoreDistance.setText(split[0] + getResources().getString(R.string.m));
//            }
//        }
//        if (distances != null) {
//            double v1 = Double.parseDouble(distances);
//            if (v1 >= 1000) {
//                double v = v1 / 1000;
//                String s = DateUtil.roundByScale(v, 1);
//                tvStoreDistance.setText(s + getResources().getString(R.string.km_away));
//
//            } else {
//                String of = String.valueOf(distance);
//                String[] split = of.split("\\.");
//                tvStoreDistance.setText(split[0] + getResources().getString(R.string.m_away));
//            }
//        }

        Uri uri = intent.getData();
        if (uri != null) {
            //完整的url信息
            String url = uri.toString();
            Log.d("dksfjdk", url);
            //scheme部分
            String schemes = uri.getScheme();
            //host部分
            String host = uri.getHost();
            //port部分
            int port = uri.getPort();
            //访问路径
            String path = uri.getPath();
            //编码路径
            String path1 = uri.getEncodedPath();
            //query部分
            String queryString = uri.getQuery();
            //获取参数值
            locationId1 = uri.getQueryParameter("locationId");
            couponId1 = uri.getQueryParameter("couponId");
            shopId1 = uri.getQueryParameter("shopId");
            shopGiftCfgId1 = uri.getQueryParameter("shopGiftCfgId");

            //判断是否登录
            token = (String) SPUtil.get(StoreDetailActivity.this, "Token", "");
            String accToken = (String) SPUtil.get(StoreDetailActivity.this, "accToken", "");

            if (token.length() == 0 && accToken.length() == 0) {
                Intent intent1 = new Intent(StoreDetailActivity.this, LoginActivity.class);
                startActivity(intent1);
            } else {

                if (couponId1.equals("undefined")) {

                } else {
                    if (token.length() == 0) {
                        browsDialog = new GiftRewardDialog(StoreDetailActivity.this, R.style.box_dialog, onClickListener);
                        browsDialog.show();
                    }

                }
            }

        }


        titleCarditemImg.setVisibility(View.VISIBLE);
//        titleCarditemTitle.setText(getResources().getString(R.string.store_w));
        isGaoDe = isApp(StoreDetailActivity.this, AMAP_PACKAGENAME);
        isBAidu = isApp(StoreDetailActivity.this, BAIDUMAP_PACKAGENAME);
        isGOOGLE = isApp(StoreDetailActivity.this, GOOGLE_PACKAGENAME);

        id = intent.getIntExtra("id", 0);
//        distance = intent.getStringExtra("distance");
//        int OperatingStatus = intent.getIntExtra("closingSoon", 4);
//        if (OperatingStatus == 1) {
//            tvOpenStatus.setText(R.string.soon);
//        } else if (OperatingStatus == 2) {
//
//            tvOpenStatus.setText(R.string.closs_wei);
//        } else if (OperatingStatus == 0) {
//            tvOpenStatus.setText(R.string.open);
//        }
//        mData.setOpenStatus(OperatingStatus);

        Log.d(TAG, "id:" + id);
        if (id == 0) {
            if (!shopId1.equals("undefined")) {
                id = Integer.parseInt(shopId1);
            }
        }
        mData.setShopId(id);
        url = urlUtil + UrlUtil.comme + "?locationId=1" + "&shopId=" + id + "&start=0" + "&rows=" + 5;

        initView();
        initLoginButtonListener();


    }


    private String shop_id = "";
    private List<CardBean> itemlist = new ArrayList<CardBean>();//评论

    private int favShopId;
    public void getComment() {
        if (shopId1.equals("undefined")) {
            shop_id = String.valueOf(id);
        } else {
            shop_id = shopId1;
        }
        Http.OkHttpGet(StoreDetailActivity.this, urlUtil + UrlUtil.saveinfo + "?locationId=" + "1" + "&shopId=" + shop_id, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                getUserTokenInfo();
                try {
                    Log.d(TAG, result + "==" + urlUtil + UrlUtil.saveinfo + "?locationId=" + "1" + "&shopId=" + shop_id);
                    JSONObject jsonObject = new JSONObject(result);
                    if (itemlist.size() != 0) {
                        itemlist.clear();
                    }
                    String code = jsonObject.getString("code");
                    if (code.equals("0")) {
                        try {
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray commentAry=data.getJSONArray("commentList");
                            String coupHistoryStr=data.optString("couponHistory");
                            CardBean.CouponHistoryBean historyBean=null;
                            if(coupHistoryStr!=null){
                                historyBean= GSonUtil.parseGson(coupHistoryStr, CardBean.CouponHistoryBean.class);
                            }

                            for (int i = 0; i < commentAry.length(); i++) {
                                Log.d(TAG, "OnSuccess: data.getString "+commentAry.getString(i));
                                CardBean bean = GSonUtil.parseGson(commentAry.getString(i), CardBean.class);
                                if(bean!=null) {
                                    bean.setCouponHistory(historyBean);
                                    itemlist.add(bean);
                                }
                            }
                            mData.setCouponHistoryBean(historyBean);
                            mData.setReviewList(itemlist);
                            /**
                             * 收藏
                             * */
                            boolean favShop = data.optBoolean("favShop");
                            favShopId = data.optInt("favShopId");
                            Log.d(TAG, "OnSuccess: favShop "+favShop+",favShopId "+favShopId);
                            if (favShop) {
                                titleCarditemNo.setVisibility(View.VISIBLE);
                                titleCarditemShou.setVisibility(View.GONE);
                            } else {
                                titleCarditemShou.setVisibility(View.VISIBLE);
                                titleCarditemNo.setVisibility(View.GONE);
                            }

                            boolean uncommented = data.optBoolean("uncommented");
                            Log.d(TAG, "OnSuccess: uncommented "+uncommented);
                            mData.setUncommented(uncommented);
                            mDishReviewFrag.refreshOnDataLoad(mData);
                        } catch (Exception e) {
                          e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                getUserTokenInfo();
            }
        });
    }

    //TODO
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.vox_finsh:  //确认
//                    try {
//                        dialog.dismiss();
//                    } catch (Exception e) {
//                    }
                    try {
                        browsDialog.dismiss();
                    } catch (Exception e) {
                    }
                    try {
                        OneDialog.dismiss();
                    } catch (Exception e) {
                    }

                    try {
                        ThreeDialog.dismiss();
                    } catch (Exception e) {
                    }
                    break;
                case R.id.gift_frag_facce:   //facebook登录
                    LoginManager.getInstance().logInWithReadPermissions(StoreDetailActivity.this, Arrays.asList("public_profile"));
                    browsDialog.dismiss();

                    break;
                case R.id.gift_box_ly:
                    try {
                        TweDialog.dismiss();
                    } catch (Exception e) {
                    }
                    break;
                case R.id.gift_ly_one: //查看礼包
                    Intent intent = new Intent(StoreDetailActivity.this, GiftItemActivity.class);
                    intent.putExtra("shopid", shopid + "");
                    intent.putExtra("giftSn", giftSn);
                    intent.putExtra("expiredTm", expiredTm + "");
                    intent.putExtra("giftUseId", giftUseId + "");
                    intent.putExtra("type", type + "");
                    intent.putExtra("userId", userId + "");
                    intent.putExtra("giftId", giftId + "");
                    startActivity(intent);
                    ThreeDialog.dismiss();
                    break;

            }
        }
    };

    private boolean isApp(Context context, String packname) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo != null) {
            return true;
        } else {
            return false;
        }
    }

    private void initLoginButtonListener() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                Log.d(TAG, "facebook登录成功了" + loginResult.getAccessToken().getToken());
                //获取登录信息
                getLoginInfo(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // Toast.makeText(CardItemActivity.this, getResources().getString(R.string.facebook), Toast.LENGTH_SHORT).show();
                ToastUtil.showgravity(StoreDetailActivity.this, getResources().getString(R.string.facebook));
                Log.d(TAG, "facebook登录成功了");

            }

            @Override
            public void onError(FacebookException error) {
                //Toast.makeText(CardItemActivity.this, getResources().getString(R.string.facebook_one), Toast.LENGTH_SHORT).show();
                ToastUtil.showgravity(StoreDetailActivity.this, getResources().getString(R.string.facebook_one));
                Log.d(TAG, "facebook登录成功了");

            }
        });
    }

    public void getLoginInfo(AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object != null) {
                    String id = object.optString("id");   //比如:1565455221565
                    String name = object.optString("name");  //比如：Zhang San
                    String gender = object.optString("gender");  //性别：比如 male （男）  female （女）
                    String emali = object.optString("email");  //邮箱：比如：56236545@qq.com
                    SPUtil.put(StoreDetailActivity.this, "email", emali);

                    //获取用户头像
                    JSONObject object_pic = object.optJSONObject("picture");
                    JSONObject object_data = object_pic.optJSONObject("data");
                    String photo = object_data.optString("url");
                    // SPUtil.put(LoginActivity.this,"photo",photo);
                    //获取地域信息
                    String locale = object.optString("locale");   //zh_CN 代表中文简体


                    Log.d("hhahha", object.toString());
                    String token = accessToken.getToken();
                    /*token="EAAdMphXU5hIBAAp7Xcxu3rc7oiLpgvcQDKswCkb5UXA2CdmvvBBV7EUk7KUKytmXfM11mncfbjJf86MYkjVjc1Qa8aCUSZBalTQxMkpGdCgJvjj4zmDfH5ghV0SH8sIxKAcCV0nZ" +
                            "AthpOW3a9Iqdz98yxtbwt4WAYSSwYiGgpArZBZBJg5oapdgxEKuNfPa5c05JNSHthuhLtMacHouz";*/
                    FormBody body = new FormBody.Builder()
                            .add("type", String.valueOf(2))
                            .add("accessToken", token)
                            .build();
                    Http.OkHttpPost(StoreDetailActivity.this, urlUtil + UrlUtil.login, body, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            Log.d("loginpoiuyy", result);
                            // SPUtil.remove(LoginActivity.this,"accToken");
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
                                    String userid = account.getString("id");
                                  /*  try{
                                        String email = account.getString("email");
                                        SPUtil.put(LoginActivity.this,"email",email);
                                    }catch (Exception e){

                                    }*/
                                    try {
                                        long couponCd = account.getLong("couponCd");
                                        long couponCdl = couponCd - 300000;
                                        SPUtil.put(StoreDetailActivity.this, "downco", String.valueOf(couponCdl));
                                    } catch (Exception e) {

                                    }

                                    SPUtil.put(StoreDetailActivity.this, "Token", Token);
                                    SPUtil.put(StoreDetailActivity.this, "photo", photo1); //头像
                                    SPUtil.put(StoreDetailActivity.this, "gen", gen); //性别
                                    SPUtil.put(StoreDetailActivity.this, "name_one", name_one); //名字
                                    SPUtil.put(StoreDetailActivity.this, "you_usid", userid + "");

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

                                            SPUtil.put(StoreDetailActivity.this, "you_id", id1 + "");
                                            SPUtil.put(StoreDetailActivity.this, "you_Sn", couponSn + "");
                                            SPUtil.put(StoreDetailActivity.this, "you_type", couponId + "");


                                        }

                                    } catch (Exception e) {

                                    }


                                    initData();

                                } else {
                                    // Toast.makeText(CardItemActivity.this,getResources().getString(R.string.lo),Toast.LENGTH_SHORT).show();
                                    ToastUtil.showgravity(StoreDetailActivity.this, getResources().getString(R.string.lo));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //facebook回调
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        llDetailInfo.setTag("expand");
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mCollapsingtoolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar);
        mAppBarLayout = findViewById(R.id.app_bar_layout);
        mDishReviewFrag = new DishReviewFragment();
        titles.add(getResources().getString(R.string.label_dishes));
        titles.add(getResources().getString(R.string.label_reviews));
        initTab();
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(mDishReviewFrag);
        MyFragmentPager fragmentPager = new MyFragmentPager(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(fragmentPager);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabIndex = tab.getPosition();

                for(int i=0;i<mTabLayout.getTabCount();i++) {
                    TextView textView = (TextView) mTabLayout.getTabAt(i).getCustomView().findViewById(R.id.tv_tab);
                    if(i==tabIndex) {
                        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    }else{
                        textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    }
                }
                mCurrentTabIndex=tabIndex;
                updateTabOnIndexChange();



            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                appBarLayout.setExpanded(false);
            }
        });
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            refreshLayout.setEnableRefresh(true);
            if (mCurrentState != State.EXPANDED) {
//                        onStateChanged(appBarLayout, State.EXPANDED);
                Log.d(TAG, "onOffsetChanged: EXPANDED");
                updateTabIfCollapse(false);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            refreshLayout.setEnableRefresh(false);
            if (mCurrentState != State.COLLAPSED) {
//                        onStateChanged(appBarLayout, State.COLLAPSED);
                Log.d(TAG, "onOffsetChanged: COLLAPSED");
                updateTabIfCollapse(true);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            refreshLayout.setEnableRefresh(false);
            if (mCurrentState != State.INTERMEDIATE) {
//                        onStateChanged(appBarLayout, State.INTERMEDIATE);
                Log.d(TAG, "onOffsetChanged: INTERMEDIATE");
                updateTabIfCollapse(false);
            }
            mCurrentState = State.INTERMEDIATE;
        }

        if (verticalOffset <= -titleCarditemFl.getHeight() / 2) {
            titleCarditemTitle.setText(nameCh);
            //使用下面两个CollapsingToolbarLayout的方法设置展开透明->折叠时你想要的颜色
//                    mCollapsingtoolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
//                    mCollapsingtoolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            titleCarditemTitle.setText("");
        }
    }

    private void initTab(){

        for (int i = 0; i < titles.size(); i++) {
            //获取每一个tab对象
            TabLayout.Tab tabAt = mTabLayout.newTab();
            //将每一个条目设置我们自定义的视图
            tabAt.setCustomView(R.layout.item_tab);

            //通过tab对象找到自定义视图的ID
            TextView textView = (TextView) tabAt.getCustomView().findViewById(R.id.tv_tab);
//            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
            textView.setText(titles.get(i));//设置tab上的文字
            if(i==0){
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }else{
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
            int pos=i;
            tabAt.getCustomView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: "+pos);
                    tabAt.select();
                    appBarLayout.setExpanded(false);
                    mDishReviewFrag.onTabChange(pos);

                }
            });
            mTabLayout.addTab(tabAt);
        }
        View tabStripView = mTabLayout.getChildAt(0);
        tabStripView.setBackground(new IndicatorDrawable(tabStripView));//设置背景 添加自定义下划线
        updateTabOnIndexChange();
        appBarLayout.setExpanded(true);
    }

    private List<CouponBean> couponlist = new ArrayList<CouponBean>();
    List<CouponBean.DataBean.CouponsBean> brozens = new ArrayList<>();
    List<CouponBean.DataBean.CouponsBean> silvers = new ArrayList<>();
    List<CouponBean.DataBean.CouponsBean> golds = new ArrayList<>();
    List<CouponBean.DataBean.CouponsBean> diamons = new ArrayList<>();

    public void getUserTokenInfo() {

        String token = (String) SPUtil.get(StoreDetailActivity.this, "Token", "");
        if (token.length()==0){
            dissLoad();
            refreshLayout.finishLoadMore();
            refreshLayout.finishRefresh();
            return;
        }

        Http.OkHttpGet(StoreDetailActivity.this, urlUtil + UrlUtil.coupon, new Http.OnDataFinish() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void OnSuccess(String result) {
                if(isDestroyed()||isFinishing()){
                    return;
                }
                Log.d(TAG, "getLOdin: "+result);
                dissLoad();
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    long serverTime = jsonObject.getLong("serverTime");

                    if (code.equals("0")) {
                        if (couponlist.size() > 0) {
                            couponlist.clear();
                        }
                        Gson gson = new Gson();
                        CouponBean couponBean = gson.fromJson(result, CouponBean.class);
                        couponlist.add(couponBean);
                        List<CouponBean.DataBean.CouponsBean> coupons = couponlist.get(0).getData().getCoupons();
                        List<CouponBean.DataBean.CouponPiecesBean> couponPieces = couponlist.get(0).getData().getCouponPieces();
                        List<CycleRecord> cycleRecords = couponBean.getData().getCycleRecords();
//                        if (cycleRecords != null && cycleRecords.size() > 0) {
//                            for (CycleRecord record : cycleRecords) {
//                                switch (record.getType()) {
//                                    case Constants.TYPE_COUPON_EXCHANGE_BROZEN:
//                                        tvBrozenProgress.setText((int)(record.getProgress() * 2) + "");
//                                        if (record.getProgress() == 0.5) {
//                                            ivArrowBrozen.setVisibility(View.VISIBLE);
//                                        } else {
//                                            ivArrowBrozen.setVisibility(View.GONE);
//                                        }
//                                        mBrozenExchangeProgress = record.getProgress();
//                                        break;
//                                    case Constants.TYPE_COUPON_EXCHANGE_SILVER:
//                                        tvSilverProgress.setText((int)(record.getProgress() * 2) + "");
//                                        if (record.getProgress() == 0.5) {
//                                            ivArrowSilver.setVisibility(View.VISIBLE);
//                                        } else {
//                                            ivArrowSilver.setVisibility(View.GONE);
//                                        }
//                                        mSilverExchangeProgress = record.getProgress();
//                                        break;
//                                    case Constants.TYPE_COUPON_EXCHANGE_GOLDEN:
//                                        tvGoldProgres.setText((int)(record.getProgress() * 2) + "");
//                                        if (record.getProgress() == 0.5) {
//                                            arrowGoldIv.setVisibility(View.VISIBLE);
//                                        } else {
//                                            arrowGoldIv.setVisibility(View.GONE);
//                                        }
//                                        mGoldExchangeProgress = record.getProgress();
//                                        break;
//                                }
//
//                            }
//                        }
                        brozens.clear();
                        silvers.clear();
                        golds.clear();
                        diamons.clear();

                        int e = 0, b = 0, c = 0, d = 0;
                        for (int i = 0; i < coupons.size(); i++) {
                            CouponBean.DataBean.CouponsBean bean=coupons.get(i);
                            if (coupons.get(i).getCouponId() == 1) {    //铜

                                if (e == 0) {
                                    mData.setCouponSnBrozen(couponlist.get(0).getData().getCoupons().get(i).getCouponSn());
                                    mData.setCouponIdBrozen(couponlist.get(0).getData().getCoupons().get(i).getId());
                                    mData.setCouponTypeBrozen(couponlist.get(0).getData().getCoupons().get(i).getCouponId());

                                    e = 1;
                                }
                                brozens.add(bean);
                            } else if (coupons.get(i).getCouponId() == 2) {  //银

                                long expiredTm1 = coupons.get(i).getExpiredTm();
                                boolean nowTime = TimeUtil.compareNowTime(String.valueOf(expiredTm1), String.valueOf(serverTime));
                                if (nowTime) {
                                    if (b == 0) {
                                        mData.setCouponSnSilver(couponlist.get(0).getData().getCoupons().get(i).getCouponSn());
                                        mData.setCouponIdSilver(couponlist.get(0).getData().getCoupons().get(i).getId());
                                        mData.setCouponTypeSilver(couponlist.get(0).getData().getCoupons().get(i).getCouponId());
//                                        expiredTm_yin = couponlist.get(0).getData().getCoupons().get(i).getExpiredTm();
                                    }

                                    silvers.add(bean);
                                }

                            } else if (coupons.get(i).getCouponId() == 3) {  //金

                                long expiredTm1 = coupons.get(i).getExpiredTm();
                                boolean nowTime = TimeUtil.compareNowTime(String.valueOf(expiredTm1), String.valueOf(serverTime));
                                if (nowTime) {
                                    if (c == 0) {
                                        mData.setCouponSnGold(couponlist.get(0).getData().getCoupons().get(i).getCouponSn());
                                        mData.setCouponIdGold(couponlist.get(0).getData().getCoupons().get(i).getId());
                                        mData.setCouponTypeGold(couponlist.get(0).getData().getCoupons().get(i).getCouponId());
//                                        expiredTm_jin = couponlist.get(0).getData().getCoupons().get(i).getExpiredTm();
                                        c = 1;
                                    }
                                    golds.add(bean);

                                }

                            } else if (coupons.get(i).getCouponId() == 4) {  //钻
                                long expiredTm1 = coupons.get(i).getExpiredTm();
                                boolean nowTime = TimeUtil.compareNowTime(String.valueOf(expiredTm1), String.valueOf(serverTime));
                                if (nowTime) {
                                    if (d == 0) {
                                        mData.setCouponSnDiamond(couponlist.get(0).getData().getCoupons().get(i).getCouponSn());
                                        mData.setCouponIdDiamond(couponlist.get(0).getData().getCoupons().get(i).getId());
                                        mData.setCouponTypeDiamond(couponlist.get(0).getData().getCoupons().get(i).getCouponId());
//                                        expiredTm_zuan = couponlist.get(0).getData().getCoupons().get(i).getExpiredTm();

                                        d = 1;
                                    }

                                    diamons.add(bean);
                                }

                            } else {

                            }

                        }
                        mData.setBrozenTokenNum(brozens.size());
                        mData.setSilverTokenNum(silvers.size());
                        mData.setGoldTokenNum(golds.size());
                        mData.setDiamongTokenNum(diamons.size());
                        SPUtil.put(StoreDetailActivity.this,"brozens_num",brozens.size());

                    } else if (code.equals("5007")) {
                        SPUtil.remove(StoreDetailActivity.this, "Token");
                        SPUtil.remove(StoreDetailActivity.this, "photo1");
                        SPUtil.remove(StoreDetailActivity.this, "photo");
                        SPUtil.remove(StoreDetailActivity.this, "gen");
                        SPUtil.remove(StoreDetailActivity.this, "name_one");
                        SPUtil.put(StoreDetailActivity.this, "deng", false);
                        Intent intent = new Intent(StoreDetailActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        //失败
                        //Toast.makeText(getActivity(), getResources().getString(R.string.fan), Toast.LENGTH_SHORT).show();
                        ToastUtil.showgravity(StoreDetailActivity.this, getResources().getString(R.string.fan));
                    }

                    mDishReviewFrag.refreshOnDataLoad(mData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onError(Exception e) {
                if(isFinishing()||isDestroyed()){
                    return;
                }
                dissLoad();
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        reflex(mTabLayout);
        appBarLayout.addOnOffsetChangedListener(StoreDetailActivity.this);
        String toke = (String) SPUtil.get(this, "Token", "");
        if (toke.length() != 0) {
            int my_tong = (int) SPUtil.get(this, "My_tong", 0);
            if (my_tong!=0){
                loginCoupDialog = new LoginCoupDialog(StoreDetailActivity.this, R.style.box_dialog,onClickListenerToken);
                loginCoupDialog.show();
                SPUtil.remove(this,"My_tong");
                SPUtil.remove(this,"My_yin");
                SPUtil.remove(this,"My_jin");
                SPUtil.remove(this,"My_zuan");
            }
        }
        Log.d(TAG, "onResume: isPreviewingImage "+mDishReviewFrag.isPreviewingImage());
        if(mDishReviewFrag!=null&&mDishReviewFrag.isPreviewingImage()){
            mDishReviewFrag.setPreviewingImage(false);//reset status
            return;
        }
        if (flag_sto){
            flag_sto=false;
        }else {
            loading(getResources().getString(R.string.listview_loading));
            initData();
        }

    }
    private View.OnClickListener onClickListenerToken = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_box_ly:
                    loginCoupDialog.dismiss();
                    EventBusCarrier carrier=new EventBusCarrier();
                    TokenExchangeBean bean=new TokenExchangeBean();
                    bean.setSuToken(1);
                    bean.setFlagToken(2);
                    bean.setFlagCoupn(0);
                    carrier.setEventType(EVENT_TYPE_TOKEN_EXCHANGE);
                    carrier.setObject(bean);
                    EventBus.getDefault().post(carrier);
                    finish();
                    break;

            }
        }
    };



    public void reflex(final TabLayout tabLayout){

        /**
         * 通过反射修改TabLayout Indicator的宽度（仅在Android 4.2及以上生效）
         */

        Class<?> tabLayoutClass = tabLayout.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayoutClass.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        LinearLayout layout = null;
        try {
            if (tabStrip != null) {
                layout = (LinearLayout) tabStrip.get(tabLayout);
            }
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                    params.setMarginStart(ScreenUtils.dp2px(instance, 18f));
//                    params.setMarginEnd(ScreenUtils.dp2px(instance,  18f));
//                }
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentTab(int index){
        if(mCurrentTabIndex==index){
            return;
        }
        mCurrentTabIndex=index;
        for (int i = 0; i < titles.size(); i++) {
            TabLayout.Tab tabAt = mTabLayout.getTabAt(i);
            if(index==i){
                tabAt.select();
                break;
            }
        }
    }

    public void updateTabOnIndexChange(){
        for (int i = 0; i < titles.size(); i++) {
            TabLayout.Tab tabAt = mTabLayout.getTabAt(i);
            //通过tab对象找到自定义视图的ID
            TextView textView = (TextView) tabAt.getCustomView().findViewById(R.id.tv_tab);
            if(mCurrentTabIndex==i) {
                textView.setTextColor(getResources().getColor(R.color.main_gold));
            }else{
                textView.setTextColor(getResources().getColor(R.color.card_cc));
            }
        }
    }
//    private boolean mCollapse=false;
    private void updateTabIfCollapse(boolean collapse){
        for(int i=0;i<titles.size();i++){
            TabLayout.Tab tabAt = mTabLayout.getTabAt(i);
            TextView tv = (TextView) tabAt.getCustomView().findViewById(R.id.tv_tab);
            if(collapse){
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                mTabLayout.invalidate();
            }else{
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
                mTabLayout.invalidate();
            }
        }
//        mCollapse=collapse;
//        commonNavigatorAdapter.notifyDataSetChanged();
    }


    enum State{
        EXPANDED,//展开
        COLLAPSED,//折叠
        INTERMEDIATE//中间状态
    }
    private State mCurrentState = State.INTERMEDIATE;
    private static final long HALF_HOUR=30*60*1000;
//    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
    private long mCurrenServerTime;
    String luchang;
    private void initData() {
        String lastidu = (String) SPUtil.get(this, "lastidu", "");
        String longtidu = (String) SPUtil.get(this, "longtidu", "");
        if (shopId1.equals("undefined")) {
            url1 = urlUtil + UrlUtil.shop_info_new + "?shopId=" + id + "&latitude=" + lastidu + "&longitude=" + longtidu;
        } else {
            if (couponId1.equals("undefined")) {
                url1 = urlUtil + UrlUtil.shop_info_new + "?shopId=" + shopId1 + "&latitude=" + lastidu + "&longitude=" + longtidu;
            } else {
                url1 = urlUtil + UrlUtil.shop_info_new + "?shopId=" + shopId1 + "&latitude=" + lastidu + "&longitude=" + longtidu + "&locationId=" + locationId1 + "&couponId=" + couponId1 + "&shopGiftCfgId=" + shopGiftCfgId1;
            }
        }
        Http.OkHttpGet(StoreDetailActivity.this, url1, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                dissLoad();
                noNetworkView.setVisibility(View.GONE);
                mViewPager.setVisibility(View.VISIBLE);
                mTabLayout.setVisibility(View.VISIBLE);
                llStoreDetailHeader.setVisibility(View.VISIBLE);
                Log.d(TAG, "refreshOnDataLoad: getOpenStatus "+mData.getOpenStatus());
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.d("shangdianxiangqig", result + "==" + url1);
                    mCurrenServerTime = jsonObject.getLong("serverTime");
                    if (jsonObject.getString("code").equals("0")) {
                        if (Toplist.size() > 0) {
                            Toplist.clear();
                            readlist.clear();
                        }
                        JSONObject data = jsonObject.getJSONObject("data");
                        Bundle b=new Bundle();
                        b.putString("type","SHOW_RESTAURANT_INFO");
                        b.putString("from",(String)SPUtil.get(StoreDetailActivity.this,Constants.SP_KEY_STORE_DETAIL_FROM,""));
                        b.putString("id",shop_id);
                        b.putString("name",data.optString("name"));
                        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.SHOW_RESTAURANT_DETAIL,b);
                        /**
                         * 轮播图
                         * */
                        images.clear();
                        try {
                            JSONArray pics = data.getJSONArray("pics");
                            for (int p = 0; p < pics.length(); p++) {
                                images.add(pics.getString(p));
                            }
                            mData.setStoreImages(images);
                            updateStoreImages();
                        } catch (Exception e) {

                        }
                        mData.setCloseTmFrist(data.optLong("closeTmFrist"));
                        mData.setCloseTmSecond(data.optLong("closeTmSecond"));
                        mData.setOpenTmFirst(data.optLong("openingTmFrist"));
                        mData.setOpenTmSecond(data.optLong("openingTmSecond"));
                        if(mData.getCloseTmFrist()!=0) {
                            mData.setCloseTmFrist(TimeUtil.getExactTime(mData.getCloseTmFrist(), mCurrenServerTime));
                        }
                        if(mData.getCloseTmSecond()!=0) {
                            mData.setCloseTmSecond(TimeUtil.getExactTime(mData.getCloseTmSecond(), mCurrenServerTime));
                        }
                        if(mData.getOpenTmFirst()!=0) {
                            mData.setOpenTmFirst(TimeUtil.getExactTime(mData.getOpenTmFirst(), mCurrenServerTime));
                        }
                        if(mData.getOpenTmSecond()!=0) {
                            mData.setOpenTmSecond(TimeUtil.getExactTime(mData.getOpenTmSecond(), mCurrenServerTime));
                        }
                        if(mData.getCloseTmFrist()!=0&&mData.getCloseTmSecond()!=0&&(mCurrenServerTime>=mData.getCloseTmFrist()&&mCurrenServerTime<mData.getOpenTmSecond()||mCurrenServerTime<mData.getOpenTmFirst()||mCurrenServerTime>=mData.getCloseTmSecond())
                        ||(mData.getCloseTmFrist()==0&&mData.getCloseTmSecond()!=0&&(mCurrenServerTime<mData.getOpenTmSecond()||mCurrenServerTime>=mData.getCloseTmSecond()))
                        ||(mData.getCloseTmFrist()!=0&&mData.getCloseTmSecond()==0&&(mCurrenServerTime<mData.getOpenTmFirst()||mCurrenServerTime>=mData.getCloseTmFrist()))
                        ||(mData.getCloseTmFrist()==0&&mData.getCloseTmSecond()==0)){//已关门
                            mData.setOpenStatus(Constants.OPEN_STATUS_CLOSED);
                        }else if(mCurrenServerTime>=mData.getOpenTmFirst()&&mCurrenServerTime+HALF_HOUR<mData.getCloseTmFrist()||
                                mCurrenServerTime>=mData.getOpenTmSecond()&&mCurrenServerTime+HALF_HOUR<mData.getCloseTmSecond()){//已开门
                            mData.setOpenStatus(Constants.OPEN_STATUS_OPEND);
                        }else{//即将关门
                            mData.setOpenStatus(Constants.OPEN_STATUS_CLOSEING_SOON);
                        }
                        if(mData.getOpenStatus() == 1){
                            storeCloseStatusTv.setVisibility(View.VISIBLE);
                            String formatTime="";
                            String closeStr="";
                            SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
                            if(mCurrenServerTime<=mData.getCloseTmFrist()) {
                                closeStr = String.format(getResources().getString(R.string.label_close_time), sdf.format(new Date(mData.getCloseTmFrist())));
                            }else if(mCurrenServerTime<=mData.getCloseTmSecond()){
                                closeStr = String.format(getResources().getString(R.string.label_close_time), sdf.format(new Date(mData.getCloseTmSecond())));
                            }
                            storeCloseStatusTv.setText(getString(R.string.soon)+"\n"+closeStr);
                        }else if(mData.getOpenStatus() == 2){
                            storeCloseStatusTv.setVisibility(View.VISIBLE);
                            storeCloseStatusTv.setText(getString(R.string.closs_wei));
                        }else{
                            storeCloseStatusTv.setVisibility(View.GONE);
                        }
                        int avgConsumption=data.optInt("avgConsumptio");
                        tvAverageConsumption.setText(String.format(getResources().getString(R.string.label_hkd_person),avgConsumption));
                        if(avgConsumption==0){
                            tvAverageConsumption.setVisibility(View.GONE);
                        }else{
                            tvAverageConsumption.setVisibility(View.VISIBLE);
                        }
                        int priceLevel = 0;
                        try {
                            priceLevel = data.getInt("priceLevel");
                        } catch (Exception e) {
                            priceLevel = 0;
                        }
                        if(MyApplication.getInstance().getPriceScopeBeanMap().containsKey(priceLevel+"")) {
                            PriceScopeBean bean=MyApplication.getInstance().getPriceScopeBeanMap().get(priceLevel+"");
                            Log.d(TAG, "OnSuccess: priceLevel "+priceLevel+","+bean.getMin()+","+bean.getMax());
                            if(bean!=null) {
                                switch (priceLevel) {
                                    case 1:
//                                        tvPriceLevel.setVisibility(View.VISIBLE);
                                        tvPriceLevel.setText("$ <" + bean.getMax());
                                        break;
                                    case 2:
                                    case 3:
                                    case 4:
//                                        tvPriceLevel.setVisibility(View.VISIBLE);
                                        tvPriceLevel.setText("$ " + bean.getMin() + "-" + bean.getMax());
                                        break;
                                    case 5:
//                                        tvPriceLevel.setVisibility(View.VISIBLE);
                                        tvPriceLevel.setText("$ >" + bean.getMin());
                                        break;
                                    default:
//                                        tvPriceLevel.setVisibility(View.GONE);
                                        break;
                                }

                            }
                        }else{
//                            tvPriceLevel.setVisibility(View.GONE);
                        }
                        tags = "";
                        try {
                            tags = data.getString("tags");
                        } catch (Exception e) {
                            tags = "";
                        }
                        try {
                            describe = data.getString("describe");
                            if (describe.length() > 0) {
                                tvStoreDesc.setVisibility(View.VISIBLE);
                                tvStoreDesc.setText(describe);
                            } else {
                                tvStoreDesc.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            tvStoreDesc.setVisibility(View.GONE);
                        }


                        JSONArray redeem = data.getJSONArray("redeem");
                        JSONArray hotCommodity = data.getJSONArray("hotCommodity");
                        try {
                            JSONArray workingTmList = data.getJSONArray("workingTmList");//营业时间
                            if (workingTmList.length() != 0) {
                                if (Tmlist.size() > 0) {
                                    Tmlist.clear();
                                }
                                int week = workingTmList.getJSONObject(0).getInt("week");
                                switch (week) {
                                    case 1:
                                        card_qi.setText(getResources().getString(R.string.mon));
                                        break;
                                    case 2:
                                        card_qi.setText(getResources().getString(R.string.tues));
                                        break;
                                    case 3:
                                        card_qi.setText(getResources().getString(R.string.wednes));
                                        break;
                                    case 4:
                                        card_qi.setText(getResources().getString(R.string.thurs));
                                        break;
                                    case 5:
                                        card_qi.setText(getResources().getString(R.string.fri));
                                        break;
                                    case 6:
                                        card_qi.setText(getResources().getString(R.string.satur));
                                        break;
                                    case 7:
                                        card_qi.setText(getResources().getString(R.string.sun));
                                        break;
                                }
                                //第一次开关门时间
                                try {
                                    closeTm = workingTmList.getJSONObject(0).getLong("closeTmFrist");

                                } catch (Exception e) {
                                    closeTm = -1;

                                }
                                try {
                                    openingTm = workingTmList.getJSONObject(0).getLong("openingTmFrist");
                                } catch (Exception e) {
                                    openingTm = -1;
                                }
                                int tm = 0;
                                if (closeTm != -1 && openingTm != -1) {
                                    String closeTmText = TimeUtil.timeUTCdate(closeTm + timeLag);
                                    String openingTmText = TimeUtil.timeUTCdate(openingTm + timeLag);
                                    String[] splitcloseTm = closeTmText.split(" ");
                                    String[] splitopeningTm = openingTmText.split(" ");
                                    String s = splitcloseTm[1];
                                    String s1 = splitopeningTm[1];
                                    card_time.setText(s1 + "-" + s);
                                } else {
                                    card_time_l.setVisibility(View.GONE);
                                    tm = 1;
                                }
                                /**
                                 * 第二次关门时间
                                 * */
                                try {
                                    closeTmSecond = workingTmList.getJSONObject(0).getLong("closeTmSecond");
                                } catch (Exception e) {
                                    closeTmSecond = -1;
                                }
                                try {
                                    openingTmSecond = workingTmList.getJSONObject(0).getLong("openingTmSecond");
                                } catch (Exception e) {
                                    openingTmSecond = -1;
                                }
                                if (closeTmSecond != -1 && openingTmSecond != -1) {
                                    String closeTmSecondtimedate = TimeUtil.timeUTCdate(closeTmSecond + timeLag);
                                    String openingTmSecondtimedate = TimeUtil.timeUTCdate(openingTmSecond + timeLag);
                                    String[] splitclose = closeTmSecondtimedate.split(" ");
                                    String[] splitopening = openingTmSecondtimedate.split(" ");
                                    String s = splitclose[1];
                                    String s1 = splitopening[1];
                                    card_time_cl.setText(s1 + "-" + s);
                                } else {
                                    card_time_l.setVisibility(View.GONE);
                                    if (tm == 1) {
                                        card_time.setText(getResources().getString(R.string.closs_wei));
                                        card_qi.setTextColor(getResources().getColor(R.color.main_gold));
                                        card_time.setTextColor(getResources().getColor(R.color.main_gold));
                                    }
                                }


                                for (int j = 1; j < workingTmList.length(); j++) {
                                    WorkingTmListBean tmBean = GSonUtil.parseGson(workingTmList.getString(j), WorkingTmListBean.class);
                                    Tmlist.add(tmBean);
                                }
                                updateOpenTimeView();
                            }
                        } catch (Exception e) {
                        }

                        name = data.getString("name");


                        if (hotCommodity.length() != 0) {  //热卖

                            if (Toplist.size() > 0) {
                                Toplist.clear();
                            }
                            for (int a = 0; a < hotCommodity.length(); a++) {

                                RewardItemBean readBean = GSonUtil.parseGson(hotCommodity.getString(a), RewardItemBean.class);
                                Toplist.add(readBean);
                            }
                            mData.setRewardItemBeanList(Toplist);
                        }
//                        else {
//                            cardMoreItem.setVisibility(View.GONE);
//                            card_vi.setVisibility(View.GONE);
//                            card_vie.setVisibility(View.GONE);
//                            horr_list.setVisibility(View.GONE);
//                        }
                        if (redeem.length() != 0) {
                            if (readlist.size() > 0) {
                                readlist.clear();
                            }
                            for (int i = 0; i < redeem.length(); i++) {


                                CardItemTopBean topBean = GSonUtil.parseGson(redeem.getString(i), CardItemTopBean.class);
                                readlist.add(topBean);   //热卖list
                            }
                            mData.setRecommendList(readlist);
                        }
//                        else {
//                            cardNoText.setVisibility(View.VISIBLE);
//                        }
                        //礼包信息
                        try {
                            JSONObject userGiftReqVo = data.getJSONObject("userGiftReqVo");
                            boolean giftReceived = userGiftReqVo.getBoolean("giftReceived");
                            boolean giftExpired = userGiftReqVo.getBoolean("giftExpired");
                            if (giftExpired) { //已过期
                                if (token.equals("")) {

                                } else {

                                    OneDialog = new GiftReOneDialog(StoreDetailActivity.this, R.style.box_dialog, onClickListener);
                                    OneDialog.show();
                                }
                            } else {

                                if (giftReceived) {  //已经领取

                                    String token = (String) SPUtil.get(StoreDetailActivity.this, "Token", "");
                                    if (token.equals("")) {

                                    } else {
                                        TweDialog = new GiftReTweDialog(StoreDetailActivity.this, R.style.box_dialog, onClickListener);
                                        TweDialog.show();
                                    }

                                } else {
                                    if (token.equals("")) {

                                    } else {
                                        ThreeDialog = new GiftThreeReDialog(StoreDetailActivity.this, R.style.box_dialog, onClickListener);
                                        ThreeDialog.show();
                                    }
                                }
                            }
                            expiredTm = userGiftReqVo.getString("expiredTm");

                            giftSn = userGiftReqVo.getString("giftSn");
                            shopid = userGiftReqVo.getString("shopid");
                            userId = userGiftReqVo.getString("userId");
                            giftUseId = userGiftReqVo.getString("giftUseId");
                            type = userGiftReqVo.getString("type");
                            giftId = userGiftReqVo.getString("id");
                        } catch (Exception e) {

                        }
                        luchang= (String) SPUtil.get(StoreDetailActivity.this, "luchang", "");
                        nameCh = "";
                        try {
                            nameCh = data.getString("name");
                            tvName.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            nameCh = "";
                            tvName.setVisibility(View.GONE);
                        }
                        tvName.setText(nameCh);
                        String nameEn = "";
                        try {
                            nameEn = data.getString("nameEn");
                            tvNameEn.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            tvNameEn.setVisibility(View.GONE);
                            nameEn = "";
                        }
                        tvNameEn.setText(nameEn);

                        String address = data.getString("address");
                        if (address != null) {
                            tvStoreAddress.setText(address);
                        }
                        double grade = 0;
                        try {
                            grade = data.getDouble("grade");
                        } catch (Exception e) {
                            grade = 0;
                        }
                        mData.setGrade(grade);

                        if (grade <= 5 && grade >= 4.5) {
                            carItemScore.setBackgroundResource(R.drawable.home_shape);
                            carItemScore.setText(grade + "");
                        } else if (grade < 4.5 && grade >= 4) {
                            carItemScore.setBackgroundResource(R.drawable.home_shape_four);
                            carItemScore.setText(grade + "");
                        } else if (grade < 4 && grade >= 3) {
                            carItemScore.setBackgroundResource(R.drawable.home_shape_three);
                            carItemScore.setText(grade + "");
                        } else if (grade < 3 && grade>0) {
                            carItemScore.setBackgroundResource(R.drawable.home_shape_twe);
                            carItemScore.setText(grade + "");
                        }else if(grade==0){
                            carItemScore.setBackgroundResource(R.drawable.home_shape_twe);
                            carItemScore.setText(getString(R.string.not_rating));
                        }

                        String pingfen = "";
                        try {
                            if (data.getString("commentCount").equals("0")) {
                                carItemScoreOne.setVisibility(View.INVISIBLE);
                            } else {
                                carItemScoreOne.setText("(" + data.getString("commentCount") + " " + String.format(getString(R.string.home_item_fen)));
                            }
                            pingfen = data.getString("commentCount");
                        } catch (Exception e) {
                            carItemScoreOne.setText("(0" + String.format(getString(R.string.home_item_fen)));
                            pingfen = 0 + "";
                        }
                        mData.setPingfen(pingfen);
                        int type = data.getInt("type");
                        try {
                            int currency = data.getInt("currency");
                        } catch (Exception e) {
                        }
                        int commentCount = 0;
                        try {
                            commentCount = data.getInt("commentCount");
                            mData.setCommentCount(commentCount);

                        } catch (Exception e) {
                            commentCount = 0;
                        }
                        String category1 = data.getString("category");
                        boolean zh = TimeUtil.isZh(StoreDetailActivity.this);
                        if (luchang.equals("0")) {
                            if (zh) {
                                luchang = "3";
                            } else {
                                luchang = "2";
                            }
                        }
                        int categoryId=0;
                        try {
                            categoryId = Integer.parseInt(category1);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if (luchang.equals("1") || luchang.equals("2") || luchang.equals("0")) {

                            if(MyApplication.getInstance().getChineseLanDishMap().containsKey(categoryId)) {
                                try {
                                    tvDishStyle.setVisibility(View.VISIBLE);
                                    tvDishStyle.setText(MyApplication.getInstance().getChineseLanDishMap().get(categoryId).get(type));
                                } catch (Exception e) {
                                    tvDishStyle.setVisibility(View.GONE);
                                }
                            }else{
                                tvDishStyle.setVisibility(View.GONE);
                            }
                        } else if (luchang.equals("3")) {
                            if(MyApplication.getInstance().getEnglishLanDishMap().containsKey(categoryId)) {
                                try {
                                    tvDishStyle.setVisibility(View.VISIBLE);
                                    tvDishStyle.setText(MyApplication.getInstance().getEnglishLanDishMap().get(categoryId).get(type));
                                } catch (Exception e) {
                                    tvDishStyle.setVisibility(View.GONE);
                                }
                            }else{
                                tvDishStyle.setVisibility(View.GONE);
                            }

                        }
                        Http.OkHttpGet(StoreDetailActivity.this, cfgURL, new Http.OnDataFinish() {
                            @Override
                            public void OnSuccess(String result) {
                                Log.d(TAG, "OnSuccess: cfgURL");
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    JSONObject location = data.getJSONObject("Location");
                                    JSONArray category = location.getJSONArray("Category");
                                    JSONArray shopTags = location.getJSONArray("ShopTags");
                                    for (int s = 0; s < shopTags.length(); s++) {
                                        ShopTagsBean bean = GSonUtil.parseGson(shopTags.getString(s), ShopTagsBean.class);
                                        sags.add(bean);
                                    }


//                                    String luchang = (String) SPUtil.get(StoreDetailActivity.this, "luchang", "");
//                                    boolean zh = TimeUtil.isZh(StoreDetailActivity.this);
//                                    if (luchang.equals("0")) {
//                                        if (zh) {
//                                            luchang = "3";
//                                        } else {
//                                            luchang = "2";
//                                        }
//                                    }
//                                    for (int i = 0; i < category.length(); i++) {
//                                        JSONObject jsonObject1 = category.getJSONObject(i);
//                                        String id = jsonObject1.getString("Id");
//                                        if (category1.equals(id)) {
//                                            JSONArray sub = jsonObject1.getJSONArray("Sub");
//                                            boolean existDishStyle=false;
//                                            for (int j = 0; j < sub.length(); j++) {
//                                                JSONObject o = (JSONObject) sub.get(j);
//                                                int id1 = o.getInt("Id");
//                                                if (type == id1) {
//                                                    existDishStyle=true;
//                                                    if (luchang.equals("1") || luchang.equals("2") || luchang.equals("0")) {
//                                                        try {
//                                                            tvDishStyle.setText(o.getString("NameCh"));
//                                                        } catch (Exception e) {
//                                                        }
//
//                                                    } else if (luchang.equals("3")) {
//                                                        try {
//                                                            tvDishStyle.setText(o.getString("NameEn"));
//                                                        } catch (Exception e) {
//                                                        }
//
//                                                    }
//                                                }
//                                            }
//                                            if(existDishStyle){
//                                                tvDishStyle.setVisibility(View.VISIBLE);
//                                            }else{
//                                                tvDishStyle.setVisibility(View.GONE);
//                                            }
//                                        }
//
//                                    }

                                    JSONArray currency = data.getJSONArray("Currency");
                                    String splitRes[] = null;
                                    Log.d(TAG, "OnSuccess: tags "+tags);
                                    mStoreTags.clear();
                                    if (!tags.equals("")) {
                                        splitRes = tags.replace("[", "").replace("]", "").split(",");
                                        for (int t = 0; t < splitRes.length; t++) {

                                            String string = splitRes[t];
                                            if (!string.equals("")) {
                                                for (int f = 0; f < sags.size(); f++) {
                                                    int id = sags.get(f).getId();
                                                    if (Integer.parseInt(string) == id) {
                                                        StoreTagBean bean = new StoreTagBean();
                                                        bean.setId(id);
                                                        if (luchang.equals("1") || luchang.equals("2") || luchang.equals("0")) {
                                                            bean.setName(sags.get(f).getNameCh());
                                                        } else if (luchang.equals("3")) {
                                                            bean.setName(sags.get(f).getNameEn());
                                                        }
                                                        if (string.equals("1")) {
                                                            bean.setDrawableResId(R.drawable.storedetail_info_icon_vegan);
                                                        } else if (string.equals("2")) {
                                                            bean.setDrawableResId(R.drawable.storedetail_info_icon_creditcard);
                                                        } else if (string.equals("3")) {
                                                            bean.setDrawableResId(R.drawable.storedetail_info_icon_freewifi);
                                                        } else if (string.equals("4")) {
                                                            bean.setDrawableResId(R.drawable.storedetail_info_icon_kids);
                                                        } else if (string.equals("5")) {
                                                            bean.setDrawableResId(R.drawable.storedetail_info_icon_group);
                                                        } else if (string.equals("6")) {
                                                            bean.setDrawableResId(R.drawable.storedetail_info_icon_takeout);
                                                        } else if (string.equals("7")) {
                                                            bean.setDrawableResId(R.drawable.storedetail_info_icon_pet);
                                                        }else{

                                                        }
                                                        mStoreTags.add(bean);
                                                        break;

                                                    }
                                                }
                                            }
                                        }


                                    }
                                    updateStoreTagView();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                ToastUtil.showgravity(StoreDetailActivity.this,getResources().getString(R.string.load_restaurant_service_fail));
                            }
                        });

//                        imgUrl = data.getString("imgUrl");
//                        Glide.with(StoreDetailActivity.this)
//                                .load(imgUrl)
//                                .centerCrop()
//                                .skipMemoryCache(true)
//                                .error(R.drawable.home_blank_pic)
//                                .into(card_img);
                        tel = data.getString("tel");
                        tvStorePhone.setText(tel);
                        Log.d(TAG, "tel: "+tel);
                        latitude = data.getDouble("latitude");
                        longitude = data.getDouble("longitude");
                        if (distance == null) {
                            String distance = data.getString("distance");
                            double v1 = Double.parseDouble(distance);
                            if (v1 >= 1000) {
                                double v = v1 / 1000;
                                String s = DateUtil.roundByScale(v, 1);
                                tvStoreDistance.setText(s + getResources().getString(R.string.km_away));
                            } else {
                                String of = String.valueOf(distance);
                                String[] split = of.split("\\.");
                                tvStoreDistance.setText(split[0] + getResources().getString(R.string.m_away));
                            }
//
//                            int operatingStatus = data.getInt("operatingStatus");
//
//                            if (operatingStatus == 1) {
////                                tvOpenStatus.setBackgroundResource(R.drawable.home_shape_soon);
//                                tvOpenStatus.setText(R.string.soon);
//                            } else if (operatingStatus == 2) {
////                                tvOpenStatus.setBackgroundResource(R.drawable.home_shape_close);
//                                tvOpenStatus.setText(R.string.closs_wei);
//                            } else if (operatingStatus == 0) {
////                                tvOpenStatus.setBackgroundResource(R.drawable.home_shape_open);
//                                tvOpenStatus.setText(R.string.open);
//                            }
//
                        } else {
                            double v1 = Double.parseDouble(distance);
                            if (v1 >= 1000) {
                                double v = v1 / 1000;
                                String s = DateUtil.roundByScale(v, 1);
                                tvStoreDistance.setText(s + getResources().getString(R.string.km));
                            } else {
                                String of = String.valueOf(distance);
                                String[] split = of.split("\\.");
                                tvStoreDistance.setText(split[0] + getResources().getString(R.string.m));
                            }
                        }
//                        if (distances != null) {
//                            double v1 = Double.parseDouble(distances);
//                            if (v1 >= 1000) {
//                                double v = v1 / 1000;
//                                String s = DateUtil.roundByScale(v, 1);
//                                tvStoreDistance.setText(s + getResources().getString(R.string.km));
//                            } else {
//                                String of = String.valueOf(distance);
//                                String[] split = of.split("\\.");
//                                tvStoreDistance.setText(split[0] + getResources().getString(R.string.m));
//                            }
//                        }

                        String s_xing = DateUtil.formateRate(String.valueOf(grade));
                        String substring = String.valueOf(grade).substring(0, 1);
                        mData.setSubstring(substring);
                        if (substring.equals("1")) {
                            cardItemImg0.setBackgroundResource(R.drawable.icon_star_10);
                            if (s_xing.equals("1")) {
                                cardItemImg1.setBackgroundResource(R.drawable.icon_star_0);
                            } else if (s_xing.equals("2")) {
                                cardItemImg1.setBackgroundResource(R.drawable.icon_star_0);
                            } else if (s_xing.equals("3")) {
                                cardItemImg1.setBackgroundResource(R.drawable.icon_star_0);
                            } else if (s_xing.equals("4")) {
                                cardItemImg1.setBackgroundResource(R.drawable.icon_star_0);
                            } else if (s_xing.equals("5")) {
                                cardItemImg1.setBackgroundResource(R.drawable.icon_star_5);
                            } else if (s_xing.equals("6")) {
                                cardItemImg1.setBackgroundResource(R.drawable.icon_star_10);
                            } else if (s_xing.equals("7")) {
                                cardItemImg1.setBackgroundResource(R.drawable.icon_star_10);
                            } else if (s_xing.equals("8")) {
                                cardItemImg1.setBackgroundResource(R.drawable.icon_star_10);
                            } else if (s_xing.equals("9")) {
                                cardItemImg1.setBackgroundResource(R.drawable.icon_star_10);
                            } else {
                                cardItemImg1.setBackgroundResource(R.drawable.icon_star_0);
                            }

                            cardItemImg2.setBackgroundResource(R.drawable.icon_star_0);
                            cardItemImg3.setBackgroundResource(R.drawable.icon_star_0);
                            cardItemImg4.setBackgroundResource(R.drawable.icon_star_0);
                        } else if (substring.equals("2")) {
                            cardItemImg0.setBackgroundResource(R.drawable.icon_star_10);
                            cardItemImg1.setBackgroundResource(R.drawable.icon_star_10);
                            if (s_xing.equals("1")) {
                                cardItemImg2.setBackgroundResource(R.drawable.icon_star_0);
                            } else if (s_xing.equals("2")) {
                                cardItemImg2.setBackgroundResource(R.drawable.icon_star_0);
                            } else if (s_xing.equals("3")) {
                                cardItemImg2.setBackgroundResource(R.drawable.icon_star_0);
                            } else if (s_xing.equals("4")) {
                                cardItemImg2.setBackgroundResource(R.drawable.icon_star_0);
                            } else if (s_xing.equals("5")) {
                                cardItemImg2.setBackgroundResource(R.drawable.icon_star_5);
                            } else if (s_xing.equals("6")) {
                                cardItemImg2.setBackgroundResource(R.drawable.icon_star_10);
                            } else if (s_xing.equals("7")) {
                                cardItemImg2.setBackgroundResource(R.drawable.icon_star_10);
                            } else if (s_xing.equals("8")) {
                                cardItemImg2.setBackgroundResource(R.drawable.icon_star_10);
                            } else if (s_xing.equals("9")) {
                                cardItemImg2.setBackgroundResource(R.drawable.icon_star_10);
                            } else {
                                cardItemImg2.setBackgroundResource(R.drawable.icon_star_0);
                            }

                            cardItemImg3.setBackgroundResource(R.drawable.icon_star_0);
                            cardItemImg4.setBackgroundResource(R.drawable.icon_star_0);
                        } else if (substring.equals("3")) {
                            cardItemImg0.setBackgroundResource(R.drawable.icon_star_10);
                            cardItemImg1.setBackgroundResource(R.drawable.icon_star_10);
                            cardItemImg2.setBackgroundResource(R.drawable.icon_star_10);
                            if (s_xing.equals("1")) {
                                cardItemImg3.setBackgroundResource(R.drawable.icon_star_0);
                            } else if (s_xing.equals("2")) {
                                cardItemImg3.setBackgroundResource(R.drawable.icon_star_0);
                            } else if (s_xing.equals("3")) {
                                cardItemImg3.setBackgroundResource(R.drawable.icon_star_0);
                            } else if (s_xing.equals("4")) {
                                cardItemImg3.setBackgroundResource(R.drawable.icon_star_0);
                            } else if (s_xing.equals("5")) {
                                cardItemImg3.setBackgroundResource(R.drawable.icon_star_5);
                            } else if (s_xing.equals("6")) {
                                cardItemImg3.setBackgroundResource(R.drawable.icon_star_10);
                            } else if (s_xing.equals("7")) {
                                cardItemImg3.setBackgroundResource(R.drawable.icon_star_10);
                            } else if (s_xing.equals("8")) {
                                cardItemImg3.setBackgroundResource(R.drawable.icon_star_10);
                            } else if (s_xing.equals("9")) {
                                cardItemImg3.setBackgroundResource(R.drawable.icon_star_10);
                            } else {
                                cardItemImg3.setBackgroundResource(R.drawable.icon_star_0);
                            }

                            cardItemImg4.setBackgroundResource(R.drawable.icon_star_0);
                        } else if (substring.equals("4")) {
                            cardItemImg0.setBackgroundResource(R.drawable.icon_star_10);
                            cardItemImg1.setBackgroundResource(R.drawable.icon_star_10);
                            cardItemImg2.setBackgroundResource(R.drawable.icon_star_10);
                            cardItemImg3.setBackgroundResource(R.drawable.icon_star_10);
                            if (s_xing.equals("1")) {
                                cardItemImg4.setBackgroundResource(R.drawable.icon_star_0);
                            } else if (s_xing.equals("2")) {
                                cardItemImg4.setBackgroundResource(R.drawable.icon_star_0);
                            } else if (s_xing.equals("3")) {
                                cardItemImg4.setBackgroundResource(R.drawable.icon_star_0);
                            } else if (s_xing.equals("4")) {
                                cardItemImg4.setBackgroundResource(R.drawable.icon_star_0);
                            } else if (s_xing.equals("5")) {
                                cardItemImg4.setBackgroundResource(R.drawable.icon_star_5);
                            } else if (s_xing.equals("6")) {
                                cardItemImg4.setBackgroundResource(R.drawable.icon_star_10);
                            } else if (s_xing.equals("7")) {
                                cardItemImg4.setBackgroundResource(R.drawable.icon_star_10);
                            } else if (s_xing.equals("8")) {
                                cardItemImg4.setBackgroundResource(R.drawable.icon_star_10);
                            } else if (s_xing.equals("9")) {
                                cardItemImg4.setBackgroundResource(R.drawable.icon_star_10);
                            } else {
                                cardItemImg4.setBackgroundResource(R.drawable.icon_star_0);
                            }

                        } else if (substring.equals("5")) {
                            cardItemImg0.setBackgroundResource(R.drawable.icon_star_10);
                            cardItemImg1.setBackgroundResource(R.drawable.icon_star_10);
                            cardItemImg2.setBackgroundResource(R.drawable.icon_star_10);
                            cardItemImg3.setBackgroundResource(R.drawable.icon_star_10);
                            cardItemImg4.setBackgroundResource(R.drawable.icon_star_10);
                        } else {
                            cardItemImg0.setBackgroundResource(R.drawable.icon_star_0);
                            cardItemImg1.setBackgroundResource(R.drawable.icon_star_0);
                            cardItemImg2.setBackgroundResource(R.drawable.icon_star_0);
                            cardItemImg3.setBackgroundResource(R.drawable.icon_star_0);
                            cardItemImg4.setBackgroundResource(R.drawable.icon_star_0);
                        }
                        getComment();
//                        if (Toplist.size() != 0) {
//
//                            //创建LinearLayoutManager
//                            LinearLayoutManager manager = new LinearLayoutManager(StoreDetailActivity.this);
//                            //设置为横向滑动
//                            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
//                            //设置
//                            horr_list.setLayoutManager(manager);
//                            //实例化适配器
//                            MyReAdapter myAdapter = new MyReAdapter(Toplist, CardItemActivity.this);
//                            //设置适配器
//                            horr_list.setAdapter(myAdapter);
//
//
//                        }
//                        if (readlist.size() != 0) {
//                            gridview.setAdapter(new CardItemActivity.MyAdapter());
//                        }

                    } else if (jsonObject.getString("code").equals("5007")) {
                        SPUtil.remove(StoreDetailActivity.this, "Token");
                        SPUtil.remove(StoreDetailActivity.this, "photo1");
                        SPUtil.remove(StoreDetailActivity.this, "photo");
                        SPUtil.remove(StoreDetailActivity.this, "gen");
                        SPUtil.remove(StoreDetailActivity.this, "name_one");
                        SPUtil.put(StoreDetailActivity.this, "deng", false);
                        Intent intent3 = new Intent(StoreDetailActivity.this, LoginActivity.class);
                        startActivity(intent3);
                        finish();
                    } else {
                        //失败
                        //Toast.makeText(CardItemActivity.this, getResources().getString(R.string.fan), Toast.LENGTH_SHORT).show();
                        ToastUtil.showgravity(StoreDetailActivity.this, getResources().getString(R.string.fan));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                dissLoad();
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
                noNetworkView.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.GONE);
                mTabLayout.setVisibility(View.GONE);
                llStoreDetailHeader.setVisibility(View.GONE);
                storeCloseStatusTv.setVisibility(View.GONE);
            }
        });


    }

    //返回的时候获取数据
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (flag_sto){

            bundle = data.getExtras();
            int currentPosition = bundle.getInt(ImagePagerActivity.STATE_POSITION,0);
            //做相应的滚动
            rvStoreImages.scrollToPosition(currentPosition);
            //暂时延迟 Transition 的使用，直到我们确定了共享元素的确切大小和位置才使用
            //postponeEnterTransition后不要忘记调用startPostponedEnterTransition
            ActivityCompat.postponeEnterTransition(this);
            rvStoreImages.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    rvStoreImages.getViewTreeObserver().removeOnPreDrawListener(this);
                    // TODO: figure out why it is necessary to request layout here in order to get a smooth transition.
                    rvStoreImages.requestLayout();
                    //共享元素准备好后调用startPostponedEnterTransition来恢复过渡效果
                    ActivityCompat.startPostponedEnterTransition(StoreDetailActivity.this);
                    return true;
                }
            });
        }else {
            ActivityCompat.postponeEnterTransition(this);
            if(mDishReviewFrag!=null){
                mDishReviewFrag.onActivityReenterCallback(resultCode,data);
            }
        }
    }

    /**
     * 营业时间
     */
    class TmAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Tmlist.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(StoreDetailActivity.this, R.layout.tmlist_item, null);
            TextView tmlist_item_qi = view.findViewById(R.id.tmlist_item_qi);
            TextView tmlist_item_time = view.findViewById(R.id.tmlist_item_time);
            TextView tmlist_item_tim = view.findViewById(R.id.tmlist_item_tim);
            TextView tmlist_item_l = view.findViewById(R.id.tmlist_item_l);
            int week = Tmlist.get(position).getWeek();
            switch (week) {
                case 1:
                    tmlist_item_qi.setText(getString(R.string.mon));
                    break;
                case 2:
                    tmlist_item_qi.setText(getString(R.string.tues));
                    break;
                case 3:
                    tmlist_item_qi.setText(getString(R.string.wednes));
                    break;
                case 4:
                    tmlist_item_qi.setText(getString(R.string.thurs));
                    break;
                case 5:
                    tmlist_item_qi.setText(getString(R.string.fri));
                    break;
                case 6:
                    tmlist_item_qi.setText(getString(R.string.satur));
                    break;
                case 7:
                    tmlist_item_qi.setText(getString(R.string.sun));
                    break;
            }
            int closeTmlist = -1;
            //第一次开关门时间
            try {
                closeTmlist = Tmlist.get(position).getCloseTmFrist();
            } catch (Exception e) {
                closeTmlist = -1;
            }
            long openingTmlist = -1;
            try {
                openingTmlist = Tmlist.get(position).getOpeningTmFrist();
            } catch (Exception e) {
                openingTmlist = -1;
            }
            int tm = 0;
            if (closeTmlist != -1 && openingTmlist != -1) {
                String closeTmText = TimeUtil.timeUTCdate(closeTmlist + timeLag);
                String openingTmText = TimeUtil.timeUTCdate(openingTmlist + timeLag);
                String[] splitcloseTm = closeTmText.split(" ");
                String[] splitopeningTm = openingTmText.split(" ");
                String s = splitcloseTm[1];
                String s1 = splitopeningTm[1];
                tmlist_item_time.setText(s1 + "-" + s);
            } else {
                tmlist_item_l.setVisibility(View.GONE);
                tm = 1;
            }
            /**
             * 第二次关门时间
             * */
            long closeTmSecondlist = -1;
            try {
                closeTmSecondlist = Tmlist.get(position).getCloseTmSecond();
            } catch (Exception e) {
                closeTmSecondlist = -1;
            }
            long openingTmSecondlist = -1;
            try {
                openingTmSecondlist = Tmlist.get(position).getOpeningTmSecond();
            } catch (Exception e) {
                openingTmSecondlist = -1;
            }
            if (closeTmSecondlist != -1 && openingTmSecondlist != -1) {
                Log.d("sdfdsfd", closeTmSecondlist + "==" + openingTmSecondlist);
                String closeTmSecondtimedate = TimeUtil.timeUTCdate(closeTmSecondlist + timeLag);
                String openingTmSecondtimedate = TimeUtil.timeUTCdate(openingTmSecondlist + timeLag);
                String[] splitclose = closeTmSecondtimedate.split(" ");
                String[] splitopening = openingTmSecondtimedate.split(" ");
                String s = splitclose[1];
                String s1 = splitopening[1];
                tmlist_item_time.setVisibility(View.VISIBLE);
                tmlist_item_tim.setText(s1 + "-" + s);
            } else {
                tmlist_item_tim.setVisibility(View.GONE);
                tmlist_item_l.setVisibility(View.GONE);
                if (tm == 1) {
                    tmlist_item_time.setText(getResources().getString(R.string.closs_wei));
                    tmlist_item_qi.setTextColor(getResources().getColor(R.color.main_gold));
                    tmlist_item_time.setTextColor(getResources().getColor(R.color.main_gold));
                }
            }
            tm = 0;

            return view;
        }
    }
    /**
     * 餐廳介紹
     * */
    private void updateStoreImages(){
        if(images.size()>0){
            rvStoreImages.setVisibility(View.VISIBLE);
            llStoreDescLabel.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(describe)){
                tvStoreDesc.setVisibility(View.VISIBLE);
            }else {
                tvStoreDesc.setVisibility(View.GONE);
            }
            //创建LinearLayoutManager
            LinearLayoutManager manager = new LinearLayoutManager(StoreDetailActivity.this);
            //设置为横向滑动
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            //设置
            rvStoreImages.setLayoutManager(manager);
            //实例化适配器
            StoreImageAdapter myAdapter = new StoreImageAdapter(images, StoreDetailActivity.this);
            //设置适配器
            rvStoreImages.setAdapter(myAdapter);

            myAdapter.setOnItemClickListener(new StoreImageAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int pos) {
                    ImagePagerActivity.startImagePage(StoreDetailActivity.this,
                            images,pos,rvStoreImages.getLayoutManager().findViewByPosition(pos));
                    flag_sto=true;
                }
            });

            //设置转场动画的共享元素，因为跳转和返回都会调用，需要判断bundle是否为空
            setExitSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    if (bundle!=null){
                        int index = bundle.getInt(ImagePagerActivity.STATE_POSITION,0);
                        sharedElements.clear();
                        sharedElements.put("img", rvStoreImages.getLayoutManager().findViewByPosition(index));
                        bundle=null;

                    }
                }
            });


        }else{
            rvStoreImages.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(describe)){
                llStoreDescLabel.setVisibility(View.VISIBLE);
                tvStoreDesc.setVisibility(View.VISIBLE);
            }else {
                llStoreDescLabel.setVisibility(View.GONE);
                tvStoreDesc.setVisibility(View.GONE);
            }
        }
    }

    private void updateOpenTimeView(){
        if(Tmlist.size()>0){
            car_item_list.setVisibility(View.VISIBLE);
            llOpenTimeLabel.setVisibility(View.VISIBLE);
            car_item_list.setAdapter(new TmAdapter());
        }else{
            car_item_list.setVisibility(View.GONE);
            llOpenTimeLabel.setVisibility(View.GONE);
        }
    }

    private void updateStoreTagView() {
        Log.d(TAG, "updateStoreTagView: "+mStoreTags.size());
        tagContainer.removeAllViews();
        if (mStoreTags.size()>0){
            llTagLabel.setVisibility(View.VISIBLE);
            tagContainer.setVisibility(View.VISIBLE);
            showDetailInfoDivider.setVisibility(View.VISIBLE);
            tagContainer.setHorizontalInterval(ScreenUtils.dp2px(instance,8));
            tagContainer.setVerticalInterval(ScreenUtils.dp2px(instance,8));
            for (int i = 0; i < mStoreTags.size(); i++) {
                StoreTagBean bean = mStoreTags.get(i);
                TagView view = new TagView(this, bean.getName());
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lp);
                Log.d(TAG, "updateStoreTagView: "+bean.getName()+","+bean.getDrawableResId());
                view.setBorderColor(getResources().getColor(R.color.lo));
                view.setBorderWidth(ScreenUtils.dp2px(instance,0.5f));
                view.setRadius(ScreenUtils.dp2px(StoreDetailActivity.this,15));
                view.setTextColor(getResources().getColor(R.color.card_cc));
                int padding=ScreenUtils.dp2px(instance,16);
                view.setHorizontalPadding(padding);
                view.setTextSize(ScreenUtils.dp2px(instance,12));
                view.setDecorateIcon(getResources().getDrawable(bean.getDrawableResId()));
                tagContainer.addView(view);
            }
        }else{
            llTagLabel.setVisibility(View.GONE);
            tagContainer.setVisibility(View.GONE);
            showDetailInfoDivider.setVisibility(View.GONE);
        }

    }


    private LinearLayout map_map_ly, pop_map_gd_ly, pop_map_bd_ly, pop_map_blank, pop_map_google_ly;
    private TextView pop_map_cancle;
    private String tel;

    private void popwindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_ditu, null);
        pop_map_gd_ly = (LinearLayout) view.findViewById(R.id.pop_map_gd_ly);
        pop_map_bd_ly = (LinearLayout) view.findViewById(R.id.pop_map_bd_ly);
        pop_map_blank = (LinearLayout) view.findViewById(R.id.pop_map_blank);
        pop_map_cancle = (TextView) view.findViewById(R.id.pop_map_cancle);
        pop_map_google_ly = (LinearLayout) view.findViewById(R.id.pop_map_google_ly);
        if (!isGaoDe) {
            pop_map_gd_ly.setVisibility(View.GONE);
        }
        if (!isBAidu) {
            pop_map_bd_ly.setVisibility(View.GONE);
        }
        pop_map_gd_ly.setOnClickListener(this);
        pop_map_bd_ly.setOnClickListener(this);
        pop_map_google_ly.setOnClickListener(this);
        pop_map_blank.setOnClickListener(this);
        pop_map_cancle.setOnClickListener(this);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
//        popupWindow.setTouchable(true);
        //add this code to response the back key
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        homeListview.setFocusable(false);
        popupWindow.showAtLocation(tvStoreAddress, Gravity.BOTTOM, 0, 0);
    }

    private PopupWindow popupWindow;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_map_gd_ly:
                /*Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("amapuri://route/plan/?slat="
                        + lastidu + "&slon=" + longtidu
                        + "&dlat=" + latitude + "&dlon=" + longitude +"&dev=0&t=4")); //&dname="+name+"*/
                try {
                    Intent intent = new Intent(
                            "android.intent.action.VIEW",
                            Uri.parse(
                                    "androidamap://route?sourceApplication=Whoot!" + "&dlat=" + latitude//终点的经度
                                            + "&dlon=" + longitude//终点的纬度
                                            + "&dname=" + name
                                            + "&dev=0" + "&t=1"));

                    intent.setPackage(AMAP_PACKAGENAME);
                    startActivity(intent);

                    popupWindow.dismiss();
                } catch (Exception e) {
                   // ToastUtil.showgravity(StoreDetailActivity.this, getResources().getString(R.string.map_enable));
                }
                SPUtil.put(StoreDetailActivity.this,"flag_map",3);
                break;
            case R.id.pop_map_bd_ly:
                try {
                    Intent intent1 = new Intent("android.intent.action.VIEW", Uri.parse("baidumap://map/walknavi?origin="
                            + lastidu + "," + longtidu
                            + "&destination=" + latitude + "," + longitude));
                    intent1.setPackage(BAIDUMAP_PACKAGENAME);
                    startActivity(intent1);
                    popupWindow.dismiss();
                } catch (Exception e) {
                  //  ToastUtil.showgravity(StoreDetailActivity.this, getResources().getString(R.string.map_enable));
                }
                SPUtil.put(StoreDetailActivity.this,"flag_map",1);
                break;
            case R.id.pop_map_blank:
                popupWindow.dismiss();
                break;
            case R.id.pop_map_cancle:
                popupWindow.dismiss();
                break;
            case R.id.pop_map_google_ly:

                try {
                    if (isGOOGLE) {
                        Intent i = new Intent(
                                Intent.ACTION_VIEW,
                                Uri
                                        .parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=" + latitude + "," + longitude + "(" + name + ")"));
                        //    .parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=31.1198723,121.1099877(上海青浦大街100号)"));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        i.setClassName("com.google.android.apps.maps",
                                "com.google.android.maps.MapsActivity");
                        startActivity(i);
                        popupWindow.dismiss();
                    } else {
                        Intent i = new Intent(
                                Intent.ACTION_VIEW,
                                Uri
                                        .parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=" + latitude + "," + longitude));
                        startActivity(i);
                        popupWindow.dismiss();
                    }
                } catch (Exception e) {
                    ToastUtil.showgravity(StoreDetailActivity.this, getResources().getString(R.string.map_enable));
                }
                SPUtil.put(StoreDetailActivity.this,"flag_map",2);
                break;

        }
    }

    private double latitude;
    private double longitude;
    private String name;
    private String lastidu;
    private String longtidu;

    //拨打电话
    public void callPhone(String tel) {
        final CustomDialog.Builder builder = new CustomDialog.Builder(StoreDetailActivity.this);
        builder.setTitle(String.format(getString(R.string.md_title)));
        builder.setMessage(String.format(getString(R.string.call)) +"\n"+ tel);
        builder.setPositiveButton(R.string.cancel_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(R.string.determine, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + tel));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.create().show();
    }

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0x3304;

    public void andro() {
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        for (int i = 0; i < 10; i++) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Check out: \n" +
                    name + "\n" + Html.fromHtml(UrlUtil.Share + "shopId=" + id) + "");
            intent.setType("text/plain");
            //intent.setType("image/jpeg");
            //intent.putExtra(Intent.EXTRA_TITLE, "标题");
            //intent.putExtra(Intent.EXTRA_SUBJECT, "内容");
            if (i == 0) {
                // intent.setPackage("com.whatsapp");
                intent.setClassName("com.whatsapp", "com.whatsapp.ContactPicker");
            } else if (i == 1) {
                //intent.setPackage("com.instagram.android");
                intent.setClassName("com.instagram.android", "com.instagram.direct.share.handler.DirectShareHandlerActivity");
            } else if (i == 2) {
                intent.setPackage("com.facebook.katana");
                //intent.setClassName("com.samsung.android.voc","com.samsung.android.community.ui.WebFacebookLaunchActivity");
            } else if (i == 3) {
                intent.setPackage("com.facebook.orca");
            } else if (i == 4) {
                //intent.setPackage("jp.naver.line.android");
                intent.setClassName("jp.naver.line.android", "jp.naver.line.android.activity.selectchat.SelectChatActivityLaunchActivity");
            } else if (i == 5) {
                intent.setPackage("com.tencent.mm");
            } else if (i == 6) {
                // intent.setPackage("com.google.android.gm");
                intent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
            } else if (i == 7) {
                intent.setPackage("com.samsung.android.email.provider");
            } else if (i == 8) {
                intent.setPackage("com.samsung.android.messaging");
            } else if (i == 9) {
                intent.setPackage("com.google.android.gm");
              /* intent.setClassName("com.android.mms","com.android.mms.ui.ComposeMessageActivity");
               intent.setComponent(new ComponentName("com.android.mms","com.android.mms.ui.ComposeMessageActivity"));
               intent.setAction("android.intent.action.SEND_MULTIPLE");*/
            }
            targetedShareIntents.add(intent);
        }
        Intent chooserIntent = null;
        try {
            chooserIntent = Intent.createChooser(Intent.getIntent(""), "Select app to share");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (chooserIntent == null) {
            return;
        }
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));

        // A Parcelable[] of Intent or LabeledIntent objects as set with
        // putExtra(String, Parcelable[]) of additional activities to place
        // a the front of the list of choices, when shown to the user with a
        // ACTION_CHOOSER.
        try {
            startActivity(chooserIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            // Toast.makeText(this, "Can't find share component to share", Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick({R.id.ll_address, R.id.ll_phone, R.id.ll_detail_info,R.id.title_carditem_fl,R.id.title_carditem_img,R.id.title_carditem_shou,R.id.title_carditem_no})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_carditem_img:
                andro();
                break;
            case R.id.title_carditem_shou: //收藏

                String token = (String) SPUtil.get(StoreDetailActivity.this, "Token", "");
                if (token.length() > 0) {
                    FormBody body = new FormBody.Builder()
                            .add("locationId", "1")
                            .add("shopId", shop_id)
                            .build();
                    Http.OkHttpPost(StoreDetailActivity.this, urlUtil + UrlUtil.favshop, body, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            Log.d("favshop", result + urlUtil + UrlUtil.favshop);
                            try {
                                JSONObject jsonObject1 = new JSONObject(result);
                                if (jsonObject1.getString("code").equals("0")) {
                                    int fav_ShopId = jsonObject1.getInt("data");
                                    favShopId = fav_ShopId;
                                    titleCarditemNo.setVisibility(View.VISIBLE);
                                    titleCarditemShou.setVisibility(View.GONE);
                                    ToastUtil.showgravity(StoreDetailActivity.this, getResources().getString(R.string.shouc));
                                    if (favour==1){
                                        SPUtil.put(StoreDetailActivity.this,"favour",2);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                } else {
                    //ToastUtil.showgravity(StoreDetailActivity.this, getResources().getString(R.string.login_inj));
                    Intent intent = new Intent(StoreDetailActivity.this, LoginMeActivity.class);
                    intent.putExtra("Login_flag",1);
                    startActivity(intent);

                }

                break;
            case R.id.title_carditem_no:  //取消收藏
                Http.OkHttpDelete(StoreDetailActivity.this, urlUtil + UrlUtil.delete_favshop + "?id=" + favShopId, new Http.OnDataFinish() {
                    @Override
                    public void OnSuccess(String result) {
                        Log.d("delete_favshop", result + urlUtil + UrlUtil.delete_favshop + "?id=" + favShopId);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("code").equals("0")) {

                                titleCarditemNo.setVisibility(View.GONE);
                                titleCarditemShou.setVisibility(View.VISIBLE);
                                if (favour==1){
                                    SPUtil.put(StoreDetailActivity.this,"favour",1);
                                }
                            }
                            ToastUtil.showgravity(StoreDetailActivity.this, getResources().getString(R.string.cancel_shouc));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                break;
            case R.id.title_carditem_fl:
                finish();
                break;
            case R.id.ll_address:
                Intent toMap=new Intent(this,MainActivity.class);
                toMap.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                toMap.putExtra(Constants.EXTRA_KEY_START_MAIN_ACTIVITY, Constants.ACTION_START_MAIN_ACTIVITY_TO_MAP);
                toMap.putExtra("shopId",mData.getShopId());
                startActivity(toMap);
//                int flag_map = (int) SPUtil.get(StoreDetailActivity.this, "flag_map", 0);
//                if (flag_map==1){
//                    try {
//                        Intent intent1 = new Intent("android.intent.action.VIEW", Uri.parse("baidumap://map/walknavi?origin="
//                                + lastidu + "," + longtidu
//                                + "&destination=" + latitude + "," + longitude));
//                        intent1.setPackage(BAIDUMAP_PACKAGENAME);
//                        startActivity(intent1);
//
//                    } catch (Exception e) {
//                       // ToastUtil.showgravity(StoreDetailActivity.this, getResources().getString(R.string.map_enable));
//                    }
//                }else if (flag_map==2){
//                    try {
//                        if (isGOOGLE) {
//                            Intent i = new Intent(
//                                    Intent.ACTION_VIEW,
//                                    Uri
//                                            .parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=" + latitude + "," + longitude + "(" + name + ")"));
//                            //    .parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=31.1198723,121.1099877(上海青浦大街100号)"));
//                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                    & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                            i.setClassName("com.google.android.apps.maps",
//                                    "com.google.android.maps.MapsActivity");
//                            startActivity(i);
//
//                        } else {
//                            Intent i = new Intent(
//                                    Intent.ACTION_VIEW,
//                                    Uri
//                                            .parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=" + latitude + "," + longitude));
//                            startActivity(i);
//                        }
//                    } catch (Exception e) {
//                        ToastUtil.showgravity(StoreDetailActivity.this, getResources().getString(R.string.map_enable));
//                    }
//                }else if (flag_map==3){
//                    try {
//                        Intent intent = new Intent(
//                                "android.intent.action.VIEW",
//                                Uri.parse(
//                                        "androidamap://route?sourceApplication=Whoot!" + "&dlat=" + latitude//终点的经度
//                                                + "&dlon=" + longitude//终点的纬度
//                                                + "&dname=" + name
//                                                + "&dev=0" + "&t=1"));
//
//                        intent.setPackage(AMAP_PACKAGENAME);
//                        startActivity(intent);
//                    } catch (Exception e) {
//                      //  ToastUtil.showgravity(StoreDetailActivity.this, getResources().getString(R.string.map_enable));
//                    }
//                }else {
//                    popwindow();
//                }


                break;
            case R.id.ll_phone:
                //申请权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    } else {
                        callPhone(tel);
                    }
                }
                break;
            case R.id.ll_detail_info:
                String tag=(String)llDetailInfo.getTag();
                if("expand".equals(tag)){
                    llDetailInfo.setTag("collapse");
                    detailInfoLayout.setVisibility(View.VISIBLE);
                    ivExpandCollapse.setImageResource(R.drawable.storedetail_arrow_up);
                }else{
                    llDetailInfo.setTag("expand");
                    detailInfoLayout.setVisibility(View.GONE);
                    ivExpandCollapse.setImageResource(R.drawable.arrow_down);
                }
                break;
        }
    }

}
