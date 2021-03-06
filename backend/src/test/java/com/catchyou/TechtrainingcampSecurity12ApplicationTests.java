package com.catchyou;

import com.catchyou.controller.AuthController;
import com.catchyou.controller.VerifyCodeController;
import com.catchyou.dao.AuthDao;
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
	private AuthController authController;
	@Autowired
	private VerifyCodeController verifyCodeController;
	@Autowired
	private VerifyCodeService verifyCodeService;
	@Autowired
	private AuthService authService;
	@Autowired
	private AuthDao authdao;


	private static HashMap<String, Object> requestBody = new HashMap<>();
	private static HashMap<String, Object> environment = new HashMap<>();
	private static CommonResult<Map<String, Object>> responseBody;
	private static final String uidExist = "a3d591d0-c0b2-4b92-8fff-85fa765b6458";
	private static final String phoneNumberExist = "15380436808";// 存在的手机号
	private static final String usernameExist = "chenysh";
	private static final String passwordCorrect = "123456";
	private static final String phoneNumberNotExist = "15380436809";// 不存在的手机号
	private static String ip;
	private static String deviceId;
	private static final String uidNotExist = "m7bedf61-5615-4hdd-wcfe-4aa94bdb93c7";

//	@Test
//	void setUser(){
//		environment.put("ip", "180.101.49.11");
//		environment.put("deviceId", "123456");
//		requestBody.put("environment", environment);
//		//创建当手机号不重复且验证码正确且用户名不重复时的请求体
//		requestBody.put("phoneNumber", "15380436808");
//		requestBody.put("verifyCode",
//				verifyCodeService.generateVerifyCode("15380436808"));
//		requestBody.put("username", "chenysh");
//		requestBody.put("password", "123456");
//
//		//调用接口
//		responseBody = authController.register(requestBody);
//		//测试接口返回值，若响应体中的code等于0则测试通过
//		System.out.println(responseBody.getMessage());
//		Assertions.assertEquals(0, responseBody.getCode());
//	}

	void init() {
		ip = String.valueOf((int)(Math.random() * 128)) + "."
				+ String.valueOf((int)(Math.random() * 128)) + "."
				+ String.valueOf((int)(Math.random() * 128)) + "."
				+ String.valueOf((int)(Math.random() * 128));
		deviceId = String.valueOf((int)(Math.random() * 100000000));
		environment.put("ip", ip);
		environment.put("deviceId", deviceId);
		requestBody.put("environment", environment);
	}
	/**
	 * 登陆时, 手机号存在与不存在两种情况下,申请验证码的功能能否正常实现
	 */
	@Test
	void testApplyCode_1() {
		init();
		//设置请求体参数
		int type = 1;

		//创建手机号存在时的请求体
		requestBody.put("phoneNumber", phoneNumberExist);
		requestBody.put("type", type);
		//调用接口
		responseBody = verifyCodeController.applyCode(requestBody);// 手机号存在时的响应体
		//测试接口返回值，若响应体中的code等于0则测试通过
		System.out.println(responseBody.getMessage());
		Assertions.assertEquals(0, responseBody.getCode());

		//创建手机号不存在时请求体
		requestBody.put("phoneNumber", String.valueOf((long)(Math.random() * 20000000000L)));
		//调用接口
		responseBody = verifyCodeController.applyCode(requestBody);// 手机号不存在时的响应体
		//测试接口返回值，若响应体中的code等于1则测试通过
		System.out.println(responseBody.getMessage());
		Assertions.assertEquals(1, responseBody.getCode());
	}

	/**
	 * 注册时, 手机号存在与不存在两种情况下,申请验证码的功能能否实现
	 */
	@Test
	void testApplyCode_2() {
		init();
		//设置请求体参数
		int type = 2;

		//创建手机号存在时请求体
		requestBody.put("phoneNumber", phoneNumberExist);
		requestBody.put("type", type);
		//调用接口
		responseBody = verifyCodeController.applyCode(requestBody);// 手机号存在时的响应体
		//测试接口返回值，若响应体中的code等于1则测试通过
		System.out.println(responseBody.getMessage());
		Assertions.assertEquals(1, responseBody.getCode());

		//创建手机号不存在时请求体
		requestBody.put("phoneNumber", String.valueOf((long)(Math.random() * 20000000000L)));
		//调用接口
		responseBody = verifyCodeController.applyCode(requestBody);// 手机号不存在时的响应体
		//测试接口返回值，若响应体中的code等于0则测试通过
		System.out.println(responseBody.getMessage());
		Assertions.assertEquals(0, responseBody.getCode());
	}


	@Test
	void testGetLoginRecord() {
		init();
		//创建请求体
		requestBody.put("sessionId", uidExist);
		//调用接口
		responseBody = authController.getLoginRecord(requestBody);

		//测试接口返回值，若响应体中的code等于0则测试通过
		Assertions.assertEquals(0, responseBody.getCode());
	}

	@Test
	void testGetUser() {
		init();
		//创建请求体
		requestBody.put("sessionId", uidExist);
		//调用接口
		responseBody = authController.getUser(requestBody);

		//测试接口返回值，若响应体中的code等于0则测试通过
		System.out.println(responseBody.getMessage());
		Assertions.assertEquals(0, responseBody.getCode());
	}

	/**
	 * 验证码正确和错误的情况下, 登录功能能否正常使用
	 */
	@Test
	void testLoginWithPhone() {
		init();
		//设置请求体参数
		String verifyCodeCorrect = verifyCodeService.generateVerifyCode(phoneNumberExist);

		//创建验证码正确时的请求体
		requestBody.put("phoneNumber", phoneNumberExist);
		requestBody.put("verifyCode", verifyCodeCorrect);
		//调用接口
		responseBody = authController.loginWithPhone(requestBody);
		//测试接口返回值，若响应体中的code等于0则测试通过
		System.out.println(responseBody.getMessage());
		Assertions.assertEquals(0, responseBody.getCode());

		//创建验证码错误时的请求体
		requestBody.put("verifyCode", String.valueOf((int)(Math.random() * 1000000)));
		//调用接口
		responseBody = authController.loginWithPhone(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		System.out.println(responseBody.getMessage());
		Assertions.assertEquals(1, responseBody.getCode());
	}


	@Test
	void testLoginWithUsername() {
		init();
		//创建用户名不存在请求体
		requestBody.put("username", "usernameNotExist");
		requestBody.put("password", "passwordIncorrect");
		//调用接口
		responseBody = authController.loginWithUsername(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		Assertions.assertEquals(1, responseBody.getCode());

		//创建用户名存在但密码不正确的请求体
		requestBody.put("username", usernameExist);
		requestBody.put("password", "passwordIncorrect");
		//调用接口
		responseBody = authController.loginWithUsername(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		System.out.println(responseBody.getMessage());
		Assertions.assertEquals(1, responseBody.getCode());


		//创建用户名存在且密码正确时的请求体
		requestBody.put("username", usernameExist);
		requestBody.put("password", passwordCorrect);
		//调用接口
		responseBody = authController.loginWithUsername(requestBody);
		//测试接口返回值，若响应体中的code等于0则测试通过
		System.out.println(responseBody.getMessage());
		Assertions.assertEquals(0, responseBody.getCode());
	}

	/**
	 * 联合logout方法测试
	 */
	@Test
	void testRegister() {
		init();
		//创建当手机号重复时的请求体
		requestBody.put("phoneNumber", phoneNumberExist);
		//调用接口
		responseBody = authController.register(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		System.out.println(responseBody.getMessage());
		Assertions.assertEquals(1, responseBody.getCode());

		//创建当手机号不重复但验证码错误时的请求体
		requestBody.put("phoneNumber", phoneNumberNotExist);
		requestBody.put("verifyCode", "aaaaaa");
		//调用接口
		responseBody = authController.register(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		System.out.println(responseBody.getMessage());
		Assertions.assertEquals(1, responseBody.getCode());

		//创建当手机号不重复且验证码正确但用户名重复时的请求体
		requestBody.put("phoneNumber", phoneNumberNotExist);
		requestBody.put("verifyCode", verifyCodeService.generateVerifyCode(phoneNumberNotExist));
		requestBody.put("username", usernameExist);
		//调用接口
		responseBody = authController.register(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		System.out.println(responseBody.getMessage());
		Assertions.assertEquals(1, responseBody.getCode());
	}

	/**
	 * 登出与注销功能能否正确使用
	 */
	@Test
	void testLogout() {
		init();

		//设置请求体参数
		String actionType= "1";
		//创建请求体
		requestBody.put("sessionId", uidExist);
		requestBody.put("actionType", actionType);
		//调用接口
		responseBody = authController.logout(requestBody);
		//测试接口返回值，若响应体中的code等于0则测试通过
		System.out.println(responseBody.getMessage());
		Assertions.assertEquals(0, responseBody.getCode());


		//设置请求体参数
		actionType = "2";
		//创建sessionId不存在时注销的请求体
		requestBody.put("sessionId", uidNotExist);
		requestBody.put("actionType", actionType);
		//调用接口
		responseBody = authController.logout(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		System.out.println(responseBody.getMessage());
		Assertions.assertEquals(1, responseBody.getCode());
	}
	@Test
	void testRegisterAndLogout(){
		init();

		//创建当手机号不重复且验证码正确且用户名不重复时的请求体
		requestBody.put("phoneNumber", phoneNumberNotExist);
		requestBody.put("verifyCode", verifyCodeService.generateVerifyCode(phoneNumberNotExist));
		requestBody.put("username", "usernameNotExist");
		requestBody.put("password", "123456");
		//调用接口
		responseBody = authController.register(requestBody);
		//测试接口返回值，若响应体中的code等于1则测试通过
		System.out.println(responseBody.getMessage());
		Assertions.assertEquals(0, responseBody.getCode());

		//创建sessionId存在时注销的请求体
		requestBody.put("sessionId", authdao.getUserByName("usernameNotExist").getId());
		requestBody.put("actionType", "2");
		//调用接口
		responseBody = authController.logout(requestBody);
		//测试接口返回值，若响应体中的code等于0则测试通过
		System.out.println(responseBody.getMessage());
		Assertions.assertEquals(0, responseBody.getCode());
	}
}