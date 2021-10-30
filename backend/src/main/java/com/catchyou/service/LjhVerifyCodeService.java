package com.catchyou.service;

import com.catchyou.pojo.VerifyCode;

public interface LjhVerifyCodeService {
    VerifyCode generateVerifyCode(String phoneNumber);

    boolean checkVerifyCode(String phoneNumber, String code);
}
