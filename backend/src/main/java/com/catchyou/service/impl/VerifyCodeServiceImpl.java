package com.catchyou.service.impl;

import com.catchyou.service.VerifyCodeService;
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
public class VerifyCodeServiceImpl implements VerifyCodeService {
    private static final Duration EXPIRE_TIME = Duration.ofMinutes(3);
    private static final Duration LIMIT_TIME = Duration.ofMinutes(1);

    private final RedisTemplate<String, Object> redisTemplate;

    public VerifyCodeServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 生成随机的验证码
     * @param phoneNumber 请求验证码的手机号
     * @return 随机验证码
     */
    @Override
    public String generateVerifyCode(String phoneNumber) {
        //生成随机6位验证码
        Random random = new Random(System.currentTimeMillis());
        String verifyCode = String.format("%06d", random.nextInt(1000000));
        //存储验证码，有效期为3分钟
        String key = "verify_code_" + phoneNumber;
        redisTemplate.opsForValue().set(key, verifyCode, EXPIRE_TIME);
        //记录请求了验证码的手机号，防止1分钟内进行多次验证码请求
        key = "verify_code_limit_" + phoneNumber;
        redisTemplate.opsForValue().set(key, 1, LIMIT_TIME);
        //记录该手机号请求验证码的累计次数
        key = "verify_code_request_count_" + phoneNumber;
        redisTemplate.opsForValue().increment(key);
        return verifyCode;
    }

    /**
     * 检查验证码是否有效
     * @param phoneNumber 手机号
     * @param code 用户输入的验证码
     * @return 若验证码有效则返回true，否则返回false
     */
    @Override
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

    /**
     * 判断手机号是否在一分钟内进行了多次验证码请求
     * @param phoneNumber 手机号
     * @return 若手机号在一分钟内进行了多次验证码请求 则返回true 否则返回false
     */
    @Override
    public boolean withinOneMinute(String phoneNumber) {
        String key = "verify_code_limit_" + phoneNumber;
        return redisTemplate.opsForValue().get(key) != null;
    }

    /**
     * 判断手机号是否多次进行验证码请求 请求次数达到3次则认为请求次数过多
     * @param phoneNumber 手机号
     * @return 若手机号进行验证码请求的次数达到3次 则返回true 否则返回false
     */
    @Override
    public boolean isFrequentRequest(String phoneNumber) {
        String key = "verify_code_request_count_" + phoneNumber;
        Integer count = (Integer) redisTemplate.opsForValue().get(key);
        return count != null && count == 3;
    }

    /**
     * 将手机号请求次数的记录置零
     * @param phoneNumber 手机号
     */
    @Override
    public void recountRequest(String phoneNumber) {
        String key = "verify_code_request_count_" + phoneNumber;
        redisTemplate.opsForValue().set(key, 0);
    }
}