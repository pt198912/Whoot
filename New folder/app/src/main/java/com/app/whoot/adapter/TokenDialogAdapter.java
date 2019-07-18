package com.app.whoot.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;
import com.app.whoot.bean.CouponBean;
import com.app.whoot.ui.view.dialog.LoginCoupDialog;
import com.app.whoot.util.ScreenUtils;
import com.app.whoot.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class TokenDialogAdapter extends BaseAdapter {

    private List<CouponBean.DataBean.CouponsBean> followlist=new ArrayList<>();
    private Context context;
    private static final String TAG = "TokenDialogAdapter";
    public TokenDialogAdapter(Context context, List<CouponBean.DataBean.CouponsBean> followlist) {
        this.context=context;
        setData(followlist);
    }
    public void setData( List<CouponBean.DataBean.CouponsBean> followlist){
        this.followlist.clear();
        if(followlist!=null&&followlist.size()!=0) {
            this.followlist.addAll(followlist);
        }
        notifyDataSetChanged();
        Log.d(TAG, "setData: "+this.followlist.size());
    }
    @Override
    public int getCount() {
        return this.followlist.size();
    }

    @Override
    public Object getItem(int position) {
        return followlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CouponBean.DataBean.CouponsBean bean=followlist.get(position);
        ViewHodler hodler;
        Log.d(TAG, "getView: pos "+position);
        if (convertView==null){
            hodler=new ViewHodler();
            convertView= LayoutInflater.from(context).inflate(R.layout.token_item,null);
            convertView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dp2px(context,85)));
            hodler.tokenTypeIv=(ImageView)convertView.findViewById(R.id.iv_token_type);
            hodler.expireTimeTv=convertView.findViewById(R.id.tv_expire_time);
            hodler.rewardDescTv=convertView.findViewById(R.id.tv_reward_desc);
            convertView.setTag(hodler);
        }else {
            hodler= (ViewHodler) convertView.getTag();

        }
        Log.d(TAG, "getView: couponId "+bean.getCouponId());
        switch (bean.getCouponId()){
            case 1:
                hodler.tokenTypeIv.setImageResource(R.drawable.token_icon_bronze);
                break;
            case 2:
                hodler.tokenTypeIv.setImageResource(R.drawable.token_icon_silver);
                break;
            case 3:
                hodler.tokenTypeIv.setImageResource(R.drawable.token_icon_gold);
                break;
            case 4:
                hodler.tokenTypeIv.setImageResource(R.drawable.token_icon_diamond);
                break;
        }
        String expiredTm = TimeUtil.timedateone2(bean.getExpiredTm());
        String createTm=TimeUtil.timedateone2(bean.getCreateTm());
        Log.d(TAG, "getView: "+createTm+","+expiredTm+","+bean.getExpiredTm());
        if(bean.getCouponId()==1){
            hodler.expireTimeTv.setText(context.getResources().getString(R.string.label_no_expire_day));
        }else {
            hodler.expireTimeTv.setText(createTm + " to " + expiredTm);
        }
        hodler.rewardDescTv.setText(bean.getTokenFromDesc());
        return convertView;
    }

    class ViewHodler{
        private ImageView tokenTypeIv;
        private TextView expireTimeTv;
        private TextView rewardDescTv;
    }
}
