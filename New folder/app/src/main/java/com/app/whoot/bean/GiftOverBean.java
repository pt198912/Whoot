package com.app.whoot.bean;

/**
 * Created by Sunrise on 2/26/2019.
 */

public class GiftOverBean {

    /**
     * commodityId : 6
     * commodityImg : https://hk-storage.whoot.com/alcbn_AgZ1jxnbGhiMDGTg==.blob
     * commodityName : 阿六囖
     * createTm : 1551148047224
     * id : 14
     * locationId : 1
     * shopId : 12
     * commentId : 23
     * shopName : 风飞沙 笔记yh2
     */

    private int commodityId;
    private String commodityImg;
    private String commodityName;
    private long createTm;
    private int id;
    private int locationId;
    private int shopId;
    private String shopName;
    private int commentId;

    public int getCommentId(){
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
