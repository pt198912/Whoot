package com.app.whoot.bean;

import java.util.List;

/**
 * Created by Sunrise on 4/20/2018.
 */

public class ConfiguraBean {


    /**
     * code : 0
     * data : {"Coupons":[{"Expired":100,"Icon":"https://www.whoot.com/1.png","Id":1,"MaxPieces":5,"Name":"é\u201cœä¼˜æƒ åˆ¸","Type":1},{"Expired":100,"Icon":"https://www.whoot.com/2.png","Id":2,"MaxPieces":5,"Name":"é\u201c¶ä¼˜æƒ åˆ¸","Type":1},{"Expired":100,"Icon":"https://www.whoot.com/3.png","Id":3,"MaxPieces":5,"Name":"é\u2021\u2018ä¼˜æƒ åˆ¸","Type":1},{"Expired":100,"Icon":"https://www.whoot.com/4.png","Id":4,"Limited":true,"MaxPieces":5,"Name":"é\u2019»çŸ³","Type":1}],"GiftRuleGroups":[{"Id":1,"Name":"é\u2021\u2018å®\u009dç®±å¥\u2013åŠ±è§\u201eåˆ™","Rules":[{"Id":1,"Items":[{"ItemId":1,"ItemType":1,"Max":1,"Min":1,"Weight":10}],"Name":"é\u201cœä¼˜æƒ åˆ¸å\u008f\u2018æ\u201d¾è§\u201eåˆ™","RuleType":2},{"Id":2,"Items":[{"ItemId":2,"ItemType":2,"Max":3,"Min":1,"Weight":1},{"ItemId":2,"ItemType":2,"Max":4,"Min":1,"Weight":1}],"Name":"é\u201c¶ä¼˜æƒ åˆ¸å\u008d°èŠ±å\u008f\u2018æ\u201d¾è§\u201eåˆ™","RuleType":1},{"Id":3,"Items":[{"ItemId":3,"ItemType":2,"Max":3,"Min":1,"Weight":2},{"ItemId":3,"ItemType":2,"Max":4,"Min":1,"Weight":1}],"Name":"é\u2021\u2018ä¼˜æƒ åˆ¸å\u008d°èŠ±å\u008f\u2018æ\u201d¾è§\u201eåˆ™","RuleType":1},{"Id":4,"Items":[{"ItemId":4,"ItemType":2,"Max":1,"Min":1,"Weight":2}],"Name":"é\u2019»çŸ³ä¼˜æƒ åˆ¸å\u008f\u2018æ\u201d¾è§\u201eåˆ™","RuleType":2}]}],"Gifts":[{"Id":1,"Name":"é\u2021\u2018å®\u009dç®±","Rule":1},{"Id":2,"Name":"é\u201c¶å®\u009dç®±","Rule":1},{"Id":3,"Name":"é\u201cœå®\u009dç®±","Rule":1}],"Location":{"Category":[{"Id":1,"Name":"é¤\u0090é¥®","Sub":[{"Icon":"https://www.xxx.com/aaa.png","Id":1,"Name":"ä¸­é¤\u0090"},{"Icon":"https://www.xxx.com/aaa.png","Id":2,"Name":"å\u008d°åº¦è\u008fœ"},{"Icon":"https://www.xxx.com/aaa.png","Id":3,"Name":"æ\u2014¥æœ¬æ\u2013™ç\u0090\u2020"},{"Icon":"https://www.xxx.com/aaa.png","Id":4,"Name":"å\u2019\u2013å\u2022¡åŽ\u2026"},{"Icon":"https://www.xxx.com/aaa.png","Id":5,"Name":"é\u2026\u2019å\u0090§"},{"Icon":"https://www.xxx.com/aaa.png","Id":6,"Name":"å\u2020°æ¿\u20acæ·\u2039åº\u2014"}]},{"Id":2,"Name":"é\u203a¶å\u201d®","Sub":[{"Icon":"https://www.xxx.com/aaa.png","Id":7,"Name":"ä¸°æ³½"}]}],"Currency":"HKD","Id":1,"Language":"zh_HK","Map":"Google","MaxDistance":5,"MerchantChannel":["Mobile_SMS"],"Name":"HongKong","UserChannel":["Facebook"]}}
     */

    private int code;
    private DataBean data;

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

