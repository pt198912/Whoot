package com.app.whoot.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.RecyclerData;
import com.github.jdsjlzx.interfaces.OnItemClickListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunrise on 1/30/2019.
 */

public class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.VH>{

    private int adapterPosition;

    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    private OnItemLongClickListener longClickListener;

    private int selectedPosition = -5;// 选中的位置
    private int defItem = -1;
    public void setDefSelect(int position) {
        this.defItem = position;
        //        notifyDataSetChanged();
    }


    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }



    //② 创建ViewHolder
    public static class VH extends RecyclerView.ViewHolder{
        public final TextView title;
        public VH(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.tv_item);
        }
    }
    private ArrayList<RecyclerData> mDatas;
    public NormalAdapter(ArrayList<RecyclerData> data) {
        this.mDatas = data;

    }

    //③ 在Adapter中实现3个方法
    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.title.setText(mDatas.get(position).content);
        RecyclerData re=mDatas.get(position);
        if (defItem != -1) {
            if (defItem == position) {
                //              点击的位置
                if(re.isSelect==true){
                    //              选中状态
                    holder.title.setTextColor(Color.parseColor("#fc4a1a"));

                }else {
                    holder.title.setTextColor(Color.parseColor("#333333"));
                }

            } else {
                //              没有点击的位置都变成默认背景
                holder.title.setTextColor(Color.parseColor("#333333"));
                mDatas.get(position).isSelect=false;
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item 点击事件

                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
        //5、记录要更改属性的控件
        holder.itemView.setTag(holder.title);

    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater.from指定写法
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_linearrv_item, parent, false);
        return new VH(v);
    }


}
