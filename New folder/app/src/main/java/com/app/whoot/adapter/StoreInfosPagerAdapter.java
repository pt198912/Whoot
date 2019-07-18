package com.app.whoot.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.app.whoot.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class StoreInfosPagerAdapter extends PagerAdapter {
    List<View> mViews=new ArrayList<>();
    Context mContext;
    private static final String TAG = "StoreInfosPagerAdapter";

    public StoreInfosPagerAdapter(Context context, List<View> views){
        this.mContext=context;
        setData(views);
    }
    public void setData(List<View> views){
        mViews.clear();
        if(views!=null&&views.size()>0){
            mViews.addAll(views);
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view=mViews.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }

    public static class ViewPagerTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.9f;

        @Override
        public void transformPage(View view, float position) {
            /**
             * 过滤那些 <-1 或 >1 的值，使它区于【-1，1】之间
             */
            if (position < -1) {
                position = -1;
            } else if (position > 1) {
                position = 1;
            }
            /**
             * 判断是前一页 1 + position ，右滑 pos -> -1 变 0
             * 判断是后一页 1 - position ，左滑 pos -> 1 变 0
             */
            float tempScale = position < 0 ? 1 + position : 1 - position; // [0,1]
            float scaleValue = MIN_SCALE + tempScale * 0.1f; // [0,1]
            view.setScaleX(scaleValue);
            view.setScaleY(scaleValue);
        }
    }

}
