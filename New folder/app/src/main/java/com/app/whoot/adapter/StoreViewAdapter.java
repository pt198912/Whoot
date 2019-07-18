package com.app.whoot.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.CardBean;
import com.app.whoot.bean.CommBean;
import com.app.whoot.interfacevoke.OnActivityReenterListener;
import com.app.whoot.ui.activity.CardItemActivity;
import com.app.whoot.ui.activity.MeCommentActivity;
import com.app.whoot.ui.view.custom.MultiImageView;
import com.app.whoot.ui.view.custom.MyGridView;
import com.app.whoot.ui.view.custom.RoundImageView;
import com.app.whoot.ui.view.custom.ScaleImageView;
import com.app.whoot.ui.view.demo.ImageViewer;
import com.app.whoot.ui.view.demo.ViewData;
import com.app.whoot.util.TimeUtil;
import com.bumptech.glide.Glide;
import com.jchou.imagereview.ui.ImagePagerActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Sunrise on 7/13/2018.
 */

public class StoreViewAdapter extends ListBaseAdapter<CardBean> implements MultiImageView.OnItemClickListener ,OnActivityReenterListener {

    private Context context;


    private int posi = 0;
    private String[] split;
    private List<ViewData> mViewDatas;
    private StoreViewAdapter.MyClickListener mListener;
    //存放返回时当前页码
    private Bundle bundle;


    // 采用接口回调的方式实现RecyclerView的ItemClick
    public StoreViewAdapter.OnRecyclerViewListener mOnRecyclerViewListener;
    private long replyTm;



    @Override
    public void onItemClick(View view, int position) {
        mListener.clickListener(view, position);
    }

