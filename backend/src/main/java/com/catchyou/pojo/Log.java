package com.catchyou.pojo;

import java.util.Date;

public class Log {
    private Integer id;
    private String uid;
    private Date time;
    private String ip;
    private String deviceId;

    public Log() {
    }

    public Log(Integer id,
               String uid,
               Date time,
               String ip,
               String deviceId) {
        this.id = id;
        this.uid = uid;
        this.time = time;
        this.ip = ip;
        this.deviceId = deviceId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
