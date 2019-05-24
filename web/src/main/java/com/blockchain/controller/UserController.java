package com.blockchain.controller;

import com.blockchain.model.Roles;
import com.blockchain.model.User;
import com.blockchain.service.UserService;
import com.blockchain.utils.AESToken;
import com.blockchain.utils.JSON;
import com.blockchain.utils.MStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/user")
public class UserController
{

	@Autowired
	private final UserService userService;

	//todo：这地方不太对，想办法解决一下
	public UserController()
	{
		userService = null;
	}

	public UserController(UserService _service)
	{
		userService = _service;
	}

	@RequestMapping(value = "register", method = {RequestMethod.POST})
	public String register(@RequestBody String request)
	{
		JSON req = new JSON(request);
		JSON response = new JSON();
		try
		{
			User user = new User();
			user.email = req.getString("email");
			user.normalizedEmail = MStringUtils.normalize(req.getString("email"));
			user.companyName = req.getString("name");
			user.passwordHash = AESToken.encrypt(req.getString("password"));
			user.role = Roles.valueOf(req.getString("role"));
			user.profile = req.getString("profile");
			if (userService.isEmailExist(user.email))
			{
				response.put("status", 0);
				response.put("msg", "邮箱已被注册");
			} else
			{
				var id = userService.register(user);
				response.put("status", 1);
				response.put("msg", "Success");
			}

		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", "");
		}
		return response.toString();
	}

	@RequestMapping(value = "getUserInfo", method = {RequestMethod.GET})
	public String getUserInfo(@RequestBody String request)
	{
		var req = new JSON(request);
		var response = new JSON();
		try
		{
			var token = req.getString("token");
			var res = AESToken.verifyToken(token);
			var u = res.getJSONObject("user");
			var user = userService.getUserInfoByEmail(u.getString("email"));
			response.put("status", 1);
			response.put("msg", "Success");
			u.put("profile", user.profile);
			response.put("user", u);
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", "没有登录");
		}
		return response.toString();
	}

	@RequestMapping(value = "login", method = {RequestMethod.POST})
	public String login(@RequestBody String request)
	{
		var req = new JSON(request);
		var res = new JSON();
		try
		{
			var id = userService.signIn(req.getString("email"), req.getString("password"));
			if (id == 0)
			{
				throw new RuntimeException("用户名或者密码错误");
			}
			var user = userService.getUserInfoByID(id);
			var userInfo = user.toJSON();
			//todo：更合理的token验证
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

	@RequestMapping(value = "updateUserInfo", method = {RequestMethod.POST})
	public String updateUserInfo(@RequestBody String request)
	{
		var req = new JSON(request);
		var response = new JSON();
		try
		{
			var cpsw = req.getString("confirmPassword");
			var token = req.getString("token");
			var res = AESToken.verifyToken(token);
			var u = res.getJSONObject("user");
			var id = userService.signIn(u.getString("email"), cpsw);
			if (id == 0)
			{
				throw new RuntimeException("密码错误");
			}
			u = req.getJSONObject("newUser");

			userService.updateUserinfo(u.getString("companyName"), u.getString("profile"), id);

			var user = userService.getUserInfoByID(id);
			var userInfo = user.toJSON();
			token = AESToken.getToken(userInfo);
			res.put("status", 1);
			res.put("msg", "Success");
			res.put("data", token);
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", "密码错误");
		}
		return response.toString();
	}

	@RequestMapping(value = "updatePassword", method = {RequestMethod.POST})
	public String updatePassword(@RequestBody String request)
	{
		var req = new JSON(request);
		var response = new JSON();
		try
		{
			var cpsw = req.getString("confirmPassword");
			var token = req.getString("token");
			var res = AESToken.verifyToken(token);
			var u = res.getJSONObject("user");
			var id = userService.signIn(u.getString("email"), cpsw);
			if (id == 0)
			{
				throw new RuntimeException("密码错误");
			}
			var psw = req.getString("newPassword");

			userService.updatePassword(psw, id);

			res.put("status", 1);
			res.put("msg", "Success");
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", "密码错误");
		}
		return response.toString();
	}
}