    public static class DataBean {
        /**
         * Coupons : [{"Expired":100,"Icon":"https://www.whoot.com/1.png","Id":1,"MaxPieces":5,"Name":"é\u201cœä¼˜æƒ åˆ¸","Type":1},{"Expired":100,"Icon":"https://www.whoot.com/2.png","Id":2,"MaxPieces":5,"Name":"é\u201c¶ä¼˜æƒ åˆ¸","Type":1},{"Expired":100,"Icon":"https://www.whoot.com/3.png","Id":3,"MaxPieces":5,"Name":"é\u2021\u2018ä¼˜æƒ åˆ¸","Type":1},{"Expired":100,"Icon":"https://www.whoot.com/4.png","Id":4,"Limited":true,"MaxPieces":5,"Name":"é\u2019»çŸ³","Type":1}]
         * GiftRuleGroups : [{"Id":1,"Name":"é\u2021\u2018å®\u009dç®±å¥\u2013åŠ±è§\u201eåˆ™","Rules":[{"Id":1,"Items":[{"ItemId":1,"ItemType":1,"Max":1,"Min":1,"Weight":10}],"Name":"é\u201cœä¼˜æƒ åˆ¸å\u008f\u2018æ\u201d¾è§\u201eåˆ™","RuleType":2},{"Id":2,"Items":[{"ItemId":2,"ItemType":2,"Max":3,"Min":1,"Weight":1},{"ItemId":2,"ItemType":2,"Max":4,"Min":1,"Weight":1}],"Name":"é\u201c¶ä¼˜æƒ åˆ¸å\u008d°èŠ±å\u008f\u2018æ\u201d¾è§\u201eåˆ™","RuleType":1},{"Id":3,"Items":[{"ItemId":3,"ItemType":2,"Max":3,"Min":1,"Weight":2},{"ItemId":3,"ItemType":2,"Max":4,"Min":1,"Weight":1}],"Name":"é\u2021\u2018ä¼˜æƒ åˆ¸å\u008d°èŠ±å\u008f\u2018æ\u201d¾è§\u201eåˆ™","RuleType":1},{"Id":4,"Items":[{"ItemId":4,"ItemType":2,"Max":1,"Min":1,"Weight":2}],"Name":"é\u2019»çŸ³ä¼˜æƒ åˆ¸å\u008f\u2018æ\u201d¾è§\u201eåˆ™","RuleType":2}]}]
         * Gifts : [{"Id":1,"Name":"é\u2021\u2018å®\u009dç®±","Rule":1},{"Id":2,"Name":"é\u201c¶å®\u009dç®±","Rule":1},{"Id":3,"Name":"é\u201cœå®\u009dç®±","Rule":1}]
         * Location : {"Category":[{"Id":1,"Name":"é¤\u0090é¥®","Sub":[{"Icon":"https://www.xxx.com/aaa.png","Id":1,"Name":"ä¸­é¤\u0090"},{"Icon":"https://www.xxx.com/aaa.png","Id":2,"Name":"å\u008d°åº¦è\u008fœ"},{"Icon":"https://www.xxx.com/aaa.png","Id":3,"Name":"æ\u2014¥æœ¬æ\u2013™ç\u0090\u2020"},{"Icon":"https://www.xxx.com/aaa.png","Id":4,"Name":"å\u2019\u2013å\u2022¡åŽ\u2026"},{"Icon":"https://www.xxx.com/aaa.png","Id":5,"Name":"é\u2026\u2019å\u0090§"},{"Icon":"https://www.xxx.com/aaa.png","Id":6,"Name":"å\u2020°æ¿\u20acæ·\u2039åº\u2014"}]},{"Id":2,"Name":"é\u203a¶å\u201d®","Sub":[{"Icon":"https://www.xxx.com/aaa.png","Id":7,"Name":"ä¸°æ³½"}]}],"Currency":"HKD","Id":1,"Language":"zh_HK","Map":"Google","MaxDistance":5,"MerchantChannel":["Mobile_SMS"],"Name":"HongKong","UserChannel":["Facebook"]}
         */

        private LocationBean Location;
        private List<CouponsBean> Coupons;
        private List<GiftRuleGroupsBean> GiftRuleGroups;
        private List<GiftsBean> Gifts;

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

