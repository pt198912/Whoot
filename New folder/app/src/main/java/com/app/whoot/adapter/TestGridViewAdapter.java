package com.app.whoot.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.app.whoot.R;
import com.app.whoot.util.SysUtils;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */

public class TestGridViewAdapter extends BaseAdapter {
    Activity context;
    List<String> list;
    public Bitmap bitmaps[];
    private int wh;
    private ImageLoader imageLoader;


    public TestGridViewAdapter(Activity context, List<String> data) {
        this.context=context;
        this.wh=(SysUtils.getScreenWidth(context)-SysUtils.Dp2Px(context, 99))/3;
        this.list=data;
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(configuration);
        imageLoader = ImageLoader.getInstance();

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }


    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        Holder holder;
        Log.d("wobuyiy"," "+list.size());
        if (view==null) {
            view= LayoutInflater.from(context).inflate(R.layout.item_gridview, null);
            holder=new Holder();
            holder.imageView=(ImageView) view.findViewById(R.id.imageView);
            view.setTag(holder);
        }else {
            holder= (Holder) view.getTag();
        }
        String s = list.get(position);
        Log.d("tupian",s);
        imageLoader.displayImage(s.trim(), holder.imageView, getDefaultOptions());
        /*Glide.with(context)
                .load("http://t2.27270.com/uploads/tu/201706/9999/d38274f15c.jpg")
                .centerCrop()
                .skipMemoryCache(true)
                .into(holder.imageView);*/
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(wh,wh);
        view.setLayoutParams(param);
        return view;
    }

    class Holder{
        ImageView imageView;
    }

    /**
     * 设置默认图片
     *
     * @return
     */
    public static DisplayImageOptions getDefaultOptions() {
        //设置图片加载的属性
        DisplayImageOptions.Builder b = new DisplayImageOptions.Builder();
        //图片为空的时候显示为
        b.showImageForEmptyUri(R.drawable.review_blank_pic);
        //图片加载失败的时候显示为
        b.showImageOnFail(R.drawable.review_blank_pic);
        //图片正在加载的时候显示为
        b.showImageOnLoading(R.drawable.review_blank_pic);

        b.resetViewBeforeLoading(Boolean.TRUE);

        b.cacheOnDisk(Boolean.TRUE);

        b.cacheInMemory(Boolean.TRUE);

        b.imageScaleType(ImageScaleType.EXACTLY_STRETCHED);

        return b.bitmapConfig(Bitmap.Config.RGB_565).build();
    }
}
