package com.app.whoot.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.FavBean;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.UrlUtil;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Sunrise on 5/2/2018.
 */

public class PopAdapter extends BaseAdapter {

    private List<FavBean> list;
    private Context context;

    public PopAdapter(Context context, List<FavBean> list) {
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {

        return list.size();
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
            view = LayoutInflater.from(context).inflate(R.layout.pop_item, null);
            view.setTag(hodler);
        }else {
            hodler = (ViewHodler) view.getTag();
        }
        hodler.pop_item_img = (ImageView)view.findViewById(R.id.pop_item_img);
        hodler.pop_item_img0 = (ImageView)view.findViewById(R.id.pop_item_img0);
        hodler.pop_item_img1 = (ImageView)view.findViewById(R.id.pop_item_img1);
        hodler.pop_item_img2 = (ImageView)view.findViewById(R.id.pop_item_img2);
        hodler.pop_item_img3 = (ImageView)view.findViewById(R.id.pop_item_img3);
        hodler.pop_item_img4 = (ImageView)view.findViewById(R.id.pop_item_img4);
        hodler.pop_item_text=(TextView)view.findViewById(R.id.pop_item_text);
        hodler.pop_item_lod=(TextView)view.findViewById(R.id.pop_item_lod);

        Glide.with(context)
                .load(list.get(position).getImgUrl())
                .centerCrop()
                .skipMemoryCache(true)
                .into(hodler.pop_item_img);
        String grade = String.valueOf(list.get(position).getGrade());

        double distance = list.get(position).getDistance();
        if (distance>=1000){
            double v = distance / 1000;
            String s = DateUtil.roundByScale(v, 1);
            hodler.pop_item_lod.setText(s+context.getResources().getString(R.string.km));
        }else {
            String of = String.valueOf(distance);
            String[] split = of.split("\\.");
            hodler.pop_item_lod.setText(split[0]+context.getResources().getString(R.string.m));
        }

        String s_xing = DateUtil.formateRate(String.valueOf(grade));
        String substring = grade.substring(0, 1);
        if (substring.equals("1")){
            hodler.pop_item_img0.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                hodler.pop_item_img1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                hodler.pop_item_img1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                hodler.pop_item_img1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                hodler.pop_item_img1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                hodler.pop_item_img1.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                hodler.pop_item_img1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                hodler.pop_item_img1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                hodler.pop_item_img1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                hodler.pop_item_img1.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                hodler.pop_item_img1.setBackgroundResource(R.drawable.icon_star_0);
            }

            hodler.pop_item_img2.setBackgroundResource(R.drawable.icon_star_0);
            hodler.pop_item_img3.setBackgroundResource(R.drawable.icon_star_0);
            hodler.pop_item_img4.setBackgroundResource(R.drawable.icon_star_0);
        }else if (substring.equals("2")){
            hodler.pop_item_img0.setBackgroundResource(R.drawable.icon_star_10);
            hodler.pop_item_img1.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                hodler.pop_item_img2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                hodler.pop_item_img2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                hodler.pop_item_img2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                hodler.pop_item_img2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                hodler.pop_item_img2.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                hodler.pop_item_img2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                hodler.pop_item_img2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                hodler.pop_item_img2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                hodler.pop_item_img2.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                hodler.pop_item_img2.setBackgroundResource(R.drawable.icon_star_0);
            }

            hodler.pop_item_img3.setBackgroundResource(R.drawable.icon_star_0);
            hodler.pop_item_img4.setBackgroundResource(R.drawable.icon_star_0);
        }else if (substring.equals("3")){
            hodler.pop_item_img0.setBackgroundResource(R.drawable.icon_star_10);
            hodler.pop_item_img1.setBackgroundResource(R.drawable.icon_star_10);
            hodler.pop_item_img2.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                hodler.pop_item_img3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                hodler.pop_item_img3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                hodler.pop_item_img3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                hodler.pop_item_img3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                hodler.pop_item_img3.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                hodler.pop_item_img3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                hodler.pop_item_img3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                hodler.pop_item_img3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                hodler.pop_item_img3.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                hodler.pop_item_img3.setBackgroundResource(R.drawable.icon_star_0);
            }

            hodler.pop_item_img4.setBackgroundResource(R.drawable.icon_star_0);
        }else if (substring.equals("4")){
            hodler.pop_item_img0.setBackgroundResource(R.drawable.icon_star_10);
            hodler.pop_item_img1.setBackgroundResource(R.drawable.icon_star_10);
            hodler.pop_item_img2.setBackgroundResource(R.drawable.icon_star_10);
            hodler.pop_item_img3.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                hodler.pop_item_img4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                hodler.pop_item_img4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                hodler.pop_item_img4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                hodler.pop_item_img4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                hodler.pop_item_img4.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                hodler.pop_item_img4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                hodler.pop_item_img4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                hodler.pop_item_img4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                hodler.pop_item_img4.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                hodler.pop_item_img4.setBackgroundResource(R.drawable.icon_star_0);
            }

        }else if (substring.equals("5")){
            hodler.pop_item_img0.setBackgroundResource(R.drawable.icon_star_10);
            hodler.pop_item_img1.setBackgroundResource(R.drawable.icon_star_10);
            hodler.pop_item_img2.setBackgroundResource(R.drawable.icon_star_10);
            hodler.pop_item_img3.setBackgroundResource(R.drawable.icon_star_10);
            hodler.pop_item_img4.setBackgroundResource(R.drawable.icon_star_10);
        }else {
            hodler.pop_item_img0.setBackgroundResource(R.drawable.icon_star_0);
            hodler.pop_item_img1.setBackgroundResource(R.drawable.icon_star_0);
            hodler.pop_item_img2.setBackgroundResource(R.drawable.icon_star_0);
            hodler.pop_item_img3.setBackgroundResource(R.drawable.icon_star_0);
            hodler.pop_item_img4.setBackgroundResource(R.drawable.icon_star_0);
        }
        hodler.pop_item_text.setText(list.get(position).getName());
        return view;
    }

    class ViewHodler{
        ImageView pop_item_img,pop_item_img0,pop_item_img1,pop_item_img2,pop_item_img3,pop_item_img4;
        TextView pop_item_text,pop_item_lod;
    }
}
