package com.blockchain.controller;

import com.blockchain.service.AccountService;
import com.blockchain.service.PaymentService;
import com.blockchain.service.UserService;
import com.blockchain.utils.AESToken;
import com.blockchain.utils.JSON;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/account")
public class AccountController
{

	@Autowired
	private final AccountService accountService;
	@Autowired
	private final PaymentService paymentService;
	@Autowired
	private final UserService userService;

	public AccountController()
	{
		accountService = null;
		paymentService = null;
		userService = null;
	}

	public AccountController(AccountService accountService,
			PaymentService paymentService, UserService userService)
	{
		this.accountService = accountService;
		this.paymentService = paymentService;
		this.userService = userService;
	}

	@RequestMapping(value = "getMoney", method = {RequestMethod.GET})
	public String getMoney(@RequestBody String request)
	{
		var req = new JSON(request);
		var response = new JSON();
		try
		{
			var token = req.getString("token");
			var res = AESToken.verifyToken(token);
			var u = res.getJSONObject("user");
			var user = userService.getUserInfoByEmail(u.getString("email"));
			var money = accountService.getMoney(user.id);
			response.put("status", 1);
			response.put("msg", "Success");
			response.put("money", money);
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg",e.getMessage());
		}
		return response.toString();
	}


	@RequestMapping(value = "recharge", method = {RequestMethod.POST})
	public String recharge(@RequestBody String request)
	{
		var req = new JSON(request);
		var response = new JSON();
		try
		{
			var token = req.getString("token");
			var res = AESToken.verifyToken(token);
			var u = res.getJSONObject("user");
			var user = userService.getUserInfoByEmail(u.getString("email"));
			var money = new BigDecimal(req.getString("money"));
			paymentService.transfer(1, user.id, money);
			response.put("status", 1);
			response.put("msg", "Success");
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", e.getMessage());
		}
		return response.toString();
	}

	@RequestMapping(value = "withdraw", method = {RequestMethod.POST})
	public String withdraw(@RequestBody String request)
	{
		var req = new JSON(request);
		var response = new JSON();
		try
		{
			var token = req.getString("token");
			var res = AESToken.verifyToken(token);
			var u = res.getJSONObject("user");
			var user = userService.getUserInfoByEmail(u.getString("email"));
			var money = new BigDecimal(req.getString("money"));
			paymentService.transfer(user.id, 1, money);
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
