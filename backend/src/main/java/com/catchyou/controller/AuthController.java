package com.catchyou.controller;

import com.catchyou.pojo.CommonResult;
import com.catchyou.pojo.User;
import com.catchyou.pojo.VerifyCode;
import com.catchyou.service.UserService;
import com.catchyou.service.VerifyCodeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户认证控制层
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final VerifyCodeService verifyCodeService;
    private final UserService userService;

    /**
     * 获取验证码
     * @param requestBody
     * phoneNumber    //手机号
     * environment{
     *     ip         //IP
     *     deviceId   //设备ID
     * }
     * @return
     * code     //0表示请求成功，1表示请求失败
     * message  //表示返回的说明，例如code=1时，message=“请求失败”
     * data{
     *     verifyCode     //6位随机的数字
     *     expireTime     //验证码过期的时间，例如有效期3分钟，这个时间可以自行设定
     *     decisionType   //0表示正常下发验证码，1表示需要用户通过滑块验证，通过后才能请求验证码，2表示需要用户过一段时间，才能重试获取验证码，3表示对这个用户不下发验证码
     * }
     */
    @PostMapping("/apply-code")
    public CommonResult<Map<String, Object>> applyCode(@RequestBody Map<String, Object> requestBody) {
        String phoneNumber = (String) requestBody.get("phoneNumber");
        Map<String, Object> environment = (Map<String, Object>) requestBody.get("environment");
        String ip = (String) requestBody.get("ip");
        String deviceId = (String) requestBody.get("deviceId");

        String verifyCode = verifyCodeService.generateVerifyCode(phoneNumber);
        int code = verifyCode != null ? 0 : 1;
        String message = code == 0 ? "请求成功" : "请求失败";
        int decisionType = verifyCode != null ? 0 : 2;

        CommonResult<Map<String, Object>> responseBody = new CommonResult<>(code, message);
        Map<String, Object> data = new HashMap<>();
        data.put("verifyCode", verifyCode);
        data.put("expireTime", 3 * 60);
        data.put("decisionType", decisionType);
        responseBody.setData(data);

        return responseBody;
    }

    /**
     * 登录
     * @param map
     * - 根据用户名登录的请求参数：
     * userName
     * password
     * environment{
     *     ip
     *     deviceId
     * }
     * - 根据手机号登录的请求参数：
     * phoneNumber
     * verifyCode
     * environment{
     *     ip
     *     deviceId
     * }
     * @return
     * code      //0表示登录成功，1表示登录失败
     * message   //表示返回的说明，例如code=1时，message=“用户名或者密码不对”
     * data{
     *     sessionId
     *     expireTime
     *     decisionType //0表示用户可以正常登录，1表示需要用户通过滑块验证，通过后才能登录，2表示需要用户过一段时间，才能重新登录，3表示这个用户不能登录
     * }
     */
    @PostMapping("/login-with-username")
    public CommonResult loginWithUsername(@RequestBody HashMap<String, Object> map) {
        try {
            //判断用户名是否已经存在，存在的话用户名和密码又是否匹配
            String username = (String) map.get("username");
            String password = (String) map.get("password");
            if (!userService.checkUsernamePasswordMatch(username, password)) {
                return new CommonResult(1, "用户名或密码不正确，登录失败");
            }
            //提取ip和deviceId
            HashMap<String, String> environment = (HashMap) map.get("environment");
            String ip = environment.get("ip");
            String deviceId = environment.get("deviceId");
            //尝试进行登录
            String uuid = userService.loginWithUsernameAfterCheck(username, ip, deviceId);
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

    @PostMapping("/login-with-phone")
    public CommonResult loginWithPhone(@RequestBody HashMap<String, Object> map) {
        try {
            //判断手机号是否已经存在（在发送验证码的时候其实就做过这一步了）
            String phoneNumber = (String) map.get("phoneNumber");
            if (!userService.checkPhoneExist(phoneNumber)) {
                return new CommonResult(1, "手机号不存在");
            }
            //判断手机验证码是否正确
            String verifyCode = (String) map.get("verifyCode");
            if (!verifyCodeService.checkVerifyCode(phoneNumber, verifyCode)) {
                return new CommonResult(1, "验证码不正确");
            }
            //提取ip和deviceId
            HashMap<String, String> environment = (HashMap) map.get("environment");
            String ip = environment.get("ip");
            String deviceId = environment.get("deviceId");
            //尝试进行登录
            String uuid = userService.loginWithPhoneAfterCheck(phoneNumber, ip, deviceId);
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

    /**
     * 注册
     * @param requestBody
     * userName     //用户名
     * password     //密码
     * phoneNumber
     * verifyCode
     * environment{
     *     ip
     *     deviceId
     * }
     * @return
     * code      //0表示注册成功，1表示注册失败
     * message   //表示返回的说明，例如code=1时，message=“相同的用户名已经被注册过了，请更换用户名试试”
     * sessionId //uuid
     * data{
     *     sessionId    //随机的uuid
     *     expireTime   //过期时间，例如有效期3小时，这个时间可以自行设定
     *     decisionType //0表示用户可以正常注册，1表示需要用户通过滑块验证，通过后才能注册，2表示需要用户过一段时间，才能重新注册，3表示这个用户不能注册
     * }
     */
    @PostMapping("/register")
    public CommonResult<Map<String, Object>> register(@RequestBody Map<String, Object> requestBody) {
        try {
            //判断手机是否已经存在（理应在发送验证码的时候就已经做了）
            String phoneNumber = (String) requestBody.get("phoneNumber");
            if (userService.checkPhoneExist(phoneNumber)) {
                return new CommonResult(1, "手机号重复了，注册失败");
            }
            //判断手机验证码是否正确
            String verifyCode = (String) requestBody.get("verifyCode");
            if (!verifyCodeService.checkVerifyCode(phoneNumber, verifyCode)) {
                return new CommonResult(1, "验证码不正确，注册失败");
            }
            //判断用户名是否已经存在
            String username = (String) requestBody.get("username");
            if (userService.checkUsernameExist(username)) {
                return new CommonResult(1, "用户名重复了，注册失败");
            }
            //验证码、用户名都没问题，就可以注册了
            //准备一个user进行注册
            User user = new User();
            user.setUsername((String) requestBody.get("username"));
            user.setPassword((String) requestBody.get("password"));
            user.setPhoneNumber((String) requestBody.get("phoneNumber"));
            user.setRegisterTime(new Date());
            HashMap<String, String> environment = (HashMap) requestBody.get("environment");
            user.setRegisterIp(environment.get("ip"));
            user.setRegisterDeviceId(environment.get("deviceId"));
            //进行注册
            String uuid = userService.registerAfterCheck(user);
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

    /**
     * 登出或注销
     * @param map
     * sessionId
     * actionType  //1代表登出，2代表注销
     * environment{
     *     ip
     *     deviceId
     * }
     * @return
     * code      //0表示登出或注销成功，1表示登出或注销失败
     * message   //表示返回的说明，例如退出时，code=0，message=“退出成功”
     */
    @PostMapping("/logout")
    public CommonResult logout(@RequestBody HashMap<String, Object> map) {
        try {
            //提取信息
            String sessionId = (String) map.get("sessionId"); //目前当作uid处理
            String actionType = (String) map.get("actionType");
            HashMap<String, String> environment = (HashMap) map.get("environment");
            String ip = environment.get("ip");
            String deviceId = environment.get("deviceId");

            //登出目前不作处理
            if (actionType.equals("1")) {
                return new CommonResult(0, "登出成功");
            }

            //注销
            if (actionType.equals("2")) {
                Boolean res = userService.logout(sessionId);
                if (!res) {
                    return new CommonResult(1, "注销失败");
                }
                return new CommonResult(0, "注销成功");
            }

            return new CommonResult(1, "不正确的actionType");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(1, "未知错误");
        }
    }

    @PostMapping("/get-login-record")
    public CommonResult getLoginRecord(@RequestBody HashMap<String, Object> map) {
        try {
            //提取信息
            String sessionId = (String) map.get("sessionId"); //目前当作uid处理
            HashMap<String, String> environment = (HashMap) map.get("environment");
            String ip = environment.get("ip");
            String deviceId = environment.get("deviceId");
            return new CommonResult(0, "请求成功", userService.getLoginRecordById(sessionId));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(1, "未知错误");
        }
    }

    @PostMapping("/get-user")
    public CommonResult getUser(@RequestBody HashMap<String, Object> map) {
        try {
            //提取信息
            String sessionId = (String) map.get("sessionId"); //目前当作uid处理
            HashMap<String, String> environment = (HashMap) map.get("environment");
            String ip = environment.get("ip");
            String deviceId = environment.get("deviceId");
            return new CommonResult(0, "请求成功", userService.getUserById(sessionId));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(1, "未知错误");
        }
    }

    public AuthController(VerifyCodeService verifyCodeService, UserService userService) {
        this.verifyCodeService = verifyCodeService;
        this.userService = userService;
    }
}
