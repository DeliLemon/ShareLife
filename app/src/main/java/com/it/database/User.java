package com.it.database;

public class User {
    String account;
    String password;
    String name;
    String personality;
    String phone;

    public User() {
    }

    public User(String account, String password, String name, String personality, String phone) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.personality = personality;
        this.phone = phone;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
