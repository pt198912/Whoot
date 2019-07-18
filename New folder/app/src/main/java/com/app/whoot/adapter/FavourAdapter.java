package com.app.whoot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.FavourBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.FavourActivity;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;
import com.app.whoot.util.UrlUtil;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Sunrise on 1/16/2019.
 */

public class FavourAdapter extends ListBaseAdapter<FavourBean> {

    private String s_xing;

    private int priceLevel;
    private OnItemClickListener onItemClickListener;//定义的接口
    private static final int MYLIVE_MODE_CHECK = 0;
    int mEditMode = MYLIVE_MODE_CHECK;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void SetOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public FavourAdapter(Context context) {
        super(context);
        this.mContext=context;
    }
    @Override
    public int getLayoutId() {
        return R.layout.favour_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        FavourBean bean = mDataList.get(position);
        ImageView favour_img = holder.getView(R.id.favour_img);
        TextView favour_name = holder.getView(R.id.favour_name);
        TextView favour_item_score = holder.getView(R.id.favour_item_score);
        ImageView favour_ex_0 = holder.getView(R.id.favour_ex_0);
        ImageView favour_ex_1 = holder.getView(R.id.favour_ex_1);
        ImageView favour_ex_2 = holder.getView(R.id.favour_ex_2);
        ImageView favour_ex_3 = holder.getView(R.id.favour_ex_3);
        ImageView favour_ex_4 = holder.getView(R.id.favour_ex_4);
        TextView favour_ping = holder.getView(R.id.favour_ping);
        TextView favour_km = holder.getView(R.id.favour_km);
        TextView favour_cai = holder.getView(R.id.favour_cai);
        ImageView favour_mon_0 = holder.getView(R.id.favour_mon_0);
        ImageView favour_mon_1 = holder.getView(R.id.favour_mon_1);
        ImageView favour_mon_2 = holder.getView(R.id.favour_mon_2);
        ImageView favour_mon_3 = holder.getView(R.id.favour_mon_3);
        ImageView favour_mon_4 = holder.getView(R.id.favour_mon_4);
        ImageView favour_dele = holder.getView(R.id.favour_dele);
        LinearLayout favour_img_ly = holder.getView(R.id.favour_img_ly);
        ImageView favour_i = holder.getView(R.id.favour_i);
        TextView favour_item_score_too = holder.getView(R.id.favour_item_score_too);
        FavourBean.FavShopInfoBean favShopInfo = bean.getFavShopInfo();

        if (bean.isSelect()){
            favour_i.setVisibility(View.VISIBLE);

        }else {
            favour_i.setVisibility(View.GONE);

        }
        favour_dele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onItemClickListener.onItemClick(position);
                String urlUtil = (String) SPUtil.get(mContext, "URL", "");
                int id = mDataList.get(position).getId();
                Http.OkHttpDelete(mContext, urlUtil + UrlUtil.list_favshop + "?id=" + id, new Http.OnDataFinish() {
                    @Override
                    public void OnSuccess(String result) {
                        Log.d("shangchude",result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("code").equals("0")){

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

            }
        });
        Glide.with(mContext)
                .load(favShopInfo.getImgUrl())
                .centerCrop()
                .skipMemoryCache(true)
                .error(R.drawable.storedetail_topfive_blank_pic)
                .into(favour_img);

        favour_name.setText(favShopInfo.getName());
        double distance = favShopInfo.getDistance();
        if (distance>=1000){
            double v = distance / 1000;
            String s = DateUtil.roundByScale(v, 1);
            favour_km.setText(s+mContext.getResources().getString(R.string.km));
        }else {
            String of = String.valueOf(distance);
            String[] split = of.split("\\.");
            favour_km.setText(split[0]+mContext.getResources().getString(R.string.m));
        }
        double grade = Double.parseDouble(String.valueOf(favShopInfo.getGrade()));
        if (grade<=5&&grade>=4.5){
            favour_item_score.setBackgroundResource(R.drawable.home_shape);
            favour_item_score.setText(grade+"");
            favour_item_score_too.setVisibility(View.GONE);
            favour_item_score.setVisibility(View.VISIBLE);
        }else if (grade<4.5&&grade>=4){
            favour_item_score.setBackgroundResource(R.drawable.home_shape_four);
            favour_item_score.setText(grade+"");
            favour_item_score_too.setVisibility(View.GONE);
            favour_item_score.setVisibility(View.VISIBLE);
        }else if (grade<4&&grade>=3){
            favour_item_score.setBackgroundResource(R.drawable.home_shape_three);
            favour_item_score.setText(grade+"");
            favour_item_score_too.setVisibility(View.GONE);
            favour_item_score.setVisibility(View.VISIBLE);
        }else if (grade<3&&grade>=1){
            favour_item_score.setBackgroundResource(R.drawable.home_shape_twe);
            favour_item_score.setText(grade+"");
            favour_item_score_too.setVisibility(View.GONE);
            favour_item_score.setVisibility(View.VISIBLE);
        }else if (grade==0){

            favour_item_score.setVisibility(View.GONE);
            favour_item_score_too.setText(mContext.getResources().getString(R.string.not_rating));

        }


        String substring = String.valueOf(grade).substring(0, 1);
        s_xing = DateUtil.formateRate(String.valueOf(grade));
        if (substring.equals("1")){
            favour_ex_0.setBackgroundResource(R.drawable.icon_star_10);

            if (s_xing.equals("1")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                favour_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            }else {

                favour_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            }
            favour_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            favour_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
        }else if (substring.equals("2")){
            favour_ex_0.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                favour_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            }else {

                favour_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            }
            favour_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
        }else if (substring.equals("3")){
            favour_ex_0.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                favour_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            }else {

                favour_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            }

            favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
        }else if (substring.equals("4")){
            favour_ex_0.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            if (s_xing.equals("1")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("2")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("3")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("4")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            }else if (s_xing.equals("5")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_5);
            }else if (s_xing.equals("6")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("7")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("8")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_10);
            }else if (s_xing.equals("9")){
                favour_ex_4.setBackgroundResource(R.drawable.icon_star_10);
            }else {

                favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
            }
        }else if (substring.equals("5")){
            favour_ex_0.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            favour_ex_4.setBackgroundResource(R.drawable.icon_star_10);
        }else {
            favour_ex_0.setBackgroundResource(R.drawable.icon_star_0);
            favour_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            favour_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            favour_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            favour_ex_4.setBackgroundResource(R.drawable.icon_star_0);
        }
        int commentCount = favShopInfo.getCommentCount();
        if (commentCount==0){
            favour_ping.setVisibility(View.GONE);
        }else {
            favour_ping.setText("("+commentCount+" "+mContext.getResources().getString(R.string.home_item_fen));
        }

        int category1 = favShopInfo.getCategory();
        int type = favShopInfo.getType();
        String cfgURL = (String) SPUtil.get(mContext, "cfgURL", "");
        Http.OkHttpGet(mContext, cfgURL, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject location = data.getJSONObject("Location");
                    JSONArray category = location.getJSONArray("Category");
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
                        String id2 = jsonObject1.getString("Id");
                        if (id2.equals(String.valueOf(category1))){
                            JSONArray sub = jsonObject1.getJSONArray("Sub");
                            for (int j=0;j<sub.length();j++){
                                JSONObject o = (JSONObject) sub.get(j);
                                int id1 = o.getInt("Id");
                                if (type==id1){
                                    if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
                                        favour_cai.setText(o.getString("NameCh"));
                                    }else if (luchang.equals("3")){
                                        favour_cai.setText(o.getString("NameEn"));
                                    }

                                }
                            }
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
        try{
            priceLevel = favShopInfo.getPriceLevel();
        }catch (Exception e){
            priceLevel=0;
        }

        favour_img_ly.setVisibility(View.GONE);
        if (priceLevel==1){
            favour_mon_0.setBackgroundResource(R.drawable.home_icon_pricerange_s);
        }else if (priceLevel==2){
            favour_mon_0.setBackgroundResource(R.drawable.home_icon_pricerange_s);
            favour_mon_1.setBackgroundResource(R.drawable.home_icon_pricerange_s);
        }else if (priceLevel==3){
            favour_mon_0.setBackgroundResource(R.drawable.home_icon_pricerange_s);
            favour_mon_1.setBackgroundResource(R.drawable.home_icon_pricerange_s);
            favour_mon_2.setBackgroundResource(R.drawable.home_icon_pricerange_s);
        }else if (priceLevel==4){
            favour_mon_0.setBackgroundResource(R.drawable.home_icon_pricerange_s);
            favour_mon_1.setBackgroundResource(R.drawable.home_icon_pricerange_s);
            favour_mon_2.setBackgroundResource(R.drawable.home_icon_pricerange_s);
            favour_mon_3.setBackgroundResource(R.drawable.home_icon_pricerange_s);
        }else if (priceLevel==5){
            favour_mon_0.setBackgroundResource(R.drawable.home_icon_pricerange_s);
            favour_mon_1.setBackgroundResource(R.drawable.home_icon_pricerange_s);
            favour_mon_2.setBackgroundResource(R.drawable.home_icon_pricerange_s);
            favour_mon_3.setBackgroundResource(R.drawable.home_icon_pricerange_s);
            favour_mon_4.setBackgroundResource(R.drawable.home_icon_pricerange_s);
        }else {
            favour_img_ly.setVisibility(View.INVISIBLE);
        }
    }
    //点击item选中CheckBox
    public void setSelectItem(int position) {
        //对当前状态取反
        if (mDataList.get(position).isSelect()) {

            mDataList.get(position).setSelect(false);
        } else {
            mDataList.get(position).setSelect(true);
        }
        notifyItemChanged(position);
    }






}
