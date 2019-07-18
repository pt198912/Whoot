package com.app.whoot.bean;

/**
 * Created by Sunrise on 8/1/2018.
 */

public class GiftListBean {


    /**
     * expiredTm : 1533263648000
     * giftSn : 1530070724547493605
     * giftUseId : 1
     * id : 2
     * shopName : aaa
     * shopid : 1
     * type : 1
     * userId : 10496
     * "couponId":3
     */

    private long expiredTm;
    private String giftSn;
    private int giftUseId;
    private int id;
    private String shopName;
    private int shopid;
    private int type;
    private int userId;
    private int couponId;
    private String commodityName;

    public long getExpiredTm() {
        return expiredTm;
    }

    public void setExpiredTm(long expiredTm) {
        this.expiredTm = expiredTm;
    }
    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }
    public String getGiftSn() {
        return giftSn;
    }

    public void setGiftSn(String giftSn) {
        this.giftSn = giftSn;
    }
    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }
    public int getGiftUseId() {
        return giftUseId;
    }

    public void setGiftUseId(int giftUseId) {
        this.giftUseId = giftUseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getShopid() {
        return shopid;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
