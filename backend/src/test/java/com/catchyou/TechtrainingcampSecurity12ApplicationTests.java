package com.catchyou;

import com.catchyou.controller.LjhAuthController;
import com.catchyou.pojo.CommonResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class TechtrainingcampSecurity12ApplicationTests {
	@Autowired
	private LjhAuthController ljhAuthController;

	@Test
	void contextLoads() {
		//设置请求体参数
		String phoneNumber = "13650068814";
		int type = 2;
		Map<String, Object> environment = new HashMap<>();
		String ip = "127.0.0.1";
		String deviceId = "111111111";

		//创建请求体
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("phoneNumber", phoneNumber);
		requestBody.put("type", type);
		environment.put("ip", ip);
		environment.put("deviceId", deviceId);
		requestBody.put("environment", environment);

		//调用接口
		CommonResult<Map<String, Object>> responseBody = ljhAuthController.applyCode(requestBody);

		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());
	}

}