        public List<GiftRuleGroupsBean> getGiftRuleGroups() {
            return GiftRuleGroups;
        }

        public void setGiftRuleGroups(List<GiftRuleGroupsBean> GiftRuleGroups) {
            this.GiftRuleGroups = GiftRuleGroups;
        }

        public List<GiftsBean> getGifts() {
            return Gifts;
        }

        public void setGifts(List<GiftsBean> Gifts) {
            this.Gifts = Gifts;
        }

        public static class LocationBean {
            /**
             * Category : [{"Id":1,"Name":"é¤\u0090é¥®","Sub":[{"Icon":"https://www.xxx.com/aaa.png","Id":1,"Name":"ä¸­é¤\u0090"},{"Icon":"https://www.xxx.com/aaa.png","Id":2,"Name":"å\u008d°åº¦è\u008fœ"},{"Icon":"https://www.xxx.com/aaa.png","Id":3,"Name":"æ\u2014¥æœ¬æ\u2013™ç\u0090\u2020"},{"Icon":"https://www.xxx.com/aaa.png","Id":4,"Name":"å\u2019\u2013å\u2022¡åŽ\u2026"},{"Icon":"https://www.xxx.com/aaa.png","Id":5,"Name":"é\u2026\u2019å\u0090§"},{"Icon":"https://www.xxx.com/aaa.png","Id":6,"Name":"å\u2020°æ¿\u20acæ·\u2039åº\u2014"}]},{"Id":2,"Name":"é\u203a¶å\u201d®","Sub":[{"Icon":"https://www.xxx.com/aaa.png","Id":7,"Name":"ä¸°æ³½"}]}]
             * Currency : HKD
             * Id : 1
             * Language : zh_HK
             * Map : Google
             * MaxDistance : 5.0
             * MerchantChannel : ["Mobile_SMS"]
             * Name : HongKong
             * UserChannel : ["Facebook"]
             */

            private String Currency;
            private int Id;
            private String Language;
            private String Map;
            private double MaxDistance;
            private String Name;
            private List<CategoryBean> Category;
            private List<String> MerchantChannel;
            private List<String> UserChannel;

            public String getCurrency() {
                return Currency;
            }

            public void setCurrency(String Currency) {
                this.Currency = Currency;
            }

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public String getLanguage() {
                return Language;
            }

            public void setLanguage(String Language) {
                this.Language = Language;
            }

            public String getMap() {
                return Map;
            }

            public void setMap(String Map) {
                this.Map = Map;
            }

            public double getMaxDistance() {
                return MaxDistance;
            }

