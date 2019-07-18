package com.app.whoot.bean;

/**
 * Created by Sunrise on 1/8/2019.
 */

public class SignBean {

    /**
     * couponId : 1
     * couponLocation : 1
     * couponSn : 1563177751611849701
     * couponType : 1
     * createTm : 1563177751611
     * id : 5462
     * tokenFrom : 3
     */

    private int couponId;
    private int couponLocation;
    private String couponSn;
    private int couponType;
    private long createTm;
    private int id;
    private int tokenFrom;
    private String desc;

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public int getCouponLocation() {
        return couponLocation;
    }

    public void setCouponLocation(int couponLocation) {
        this.couponLocation = couponLocation;
    }

    public String getCouponSn() {
        return couponSn;
    }

    public void setCouponSn(String couponSn) {
        this.couponSn = couponSn;
    }

    public int getCouponType() {
        return couponType;
    }

    public void setCouponType(int couponType) {
        this.couponType = couponType;
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

    public int getTokenFrom() {
        return tokenFrom;
    }

    public void setTokenFrom(int tokenFrom) {
        this.tokenFrom = tokenFrom;
    }
}
