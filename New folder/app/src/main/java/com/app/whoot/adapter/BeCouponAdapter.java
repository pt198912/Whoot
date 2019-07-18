package com.app.whoot.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.GiftListBean;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;

/**
 * Created by Sunrise on 1/16/2019.
 */

public class BeCouponAdapter extends ListBaseAdapter<GiftListBean> {

    private OnItemClickListener onItemClickListener;//定义的接口

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void SetOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public BeCouponAdapter(Context context) {
        super(context);
        this.mContext=context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.coupon_item_be;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        GiftListBean bean = mDataList.get(position);
        TextView gift_item_text = holder.getView(R.id.gift_item_text);
        TextView gift_item_name = holder.getView(R.id.gift_item_name);
        LinearLayout becoupon_ly = holder.getView(R.id.becoupon_ly);
        ImageView becoupon_img = holder.getView(R.id.becoupon_img);
        ImageView gift_img = holder.getView(R.id.gift_img);
        long expiredTm = bean.getExpiredTm();
        int couponId = bean.getCouponId();
        String  luchang= (String) SPUtil.get(mContext, "luchang", "");
        boolean zh = TimeUtil.isZh(mContext);
        String timedate = TimeUtil.timedateone(expiredTm);

        if (luchang.equals("0")){
            if (zh){
                luchang="3";
            }else {
                luchang="2";
            }
        }
        if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
            becoupon_img.setBackgroundResource(R.drawable.tag_expireds_fan);
            gift_item_text.setText(mContext.getResources().getString(R.string.expired_time)+timedate);

        }else if (luchang.equals("3")){
            becoupon_img.setBackgroundResource(R.drawable.tag_expired_en);
            gift_item_text.setText(mContext.getResources().getString(R.string.expired_time)+"\n"+timedate);
        }
        if (couponId==1){
            gift_item_name.setText(mContext.getResources().getString(R.string.copper));
            gift_img.setBackgroundResource(R.drawable.expiredtoken_icon_bronze);
        }else if (couponId==2){
            gift_item_name.setText(mContext.getResources().getString(R.string.silver));
            gift_img.setBackgroundResource(R.drawable.expiredtoken_icon_silver);
        }else if (couponId==3){
            gift_item_name.setText(mContext.getResources().getString(R.string.gold));
            gift_img.setBackgroundResource(R.drawable.expiredtoken_icon_gold);
        }else if (couponId==4){
            gift_item_name.setText(mContext.getResources().getString(R.string.diamond));
            gift_img.setBackgroundResource(R.drawable.expiredtoken_icon_diamond);
        }

        becoupon_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });

    }
}
