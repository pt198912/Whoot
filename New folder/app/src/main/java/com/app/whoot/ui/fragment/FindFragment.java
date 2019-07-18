package com.app.whoot.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.app.whoot.R;
import com.app.whoot.adapter.LefteAdapter;
import com.app.whoot.adapter.MsgAdapter;
import com.app.whoot.adapter.NormalAdapter;
import com.app.whoot.adapter.RightAdapter;
import com.app.whoot.adapter.StoreInfosPagerAdapter;
import com.app.whoot.app.MyApplication;
import com.app.whoot.base.fragment.BaseFragment;
import com.app.whoot.bean.ActivityBean;
import com.app.whoot.bean.CategoryBean;
import com.app.whoot.bean.CfgBean;
import com.app.whoot.bean.PriceScopeBean;
import com.app.whoot.bean.RecyclerData;
import com.app.whoot.bean.ShopBean;
import com.app.whoot.bean.ShopBeanNew;
import com.app.whoot.component.service.FetchAddressIntentService;
import com.app.whoot.manager.ConfigureInfoManager;
import com.app.whoot.modle.http.Http;
import com.app.whoot.modle.http.OkHttp;
import com.app.whoot.threadpool.FixedThreadPool;
import com.app.whoot.ui.activity.LoginMeActivity;
import com.app.whoot.ui.activity.SearchActivity;
import com.app.whoot.ui.activity.StoreDetailActivity;
import com.app.whoot.ui.activity.WebViewActivity;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.ui.view.MultiDrawable;
import com.app.whoot.ui.view.RecycleViewDivider;
import com.app.whoot.ui.view.SeekBarPressure;
import com.app.whoot.ui.view.dialog.BoxDialog;
import com.app.whoot.ui.view.dialog.CustomDialog;
import com.app.whoot.ui.view.popup.TestPopupWindow;
import com.app.whoot.ui.view.recycler.RecyclerViewNoBugLinearLayoutManager;
import com.app.whoot.ui.view.recycler.adapter.StandardAdapter;
import com.app.whoot.util.AppUtil;
import com.app.whoot.util.Constants;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.FireBaseEventKey;
import com.app.whoot.util.FirebaseReportUtils;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.NetworkUtils;
import com.app.whoot.util.PermissionUtils;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ScreenUtils;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.app.whoot.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.donkingliang.banner.CustomBanner;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import java.util.ArrayList;
import java.util.Collections;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Sunrise on 4/2/2018.
 * 发现
 */

public class FindFragment extends BaseFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.InfoWindowAdapter, GoogleMap.OnMapClickListener, ClusterManager.OnClusterClickListener<FindFragment.LocationBean>,
        ClusterManager.OnClusterItemClickListener<FindFragment.LocationBean>, View.OnClickListener ,
        AppBarLayout.OnOffsetChangedListener{
    @BindView(R.id.title_tool_bar)
    Toolbar toolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.title_map)
    ImageView titleMap;
    @BindView(R.id.title_search)
    LinearLayout titleSearch;
    @BindView(R.id.frag_fi)
    View fragFi;
    @BindView(R.id.title_addre)
    TextView titleAddre;
    @BindView(R.id.frag_ly)
    LinearLayout fragLy;
    @BindView(R.id.feco_list)
    RecyclerView feco_list;
//    @BindView(R.id.title_ly)
//    LinearLayout title_ly;
    @BindView(R.id.frag_img)
    ImageView frag_img;
    @BindView(R.id.frag_ding)
    LinearLayout frag_ding;
    @BindView(R.id.frag_set)
    TextView frag_set;
    @BindView(R.id.frag_ding_txt)
    TextView frag_ding_txt;
    @BindView(R.id.frag_ding_tx)
    TextView frag_ding_tx;
    @BindView(R.id.frag_d)
    LinearLayout frag_d;
    @BindView(R.id.frag_find_ly)
    LinearLayout frag_find_ly;
    @BindView(R.id.tv_frag_find_consumption)
    TextView frag_find_consumption;
    @BindView(R.id.tv_frag_find_rating)
    TextView frag_find_rating;
    @BindView(R.id.tv_frag_find_dish_style)
    TextView frag_find_dish_style;
    @BindView(R.id.ll_frag_find_consumption)
    View consumptionLayout;
    @BindView(R.id.ll_frag_find_rating)
    View ratingLayout;
    @BindView(R.id.ll_frag_find_dish_style)
    View dishStyleLayout;
    @BindView(R.id.iv_dish_style_sel)
    ImageView dishStyleSelIv;
    @BindView(R.id.iv_consumption_sel)
    ImageView consumptionSelIv;
//    @BindView(R.id.title_caixi)
//    LinearLayout title_caixi;
//    @BindView(R.id.title_image)
//    ImageView title_image;
    @BindView(R.id.title_ding)
    LinearLayout title_ding;
    @BindView(R.id.frag_price)
    LinearLayout fragPrice;
    @BindView(R.id.frag_recyc)
    RecyclerView frag_recyc;
    @BindView(R.id.frag_ly_txt)
    TextView frag_ly_txt;
    @BindView(R.id.no_network_view)
    View noNetworkView;
//    @BindView(R.id.frag_log_ly)
//    LinearLayout frag_log_ly;

    @BindView(R.id.find_refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.find_banner)
    CustomBanner customBanner;
    @BindView(R.id.ll_whoot_label)
    View whootLabelLayout;
    @BindView(R.id.frag_lo)
    LinearLayout frag_lo;
    @BindView(R.id.tv_login)
    TextView loginTv;
    @BindView(R.id.iv_map_back)
    ImageView backFromMapIv;
    @BindView(R.id.collapsingtoolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private MsgAdapter mDataAdapter = null;


    /**
     * 服务器端一共多少条数据
     */
    private static final int TOTAL_COUNTER = 24;//如果服务器没有返回总数据或者总页数，这里设置为最大值比如10000，什么时候没有数据了根据接口返回判断

    /**
     * 每一页展示多少条数据
     */
    private static final int REQUEST_COUNT = 5;

    /**
     * 已经获取到多少条数据了
     */
    private static int mCurrentCounter = 0;

    private LinearLayout map_map_ly, pop_map_gd_ly, pop_map_bd_ly, pop_map_blank, pop_map_google_ly;
    private TextView pop_map_cancle;
    private PopupWindow popupWindow;
    private int postion_one = 0;


    //高德地图应用包名
    public static final String AMAP_PACKAGENAME = "com.autonavi.minimap";
    //百度地图应用包名
    public static final String BAIDUMAP_PACKAGENAME = "com.baidu.BaiduMap";
    //google地图应用包名
    public static final String GOOGLE_PACKAGENAME = "com.google.android.apps.maps";

    private boolean isGaoDe, isBAidu, isGOOGLE;

    private double latitude;
    private double longitude;
    private String name1;

    /**
     * 标记判断是下拉刷新还是加载更多
     */
    private int flg = 1;

    private int who = 0;

    private StandardAdapter<ShopBean> adapter;


    private int flag = 0;
    private int index = 0;

    private AddressResultReceiver mResultReceiver;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Location mLastLocation;
    private boolean mPermissionDenied = false;
    private PopupWindow mPopupWindow;
    private PopupWindow mConsumpPopwindow;
    private List<String> my_list = new ArrayList<>();

    private List<ShopBean> list = new ArrayList<ShopBean>();

    private static List<ShopBeanNew> newList = new ArrayList<ShopBeanNew>();
    private List<ShopBeanNew> list_map = new ArrayList<ShopBeanNew>();
    private Marker customMarker;

    ArrayList<LocationBean> locationBeens;//地点数据集合
    /**
     * 地图上锚点
     */
    private Marker perth;
    private LatLng lastLatLng, perthLatLng;
    /**
     * 用来判断用户在连接上Google Play services之前是否有请求地址的操作
     */
    private boolean mAddressRequested;
    private BoxDialog browsDialog;

    private int refresh;
    private int loadMore;
    private String minDistance = ""; //最小距离
    private String maxDistance = ""; //最大距离
    private String minSpend = ""; //最低消费
    private String maxSpend = ""; //高消费
    private String typeList = ""; //
    private int start = 0;
    private int rows = 1;

    ClusterManager<LocationBean> clusterManager;//聚类管理器


    //private String url = "http://172.17.1.24:8082/portal/shop/get?latitude=22.2&longitude=22.2";
    private String url = "";
    private String url_cv = "";

    private String name;
    private double price;
    private int id;
    private long serverTime;
    private String accToken;

    private LatLng markerLatLng;
    private static String longitude_o;
    private static String latitude_o;
    private ImageLoader imageLoader;
    Random random;
    private TextView map_txt;
    private TextView map_item_score;
    private ImageView map_ex_0;
    private ImageView map_ex_1;
    private ImageView map_ex_2;
    private ImageView map_ex_3;
    private ImageView map_ex_4;
    private TextView map_item_score_one;
    private TextView map_item_dish;
    private TextView map_item_money;
    private TextView map_item_distance;
    private TextView map_closing;
    private String url_to = "";


    private String url_twe;

    private String urlUtil;

    private int mDimension;
    private ListView right_image;

    private int mGravity = Gravity.START;
    private int mOffsetX = 0;
    private int mOffsetY = 0;
    private TestPopupWindow mWindow;
    private boolean useSmartPopup = true;
    private View mPopupContentView;
    private int prilength;
    private String[] city;
    private int id_city;

    private boolean orderByGrade = false;
    private boolean open_f = false;
    private int priceLevel;
    private ArrayList<RecyclerData> datas;
    private NormalAdapter honmeAdapter;
    private String luchang;
    private List<CategoryBean> categList = new ArrayList<CategoryBean>();
    private int type_id;
    private String cfgURL;
    private static final String TAG = "FindFragment";

    @Override
    public int getLayoutId() {
        return R.layout.frag_find;
    }
    public int getContentFlag(){
        return flag;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            updateLoginTipView();
        }
    }


    enum State{
        EXPANDED,//展开
        COLLAPSED,//折叠
        INTERMEDIATE//中间状态
    }
    private State mCurrentState=State.EXPANDED;
    private float mLastDelta=0;

    public void updateLoginTipView() {
        String token = (String) SPUtil.get(getContext(), "Token", "");
        if (frag_lo != null) {
            if (token != null && token.length() == 0) {
                if(flag==1||mFromStoreDetailToOpenMap){
                    frag_lo.setVisibility(View.GONE);
                }else {
                    frag_lo.setVisibility(View.VISIBLE);
                    frag_lo.getBackground().setAlpha(170);
                }
            } else {
                frag_lo.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        float delta=((float)Math.abs(verticalOffset))/appBarLayout.getTotalScrollRange();
        float alpha=1-delta;
        mLastDelta=delta;
//        Log.d(TAG, "onOffsetChanged: alpha "+alpha);
        if (verticalOffset == 0) {
            refreshLayout.setEnableRefresh(true);
            if (mCurrentState != State.EXPANDED) {
//                        onStateChanged(appBarLayout, State.EXPANDED);
                Log.d(TAG, "onOffsetChanged: EXPANDED");

            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            refreshLayout.setEnableRefresh(false);
            if (mCurrentState != State.COLLAPSED) {
//                        onStateChanged(appBarLayout, State.COLLAPSED);
                Log.d(TAG, "onOffsetChanged: COLLAPSED");

            }
            mCurrentState = State.COLLAPSED;

        } else {
            refreshLayout.setEnableRefresh(false);
            if (mCurrentState != State.INTERMEDIATE) {
//                        onStateChanged(appBarLayout, State.INTERMEDIATE);
                Log.d(TAG, "onOffsetChanged: INTERMEDIATE");

            }
            mCurrentState = State.INTERMEDIATE;

        }
        customBanner.setAlpha(alpha);
        title_ding.getBackground().setAlpha((int)(delta*255));
        if (verticalOffset <= -toolbar.getHeight() / 2) {

            //使用下面两个CollapsingToolbarLayout的方法设置展开透明->折叠时你想要的颜色
//                    mCollapsingtoolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
//                    mCollapsingtoolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.colorAccent));
        } else {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(appBarLayout!=null) {
            appBarLayout.removeOnOffsetChangedListener(this);
        }
    }
    RecyclerViewNoBugLinearLayoutManager mLinearLm;
    @Override
    public void onViewCreatedInit() {



        mWindow = new TestPopupWindow(getActivity());
        random = new Random(20);
        random = new Random(20);
        mPopupContentView = getLayoutInflater().inflate(R.layout.popup_content_price, null);
        urlUtil = (String) SPUtil.get(getActivity(), "URL", "");
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getActivity())
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(configuration);
        imageLoader = ImageLoader.getInstance();
        cfgURL = (String) SPUtil.get(getActivity(), "cfgURL", "");
//        loading(getResources().getString(R.string.listview_loading));
        initModle();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 5);
        //设置布局管理器
        frag_recyc.setLayoutManager(layoutManager);
        datas = intData();
        honmeAdapter = new NormalAdapter(datas);
        frag_recyc.setAdapter(honmeAdapter);
        //设置增加或删除条目的动画
        frag_recyc.setItemAnimator(new DefaultItemAnimator());
        isGaoDe = isApp(getActivity(), AMAP_PACKAGENAME);
        isBAidu = isApp(getActivity(), BAIDUMAP_PACKAGENAME);
        isGOOGLE = isApp(getActivity(), GOOGLE_PACKAGENAME);




//        frag_log_ly.getBackground().setAlpha(200);

        /**
         * 检测新版本
         *
         * */
        String versionName = AppUtil.getVersionName(getActivity());
        String newestVersion = (String) SPUtil.get(getActivity(), "newestVersion", "");
        String versionName_replace = versionName.replace(".", "");
        String newestVersion_replace = newestVersion.replace(".", "");

        if (newestVersion.length() > 0) {
            int versionName_int = Integer.parseInt(versionName_replace);
            int newestVersion_int = Integer.parseInt(newestVersion_replace);
            if (versionName_int < newestVersion_int) {
                //BoxDialog
                browsDialog = new BoxDialog((Activity) getContext(), R.style.box_dialog, onClickListener);
                browsDialog.setCanceledOnTouchOutside(false);
                browsDialog.show();
                browsDialog.setCancelable(false);
            }
        }

        //接收FetchAddressIntentService返回的结果
        mResultReceiver = new AddressResultReceiver(new Handler());
        //创建GoogleAPIClient实例
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //模拟数据
        mDataAdapter = new MsgAdapter(getActivity());

        feco_list.setAdapter(mDataAdapter);
        //添加分割线
        RecycleViewDivider divider = new RecycleViewDivider(getContext(),0,ScreenUtils.dp2px(getContext(),10), Color.parseColor("#F3F4F6"));
        //mRecyclerView.setHasFixedSize(true);
        feco_list.addItemDecoration(divider);
        mLinearLm=new RecyclerViewNoBugLinearLayoutManager(getActivity());
        mLinearLm.setOrientation(LinearLayoutManager.VERTICAL);
        feco_list.setLayoutManager(mLinearLm);

        //设置下拉刷新Progress的样式
//        feco_list.setRefreshProgressStyle(ProgressStyle.SysProgress);
//        //设置下拉刷新箭头
//        //feco_list.setArrowImageView(R.drawable.load_1);
//        feco_list.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
//        feco_list.setLoadMoreEnabled(true);
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                flg = 1;
//                rows++;
//                if (prilength == 0) {
//                    if (type_id != 0) {
//                        url_to = url + "&start=" + (rows - 1) * 10 + "&rows=" + 10 + "&orderByGrade=" + orderByGrade + "&open=" + open_f + "&categoryList=" + type_id + "&typeList=" + id_city;
//                    } else {
//                        url_to = url + "&start=" + (rows - 1) * 10 + "&rows=" + 10 + "&orderByGrade=" + orderByGrade + "&open=" + open_f;
//                    }
//                } else {
//                    if (type_id != 0) {
//                        url_to = url + "&start=" + (rows - 1) * 10 + "&rows=" + 10 + "&priceLevel=" + prilength + "&orderByGrade=" + orderByGrade + "&open=" + open_f + "&categoryList=" + type_id + "&typeList=" + id_city;
//                    } else {
//                        url_to = url + "&start=" + (rows - 1) * 10 + "&rows=" + 10 + "&priceLevel=" + prilength + "&orderByGrade=" + orderByGrade + "&open=" + open_f;
//                    }
//                }
//                requestData();
            }
        });

        //设置头部加载颜色
