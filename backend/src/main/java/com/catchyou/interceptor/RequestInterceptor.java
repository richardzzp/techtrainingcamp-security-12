package com.catchyou.interceptor;


import com.catchyou.config.RedisConfig;
import com.catchyou.pojo.CommonResult;
import com.catchyou.util.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RequestInterceptor implements HandlerInterceptor {

    private String ipGenKey(String ip) {
        return new StringBuilder().append(ip).append("_request_count").toString();
    }


    private RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getHeader("ip");
        System.out.println("ip=" + ip);
        String deviceId = request.getHeader("deviceId");

        //频度检测，同一个ip在一个时间片（5s）内只允许请求最多10次
        String ipKey = ipGenKey(ip);
        redisTemplate.opsForValue().set("test", 1);
//        if (!(redisTemplate.hasKey(ipKey))) {
//            opsForValue.set(ipKey, 1, 5, TimeUnit.SECONDS);
//        } else {
//            opsForValue.increment(ipKey);
//        }
//        if ((Integer) opsForValue.get(ipKey) > 10) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("decisionType", 1);
//            MyUtil.setResponse(response, new CommonResult<>(1, "ip访问过于频繁", map));
//            return false;
//        }


        return true;
    }

}
