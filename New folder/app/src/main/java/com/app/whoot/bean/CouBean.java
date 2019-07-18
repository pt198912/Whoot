package com.app.whoot.bean;

/**
 * Created by Sunrise on 5/15/2018.
 */

public class CouBean {


    /**
     * couponId : 1
     * couponLocation : 1
     * couponSn : 1525770720634277777
     * couponType : 1
     * expiredTm : 1525770720634
     * id : 2
     * userId : 1
     */

    private int couponId;
    private int couponLocation;
    private String couponSn;
    private int couponType;
    private long expiredTm;
    private int id;
    private int userId;

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

    public long getExpiredTm() {
        return expiredTm;
    }

    public void setExpiredTm(long expiredTm) {
        this.expiredTm = expiredTm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