//        feco_list.setHeaderViewColor(R.color.colorAccent, R.color.login_color, android.R.color.white);
        //设置底部加载颜色
//        feco_list.setFooterViewColor(R.color.login_color, R.color.login_color, android.R.color.white);
        //fecoList.setFooterViewColor(R.color.colorAccent, R.color.login_color ,android.R.color.white);
        //设置底部加载文字提示
//        feco_list.setFooterViewHint(getResources().getString(R.string.listview_loading), getResources().getString(R.string.listview_loading), "网络不给力啊，点击再试一次吧");
        noNetworkView.findViewById(R.id.tv_retry_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading(getResources().getString(R.string.listview_loading));
                requestData();
            }
        });
        loading(getResources().getString(R.string.listview_loading));
        //条目的点击事件
        mDataAdapter.setOnItemClickListener(new MsgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SPUtil.put(getContext(),Constants.SP_KEY_STORE_DETAIL_FROM,Constants.GO_STORE_DETAIL_FROM_DISCOVERY);
                ShopBeanNew shopBean = mDataAdapter.getDataList().get(position);
                Intent intent = new Intent(getActivity(), StoreDetailActivity.class);
                try {
                    int id = shopBean.getId();
                    intent.putExtra("id", id);
                    intent.putExtra("closingSoon", shopBean.getOperatingStatus());
//                    intent.putExtra("distances", shopBean.getDistance() + "");
                    intent.putExtra("flags", 1);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login=new Intent(getContext(), LoginMeActivity.class);
                login.putExtra("Login_flag",1);
                startActivity(login);
            }
        });

    }

    /**
     * 从餐厅详情页打开地图的场景
     * @param shopId
     */
    private boolean mFromStoreDetailToOpenMap=false;
    public boolean isFromStoreDetailToOpenMap(){
        return mFromStoreDetailToOpenMap;
    }
    public void setFromStoreDetailToOpenMap(boolean flag){
        mFromStoreDetailToOpenMap=flag;
    }
    private void backToStoreDetail(int  shopId){
        SPUtil.put(getContext(),Constants.SP_KEY_STORE_DETAIL_FROM,Constants.GO_STORE_DETAIL_FROM_MAP);
        Intent storeDetail=new Intent(getContext(),StoreDetailActivity.class);
        storeDetail.putExtra("id",shopId);
        storeDetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(storeDetail);
    }
    private List<ShopBeanNew> toShowShopList=new ArrayList<>();
    public void openStoreInMap(final int shopId){
        resetUIOnExitShowingOneStoreInMap();
        toShowShopList.clear();
        mFromStoreDetailToOpenMap=true;
        title_ding.setVisibility(View.GONE);
        backFromMapIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFromStoreDetailToOpenMap=false;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            resetUIOnExitShowingOneStoreInMap();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },200);
                backToStoreDetail(shopId);
            }
        });
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setTabLayoutVisibility(View.GONE);
        }
