package com.app.whoot.ui.view.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.whoot.R;


/**
 * Created by Sunrise on 4/16/2018.
 * 页面为空的时候展示
 *
 */

public class EmptyView extends LinearLayout {
    public static final int[] IMGS = {R.drawable.blank_searchnone, R.drawable.blank_expiredcoupon, R.drawable.blank_myreview, R.drawable.blank_nopage};
    public static final String[] TIPS = {String.valueOf(R.string.not_result), "暂无可投项目", "您还没有相关的投资", "暂无可用红包"};
    ImageView mImg;
    TextView mTip;

    public EmptyView(Context context) {
        super(context);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.nyh_empty_view, this);
        mImg = findViewById(R.id.m_img);
        mTip = findViewById(R.id.m_tips);
    }

    /**
     * 空产品列表
     */
    public void showProductListEmpty() {
        mImg.setImageResource(IMGS[1]);
        mTip.setText(TIPS[1]);
    }

    /**
     * 空收益列表
     */
    public void showProfitListEmpty() {
        mImg.setImageResource(IMGS[2]);
        mTip.setText(TIPS[2]);
    }

    /**
     * 空红包列表
     */
    public void showRedPacketListEmpty() {
        mImg.setImageResource(IMGS[3]);
        mTip.setText(TIPS[3]);
    }
}
