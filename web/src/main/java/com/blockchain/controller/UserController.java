package com.blockchain.controller;

import com.blockchain.model.CoreBusinessProfile;
import com.blockchain.model.MoneyGiverProfile;
import com.blockchain.model.SupplierProfile;
import com.blockchain.model.User;
import com.blockchain.service.AccountService;
import com.blockchain.service.UserService;
import com.blockchain.utils.AESToken;
import com.blockchain.utils.Authorization;
import com.blockchain.utils.CurrentUser;
import com.blockchain.utils.JSONUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "api/user")
public class UserController
{
	@Autowired
	private UserService userService;
	@Autowired
	private AccountService accountService;

	// 注册
	@RequestMapping(value = "register", method = { RequestMethod.POST })
	public String register(@RequestBody String request)
	{
		// 1.检查输入是否符合要求
		var req = new JSONObject(request);
		var email = req.getString("email").toUpperCase();
		if (email.isEmpty() || !email.contains("@")) {
			return JSONUtils.failResponse("邮箱无效");
		}
		if (userService.getUserByEmail(email) != null) {
			return JSONUtils.failResponse("邮箱已被注册");
		}
		var password = req.getString("password");
		if(password.isEmpty()) {
			return JSONUtils.failResponse("密码不能为空");
		}

		// 2.创建User对象
		var user = new User();
		var err_msg = user.hashAndSetPassword(password);
		if (err_msg != null) {
			return JSONUtils.failResponse(err_msg);
		}
		user.db.email = email;
		user.db.name = req.getString("name");
		user.db.role = User.Roles.valueOf(req.getString("role"));
		if (req.has("additional")) {
			user.db.additional = req.getString("additional");
		}

		// 3.根据用户身份添加profile
		switch (user.db.role)
		{
			case Supplier:
				user.profile = new SupplierProfile();
				break;
			case CoreBusiness:
				user.profile = new CoreBusinessProfile();
				break;
			case MoneyGiver:
				user.profile = new MoneyGiverProfile();
				break;
			case Admin:
				user.profile = null;
				break;
		}

		// 4.在数据库中添加字段
		if (userService.insertUser(user) == 0) {
			return JSONUtils.failResponse("注册失败");
		}

		// 5.在区块链里创建账户
		accountService.createAccount(user.db.id);

		// 6.返回成功提示
		return JSONUtils.successResponse();
	}

	// 登录
	@RequestMapping(value = "login", method = { RequestMethod.POST })
	public String login(@RequestBody String request)
	{
		// 1.检查用户名和密码
		var req = new JSONObject(request);
		var user = userService.getUserByEmail(req.getString("email").toUpperCase());
		if (user == null) {
			return JSONUtils.failResponse("用户名不存在");
		}
		var err_msg = user.checkPassword(req.getString("password"));
		if (err_msg != null) {
			return JSONUtils.failResponse(err_msg);
		}

		// 2.生成并返回token
		var token = AESToken.getToken(user.toJSON());
		return JSONUtils.successResponse("data", token);
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
	@RequestMapping(value = "updateUserInfo", method = { RequestMethod.POST })
	public String updateUserInfo(@CurrentUser User user, @RequestBody String request)
	{
		// 1.检查密码
		var req = new JSONObject(request);
		var err_msg = user.checkPassword(req.getString("confirmPassword"));
		if (err_msg != null) {
			return JSONUtils.failResponse(err_msg);
		}

		// 2.在数据库中更新
		var u = req.getJSONObject("newUser");
		if (u.has("name")) {
			user.db.name = u.getString("name");
		}
		if (u.has("additional")) {
			user.db.additional = u.getString("additional");
		}
		userService.updateUser(user);

		// 3.返回成功提示
		return JSONUtils.successResponse();
	}

	// 修改密码
	@Authorization
	@RequestMapping(value = "updatePassword", method = { RequestMethod.POST })
	public String updatePassword(@CurrentUser User user, @RequestBody String request)
	{
		// 1.检查旧的密码
		var req = new JSONObject(request);
		var err_msg = user.checkPassword(req.getString("confirmPassword"));
		if (err_msg != null) {
			return JSONUtils.failResponse(err_msg);
		}

		// 2.在数据库中更新
		user.hashAndSetPassword(req.getString("newPassword"));
		userService.updateUser(user);

		// 3.返回成功提示
		return JSONUtils.successResponse();
	}

	// 获取所有资金方信息
	@RequestMapping(value = "getAllMoneyGivers", method = { RequestMethod.GET })
	public String getAllMoneyGivers()
	{
		return JSONUtils.successResponse("data", JSONUtils.arrayToJSONs(userService.getAllMoneyGivers()));
	}
}
