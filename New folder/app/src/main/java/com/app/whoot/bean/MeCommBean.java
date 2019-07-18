package com.app.whoot.bean;

/**
 * Created by Sunrise on 6/19/2018.
 */

public class MeCommBean {

    /**
     * code : 0
     * data : {"commented":false,"commodityId":1,"commodityName":"中文啊啊啊啊a","content":"gbnnjjjjj","couponId":1,"grade":5,"id":13,"imgUrl":"https://world-storage.whoot.com/ucabdD5DiSFkK4bxUlMb0g==.png, https://world-storage.whoot.com/Uea_nVomeeKvFnbpsRYKJQ==.png","locationId":1,"reply":"asdasdasd","replyTm":1553504853981,"shopGrade":0,"shopId":8,"shopImg":"https://hk-storage.whoot.com/DpcACTsBqfSXi5ueguV-QQ==.gif","shopName":"丫里不達97822","updateTm":1553502529359,"useCount":1,"userId":81115}
     * message : success
     * serverTime : 1553505291203
     */

    private int code;
    private DataBean data;
    private String message;
    private long serverTime;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public static class DataBean {
        /**
         * commented : false
         * commodityId : 1
         * commodityName : 中文啊啊啊啊a
         * content : gbnnjjjjj
         * couponId : 1
         * grade : 5
         * id : 13
         * imgUrl : https://world-storage.whoot.com/ucabdD5DiSFkK4bxUlMb0g==.png, https://world-storage.whoot.com/Uea_nVomeeKvFnbpsRYKJQ==.png
         * locationId : 1
         * reply : asdasdasd
         * replyTm : 1553504853981
         * shopGrade : 0.0
         * shopId : 8
         * shopImg : https://hk-storage.whoot.com/DpcACTsBqfSXi5ueguV-QQ==.gif
         * shopName : 丫里不達97822
         * updateTm : 1553502529359
         * useCount : 1
         * userId : 81115
         */

        private boolean commented;
        private int commodityId;
        private String commodityName;
        private String content;
        private int couponId;
        private int grade;
        private int id;
        private String imgUrl;
        private int locationId;
        private String reply;
        private long replyTm;
        private double shopGrade;
        private int shopId;
        private String shopImg;
        private String shopName;
        private long updateTm;
        private int useCount;
        private int userId;

        public boolean isCommented() {
            return commented;
        }

        public void setCommented(boolean commented) {
            this.commented = commented;
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

        public long getReplyTm() {
            return replyTm;
        }

        public void setReplyTm(long replyTm) {
            this.replyTm = replyTm;
        }

        public double getShopGrade() {
            return shopGrade;
        }

        public void setShopGrade(double shopGrade) {
            this.shopGrade = shopGrade;
        }

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public String getShopImg() {
            return shopImg;
        }

        public void setShopImg(String shopImg) {
            this.shopImg = shopImg;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public long getUpdateTm() {
            return updateTm;
        }

        public void setUpdateTm(long updateTm) {
            this.updateTm = updateTm;
        }

        public int getUseCount() {
            return useCount;
        }

        public void setUseCount(int useCount) {
            this.useCount = useCount;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
