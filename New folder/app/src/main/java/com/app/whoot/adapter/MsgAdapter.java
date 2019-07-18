package com.app.whoot.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.app.MyApplication;
import com.app.whoot.bean.ShopBean;
import com.app.whoot.bean.ShopBeanNew;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.view.RecycleViewDivider;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.ScreenUtils;
import com.app.whoot.util.TimeUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sunrise on 6/20/2018.
 */

public class MsgAdapter extends RecyclerView.Adapter<SuperViewHolder> {

    private int category1;
    private double price;
    private String s_xing;
    private String nameEn;
    private String name;
    private Context mContext;
    private List<ShopBeanNew> mDataList=new ArrayList<>();
    public MsgAdapter(Context context) {
        this.mContext = context;
    }
    public List<ShopBeanNew> getDataList(){
        return mDataList;
    }
    public void clear(){
        this.mDataList.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<ShopBeanNew> datas){
        this.mDataList.clear();
        if(datas!=null){
            this.mDataList.addAll(datas);
        }
        notifyDataSetChanged();
    }
    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(int pos);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private static final double EARTH_RADIUS = 6378137.0;
    // 返回单位是米
    public static double getDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.item_find_list,null);
        return new SuperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.rightMargin=ScreenUtils.dp2px(mContext,10);
        lp.leftMargin=ScreenUtils.dp2px(mContext,10);
        holder.itemView.setLayoutParams(lp);
        ShopBeanNew item = mDataList.get(position);
        RoundedImageView iv_logo = holder.getView(R.id.iv_store_logo);
        TextView frag_item_name = holder.getView(R.id.frag_item_name);
//        TextView frag_item_score = holder.getView(R.id.frag_item_score);
        ImageView item_ex_0 = holder.getView(R.id.item_ex_0);
        ImageView item_ex_1 = holder.getView(R.id.item_ex_1);
        ImageView item_ex_2 = holder.getView(R.id.item_ex_2);
        ImageView item_ex_3 = holder.getView(R.id.item_ex_3);
        ImageView item_ex_4 = holder.getView(R.id.item_ex_4);
        TextView frag_item_nameEn = holder.getView(R.id.frag_item_nameEn);
        TextView tvExchangeLabel=holder.getView(R.id.tv_exchange_label);
        RecyclerView exchanegRv=holder.getView(R.id.rv_exchange_goods);
        TextView frag_item_score_one = holder.getView(R.id.frag_item_score_one);
        TextView frag_item_distance = holder.getView(R.id.frag_item_distance);
        TextView frag_item_dish = holder.getView(R.id.frag_item_dish);
//        ImageView statusIv=holder.getView(R.id.iv_status);
//        TextView frag_item_money = holder.getView(R.id.frag_item_money);
        int priceLevel = item.getPriceLevel();
        try{
            name = item.getName();
            frag_item_name.setVisibility(View.VISIBLE);
        }catch (Exception e){
            name="";
            frag_item_name.setVisibility(View.GONE);
        }
        frag_item_name.setText(name);
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
        frag_item_nameEn.setText(nameEn);
        double distance = item.getDistance();
        if (distance>=1){
//            double v = distance / 1000;
            String s = DateUtil.roundByScale(distance, 1);
            frag_item_distance.setText(s+mContext.getResources().getString(R.string.km));
        }else {
            double v = distance * 1000;
            String of = String.valueOf(v);
            String[] split = of.split("\\.");
            frag_item_distance.setText(split[0]+mContext.getResources().getString(R.string.m));
        }

        TextView storeStatus = holder.getView(R.id.tv_store_status);
        TextView averageCostTv=holder.getView(R.id.tv_average_cost);
        averageCostTv.setText(String.format(mContext.getResources().getString(R.string.label_hkd_person),item.getAvgConsumptio()));
        if(item.getAvgConsumptio()==0){
            averageCostTv.setVisibility(View.GONE);
        }else{
            averageCostTv.setVisibility(View.VISIBLE);
        }
        int id = item.getId();
        int type = item.getType();
        int currency1 = item.getCurrency();
        try{
            price = item.getPrice();
        }catch (Exception e){

            price=0;
        }
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
                        int idValue=0;
                        try {
                            idValue = Integer.parseInt(id2);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Map<Integer,String> chineseSubDishMap=new HashMap<>();
                        Map<Integer,String> englishSubDishMap=new HashMap<>();
                        JSONArray sub = jsonObject1.getJSONArray("Sub");
                        for (int j=0;j<sub.length();j++){
                            JSONObject o = (JSONObject) sub.get(j);
                            int id1 = o.getInt("Id");
                            if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
                                chineseSubDishMap.put(id1,o.getString("NameCh"));
                            }else if (luchang.equals("3")){
                                englishSubDishMap.put(id1,o.getString("NameEn"));
                            }
                        }
                        MyApplication.getInstance().getChineseLanDishMap().put(idValue,chineseSubDishMap);
                        MyApplication.getInstance().getEnglishLanDishMap().put(idValue,englishSubDishMap);
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

                                        frag_item_dish.setText(o.getString("NameCh"));
                                    }else if (luchang.equals("3")){
                                        frag_item_dish.setText(o.getString("NameEn"));

                                    }

                                }

                            }

                        }
                    }
