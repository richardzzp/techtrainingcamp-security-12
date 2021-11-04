package com.catchyou;

import com.catchyou.controller.CnController;
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
	@Autowired
	private CnController cnController;



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
	@Test
	void getLoginRecordTest() {
		//设置请求体参数
		String sessionId = "123456";
		String ip = "127.0.0.1";
		String deviceId = "111111111";


		//创建请求体
		HashMap<String, Object> requestBody = new HashMap<>();
		requestBody.put("sessionId", "132384854");
		HashMap<String, String> environment = new HashMap<>();
		environment.put("ip", ip);
		environment.put("deviceId", deviceId);
		requestBody.put("environment", environment);
		//调用接口
		CommonResult<Map<String, Object>> responseBody = cnController.getLoginRecord(requestBody);

		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());
	}

	@Test
	void getUserTest() {
		//设置请求体参数
		String sessionId = "123456";
		String ip = "127.0.0.1";
		String deviceId = "111111111";


		//创建请求体
		HashMap<String, Object> requestBody = new HashMap<>();
		requestBody.put("sessionId", "132384854");
		HashMap<String, String> environment = new HashMap<>();
		environment.put("ip", ip);
		environment.put("deviceId", deviceId);
		requestBody.put("environment", environment);
		//调用接口
		CommonResult<Map<String, Object>> responseBody = cnController.getUser(requestBody);

		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());
	}

	@Test
	void loginWithPhoneTest() {
		//设置请求体参数
		String PhoneNumber = "15223643698";
		String  verifyCode= "365258";
		String ip = "127.0.0.1";
		String deviceId = "111111111";


		//创建请求体
		HashMap<String, Object> requestBody = new HashMap<>();
		requestBody.put("PhoneNumber", PhoneNumber);
		HashMap<String, String> environment = new HashMap<>();
		environment.put("ip", ip);
		environment.put("deviceId", deviceId);
		requestBody.put("environment", environment);

		//调用接口
		CommonResult<Map<String, Object>> responseBody = cnController.loginWithPhone(requestBody);

		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(1, responseBody.getCode());
	}

	@Test
	void logoutTest() {
		//设置请求体参数
		String sessionId = "11254515";
		String actionType_1= "1";
		String actionType_2= "2";
		String actionType_3= "3";
		String ip = "127.0.0.1";
		String deviceId = "111111111";


		//创建请求体
		HashMap<String, Object> requestBody = new HashMap<>();
		requestBody.put("sessionId", sessionId);
		requestBody.put("actionType", actionType_1);
		HashMap<String, String> environment = new HashMap<>();
		environment.put("ip", ip);
		environment.put("deviceId", deviceId);
		requestBody.put("environment", environment);

		//调用接口
		CommonResult<Map<String, Object>> responseBody = cnController.logout(requestBody);

		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());

		requestBody.put("actionType", actionType_2);
		responseBody = cnController.logout(requestBody);

		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());

		requestBody.put("actionType", actionType_3);
		responseBody = cnController.logout(requestBody);

		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());
	}

	@Test
	void loginWithUsernameTest() {
		//设置请求体参数
		String username = "ewgweg051";
		String password = "123456";
		String ip = "127.0.0.1";
		String deviceId = "111111111";


		//创建请求体
		HashMap<String, Object> requestBody = new HashMap<>();
		requestBody.put("username", username);
		requestBody.put("password", password);
		HashMap<String, String> environment = new HashMap<>();
		environment.put("ip", ip);
		environment.put("deviceId", deviceId);
		requestBody.put("environment", environment);

		//调用接口
		CommonResult<Map<String, Object>> responseBody = cnController.loginWithUsername(requestBody);

		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(1, responseBody.getCode());
	}


	@Test
	void registerTest() {
		//设置请求体参数
		String PhoneNumber = "15223643698";
		String verifyCode = "320595";
		String username = "ewgweg051";
		String password = "123456";
		String ip = "127.0.0.1";
		String deviceId = "111111111";


		//创建请求体
		HashMap<String, Object> requestBody = new HashMap<>();
		requestBody.put("PhoneNumber", PhoneNumber);
		requestBody.put("verifyCode", verifyCode);
		requestBody.put("username", username);
		requestBody.put("password", password);
		HashMap<String, String> environment = new HashMap<>();
		environment.put("ip", ip);
		environment.put("deviceId", deviceId);
		requestBody.put("environment", environment);

		//调用接口
		CommonResult<Map<String, Object>> responseBody = cnController.loginWithUsername(requestBody);

		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(1, responseBody.getCode());
	}
}