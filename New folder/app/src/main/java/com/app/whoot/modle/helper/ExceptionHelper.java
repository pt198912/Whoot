package com.app.whoot.modle.helper;

//  Created by ruibing.han on 2018/3/29.

import android.content.Context;
import android.text.TextUtils;

import com.app.whoot.modle.http.exception.AppException;
import com.app.whoot.ui.view.custom.CustomToast;

//统一异常处理
public class ExceptionHelper {

    public static void handleException(Context context,AppException e) {
        if (e == null || TextUtils.isEmpty(e.getErrorCode())) {
            return;
        }
        switch (e.getErrorCode()) {
            case "3003":
                CustomToast.create(context).longShow(e.getMsg());
                break;
            case "10001":
                CustomToast.create(context).longShow(e.getMsg());
                break;
            default:
                CustomToast.create(context).longShow(e.getMsg());
                break;
        }
    }
}
