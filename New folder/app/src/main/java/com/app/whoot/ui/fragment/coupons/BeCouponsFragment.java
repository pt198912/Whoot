package com.app.whoot.ui.fragment.coupons;

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
import com.app.whoot.adapter.BeCouponAdapter;
import com.app.whoot.adapter.GiftOverdueAdapter;
import com.app.whoot.base.fragment.BaseFragment;
import com.app.whoot.bean.GiftListBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.GiftItemActivity;
import com.app.whoot.ui.attr.EventBusCarrier;
import com.app.whoot.ui.fragment.gift.GiftOverdueFragment;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
 * Created by Sunrise on 1/15/2019.
 * 过期优惠卷
 */

public class BeCouponsFragment extends BaseFragment {


    @BindView(R.id.coupon_overdue_view)
    LRecyclerView couponOverdueView;
    @BindView(R.id.coupon_overdue_li)
    LinearLayout couponOverdueLi;
    @BindView(R.id.no_network_view)
    View noNetworkView;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private BeCouponAdapter mDataAdapter = null;
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

    private List<GiftListBean> list = new ArrayList<GiftListBean>();

    private CallbackManager callbackManager;

    private String token;

    private int fac_num = 0;  //控制缺省页显示
    private String urlUtil;

    protected boolean isCreated = false;

    @Override
    public int getLayoutId() {
        return R.layout.be_coupons;
    }

    @Override
    public void onViewCreatedInit() {

        isCreated=true;
        EventBus.getDefault().register(this);  //事件的注册
        urlUtil = (String) SPUtil.get(getActivity(), "URL", "");
        token = (String) SPUtil.get(getActivity(), "Token", "");
        mDataAdapter = new BeCouponAdapter(getActivity());
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        couponOverdueView.setAdapter(mLRecyclerViewAdapter);
        //添加分割线
        DividerDecoration divider = new DividerDecoration.Builder(getActivity())
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.hen)
                .build();

        couponOverdueView.setHasFixedSize(true);
//        couponOverdueView.addItemDecoration(divider);