//        CoordinatorLayout.LayoutParams lp=(CoordinatorLayout.LayoutParams)fragFi.getLayoutParams();
//        lp.topMargin=ScreenUtils.dp2px(getContext(),20);
//        fragFi.setLayoutParams(lp);
        fragFi.setVisibility(View.VISIBLE);
        feco_list.setVisibility(View.GONE);
        fragLy.setVisibility(View.GONE);
        customBanner.setVisibility(View.GONE);
        frag_find_ly.setVisibility(View.GONE);
        appBarLayout.removeOnOffsetChangedListener(this);
        refreshLayout.setEnableRefresh(false);
        frag_lo.setVisibility(View.GONE);
        collapsingToolbarLayout.setVisibility(View.GONE);
        backFromMapIv.setVisibility(View.VISIBLE);
        List<LocationBean> storeList=new ArrayList<>();
        ShopBeanNew target=null;
        for(ShopBeanNew bean:newList){
            if(bean.getId()==shopId){
                Log.d(TAG, "openStoreInMap: bean.getId "+bean.getId()+",shopId "+shopId+",name "+bean.getName());
                target=bean;
                toShowShopList.add(target);
                break;
            }
        }
        if(target!=null) {
            for (LocationBean locationBean : locationBeens) {
                if (target.getLongitude()==locationBean.latLng.longitude&&target.getLatitude()==locationBean.latLng.latitude) {
                    Log.d(TAG, "openStoreInMap: storeList.add "+locationBean.name+","+target.getName());
                    storeList.add(locationBean);
                    break;
                }
            }

        }
        if (storeList.size() > 0) {
            LocationBean bean = storeList.get(0);
            for (int i = 0; i < mMarkerList.size(); i++) {
                Marker marker = mMarkerList.get(i);
                if (marker != null) {
                    if (marker.getPosition() != null && marker.getPosition().latitude == bean.getPosition().latitude
                            && marker.getPosition().longitude == bean.getPosition().longitude) {
                        marker.showInfoWindow();
                        Log.d(TAG, "openStoreInMap: setVisible");
                        marker.setVisible(true);
                        showStoreInfos(marker);
                    }else{
                        Log.d(TAG, "openStoreInMap: setinVisible");
                        marker.hideInfoWindow();
                        marker.setVisible(false);
                    }
                }
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(bean.latLng));
        }

    }

    private void updateUiOnMapShownOrHidden(){
        //reset CordinatorLayout's topMargin
        CoordinatorLayout.LayoutParams lp=(CoordinatorLayout.LayoutParams)fragFi.getLayoutParams();
        lp.topMargin=ScreenUtils.dp2px(getContext(),0);
        fragFi.setLayoutParams(lp);
        if (flag == 1) {
            titleMap.setBackgroundResource(R.drawable.topnav_icon_list);
            fragFi.setVisibility(View.VISIBLE);
            backFromMapIv.setVisibility(View.GONE);
            feco_list.setVisibility(View.GONE);
            collapsingToolbarLayout.setVisibility(View.VISIBLE);
            fragLy.setVisibility(View.GONE);
            titleSearch.setVisibility(View.GONE);
            whootLabelLayout.setVisibility(View.VISIBLE);
            customBanner.setVisibility(View.GONE);
            frag_find_ly.setVisibility(View.GONE);
            appBarLayout.removeOnOffsetChangedListener(this);
            if(title_ding.getBackground()!=null) {
                title_ding.getBackground().setAlpha(255);
            }
            title_ding.setVisibility(View.VISIBLE);
            refreshLayout.setEnableRefresh(false);
            frag_lo.setVisibility(View.GONE);
            if (getActivity() != null) {
                ((MainActivity) getActivity()).setTabLayoutVisibility(View.GONE);
            }
        } else if (flag == 0) {
            titleMap.setBackgroundResource(R.drawable.topnav_icon_map);
            updateLoginTipView();
            fragFi.setVisibility(View.GONE);
            backFromMapIv.setVisibility(View.GONE);
            fragLy.setVisibility(View.VISIBLE);
            collapsingToolbarLayout.setVisibility(View.VISIBLE);
            titleSearch.setVisibility(View.VISIBLE);
            whootLabelLayout.setVisibility(View.GONE);
            title_ding.setVisibility(View.VISIBLE);
            frag_find_ly.setVisibility(View.VISIBLE);
            refreshLayout.setEnableRefresh(true);
//                    title_ly.setVisibility(View.VISIBLE);
            if(frag_ding.getVisibility()==View.VISIBLE||noNetworkView.getVisibility()==View.VISIBLE){
                feco_list.setVisibility(View.GONE);
                customBanner.setVisibility(View.GONE);
                if(title_ding.getBackground()!=null) {
                    title_ding.getBackground().setAlpha(255);
                }
            }else{
                feco_list.setVisibility(View.VISIBLE);
                customBanner.setVisibility(View.VISIBLE);
                appBarLayout.addOnOffsetChangedListener(this);
                if(title_ding.getBackground()!=null) {
                    title_ding.getBackground().setAlpha((int) (mLastDelta * 255));
                }
            }

            //切换回门店列表的时候，如果地图上的InfoWindow还在显示则dismiss掉
            if (mShowStoreInfoPopWindow != null && mShowStoreInfoPopWindow.isShowing()) {
                mShowStoreInfoPopWindow.dismiss();
            }
            if (getActivity() != null) {
                ((MainActivity) getActivity()).setTabLayoutVisibility(View.VISIBLE);
            }
        }
    }
    private void forceHideStoreInfoPopWindow(){
        if (mShowStoreInfoPopWindow != null && mShowStoreInfoPopWindow.isShowing()) {
            mShowStoreInfoPopWindow.dismiss();
        }
    }
    public void resetUIOnExitShowingOneStoreInMap(){
        forceHideStoreInfoPopWindow();
        updateUiOnMapShownOrHidden();
        Manager(locationBeens);
    }

    protected ArrayList<RecyclerData> intData() {
        ArrayList<RecyclerData> mDatas = new ArrayList<RecyclerData>();
        List<String> stringList = initData();
        for (int i = 0; i < 5; i++) {
            RecyclerData recyclerData = new RecyclerData();
            recyclerData.content = stringList.get(i);
            mDatas.add(recyclerData);

        }
        return mDatas;
    }

    private List<String> initData() {
        List<String> stringList = new ArrayList<String>();
        stringList.add("$");
        stringList.add("$$");
        stringList.add("$$$");
        stringList.add("$$$$");
        stringList.add("$$$$$");
        return stringList;
    }

    private void notifyDataSetChanged() {
        mDataAdapter.notifyDataSetChanged();
    }


    private void addItems(List<ShopBeanNew> list) {

        mDataAdapter.addAll(list);
        mCurrentCounter += list.size();

    }


    private void setUpMap() {
        MainActivity activity = (MainActivity) this.getActivity();
        View marker = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        ImageView marker_img = marker.findViewById(R.id.marker_img);
        marker_img.setBackgroundResource(R.drawable.map_icon_restaurant);
        TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);
        //numTxt.setText("27");

        customMarker = mMap.addMarker(new MarkerOptions()
                .position(markerLatLng)
                .title("Title")
                .snippet("Description")

                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(), marker))));

        final View mapView = getChildFragmentManager().findFragmentById(R.id.map).getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    LatLngBounds bounds = new LatLngBounds.Builder().include(markerLatLng).build();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    //   mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                }
            });
        }
    }

    // Convert a view to bitmap
    public Bitmap createDrawableFromView(Context context, View view) {


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.vox_finsh:  //确认

                    browsDialog.dismiss();

                    mHandler.sendEmptyMessage(-4);
                    break;

            }
        }
    };

    @Override
    public void onStartInit() {
       /* boolean running = gifDrawDrawable.isRunning();
        if (!running) {
            gifDraw.setVisibility(View.GONE);
        }*/
    }

    public int open = 0;
    public int rat = 0;
    public int pri = 0;
    public int caixi = 0;
    private boolean mDishStyleFiltered;
    private boolean mConsumptionFiltered;
    private int mShowDishStyleSel;
    private int mShowConsumptionSel;

    private void sortData(){
        List<ShopBeanNew> tmp=new ArrayList<>();
        mFilterShops.clear();
        if(id_city!=0){

            for (ShopBeanNew entity : newList) {
                if (entity.getCategory()==mCategoryId&&entity.getType() == id_city) {
                    tmp.add(entity);
                }
            }
            mFilterShops.addAll(tmp);

        }else{
            tmp.addAll(newList);
            mFilterShops.addAll(tmp);
        }
        if(mLowValue!=SeekBarPressure.MIN_VALUE||mHighValue!=SeekBarPressure.MAX_VALUE) {
            tmp.clear();
            if (mFilterShops.size() == 0) {
                for (ShopBeanNew entity : newList) {
                    if(mLowValue==SeekBarPressure.MAX_VALUE&&mHighValue==SeekBarPressure.MAX_VALUE){
                        if(entity.getAvgConsumptio()>=SeekBarPressure.MAX_VALUE-SeekBarPressure.EXTRA_VALUE){
                            tmp.add(entity);
                        }
                    }else if(mLowValue!=SeekBarPressure.MAX_VALUE&&mHighValue==SeekBarPressure.MAX_VALUE){
                        if(entity.getAvgConsumptio()>=mLowValue){
                            tmp.add(entity);
                        }
                    }else if (entity.getAvgConsumptio() >= mLowValue && entity.getAvgConsumptio() <= mHighValue) {
                        tmp.add(entity);
                    }
                }
                mFilterShops.addAll(tmp);
            } else {
                for (ShopBeanNew entity : mFilterShops) {
                    if(mLowValue==SeekBarPressure.MAX_VALUE&&mHighValue==SeekBarPressure.MAX_VALUE){
                        if(entity.getAvgConsumptio()>=SeekBarPressure.MAX_VALUE-SeekBarPressure.EXTRA_VALUE){
                            tmp.add(entity);
                        }
                    }else if(mLowValue!=SeekBarPressure.MAX_VALUE&&mHighValue==SeekBarPressure.MAX_VALUE){
                        if(entity.getAvgConsumptio()>=mLowValue){
                            tmp.add(entity);
                        }
                    }else if (entity.getAvgConsumptio() >= mLowValue && entity.getAvgConsumptio() <= mHighValue) {
                        tmp.add(entity);
                    }
                }
            }

        }

        if(rat==1){
            ShopBeanNew.sSortType = Constants.SORT_TYPE.SORT_TYPE_RATING;
            Collections.sort(tmp);

        }else if(mLowValue!=SeekBarPressure.MIN_VALUE||mHighValue!=SeekBarPressure.MAX_VALUE||id_city!=0){
            ShopBeanNew.sSortType = Constants.SORT_TYPE.SORT_TYPE_DEFAULT;
            List<ShopBeanNew> closeShops=new ArrayList<>();
            List<ShopBeanNew> openingShops=new ArrayList<>();
            for(ShopBeanNew bean:tmp){
                if(bean!=null&&bean.getOperatingStatus()==2){
                    closeShops.add(bean);
                }else{
                    openingShops.add(bean);
                }
            }
            Collections.sort(openingShops);
            Collections.sort(closeShops);
            tmp.clear();
            tmp.addAll(openingShops);
            tmp.addAll(closeShops);

        }else{
            ShopBeanNew.sSortType= Constants.SORT_TYPE.SORT_TYPE_DEFAULT;
            List<ShopBeanNew> closeShops=new ArrayList<>();
            List<ShopBeanNew> openingShops=new ArrayList<>();
            for(ShopBeanNew bean:newList){
                if(bean!=null&&bean.getOperatingStatus()==2){
                    closeShops.add(bean);
                }else{
                    openingShops.add(bean);
                }
            }
            Collections.sort(openingShops);
            Collections.sort(closeShops);
            tmp.clear();
            tmp.addAll(openingShops);
            tmp.addAll(closeShops);

        }
        mDataAdapter.addAll(tmp);

    }

    private void reportSearchClick(){
        Bundle b=new Bundle();
        b.putString("type","SHOW_SEARCH");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.CLICK_DISCOVER_SEARCHBTN,b);
    }

    private void reportFilterClick(int filterBtnIndex){
        Bundle b=new Bundle();
        b.putString("type","CLICK_FILTER");
        b.putString("btn","B"+(filterBtnIndex+1));
        String btnName="";
        switch (filterBtnIndex){
            case 0:
                btnName=getString(R.string.restaurant);
                break;
            case 1:
                btnName=getString(R.string.label_average_cost);
                break;
            case 2:
                btnName=getString(R.string.label_rating);
                break;
        }
        b.putString("name",btnName);
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.CLICK_DISCOVER_FILTER,b);
    }

    private void reportPriceFilterClick(int min,int max){
        Bundle b=new Bundle();
        b.putString("type","CLICK_FILTER_PRICE");
        b.putString("min",min+"");
        b.putString("max",max+"");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.CLICK_DISCOVER_PRICEFILTERGO,b);
    }

    @OnClick({R.id.title_map, R.id.title_search, R.id.frag_set, R.id.ll_frag_find_consumption, R.id.ll_frag_find_rating, R.id.ll_frag_find_dish_style})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.title_map:
                if (flag == 0) {
                    flag = 1;
                    titleMap.setBackgroundResource(R.drawable.topnav_icon_list);
                    fragFi.setVisibility(View.VISIBLE);
                    feco_list.setVisibility(View.GONE);
                    fragLy.setVisibility(View.GONE);
                    titleSearch.setVisibility(View.GONE);
                    whootLabelLayout.setVisibility(View.VISIBLE);
                    customBanner.setVisibility(View.GONE);
                    frag_find_ly.setVisibility(View.GONE);
                    appBarLayout.removeOnOffsetChangedListener(this);
                    if(title_ding.getBackground()!=null) {
                        title_ding.getBackground().setAlpha(255);
                    }
                    refreshLayout.setEnableRefresh(false);
                    frag_lo.setVisibility(View.GONE);
                    if (getActivity() != null) {
                        ((MainActivity) getActivity()).setTabLayoutVisibility(View.GONE);
                    }
                } else if (flag == 1) {
                    flag = 0;
                    titleMap.setBackgroundResource(R.drawable.topnav_icon_map);
                    updateLoginTipView();
                    fragFi.setVisibility(View.GONE);
                    fragLy.setVisibility(View.VISIBLE);
                    titleSearch.setVisibility(View.VISIBLE);
                    whootLabelLayout.setVisibility(View.GONE);
                    title_ding.setVisibility(View.VISIBLE);
                    frag_find_ly.setVisibility(View.VISIBLE);
                    refreshLayout.setEnableRefresh(true);
//                    title_ly.setVisibility(View.VISIBLE);
                    if(frag_ding.getVisibility()==View.VISIBLE||noNetworkView.getVisibility()==View.VISIBLE){
                        feco_list.setVisibility(View.GONE);
                        customBanner.setVisibility(View.GONE);
                        if(title_ding.getBackground()!=null) {
                            title_ding.getBackground().setAlpha(255);
                        }
                    }else{
                        feco_list.setVisibility(View.VISIBLE);
                        customBanner.setVisibility(View.VISIBLE);
                        appBarLayout.addOnOffsetChangedListener(this);
                        if(title_ding.getBackground()!=null) {
                            title_ding.getBackground().setAlpha((int) (mLastDelta * 255));
                        }
                    }

                    //切换回门店列表的时候，如果地图上的InfoWindow还在显示则dismiss掉
                    if (mShowStoreInfoPopWindow != null && mShowStoreInfoPopWindow.isShowing()) {
                        mShowStoreInfoPopWindow.dismiss();
                    }
                    if (getActivity() != null) {
                        ((MainActivity) getActivity()).setTabLayoutVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.title_search: //搜索
                reportSearchClick();
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.frag_set:
                final CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setTitle(String.format(getString(R.string.ding)));
                builder.setMessage(String.format(getString(R.string.wei)));
                builder.setPositiveButton(R.string.lue, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.determine, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
                builder.create().show();
                break;
            case R.id.ll_frag_find_dish_style:
                reportFilterClick(0);
                if(mShowDishStyleSel==0){
                    showDishStylePopwindow();
                    mShowDishStyleSel=1;
                    dishStyleSelIv.setImageResource(R.drawable.storedetail_arrow_up);
                    frag_find_dish_style.setTextColor(getResources().getColor(R.color.main_gold));
                }else{

                }

                break;
            case R.id.ll_frag_find_rating:
                reportFilterClick(2);
                if(mPopupWindow!=null&&mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                if(mConsumpPopwindow!=null&&mConsumpPopwindow.isShowing()){
                    mConsumpPopwindow.dismiss();
                }
//                refreshLayout.setNoMoreData(false);
                if (rat == 0) {
                    rat = 1;
//                    orderByGrade = true;
//                    frag_find_rating.setBackgroundResource(R.drawable.home_shape_pai);
                    frag_find_rating.setTextColor(getResources().getColor(R.color.main_gold));
                    sortData();

                } else if (rat == 1) {
                    rat = 0;
//                    orderByGrade = false;
//                    frag_find_rating.setBackgroundResource(R.drawable.home_shape_xu);
                    frag_find_rating.setTextColor(getResources().getColor(R.color.main_tab_defaule));
                    sortData();
                }
                break;
            case R.id.ll_frag_find_consumption:
                reportFilterClick(1);
                if(mShowConsumptionSel==0){
                    showConsumptionSelPopWindow();
                    mShowConsumptionSel=1;
                    consumptionSelIv.setImageResource(R.drawable.storedetail_arrow_up);
                    frag_find_consumption.setTextColor(getResources().getColor(R.color.main_gold));
                }else{

                }

                break;
        }
    }
    private static final long HALF_HOUR=30*60*1000;

    private void initShopBeanExactOpeningTime(){
        for(ShopBeanNew bean:newList){
            if(bean!=null){
                if(bean.getOpeningTmFrist()!=0) {
                    bean.setOpeningTmFrist(TimeUtil.getExactTime(bean.getOpeningTmFrist(), serverTime));
                }
                if(bean.getOpeningTmSecond()!=0) {
                    bean.setOpeningTmSecond(TimeUtil.getExactTime(bean.getOpeningTmSecond(), serverTime));
                }
                if(bean.getCloseTmFrist()!=0) {
                    bean.setCloseTmFrist(TimeUtil.getExactTime(bean.getCloseTmFrist(), serverTime));
                }
                if(bean.getCloseTmSecond()!=0) {
                    bean.setCloseTmSecond(TimeUtil.getExactTime(bean.getCloseTmSecond(), serverTime));
                }
            }

        }
    }
    private void initShopBeanOpenStatus(){
        initShopBeanExactOpeningTime();
        for(ShopBeanNew bean:newList){
            if(bean!=null){
                if(bean.getCloseTmFrist()!=0&&bean.getCloseTmSecond()!=0&&(serverTime>=bean.getCloseTmFrist()&&serverTime<bean.getOpeningTmSecond()||serverTime<bean.getOpeningTmFrist()||serverTime>=bean.getCloseTmSecond())
                        ||(bean.getCloseTmFrist()==0&&bean.getCloseTmSecond()!=0&&(serverTime<bean.getOpeningTmSecond()||serverTime>=bean.getCloseTmSecond()))
                        ||(bean.getCloseTmFrist()!=0&&bean.getCloseTmSecond()==0&&(serverTime<bean.getOpeningTmFrist()||serverTime>=bean.getCloseTmFrist()))
                        ||(bean.getCloseTmFrist()==0&&bean.getCloseTmSecond()==0)){//已关门
//                if(serverTime>=bean.getCloseTmFrist()&&serverTime<bean.getOpeningTmSecond()||serverTime<bean.getOpeningTmFrist()||serverTime>=bean.getCloseTmSecond()){//已关门
                    bean.setOperatingStatus(Constants.OPEN_STATUS_CLOSED);
                }else if(serverTime>=bean.getOpeningTmFrist()&&serverTime+HALF_HOUR<bean.getCloseTmFrist()||
                        serverTime>=bean.getOpeningTmSecond()&&serverTime+HALF_HOUR<bean.getCloseTmSecond()){//已开门
                    bean.setOperatingStatus(Constants.OPEN_STATUS_OPEND);
                }else{//即将关门
                    bean.setOperatingStatus(Constants.OPEN_STATUS_CLOSEING_SOON);
                }
            }

        }
    }

    private void showConsumptionSelPopWindow() {
        View contentView = getConsumptionSelPopupWindow();
        View darkLayer=contentView.findViewById(R.id.dark_layer);
        mConsumpPopwindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        mConsumpPopwindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

        // 设置好参数之后再show

        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int xOffset = consumptionLayout.getWidth() / 2 - contentView.getMeasuredWidth() / 2;

        mConsumpPopwindow.showAsDropDown(title_ding, xOffset, dishStyleLayout.getHeight());    // 在mButton2的中间显示
        appBarLayout.setExpanded(false);
//        WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
//        lp.alpha=0.6f;
//        getActivity().getWindow().setAttributes(lp);
        darkLayer.setAlpha(0.8f);
        darkLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConsumpPopwindow.dismiss();
            }
        });
        mConsumpPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
