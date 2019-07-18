package com.app.whoot.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class NoVerticalScrollRecycleView extends RecyclerView {
    public NoVerticalScrollRecycleView(Context context) {
        super(context);
    }

    public NoVerticalScrollRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoVerticalScrollRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }
}
