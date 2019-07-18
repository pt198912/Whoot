package com.app.whoot.bean;

public class ExchangeCodeBean {
    private int couponId;
    private int couponLocation;
    private String couponSn;
    private int couponType;
    private long createTm;
    private long expiredTm;
    private int id;
    private int tokenFrom;
    private String desc;

    public void setTokenFrom(int tokenFrom) {
        this.tokenFrom = tokenFrom;
    }

    public int getTokenFrom() {
        return tokenFrom;
    }

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
}
