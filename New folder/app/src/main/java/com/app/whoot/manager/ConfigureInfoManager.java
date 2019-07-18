package com.app.whoot.manager;

import com.app.whoot.bean.ActivityBean;
import com.app.whoot.bean.CurrencyBean;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

public class ConfigureInfoManager {
    private Map<Integer, Map<Integer,String>> mChineseLanDishMap=new HashMap<>();
    private Map<Integer,Map<Integer,String>> mEnglishLanDishMap=new HashMap<>();
    private List<ActivityBean> mActList=new ArrayList<>();
    private List<CurrencyBean> mCurrencyBeanList=new ArrayList<>();
    private long mLastServerTime;
    private long mLastMobileTime;
    private ConfigureInfoManager() {
        if (SingletonHolder.instance != null) {
            throw new IllegalStateException();
        }
    }

    private static class SingletonHolder {
        private static ConfigureInfoManager instance = new ConfigureInfoManager();
    }

    public void setLastServerTime(long lastServerTime) {
        if(lastServerTime!=0) {
            this.mLastServerTime = lastServerTime;
        }
    }

    public long getLastServerTime() {
        return mLastServerTime;
    }

    public void setLastMobileTime(long lastMobileTime) {
        this.mLastMobileTime = lastMobileTime;
    }

    public long getLastMobileTime() {
        return mLastMobileTime;
    }

    public static ConfigureInfoManager getInstance() {
        return SingletonHolder.instance;
    }
    public Map<Integer, Map<Integer, String>> getChineseLanDishMap() {
        return mChineseLanDishMap;
    }

    public Map<Integer, Map<Integer, String>> getEnglishLanDishMap() {
        return mEnglishLanDishMap;
    }

    public void setChineseLanDishMap(Map<Integer, Map<Integer, String>> mChineseLanDishMap) {
        this.mChineseLanDishMap = mChineseLanDishMap;
    }

    public void setEnglishLanDishMap(Map<Integer, Map<Integer, String>> mEnglishLanDishMap) {
        this.mEnglishLanDishMap = mEnglishLanDishMap;
    }

    public void setActList(List<ActivityBean> mActList) {
        this.mActList = mActList;
    }

    public List<ActivityBean> getActList() {
        return mActList;
    }

    public void setCurrencyBeanList(List<CurrencyBean> currencyBeanList) {
        this.mCurrencyBeanList = currencyBeanList;
    }

    public List<CurrencyBean> getCurrencyBeanList() {
        return mCurrencyBeanList;
    }
}
