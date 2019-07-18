package com.app.whoot.base.mvp;

//  Created by ruibing.han on 2018/3/29.

import com.app.whoot.modle.http.exception.AppException;

public interface BaseView {

    void showErrorMsg(AppException e);
}
