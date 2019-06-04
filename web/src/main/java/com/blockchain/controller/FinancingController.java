package com.blockchain.controller;

import com.blockchain.model.FinancingStatus;
import com.blockchain.model.User;
import com.blockchain.service.AgreementService;
import com.blockchain.service.FinancingService;
import com.blockchain.service.MortgageService;
import com.blockchain.service.PaymentService;
import com.blockchain.service.UserService;
import com.blockchain.utils.Authorization;
import com.blockchain.utils.CurrentUser;
import com.blockchain.utils.JSON;
import com.blockchain.utils.MStatusUtils;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/financing")
public class FinancingController
{

	@Autowired
	private PaymentService paymentService;
	@Autowired
	private UserService userService;
	@Autowired
	private FinancingService financingService;
	@Autowired
	private MortgageService mortgageService;
	@Autowired
	private AgreementService agreementService;

	@Authorization
	@RequestMapping(value = "create", method = {RequestMethod.POST})
	public String create(@CurrentUser User user, @RequestBody String request)
	{
		JSON req = new JSON(request);
		JSON response = new JSON();
		try
		{
			var f = new JSON(req.getJSONObject("financing").toString());
			var m = new JSON(f.getJSONObject("mortgage").toString());
			int partyA = userService.getUserInfoByEmail(m.getString("partyA")).id;
			int partyB = userService.getUserInfoByEmail(m.getString("partyB")).id;

			int mid = mortgageService
					.create(m.getInt("cid"), m.getBigDecimal("money"), partyA, partyB);

			int fid = financingService
					.create(f.getInt("aid"), mid, partyA, partyB, f.getString("terms"));
			response.put("status", 1);
			response.put("msg", "Success");
			response.put("financing", fid);

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
			var fid = req.getInt("fid");
			var status = FinancingStatus.values()[req.getInt("status")];
			var f = financingService.getFinancing(fid);

			if (!status.equals(f.status))
			{
				throw new Exception("参数错误");
			}

			if (status.equals(FinancingStatus.init) && user.id == f.partyA)
			{
				financingService.updateStatus(MStatusUtils.getNextFinancingStatus(status), fid);
			} else if (status.equals(FinancingStatus.confirminfo))
			{
				var tmp = agreementService.getAgreement(f.aid);
				if (user.id == tmp.partyA)
				{
					financingService.updateStatus(MStatusUtils.getNextFinancingStatus(status), fid);
				}

			} else if (status.equals(FinancingStatus.paid) && user.id == f.partyB)
			{
				financingService.updateStatus(MStatusUtils.getNextFinancingStatus(status), fid);
			} else
			{
				throw new Exception("参数错误");
			}
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
	@RequestMapping(value = "pay", method = {RequestMethod.POST})
	public String pay(@CurrentUser User user, @RequestBody String request)
	{
		JSON req = new JSON(request);
		JSON response = new JSON();
		try
		{
			var fid = req.getInt("fid");
			var money = req.getBigDecimal("money");
			var status = FinancingStatus.values()[req.getInt("status")];
			var f = financingService.getFinancing(fid);

			if (status != FinancingStatus.cbConfirm || user.id != f.partyA)
			{
				throw new Exception("参数错误");
			}

			paymentService.transfer(f.partyA, f.partyB, money);
			financingService.updateStatus(MStatusUtils.getNextFinancingStatus(status), fid);

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
	@RequestMapping(value = "getFinancing", method = {RequestMethod.GET})
	public String getFinancing(@CurrentUser User user, @RequestParam("mid") int fid)
	{
		JSON response = new JSON();
		try
		{
			var f = financingService.getFinancing(fid);
			var m = mortgageService.getMortgage(f.mid);
			var fi = f.toJSON();
			fi.put("mortgage", m.toJSON());
			response.put("status", 1);
			response.put("msg", "Success");
			response.put("financing", fi);
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", e.getMessage());
		}
		return response.toString();
	}

	@Authorization
	@RequestMapping(value = "getFinancingByUser", method = {RequestMethod.GET})
	public String getFinancingByUser(@CurrentUser User user)
	{
		JSON response = new JSON();
		try
		{
			var f = financingService.getFinancingByUser(user.id);
			List<JSON> t = new LinkedList<>();
			for (var i : f)
			{
				var m = mortgageService.getMortgage(i.mid).toJSON();
				var tmp = i.toJSON();
				tmp.put("mortgage", m);
				t.add(tmp);
			}
			response.put("status", 1);
			response.put("msg", "Success");
			response.put("financing", t);
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", e.getMessage());
		}
		return response.toString();
	}
}
