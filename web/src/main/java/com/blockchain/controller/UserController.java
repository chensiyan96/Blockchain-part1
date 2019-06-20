package com.blockchain.controller;

import com.blockchain.model.User;
import com.blockchain.service.UserService;
import com.blockchain.utils.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

@CrossOrigin
@RestController
@RequestMapping(value = "api/user")
public class UserController
{
	@Autowired
	private UserService userService;

	// 注册
	@RequestMapping(value = "register", method = { RequestMethod.POST })
	public String register(@RequestBody String request)
	{
		var req = new JSONObject(request);
		var email = req.getString("email").toUpperCase();
		if (email.isEmpty() || !email.contains("@")) {
			return JSONUtils.failResponse("邮箱无效");
		}
		if (userService.isEmailExist(email)) {
			return JSONUtils.failResponse("邮箱已被注册");
		}
		var password = req.getString("password");
		if(password.isEmpty()) {
			return JSONUtils.failResponse("密码不能为空");
		}

		User user = new User();
		user.email = email;
		user.companyName = req.getString("name");

		try	{
			user.passwordHash = AESToken.encrypt(password);
		} catch (InvalidKeyException e) {
			return JSONUtils.failResponse(e.getMessage());
		} catch (InvalidAlgorithmParameterException e) {
			return JSONUtils.failResponse(e.getMessage());
		} catch (IllegalBlockSizeException e) {
			return JSONUtils.failResponse(e.getMessage());
		} catch (BadPaddingException e) {
			return JSONUtils.failResponse(e.getMessage());
		}

		user.role = User.Roles.valueOf(req.getString("role"));
		user.profile = req.getString("profile");

		if (userService.register(user) == 0) {
			return JSONUtils.failResponse("注册失败");
		}

		// todo 区块链里创建账户

		return JSONUtils.successResponse();
	}

	// 登录
	@RequestMapping(value = "login", method = { RequestMethod.POST })
	public String login(@RequestBody String request)
	{
		try {
			var req = new JSONObject(request);
			var email = req.getString("email").toUpperCase();
			var password = req.getString("password");
			var passwordHash = AESToken.encrypt(password);
			var id = userService.signIn(email, passwordHash);
			if (id == 0) {
				return JSONUtils.failResponse("用户名或者密码错误");
			}
			var user = userService.getUserInfoByID(id);
			var token = AESToken.getToken(user.toJSON());
			return JSONUtils.successResponse("data", token);

		} catch (InvalidAlgorithmParameterException e) {
			return JSONUtils.failResponse(e.getMessage());
		} catch (InvalidKeyException e) {
			return JSONUtils.failResponse(e.getMessage());
		} catch (BadPaddingException e) {
			return JSONUtils.failResponse(e.getMessage());
		} catch (IllegalBlockSizeException e) {
			return JSONUtils.failResponse(e.getMessage());
		}
	}

	// 获取用户信息
	@Authorization
	@RequestMapping(value = "getUserInfo", method = { RequestMethod.GET })
	public String getUserInfo(@CurrentUser User user)
	{
		return JSONUtils.successResponse("user", user.toJSON());
	}

	// 更新用户信息
	@Authorization
	@RequestMapping(value = "updateUserInfo", method = {RequestMethod.POST})
	public String updateUserInfo(@CurrentUser User user, @RequestBody String request)
	{
		var req = new JSONObject(request);
		var password = req.getString("confirmPassword");

		try {
			var passwordHash = AESToken.encrypt(password);
			if (!passwordHash.equals(user.passwordHash)) {
				return JSONUtils.failResponse("密码错误");
			}
		} catch (InvalidAlgorithmParameterException e) {
			return JSONUtils.failResponse(e.getMessage());
		} catch (InvalidKeyException e) {
			return JSONUtils.failResponse(e.getMessage());
		} catch (BadPaddingException e) {
			return JSONUtils.failResponse(e.getMessage());
		} catch (IllegalBlockSizeException e) {
			return JSONUtils.failResponse(e.getMessage());
		}

		var u = req.getJSONObject("newUser");
		user.companyName = u.getString("companyName");
		user.profile = u.getString("profile");
		userService.updateUserinfo(user.id, user.companyName, user.profile);
		return JSONUtils.successResponse();
	}

	// 修改密码
	@Authorization
	@RequestMapping(value = "updatePassword", method = {RequestMethod.POST})
	public String updatePassword(@CurrentUser User user, @RequestBody String request)
	{
		try {
			var req = new JSONObject(request);
			var password = req.getString("confirmPassword");
			var passwordHash = AESToken.encrypt(password);
			if (!passwordHash.equals(user.passwordHash)) {
				return JSONUtils.failResponse("密码错误");
			}
			user.passwordHash = AESToken.encrypt(req.getString("newPassword"));

		} catch (InvalidAlgorithmParameterException e) {
			return JSONUtils.failResponse(e.getMessage());
		} catch (InvalidKeyException e) {
			return JSONUtils.failResponse(e.getMessage());
		} catch (BadPaddingException e) {
			return JSONUtils.failResponse(e.getMessage());
		} catch (IllegalBlockSizeException e) {
			return JSONUtils.failResponse(e.getMessage());
		}

		userService.updatePassword(user.id, user.passwordHash);
		return JSONUtils.successResponse();
	}
}
