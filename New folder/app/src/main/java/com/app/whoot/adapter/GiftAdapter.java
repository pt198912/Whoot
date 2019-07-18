package com.app.whoot.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.ExchBean;
import com.app.whoot.bean.GiftListBean;
import com.app.whoot.ui.activity.CardItemActivity;
import com.app.whoot.util.SPUtil;
import com.app.whoot.util.TimeUtil;

/**
 * Created by Sunrise on 7/30/2018.
 */

public class GiftAdapter extends ListBaseAdapter<GiftListBean> {
    public GiftAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.gift_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        GiftListBean bean = mDataList.get(position);
        TextView gift_item_text = holder.getView(R.id.gift_item_text);
        TextView gift_item_name = holder.getView(R.id.gift_item_name);
        TextView gift_item_shopname = holder.getView(R.id.gift_item_shopname);

        long expiredTm = bean.getExpiredTm();
        String shopName = bean.getShopName();
        String timela = (String) SPUtil.get(mContext, "timeLag", "0");
        long aLong = Long.parseLong(timela);

        String timedate = TimeUtil.timeUTCone(expiredTm+aLong);
        gift_item_shopname.setText(bean.getCommodityName());
        gift_item_text.setText(mContext.getResources().getString(R.string.expired_ri)+": "+timedate);
        gift_item_name.setText(shopName);
    }
}
