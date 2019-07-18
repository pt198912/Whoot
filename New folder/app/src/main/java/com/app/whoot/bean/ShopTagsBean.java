package com.app.whoot.bean;

/**
 * Created by Sunrise on 1/22/2019.
 */

public class ShopTagsBean {

    /**
     * Id : 1
     * NameCh : 友善素食餐廳
     * NameEn : Vegan Friendly
     */

    private int Id;
    private String NameCh;
    private String NameEn;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getNameCh() {
        return NameCh;
    }

    public void setNameCh(String NameCh) {
        this.NameCh = NameCh;
    }

    public String getNameEn() {
        return NameEn;
    }

    public void setNameEn(String NameEn) {
        this.NameEn = NameEn;
    }
}
