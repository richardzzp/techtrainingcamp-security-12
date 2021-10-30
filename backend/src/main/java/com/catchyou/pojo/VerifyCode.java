package com.catchyou.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 验证码实体类
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VerifyCode {
    private String code;
    private Date expireTime;
}
