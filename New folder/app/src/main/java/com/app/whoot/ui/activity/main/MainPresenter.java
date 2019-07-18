package com.app.whoot.ui.activity.main;

//  Created by ruibing.han on 2018/3/29.

import com.app.whoot.modle.helper.RetrofitHelper;
import com.app.whoot.modle.http.response.BaseResponse;
import com.app.whoot.modle.rxjava.CommonSubscriber;
import com.app.whoot.modle.rxjava.RxJavaPresent;
import com.app.whoot.util.RxJavaUtil;

import java.util.HashMap;

import javax.inject.Inject;

public class MainPresenter extends RxJavaPresent<MainContract.View> implements MainContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    @Inject
    MainPresenter(RetrofitHelper retrofitHelper) {
        this.mRetrofitHelper = retrofitHelper;
    }

    @Override
    public void getData() {
        addSubscribe(
                mRetrofitHelper.login(new HashMap<String, String>())
                        .compose(RxJavaUtil.<BaseResponse<String>>rxSchedulerHelper())
                        .compose(RxJavaUtil.<String>handleBaseResponseResult())
                        .subscribeWith(new CommonSubscriber<String>(mView) {
                            @Override
                            public void onNext(String userCommand) {
                                mView.showData();
                            }
                        }));
    }
}
