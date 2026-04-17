package com.learninglog.entity;

import java.sql.Timestamp;

public class User {
    private int id;
    private String username;
    private  String email;
    private String password;
    private Timestamp createdAt;
    private  Timestamp updatedat;


//    Constructor for Register
    public User(String username, String email ,String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }


//    This is for fetching data to database  constructor Overload
    public User(int id, String username , String email,String password, Timestamp createdAt ,Timestamp updatedat){
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedat = updatedat;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedat() {
        return updatedat;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    Note no setter for time cause time is given by System not by user
}
