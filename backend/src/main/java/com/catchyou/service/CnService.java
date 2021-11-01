package com.catchyou.service;

import com.catchyou.pojo.CommonResult;
import com.catchyou.pojo.User;

public interface CnService {

    Boolean checkVerifyCode(String code);

    Boolean checkUsernameExist(String username);

    Boolean checkPhoneExist(String phone);

    String registerAfterCheck(User user);

    Boolean checkUsernamePasswordMatch(String username, String password);

    String loginWithUsernameAfterCheck(String username, String ip, String deviceId);
}
