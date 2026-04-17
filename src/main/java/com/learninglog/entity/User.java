package com.learninglog.entity;

import java.security.PrivateKey;
import java.sql.Timestamp;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private Timestamp createdAt;
    private Timestamp updatedAt;


    private String phone;
    private String role;
    private boolean isactive;




//    Constructor for Register
    public User(String username, String email ,String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }


//    This is for fetching data to database  constructor Overload
    public User(int id, String username , String email,String password,String phone, String role ,boolean isactive, Timestamp createdAt ,Timestamp updatedat){
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedat;
        this.phone = phone;
        this.role = role;
        this.isactive = isactive;
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
        return updatedAt;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public boolean isIsactive() {
        return isactive;
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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }
    //    Note no setter for time cause time is given by System not by user
}
