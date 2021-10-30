package com.catchyou.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//由于所有的返回都有code和message两个参数，因此做一个简单的封装
public class CommonResult<T> {
    private Integer code;
    private String message;
    //数据主体
    private T data;

    public CommonResult(Integer code, String message) {
        this(code, message, null);
    }
}
