package com.catchyou.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class MyUtil {

    private static RestTemplate restTemplate = new RestTemplate();

    public static void setResponse(HttpServletResponse response, Object payload) throws IOException {
        byte[] json = new ObjectMapper().writeValueAsBytes(payload);
        OutputStream out = response.getOutputStream();
        out.write(json);
        out.flush();
        out.close();
    }

    public static String getCityFromIp(String ip) {
        HashMap<String, Object> response = restTemplate.getForObject(
                "https://apis.map.qq.com/ws/location/v1/ip" +
                        "?ip=" + ip +
                        "&key=7YMBZ-4UJ6W-5DCRO-OGTUK-ENXY2-JLBOY", HashMap.class);
        HashMap<String, Object> result = (HashMap) (response.get("result"));
        HashMap<String, Object> adInfo = (HashMap) (result.get("ad_info"));
        String city = (String) adInfo.get("city");
        return city;
    }

}
