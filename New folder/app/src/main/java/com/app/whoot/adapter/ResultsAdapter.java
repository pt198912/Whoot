package com.app.whoot.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;

import com.app.whoot.bean.ShopBean;
import com.app.whoot.bean.ShopBeanNew;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.CardItemActivity;
import com.app.whoot.ui.activity.ResultsActivity;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.UrlUtil;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sunrise on 7/2/2018.
 */

public class ResultsAdapter extends ListBaseAdapter<ShopBeanNew> {

    private Context context;
    private String name;
    private int id;
    private double price;
    private int category1;
    private int type;
    private String nameEn;

    public ResultsAdapter(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_expandable_lv0;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ShopBeanNew item = mDataList.get(position);
        ImageView iv_head = holder.getView(R.id.iv_head);
        TextView frag_item_name = holder.getView(R.id.frag_item_name);
        TextView frag_item_score = holder.getView(R.id.frag_item_score);
        ImageView item_ex_0 = holder.getView(R.id.item_ex_0);
        ImageView item_ex_1 = holder.getView(R.id.item_ex_1);
        ImageView item_ex_2 = holder.getView(R.id.item_ex_2);
        ImageView item_ex_3 = holder.getView(R.id.item_ex_3);
        ImageView item_ex_4 = holder.getView(R.id.item_ex_4);
        TextView frag_item_soon = holder.getView(R.id.frag_item_soon);
        TextView frag_item_nameEn = holder.getView(R.id.frag_item_nameEn);
        TextView frag_item_score_one = holder.getView(R.id.frag_item_score_one);
        TextView frag_item_distance = holder.getView(R.id.frag_item_distance);
        TextView frag_item_dish = holder.getView(R.id.frag_item_dish);
        TextView frag_item_money = holder.getView(R.id.frag_item_money);
        LinearLayout msg_img_ly = holder.getView(R.id.msg_img_ly);
        ImageView msg_mon_0 = holder.getView(R.id.msg_mon_0);
        ImageView msg_mon_1 = holder.getView(R.id.msg_mon_1);
        ImageView msg_mon_2 = holder.getView(R.id.msg_mon_2);
        ImageView msg_mon_3 = holder.getView(R.id.msg_mon_3);
        ImageView msg_mon_4 = holder.getView(R.id.msg_mon_4);
        name = item.getName();
        id = item.getId();


        int currency1 = item.getCurrency();
        Glide.with(context)
                .load(item.getImgUrl())
                .centerCrop()
                .skipMemoryCache(true)
                .into(iv_head);
        int priceLevel = item.getPriceLevel();
        msg_img_ly.setVisibility(View.VISIBLE);

        if (priceLevel==1){
            msg_mon_0.setVisibility(View.VISIBLE);
            msg_mon_1.setVisibility(View.GONE);
            msg_mon_2.setVisibility(View.GONE);
            msg_mon_3.setVisibility(View.GONE);
            msg_mon_4.setVisibility(View.GONE);
        }else if (priceLevel==2){
            msg_mon_0.setVisibility(View.VISIBLE);
            msg_mon_1.setVisibility(View.VISIBLE);
            msg_mon_2.setVisibility(View.GONE);
            msg_mon_3.setVisibility(View.GONE);
            msg_mon_4.setVisibility(View.GONE);
        }else if (priceLevel==3){
            msg_mon_0.setVisibility(View.VISIBLE);
            msg_mon_1.setVisibility(View.VISIBLE);
            msg_mon_2.setVisibility(View.VISIBLE);
            msg_mon_3.setVisibility(View.GONE);
            msg_mon_4.setVisibility(View.GONE);
        }else if (priceLevel==4){
            msg_mon_0.setVisibility(View.VISIBLE);
            msg_mon_1.setVisibility(View.VISIBLE);
            msg_mon_2.setVisibility(View.VISIBLE);
            msg_mon_3.setVisibility(View.VISIBLE);
            msg_mon_4.setVisibility(View.GONE);
        }else if (priceLevel==5){
            msg_mon_0.setVisibility(View.VISIBLE);
            msg_mon_1.setVisibility(View.VISIBLE);
            msg_mon_2.setVisibility(View.VISIBLE);
            msg_mon_3.setVisibility(View.VISIBLE);
            msg_mon_4.setVisibility(View.VISIBLE);
        }else {
            msg_img_ly.setVisibility(View.INVISIBLE);
        }
        int operatingStatus = item.getOperatingStatus();
        if (operatingStatus==1){
            frag_item_soon.setBackgroundResource(R.drawable.home_shape_soon);
            frag_item_soon.setText(R.string.soon);
        }else if (operatingStatus==2){
            frag_item_soon.setBackgroundResource(R.drawable.home_shape_close);
            frag_item_soon.setText(R.string.closs_wei);
        }else if (operatingStatus==0){
            frag_item_soon.setBackgroundResource(R.drawable.home_shape_open);
            frag_item_soon.setText(R.string.open);
        }
        frag_item_soon.getBackground().mutate().setAlpha(200);
        try {
            nameEn = item.getNameEn();
            if (nameEn==null||nameEn.length()==0){
                frag_item_nameEn.setVisibility(View.GONE);
            }else {
                frag_item_nameEn.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            frag_item_nameEn.setVisibility(View.GONE);
            nameEn="";
        }

        frag_item_name.setText(name);
        frag_item_nameEn.setText(nameEn);
        String cfgURL = (String) SPUtil.get(mContext, "cfgURL", "");
        Http.OkHttpGet(mContext, cfgURL, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject location = data.getJSONObject("Location");
                    JSONArray category = location.getJSONArray("Category");
                    category1 = item.getCategory();
                    type = item.getType();
                    String  luchang= (String) SPUtil.get(mContext, "luchang", "");
                    boolean zh = TimeUtil.isZh(mContext);
                    if (luchang.equals("0")){
                        if (zh){
                            luchang="3";
                        }else {
                            luchang="2";
                        }
                    }
                    for (int i = 0; i <category.length() ; i++) {
                        JSONObject jsonObject1 = category.getJSONObject(i);
                        int id2 = jsonObject1.getInt("Id");
                        if (id2==category1){
                            JSONArray sub = jsonObject1.getJSONArray("Sub");
                            for (int j=0;j<sub.length();j++){
                                JSONObject o = (JSONObject) sub.get(j);
                                int id1 = o.getInt("Id");
                                if (type ==id1){
                                    if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
                                        frag_item_dish.setText(o.getString("NameCh"));
                                    }else if (luchang.equals("3")){
                                        frag_item_dish.setText(o.getString("NameEn"));
                                    }

                                }
                            }
                        }
                    }
                    JSONArray currency = data.getJSONArray("Currency");

                    for (int k=0;k<currency.length();k++){
                        JSONObject o = (JSONObject) currency.get(k);
                        int id1 = o.getInt("Id");
                        if (currency1==id1){
                            frag_item_money.setText(o.getString("Name")+ price);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        double distance = item.getDistance();

        if (distance>=1000){
            double v = distance / 1000;
            String s = DateUtil.roundByScale(v, 1);
            frag_item_distance.setText(s+mContext.getResources().getString(R.string.km));
        }else {
            String of = String.valueOf(distance);
            String[] split = of.split("\\.");
            frag_item_distance.setText(split[0]+mContext.getResources().getString(R.string.m));
        }

        price = item.getPrice();
        frag_item_money.setText("$" + price);
        double grade = item.getGrade();
        frag_item_score.setText(grade + "");
        if (grade <= 5 && grade >= 4.5) {
            frag_item_score.setBackgroundResource(R.drawable.home_shape);
        } else if (grade < 4.5 && grade >= 4) {
            frag_item_score.setBackgroundResource(R.drawable.home_shape_four);
        } else if (grade < 4 && grade >= 3) {
            frag_item_score.setBackgroundResource(R.drawable.home_shape_three);
        } else if (grade < 3) {
            frag_item_score.setBackgroundResource(R.drawable.home_shape_twe);
        }

        String substring = String.valueOf(grade).substring(0, 1);
        String s_xing = DateUtil.formateRate(String.valueOf(grade));
        if (substring.equals("1")) {
            item_ex_0.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                item_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                item_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                item_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                item_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                item_ex_1.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                item_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                item_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                item_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                item_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                item_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            }

            item_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            item_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            item_ex_4.setBackgroundResource(R.drawable.icon_star_0);
        } else if (substring.equals("2")) {
            item_ex_0.setBackgroundResource(R.drawable.icon_star_10);
            item_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                item_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                item_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                item_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                item_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                item_ex_2.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                item_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                item_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                item_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                item_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                item_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            }

            item_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            item_ex_4.setBackgroundResource(R.drawable.icon_star_0);
        } else if (substring.equals("3")) {
            item_ex_0.setBackgroundResource(R.drawable.icon_star_10);
            item_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            item_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                item_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                item_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                item_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                item_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                item_ex_3.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                item_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                item_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                item_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                item_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                item_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            }

            item_ex_4.setBackgroundResource(R.drawable.icon_star_0);
        } else if (substring.equals("4")) {
            item_ex_0.setBackgroundResource(R.drawable.icon_star_10);
            item_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            item_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            item_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                item_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                item_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                item_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                item_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                item_ex_4.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                item_ex_4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                item_ex_4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                item_ex_4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                item_ex_4.setBackgroundResource(R.drawable.icon_star_10);
            }else {
                item_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            }

        } else if (substring.equals("5")) {
            item_ex_0.setBackgroundResource(R.drawable.icon_star_10);
            item_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            item_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            item_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            item_ex_4.setBackgroundResource(R.drawable.icon_star_10);
        } else {
            item_ex_0.setBackgroundResource(R.drawable.icon_star_0);
            item_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            item_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            item_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            item_ex_4.setBackgroundResource(R.drawable.icon_star_0);
        }

        try {
            if (item.getCommentCount()==0){
                frag_item_score_one.setVisibility(View.INVISIBLE);
            }else {
                frag_item_score_one.setText("(" + item.getCommentCount()+"  " + String.format(context.getString(R.string.home_item_fen)));
            }

        } catch (Exception e) {

        }
    }
}
