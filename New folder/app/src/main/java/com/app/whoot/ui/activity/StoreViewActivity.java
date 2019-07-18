package com.app.whoot.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whoot.R;

import com.app.whoot.adapter.StoreViewAdapter;
import com.app.whoot.adapter.TestGridViewAdapter;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.CardBean;
import com.app.whoot.bean.CommBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.view.recycler.adapter.StandardAdapter;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.NetworkUtils;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.jchou.imagereview.ui.ImagePagerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sunrise on 7/13/2018.
 * 店铺评论
 */

public class StoreViewActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;
    @BindView(R.id.store_commview)
    LRecyclerView storeCommview;
    @BindView(R.id.stovie_item_score)
    TextView stovie_item_score;
    @BindView(R.id.stovie_item_score_one)
    TextView stovie_item_score_one;
    @BindView(R.id.stovie_item_img0)
    ImageView cardItemImg0;
    @BindView(R.id.stovie_item_img1)
    ImageView cardItemImg1;
    @BindView(R.id.stovie_item_img2)
    ImageView cardItemImg2;
    @BindView(R.id.stovie_item_img3)
    ImageView cardItemImg3;
    @BindView(R.id.stovie_item_img4)
    ImageView cardItemImg4;


    private StandardAdapter<CardBean> adapter;
    private int refresh;
    private int loadMore;
    private List<CardBean> list = new ArrayList<CardBean>();
    private List<String> imglist = new ArrayList<>();
    private TestGridViewAdapter nearByInfoImgsAdapter;
    private PreviewHandler mHandler = new PreviewHandler(StoreViewActivity.this);
    private StoreViewAdapter mDataAdapter = null;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private String url = "";
    private int rows = 1;

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

    /**
     * 标记判断是下拉刷新还是加载更多
     */
    private int flag = 1;

    private int biao=0;
    private String shopId;
    private String urlUtil;

    @Override
    public int getLayoutId() {
        return R.layout.store_view;
    }

    @Override
    public void onCreateInit() {

        Intent intent = getIntent();
        shopId = intent.getStringExtra("shopId");
        String substring = intent.getStringExtra("substring");
        String grade1 = intent.getStringExtra("grade");
        String pingfen = intent.getStringExtra("pingfen");

        urlUtil = (String) SPUtil.get(StoreViewActivity.this, "URL", "");

        titleBackTitle.setText(getResources().getString(R.string.ratings));

        double grade = Double.parseDouble(grade1);
        if (grade <= 5 && grade >= 4.5) {
            stovie_item_score.setBackgroundResource(R.drawable.home_shape);
        } else if (grade < 4.5 && grade >= 4) {
            stovie_item_score.setBackgroundResource(R.drawable.home_shape_four);
        } else if (grade < 4 && grade >= 3) {
            stovie_item_score.setBackgroundResource(R.drawable.home_shape_three);
        } else if (grade < 3) {
            stovie_item_score.setBackgroundResource(R.drawable.home_shape_twe);
        }
        stovie_item_score.setText(grade + "");

        stovie_item_score_one.setText("("+pingfen+getResources().getString(R.string.home_item_fen));
        String s_xing = DateUtil.formateRate(String.valueOf(grade));
        if (substring.equals("1")) {
            cardItemImg0.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                cardItemImg1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                cardItemImg1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                cardItemImg1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                cardItemImg1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                cardItemImg1.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                cardItemImg1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                cardItemImg1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                cardItemImg1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                cardItemImg1.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                cardItemImg1.setBackgroundResource(R.drawable.icon_star_0);
            }
            cardItemImg2.setBackgroundResource(R.drawable.icon_star_0);
            cardItemImg3.setBackgroundResource(R.drawable.icon_star_0);
            cardItemImg4.setBackgroundResource(R.drawable.icon_star_0);
        } else if (substring.equals("2")) {
            cardItemImg0.setBackgroundResource(R.drawable.icon_star_10);
            cardItemImg1.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                cardItemImg2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                cardItemImg2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                cardItemImg2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                cardItemImg2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                cardItemImg2.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                cardItemImg2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                cardItemImg2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                cardItemImg2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                cardItemImg2.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                cardItemImg2.setBackgroundResource(R.drawable.icon_star_0);
            }
            cardItemImg3.setBackgroundResource(R.drawable.icon_star_0);
            cardItemImg4.setBackgroundResource(R.drawable.icon_star_0);
        } else if (substring.equals("3")) {
            cardItemImg0.setBackgroundResource(R.drawable.icon_star_10);
            cardItemImg1.setBackgroundResource(R.drawable.icon_star_10);
            cardItemImg2.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                cardItemImg3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                cardItemImg3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                cardItemImg3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                cardItemImg3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                cardItemImg3.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                cardItemImg3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                cardItemImg3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                cardItemImg3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                cardItemImg3.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                cardItemImg3.setBackgroundResource(R.drawable.icon_star_0);
            }
            cardItemImg4.setBackgroundResource(R.drawable.icon_star_0);
        } else if (substring.equals("4")) {
            cardItemImg0.setBackgroundResource(R.drawable.icon_star_10);
            cardItemImg1.setBackgroundResource(R.drawable.icon_star_10);
            cardItemImg2.setBackgroundResource(R.drawable.icon_star_10);
            cardItemImg3.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                cardItemImg4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                cardItemImg4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                cardItemImg4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                cardItemImg4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                cardItemImg4.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                cardItemImg4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                cardItemImg4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                cardItemImg4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                cardItemImg4.setBackgroundResource(R.drawable.icon_star_10);
            }else {
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

        mDataAdapter = new StoreViewAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        storeCommview.setAdapter(mLRecyclerViewAdapter);
        //添加分割线
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.filter_pop)
                .build();

        //mRecyclerView.setHasFixedSize(true);
        storeCommview.addItemDecoration(divider);

        storeCommview.setLayoutManager(new LinearLayoutManager(this));
        //add a HeaderView
        final View header = LayoutInflater.from(this).inflate(R.layout.sample_header,(ViewGroup)findViewById(android.R.id.content), false);
        TextView header_item_score = header.findViewById(R.id.header_item_score);
        ImageView header_item_img0 = header.findViewById(R.id.header_item_img0);
        ImageView header_item_img1 = header.findViewById(R.id.header_item_img1);
        ImageView header_item_img2 = header.findViewById(R.id.header_item_img2);
        ImageView header_item_img3 = header.findViewById(R.id.header_item_img3);
        ImageView header_item_img4 = header.findViewById(R.id.header_item_img4);
        TextView header_item_score_one = header.findViewById(R.id.header_item_score_one);
        TextView header_item_distance = header.findViewById(R.id.header_item_distance);

        if (grade <= 5 && grade >= 4.5) {
            header_item_score.setBackgroundResource(R.drawable.home_shape);
        } else if (grade < 4.5 && grade >= 4) {
            header_item_score.setBackgroundResource(R.drawable.home_shape_four);
        } else if (grade < 4 && grade >= 3) {
            header_item_score.setBackgroundResource(R.drawable.home_shape_three);
        } else if (grade < 3) {
            header_item_score.setBackgroundResource(R.drawable.home_shape_twe);
        }
        header_item_score.setText(grade + "");
        header_item_score_one.setText("("+pingfen+getResources().getString(R.string.home_item_fen));

        if (substring.equals("1")) {
            header_item_img0.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                header_item_img1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                header_item_img1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                header_item_img1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                header_item_img1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                header_item_img1.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                header_item_img1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                header_item_img1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                header_item_img1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                header_item_img1.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                header_item_img1.setBackgroundResource(R.drawable.icon_star_0);
            }

            header_item_img2.setBackgroundResource(R.drawable.icon_star_0);
            header_item_img3.setBackgroundResource(R.drawable.icon_star_0);
            header_item_img4.setBackgroundResource(R.drawable.icon_star_0);
        } else if (substring.equals("2")) {
            header_item_img0.setBackgroundResource(R.drawable.icon_star_10);
            header_item_img1.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                header_item_img2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                header_item_img2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                header_item_img2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                header_item_img2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                header_item_img2.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                header_item_img2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                header_item_img2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                header_item_img2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                header_item_img2.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                header_item_img2.setBackgroundResource(R.drawable.icon_star_0);
            }
            header_item_img3.setBackgroundResource(R.drawable.icon_star_0);
            header_item_img4.setBackgroundResource(R.drawable.icon_star_0);
        } else if (substring.equals("3")) {
            header_item_img0.setBackgroundResource(R.drawable.icon_star_10);
            header_item_img1.setBackgroundResource(R.drawable.icon_star_10);
            header_item_img2.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                header_item_img3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                header_item_img3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                header_item_img3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                header_item_img3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                header_item_img3.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                header_item_img3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                header_item_img3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                header_item_img3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                header_item_img3.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                header_item_img3.setBackgroundResource(R.drawable.icon_star_0);
            }
            header_item_img4.setBackgroundResource(R.drawable.icon_star_0);
        } else if (substring.equals("4")) {
            header_item_img0.setBackgroundResource(R.drawable.icon_star_10);
            header_item_img1.setBackgroundResource(R.drawable.icon_star_10);
            header_item_img2.setBackgroundResource(R.drawable.icon_star_10);
            header_item_img3.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                header_item_img4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                header_item_img4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                header_item_img4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                header_item_img4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                header_item_img4.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                header_item_img4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                header_item_img4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                header_item_img4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                header_item_img4.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                header_item_img4.setBackgroundResource(R.drawable.icon_star_0);
            }

        } else if (substring.equals("5")) {
            header_item_img0.setBackgroundResource(R.drawable.icon_star_10);
            header_item_img1.setBackgroundResource(R.drawable.icon_star_10);
            header_item_img2.setBackgroundResource(R.drawable.icon_star_10);
            header_item_img3.setBackgroundResource(R.drawable.icon_star_10);
            header_item_img4.setBackgroundResource(R.drawable.icon_star_10);
        } else {
            header_item_img0.setBackgroundResource(R.drawable.icon_star_0);
            header_item_img1.setBackgroundResource(R.drawable.icon_star_0);
            header_item_img2.setBackgroundResource(R.drawable.icon_star_0);
            header_item_img3.setBackgroundResource(R.drawable.icon_star_0);
            header_item_img4.setBackgroundResource(R.drawable.icon_star_0);
        }

        mLRecyclerViewAdapter.addHeaderView(header);

        //设置下拉刷新Progress的样式
        storeCommview.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        //设置下拉刷新箭头
        //storeCommview.setArrowImageView(R.drawable.load_1);
        storeCommview.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);


        //下拉刷新
        storeCommview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

                url = urlUtil + UrlUtil.comme + "?locationId=1" + "&shopId=" + shopId+"&start=0"  + "&rows=" + 5;
                flag = 2;
                rows=1;
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                requestData();

            }
        });
        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
        storeCommview.setLoadMoreEnabled(true);
        //加载更多
        storeCommview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                rows++;
                url = urlUtil+ UrlUtil.comme + "?locationId=1" + "&shopId=" + shopId+ "&start=" + (rows - 1) * 5 + "&rows=" + 5;

                requestData();
            }
        });

        storeCommview.setLScrollListener(new LRecyclerView.LScrollListener() {

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
        storeCommview.setHeaderViewColor(R.color.colorAccent, R.color.login_color, android.R.color.white);
        //设置底部加载颜色
        storeCommview.setFooterViewColor(R.color.login_color, R.color.login_color, android.R.color.white);
        //fecoList.setFooterViewColor(R.color.colorAccent, R.color.login_color ,android.R.color.white);
        //设置底部加载文字提示
        storeCommview.setFooterViewHint(getResources().getString(R.string.listview_loading), "到底啦~到底啦~", "网络不给力啊，点击再试一次吧");

        storeCommview.refresh();
        //条目的点击事件
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mDataAdapter.getDataList().size() > position) {

                }

            }

        });
        //条目的长点击事件
        mLRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }
    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }


    private void addItems(List<CardBean> list) {

        mDataAdapter.addAll(list);

        mCurrentCounter += list.size();

    }


    //返回的时候获取数据
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        ActivityCompat.postponeEnterTransition(this);
        if(mDataAdapter!=null&&mDataAdapter.getOnActivityReenterListener()!=null){
            mDataAdapter.getOnActivityReenterListener().onActivityReenterCallback(resultCode,data);
        }
    }

    private class PreviewHandler extends Handler {

        private WeakReference<StoreViewActivity> ref;

        PreviewHandler(StoreViewActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final StoreViewActivity activity = ref.get();
            if (activity == null) {
                return;
            }
            switch (msg.what) {

                case -1:
                    int currentSize = activity.mDataAdapter.getItemCount();
                    Http.OkHttpGet(StoreViewActivity.this, url, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            Log.d("store",url);
                            Log.d("Comment",result);
                            if (flag == 2) {
                                mDataAdapter.clear();
                                list.clear();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String code = jsonObject.getString("code");
                                if (code.equals("0")) {
                                    list.clear();
                                    JSONArray data = jsonObject.getJSONArray("data");
                                    if (data.length() == 0) {

                                        storeCommview.setNoMore(true);

                                    } else {
                                        biao=1;
                                        for (int i = 0; i < data.length(); i++) {

                                            CardBean bean = GSonUtil.parseGson(data.getString(i), CardBean.class);
                                            list.add(bean);
                                        }

                                        activity.addItems(list);
                                    }
                                } else {
                                    //Toast.makeText(StoreViewActivity.this, getResources().getString(R.string.fan), Toast.LENGTH_SHORT).show();
                                    ToastUtil.showgravity(StoreViewActivity.this, getResources().getString(R.string.fan));

                                }
                                try{
                                   activity.storeCommview.refreshComplete();

                                }catch (Exception e){}

                                flag = 1;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

                    break;
                case -3:
                    //摸你网络请求失败的情景
                    activity.storeCommview.refreshComplete();
                    activity.notifyDataSetChanged();
                    activity.storeCommview.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            requestData();
                        }
                    });

                    break;
                default:
                    break;
            }

        }
    }

    private void requestData() {
        // Log.d(TAG, "requestData");
        new Thread() {

            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //模拟一下网络请求失败的情况
                if (NetworkUtils.isNetAvailable(StoreViewActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    @OnClick({R.id.title_back_fl, R.id.title_back_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.title_back_title:
                break;
        }
    }
}
