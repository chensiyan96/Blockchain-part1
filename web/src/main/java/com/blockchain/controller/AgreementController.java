package com.blockchain.controller;

import com.blockchain.controller.Jobs.RepaidNoticeJob;
import com.blockchain.model.AgreeStatus;
import com.blockchain.model.User;
import com.blockchain.service.AgreementService;
import com.blockchain.service.CreditService;
import com.blockchain.service.PaymentService;
import com.blockchain.service.QuartzService;
import com.blockchain.service.UserService;
import com.blockchain.utils.AESToken;
import com.blockchain.utils.Authorization;
import com.blockchain.utils.CurrentUser;
import com.blockchain.utils.JSON;
import com.blockchain.utils.MDateCmp;
import com.blockchain.utils.MStatusUtils;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "api/agreement")
public class AgreementController
{

	@Autowired
	private AgreementService agreementService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private CreditService creditService;
	@Autowired
	private UserService userService;
	@Autowired
	private QuartzService quartzService;

	@Authorization
	@RequestMapping(value = "create", method = {RequestMethod.POST})
	public String create(@CurrentUser User user, @RequestBody String request)
	{
		JSON req = new JSON(request);
		JSON response = new JSON();
		try
		{
			var a = req.getJSONObject("agreement");
			var c = a.getJSONObject("credit");
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

	@Authorization
	@RequestMapping(value = "confirm", method = {RequestMethod.POST})
	public String confirm(@CurrentUser User user, @RequestBody String request)
	{
		JSON req = new JSON(request);
		JSON response = new JSON();
		try
		{
			var aid = req.getInt("aid");
			var u = userService.getUserInfoByEmail(user.email);
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

	@Authorization
	@RequestMapping(value = "repay", method = {RequestMethod.POST})
	public String repay(@CurrentUser User user, @RequestBody String request)
	{
		JSON req = new JSON(request);
		JSON response = new JSON();
		try
		{
			var aid = req.getInt("aid");
			var u = userService.getUserInfoByEmail(user.email);
			var status = AgreeStatus.values()[req.getInt("status")];
			var ag = agreementService.getAgreement(aid);
			var cd = creditService.getCredit(ag.creditId);

			if (status != AgreeStatus.comfirmship || !status.equals(ag.status) || u.id != cd.partyA)
			{
				throw new Exception("参数错误");
			}

			paymentService.transfer(cd.partyA, cd.partyB, cd.money);
			creditService.updateStatus(1, cd.id);
			agreementService.updateStatus(MStatusUtils.getNextAgreeStatus(status), aid);
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
	@RequestMapping(value = "getAgreement", method = {RequestMethod.GET})
	public String getAgreement(@CurrentUser User user, @RequestParam("aid") Integer aid)
	{
		JSON response = new JSON();
		try
		{
			var ag = agreementService.getAgreement(aid);
			var cd = creditService.getCredit(ag.creditId).toJSON();
			var t = ag.toJSON();
			t.put("credit", cd);
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

	@Authorization
	@RequestMapping(value = "getAgreementByUser", method = {RequestMethod.GET})
	public String getAgreementByUser(@CurrentUser User user)
	{
		JSON response = new JSON();
		try
		{
			var u = userService.getUserInfoByEmail(user.email);
			var ag = agreementService.getAgreementsByUser(u.id);
			List<JSON> t = new LinkedList<>();
			for (var i : ag)
			{
				var cd = creditService.getCredit(i.creditId).toJSON();
				var tmp = i.toJSON();
				tmp.put("credit", cd);
				t.add(tmp);
			}
			response.put("status", 1);
			response.put("msg", "Success");
			response.put("agreements", t);
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", e.getMessage());
		}
		return response.toString();
	}
}
