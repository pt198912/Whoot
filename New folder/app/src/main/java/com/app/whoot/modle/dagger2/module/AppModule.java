package com.app.whoot.modle.dagger2.module;

//  Created by ruibing.han on 2018/3/29.

import com.app.whoot.app.MyApplication;
import com.app.whoot.modle.helper.RetrofitHelper;
import com.app.whoot.modle.http.api.AppService;
import com.app.whoot.modle.http.api.SubService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private MyApplication mApplication;

    public AppModule(MyApplication application) {
        this.mApplication = application;
    }

    @Singleton
    @Provides
    MyApplication provideApplication() {
        return mApplication;
    }

    //网络请求的帮助类
    @Singleton
    @Provides
    RetrofitHelper provideRetrofitHelper(AppService appService, SubService subService) {
        return new RetrofitHelper(appService, subService);
    }
}
