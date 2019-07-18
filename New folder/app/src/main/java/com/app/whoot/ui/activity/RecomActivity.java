package com.app.whoot.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.adapter.CommentAdapter;
import com.app.whoot.adapter.RecomAdapter;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.ReddBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.view.custom.MyListView;
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
 * Created by Sunrise on 4/24/2018.
 *
 * Top 5商品
 */

public class RecomActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.title_back_txt)
    TextView titleBackTxt;
    @BindView(R.id.recom_listview)
    LRecyclerView recomListview;
    @BindView(R.id.no_network_view)
    View noNetworkView;

    private List<ReddBean> list=new ArrayList<ReddBean>();
    private String urlUtil;

    private PreviewHandler mHandler = new PreviewHandler(RecomActivity.this);
    private RecomAdapter mDataAdapter = null;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
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
    private String url = "";
    private int rows = 1;

    @Override
    public int getLayoutId() {
        return R.layout.recom_card;
    }

    @Override
    public void onCreateInit() {
        titleBackTitle.setText(R.string.top);

        urlUtil = (String) SPUtil.get(RecomActivity.this, "URL", "");
        Intent intent = getIntent();
        int top_id = intent.getIntExtra("Top_id", 0);

        mDataAdapter = new RecomAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        recomListview.setAdapter(mLRecyclerViewAdapter);
        //添加分割线
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.item)
                .build();
        //mRecyclerView.setHasFixedSize(true);
        //recomListview.addItemDecoration(divider);
        recomListview.setLayoutManager(new LinearLayoutManager(this));
        //设置下拉刷新Progress的样式
        recomListview.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        //设置下拉刷新箭头
        //recomListview.setArrowImageView(R.drawable.load_1);
        recomListview.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);


        //下拉刷新
        recomListview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

                url = urlUtil + UrlUtil.shop_info + "?shopId=" + top_id + "&start=0" + "&rows=" + 5;
                flag = 2;
                rows=1;
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                requestData();

            }
        });
        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
        recomListview.setLoadMoreEnabled(false);
        recomListview.setLoadingEnabled(true);
        //加载更多
        recomListview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                rows++;
                url = urlUtil + UrlUtil.shop_info + "?shopId=" + top_id +"&start=" + (rows - 1) * 5 + "&rows=" + 5;
                requestData();
            }
        });
        //TODO 暂时放下
        mDataAdapter.setOnRecyclerViewListener(new CommentAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(TextView ation_item_more, int position) {


            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });
        recomListview.setLScrollListener(new LRecyclerView.LScrollListener() {

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
        recomListview.setHeaderViewColor(R.color.colorAccent, R.color.login_color, android.R.color.white);
        //设置底部加载颜色
        recomListview.setFooterViewColor(R.color.login_color, R.color.login_color, android.R.color.white);
        //fecoList.setFooterViewColor(R.color.colorAccent, R.color.login_color ,android.R.color.white);
        //设置底部加载文字提示
        recomListview.setFooterViewHint(getResources().getString(R.string.listview_loading), "到底啦~到底啦~", "网络不给力啊，点击再试一次吧");
        noNetworkView.findViewById(R.id.tv_retry_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading(getResources().getString(R.string.listview_loading));
                requestData();
            }
        });
        loading(getResources().getString(R.string.listview_loading));
        recomListview.refresh();
        //条目的点击事件
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mDataAdapter.getDataList().size() > position) {
                    String describe = mDataAdapter.getDataList().get(position).getDescribe(); //描述
                    String imgUrl = list.get(position).getImgUrl(); //图片
                    String name = list.get(position).getName();    //名字
                    double price = list.get(position).getPrice();  //价格
                    Intent intent1 = new Intent(RecomActivity.this, DishesActivity.class);
                    intent1.putExtra("describe",describe);
                    intent1.putExtra("imgUrl",imgUrl);
                    intent1.putExtra("name",name);
                    intent1.putExtra("price",price);
                    startActivity(intent1);
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
    private void addItems(List<ReddBean> list) {

        mDataAdapter.addAll(list);

        mCurrentCounter += list.size();

    }

    private class PreviewHandler extends Handler {

        private WeakReference<RecomActivity> ref;

        PreviewHandler(RecomActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final RecomActivity activity = ref.get();
            if (activity == null) {
                return;
            }

            boolean finishing = isFinishing();
            if (finishing){
                return;
            }

            switch (msg.what) {

                case -1:

                    Http.OkHttpGet(RecomActivity.this, url, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            dissLoad();
                            recomListview.setVisibility(View.VISIBLE);
                            noNetworkView.setVisibility(View.GONE);

                            Log.d("Comment",url+"=="+result);
                            if (flag == 2) {
                                mDataAdapter.clear();
                                list.clear();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String code = jsonObject.getString("code");
                                if (code.equals("0")) {
                                    list.clear();
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    JSONArray hotCommodity = data.getJSONArray("hotCommodity");
                                    if (hotCommodity.length() == 0) {

                                        recomListview.setNoMore(true);

                                    } else {

                                        for (int i = 0; i < hotCommodity.length(); i++) {
                                            ReddBean bean = GSonUtil.parseGson(hotCommodity.getString(i), ReddBean.class);
                                            list.add(bean);
                                        }

                                        activity.addItems(list);
                                    }
                                } else {

                                    ToastUtil.showgravity(RecomActivity.this, getResources().getString(R.string.fan));
                                }
                                try{
                                    activity.recomListview.refreshComplete();
                                }catch (Exception e){
                                    activity.recomListview.refreshComplete();
                                }


                                flag = 1;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Exception e) {
                            dissLoad();
                            recomListview.setVisibility(View.GONE);
                            noNetworkView.setVisibility(View.VISIBLE);

                        }
                    });

                    break;
                case -3:
                    //摸你网络请求失败的情景
                    if (isDestroyed() || isFinishing()) {
                        return;
                    }
                    dissLoad();
                    recomListview.setVisibility(View.GONE);
                    noNetworkView.setVisibility(View.VISIBLE);
                    activity.recomListview.refreshComplete();
                    activity.notifyDataSetChanged();
                    activity.recomListview.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
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
                if (NetworkUtils.isNetAvailable(RecomActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }


    @OnClick({R.id.title_back_fl, R.id.title_back_title, R.id.title_back_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.title_back_title:
                break;
            case R.id.title_back_txt:
                break;
        }
    }


}
