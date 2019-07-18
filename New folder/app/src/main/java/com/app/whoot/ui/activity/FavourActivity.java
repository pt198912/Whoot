package com.app.whoot.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.adapter.BuxAdapter;
import com.app.whoot.adapter.FavourAdapter;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.FavourBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.util.Constants;
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
 * Created by Sunrise on 1/8/2019.
 * 我的收藏
 */

public class FavourActivity extends BaseActivity {
    @BindView(R.id.title_back_fl)
    LinearLayout titleBackFl;
    @BindView(R.id.title_back_title)
    TextView titleBackTitle;
    @BindView(R.id.favour_ly)
    LinearLayout favourLy;
    @BindView(R.id.favourview)
    LRecyclerView favourview;
    @BindView(R.id.title_del_img)
    ImageView title_del_img;
    @BindView(R.id.no_network_view)
    View noNetworkView;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private FavourAdapter mDataAdapter = null;
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

    private List<FavourBean> list=new ArrayList<FavourBean>();

    private int refresh;
    private int loadMore;
    private String urlUtil;
    private JSONArray data;
    private String lastidu;
    private String longtidu;

    private static final int MYLIVE_MODE_CHECK = 0;
    private static final int MYLIVE_MODE_EDIT = 1;
    private int mEditMode = MYLIVE_MODE_CHECK;
    private boolean editorStatus = false;
    private int isedit=0;
    private int a=0;
    private int b=0;
    private int id;

    @Override
    public int getLayoutId() {
        return R.layout.favour_activi;
    }

    @Override
    public void onCreateInit() {
        titleBackTitle.setText(getResources().getString(R.string.favour));

        urlUtil = (String) SPUtil.get(FavourActivity.this, "URL", "");
        mDataAdapter = new FavourAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        favourview.setAdapter(mLRecyclerViewAdapter);
        //添加分割线
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.filter_pop)
                .build();

        //mRecyclerView.setHasFixedSize(true);
        favourview.addItemDecoration(divider);

        favourview.setLayoutManager(new LinearLayoutManager(this));


