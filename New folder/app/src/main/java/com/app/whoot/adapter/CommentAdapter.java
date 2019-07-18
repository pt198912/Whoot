package com.app.whoot.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.CommBean;
import com.app.whoot.ui.activity.CardItemActivity;
import com.app.whoot.ui.activity.CommentActivity;
import com.app.whoot.ui.activity.SearchActivity;
import com.app.whoot.ui.activity.StoreDetailActivity;
import com.app.whoot.ui.activity.ViewPagerActivity;
import com.app.whoot.ui.view.custom.MultiImageView;
import com.app.whoot.ui.view.custom.MyGridView;
import com.app.whoot.ui.view.custom.ScaleImageView;
import com.app.whoot.ui.view.demo.ImageLoader;
import com.app.whoot.ui.view.demo.ImageViewer;
import com.app.whoot.ui.view.demo.ViewData;
import com.app.whoot.util.Constants;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.SysUtils;
import com.app.whoot.util.TimeUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.fido.fido2.api.common.RequestOptions;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sunrise on 6/21/2018.
 */

public class CommentAdapter extends ListBaseAdapter<CommBean> implements MultiImageView.OnItemClickListener {

    private List<String> imglist = new ArrayList<>();
    private TestGridViewAdapter nearByInfoImgsAdapter;
    private int wh;
    private String[] split;
    private List<ViewData> mViewDatas;
    private RequestOptions mOptions;
    private MyClickListener mListener;
    private int posi = 0;
    // 采用接口回调的方式实现RecyclerView的ItemClick
    public OnRecyclerViewListener mOnRecyclerViewListener;

    // 接口回调第一步: 定义接口和接口中的方法
    public interface OnRecyclerViewListener {

        void onItemClick(TextView ation_item_more, int position);

        boolean onItemLongClick(int position);
    }

    // 接口回调第二步: 初始化接口的引用
    public void setOnRecyclerViewListener(OnRecyclerViewListener l) {
        this.mOnRecyclerViewListener = l;
    }

