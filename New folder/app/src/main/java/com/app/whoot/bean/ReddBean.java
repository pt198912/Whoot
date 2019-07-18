package com.app.whoot.bean;

/**
 * Created by Sunrise on 6/13/2018.
 */

public class ReddBean {


    /**
     * describe : 永澤洋蔥頭
     * id : 11
     * imgUrl : http://whoot-1251007673.cosgz.myqcloud.com/upload/P-u_yxLxmNz3n0esEmI0aw==.jpeg
     * name : 永澤洋蔥頭
     * price : 52.0
     * shopId : 17
     */

    private String describe;
    private int id;
    private String imgUrl;
    private String name;
    private double price;
    private int shopId;

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }
}
