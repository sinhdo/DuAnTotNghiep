package com.example.duantotnghiep.model;

import java.util.List;
import java.util.Map;

public class User {
    private String id,username,password,email,address,phone,wallet;
    private String img;
    private Boolean user_type;
    private Map<String, Location> location;  // Thay vì List<Location>
    public Boolean getUser_type() {
        return user_type;
    }

    public void setUser_type(Boolean user_type) {
        this.user_type = user_type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public User() {
    }

    public Map<String, Location> getLocation() {
        return location;
    }

    public void setLocation(Map<String, Location> location) {
        this.location = location;
    }

    public User(String id, String username, String password, String email, String address, String phone, String wallet, String img, Boolean user_type,Map<String, Location> location) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.wallet = wallet;
        this.img = img;
        this.user_type = user_type;
        this.location = location;
    }
}
