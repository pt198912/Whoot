package com.app.whoot.bean;

import java.util.List;

/**
 * Created by Sunrise on 5/25/2018.
 */

public class CardItemNoBean {

    /**
     * address : asd1q12a
     * category : 3
     * closeTm : 1524630208000
     * commentCount : 0
     * createTm : 1524542987161
     * currency : 111
     * grade : 3.8
     * hotCommodity : []
     * id : 2
     * imgUrl : 12551241
     * latitude : 22.2
     * longitude : 22.2
     * merchantId : 1
     * name : 肯德基
     * openingTm : 1524630208000
     * price : 12.0
     * redeem : []
     * tel : 12312512
     * type : 2
     * updateTm : 1524438404467
     */

    private String address;
    private int category;
    private long closeTm;
    private int commentCount;
    private long createTm;
    private int currency;
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
    private List<?> hotCommodity;
    private List<?> redeem;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public long getCloseTm() {
        return closeTm;
    }

    public void setCloseTm(long closeTm) {
        this.closeTm = closeTm;
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

    public long getUpdateTm() {
        return updateTm;
    }

    public void setUpdateTm(long updateTm) {
        this.updateTm = updateTm;
    }

    public List<?> getHotCommodity() {
        return hotCommodity;
    }

    public void setHotCommodity(List<?> hotCommodity) {
        this.hotCommodity = hotCommodity;
    }

    public List<?> getRedeem() {
        return redeem;
    }

    public void setRedeem(List<?> redeem) {
        this.redeem = redeem;
    }
}
