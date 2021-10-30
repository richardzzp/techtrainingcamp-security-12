package com.catchyou.service;

import com.catchyou.pojo.CommonResult;
import com.catchyou.pojo.User;

import java.security.NoSuchAlgorithmException;

public interface CnService {
    CommonResult register(User user) throws NoSuchAlgorithmException;
}
