package com.app.whoot.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.adapter.DishReviewAdapter;
import com.app.whoot.base.fragment.BaseFragment;
import com.app.whoot.bean.DishReviewBean;
import com.app.whoot.ui.activity.StoreDetailActivity;


import butterknife.BindInt;
import butterknife.BindView;


public class DishReviewFragment extends BaseFragment {

    @BindView(R.id.rv_dish_review)
    RecyclerView rvDishReview;
    @BindView(R.id.rl_dish_review)
    RelativeLayout rlDishReview;

    private DishReviewAdapter mDishReviewAdapter;
    private DishReviewBean mData;
    private static final String TAG = "DishReviewFragment";
    @Override
    public int getLayoutId() {
        return R.layout.frag_dish_review;
    }

    @Override
    public void onViewCreatedInit() {
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mDishReviewAdapter!=null){
            mDishReviewAdapter.releaseRes();
            mDishReviewAdapter=null;
        }
    }

    public void onActivityReenterCallback(int resultCode, Intent data){
        if(mDishReviewAdapter!=null&&mDishReviewAdapter.getOnActivityReenterListener()!=null){
            mDishReviewAdapter.getOnActivityReenterListener().onActivityReenterCallback(resultCode,data);
        }
    }


    private LinearLayoutManager linearLayoutManager;
    private void initView(){
        linearLayoutManager=new LinearLayoutManager(getContext());
        mData=new DishReviewBean();
        mDishReviewAdapter=new DishReviewAdapter(getActivity(),mData);
        rvDishReview.setLayoutManager(linearLayoutManager);
        rvDishReview.setAdapter(mDishReviewAdapter);
        rvDishReview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                Log.d(TAG, "onLayoutChange: ");
                addRvListener();
            }
        });
       

    }
    private void addRvListener(){
        rvDishReview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                //获取第一个可见view的位置
                int firstItemPosition =linearLayoutManager.findFirstVisibleItemPosition();
                Log.d(TAG, "onScrollStateChanged: firstItemPosition "+firstItemPosition+",lastItemPosition "+lastItemPosition);
                try {
                    if (firstItemPosition + 1 == mDishReviewAdapter.getItemCount()) {
                        //最后一个itemView的position为adapter中最后一个数据时,说明该itemView就是底部的view了
                        //需要注意position从0开始索引,adapter.getItemCount()是数据量总数
                        Log.d(TAG, "onScrollStateChanged: scroll to last");
                        ((StoreDetailActivity) getActivity()).setCurrentTab(1);
                    } else if (firstItemPosition + 1 < mDishReviewAdapter.getItemCount()) {
                        Log.d(TAG, "onScrollStateChanged: scroll to not last");
                        ((StoreDetailActivity) getActivity()).setCurrentTab(0);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
//                //同理检测是否为顶部itemView时,只需要判断其位置是否为0即可
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && firstItemPosition == 0) {
//                    Log.d(TAG, "onScrollStateChanged: scroll to first");
//                }
            }
        });

    }
    public void refreshOnDataLoad(DishReviewBean bean){
        if(isDetached()){
            return;
        }
        this.mData=bean;
        if(mDishReviewAdapter!=null) {
            mDishReviewAdapter.setData(bean);
        }
    }



    @Override
    public void onStartInit() {

    }

    public boolean isPreviewingImage(){
        if(mDishReviewAdapter!=null){
            return mDishReviewAdapter.isPreviewingImage();
        }
        return false;
    }

    public void setPreviewingImage(boolean flag){
        if(mDishReviewAdapter!=null){
            mDishReviewAdapter.setPreviewingImage(flag);
        }
    }

    public void onTabChange(int tabIndex) {
        if(tabIndex==0){
            linearLayoutManager.scrollToPositionWithOffset(0,0);
        }else{
            linearLayoutManager.scrollToPositionWithOffset(2,0);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