            public void setMaxDistance(double MaxDistance) {
                this.MaxDistance = MaxDistance;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public List<CategoryBean> getCategory() {
                return Category;
            }

            public void setCategory(List<CategoryBean> Category) {
                this.Category = Category;
            }

            public List<String> getMerchantChannel() {
                return MerchantChannel;
            }

            public void setMerchantChannel(List<String> MerchantChannel) {
                this.MerchantChannel = MerchantChannel;
            }

            public List<String> getUserChannel() {
                return UserChannel;
            }

            public void setUserChannel(List<String> UserChannel) {
                this.UserChannel = UserChannel;
            }

            public static class CategoryBean {
                /**
                 * Id : 1
                 * Name : é¤é¥®
                 * Sub : [{"Icon":"https://www.xxx.com/aaa.png","Id":1,"Name":"ä¸­é¤\u0090"},{"Icon":"https://www.xxx.com/aaa.png","Id":2,"Name":"å\u008d°åº¦è\u008fœ"},{"Icon":"https://www.xxx.com/aaa.png","Id":3,"Name":"æ\u2014¥æœ¬æ\u2013™ç\u0090\u2020"},{"Icon":"https://www.xxx.com/aaa.png","Id":4,"Name":"å\u2019\u2013å\u2022¡åŽ\u2026"},{"Icon":"https://www.xxx.com/aaa.png","Id":5,"Name":"é\u2026\u2019å\u0090§"},{"Icon":"https://www.xxx.com/aaa.png","Id":6,"Name":"å\u2020°æ¿\u20acæ·\u2039åº\u2014"}]
                 */

                private int Id;
                private String Name;
                private List<SubBean> Sub;

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

                public List<SubBean> getSub() {
                    return Sub;
                }

                public void setSub(List<SubBean> Sub) {
                    this.Sub = Sub;
                }

                public static class SubBean {
                    /**
                     * Icon : https://www.xxx.com/aaa.png
                     * Id : 1
                     * Name : ä¸­é¤
                     */

                    private String Icon;
                    private int Id;
                    private String Name;

                    public String getIcon() {
                        return Icon;
                    }

                    public void setIcon(String Icon) {
                        this.Icon = Icon;
                    }

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
            }
        }

        public static class CouponsBean {
            /**
             * Expired : 100
             * Icon : https://www.whoot.com/1.png
             * Id : 1
             * MaxPieces : 5
             * Name : é“œä¼˜æƒ åˆ¸
             * Type : 1
             * Limited : true
             */

            private int Expired;
            private String Icon;
            private int Id;
            private int MaxPieces;
            private String Name;
            private int Type;
            private boolean Limited;

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

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public int getMaxPieces() {
                return MaxPieces;
            }

            public void setMaxPieces(int MaxPieces) {
                this.MaxPieces = MaxPieces;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public int getType() {
                return Type;
            }

            public void setType(int Type) {
                this.Type = Type;
            }

            public boolean isLimited() {
                return Limited;
            }

            public void setLimited(boolean Limited) {
                this.Limited = Limited;
            }
        }

        public static class GiftRuleGroupsBean {
            /**
             * Id : 1
             * Name : é‡‘å®ç®±å¥–åŠ±è§„åˆ™
             * Rules : [{"Id":1,"Items":[{"ItemId":1,"ItemType":1,"Max":1,"Min":1,"Weight":10}],"Name":"é\u201cœä¼˜æƒ åˆ¸å\u008f\u2018æ\u201d¾è§\u201eåˆ™","RuleType":2},{"Id":2,"Items":[{"ItemId":2,"ItemType":2,"Max":3,"Min":1,"Weight":1},{"ItemId":2,"ItemType":2,"Max":4,"Min":1,"Weight":1}],"Name":"é\u201c¶ä¼˜æƒ åˆ¸å\u008d°èŠ±å\u008f\u2018æ\u201d¾è§\u201eåˆ™","RuleType":1},{"Id":3,"Items":[{"ItemId":3,"ItemType":2,"Max":3,"Min":1,"Weight":2},{"ItemId":3,"ItemType":2,"Max":4,"Min":1,"Weight":1}],"Name":"é\u2021\u2018ä¼˜æƒ åˆ¸å\u008d°èŠ±å\u008f\u2018æ\u201d¾è§\u201eåˆ™","RuleType":1},{"Id":4,"Items":[{"ItemId":4,"ItemType":2,"Max":1,"Min":1,"Weight":2}],"Name":"é\u2019»çŸ³ä¼˜æƒ åˆ¸å\u008f\u2018æ\u201d¾è§\u201eåˆ™","RuleType":2}]
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
                 * Items : [{"ItemId":1,"ItemType":1,"Max":1,"Min":1,"Weight":10}]
                 * Name : é“œä¼˜æƒ åˆ¸å‘æ”¾è§„åˆ™
                 * RuleType : 2
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
                     * ItemId : 1
                     * ItemType : 1
                     * Max : 1
                     * Min : 1
                     * Weight : 10
                     */

                    private int ItemId;
                    private int ItemType;
                    private int Max;
                    private int Min;
                    private int Weight;

                    public int getItemId() {
                        return ItemId;
                    }

                    public void setItemId(int ItemId) {
                        this.ItemId = ItemId;
                    }

                    public int getItemType() {
                        return ItemType;
                    }

                    public void setItemType(int ItemType) {
                        this.ItemType = ItemType;
                    }

                    public int getMax() {
                        return Max;
                    }

                    public void setMax(int Max) {
                        this.Max = Max;
                    }

                    public int getMin() {
                        return Min;
                    }

                    public void setMin(int Min) {
                        this.Min = Min;
                    }

                    public int getWeight() {
                        return Weight;
                    }

                    public void setWeight(int Weight) {
                        this.Weight = Weight;
                    }
                }
            }
        }

        public static class GiftsBean {
            /**
             * Id : 1
             * Name : é‡‘å®ç®±
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
    }
}
