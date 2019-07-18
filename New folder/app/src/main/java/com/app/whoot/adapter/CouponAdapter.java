package com.app.whoot.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.CouBean;
import com.app.whoot.ui.view.custom.RotateTextView;
import com.app.whoot.util.TimeUtil;

import java.util.Date;

/**
 * Created by Sunrise on 6/21/2018.
 * 过期优惠卷
 */

public class CouponAdapter extends ListBaseAdapter<CouBean> {
    public CouponAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_coup;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        CouBean item = mDataList.get(position);
        ImageView item_coup_img = holder.getView(R.id.item_coup_img);
        TextView item_coup_text = holder.getView(R.id.item_coup_text);
        TextView item_coup_time = holder.getView(R.id.item_coup_time);
        RotateTextView item_coup_tag = holder.getView(R.id.item_coup_tag);
        long expiredTm = item.getExpiredTm();
        String timeFormatText = TimeUtil.timedateone(expiredTm);
        item_coup_time.setText(mContext.getResources().getString(R.string.expired)+": "+timeFormatText);
        item_coup_tag.setText(R.string.expired_yi);
        int couponType = item.getCouponId();
        if (couponType==1){
            item_coup_img.setBackgroundResource(R.drawable.expiredcoupons_icon_bronze);
            item_coup_text.setText(R.string.copper);
        }else if (couponType==2){
            item_coup_img.setBackgroundResource(R.drawable.expiredcoupons_icon_gold);
            item_coup_text.setText(R.string.silver);
        }else if (couponType==3){
            item_coup_img.setBackgroundResource(R.drawable.expiredcoupons_icon_silver);
            item_coup_text.setText(R.string.gold);
        }else if (couponType==4){
            item_coup_img.setBackgroundResource(R.drawable.expiredcoupons_icon_diamond);
            item_coup_text.setText(R.string.diamond);
        }


    }
}
