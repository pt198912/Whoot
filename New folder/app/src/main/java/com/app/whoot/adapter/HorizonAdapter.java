package com.app.whoot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.RewardItemBean;
import com.app.whoot.ui.activity.CardItemActivity;
import com.bumptech.glide.Glide;


import java.util.List;

/**
 * Created by Sunrise on 6/13/2018.
 */

public class HorizonAdapter extends BaseAdapter {
    private  Context context;
    private  List<RewardItemBean> toplist;

    public HorizonAdapter(Context context, List<RewardItemBean> toplist) {
        this.context=context;
        this.toplist=toplist;
    }

    @Override
    public int getCount() {
        return toplist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHodler hodler;
        if (view == null) {
            hodler = new ViewHodler();

            view = LayoutInflater.from(context).inflate(R.layout.activity_index_item, null);
            view.setTag(hodler);
        } else {
            hodler = (ViewHodler) view.getTag();
        }
        hodler.id_item_image = view.findViewById(R.id.id_item_image);
        hodler.id_item_text = view.findViewById(R.id.id_item_text);
        Glide.with(context)
                .load(toplist.get(position).getImgUrl())
                .centerCrop()
                .skipMemoryCache(true)
                .into(hodler.id_item_image);
        String name = toplist.get(position).getName();
        hodler.id_item_text.setText(name);
        return view;
    }

    class ViewHodler{
        ImageView id_item_image;
        TextView id_item_text;
    }
}
