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
 * Created by Sunrise on 7/30/2018.
 */

public class GiftAlreadyAdapter extends ListBaseAdapter<GiftListBean> {

    private OnItemClickListener onItemClickListener;//定义的接口

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void SetOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
    public GiftAlreadyAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.gift_item_already;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        GiftListBean bean = mDataList.get(position);
        TextView gift_item_text = holder.getView(R.id.gift_item_text);
        TextView gift_item_name = holder.getView(R.id.gift_item_name);
        LinearLayout gift_item_dele = holder.getView(R.id.gift_item_dele);
        ImageView gift_item_img = holder.getView(R.id.gift_item_img);
        long expiredTm = bean.getExpiredTm();
        String shopName = bean.getShopName();
        String timedate = TimeUtil.timedateone(expiredTm);
        gift_item_text.setText(mContext.getResources().getString(R.string.expired_time)+timedate);
        gift_item_name.setText(shopName);
        String  luchang= (String) SPUtil.get(mContext, "luchang", "");
        boolean zh = TimeUtil.isZh(mContext);
        if (luchang.equals("0")){
            if (zh){
                luchang="3";
            }else {
                luchang="2";
            }
        }
        if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
            gift_item_img.setBackgroundResource(R.drawable.tag_expireds_fan);
        }else if (luchang.equals("3")){
            gift_item_img.setBackgroundResource(R.drawable.tag_expired_en);
        }
        gift_item_dele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }
}
