package com.app.whoot.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.adapter.ExchangeAdapter;
import com.app.whoot.adapter.MsgAdapter;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.ExchBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.view.recycler.adapter.StandardAdapter;
import com.app.whoot.ui.view.recycler.decoration.CustomRefreshLayout;
import com.app.whoot.ui.view.recycler.decoration.DrawableItemDecoration;
import com.app.whoot.ui.view.recycler.decoration.RefreshRecyclerView;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.NetworkUtils;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.UrlUtil;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.JarFile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

/**
 * Created by Sunrise on 4/3/2018.
 * 兑换记录
 */

public class ExchangeActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.refreshVie)
    LRecyclerView refreshVie;
    @BindView(R.id.exchang_ly)
    LinearLayout exchang_ly;
    @BindView(R.id.no_network_view)
    View noNetworkView;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private ExchangeAdapter mDataAdapter = null;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    /**
     * 服务器端一共多少条数据
     */
    private static final int TOTAL_COUNTER = 24;//如果服务器没有返回总数据或者总页数，这里设置为最大值比如10000，什么时候没有数据了根据接口返回判断

    /**
     * 每一页展示多少条数据
     */
    private static final int REQUEST_COUNT = 10;

    /**
     * 已经获取到多少条数据了
     */
    private static int mCurrentCounter = 0;

    /**
     * 标记判断是下拉刷新还是加载更多
     */
    private int flag = 1;

    private int biao=0;

    private String url="";
    private int rows=1;

    private List<ExchBean> list=new ArrayList<ExchBean>();

    private int refresh;
    private int loadMore;
    private String urlUtil;

    @Override
    public int getLayoutId() {
        return R.layout.exchang_activi;
    }

    @Override
    public void onCreateInit() {
        titleBackTitle.setText(R.string.exchange);

        urlUtil = (String) SPUtil.get(ExchangeActivity.this, "URL", "");
        mDataAdapter = new ExchangeAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        refreshVie.setAdapter(mLRecyclerViewAdapter);
        //添加分割线
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.gray)
                .build();

        //mRecyclerView.setHasFixedSize(true);
        refreshVie.addItemDecoration(divider);

        refreshVie.setLayoutManager(new LinearLayoutManager(this));


        //设置下拉刷新Progress的样式
        refreshVie.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        //设置下拉刷新箭头
        //refreshVie.setArrowImageView(R.drawable.load_1);
        refreshVie.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);


        //下拉刷新
        refreshVie.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //page = 1;
                flag = 2;
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                url=urlUtil+ UrlUtil.history+ "?start=0"  + "&rows=" + 5;
                rows=1;
                requestData();

            }
        });
        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
        refreshVie.setLoadMoreEnabled(true);
        //加载更多
        refreshVie.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                rows++;
                url = urlUtil+ UrlUtil.history + "?start=" + (rows - 1) * 5 + "&rows=" + 5;
                requestData();
            }
        });

        refreshVie.setLScrollListener(new LRecyclerView.LScrollListener() {

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
        refreshVie.setHeaderViewColor(R.color.colorAccent, R.color.login_color, android.R.color.white);
        //设置底部加载颜色
        refreshVie.setFooterViewColor(R.color.login_color, R.color.login_color, android.R.color.white);
        //fecoList.setFooterViewColor(R.color.colorAccent, R.color.login_color ,android.R.color.white);
        //设置底部加载文字提示
        refreshVie.setFooterViewHint(getResources().getString(R.string.listview_loading), "到底啦~到底啦~", "网络不给力啊，点击再试一次吧");
        noNetworkView.findViewById(R.id.tv_retry_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading(getResources().getString(R.string.listview_loading));
                requestData();
            }
        });
        loading(getResources().getString(R.string.listview_loading));
        refreshVie.refresh();
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

    @Override
    protected void onResume() {
        super.onResume();
        int pinglun = (int) SPUtil.get(ExchangeActivity.this, "pinglun", 0);
        if (pinglun==1){
            flag = 2;
            mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
            mCurrentCounter = 0;
            url=urlUtil+ UrlUtil.history+ "?start=0"  + "&rows=" + 5;
            rows=1;
            requestData();

            SPUtil.remove(ExchangeActivity.this,"pinglun");
        }

    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }


    private void addItems(List<ExchBean> list) {

        mDataAdapter.addAll(list);

        mCurrentCounter += list.size();

    }

    private class PreviewHandler extends Handler {

        private WeakReference<ExchangeActivity> ref;

        PreviewHandler(ExchangeActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ExchangeActivity activity = ref.get();
            if (activity == null) {
                return;
            }
            switch (msg.what) {

                case -1:
                    int currentSize = activity.mDataAdapter.getItemCount();
                    Http.OkHttpGet(ExchangeActivity.this, url, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            dissLoad();
                            noNetworkView.setVisibility(View.GONE);
                            refreshVie.setVisibility(View.VISIBLE);
                            exchang_ly.setVisibility(View.GONE);
                            if (flag == 2) {
                                mDataAdapter.clear();
                                list.clear();
                            }

                            Log.d("兑换记录",result);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String code = jsonObject.getString("code");
                                if (code.equals("0")){
                                    list.clear();
                                    JSONArray data = jsonObject.getJSONArray("data");
                                    if (data.length()>0){
                                        biao=1;
                                        for (int i = 0; i <data.length() ; i++) {
                                            ExchBean bean = GSonUtil.parseGson(data.getString(i), ExchBean.class);
                                            list.add(bean);
                                        }
                                        activity.addItems(list);
                                    }else {
                                        refreshVie.setNoMore(true);
                                        if (biao==0){
                                            exchang_ly.setVisibility(View.VISIBLE);
                                        }

                                    }

                                }
                                try{
                                    activity.refreshVie.refreshComplete();
                                }catch (Exception e){

                                }


                                flag = 1;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Exception e) {
                            dissLoad();
                            noNetworkView.setVisibility(View.VISIBLE);
                            refreshVie.setVisibility(View.GONE);
                            exchang_ly.setVisibility(View.GONE);
                        }
                    });

                    break;
                case -3:
                    //摸你网络请求失败的情景
                    dissLoad();
                    noNetworkView.setVisibility(View.VISIBLE);
                    refreshVie.setVisibility(View.GONE);
                    exchang_ly.setVisibility(View.GONE);
                    activity.refreshVie.refreshComplete();
                    activity.notifyDataSetChanged();
                    activity.refreshVie.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
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
                if (NetworkUtils.isNetAvailable(ExchangeActivity.this)) {
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
