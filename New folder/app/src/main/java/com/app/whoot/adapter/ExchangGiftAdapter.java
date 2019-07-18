package com.app.whoot.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.ExchBean;
import com.app.whoot.bean.GiftsExchangBean;
import com.app.whoot.util.TimeUtil;

/**
 * Created by Sunrise on 7/30/2018.
 */

public class ExchangGiftAdapter extends ListBaseAdapter<GiftsExchangBean> {
    private Context context;

    public ExchangGiftAdapter(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.exch_gift_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        GiftsExchangBean bean = mDataList.get(position);
        TextView exch_gift_item_name = holder.getView(R.id.exch_gift_item_name);
        TextView exch_gift_item_text = holder.getView(R.id.exch_gift_item_text);

        exch_gift_item_name.setText(bean.getShopName());
        String timedate = TimeUtil.timedateone(bean.getExpiredTm());
        exch_gift_item_text.setText(context.getResources().getString(R.string.expired)+": "+timedate);
    }
}
