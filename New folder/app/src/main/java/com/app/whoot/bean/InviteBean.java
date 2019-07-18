package com.app.whoot.bean;

public class InviteBean {


    /**
     * email : xx@xx.com
     * id : 118623
     * inviter : 5428
     * lastloginTm : 1562740249575
     * name : hhhhh
     * notifyToken : c3AIzoKMdA4:APA91bEYEmu7-QhMgb8m48XLktHDPAMeM7PQkHqmQu8xnk_iPk1CAhWURnWzCOMmEcYAA7Hqu9SM3JY9px6taq9qNp6CKnlqK6GSK6XVLhh6PBHicYOSvqjNQSiNgi9J-jtFRi0qLV01
     * registTm : 1562740249575
     * type : 3
     * gender : female
     * photo : https://world-storage.whoot.com/on9zAo-f4exniNPeh70U9w==.png
     */

    private String email;
    private int id;
    private int inviter;
    private long lastloginTm;
    private String name;
    private String notifyToken;
    private long registTm;
    private int type;
    private String gender;
    private String photo;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInviter() {
        return inviter;
    }

    public void setInviter(int inviter) {
        this.inviter = inviter;
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

    public String getNotifyToken() {
        return notifyToken;
    }

    public void setNotifyToken(String notifyToken) {
        this.notifyToken = notifyToken;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