    public CommentAdapter(Context context) {

        super(context);
        this.mContext = context;
        this.wh = (SysUtils.getScreenWidth((Activity) mContext) - SysUtils.Dp2Px(mContext, 99)) / 3;

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_expandable_lv;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

        CommBean item = mDataList.get(position);
        if (imglist.size() > 0) {
            imglist.clear();
        }
        posi = position;

        TextView tv_lv0_title = holder.getView(R.id.tv_lv0_title);
        ImageView favour_ex_0 = holder.getView(R.id.favour_ex_0);
        ImageView favour_ex_1 = holder.getView(R.id.favour_ex_1);
        ImageView favour_ex_2 = holder.getView(R.id.favour_ex_2);
        ImageView favour_ex_3 = holder.getView(R.id.favour_ex_3);
        ImageView favour_ex_4 = holder.getView(R.id.favour_ex_4);
        TextView review_text = holder.getView(R.id.review_text);
        MyGridView review_grid = holder.getView(R.id.review_grid);
        TextView review_txt = holder.getView(R.id.review_txt);
        TextView review_pork = holder.getView(R.id.review_pork);
        ImageView review_img = holder.getView(R.id.review_img);
        TextView review_tet = holder.getView(R.id.review_tet);
        LinearLayout review_ly = holder.getView(R.id.review_ly);
        TextView review_z = holder.getView(R.id.review_z);
        TextView review_zx = holder.getView(R.id.review_zx);
        TextView review_tz = holder.getView(R.id.review_tz);
        MultiImageView review_layout = holder.getView(R.id.review_layout);
        ImageViewer imagePreivew = holder.getView(R.id.imagePreivew);
        LinearLayout item_ly = holder.getView(R.id.item_ly);
        ImageView favour_img = holder.getView(R.id.favour_img);
        TextView favour_name = holder.getView(R.id.favour_name);
        TextView favour_ping = holder.getView(R.id.favour_ping);
        favour_name.setText(item.getShopName());
        Glide.with(mContext)
                .load(item.getShopImg())
                .centerCrop()
                .skipMemoryCache(true)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) //缓存
                .error(R.drawable.storedetail_topfive_blank_pic)
                .into(new GlideDrawableImageViewTarget(favour_img, 1));
        mViewDatas = new ArrayList<>();
        double shopGrade = item.getGrade();
        String substring = String.valueOf(shopGrade).substring(0, 1);
        String s_xing = DateUtil.formateRate(String.valueOf(shopGrade));
        if (substring.equals("1")){
            favour_ex_0.setBackgroundResource(R.drawable.icon_star_10);

            if (s_xing.equals("1")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            }
            favour_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            favour_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
        }else if (substring.equals("2")){
            favour_ex_0.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            }else {

                favour_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            }
            favour_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
        }else if (substring.equals("3")){
            favour_ex_0.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            }

            favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
        }else if (substring.equals("4")){
            favour_ex_0.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            }
        }else if (substring.equals("5")){
            favour_ex_0.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_4.setBackgroundResource(R.drawable.icon_star_10);
        }else {
            favour_ex_0.setBackgroundResource(R.drawable.icon_star_0);
            favour_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            favour_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            favour_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
        }
        String timeFormatText = TimeUtil.timedateone(item.getUpdateTm());

        String  luchang= (String) SPUtil.get(mContext, "luchang", "");
        boolean zh = TimeUtil.isZh(mContext);


        if (luchang.equals("0")){
            if (zh){
                luchang="3";
            }else {
                luchang="2";
            }
        }
        if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
            favour_ping.setText(mContext.getResources().getString(R.string.redemp)+timeFormatText);

        }else if (luchang.equals("3")){
            favour_ping.setText(mContext.getResources().getString(R.string.redemp)+"\n"+timeFormatText);
        }
        String commodityName = item.getCommodityName();
        review_pork.setText(mContext.getResources().getString(R.string.duihuan)+" :"+commodityName);
        int couponId = item.getCouponId();
        if (couponId==0){
            review_img.setVisibility(View.INVISIBLE);
            review_tet.setVisibility(View.INVISIBLE);
        }else {
            review_img.setVisibility(View.VISIBLE);
            review_tet.setVisibility(View.VISIBLE);
            if (couponId==1){
                review_img.setBackgroundResource(R.drawable.all_icon_bronze);
            }else if (couponId==2){
                review_img.setBackgroundResource(R.drawable.all_icon_silver);
            }else if (couponId==3){
                review_img.setBackgroundResource(R.drawable.all_icon_gold);
            }else if (couponId==4){
                review_img.setBackgroundResource(R.drawable.all_icon_diamond);
            }
            review_tet.setText("X" + item.getUseCount());
        }


        review_txt.setOnClickListener(new View.OnClickListener() {  //137 5178 8376
            @Override
            public void onClick(View v) {
                int layoutPosition = holder.getLayoutPosition();
                //mOnRecyclerViewListener.onItemClick(review_txt, layoutPosition);
                int id = item.getShopId();
                SPUtil.put(mContext, Constants.SP_KEY_STORE_DETAIL_FROM,Constants.GO_STORE_DETAIL_FROM_MY_REVIEW);
                Intent intent = new Intent(mContext, StoreDetailActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("comment",1);
                mContext.startActivity(intent);
            }
        });

        try {
            String imgUrl = item.getImgUrl();
            if (imgUrl.equals("")) {
                review_grid.setVisibility(View.GONE);
            } else {

                if (imgUrl.indexOf(",") != -1) {
                    split = imgUrl.split(",");
                    if (imglist.size() != 0) {
                        imglist.clear();
                    }
                    for (int i = 0; i < split.length; i++) {
                        String s = split[i];

                        imglist.add(s.trim());

                    }
                } else {
                    imglist.add(imgUrl.trim());


                }
                review_layout.setList(imglist);
                review_layout.setOnItemClickListener(this);
                review_layout.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        imglist.clear();
                        String imgUrl = item.getImgUrl();
                        if (imgUrl.indexOf(",") != -1) {
                            split = imgUrl.split(",");
                            if (imglist.size() != 0) {
                                imglist.clear();
                            }
                            for (int i = 0; i < split.length; i++) {
                                String s = split[i];

                                imglist.add(s.trim());


                            }
                        } else {
                            imglist.add(imgUrl.trim());


                        }
                       // item_ly.setVisibility(View.GONE);
                        ScaleImageView scaleImageView = new ScaleImageView((Activity) mContext);
                        scaleImageView.setUrls(imglist, position);
                        scaleImageView.create();


                    }
                });


            }

        } catch (Exception e) {

        }

        // initInfoImages(review_grid, imglist);

        review_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        mListener.clickListener(view, position);
    }

    //自定义接口，用于回调按钮点击事件到Activity
    public interface MyClickListener {

        public void clickListener(View view, int position);

    }


}
