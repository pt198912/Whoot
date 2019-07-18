package com.app.whoot.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.util.SPUtil;

/**
 * Created by Sunrise on 1/18/2019.
 */

public class RightAdapter extends BaseAdapter {
    private Context context;
    private String[] text_list;
    private int position = 0;
    Holder hold;

    public RightAdapter(Context context, String[] text_list) {
        this.context = context;
        this.text_list = text_list;
    }

    public int getCount() {
        return text_list.length;
    }

    public Object getItem(int position) {
        return text_list[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int arg0, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = View.inflate(context, R.layout.item_classify_morelist, null);
            hold = new Holder(view);
            view.setTag(hold);
        } else {
            hold = (Holder) view.getTag();
        }
        hold.txt.setText(text_list[arg0]);
        hold.txt.setTextColor(0xFF666666);
        int arg_right = (int) SPUtil.get(context, "arg_right", 1000);
        int arg_lefte = (int) SPUtil.get(context, "arg_lefte", 0);
        String arg_right_name = (String) SPUtil.get(context, "arg_right_name", "");
        if (arg_right_name.equals(text_list[arg0])){
            hold.txt.setTextColor(context.getResources().getColor(R.color.main_gold));
        }else {

        }
        if (arg_right==arg0){

        }

        return view;
    }

    public void setSelectItem(int position) {
        this.position = position;
    }

    private static class Holder {
        TextView txt;

        public Holder(View view) {
            txt = (TextView) view.findViewById(R.id.moreitem_txt);
        }
    }
}
