package com.app.whoot.bean;

import android.support.annotation.NonNull;

/**
 * Created by Sunrise on 5/25/2018.
 * 兑换
 */

public class CardItemTopBean implements Comparable<CardItemTopBean>{


    /**
     * beginTm : 1520179200000
     * commodityId : 1
     * commodityName : apple
     * commodityPic : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3667342462,1407728484&fm=27&gp=0.jpg
     * costs : 333
     * couponId : 2
     * detail : 青涩的分
     *
     * endTm : 1525536000000
     * id : 2
     * maxUseAmout : 5
     * shopId : 1
     * usedAmout : 2
     * commodityDescribe: aaacccc
     */

    private long beginTm;
    private int commodityId;
    private String commodityName;
    private String commodityPic;
    private int costs;
    private int couponId;
    private String detail;
    private long endTm;
    private int id;
    private int maxUseAmout;
    private int shopId;
    private int usedAmout;
    private String commodityDescribe;
    private String nameEn;
    private int commodityActivityTag;

    public void setCommodityActivityTag(int commodityActivityTag) {
        this.commodityActivityTag = commodityActivityTag;
    }

    public int getCommodityActivityTag() {
        return commodityActivityTag;
    }

    public long getBeginTm() {
        return beginTm;
    }

    public void setBeginTm(long beginTm) {
        this.beginTm = beginTm;
    }
    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityPic() {
        return commodityPic;
    }

    public void setCommodityPic(String commodityPic) {
        this.commodityPic = commodityPic;
    }

    public int getCosts() {
        return costs;
    }

    public void setCosts(int costs) {
        this.costs = costs;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getEndTm() {
        return endTm;
    }

    public void setEndTm(long endTm) {
        this.endTm = endTm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxUseAmout() {
        return maxUseAmout;
    }

    public void setMaxUseAmout(int maxUseAmout) {
        this.maxUseAmout = maxUseAmout;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getUsedAmout() {
        return usedAmout;
    }

    public void setUsedAmout(int usedAmout) {
        this.usedAmout = usedAmout;
    }

    public String getCommodityDescribe() {
        return commodityDescribe;
    }

    public void setCommodityDescribe(String commodityDescribe) {
        this.detail = commodityDescribe;
    }

    @Override
    public int compareTo(@NonNull CardItemTopBean cardItemTopBean) {
        if(this.getCouponId()>cardItemTopBean.getCouponId())
        {
            return 1;
        }else if(this.getCouponId()<cardItemTopBean.getCouponId()){
            return -1;
        }
        return 0;
    }
}
