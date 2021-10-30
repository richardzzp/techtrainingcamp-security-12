package com.catchyou.service;

import com.catchyou.pojo.VerifyCode;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 处理手机验证码的业务逻辑层
 * VALID_DURATION: 验证码的有效期，单位为秒
 * verifyCodes: 存放每个手机号码对应的验证码，以手机号为键，验证码为值
 */
@Service
public class VerifyCodeService {
    private static final long VALID_DURATION = 3 * 60;

    private final Map<String, VerifyCode> verifyCodes = new HashMap<>();

    /**
     * 生成随机的验证码
     * @return 随机验证码
     */
    public VerifyCode generateVerifyCode(String phoneNumber) {
        BigInteger pureNumberUuid = new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16);
        String code = String.format("%06d", pureNumberUuid).substring(0, 6);
        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(VALID_DURATION);
        Date expireTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        VerifyCode verifyCode = new VerifyCode(code, expireTime);
        verifyCodes.put(phoneNumber, verifyCode);
        return verifyCode;
    }

    /**
     * 检查验证码是否有效
     * @param phoneNumber 手机号
     * @param code 用户输入的验证码
     * @return 若验证码有效则返回true，否则返回false
     */
    public boolean checkVerifyCode(String phoneNumber, String code) {
        VerifyCode verifyCode = verifyCodes.get(phoneNumber);
        if (verifyCode == null) {
            return false;
        }
        if (new Date().after(verifyCode.getExpireTime())) {
            return false;
        }
        return code.equals(verifyCode.getCode());
    }
}
