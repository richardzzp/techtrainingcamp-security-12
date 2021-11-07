package com.catchyou.controller;

import com.catchyou.pojo.CommonResult;
import com.catchyou.service.LjhVerifyCodeService;
import com.catchyou.service.impl.CnServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ljh")
@AllArgsConstructor
public class LjhAuthController {
    private final LjhVerifyCodeService verifyCodeService;
    private final CnServiceImpl cnService;

    /**
     * 获取验证码
     * @param requestBody
     * Type 指定该验证码是用来注册还是用来登录的 1表示登录 2表示注册
     * PhoneNumber    //手机号
     * Environment{
     *     IP         //IP
     *     DeviceID   //设备ID
     * }
     * @return
     * Code     //0表示请求成功，1表示请求失败
     * Message  //表示返回的说明，例如code=1时，message=“请求成功”
     * Data{
     *     VerifyCode     //6位随机的数字
     *     ExpireTime     //验证码过期的时间，例如有效期3分钟，这个时间可以自行设定
     *     DecisionType   //0表示正常下发验证码，1表示需要用户通过滑块验证，
     *                      通过后才能请求验证码，2表示需要用户过一段时间，才能重试获取验证码，3表示对这个用户不下发验证码
     * }
     */
    @PostMapping("/apply-code")
    public CommonResult<Map<String, Object>> applyCode(@RequestBody Map<String, Object> requestBody) {
        String phoneNumber = (String) requestBody.get("phoneNumber");
        Integer type = (Integer) requestBody.get("type");

        int code = 1;
        String message = "请求成功";
        String verifyCode = null;
        int expireTime = 180;
        int decisionType = 0;

        if (type == 1 && !cnService.checkPhoneExist(phoneNumber)) {
            message = "手机号不存在";
            decisionType = 3;
        } else if (type == 2 && cnService.checkPhoneExist(phoneNumber)) {
            message = "手机号已被注册";
            decisionType = 3;
        } else if (verifyCodeService.withinOneMinute(phoneNumber)) {
            message = "验证码请求过于频繁";
            decisionType = 2;
        } else if (verifyCodeService.isFrequentRequest(phoneNumber)) {
            message = "需要进行滑块验证";
            decisionType = 1;
            verifyCodeService.recountRequest(phoneNumber);
        } else {
            code = 0;
            verifyCode = verifyCodeService.generateVerifyCode(phoneNumber);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("verifyCode", verifyCode);
        data.put("expireTime", expireTime);
        data.put("decisionType", decisionType);
        return new CommonResult<>(code, message, data);
    }

    /**
     * 登录
     * @param requestBody
     * - 根据用户名登录的请求参数：
     * UserName
     * Password
     * Environment{
     *     IP
     *     DeviceID
     * }
     * - 根据手机号登录的请求参数：
     * PhoneNumber
     * VerifyCode
     * Environment{
     *     IP
     *     DeviceID
     * }
     * @return
     * Code      //0表示登录成功，1表示登录失败
     * Message   //表示返回的说明，例如code=1时，message=“用户名或者密码不对”
     * Data{
     *     SessionID
     *     ExpireTime
     *     DecisionType //0表示用户可以正常登录，1表示需要用户通过滑块验证，通过后才能登录，2表示需要用户过一段时间，才能重新登录，3表示这个用户不能登录
     * }
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, Object> requestBody) {
        return null;
    }

    /**
     * 注册
     * @param requestBody
     * UserName     //用户名
     * Password     //密码
     * PhoneNumber
     * VerifyCode
     * Environment{
     *     IP
     *     DeviceID
     * }
     * @return
     * Code      //0表示注册成功，1表示注册失败
     * Message   //表示返回的说明，例如code=1时，message=“相同的用户名已经被注册过了，请更换用户名试试”
     * SessionID //uuid
     * Data{
     *     SessionID    //随机的uuid
     *     ExpireTime   //过期时间，例如有效期3小时，这个时间可以自行设定
     *     DecisionType //0表示用户可以正常注册，1表示需要用户通过滑块验证，通过后才能注册，2表示需要用户过一段时间，才能重新注册，3表示这个用户不能注册
     * }
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, Object> requestBody) {
        return null;
    }

    /**
     * 登出或注销
     * @param requestBody
     * SessionID
     * ActionType  //1代表登出，2代表注销
     * Environment{
     *     IP
     *     DeviceID
     * }
     * @return
     * Code      //0表示登出或注销成功，1表示登出或注销失败
     * Message   //表示返回的说明，例如退出时，code=0，message=“退出成功”
     */
    @PostMapping("/logout")
    public Map<String, Object> logout(@RequestBody Map<String, Object> requestBody) {
        return null;
    }
}
