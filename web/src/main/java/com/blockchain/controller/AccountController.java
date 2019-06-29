package com.blockchain.controller;

import com.blockchain.model.User;
import com.blockchain.service.AccountService;
import com.blockchain.utils.Authorization;
import com.blockchain.utils.CurrentUser;
import com.blockchain.utils.JSONUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin
@RestController
@RequestMapping(value = "api/account")
public class AccountController
{
	@Autowired
	private AccountService accountService;

	// 余额查询
	@Authorization
	@RequestMapping(value = "getMoney", method = { RequestMethod.GET })
	public String getMoney(@CurrentUser User user)
	{
		var money = accountService.queryMoney(user);
		if (money != null) {
			return JSONUtils.successResponse(money);
		}
		return JSONUtils.failResponse("请稍后再试");
	}

	// 充值
	@Authorization
	@RequestMapping(value = "recharge", method = { RequestMethod.POST })
	public String recharge(@CurrentUser User user, @RequestBody String request)
	{
        var req = new JSONObject(request);
        var money = new BigDecimal(req.getString("money"));
		if (money.compareTo(BigDecimal.ZERO) <= 0) {
			return JSONUtils.failResponse("金额必须大于0");
		}
		var s = accountService.investMoney(user, money);
		return s == null ? JSONUtils.successResponse() : JSONUtils.failResponse(s);
	}

	// 提现
	@Authorization
	@RequestMapping(value = "withdraw", method = { RequestMethod.POST })
	public String withdraw(@CurrentUser User user, @RequestBody String request)
	{
        var req = new JSONObject(request);
        var money = new BigDecimal(req.getString("money"));
		if (money.compareTo(BigDecimal.ZERO) <= 0) {
			return JSONUtils.failResponse("金额必须大于0");
		}
		var s = accountService.withdrawMoney(user, money);
		return s == null ? JSONUtils.successResponse() : JSONUtils.failResponse(s);
	}
}