//                lp.alpha=1f;
//                getActivity().getWindow().setAttributes(lp);
                mShowConsumptionSel=0;
                frag_find_consumption.setTextColor(getResources().getColor(R.color.color_9c9c9c));
                consumptionSelIv.setImageResource(R.drawable.arrow_down);
            }
        });
    }
    private double mProgressLow=0;
    private double mProgressHigh=100;
    private int mLowValue=SeekBarPressure.MIN_VALUE;
    private int mHighValue=SeekBarPressure.MAX_VALUE;

    private void updateViewOnPriceChanged(SeekBarPressure seekBarPressure,TextView priceTv){
        double progressLow=seekBarPressure.getProgressLow();
        double progressHigh=seekBarPressure.getProgressHigh();
        int minUnit=seekBarPressure.getMinUnit();
        int lowValue=(int)(progressLow*SeekBarPressure.MAX_VALUE/100);
        int highValue=(int)(progressHigh*SeekBarPressure.MAX_VALUE/100);
        if(lowValue%minUnit>=minUnit/2){
            lowValue=(lowValue/minUnit+1)*minUnit;
        }else{
            lowValue=(lowValue/minUnit)*minUnit;
        }
        if(highValue%minUnit>=minUnit/2){
            highValue=(highValue/minUnit+1)*minUnit;
        }else{
            highValue=(highValue/minUnit)*minUnit;
        }
        mLowValue=lowValue;
        mHighValue=highValue;
        Log.d(TAG, "onClick: progressLow "+progressLow+",progressHigh "+progressHigh);
        Log.d(TAG, "onClick: lowValue "+lowValue+",highValue "+highValue);
        if(lowValue==SeekBarPressure.MIN_VALUE&&highValue==SeekBarPressure.MAX_VALUE){
            priceTv.setText(getResources().getString(R.string.label_consumption));
        }else if(lowValue==SeekBarPressure.MIN_VALUE&&highValue<SeekBarPressure.MAX_VALUE){
            priceTv.setText(highValue+getResources().getString(R.string.label_average_cost_less_than));
        }else if(lowValue>=SeekBarPressure.MAX_VALUE-SeekBarPressure.EXTRA_VALUE&&highValue<=SeekBarPressure.MAX_VALUE&&lowValue<=highValue){
            if(lowValue==highValue&&lowValue==SeekBarPressure.MAX_VALUE-SeekBarPressure.EXTRA_VALUE){
                priceTv.setText((SeekBarPressure.MAX_VALUE - SeekBarPressure.EXTRA_VALUE) + getResources().getString(R.string.label_average_cost));
            }else {
                priceTv.setText((SeekBarPressure.MAX_VALUE - SeekBarPressure.EXTRA_VALUE) + getResources().getString(R.string.label_average_cost_more_than));
            }
        }else if(lowValue>SeekBarPressure.MIN_VALUE&&highValue==SeekBarPressure.MAX_VALUE){
            priceTv.setText(lowValue+getResources().getString(R.string.label_average_cost_more_than));
        }else if(lowValue==highValue){
            priceTv.setText(lowValue+getResources().getString(R.string.label_average_cost));
        }else{
            priceTv.setText(String.format(getResources().getString(R.string.label_average_cost_between),lowValue,highValue));
        }
        mProgressHigh=progressHigh;
        mProgressLow=progressLow;
    }
    private View getConsumptionSelPopupWindow() {
        // 一个自定义的布局，作为显示的内容
        int layoutId = R.layout.layout_consumption_sel;   // 布局ID
        View contentView = LayoutInflater.from(getActivity()).inflate(layoutId, null);
        SeekBarPressure seekBarPressure=contentView.findViewById(R.id.seekBar);
        TextView priceTv=contentView.findViewById(R.id.tv_price);
        TextView confirmTv=contentView.findViewById(R.id.tv_confirm);
        confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double progressLow = seekBarPressure.getProgressLow();
                double progressHigh = seekBarPressure.getProgressHigh();
                int lowValue = (int) (progressLow * SeekBarPressure.MAX_VALUE / 100);
                int highValue = (int) (progressHigh * SeekBarPressure.MAX_VALUE / 100);
                reportPriceFilterClick(lowValue,highValue);
                updateViewOnPriceChanged(seekBarPressure, frag_find_consumption);
                mConsumpPopwindow.dismiss();
                sortData();
                checkToShowNoDataView();
            }
        });
        seekBarPressure.setProgressHigh(mProgressHigh);
        seekBarPressure.setProgressLow(mProgressLow);
        seekBarPressure.setOnSeekBarChangeListener(new SeekBarPressure.OnSeekBarChangeListener() {
            @Override
            public void onProgressBefore() {

            }

            @Override
            public void onProgressChanged(SeekBarPressure seekBar, double progressLow, double progressHigh) {
                Log.d(TAG, "onProgressChanged: "+progressLow+","+progressHigh);
                updateViewOnPriceChanged(seekBarPressure,priceTv);
            }

            @Override
            public void onProgressAfter() {

            }
        });
        //contentView.findViewById(R.id.menu_item1).setOnClickListener(menuItemOnClickListener);

        return contentView;
    }

    /**
     * 菜系筛选弹窗
     */
    private void showDishStylePopwindow() {
        if (Chcitys == null) {
            ToastUtil.showgravity(getActivity(), getResources().getString(R.string.jia));
            return;
        }
        View contentView = getPopupWindowContentView();
        View darkLayer=contentView.findViewById(R.id.dark_layer);
        darkLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

//        mPopupWindow.setHeight(SCREEN_HEIGHT / 2 * 1);
        // 设置好参数之后再show

        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int xOffset = title_ding.getWidth() / 2 - contentView.getMeasuredWidth() / 2;
        appBarLayout.setExpanded(false);
        mPopupWindow.showAsDropDown(title_ding, xOffset, dishStyleLayout.getHeight());    // 在mButton2的中间显示
        darkLayer.setAlpha(0.8f);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                title_image.setBackgroundResource(R.drawable.ropnav_icon_cuisine_n);

                mShowDishStyleSel=0;
                frag_find_dish_style.setTextColor(getResources().getColor(R.color.color_9c9c9c));
                dishStyleSelIv.setImageResource(R.drawable.arrow_down);
            }
        });
    }

    private List<Map<String, String>> mainList;
    private String Sub;
    private int Lift_T = 0;
    private void checkToShowNoDataView(){
        if(mDataAdapter.getDataList().size()==0){
            noNetworkView.setVisibility(View.GONE);
            frag_ding.setVisibility(View.GONE);
            fragLy.setVisibility(View.VISIBLE);
            frag_img.setVisibility(View.VISIBLE);
            frag_ly_txt.setVisibility(View.VISIBLE);
            feco_list.setVisibility(View.GONE);
        }else{
            noNetworkView.setVisibility(View.GONE);
            frag_ding.setVisibility(View.GONE);
            fragLy.setVisibility(View.GONE);
            feco_list.setVisibility(View.VISIBLE);
        }
    }
    private View getPopupWindowContentView() {

        // 一个自定义的布局，作为显示的内容
        int layoutId = R.layout.popup_content_layout;   // 布局ID
        View contentView = LayoutInflater.from(getActivity()).inflate(layoutId, null);
        ListView lefte_image = contentView.findViewById(R.id.lefte_image);
        right_image = contentView.findViewById(R.id.right_image);
        LefteAdapter lefte = new LefteAdapter(getContext(), mainList);
        lefte.setSelectItem(0);
        lefte_image.setAdapter(lefte);
        luchang = (String) SPUtil.get(getActivity(), "luchang", "");
        boolean zh = TimeUtil.isZh(getActivity());
        if (luchang.equals("0")) {
            if (zh) {
                luchang = "3";
            } else {
                luchang = "2";
            }
        }
        lefte_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SPUtil.put(getActivity(), "arg_lefte", position);
                if(position==0){

                    id_city=0;
                    mCategoryId=0;

                    sortData();

                    Lift_T = 0;
                    mPopupWindow.dismiss();
                    SPUtil.remove(getActivity(), "arg_right_name");
                    SPUtil.remove(getActivity(), "arg_lefte");
                    frag_find_dish_style.setText(getResources().getString(R.string.label_all_food));

                    return;
                }

                if (luchang.equals("1") || luchang.equals("2") || luchang.equals("0")) {
                    initAdapter(Chcitys[position-1]);
                    lefte.setSelectItem(position);
                    lefte.notifyDataSetChanged();
                    city = Chcitys[position-1];
                } else if (luchang.equals("3")) {
                    initAdapter(citys[position-1]);
                    lefte.setSelectItem(position);
                    lefte.notifyDataSetChanged();
                    city = citys[position-1];
                }
                Lift_T = position-1;
            }
        });
        lefte_image.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        int arg_lefte = (int) SPUtil.get(getActivity(), "arg_lefte", 0);
        if(arg_lefte!=0) {
            // 一定要设置这个属性，否则ListView不会刷新
            if (luchang.equals("1") || luchang.equals("2") || luchang.equals("0")) {
                initAdapter(Chcitys[arg_lefte-1]);
            } else if (luchang.equals("3")) {
                initAdapter(citys[arg_lefte-1]);
            }
        }

        return contentView;
    }

    RightAdapter moreAdapter;
    private List<ShopBeanNew> mFilterShops=new ArrayList<>();
    private int mCategoryId;
    private void initAdapter(String[] array) {

        moreAdapter = new RightAdapter(getActivity(), array);
        right_image.setAdapter(moreAdapter);
        moreAdapter.notifyDataSetChanged();
        right_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ToastUtil.showgravity(getActivity(), array[position]);
                String right_name = array[position];
                frag_find_dish_style.setText(right_name);
                SPUtil.put(getActivity(), "arg_right_name", right_name);
                Log.d("qwefsf", right_name);
                mCategoryId=categoryBeanList.get(Lift_T).getId();
                Log.d(TAG, "onItemClick: mCategoryId "+mCategoryId);
                if (luchang.equals("1") || luchang.equals("2") || luchang.equals("0")) {

                    List<CategoryBean.SubBean> sub = categoryBeanList.get(Lift_T).getSub();
                    type_id = Lift_T + 1;

                    for (int i = 0; i < sub.size(); i++) {
                        String nameCh = sub.get(i).getNameCh();
                        if (right_name.equals(nameCh)) {
                            id_city = sub.get(i).getId();
                            Log.d(TAG, "onItemClick: id_city "+id_city);
                            break;
                        }
                    }
                } else if (luchang.equals("3")) {
                    List<CategoryBean.SubBean> sub = categoryBeanList.get(Lift_T).getSub();
                    type_id = Lift_T + 1;
                    for (int i = 0; i < sub.size(); i++) {
                        String nameEn = sub.get(i).getNameEn();
                        if (right_name.equals(nameEn)) {
                            id_city = sub.get(i).getId();
                            break;
                        }
                    }
                }
                mPopupWindow.dismiss();
                Log.d(TAG, "onItemClick: id_city "+id_city);
                sortData();
                checkToShowNoDataView();
            }
        });
    }



    private String[][] citys;
    private String[][] Chcitys;
    private List<CategoryBean> categoryBeanList = new ArrayList<CategoryBean>();
    private List<CategoryBean> ChBeanList = new ArrayList<CategoryBean>();
    private List<CfgBean> cfgList = new ArrayList<>();

    private void initModle() {
        mainList = new ArrayList<Map<String, String>>();
        Http.OkHttpGet(getActivity(), cfgURL, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                if (isDetached()) {
                    return;
                }

                Location();
                Log.d("array", result + "====" + cfgURL);
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("0")) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        JSONObject location = data.getJSONObject("Location");
                        String currency = location.getString("Currency");
                        SPUtil.put(getActivity(), "currency", currency);
                        String timeLag = location.getString("TimeLag");
                        SPUtil.put(getActivity(), "timeLag", timeLag);
                        JSONArray category = location.getJSONArray("Category");
                        citys = new String[category.length()][];
                        Chcitys = new String[category.length()][];
                        JSONArray price=location.optJSONArray("priceScope");
                        if(price!=null&&price.length()>0) {
                            for (int i = 0; i < price.length(); i++) {
                                PriceScopeBean bean = GSonUtil.parseGson(price.getString(i), PriceScopeBean.class);
                                MyApplication.getInstance().getPriceScopeBeanMap().put(bean.getLevel(), bean);
                            }
                        }
                        Map<String, String> dishStyleHeader = new HashMap<String, String>();
                        dishStyleHeader.put("NameEn", getResources().getString(R.string.label_all_food));
                        dishStyleHeader.put("NameCh", getResources().getString(R.string.label_all_food));
                        mainList.add(dishStyleHeader);
                        for (int i = 0; i < category.length(); i++) {
                            CategoryBean ban = GSonUtil.parseGson(category.getString(i), CategoryBean.class);
                            categList.add(ban);
                            Map<String, String> map = new HashMap<String, String>();
                            String nameEn = category.getJSONObject(i).getString("NameEn");
                            String nameCn = category.getJSONObject(i).getString("NameCh");
                            map.put("NameEn", nameEn);
                            map.put("NameCh", nameCn);
                            mainList.add(map);
                            JSONArray sub = category.getJSONObject(i).getJSONArray("Sub");
                            String[] citysCol = new String[sub.length()];
                            String[] ChcitysCol = new String[sub.length()];
                            for (int a = 0; a < citysCol.length; a++) {
                                citysCol[a] = sub.getJSONObject(a).getString("NameEn");
                            }
                            citys[i] = citysCol;
                            CategoryBean bean = GSonUtil.parseGson(category.getString(i), CategoryBean.class);
                            categoryBeanList.add(bean);
                            for (int q = 0; q < ChcitysCol.length; q++) {
                                ChcitysCol[q] = sub.getJSONObject(q).getString("NameCh");
                            }
                            Chcitys[i] = ChcitysCol;
                            ChBeanList.add(bean);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dissLoad();
                }
            }

            @Override
            public void onError(Exception e) {
                if (isDetached()) {
                    return;
                }
                dissLoad();
                if (noNetworkView != null) {
                    noNetworkView.setVisibility(View.VISIBLE);
                }
                if(title_ding.getBackground()!=null){
                    title_ding.getBackground().setAlpha(255);
                }
                appBarLayout.removeOnOffsetChangedListener(FindFragment.this);
                customBanner.setVisibility(View.GONE);
                feco_list.setVisibility(View.GONE);
                fragLy.setVisibility(View.GONE);
                frag_ding.setVisibility(View.GONE);
                Location();
            }
        });
    }

    private MarkerOptions mMarkOption;

    @Override
    public void onMapReady(GoogleMap googleMap) {
       /* //添加标记

        //移动摄像头
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleLatLng, 13));*/
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnMapClickListener(this);
        enableMyLocation();
        mMap.setOnMarkerClickListener(this);
        //添加定制聚类标记
        //初始化聚类管理器
        clusterManager = new ClusterManager<LocationBean>(getActivity(), mMap);
        //设置聚类标记渲染样式
        clusterManager.setRenderer(new LocationRenderer(getApplicationContext(), mMap, clusterManager));
        //设置地图摄像头控制监听
//        mMap.setOnCameraIdleListener(clusterManager);
        //设置地图标记点击事件监听
//        mMap.setOnMarkerClickListener(clusterManager);


        //设置聚类管理器聚合聚类标记点击事件
        clusterManager.setOnClusterClickListener(this);
        //设置聚类管理器普通聚类标记点击事件
        clusterManager.setOnClusterItemClickListener(this);


       /* mMarkOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromAsset("map_icon_coffee.png"));
        mMarkOption.draggable(true);*/




      /*  List<String> lalist=new ArrayList<String>();
        lalist.add("22.5334086550,114.0441799164");
        lalist.add("22.5356657,114.0529055");
        LatLng latlng = new LatLng(22.5356657,114.0529055);
        mMarkOption.position(latlng);
        mMap.addMarker(mMarkOption);
        LatLng latlng1 = new LatLng(22.5334086550,114.0441799164);

        mMarkOption.position(latlng1);
        for (int i = 0; i <2 ; i++) {
            String s = lalist.get(i);
            String[] split = s.split(",");
            String s1 = split[0];
            String s2 = split[1];
            mMarkOption.position(new LatLng(Double.parseDouble(s1),Double.parseDouble(s2)));
        }

       // mMarkOption.title("title");
        //mMarkOption.snippet("snippet");
        mMap.addMarker(mMarkOption);*/
        mMap.setInfoWindowAdapter(this);

        /*markerLatLng = new LatLng(22.5356657,114.0529055);
         setUpMapIfNeeded();
        markerLatLng=new LatLng(22.5334086550,114.0441799164);
        setUpMapIfNeeded();*/

    }


    private void popwindow(String title, String replace) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_ditu, null);
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
        popupWindow.setTouchable(true);

        popupWindow.showAtLocation(map_txt, Gravity.BOTTOM, 0, 0);
        name1 = title;
        String[] split = replace.split(",");
        latitude = Double.parseDouble(split[0]);
        longitude = Double.parseDouble(split[1]);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_map_gd_ly:
                SPUtil.put(getActivity(),"flag_map",3);
                /*Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("amapuri://route/plan/?slat="
                        + lastidu + "&slon=" + longtidu
                        + "&dlat=" + latitude + "&dlon=" + longitude +"&dev=0&t=4")); //&dname="+name+"*/
                try {
                    Intent intent = new Intent(
                            "android.intent.action.VIEW",
                            Uri.parse(
                                    "androidamap://route?sourceApplication=Whoot!" + "&dlat=" + latitude//终点的经度
                                            + "&dlon=" + longitude//终点的纬度
                                            + "&dname=" + name1
                                            + "&dev=0" + "&t=1"));
                    intent.setPackage(AMAP_PACKAGENAME);
                    startActivity(intent);
                    popupWindow.dismiss();
                } catch (Exception e) {
                  //  ToastUtil.showgravity(getActivity(), getResources().getString(R.string.map_enable));
                }
                break;
            case R.id.pop_map_bd_ly:
                /* Intent intent1 = new Intent("android.intent.action.VIEW", Uri.parse("baidumap://map/walknavi?origin="
                         + lastidu + "," + longtidu
                         + "&destination=" + latitude + "," + longitude));
                 intent1.setPackage(BAIDUMAP_PACKAGENAME);
                 startActivity(intent1);
                popupWindow.dismiss();*/
                break;
            case R.id.pop_map_blank:
                popupWindow.dismiss();
                break;
            case R.id.pop_map_cancle:
                popupWindow.dismiss();
                break;
            case R.id.pop_map_google_ly:
                SPUtil.put(getActivity(),"flag_map",2);
                try {
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
                    } else {
                        Intent i = new Intent(
                                Intent.ACTION_VIEW,
                                Uri
                                        .parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=" + latitude + "," + longitude));
                        startActivity(i);
                    }
                } catch (Exception e) {
                  //  ToastUtil.showgravity(getActivity(), getResources().getString(R.string.map_enable));
                }

                break;
        }
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "onMarkerClick: ");
        marker.showInfoWindow();
        showStoreInfos(marker);
        return false;
    }


    private void showStoreInfos(Marker locationBean) {
//        String id = marker.getId();
//        String title = marker.getTitle();
        //LayoutInflater mInflater = LayoutInflater.from(getActivity()); //this即activity的context
        LatLng position = locationBean.getPosition();
        String s = position.toString();
        String replace = s.replace("(", "");
        String replace1 = replace.replace(")", "");
        String replace2 = replace1.replace("lat/lng:", "");
        show1(replace2);

        if (mInfoWindowContent == null) {

            /*mInfoWindowContent = mInflater.inflate(R.layout.map_pop, null);
            LinearLayout map_ly = (LinearLayout)mInfoWindowContent.findViewById(R.id.map_ly);
            map_txt = (TextView)mInfoWindowContent.findViewById(R.id.map_txt);
            map_item_score = (TextView)mInfoWindowContent.findViewById(R.id.map_item_score);
            map_ex_0 = (ImageView)mInfoWindowContent.findViewById(R.id.map_ex_0);
            map_ex_1 = (ImageView)mInfoWindowContent.findViewById(R.id.map_ex_1);
            map_ex_2 = (ImageView)mInfoWindowContent.findViewById(R.id.map_ex_2);
            map_ex_3 = (ImageView)mInfoWindowContent.findViewById(R.id.map_ex_3);
            map_ex_4 = (ImageView)mInfoWindowContent.findViewById(R.id.map_ex_4);
            map_item_score_one = (TextView)mInfoWindowContent.findViewById(R.id.map_item_score_one);
            map_item_distance = (TextView)mInfoWindowContent.findViewById(R.id.map_item_distance);
            map_item_dish = (TextView)mInfoWindowContent.findViewById(R.id.map_item_dish);
            map_item_money = (TextView)mInfoWindowContent.findViewById(R.id.map_item_money);
            map_closing = (TextView)mInfoWindowContent.findViewById(R.id.map_closing);*/
        }
    }


    @Override
    public void onCameraIdle() {
        Log.d(TAG, "onCameraIdle: ");


    }
    private void popwindow() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_ditu, null);
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
        //homeListview.setFocusable(false);
        popupWindow.showAtLocation(fragFi, Gravity.BOTTOM, 0, 0);
    }
    private List<View> loadStoreInfoViews(PopupWindow storeInfosDlg,List<ShopBeanNew> list_map) {
        List<View> views = new ArrayList<>();
        for (int i = 0; i < list_map.size(); i++) {
            View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_content_normal, null);
            ShopBeanNew bean = list_map.get(i);
            map_txt = (TextView) contentView.findViewById(R.id.dialog_map_txt_new);
            ImageView dialog_img = contentView.findViewById(R.id.dialog_img_new);
            TextView nameEnTv = (TextView) contentView.findViewById(R.id.tv_name_en_new);
            TextView map_item_score = (TextView) contentView.findViewById(R.id.dialog_map_item_score_new);
            LinearLayout map_ly = (LinearLayout) contentView.findViewById(R.id.dialog_map_ly_new);
            ImageView map_ex_0 = (ImageView) contentView.findViewById(R.id.dialog_map_ex_0_new);
            ImageView map_ex_1 = (ImageView) contentView.findViewById(R.id.dialog_map_ex_1_new);
            ImageView map_ex_2 = (ImageView) contentView.findViewById(R.id.dialog_map_ex_2_new);
            ImageView map_ex_3 = (ImageView) contentView.findViewById(R.id.dialog_map_ex_3_new);
            ImageView map_ex_4 = (ImageView) contentView.findViewById(R.id.dialog_map_ex_4_new);
            TextView map_item_score_one = (TextView) contentView.findViewById(R.id.dialog_map_item_score_one_new);
            TextView map_item_distance = (TextView) contentView.findViewById(R.id.dialog_map_item_distance_new);
            TextView map_item_dish = (TextView) contentView.findViewById(R.id.dialog_map_item_dish_new);
            TextView map_item_money = (TextView) contentView.findViewById(R.id.dialog_map_item_money);
            TextView map_closing = (TextView) contentView.findViewById(R.id.dialog_map_closing_new);
            LinearLayout dialog_map_ly = contentView.findViewById(R.id.dialog_map_ly);
            LinearLayout map_msg_img_ly = contentView.findViewById(R.id.map_msg_img_ly);
            ImageView map_msg_mon_0 = contentView.findViewById(R.id.map_msg_mon_0);
            ImageView map_msg_mon_1 = contentView.findViewById(R.id.map_msg_mon_1);
            ImageView map_msg_mon_2 = contentView.findViewById(R.id.map_msg_mon_2);
            ImageView map_msg_mon_3 = contentView.findViewById(R.id.map_msg_mon_3);
            ImageView map_msg_mon_4 = contentView.findViewById(R.id.map_msg_mon_4);
            TextView dialog_map_price = contentView.findViewById(R.id.dialog_map_price);
            ImageView dialog_item_image = contentView.findViewById(R.id.dialog_item_image);
            TextView statusTv=contentView.findViewById(R.id.tv_status);
            View statusLl=contentView.findViewById(R.id.ll_status);
            LocationBean tmp =null;
            for(int  j=0;j<locationBeens.size();j++){
                if(locationBeens.get(j).getPosition().longitude==bean.getLongitude()&&locationBeens.get(j).getPosition().latitude==bean.getLatitude()){
                    tmp=locationBeens.get(j);
                    break;
                }

            }
            if(tmp==null){
                return views;
            }
            LocationBean locationBean=tmp;
            String s = locationBean.getPosition().toString();
            if(mFromStoreDetailToOpenMap) {
                dialog_img.setVisibility(View.GONE);
            }else{
                dialog_img.setVisibility(View.VISIBLE);
            }

            Glide.with(this)
                    .load(bean.getShopLogo())
                    .skipMemoryCache(true)
                    .error(R.drawable.storedetail_top5_blank_pic)
                    .into(new GlideDrawableImageViewTarget(dialog_item_image, 1));



            String replace = s.replace("(", "");
            String replace1 = replace.replace(")", "");
            String replace2 = replace1.replace("lat/lng:", "");
            storeInfosDlg.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    //清除掉上一次点击后显示的InfoWindow
                    mMap.clear();
                    for (LocationBean locationBean : locationBeens) {
                        Marker marker = mMap.addMarker(new MarkerOptions().position(locationBean.latLng).title(locationBean.name).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.map_icon_restaurant))));
                        marker.setTag(locationBean);
                    }

                }
            });
            dialog_map_ly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (storeInfosDlg != null && storeInfosDlg.isShowing()) {
                        storeInfosDlg.dismiss();
                    }
                    popwindow(bean.getName(), replace2);
                }
            });
            dialog_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (storeInfosDlg != null && storeInfosDlg.isShowing()) {
                        storeInfosDlg.dismiss();
                    }

                }
            });

            map_txt.setText(locationBean.name);
            if (TextUtils.isEmpty(bean.getNameEn())) {
                nameEnTv.setVisibility(View.GONE);
            } else {
                nameEnTv.setVisibility(View.VISIBLE);
            }
            nameEnTv.setText(bean.getNameEn());
            int operatingStatus = bean.getOperatingStatus();
            if (operatingStatus==1){
                statusTv.setText(R.string.soon);
                statusLl.setVisibility(View.VISIBLE);
            }else if (operatingStatus==2){
                statusTv.setText(R.string.closs_wei);
                statusLl.setVisibility(View.VISIBLE);
            }else if (operatingStatus==0){
                statusTv.setText(R.string.open);
                statusLl.setVisibility(View.GONE);
            }
            if(bean.getAvgConsumptio()==0){
                dialog_map_price.setVisibility(View.GONE);
            }else{
                dialog_map_price.setVisibility(View.VISIBLE);
            }
            dialog_map_price.setText(String.format(getContext().getResources().getString(R.string.label_hkd_person),bean.getAvgConsumptio()));
            double latitude = list_map.get(i).getLatitude();
            double longitude = list_map.get(i).getLongitude();
            String s1 = " " + latitude + "," + longitude;
