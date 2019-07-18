package com.app.whoot.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.ExchBean;
import com.app.whoot.ui.activity.CardItemActivity;
import com.app.whoot.ui.activity.ExchangeActivity;
import com.app.whoot.ui.activity.MeCommentActivity;
import com.app.whoot.ui.activity.StoreDetailActivity;
import com.app.whoot.ui.activity.WriteActivity;
import com.app.whoot.util.Constants;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;

import java.util.Date;

/**
 * Created by Sunrise on 6/21/2018.
 */

public class ExchangeAdapter extends ListBaseAdapter<ExchBean> {

    public ExchangeAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_exchange;
    }
    /**
     * use git and optional unix tools from the command prompt
     * git from the command line and also from 3rd-party software
     * */

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ExchBean item = mDataList.get(position);
        ImageView item_exch_img = holder.getView(R.id.item_exch_img);  //图片
        TextView item_exch_txt = holder.getView(R.id.item_exch_txt);   //数量
        TextView item_exch_text = holder.getView(R.id.item_exch_text); //店名
        TextView item_exch_tx = holder.getView(R.id.item_exch_tx);   //使用日期
        TextView item_exch_review = holder.getView(R.id.item_exch_review); //评价
        TextView item_exch_see = holder.getView(R.id.item_exch_see);     //查看评价
        TextView item_exch_cai = holder.getView(R.id.item_exch_cai);  //菜名
        TextView item_exch_write = holder.getView(R.id.item_exch_write);

        int couponId = item.getCouponLevel();
        if (couponId==1){
            item_exch_img.setBackgroundResource(R.drawable.usedtoken_icon_bronze);
        }else if (couponId==2){
            item_exch_img.setBackgroundResource(R.drawable.usedtoken_icon_silver);
        }else if (couponId==3){
            item_exch_img.setBackgroundResource(R.drawable.usedtoken_icon_gold);
        }else if (couponId==4){
            item_exch_img.setBackgroundResource(R.drawable.usedtoken_icon_diamond);
        }
        int useCount = item.getUseCount();
        item_exch_txt.setText("X"+useCount);
        String commodityName = item.getShopName();
        item_exch_text.setText(commodityName);
        String timeFormatText = TimeUtil.timedateone(item.getUseTm());




        item_exch_cai.setText(item.getCommodityName());

        item_exch_tx.setText(mContext.getResources().getString(R.string.redemp)+timeFormatText);
        try{
            int commentId = item.getCommentId();

            if (commentId==0){
                item_exch_review.setVisibility(View.VISIBLE);  //评论
                item_exch_see.setVisibility(View.GONE);
                //item_exch_write.setVisibility(View.VISIBLE);
            }else {
                item_exch_review.setVisibility(View.GONE);
                item_exch_see.setVisibility(View.VISIBLE);   //查看评论
                //item_exch_write.setVisibility(View.INVISIBLE);
            }

        }catch (Exception e){
            item_exch_review.setVisibility(View.VISIBLE);
            item_exch_see.setVisibility(View.GONE);
        }



        int shopId = item.getShopId();
        //评价
        item_exch_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WriteActivity.class);
                intent.putExtra("shopid",String.valueOf(shopId));
                intent.putExtra("couponId",String.valueOf(couponId));
                intent.putExtra("commodityId",String.valueOf(item.getCommodityId()));
                intent.putExtra("shopName",item.getShopName());
                intent.putExtra("commodityName",item.getCommodityName());
                intent.putExtra("couponHistoryId",String.valueOf(item.getId()));
                intent.putExtra("useCount",String.valueOf(item.getUseCount()));
                intent.putExtra("Excoupen",1);
                mContext.startActivity(intent);
            }
        });
        //查看评论
        item_exch_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,MeCommentActivity.class);
                intent.putExtra("commentId",item.getCommentId()+"");
                intent.putExtra("useCount",useCount+"");
                intent.putExtra("flag_cou",1);
                mContext.startActivity(intent);

            }
        });
        item_exch_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.put(mContext, Constants.SP_KEY_STORE_DETAIL_FROM,Constants.GO_STORE_DETAIL_FROM_USED_TOKEN);
                Intent intent = new Intent(mContext, StoreDetailActivity.class);
                intent.putExtra("id", item.getShopId());
                Log.d("shopid",item.getShopId()+"");
                mContext.startActivity(intent);
            }
        });
    }
}
