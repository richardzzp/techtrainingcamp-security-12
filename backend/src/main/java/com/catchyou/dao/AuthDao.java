package com.catchyou.dao;

import com.catchyou.pojo.Log;
import com.catchyou.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthDao {

    //注册成功时插入一条记录到表中
    Integer insertUser(User user);

    //根据用户名查找某一个用户
    User getUserByName(String username);

    //根据手机查找某一个用户
    User getUserByPhone(String phone);

    //插入一条登录记录
    Integer insertLog(Log log);

    //注销
    Integer setActiveFalse(User user);

    User getUserById(String Id);

    Log[] getLoginRecordById(String Id);

    Integer getMacRegisterCount(String deviceId);
}
