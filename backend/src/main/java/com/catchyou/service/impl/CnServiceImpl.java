package com.catchyou.service.impl;

import com.catchyou.dao.CnDao;
import com.catchyou.pojo.CommonResult;
import com.catchyou.pojo.Log;
import com.catchyou.pojo.User;
import com.catchyou.service.CnService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Service
public class CnServiceImpl implements CnService {

    @Resource
    private CnDao cnDao;

    @Override
    //理应通过redis来验证，现在先固定死123456
    public Boolean checkVerifyCode(String phone, String code) {
        if (code.equals("123456")) {
            return true;
        }
        return false;
    }

    @Override
    //判断用户名是否已经存在
    public Boolean checkUsernameExist(String username) {
        User user = cnDao.getUserByName(username);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkPhoneExist(String phone) {
        User user = cnDao.getUserByPhone(phone);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Override
    public String registerAfterCheck(User user) {
        //密码需要加密
        //user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()).substring(0, 50));
        user.setPassword(BCrypt.hashpw(user.getPassword(), "$2a$10$U79It..3Pdw2dGEx16t1Te").substring(0, 50));
        //为用户设置一个uuid
        String uuid = UUID.randomUUID().toString();
        user.setId(uuid);
        //插入到数据库中
        System.out.println(user);
        Integer res = cnDao.insertUser(user);
        if (res == 0) {
            //插入失败返回null
            return null;
        }
        //登录记录
        Log log = new Log(null, user.getId(), new Date(), user.getRegisterIp(), user.getRegisterDeviceId());
        cnDao.insertLog(log);
        //插入成功返回uuid
        return uuid;
    }

    @Override
    public Boolean checkUsernamePasswordMatch(String username, String password) {
        User user = cnDao.getUserByName(username);
        if (user == null) {
            return false;
        }
        String encode = BCrypt.hashpw(password, "$2a$10$U79It..3Pdw2dGEx16t1Te").substring(0, 50);
        if (!encode.equals(user.getPassword())) {
            return false;
        }
        return true;
    }

    @Override
    public String loginWithUsernameAfterCheck(String username, String ip, String deviceId) {
        User user = cnDao.getUserByName(username);
        //登录记录
        Log log = new Log(null, user.getId(), new Date(), ip, deviceId);
        cnDao.insertLog(log);
        return user.getId();
    }

    @Override
    public String loginWithPhoneAfterCheck(String phone, String ip, String deviceId) {
        User user = cnDao.getUserByPhone(phone);
        //登录记录
        Log log = new Log(null, user.getId(), new Date(), ip, deviceId);
        cnDao.insertLog(log);
        return user.getId();
    }
}
