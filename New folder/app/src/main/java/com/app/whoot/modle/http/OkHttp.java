package com.app.whoot.modle.http;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.app.whoot.R;
import com.app.whoot.manager.ConfigureInfoManager;
import com.app.whoot.ui.activity.WindowActivity;
import com.app.whoot.ui.activity.login.LoginActivity;
import com.app.whoot.ui.activity.main.MainActivity;
import com.app.whoot.ui.view.dialog.CustomDialog;
import com.app.whoot.util.AppUtil;
import com.app.whoot.util.DateUtil;
import com.app.whoot.util.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
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
public class OkHttp {
    private static OkHttpClient client;
    private static Request request;
    private static Handler handler = new Handler();

    /**
     * OkHttpClient单例对象实例
     */
    public static OkHttpClient getClientInstance() {
        if (client == null) {
            synchronized (OkHttp.class) {
                if (client == null) {
                    client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS)

                            .build();
                }
            }
        }
        return client;
    }


    /**
     * OkHttp的get请求
     */
    public static void OkHttpGet(final Context context, String url, final OnDataFinish onDataFinish) {
        if(TextUtils.isEmpty(url)){
            return;
        }

        String Token = (String) SPUtil.get(context, "Token", "");


        //46c2d005ef284c0bb89504e3b4e774cb
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
    /*    if (!
                DateUtil.isNetworkAvailable(context)) {
            final CustomDialog.Builder builder = new CustomDialog.Builder(context);
            builder.setTitle(R.string.md_title);
            builder.setPositiveButton(R.string.md_sz, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
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
        } else {*/
            OkHttp.getClientInstance().newCall(request).enqueue(new Callback() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("http_g", "onFailure: "+e.toString());
                    if (context instanceof Activity) {
                        if (((Activity) context).isDestroyed() || ((Activity) context).isFinishing()) {
                            return;
                        }
                    }
                    e.printStackTrace();
                    if (e instanceof SocketTimeoutException) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onDataFinish.OnSocketTimeout();
                            }
                        });
                    }
                    if (e instanceof ConnectException) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onDataFinish.OnConnect();
                            }
                        });
                    }
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

                    Log.d("http_get",result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int code1 = jsonObject.getInt("code");
                        long serverTime=jsonObject.optLong("serverTime");
                        Log.d("http", "onResponse: serverTime "+serverTime);
                        ConfigureInfoManager.getInstance().setLastServerTime(serverTime);
                        ConfigureInfoManager.getInstance().setLastMobileTime(System.currentTimeMillis());
                        if (code1 == 5003) {
                                Intent intent = new Intent(context, WindowActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
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
       // }
    }


    /**
     * OkHttp的post请求
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void OkHttpPost(final Context context, String url, RequestBody body, final OnDataFinish onDataFinish) {
        if(TextUtils.isEmpty(url)){
            return;
        }

        String Token = (String) SPUtil.get(context, "Token", "");

        try{
            request = new Request.Builder()
                    .addHeader("whoot_token", Token)
                    .addHeader("client_version", AppUtil.getVersionName(context))
                    .url(url)
                    .post(body)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    /*    if (!DateUtil.isNetworkAvailable(context)) {
            final CustomDialog.Builder builder = new CustomDialog.Builder(context);
            builder.setTitle(R.string.md_title);
            builder.setPositiveButton(R.string.md_sz, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
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
        } else {*/
            OkHttp.getClientInstance().newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    if (context instanceof Activity) {
                        if (((Activity) context).isDestroyed() || ((Activity) context).isFinishing()) {
                            return;
                        }
                    }
                    e.printStackTrace();
                    if (e instanceof SocketTimeoutException) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onDataFinish.OnSocketTimeout();
                            }
                        });
                    }
                    if (e instanceof ConnectException) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onDataFinish.OnConnect();
                            }
                        });
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (context instanceof Activity) {
                        if (((Activity) context).isDestroyed() || ((Activity) context).isFinishing()) {
                            return;
                        }
                    }
                    final String result = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int code1 = jsonObject.getInt("code");
                        ConfigureInfoManager.getInstance().setLastServerTime(jsonObject.optLong("serverTime"));
                        ConfigureInfoManager.getInstance().setLastMobileTime(System.currentTimeMillis());
                        if (code1 == 5003) {
                            Intent intent = new Intent(context, WindowActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
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
    public static void OkHttpDelete(final Context context, String url, final OnDataFinish onDataFinish) {
        if(TextUtils.isEmpty(url)){
            return;
        }

        String Token = (String) SPUtil.get(context, "Token", "");


        try{
            request = new Request.Builder()
                    .addHeader("whoot_token", Token)
                    .addHeader("client_version", AppUtil.getVersionName(context))
                    .delete()
                    .url(url)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
   /*     if (!
                DateUtil.isNetworkAvailable(context)) {
            final CustomDialog.Builder builder = new CustomDialog.Builder(context);
            builder.setTitle(R.string.md_title);
            builder.setPositiveButton(R.string.md_sz, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
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
        } else {*/
            OkHttp.getClientInstance().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    e.printStackTrace();
                    if (e instanceof SocketTimeoutException) {

                    }
                    if (e instanceof ConnectException) {

                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
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
    public static void OkHttpPatch(final Context context, String url, RequestBody body, final OnDataFinish onDataFinish) {
        if(TextUtils.isEmpty(url)){
            return;
        }

        String Token = (String) SPUtil.get(context, "Token", "");


        try{
            request = new Request.Builder()
                    .addHeader("whoot_token", Token)
                    .addHeader("client_version", AppUtil.getVersionName(context))
                    .patch(body)
                    .url(url)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
       /* if (!
                DateUtil.isNetworkAvailable(context)) {
            final CustomDialog.Builder builder = new CustomDialog.Builder(context);
            builder.setTitle(R.string.md_title);
            builder.setPositiveButton(R.string.md_sz, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
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
        } else {*/
            OkHttp.getClientInstance().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    e.printStackTrace();
                    if (e instanceof SocketTimeoutException) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onDataFinish.OnSocketTimeout();
                            }
                        });
                    }
                    if (e instanceof ConnectException) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onDataFinish.OnConnect();
                            }
                        });
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
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

        void OnSocketTimeout();

        void OnConnect();
    }

}
