package com.catchyou;

import com.catchyou.controller.CnController;
import com.catchyou.controller.LjhAuthController;
import com.catchyou.pojo.CommonResult;
import com.catchyou.service.CnService;
import com.catchyou.service.LjhVerifyCodeService;
import org.junit.Before;
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
	@Autowired
	LjhVerifyCodeService ljhVerifyCodeService;
	@Autowired
	CnService cnService;


	private static HashMap<String, Object> requestBody = new HashMap<>();
	private static HashMap<String, Object> environment = new HashMap<>();
	private static CommonResult<Map<String, Object>> responseBody;
	private static final String phoneNumberExist = "13391086386";// 存在的手机号
	private static final String phoneNumberNotExist = "15380436808";// 不存在的手机号
	private static final String ip = "127.0.0.1";
	private static final String deviceId = "123456789";
	private static final String uidExist = "d7bedf61-5646-4aad-b59e-4aa94bdb93c7";
	private static final String uidNotExist = "m7bedf61-5615-4hdd-wcfe-4aa94bdb93c7";
	private static final String usernameExist = "zanding";
	private static final String usernameNotExist = "18546516574";
	private static final String passwordCorrect = "zanding";
	private static final String passwordIncorrect = "18546516574";
	private static final String verifyCodeIncorrect = "123456";


