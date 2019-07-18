package com.app.whoot.bean;

import java.util.List;

public class CfgBean {

    /**
     * Coupons : [{"Id":1,"Type":1,"Name":"铜优惠券","Expired":100,"Icon":"https://www.whoot.com/1.png","MaxPieces":5},{"Id":2,"Type":1,"Name":"银优惠券","Expired":100,"Icon":"https://www.whoot.com/2.png","MaxPieces":5},{"Id":3,"Type":1,"Name":"金优惠券","Expired":100,"Icon":"https://www.whoot.com/3.png","MaxPieces":5},{"Id":4,"Type":1,"Name":"钻石","Expired":100,"Icon":"https://www.whoot.com/4.png","MaxPieces":5,"Limited":true}]
     * Location : {"Id":1,"Name":"HongKong","MaxDistance":5,"TimeZone":8,"TimeLag":28800000,"Language":"zh_HK","Currency":1,"Map":"Google","Category":[{"Id":1,"NameEn":"Asian","NameCh":"亞洲","Sub":[{"Id":1,"NameEn":"Hong Kong Style","NameCh":"港式","Icon":"https://www.xxx.com/aaa.png"},{"Id":2,"NameEn":"Chinese","NameCh":"中式","Icon":"https://www.xxx.com/aaa.png"},{"Id":3,"NameEn":"Cantonese","NameCh":"粵式料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":4,"NameEn":"Taiwanese","NameCh":"台式","Icon":"https://www.xxx.com/aaa.png"},{"Id":5,"NameEn":"Japanese","NameCh":"日式","Icon":"https://www.xxx.com/aaa.png"},{"Id":6,"NameEn":"Korean","NameCh":"韓式料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":7,"NameEn":"Thai","NameCh":"泰國料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":8,"NameEn":"Indonesian","NameCh":"印尼料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":9,"NameEn":"Vietnamese","NameCh":"越式料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":10,"NameEn":"Singaporean","NameCh":"新加坡料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":11,"NameEn":"Malaysian","NameCh":"馬來西亞料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":12,"NameEn":"Indian","NameCh":"印度料理","Icon":"https://www.xxx.com/aaa.png"}]},{"Id":2,"NameEn":"Western","NameCh":"西方","Sub":[{"Id":1,"NameEn":"French","NameCh":"法式料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":2,"NameEn":"German","NameCh":"德國料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":3,"NameEn":"British","NameCh":"英式料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":4,"NameEn":"Italian","NameCh":"義式料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":5,"NameEn":"Spanish","NameCh":"西班牙料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":6,"NameEn":"Mediterranean","NameCh":"地中海料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":7,"NameEn":"American","NameCh":"美式料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":8,"NameEn":"Mexican","NameCh":"墨西哥料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":9,"NameEn":"South American","NameCh":"南美洲料理","Icon":"https://www.xxx.com/aaa.png"}]},{"Id":3,"NameEn":"Others","NameCh":"其他","Sub":[{"Id":1,"NameEn":"Coffee","NameCh":"咖啡店","Icon":"https://www.xxx.com/aaa.png"},{"Id":2,"NameEn":"Bar","NameCh":"酒吧","Icon":"https://www.xxx.com/aaa.png"},{"Id":3,"NameEn":"Cake","NameCh":"甜點店","Icon":"https://www.xxx.com/aaa.png"}]}],"UserChannel":["Facebook"],"MerchantChannel":["Mobile_SMS"],"ShopTags":[{"Id":1,"NameCh":"素食友善餐廳","NameEn":"Vegan Friendly"},{"Id":2,"NameCh":"接受信用卡","NameEn":"Credit Card"},{"Id":3,"NameCh":"免費WIFI","NameEn":"Free WIFI"},{"Id":4,"NameCh":"適合親子","NameEn":"Good for Kids"},{"Id":5,"NameCh":"適合聚餐","NameEn":"Good for Group"},{"Id":6,"NameCh":"可外賣","NameEn":"Take Out"},{"Id":7,"NameCh":"可攜帶寵物","NameEn":"Pet Friendly"}]}
     * Gifts : [{"Id":1,"Name":"金宝箱","Rule":1},{"Id":2,"Name":"银宝箱","Rule":1},{"Id":3,"Name":"铜宝箱","Rule":1}]
     * GiftRuleGroups : [{"Id":1,"Name":"金宝箱奖励规则","Rules":[{"Id":1,"Name":"铜优惠券发放规则","RuleType":2,"Items":[{"Weight":10,"Min":1,"Max":1,"ItemType":1,"ItemId":1}]},{"Id":2,"Name":"银优惠券印花发放规则","RuleType":1,"Items":[{"Weight":1,"Min":1,"Max":3,"ItemType":2,"ItemId":2},{"Weight":1,"Min":1,"Max":4,"ItemType":2,"ItemId":2}]},{"Id":3,"Name":"金优惠券印花发放规则","RuleType":1,"Items":[{"Weight":2,"Min":1,"Max":3,"ItemType":2,"ItemId":3},{"Weight":1,"Min":1,"Max":4,"ItemType":2,"ItemId":3}]},{"Id":4,"Name":"钻石优惠券发放规则","RuleType":2,"Items":[{"Weight":2,"Min":1,"Max":1,"ItemType":2,"ItemId":4}]}]}]
     * Currency : [{"Id":1,"Name":"HKD"},{"Id":2,"Name":"RMB"},{"Id":3,"Name":"USD"},{"Id":4,"Name":"EUR"}]
     * Bux : [{"type":1,"NameCh":"每日登入","NameEn":"Daily Login","Duration":1},{"type":2,"NameCh":"撰寫評論","NameEn":"Write a review","Duration":10}]
     */

