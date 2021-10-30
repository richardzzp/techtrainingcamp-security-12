package com.catchyou.service;

import com.catchyou.pojo.VerifyCode;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 处理手机验证码的业务逻辑层
 */
@Service
public class VerifyCodeService {
    private Map<String, VerifyCode> verifyCodes = new HashMap<>();

    /**
     * 生成随机的验证码
     *
     * @return 随机验证码
     */
    public VerifyCode generateVerifyCode(String phoneNumber) {
        BigInteger pureNumberUuid = new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16);
        String code = String.format("%06d", pureNumberUuid).substring(0, 6);
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(3);
        Date expireTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        VerifyCode verifyCode = new VerifyCode(code, expireTime);
        verifyCodes.put(phoneNumber, verifyCode);
        return verifyCode;
    }
}
