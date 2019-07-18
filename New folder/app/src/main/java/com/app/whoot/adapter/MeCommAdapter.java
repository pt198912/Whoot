package com.app.whoot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.whoot.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class MeCommAdapter extends RecyclerView.Adapter<MeCommAdapter.Holder> {
    private static final String TAG = "MeCommAdapter";
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    private List<String> mDatas;
    private Context mContext;

    public MeCommAdapter(List<String> datas) {
        mDatas = datas;
    }

    @Override
    public MeCommAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: "+"2222222222222");
        mContext=parent.getContext();
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.item_activity_main, parent, false));
    }

    @Override
    public void onBindViewHolder(MeCommAdapter.Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
       Glide.with(mContext).load(mDatas.get(position))
                .placeholder(R.drawable.review_pic_blank).dontAnimate()
                .into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public Holder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_item_main_activity_image);
        }
    }
}
