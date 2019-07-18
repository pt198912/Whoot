package com.app.whoot.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.app.whoot.R;
import com.app.whoot.app.MyApplication;
import com.app.whoot.bean.ActivityBean;
import com.app.whoot.bean.CurrencyBean;
import com.app.whoot.bean.TouristsBean;
import com.app.whoot.manager.ConfigureInfoManager;
import com.app.whoot.modle.http.Http;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;

public class LoadCfgUtils {
    private static final String TAG = "LoadCfgUtils";
    public interface LoadCfgCallback{
        void loadCfgSuccess();
        void loadCfgFail();
    }
    public static void loadCfg(Context context,LoadCfgCallback callback){
        Http.OkHttpGet(context, UrlUtil.Release, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray versions = data.getJSONArray("Versions");
                    String newestVersion = data.getString("NewestVersion");
                    boolean upgradeNew = data.getBoolean("UpgradeNew");
                    SPUtil.put(context,"newestVersion",newestVersion);
                    SPUtil.put(context,"upgradeNew",upgradeNew);
                    String url=null;
                    String cfgURL=null;
                    for (int i = 0; i < versions.length(); i++) {
                        JSONObject jsonObject1 = versions.getJSONObject(i);
                        String version = jsonObject1.getString("Version");
                        String versionName = AppUtil.getVersionName(context);
                        if (versionName.equals(version)) {
                            url = jsonObject1.getString("URL");
                            cfgURL = jsonObject1.getString("CfgURL");
                            //test
                         //   url="http://172.16.47.75:8888/whoot/";   http://172.16.0.211:8082/
//                            cfgURL="https://qc.ingcreations.com/cfg/whoot/whootserv_dev";
                            //test end
                            SPUtil.put(context, "URL", url);
                            SPUtil.put(context,"cfgURL",cfgURL);
                            break;
                        }
                    }
                    if(TextUtils.isEmpty(url)){
                        if(callback!=null){
                            callback.loadCfgFail();
                        }
                        return;
                    }
                    loadConfigUrlInfo(cfgURL,callback);

                } catch (JSONException e) {
                    e.printStackTrace();
                    if(callback!=null){
                        callback.loadCfgFail();
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                if(callback!=null){
                    callback.loadCfgFail();
                }
            }

        });
    }

    private static void loadConfigUrlInfo(String cfgURL,LoadCfgCallback callback){

        if(TextUtils.isEmpty(cfgURL)){
            if(callback!=null){
                callback.loadCfgFail();
            }
            return;
        }
        Http.OkHttpGet(MyApplication.getInstance(), cfgURL, new Http.OnDataFinish() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject location = data.getJSONObject("Location");
                    JSONArray category = location.getJSONArray("Category");
                    String  luchang= (String) SPUtil.get(MyApplication.getInstance(),"luchang", "");
                    boolean zh = TimeUtil.isZh(MyApplication.getInstance());
                    if (luchang.equals("0")){
                        if (zh){
                            luchang="3";
                        }else {
                            luchang="2";
                        }
                    }
                    for (int i = 0; i <category.length() ; i++) {
                        JSONObject jsonObject1 = category.getJSONObject(i);
                        String id2 = jsonObject1.getString("Id");
                        int idValue=0;
                        try {
                            idValue = Integer.parseInt(id2);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Map<Integer,String> chineseSubDishMap=new HashMap<>();
                        Map<Integer,String> englishSubDishMap=new HashMap<>();
                        JSONArray sub = jsonObject1.getJSONArray("Sub");
                        for (int j=0;j<sub.length();j++){
                            JSONObject o = (JSONObject) sub.get(j);
                            int id1 = o.getInt("Id");
                            if (luchang.equals("1")||luchang.equals("2")||luchang.equals("0")){
                                chineseSubDishMap.put(id1,o.getString("NameCh"));
                            }else if (luchang.equals("3")){
                                englishSubDishMap.put(id1,o.getString("NameEn"));
                            }
                        }
                        MyApplication.getInstance().getChineseLanDishMap().put(idValue,chineseSubDishMap);
                        MyApplication.getInstance().getEnglishLanDishMap().put(idValue,englishSubDishMap);
                        ConfigureInfoManager.getInstance().getChineseLanDishMap().put(idValue,chineseSubDishMap);
                        ConfigureInfoManager.getInstance().getEnglishLanDishMap().put(idValue,englishSubDishMap);
                    }
                    JSONArray activitys = location.getJSONArray("activity");
                    for(int i=0;i<activitys.length();i++){
                        ActivityBean act=GSonUtil.parseGson(activitys.get(i).toString(), ActivityBean.class);
                        ConfigureInfoManager.getInstance().getActList().add(act);
                    }
                    JSONArray currencys = data.getJSONArray("Currency");
                    for(int i=0;i<currencys.length();i++){
                        CurrencyBean currencyBean=GSonUtil.parseGson(currencys.get(i).toString(), CurrencyBean.class);
                        ConfigureInfoManager.getInstance().getCurrencyBeanList().add(currencyBean);
                    }
                    if(callback!=null){
                        callback.loadCfgSuccess();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(callback!=null){
                        callback.loadCfgFail();
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                if(callback!=null){
                    callback.loadCfgFail();
                }
            }
        });
    }
}
