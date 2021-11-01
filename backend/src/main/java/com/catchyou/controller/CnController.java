package com.catchyou.controller;

import com.catchyou.pojo.CommonResult;
import com.catchyou.pojo.User;
import com.catchyou.service.CnService;
import com.catchyou.service.impl.CnServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;

@RestController
public class CnController {

    @Resource
    private CnServiceImpl cnService;

    @PostMapping("/cn/register")
    public CommonResult register(@RequestBody HashMap<String, Object> map) {
        try {
            //判断验证码是否正确
            String verifyCode = (String) map.get("verifyCode");
            if (!cnService.checkVerifyCode(verifyCode)) {
                return new CommonResult(1, "验证码不正确，注册失败");
            }
            //判断用户名是否已经存在
            String username = (String) map.get("username");
            if (cnService.checkUsernameExist(username)) {
                return new CommonResult(1, "用户名重复了，注册失败");
            }
            //判断手机是否已经存在
            String phoneNumber = (String) map.get("phoneNumber");
            if (cnService.checkPhoneExist(phoneNumber)) {
                return new CommonResult(1, "手机号重复了，注册失败");
            }
            //验证码、用户名都没问题，就可以注册了
            //准备一个user进行注册
            User user = new User();
            user.setUsername((String) map.get("username"));
            user.setPassword((String) map.get("password"));
            user.setPhoneNumber((String) map.get("phoneNumber"));
            user.setRegisterTime(new Date());
            HashMap<String, String> environment = (HashMap) map.get("environment");
            user.setRegisterIp(environment.get("ip"));
            user.setRegisterDeviceId(environment.get("deviceId"));
            //进行注册
            String uuid = cnService.registerAfterCheck(user);
            if (uuid == null) {
                return new CommonResult(1, "未知错误，注册失败");
            }
            //需要返回的一些信息（目前不清楚具体用途，先在这里随便写着）
            HashMap<String, Object> data = new HashMap<>();
            data.put("sessionId", uuid); //先拿uuid混一下
            data.put("expireTime", 0);
            data.put("decisionType", 0);
            return new CommonResult(0, "注册成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(1, "未知错误，注册失败");
        }
    }

    @PostMapping("/cn/loginWithUsername")
    public CommonResult loginWithUsername(@RequestBody HashMap<String, Object> map) {
        try {
            //判断用户名是否已经存在，存在的话用户名和密码又是否匹配
            String username = (String) map.get("username");
            String password = (String) map.get("password");
            if (!cnService.checkUsernamePasswordMatch(username, password)) {
                return new CommonResult(1, "用户名或密码不正确，登录失败");
            }
            //提取ip和deviceId
            HashMap<String, String> environment = (HashMap) map.get("environment");
            String ip = environment.get("ip");
            String deviceId = environment.get("deviceId");
            //尝试进行登录
            String uuid = cnService.loginWithUsernameAfterCheck(username, ip, deviceId);
            //需要返回的一些信息（目前不清楚具体用途，先在这里随便写着）
            HashMap<String, Object> data = new HashMap<>();
            data.put("sessionId", uuid); //先拿uuid混一下
            data.put("expireTime", 0);
            data.put("decisionType", 0);
            return new CommonResult(0, "登录成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(1, "未知错误，登录失败");
        }
    }
}
