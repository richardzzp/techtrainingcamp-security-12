package com.catchyou.controller;

import com.catchyou.pojo.CommonResult;
import com.catchyou.pojo.User;
import com.catchyou.service.impl.CnServiceImpl;
import com.catchyou.service.impl.LjhVerifyCodeServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;

@RestController
public class CnController {

    @Resource
    private CnServiceImpl cnService;

    @Resource
    private LjhVerifyCodeServiceImpl ljhVerifyCodeService;

    @Resource
    private RedisTemplate redisTemplate;

    @PostMapping("/cn/register")
    public CommonResult register(@RequestBody HashMap<String, Object> map) {
        try {
            //判断手机号是否重复（发验证码的时候其实也做过了）
            String phoneNumber = (String) map.get("phoneNumber");
            if (cnService.checkPhoneExist(phoneNumber)) {
                return new CommonResult(1, "手机号重复了，注册失败");
            }
            //判断手机验证码是否正确
            String verifyCode = (String) map.get("verifyCode");
            if (!ljhVerifyCodeService.checkVerifyCode(phoneNumber, verifyCode)) {
                return new CommonResult(2, "验证码不正确，注册失败");
            }
            //判断用户名是否重复
            String username = (String) map.get("username");
            if (cnService.checkUsernameExist(username)) {
                return new CommonResult(3, "用户名重复了，注册失败");
            }
            //判断是否为垃圾注册
            HashMap<String, String> environment = (HashMap) map.get("environment");
            String deviceId = environment.get("deviceId");
            if (cnService.checkRubbishRegister(deviceId)) {
                return new CommonResult(4, "该设备已经注册过多账号，请注销部分非常用账号后再试");
            }
            //验证码、用户名都没问题，就可以注册了
            //准备一个user进行注册
            User user = new User();
            user.setUsername((String) map.get("username"));
            user.setPassword((String) map.get("password"));
            user.setPhoneNumber((String) map.get("phoneNumber"));
            user.setRegisterTime(new Date());
            user.setRegisterIp(environment.get("ip"));
            user.setRegisterDeviceId(environment.get("deviceId"));
            //进行注册
            String uuid = cnService.registerAfterCheck(user);
            if (uuid == null) {
                return new CommonResult(5, "未知错误，注册失败");
            }
            //需要返回的一些信息（目前不清楚具体用途，先在这里随便写着）
            HashMap<String, Object> data = new HashMap<>();
            data.put("sessionId", uuid); //先拿uuid混一下
            data.put("expireTime", 0);
            data.put("decisionType", 0);
            return new CommonResult(0, "注册成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(5, "未知错误，注册失败");
        }
    }

    @PostMapping("/cn/loginWithUsername")
    public CommonResult loginWithUsername(@RequestBody HashMap<String, Object> map) {
        try {
            //提取ip和deviceId
            HashMap<String, String> environment = (HashMap) map.get("environment");
            String ip = environment.get("ip");
            String deviceId = environment.get("deviceId");
            //判断用户名是否已经存在，存在的话用户名和密码又是否匹配
            String username = (String) map.get("username");
            String password = (String) map.get("password");
            Integer res = cnService.checkUsernamePasswordMatch(username, password, ip);
            if (res == 1) {
                return new CommonResult(1, "该用户名不存在");
            }
            if (res == 2) {
                return new CommonResult(2, "密码错误");
            }
            if (res == 3) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("banTime", 1 * 60 * 1000);
                data.put("decisionType", 2);
                return new CommonResult(3, "已经5次密码错误，1分钟内禁止尝试", data);
            }
            if (res == 4) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("banTime", 5 * 60 * 1000);
                data.put("decisionType", 2);
                return new CommonResult(4, "已经10次密码错误，5分钟内禁止尝试", data);
            }
            if (res == 5) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("banTime", -1);
                data.put("decisionType", 2);
                //此时还应短信通知用户账号存在风险
                return new CommonResult(5, "已经15次密码错误，不再允许新的尝试", data);
            }

            //判断是不是异地登录
            if (cnService.checkRemoteLogin(username, ip, deviceId)) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("decisionType", 0);
                return new CommonResult(6, "异地登录，请使用手机号登录", data);
            }

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
            return new CommonResult(7, "未知错误，登录失败");
        }
    }

    @PostMapping("/cn/loginWithPhone")
    public CommonResult loginWithPhone(@RequestBody HashMap<String, Object> map) {
        try {
            //判断手机号是否存在（发验证码的时候其实也做过了）
            String phoneNumber = (String) map.get("phoneNumber");
            if (!cnService.checkPhoneExist(phoneNumber)) {
                return new CommonResult(1, "手机号不存在");
            }
            //判断手机验证码是否正确
            String verifyCode = (String) map.get("verifyCode");
            if (!ljhVerifyCodeService.checkVerifyCode(phoneNumber, verifyCode)) {
                return new CommonResult(2, "验证码不正确");
            }
            //提取ip和deviceId
            HashMap<String, String> environment = (HashMap) map.get("environment");
            String ip = environment.get("ip");
            String deviceId = environment.get("deviceId");
            //尝试进行登录
            String uuid = cnService.loginWithPhoneAfterCheck(phoneNumber, ip, deviceId);
            //需要返回的一些信息（目前不清楚具体用途，先在这里随便写着）
            HashMap<String, Object> data = new HashMap<>();
            data.put("sessionId", uuid); //先拿uuid混一下
            data.put("expireTime", 0);
            data.put("decisionType", 0);
            return new CommonResult(0, "登录成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(3, "未知错误，登录失败");
        }
    }

    @PostMapping("/cn/logout")
    public CommonResult logout(@RequestBody HashMap<String, Object> map) {
        try {
            //提取信息
            String sessionId = (String) map.get("sessionId"); //目前当作uid处理
            String actionType = (String) map.get("actionType");

            //登出目前不作处理
            if (actionType.equals("1")) {
                return new CommonResult(0, "登出成功");
            }

            //注销
            if (actionType.equals("2")) {
                Boolean res = cnService.logout(sessionId);
                if (!res) {
                    return new CommonResult(1, "注销失败");
                }
                return new CommonResult(0, "注销成功");
            }

            return new CommonResult(2, "不正确的actionType");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(3, "未知错误");
        }
    }

    @PostMapping("/cn/getLoginRecord")
    public CommonResult getLoginRecord(@RequestBody HashMap<String, Object> map) {
        try {
            //提取信息
            String sessionId = (String) map.get("sessionId"); //目前当作uid处理
            return new CommonResult(0, "请求成功", cnService.getLoginRecordById(sessionId));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(1, "未知错误");
        }
    }

    @PostMapping("/cn/getUser")
    public CommonResult getUser(@RequestBody HashMap<String, Object> map) {
        try {
            //提取信息
            String sessionId = (String) map.get("sessionId"); //目前当作uid处理
            return new CommonResult(0, "请求成功", cnService.getUserById(sessionId));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(1, "未知错误");
        }
    }

    @GetMapping("/cn/delete/{username}/{ip}")
    public Integer delete(@PathVariable String username,
                          @PathVariable String ip) {
        try {
            String key = new StringBuilder().append(ip).append("_request_count").toString();
            redisTemplate.delete(key);
            key = new StringBuilder().append(username).append("_block_count").toString();
            redisTemplate.delete(key);
            redisTemplate.opsForSet().remove("ip_black_list", ip);
            key = new StringBuilder().append(username).append("_").append(ip)
                    .append("_wrong_pwd_count").toString();
            redisTemplate.delete(key);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
