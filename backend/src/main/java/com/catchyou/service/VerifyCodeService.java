package com.catchyou.service;

import com.catchyou.pojo.VerifyCode;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 处理手机验证码的业务逻辑层
 */
@Service
public class VerifyCodeService {
    private List<VerifyCode> verifyCodes = new ArrayList<>();

    /**
     * 生成随机的验证码
     * @return 随机验证码
     */
    public VerifyCode generateVerifyCode() {
        BigInteger pureNumberUuid = new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16);
        String code = String.format("%06d", pureNumberUuid).substring(0, 6);
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(3);
        Date expireTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        VerifyCode verifyCode = new VerifyCode(code, expireTime);
        verifyCodes.add(verifyCode);
        return verifyCode;
    }
}
