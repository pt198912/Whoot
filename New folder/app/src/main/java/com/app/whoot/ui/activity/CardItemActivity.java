package com.app.whoot.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.adapter.MyReAdapter;
import com.app.whoot.adapter.ShopsLVAdapter;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.AppInfoVo;
import com.app.whoot.bean.CardBean;
import com.app.whoot.bean.CardItemBean;
import com.app.whoot.bean.CardItemNoBean;
import com.app.whoot.bean.CardItemTopBean;
import com.app.whoot.bean.RewardItemBean;
import com.app.whoot.bean.ShopTagsBean;
import com.app.whoot.bean.WorkingTmListBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.ui.view.custom.MyGridView;
import com.app.whoot.ui.view.custom.MyListView;
import com.app.whoot.ui.view.custom.StarRatingView;
import com.app.whoot.ui.view.dialog.CustomDialog;
import com.app.whoot.ui.view.dialog.ExchangeDialog;
import com.app.whoot.ui.view.dialog.GiftReOneDialog;
import com.app.whoot.ui.view.dialog.GiftReTweDialog;
import com.app.whoot.ui.view.dialog.GiftRewardDialog;
import com.app.whoot.ui.view.dialog.GiftThreeReDialog;
import com.app.whoot.ui.view.popup.SharePopupWindow;
import com.app.whoot.util.AppUtil;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.bumptech.glide.Glide;
import com.donkingliang.banner.CustomBanner;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import okhttp3.FormBody;


/**
 * Created by Sunrise on 4/23/2018.
 * 门店详情
 */

