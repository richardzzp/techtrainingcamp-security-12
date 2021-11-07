package com.catchyou.service.impl;

import com.catchyou.dao.CnDao;
import com.catchyou.pojo.Log;
import com.catchyou.pojo.User;
import com.catchyou.service.CnService;
import com.catchyou.util.MyUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Service
public class CnServiceImpl implements CnService {

    @Resource
    private CnDao cnDao;

    @Resource
    private RedisTemplate redisTemplate;

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
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
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
        //风控信息
        String key = new StringBuilder().append(user.getUsername()).append("_login_cities").toString();
        redisTemplate.opsForSet().add(key, MyUtil.getCityFromIp(user.getRegisterIp()));
        key = new StringBuilder().append(user.getUsername()).append("_login_devices").toString();
        redisTemplate.opsForSet().add(key, user.getRegisterDeviceId());
        //插入成功返回uuid
        return uuid;
    }

    @Override
    //返回 0 表示匹配成功
    //返回 1 表示用户名不存在，无惩罚机制
    //返回 2 表示匹配失败，无惩罚机制
    //返回 3 表示匹配失败，用户1分钟内无法再尝试（针对于某个ip地址）
    //返回 4 表示匹配失败，用户5分钟内无法再尝试（针对于某个ip地址）
    //返回 5 表示匹配失败，禁止用户登录（针对于某个ip地址）
    public Integer checkUsernamePasswordMatch(String username, String password, String ip) {
        User user = cnDao.getUserByName(username);
        if (user == null) {
            return 1;
        }
        String key = null;
        if (!BCrypt.checkpw(password, user.getPassword())) {
            key = new StringBuilder().append(username).append("_").append(ip)
                    .append("_wrong_pwd_count").toString();
            if (!redisTemplate.hasKey(key)) {
                redisTemplate.opsForValue().set(key, 1);
            } else {
                redisTemplate.opsForValue().increment(key);
            }
            Integer count = (Integer) redisTemplate.opsForValue().get(key);
            //如果错了5次，那么1分钟内不允许用户再尝试
            if (count == 5) {
                return 3;
            }
            //如果错了10次，那么5分钟内不允许用户再尝试
            if (count == 10) {
                return 4;
            }
            //如果错了15次，那么封号处理（针对这个ip的封号）
            if (count == 15) {
                return 5;
            }
            return 2;
        }
        //一旦登录成功，那么需要把风控信息清除
        if (key != null) {
            redisTemplate.delete(key);
        }
        return 0;
    }

    @Override
    public String loginWithUsernameAfterCheck(String username, String ip, String deviceId) {
        User user = cnDao.getUserByName(username);
        //登录记录
        Log log = new Log(null, user.getId(), new Date(), ip, deviceId);
        cnDao.insertLog(log);
        //风控信息
        String key = new StringBuilder().append(username).append("_login_cities").toString();
        redisTemplate.opsForSet().add(key, MyUtil.getCityFromIp(ip));
        key = new StringBuilder().append(username).append("_login_devices").toString();
        redisTemplate.opsForSet().add(key, deviceId);
        return user.getId();
    }

    @Override
    public String loginWithPhoneAfterCheck(String phone, String ip, String deviceId) {
        User user = cnDao.getUserByPhone(phone);
        //登录记录
        Log log = new Log(null, user.getId(), new Date(), ip, deviceId);
        cnDao.insertLog(log);
        //风控信息
        String key = new StringBuilder().append(user.getUsername()).append("_login_cities").toString();
        redisTemplate.opsForSet().add(key, MyUtil.getCityFromIp(ip));
        key = new StringBuilder().append(user.getUsername()).append("_login_devices").toString();
        redisTemplate.opsForSet().add(key, deviceId);
        return user.getId();
    }

    @Override
    public Boolean logout(String uid) {
        User user = cnDao.getUserById(uid);
        if (user == null) {
            return false;
        }
        cnDao.setActiveFalse(user);
        //一些风控信息的清除
        String key = new StringBuilder().append(user.getUsername()).append("_login_cities").toString();
        redisTemplate.delete(key);
        key = new StringBuilder().append(user.getUsername()).append("_login_devices").toString();
        redisTemplate.delete(key);
        return true;
    }

    @Override
    public Log[] getLoginRecordById(String uid) {
        return cnDao.getLoginRecordById(uid);
    }

    @Override
    public User getUserById(String uid) {
        return cnDao.getUserById(uid);
    }

    @Override
    //规定，一个设备最多只能注册五个账号
    public Boolean checkRubbishRegister(String deviceId) {
        Integer count = cnDao.getMacRegisterCount(deviceId);
        if (count >= 5) {
            return true;
        }
        return false;
    }

    @Override
    //如果使用密码登录，那么需要进行异地检测
    public Boolean checkRemoteLogin(String username, String ip, String deviceId) {
        String key = new StringBuilder().append(username).append("_login_devices").toString();
        //如果不是信任的设备才需要检测
        if (!redisTemplate.opsForSet().isMember(key, deviceId)) {
            String city = MyUtil.getCityFromIp(ip);
            key = new StringBuilder().append(username).append("_login_cities").toString();
            //如果不是信任的ip地址
            if (!redisTemplate.opsForSet().isMember(key, city)) {
                return true;
            }
        }
        return false;
    }
}
