package com.app.whoot.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.adapter.CommentAdapter;
import com.app.whoot.adapter.MsgAdapter;
import com.app.whoot.adapter.TestGridViewAdapter;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.CommBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.view.custom.MyGridView;
import com.app.whoot.ui.view.recycler.adapter.StandardAdapter;
import com.app.whoot.ui.view.recycler.decoration.CustomRefreshLayout;
import com.app.whoot.ui.view.recycler.decoration.DrawableItemDecoration;
import com.app.whoot.ui.view.recycler.decoration.RefreshRecyclerView;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.NetworkUtils;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.SysUtils;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.ToastUtil;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

/**
 * Created by Sunrise on 4/3/2018.
 * 我的评论
 */

public class CommentActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.comm_ly)
    LinearLayout comm_ly;
    @BindView(R.id.commview)
    LRecyclerView commview;
    @BindView(R.id.no_network_view)
    View noNetworkView;
    private StandardAdapter<CommBean> adapter;
    private int refresh;
    private int loadMore;
    private List<CommBean> list = new ArrayList<CommBean>();
    private List<String> imglist = new ArrayList<>();
    private TestGridViewAdapter nearByInfoImgsAdapter;

    private PreviewHandler mHandler = new PreviewHandler(CommentActivity.this);
    private CommentAdapter mDataAdapter = null;
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
    private String urlUtil;

    @Override
    public int getLayoutId() {
        return R.layout.comm_activi;
    }
    @Override
    public void onCreateInit() {
        titleBackTitle.setText(R.string.comment);
        urlUtil = (String) SPUtil.get(CommentActivity.this, "URL", "");
        mDataAdapter = new CommentAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        commview.setAdapter(mLRecyclerViewAdapter);
        //添加分割线
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.item)
                .build();
        //mRecyclerView.setHasFixedSize(true);
        //commview.addItemDecoration(divider);
        commview.setLayoutManager(new LinearLayoutManager(this));
        //设置下拉刷新Progress的样式
        commview.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        //设置下拉刷新箭头
        //commview.setArrowImageView(R.drawable.load_1);
        commview.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);


        //下拉刷新
        commview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

                url = urlUtil+ UrlUtil.comments + "?start=0" + "&rows=" + 5;
                flag = 2;
                rows=1;
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                requestData();

            }
        });
        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
        commview.setLoadMoreEnabled(false);
        //加载更多
        commview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                rows++;
                url = urlUtil+ UrlUtil.comments + "?start=" + (rows - 1) * 5 + "&rows=" + 5;
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
        commview.setLScrollListener(new LRecyclerView.LScrollListener() {

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
        commview.setHeaderViewColor(R.color.colorAccent, R.color.login_color, android.R.color.white);
        //设置底部加载颜色
        commview.setFooterViewColor(R.color.login_color, R.color.login_color, android.R.color.white);
        //fecoList.setFooterViewColor(R.color.colorAccent, R.color.login_color ,android.R.color.white);
        //设置底部加载文字提示
        commview.setFooterViewHint(getResources().getString(R.string.listview_loading), "到底啦~到底啦~", "网络不给力啊，点击再试一次吧");
        noNetworkView.findViewById(R.id.tv_retry_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading(getResources().getString(R.string.listview_loading));
                requestData();
            }
        });
        loading(getResources().getString(R.string.listview_loading));
        commview.refresh();
        //条目的点击事件
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mDataAdapter.getDataList().size() > position) {
                    Intent intent = new Intent(CommentActivity.this,MeCommentActivity.class);
                    intent.putExtra("commentId",mDataAdapter.getDataList().get(position).getId()+"");
                    intent.putExtra("useCount",mDataAdapter.getDataList().get(position).getUseCount()+"");
                    startActivity(intent);
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
    private void addItems(List<CommBean> list) {

        mDataAdapter.addAll(list);

        mCurrentCounter += list.size();

    }

    private class PreviewHandler extends Handler {

        private WeakReference<CommentActivity> ref;

        PreviewHandler(CommentActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final CommentActivity activity = ref.get();
            if (activity == null) {
                return;
            }

            boolean finishing = isFinishing();
            if (finishing){
               return;
            }

            switch (msg.what) {

                case -1:
                    int currentSize = activity.mDataAdapter.getItemCount();
                    Http.OkHttpGet(CommentActivity.this, url, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            dissLoad();
                            commview.setVisibility(View.VISIBLE);
                            noNetworkView.setVisibility(View.GONE);
                            comm_ly.setVisibility(View.GONE);
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
                                        if (biao==0){
                                            commview.setVisibility(View.GONE);
                                            comm_ly.setVisibility(View.VISIBLE);
                                        }
                                        commview.setNoMore(true);

                                    } else {
                                        biao=1;
                                        for (int i = 0; i < data.length(); i++) {

                                            CommBean bean = GSonUtil.parseGson(data.getString(i), CommBean.class);
                                            list.add(bean);
                                        }

                                        activity.addItems(list);
                                    }
                                } else {
                                    //Toast.makeText(CommentActivity.this, getResources().getString(R.string.fan), Toast.LENGTH_SHORT).show();
                                    ToastUtil.showgravity(CommentActivity.this, getResources().getString(R.string.fan));
                                }
                                try{
                                    activity.commview.refreshComplete();
                                }catch (Exception e){
                                    activity.commview.refreshComplete();
                                }


                                flag = 1;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Exception e) {
                            dissLoad();
                            commview.setVisibility(View.GONE);
                            noNetworkView.setVisibility(View.VISIBLE);
                            comm_ly.setVisibility(View.GONE);
                        }
                    });

                    break;
                case -3:
                    //摸你网络请求失败的情景
                    dissLoad();
                    commview.setVisibility(View.GONE);
                    noNetworkView.setVisibility(View.VISIBLE);
                    comm_ly.setVisibility(View.GONE);
                    activity.commview.refreshComplete();
                    activity.notifyDataSetChanged();
                    activity.commview.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
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
                if (NetworkUtils.isNetAvailable(CommentActivity.this)) {
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

    private int index;


}
