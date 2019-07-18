package com.app.whoot.adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.bean.CardBean;
import com.app.whoot.ui.activity.ViewPagerActivity;
import com.app.whoot.ui.view.custom.MultiImageView;
import com.app.whoot.ui.view.custom.MyGridView;
import com.app.whoot.ui.view.custom.RoundImageView;
import com.app.whoot.ui.view.custom.ScaleImageView;
import com.app.whoot.util.SysUtils;
import com.app.whoot.util.TimeUtil;
import com.bumptech.glide.Glide;
import com.jchou.imagereview.ui.ImagePagerActivity;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by pt on 5/15/2018.
 */

public class StoreDetailReviewAdapter extends RecyclerView.Adapter<StoreDetailReviewAdapter.ViewHodler> {


    private Context context;
    private List<CardBean> list;


    private int ab=0;
    private ItemGridViewAdapter nearByInfoImgsAdapter;
    private long replyTm;
    private String sub_name;
    private Bundle bundle;
    private boolean isPreviewingImage;
    private static final String TAG = "ShopsLVAdapter";

    public void setPreviewingImage(boolean previewingImage) {
        isPreviewingImage = previewingImage;
    }

    public boolean isPreviewingImage() {
        return isPreviewingImage;
    }

    public void onActivityReenterCallback(int resultCode, Intent data) {

        Bundle bundle = data.getExtras();
        int currentPosition = bundle.getInt(ImagePagerActivity.STATE_POSITION,0);
        Log.d(TAG, "onActivityReenterCallback: currentPosition "+currentPosition);
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
                    ActivityCompat.startPostponedEnterTransition((Activity) context);
                    return true;
                }
            });
        }
    }


    public StoreDetailReviewAdapter(Context context, List<CardBean> list) {
        this.context = context;
        this.list = list;

    }
    public List<CardBean> getList() {
        return list;
    }

    public void setList(List<CardBean> list) {
        this.list = list;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private RecyclerView mCurrentClickRv;

    @Override
    public ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hera, parent,false);
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(ViewHodler hodler, int position) {
        ArrayList<String> imglist=new ArrayList();
        CardBean bean=list.get(position);
        hodler.store_item_name.setText(bean.getCommodityName());
        int couponId = bean.getCouponId();
        if (couponId==1){
            hodler.store_item_token.setBackgroundResource(R.drawable.token_icon_bronze);
        }else if (couponId==2){
            hodler.store_item_token.setBackgroundResource(R.drawable.token_icon_silver);
        }else if (couponId==3){
            hodler.store_item_token.setBackgroundResource(R.drawable.token_icon_gold);
        }else if (couponId==4){
            hodler.store_item_token.setBackgroundResource(R.drawable.token_icon_diamond);
        }
        String userName = list.get(position).getUserName();
        if (userName!=null&&userName.length()>3){
            sub_name = userName.substring(0, 3) + "*****";
        }else {
            sub_name=userName;
        }

        hodler.item_hera_txview.setText(sub_name);
        int grade = list.get(position).getGrade();
        if (grade==1){
            hodler.item_hera_img0.setBackgroundResource(R.drawable.icon_star_10);
            hodler.item_hera_img1.setBackgroundResource(R.drawable.icon_star_0);
            hodler.item_hera_img2.setBackgroundResource(R.drawable.icon_star_0);
            hodler.item_hera_img3.setBackgroundResource(R.drawable.icon_star_0);
            hodler.item_hera_img4.setBackgroundResource(R.drawable.icon_star_0);
        }else if (grade==2){
            hodler.item_hera_img0.setBackgroundResource(R.drawable.icon_star_10);
            hodler.item_hera_img1.setBackgroundResource(R.drawable.icon_star_10);
            hodler.item_hera_img2.setBackgroundResource(R.drawable.icon_star_0);
            hodler.item_hera_img3.setBackgroundResource(R.drawable.icon_star_0);
            hodler.item_hera_img4.setBackgroundResource(R.drawable.icon_star_0);
        }else if (grade==3){
            hodler.item_hera_img0.setBackgroundResource(R.drawable.icon_star_10);
            hodler.item_hera_img1.setBackgroundResource(R.drawable.icon_star_10);
            hodler.item_hera_img2.setBackgroundResource(R.drawable.icon_star_10);
            hodler.item_hera_img3.setBackgroundResource(R.drawable.icon_star_0);
            hodler.item_hera_img4.setBackgroundResource(R.drawable.icon_star_0);
        }else if (grade==4){
            hodler.item_hera_img0.setBackgroundResource(R.drawable.icon_star_10);
            hodler.item_hera_img1.setBackgroundResource(R.drawable.icon_star_10);
            hodler.item_hera_img2.setBackgroundResource(R.drawable.icon_star_10);
            hodler.item_hera_img3.setBackgroundResource(R.drawable.icon_star_10);
            hodler.item_hera_img4.setBackgroundResource(R.drawable.icon_star_0);
        }else if (grade==5){
            hodler.item_hera_img0.setBackgroundResource(R.drawable.icon_star_10);
            hodler.item_hera_img1.setBackgroundResource(R.drawable.icon_star_10);
            hodler.item_hera_img2.setBackgroundResource(R.drawable.icon_star_10);
            hodler.item_hera_img3.setBackgroundResource(R.drawable.icon_star_10);
            hodler.item_hera_img4.setBackgroundResource(R.drawable.icon_star_10);
        }else {
            hodler.item_hera_img0.setBackgroundResource(R.drawable.icon_star_0);
            hodler.item_hera_img1.setBackgroundResource(R.drawable.icon_star_0);
            hodler.item_hera_img2.setBackgroundResource(R.drawable.icon_star_0);
            hodler.item_hera_img3.setBackgroundResource(R.drawable.icon_star_0);
            hodler.item_hera_img4.setBackgroundResource(R.drawable.icon_star_0);
        }
        try{String timeFormatZx = TimeUtil.timedaNo(Long.parseLong(list.get(position).getUpdateTm()));
            hodler.item_hera_text.setText(timeFormatZx);
        }catch (Exception e){}


        String content = list.get(position).getContent();
        hodler.item_hera_title.setText(content);
        String replyT = list.get(position).getReplyTm();
        try
        {
            replyTm = Long.parseLong(replyT);
        }catch (Exception e){
            replyTm=0;
        }
        if (replyTm==0){
            hodler.item_hera_ly.setVisibility(View.GONE);
        }else {
            hodler.item_hera_ly.setVisibility(View.VISIBLE);
        }
        hodler.item_hera_zx.setText(TimeUtil.timedate(replyTm));
        String reply = list.get(position).getReply();
        hodler.item_hera_tz.setText(reply);
        Glide.with(context)
                .load(list.get(position).getUserPhoto())
                .centerCrop()
                .skipMemoryCache(true)
                .into(hodler.item_hera_round);
        String imgUrl = list.get(position).getImgUrl();

        if (imgUrl==null||imgUrl.length()==0){
        }else {

            if (imgUrl.indexOf(",")!=-1){
                String[] split = imgUrl.split(",");
                if (imglist.size()!=0){
                    imglist.clear(); }
                for (int i = 0; i <split.length ; i++) {
                    imglist.add(split[i]);
                }
                hodler.image_recycler.setLayoutManager(new GridLayoutManager(context,3));
                MeCommAdapter commAdapter = new MeCommAdapter(imglist);
                commAdapter.setOnItemClickListener(new MeCommAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int pos) {
                        DishReviewAdapter.mCurrentShopLvAdapter=StoreDetailReviewAdapter.this;
                        isPreviewingImage=true;
                        mCurrentClickRv=hodler.image_recycler;
                        ImagePagerActivity.startImagePage((Activity) context,
                                imglist,pos, hodler.image_recycler.getLayoutManager().findViewByPosition(pos));
                    }
                });
                hodler.image_recycler.setAdapter(commAdapter);
                //设置转场动画的共享元素，因为跳转和返回都会调用，需要判断bundle是否为空
                ((BaseActivity) context).setExitSharedElementCallback(new SharedElementCallback() {
                    @Override
                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                        if (bundle!=null){
                            int index = bundle.getInt(ImagePagerActivity.STATE_POSITION,0);
                            sharedElements.clear();
                            sharedElements.put("img", hodler.image_recycler.getLayoutManager().findViewByPosition(index));
                            bundle=null;
                        }
                    }
                });
            }

        }

    }

    class ViewHodler extends RecyclerView.ViewHolder{
        TextView item_hera_txview, item_hera_text, item_hera_title,item_hera_z, item_hera_zx, item_hera_tz;
        ImageView item_hera_img0, item_hera_img1, item_hera_img2, item_hera_img3, item_hera_img4;
        RoundImageView item_hera_round;
        LinearLayout item_hera_ly;
        TextView store_item_name;
        ImageView store_item_token;
        TextView store_item_num;
        RecyclerView image_recycler;

        public ViewHodler(View view) {
            super(view);
            item_hera_txview=(TextView)view.findViewById(R.id.item_hera_txview);
            item_hera_text=(TextView)view.findViewById(R.id.item_hera_text);
            item_hera_title=(TextView)view.findViewById(R.id.item_hera_title);
            item_hera_z=(TextView)view.findViewById(R.id.item_hera_z);
            item_hera_zx=(TextView)view.findViewById(R.id.item_hera_zx);
            item_hera_tz=(TextView)view.findViewById(R.id.item_hera_tz);
            item_hera_img0=(ImageView)view.findViewById(R.id.item_hera_img0);
            item_hera_img1=(ImageView)view.findViewById(R.id.item_hera_img1);
            item_hera_img2=(ImageView)view.findViewById(R.id.item_hera_img2);
            item_hera_img3=(ImageView)view.findViewById(R.id.item_hera_img3);
            item_hera_img4=(ImageView)view.findViewById(R.id.item_hera_img4);
            item_hera_round=(RoundImageView)view.findViewById(R.id.item_hera_round);
            item_hera_ly=(LinearLayout)view.findViewById(R.id.item_hera_ly);
            store_item_name =view.findViewById(R.id.store_item_name);
            store_item_token =view.findViewById(R.id.store_item_token);
            store_item_num = view.findViewById(R.id.store_item_num);
            image_recycler = (RecyclerView)view.findViewById(R.id.image_recycler);
        }
    }
}
