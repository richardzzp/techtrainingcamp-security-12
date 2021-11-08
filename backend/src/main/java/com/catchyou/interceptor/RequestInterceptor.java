package com.catchyou.interceptor;

import com.catchyou.pojo.CommonResult;
import com.catchyou.util.MyUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RequestInterceptor implements HandlerInterceptor {
    private final RedisTemplate<String, Object> redisTemplate;

    public RequestInterceptor(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String ipGenRequestCountKey(String ip) {
        return new StringBuilder().append(ip).append("_request_count").toString();
    }

    private String ipGenBlockCountKey(String ip) {
        return new StringBuilder().append(ip).append("_block_count").toString();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getHeader("ip");
        System.out.println("拦截判断:" + ip);
        //先看黑名单里有没有这个ip，有的话就直接打回去
        if (redisTemplate.opsForSet().isMember("ip_black_list", ip)) {
            System.out.println("请求被拦截");
            HashMap<String, Object> map = new HashMap<>();
            map.put("decisionType", 3);
            MyUtil.setResponse(response, new CommonResult<>(-1, "由于多次的高频访问，您的ip已被锁定", map));
            return false;
        }
        //频度检测，同一个ip在一个时间片（5s）内只允许请求最多10次，无论什么接口
        String requestCountKey = ipGenRequestCountKey(ip);
        if (!(redisTemplate.hasKey(requestCountKey))) {
            redisTemplate.opsForValue().set(requestCountKey, 1, 5, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().increment(requestCountKey);
        }
        //如果用户请求接口频率过高，将要求滑块验证
        //此外，如果用户在10分钟内已经是第五次高频请求了，将直接锁定ip
        if ((Integer) redisTemplate.opsForValue().get(requestCountKey) > 10) {
            //记录是第几次高频请求
            String blockCountKey = ipGenBlockCountKey(ip);
            if (!(redisTemplate.hasKey(blockCountKey))) {
                redisTemplate.opsForValue().set(blockCountKey, 1, 10, TimeUnit.MINUTES);
            } else {
                redisTemplate.opsForValue().increment(blockCountKey);
            }
            //ip加入黑名单
            if ((Integer) redisTemplate.opsForValue().get(blockCountKey) >= 5) {
                redisTemplate.opsForSet().add("ip_black_list", ip);
                HashMap<String, Object> map = new HashMap<>();
                map.put("decisionType", 3);
                MyUtil.setResponse(response, new CommonResult<>(-1, "由于多次的高频访问，您的ip已被锁定", map));
                redisTemplate.delete(blockCountKey);
                return false;
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("decisionType", 1);
            MyUtil.setResponse(response, new CommonResult<>(-1, "ip访问过于频繁，需要进行滑块验证", map));
            redisTemplate.delete(requestCountKey);
            return false;
        }
        return true;
    }

}
