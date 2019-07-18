package com.app.whoot.bean;

import java.util.List;

/**
 * Created by Sunrise on 4/20/2018.
 */

public class CouponBean {


    /**
     * code : 0
     * data : {"couponPieces":[{"amount":1,"couponId":4,"id":2,"updateTm":1525770441325,"userId":1},{"amount":1,"couponId":3,"id":3,"updateTm":1525770441325,"userId":1},{"amount":3,"couponId":2,"id":4,"updateTm":1525770441325,"userId":1},{"amount":3,"couponId":1,"id":5,"updateTm":1525770441325,"userId":1}],"coupons":[{"couponId":1,"couponLocation":1,"couponSn":"1525770720634277777","couponType":1,"expiredTm":1525770720634,"id":2,"userId":1},{"couponId":1,"couponLocation":1,"couponSn":"1525770720634881368","couponType":1,"expiredTm":1525770720634,"id":3,"userId":1},{"couponId":1,"couponLocation":1,"couponSn":"1525770809157921587","couponType":1,"expiredTm":1525770809157,"id":4,"userId":1},{"couponId":1,"couponLocation":1,"couponSn":"1525770809157360697","couponType":1,"expiredTm":1525770809157,"id":5,"userId":1},{"couponId":1,"couponLocation":1,"couponSn":"1525770919656415062","couponType":1,"expiredTm":1525770919656,"id":6,"userId":1}]}
     * message : success
     * serverTime : 1526001539406
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
        private List<CouponPiecesBean> couponPieces;
        private List<CouponsBean> coupons;
        private List<CycleRecord> cycleRecords;

        public List<CycleRecord> getCycleRecords() {
            return cycleRecords;
        }

        public void setCycleRecords(List<CycleRecord> cycleRecords) {
            this.cycleRecords = cycleRecords;
        }

        public List<CouponPiecesBean> getCouponPieces() {
            return couponPieces;
        }

        public void setCouponPieces(List<CouponPiecesBean> couponPieces) {
            this.couponPieces = couponPieces;
        }

        public List<CouponsBean> getCoupons() {
            return coupons;
        }

        public void setCoupons(List<CouponsBean> coupons) {
            this.coupons = coupons;
        }

        public static class CouponPiecesBean {
            /**
             * amount : 1
             * couponId : 4
             * id : 2
             * updateTm : 1525770441325
             * userId : 1
             */

            private int amount;
            private int couponId;
            private int id;
            private long updateTm;
            private int userId;

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public int getCouponId() {
                return couponId;
            }

            public void setCouponId(int couponId) {
                this.couponId = couponId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getUpdateTm() {
                return updateTm;
            }

            public void setUpdateTm(long updateTm) {
                this.updateTm = updateTm;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }

        public static class CouponsBean {
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
            private long createTm;
            private long expiredTm;
            private int id;
            private int userId;
            //token的来源 优惠券来源（1合成，2评论，3注册，4邀请，5被邀请，6活动码，7每日登录,8版本更新获得
            private int tokenFrom;
            private String tokenFromDesc;

            public String getTokenFromDesc() {
                return tokenFromDesc;
            }

            public void setTokenFromDesc(String tokenFromDesc) {
                this.tokenFromDesc = tokenFromDesc;
            }

            public void setTokenFrom(int tokenFrom) {
                this.tokenFrom = tokenFrom;
            }

            public int getTokenFrom() {
                return tokenFrom;
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

            public void setCreateTm(long createTm) {
                this.createTm = createTm;
            }

            public long getCreateTm() {
                return createTm;
            }
        }
    }
}
