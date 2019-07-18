package com.app.whoot.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sunrise on 5/15/2018.
 */

public class CardBean {


    /**
     * commodityId : 0
     * commodityName : string
     * content : string
     * couponId : 0
     * favShop : true
     * favShopId : 0
     * grade : 0
     * id : 0
     * imgUrl : string
     * locationId : 0
     * reply : string
     * replyTm : 2019-01-10T04:10:04.500Z
     * shopId : 0
     * shopName : string
     * title : string
     * updateTm : 2019-01-10T04:10:04.500Z
     * userId : 0
     * userName : string
     * userPhoto : string
     * uncommented: true
     */

    private int commodityId;
    private String commodityName;
    private String content;
    private int couponId;
    private boolean uncommented;
    private boolean favShop;
    private int favShopId;
    private int grade;
    private int id;
    private String imgUrl;
    private int locationId;
    private String reply;
    private String replyTm;
    private int shopId;
    private String shopName;
    private String title;
    private String updateTm;
    private int userId;
    private String userName;
    private String userPhoto;
    /**
     * couponHistory : {"commodityId":5,"commodityName":"月巴","couponId":63,"couponLevel":2,"id":"13","locationId":1,"shopId":8,"shopName":"丫里不達978","useCount":1,"useId":7,"useTm":1548407929251,"userId":93435}
     */

    private CouponHistoryBean couponHistory;


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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public boolean isFavShop() {
        return favShop;
    }

    public void setFavShop(boolean favShop) {
        this.favShop = favShop;
    }

    public boolean isUncommented() {
        return uncommented;
    }

    public void setUncommented(boolean uncommented) {
        this.uncommented = uncommented;
    }

    public int getFavShopId() {
        return favShopId;
    }

    public void setFavShopId(int favShopId) {
        this.favShopId = favShopId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
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

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getReplyTm() {
        return replyTm;
    }

    public void setReplyTm(String replyTm) {
        this.replyTm = replyTm;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateTm() {
        return updateTm;
    }

    public void setUpdateTm(String updateTm) {
        this.updateTm = updateTm;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public CouponHistoryBean getCouponHistory() {
        return couponHistory;
    }

    public void setCouponHistory(CouponHistoryBean couponHistory) {
        this.couponHistory = couponHistory;
    }


    public static class CouponHistoryBean {
        /**
         * commodityId : 5
         * commodityName : 月巴
         * couponId : 63
         * couponLevel : 2
         * id : 13
         * locationId : 1
         * shopId : 8
         * shopName : 丫里不達978
         * useCount : 1
         * useId : 7
         * useTm : 1548407929251
         * userId : 93435
         */

        @SerializedName("commodityId")
        private int commodityIdX;
        @SerializedName("commodityName")
        private String commodityNameX;
        @SerializedName("couponId")
        private int couponIdX;
        private int couponLevel;
        @SerializedName("id")
        private String idX;
        @SerializedName("locationId")
        private int locationIdX;
        @SerializedName("shopId")
        private int shopIdX;
        @SerializedName("shopName")
        private String shopNameX;
        private int useCount;
        private int useId;
        private long useTm;
        @SerializedName("userId")
        private int userIdX;

        public int getCommodityIdX() {
            return commodityIdX;
        }

        public void setCommodityIdX(int commodityIdX) {
            this.commodityIdX = commodityIdX;
        }

        public String getCommodityNameX() {
            return commodityNameX;
        }

        public void setCommodityNameX(String commodityNameX) {
            this.commodityNameX = commodityNameX;
        }

        public int getCouponIdX() {
            return couponIdX;
        }

        public void setCouponIdX(int couponIdX) {
            this.couponIdX = couponIdX;
        }

        public int getCouponLevel() {
            return couponLevel;
        }

        public void setCouponLevel(int couponLevel) {
            this.couponLevel = couponLevel;
        }

        public String getIdX() {
            return idX;
        }

        public void setIdX(String idX) {
            this.idX = idX;
        }

        public int getLocationIdX() {
            return locationIdX;
        }

        public void setLocationIdX(int locationIdX) {
            this.locationIdX = locationIdX;
        }

        public int getShopIdX() {
            return shopIdX;
        }

        public void setShopIdX(int shopIdX) {
            this.shopIdX = shopIdX;
        }

        public String getShopNameX() {
            return shopNameX;
        }

        public void setShopNameX(String shopNameX) {
            this.shopNameX = shopNameX;
        }

        public int getUseCount() {
            return useCount;
        }

        public void setUseCount(int useCount) {
            this.useCount = useCount;
        }

        public int getUseId() {
            return useId;
        }

        public void setUseId(int useId) {
            this.useId = useId;
        }

        public long getUseTm() {
            return useTm;
        }

        public void setUseTm(long useTm) {
            this.useTm = useTm;
        }

        public int getUserIdX() {
            return userIdX;
        }

        public void setUserIdX(int userIdX) {
            this.userIdX = userIdX;
        }
    }
}