//            if (replace.equals(s1)) {
            double grade = list_map.get(i).getGrade();
            if (grade <= 5 && grade >= 4.5) {
                map_item_score.setBackgroundResource(R.drawable.home_shape);
            } else if (grade < 4.5 && grade >= 4) {
                map_item_score.setBackgroundResource(R.drawable.home_shape_four);
            } else if (grade < 4 && grade >= 3) {
                map_item_score.setBackgroundResource(R.drawable.home_shape_three);
            } else if (grade < 3) {
                map_item_score.setBackgroundResource(R.drawable.home_shape_twe);
            }
            if(grade==0){
                map_item_score.setText(getString(R.string.not_rating));
            }else {
                map_item_score.setText(grade + "");
            }

            /**
             * 跳转门店详情
             * */
            ShopBeanNew shop=list_map.get(i);
            int shopId = shop.getId();
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SPUtil.put(getContext(),Constants.SP_KEY_STORE_DETAIL_FROM,Constants.GO_STORE_DETAIL_FROM_MAP);
                    Intent intent = new Intent(getApplicationContext(), StoreDetailActivity.class);
                    intent.putExtra("closingSoon", shop.getOperatingStatus());
                    intent.putExtra("distances", shop.getDistance() + "");
                    intent.putExtra("id", shopId);
                    intent.putExtra("comment", 1);
                    startActivity(intent);
                    if (mShowStoreInfoPopWindow != null && mShowStoreInfoPopWindow.isShowing()) {
                        if(!mFromStoreDetailToOpenMap) {
                            mShowStoreInfoPopWindow.dismiss();
                        }
                    }
                }
            });
            map_ly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int flag_map = (int) SPUtil.get(getActivity(), "flag_map", 0);
                   if (flag_map==1){   //百度
                       Intent intent = new Intent(Intent.ACTION_VIEW);
                       intent.setData(Uri.parse("baidumap://map/direction?origin=我的位置&destination=name:"
                               + locationBean.name
                               + "|latlng:" + latitude + "," + longitude
                               + "&mode=transit&sy=3&index=0&target=1"));
                       startActivity(intent);
                       if (popupWindow!=null){
                           popupWindow.dismiss();
                       }

                    }else if (flag_map==2){ //google
                       try{
                           if (isGOOGLE) {
                               Intent i = new Intent(
                                       Intent.ACTION_VIEW,
                                       Uri
                                               .parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=" + latitude + "," + longitude + "(" + locationBean.name + ")"));
                               //    .parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=31.1198723,121.1099877(上海青浦大街100号)"));
                               i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                       & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                               i.setClassName("com.google.android.apps.maps",
                                       "com.google.android.maps.MapsActivity");
                               startActivity(i);

                           } else {
                               Intent i = new Intent(
                                       Intent.ACTION_VIEW,
                                       Uri
                                               .parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=" + latitude + "," + longitude));
                               startActivity(i);

                           }
                       }catch (Exception e){
                           ToastUtil.showgravity(getActivity(),getResources().getString(R.string.map_enable));
                       }

                   }else if (flag_map==3){ //高德
                       try{
                           Intent intent = new Intent(
                                   "android.intent.action.VIEW",
                                   android.net.Uri.parse(
                                           "androidamap://route?sourceApplication=Whoot!" + "&dlat=" + latitude//终点的经度
                                                   + "&dlon=" + longitude//终点的纬度
                                                   + "&dname=" + locationBean.name
                                                   + "&dev=0" + "&t=1"));

                           intent.setPackage(AMAP_PACKAGENAME);
                           startActivity(intent);

                       }catch (Exception e){
                          // ToastUtil.showgravity(getActivity(),getResources().getString(R.string.map_enable));
                       }
                    }else {
                       popwindow();
                    }
                }
            });
            try {
                priceLevel = list_map.get(i).getPriceLevel();

            } catch (Exception e) {
                priceLevel = 0;
            }
            map_msg_img_ly.setVisibility(View.VISIBLE);
            if (priceLevel == 1) {
                map_msg_mon_0.setBackgroundResource(R.drawable.home_icon_pricerange_s);
            } else if (priceLevel == 2) {
                map_msg_mon_0.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                map_msg_mon_1.setBackgroundResource(R.drawable.home_icon_pricerange_s);
            } else if (priceLevel == 3) {
                map_msg_mon_0.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                map_msg_mon_1.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                map_msg_mon_2.setBackgroundResource(R.drawable.home_icon_pricerange_s);
            } else if (priceLevel == 4) {
                map_msg_mon_0.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                map_msg_mon_1.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                map_msg_mon_2.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                map_msg_mon_3.setBackgroundResource(R.drawable.home_icon_pricerange_s);
            } else if (priceLevel == 5) {
                map_msg_mon_0.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                map_msg_mon_1.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                map_msg_mon_2.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                map_msg_mon_3.setBackgroundResource(R.drawable.home_icon_pricerange_s);
                map_msg_mon_4.setBackgroundResource(R.drawable.home_icon_pricerange_s);
            } else {
                map_msg_img_ly.setVisibility(View.INVISIBLE);
            }
            String substring = String.valueOf(grade).substring(0, 1);
            String s_xing = DateUtil.formateRate(String.valueOf(grade));
            if (substring.equals("1")) {
                map_ex_0.setBackgroundResource(R.drawable.icon_star_10);
                if (s_xing.equals("1")) {
                    map_ex_1.setBackgroundResource(R.drawable.icon_star_0);
                } else if (s_xing.equals("2")) {
                    map_ex_1.setBackgroundResource(R.drawable.icon_star_0);
                } else if (s_xing.equals("3")) {
                    map_ex_1.setBackgroundResource(R.drawable.icon_star_0);
                } else if (s_xing.equals("4")) {
                    map_ex_1.setBackgroundResource(R.drawable.icon_star_0);
                } else if (s_xing.equals("5")) {
                    map_ex_1.setBackgroundResource(R.drawable.icon_star_5);
                } else if (s_xing.equals("6")) {
                    map_ex_1.setBackgroundResource(R.drawable.icon_star_10);
                } else if (s_xing.equals("7")) {
                    map_ex_1.setBackgroundResource(R.drawable.icon_star_10);
                } else if (s_xing.equals("8")) {
                    map_ex_1.setBackgroundResource(R.drawable.icon_star_10);
                } else if (s_xing.equals("9")) {
                    map_ex_1.setBackgroundResource(R.drawable.icon_star_10);
                } else {

                    map_ex_1.setBackgroundResource(R.drawable.icon_star_0);
                }

                map_ex_2.setBackgroundResource(R.drawable.icon_star_0);
                map_ex_3.setBackgroundResource(R.drawable.icon_star_0);
                map_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            } else if (substring.equals("2")) {
                map_ex_0.setBackgroundResource(R.drawable.icon_star_10);
                map_ex_1.setBackgroundResource(R.drawable.icon_star_10);
                if (s_xing.equals("1")) {
                    map_ex_2.setBackgroundResource(R.drawable.icon_star_0);
                } else if (s_xing.equals("2")) {
                    map_ex_2.setBackgroundResource(R.drawable.icon_star_0);
                } else if (s_xing.equals("3")) {
                    map_ex_2.setBackgroundResource(R.drawable.icon_star_0);
                } else if (s_xing.equals("4")) {
                    map_ex_2.setBackgroundResource(R.drawable.icon_star_0);
                } else if (s_xing.equals("5")) {
                    map_ex_2.setBackgroundResource(R.drawable.icon_star_5);
                } else if (s_xing.equals("6")) {
                    map_ex_2.setBackgroundResource(R.drawable.icon_star_10);
                } else if (s_xing.equals("7")) {
                    map_ex_2.setBackgroundResource(R.drawable.icon_star_10);
                } else if (s_xing.equals("8")) {
                    map_ex_2.setBackgroundResource(R.drawable.icon_star_10);
                } else if (s_xing.equals("9")) {
                    map_ex_2.setBackgroundResource(R.drawable.icon_star_10);
                } else {

                    map_ex_2.setBackgroundResource(R.drawable.icon_star_0);
                }

                map_ex_3.setBackgroundResource(R.drawable.icon_star_0);
                map_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            } else if (substring.equals("3")) {
                map_ex_0.setBackgroundResource(R.drawable.icon_star_10);
                map_ex_1.setBackgroundResource(R.drawable.icon_star_10);
                map_ex_2.setBackgroundResource(R.drawable.icon_star_10);
                if (s_xing.equals("1")) {
                    map_ex_3.setBackgroundResource(R.drawable.icon_star_0);
                } else if (s_xing.equals("2")) {
                    map_ex_3.setBackgroundResource(R.drawable.icon_star_0);
                } else if (s_xing.equals("3")) {
                    map_ex_3.setBackgroundResource(R.drawable.icon_star_0);
                } else if (s_xing.equals("4")) {
                    map_ex_3.setBackgroundResource(R.drawable.icon_star_0);
                } else if (s_xing.equals("5")) {
                    map_ex_3.setBackgroundResource(R.drawable.icon_star_5);
                } else if (s_xing.equals("6")) {
                    map_ex_3.setBackgroundResource(R.drawable.icon_star_10);
                } else if (s_xing.equals("7")) {
                    map_ex_3.setBackgroundResource(R.drawable.icon_star_10);
                } else if (s_xing.equals("8")) {
                    map_ex_3.setBackgroundResource(R.drawable.icon_star_10);
                } else if (s_xing.equals("9")) {
                    map_ex_3.setBackgroundResource(R.drawable.icon_star_10);
                } else {
                    map_ex_3.setBackgroundResource(R.drawable.icon_star_0);
                }
                map_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            } else if (substring.equals("4")) {
                map_ex_0.setBackgroundResource(R.drawable.icon_star_10);
                map_ex_1.setBackgroundResource(R.drawable.icon_star_10);
                map_ex_2.setBackgroundResource(R.drawable.icon_star_10);
                map_ex_3.setBackgroundResource(R.drawable.icon_star_10);
                if (s_xing.equals("1")) {
                    map_ex_4.setBackgroundResource(R.drawable.icon_star_0);
                } else if (s_xing.equals("2")) {
                    map_ex_4.setBackgroundResource(R.drawable.icon_star_0);
                } else if (s_xing.equals("3")) {
                    map_ex_4.setBackgroundResource(R.drawable.icon_star_0);
                } else if (s_xing.equals("4")) {
                    map_ex_4.setBackgroundResource(R.drawable.icon_star_0);
                } else if (s_xing.equals("5")) {
                    map_ex_4.setBackgroundResource(R.drawable.icon_star_5);
                } else if (s_xing.equals("6")) {
                    map_ex_4.setBackgroundResource(R.drawable.icon_star_10);
                } else if (s_xing.equals("7")) {
                    map_ex_4.setBackgroundResource(R.drawable.icon_star_10);
                } else if (s_xing.equals("8")) {
                    map_ex_4.setBackgroundResource(R.drawable.icon_star_10);
                } else if (s_xing.equals("9")) {
                    map_ex_4.setBackgroundResource(R.drawable.icon_star_10);
                } else {
                    map_ex_4.setBackgroundResource(R.drawable.icon_star_0);
                }

            } else if (substring.equals("5")) {
                map_ex_0.setBackgroundResource(R.drawable.icon_star_10);
                map_ex_1.setBackgroundResource(R.drawable.icon_star_10);
                map_ex_2.setBackgroundResource(R.drawable.icon_star_10);
                map_ex_3.setBackgroundResource(R.drawable.icon_star_10);
                map_ex_4.setBackgroundResource(R.drawable.icon_star_10);
            } else {
                map_ex_0.setBackgroundResource(R.drawable.icon_star_0);
                map_ex_1.setBackgroundResource(R.drawable.icon_star_0);
                map_ex_2.setBackgroundResource(R.drawable.icon_star_0);
                map_ex_3.setBackgroundResource(R.drawable.icon_star_0);
                map_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            }
            try {
                if (list_map.get(i).getCommentCount() == 0) {
                    map_item_score_one.setVisibility(View.INVISIBLE);
                } else {
                    map_item_score_one.setText("(" + list_map.get(i).getCommentCount() + " " + String.format(getString(R.string.home_item_fen)));
                }

            } catch (Exception e) {
                map_item_score_one.setText("(0" + String.format(getString(R.string.home_item_fen)));

            }
            int type = list_map.get(i).getType();
            int category1 = list_map.get(i).getCategory();
            int currency1 = list_map.get(i).getCurrency();
            Http.OkHttpGet(getActivity(), cfgURL, new Http.OnDataFinish() {
                @Override
                public void OnSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject location = data.getJSONObject("Location");
                        JSONArray category = location.getJSONArray("Category");
                        JSONArray priceScope = location.getJSONArray("priceScope");
//                        for (int j = 0; j <priceScope.length() ; j++) {
//                            JSONObject jsonpriceScope = priceScope.getJSONObject(j);
//                            int level = jsonpriceScope.getInt("level");
//                            if (priceLevel==level){
//                                dialog_map_price.setText("$"+jsonpriceScope.getString("min")+"-"+jsonpriceScope.getString("max"));
//                            }
//                        }
                        String luchang = (String) SPUtil.get(getActivity(), "luchang", "");
                        boolean zh = TimeUtil.isZh(getActivity());
                        if (luchang.equals("0")) {
                            if (zh) {
                                luchang = "3";
                            } else {
                                luchang = "2";
                            }
                        }
                        for (int i = 0; i < category.length(); i++) {
                            JSONObject jsonObject1 = category.getJSONObject(i);
                            String id2 = jsonObject1.getString("Id");
                            if (id2.equals(String.valueOf(category1))) {
                                JSONArray sub = jsonObject1.getJSONArray("Sub");
                                for (int j = 0; j < sub.length(); j++) {
                                    JSONObject o = (JSONObject) sub.get(j);
                                    int id1 = o.getInt("Id");
                                    if (type == id1) {
                                        if (luchang.equals("1") || luchang.equals("2") || luchang.equals("0")) {
                                            String nameCh = o.getString("NameCh");
                                            map_item_dish.setText(nameCh);
                                            i = category.length();
                                            Log.d("caixicaixi", type + "==" + o.getString("NameCh"));
                                        } else if (luchang.equals("3")) {
                                            i = category.length();
                                            map_item_dish.setText(o.getString("NameEn"));
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        JSONArray currency = data.getJSONArray("Currency");

                        for (int k = 0; k < currency.length(); k++) {
                            JSONObject o = (JSONObject) currency.get(k);
                            int id1 = o.getInt("Id");
                            if (currency1 == id1) {
                                map_item_money.setText(o.getString("Name") + price);
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
            double distance = list_map.get(i).getDistance();
            String  luchang= (String) SPUtil.get(getActivity(), "luchang", "");
            boolean zh = TimeUtil.isZh(getActivity());
            if (luchang.equals("0")){
                if (zh){
                    luchang="3";
                }else {
                    luchang="2";
                }
            }
            if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
                if (distance >=1) {
//                    double v = distance / 1000;
                    String st = DateUtil.roundByScale(distance, 1);
                    map_item_distance.setText(getResources().getString(R.string.ju_li)+st + getResources().getString(R.string.km));
                } else {
                    double v = distance * 1000;
                    String of = String.valueOf(v);
                    String[] split = of.split("\\.");
                    map_item_distance.setText(getResources().getString(R.string.ju_li)+split[0] + getResources().getString(R.string.m));
                }
            }else if (luchang.equals("3")){
                if (distance >=1) {
//                    double v = distance / 1000;
                    String st = DateUtil.roundByScale(distance, 1);
                    map_item_distance.setText(st +" "+ getResources().getString(R.string.km)+"  "+getResources().getString(R.string.ju_li));
                } else {
                    double v = distance * 1000;
                    String of = String.valueOf(v);
                    String[] split = of.split("\\.");
                    map_item_distance.setText(split[0] +" "+ getResources().getString(R.string.m)+"  "+getResources().getString(R.string.ju_li));
                }
            }


            views.add(contentView);
//            }
        }
        return views;
    }


    private int mCurrentStoreVpIndex;
    //    private Dialog bottomDialog;
    private PopupWindow mShowStoreInfoPopWindow;

    private void show1(String replace) {
//        if(bottomDialog!=null&&bottomDialog.isShowing()){
//            bottomDialog.dismiss();
//        }
//        bottomDialog = new Dialog(getActivity(), R.style.BottomDialog);
        if (mShowStoreInfoPopWindow != null && mShowStoreInfoPopWindow.isShowing()) {
            mShowStoreInfoPopWindow.dismiss();
        }
        View vpStoreInfosDlg = LayoutInflater.from(getActivity()).inflate(R.layout.dlg_store_infos, null);
        mShowStoreInfoPopWindow = new PopupWindow(vpStoreInfosDlg, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ViewPager vp = (ViewPager) vpStoreInfosDlg.findViewById(R.id.vp_store_infos);
        List<View> views =null;
        if(mFromStoreDetailToOpenMap){
            views = loadStoreInfoViews(mShowStoreInfoPopWindow,toShowShopList);
        }else {
            views = loadStoreInfoViews(mShowStoreInfoPopWindow,list_map);
        }
        StoreInfosPagerAdapter adapter = new StoreInfosPagerAdapter(getContext(), views);
        vp.setAdapter(adapter);
        int index = 0;
        for (int i = 0; i < list_map.size(); i++) {
            double latitude = list_map.get(i).getLatitude();
            double longitude = list_map.get(i).getLongitude();
            String s1 = " " + latitude + "," + longitude;
            if (replace.equals(s1)) {
                index = i;
                Log.d(TAG, "show1: index " + index);
                break;
            }
        }
        if(mFromStoreDetailToOpenMap){
            index=0;
        }
        mCurrentStoreVpIndex = index;
        vp.setCurrentItem(index);
        vp.setPageMargin(ScreenUtils.dp2px(getContext(), 7));
        vp.setOffscreenPageLimit(3);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position >= locationBeens.size()) {
                    return;
                }
                mCurrentStoreVpIndex = position;
                LocationBean bean = locationBeens.get(position);
                Log.d(TAG, "onPageSelected: " + position + "," + bean.name);


                if (bean != null) {
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bean.latLng,16));

                    for (int i = 0; i < mMarkerList.size(); i++) {
                        Marker marker = mMarkerList.get(i);
                        if (marker != null) {
                            if (marker.getPosition() != null && marker.getPosition().longitude==bean.getPosition().longitude
                                && marker.getPosition().latitude==bean.getPosition().latitude) {
                                marker.showInfoWindow();
                                break;
                            }
                        }
                    }
//                    if(markerList.size()>mCurrentStoreVpIndex){
//                        Marker marker=markerList.get(mCurrentStoreVpIndex);
//                        if(marker!=null){
//                            marker.showInfoWindow();
//                        }
//                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(bean.latLng));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        bottomDialog.setContentView(vpStoreInfosDlg);
        mShowStoreInfoPopWindow.showAtLocation(map_txt, Gravity.BOTTOM, 0, ScreenUtils.dp2px(getContext(), 70));
        ViewGroup.LayoutParams lp = vpStoreInfosDlg.getLayoutParams();
        lp.width = ScreenUtils.getScreenWidth(getContext());
        vpStoreInfosDlg.setLayoutParams(lp);
    }


    private View mInfoWindowContent = null;


    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_marker_layout, null);
        LocationBean bean=null;
        for(int i=0;i<locationBeens.size();i++){
            LocationBean lb=locationBeens.get(i);
            if(marker.getPosition().latitude==lb.latLng.latitude&&marker.getPosition().longitude==lb.latLng.longitude){
                bean=lb;
            }
        }
//        LocationBean bean = locationBeens.get(mCurrentStoreVpIndex);
        Log.d(TAG, "getInfoContents: " + bean.getTitle());
        Log.d(TAG, "getInfoContents: marker " + marker.getTitle());
        ImageView marker_img = view.findViewById(R.id.marker_img);
        marker_img.setBackgroundResource(R.drawable.map_icon_restaurant);
        TextView numTxt = (TextView) view.findViewById(R.id.num_txt);
        numTxt.setText(bean.getTitle());
//               if(mCurrentStoreVpIndex<locationBeens.size()){
//
//        }

        return view;

    }


    /**
     * 如果取得了权限,显示地图定位层
     */
    private void enableMyLocation() {
      
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            Log.d(TAG, "requestPermission: ");
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            Log.d(TAG, "enableMyLocation: ");
            mMap.setMyLocationEnabled(true);
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "--onConnected--");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(getActivity(), "Permission to access the location is missing.", Toast.LENGTH_LONG).show();
            // ToastUtil.showgravity(getActivity(), "Permission to access the location is missing.");
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            Log.d(TAG, "onConnected: no permission");
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(!mFromStoreDetailToOpenMap) {
            refreshMapData();
        }



    }

    private void refreshMapData(){
        
        
        Log.d(TAG, "refreshMapData "+mLastLocation + "");
        if (mLastLocation != null) {
            lastLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MyApplication.getInstance().setLastLocation(lastLatLng);
            String address = getAddress(getActivity(), lastLatLng.latitude, lastLatLng.longitude);

            /*frag_d.setVisibility(View.GONE);
            feco_list.setVisibility(View.VISIBLE);*/

            SPUtil.put(getApplicationContext(), "lastidu", lastLatLng.latitude);
            SPUtil.put(getApplicationContext(), "longtidu", lastLatLng.longitude);


            url = urlUtil + UrlUtil.shop + "?latitude=" + lastLatLng.latitude + "&longitude=" + lastLatLng.longitude;
            url_cv = urlUtil + UrlUtil.shop + "?latitude=" + lastLatLng.latitude + "&longitude=" + lastLatLng.longitude;
            flg = 2;
            url_to = url + "&start=0" + "&rows=" + 10;
            //rows=1;

            double latitude = lastLatLng.latitude;
            if (who == 0) {
                requestData();
//                mockData();
                who = 1;
            }else{
                dissLoad();
            }
            displayPerth(true, lastLatLng);
            titleAddre.setText(address);
            initCamera(lastLatLng);
            if (!Geocoder.isPresent()) {
                //Toast.makeText(getActivity(), "No geocoder available", Toast.LENGTH_LONG).show();
                ToastUtil.showgravity(getActivity(), "No geocoder available");
                return;
            }
            if (mAddressRequested) {
                startIntentService(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

            }
        }else{
            Log.d(TAG, "refreshMapData: no location");
            dissLoad();
//            noNetworkView.setVisibility(View.GONE);
//            fragLy.setVisibility(View.GONE);
//            frag_ding.setVisibility(View.VISIBLE);
            if(lm==null){
                Log.d(TAG, "refreshMapData: location service is null");
                return;
            }
            boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean providerEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (ok == false && providerEnabled == false) {//没有开了定位服务

            }else{
                final CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
//            builder.setTitle(String.format(getString(R.string.ding)));
                builder.setMessage(String.format(getString(R.string.location_fail)));
//            builder.setPositiveButton(R.string.lue, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                }
//            });
                builder.setNegativeButton(R.string.determine, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        getActivity().finish();
                        Intent restart=new Intent(getContext(),MainActivity.class);
                        restart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(restart);
                    }
                });
                builder.create().show();
            }
        }
    }
    
    /**
     * 添加标记
     */
    private void displayPerth(boolean isDraggable, LatLng latLng) {
        if (perth == null) {
            //  perth = mMap.addMarker(new MarkerOptions().position(latLng));
            // perth.setDraggable(isDraggable); //设置可移动


        }

    }

    /**
     * 将地图视角切换到定位的位置
     */
    private void initCamera(final LatLng sydney) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                FragmentActivity activity = getActivity();
                if (activity == null) {

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!mFromStoreDetailToOpenMap) {
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
                            }
                        }
                    });
                }

            }
        }).start();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        dissLoad();
    }

    /**
     * '我的位置'按钮点击时的调用
     *
     * @return
     */
    @Override
    public boolean onMyLocationButtonClick() {
        // Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        if (mLastLocation != null) {
            Log.i("MapsActivity", "Latitude-->" + String.valueOf(mLastLocation.getLatitude()));
            Log.i("MapsActivity", "Longitude-->" + String.valueOf(mLastLocation.getLongitude()));
        }
        if (lastLatLng != null)
            // perth.setPosition(lastLatLng);
            checkIsGooglePlayConn();
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        try{
            mGoogleApiClient.connect();
        }catch (Exception e){}

    }

    /**
     * 检查是否已经连接到 Google Play services
     */
    private void checkIsGooglePlayConn() {
        Log.i("MapsActivity", "checkIsGooglePlayConn-->" + mGoogleApiClient.isConnected());
        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
            startIntentService(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        }
        mAddressRequested = true;
    }

    /**
     * 启动地址搜索Service
     */
    protected void startIntentService(LatLng latLng) {
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.LATLNG_DATA_EXTRA, latLng);
        getActivity().startService(intent);

    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        perthLatLng = marker.getPosition();
        startIntentService(perthLatLng);
    }

    /**
     * 逆地理编码 得到地址
     *
     * @param context
     * @param latitude
     * @param longitude
     * @return
     */
    public static String getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {

            List<Address> address = geocoder.getFromLocation(latitude, longitude, 1);
            longitude_o = String.valueOf(address.get(0).getLongitude());
            latitude_o = String.valueOf(address.get(0).getLatitude());


            Log.d("位置", "得到位置当前" + address + "'\n"
                    + "经度：" + String.valueOf(address.get(0).getLongitude()) + "\n"
                    + "纬度：" + String.valueOf(address.get(0).getLatitude()) + "\n"
                    + "纬度：" + "国家：" + address.get(0).getCountryName() + "\n"
                    + "城市：" + address.get(0).getLocality() + "\n"
                    + "名称：" + address.get(0).getAddressLine(1) + "\n"
                    + "街道：" + address.get(0).getAddressLine(0)

            );

            //return address.get(0).getAddressLine(0) + "  " + address.get(0).getLocality() + " " + address.get(0).getCountryName();
            if (address.get(0).getLocality() == null) {
                return address.get(0).getCountryName();
            } else {
                return address.get(0).getLocality();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "未知";
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

    //聚合类标记点击事件
    @Override
    public boolean onClusterClick(Cluster<LocationBean> cluster) {

        return true;
    }

    //普通标记点击事件
    @Override
    public boolean onClusterItemClick(LocationBean item) {
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(mShowStoreInfoPopWindow!=null&&mShowStoreInfoPopWindow.isShowing()){
            if(!mFromStoreDetailToOpenMap) {
                mShowStoreInfoPopWindow.dismiss();
            }
        }
    }


    class AddressResultReceiver extends ResultReceiver {
        private String mAddressOutput;

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            mAddressOutput = resultData.getString(FetchAddressIntentService.RESULT_DATA_KEY);
            if (resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
                Log.i("MapsActivity", "mAddressOutput-->" + mAddressOutput);
                /*new AlertDialog.Builder(getActivity())
                        .setTitle("Position")
                        .setMessage(mAddressOutput)
                        .create()
                        .show();*/
            }

        }
    }

    /**
     * 聚类标记数据实体类
     * 启用聚合算法实体必须实现ClusterItem
     */
    class LocationBean implements ClusterItem {
        String name;
        boolean state;
        LatLng latLng;
        int shopId;
        @Override
        public LatLng getPosition() {
            return latLng;
        }

        @Override
        public String getTitle() {
            return name;
        }

        @Override
        public String getSnippet() {
            return name;
        }
    }

    /**
     * 地图门店数据
     */
    private void mockData() {
        locationBeens = new ArrayList<>();
        OkHttp.OkHttpGet(getActivity(), url + "&start=0" + "&rows=" + 1000, new OkHttp.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                Log.d("lianjie", url);
                Log.d("pengtao", result);

                if (list_map.size() > 0) {
                    list_map.clear();

                }

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    List<ShopBean> shopBeans=new ArrayList<>();
                    if (jsonObject.getString("code").equals("0")) {

                        JSONArray data = jsonObject.getJSONArray("data");

                        if (data.length() == 0) {
                            //无商店

                            // Toast.makeText(getActivity(),"无商店"+list.size(),Toast.LENGTH_SHORT).show();
                        } else {

                            for (int i = 0; i < data.length(); i++) {

                                ShopBean bean = GSonUtil.parseGson(data.getString(i), ShopBean.class);
                                shopBeans.add(bean);
                            }

                        }


                    } else if (jsonObject.getString("code").equals("5007")) {
                        SPUtil.remove(getActivity(), "Token");
                        SPUtil.remove(getActivity(), "photo1");
                        SPUtil.remove(getActivity(), "photo");
                        SPUtil.remove(getActivity(), "gen");
                        SPUtil.remove(getActivity(), "name_one");
                        SPUtil.put(getActivity(), "deng", false);
                        Intent intent = new Intent(getActivity(), LoginMeActivity.class);
                        startActivity(intent);
                    } else {
                        //失败
                        //Toast.makeText(getActivity(), getResources().getString(R.string.fan), Toast.LENGTH_SHORT).show();
                        ToastUtil.showgravity(getActivity(), getResources().getString(R.string.fan));
                    }

                    List<String> lalist = new ArrayList<String>();
                    for (int a = 0; a < shopBeans.size(); a++) {
                        double latitude = shopBeans.get(a).getLatitude();   //22
                        double longitude = shopBeans.get(a).getLongitude();  //114
                        lalist.add(latitude + "," + longitude);
                    }
                    // lalist.add("22.5334086550,114.0441799164");
                    //lalist.add("22.5356657,114.0529055");
                    for (int i = 0; i < lalist.size(); i++) {
                        LocationBean locationBean = new LocationBean();
                        locationBean.name = shopBeans.get(i).getName();
                        String s = lalist.get(i);
                        String[] split = s.split(",");
                        String s1 = split[0];
                        String s2 = split[1];
                        locationBean.state = random.nextBoolean();
                        locationBean.latLng = new LatLng(Double.parseDouble(s1), Double.parseDouble(s2));
                        locationBeens.add(locationBean);
                    }

                    Manager(locationBeens);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void OnSocketTimeout() {

            }

            @Override
            public void OnConnect() {

            }


        });


    }
    private List< Marker > mMarkerList=new ArrayList<>();
    private void Manager(List<LocationBean> locationBeanList) {
        mMap.clear();
        mMarkerList.clear();
        Log.d(TAG, "Manager: locationBeanList "+locationBeanList.size());
        mMap.getUiSettings().setMapToolbarEnabled(false);

        //添加普通标记
        for (LocationBean locationBean : locationBeanList) {
            BitmapDescriptor bd= BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.map_icon_restaurant));
            Marker marker = mMap.addMarker(new MarkerOptions().position(locationBean.latLng).title(locationBean.name).icon(bd));
            marker.setTag(locationBean);
            mMarkerList.add(marker);
        }
