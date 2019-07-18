package com.app.whoot.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.CardBean;
import com.app.whoot.bean.CardItemTopBean;
import com.app.whoot.bean.DishReviewBean;
import com.app.whoot.interfacevoke.OnActivityReenterListener;
import com.app.whoot.ui.activity.CardItemActivity;
import com.app.whoot.ui.activity.CoupDetaiActivity;
import com.app.whoot.ui.activity.RecomActivity;
import com.app.whoot.ui.activity.StoreDetailActivity;
import com.app.whoot.ui.activity.StoreViewActivity;
import com.app.whoot.ui.activity.WriteActivity;
import com.app.whoot.ui.view.RecycleViewDivider;
import com.app.whoot.ui.view.custom.MyGridView;
import com.app.whoot.ui.view.custom.MyListView;
import com.app.whoot.util.Constants;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ScreenUtils;
import com.app.whoot.util.ToastUtil;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.jchou.imagereview.ui.ImagePagerActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DishReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnActivityReenterListener{



    private Activity mContext;
    private DishReviewBean mData;
    public static final int ITEM_TYPE_REWARD = 1;
    public static final int ITEM_TYPE_RECOMMEND = 2;
    public static final int ITEM_TYPE_REVIEW = 3;
    public static StoreDetailReviewAdapter mCurrentShopLvAdapter;
    private static final String TAG = "DishReviewAdapter";

    public DishReviewAdapter(Activity context, DishReviewBean bean) {
        this.mContext = context;
        this.mData = bean;
        setOnActivityReenterListener(this);
    }

    public void setData(DishReviewBean bean){
        this.mData=bean;
        notifyDataSetChanged();
    }
    // 接口回调第一步: 定义接口和接口中的方法
    public interface OnRecyclerViewListener {

        void onItemClick(TextView ation_item_more, int position);

        boolean onItemLongClick(int position);
    }

    private OnActivityReenterListener onActivityReenterListener;

    public OnActivityReenterListener getOnActivityReenterListener() {
        return onActivityReenterListener;
    }

    public void setOnActivityReenterListener(OnActivityReenterListener onActivityReenterListener) {
        this.onActivityReenterListener = onActivityReenterListener;

    }

    @Override
    public void onActivityReenterCallback(int resultCode, Intent data) {
        if(mCurrentShopLvAdapter!=null){
            mCurrentShopLvAdapter.onActivityReenterCallback(resultCode,data);
        }
    }
    public void releaseRes(){
        mCurrentShopLvAdapter=null;
        mContext=null;
    }
    public boolean isPreviewingImage(){
        if(mCurrentShopLvAdapter!=null){
            return mCurrentShopLvAdapter.isPreviewingImage();
        }
        return false;
    }
    public void setPreviewingImage(boolean flag){
        if(mCurrentShopLvAdapter!=null){
            mCurrentShopLvAdapter.setPreviewingImage(flag);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = null;
        switch (viewType) {
            case ITEM_TYPE_REWARD:
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_store_detail_reward, null);
                holder = new RewardHolder(view);
                break;
            case ITEM_TYPE_RECOMMEND:
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_store_detail_recommend, null);
                holder = new RecommendHolder(view);
                break;
            case ITEM_TYPE_REVIEW:
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_store_detail_review, null);
                holder = new ReviewHolder(view);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_TYPE_REWARD:
                RewardHolder rewardHolder = (RewardHolder) holder;
                updateRewardViews(rewardHolder);
                break;
            case ITEM_TYPE_RECOMMEND:
                RecommendHolder recommendHolder = (RecommendHolder) holder;
                updateRecommendViews(recommendHolder);
                break;
            case ITEM_TYPE_REVIEW:
                ReviewHolder reviewHolder = (ReviewHolder) holder;
                updateReviewViews(reviewHolder);
                break;
        }
    }
    private void updateRewardViews(RewardHolder rewardHolder){
        if(mData.getRecommendList().size()==0){
            LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            rewardHolder.rewardLayout.setLayoutParams(lp);
            rewardHolder.recyclerView.setVisibility(View.GONE);
            rewardHolder.noRewardItemView.setVisibility(View.VISIBLE);
        }else{
            LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            rewardHolder.rewardLayout.setLayoutParams(lp);
            rewardHolder.recyclerView.setVisibility(View.VISIBLE);
            rewardHolder.noRewardItemView.setVisibility(View.GONE);
            rewardHolder.recyclerView.setCanScrollVertical(false);
            rewardHolder.recyclerView.setAdapter(new StoreRewardAdapter(mContext,mData.getRecommendList(),mData));
            rewardHolder.recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    CardItemTopBean bean=mData.getRecommendList().get(position);
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
                    SPUtil.put(mContext, Constants.SP_KEY_COUP_DETAIL_FROM, Constants.GO_COUP_DETAIL_FROM_STORE_DETAIL);
                    Intent coupDetail = new Intent(mContext, CoupDetaiActivity.class);
                    coupDetail.putStringArrayListExtra("image",(ArrayList<String>) mData.getStoreImages());
                    mContext.startActivity(coupDetail);
                }
            });
        }

    }


    private void updateRecommendViews(RecommendHolder recommendHolder){
        if(mData.getRewardItemBeanList().size()==0){
            LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
            recommendHolder.recommendLayout.setLayoutParams(lp);
        }else{
            LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            recommendHolder.recommendLayout.setLayoutParams(lp);
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recommendHolder.rvStoreDetailRecommend.setLayoutManager(manager);
            StoreRecommendAdapter storeRecommendAdapter = new StoreRecommendAdapter(mData.getRewardItemBeanList(),mContext);
            recommendHolder.rvStoreDetailRecommend.setAdapter(storeRecommendAdapter);
            recommendHolder.tvRecommendMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, RecomActivity.class);
                    intent.putExtra("Top_id", mData.getShopId());
                    mContext.startActivity(intent);
                }
            });
        }

    }

    public void updateReviewViews(ReviewHolder reviewHolder) {
        int count = mData.getReviewList().size();
        Log.d(TAG, "updateReviewViews: getReviewList "+count);
        String token = (String) SPUtil.get(mContext, "Token", "");
        if (count == 0) {
            reviewHolder.cardNoRed.setVisibility(View.VISIBLE);
            reviewHolder.seeMoreCommentTv.setVisibility(View.GONE);
            reviewHolder.rvStoreDetailReview.setVisibility(View.GONE);
            if(mData.isUncommented()){
//                reviewHolder.tvWriteComment.setVisibility(View.VISIBLE);
//                reviewHolder.tipView.setVisibility(View.VISIBLE);
                reviewHolder.tipLayout.setVisibility(View.VISIBLE);
            }else{
//                reviewHolder.tvWriteComment.setVisibility(View.GONE);
//                reviewHolder.tipView.setVisibility(View.GONE);
                reviewHolder.tipLayout.setVisibility(View.GONE);
            }
        } else {
            LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            reviewHolder.rewardLayout.setLayoutParams(lp);
            reviewHolder.seeMoreCommentTv.setVisibility(View.VISIBLE);
            reviewHolder.seeMoreCommentTv.setText(String.format(mContext.getResources().getString(R.string.label_see_more_comment),mData.getPingfen()));
            reviewHolder.seeMoreCommentTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mData.getReviewList().size()<=0){
                        return;
                    }
                    String shopName=null;
                    try{
                        shopName = mData.getReviewList().get(0).getShopName();
                    }catch (Exception e){
                        shopName=null;
                    }

                    if (shopName==null) {
                        //Toast.makeText(CardItemActivity.this, getResources().getString(R.string.zan), Toast.LENGTH_SHORT).show();
                        ToastUtil.showgravity(mContext, mContext.getResources().getString(R.string.zan));
                    } else {
                        //评论
                        Intent intent1 = new Intent(mContext, StoreViewActivity.class);
                        intent1.putExtra("grade", mData.getGrade() + "");
                        intent1.putExtra("shopId", mData.getShopId() + "");
                        intent1.putExtra("substring", mData.getSubstring());
                        intent1.putExtra("pingfen", mData.getPingfen());
                        mContext.startActivity(intent1);
                    }
                }
            });
            if(mData.isUncommented()){
//                reviewHolder.tvWriteComment.setVisibility(View.VISIBLE);
//                reviewHolder.tipView.setVisibility(View.VISIBLE);
                reviewHolder.tipLayout.setVisibility(View.VISIBLE);
            }else{
//                reviewHolder.tvWriteComment.setVisibility(View.GONE);
//                reviewHolder.tipView.setVisibility(View.GONE);
                reviewHolder.tipLayout.setVisibility(View.GONE);
            }

            reviewHolder.cardNoRed.setVisibility(View.GONE);
            //添加分割线
