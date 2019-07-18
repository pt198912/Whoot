package com.app.whoot.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.CardItemTopBean;
import com.app.whoot.bean.RewardItemBean;
import com.app.whoot.ui.activity.CardItemActivity;
import com.app.whoot.ui.activity.DishesActivity;
import com.app.whoot.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

class StoreRecommendAdapter extends RecyclerView.Adapter<StoreRecommendAdapter.ViewHolder> {
    private Context mcontext;
    //动态数组
    private List<RewardItemBean> mList;


    //构造
    public StoreRecommendAdapter(List<RewardItemBean> mList, Context context) {
        this.mList = mList;
        this.mcontext=context;
    }

    @Override
    public StoreRecommendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //绑定行布局
        View view = View.inflate(parent.getContext(), R.layout.activity_index_item,null);
        //实例化ViewHolder
        StoreRecommendAdapter.ViewHolder holder = new StoreRecommendAdapter.ViewHolder(view);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取到position
                int layoutPosition = holder.getLayoutPosition();
                //获取当前实体类对象
                RewardItemBean vo = mList.get(layoutPosition);
                String describe = vo.getDescribe(); //描述
                String imgUrl = vo.getImgUrl(); //图片
                String name = vo.getName();    //名字
                double price = vo.getPrice();  //价格
                Intent intent1 = new Intent(mcontext, DishesActivity.class);
                intent1.putExtra("describe", describe);
                intent1.putExtra("imgUrl", imgUrl);
                intent1.putExtra("name", name);
                intent1.putExtra("price", price);
                mcontext.startActivity(intent1);
            }
        });

        return holder;
    }

    //设置数据
    @Override
    public void onBindViewHolder(StoreRecommendAdapter.ViewHolder holder, int position) {
        //获取当前实体类对象
        RewardItemBean vo = mList.get(position);
        //设置
        holder.text.setText(vo.getName());
        if(vo.getActivityTag()!=0) {
            holder.actTagIv.setVisibility(View.VISIBLE);
            if (Util.isChineseLanguage(mcontext)){
                holder.actTagIv.setBackgroundResource(R.drawable.home_tag_activity_cn);
            }else {
                holder.actTagIv.setBackgroundResource(R.drawable.home_tag_activity_en);
            }
        }else{
            holder.actTagIv.setVisibility(View.GONE);
        }
        Glide.with(mcontext)
                .load(vo.getImgUrl())
                .asBitmap()
                .thumbnail(0.3f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) //缓存
                .error(R.drawable.headimg)
                .into(holder.img);

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
        ImageView actTagIv;
        public ViewHolder(View itemView) {
            super(itemView);
            //绑定控件
            img = (ImageView) itemView.findViewById(R.id.id_item_image);
            text = (TextView) itemView.findViewById(R.id.id_item_text);
            actTagIv=(ImageView)itemView.findViewById(R.id.iv_activity_tag);
        }
    }
}
