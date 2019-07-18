package com.app.whoot.ui.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ListView;

public class AutoFitRecycleView extends RecyclerView {
    public AutoFitRecycleView(Context context) {
        super(context);
    }

    public AutoFitRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoFitRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, size);
    }
}