    private LocationBean Location;
    private List<CouponsBean> Coupons;
    private List<GiftsBean> Gifts;
    private List<GiftRuleGroupsBean> GiftRuleGroups;
    private List<CurrencyBean> Currency;
    private List<BuxBean> Bux;

    public LocationBean getLocation() {
        return Location;
    }

    public void setLocation(LocationBean Location) {
        this.Location = Location;
    }

    public List<CouponsBean> getCoupons() {
        return Coupons;
    }

    public void setCoupons(List<CouponsBean> Coupons) {
        this.Coupons = Coupons;
    }

    public List<GiftsBean> getGifts() {
        return Gifts;
    }

    public void setGifts(List<GiftsBean> Gifts) {
        this.Gifts = Gifts;
    }

    public List<GiftRuleGroupsBean> getGiftRuleGroups() {
        return GiftRuleGroups;
    }

    public void setGiftRuleGroups(List<GiftRuleGroupsBean> GiftRuleGroups) {
        this.GiftRuleGroups = GiftRuleGroups;
    }

    public List<CurrencyBean> getCurrency() {
        return Currency;
    }

    public void setCurrency(List<CurrencyBean> Currency) {
        this.Currency = Currency;
    }

    public List<BuxBean> getBux() {
        return Bux;
    }

    public void setBux(List<BuxBean> Bux) {
        this.Bux = Bux;
    }

