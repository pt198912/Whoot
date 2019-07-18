package com.app.whoot.base.activity.mvp;

//  Created by ruibing.han on 2018/3/29.

import com.app.whoot.base.activity.dagger2.BaseDaggerFragmentActivity;
import com.app.whoot.base.mvp.BasePresenter;
import com.app.whoot.base.mvp.BaseView;

import javax.inject.Inject;

public abstract class BaseMvpFragmentActivity<T extends BasePresenter> extends BaseDaggerFragmentActivity implements BaseView {

    @Inject
    protected T mPresenter;

    @Override
    protected void onInitPageAndData() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }

        onCreateInitPageAndData();//页面进行初始化操作
    }

    protected abstract void onCreateInitPageAndData();//初始化页面和数据

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }
}
