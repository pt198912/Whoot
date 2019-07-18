package com.app.whoot.bean;

import java.util.List;

/**
 * Created by Sunrise on 4/25/2018.
 */

public class ShopBean {


    /**
     * address : 你猜我地址在哪
     * category : 1
     * closeTm : 1524659008000
     * closingSoon : false
     * commentCount : 0
     * createTm : 1523846502495
     * currency : 1
     * distance : 7.0
     * grade : 5.0
     * id : 4
     * imgUrl : hahahahah
     * latitude : 22.535639
     * longitude : 114.052
     * merchantId : 1
     * name :  德州必胜客
     * openingTm : 1524619408000
     * price : 11.1
     * operatingStatus:2
     * tel : 7758258
     * type : 1
     * updateTm : 1523846502495
     * nameEn:Alibuda978
     * priceLevel
     */

    private String address;
    private int category;
    private long closeTm;
    private boolean closingSoon;
    private int commentCount;
    private long createTm;
    private int currency;
    private double distance;
    private double grade;
    private int id;
    private String imgUrl;
    private double latitude;
    private double longitude;
    private int merchantId;
    private String name;
    private long openingTm;
    private double price;
    private String tel;
    private int type;
    private long updateTm;
    private int operatingStatus;
    private String nameEn;
    private int priceLevel;
    private List<CardItemTopBean> goodsList;

    public List<CardItemTopBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<CardItemTopBean> goodsList) {
        this.goodsList = goodsList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
    public int getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }
    public long getCloseTm() {
        return closeTm;
    }

    public void setCloseTm(long closeTm) {
        this.closeTm = closeTm;
    }

    public boolean isClosingSoon() {
        return closingSoon;
    }

    public void setClosingSoon(boolean closingSoon) {
        this.closingSoon = closingSoon;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public long getCreateTm() {
        return createTm;
    }

    public void setCreateTm(long createTm) {
        this.createTm = createTm;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOpeningTm() {
        return openingTm;
    }

    public void setOpeningTm(long openingTm) {
        this.openingTm = openingTm;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public int getOperatingStatus() {
        return operatingStatus;
    }

    public void setOperatingStatus(int operatingStatus) {
        this.operatingStatus = operatingStatus;
    }
    public long getUpdateTm() {
        return updateTm;
    }

    public void setUpdateTm(long updateTm) {
        this.updateTm = updateTm;
    }
}
