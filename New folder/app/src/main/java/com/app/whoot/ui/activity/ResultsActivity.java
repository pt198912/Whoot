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
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.adapter.CouponAdapter;
import com.app.whoot.adapter.MsgAdapter;
import com.app.whoot.adapter.ResultsAdapter;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.CouBean;
import com.app.whoot.bean.ShopBean;
import com.app.whoot.bean.ShopBeanNew;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.view.recycler.adapter.StandardAdapter;
import com.app.whoot.ui.view.recycler.decoration.CustomRefreshLayout;
import com.app.whoot.ui.view.recycler.decoration.DrawableItemDecoration;
import com.app.whoot.ui.view.recycler.decoration.RefreshRecyclerView;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.NetworkUtils;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ScreenUtils;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.UrlUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sunrise on 4/16/2018.
 * 搜索结果
 */

public class ResultsActivity extends BaseActivity {


    @BindView(R.id.search)
    LinearLayout search;
    @BindView(R.id.result_img)
    ImageView resultImg;
    @BindView(R.id.result_ly)
    LinearLayout resultLy;
    @BindView(R.id.result_view)
    LRecyclerView resultView;
    @BindView(R.id.result_a)
    LinearLayout result_a;
    @BindView(R.id.no_network_view)
    View noNetworkView;
    private List<ShopBeanNew> list = new ArrayList<ShopBeanNew>();
    private StandardAdapter<ShopBeanNew> adapter;
    private int refresh;
    private int loadMore;
    private String name;
    private double price;
    private int id;

    private PreviewHandler mHandler = new PreviewHandler(ResultsActivity.this);
    private MsgAdapter mDataAdapter = null;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    public static ResultsActivity instance;

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
    private int rows = 1;


    private String url;
    private String urlUtil;

    @Override
    public int getLayoutId() {
        return R.layout.activi_result;
    }

