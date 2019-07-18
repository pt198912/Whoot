package com.app.whoot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.NonNull;

import com.app.whoot.R;
import com.app.whoot.bean.InviteBean;
import com.app.whoot.ui.view.custom.ShapeImageView;
import com.app.whoot.util.TimeUtil;
import com.bumptech.glide.Glide;

import java.util.List;

public class DemoAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<InviteBean> mEntityList;

    public DemoAdapter (Context context, List<InviteBean> entityList){
        this.mContext = context;
        this.mEntityList = entityList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_invite, parent, false);
        return new DemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        InviteBean s = mEntityList.get(position);
        long registTm = s.getLastloginTm();
        String s1 = TimeUtil.timedateNomm(registTm);
        ((DemoViewHolder)holder).mText.setText(s1);
        ((DemoViewHolder)holder).demo_name.setText(s.getName());
        Glide.with(mContext)
                .load(s.getPhoto())
                .centerCrop()
                .skipMemoryCache(true)
                .error(R.drawable.profile_photo_owner)
                .into( ((DemoViewHolder)holder).demo_round_too);
    }

    @Override
    public int getItemCount() {
        return mEntityList.size();
    }

    private class DemoViewHolder extends RecyclerView.ViewHolder{

        private TextView mText;
        private TextView demo_name;
        private ShapeImageView demo_round_too;

        public DemoViewHolder(View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.demo_time);
            demo_name = itemView.findViewById(R.id.demo_name);
            demo_round_too = itemView.findViewById(R.id.demo_round_too);
        }
    }
}
