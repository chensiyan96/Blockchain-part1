package com.blockchain.controller;

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
		user.db.additional = req.has("additional") ? req.getString("additional") : null;

		// 3.根据用户身份添加profile
		// todo
//		switch (user.db.role)
//		{
//			case Supplier:
//				user.profile = new SupplierProfile();
//				break;
//			case CoreBusiness:
//				user.profile = new CoreBusinessProfile();
//				break;
//			case MoneyGiver:
//				user.profile = new MoneyGiverProfile();
//				break;
//			case Admin:
//				user.profile = null;
//				break;
//		}

		// 4.在数据库中添加字段
		if (!userService.insertUser(user)) {
			return JSONUtils.failResponse("注册失败");
		}

		// 5.在区块链里创建账户并返回成功提示
		accountService.createAccount(user.db.id);
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
			return JSONUtils.failResponse("不存在该用户");
		}
		var err_msg = user.checkPassword(req.getString("password"));
		if (err_msg != null) {
			return JSONUtils.failResponse(err_msg);
		}

		// 2.检查用户是否被冻结
		if (user.db.frozen != 0) {
			return JSONUtils.failResponse("用户已被冻结");
		}

		// 3.生成并返回token
		var token = AESToken.getToken(user.toJSON());
		return JSONUtils.successResponse(token);
	}

	// 获取当前用户的公开信息
	@Authorization
	@RequestMapping(value = "getUserInfo", method = { RequestMethod.GET })
	public String getUserInfo(@CurrentUser User user)
	{
		return JSONUtils.successResponse(user.toJSON());
	}

	// 通过email查询一个用户的信息
	@RequestMapping(value = "getUserByEmail", method = { RequestMethod.POST })
	public String getUserByEmail(@RequestBody String request)
	{
		var req = new JSONObject(request);
		var user = userService.getUserByEmail(req.getString("email").toUpperCase());
		if (user == null) {
			return JSONUtils.failResponse("不存在该用户");
		}
		return JSONUtils.successResponse(user.toJSON());
	}

	// 修改昵称
	@Authorization
	@RequestMapping(value = "updateName", method = { RequestMethod.POST })
	public String updateName(@CurrentUser User user, @RequestBody String request)
	{
		var req = new JSONObject(request);
		user.db.name = req.getString("name");
		userService.updateUser(user);
		return JSONUtils.successResponse();
	}

	// 修改附加信息
	@Authorization
	@RequestMapping(value = "updateAdditional", method = { RequestMethod.POST })
	public String updateAdditional(@CurrentUser User user, @RequestBody String request)
	{
		var req = new JSONObject(request);
		user.db.additional = req.has("additional") ? req.getString("additional") : null;
		userService.updateUser(user);
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

		// 2.在数据库中更新并返回成功提示
		user.hashAndSetPassword(req.getString("newPassword"));
		userService.updateUser(user);
		return JSONUtils.successResponse();
	}

	// 修改是否冻结
	@Authorization
	@RequestMapping(value = "setFrozen", method = { RequestMethod.POST })
	public String setFrozen(@CurrentUser User user, @RequestBody String request)
	{
		// 1.检查用户身份
		if (user.db.role != User.Roles.Admin) {
			return JSONUtils.failResponse("只有管理员才能冻结用户");
		}

		// 2.查找要冻结的用户
		var req = new JSONObject(request);
		var other_user = userService.getUserByID(req.getLong("id"));
		if (other_user == null) {
			return JSONUtils.failResponse("不存在此用户");
		}

		// 3.更新用户状态并返回成功提示
		user.db.frozen = req.getInt("frozen") != 0 ? (byte)1 : (byte)0;
		userService.updateUser(user);
		return JSONUtils.successResponse();
	}

	// 修改是否自动通过
	@Authorization
	@RequestMapping(value = "setAutoPass", method = { RequestMethod.POST })
	public String setAutoPass(@CurrentUser User user, @RequestBody String request)
	{
		// 1.检查用户身份
		if (user.db.role != User.Roles.CoreBusiness && user.db.role != User.Roles.MoneyGiver) {
			return JSONUtils.failResponse("只有核心企业和资金方才能设置授信或确权自动通过");
		}

		// 2.更新用户设置并返回成功提示
		var req = new JSONObject(request);
		user.db.autoPass = req.getInt("autoPass") != 0 ? (byte)1 : (byte)0;
		userService.updateUser(user);
		return JSONUtils.successResponse();
	}

	// 获取所有供应商信息
	@RequestMapping(value = "getAllSuppliers", method = { RequestMethod.GET })
	public String getAllSuppliers()
	{
		return JSONUtils.successResponse(JSONUtils.arrayToJSONs(userService.getAllSuppliers()));
	}

	// 获取所有核心企业信息
	@RequestMapping(value = "getAllCoreBusinesses", method = { RequestMethod.GET })
	public String getAllCoreBusinesses()
	{
		return JSONUtils.successResponse(JSONUtils.arrayToJSONs(userService.getAllCoreBusinesses()));
	}

	// 获取所有资金方信息
	@RequestMapping(value = "getAllMoneyGivers", method = { RequestMethod.GET })
	public String getAllMoneyGivers()
	{
		return JSONUtils.successResponse(JSONUtils.arrayToJSONs(userService.getAllMoneyGivers()));
	}
}
