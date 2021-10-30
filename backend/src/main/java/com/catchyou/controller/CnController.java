package com.catchyou.controller;

import com.catchyou.pojo.CommonResult;
import com.catchyou.pojo.User;
import com.catchyou.service.CnService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;

@RestController
public class CnController {

    @Resource
    private CnService cnService;

    @PostMapping("/cn/register")
    public CommonResult register(@RequestBody HashMap<String, Object> map) {
        try {
            //首先得判断验证码是否正确，先简单判断是否为123456
            String verifyCode = (String) map.get("verifyCode");
            if (!verifyCode.equals("123456")) {
                return new CommonResult(1, "验证码不正确");
            }
            //准备一个user准备插入到数据库中
            User user = new User();
            user.setUsername((String) map.get("username"));
            user.setPassword((String) map.get("password"));
            user.setPhoneNumber((String) map.get("phoneNumber"));
            user.setRegisterTime(new Date());
            HashMap<String, String> environment = (HashMap) map.get("environment");
            user.setRegisterIp(environment.get("ip"));
            user.setRegisterDeviceId(environment.get("deviceId"));
            //插入
            return cnService.register(user);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(1, "未知错误");
        }
    }
}
