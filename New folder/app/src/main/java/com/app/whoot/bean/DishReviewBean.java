package com.app.whoot.bean;

import java.util.ArrayList;
import java.util.List;

public class DishReviewBean {
    private List<RewardItemBean> rewardItemBeanList=new ArrayList<>();//兑换
    private List<CardItemTopBean> recommendList=new ArrayList<>();//推荐
    private List<CardBean> reviewList=new ArrayList<>();//评论
    private List<String> storeImages=new ArrayList<>();
    private CardBean.CouponHistoryBean couponHistoryBean;
    private int brozenTokenNum=-1;
    private int silverTokenNum=-1;
    private int goldTokenNum=-1;
    private int diamongTokenNum=-1;
    private String couponSnBrozen;
    private int couponIdBrozen;
    private int couponTypeBrozen;
    private String couponSnSilver;
    private int couponIdSilver;
    private int couponTypeSilver;
    private String couponSnGold;
    private int couponIdGold;
    private int couponTypeGold;
    private String couponSnDiamond;
    private int couponIdDiamond;
    private int couponTypeDiamond;
    private double grade;
    private String substring;
    private String pingfen = "0";
    private int shopId;
    int commentCount;
    boolean uncommented;
    int openStatus;
    int avgConsumptio;
    long closeTmFrist;
    long closeTmSecond;
    long openTmFirst;
    long openTmSecond;
    public int getAvgConsumptio() {
        return avgConsumptio;
    }

    public void setAvgConsumptio(int avgConsumptio) {
        this.avgConsumptio = avgConsumptio;
    }

    public long getCloseTmFrist() {
        return closeTmFrist;
    }

    public void setCloseTmFrist(long closeTmFrist) {
        this.closeTmFrist = closeTmFrist;
    }

    public long getCloseTmSecond() {
        return closeTmSecond;
    }

    public long getOpenTmFirst() {
        return openTmFirst;
    }

    public void setOpenTmFirst(long openTmFirst) {
        this.openTmFirst = openTmFirst;
    }

    public long getOpenTmSecond() {
        return openTmSecond;
    }

    public void setOpenTmSecond(long openTmSecond) {
        this.openTmSecond = openTmSecond;
    }

    public void setCloseTmSecond(long closeTmSecond) {
        this.closeTmSecond = closeTmSecond;
    }

    public void setRecommendList(List<CardItemTopBean> recommendList) {
        this.recommendList = recommendList;
    }

    public List<RewardItemBean> getRewardItemBeanList() {
        return rewardItemBeanList;
    }

    public void setRewardItemBeanList(List<RewardItemBean> rewardItemBeanList) {
        this.rewardItemBeanList = rewardItemBeanList;
    }

    public List<CardItemTopBean> getRecommendList() {
        return recommendList;
    }

    public List<CardBean> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<CardBean> reviewList) {
        this.reviewList = reviewList;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getSubstring() {
        return substring;
    }

    public void setSubstring(String substring) {
        this.substring = substring;
    }

    public String getPingfen() {
        return pingfen;
    }

    public void setPingfen(String pingfen) {
        this.pingfen = pingfen;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public void setUncommented(boolean uncommented) {
        this.uncommented = uncommented;
    }

    public boolean isUncommented() {
        return uncommented;
    }

    public void setOpenStatus(int openStatus) {
        this.openStatus = openStatus;
    }

    public int getOpenStatus() {
        return openStatus;
    }

    public void setStoreImages(List<String> storeImages) {
        this.storeImages = storeImages;
    }

    public List<String> getStoreImages() {
        return storeImages;
    }

    public int getBrozenTokenNum() {
        return brozenTokenNum;
    }

    public void setBrozenTokenNum(int brozenTokenNum) {
        this.brozenTokenNum = brozenTokenNum;
    }

    public int getSilverTokenNum() {
        return silverTokenNum;
    }

    public void setSilverTokenNum(int silverTokenNum) {
        this.silverTokenNum = silverTokenNum;
    }

    public int getGoldTokenNum() {
        return goldTokenNum;
    }

    public void setGoldTokenNum(int goldTokenNum) {
        this.goldTokenNum = goldTokenNum;
    }

    public int getDiamongTokenNum() {
        return diamongTokenNum;
    }

    public void setDiamongTokenNum(int diamongTokenNum) {
        this.diamongTokenNum = diamongTokenNum;
    }

    public String getCouponSnBrozen() {
        return couponSnBrozen;
    }

    public void setCouponSnBrozen(String couponSnBrozen) {
        this.couponSnBrozen = couponSnBrozen;
    }

    public int getCouponIdBrozen() {
        return couponIdBrozen;
    }

    public void setCouponIdBrozen(int couponIdBrozen) {
        this.couponIdBrozen = couponIdBrozen;
    }

    public int getCouponTypeBrozen() {
        return couponTypeBrozen;
    }

    public void setCouponTypeBrozen(int couponTypeBrozen) {
        this.couponTypeBrozen = couponTypeBrozen;
    }

    public String getCouponSnSilver() {
        return couponSnSilver;
    }

    public void setCouponSnSilver(String couponSnSilver) {
        this.couponSnSilver = couponSnSilver;
    }

    public int getCouponIdSilver() {
        return couponIdSilver;
    }

    public void setCouponIdSilver(int couponIdSilver) {
        this.couponIdSilver = couponIdSilver;
    }

    public int getCouponTypeSilver() {
        return couponTypeSilver;
    }

    public void setCouponTypeSilver(int couponTypeSilver) {
        this.couponTypeSilver = couponTypeSilver;
    }

    public String getCouponSnGold() {
        return couponSnGold;
    }

    public void setCouponSnGold(String couponSnGold) {
        this.couponSnGold = couponSnGold;
    }

    public int getCouponIdGold() {
        return couponIdGold;
    }

    public void setCouponIdGold(int couponIdGold) {
        this.couponIdGold = couponIdGold;
    }

    public int getCouponTypeGold() {
        return couponTypeGold;
    }

    public void setCouponTypeGold(int couponTypeGold) {
        this.couponTypeGold = couponTypeGold;
    }

    public String getCouponSnDiamond() {
        return couponSnDiamond;
    }

    public void setCouponSnDiamond(String couponSnDiamond) {
        this.couponSnDiamond = couponSnDiamond;
    }

    public int getCouponIdDiamond() {
        return couponIdDiamond;
    }

    public void setCouponIdDiamond(int couponIdDiamond) {
        this.couponIdDiamond = couponIdDiamond;
    }

    public int getCouponTypeDiamond() {
        return couponTypeDiamond;
    }

    public void setCouponTypeDiamond(int couponTypeDiamond) {
        this.couponTypeDiamond = couponTypeDiamond;
    }

    public void setCouponHistoryBean(CardBean.CouponHistoryBean couponHistoryBean) {
        this.couponHistoryBean = couponHistoryBean;
    }

    public CardBean.CouponHistoryBean getCouponHistoryBean() {
        return couponHistoryBean;
    }
}
