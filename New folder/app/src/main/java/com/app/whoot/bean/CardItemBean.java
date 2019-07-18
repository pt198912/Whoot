package com.app.whoot.bean;

import java.util.List;

/**
 * Created by Sunrise on 5/9/2018.
 */

public class CardItemBean {


    /**
     * address : 按时发放
     * category : 1
     * closeTm : 1524630208000
     * commentCount : 0
     * createTm : 1524542987161
     * currency : 1
     * grade : 5
     * hotCommodity : [{"describe":"aaa","id":1,"imgUrl":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3667342462,1407728484&fm=27&gp=0.jpg","name":"apple","price":12.2,"shopId":1,"type":"1"},{"describe":"又酸又甜","id":2,"imgUrl":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3667342462,1407728484&fm=27&gp=0.jpg","name":"李子","price":22.2,"shopId":1,"type":"1"},{"describe":"又酸又甜","id":3,"imgUrl":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3667342462,1407728484&fm=27&gp=0.jpg","name":"李子","price":22.2,"shopId":1,"type":"1"},{"describe":"不酸也不甜一点都不好吃","id":4,"imgUrl":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3667342462,1407728484&fm=27&gp=0.jpg","name":"哈哈李子哦","price":0,"shopId":1,"type":"1"}]
     * id : 1
     * imgUrl : https://whoot.ingstatics.com/upload/adf0365d-1f04-495d-96cd-26b37a61c6fc/b9af8db5a267577c4363686d1088336a.jpeg
     * latitude : 22.2
     * longitude : 22.2
     * merchantId : 1
     * name : 茶餐厅
     * openingTm : 1523334203000
     * price : 22.2
     * redeem : [{"beginTm":1520179200000,"commodityId":1,"commodityName":"apple","commodityPic":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3667342462,1407728484&fm=27&gp=0.jpg","costs":1,"couponId":1,"detail":"qqq","endTm":1525536000000,"id":1,"maxUseAmout":5,"shopId":1,"usedAmout":2},{"beginTm":1520179200000,"commodityId":1,"commodityName":"apple","commodityPic":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3667342462,1407728484&fm=27&gp=0.jpg","costs":1,"couponId":1,"detail":"qqq","endTm":1525536000000,"id":2,"maxUseAmout":5,"shopId":1,"usedAmout":2},{"beginTm":1520179200000,"commodityId":1,"commodityName":"apple","commodityPic":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3667342462,1407728484&fm=27&gp=0.jpg","costs":4,"couponId":1,"detail":"1241241a","endTm":1525536000000,"id":3,"maxUseAmout":5,"shopId":1,"usedAmout":2}]
     * tel : 111111
     * type : 1
     * updateTm : 1522381050891
     */

    private String address;
    private int category;
    private long closeTm;
    private int commentCount;
    private long createTm;
    private int currency;
    private int grade;
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
    private List<HotCommodityBean> hotCommodity;
    private List<RedeemBean> redeem;

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

    public List<HotCommodityBean> getHotCommodity() {
        return hotCommodity;
    }

    public void setHotCommodity(List<HotCommodityBean> hotCommodity) {
        this.hotCommodity = hotCommodity;
    }

    public List<RedeemBean> getRedeem() {
        return redeem;
    }

    public void setRedeem(List<RedeemBean> redeem) {
        this.redeem = redeem;
    }

    public static class HotCommodityBean {
        /**
         * describe : aaa
         * id : 1
         * imgUrl : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3667342462,1407728484&fm=27&gp=0.jpg
         * name : apple
         * price : 12.2
         * shopId : 1
         * type : 1
         */

        private String describe;
        private int id;
        private String imgUrl;
        private String name;
        private double price;
        private int shopId;
        private String type;

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
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

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class RedeemBean {
        /**
         * beginTm : 1520179200000
         * commodityId : 1
         * commodityName : apple
         * commodityPic : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3667342462,1407728484&fm=27&gp=0.jpg
         * costs : 1
         * couponId : 1
         * detail : qqq
         * endTm : 1525536000000
         * id : 1
         * maxUseAmout : 5
         * shopId : 1
         * usedAmout : 2
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

        public long getBeginTm() {
            return beginTm;
        }

        public void setBeginTm(long beginTm) {
            this.beginTm = beginTm;
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
    }
}
