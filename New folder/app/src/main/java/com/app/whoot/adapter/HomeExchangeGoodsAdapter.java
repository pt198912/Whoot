package com.app.whoot.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.CardItemTopBean;
import com.app.whoot.ui.activity.CoupDetaiActivity;
import com.app.whoot.ui.view.custom.RoundImageView;
import com.app.whoot.util.Constants;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ScreenUtils;
import com.app.whoot.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeExchangeGoodsAdapter extends RecyclerView.Adapter<HomeExchangeGoodsAdapter.ViewHolder> {

    private List<CardItemTopBean> mDatas = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;

    public HomeExchangeGoodsAdapter(Context context, List<CardItemTopBean> datas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        setData(datas);
    }

    public void setData(List<CardItemTopBean> datas) {
        this.mDatas.clear();
        if (datas != null) {
            this.mDatas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_home_exchange_goods, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardItemTopBean good=mDatas.get(position);
        Glide.with(mContext)
                .load(good.getCommodityPic())
                .asBitmap()
                .thumbnail(0.3f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) //缓存
                .error(R.drawable.home_blank_pic)
                .into(holder.ivGoods);
        initGoodsType(good,holder);
        holder.tvGoodsName.setText(good.getCommodityName());
        if(good.getCommodityActivityTag()!=0){
            holder.activityTagIv.setVisibility(View.VISIBLE);
            if(Util.isChineseLanguage(mContext)){
                holder.activityTagIv.setImageResource(R.drawable.home_tag_activity_cn);
            }else{
                holder.activityTagIv.setImageResource(R.drawable.home_tag_activity_en);
            }
        }else{
            holder.activityTagIv.setVisibility(View.GONE);
        }
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoGoodsDetail(good);
            }
        });
    }

    private void gotoGoodsDetail(CardItemTopBean bean){
        String commodityPic = bean.getCommodityPic();  //产品图片
        String commodityName = bean.getCommodityName(); //产品名字
        String detail = bean.getDetail();            //介绍
        int couponId = bean.getCouponId();   //优惠券类型
        int maxUseAmout = bean.getMaxUseAmout(); //最大兑换
        int usedAmout = bean.getUsedAmout();    //已兑换
        int costs = bean.getCosts(); //数量
        int shopId = bean.getShopId(); //门店id
        int commodityId = bean.getCommodityId(); //商品id
        String commodityDescribe = bean.getCommodityDescribe(); //商品描述
        SPUtil.put(mContext, "shopId_item", String.valueOf(shopId));
        SPUtil.put(mContext, "commodityId_item", String.valueOf(commodityId));
        SPUtil.put(mContext, "costs_item", String.valueOf(costs));
        SPUtil.put(mContext, "commodityPic_item", commodityPic);
        SPUtil.put(mContext, "commodityName_item", commodityName);
        SPUtil.put(mContext, "detail_item", detail);
        SPUtil.put(mContext, "couponId_item", String.valueOf(couponId)); //优惠卷类型
        SPUtil.put(mContext, "maxUseAmout_item", String.valueOf(maxUseAmout));
        SPUtil.put(mContext, "usedAmout_item", String.valueOf(usedAmout));
        SPUtil.put(mContext, "commodityDescribe", commodityDescribe); //商品描述
        SPUtil.put(mContext, Constants.SP_KEY_COUP_DETAIL_FROM, Constants.GO_COUP_DETAIL_FROM_DISCOVERY);
        Intent coupDetail = new Intent(mContext, CoupDetaiActivity.class);
//        coupDetail.putStringArrayListExtra("image",null);
        mContext.startActivity(coupDetail);
    }

    private void initGoodsType(CardItemTopBean good,ViewHolder holder){
        if(good==null){
            return;
        }
        switch (good.getCouponId()){
            case 1:
                holder.ivTokenIcon.setImageResource(R.drawable.token_icon_bronze);
                break;
            case 2:
                holder.ivTokenIcon.setImageResource(R.drawable.token_icon_silver);
                break;
            case 3:
                holder.ivTokenIcon.setImageResource(R.drawable.token_icon_gold);
                break;
            case 4:
                holder.ivTokenIcon.setImageResource(R.drawable.token_icon_diamond);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View content;
        @BindView(R.id.iv_goods)
        RoundedImageView ivGoods;
        @BindView(R.id.iv_token_icon)
        ImageView ivTokenIcon;
        @BindView(R.id.tv_goods_name)
        TextView tvGoodsName;
        @BindView(R.id.iv_activity_tag)
        ImageView activityTagIv;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            content=itemView;
        }
    }
}
