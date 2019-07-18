package com.app.whoot.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.whoot.R;

import com.app.whoot.adapter.GiftsAdapter;
import com.app.whoot.base.fragment.BaseFragment;
import com.app.whoot.bean.GiftsRecordBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.CoponsActivity;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.util.GSonUtil;
import com.app.whoot.util.NetworkUtils;
import com.app.whoot.util.SPUtil;
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
import butterknife.Unbinder;

/**
 * Created by Sunrise on 7/31/2018.
 */

public class GiftsFragment extends BaseFragment {

    @BindView(R.id.exchang_ly)
    LinearLayout exchang_ly;
    @BindView(R.id.gift_lrecy)
    LRecyclerView giftLrecy;

    private PreviewHandler mHandler = new PreviewHandler(this);
    private GiftsAdapter mDataAdapter = null;
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

    private int ji=0;

    private String urlUtil;

    private List<GiftsRecordBean> list=new ArrayList<GiftsRecordBean>();

    public static Fragment newInstance() {
        GiftsFragment fragment = new GiftsFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.gifts_frag;
    }

    @Override
    public void onViewCreatedInit() {

        urlUtil = (String) SPUtil.get(getActivity(), "URL", "");
        mDataAdapter = new GiftsAdapter(getActivity());
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        giftLrecy.setAdapter(mLRecyclerViewAdapter);
        //添加分割线
        DividerDecoration divider = new DividerDecoration.Builder(getActivity())
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.gray)
                .build();

        //mRecyclerView.setHasFixedSize(true);
        //giftLrecy.addItemDecoration(divider);

        giftLrecy.setLayoutManager(new LinearLayoutManager(getActivity()));


        //设置下拉刷新Progress的样式
        giftLrecy.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        //设置下拉刷新箭头
        //giftLrecy.setArrowImageView(R.drawable.load_1);
        giftLrecy.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);


        //下拉刷新
        giftLrecy.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //page = 1;
                flag = 2;
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                url= urlUtil + UrlUtil.historygift+ "?start=0"  + "&rows=" + 10;
                rows=1;
                requestData();

            }
        });
        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
        giftLrecy.setLoadMoreEnabled(true);
        //加载更多
        giftLrecy.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                rows++;
                url = urlUtil + UrlUtil.historygift + "?start=" + (rows - 1) * 10 + "&rows=" + 10;
                requestData();
            }
        });

        giftLrecy.setLScrollListener(new LRecyclerView.LScrollListener() {

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
        giftLrecy.setHeaderViewColor(R.color.colorAccent, R.color.login_color, android.R.color.white);
        //设置底部加载颜色
        giftLrecy.setFooterViewColor(R.color.login_color, R.color.login_color, android.R.color.white);
        //fecoList.setFooterViewColor(R.color.colorAccent, R.color.login_color ,android.R.color.white);
        //设置底部加载文字提示
        giftLrecy.setFooterViewHint(getResources().getString(R.string.listview_loading), "到底啦~到底啦~", "网络不给力啊，点击再试一次吧");

        giftLrecy.refresh();
        //条目的点击事件
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mDataAdapter.getDataList().size() > position) {

                    Intent intent = new Intent(getActivity(), CoponsActivity.class);
                    intent.putExtra("difference","2");
                  //  startActivity(intent);
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
    public void onStartInit() {

    }

    @Override
    public void onResume() {
        super.onResume();



           if (ji==1){

            flag = 2;
            mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
            mCurrentCounter = 0;
            url=urlUtil+ UrlUtil.historygift+ "?start=0"  + "&rows=" + 10;
            rows=1;
            requestData();
           }






    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }


    private void addItems(List<GiftsRecordBean> list) {

        mDataAdapter.addAll(list);

        mCurrentCounter += list.size();

    }

    private class PreviewHandler extends Handler {

        private WeakReference<GiftsFragment> ref;

        PreviewHandler(GiftsFragment activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final GiftsFragment activity = ref.get();
            if (activity == null) {
                return;
            }

            boolean detached = isDetached();

            if (detached){
                return;
            }

            switch (msg.what) {

                case -1:
                    int currentSize = activity.mDataAdapter.getItemCount();

                    Http.OkHttpGet(getActivity(), url, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {

                            if (flag == 2) {
                                mDataAdapter.clear();
                                list.clear();
                            }

                            Log.d("礼包兑换",result);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String code = jsonObject.getString("code");
                                if (code.equals("0")){
                                    list.clear();
                                    JSONArray data = jsonObject.getJSONArray("data");
                                    if (data.length()>0){
                                        biao=1;
                                        for (int i = 0; i <data.length() ; i++) {
                                            GiftsRecordBean bean = GSonUtil.parseGson(data.getString(i), GiftsRecordBean.class);
                                            list.add(bean);
                                        }
                                        activity.addItems(list);
                                    }else {
                                        try{
                                            giftLrecy.setNoMore(true);
                                        }catch (Exception e){
                                            giftLrecy.setNoMore(true);
                                        }

                                        if (biao==0){
                                            exchang_ly.setVisibility(View.VISIBLE);
                                        }

                                    }

                                }
                                try{
                                    activity.giftLrecy.refreshComplete();
                                }catch (Exception e){

                                }


                                flag = 1;
                                ji=1;
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
                    activity.giftLrecy.refreshComplete();
                    activity.notifyDataSetChanged();
                    activity.giftLrecy.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
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
                if (NetworkUtils.isNetAvailable(getActivity())) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }


}