//                    JSONArray currency = data.getJSONArray("Currency");
//
//                    for (int k=0;k<currency.length();k++){
//                        JSONObject o = (JSONObject) currency.get(k);
//                        int id1 = o.getInt("Id");
//                        if (currency1==id1){
//                            frag_item_money.setText(o.getString("Name")+ price);
//                        }
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        int operatingStatus = item.getOperatingStatus();
        if (operatingStatus==1){
            storeStatus.setText(R.string.soon);
//            statusIv.setVisibility(View.VISIBLE);
            storeStatus.setVisibility(View.VISIBLE);
        }else if (operatingStatus==2){
            storeStatus.setText(R.string.closs_wei);
//            statusIv.setVisibility(View.VISIBLE);
            storeStatus.setVisibility(View.VISIBLE);
        }else if (operatingStatus==0){
            storeStatus.setText(R.string.open);
//            statusIv.setVisibility(View.GONE);
            storeStatus.setVisibility(View.GONE);
        }
        exchanegRv.setLayoutManager(new GridLayoutManager(mContext,2));
//        exchanegRv.addItemDecoration(new RecycleViewDivider(mContext,0, ScreenUtils.dp2px(mContext,10), Color.parseColor("#00000000")));
        exchanegRv.setAdapter(new HomeExchangeGoodsAdapter(mContext,item.getRedeem()));
        if(item.getRedeem()==null||item.getRedeem().size()==0){
            tvExchangeLabel.setVisibility(View.GONE);
        }else{
            tvExchangeLabel.setVisibility(View.VISIBLE);
        }
        Glide.with(mContext)
                .load(item.getShopLogo())
                .asBitmap()
                .thumbnail(0.1f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) //缓存
                .error(R.drawable.storedetail_topfive_blank_pic)
                .into(iv_logo);
        double grade = item.getGrade();

//        if (grade<=5&&grade>=4.5){
//            frag_item_score.setBackgroundResource(R.drawable.home_shape);
//        }else if (grade<4.5&&grade>=4){
//            frag_item_score.setBackgroundResource(R.drawable.home_shape_four);
//        }else if (grade<4&&grade>=3){
//            frag_item_score.setBackgroundResource(R.drawable.home_shape_three);
//        }else if (grade<3){
//            frag_item_score.setBackgroundResource(R.drawable.home_shape_twe);
//        }
//        frag_item_score.setText(grade+"");
        String substring = String.valueOf(grade).substring(0, 1);
        s_xing = DateUtil.formateRate(String.valueOf(grade));
        if (substring.equals("1")){
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
        }else if (substring.equals("2")){
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
        }else if (substring.equals("3")){
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
        }else if (substring.equals("4")){
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
        }else if (substring.equals("5")){
            item_ex_0.setBackgroundResource(R.drawable.icon_star_10);
            item_ex_1.setBackgroundResource(R.drawable.icon_star_10);
            item_ex_2.setBackgroundResource(R.drawable.icon_star_10);
            item_ex_3.setBackgroundResource(R.drawable.icon_star_10);
            item_ex_4.setBackgroundResource(R.drawable.icon_star_10);
        }else {
            item_ex_0.setBackgroundResource(R.drawable.icon_star_0);
            item_ex_1.setBackgroundResource(R.drawable.icon_star_0);
            item_ex_2.setBackgroundResource(R.drawable.icon_star_0);
            item_ex_3.setBackgroundResource(R.drawable.icon_star_0);
            item_ex_4.setBackgroundResource(R.drawable.icon_star_0);
        }
        frag_item_score_one.setVisibility(View.VISIBLE);
        frag_item_score_one.setText("("+item.getCommentCount()+" "+mContext.getResources().getString(R.string.home_item_fen));


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
