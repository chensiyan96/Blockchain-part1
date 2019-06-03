package com.blockchain.controller;

import com.blockchain.model.FinancingStatus;
import com.blockchain.service.AgreementService;
import com.blockchain.service.FinancingService;
import com.blockchain.service.MortgageService;
import com.blockchain.service.PaymentService;
import com.blockchain.service.UserService;
import com.blockchain.utils.AESToken;
import com.blockchain.utils.JSON;
import com.blockchain.utils.MStatusUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/financing")
public class FinancingController
{

	@Autowired
	private final PaymentService paymentService;
	@Autowired
	private final UserService userService;
	@Autowired
	private final FinancingService financingService;
	@Autowired
	private final MortgageService mortgageService;
	@Autowired
	private final AgreementService agreementService;

	public FinancingController()
	{
		mortgageService = null;
		financingService = null;
		userService = null;
		paymentService = null;
		agreementService = null;
	}

	public FinancingController(PaymentService paymentService,
			UserService userService, FinancingService financingService,
			MortgageService mortgageService,
			AgreementService agreementService)
	{
		this.paymentService = paymentService;
		this.userService = userService;
		this.financingService = financingService;
		this.mortgageService = mortgageService;
		this.agreementService = agreementService;
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
			var fid = req.getInt("fid");
			var user = res.getJSONObject("user");
			var u = userService.getUserInfoByEmail(user.getString("email"));
			var status = FinancingStatus.values()[req.getInt("status")];
			var f = financingService.getFinancing(fid);

			if (!status.equals(f.status))
			{
				throw new Exception("参数错误");
			}

			if (status.equals(FinancingStatus.init) && u.id == f.partyA)
			{
				financingService.updateStatus(MStatusUtils.getNextFinancingStatus(status), fid);
			} else if (status.equals(FinancingStatus.confirminfo))
			{
				var tmp = agreementService.getAgreement(f.aid);
				if (u.id == tmp.partyA)
				{
					financingService.updateStatus(MStatusUtils.getNextFinancingStatus(status), fid);
				}

			} else if (status.equals(FinancingStatus.paid) && u.id == f.partyB)
			{
				financingService.updateStatus(MStatusUtils.getNextFinancingStatus(status), fid);
			} else
			{
				throw new Exception("参数错误");
			}
			response.put("status", 1);
			response.put("msg", "Success");
			response.put("agreement", fid);

		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", e.getMessage());
		}
		return response.toString();
	}

	@RequestMapping(value = "pay", method = {RequestMethod.POST})
	public String pay(@RequestBody String request)
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
			var fid = req.getInt("fid");
			var user = res.getJSONObject("user");
			var money = req.getBigDecimal("money");
			var u = userService.getUserInfoByEmail(user.getString("email"));
			var status = FinancingStatus.values()[req.getInt("status")];
			var f = financingService.getFinancing(fid);

			if (status != FinancingStatus.cbConfirm || u.id != f.partyA)
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

	@RequestMapping(value = "getFinancing", method = {RequestMethod.POST})
	public String getFinancing(@RequestBody String request)
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
			var fid = req.getInt("fid");
			var f = financingService.getFinancing(fid);
			var m = mortgageService.getMortgage(f.mid);

			response.put("status", 1);
			response.put("msg", "Success");
			response.put("financing", f);
			response.put("mortgage", m);
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", e.getMessage());
		}
		return response.toString();
	}
}