        couponOverdueView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //设置下拉刷新Progress的样式
        couponOverdueView.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        //设置下拉刷新箭头
        //giftView.setArrowImageView(R.drawable.load_1);
        couponOverdueView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);

        //下拉刷新
        couponOverdueView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //page = 1;
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                flag = 2;
                url = urlUtil + UrlUtil.coupon_expired + "?start=0" + "&rows=" + 5;
                rows = 1;
                requestData();

            }
        });
        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
        couponOverdueView.setLoadMoreEnabled(true);
        //加载更多
        couponOverdueView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                flag=1;
                rows++;
                url = urlUtil + UrlUtil.coupon_expired + "?start=" + (rows - 1) * 5 + "&rows=" + 5;
                requestData();
            }
        });

        couponOverdueView.setLScrollListener(new LRecyclerView.LScrollListener() {

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
        couponOverdueView.setHeaderViewColor(R.color.colorAccent, R.color.login_color, android.R.color.white);
        //设置底部加载颜色
        couponOverdueView.setFooterViewColor(R.color.login_color, R.color.login_color, android.R.color.white);
        //fecoList.setFooterViewColor(R.color.colorAccent, R.color.login_color ,android.R.color.white);
        //设置底部加载文字提示
        couponOverdueView.setFooterViewHint(getResources().getString(R.string.listview_loading), "到底啦~到底啦~", "网络不给力啊，点击再试一次吧");

        couponOverdueView.refresh();
        //条目的点击事件
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mDataAdapter.getDataList().size() > position) {
                  /*  int shopid = mDataAdapter.getDataList().get(position).getShopid();
                    String giftSn = mDataAdapter.getDataList().get(position).getGiftSn();
                    long expiredTm = mDataAdapter.getDataList().get(position).getExpiredTm();
                    int giftUseId = mDataAdapter.getDataList().get(position).getGiftUseId();
                    int type = mDataAdapter.getDataList().get(position).getType();
                    int userId = mDataAdapter.getDataList().get(position).getUserId();
                    int id = mDataAdapter.getDataList().get(position).getId();
                    Intent intent = new Intent(getActivity(), GiftItemActivity.class);
                    intent.putExtra("shopid", shopid + "");
                    intent.putExtra("giftSn", giftSn);
                    intent.putExtra("expiredTm", expiredTm + "");
                    intent.putExtra("giftUseId", giftUseId + "");
                    intent.putExtra("type", type + "");
                    intent.putExtra("userId", userId + "");
                    intent.putExtra("giftId", id + "");
                    startActivity(intent);*/
                }

            }

        });
        //条目的长点击事件
        mLRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });


        mDataAdapter.SetOnItemClickListener(new BeCouponAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int id = mDataAdapter.getDataList().get(position).getId();
                Http.OkHttpDelete(getActivity(), urlUtil + UrlUtil.coupon_expired+"/"+id, new Http.OnDataFinish() {
                    @Override
                    public void OnSuccess(String result) {
                        Log.d("shangchude",urlUtil + UrlUtil.gift_expired+"/"+id+"=="+result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("code").equals("0")){
                                if (list.size()>=position){
                                    list.remove(position);
                                    mDataAdapter.remove(position);
                                    mDataAdapter.notifyDataSetChanged();
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
        });
        noNetworkView.findViewById(R.id.tv_retry_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading(getResources().getString(R.string.listview_loading));
                requestData();
            }
        });
    }

    // 普通事件的处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventBusCarrier carrier) {
        String content = (String) carrier.getObject();


            Http.OkHttpDelete(getActivity(), urlUtil + UrlUtil.coupon_expired, new Http.OnDataFinish() {
                    @Override
                    public void OnSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("code").equals("0")){
                                    requestData();
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


    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }


    private void addItems(List<GiftListBean> list) {

        mDataAdapter.addAll(list);

        mCurrentCounter += list.size();

    }


    private class PreviewHandler extends Handler {

        private WeakReference<BeCouponsFragment> ref;

        PreviewHandler(BeCouponsFragment activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final BeCouponsFragment activity = ref.get();
            if (activity == null) {
                return;
            }

            switch (msg.what) {

                case -1:
                    token = (String) SPUtil.get(getActivity(), "Token", "");
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
                                couponOverdueView.setVisibility(View.VISIBLE);
                                if (flag == 2) {
                                    mDataAdapter.clear();
                                    list.clear();
                                }
                                if (rows == 1) {
                                    mDataAdapter.clear();
                                    list.clear();
                                }
                                Log.d("有效礼包记录", result + "==" + url);
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String code = jsonObject.getString("code");
                                    if (code.equals("0")) {
                                        list.clear();
                                        JSONArray data = jsonObject.getJSONArray("data");
                                        if (data.length() > 0) {
                                            try{
                                                couponOverdueLi.setVisibility(View.GONE);
                                            }catch (Exception e){}

                                            fac_num = 1;
                                            for (int i = 0; i < data.length(); i++) {
                                                GiftListBean bean = GSonUtil.parseGson(data.getString(i), GiftListBean.class);
                                                list.add(bean);
                                            }
                                            activity.addItems(list);
                                        } else {
                                            try {
                                                couponOverdueView.setNoMore(true);
                                            }catch (Exception e){

                                            }
                                            if (fac_num == 0) {
                                                Log.d("youyiyiyi", list.size() + "");
                                                try{couponOverdueView.setVisibility(View.GONE);
                                                    couponOverdueLi.setVisibility(View.VISIBLE);}catch (Exception e){}
                                            }
                                        }

                                    } else {
                                        //Toast.makeText(getActivity(),getResources().getString(R.string.jia),Toast.LENGTH_SHORT).show();
                                        ToastUtil.showgravity(getActivity(), getResources().getString(R.string.jia));
                                    }
                                    try {
                                        activity.couponOverdueView.refreshComplete();
                                    } catch (Exception e) {

                                    }


                                    flag = 1;

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
                                couponOverdueView.setVisibility(View.GONE);
                                couponOverdueLi.setVisibility(View.GONE);
                            }
                        });
                    }else{
                        if(isDetached()){
                            return;
                        }
                        dissLoad();
                    }
                    break;
                case -3:
                    //网络请求失败的情景
                    if(isDetached()){
                        return;
                    }
                    dissLoad();
                    noNetworkView.setVisibility(View.VISIBLE);
                    couponOverdueView.setVisibility(View.GONE);
                    couponOverdueLi.setVisibility(View.GONE);
                    activity.couponOverdueView.refreshComplete();
                    activity.notifyDataSetChanged();
                    activity.couponOverdueView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
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
    private Thread mLoadDataTh;
    private void requestData() {
        // Log.d(TAG, "requestData");
        mLoadDataTh=new Thread() {

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
        };
        mLoadDataTh.start();
    }

    @Override
    public void onStartInit() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mLoadDataTh!=null&&!mLoadDataTh.isInterrupted()){
            mLoadDataTh.interrupt();
        }
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this); //解除注册
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isCreated){
            return;
        }

        if (isVisibleToUser){

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        flag = 2;
        url = urlUtil + UrlUtil.coupon_expired + "?start=0" + "&rows=" + 5;
        rows = 1;
        requestData();
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible){
            flag = 2;
            url = urlUtil + UrlUtil.coupon_expired + "?start=0" + "&rows=" + 5;
            rows = 1;
            loading(getResources().getString(R.string.listview_loading));
            requestData();
        }
    }
}
