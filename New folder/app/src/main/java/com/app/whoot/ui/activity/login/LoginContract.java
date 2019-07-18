package com.app.whoot.ui.activity.login;

//  Created by ruibing.han on 2018/3/29.

import com.app.whoot.base.mvp.BasePresenter;
import com.app.whoot.base.mvp.BaseView;

public interface LoginContract {

    interface View extends BaseView {

        void showData();
    }

    interface LoginPresenter extends BasePresenter<View> {

        void getData();
    }
}
