package com.blockchain.controller;

import com.blockchain.model.AgreeStatus;
import com.blockchain.model.User;
import com.blockchain.service.*;
import com.blockchain.utils.Authorization;
import com.blockchain.utils.CurrentUser;
import com.blockchain.utils.MDateCmp;
import com.blockchain.utils.MStatusUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
		var req = new JSONObject(request);
		var response = new JSONObject();
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
			var t = new JSONObject();
			t.put("partyA", partyA);
			t.put("partyB", partyB);
			t.put("msg", "nmd 该还钱了");
//			quartzService.addJob("credit" + cid, "credit", "nmsl", "nmsl", RepaidNoticeJob.class,
//					MDateCmp.cronFormate(ddl), t);
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
		var req = new JSONObject(request);
		var response = new JSONObject();
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
		var req = new JSONObject(request);
		var response = new JSONObject();
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
		var response = new JSONObject();
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
		var response = new JSONObject();
		try
		{
			var u = userService.getUserInfoByEmail(user.email);
			var ag = agreementService.getAgreementsByUser(u.id);
			List<JSONObject> t = new LinkedList<>();
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
