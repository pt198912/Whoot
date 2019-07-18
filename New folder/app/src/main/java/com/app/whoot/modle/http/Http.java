package com.app.whoot.modle.http;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;


import com.app.whoot.R;
import com.app.whoot.base.activity.BaseActivity;
import com.app.whoot.manager.ConfigureInfoManager;
import com.app.whoot.ui.activity.WindowActivity;
import com.app.whoot.ui.view.dialog.CustomDialog;
import com.app.whoot.util.AppUtil;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 创建人: 何成磊
 * 创建时间：16:25 2017/4/18 0018
 * 修改人：
 * 修改时间：
 * 描述：
 */
public class Http {
    private static OkHttpClient client;
    private static Request request;
    private static Handler handler = new Handler();
    private static final String TAG = "Http";
    /**
     * OkHttpClient单例对象实例
     */
    public static OkHttpClient getClientInstance() {
        if (client == null) {
            synchronized (Http.class) {
                if (client == null) {
                    client = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS)

                    .build();
                }
            }
        }
        return client;
    }
    /**
     * OkHttp的get请求
     */
    public static void OkHttpGet(final Context context, String url, final OnDataFinish onDataFinish){
        if(TextUtils.isEmpty(url)){
            return;
        }

        String Token = (String) SPUtil.get(context, "Token", "");


        Log.d(TAG,"wootToken:"+Token+",url "+url);
        try {
            request = new Request.Builder()
                    .addHeader("whoot_token", Token)
                    .addHeader("client_version", AppUtil.getVersionName(context))
                    .url(url)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
       /* if(!
        DateUtil.isNetworkAvailable(context)){
            final CustomDialog.Builder builder=new CustomDialog.Builder(context);
            builder.setTitle(R.string.md_title);
            builder.setPositiveButton(R.string.md_sz, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Intent intent=new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.cancel_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }else {*/
            Http.getClientInstance().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: "+e.toString());
                    e.printStackTrace();
                    if (context instanceof Activity) {
                        if (((Activity) context).isDestroyed() || ((Activity) context).isFinishing()) {
                            return;
                        }
                    }
                    if (e instanceof SocketTimeoutException){

                    }
                    if (e instanceof ConnectException){

                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onDataFinish.onError(e);
                        }
                    });

                }
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (context instanceof Activity) {
                        if (((Activity) context).isDestroyed() || ((Activity) context).isFinishing()) {
                            return;
                        }
                    }
                    final String result = response.body().string();
                    int code = response.code();
                    Log.d(TAG, "onResponse:"+result + "==" + code);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int code1 = jsonObject.getInt("code");
                        ConfigureInfoManager.getInstance().setLastServerTime(jsonObject.optLong("serverTime"));
                        ConfigureInfoManager.getInstance().setLastMobileTime(System.currentTimeMillis());
                        if (code1 == 5003) {
                            Intent intent = new Intent(context, WindowActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                          //  context.startActivity(intent);
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    onDataFinish.OnSuccess(result);


                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        //}
    }

    /**
     * OkHttp的post请求
     */
    public static void OkHttpPost(final Context context, String url, RequestBody body, final OnDataFinish onDataFinish) {
        if(TextUtils.isEmpty(url)){
            return;
        }

        String Token = (String) SPUtil.get(context, "Token", "");
        Log.d(TAG, "OkHttpPost: body "+body.toString());
        Log.d(TAG,"wootToken:"+Token+",url "+url);
        try{
            request = new Request.Builder()
                    .addHeader("whoot_token",Token)
                    .addHeader("client_version", AppUtil.getVersionName(context))
                    .url(url)
                    .post(body)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
       /* if(!DateUtil.isNetworkAvailable(context)){
            final CustomDialog.Builder builder=new CustomDialog.Builder(context);
            builder.setTitle(R.string.md_title);
            builder.setPositiveButton(R.string.md_sz, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Intent intent=new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.cancel_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }else {*/
            Http.getClientInstance().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: "+e.toString());
                    e.printStackTrace();
                    if (e instanceof SocketTimeoutException){

                    }
                    if (e instanceof ConnectException){

                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onDataFinish.onError(e);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        Log.d(TAG, "onResponse: "+result);
                        int code1 = jsonObject.getInt("code");
                        ConfigureInfoManager.getInstance().setLastServerTime(jsonObject.optLong("serverTime"));
                        ConfigureInfoManager.getInstance().setLastMobileTime(System.currentTimeMillis());
                        if (code1 == 5003) {
                            Intent intent = new Intent(context, WindowActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            context.startActivity(intent);
                         //   context.startActivity(intent);

                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    onDataFinish.OnSuccess(result);


                                }
                            });

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        //}
    }

    /**
     * OkHttp的post请求
     */
    public static void OkHttpPatc(final Context context, String url, RequestBody body, final OnDataFinish onDataFinish) {
        if(TextUtils.isEmpty(url)){
            return;
        }

        String Token = (String) SPUtil.get(context, "Token", "");

        Log.d(TAG,"wootToken:"+Token+",url "+url);
        try{
            request = new Request.Builder()
                    .addHeader("whoot_token",Token)
                    .addHeader("client_version", AppUtil.getVersionName(context))
                    .url(url)
                    .patch(body)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    /*    if(!DateUtil.isNetworkAvailable(context)){
            final CustomDialog.Builder builder=new CustomDialog.Builder(context);
            builder.setTitle(R.string.md_title);
            builder.setPositiveButton(R.string.md_sz, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Intent intent=new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.cancel_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }else {*/
            Http.getClientInstance().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: "+e.toString());
                    e.printStackTrace();
                    if (e instanceof SocketTimeoutException){

                    }
                    if (e instanceof ConnectException){

                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onDataFinish.onError(e);
                        }
                    });

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int code1 = jsonObject.getInt("code");
                        ConfigureInfoManager.getInstance().setLastServerTime(jsonObject.optLong("serverTime"));
                        ConfigureInfoManager.getInstance().setLastMobileTime(System.currentTimeMillis());
                        Log.d(TAG, "onResponse: "+result+","+code1);
                        if (code1 == 5003) {
                            Intent intent = new Intent(context, WindowActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         //   context.startActivity(intent);
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    onDataFinish.OnSuccess(result);
                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
        //}
    }
    /**
     * OkHttp的Delete请求
     */
    public static void OkHttpDelete(final Context context, String url, final OnDataFinish onDataFinish){
        if(TextUtils.isEmpty(url)){
            return;
        }

        String Token = (String) SPUtil.get(context, "Token", "");


        Log.d(TAG,"wootToken:"+Token+",url "+url);
        try{
            request = new Request.Builder()
                    .addHeader("whoot_token",Token)
                    .addHeader("client_version", AppUtil.getVersionName(context))
                    .delete()
                    .url(url)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
  /*      if(!
                DateUtil.isNetworkAvailable(context)){
            final CustomDialog.Builder builder=new CustomDialog.Builder(context);
            builder.setTitle(R.string.md_title);
            builder.setPositiveButton(R.string.md_sz, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Intent intent=new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.cancel_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }else {*/
            Http.getClientInstance().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: "+e.toString());
                    e.printStackTrace();
                    if (e instanceof SocketTimeoutException){

                    }
                    if (e instanceof ConnectException){

                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onDataFinish.onError(e);

                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
                    Log.d(TAG, "onResponse: "+result);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onDataFinish.OnSuccess(result);

                        }
                    });
                }
            });
        //}
    }
    /**
     * OkHttp的Delete请求
     */
    public static void OkHttpPatch(final Context context, String url,RequestBody body, final OnDataFinish onDataFinish){
        if(TextUtils.isEmpty(url)){
            return;
        }

        String Token = (String) SPUtil.get(context, "Token", "");



        try {
            request = new Request.Builder()
                    .addHeader("client_version", AppUtil.getVersionName(context))
                    .patch(body)
                    .url(url)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
      /*  if(!
                DateUtil.isNetworkAvailable(context)){
            final CustomDialog.Builder builder=new CustomDialog.Builder(context);
            builder.setTitle(R.string.md_title);
            builder.setPositiveButton(R.string.md_sz, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Intent intent=new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.cancel_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }else {*/
            Http.getClientInstance().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: "+e.toString());
                    e.printStackTrace();
                    if (e instanceof SocketTimeoutException){

                    }
                    if (e instanceof ConnectException){

                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onDataFinish.onError(e);

                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
                    Log.d(TAG, "onResponse: "+result);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onDataFinish.OnSuccess(result);

                        }
                    });
                }
            });
        //}
    }
    /**
     * OkHttp的post请求
     */
    public static void OkHttpPostOne(final Context context, String url, RequestBody body, final OnDataFinish onDataFinish) {
        if(TextUtils.isEmpty(url)){
            return;
        }
        try{
            request = new Request.Builder()
                    .addHeader("client_version", AppUtil.getVersionName(context))
                    .url(url)
                    .post(body)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
      /*  if(!DateUtil.isNetworkAvailable(context)){
            final CustomDialog.Builder builder=new CustomDialog.Builder(context);
            builder.setTitle(R.string.md_title);
            builder.setPositiveButton(R.string.md_sz, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Intent intent=new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.cancel_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }else {*/
            Http.getClientInstance().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: "+e.toString());
                    e.printStackTrace();
                    if (e instanceof SocketTimeoutException){

                    }
                    if (e instanceof ConnectException){

                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onDataFinish.onError(e);

                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
                    Log.d(TAG, "onResponse: "+result);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onDataFinish.OnSuccess(result);

                        }
                    });
                }
            });
        //}

    }



    /**
     * 回调接口
     */
    public interface OnDataFinish {
        void OnSuccess(String result);
        void onError(Exception e);
    }

}
