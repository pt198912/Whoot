package com.app.whoot.manager;

import com.app.whoot.bean.ShopBeanNew;

import java.util.ArrayList;
import java.util.List;

public class ShopBeanManager {
    private List<ShopBeanNew> mAllShops=new ArrayList<>();
    private ShopBeanManager() {
        if (SingletonHolder.instance != null) {
            throw new IllegalStateException();
        }
    }

    private static class SingletonHolder {
        private static ShopBeanManager instance = new ShopBeanManager();
    }

    public static ShopBeanManager getInstance() {
        return SingletonHolder.instance;
    }

    public void setAllShops(List<ShopBeanNew> allShops){
        mAllShops.clear();
        if(allShops!=null){
            mAllShops.addAll(allShops);
        }
    }
    public List<ShopBeanNew> getAllShops(){
        return mAllShops;
    }

}