    public static class LocationBean {
        /**
         * Id : 1
         * Name : HongKong
         * MaxDistance : 5
         * TimeZone : 8
         * TimeLag : 28800000
         * Language : zh_HK
         * Currency : 1
         * Map : Google
         * Category : [{"Id":1,"NameEn":"Asian","NameCh":"亞洲","Sub":[{"Id":1,"NameEn":"Hong Kong Style","NameCh":"港式","Icon":"https://www.xxx.com/aaa.png"},{"Id":2,"NameEn":"Chinese","NameCh":"中式","Icon":"https://www.xxx.com/aaa.png"},{"Id":3,"NameEn":"Cantonese","NameCh":"粵式料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":4,"NameEn":"Taiwanese","NameCh":"台式","Icon":"https://www.xxx.com/aaa.png"},{"Id":5,"NameEn":"Japanese","NameCh":"日式","Icon":"https://www.xxx.com/aaa.png"},{"Id":6,"NameEn":"Korean","NameCh":"韓式料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":7,"NameEn":"Thai","NameCh":"泰國料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":8,"NameEn":"Indonesian","NameCh":"印尼料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":9,"NameEn":"Vietnamese","NameCh":"越式料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":10,"NameEn":"Singaporean","NameCh":"新加坡料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":11,"NameEn":"Malaysian","NameCh":"馬來西亞料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":12,"NameEn":"Indian","NameCh":"印度料理","Icon":"https://www.xxx.com/aaa.png"}]},{"Id":2,"NameEn":"Western","NameCh":"西方","Sub":[{"Id":1,"NameEn":"French","NameCh":"法式料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":2,"NameEn":"German","NameCh":"德國料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":3,"NameEn":"British","NameCh":"英式料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":4,"NameEn":"Italian","NameCh":"義式料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":5,"NameEn":"Spanish","NameCh":"西班牙料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":6,"NameEn":"Mediterranean","NameCh":"地中海料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":7,"NameEn":"American","NameCh":"美式料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":8,"NameEn":"Mexican","NameCh":"墨西哥料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":9,"NameEn":"South American","NameCh":"南美洲料理","Icon":"https://www.xxx.com/aaa.png"}]},{"Id":3,"NameEn":"Others","NameCh":"其他","Sub":[{"Id":1,"NameEn":"Coffee","NameCh":"咖啡店","Icon":"https://www.xxx.com/aaa.png"},{"Id":2,"NameEn":"Bar","NameCh":"酒吧","Icon":"https://www.xxx.com/aaa.png"},{"Id":3,"NameEn":"Cake","NameCh":"甜點店","Icon":"https://www.xxx.com/aaa.png"}]}]
         * UserChannel : ["Facebook"]
         * MerchantChannel : ["Mobile_SMS"]
         * ShopTags : [{"Id":1,"NameCh":"素食友善餐廳","NameEn":"Vegan Friendly"},{"Id":2,"NameCh":"接受信用卡","NameEn":"Credit Card"},{"Id":3,"NameCh":"免費WIFI","NameEn":"Free WIFI"},{"Id":4,"NameCh":"適合親子","NameEn":"Good for Kids"},{"Id":5,"NameCh":"適合聚餐","NameEn":"Good for Group"},{"Id":6,"NameCh":"可外賣","NameEn":"Take Out"},{"Id":7,"NameCh":"可攜帶寵物","NameEn":"Pet Friendly"}]
         */

        private int Id;
        private String Name;
        private int MaxDistance;
        private int TimeZone;
        private int TimeLag;
        private String Language;
        private int Currency;
        private String Map;
        private List<CategoryBean> Category;
        private List<String> UserChannel;
        private List<String> MerchantChannel;
        private List<ShopTagsBean> ShopTags;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getMaxDistance() {
            return MaxDistance;
        }

        public void setMaxDistance(int MaxDistance) {
            this.MaxDistance = MaxDistance;
        }

        public int getTimeZone() {
            return TimeZone;
        }

        public void setTimeZone(int TimeZone) {
            this.TimeZone = TimeZone;
        }

        public int getTimeLag() {
            return TimeLag;
        }

        public void setTimeLag(int TimeLag) {
            this.TimeLag = TimeLag;
        }

        public String getLanguage() {
            return Language;
        }

        public void setLanguage(String Language) {
            this.Language = Language;
        }

        public int getCurrency() {
            return Currency;
        }

        public void setCurrency(int Currency) {
            this.Currency = Currency;
        }

        public String getMap() {
            return Map;
        }

        public void setMap(String Map) {
            this.Map = Map;
        }

        public List<CategoryBean> getCategory() {
            return Category;
        }

        public void setCategory(List<CategoryBean> Category) {
            this.Category = Category;
        }

        public List<String> getUserChannel() {
            return UserChannel;
        }

        public void setUserChannel(List<String> UserChannel) {
            this.UserChannel = UserChannel;
        }

        public List<String> getMerchantChannel() {
            return MerchantChannel;
        }

        public void setMerchantChannel(List<String> MerchantChannel) {
            this.MerchantChannel = MerchantChannel;
        }

        public List<ShopTagsBean> getShopTags() {
            return ShopTags;
        }

        public void setShopTags(List<ShopTagsBean> ShopTags) {
            this.ShopTags = ShopTags;
        }

