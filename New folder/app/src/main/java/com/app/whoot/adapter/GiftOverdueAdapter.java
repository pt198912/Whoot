package com.app.whoot.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.GiftListBean;
import com.app.whoot.bean.GiftOverBean;
import com.app.whoot.modle.http.Http;
import com.app.whoot.ui.activity.MeCommentActivity;
import com.app.whoot.ui.activity.WriteActivity;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;

/**
 * Created by Sunrise on 7/30/2018.
 */

public class GiftOverdueAdapter extends ListBaseAdapter<GiftOverBean> {

    private OnItemClickListener onItemClickListener;//定义的接口

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void SetOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
    public GiftOverdueAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.gift_item_overdue;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        GiftOverBean bean = mDataList.get(position);
        TextView gift_item_text = holder.getView(R.id.gift_item_text);
        TextView gift_item_name = holder.getView(R.id.gift_item_name);
        TextView item_overdue_txt = holder.getView(R.id.item_overdue_txt);
        TextView item_overdue_review = holder.getView(R.id.item_overdue_review);
        TextView item_overdue_see = holder.getView(R.id.item_overdue_see);
        TextView gift_item_na = holder.getView(R.id.gift_item_na);
        long expiredTm = bean.getCreateTm();
        String shopName = bean.getShopName();
        String timela = (String) SPUtil.get(mContext, "timeLag", "0");
        long aLong = Long.parseLong(timela);
        String timedate = TimeUtil.timeUTCone(expiredTm+aLong);


        gift_item_text.setText(mContext.getResources().getString(R.string.redemp)+timedate);
        gift_item_name.setText(shopName);
        gift_item_na.setText(bean.getCommodityName());

        try{
            int commentId = bean.getCommentId();

            if (commentId==0){
                item_overdue_review.setVisibility(View.VISIBLE);
                item_overdue_see.setVisibility(View.GONE);
                item_overdue_txt.setVisibility(View.INVISIBLE);
            }else {
                item_overdue_review.setVisibility(View.GONE);
                item_overdue_see.setVisibility(View.VISIBLE);
                item_overdue_txt.setVisibility(View.INVISIBLE);
            }

        }catch (Exception e){
            item_overdue_review.setVisibility(View.VISIBLE);
            item_overdue_see.setVisibility(View.GONE);
            item_overdue_txt.setVisibility(View.INVISIBLE);
        }
        item_overdue_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WriteActivity.class);
                intent.putExtra("shopid",String.valueOf(bean.getShopId()));
                intent.putExtra("commodityId",String.valueOf(bean.getCommodityId()));
                intent.putExtra("shopName",bean.getShopName());
                intent.putExtra("commodityName",bean.getCommodityName());
                intent.putExtra("couponHistoryId",String.valueOf(bean.getId()));
                intent.putExtra("gift_flag","ooo");
                intent.putExtra("Excoupen",1);
                mContext.startActivity(intent);
            }
        });
        item_overdue_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,MeCommentActivity.class);
                intent.putExtra("commentId",bean.getCommentId()+"");
                intent.putExtra("gift_flag",1+"");
                intent.putExtra("flag_cou",1);
                mContext.startActivity(intent);
            }
        });
    }
}