//            DividerDecoration divider = new DividerDecoration.Builder(mContext)
//                    .setHeight(R.dimen.default_divider_height)
//                    .setPadding(R.dimen.default_divider_padding)
//                    .setColorResource(R.color.filter_pop)
//                    .build();
            //mRecyclerView.setHasFixedSize(true);
            reviewHolder.rvStoreDetailReview.addItemDecoration(new RecycleViewDivider(
                    mContext, LinearLayoutManager.VERTICAL, ScreenUtils.dp2px(mContext,0.5f), mContext.getResources().getColor(R.color.filter_pop)));
            reviewHolder.rvStoreDetailReview.setLayoutManager(new LinearLayoutManager(mContext));
            reviewHolder.rvStoreDetailReview.setVisibility(View.VISIBLE);
            StoreDetailReviewAdapter shopsLVAdapter = new StoreDetailReviewAdapter(mContext, mData.getReviewList());
            try {
                reviewHolder.rvStoreDetailReview.setAdapter(shopsLVAdapter);

            } catch (Exception e) {
            }
            shopsLVAdapter.notifyDataSetChanged();
        }
        if(token.length()==0){
//            reviewHolder.tvWriteComment.setVisibility(View.GONE);
//            reviewHolder.tipView.setVisibility(View.GONE);
            reviewHolder.tipLayout.setVisibility(View.GONE);
        }
        reviewHolder.tvWriteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                CardBean.CouponHistoryBean couponHistory = mData.getCouponHistoryBean();
                if(couponHistory==null){
                    ToastUtil.showgravity(mContext,mContext.getString(R.string.load_fail));
                    return;
                }
                Intent intent1 = new Intent(mContext, WriteActivity.class);
                intent1.putExtra("shopid", String.valueOf(couponHistory.getShopIdX()));
                intent1.putExtra("commodityId", String.valueOf(couponHistory.getCommodityIdX()));
                intent1.putExtra("shopName", couponHistory.getShopNameX());
                intent1.putExtra("commodityName", couponHistory.getCommodityNameX());
                intent1.putExtra("couponHistoryId", String.valueOf(couponHistory.getIdX()));
                intent1.putExtra("couponId", String.valueOf(couponHistory.getCouponLevel()));
                intent1.putExtra("useCount", String.valueOf(couponHistory.getUseCount()));
                mContext.startActivity(intent1);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position + 1;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class RewardHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        MyGridView recyclerView;
        View rewardLayout;
        View noRewardItemView;
        public RewardHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.tv_store_detail_reward);
            recyclerView = itemView.findViewById(R.id.rv_store_detail_reward);
            rewardLayout= itemView.findViewById(R.id.ll_store_detail_reward);
            noRewardItemView= itemView.findViewById(R.id.tv_no_reward_item);
        }
    }

    class RecommendHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_recommend_more)
        TextView tvRecommendMore;
        @BindView(R.id.ll_store_detail_recommend)
        View recommendLayout;
        @BindView(R.id.tv_store_detail_recommend)
        TextView tvStoreDetailRecommend;
        @BindView(R.id.rv_store_detail_recommend)
        RecyclerView rvStoreDetailRecommend;

        public RecommendHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ReviewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_review)
        View rewardLayout;
        @BindView(R.id.tv_see_more_comment)
        TextView seeMoreCommentTv;
        @BindView(R.id.tv_write_comment)
        TextView tvWriteComment;
        @BindView(R.id.tv_store_detail_review)
        TextView tvStoreDetailReview;
        @BindView(R.id.rv_store_detail_review)
        RecyclerView rvStoreDetailReview;
        @BindView(R.id.card_no_red)
        TextView cardNoRed;
        @BindView(R.id.tv_store_detail_review_tip)
        View tipView;
        @BindView(R.id.ll_review_tip)
        View tipLayout;
        public ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
