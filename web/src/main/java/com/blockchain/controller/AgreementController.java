package com.blockchain.controller;

import com.blockchain.model.AgreeStatus;
import com.blockchain.model.Agreement;
import com.blockchain.service.AgreementService;
import com.blockchain.service.CreditService;
import com.blockchain.service.PaymentService;
import com.blockchain.service.UserService;
import com.blockchain.utils.AESToken;
import com.blockchain.utils.JSON;
import com.blockchain.utils.MDateCmp;
import com.blockchain.utils.MStatusUtils;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/agreement")
public class AgreementController
{

	@Autowired
	private final AgreementService agreementService;
	@Autowired
	private final PaymentService paymentService;
	@Autowired
	private final CreditService creditService;
	@Autowired
	private final UserService userService;

	public AgreementController()
	{
		agreementService = null;
		paymentService = null;
		creditService = null;
		userService = null;
	}

	public AgreementController(AgreementService agreementService,
			PaymentService paymentService, CreditService creditService,
			UserService userService)
	{
		this.agreementService = agreementService;
		this.paymentService = paymentService;
		this.creditService = creditService;
		this.userService = userService;
	}

	@RequestMapping(value = "create", method = {RequestMethod.POST})
	public String create(@RequestBody String request)
	{
		JSON req = new JSON(request);
		JSON response = new JSON();
		try
		{
			var token = req.getString("token");
			var res = AESToken.verifyToken(token);
			if (res.getInt("status") == 0)
			{
				throw new Exception("登录信息错误");
			}
			var a = req.getJSONObject("agreement");
			var c = req.getJSONObject("credit");
			Date d = new Date();
			int cid = creditService.create(c.getInt("partyA"), c.getInt("partyB"), d,
					MDateCmp.timeAdd(c.getInt("deadline"), d), c.getBigDecimal("money"));
			int aid = agreementService.create(a.getInt("partyA"), a.getInt("partyB"), d,
					cid, a.getString("terms"));
			response.put("status", 1);
			response.put("msg", "Success");
			response.put("agreement", aid);

		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", e.getMessage());
		}
		return response.toString();
	}

	@RequestMapping(value = "confirm", method = {RequestMethod.POST})
	public String confirm(@RequestBody String request)
	{
		JSON req = new JSON(request);
		JSON response = new JSON();
		try
		{
			var token = req.getString("token");
			var res = AESToken.verifyToken(token);
			if (res.getInt("status") == 0)
			{
				throw new Exception("登录信息错误");
			}
			var aid = req.getInt("aid");
			var user = res.getJSONObject("user");
			var u = userService.getUserInfoByEmail(user.getString("email"));
			var status = AgreeStatus.values()[req.getInt("status")];
			var ag = agreementService.getAgreement(aid);

			if (!status.equals(ag.status))
			{
				throw new Exception("参数错误");
			}

			if ((MStatusUtils.agreeStatusDivision(status) == 1 && u.id == ag.partyA) ||
					(MStatusUtils.agreeStatusDivision(status) == 2 && u.id == ag.partyB))
			{

				agreementService.updateStatus(MStatusUtils.getNextAgreeStatus(status), aid);
				if (status.equals(AgreeStatus.repaid))
				{
					creditService.updateStatus(2, ag.creditId);
				}
			} else
			{
				throw new Exception("参数错误");
			}

			response.put("status", 1);
			response.put("msg", "Success");
			response.put("agreement", aid);

		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", e.getMessage());
		}
		return response.toString();
	}

	@RequestMapping(value = "repay", method = {RequestMethod.POST})
	public String repay(@RequestBody String request)
	{
		JSON req = new JSON(request);
		JSON response = new JSON();
		try
		{
			var token = req.getString("token");
			var res = AESToken.verifyToken(token);
			if (res.getInt("status") == 0)
			{
				throw new Exception("登录信息错误");
			}
			var aid = req.getInt("aid");
			var user = res.getJSONObject("user");
			var u = userService.getUserInfoByEmail(user.getString("email"));
			var status = AgreeStatus.values()[req.getInt("status")];
			var ag = agreementService.getAgreement(aid);
			var cd = creditService.getCredit(ag.creditId);

			if (status != AgreeStatus.comfirmship || u.id != cd.partyA)
			{
				throw new Exception("参数错误");
			}

			paymentService.transfer(cd.partyA, cd.partyB, cd.money);
			creditService.updateStatus(1, cd.id);
			response.put("status", 1);
			response.put("msg", "Success");
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", e.getMessage());
		}
		return response.toString();
	}

	@RequestMapping(value = "getAgreement", method = {RequestMethod.POST})
	public String getAgreement(@RequestBody String request)
	{
		JSON req = new JSON(request);
		JSON response = new JSON();
		try
		{
			var token = req.getString("token");
			var res = AESToken.verifyToken(token);
			if (res.getInt("status") == 0)
			{
				throw new Exception("登录信息错误");
			}
			var aid = req.getInt("aid");
			var ag = agreementService.getAgreement(aid);
			var cd = creditService.getCredit(ag.creditId);

			response.put("status", 1);
			response.put("msg", "Success");
			response.put("agreement", ag);
			response.put("credit", cd);
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", e.getMessage());
		}
		return response.toString();
	}

}
