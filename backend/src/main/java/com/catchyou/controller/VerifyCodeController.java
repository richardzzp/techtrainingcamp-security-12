package com.catchyou.controller;

import com.catchyou.pojo.CommonResult;
import com.catchyou.service.AuthService;
import com.catchyou.service.VerifyCodeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/verifyCode")
@AllArgsConstructor
public class VerifyCodeController {
    private final VerifyCodeService verifyCodeServiceImpl;
    private final AuthService authServiceImpl;

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
    @PostMapping("/applyCode")
    public CommonResult<Map<String, Object>> applyCode(@RequestBody Map<String, Object> requestBody) {
        String phoneNumber = (String) requestBody.get("phoneNumber");
        Integer type = (Integer) requestBody.get("type");

        int code = 1;
        String message = "请求成功";
        String verifyCode = null;
        int expireTime = 180;
        int decisionType = 0;
        if (type == 1 && !authServiceImpl.checkPhoneExist(phoneNumber)) {
            message = "手机号不存在";
            return new CommonResult<>(code, message);
        } else if (type == 2 && authServiceImpl.checkPhoneExist(phoneNumber)) {
            message = "手机号已被注册";
            return new CommonResult<>(code, message);
        } else if (verifyCodeServiceImpl.withinOneMinute(phoneNumber)) {
            message = "验证码请求过于频繁";
            decisionType = 2;
        } else if (verifyCodeServiceImpl.isFrequentRequest(phoneNumber)) {
            message = "需要进行滑块验证";
            decisionType = 1;
            verifyCodeServiceImpl.recountRequest(phoneNumber);
        } else {
            code = 0;
            verifyCode = verifyCodeServiceImpl.generateVerifyCode(phoneNumber);
        }

        System.out.println("验证码为" + verifyCode);

        Map<String, Object> data = new HashMap<>();
        data.put("verifyCode", verifyCode);
        data.put("expireTime", expireTime);
        data.put("decisionType", decisionType);
        return new CommonResult<>(code, message, data);
    }
}
