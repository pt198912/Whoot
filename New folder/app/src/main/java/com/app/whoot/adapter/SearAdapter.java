package com.app.whoot.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.whoot.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Sunrise on 1/9/2019.
 *
 */

public class SearAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private int position = 0;
    private boolean islodingimg = true;
    Holder hold;

    public SearAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public SearAdapter(Context context, List<String> list,
                       boolean islodingimg) {
        this.context = context;
        this.list = list;
        this.islodingimg = islodingimg;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int arg0, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = View.inflate(context, R.layout.item_image_deme, null);
            hold = new Holder(view);
            view.setTag(hold);
        } else {
            hold = (Holder) view.getTag();
        }

        hold.txt.setText(list.get(arg0).toString());
        hold.txt.setTextColor(0xFF666666);
        if (arg0 == position) {
            hold.txt.setTextColor(0xFFFF8C00);
        }
        return view;
    }

    public void setSelectItem(int position) {
        this.position = position;
    }

    public int getSelectItem() {
        return position;
    }

    private static class Holder {
        TextView txt;

        public Holder(View view) {
            txt = (TextView) view.findViewById(R.id.image);

        }
    }

}