//        clusterManager.clearItems();
//        //添加聚类标记
//        clusterManager.addItems(locationBeanList);
//
//        clusterManager.cluster();

    }

    /**
     * 自定义聚类标记渲染类
     */
    class LocationRenderer extends DefaultClusterRenderer<LocationBean> {

        //普通聚类标记icon生产类
        IconGenerator markerIconGenerator = new IconGenerator(getApplicationContext());
        //聚合聚类标记icon生产类
        IconGenerator clusterIconGenerator = new IconGenerator(getApplicationContext());

        View markerView, clusterView;
        ImageView cb;
        TextView tv, tvCount;


        public LocationRenderer(Context context, GoogleMap map, ClusterManager<LocationBean> clusterManager) {
            super(context, map, clusterManager);

            //初始化

            //普通聚类标记布局
            markerView = getLayoutInflater().inflate(R.layout.multi_profile, null);
            cb = (ImageView) markerView.findViewById(R.id.image);
            tv = (TextView) markerView.findViewById(R.id.amu_text);
            markerIconGenerator.setContentView(markerView);

            //聚合聚类标记布局
            clusterView = getLayoutInflater().inflate(R.layout.multi_profile, null);
            // tvCount= (TextView) clusterView.findViewById(R.id.tvCount);
            clusterIconGenerator.setContentView(clusterView);
        }

        /**
         * 设置普通聚类标记样式
         *
         * @param item          标记数据实体类
         * @param markerOptions 标记样式设置类
         */
        @Override
        protected void onBeforeClusterItemRendered(LocationBean item, MarkerOptions markerOptions) {

            // cb.setChecked(item.state);
            // tv.setText(item.name);
            //通过设置透明图片隐藏IconGenerator自带的边框背景,实现完全自定义

            markerIconGenerator.setBackground(getResources().getDrawable(R.drawable.map_icon_restaurant));
            Bitmap icon = markerIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        /**
         * 设置聚合聚类标记样式
         *
         * @param cluster
         * @param markerOptions
         */
        @Override
        protected void onBeforeClusterRendered(final Cluster<LocationBean> cluster, MarkerOptions markerOptions) {

            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            mDimension = (int) getResources().getDimension(R.dimen.px_50);
            int width = mDimension;
            int height = mDimension;


            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            //           tvCount.setText(String.valueOf(cluster.getSize()));
            clusterIconGenerator.setBackground(getResources().getDrawable(R.drawable.map_icon_restaurant));
            Bitmap icon = clusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        /**
         * 判断是否渲染为聚类标记
         *
         * @param cluster
         * @return
         */
        @Override
        protected boolean shouldRenderAsCluster(Cluster<LocationBean> cluster) {
            //当前标记含有的数据点大于1则渲染
            return cluster.getSize() > 1000;
        }
    }

    private LocationManager lm;//【位置管理】
    private static final int BAIDU_READ_PHONE_STATE = 100;

    /**
     * 获取定位权限  GPS_PROVIDER  NETWORK_PROVIDER
     */
    public void Location() {
        lm = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        if(lm==null){
            final CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.get_location_service_fail));
            builder.setNegativeButton(R.string.determine, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if(getActivity()!=null) {
                        getActivity().finish();
                    }
                }
            });builder.create().show();
            return
            ;
        }
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean providerEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.d(TAG, "Location: ok "+ok+",providerEnabled "+providerEnabled);
        if (ok == false && providerEnabled == false) {//没有开了定位服务
            frag_ding.setVisibility(View.VISIBLE);
            if(title_ding.getBackground()!=null){
                title_ding.getBackground().setAlpha(255);
            }
            appBarLayout.removeOnOffsetChangedListener(FindFragment.this);
            feco_list.setVisibility(View.GONE);
            noNetworkView.setVisibility(View.GONE);
            customBanner.setVisibility(View.GONE);
            dissLoad();
        } else {
            frag_ding.setVisibility(View.GONE);
            if(title_ding.getBackground()!=null){
                title_ding.getBackground().setAlpha(255);
            }
            feco_list.setVisibility(View.VISIBLE);
            customBanner.setVisibility(View.VISIBLE);
            frag_find_ly.setVisibility(View.VISIBLE);
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // 没有权限，申请权限。
                //                    Toast.makeText(getActivity(), "没有权限", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

            } else {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    lastLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                    String address = getAddress(getActivity(), lastLatLng.latitude, lastLatLng.longitude);

                    frag_d.setVisibility(View.GONE);
                    feco_list.setVisibility(View.VISIBLE);
                    Log.d("qqqqqqq","555555555555");
                    SPUtil.put(getApplicationContext(), "lastidu", lastLatLng.latitude);
                    SPUtil.put(getApplicationContext(), "longtidu", lastLatLng.longitude);

                    //url=urlUtil+"user/shop/get?latitude="+lastLatLng.latitude+"&longitude="+lastLatLng.longitude;
                    //url_cv=urlUtil+"user/shop/get?latitude="+lastLatLng.latitude+"&longitude="+lastLatLng.longitude;
                    url = urlUtil + UrlUtil.shop + "?latitude=" + lastLatLng.latitude + "&longitude=" + lastLatLng.longitude;
                    url_cv = urlUtil + UrlUtil.shop + "?latitude=" + lastLatLng.latitude + "&longitude=" + lastLatLng.longitude;
                    flg = 2;
                    url_to = url + "&start=0" + "&rows=" + 10;
                    //rows=1;

                    double latitude = lastLatLng.latitude;
                    if (who == 0) {
                        requestData();
//                        mockData();2000
                        who = 1;
                    }else{
                        dissLoad();
                    }
                    displayPerth(true, lastLatLng);
                    titleAddre.setText(address);
                    initCamera(lastLatLng);
                    if (!Geocoder.isPresent()) {
                        //Toast.makeText(getActivity(), "No geocoder available", Toast.LENGTH_LONG).show();
                        ToastUtil.showgravity(getActivity(), "No geocoder available");
                        return;
                    }
                    if (mAddressRequested) {
                        startIntentService(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

                    }
                }else{
                    dissLoad();
                }


                // 有权限了，去放肆吧。
                //下拉刷新
                refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                        //    page = 1;
                        flg = 2;

                        mCurrentCounter = 0;
                        //url="http://192.168.50.194:8082/user/shop/get?latitude=22.535638"+"&longitude=114.052906";
                        // url="http://193.112.160.85:8082/user/shop/get?latitude=22.535638"+"&longitude=114.052906";
                        if (url.equals("")) {

                        } else {
                            rows = 1;
                            requestData();
                            if (prilength == 0) {
                                if (type_id!=0){
                                    url_to = url + "&start=" + (rows - 1) * 10 + "&rows=" + 10 + "&categoryList=" + type_id+"&typeList="+id_city + "&orderByGrade=" + orderByGrade + "&open=" + open_f;
                                }else {
                                    url_to = url + "&start=" + (rows - 1) * 10 + "&rows=" + 10 + "&orderByGrade=" + orderByGrade + "&open=" + open_f;
                                }
                            } else {
                                if (type_id!=0){
                                    url_to = url + "&start=" + (rows - 1) * 10 + "&rows=" + 10 + "&priceLevel=" + prilength + "&orderByGrade=" + orderByGrade + "&open=" + open_f+ "&categoryList=" + type_id+"&typeList="+id_city;
                                }else {
                                    url_to = url + "&start=" + (rows - 1) * 10 + "&rows=" + 10 + "&priceLevel=" + prilength + "&orderByGrade=" + orderByGrade + "&open=" + open_f;
                                }
                            }
                        }
                        // mockData();
                    }
                });
            }
            // Toast.makeText(getActivity(), "系统检测到未开启GPS定位服务", Toast.LENGTH_SHORT).show();
            /*Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 1315);*/
        }
    }
    private String getLanguage(){
        String  luchang= (String) SPUtil.get(getContext(), "luchang", "");
        boolean zh = TimeUtil.isZh(getContext());
        if (luchang.equals("0")){
            if (zh){
                luchang="3";
            }else {
                luchang="2";
            }
        }
        return luchang;
    }

    private void reportBannerClick(String bannerType){
        Bundle b=new Bundle();
        b.putString("type","CLICK_BANNER");
        b.putString("banner",bannerType);
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.CLICK_DISCOVER_BANNER,b);
    }

    //设置普通指示器
    private void setBanner(final ArrayList<String> beans) {
        String luchang=getLanguage();
        int resId=R.drawable.home_banner;

        List<ActivityBean> actList=ConfigureInfoManager.getInstance().getActList();
        if(actList!=null&&actList.size()>0){
            ActivityBean act=actList.get(0);
            if(serverTime<act.getActivityStartTm()||serverTime>=act.getActivityEndTm()){
                customBanner.setOnPageClickListener(null);
            }else{
                if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
                    resId=R.drawable.home_banner_kol;
                }else if (luchang.equals("3")){
                    resId=R.drawable.home_banner_kol_en;
                }
                customBanner.setOnPageClickListener(new CustomBanner.OnPageClickListener() {
                    @Override
                    public void onPageClick(int i, Object o) {
                        reportBannerClick("KOL");
                        Intent banner=new Intent(getContext(), WebViewActivity.class);
                        banner.putExtra("html", 7);
                        startActivity(banner);
                    }
                });
            }
        }else{
            customBanner.setOnPageClickListener(null);
        }

        final int imageResId=resId;
        customBanner.setPages(new CustomBanner.ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                imageView.setImageResource(R.drawable.home_banner);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String entity) {
                ((ImageView) view).setImageResource(imageResId);
//                Glide.with(context).load(entity).placeholder(imageResId).error(imageResId).into((ImageView) view);
            }
        }, beans)
