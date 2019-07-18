package com.app.whoot.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.ExchBean;
import com.app.whoot.bean.GiftsRecordBean;
import com.app.whoot.ui.activity.CardItemActivity;
import com.app.whoot.ui.activity.MeCommentActivity;
import com.app.whoot.ui.activity.StoreDetailActivity;
import com.app.whoot.ui.activity.WriteActivity;
import com.app.whoot.util.Constants;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;

import java.util.Date;

/**
 * Created by Sunrise on 7/31/2018.
 */

public class GiftsAdapter extends ListBaseAdapter<GiftsRecordBean> {
    public GiftsAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.gifts_adapter;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        GiftsRecordBean item = mDataList.get(position);
        ImageView item_exch_img = holder.getView(R.id.item_exch_img);  //图片
        TextView item_exch_txt = holder.getView(R.id.item_exch_txt);   //数量
        TextView item_exch_text = holder.getView(R.id.item_exch_text); //店名
        TextView item_exch_tx = holder.getView(R.id.item_exch_tx);   //使用日期
        TextView item_exch_review = holder.getView(R.id.item_exch_review); //评价
        TextView item_exch_see = holder.getView(R.id.item_exch_see);     //查看评价
        TextView item_exch_cai = holder.getView(R.id.item_exch_cai);  //菜名

        item_exch_text.setText(item.getShopName());
        item_exch_cai.setText(item.getCommodityName());
        String timedate = TimeUtil.timedate(item.getCreateTm());
        item_exch_tx.setText(timedate);

        try {
            int commentId = item.getCommentId();
            if (commentId == 0) {
                item_exch_review.setVisibility(View.VISIBLE);
                item_exch_see.setVisibility(View.GONE);
            } else {
                item_exch_review.setVisibility(View.GONE);
                item_exch_see.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            item_exch_review.setVisibility(View.VISIBLE);
            item_exch_see.setVisibility(View.GONE);
        }

        int shopId = item.getShopId();
        //评价
        item_exch_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WriteActivity.class);
                intent.putExtra("gift_flag", "1");
                intent.putExtra("shopid", String.valueOf(shopId));
                intent.putExtra("commodityId", String.valueOf(item.getCommodityId()));
                intent.putExtra("shopName", item.getShopName());
                intent.putExtra("commodityName", item.getCommodityName());
                intent.putExtra("couponHistoryId", String.valueOf(item.getId()));

                mContext.startActivity(intent);
            }
        });
        //查看评论
        item_exch_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MeCommentActivity.class);
                intent.putExtra("gift_flag", "1");
                intent.putExtra("commentId", item.getCommentId() + "");
                mContext.startActivity(intent);

            }
        });

        item_exch_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.put(mContext, Constants.SP_KEY_STORE_DETAIL_FROM,Constants.GO_STORE_DETAIL_FROM_GIFT);
                Intent intent = new Intent(mContext, StoreDetailActivity.class);
                intent.putExtra("id", item.getShopId());
                Log.d("shopid",item.getShopId()+"");
                mContext.startActivity(intent);
            }
        });
    }
}
