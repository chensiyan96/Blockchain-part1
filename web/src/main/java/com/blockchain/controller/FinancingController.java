package com.blockchain.controller;

import com.blockchain.model.Financing;
import com.blockchain.model.User;
import com.blockchain.service.AccountService;
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
    @Autowired
    private AccountService accountService;

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
        fin.db.pid = req.getLong("product_id"); //todo：检查产品是否存在
        fin.db.money = req.getBigDecimal("adopt_money");
        fin.db.createTime = new Date(System.currentTimeMillis());
        fin.db.status = 0;

        // 4.如果下两步设置了自动通过，则直接通过
        // todo

        // 5.在数据库中添加字段
        if (financingService.insertFinancing(fin) == 0) {
            return JSONUtils.failResponse("创建失败");
        }

        // 6.返回成功提示
        return JSONUtils.successResponse();
	}

    // 授信（第一步）
    @Authorization
    @RequestMapping(value = "credit", method = { RequestMethod.POST })
    public String credit(@CurrentUser User user, @RequestBody String request)
    {
        // 1.检查用户身份
        if (user.db.role != User.Roles.MoneyGiver) {
            return JSONUtils.failResponse("必须由资金方授信");
        }

        // 2.检查融资申请状态
        var req = new JSONObject(request);
        var fin = financingService.getFinancingByID(req.getLong("id"));
        if (fin == null || fin.db.mid != user.db.id) {
            return JSONUtils.failResponse("您不存在该申请");
        }
        if (fin.db.status != 0) {
            return JSONUtils.failResponse("本申请不该进行此操作");
        }

        // 3.更新融资申请状态
        // todo： 如果下一步设置了自动通过，则直接通过
        fin.db.status = 1;
        financingService.updateFinancingStatus(fin);

        // 4.返回成功提示
        return JSONUtils.successResponse();
    }

	// 确权（第二步）
	@Authorization
	@RequestMapping(value = "confirm", method = { RequestMethod.POST })
	public String confirm(@CurrentUser User user, @RequestBody String request)
    {
        // 1.检查用户身份
        if (user.db.role != User.Roles.CoreBusiness) {
            return JSONUtils.failResponse("必须由核心企业确权");
        }

        // 2.检查融资申请状态
        var req = new JSONObject(request);
        var fin = financingService.getFinancingByID(req.getLong("id"));
        if (fin == null || fin.db.cid != user.db.id) {
            return JSONUtils.failResponse("您不存在该申请");
        }
        if (fin.db.status != 1) {
            return JSONUtils.failResponse("本申请不该进行此操作");
        }

        // 3.更新融资申请状态
        fin.db.status = 2;
        financingService.updateFinancingStatus(fin);

        // 4.返回成功提示
        return JSONUtils.successResponse();
    }

    // 放款（第三步）
	@Authorization
	@RequestMapping(value = "pay", method = { RequestMethod.POST })
	public String pay(@CurrentUser User user, @RequestBody String request)
	{
        // 1.检查用户身份
        if (user.db.role != User.Roles.MoneyGiver) {
            return JSONUtils.failResponse("必须由资金方放款");
        }

        // 2.检查融资申请状态
        var req = new JSONObject(request);
        var fin = financingService.getFinancingByID(req.getLong("id"));
        if (fin == null || fin.db.mid != user.db.id) {
            return JSONUtils.failResponse("您不存在该申请");
        }
        if (fin.db.status != 2) {
            return JSONUtils.failResponse("本申请不该进行此操作");
        }

        // 3.放款
        if (!accountService.transferMoney(fin.db.sid, fin.db.mid, fin.db.money)) {
            return JSONUtils.failResponse("账户余额不足");
        }

        // 4.更新融资申请状态
        fin.db.status = 3;
        financingService.updateFinancingStatus(fin);

        // 5.返回成功提示
        return JSONUtils.successResponse();
	}

    // 还款（第四步）
    @Authorization
    @RequestMapping(value = "repay", method = { RequestMethod.POST })
    public String repay(@CurrentUser User user, @RequestBody String request)
    {
        // 1.检查用户身份
        if (user.db.role != User.Roles.Supplier) {
            return JSONUtils.failResponse("必须由供应商放款");
        }

        // 2.检查融资申请状态
        var req = new JSONObject(request);
        var fin = financingService.getFinancingByID(req.getLong("id"));
        if (fin == null || fin.db.sid != user.db.id) {
            return JSONUtils.failResponse("您不存在该申请");
        }
        if (fin.db.status != 3) {
            return JSONUtils.failResponse("本申请不该进行此操作");
        }

        // 3.还款
        if (!accountService.transferMoney(fin.db.mid, fin.db.sid, fin.db.money)) {
            return JSONUtils.failResponse("账户余额不足");
        }

        // 4.更新融资申请状态
        fin.db.status = 4;
        financingService.updateFinancingStatus(fin);

        // 5.返回成功提示
        return JSONUtils.successResponse();
    }

    // 获取当前登录的用户处于[参数status]状态下的所有融资申请
	@Authorization
	@RequestMapping(value = "getFinancingByUserAndStatus", method = { RequestMethod.GET })
	public String getFinancingByUserAndStatus(@CurrentUser User user, @RequestBody String request)
	{
        var req = new JSONObject(request);
	    var status = (byte) req.getInt("status");
        Financing[] fins;
        switch (user.db.role)
        {
            case Supplier:
                fins = financingService.getFinancingBySidAndStatus(user.db.id, status);
                break;
            case CoreBusiness:
                fins = financingService.getFinancingByCidAndStatus(user.db.id, status);
                break;
            case MoneyGiver:
                fins = financingService.getFinancingByMidAndStatus(user.db.id, status);
                break;
            default:
                return JSONUtils.failResponse("管理员没有融资申请");
        }
        return JSONUtils.successResponse("data", JSONUtils.arrayToJSONs(fins));
	}
}
