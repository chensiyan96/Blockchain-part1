package com.blockchain.controller;

import com.blockchain.controller.Jobs.RepaidNoticeJob;
import com.blockchain.model.AgreeStatus;
import com.blockchain.service.AgreementService;
import com.blockchain.service.CreditService;
import com.blockchain.service.PaymentService;
import com.blockchain.service.QuartzService;
import com.blockchain.service.UserService;
import com.blockchain.utils.AESToken;
import com.blockchain.utils.JSON;
import com.blockchain.utils.MDateCmp;
import com.blockchain.utils.MStatusUtils;
import java.util.Date;
import org.apache.ibatis.jdbc.Null;
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
	@Autowired
	private final QuartzService quartzService;

	public AgreementController()
	{
		agreementService = null;
		paymentService = null;
		creditService = null;
		userService = null;
		quartzService = null;
	}

	public AgreementController(AgreementService agreementService,
			PaymentService paymentService, CreditService creditService,
			UserService userService, QuartzService quartzService)
	{
		this.agreementService = agreementService;
		this.paymentService = paymentService;
		this.creditService = creditService;
		this.userService = userService;
		this.quartzService = quartzService;
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
			int partyA = userService.getUserInfoByEmail(c.getString("partyA")).id;
			int partyB = userService.getUserInfoByEmail(c.getString("partyB")).id;
			Date d = new Date();
			var ddl = MDateCmp.timeAdd(c.getInt("deadline"), d);
			int cid = creditService.create(partyA, partyB, d,
					ddl, c.getBigDecimal("money"));
			int aid = agreementService.create(partyA, partyB, d,
					cid, a.getString("terms"));
			JSON t = new JSON();
			t.put("partyA", partyA);
			t.put("partyB", partyB);
			t.put("msg", "nmd 该还钱了");
			quartzService.addJob("credit" + cid, "credit", "nmsl", "nmsl", RepaidNoticeJob.class,
					MDateCmp.cronFormate(ddl), t);
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
			var cd = creditService.getCredit(ag.creditId).toJSON();
			var t = ag.toJSON();
			t.put("credit",cd);
			response.put("status", 1);
			response.put("msg", "Success");
			response.put("agreement", t);
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", e.getMessage());
		}
		return response.toString();
	}

}
