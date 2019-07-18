package com.app.whoot.util;

//  Created by ruibing.han on 2018/3/27.

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class GSonUtil {
    //屏蔽new 创建和反射创建
    private GSonUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static Gson gson;
    static Gson defaultGSon() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setDateFormat("yyyy/MM/dd HH:mm:ss")
                    .create();
        }
        return gson;
    }

    public static <T> T parseGson(String jsonStr, Type clas){
        try {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(jsonStr,clas);
        }catch (Exception e){
            Log.d("Exception_GsonUtil",e.toString());
            return null;
        }

    }



}
