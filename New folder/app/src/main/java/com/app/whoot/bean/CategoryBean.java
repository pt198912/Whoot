package com.app.whoot.bean;

import java.util.List;

/**
 * Created by Sunrise on 5/10/2018.
 */

public class CategoryBean {


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
