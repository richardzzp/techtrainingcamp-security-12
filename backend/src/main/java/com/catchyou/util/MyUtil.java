package com.catchyou.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class MyUtil {
    public static void setResponse(HttpServletResponse response,Object payload) throws IOException{
        byte[] json = new ObjectMapper().writeValueAsBytes(payload);
        OutputStream out = response.getOutputStream();
        out.write(json);
        out.flush();
        out.close();
    }
}
