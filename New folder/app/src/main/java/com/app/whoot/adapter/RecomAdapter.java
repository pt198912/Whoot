package com.app.whoot.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.CardItemBean;
import com.app.whoot.bean.CommBean;
import com.app.whoot.bean.ReddBean;
import com.app.whoot.ui.activity.StoreDetailActivity;
import com.app.whoot.ui.view.custom.MultiImageView;
import com.app.whoot.ui.view.custom.MyGridView;
import com.app.whoot.ui.view.custom.ScaleImageView;
import com.app.whoot.ui.view.demo.ImageViewer;
import com.app.whoot.ui.view.demo.ViewData;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.SysUtils;
import com.app.whoot.util.TimeUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gms.fido.fido2.api.common.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunrise on 5/16/2018.
 */

public class RecomAdapter extends ListBaseAdapter<ReddBean> {
    private Context mContext;
    private List<ReddBean> list;
    private String cur_os;
    private int wh;
    private CommentAdapter.MyClickListener mListener;
    private int posi = 0;
    // 采用接口回调的方式实现RecyclerView的ItemClick
    public CommentAdapter.OnRecyclerViewListener mOnRecyclerViewListener;

    // 接口回调第一步: 定义接口和接口中的方法
    public interface OnRecyclerViewListener {

        void onItemClick(TextView ation_item_more, int position);

        boolean onItemLongClick(int position);
    }

    // 接口回调第二步: 初始化接口的引用
    public void setOnRecyclerViewListener(CommentAdapter.OnRecyclerViewListener l) {
        this.mOnRecyclerViewListener = l;
    }

    public RecomAdapter(Context context) {

        super(context);
        this.mContext = context;
        this.wh = (SysUtils.getScreenWidth((Activity) mContext) - SysUtils.Dp2Px(mContext, 99)) / 3;

    }

    @Override
    public int getLayoutId() {
        return R.layout.recom_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

        ReddBean item = mDataList.get(position);

        ImageView recom_img = holder.getView(R.id.recom_img);
        TextView recom_txtx = holder.getView(R.id.recom_txtx);
        TextView recom_tx = holder.getView(R.id.recom_tx);
        TextView recom_txex = holder.getView(R.id.recom_txex);
        LinearLayout recom_ly = holder.getView(R.id.recom_ly);
        String currency = (String) SPUtil.get(mContext, "currency", "");

        if (currency.equals("1")){
            cur_os="HKD";
        }else if (currency.equals("2")){
            cur_os="RMB";
        }else if (currency.equals("3")){
            cur_os="USD";
        }else if (currency.equals("4")){
            cur_os="EUR";
        }
        String commodityPic = item.getImgUrl();
        Glide.with(mContext)
                .load(commodityPic)
                .centerCrop()
                .skipMemoryCache(true)
                .error(R.drawable.storedetail_top5_blank_pic)
                .into(recom_img);
        String name = item.getName();
        recom_txtx.setText(name);
        recom_ly.setVisibility(View.VISIBLE);

        try {
            double price = item.getPrice();

            if (price == 0) {
                recom_ly.setVisibility(View.INVISIBLE);
            } else {
                recom_txex.setText(" "+cur_os+" "+price);
            }
        } catch (Exception e) {
            recom_ly.setVisibility(View.INVISIBLE);
        }

        String describe = item.getDescribe();
        recom_tx.setText(describe);


    }

    //自定义接口，用于回调按钮点击事件到Activity
    public interface MyClickListener {

        public void clickListener(View view, int position);

    }

}