//存在的手机13391086386
//不存在的手机15380436808
	/**
	 * 登陆时, 手机号存在与不存在两种情况下,申请验证码的功能能否正常实现
	 */
	@Test
	void testApplyCode_1() {
		//设置请求体参数
		int type = 1;

		//创建手机号存在时的请求体
		requestBody.put("phoneNumber", phoneNumberExist);
		requestBody.put("type", type);
		environment.put("ip", ip);
		environment.put("deviceId", deviceId);
		requestBody.put("environment", environment);
		//调用接口
		responseBody = ljhAuthController.applyCode(requestBody);// 手机号存在时的响应体
		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());

		//创建手机号不存在时请求体
		requestBody.put("phoneNumber", phoneNumberNotExist);
		//调用接口
		responseBody = ljhAuthController.applyCode(requestBody);// 手机号不存在时的响应体
		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());
	}

	/**
	 * 注册时, 手机号存在与不存在两种情况下,申请验证码的功能能否实现
	 */
	@Test
	void testApplyCode_2() {
		//设置请求体参数
		int type = 2;

		//创建手机号不存在时请求体
		requestBody.put("phoneNumber", phoneNumberExist);
		requestBody.put("type", type);
		environment.put("ip", ip);
		environment.put("deviceId", deviceId);
		requestBody.put("environment", environment);
		//调用接口
		responseBody = ljhAuthController.applyCode(requestBody);// 手机号存在时的响应体
		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());

		//创建手机号不存在时请求体
		requestBody.put("phoneNumber", phoneNumberNotExist);
		//调用接口
		responseBody = ljhAuthController.applyCode(requestBody);// 手机号不存在时的响应体
		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());
	}


	@Test
	void testGetLoginRecord() {
		//创建请求体
		requestBody.put("sessionId", "132384854");
		environment.put("ip", ip);
		environment.put("deviceId", deviceId);
		requestBody.put("environment", environment);
		//调用接口
		responseBody = cnController.getLoginRecord(requestBody);

		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());
	}

	@Test
	void testGetUser() {
		//创建请求体
		requestBody.put("sessionId", "132384854");
		environment.put("ip", ip);
		environment.put("deviceId", deviceId);
		requestBody.put("environment", environment);
		//调用接口
		responseBody = cnController.getUser(requestBody);

		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());
	}

	/**
	 * 验证码正确和错误的情况下, 登录功能能否正常使用
	 */
	@Test
	void testLoginWithPhone() {
		//设置请求体参数
		String verifyCodeCorrect = ljhVerifyCodeService.generateVerifyCode(phoneNumberExist);
		String verifyCodeIncorrect = "123456";

		//创建验证码正确时的请求体
		requestBody.put("phoneNumber", phoneNumberExist);
		requestBody.put("verifyCode", verifyCodeCorrect);
		environment.put("ip", ip);
		environment.put("deviceId", deviceId);
		requestBody.put("environment", environment);
		//调用接口
		responseBody = cnController.loginWithPhone(requestBody);
		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());

		//创建验证码错误时的请求体
		requestBody.put("verifyCode", verifyCodeIncorrect);
		//调用接口
		responseBody = cnController.loginWithPhone(requestBody);
		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(1, responseBody.getCode());


	}
	@Test
	void demo(){
		System.out.println(cnService.getUserById(uidExist));
	}
	/**
	 * sessionId存在和不存在时, 登出能否正确使用
	 */
	@Test
	void testLogout_1() {
		//设置请求体参数
		String actionType= "1";


		//创建sessionId存在时登出的请求体
		requestBody.put("sessionId", uidExist);
		requestBody.put("actionType", actionType);
		environment.put("ip", ip);
		environment.put("deviceId", deviceId);
		requestBody.put("environment", environment);
		//调用接口
		responseBody = cnController.logout(requestBody);
		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());


		//创建sessionId不存在时登出的请求体
		requestBody.put("sessionId", uidNotExist);
		requestBody.put("actionType", actionType);
		//调用接口
		responseBody = cnController.logout(requestBody);
		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());

	}
	/**
	 * sessionId存在和不存在时, 注销能否正确使用
	 */
	@Test
	void testLogout_2() {
		//设置请求体参数
		String actionType= "2";

		//创建sessionId存在时注销的请求体
		requestBody.put("sessionId", uidExist);
		requestBody.put("actionType", actionType);
		environment.put("ip", ip);
		environment.put("deviceId", deviceId);
		requestBody.put("environment", environment);
		//调用接口
		responseBody = cnController.logout(requestBody);
		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());


		//创建sessionId不存在时注销的请求体
		requestBody.put("sessionId", uidNotExist);
		//调用接口
		responseBody = cnController.logout(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());
	}

	@Test
	void testLoginWithUsername() {
		//创建用户名不存在请求体
		requestBody.put("username", usernameNotExist);
		requestBody.put("password", passwordIncorrect);
		environment.put("ip", ip);
		environment.put("deviceId", deviceId);
		//调用接口
		responseBody = cnController.loginWithUsername(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());

		//创建用户名存在但密码不正确的请求体
		requestBody.put("username", usernameExist);
		requestBody.put("password", passwordIncorrect);
		//调用接口
		responseBody = cnController.loginWithUsername(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());


		//创建用户名存在且密码正确时的请求体
		requestBody.put("username", usernameExist);
		requestBody.put("password", passwordCorrect);
		//调用接口
		responseBody = cnController.loginWithUsername(requestBody);
		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());
	}

	@Test
	void testRegister() {
		//创建当手机号重复时的请求体
		requestBody.put("PhoneNumber", phoneNumberExist);
		environment.put("ip", ip);
		environment.put("deviceId", deviceId);
		requestBody.put("environment", environment);
		//调用接口
		responseBody = cnController.loginWithUsername(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());

		//创建当手机号不重复但验证码错误时的请求体
		requestBody.put("PhoneNumber", phoneNumberNotExist);
		requestBody.put("verifyCode", verifyCodeIncorrect);
		//调用接口
		responseBody = cnController.loginWithUsername(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());

		//创建当手机号不重复且验证码正确但用户名重复时的请求体
		requestBody.put("PhoneNumber", phoneNumberNotExist);
		requestBody.put("verifyCode", ljhVerifyCodeService.generateVerifyCode(phoneNumberNotExist));
		requestBody.put("username", usernameExist);
		//调用接口
		responseBody = cnController.loginWithUsername(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());

		//创建当手机号不重复且验证码正确且用户名不重复时的请求体
		requestBody.put("PhoneNumber", phoneNumberNotExist);
		requestBody.put("verifyCode", ljhVerifyCodeService.generateVerifyCode(phoneNumberNotExist));
		requestBody.put("username", usernameNotExist);
		requestBody.put("password", passwordCorrect);
		//调用接口
		responseBody = cnController.loginWithUsername(requestBody);
		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());
	}


}