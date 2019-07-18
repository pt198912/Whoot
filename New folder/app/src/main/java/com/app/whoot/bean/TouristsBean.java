package com.app.whoot.bean;

import java.util.List;

/**
 * Created by Sunrise on 4/23/2018.
 */

public class TouristsBean {


    /**
     * code : 0
     * data : {"account":{"accessToken":"1525320145474529015","id":10510,"lastloginTm":1525765957002,"name":"Guest10510","registTm":1525320145621,"type":1},"couponPieces":[{"amount":1,"couponId":4,"id":1525765957007184364,"updateTm":1525765957007,"userId":10510},{"amount":1,"couponId":3,"id":1525765957007739056,"updateTm":1525765957007,"userId":10510},{"amount":3,"couponId":2,"id":1525765957007898188,"updateTm":1525765957007,"userId":10510},{"amount":0,"couponId":1,"id":1525765957007483042,"updateTm":1525765957007,"userId":10510}],"coupons":[{"couponId":1,"couponLocation":1,"couponSn":"1525765957007815935","couponType":1,"id":1525765957007314696,"userId":10510},{"couponId":1,"couponLocation":1,"couponSn":"1525765957008762557","couponType":1,"id":1525765957007155845,"userId":10510}],"firstLogin":false,"serverTime":1525765957025,"token":"1525320145474529015"}
     * message : success
     */

    private int code;
    private DataBean data;
    private String message;

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

    public static class DataBean {
        /**
         * account : {"accessToken":"1525320145474529015","id":10510,"lastloginTm":1525765957002,"name":"Guest10510","registTm":1525320145621,"type":1}
         * couponPieces : [{"amount":1,"couponId":4,"id":1525765957007184364,"updateTm":1525765957007,"userId":10510},{"amount":1,"couponId":3,"id":1525765957007739056,"updateTm":1525765957007,"userId":10510},{"amount":3,"couponId":2,"id":1525765957007898188,"updateTm":1525765957007,"userId":10510},{"amount":0,"couponId":1,"id":1525765957007483042,"updateTm":1525765957007,"userId":10510}]
         * coupons : [{"couponId":1,"couponLocation":1,"couponSn":"1525765957007815935","couponType":1,"id":1525765957007314696,"userId":10510},{"couponId":1,"couponLocation":1,"couponSn":"1525765957008762557","couponType":1,"id":1525765957007155845,"userId":10510}]
         * firstLogin : false
         * serverTime : 1525765957025
         * token : 1525320145474529015
         */

        private AccountBean account;
        private boolean firstLogin;
        private long serverTime;
        private String token;
        private List<CouponPiecesBean> couponPieces;
        private List<CouponsBean> coupons;

        public AccountBean getAccount() {
            return account;
        }

        public void setAccount(AccountBean account) {
            this.account = account;
        }

        public boolean isFirstLogin() {
            return firstLogin;
        }

        public void setFirstLogin(boolean firstLogin) {
            this.firstLogin = firstLogin;
        }

        public long getServerTime() {
            return serverTime;
        }

        public void setServerTime(long serverTime) {
            this.serverTime = serverTime;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
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

        public static class AccountBean {
            /**
             * accessToken : 1525320145474529015
             * id : 10510
             * lastloginTm : 1525765957002
             * name : Guest10510
             * registTm : 1525320145621
             * type : 1
             */

            private String accessToken;
            private int id;
            private long lastloginTm;
            private String name;
            private long registTm;
            private int type;

            public String getAccessToken() {
                return accessToken;
            }

            public void setAccessToken(String accessToken) {
                this.accessToken = accessToken;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getLastloginTm() {
                return lastloginTm;
            }

            public void setLastloginTm(long lastloginTm) {
                this.lastloginTm = lastloginTm;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public long getRegistTm() {
                return registTm;
            }

            public void setRegistTm(long registTm) {
                this.registTm = registTm;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }

        public static class CouponPiecesBean {
            /**
             * amount : 1
             * couponId : 4
             * id : 1525765957007184364
             * updateTm : 1525765957007
             * userId : 10510
             */

            private int amount;
            private int couponId;
            private long id;
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

            public long getId() {
                return id;
            }

            public void setId(long id) {
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
             * couponSn : 1525765957007815935
             * couponType : 1
             * id : 1525765957007314696
             * userId : 10510
             */

            private int couponId;
            private int couponLocation;
            private String couponSn;
            private int couponType;
            private long id;
            private int userId;

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

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }
    }
}
