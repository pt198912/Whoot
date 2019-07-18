package com.app.whoot.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.adapter.HistoryAdapter;
import com.app.whoot.adapter.LefteAdapter;
import com.app.whoot.adapter.MsgAdapter;
import com.app.whoot.adapter.RightAdapter;
import com.app.whoot.adapter.SearAdapter;
import com.app.whoot.app.MyApplication;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.CategoryBean;
import com.app.whoot.bean.GoogleMapBean;
import com.app.whoot.bean.ShopBeanNew;
import com.app.whoot.bean.StoreTagBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.view.popup.CustomPopupWindow;
import com.app.whoot.util.Constants;
import com.app.whoot.util.FireBaseEventKey;
import com.app.whoot.util.FirebaseReportUtils;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.ListDataUtil;
import com.app.whoot.util.Model;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ScreenUtils;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;

import com.app.whoot.util.Util;
import com.dl7.tag.TagLayout;
import com.dl7.tag.TagView;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static com.app.whoot.base.BaseApplication.SCREEN_HEIGHT;
import static com.app.whoot.util.Constants.TYPE_DIAMOND;

/**
 * Created by Sunrise on 4/9/2018.
 * 搜索
 */

public class SearchActivity extends BaseActivity{

    @BindView(R.id.search)
    LinearLayout search;
    @BindView(R.id.id_flowlayout)
    TagFlowLayout idFlowlayout;
    @BindView(R.id.search_ad)
    TextView searchAd;
    @BindView(R.id.serach_del)
    ImageView serachDel;
    @BindView(R.id.sea_ed)
    EditText seaEd;
    @BindView(R.id.serach_list)
    ListView serachList;
    @BindView(R.id.search_cloose)
    View search_cloose;
    @BindView(R.id.search_txt)
    View search_txt;
    @BindView(R.id.search_ly)
    LinearLayout search_ly;
    @BindView(R.id.sea_ed_map)
    EditText sea_ed_map;
    @BindView(R.id.serarch_caixi)
    LinearLayout serarch_caixi;
    @BindView(R.id.serarch_image)
    ImageView serarch_image;
    @BindView(R.id.search_ly_map)
    LinearLayout search_ly_map;
    @BindView(R.id.tag_container)
    TagLayout tagContainer;
    @BindView(R.id.ll_search_label)
    View searchLabel;
    @BindView(R.id.result_ly)
    LinearLayout resultLy;
    @BindView(R.id.result_view)
    LRecyclerView resultView;

    @BindView(R.id.no_network_view)
    View noNetworkView;
    private String[] vals = new String[5];

    private List<String> valse = new ArrayList<>();

    private List<String> history = new ArrayList<>();
    private PopupWindow mPopupWindow;

    private CustomPopupWindow mPop; //显示搜索联想的pop
    private ListView searchLv; //搜索联想结果的列表
    private ArrayAdapter mAdapter; //ListView的适配器
    private List<String> mSearchList = new ArrayList<>(); //搜索结果的数据源
    private List<GoogleMapBean> googleMapt = new ArrayList<>(); //搜索结果的数据源
    private PublishSubject<String> mPublishSubject;
    private CompositeDisposable mCompositeDisposable;
    private static final String TAG = "MainActivity";

    int page = 0;
    int rows = 5;
    int start = 0;
    private HistoryAdapter adapter;
    private ListDataUtil soso;
    private List<String> sousuo;
    private String urlUtil;
    private ListView right_image;
    private String[] city;
    private int id;

