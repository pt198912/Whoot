package com.app.whoot.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.RewardItemBean;
import com.app.whoot.ui.activity.CardItemActivity;
import com.app.whoot.ui.activity.DishesActivity;
import com.app.whoot.ui.view.custom.ScaleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;



public class StoreImageAdapter extends RecyclerView.Adapter<StoreImageAdapter.ViewHolder>{

    private Context mContext;
    //动态数组
    private List<String> mList;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    //构造
    public StoreImageAdapter(List<String> mList, Context context) {
        this.mList = mList;
        this.mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //绑定行布局
        View view = View.inflate(parent.getContext(), R.layout.activity_index_item,null);
        //实例化ViewHolder
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    //设置数据
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //获取当前实体类对象
        String url = mList.get(position);
        holder.text.setVisibility(View.GONE);
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .thumbnail(0.3f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) //缓存
                .error(R.drawable.headimg)
                .into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* ScaleImageView scaleImageView = new ScaleImageView((Activity) mContext);
                scaleImageView.setUrls(mList, position);
                scaleImageView.create();*/
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

    }

    //数量
    @Override
    public int getItemCount() {
        return mList.size();
    }

    //内部类
    class ViewHolder extends RecyclerView.ViewHolder{
        //行布局中的控件
        ImageView img;
        TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            //绑定控件
            img = (ImageView) itemView.findViewById(R.id.id_item_image);
            text = (TextView) itemView.findViewById(R.id.id_item_text);
        }
    }
}

