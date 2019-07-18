package com.app.whoot.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.ui.activity.SearchActivity;

import java.util.Collections;
import java.util.List;

/**
 * Created by Sunrise on 5/8/2018.
 */

public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private List<String> history;

    private HisOnClickListener onDelItem;//删除单条item的接口

    public interface HisOnClickListener{
        void onItemClick(ImageView history_item_img, int position);
    }

    public void setHisOnClickListener(HisOnClickListener ll){
        this.onDelItem=ll;
    }

    public HistoryAdapter(Context context, List<String> history) {
        this.context=context;
        this.history=history;
    }

    @Override
    public int getCount() {
        Log.d("hist",history.size()+"");
        return history.size();
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
        if (view==null){
            hodler = new ViewHodler();
            view = LayoutInflater.from(context).inflate(R.layout.history_item, null);
            view.setTag(hodler);
        }else {
            hodler = (ViewHodler) view.getTag();
        }
        hodler.history_item_img = view.findViewById(R.id.history_item_img);
        hodler.history_item_txt = view.findViewById(R.id.history_item_txt);
        hodler.history_item_txt.setText(history.get(position));
        hodler.history_item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelItem.onItemClick(hodler.history_item_img,position);
            }
        });

        return view;
    }
    class ViewHodler{
        ImageView history_item_img;
        TextView history_item_txt;
    }


}
