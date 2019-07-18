package com.app.whoot.bean;

/**
 * Created by Sunrise on 8/2/2018.
 */

public class GiftsRecordBean {


    /**
     * commodityId : 1
     * commodityImg : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3667342462,1407728484&fm=27&gp=0.jpg
     * commodityName : apple
     * createTm : 1533117727226
     * id : 6
     * locationId : 1
     * shopId : 1
     * shopName : aaa
     */

    private int commodityId;
    private String commodityImg;
    private String commodityName;
    private long createTm;
    private int id;
    private int locationId;
    private int shopId;
    private int commentId;
    private String shopName;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityImg() {
        return commodityImg;
    }

    public void setCommodityImg(String commodityImg) {
        this.commodityImg = commodityImg;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public long getCreateTm() {
        return createTm;
    }

    public void setCreateTm(long createTm) {
        this.createTm = createTm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
