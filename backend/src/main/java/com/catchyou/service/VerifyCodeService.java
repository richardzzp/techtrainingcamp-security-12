package com.catchyou.service;

import com.catchyou.pojo.VerifyCode;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZoneId;
import java.util.*;

/**
 * 处理手机验证码的业务逻辑层
 * VALID_DURATION: 验证码的有效期，单位为秒
 * verifyCodes: 存放每个手机号码对应的验证码，以手机号为键，验证码为值
 */
@Service
public class VerifyCodeService {
    private static final Duration EXPIRE_TIME = Duration.ofMinutes(3);
    //限制一分钟内不能再次发送短信
    private static final Duration LIMIT_TIME = Duration.ofMinutes(1);

    private final RedisTemplate<String, Object> redisTemplate;

    public VerifyCodeService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 生成随机的验证码
     * @return 随机验证码
     */
    public String generateVerifyCode(String phoneNumber) {
        Integer isLimit = (Integer) redisTemplate.opsForValue().get("verify_code_limit_" + phoneNumber);
        if (isLimit != null) {
            return null;
        }

        Random random = new Random(System.currentTimeMillis());
        String verifyCode = String.format("%06d", random.nextInt(1000000));
        redisTemplate.opsForValue().set("verify_code_" + phoneNumber, verifyCode, EXPIRE_TIME);
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
        String validVerifyCode = (String) redisTemplate.opsForValue().get("verify_code_" + phoneNumber);
        if (validVerifyCode == null) {
            return false;
        }
        return code.equals(validVerifyCode);
    }
}