    @Override
    public void onCreateInit() {

        instance=this;
        urlUtil = (String) SPUtil.get(ResultsActivity.this, "URL", "");
        Intent intent = getIntent();
        String reasu = intent.getStringExtra("reasu");
        String categoryList = intent.getStringExtra("categoryList");
        String lastidu = (String) SPUtil.get(ResultsActivity.this, "lastidu", "");
        String longtidu = (String) SPUtil.get(ResultsActivity.this, "longtidu", "");
//        url = urlUtil + UrlUtil.shop + "?latitude=" + lastidu + "&longitude=" + longtidu + "&word=" + reasu;
        url=urlUtil+UrlUtil.all_shop+"?word="+reasu;
        Log.d("hhahha", url);
       /* Http.OkHttpGet(ResultsActivity.this, url, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("0")){
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data.length()>0){
                            for (int i = 0; i <data.length() ; i++) {
                                ShopBean bean = GSonUtil.parseGson(data.getString(i), ShopBean.class);
                                list.add(bean);
                            }
                        }else {

                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/



        result_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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


                flag = 2;
                rows = 1;
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                requestData();

            }
        });
        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
        resultView.setLoadMoreEnabled(true);
        //加载更多
        resultView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                rows++;

                requestData();
            }
        });

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
                requestData();
            }
        });
        loading(getResources().getString(R.string.listview_loading));
        resultView.refresh();
        //条目的点击事件

        mDataAdapter.setOnItemClickListener(new MsgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mDataAdapter.getDataList().size() > position) {
                    ShopBeanNew shopBean = mDataAdapter.getDataList().get(position);

                    Intent intent = new Intent(ResultsActivity.this, StoreDetailActivity.class);
                    intent.putExtra("id",shopBean.getId());
                    intent.putExtra("flags",1);
                    intent.putExtra("closingSoon",shopBean.getOperatingStatus());
                    intent.putExtra("distances",shopBean.getDistance()+"");
                    startActivity(intent);
                }

            }

        });


    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }


    private void addItems(List<ShopBeanNew> list) {

        mDataAdapter.addAll(list);

        mCurrentCounter += list.size();

    }

    private class PreviewHandler extends Handler {

        private WeakReference<ResultsActivity> ref;

        PreviewHandler(ResultsActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ResultsActivity activity = ref.get();
            if (activity == null) {
                return;
            }
            switch (msg.what) {

                case -1:

                    int currentSize = activity.mDataAdapter.getItemCount();
                    Http.OkHttpGet(ResultsActivity.this, url, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            dissLoad();
                            resultView.setVisibility(View.VISIBLE);
                            noNetworkView.setVisibility(View.GONE);
                            resultLy.setVisibility(View.GONE);
                            Log.d("guoqi", result);
                            Log.d("guoqiurl", url);
                            if (flag == 2) {
                                mDataAdapter.clear();
                                list.clear();

                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String code = jsonObject.getString("code");
                                    if (code.equals("0")) {
                                        list.clear();
                                        JSONArray data = jsonObject.getJSONArray("data");

                                        if (data.length() == 0) {
                                            resultView.setNoMore(true);
                                            resultLy.setVisibility(View.VISIBLE);
                                        } else {
                                            for (int i = 0; i < data.length(); i++) {

                                                ShopBeanNew bean = GSonUtil.parseGson(data.getString(i), ShopBeanNew.class);
                                                list.add(bean);
                                            }
                                            activity.addItems(list);
                                        }


                                    }
                                    try{
                                        activity.resultView.refreshComplete();
                                    }catch (Exception e){}


                                    flag = 1;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else if (flag == 1) {
                                list.clear();
                                resultView.setNoMore(true);
                            }

                        }

                        @Override
                        public void onError(Exception e) {
                            dissLoad();
                            resultView.setVisibility(View.GONE);
                            noNetworkView.setVisibility(View.VISIBLE);
                            resultLy.setVisibility(View.GONE);
                        }
                    });

                    break;
                case -3:
                    //摸你网络请求失败的情景
                    dissLoad();
                    resultView.setVisibility(View.GONE);
                    noNetworkView.setVisibility(View.VISIBLE);
                    resultLy.setVisibility(View.GONE);
                    activity.resultView.refreshComplete();
                    activity.notifyDataSetChanged();
                    activity.resultView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
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
                if (NetworkUtils.isNetAvailable(ResultsActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

//    private void initRecyclerView(RecyclerView recyclerView) {
//        adapter = new StandardAdapter<ShopBean>(R.layout.item_expandable_lv0, null) {
//            @Override
//            protected void convert(BaseViewHolder holder, ShopBean item) {
//                //  holder.setText(R.id.tv_lv0_title, item);
//                ImageView iv_head = holder.getView(R.id.iv_head);
//                TextView frag_item_name = holder.getView(R.id.frag_item_name);
//                TextView frag_item_score = holder.getView(R.id.frag_item_score);
//                ImageView item_ex_0 = holder.getView(R.id.item_ex_0);
//                ImageView item_ex_1 = holder.getView(R.id.item_ex_1);
//                ImageView item_ex_2 = holder.getView(R.id.item_ex_2);
//                ImageView item_ex_3 = holder.getView(R.id.item_ex_3);
//                ImageView item_ex_4 = holder.getView(R.id.item_ex_4);
//                TextView frag_item_score_one = holder.getView(R.id.frag_item_score_one);
//                TextView frag_item_distance = holder.getView(R.id.frag_item_distance);
//                TextView frag_item_dish = holder.getView(R.id.frag_item_dish);
//                TextView frag_item_money = holder.getView(R.id.frag_item_money);
//                name = item.getName();
//                id = item.getId();
//                frag_item_name.setText(name);
//                int type = item.getType();
//                if (type == 1) {
//                    frag_item_dish.setText(getResources().getString(R.string.zhong));
//                } else if (type == 2) {
//                    frag_item_dish.setText(getResources().getString(R.string.yin));
//                } else if (type == 3) {
//                    frag_item_dish.setText(getResources().getString(R.string.riben));
//                } else if (type == 4) {
//                    frag_item_dish.setText(getResources().getString(R.string.kaifei));
//                } else if (type == 5) {
//                    frag_item_dish.setText(getResources().getString(R.string.jiuba));
//                } else if (type == 6) {
//                    frag_item_dish.setText(getResources().getString(R.string.bingji));
//                }
//                double latitude = item.getLatitude();
//                double longitude = item.getLongitude();
//                String latitude_o = (String) SPUtil.get(ResultsActivity.this, "lastidu", "");
//                String longitude_o = (String) SPUtil.get(ResultsActivity.this, "longtidu", "");
//                double distance = getDistance(Double.parseDouble(longitude_o), Double.parseDouble(latitude_o), longitude, latitude);
//                frag_item_distance.setText(distance + getResources().getString(R.string.m));
//
//                price = item.getPrice();
//                frag_item_money.setText("$" + price);
//                double grade = item.getGrade();
//                frag_item_score.setText(grade + "");
//                if (grade <= 5 && grade >= 4.5) {
//                    frag_item_score.setBackgroundResource(R.drawable.home_shape);
//                } else if (grade < 4.5 && grade >= 4) {
//                    frag_item_score.setBackgroundResource(R.drawable.home_shape_four);
//                } else if (grade < 4 && grade >= 3) {
//                    frag_item_score.setBackgroundResource(R.drawable.home_shape_three);
//                } else if (grade < 3) {
//                    frag_item_score.setBackgroundResource(R.drawable.home_shape_twe);
//                }
//
//                String substring = String.valueOf(grade).substring(0, 1);
//                if (substring.equals("1")) {
//                    item_ex_0.setBackgroundResource(R.drawable.icon_star_10);
//                    item_ex_1.setBackgroundResource(R.drawable.icon_star_0);
//                    item_ex_2.setBackgroundResource(R.drawable.icon_star_0);
//                    item_ex_3.setBackgroundResource(R.drawable.icon_star_0);
//                    item_ex_4.setBackgroundResource(R.drawable.icon_star_0);
//                } else if (substring.equals("2")) {
//                    item_ex_0.setBackgroundResource(R.drawable.icon_star_10);
//                    item_ex_1.setBackgroundResource(R.drawable.icon_star_10);
//                    item_ex_2.setBackgroundResource(R.drawable.icon_star_0);
//                    item_ex_3.setBackgroundResource(R.drawable.icon_star_0);
//                    item_ex_4.setBackgroundResource(R.drawable.icon_star_0);
//                } else if (substring.equals("3")) {
//                    item_ex_0.setBackgroundResource(R.drawable.icon_star_10);
//                    item_ex_1.setBackgroundResource(R.drawable.icon_star_10);
//                    item_ex_2.setBackgroundResource(R.drawable.icon_star_10);
//                    item_ex_3.setBackgroundResource(R.drawable.icon_star_0);
//                    item_ex_4.setBackgroundResource(R.drawable.icon_star_0);
//                } else if (substring.equals("4")) {
//                    item_ex_0.setBackgroundResource(R.drawable.icon_star_10);
//                    item_ex_1.setBackgroundResource(R.drawable.icon_star_10);
//                    item_ex_2.setBackgroundResource(R.drawable.icon_star_10);
//                    item_ex_3.setBackgroundResource(R.drawable.icon_star_10);
//                    item_ex_4.setBackgroundResource(R.drawable.icon_star_0);
//                } else if (substring.equals("5")) {
//                    item_ex_0.setBackgroundResource(R.drawable.icon_star_10);
//                    item_ex_1.setBackgroundResource(R.drawable.icon_star_10);
//                    item_ex_2.setBackgroundResource(R.drawable.icon_star_10);
//                    item_ex_3.setBackgroundResource(R.drawable.icon_star_10);
//                    item_ex_4.setBackgroundResource(R.drawable.icon_star_10);
//                } else {
//                    item_ex_0.setBackgroundResource(R.drawable.icon_star_0);
//                    item_ex_1.setBackgroundResource(R.drawable.icon_star_0);
//                    item_ex_2.setBackgroundResource(R.drawable.icon_star_0);
//                    item_ex_3.setBackgroundResource(R.drawable.icon_star_0);
//                    item_ex_4.setBackgroundResource(R.drawable.icon_star_0);
//                }
//                try {
//                    frag_item_score_one.setText("(" + item.getCommentCount() + String.format(getString(R.string.home_item_fen)));
//                } catch (Exception e) {
//                    frag_item_score_one.setText("(0" + String.format(getString(R.string.home_item_fen)));
//                }
//
//            }
//        };
//        DrawableItemDecoration itemDecoration = new DrawableItemDecoration(ResultsActivity.this, DrawableItemDecoration.VERTICAL);
//        itemDecoration.setDrawable(ContextCompat.getDrawable(ResultsActivity.this, R.drawable.shape_line_dp2));
//        recyclerView.setLayoutManager(new LinearLayoutManager(ResultsActivity.this));
//        recyclerView.addItemDecoration(itemDecoration);//添加分割线
//        recyclerView.setAdapter(adapter);



//        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(ResultsActivity.this, StoreDetailActivity.class);
//                intent.putExtra("id", id);
//                intent.putExtra("closingSoon", list.get(position).getOperatingStatus()==1);
//                intent.putExtra("distance", list.get(position).getDistance() + "");
//                startActivity(intent);
//            }
//        });
//    }

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
}
