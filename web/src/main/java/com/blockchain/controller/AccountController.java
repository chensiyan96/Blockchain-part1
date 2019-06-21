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

	@Authorization
	@RequestMapping(value = "getMoney", method = { RequestMethod.GET })
	public String getMoney(@CurrentUser User user)
	{
		return JSONUtils.successResponse("money", accountService.queryMoney(user.id));
	}

	@Authorization
	@RequestMapping(value = "recharge", method = { RequestMethod.POST })
	public String recharge(@CurrentUser User user, @RequestBody String request)
	{
        var req = new JSONObject(request);
        var money = new BigDecimal(req.getString("money"));
	    if (accountService.investMoney(user.id, money)) {
            return JSONUtils.successResponse();
        }
        else {
            return JSONUtils.failResponse("银行卡余额不足");
        }
	}

	@Authorization
	@RequestMapping(value = "withdraw", method = { RequestMethod.POST })
	public String withdraw(@CurrentUser User user, @RequestBody String request)
	{
        var req = new JSONObject(request);
        var money = new BigDecimal(req.getString("money"));
        if (accountService.withdrawMoney(user.id, money)) {
            return JSONUtils.successResponse();
        }
        else {
            return JSONUtils.failResponse("账户余额不足");
        }
	}

//	@Authorization
//	@RequestMapping(value = "getPayments", method = {RequestMethod.GET})
//	public String getPayments(@CurrentUser User user)
//	{
//		var response = new JSONObject();
//		try
//		{
//			var r = paymentService.getPaymentsByUser(user.id);
//			List<JSONObject> j = new LinkedList<>();
//			for (var i : r)
//			{
//				j.add(i.toJSON());
//			}
//			response.put("status", 1);
//			response.put("msg", "Success");
//			response.put("data", j);
//		} catch (Exception e)
//		{
//			response.put("status", 0);
//			response.put("msg", e.getMessage());
//		}
//		return response.toString();
//	}

}
