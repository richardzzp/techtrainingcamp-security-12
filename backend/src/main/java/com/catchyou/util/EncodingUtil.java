package com.catchyou.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//用于给密码加密
public class EncodingUtil {

    public static String encode(String ori) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(ori.getBytes());
        byte[] b = md5.digest();
        StringBuffer result = new StringBuffer();
        String hex;
        for (int i = 0; i < b.length; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result.append(hex.toUpperCase());
        }
        return result.toString();
    }

}
