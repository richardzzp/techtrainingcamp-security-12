package com.catchyou.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String username;
    private String phoneNumber;
    private String password;
    private Date registerTime;
    private String registerIp;
    private String registerDeviceId;
    private Integer isActive;
}
