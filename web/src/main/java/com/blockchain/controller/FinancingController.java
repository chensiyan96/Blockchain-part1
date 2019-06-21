package com.blockchain.controller;

import com.blockchain.model.Financing;
import com.blockchain.model.User;
import com.blockchain.service.FinancingService;
import com.blockchain.service.UserService;
import com.blockchain.utils.Authorization;
import com.blockchain.utils.CurrentUser;
import com.blockchain.utils.JSONUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

@RestController
@RequestMapping(value = "api/financing")
public class FinancingController
{
	@Autowired
	private UserService userService;
	@Autowired
	private FinancingService financingService;

    // 新建融资申请
	@Authorization
	@RequestMapping(value = "create", method = { RequestMethod.POST })
	public String create(@CurrentUser User us, @RequestBody String request)
	{
	    // 1.检查用户身份
	    if (us.db.role != User.Roles.Supplier) {
            return JSONUtils.failResponse("必须由供应商提交融资申请");
        }

	    // 2.检查申请的另外两方是否存在和身份是否正确
		var req = new JSONObject(request);
        User uc = userService.getUserByEmail(req.getString("CoreBusiness_email").toUpperCase());
        if (uc == null) {
            return JSONUtils.failResponse("核心企业email不存在");
        }
        if (uc.db.role !=  User.Roles.CoreBusiness) {
            return JSONUtils.failResponse("核心企业email身份不正确");
        }
        User um = userService.getUserByEmail(req.getString("CoreBusiness_email").toUpperCase());
        if (um == null) {
            return JSONUtils.failResponse("资金方email不存在");
        }
        if (um.db.role !=  User.Roles.MoneyGiver) {
            return JSONUtils.failResponse("资金方email身份不正确");
        }

        // 3.创建Financing对象
        var fin = new Financing();
        fin.setSupplier(us);
        fin.setCoreBusiness(uc);
        fin.setMoneyGiver(um);
        fin.db.createTime = new Date(System.currentTimeMillis());
        fin.db.state = 0;

        // 4.在数据库中添加字段
        if (financingService.insertFinancing(fin) == 0) {
            return JSONUtils.failResponse("创建失败");
        }

        // 5.返回成功提示
        return JSONUtils.successResponse();
	}

//	@Authorization
//	@RequestMapping(value = "confirm", method = {RequestMethod.POST})
//	public String confirm(@CurrentUser User user, @RequestBody String request)
//	{
//		var req = new JSONObject(request);
//		var response = new JSONObject();
//		try
//		{
//			var fid = req.getInt("fid");
//			var status = FinancingStatus.values()[req.getInt("status")];
//			var f = financingService.getFinancing(fid);
//
//			if (!status.equals(f.status))
//			{
//				throw new Exception("参数错误");
//			}
//
//			if (status.equals(FinancingStatus.init) && user.id == f.partyA)
//			{
//				financingService.updateStatus(MStatusUtils.getNextFinancingStatus(status), fid);
//			} else if (status.equals(FinancingStatus.confirminfo))
//			{
//				var tmp = agreementService.getAgreement(f.aid);
//				if (user.id == tmp.partyA)
//				{
//					financingService.updateStatus(MStatusUtils.getNextFinancingStatus(status), fid);
//				} else
//				{
//					throw new Exception("参数错误");
//				}
//
//			} else if (status.equals(FinancingStatus.paid) && user.id == f.partyB)
//			{
//				financingService.updateStatus(MStatusUtils.getNextFinancingStatus(status), fid);
//			} else
//			{
//				throw new Exception("参数错误");
//			}
//			response.put("status", 1);
//			response.put("msg", "Success");
//
//		} catch (Exception e)
//		{
//			response.put("status", 0);
//			response.put("msg", e.getMessage());
//		}
//		return response.toString();
//	}
//
//	@Authorization
//	@RequestMapping(value = "pay", method = {RequestMethod.POST})
//	public String pay(@CurrentUser User user, @RequestBody String request)
//	{
//		var req = new JSONObject(request);
//		var response = new JSONObject();
//		try
//		{
//			var fid = req.getInt("fid");
//			var money = req.getBigDecimal("money");
//			var status = FinancingStatus.values()[req.getInt("status")];
//			var f = financingService.getFinancing(fid);
//
//			if (status != FinancingStatus.cbConfirm || user.id != f.partyA)
//			{
//				throw new Exception("参数错误");
//			}
//
//			paymentService.transfer(f.partyA, f.partyB, money);
//			financingService.updateStatus(MStatusUtils.getNextFinancingStatus(status), fid);
//
//			response.put("status", 1);
//			response.put("msg", "Success");
//		} catch (Exception e)
//		{
//			response.put("status", 0);
//			response.put("msg", e.getMessage());
//		}
//		return response.toString();
//	}
//
//	@Authorization
//	@RequestMapping(value = "getFinancing", method = {RequestMethod.GET})
//	public String getFinancing(@CurrentUser User user, @RequestParam("fid") int fid)
//	{
//		var response = new JSONObject();
//		try
//		{
//			var f = financingService.getFinancing(fid);
//			var m = mortgageService.getMortgage(f.mid);
//			var fi = f.toJSON();
//			fi.put("mortgage", m.toJSON());
//			response.put("status", 1);
//			response.put("msg", "Success");
//			response.put("financing", fi);
//		} catch (Exception e)
//		{
//			response.put("status", 0);
//			response.put("msg", e.getMessage());
//		}
//		return response.toString();
//	}
//
//	@Authorization
//	@RequestMapping(value = "getFinancingByUser", method = {RequestMethod.GET})
//	public String getFinancingByUser(@CurrentUser User user)
//	{
//		var response = new JSONObject();
//		try
//		{
//			var f = financingService.getFinancingByUser(user.id);
//			List<JSONObject> t = new LinkedList<>();
//			for (var i : f)
//			{
//				var m = mortgageService.getMortgage(i.mid).toJSON();
//				var tmp = i.toJSON();
//				tmp.put("mortgage", m);
//				t.add(tmp);
//			}
//			response.put("status", 1);
//			response.put("msg", "Success");
//			response.put("financing", t);
//		} catch (Exception e)
//		{
//			response.put("status", 0);
//			response.put("msg", e.getMessage());
//		}
//		return response.toString();
//	}
}
