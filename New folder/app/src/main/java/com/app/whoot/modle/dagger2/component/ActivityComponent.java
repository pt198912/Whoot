package com.app.whoot.modle.dagger2.component;

//  Created by ruibing.han on 2018/3/29.

import android.app.Activity;

import com.app.whoot.modle.dagger2.module.ActivityModule;
import com.app.whoot.modle.dagger2.scope.ActivityScope;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import dagger.Component;

@ActivityScope
@Component(modules = ActivityModule.class, dependencies = AppComponent.class)
public interface ActivityComponent {

    Activity getActivity();

    RxPermissions getRxPermissions();//动态权限

    void inject(LoginActivity loginActivity);

    void inject(MainActivity mainActivity);

}
