package com.app.whoot.bean;

/**
 * Created by Sunrise on 5/25/2018.
 * Top
 */

public class RewardItemBean {


    /**
     * describe : aaa
     * id : 1
     * imgUrl : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3667342462,1407728484&fm=27&gp=0.jpg
     * name : apple
     * price : 12.2
     * shopId : 1
     * type : 1
     */

    private String describe;
    private int id;
    private String imgUrl;
    private String name;
    private double price;
    private int shopId;
    private String type;
    private int activityTag;

    public void setActivityTag(int activityTag) {
        this.activityTag = activityTag;
    }

    public int getActivityTag() {
        return activityTag;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
