package com.catchyou.pojo;

import java.util.Date;

public class User {
    private String id;
    private String username;
    private String phoneNumber;
    private String password;
    private Date registerTime;
    private String registerIp;
    private String registerDeviceId;
    private Integer isActive;

    public User() {
    }

    public User(String id,
                String username,
                String phoneNumber,
                String password,
                Date registerTime,
                String registerIp,
                String registerDeviceId,
                Integer isActive) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.registerTime = registerTime;
        this.registerIp = registerIp;
        this.registerDeviceId = registerDeviceId;
        this.isActive = isActive;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public String getRegisterDeviceId() {
        return registerDeviceId;
    }

    public void setRegisterDeviceId(String registerDeviceId) {
        this.registerDeviceId = registerDeviceId;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
}