public class CardItemActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_carditem_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_carditem_title)
    TextView titleBackTitle;
    @BindView(R.id.title_carditem_no)
    ImageView title_back_no;
    @BindView(R.id.title_carditem_shou)
    ImageView title_back_shou;
    @BindView(R.id.title_carditem_img)
    ImageView title_back_img;
    @BindView(R.id.car_item_name)
    TextView carItemName;
    @BindView(R.id.car_item_score)
    TextView carItemScore;
    @BindView(R.id.car_item_score_one)
    TextView carItemScoreOne;
    @BindView(R.id.car_item_distance)
    TextView carItemDistance;
    @BindView(R.id.car_item_dish)
    TextView carItemDish;
    @BindView(R.id.car_item_money)
    TextView carItemMoney;
    @BindView(R.id.gridview)
    MyGridView gridview;
    @BindView(R.id.card_item_phone)
    ImageView cardItemPhone;
    @BindView(R.id.card_more_item)
    LinearLayout cardMoreItem;
    @BindView(R.id.card_item_add)
    TextView cardItemAdd;
    @BindView(R.id.card_item_soon)
    TextView cardItemSoon;
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
    @BindView(R.id.home_listview)
    MyListView homeListview;
    @BindView(R.id.card_no_text)
    TextView cardNoText;
    @BindView(R.id.card_no_red)
    TextView cardNoRed;
    @BindView(R.id.card_item_a)
    LinearLayout card_item_a;
    @BindView(R.id.card_fou_lt)
    LinearLayout card_fou_lt;
    @BindView(R.id.img_to)
    ImageView img_to;
    @BindView(R.id.card_img)
    ImageView card_img;
    @BindView(R.id.card_time)
    TextView card_time;
    @BindView(R.id.card_vie)
    View card_vie;
    @BindView(R.id.card_vi)
    View card_vi;
    @BindView(R.id.card_ly)
    LinearLayout card_ly;
    @BindView(R.id.card_txt)
    TextView card_txt;
    @BindView(R.id.login_e_button)
    LoginButton loginButton;
    @BindView(R.id.card_time_cl)
    TextView card_time_cl;
    @BindView(R.id.card_time_l)
    TextView card_time_l;
    @BindView(R.id.card_item_arrow)
    LinearLayout card_item_arrow;
    @BindView(R.id.car_item_list)
    MyListView car_item_list;
    @BindView(R.id.card_qi)
    TextView card_qi;
    @BindView(R.id.card_item_arrow_img)
    ImageView card_item_arrow_img;
    @BindView(R.id.card_arrow_ig)
    ImageView card_arrow_ig;
    @BindView(R.id.crd_ly)
    LinearLayout crd_ly;
    @BindView(R.id.horr_list)
    RecyclerView horr_list;
    @BindView(R.id.card_banner)
    CustomBanner card_banner;
    @BindView(R.id.card_item_write)
    LinearLayout card_item_write;
    @BindView(R.id.card_info)
    MyGridView card_info;
    @BindView(R.id.card_info_card_txt)
    TextView card_info_card_txt;
    @BindView(R.id.card_info_vegen)
    ImageView card_info_vegen;
    @BindView(R.id.card_info_vegn_txt)
    TextView card_info_vegn_txt;
    @BindView(R.id.card_info_card)
    ImageView card_info_card;
    @BindView(R.id.card_item_nameEn)
    TextView card_item_nameEn;

    @BindView(R.id.card_item_view0)
    View card_item_view0;
    @BindView(R.id.card_item_view1)
    View card_item_view1;
    @BindView(R.id.card_item_info)
    LinearLayout card_item_info;
    @BindView(R.id.card_info_ig)
    ImageView card_info_ig;
    @BindView(R.id.card_item_info_img)
    ImageView card_item_info_img;
    @BindView(R.id.card_star)
    StarRatingView card_star;
    @BindView(R.id.card_info_lyy)
    LinearLayout card_info_lyy;
    @BindView(R.id.card_info_ly)
    LinearLayout card_info_ly;
    @BindView(R.id.card_info_vi)
    View card_info_vi;
    @BindView(R.id.car_msg_img_ly)
    LinearLayout car_msg_img_ly;
    @BindView(R.id.car_msg_mon_0)
    ImageView car_msg_mon_0;
    @BindView(R.id.car_msg_mon_1)
    ImageView car_msg_mon_1;
    @BindView(R.id.car_msg_mon_2)
    ImageView car_msg_mon_2;
    @BindView(R.id.car_msg_mon_3)
    ImageView car_msg_mon_3;
    @BindView(R.id.car_msg_mon_4)
    ImageView car_msg_mon_4;
    @BindView(R.id.card_tx_read)
    TextView card_tx_read;
    @BindView(R.id.card_describe)
    TextView card_describe;
    @BindView(R.id.card_item_miao)
    LinearLayout card_item_miao;
    @BindView(R.id.card_arrow_miao)
    ImageView card_arrow_miao;
    @BindView(R.id.card_item_arrow_miao)
    ImageView card_item_arrow_miao;
    @BindView(R.id.card_miao_view)
    View card_miao_view;
    @BindView(R.id.card_miao_ly)
    LinearLayout card_miao_ly;
    @BindView(R.id.layout_content)
    View contentLayout;
    @BindView(R.id.no_network_view)
    View noNetworkView;
    private ExchangeDialog dialog;
    private int i = 0;



    private LayoutInflater inflater;
    private int id;

    private List<CardItemBean> list = new ArrayList<CardItemBean>();
    private List<CardItemNoBean> Nolist = new ArrayList<CardItemNoBean>();
    private List<RewardItemBean> Toplist = new ArrayList<RewardItemBean>();
    private List<CardItemTopBean> readlist = new ArrayList<CardItemTopBean>();
    private List<WorkingTmListBean> Tmlist = new ArrayList<WorkingTmListBean>(); //营业时间

    private List<ShopTagsBean> sags = new ArrayList<ShopTagsBean>();

    private List<String> imglist = new ArrayList();

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    //高德地图应用包名
    public static final String AMAP_PACKAGENAME = "com.autonavi.minimap";
    //百度地图应用包名
    public static final String BAIDUMAP_PACKAGENAME = "com.baidu.BaiduMap";
    //google地图应用包名
    public static final String GOOGLE_PACKAGENAME = "com.google.android.apps.maps";

    private boolean isGaoDe, isBAidu, isGOOGLE;

    private LinearLayout map_map_ly, pop_map_gd_ly, pop_map_bd_ly, pop_map_blank, pop_map_google_ly;

    private TextView pop_map_cancle;
    private PopupWindow popupWindow;
    private ShopsLVAdapter shopsLVAdapter;
    private List<CardBean> itemlist = new ArrayList<CardBean>();

    private static final double mLatitude = 28.1903;  //起点纬度
    private static final double mLongitude = 113.031738;  //起点经度

    private static final double sLatitude = 28.187519;  //终点纬度
    private static final double sLongitude = 113.029713;  //终点经度
    private String tel;
    private double latitude;
    private double longitude;
    private String lastidu;
    private String longtidu;
    private ImageLoader imageLoader;
    private String distance;
    private int row = 1;
    private String url;
    private double grade;
    private String substring;
    private String pingfen = "0";
    private SharePopupWindow popup;
    private String imgUrl;
    private long closeTm;
    private long openingTm;
    private GiftRewardDialog browsDialog;
    private GiftReOneDialog OneDialog;   //过期
    private GiftReTweDialog TweDialog;   //已经领取
    private GiftThreeReDialog ThreeDialog;  //未领取
    private CallbackManager callbackManager;
    private String locationId = "";
    private String couponId = "";
    private String locationId1 = "undefined";
    private String couponId1 = "undefined";
    private String shopId1 = "undefined";
    private String url1 = "";
    private String name;
    private int currency1;
    private String price;
    private String category1;
    private int flags;
    private String giftSn;
    private String shopid;
    private String userId;
    private String giftUseId;
    private String type;
    private String giftId;
    private String expiredTm;
    private String token;
    private long closeTmSecond;
    private long openingTmSecond;
    private String urlUtil;
    private int phot = 0;
    private String shopGiftCfgId1;
    private String address;
    private String distances;
    private String name1;
    ArrayList<String> images = new ArrayList<>();
    private String tags;
    private boolean favShop;
    private int favShopId;
    private int fav = 0;
    private String[] split1;
    private int comment;
    private int priceLevel;
    private int favour;
    private String nameEn;
    private long timeLag;
    private int desc=0;
    private boolean textView;
    private int commentCount;
    private String shopName;
    private String cfgURL;
    public static CardItemActivity instance;

    @Override
    public int getLayoutId() {
        return R.layout.card_item;
    }

    @Override
    public void onCreateInit() {
        instance=this;
        String versionName = AppUtil.getVersionName(this);
        noNetworkView.findViewById(R.id.tv_retry_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading(getResources().getString(R.string.listview_loading));
                initData();
            }
        });
        loading(getResources().getString(R.string.listview_loading));
        Http.OkHttpGet(this, UrlUtil.Release, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray versions = data.getJSONArray("Versions");
                    String newestVersion = data.getString("NewestVersion");
                    boolean upgradeNew = data.getBoolean("UpgradeNew");
                    SPUtil.put(CardItemActivity.this,"newestVersion",newestVersion);
                    SPUtil.put(CardItemActivity.this,"upgradeNew",upgradeNew);
                    for (int i = 0; i < versions.length(); i++) {
                        JSONObject jsonObject1 = versions.getJSONObject(i);
                        String version = jsonObject1.getString("Version");
                        if (versionName.equals(version)) {
                            url = jsonObject1.getString("URL");
                            cfgURL = jsonObject1.getString("CfgURL");
                            SPUtil.put(CardItemActivity.this, "URL", url);
                            SPUtil.put(CardItemActivity.this,"cfgURL", cfgURL);
                            break;
                        }
                    }
                    //url = "http://172.16.47.75:8888/";
                    //url = "https://qc.whoot.com/";
                    //url="https://hk.whoot.com/";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

        String url2 = (String) SPUtil.get(this, "URL", "");
        if (url2.equals("")){
            SPUtil.put(CardItemActivity.this, "URL", "https://hk.whoot.com/");
        }
        /*try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray shopTags = jsonObject.getJSONArray("ShopTags");
            for (int s = 0; s < shopTags.length(); s++) {
                ShopTagsBean bean = GSonUtil.parseGson(shopTags.getString(s), ShopTagsBean.class);
                sags.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        urlUtil = (String) SPUtil.get(CardItemActivity.this, "URL", "");
        String timela = (String) SPUtil.get(CardItemActivity.this, "timeLag", "0");
        timeLag= Long.parseLong(timela);
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
        flags = intent.getIntExtra("flags", 0);
        comment = intent.getIntExtra("comment", 0);
        favour = intent.getIntExtra("favour", 0);
        String scheme = intent.getScheme();
        String dataString = intent.getDataString();
        distances = intent.getStringExtra("distances");

        if (distances != null) {
            double v1 = Double.parseDouble(distances);
            if (v1 >= 1000) {
                double v = v1 / 1000;
                String s = DateUtil.roundByScale(v, 1);
                carItemDistance.setText(s + getResources().getString(R.string.km));

            } else {
                String of = String.valueOf(distance);
                String[] split = of.split("\\.");
                carItemDistance.setText(split[0] + getResources().getString(R.string.m));
            }
        }

   /*     Uri uri = intent.getData();
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
            token = (String) SPUtil.get(CardItemActivity.this, "Token", "");
            String accToken = (String) SPUtil.get(CardItemActivity.this, "accToken", "");

            if (token.length() == 0 && accToken.length() == 0) {
                Intent intent1 = new Intent(CardItemActivity.this, LoginActivity.class);
                startActivity(intent1);
            } else {
                try{
                    if (couponId1.equals("undefined")) {

                    } else {
                        if (token.length() == 0) {
                            browsDialog = new GiftRewardDialog(CardItemActivity.this, R.style.box_dialog, onClickListener);
                            browsDialog.show();
                        }
                    }
                }catch (Exception e){}

            }
        }*/
        title_back_img.setVisibility(View.VISIBLE);
        titleBackTitle.setText(getResources().getString(R.string.store_w));
        isGaoDe = isApp(CardItemActivity.this, AMAP_PACKAGENAME);
        isBAidu = isApp(CardItemActivity.this, BAIDUMAP_PACKAGENAME);
        isGOOGLE = isApp(CardItemActivity.this, GOOGLE_PACKAGENAME);

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(CardItemActivity.this)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(configuration);
        imageLoader = ImageLoader.getInstance();

        cardItemAdd.setOnClickListener(this);
        inflater = LayoutInflater.from(this);
        id = intent.getIntExtra("id", 0);
        distance = intent.getStringExtra("distance");
        int OperatingStatus = intent.getIntExtra("closingSoon", 4);
        if (OperatingStatus == 1) {
            cardItemSoon.setBackgroundResource(R.drawable.home_shape_soon);
            cardItemSoon.setText(R.string.soon);
        } else if (OperatingStatus == 2) {
            cardItemSoon.setBackgroundResource(R.drawable.home_shape_close);
            cardItemSoon.setText(R.string.closs_wei);
        } else if (OperatingStatus == 0) {
            cardItemSoon.setBackgroundResource(R.drawable.home_shape_open);
            cardItemSoon.setText(R.string.open);
        }
        cardItemSoon.getBackground().mutate().setAlpha(200);
        Log.d("ooiu",id+"");
        try{
            if (id == 0) {
                if (!shopId1.equals("undefined")){
                    id = Integer.parseInt(shopId1);
                }
            }
        }catch (Exception e){}

        url = urlUtil + UrlUtil.comme + "?locationId=1" + "&shopId=" + id + "&start=0" + "&rows=" + 5;
        initData();
        initView();
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String commodityPic = readlist.get(position).getCommodityPic();  //产品图片
                String commodityName = readlist.get(position).getCommodityName(); //产品名字
                String detail = readlist.get(position).getDetail();            //介绍
                int couponId = readlist.get(position).getCouponId();   //优惠券类型
                int maxUseAmout = readlist.get(position).getMaxUseAmout(); //最大兑换
                int usedAmout = readlist.get(position).getUsedAmout();    //已兑换
                int costs = readlist.get(position).getCosts(); //数量
                int shopId = readlist.get(position).getShopId(); //门店id
                int commodityId = readlist.get(position).getCommodityId(); //商品id
                String commodityDescribe = readlist.get(position).getCommodityDescribe(); //商品描述
                SPUtil.put(CardItemActivity.this, "shopId_item", String.valueOf(shopId));
                SPUtil.put(CardItemActivity.this, "commodityId_item", String.valueOf(commodityId));
                SPUtil.put(CardItemActivity.this, "costs_item", String.valueOf(costs));
                SPUtil.put(CardItemActivity.this, "commodityPic_item", commodityPic);
                SPUtil.put(CardItemActivity.this, "commodityName_item", commodityName);
                SPUtil.put(CardItemActivity.this, "detail_item", detail);
                SPUtil.put(CardItemActivity.this, "couponId_item", String.valueOf(couponId)); //优惠卷类型
                SPUtil.put(CardItemActivity.this, "maxUseAmout_item", String.valueOf(maxUseAmout));
                SPUtil.put(CardItemActivity.this, "usedAmout_item", String.valueOf(usedAmout));
                SPUtil.put(CardItemActivity.this, "commodityDescribe", commodityDescribe); //商品描述
                // dialog = new ExchangeDialog(CardItemActivity.this, R.style.box_dialog, onClickListener);
                // dialog.show();
                Intent intent1 = new Intent(CardItemActivity.this, CoupDetaiActivity.class);
                intent1.putStringArrayListExtra("image",images);
                startActivity(intent1);

            }
        });

        getComment();

        init();

        card_star.setOnRateChangeListener(new StarRatingView.OnRateChangeListener() {
            @Override
            public void onRateChange(int rate) {
                if (rate == 1) {
                    i = rate;
                } else {
                    i = rate / 2;
                }
                Log.d("xingxing", String.valueOf(rate));
                CardBean.CouponHistoryBean couponHistory = itemlist.get(0).getCouponHistory();
                Intent intent1 = new Intent(CardItemActivity.this, WriteActivity.class);
                intent1.putExtra("shopid", String.valueOf(couponHistory.getShopIdX()));
                intent1.putExtra("commodityId", String.valueOf(couponHistory.getCommodityIdX()));
                intent1.putExtra("shopName", couponHistory.getShopNameX());
                intent1.putExtra("commodityName", couponHistory.getCommodityNameX());
                intent1.putExtra("couponHistoryId", String.valueOf(couponHistory.getIdX()));
                intent1.putExtra("couponId", String.valueOf(couponHistory.getCouponLevel()));
                intent1.putExtra("useCount", String.valueOf(couponHistory.getUseCount()));
                intent1.putExtra("xing", rate);
                startActivity(intent1);
                finish();
            }
        });
    }

    //设置普通指示器
    private void setBean(final ArrayList<String> beans) {
        card_banner.setPages(new CustomBanner.ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String entity) {
                Glide.with(context).load(entity).into((ImageView) view);
            }
        }, beans)
                //                //设置指示器为普通指示器
                .setIndicatorStyle(CustomBanner.IndicatorStyle.ORDINARY)
                //                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setIndicatorRes(R.drawable.shape_point_select, R.drawable.shape_point_unselect)
                //                //设置指示器的方向
                .setIndicatorGravity(CustomBanner.IndicatorGravity.CENTER)
                //                //设置指示器的指示点间隔
                .setIndicatorInterval(20)
                //设置自动翻页
                .startTurning(5000);
    }

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

    /*
    * 获取门店详情
    * */
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
        Http.OkHttpGet(CardItemActivity.this, url1, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                dissLoad();
                noNetworkView.setVisibility(View.GONE);
                contentLayout.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.d("shangdianxiangqig", result + "==" + url1);
                    Long serverTime = jsonObject.getLong("serverTime");
                    if (jsonObject.getString("code").equals("0")) {
                        if (Toplist.size() > 0) {
                            Toplist.clear();
                            readlist.clear();
                        }
                        JSONObject data = jsonObject.getJSONObject("data");
                        /**
                         * 轮播图
                         * */
                        try {
                            JSONArray pics = data.getJSONArray("pics");
                            for (int p = 0; p < pics.length(); p++) {
                                images.add(pics.getString(p));
                            }
                            setBean(images);
                        } catch (Exception e) {

                        }
                        try {
                            priceLevel = data.getInt("priceLevel");
                        } catch (Exception e) {
                            priceLevel = 0;
                        }
                        car_msg_img_ly.setVisibility(View.VISIBLE);
                        if (priceLevel == 1) {
                            car_msg_mon_0.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                        } else if (priceLevel == 2) {
                            car_msg_mon_0.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                            car_msg_mon_1.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                        } else if (priceLevel == 3) {
                            car_msg_mon_0.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                            car_msg_mon_1.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                            car_msg_mon_2.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                        } else if (priceLevel == 4) {
                            car_msg_mon_0.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                            car_msg_mon_1.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                            car_msg_mon_2.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                            car_msg_mon_3.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                        } else if (priceLevel == 5) {
                            car_msg_mon_0.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                            car_msg_mon_1.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                            car_msg_mon_2.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                            car_msg_mon_3.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                            car_msg_mon_4.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                        } else {
                            car_msg_img_ly.setVisibility(View.INVISIBLE);
                        }
                        try {
                            tags = data.getString("tags");
                        } catch (Exception e) {
                            tags = "";
                        }
                        try{
                            String describe = data.getString("describe");
                            if (describe.length()>0){
                                card_describe.setText(describe);
                            }else {
                                card_miao_ly.setVisibility(View.GONE);
                                card_miao_view.setVisibility(View.GONE);
                            }
                        }catch (Exception e){
                            card_miao_ly.setVisibility(View.GONE);
                            card_miao_view.setVisibility(View.GONE);
                        }

                        card_describe.post(new Runnable() {
                            @Override
                            public void run() {
                                boolean textView = isTextView();
                                if (textView){

                                    card_item_miao.setClickable(true);
                                }else {
                                    card_item_miao.setClickable(false);
                                    card_arrow_miao.setVisibility(View.GONE);
                                    card_item_arrow_miao.setVisibility(View.GONE);
                                }
                            }
                        });


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
                                    String closeTmText = TimeUtil.timeUTCdate(closeTm+timeLag);
                                    String openingTmText = TimeUtil.timeUTCdate(openingTm+timeLag);
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
                                    String closeTmSecondtimedate = TimeUtil.timeUTCdate(closeTmSecond+timeLag);
                                    String openingTmSecondtimedate = TimeUtil.timeUTCdate(openingTmSecond+timeLag);
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
                        } else {
                            cardMoreItem.setVisibility(View.GONE);
                            card_vi.setVisibility(View.GONE);
                            card_vie.setVisibility(View.GONE);
                            horr_list.setVisibility(View.GONE);
                        }
                        if (redeem.length() != 0) {       //兑换商品
                            if (readlist.size() > 0) {
                                readlist.clear();
                            }
                            for (int i = 0; i < redeem.length(); i++) {


                                CardItemTopBean topBean = GSonUtil.parseGson(redeem.getString(i), CardItemTopBean.class);
                                readlist.add(topBean);   //热卖list Toplist
                            }
                        } else {
                            cardNoText.setVisibility(View.VISIBLE);
                        }
                        //礼包信息
                        try {
                            JSONObject userGiftReqVo = data.getJSONObject("userGiftReqVo");
                            boolean giftReceived = userGiftReqVo.getBoolean("giftReceived");
                            boolean giftExpired = userGiftReqVo.getBoolean("giftExpired");
                            if (giftExpired) { //已过期
                                if (token.equals("")) {

                                } else {

                                    OneDialog = new GiftReOneDialog(CardItemActivity.this, R.style.box_dialog, onClickListener);
                                    OneDialog.show();
                                }
                            } else {

                                if (giftReceived) {  //已经领取

                                    String token = (String) SPUtil.get(CardItemActivity.this, "Token", "");
                                    if (token.equals("")) {

                                    } else {
                                        TweDialog = new GiftReTweDialog(CardItemActivity.this, R.style.box_dialog, onClickListener);
                                        TweDialog.show();
                                    }

                                } else {
                                    if (token.equals("")) {

                                    } else {
                                        ThreeDialog = new GiftThreeReDialog(CardItemActivity.this, R.style.box_dialog, onClickListener);
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
                        String  luchang= (String) SPUtil.get(CardItemActivity.this, "luchang", "");
                        try {
                            name1 = data.getString("name");
                            carItemName.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            name1="";
                            carItemName.setVisibility(View.GONE);
                        }
                        carItemName.setText(name1);
                        try {
                            nameEn = data.getString("nameEn");
                            card_item_nameEn.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            card_item_nameEn.setVisibility(View.GONE);
                            nameEn="";
                        }
                        card_item_nameEn.setText(nameEn);

                        address = data.getString("address");
                        if (address != null) {
                            cardItemAdd.setText(data.getString("address"));
                        }

                        try {
                            grade = data.getDouble("grade");
                        } catch (Exception e) {
                            grade = 0;
                        }

                   /*     //第一次开关门时间
                        try{
                            closeTm = data.getLong("closeTmFrist");

                        }catch (Exception e){
                            closeTm=-1;

                        }
                        try{
                            openingTm = data.getLong("openingTmFrist");
                        }catch (Exception e){
                            openingTm=-1;
                        }
                        if (closeTm!=-1&&openingTm!=-1){
                            String closeTmText = TimeUtil.timedate(closeTm);
                            String openingTmText = TimeUtil.timedate(openingTm);
                            String[] splitcloseTm = closeTmText.split(" ");
                            String[] splitopeningTm = openingTmText.split(" ");
                            String s = splitcloseTm[1];
                            String s1 = splitopeningTm[1];
                            card_time.setText(s1 + "-" + s);
                        }
                        *//**
                         * 第二次关门时间
                         * *//*
                        try{
                            closeTmSecond = data.getLong("closeTmSecond");
                        }catch (Exception e){
                            closeTmSecond=-1;
                        }
                        try{
                            openingTmSecond = data.getLong("openingTmSecond");
                        }catch (Exception e){
                            openingTmSecond=-1;
                        }
                        if (closeTmSecond!=-1&&openingTmSecond!=-1){
                            String closeTmSecondtimedate = TimeUtil.timedate(closeTmSecond);
                            String openingTmSecondtimedate = TimeUtil.timedate(openingTmSecond);
                            String[] splitclose = closeTmSecondtimedate.split(" ");
                            String[] splitopening = openingTmSecondtimedate.split(" ");
                            String s = splitclose[1];
                            String s1 = splitopening[1];
                            card_time_l.setVisibility(View.VISIBLE);
                            card_time_cl.setText(s1 + "-" + s);
                        }else {
                            card_time_l.setVisibility(View.GONE);
                        }*/

                        if (grade <= 5 && grade >= 4.5) {
                            carItemScore.setBackgroundResource(R.drawable.home_shape);
                        } else if (grade < 4.5 && grade >= 4) {
                            carItemScore.setBackgroundResource(R.drawable.home_shape_four);
                        } else if (grade < 4 && grade >= 3) {
                            carItemScore.setBackgroundResource(R.drawable.home_shape_three);
                        } else if (grade < 3) {
                            carItemScore.setBackgroundResource(R.drawable.home_shape_twe);
                        }
                        carItemScore.setText(grade + "");

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
                        int type = data.getInt("type");
                        try {
                            currency1 = data.getInt("currency");
                        } catch (Exception e) {
                        }
                        try {
                            commentCount = data.getInt("commentCount");
                            card_txt.setText("(" + commentCount + ")");
                            /*if (commentCount ==0){
                                card_tx_read.setVisibility(View.INVISIBLE);
                            }else {
                                card_tx_read.setVisibility(View.VISIBLE);
                            }*/
                        } catch (Exception e) {
                            commentCount=0;
                        }
                        category1 = data.getString("category");
                        Http.OkHttpGet(CardItemActivity.this, cfgURL, new Http.OnDataFinish() {
                            @Override
                            public void OnSuccess(String result) {
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



                                    String luchang = (String) SPUtil.get(CardItemActivity.this, "luchang", "");
                                    boolean zh = TimeUtil.isZh(CardItemActivity.this);
                                    if (luchang.equals("0")){
                                        if (zh){
                                            luchang="3";
                                        }else {
                                            luchang="2";
                                        }
                                    }
                                    for (int i = 0; i < category.length(); i++) {
                                        JSONObject jsonObject1 = category.getJSONObject(i);
                                        String id = jsonObject1.getString("Id");
                                        if (category1.equals(id)) {
                                            JSONArray sub = jsonObject1.getJSONArray("Sub");
                                            for (int j = 0; j < sub.length(); j++) {
                                                JSONObject o = (JSONObject) sub.get(j);
                                                int id1 = o.getInt("Id");
                                                if (type == id1) {
                                                    if (luchang.equals("1") || luchang.equals("2") || luchang.equals("0")) {
                                                        try {
                                                            carItemDish.setText(o.getString("NameCh"));
                                                        } catch (Exception e) {
                                                        }

                                                    } else if (luchang.equals("3")) {
                                                        try {
                                                            carItemDish.setText(o.getString("NameEn"));
                                                        } catch (Exception e) {
                                                        }

                                                    }
                                                }
                                            }
                                        }

                                    }

                                    JSONArray currency = data.getJSONArray("Currency");

                                    if (!tags.equals("")) {
                                        split1 = tags.replace("[", "").replace("]", "").split(",");
                                        int tag = 0;

                                        for (int i = 2; i < split1.length; i++) {
                                            newlist.add(split1[i]);
                                        }
                                        for (int t = 0; t < split1.length; t++) {
                                            if (t == 2) {
                                                break;
                                            }
                                            if (split1.length <= 2) {

                                                card_item_info.setVisibility(View.INVISIBLE);
                                            } else {
                                                card_item_info.setVisibility(View.VISIBLE);
                                            }
                                            String string = split1[t];
                                            if (!string.equals("")) {
                                                for (int f = 0; f < sags.size(); f++) {
                                                    int id = sags.get(f).getId();
                                                    if (Integer.parseInt(string) == id) {
                                                        if (tag == 0) {
                                                            tag = 1;
                                                            if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
                                                                card_info_vegn_txt.setText(sags.get(f).getNameCh());
                                                            }else if (luchang.equals("3")){
                                                                card_info_vegn_txt.setText(sags.get(f).getNameEn());
                                                            }

                                                            if (string.equals("1")) {
                                                                card_info_vegen.setBackgroundResource(R.drawable.storedetail_info_icon_vegan);
                                                            } else if (string.equals("2")) {
                                                                card_info_vegen.setBackgroundResource(R.drawable.storedetail_info_icon_creditcard);
                                                            } else if (string.equals("3")) {
                                                                card_info_vegen.setBackgroundResource(R.drawable.storedetail_info_icon_freewifi);
                                                            } else if (string.equals("4")) {
                                                                card_info_vegen.setBackgroundResource(R.drawable.storedetail_info_icon_kids);
                                                            } else if (string.equals("5")) {
                                                                card_info_vegen.setBackgroundResource(R.drawable.storedetail_info_icon_group);
                                                            } else if (string.equals("6")) {
                                                                card_info_vegen.setBackgroundResource(R.drawable.storedetail_info_icon_takeout);
                                                            } else if (string.equals("7")) {
                                                                card_info_vegen.setBackgroundResource(R.drawable.storedetail_info_icon_pet);
                                                            }
                                                        } else {
                                                            if (string.equals("1")) {
                                                                card_info_card.setBackgroundResource(R.drawable.storedetail_info_icon_vegan);
                                                            } else if (string.equals("2")) {
                                                                card_info_card.setBackgroundResource(R.drawable.storedetail_info_icon_creditcard);
                                                            } else if (string.equals("3")) {
                                                                card_info_card.setBackgroundResource(R.drawable.storedetail_info_icon_freewifi);
                                                            } else if (string.equals("4")) {
                                                                card_info_card.setBackgroundResource(R.drawable.storedetail_info_icon_kids);
                                                            } else if (string.equals("5")) {
                                                                card_info_card.setBackgroundResource(R.drawable.storedetail_info_icon_group);
                                                            } else if (string.equals("6")) {
                                                                card_info_card.setBackgroundResource(R.drawable.storedetail_info_icon_takeout);
                                                            } else if (string.equals("7")) {
                                                                card_info_card.setBackgroundResource(R.drawable.storedetail_info_icon_pet);
                                                            }
                                                            if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
                                                                card_info_card_txt.setText(sags.get(f).getNameCh());
                                                            }else if (luchang.equals("3")){
                                                                card_info_card_txt.setText(sags.get(f).getNameEn());
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                card_info_lyy.setVisibility(View.GONE);
                                                card_info_ly.setVisibility(View.GONE);
                                                card_info_vi.setVisibility(View.GONE);
                                            }
                                        }
                                    } else {
                                        card_info_lyy.setVisibility(View.GONE);
                                        card_info_ly.setVisibility(View.GONE);
                                        card_info_vi.setVisibility(View.GONE);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });

                        imgUrl = data.getString("imgUrl");
                        //   imageLoader.displayImage(imgUrl, card_img, getDefaultOptions());
                        Glide.with(CardItemActivity.this)
                                .load(imgUrl)
                                .centerCrop()
                                .skipMemoryCache(true)
                                .error(R.drawable.home_blank_pic)
                                .into(card_img);
                        tel = data.getString("tel");
                        latitude = data.getDouble("latitude");
                        longitude = data.getDouble("longitude");
                        if (distance == null) {
                            String distance = data.getString("distance");
                            double v1 = Double.parseDouble(distance);
                            if (v1 >= 1000) {
                                double v = v1 / 1000;
                                String s = DateUtil.roundByScale(v, 1);
                                carItemDistance.setText(s + getResources().getString(R.string.km));
                            } else {
                                String of = String.valueOf(distance);
                                String[] split = of.split("\\.");
                                carItemDistance.setText(split[0] + getResources().getString(R.string.m));
                            }

                            int operatingStatus = data.getInt("operatingStatus");

                            if (operatingStatus == 1) {
                                cardItemSoon.setBackgroundResource(R.drawable.home_shape_soon);
                                cardItemSoon.setText(R.string.soon);
                            } else if (operatingStatus == 2) {
                                cardItemSoon.setBackgroundResource(R.drawable.home_shape_close);
                                cardItemSoon.setText(R.string.closs_wei);
                            } else if (operatingStatus == 0) {
                                cardItemSoon.setBackgroundResource(R.drawable.home_shape_open);
                                cardItemSoon.setText(R.string.open);
                            }

                        } else {
                            double v1 = Double.parseDouble(distance);
                            if (v1 >= 1000) {
                                double v = v1 / 1000;
                                String s = DateUtil.roundByScale(v, 1);
                                carItemDistance.setText(s + getResources().getString(R.string.km));
                            } else {
                                String of = String.valueOf(distance);
                                String[] split = of.split("\\.");
                                carItemDistance.setText(split[0] + getResources().getString(R.string.m));
                            }
                        }
                        if (distances != null) {
                            double v1 = Double.parseDouble(distances);
                            if (v1 >= 1000) {
                                double v = v1 / 1000;
                                String s = DateUtil.roundByScale(v, 1);
                                carItemDistance.setText(s + getResources().getString(R.string.km));
                            } else {
                                String of = String.valueOf(distance);
                                String[] split = of.split("\\.");
                                carItemDistance.setText(split[0] + getResources().getString(R.string.m));
                            }
                        }

                        String s_xing = DateUtil.formateRate(String.valueOf(grade));
                        substring = String.valueOf(grade).substring(0, 1);
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
                        if (Toplist.size() != 0) {

                            //创建LinearLayoutManager
                            LinearLayoutManager manager = new LinearLayoutManager(CardItemActivity.this);
                            //设置为横向滑动
                            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            //设置
                            horr_list.setLayoutManager(manager);
                            //实例化适配器
                            MyReAdapter myAdapter = new MyReAdapter(Toplist, CardItemActivity.this);
                            //设置适配器
                            horr_list.setAdapter(myAdapter);


                        }
                        if (readlist.size() != 0) {
                            gridview.setAdapter(new MyAdapter());
                        }

                    } else if (jsonObject.getString("code").equals("5007")) {
                        SPUtil.remove(CardItemActivity.this, "Token");
                        SPUtil.remove(CardItemActivity.this, "photo1");
                        SPUtil.remove(CardItemActivity.this, "photo");
                        SPUtil.remove(CardItemActivity.this, "gen");
                        SPUtil.remove(CardItemActivity.this, "name_one");
                        SPUtil.put(CardItemActivity.this, "deng", false);
                        Intent intent3 = new Intent(CardItemActivity.this, LoginActivity.class);
                        startActivity(intent3);
                        finish();
                    } else {
                        //失败
                        //Toast.makeText(CardItemActivity.this, getResources().getString(R.string.fan), Toast.LENGTH_SHORT).show();
                        ToastUtil.showgravity(CardItemActivity.this, getResources().getString(R.string.fan));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                dissLoad();
                noNetworkView.setVisibility(View.VISIBLE);
                contentLayout.setVisibility(View.GONE);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        String exchange = (String) SPUtil.get(this, "exchange", "");
        if (exchange.equals("1")) {

            initData();
            SPUtil.remove(this, "exchange");
        }
        if (i!=0){
            card_star.setRate(0);
        }
        Log.e(AMAP_PACKAGENAME, "start onResume~~~");

    }

    private static final double EARTH_RADIUS = 6378137.0;

    // 返回单位是米
    public static double getDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    private void initView() {


    }

    //TODO
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.vox_finsh:  //确认
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                    }
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
                    LoginManager.getInstance().logInWithReadPermissions(CardItemActivity.this, Arrays.asList("public_profile"));
                    browsDialog.dismiss();

                    break;
                case R.id.gift_box_ly:
                    try {
                        TweDialog.dismiss();
                    } catch (Exception e) {
                    }
                    break;
                case R.id.gift_ly_one: //查看礼包
                    Intent intent = new Intent(CardItemActivity.this, GiftItemActivity.class);
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

    //获得所有包名类名
    public List<AppInfoVo> getShareApps(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<AppInfoVo> appInfoVos = new ArrayList<AppInfoVo>();
        List<ResolveInfo> resolveInfos = new ArrayList<ResolveInfo>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("*/*");
        PackageManager pManager = context.getPackageManager();
        resolveInfos = pManager.queryIntentActivities(intent, PackageManager
                .COMPONENT_ENABLED_STATE_DEFAULT);
        for (int i = 0; i < resolveInfos.size(); i++) {
            AppInfoVo appInfoVo = new AppInfoVo();
            ResolveInfo resolveInfo = resolveInfos.get(i);
            appInfoVo.setAppName(resolveInfo.loadLabel(packageManager).toString());
            appInfoVo.setIcon(resolveInfo.loadIcon(packageManager));
            appInfoVo.setPackageName(resolveInfo.activityInfo.packageName);
            appInfoVo.setLauncherName(resolveInfo.activityInfo.name);
            appInfoVos.add(appInfoVo);
        }
        return appInfoVos;
    }


    private int inf = 0;

    @OnClick({R.id.title_carditem_fl, R.id.title_carditem_title, R.id.card_item_phone, R.id.card_more_item, R.id.card_ly, R.id.title_carditem_img, R.id.crd_ly, R.id.card_item_write, R.id.title_carditem_shou, R.id.title_carditem_no, R.id.card_item_info,R.id.card_item_miao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_carditem_fl:
                if (distances != null) {
                    finish();
                } else if (flags == 1) {
                    finish();
                } else if (comment == 1) {
                    finish();
                } else {
                    Intent intent2 = new Intent(CardItemActivity.this, MainActivity.class);
                    startActivity(intent2);
                    finish();
                }

                break;
            case R.id.title_carditem_title:

                break;
            case R.id.card_item_phone:
                //申请权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    } else {
                        callPhone();
                    }
                }

                break;
            case R.id.card_more_item: //推荐商品
                Intent intent = new Intent(CardItemActivity.this, RecomActivity.class);
                intent.putExtra("Top_id", id);
                startActivity(intent);
                break;
            case R.id.card_ly:
                try{
                    shopName = itemlist.get(0).getShopName();
                }catch (Exception e){
                    shopName=null;
                }

                    if (shopName==null) {
                        //Toast.makeText(CardItemActivity.this, getResources().getString(R.string.zan), Toast.LENGTH_SHORT).show();
                        ToastUtil.showgravity(CardItemActivity.this, getResources().getString(R.string.zan));
                    } else {
                        //评论
                        Intent intent1 = new Intent(this, StoreViewActivity.class);
                        intent1.putExtra("grade", grade + "");
                        intent1.putExtra("shopId", id + "");
                        intent1.putExtra("substring", substring);
                        intent1.putExtra("pingfen", pingfen);
                        startActivity(intent1);
                    }
                break;

            case R.id.title_carditem_img:

                andro();
               /* popup = new SharePopupWindow(this);
                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        PopupWindowUtil.setWindowTransparentValue(CardItemActivity.this, 1.0f);
                    }
                });
                PopupWindowUtil.setWindowTransparentValue(CardItemActivity.this, 0.5f);
                //设置PopupWindow中的位置  3102083853078
                popup.showAtLocation(CardItemActivity.this.findViewById(R.id.card_view_one), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                popup.setOnItemClickListener(this);*/
                break;
            case R.id.crd_ly:
                car_item_list.setAdapter(new TmAdapter());
                if (phot == 0) {
                    car_item_list.setVisibility(View.VISIBLE);
                    phot = 1;
                    card_arrow_ig.setVisibility(View.GONE);
                    card_item_arrow_img.setVisibility(View.VISIBLE);
                } else if (phot == 1) {
                    car_item_list.setVisibility(View.GONE);
                    phot = 0;
                    card_arrow_ig.setVisibility(View.VISIBLE);
                    card_item_arrow_img.setVisibility(View.GONE);
                }
                break;
            case R.id.card_item_write:
                CardBean.CouponHistoryBean couponHistory = itemlist.get(0).getCouponHistory();
                Intent intent1 = new Intent(this, WriteActivity.class);

                intent1.putExtra("shopid", String.valueOf(couponHistory.getShopIdX()));
                intent1.putExtra("commodityId", String.valueOf(couponHistory.getCommodityIdX()));
                intent1.putExtra("shopName", couponHistory.getShopNameX());
                intent1.putExtra("commodityName", couponHistory.getCommodityNameX());
                intent1.putExtra("couponHistoryId", String.valueOf(couponHistory.getIdX()));
                intent1.putExtra("couponId", String.valueOf(couponHistory.getCouponLevel()));
                intent1.putExtra("useCount", String.valueOf(couponHistory.getUseCount()));
                startActivity(intent1);
                break;
            case R.id.title_carditem_shou: //收藏

                String token = (String) SPUtil.get(CardItemActivity.this, "Token", "");
                if (token.length() > 0) {
                    FormBody body = new FormBody.Builder()
                            .add("locationId", "1")
                            .add("shopId", shop_id)
                            .build();
                    Http.OkHttpPost(CardItemActivity.this, urlUtil + UrlUtil.favshop, body, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            Log.d("favshop", result + urlUtil + UrlUtil.favshop);
                            try {
                                JSONObject jsonObject1 = new JSONObject(result);
                                if (jsonObject1.getString("code").equals("0")) {

                                    int fav_ShopId = jsonObject1.getInt("data");

                                    favShopId = fav_ShopId;

                                    title_back_no.setVisibility(View.VISIBLE);
                                    title_back_shou.setVisibility(View.GONE);
                                    ToastUtil.showgravity(CardItemActivity.this, getResources().getString(R.string.shouc));
                                    if (favour==1){
                                        SPUtil.put(CardItemActivity.this,"favour",2);
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
                    ToastUtil.showgravity(CardItemActivity.this, getResources().getString(R.string.login_inj));
                }


                break;
            case R.id.title_carditem_no:  //取消收藏
                Http.OkHttpDelete(CardItemActivity.this, urlUtil + UrlUtil.delete_favshop + "?id=" + favShopId, new Http.OnDataFinish() {
                    @Override
                    public void OnSuccess(String result) {
                        Log.d("delete_favshop", result + urlUtil + UrlUtil.delete_favshop + "?id=" + favShopId);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("code").equals("0")) {

                                title_back_no.setVisibility(View.GONE);
                                title_back_shou.setVisibility(View.VISIBLE);
                                if (favour==1){
                                    SPUtil.put(CardItemActivity.this,"favour",1);
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
                break;
            case R.id.card_item_info:
                if (inf == 0) {
                    card_item_info_img.setVisibility(View.VISIBLE);
                    card_info_ig.setVisibility(View.GONE);
                    inf = 1;
                    card_info.setVisibility(View.VISIBLE);
                    card_info.setAdapter(new MyInfoAdapter());
                } else if (inf == 1) {
                    card_item_info_img.setVisibility(View.GONE);
                    card_info_ig.setVisibility(View.VISIBLE);
                    inf = 0;
                    card_info.setVisibility(View.GONE);
                }
                break;
            case R.id.card_item_miao:   //描述选择
                if (desc==0){
                    desc=1;
                    card_describe.setMaxLines(10000);
                    card_arrow_miao.setVisibility(View.GONE);
                    card_item_arrow_miao.setVisibility(View.VISIBLE);

                }else if (desc==1){
                    desc=0;
                    card_arrow_miao.setVisibility(View.VISIBLE);
                    card_item_arrow_miao.setVisibility(View.GONE);
                    card_describe.setMaxLines(1);
                }
                break;
        }
    }
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
        homeListview.setFocusable(false);
        popupWindow.showAtLocation(cardItemAdd, Gravity.BOTTOM, 0, 0);
    }

    //拨打电话
    public void callPhone() {
        final CustomDialog.Builder builder = new CustomDialog.Builder(CardItemActivity.this);
        builder.setTitle(String.format(getString(R.string.md_title)));
        builder.setMessage(String.format(getString(R.string.call)) + tel);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_map_gd_ly:
                /*Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("amapuri://route/plan/?slat="
                        + lastidu + "&slon=" + longtidu
                        + "&dlat=" + latitude + "&dlon=" + longitude +"&dev=0&t=4")); //&dname="+name+"*/
                try{
                    Intent intent = new Intent(
                            "android.intent.action.VIEW",
                            android.net.Uri.parse(
                                    "androidamap://route?sourceApplication=Whoot!" + "&dlat=" + latitude//终点的经度
                                            + "&dlon=" + longitude//终点的纬度
                                            + "&dname=" + name1
                                            + "&dev=0" + "&t=1"));

                    intent.setPackage(AMAP_PACKAGENAME);
                    startActivity(intent);
                    popupWindow.dismiss();
                }catch (Exception e){
                    ToastUtil.showgravity(CardItemActivity.this,getResources().getString(R.string.map_enable));
                }

                break;
            case R.id.pop_map_bd_ly:
                try{
                    Intent intent1 = new Intent("android.intent.action.VIEW", Uri.parse("baidumap://map/walknavi?origin="
                            + lastidu + "," + longtidu
                            + "&destination=" + latitude + "," + longitude));
                    intent1.setPackage(BAIDUMAP_PACKAGENAME);
                    startActivity(intent1);
                    popupWindow.dismiss();
                }catch (Exception e){
                    ToastUtil.showgravity(CardItemActivity.this,getResources().getString(R.string.map_enable));
                }

                break;
            case R.id.pop_map_blank:
                popupWindow.dismiss();
                break;
            case R.id.pop_map_cancle:
                popupWindow.dismiss();
                break;
            case R.id.pop_map_google_ly:

                try{
                    if (isGOOGLE) {
                        Intent i = new Intent(
                                Intent.ACTION_VIEW,
                                Uri
                                        .parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=" + latitude + "," + longitude + "(" + name1 + ")"));
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
                }catch (Exception e){
                    ToastUtil.showgravity(CardItemActivity.this,getResources().getString(R.string.map_enable));
                }

                break;
            case R.id.card_item_add:
                // if (isBAidu || isGaoDe) {
                popwindow();
                // } else {
                //  Toast.makeText(CardItemActivity.this, "你未安装百度地图或高德地图", Toast.LENGTH_SHORT).show();

                // }
                break;
        }
    }

    private int comm = 0;
    private String shop_id = "";
    private int info = 0;

    public void getComment() {

        if (shopId1.equals("undefined")) {
            shop_id = String.valueOf(id);
        } else {
            shop_id = shopId1;
        }
        Http.OkHttpGet(CardItemActivity.this, urlUtil + UrlUtil.saveinfo + "?locationId=" + "1" + "&shopId=" + shop_id, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    Log.d("pinglun", result + "==" + urlUtil + UrlUtil.saveinfo + "?locationId=" + "1" + "&shopId=" + shop_id);
                    JSONObject jsonObject = new JSONObject(result);
                    if (itemlist.size() != 0) {
                        itemlist.clear();
                    }
                    String code = jsonObject.getString("code");
                    if (code.equals("0")) {
                        try {
                            String data1 = jsonObject.getString("data");
                        } catch (Exception e) {
                            info = 1;
                        }
                        if (info == 0) {
                            String substring = result.substring(21, result.indexOf("\"message\""));
                            String substring1 = substring.substring(0, substring.length() - 3);
                            String s = "{\"data\": [" + substring1 + "]}";
                            JSONObject jsonObject1 = new JSONObject(s);
                            JSONArray data = jsonObject1.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                CardBean bean = GSonUtil.parseGson(data.getString(i), CardBean.class);
                                itemlist.add(bean);
                            }
                            int id = itemlist.get(0).getId();

                            if (id == 0) {
                                cardNoRed.setVisibility(View.VISIBLE);
                            } else {
                                shopsLVAdapter = new ShopsLVAdapter(CardItemActivity.this, itemlist);
                                try {
                                    homeListview.setAdapter(shopsLVAdapter);
                                } catch (Exception e) {
                                }
                                shopsLVAdapter.notifyDataSetChanged();
                            }
                        }
                        /**
                         * 收藏
                         * */
                        favShop = itemlist.get(0).isFavShop();
                        favShopId = itemlist.get(0).getFavShopId();
                        if (favShop) {
                            title_back_no.setVisibility(View.VISIBLE);
                            title_back_shou.setVisibility(View.GONE);
                        } else {
                            title_back_shou.setVisibility(View.VISIBLE);
                            title_back_no.setVisibility(View.GONE);
                        }

                        boolean uncommented = itemlist.get(0).isUncommented();
                        if (uncommented) {
                            card_item_write.setVisibility(View.VISIBLE);
                            card_item_view0.setVisibility(View.VISIBLE);
                            card_item_view1.setVisibility(View.VISIBLE);
                        } else {
                            card_item_write.setVisibility(View.GONE);
                            card_item_view0.setVisibility(View.GONE);
                            card_item_view1.setVisibility(View.GONE);
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
    }

    public boolean isTextView() {
        float m=card_describe.getPaint().measureText(card_describe.getText().toString());
        float n=3*(card_describe.getWidth()-card_describe.getPaddingRight()-card_describe.getPaddingLeft());
        return m>n;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // 条目的总数 文字组数 == 图片张数
            return readlist.size();
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

            View view = View.inflate(CardItemActivity.this, R.layout.gridview_item, null);

            ImageView gridview_img_one = view.findViewById(R.id.gridview_img_one);
            TextView gridview_txt = view.findViewById(R.id.gridview_txt);
            ImageView gridview_img = view.findViewById(R.id.gridview_img);
            TextView gridview_txtx = view.findViewById(R.id.gridview_txtx);
            TextView gridview_tx = view.findViewById(R.id.gridview_tx);
            String commodityName = readlist.get(position).getCommodityName();
            gridview_txt.setText(commodityName);
            String commodityPic = readlist.get(position).getCommodityPic();
            gridview_img_one.setVisibility(View.VISIBLE);
            Glide.with(CardItemActivity.this)
                    .load(commodityPic)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .into(gridview_img_one);

            int couponId = readlist.get(position).getCouponId();
            if (couponId == 1) {
                gridview_img.setBackgroundResource(R.drawable.token_icon_bronze);
            } else if (couponId == 2) {
                gridview_img.setBackgroundResource(R.drawable.token_icon_silver);
            } else if (couponId == 3) {
                gridview_img.setBackgroundResource(R.drawable.token_icon_gold);
            } else if (couponId == 4) {
                gridview_img.setBackgroundResource(R.drawable.token_icon_diamond);
            }
            int costs = readlist.get(position).getCosts();
            gridview_txtx.setText("X" + costs);
            int usedAmout = readlist.get(position).getUsedAmout();
            int maxUseAmout = readlist.get(position).getMaxUseAmout();
            gridview_tx.setText(usedAmout + "/" + maxUseAmout);
            return view;
        }
    }

    /**
     *
     * */
    List<String> newlist = new ArrayList<>();

    class MyInfoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // 条目的总数 文字组数 == 图片张数
            return newlist.size();
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

            View view = View.inflate(CardItemActivity.this, R.layout.gridview_item_info, null);
            ImageView info_card_info_vegen = view.findViewById(R.id.info_card_info_vegen);
            TextView info_card_info_vegn_txt = view.findViewById(R.id.info_card_info_vegn_txt);

            String  luchang= (String) SPUtil.get(CardItemActivity.this, "luchang", "");
            String string = newlist.get(position);
            for (int f = 0; f < sags.size(); f++) {
                int id = sags.get(f).getId();
                if (Integer.parseInt(string) == id) {

                    boolean zh = TimeUtil.isZh(CardItemActivity.this);
                    if (luchang.equals("0")){
                        if (zh){
                            luchang="3";
                        }else {
                            luchang="2";
                        }
                    }
                    if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
                        info_card_info_vegn_txt.setText(sags.get(f).getNameCh());
                    }else if (luchang.equals("3")){
                        info_card_info_vegn_txt.setText(sags.get(f).getNameEn());
                    }

                    if (string.equals("1")) {
                        info_card_info_vegen.setBackgroundResource(R.drawable.storedetail_info_icon_vegan);
                    } else if (string.equals("2")) {
                        info_card_info_vegen.setBackgroundResource(R.drawable.storedetail_info_icon_creditcard);
                    } else if (string.equals("3")) {
                        info_card_info_vegen.setBackgroundResource(R.drawable.storedetail_info_icon_freewifi);
                    } else if (string.equals("4")) {
                        info_card_info_vegen.setBackgroundResource(R.drawable.storedetail_info_icon_kids);
                    } else if (string.equals("5")) {
                        info_card_info_vegen.setBackgroundResource(R.drawable.storedetail_info_icon_group);
                    } else if (string.equals("6")) {
                        info_card_info_vegen.setBackgroundResource(R.drawable.storedetail_info_icon_takeout);
                    } else if (string.equals("7")) {
                        info_card_info_vegen.setBackgroundResource(R.drawable.storedetail_info_icon_pet);
                    }

                }
            }

            return view;
        }
    }

    private void getFavfo(int fav) {
        if (fav == 0) {
            favShop = true;
        } else {
            favShop = false;
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

            View view = View.inflate(CardItemActivity.this, R.layout.tmlist_item, null);
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
                String closeTmText = TimeUtil.timeUTCdate(closeTmlist+timeLag);
                String openingTmText = TimeUtil.timeUTCdate(openingTmlist+timeLag);
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
                String closeTmSecondtimedate = TimeUtil.timeUTCdate(closeTmSecondlist+timeLag);
                String openingTmSecondtimedate = TimeUtil.timeUTCdate(openingTmSecondlist+timeLag);
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
     * 设置默认图片
     *
     * @return
     */
    public static DisplayImageOptions getDefaultOptions() {
        //设置图片加载的属性
        DisplayImageOptions.Builder b = new DisplayImageOptions.Builder();
        //图片为空的时候显示为
        b.showImageForEmptyUri(R.drawable.home_blank_pic);
        //图片加载失败的时候显示为
        b.showImageOnFail(R.drawable.home_blank_pic);
        //图片正在加载的时候显示为
        b.showImageOnLoading(R.drawable.home_blank_pic);

        b.resetViewBeforeLoading(Boolean.TRUE);

        b.cacheOnDisk(Boolean.TRUE);

        b.cacheInMemory(Boolean.TRUE);

        b.imageScaleType(ImageScaleType.EXACTLY_STRETCHED);

        return b.bitmapConfig(Bitmap.Config.RGB_565).build();
    }


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

    private void init() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                Log.d("LoginActivit3333y", "facebook登录成功了999999999" + loginResult.getAccessToken().getToken());
                //获取登录信息
                getLoginInfo(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // Toast.makeText(CardItemActivity.this, getResources().getString(R.string.facebook), Toast.LENGTH_SHORT).show();
                ToastUtil.showgravity(CardItemActivity.this, getResources().getString(R.string.facebook));
                Log.d("LoginActivit3333y", "facebook登录成功了88888888");

            }

            @Override
            public void onError(FacebookException error) {
                //Toast.makeText(CardItemActivity.this, getResources().getString(R.string.facebook_one), Toast.LENGTH_SHORT).show();
                ToastUtil.showgravity(CardItemActivity.this, getResources().getString(R.string.facebook_one));
                Log.d("LoginActivit3333y", "facebook登录成功了7777777777");

            }
        });
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
                    SPUtil.put(CardItemActivity.this, "email", emali);

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
                    Http.OkHttpPost(CardItemActivity.this, urlUtil + UrlUtil.login, body, new Http.OnDataFinish() {
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
                                        SPUtil.put(CardItemActivity.this, "downco", String.valueOf(couponCdl));
                                    } catch (Exception e) {

                                    }

                                    SPUtil.put(CardItemActivity.this, "Token", Token);
                                    SPUtil.put(CardItemActivity.this, "photo", photo1); //头像
                                    SPUtil.put(CardItemActivity.this, "gen", gen); //性别
                                    SPUtil.put(CardItemActivity.this, "name_one", name_one); //名字
                                    SPUtil.put(CardItemActivity.this, "you_usid", userid + "");

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

                                            SPUtil.put(CardItemActivity.this, "you_id", id1 + "");
                                            SPUtil.put(CardItemActivity.this, "you_Sn", couponSn + "");
                                            SPUtil.put(CardItemActivity.this, "you_type", couponId + "");


                                        }

                                    } catch (Exception e) {

                                    }


                                    initData();

                                } else {
                                    // Toast.makeText(CardItemActivity.this,getResources().getString(R.string.lo),Toast.LENGTH_SHORT).show();
                                    ToastUtil.showgravity(CardItemActivity.this, getResources().getString(R.string.lo));
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (flags == 1) {
                finish();
            } else if (comm == 1) {
                finish();
            } else {
                Intent intent2 = new Intent(CardItemActivity.this, MainActivity.class);
                startActivity(intent2);
                finish();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }

    /**
     * 配置文件获取shopTags
     */

    private void getShopTags() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //we should dismiss popupWindow to avoid leaking the window when activity is destoryed
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
}
