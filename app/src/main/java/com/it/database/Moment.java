package com.it.database;

import android.content.Intent;

public class Moment {
    Integer id;
    String content;
    String image;
    String locating;
    String date;
    String ownerAccount;

    public String getOwnerAccount() {
        return ownerAccount;
    }

    public void setOwnerAccount(String ownerAccount) {
        this.ownerAccount = ownerAccount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Moment(Integer id, String content, String image, String locating, String date, String ownerAccount) {
        this.id = id;
        this.content = content;
        this.image = image;
        this.locating = locating;
        this.date = date;
        this.ownerAccount = ownerAccount;
    }

    public Moment(){

    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocating() {
        return locating;
    }

    public void setLocating(String locating) {
        this.locating = locating;
    }

}
