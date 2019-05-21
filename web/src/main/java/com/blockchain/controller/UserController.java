package com.blockchain.controller;

import com.blockchain.model.User;
import com.blockchain.service.UserService;
import com.blockchain.utils.AESToken;
import com.blockchain.utils.JSON;
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
			user.name = req.getString("name");
			user.password = req.getString("password");
			user.phone = req.getString("phone");
			//todo:如果需要头像，则这里应该设置成默认头像图片的位置。
			user.avatar = "default";
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
		var t = new AESToken();
		try
		{
			var token = req.getString("token");
			response = t.verifyToken(token);
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
		AESToken t = new AESToken();
		try
		{
			var id = userService.signIn(req.getString("email"), req.getString("password"));
			var user = userService.getUserInfoByID(id);
			var userInfo = user.toJSON();
			//todo：更合理的token验证
			var token = t.getToken(userInfo);
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

}
