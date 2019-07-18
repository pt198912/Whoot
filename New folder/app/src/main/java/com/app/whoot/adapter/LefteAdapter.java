package com.app.whoot.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.app.MyApplication;
import com.app.whoot.bean.ShopBean;
import com.app.whoot.ui.fragment.FindFragment;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sunrise on 1/9/2019.
 *
 */

public class LefteAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;
    private int position = 0;
    private boolean islodingimg = true;
    Holder hold;

    public LefteAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    public LefteAdapter(Context context, List<Map<String, String>> list,
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
        String luchang = (String) SPUtil.get(context, "luchang", "0");
        boolean zh = TimeUtil.isZh(context);
        if (luchang.equals("0")) {
            if (zh) {
                luchang = "3";
            } else {
                luchang = "2";
            }
        }
        if (luchang.equals("1") || luchang.equals("2") || luchang.equals("0")){
            hold.txt.setText(list.get(arg0).get("NameCh").toString());
        }else if (luchang.equals("3")) {
            hold.txt.setText(list.get(arg0).get("NameEn").toString());
        }
        int arg_lefte = (int) SPUtil.get(context, "arg_lefte", 0);
        hold.txt.setTextColor(0xFF666666);
        if (arg0 == arg_lefte) {
            hold.txt.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.main_gold));
            hold.image_ly.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else {
            hold.image_ly.setBackgroundColor(context.getResources().getColor(R.color.card_time));
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
        LinearLayout image_ly;

        public Holder(View view) {
            txt = (TextView) view.findViewById(R.id.image);
            image_ly=(LinearLayout)view.findViewById(R.id.image_ly);

        }
    }

}
