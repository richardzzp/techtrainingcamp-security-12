package com.catchyou.service;

public interface LjhVerifyCodeService {
    String generateVerifyCode(String phoneNumber);

    boolean checkVerifyCode(String phoneNumber, String code);
}