        public static class CategoryBean {
            /**
             * Id : 1
             * NameEn : Asian
             * NameCh : 亞洲
             * Sub : [{"Id":1,"NameEn":"Hong Kong Style","NameCh":"港式","Icon":"https://www.xxx.com/aaa.png"},{"Id":2,"NameEn":"Chinese","NameCh":"中式","Icon":"https://www.xxx.com/aaa.png"},{"Id":3,"NameEn":"Cantonese","NameCh":"粵式料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":4,"NameEn":"Taiwanese","NameCh":"台式","Icon":"https://www.xxx.com/aaa.png"},{"Id":5,"NameEn":"Japanese","NameCh":"日式","Icon":"https://www.xxx.com/aaa.png"},{"Id":6,"NameEn":"Korean","NameCh":"韓式料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":7,"NameEn":"Thai","NameCh":"泰國料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":8,"NameEn":"Indonesian","NameCh":"印尼料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":9,"NameEn":"Vietnamese","NameCh":"越式料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":10,"NameEn":"Singaporean","NameCh":"新加坡料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":11,"NameEn":"Malaysian","NameCh":"馬來西亞料理","Icon":"https://www.xxx.com/aaa.png"},{"Id":12,"NameEn":"Indian","NameCh":"印度料理","Icon":"https://www.xxx.com/aaa.png"}]
             */

            private int Id;
            private String NameEn;
            private String NameCh;
            private List<SubBean> Sub;

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public String getNameEn() {
                return NameEn;
            }

            public void setNameEn(String NameEn) {
                this.NameEn = NameEn;
            }

            public String getNameCh() {
                return NameCh;
            }

            public void setNameCh(String NameCh) {
                this.NameCh = NameCh;
            }

            public List<SubBean> getSub() {
                return Sub;
            }

            public void setSub(List<SubBean> Sub) {
                this.Sub = Sub;
            }

            public static class SubBean {
                /**
                 * Id : 1
                 * NameEn : Hong Kong Style
                 * NameCh : 港式
                 * Icon : https://www.xxx.com/aaa.png
                 */

                private int Id;
                private String NameEn;
                private String NameCh;
                private String Icon;

                public int getId() {
                    return Id;
                }

                public void setId(int Id) {
                    this.Id = Id;
                }

                public String getNameEn() {
                    return NameEn;
                }

                public void setNameEn(String NameEn) {
                    this.NameEn = NameEn;
                }

                public String getNameCh() {
                    return NameCh;
                }

                public void setNameCh(String NameCh) {
                    this.NameCh = NameCh;
                }

                public String getIcon() {
                    return Icon;
                }

                public void setIcon(String Icon) {
                    this.Icon = Icon;
                }
            }
        }

        public static class ShopTagsBean {
            /**
             * Id : 1
             * NameCh : 素食友善餐廳
             * NameEn : Vegan Friendly
             */

            private int Id;
            private String NameCh;
            private String NameEn;

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public String getNameCh() {
                return NameCh;
            }

            public void setNameCh(String NameCh) {
                this.NameCh = NameCh;
            }

            public String getNameEn() {
                return NameEn;
            }

            public void setNameEn(String NameEn) {
                this.NameEn = NameEn;
            }
        }
    }

    public static class CouponsBean {
        /**
         * Id : 1
         * Type : 1
         * Name : 铜优惠券
         * Expired : 100
         * Icon : https://www.whoot.com/1.png
         * MaxPieces : 5
         * Limited : true
         */

        private int Id;
        private int Type;
        private String Name;
        private int Expired;
        private String Icon;
        private int MaxPieces;
        private boolean Limited;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public int getType() {
            return Type;
        }

        public void setType(int Type) {
            this.Type = Type;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getExpired() {
            return Expired;
        }

        public void setExpired(int Expired) {
            this.Expired = Expired;
        }

        public String getIcon() {
            return Icon;
        }

        public void setIcon(String Icon) {
            this.Icon = Icon;
        }

        public int getMaxPieces() {
            return MaxPieces;
        }

        public void setMaxPieces(int MaxPieces) {
            this.MaxPieces = MaxPieces;
        }

        public boolean isLimited() {
            return Limited;
        }

        public void setLimited(boolean Limited) {
            this.Limited = Limited;
        }
    }

