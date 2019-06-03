package com.blockchain.controller;

import com.blockchain.model.User;
import com.blockchain.service.AccountService;
import com.blockchain.service.PaymentService;
import com.blockchain.service.UserService;
import com.blockchain.utils.AESToken;
import com.blockchain.utils.Authorization;
import com.blockchain.utils.CurrentUser;
import com.blockchain.utils.JSON;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "api/account")
public class AccountController
{

	@Autowired
	private AccountService accountService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private UserService userService;

	@Authorization
	@RequestMapping(value = "getMoney", method = {RequestMethod.GET})
	public String getMoney(@CurrentUser User user)
	{
		var response = new JSON();
		try
		{
			var money = accountService.getMoney(user.id);
			response.put("status", 1);
			response.put("msg", "Success");
			response.put("money", money);
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", e.getMessage());
		}
		return response.toString();
	}

	@Authorization
	@RequestMapping(value = "recharge", method = {RequestMethod.POST})
	public String recharge(@CurrentUser User user, @RequestBody String request)
	{
		var req = new JSON(request);
		var response = new JSON();
		try
		{
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

	@Authorization
	@RequestMapping(value = "withdraw", method = {RequestMethod.POST})
	public String withdraw(@CurrentUser User user, @RequestBody String request)
	{
		var req = new JSON(request);
		var response = new JSON();
		try
		{
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
