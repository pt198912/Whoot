package com.app.whoot.bean;

/**
 * Created by Sunrise on 5/2/2018.
 */

public class FavBean {


    /**
     * closingSoon : false
     * commentCount : 0
     * distance : 597.0
     * grade : 0.0
     * id : 17
     * imgUrl : http://whoot-1251007673.cosgz.myqcloud.com/upload/4JSScMHQm9c6UBKAu0vLbw==.jpeg
     * name : 小丸子餐廳
     */

    private boolean closingSoon;
    private int commentCount;
    private double distance;
    private double grade;
    private int id;
    private String imgUrl;
    private String name;

    public boolean isClosingSoon() {
        return closingSoon;
    }

    public void setClosingSoon(boolean closingSoon) {
        this.closingSoon = closingSoon;
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
}