    public static class GiftsBean {
        /**
         * Id : 1
         * Name : 金宝箱
         * Rule : 1
         */

        private int Id;
        private String Name;
        private int Rule;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getRule() {
            return Rule;
        }

        public void setRule(int Rule) {
            this.Rule = Rule;
        }
    }

    public static class GiftRuleGroupsBean {
        /**
         * Id : 1
         * Name : 金宝箱奖励规则
         * Rules : [{"Id":1,"Name":"铜优惠券发放规则","RuleType":2,"Items":[{"Weight":10,"Min":1,"Max":1,"ItemType":1,"ItemId":1}]},{"Id":2,"Name":"银优惠券印花发放规则","RuleType":1,"Items":[{"Weight":1,"Min":1,"Max":3,"ItemType":2,"ItemId":2},{"Weight":1,"Min":1,"Max":4,"ItemType":2,"ItemId":2}]},{"Id":3,"Name":"金优惠券印花发放规则","RuleType":1,"Items":[{"Weight":2,"Min":1,"Max":3,"ItemType":2,"ItemId":3},{"Weight":1,"Min":1,"Max":4,"ItemType":2,"ItemId":3}]},{"Id":4,"Name":"钻石优惠券发放规则","RuleType":2,"Items":[{"Weight":2,"Min":1,"Max":1,"ItemType":2,"ItemId":4}]}]
         */

        private int Id;
        private String Name;
        private List<RulesBean> Rules;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public List<RulesBean> getRules() {
            return Rules;
        }

        public void setRules(List<RulesBean> Rules) {
            this.Rules = Rules;
        }

        public static class RulesBean {
            /**
             * Id : 1
             * Name : 铜优惠券发放规则
             * RuleType : 2
             * Items : [{"Weight":10,"Min":1,"Max":1,"ItemType":1,"ItemId":1}]
             */

            private int Id;
            private String Name;
            private int RuleType;
            private List<ItemsBean> Items;

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public int getRuleType() {
                return RuleType;
            }

            public void setRuleType(int RuleType) {
                this.RuleType = RuleType;
            }

            public List<ItemsBean> getItems() {
                return Items;
            }

            public void setItems(List<ItemsBean> Items) {
                this.Items = Items;
            }

            public static class ItemsBean {
                /**
                 * Weight : 10
                 * Min : 1
                 * Max : 1
                 * ItemType : 1
                 * ItemId : 1
                 */

                private int Weight;
                private int Min;
                private int Max;
                private int ItemType;
                private int ItemId;

                public int getWeight() {
                    return Weight;
                }

                public void setWeight(int Weight) {
                    this.Weight = Weight;
                }

                public int getMin() {
                    return Min;
                }

                public void setMin(int Min) {
                    this.Min = Min;
                }

                public int getMax() {
                    return Max;
                }

                public void setMax(int Max) {
                    this.Max = Max;
                }

                public int getItemType() {
                    return ItemType;
                }

                public void setItemType(int ItemType) {
                    this.ItemType = ItemType;
                }

                public int getItemId() {
                    return ItemId;
                }

                public void setItemId(int ItemId) {
                    this.ItemId = ItemId;
                }
            }
        }
    }

    public static class CurrencyBean {
        /**
         * Id : 1
         * Name : HKD
         */

        private int Id;
        private String Name;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }
    }

    public static class BuxBean {
        /**
         * type : 1
         * NameCh : 每日登入
         * NameEn : Daily Login
         * Duration : 1
         */

        private int type;
        private String NameCh;
        private String NameEn;
        private int Duration;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getNameCh() {
            return NameCh;
        }

        public void setNameCh(String NameCh) {
            this.NameCh = NameCh;
        }

        public String getNameEn() {
            return NameEn;
        }

        public void setNameEn(String NameEn) {
            this.NameEn = NameEn;
        }

        public int getDuration() {
            return Duration;
        }

        public void setDuration(int Duration) {
            this.Duration = Duration;
        }
    }
}
