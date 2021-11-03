package com.catchyou.service;

import com.catchyou.dao.UserDao;
import com.catchyou.pojo.Log;
import com.catchyou.pojo.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Boolean checkUsernameExist(String username) {
        User user = userDao.getUserByName(username);
        if (user != null) {
            return true;
        }
        return false;
    }

    public Boolean checkPhoneExist(String phone) {
        User user = userDao.getUserByPhone(phone);
        if (user != null) {
            return true;
        }
        return false;
    }

    public String registerAfterCheck(User user) {
        //密码需要加密
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        //为用户设置一个uuid
        String uuid = UUID.randomUUID().toString();
        user.setId(uuid);
        //插入到数据库中
        System.out.println(user);
        Integer res = userDao.insertUser(user);
        if (res == 0) {
            //插入失败返回null
            return null;
        }
        //登录记录
        Log log = new Log(null, user.getId(), new Date(), user.getRegisterIp(), user.getRegisterDeviceId());
        userDao.insertLog(log);
        //插入成功返回uuid
        return uuid;
    }

    public Boolean checkUsernamePasswordMatch(String username, String password) {
        User user = userDao.getUserByName(username);
        if (user == null) {
            return false;
        }
        if (!BCrypt.checkpw(password, user.getPassword())) {
            return false;
        }
        return true;
    }

    public String loginWithUsernameAfterCheck(String username, String ip, String deviceId) {
        User user = userDao.getUserByName(username);
        //登录记录
        Log log = new Log(null, user.getId(), new Date(), ip, deviceId);
        userDao.insertLog(log);
        return user.getId();
    }

    public String loginWithPhoneAfterCheck(String phone, String ip, String deviceId) {
        User user = userDao.getUserByPhone(phone);
        //登录记录
        Log log = new Log(null, user.getId(), new Date(), ip, deviceId);
        userDao.insertLog(log);
        return user.getId();
    }

    public Boolean logout(String uid) {
        User user = userDao.getUserById(uid);
        Integer res = userDao.setActiveFalse(user);
        if (res == 0) {
            return false;
        }
        return true;
    }

    public Log[] getLoginRecordById(String uid) {
        return userDao.getLoginRecordById(uid);
    }

    public User getUserById(String uid) {
        return userDao.getUserById(uid);
    }
}
