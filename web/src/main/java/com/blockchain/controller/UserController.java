package com.blockchain.controller;

import com.blockchain.model.User;
import com.blockchain.service.UserService;
import com.blockchain.utils.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController
{
	@Autowired
	private final UserService service;

	//todo：这地方不太对，想办法解决一下
	public UserController()
	{
		service = null;
	}

	public UserController(UserService _service)
	{
		service = _service;
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
			var id = service.register(user);
			response.put("status", 0);
			response.put("msg", "Success");
			response.put("data", id);
		} catch (Exception e)
		{
			response.put("status", 1);
			response.put("msg", "");
		}
		return response.toString();
	}

	@RequestMapping(value = "getUserInfo", method = {RequestMethod.GET})
	public String getUserInfo(@RequestBody String request)
	{
		JSON req = new JSON(request);
		JSON response = new JSON();
		try
		{
			int id = req.getIntegerObject("id");
			User user = service.getUserInfoByID(id);
			response.put("status", 0);
			response.put("msg", "Success");
			response.put("data", user);
		} catch (Exception e)
		{
			response.put("status", 1);
			response.put("msg", "");
		}
		return response.toString();
	}

}
