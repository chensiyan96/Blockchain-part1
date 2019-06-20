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
		user.companyName = req.getString("companyName");

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
		var req = new JSONObject(request);
		var res = new JSONObject();
		try
		{
			var id = userService.signIn(req.getString("email"), req.getString("password"));
			if (id == 0)
			{
				throw new Exception("用户名或者密码错误");
			}
			var user = userService.getUserInfoByID(id);
			var userInfo = user.toJSON();

			var token = AESToken.getToken(userInfo);
			res.put("status", 1);
			res.put("msg", "Success");
			res.put("data", token);
		} catch (Exception e)
		{
			res.put("status", 0);
			res.put("msg", "用户名或者密码错误");
		}
		return res.toString();
	}

	@Authorization
	@RequestMapping(value = "getUserInfo", method = {RequestMethod.GET})
	public String getUserInfo(@CurrentUser User u)
	{
		var response = new JSONObject();
		try
		{

			var user = u.toJSON();
			response.put("status", 1);
			response.put("msg", "Success");
			user.put("profile", u.profile);
			response.put("user", user);
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", e.getMessage());
		}
		return response.toString();
	}



	@Authorization
	@RequestMapping(value = "updateUserInfo", method = {RequestMethod.POST})
	public String updateUserInfo(@CurrentUser User user, @RequestBody String request)
	{
		var req = new JSONObject(request);
		var response = new JSONObject();
		try
		{
			var cpsw = req.getString("confirmPassword");
			var id = userService.signIn(user.email, cpsw);
			if (id == 0)
			{
				throw new RuntimeException("密码错误");
			}
			var u = req.getJSONObject("newUser");

			userService.updateUserinfo(u.getString("companyName"), u.getString("profile"), id);

			response.put("status", 1);
			response.put("msg", "Success");
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", e.getMessage());
		}
		return response.toString();
	}

	@Authorization
	@RequestMapping(value = "updatePassword", method = {RequestMethod.POST})
	public String updatePassword(@CurrentUser User user, @RequestBody String request)
	{
		var req = new JSONObject(request);
		var response = new JSONObject();
		try
		{
			var cpsw = req.getString("confirmPassword");
			var id = userService.signIn(user.email, cpsw);
			if (id == 0)
			{
				throw new RuntimeException("密码错误");
			}
			var psw = req.getString("newPassword");
			MStringUtils.confirmPsw(psw);

			userService.updatePassword(psw, id);

			response.put("status", 1);
			response.put("msg", "Success");
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", e.getMessage());
		}
		return response.toString();
	}
}