    @Override
    public void onActivityReenterCallback(int resultCode, Intent data) {
        bundle = data.getExtras();
        int currentPosition = bundle.getInt(ImagePagerActivity.STATE_POSITION,0);
        //做相应的滚动
        if(mCurrentClickRv!=null) {
            mCurrentClickRv.scrollToPosition(currentPosition);
            //暂时延迟 Transition 的使用，直到我们确定了共享元素的确切大小和位置才使用
            //postponeEnterTransition后不要忘记调用startPostponedEnterTransition

            mCurrentClickRv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mCurrentClickRv.getViewTreeObserver().removeOnPreDrawListener(this);
                    // TODO: figure out why it is necessary to request layout here in order to get a smooth transition.
                    mCurrentClickRv.requestLayout();
                    //共享元素准备好后调用startPostponedEnterTransition来恢复过渡效果
                    ActivityCompat.startPostponedEnterTransition((Activity) mContext);
                    return true;
                }
            });
        }
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

    // 接口回调第二步: 初始化接口的引用
    public void setOnRecyclerViewListener(OnRecyclerViewListener l) {
        this.mOnRecyclerViewListener = l;
    }

    public StoreViewAdapter(Context context) {
        super(context);
        this.context =context;
        setOnActivityReenterListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.store_adapter; //item_expandable_lv   store_adapter
    }

    private RecyclerView mCurrentClickRv;

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

        ArrayList<String> imglist=new ArrayList();
        CardBean cardBean = mDataList.get(position);
        if (imglist.size() > 0) {
            imglist.clear();
        }
        posi = position;




        TextView item_hera_txview=holder.getView(R.id.store_hera_txview);
        TextView item_hera_text=holder.getView(R.id.store_hera_text);
        TextView item_hera_title=holder.getView(R.id.store_hera_title);
        ImageView item_hera_img0=holder.getView(R.id.store_hera_img0);
        ImageView item_hera_img1=holder.getView(R.id.store_hera_img1);
        ImageView item_hera_img2=holder.getView(R.id.store_hera_img2);
        ImageView item_hera_img3=holder.getView(R.id.store_hera_img3);
        ImageView item_hera_img4=holder.getView(R.id.store_hera_img4);
        LinearLayout item_hera_ly = holder.getView(R.id.item_hera_ly);
        TextView item_hera_tz = holder.getView(R.id.item_hera_tz);
        TextView store_item_name = holder.getView(R.id.store_item_name);
        ImageView store_item_token = holder.getView(R.id.store_item_token);

        RoundImageView item_hera_round=holder.getView(R.id.store_hera_round);

        TextView item_hera_zx=holder.getView(R.id.store_hera_zx);


        RecyclerView store_recycler = (RecyclerView)holder.getView(R.id.store_recycler);



        String userName = cardBean.getUserName();
        if (userName.length()>3){
            String s1 = userName.substring(0, 3) + "*****";
            item_hera_txview.setText(s1);
        }else {
            item_hera_txview.setText(userName);
        }
        store_item_name.setText(cardBean.getCommodityName());
        int couponId = cardBean.getCouponId();
        if (couponId==1){
            store_item_token.setBackgroundResource(R.drawable.token_icon_bronze);
        }else if (couponId==2){
            store_item_token.setBackgroundResource(R.drawable.token_icon_silver);
        }else if (couponId==3){
            store_item_token.setBackgroundResource(R.drawable.token_icon_gold);
        }else if (couponId==4){
            store_item_token.setBackgroundResource(R.drawable.token_icon_diamond);
        }

        int grade = cardBean.getGrade();
        if (grade==1){
            item_hera_img0.setBackgroundResource(R.drawable.icon_star_10);
            item_hera_img1.setBackgroundResource(R.drawable.icon_star_0);
            item_hera_img2.setBackgroundResource(R.drawable.icon_star_0);
            item_hera_img3.setBackgroundResource(R.drawable.icon_star_0);
            item_hera_img4.setBackgroundResource(R.drawable.icon_star_0);
        }else if (grade==2){
            item_hera_img0.setBackgroundResource(R.drawable.icon_star_10);
            item_hera_img1.setBackgroundResource(R.drawable.icon_star_10);
            item_hera_img2.setBackgroundResource(R.drawable.icon_star_0);
            item_hera_img3.setBackgroundResource(R.drawable.icon_star_0);
            item_hera_img4.setBackgroundResource(R.drawable.icon_star_0);
        }else if (grade==3){
            item_hera_img0.setBackgroundResource(R.drawable.icon_star_10);
            item_hera_img1.setBackgroundResource(R.drawable.icon_star_10);
            item_hera_img2.setBackgroundResource(R.drawable.icon_star_10);
            item_hera_img3.setBackgroundResource(R.drawable.icon_star_0);
            item_hera_img4.setBackgroundResource(R.drawable.icon_star_0);
        }else if (grade==4){
            item_hera_img0.setBackgroundResource(R.drawable.icon_star_10);
            item_hera_img1.setBackgroundResource(R.drawable.icon_star_10);
            item_hera_img2.setBackgroundResource(R.drawable.icon_star_10);
            item_hera_img3.setBackgroundResource(R.drawable.icon_star_10);
            item_hera_img4.setBackgroundResource(R.drawable.icon_star_0);
        }else if (grade==5){
            item_hera_img0.setBackgroundResource(R.drawable.icon_star_10);
            item_hera_img1.setBackgroundResource(R.drawable.icon_star_10);
            item_hera_img2.setBackgroundResource(R.drawable.icon_star_10);
            item_hera_img3.setBackgroundResource(R.drawable.icon_star_10);
            item_hera_img4.setBackgroundResource(R.drawable.icon_star_10);
        }else {
            item_hera_img0.setBackgroundResource(R.drawable.icon_star_0);
            item_hera_img1.setBackgroundResource(R.drawable.icon_star_0);
            item_hera_img2.setBackgroundResource(R.drawable.icon_star_0);
            item_hera_img3.setBackgroundResource(R.drawable.icon_star_0);
            item_hera_img4.setBackgroundResource(R.drawable.icon_star_0);
        }

        String timeFormatZx = TimeUtil.timedaNo(Long.parseLong(cardBean.getUpdateTm()));
        item_hera_text.setText(timeFormatZx);

        String content = cardBean.getContent();
        item_hera_title.setText(content);

        String replyT = cardBean.getReplyTm();
        try{
            replyTm = Long.parseLong(replyT);
        }catch (Exception e){
            replyTm=0;
        }

        if (replyTm==0){
            item_hera_ly.setVisibility(View.GONE);
        }else {
            item_hera_ly.setVisibility(View.VISIBLE);
        }
        String reply = cardBean.getReply();
        item_hera_tz.setText(reply);

        String timedate = TimeUtil.timedaNo(replyTm);
        item_hera_zx.setText(timedate);
        String userPhoto = cardBean.getUserPhoto();

       if (userPhoto!=null){
           Glide.with(context)
                   .load(userPhoto)
                   .centerCrop()
                   .skipMemoryCache(true)
                   .into(item_hera_round);
       }

        try {
            String imgUrl = cardBean.getImgUrl();
            if (imgUrl.equals("")) {
                //review_grid.setVisibility(View.GONE);
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

                store_recycler.setLayoutManager(new GridLayoutManager(mContext,3));
                MeCommAdapter commAdapter = new MeCommAdapter(imglist);
                commAdapter.setOnItemClickListener(new MeCommAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int pos) {
                        mCurrentClickRv=store_recycler;
                        ImagePagerActivity.startImagePage((Activity) mContext,
                                imglist,pos, store_recycler.getLayoutManager().findViewByPosition(pos));
                    }
                });
                store_recycler.setAdapter(commAdapter);
                //设置转场动画的共享元素，因为跳转和返回都会调用，需要判断bundle是否为空
                ((BaseActivity) mContext).setExitSharedElementCallback(new SharedElementCallback() {
                    @Override
                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                        if (bundle!=null){
                            int index = bundle.getInt(ImagePagerActivity.STATE_POSITION,0);
                            sharedElements.clear();
                            sharedElements.put("img", store_recycler.getLayoutManager().findViewByPosition(index));
                            bundle=null;
                        }
                    }
                });
            }

        } catch (Exception e) {

        }
    }


    //自定义接口，用于回调按钮点击事件到Activity
    public interface MyClickListener {

        public void clickListener(View view, int position);

    }
}
