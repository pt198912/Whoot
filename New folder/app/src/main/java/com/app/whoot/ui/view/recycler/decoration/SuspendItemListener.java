package com.app.whoot.ui.view.recycler.decoration;

public interface SuspendItemListener {

    String getGroupName(int position);

    void onGroupClick(int position);
}
