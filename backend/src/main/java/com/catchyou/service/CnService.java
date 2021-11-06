package com.catchyou.service;

import com.catchyou.pojo.Log;
import com.catchyou.pojo.User;

public interface CnService {

    Boolean checkUsernameExist(String username);

    Boolean checkPhoneExist(String phone);

    String registerAfterCheck(User user);

    Integer checkUsernamePasswordMatch(String username, String password, String ip);

    String loginWithUsernameAfterCheck(String username, String ip, String deviceId);

    String loginWithPhoneAfterCheck(String phone, String ip, String deviceId);

    Boolean logout(String uid);

    Log[] getLoginRecordById(String uid);

    User getUserById(String uid);

}
