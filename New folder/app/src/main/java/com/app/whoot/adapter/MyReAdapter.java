package com.app.whoot.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.RewardItemBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.CardItemActivity;
import com.app.whoot.ui.activity.DishesActivity;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Sunrise on 11/28/2018.
 */

public class MyReAdapter extends RecyclerView.Adapter<MyReAdapter.ViewHolder>{

    private CardItemActivity mcontext;
    //动态数组
    private List<RewardItemBean> mList;


    //构造
    public MyReAdapter(List<RewardItemBean> mList, CardItemActivity cardItemActivity) {
        this.mList = mList;
        this.mcontext=cardItemActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //绑定行布局
        View view = View.inflate(parent.getContext(), R.layout.activity_index_item,null);
        //实例化ViewHolder
        ViewHolder holder = new ViewHolder(view);

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        //获取当前实体类对象
        RewardItemBean vo = mList.get(position);
        //设置
        holder.text.setText(vo.getName());


        Glide.with(mcontext)
                .load(vo.getImgUrl())
                .centerCrop()
                .skipMemoryCache(true)
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
        public ViewHolder(View itemView) {
            super(itemView);
            //绑定控件
            img = (ImageView) itemView.findViewById(R.id.id_item_image);
            text = (TextView) itemView.findViewById(R.id.id_item_text);
        }
    }
}

