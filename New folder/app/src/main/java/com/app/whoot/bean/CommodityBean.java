package com.app.whoot.bean;

public class CommodityBean {
    private String commodityPic;  //产品图片
    private String commodityName; //产品名字
    private String commodityNameEn; //产品名字
    private  String commodityDescribe; //商品描述

    private long couponLevel;//优惠券等级
    private int maxUseAmout ; //最大兑换
    private int usedAmout;    //已兑换
    private int costs; //数量
    private int shopId; //门店id
    private long commodityId; //商品id
    private String detail;//介绍

    public String getCommodityPic() {
        return commodityPic;
    }

    public void setCommodityPic(String commodityPic) {
        this.commodityPic = commodityPic;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityNameEn() {
        return commodityNameEn;
    }

    public void setCommodityNameEn(String commodityNameEn) {
        this.commodityNameEn = commodityNameEn;
    }

    public String getCommodityDescribe() {
        return commodityDescribe;
    }

    public void setCommodityDescribe(String commodityDescribe) {
        this.commodityDescribe = commodityDescribe;
    }

    public Long getCouponLevel() {
        return couponLevel;
    }

    public void setCouponLevel(long couponLevel) {
        this.couponLevel = couponLevel;
    }

    public Integer getMaxUseAmout() {
        return maxUseAmout;
    }

    public void setMaxUseAmout(int maxUseAmout) {
        this.maxUseAmout = maxUseAmout;
    }

    public int getUsedAmout() {
        return usedAmout;
    }

    public void setUsedAmout(int usedAmout) {
        this.usedAmout = usedAmout;
    }

    public int getCosts() {
        return costs;
    }

    public void setCosts(int costs) {
        this.costs = costs;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(long commodityId) {
        this.commodityId = commodityId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
