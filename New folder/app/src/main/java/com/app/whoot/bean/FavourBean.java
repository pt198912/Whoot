package com.app.whoot.bean;

/**
 * Created by Sunrise on 1/16/2019.
 */

public class FavourBean {


    /**
     * createTm : 1551079889198
     * favShopInfo : {"category":2,"commentCount":4,"distance":365,"grade":3.3,"id":12,"imgUrl":"https://hk-storage.whoot.com/KqK8SD0YT7Y8gfOIkk9pwQ==.jpg","name":"风飞沙 笔记yh2","nameEn":"her you me","priceLevel":3,"type":5}
     * id : 10
     * locationId : 1
     * shopId : 12
     */

    private long createTm;
    private FavShopInfoBean favShopInfo;
    private int id;
    private int locationId;
    private int shopId;
    public boolean isSelect;


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public long getCreateTm() {
        return createTm;
    }

    public void setCreateTm(long createTm) {
        this.createTm = createTm;
    }

    public FavShopInfoBean getFavShopInfo() {
        return favShopInfo;
    }

    public void setFavShopInfo(FavShopInfoBean favShopInfo) {
        this.favShopInfo = favShopInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public static class FavShopInfoBean {
        /**
         * category : 2
         * commentCount : 4
         * distance : 365.0
         * grade : 3.3
         * id : 12
         * imgUrl : https://hk-storage.whoot.com/KqK8SD0YT7Y8gfOIkk9pwQ==.jpg
         * name : 风飞沙 笔记yh2
         * nameEn : her you me
         * priceLevel : 3
         * type : 5
         */

        private int category;
        private int commentCount;
        private double distance;
        private double grade;
        private int id;
        private String imgUrl;
        private String name;
        private String nameEn;
        private int priceLevel;
        private int type;

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
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

        public int getPriceLevel() {
            return priceLevel;
        }

        public void setPriceLevel(int priceLevel) {
            this.priceLevel = priceLevel;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