    private MsgAdapter mDataAdapter = null;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<ShopBeanNew> mDatas=new ArrayList<>();
    public static SearchActivity instance;
    @Override
    public int getLayoutId() {
        return R.layout.search_activi;
    }
    private void updateTagView(boolean needShow) {

        tagContainer.removeAllViews();
        if (sousuo.size()>0){
            if(needShow) {
                searchLabel.setVisibility(View.VISIBLE);
                tagContainer.setVisibility(View.VISIBLE);
            }
            tagContainer.setHorizontalInterval(ScreenUtils.dp2px(SearchActivity.this,8));
            tagContainer.setVerticalInterval(ScreenUtils.dp2px(SearchActivity.this,20));
            for (int i = 0; i < sousuo.size(); i++) {
                String name = sousuo.get(i);
                TagView view = new TagView(this, name);
                view.setBorderColor(Color.parseColor("#00000000"));
                view.setBorderWidth(0);
                view.setBgColor(Color.parseColor("#F3F4F6"));
                view.setRadius(ScreenUtils.dp2px(SearchActivity.this,16));
                view.setTextColor(getResources().getColor(R.color.card_cc));
                int padding=ScreenUtils.dp2px(SearchActivity.this,16);
//                view.setPadding(padding,0,padding,0);
                view.setHorizontalPadding(padding);
                view.setTextSize(ScreenUtils.dp2px(SearchActivity.this,16));
                tagContainer.addView(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        url=urlUtil+UrlUtil.all_shop+"?word="+name;
                        searchData();
                    }
                });
            }

        }else{
            searchLabel.setVisibility(View.GONE);
            tagContainer.setVisibility(View.GONE);
        }

    }
    @Override
    public void onCreateInit() {
        instance=this;
        soso = new ListDataUtil(SearchActivity.this, "soso");
        urlUtil = (String) SPUtil.get(SearchActivity.this, "URL", "");
        sousuo = soso.getDataList("sousuo");
        Log.d("soso",sousuo.size()+"");
        initModle();
        initEdt();
        initPop();
        updateTagView(true);
        initRecycleView();
        sea_ed_map.setImeOptions(EditorInfo.IME_ACTION_SEND);
        sea_ed_map.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(sousuo.size()>0){
                    searchLabel.setVisibility(View.VISIBLE);
                    tagContainer.setVisibility(View.VISIBLE);
                    resultView.setVisibility(View.GONE);
                }else{
                    searchLabel.setVisibility(View.GONE);
                    tagContainer.setVisibility(View.GONE);
                    resultView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sea_ed_map.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String Ed_txt = sea_ed_map.getText().toString().trim();
                if (Ed_txt.length()==0){

                }else {
                    if (actionId == EditorInfo.IME_ACTION_SEND
                            || actionId == EditorInfo.IME_ACTION_DONE
                            || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {

                        if (sousuo.size()<10){
                            sousuo.add(0,Ed_txt);
                        }else {
                            sousuo.remove(sousuo.size()-1);
                            sousuo.add(0,Ed_txt);

                        }
                        updateTagView(false);
                        url=urlUtil+UrlUtil.all_shop+"?word="+Ed_txt;
                        searchData();
                        return true;
                    }
                }
                return false;
            }
        });
        reportPageShown();
    }

    private void reportPageShown(){
        Bundle b=new Bundle();
        b.putString("type","LAUNCH");
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.SHOW_SEARCHPAGE,b);
    }

    private void initRecycleView(){
        mDataAdapter = new MsgAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        resultView.setAdapter(mLRecyclerViewAdapter);
        //添加分割线
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.right_padding)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.transparent)
                .build();

        //mRecyclerView.setHasFixedSize(true);
        resultView.addItemDecoration(divider);

        resultView.setLayoutManager(new LinearLayoutManager(this));

        //设置下拉刷新Progress的样式
        resultView.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        //设置下拉刷新箭头
        //resultView.setArrowImageView(R.drawable.load_1);
        resultView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);


        //下拉刷新
        resultView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                searchData();

            }
        });
        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
        resultView.setLoadMoreEnabled(false);

        resultView.setLScrollListener(new LRecyclerView.LScrollListener() {

            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollDown() {
            }


            @Override
            public void onScrolled(int distanceX, int distanceY) {
            }

            @Override
            public void onScrollStateChanged(int state) {

            }

        });

        //设置头部加载颜色
        resultView.setHeaderViewColor(R.color.colorAccent, R.color.login_color, android.R.color.white);
        //设置底部加载颜色
        resultView.setFooterViewColor(R.color.login_color, R.color.login_color, android.R.color.white);
        //fecoList.setFooterViewColor(R.color.colorAccent, R.color.login_color ,android.R.color.white);
        //设置底部加载文字提示
        resultView.setFooterViewHint(getResources().getString(R.string.listview_loading), "到底啦~到底啦~", "网络不给力啊，点击再试一次吧");
        noNetworkView.findViewById(R.id.tv_retry_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading(getResources().getString(R.string.listview_loading));
                searchData();
            }
        });
        //条目的点击事件

        mDataAdapter.setOnItemClickListener(new MsgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mDataAdapter.getDataList().size() > position) {
                    SPUtil.put(SearchActivity.this,Constants.SP_KEY_STORE_DETAIL_FROM,Constants.GO_STORE_DETAIL_FROM_SEARCH);
                    ShopBeanNew shopBean = mDataAdapter.getDataList().get(position);
                    Intent intent = new Intent(SearchActivity.this, StoreDetailActivity.class);
                    intent.putExtra("id",shopBean.getId());
                    intent.putExtra("flags",1);
                    intent.putExtra("closingSoon",shopBean.getOperatingStatus());
                    intent.putExtra("distances",shopBean.getDistance()+"");
                    startActivity(intent);
                }

            }

        });
    }
    private void sortData(){
        ShopBeanNew.sSortType= Constants.SORT_TYPE.SORT_TYPE_DEFAULT;
        List<ShopBeanNew> closeShops=new ArrayList<>();
        List<ShopBeanNew> openingShops=new ArrayList<>();
        for(ShopBeanNew bean:mDatas){
            if(bean!=null&&bean.getOperatingStatus()==2){
                closeShops.add(bean);
            }else{
                openingShops.add(bean);
            }
        }
        Collections.sort(openingShops);
        Collections.sort(closeShops);
        mDatas.clear();
        mDatas.addAll(openingShops);
        mDatas.addAll(closeShops);
    }
    private String url="";
    private long serverTime;
    private static final long HALF_HOUR=30*60*1000;
    private void initShopBeanExactOpeningTime(){
        for(ShopBeanNew bean:mDatas){
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
        for(ShopBeanNew bean:mDatas){
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
    private void searchData(){
        Http.OkHttpGet(SearchActivity.this, url, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                dissLoad();
                resultView.refreshComplete();
                resultView.setVisibility(View.VISIBLE);
                noNetworkView.setVisibility(View.GONE);
                resultLy.setVisibility(View.GONE);
                mDataAdapter.clear();
                searchLabel.setVisibility(View.GONE);
                tagContainer.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("0")) {
                        serverTime = jsonObject.getLong("serverTime");
                        mDatas.clear();
                        JSONArray data = jsonObject.getJSONArray("data");

                        if (data.length() == 0) {
                            resultView.setNoMore(true);
                            resultLy.setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < data.length(); i++) {
                                ShopBeanNew bean = GSonUtil.parseGson(data.getString(i), ShopBeanNew.class);
                                if(MyApplication.getInstance().getLastLocation()!=null) {
                                    double distance = Util.getDistance(bean.getLongitude(), bean.getLatitude(), MyApplication.getInstance().getLastLocation().longitude, MyApplication.getInstance().getLastLocation().latitude);
                                    bean.setDistance(distance);
                                }
                                if(bean.getRedeem()!=null) {
                                    Collections.sort(bean.getRedeem());
                                }
                                mDatas.add(bean);
                            }
                            initShopBeanOpenStatus();
                            sortData();
                            mDataAdapter.addAll(mDatas);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Exception e) {
                dissLoad();
                resultView.setVisibility(View.GONE);
                noNetworkView.setVisibility(View.VISIBLE);
                resultLy.setVisibility(View.GONE);
                searchLabel.setVisibility(View.GONE);
                tagContainer.setVisibility(View.GONE);
            }
        });
    }
    private void reportSearchClick(){
        Bundle b=new Bundle();
        b.putString("type","CLICK_SEARCH");
        String searchWords = sea_ed_map.getText().toString().trim();
        b.putString("word",searchWords);
        FirebaseReportUtils.getInstance().reportEventWithBaseData(FireBaseEventKey.CLICK_DISCOVER_SEARCHPAGE_SEARCHBTN,b);
    }
    @OnClick({R.id.search, R.id.id_flowlayout,R.id.search_cloose,R.id.search_txt,R.id.serach_del,R.id.serarch_caixi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search:
                break;
            case R.id.id_flowlayout:
                break;
            case R.id.search_ad:
                break;
            case R.id.search_txt:
                String Ed_txt = sea_ed_map.getText().toString().trim();
                if (Ed_txt.equals("")){
                    ToastUtil.showgravity(SearchActivity.this,getResources().getString(R.string.sooou_no));
                    return;
                }
                for (int i = 0; i <categoryBeanList.size() ; i++) {
                    List<CategoryBean.SubBean> sub = categoryBeanList.get(i).getSub();
                    String nameEn = sub.get(i).getNameEn();
                    if (Ed_txt.equals(nameEn)){
                        id = sub.get(i).getId();
                    }
                }
                url=urlUtil+UrlUtil.all_shop+"?word="+Ed_txt;
                searchData();
                if (sousuo.size()<10){
                    sousuo.add(0,Ed_txt);
                }else {
                    sousuo.remove(sousuo.size()-1);
                    sousuo.add(0,Ed_txt);

                }
                updateTagView(false);
                reportSearchClick();
                break;
            case R.id.search_cloose:

                soso.setDataList("sousuo",sousuo);
                finish();
                break;
            case R.id.serach_del:
                sousuo.clear();
                soso.DelDataList(sousuo);
                updateTagView(true);
                break;
            case R.id.serarch_caixi:
                serarch_image.setBackgroundResource(R.drawable.topnav_icon_cuisine_s);

                break;
        }
    }
    /**
     * 菜系筛选弹窗
     *
     */
    private void testPopupWindowType2() {
        View contentView = getPopupWindowContentView();
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

        mPopupWindow.setHeight(SCREEN_HEIGHT/2*1);
        // 设置好参数之后再show

        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int xOffset = search_ly.getWidth()/2  - contentView.getMeasuredWidth()/2;

        mPopupWindow.showAsDropDown(search_ly, xOffset, 0);    // 在mButton2的中间显示
    }
    private List<Map<String, String>> mainList;
    private String Sub;
    private View getPopupWindowContentView() {


        // 一个自定义的布局，作为显示的内容
        int layoutId = R.layout.popup_content_layout;   // 布局ID
        View contentView = LayoutInflater.from(this).inflate(layoutId, null);
        ListView lefte_image = contentView.findViewById(R.id.lefte_image);
        right_image = contentView.findViewById(R.id.right_image);
        LefteAdapter lefte = new LefteAdapter(this, mainList);

        lefte.setSelectItem(0);
        lefte_image.setAdapter(lefte);
        lefte_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                city = citys[position];
                initAdapter(citys[position]);
                lefte.setSelectItem(position);
                lefte.notifyDataSetChanged();
            }
        });
        lefte_image.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // 一定要设置这个属性，否则ListView不会刷新
        initAdapter(citys[0]);
        right_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                moreAdapter.setSelectItem(position);
                if (city==null){
                    String[] city = citys[0];
                    String s = city[position];
                    Sub=s;
                }else {
                    String s = city[position];
                    Sub=s;
                }
                seaEd.setText(Sub);

                moreAdapter.notifyDataSetChanged();
            }
        });
        View.OnClickListener menuItemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Click " + ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        };

        //contentView.findViewById(R.id.menu_item1).setOnClickListener(menuItemOnClickListener);

        return contentView;
    }
    RightAdapter moreAdapter;
    private void initAdapter(String[] array) {
        moreAdapter = new RightAdapter(this, array);
        right_image.setAdapter(moreAdapter);
        moreAdapter.notifyDataSetChanged();
    }
    private String[][] citys;
    private List<CategoryBean> categoryBeanList=new ArrayList<CategoryBean>();
    private void initModle() {
        mainList = new ArrayList<Map<String, String>>();
        String cfgURL = (String) SPUtil.get(this, "cfgURL", "");
        Http.OkHttpGet(this, cfgURL, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("0")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject location = data.getJSONObject("Location");
                        JSONArray category = location.getJSONArray("Category");
                        citys = new String[category.length()][];
                        for (int i = 0; i < category.length(); i++) {
                            Map<String, String> map = new HashMap<String, String>();
                            String nameEn = category.getJSONObject(i).getString("NameEn");
                            String nameCn = category.getJSONObject(i).getString("NameCh");
                            map.put("NameEn", nameEn);
                            map.put("NameCh", nameCn);
                            mainList.add(map);
                            JSONArray sub = category.getJSONObject(i).getJSONArray("Sub");
                            String[] citysCol = new String[sub.length()];
                            for (int a = 0; a <citysCol.length ; a++) {
                                citysCol[a] = sub.getJSONObject(a).getString("NameEn");
                            }
                            citys[i]=citysCol;

                            CategoryBean bean = GSonUtil.parseGson(category.getString(i), CategoryBean.class);
                            categoryBeanList.add(bean);
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

    private void initEdt() {

        seaEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    mPop.dismiss();
                } else {
                    //输入内容非空的时候才开始搜索
                    startSearch(s.toString());
                }
            }
        });

        mPublishSubject = PublishSubject.create();
        mPublishSubject.debounce(200, TimeUnit.MILLISECONDS) //这里我们限制只有在输入字符200毫秒后没有字符没有改变时才去请求网络，节省了资源
                .filter(new Predicate<String>() { //对源Observable产生的结果按照指定条件进行过滤，只有满足条件的结果才会提交给订阅者

                    @Override
                    public boolean test(String s) throws Exception {
                        //当搜索词为空时，不发起请求
                        return s.length() > 0;
                    }
                })
                /**
                 * flatmap:把Observable产生的结果转换成多个Observable，然后把这多个Observable
                 “扁平化”成一个Observable，并依次提交产生的结果给订阅者

                 *concatMap:操作符flatMap操作符不同的是，concatMap操作符在处理产生的Observable时，
                 采用的是“连接(concat)”的方式，而不是“合并(merge)”的方式，
                 这就能保证产生结果的顺序性，也就是说提交给订阅者的结果是按照顺序提交的，不会存在交叉的情况

                 *switchMap:与flatMap操作符不同的是，switchMap操作符会保存最新的Observable产生的
                 结果而舍弃旧的结果
                 **/
                .switchMap(new Function<String, ObservableSource<String>>() {

                    @Override
                    public ObservableSource<String> apply(String query) throws Exception {
                        return getSearchObservable(query);
                    }

                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {

                    @Override
                    public void onNext(String s) {
                        //显示搜索联想的结果
                        showSearchResult(s);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(mCompositeDisposable);
    }

    //开始搜索
    private void startSearch(String query) {
        mPublishSubject.onNext(query);
    }

    private Observable<String> getSearchObservable(final String query) {
        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                //注意：这里只是模仿求取服务器数据，实际开发中需要你根据这个输入的关键字query去请求数据
                Log.d(TAG, "开始请求，关键词为：" + query);

                Http.OkHttpGet(SearchActivity.this, UrlUtil.Google_maps+query+"+in+香港", new Http.OnDataFinish() {
                    @Override
                    public void OnSuccess(String result) {
                        if (mSearchList.size()>0){
                            mSearchList.clear(); //先清空数据源
                        }
                        Log.d("oioioiooooo",result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("status").equals("OK")){

                                JSONArray data = jsonObject.getJSONArray("results");
                                for (int i = 0; i <data.length() ; i++) {
                                    String name = data.getJSONObject(i).getString("name");
                                    GoogleMapBean bean = GSonUtil.parseGson(data.getString(i), GoogleMapBean.class);
                                    //mSearchList.add(name);
                                    mSearchList.add(name);
                                }

                            }else {
                                mPop.dismiss();
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
              /*  if (!(query.contains("科") || query.contains("耐") || query.contains("七"))) {
                    //没有联想结果，则关闭pop
                    mPop.dismiss();
                    return;
                }*/
                Log.d("SearchActivity", "结束请求，关键词为：" + query);
                observableEmitter.onNext(query);
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 显示搜索结果
     */
    private void showSearchResult(String keyWords) {

      /*  switch (keyWords) {
            case "科比":
                mSearchList.addAll(Arrays.asList(getResources().getStringArray(R.array.kobe)));
                break;
            case "耐克":
                mSearchList.addAll(Arrays.asList(getResources().getStringArray(R.array.nike)));
                break;
            case "七夕":
                mSearchList.addAll(Arrays.asList(getResources().getStringArray(R.array.qixi)));
                break;
        }*/
        mAdapter.notifyDataSetChanged();

        mPop.showAsDropDown(seaEd, 0, 0); //显示搜索联想列表的pop
    }
    /**
     * 初始化Pop，pop的布局是一个列表
     */
    private void initPop() {

        mPop = new CustomPopupWindow.Builder(this)
                .setContentView(R.layout.pop_search)
                .setwidth(LinearLayout.LayoutParams.MATCH_PARENT)
                .setheight(LinearLayout.LayoutParams.WRAP_CONTENT)
                .setBackgroundAlpha(1f)
                .build();
        searchLv = (ListView) mPop.getItemView(R.id.search_list_lv);

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mSearchList);
        searchLv.setAdapter(mAdapter);

        searchLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, ResultsActivity.class);
                intent.putExtra("reasu", mSearchList.get(position));
                startActivity(intent);
            }
        });

    }
    private View getPopupWindow() {


        // 一个自定义的布局，作为显示的内容
        int layoutId = R.layout.popup_content_lay;   // 布局ID
        View contentView = LayoutInflater.from(this).inflate(layoutId, null);
        ListView serace_image = contentView.findViewById(R.id.serace_image);
        SearAdapter searAdapter = new SearAdapter(this, mSearchList);
        serace_image.setAdapter(searAdapter);


        View.OnClickListener menuItemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Click " + ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();

                }
            }
        };

        return contentView;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        soso.setDataList("sousuo",sousuo);
    }
}