//                //                //设置指示器为普通指示器
//                .setIndicatorStyle(CustomBanner.IndicatorStyle.NONE)
//                //                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setIndicatorRes(R.drawable.shape_point_select, R.drawable.shape_point_unselect)
//                //                //设置指示器的方向
//                .setIndicatorGravity(CustomBanner.IndicatorGravity.CENTER)
//                //                //设置指示器的指示点间隔
//                .setIndicatorInterval(20)
                //设置自动翻页
             ;
             //   .startTurning(5000);
    }


    public static final int REQUEST_PERMISSION_SETTING=0x378;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_PERMISSION_SETTING:
                Log.d(TAG, "onActivityResult: refreshMapData");
                refreshMapData();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: "+requestCode);
        switch (requestCode) {
            case BAIDU_READ_PHONE_STATE:

                break;
            case LOCATION_PERMISSION_REQUEST_CODE:
                Log.d(TAG, "onRequestPermissionsResult: "+grantResults.length);
                if(grantResults!=null&&grantResults.length>0){
                    Log.d(TAG, "onRequestPermissionsResult: "+grantResults[0]);
                    refreshMapData();

                }
                break;
        }
    }

    private class PreviewHandler extends Handler {

        private WeakReference<FindFragment> ref;

        PreviewHandler(FindFragment activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final FindFragment activity = ref.get();
            if (activity == null) {
                return;
            }
            switch (msg.what) {

                case -1:
                    Log.d("womenbuyiyang", url_to);
                    OkHttp.OkHttpGet(getActivity(), urlUtil+UrlUtil.all_shop, new OkHttp.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            if(isDetached()){
                                return;
                            }
                            dissLoad();
                            noNetworkView.setVisibility(View.GONE);
                            fragLy.setVisibility(View.VISIBLE);
                            frag_ding.setVisibility(View.GONE);
                            customBanner.setVisibility(View.VISIBLE);

                            Log.d("hahhahahewrt", urlUtil+UrlUtil.all_shop+"=="+result );
                            refreshLayout.finishRefresh();
                            refreshLayout.finishLoadMoreWithNoMoreData();
                            if (flg == 2) {
                                mDataAdapter.clear();
                                newList.clear();
                            }
                            if (rows == 1) {
                                mDataAdapter.clear();
                                newList.clear();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(result);

                                if (jsonObject.getString("code").equals("0")) {
                                    serverTime = jsonObject.getLong("serverTime");
                                    Log.d(TAG, "OnSuccess: serverTime "+serverTime);
                                    newList.clear();
                                    JSONArray data = jsonObject.getJSONArray("data");
                                    if (data.length() == 0) {
                                        if (flg != 1) {

                                            frag_img.setVisibility(View.VISIBLE);
                                            frag_ly_txt.setVisibility(View.VISIBLE);
                                            feco_list.setVisibility(View.GONE);
                                        }

                                        //无商店
                                        //adapter.notifyDataSetChanged();
                                        // Toast.makeText(getActivity(),"无商店"+list.size(),Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (flg != 1) {

                                            frag_img.setVisibility(View.GONE);
                                            frag_ly_txt.setVisibility(View.GONE);
                                            feco_list.setVisibility(View.VISIBLE);
                                        }
                                        FixedThreadPool.getInstance().submit(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    for (int i = 0; i < data.length(); i++) {
                                                        ShopBeanNew bean = GSonUtil.parseGson(data.getString(i), ShopBeanNew.class);
                                                        double distance = Util.getDistance(bean.getLongitude(), bean.getLatitude(), lastLatLng.longitude, lastLatLng.latitude);
                                                        bean.setDistance(distance);
                                                        if(bean.getRedeem()!=null) {
                                                            Collections.sort(bean.getRedeem());
                                                        }
                                                        newList.add(bean);
                                                    }
                                                }catch (JSONException e){
                                                    e.printStackTrace();
                                                }
                                                initShopBeanOpenStatus();
                                                //adapter.addData(list);
                                                //test
                                                ArrayList<String > banner=new ArrayList<>();
                                                for(int i=0;i<10;i++){
                                                    banner.add("");
                                                }
                                                mHandler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        setBanner(banner);
                                                        appBarLayout.addOnOffsetChangedListener(FindFragment.this);
                                                        sortData();
                                                    }
                                                });


                                                list_map.clear();
                                                list_map.addAll(newList);
                                                ShopBeanNew.sSortType= Constants.SORT_TYPE.SORT_TYPE_DEFAULT;
                                                Collections.sort(list_map);
                                                List<String> lalist = new ArrayList<String>();
                                                for (int a = 0; a < list_map.size(); a++) {
                                                    double latitude = list_map.get(a).getLatitude();   //22
                                                    double longitude = list_map.get(a).getLongitude();  //114
                                                    lalist.add(latitude + "," + longitude);
                                                }
                                                locationBeens = new ArrayList<>();
                                                for (int i = 0; i < lalist.size(); i++) {
                                                    LocationBean locationBean = new LocationBean();
                                                    locationBean.name = list_map.get(i).getName();
                                                    String s = lalist.get(i);
                                                    String[] split = s.split(",");
                                                    String s1 = split[0];
                                                    String s2 = split[1];
                                                    locationBean.state = random.nextBoolean();
                                                    locationBean.latLng = new LatLng(Double.parseDouble(s1), Double.parseDouble(s2));
                                                    locationBeens.add(locationBean);
                                                }
                                                mHandler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Manager(locationBeens);
                                                    }
                                                });


                                            }
                                        });

                                    }

                                } else if (jsonObject.getString("code").equals("5003")){
                                   // AppUtil.Promptbox(getActivity());
                                }else if (jsonObject.getString("code").equals("5007")) {
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
                                flg = 1;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void OnSocketTimeout() {
                            if(isDetached()){
                                return;
                            }
                            Log.d("http", "onFailure: OnSocketTimeout");
                            dissLoad();
                            noNetworkView.setVisibility(View.VISIBLE);
                            if(title_ding.getBackground()!=null){
                                title_ding.getBackground().setAlpha(255);
                            }
                            appBarLayout.removeOnOffsetChangedListener(FindFragment.this);
                            fragLy.setVisibility(View.GONE);
                            frag_ding.setVisibility(View.GONE);
                            feco_list.setVisibility(View.GONE);
                            customBanner.setVisibility(View.GONE);
//                            ToastUtil.showgravity(getActivity(), getResources().getString(R.string.unstable));
                            try {
                                refreshLayout.finishRefresh();
                                refreshLayout.finishLoadMore();
                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void OnConnect() {
                            if(isDetached()){
                                return;
                            }
                            Log.d("http", "onFailure: ConnectException");
                            dissLoad();
                            noNetworkView.setVisibility(View.VISIBLE);
                            if(title_ding.getBackground()!=null){
                                title_ding.getBackground().setAlpha(255);
                            }
                            appBarLayout.removeOnOffsetChangedListener(FindFragment.this);
                            feco_list.setVisibility(View.GONE);
                            fragLy.setVisibility(View.GONE);
                            frag_ding.setVisibility(View.GONE);
                            customBanner.setVisibility(View.GONE);
//                            ToastUtil.showgravity(getActivity(), getResources().getString(R.string.unstable));
                        }
                    });
                    break;
                case -3:
                    //网络请求失败的情景
                    if(isDetached()){
                        return;
                    }
                    dissLoad();
                    noNetworkView.setVisibility(View.VISIBLE);
                    if(title_ding.getBackground()!=null){
                        title_ding.getBackground().setAlpha(255);
                    }
                    appBarLayout.removeOnOffsetChangedListener(FindFragment.this);
                    feco_list.setVisibility(View.GONE);
                    fragLy.setVisibility(View.GONE);
                    frag_ding.setVisibility(View.GONE);
                    customBanner.setVisibility(View.GONE);
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                    activity.notifyDataSetChanged();

                    break;
                case -4:
                    if(isDetached()){
                        return;
                    }
                    dissLoad();
                    break;
                default:
                    break;
            }

        }
    }

    private void requestData() {

        new Thread() {

            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(isDetached()){
                    return;
                }
                if (NetworkUtils.isNetAvailable(getContext())) {
                    mHandler.sendEmptyMessage(-1);

                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        String fai = (String) SPUtil.get(getActivity(), "fai", "");
        if (fai!=null&&fai.equals("fai")) {

            SPUtil.remove(getActivity(), "fai");
        }
        updateLoginTipView();
    }
}
