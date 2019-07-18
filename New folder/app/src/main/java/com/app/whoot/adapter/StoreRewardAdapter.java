package com.app.whoot.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.CardItemTopBean;
import com.app.whoot.bean.DishReviewBean;
import com.app.whoot.ui.activity.CoupDetaiActivity;
import com.app.whoot.ui.activity.LoginMeActivity;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.view.dialog.NoExchangedTokenDialog;
import com.app.whoot.ui.view.dialog.OfferDialog;
import com.app.whoot.util.Constants;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;


class StoreRewardAdapter extends BaseAdapter {
    private Activity mContext;
    private List<CardItemTopBean> mRewardlist;
    Handler mHandler;
    OfferDialog browsDialog;
    DishReviewBean data;
    public StoreRewardAdapter(Activity context, List<CardItemTopBean> rewardlist, DishReviewBean data) {
        this.mContext = context;
        this.mRewardlist = rewardlist;
        this.data=data;
        mHandler=new Handler();
    }

    @Override
    public int getCount() {
        // 条目的总数 文字组数 == 图片张数
        return mRewardlist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.offer_img:  //确认

                    browsDialog.dismiss();

                    break;

            }
        }
    };
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(mContext, R.layout.gridview_item, null);
        RoundedImageView gridview_img_one = view.findViewById(R.id.gridview_img_one);
        TextView gridview_txt = view.findViewById(R.id.gridview_txt);
        ImageView gridview_img = view.findViewById(R.id.gridview_img);
        TextView gridview_txtx = view.findViewById(R.id.gridview_txtx);
        TextView gridview_tx = view.findViewById(R.id.gridview_tx);
        TextView exchangeNumTv=view.findViewById(R.id.tv_exchange_num);
        TextView exchangeTv=view.findViewById(R.id.tv_exchange);
        ImageView actTagIv=view.findViewById(R.id.iv_activity_tag);
        String commodityName = mRewardlist.get(position).getCommodityName();
        gridview_txt.setText(commodityName);
        String commodityPic = mRewardlist.get(position).getCommodityPic();
        gridview_img_one.setVisibility(View.VISIBLE);
        CardItemTopBean bean=mRewardlist.get(position);
        if(bean.getCommodityActivityTag()!=0) {
            actTagIv.setVisibility(View.VISIBLE);
            if (Util.isChineseLanguage(mContext)){
                actTagIv.setBackgroundResource(R.drawable.home_tag_activity_cn);
            }else {
                actTagIv.setBackgroundResource(R.drawable.home_tag_activity_en);
            }
        }else{
            actTagIv.setVisibility(View.GONE);
        }
//        MyRoundCornersTransformation transform = new MyRoundCornersTransformation(mContext, ScreenUtils.dp2px(mContext,10), MyRoundCornersTransformation.CornerType.TOP);
        exchangeNumTv.setText(mContext.getString(R.string.label_have_used)+" "+bean.getUsedAmout());
        String token = (String) SPUtil.get(mContext, "Token", "");
        int couponId = mRewardlist.get(position).getCouponId();
        int tokenNum=0;
        switch (couponId){
            case 1:
                tokenNum=data.getBrozenTokenNum();
                break;
            case 2:
                tokenNum=data.getSilverTokenNum();
                break;
            case 3:
                tokenNum=data.getGoldTokenNum();
                break;
            case 4:
                tokenNum=data.getDiamongTokenNum();
                break;
        }
        if(TextUtils.isEmpty(token)){
            exchangeTv.setBackgroundResource(R.drawable.login_deng);
//            exchangeTv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent login=new Intent(mContext, LoginMeActivity.class);
//                    login.putExtra("Login_falg",1);
//                    mContext.startActivity(login);
//                }
//            });
        }else{

            if(tokenNum>0){
                exchangeTv.setBackgroundResource(R.drawable.login_deng);
//                exchangeTv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        switch (couponId){
//                            case 1:
//                                SPUtil.put(mContext, "couponSn", data.getCouponSnBrozen());
//                                SPUtil.put(mContext, "couponId", String.valueOf(data.getCouponIdBrozen()));
//                                SPUtil.put(mContext, "couponType", String.valueOf(data.getCouponTypeBrozen()));
//                                SPUtil.put(mContext,"shopId",String.valueOf(data.getShopId()));
//                                break;
//                            case 2:
//                                SPUtil.put(mContext, "couponSn", data.getCouponSnSilver());
//                                SPUtil.put(mContext, "couponId", String.valueOf(data.getCouponIdSilver()));
//                                SPUtil.put(mContext, "couponType", String.valueOf(data.getCouponTypeSilver()));
//                                SPUtil.put(mContext,"shopId",String.valueOf(data.getShopId()));
//                                break;
//                            case 3:
//                                SPUtil.put(mContext, "couponSn", data.getCouponSnGold());
//                                SPUtil.put(mContext, "couponId", String.valueOf(data.getCouponIdGold()));
//                                SPUtil.put(mContext, "couponType", String.valueOf(data.getCouponTypeGold()));
//                                SPUtil.put(mContext,"shopId",String.valueOf(data.getShopId()));
//                                break;
//                            case 4:
//                                SPUtil.put(mContext, "couponSn", data.getCouponSnDiamond());
//                                SPUtil.put(mContext, "couponId", String.valueOf(data.getCouponIdDiamond()));
//                                SPUtil.put(mContext, "couponType", String.valueOf(data.getCouponTypeDiamond()));
//                                SPUtil.put(mContext,"shopId",String.valueOf(data.getShopId()));
//                                break;
//                        }
//
//                        browsDialog = new OfferDialog(mContext, R.style.box_dialog, onClickListener);
//                        browsDialog.setCurrentExchangeProgress(0);
//                        browsDialog.setCanceledOnTouchOutside(false);
//                        browsDialog.setCancelable(false);
//                        browsDialog.show();
//                    }
//                });
            }else {
                exchangeTv.setBackgroundResource(R.drawable.bg_btn_disabled);
//                exchangeTv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        NoExchangedTokenDialog dlg=new NoExchangedTokenDialog(mContext,couponId,R.style.token_dialog);
//                        dlg.show();
//                        mHandler.removeCallbacksAndMessages(null);
//                        mHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                dlg.dismiss();
//                            }
//                        },1500);
//
//                    }
//                });
            }
        }
        exchangeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardItemTopBean bean=mRewardlist.get(position);
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
//                coupDetail.putStringArrayListExtra("image",(ArrayList<String>) mData.getStoreImages());
                mContext.startActivity(coupDetail);
            }
        });
        Glide.with(mContext)
                .load(commodityPic)
                .asBitmap()
                .thumbnail(0.3f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) //缓存
                .into(gridview_img_one);

        if (couponId == 1) {
            gridview_img.setBackgroundResource(R.drawable.token_icon_bronze);
        } else if (couponId == 2) {
            gridview_img.setBackgroundResource(R.drawable.token_icon_silver);
        } else if (couponId == 3) {
            gridview_img.setBackgroundResource(R.drawable.token_icon_gold);
        } else if (couponId == 4) {
            gridview_img.setBackgroundResource(R.drawable.token_icon_diamond);
        }
        int costs = mRewardlist.get(position).getCosts();
        gridview_txtx.setText("X" + costs);
        int usedAmout = mRewardlist.get(position).getUsedAmout();
        int maxUseAmout = mRewardlist.get(position).getMaxUseAmout();
        gridview_tx.setText(usedAmout + "/" + maxUseAmout);

        return view;
    }
}