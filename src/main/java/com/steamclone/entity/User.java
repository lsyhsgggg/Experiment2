package com.steamclone.entity;

import java.util.Date;

public class User {
    private int userId;
    private String username;
    private String password;
    private String email;
    private Date registerDate;
    private String avatarUrl;

    // 构造方法
    public User() {}

    public User(int userId, String username, String password, String email, Date registerDate, String avatarUrl) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.registerDate = registerDate;
        this.avatarUrl = avatarUrl;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}