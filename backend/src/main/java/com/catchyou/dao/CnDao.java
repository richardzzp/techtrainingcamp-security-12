package com.catchyou.dao;

import com.catchyou.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CnDao {
    //注册成功时插入一条记录到表中
    void insertUser(User user);
    //根据用户名查找某一个用户
    User getUserByName(String username);
}
