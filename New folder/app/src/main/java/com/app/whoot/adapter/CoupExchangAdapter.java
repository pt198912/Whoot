package com.app.whoot.adapter;

import android.content.Context;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.GiftListBean;
import com.app.whoot.util.TimeUtil;

/**
 * Created by Sunrise on 1/16/2019.
 */

public class CoupExchangAdapter extends ListBaseAdapter<GiftListBean> {
    public CoupExchangAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.coupon_item_exchang;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        GiftListBean bean = mDataList.get(position);
        TextView gift_item_text = holder.getView(R.id.gift_item_text);
        TextView gift_item_name = holder.getView(R.id.gift_item_name);
        long expiredTm = bean.getExpiredTm();
        String shopName = bean.getShopName();
        String timedate = TimeUtil.timedateone(expiredTm);
        gift_item_text.setText(mContext.getResources().getString(R.string.expired)+timedate);
        gift_item_name.setText(shopName);
    }
}
