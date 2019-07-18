package com.app.whoot.bean;

/**
 * Created by Sunrise on 8/2/2018.
 */

public class GiftsExchangBean {


    /**
     * expiredTm : 1532749124000
     * giftSn : 1530070724547493607
     * giftUseId : 2
     * id : 1
     * shopName : bbb
     * shopid : 1
     * type : 1
     * userId : 10496
     */

    private long expiredTm;
    private String giftSn;
    private int giftUseId;
    private int id;
    private String shopName;
    private int shopid;
    private int type;
    private int userId;

    public long getExpiredTm() {
        return expiredTm;
    }

    public void setExpiredTm(long expiredTm) {
        this.expiredTm = expiredTm;
    }

    public String getGiftSn() {
        return giftSn;
    }

    public void setGiftSn(String giftSn) {
        this.giftSn = giftSn;
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
