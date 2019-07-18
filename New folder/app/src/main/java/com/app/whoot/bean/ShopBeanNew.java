package com.app.whoot.bean;

import android.support.annotation.NonNull;

import com.app.whoot.util.Constants;

import java.util.List;

import static com.app.whoot.util.Constants.SORT_TYPE.SORT_TYPE_DEFAULT;

public class ShopBeanNew implements Comparable<ShopBeanNew>{
    private String  address;
    private int category;
    private long closeTmFrist;
    private long closeTmSecond;
    private int commentCount;
    private long createTm;
    private int currency;
    private int deleteStatus;
    private String describe;
    private double distance;
    private double grade;
    private int id;
    private String imgUrl;
    private double latitude;
    private double longitude;
    private int merchantId;
    private String name;
    private String nameEn;
    private long openingTmFrist;
    private long openingTmSecond;
    private int operatingStatus;
    private double price;
    private int priceLevel;
    private String tags;
    private String tel;
    private int type;
    private long updateTm;

    private int avgConsumptio;
    private String shopLogo;
    private List<CardItemTopBean> redeem;


    public void setAvgConsumptio(int avgConsumptio) {
        this.avgConsumptio = avgConsumptio;
    }

    public int getAvgConsumptio() {
        return avgConsumptio;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public void setRedeem(List<CardItemTopBean> redeem) {
        this.redeem = redeem;
    }

    public List<CardItemTopBean> getRedeem() {
        return redeem;
    }

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

    public long getCloseTmFrist() {
        return closeTmFrist;
    }

    public void setCloseTmFrist(long closeTmFrist) {
        this.closeTmFrist = closeTmFrist;
    }

    public long getCloseTmSecond() {
        return closeTmSecond;
    }

    public void setCloseTmSecond(long closeTmSecond) {
        this.closeTmSecond = closeTmSecond;
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

    public int getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(int deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public long getOpeningTmFrist() {
        return openingTmFrist;
    }

    public void setOpeningTmFrist(long openingTmFrist) {
        this.openingTmFrist = openingTmFrist;
    }

    public long getOpeningTmSecond() {
        return openingTmSecond;
    }

    public void setOpeningTmSecond(long openingTmSecond) {
        this.openingTmSecond = openingTmSecond;
    }

    public int getOperatingStatus() {
        return operatingStatus;
    }

    public void setOperatingStatus(int operatingStatus) {
        this.operatingStatus = operatingStatus;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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

    public static Constants.SORT_TYPE sSortType=SORT_TYPE_DEFAULT;
    @Override
    public int compareTo(@NonNull ShopBeanNew shopBeanNew) {
        int compareRes=0;
        switch (sSortType){
            case SORT_TYPE_DEFAULT:
                if(this.getDistance()>shopBeanNew.getDistance())
                {
                    compareRes=1;
                }else if(this.getDistance()<shopBeanNew.getDistance()){
                    compareRes=-1;
                }else{
                    compareRes=0;
                }
                break;
            case SORT_TYPE_RATING:
                if(this.getGrade()>shopBeanNew.getGrade()){
                    compareRes=-1;
                }else if(this.getGrade()<shopBeanNew.getGrade()){
                    compareRes=1;
                }else{
                    compareRes=0;
                }
                break;
            case SORT_TYPE_OPEN_STATUS:
                if(this.getOperatingStatus()>shopBeanNew.getOperatingStatus()){
                    compareRes=1;
                }else if(this.getOperatingStatus()<shopBeanNew.getOperatingStatus()){
                    compareRes=-1;
                }else{
                    compareRes=0;
                }
                break;

        }
        return compareRes;
    }
}
