package com.app.whoot.base.fragment;


//  Created by ruibing.han on 2018/3/29.

import android.app.Activity;

import com.app.whoot.app.MyApplication;
import com.app.whoot.base.mvp.BaseView;
import com.app.whoot.modle.dagger2.component.DaggerFragmentComponent;
import com.app.whoot.modle.dagger2.component.FragmentComponent;
import com.app.whoot.modle.dagger2.module.FragmentModule;

public abstract class BaseDaggerFragment<A extends Activity> extends BaseFragment<A> implements BaseView {

    private FragmentComponent mFragmentComponent;

    protected FragmentComponent getFragmentComponent() {
        if (mFragmentComponent == null) {
            mFragmentComponent = DaggerFragmentComponent.builder()
                    .fragmentModule(new FragmentModule(this))
                    .appComponent(MyApplication.getAppComponent())
                    .build();
        }
        return mFragmentComponent;
    }

    @Override
    public void onViewCreatedInit() {

        onFragmentInject();//依赖注入

        onInitPagAndData();//初始化页面和数据
    }

    protected abstract void onFragmentInject();//将当前fragment注入到 FragmentComponent容器中 产生依赖

    protected abstract void onInitPagAndData();//初始化页面和数据
}