        //设置下拉刷新Progress的样式
        favourview.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        //设置下拉刷新箭头
        //refreshVie.setArrowImageView(R.drawable.load_1);
        favourview.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);

        lastidu = (String) SPUtil.get(this, "lastidu", "");
        longtidu = (String) SPUtil.get(this, "longtidu", "");
        //下拉刷新
        favourview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                b=0;
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                flag = 2;
                url=urlUtil+ UrlUtil.list_favshop+ "?start=0"  + "&rows=" + 10+"&locationId="+1+"&latitude="+ lastidu +"&longitude="+ longtidu;
                rows=1;
                requestData();
                title_del_img.setVisibility(View.INVISIBLE);
            }
        });
        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
        favourview.setLoadMoreEnabled(true);
        //加载更多
        favourview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                rows++;
                url = urlUtil+ UrlUtil.list_favshop + "?start=" + (rows - 1) * 10 + "&rows=" + 10+"&locationId="+1+"&latitude="+ lastidu +"&longitude="+ longtidu;
                requestData();
            }
        });

        favourview.setLScrollListener(new LRecyclerView.LScrollListener() {

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
        favourview.setHeaderViewColor(R.color.colorAccent, R.color.login_color, android.R.color.white);
        //设置底部加载颜色
        favourview.setFooterViewColor(R.color.login_color, R.color.login_color, android.R.color.white);
        //fecoList.setFooterViewColor(R.color.colorAccent, R.color.login_color ,android.R.color.white);
        //设置底部加载文字提示
        favourview.setFooterViewHint(getResources().getString(R.string.listview_loading), "到底啦~到底啦~", "网络不给力啊，点击再试一次吧");
        noNetworkView.findViewById(R.id.tv_retry_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading(getResources().getString(R.string.listview_loading));
                requestData();
            }
        });
        loading(getResources().getString(R.string.listview_loading));
        favourview.refresh();

        //条目的点击事件
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                    if (b==1){
                        if (isedit==position){
                            mDataAdapter.setSelectItem(isedit);
                            b=0;
                            title_del_img.setVisibility(View.INVISIBLE);
                        }

                    }else {
                        if (mDataAdapter.getDataList().size() > position) {
                            SPUtil.put(FavourActivity.this, Constants.SP_KEY_STORE_DETAIL_FROM,Constants.GO_STORE_DETAIL_FROM_FAVOURITE);
                            Intent intent = new Intent(FavourActivity.this, StoreDetailActivity.class);
                            int shopId = mDataAdapter.getDataList().get(position).getShopId();
                            double distance = mDataAdapter.getDataList().get(position).getFavShopInfo().getDistance();
                            distance=distance/1000;
                            intent.putExtra("id",shopId);
                            intent.putExtra("comment",1);
                            intent.putExtra("favour",1);
                            intent.putExtra("distances",String.valueOf(distance));
                            startActivity(intent);
                        }
                    }
            }
        });
        //条目的长点击事件
        mLRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                if (b==0){

                    mDataAdapter.setSelectItem(position);
                    title_del_img.setVisibility(View.VISIBLE);
                    isedit=position;
                    b=1;
                    id = mDataAdapter.getDataList().get(position).getId();
                    Log.d("mLRecyclerViewAdapter",position+"=="+isedit);
                }
            }
        });

        mDataAdapter.SetOnItemClickListener(new FavourAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {


            }
        });
    }
    private void fsdfds(){
        List<FavourBean> dataList = mDataAdapter.getDataList();
        for (int i = 0; i <dataList.size() ; i++) {
            if (dataList.get(i).isSelect()){
                ToastUtil.showgravity(FavourActivity.this,"你选中了"+i);
            }
        }
    }


    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }
    private void addItems(List<FavourBean> list) {

        mDataAdapter.addAll(list);
        mCurrentCounter += list.size();
    }

    private class PreviewHandler extends Handler {

        private WeakReference<FavourActivity> ref;

        PreviewHandler(FavourActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final FavourActivity activity = ref.get();
            if (activity == null) {
                return;
            }
            switch (msg.what) {

                case -1:
                    int currentSize = activity.mDataAdapter.getItemCount();
                    Http.OkHttpGet(FavourActivity.this, url, new Http.OnDataFinish() {
                        @Override
                        public void OnSuccess(String result) {
                            dissLoad();
                            noNetworkView.setVisibility(View.GONE);
                            favourview.setVisibility(View.VISIBLE);
                            favourLy.setVisibility(View.GONE);
                            if (flag == 2) {
                                mDataAdapter.clear();
                                list.clear();
                            }
                            Log.d("兑换记录2222",result+url);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String code = jsonObject.getString("code");
                                if (code.equals("0")){
                                    list.clear();
                                    try{
                                        data = jsonObject.getJSONArray("data");
                                        if (data.length()>0){
                                            biao=1;
                                            for (int i = 0; i <data.length() ; i++) {
                                                String string = data.getString(i);
                                                FavourBean bean = GSonUtil.parseGson(data.getString(i), FavourBean.class);
                                                bean.setSelect(false);
                                                list.add(bean);
                                            }
                                            activity.addItems(list);
                                        }else {
                                            favourview.setNoMore(true);
                                            if (biao==0){
                                                favourview.setVisibility(View.GONE);
                                                favourLy.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }catch (Exception e){
                                        favourview.setNoMore(true);
                                        if (biao==0){
                                            favourLy.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                                try{
                                    activity.favourview.refreshComplete();
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
                            favourview.setVisibility(View.GONE);
                            favourLy.setVisibility(View.GONE);
                        }
                    });

                    break;
                case -3:
                    //摸你网络请求失败的情景
                    dissLoad();
                    noNetworkView.setVisibility(View.VISIBLE);
                    favourview.setVisibility(View.GONE);
                    favourLy.setVisibility(View.GONE);
                    activity.favourview.refreshComplete();
                    activity.notifyDataSetChanged();
                    activity.favourview.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
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
                if (NetworkUtils.isNetAvailable(FavourActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int favour = (int) SPUtil.get(FavourActivity.this, "favour", 0);
        if (favour==1){
            flag = 2;
            url=urlUtil+ UrlUtil.list_favshop+ "?start=0"+ "&rows=" + 6+"&locationId="+1+"&latitude="+lastidu+"&longitude="+longtidu;
            rows=1;
            requestData();
            SPUtil.put(FavourActivity.this,"favour",0);
        }
    }

    @OnClick({R.id.title_back_fl, R.id.title_back_title,R.id.title_del_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_fl:
                finish();
                break;
            case R.id.title_back_title:
                break;
            case R.id.title_del_img:
                Http.OkHttpDelete(FavourActivity.this, urlUtil + UrlUtil.list_favshop + "?id=" + id, new Http.OnDataFinish() {
                    @Override
                    public void OnSuccess(String result) {
                        Log.d("shangchude",result+urlUtil + UrlUtil.list_favshop + "?id=" + id);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("code").equals("0")){

                                title_del_img.setVisibility(View.INVISIBLE);
                                b=0;
                                mDataAdapter.getDataList().remove(isedit);
                                //mDataAdapter.setSelectItem(isedit);
                                mDataAdapter.notifyItemRemoved(isedit);
                                mDataAdapter.notifyDataSetChanged();
                                //mDataAdapter.notifyItemRangeChanged(isedit, mDataAdapter.getDataList().size());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                break;
        }
    }
}
