package com.catchyou;

import com.catchyou.controller.AuthController;
import com.catchyou.controller.VerifyCodeController;
import com.catchyou.pojo.CommonResult;
import com.catchyou.service.AuthService;
import com.catchyou.service.VerifyCodeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class TechtrainingcampSecurity12ApplicationTests {
	@Autowired
	private VerifyCodeController verifyCodeController;
	@Autowired
	private AuthController authController;
	@Autowired
	VerifyCodeService verifyCodeService;
	@Autowired
	AuthService authService;

	private static HashMap<String, Object> requestBody = new HashMap<>();
	private static HashMap<String, Object> environment = new HashMap<>();
	private static CommonResult<Map<String, Object>> responseBody;
	private static final String PHONE_NUMBER_EXIST = "13391086386";// 存在的手机号
	private static final String PHONE_NUMBER_NOT_EXIST = "15380436808";// 不存在的手机号
	private static final String IP = "127.0.0.1";
	private static final String DEVICE_ID = "123456789";
	private static final String UID_EXIST = "d7bedf61-5646-4aad-b59e-4aa94bdb93c7";
	private static final String UID_NOT_EXIST = "m7bedf61-5615-4hdd-wcfe-4aa94bdb93c7";
	private static final String USERNAME_EXIST = "zanding";
	private static final String USERNAME_NOT_EXIST = "18546516574";
	private static final String PASSWORD_CORRECT = "zanding";
	private static final String PASSWORD_INCORRECT = "18546516574";
	private static final String VERIFY_CODE_INCORRECT = "123456";


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
		requestBody.put("phoneNumber", PHONE_NUMBER_EXIST);
		requestBody.put("type", type);
		environment.put("ip", IP);
		environment.put("deviceId", DEVICE_ID);
		requestBody.put("environment", environment);
		//调用接口
		responseBody = verifyCodeController.applyCode(requestBody);// 手机号存在时的响应体
		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());

		//创建手机号不存在时请求体
		requestBody.put("phoneNumber", PHONE_NUMBER_NOT_EXIST);
		//调用接口
		responseBody = verifyCodeController.applyCode(requestBody);// 手机号不存在时的响应体
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
		requestBody.put("phoneNumber", PHONE_NUMBER_EXIST);
		requestBody.put("type", type);
		environment.put("ip", IP);
		environment.put("deviceId", DEVICE_ID);
		requestBody.put("environment", environment);
		//调用接口
		responseBody = verifyCodeController.applyCode(requestBody);// 手机号存在时的响应体
		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());

		//创建手机号不存在时请求体
		requestBody.put("phoneNumber", PHONE_NUMBER_NOT_EXIST);
		//调用接口
		responseBody = verifyCodeController.applyCode(requestBody);// 手机号不存在时的响应体
		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());
	}


	@Test
	void testGetLoginRecord() {
		//创建请求体
		requestBody.put("sessionId", "132384854");
		environment.put("ip", IP);
		environment.put("deviceId", DEVICE_ID);
		requestBody.put("environment", environment);
		//调用接口
		responseBody = authController.getLoginRecord(requestBody);

		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());
	}

	@Test
	void testGetUser() {
		//创建请求体
		requestBody.put("sessionId", "132384854");
		environment.put("ip", IP);
		environment.put("deviceId", DEVICE_ID);
		requestBody.put("environment", environment);
		//调用接口
		responseBody = authController.getUser(requestBody);

		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());
	}

	/**
	 * 验证码正确和错误的情况下, 登录功能能否正常使用
	 */
	@Test
	void testLoginWithPhone() {
		//设置请求体参数
		String verifyCodeCorrect = verifyCodeService.generateVerifyCode(PHONE_NUMBER_EXIST);
		String verifyCodeIncorrect = "123456";

		//创建验证码正确时的请求体
		requestBody.put("phoneNumber", PHONE_NUMBER_EXIST);
		requestBody.put("verifyCode", verifyCodeCorrect);
		environment.put("ip", IP);
		environment.put("deviceId", DEVICE_ID);
		requestBody.put("environment", environment);
		//调用接口
		responseBody = authController.loginWithPhone(requestBody);
		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());

		//创建验证码错误时的请求体
		requestBody.put("verifyCode", verifyCodeIncorrect);
		//调用接口
		responseBody = authController.loginWithPhone(requestBody);
		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(1, responseBody.getCode());


	}
	@Test
	void demo(){
		System.out.println(authService.getUserById(UID_EXIST));
	}
	/**
	 * sessionId存在和不存在时, 登出能否正确使用
	 */
	@Test
	void testLogout_1() {
		//设置请求体参数
		String actionType= "1";


		//创建sessionId存在时登出的请求体
		requestBody.put("sessionId", UID_EXIST);
		requestBody.put("actionType", actionType);
		environment.put("ip", IP);
		environment.put("deviceId", DEVICE_ID);
		requestBody.put("environment", environment);
		//调用接口
		responseBody = authController.logout(requestBody);
		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());


		//创建sessionId不存在时登出的请求体
		requestBody.put("sessionId", UID_NOT_EXIST);
		requestBody.put("actionType", actionType);
		//调用接口
		responseBody = authController.logout(requestBody);
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
		requestBody.put("sessionId", UID_EXIST);
		requestBody.put("actionType", actionType);
		environment.put("ip", IP);
		environment.put("deviceId", DEVICE_ID);
		requestBody.put("environment", environment);
		//调用接口
		responseBody = authController.logout(requestBody);
		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());


		//创建sessionId不存在时注销的请求体
		requestBody.put("sessionId", UID_NOT_EXIST);
		//调用接口
		responseBody = authController.logout(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());
	}

	@Test
	void testLoginWithUsername() {
		//创建用户名不存在请求体
		requestBody.put("username", USERNAME_NOT_EXIST);
		requestBody.put("password", PASSWORD_INCORRECT);
		environment.put("ip", IP);
		environment.put("deviceId", DEVICE_ID);
		//调用接口
		responseBody = authController.loginWithUsername(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());

		//创建用户名存在但密码不正确的请求体
		requestBody.put("username", USERNAME_EXIST);
		requestBody.put("password", PASSWORD_INCORRECT);
		//调用接口
		responseBody = authController.loginWithUsername(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());


		//创建用户名存在且密码正确时的请求体
		requestBody.put("username", USERNAME_EXIST);
		requestBody.put("password", PASSWORD_CORRECT);
		//调用接口
		responseBody = authController.loginWithUsername(requestBody);
		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());
	}

	@Test
	void testRegister() {
		//创建当手机号重复时的请求体
		requestBody.put("PhoneNumber", PHONE_NUMBER_EXIST);
		environment.put("ip", IP);
		environment.put("deviceId", DEVICE_ID);
		requestBody.put("environment", environment);
		//调用接口
		responseBody = authController.loginWithUsername(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());

		//创建当手机号不重复但验证码错误时的请求体
		requestBody.put("PhoneNumber", PHONE_NUMBER_NOT_EXIST);
		requestBody.put("verifyCode", VERIFY_CODE_INCORRECT);
		//调用接口
		responseBody = authController.loginWithUsername(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());

		//创建当手机号不重复且验证码正确但用户名重复时的请求体
		requestBody.put("PhoneNumber", PHONE_NUMBER_NOT_EXIST);
		requestBody.put("verifyCode", verifyCodeService.generateVerifyCode(PHONE_NUMBER_NOT_EXIST));
		requestBody.put("username", USERNAME_EXIST);
		//调用接口
		responseBody = authController.loginWithUsername(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());

		//创建当手机号不重复且验证码正确且用户名不重复时的请求体
		requestBody.put("PhoneNumber", PHONE_NUMBER_NOT_EXIST);
		requestBody.put("verifyCode", verifyCodeService.generateVerifyCode(PHONE_NUMBER_NOT_EXIST));
		requestBody.put("username", USERNAME_NOT_EXIST);
		requestBody.put("password", PASSWORD_CORRECT);
		//调用接口
		responseBody = authController.loginWithUsername(requestBody);
		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());
	}


}