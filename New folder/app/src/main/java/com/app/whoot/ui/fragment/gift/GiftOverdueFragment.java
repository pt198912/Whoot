package com.app.whoot.ui.fragment.gift;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.whoot.R;
import com.app.whoot.adapter.GiftOverdueAdapter;
import com.app.whoot.base.fragment.BaseFragment;

import com.app.whoot.bean.GiftOverBean;
import com.app.whoot.modle.http.Http;

import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.NetworkUtils;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ToastUtil;
import com.app.whoot.util.UrlUtil;
import com.facebook.CallbackManager;
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
import butterknife.Unbinder;

/**
 * Created by Sunrise on 1/14/2019.
 * 已使用礼包
 */

public class GiftOverdueFragment extends BaseFragment {
    @BindView(R.id.gift_overdue_view)
    LRecyclerView giftOverdueView;
    @BindView(R.id.gift_overdue_li)
    LinearLayout giftOverdueLi;
    @BindView(R.id.no_network_view)
    View noNetworkView;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private GiftOverdueAdapter mDataAdapter = null;
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


    private String url = "";
    private int rows = 1;

    private List<GiftOverBean> list = new ArrayList<GiftOverBean>();

    private CallbackManager callbackManager;

    private String token;

    private int fac_num = 0;  //控制缺省页显示
    private String urlUtil;

    protected boolean isCreated = false;
    @Override
    public int getLayoutId() {
        return R.layout.frag_overdue;
    }

    @Override
    public void onViewCreatedInit() {

        isCreated=true;
        urlUtil = (String) SPUtil.get(getActivity(), "URL", "");
        token = (String) SPUtil.get(getActivity(), "Token", "");
        mDataAdapter = new GiftOverdueAdapter(getActivity());
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        giftOverdueView.setAdapter(mLRecyclerViewAdapter);
        //添加分割线
        DividerDecoration divider = new DividerDecoration.Builder(getActivity())
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.hen)
                .build();

        giftOverdueView.setHasFixedSize(true);
        //giftOverdueView.addItemDecoration(divider);

        giftOverdueView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //设置下拉刷新Progress的样式
        giftOverdueView.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        //设置下拉刷新箭头
        //giftView.setArrowImageView(R.drawable.load_1);
        giftOverdueView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);

        //下拉刷新
        giftOverdueView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //page = 1;
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                flag = 2;
                mCurrentCounter = 0;
                url = urlUtil + UrlUtil.historygift + "?start=0" + "&rows=" + 5;
                rows = 1;
                requestData();

            }
        });
        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
        giftOverdueView.setLoadMoreEnabled(true);
        //加载更多
        giftOverdueView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                rows++;
                url = urlUtil + UrlUtil.giftlist + "?start=" + (rows - 1) * 5 + "&rows=" + 5;
                requestData();
            }
        });

        giftOverdueView.setLScrollListener(new LRecyclerView.LScrollListener() {

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
        giftOverdueView.setHeaderViewColor(R.color.colorAccent, R.color.login_color, android.R.color.white);
        //设置底部加载颜色
        giftOverdueView.setFooterViewColor(R.color.login_color, R.color.login_color, android.R.color.white);
        //fecoList.setFooterViewColor(R.color.colorAccent, R.color.login_color ,android.R.color.white);
        //设置底部加载文字提示
        giftOverdueView.setFooterViewHint(getResources().getString(R.string.listview_loading), "到底啦~到底啦~", "网络不给力啊，点击再试一次吧");

        giftOverdueView.refresh();
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
        noNetworkView.findViewById(R.id.tv_retry_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading(getResources().getString(R.string.listview_loading));
                requestData();
            }
        });
    }
    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }


    private void addItems(List<GiftOverBean> list) {

        mDataAdapter.addAll(list);

        mCurrentCounter += list.size();

    }
    private class PreviewHandler extends Handler {

        private WeakReference<GiftOverdueFragment> ref;

        PreviewHandler(GiftOverdueFragment activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final GiftOverdueFragment activity = ref.get();
            if (activity == null) {
                return;
            }

            switch (msg.what) {

                case -1:
                    if(isDetached()){
                        return;
                    }
                    int currentSize = activity.mDataAdapter.getItemCount();
                    if (token == null) {
                        token = "";
                    }
                    if (token.length() != 0) {

                        Log.d("lkjhg", url);
                        Http.OkHttpGet(getActivity(), url, new Http.OnDataFinish() {
                            @Override
                            public void OnSuccess(String result) {
                                if(isDetached()){
                                    return;
                                }
                                dissLoad();
                                noNetworkView.setVisibility(View.GONE);
                                giftOverdueView.setVisibility(View.VISIBLE);
                                if (flag == 2) {
                                    mDataAdapter.clear();
                                    list.clear();
                                }
                                if (rows==1){
                                    mDataAdapter.clear();
                                    list.clear();
                                }
                                Log.d("有效礼包记录111", result + "==" + url);
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String code = jsonObject.getString("code");
                                    if (code.equals("0")) {
                                        list.clear();

                                        JSONArray data = jsonObject.getJSONArray("data");
                                        if (data.length() > 0) {
                                            giftOverdueLi.setVisibility(View.GONE);
                                            giftOverdueView.setVisibility(View.VISIBLE);
                                            fac_num = 1;
                                            for (int i = 0; i < data.length(); i++) {
                                                GiftOverBean bean = GSonUtil.parseGson(data.getString(i), GiftOverBean.class);
                                                list.add(bean);
                                            }
                                            activity.addItems(list);
                                        } else {
                                            giftOverdueView.setNoMore(true);
                                            if (fac_num == 0) {
                                                Log.d("youyiyiyi", list.size() + "");
                                                giftOverdueView.setVisibility(View.GONE);
                                                giftOverdueLi.setVisibility(View.VISIBLE);
                                            }


                                        }

                                    } else {
                                        //Toast.makeText(getActivity(),getResources().getString(R.string.jia),Toast.LENGTH_SHORT).show();
                                        ToastUtil.showgravity(getActivity(), getResources().getString(R.string.jia));
                                    }
                                    try {
                                        activity.giftOverdueView.refreshComplete();
                                    } catch (Exception e) {

                                    }


                                    flag = 1;
                                    fac_num=0;
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
                                noNetworkView.setVisibility(View.VISIBLE);
                                giftOverdueView.setVisibility(View.GONE);
                                giftOverdueLi.setVisibility(View.GONE);
                            }
                        });
                    }
                    break;
                case -3:
                    if(isDetached()){
                        return;
                    }
                    dissLoad();
                    noNetworkView.setVisibility(View.VISIBLE);
                    giftOverdueView.setVisibility(View.GONE);
                    giftOverdueLi.setVisibility(View.GONE);
                    //网络请求失败的情景
                    activity.giftOverdueView.refreshComplete();
                    activity.notifyDataSetChanged();
                    activity.giftOverdueView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
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
                if (NetworkUtils.isNetAvailable(getContext())) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    @Override
    public void onStartInit() {

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        loadData(isVisibleToUser,false);
    }

    private static final String TAG = "GiftOverdueFragment";

    private void loadData(boolean isVisibleToUser,boolean forResume){

        if (!isCreated){
            return;
        }

        if (isVisibleToUser){
            token = (String) SPUtil.get(getActivity(), "Token", "");
            flag = 2;
            mCurrentCounter = 0;
            url = urlUtil + UrlUtil.historygift + "?start=0" + "&rows=" + 5;
            rows = 1;
            if(!forResume) {
                loading(getResources().getString(R.string.listview_loading));
            }
            requestData();
            Log.d("2222222222222","执行啦333");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(true,true);
    }
}
