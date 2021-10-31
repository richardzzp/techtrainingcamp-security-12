package com.catchyou.service;

import com.catchyou.dao.UserDao;
import com.catchyou.pojo.CommonResult;
import com.catchyou.pojo.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public CommonResult<Map<String, Object>> register(User user) {
        //判断有无重复的用户名
        User userByName = userDao.getUserByName(user.getUsername());
        if (userByName != null) {
            return new CommonResult<>(1, "用户名重复了");
        }
        //密码需要加密
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()).substring(0, 50));
        //为用户设置一个uuid
        String uuid = UUID.randomUUID().toString();
        user.setId(uuid);
        //插入到数据库中
        System.out.println(user);
        userDao.insertUser(user);
        //需要返回的一些信息（目前不清楚具体用途，先在这里随便写着）
        HashMap<String, Object> data = new HashMap<>();
        data.put("sessionId", uuid);
        data.put("expireTime", 0);
        data.put("decisionType", 0);
        return new CommonResult<>(0, "注册成功", data);
    }
}
