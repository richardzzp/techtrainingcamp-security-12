package com.catchyou.service.impl;

import com.catchyou.service.LjhVerifyCodeService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

/**
 * 处理手机验证码的业务逻辑层
 * EXPIRE_TIME: 验证码的有效期，默认为3分钟
 * LIMIT_TIME: 限制一分钟内只能发送一次短信
 */
@Service
public class LjhVerifyCodeServiceImpl implements LjhVerifyCodeService {
    private static final Duration EXPIRE_TIME = Duration.ofMinutes(3);
    private static final Duration LIMIT_TIME = Duration.ofMinutes(1);

    private final RedisTemplate<String, Object> redisTemplate;

    public LjhVerifyCodeServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 生成随机的验证码
     * @param phoneNumber 请求验证码的手机号
     * @return 随机验证码
     */
    public String generateVerifyCode(String phoneNumber) {
        //检测是否在一分钟内进行了两次验证码请求
        Integer isLimit = (Integer) redisTemplate.opsForValue().get("verify_code_limit_" + phoneNumber);
        if (isLimit != null) {
            return null;
        }

        //生成随机6位验证码
        Random random = new Random(System.currentTimeMillis());
        String verifyCode = String.format("%06d", random.nextInt(1000000));
        //存储验证码，有效期为3分钟
        redisTemplate.opsForValue().set("verify_code_" + phoneNumber, verifyCode, EXPIRE_TIME);
        //记录请求了验证码的手机号，防止1分钟内进行多次验证码请求
        redisTemplate.opsForValue().set("verify_code_limit_" + phoneNumber, 1, LIMIT_TIME);
        return verifyCode;
    }

    /**
     * 检查验证码是否有效
     * @param phoneNumber 手机号
     * @param code 用户输入的验证码
     * @return 若验证码有效则返回true，否则返回false
     */
    public boolean checkVerifyCode(String phoneNumber, String code) {
        //获取正确的验证码
        String key = "verify_code_" + phoneNumber;
        String validVerifyCode = (String) redisTemplate.opsForValue().get(key);
        //若验证码不存在或已过期，则验证失败
        if (validVerifyCode == null) {
            return false;
        }
        //若用户输入的验证码和正确的验证码相同，则验证通过，并使该验证码失效，否则验证不通过
        if (code.equals(validVerifyCode)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }
}