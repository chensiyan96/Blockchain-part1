package com.blockchain.controller;

import com.blockchain.model.Financing;
import com.blockchain.model.User;
import com.blockchain.service.*;
import com.blockchain.utils.Authorization;
import com.blockchain.utils.CurrentUser;
import com.blockchain.utils.JSONUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@CrossOrigin
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
    @Autowired
    private ProductService productService;
    @Autowired
    private CreditService creditService;

    // 创建一个融资申请
	@Authorization
	@RequestMapping(value = "createFinancing", method = { RequestMethod.POST })
	public String createFinancing(@CurrentUser User us, @RequestBody String request)
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
        User um = userService.getUserByEmail(req.getString("MoneyGiver_email").toUpperCase());
        if (um == null) {
            return JSONUtils.failResponse("资金方email不存在");
        }
        if (um.db.role !=  User.Roles.MoneyGiver) {
            return JSONUtils.failResponse("资金方email身份不正确");
        }

        // 3.检查产品和金额
        var product = productService.getProductById(req.getLong("product_id"));
        if (product == null) {
            return JSONUtils.failResponse("此产品不存在");
        }
        var money = req.getBigDecimal("adopt_money");
        if (money.compareTo(BigDecimal.ZERO) <= 0) {
            return JSONUtils.failResponse("金额必须大于0");
        }
        var credit = creditService.getCreditById(us.db.id);
        if (credit.db.approved == null || money.compareTo(credit.db.approved) > 0) {
            return JSONUtils.failResponse("融资金额不能大于授信额度");
        }

        // 4.创建Financing对象
        var fin = new Financing();
        fin.setSupplier(us);
        fin.setCoreBusiness(uc);
        fin.setMoneyGiver(um);
        fin.db.money = money;
        fin.db.days = product.db.days; // 这里需要复制信息而不是用外键，因为产品可能会改
        fin.db.rate = product.db.rate;
        fin.db.createTime = new Timestamp(System.currentTimeMillis());

        // 5.自动通过授信和确权，如果设置了的话
        if (fin.moneyGiver.db.autoPass != 0) { // 资金方可选自动通过授信
            if (fin.coreBusiness.db.autoPass != 0) { // 核心企业可选自动通过确权
                fin.db.status = 2;
            }
            else {
                fin.db.status = 1;
            }
        }
        else {
            fin.db.status = 0;
        }

        // 6.在数据库中添加字段
        if (financingService.insertFinancing(fin) == 0) {
            return JSONUtils.failResponse("创建失败");
        }

        // 7.返回成功提示
        return JSONUtils.successResponse(fin.db.id);
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
        if (fin.coreBusiness.db.autoPass != 0) { // 核心企业可选自动通过确权
            fin.db.status = 2;
        }
        else {
            fin.db.status = 1;
        }
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
        var s = accountService.transferMoney(userService.getUserByID(fin.db.sid), userService.getUserByID(fin.db.mid), fin.db.money);
        if (s != null) {
            return JSONUtils.failResponse(s);
        }

        // 4.更新融资申请状态并返回成功提示
        fin.db.status = 3;
        financingService.updateFinancingPayTime(fin.db.id, new Timestamp(System.currentTimeMillis()));
        financingService.updateFinancingStatus(fin);
        return JSONUtils.successResponse(fin.db.money);
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
        var money = fin.db.money.multiply(fin.db.rate.multiply(new BigDecimal(fin.db.days)).add(BigDecimal.ONE));
        var s = accountService.transferMoney(userService.getUserByID(fin.db.mid), userService.getUserByID(fin.db.sid), money);
        if (s != null) {
            return JSONUtils.failResponse(s);
        }

        // 4.更新融资申请状态并返回成功提示
        fin.db.status = 4;
        financingService.updateFinancingRepayTime(fin.db.id, new Timestamp(System.currentTimeMillis()));
        financingService.updateFinancingStatus(fin);
        return JSONUtils.successResponse(money);
    }

    // 驳回申请
    @Authorization
    @RequestMapping(value = "reject", method = { RequestMethod.POST })
    public String reject(@CurrentUser User user, @RequestBody String request)
    {
        // 2.检查融资申请状态
        var req = new JSONObject(request);
        var fin = financingService.getFinancingByID(req.getLong("id"));
        if (fin == null || (fin.db.sid != user.db.id && fin.db.cid != user.db.id && fin.db.mid != user.db.id)) {
            return JSONUtils.failResponse("您不存在该申请");
        }
        if (fin.db.status == 3) {
            return JSONUtils.failResponse("本申请已经付款不能进行此操作");
        }

        // 4.更新融资申请状态并返回成功提示
        fin.db.status = -1;
        financingService.updateFinancingStatus(fin);
        return JSONUtils.successResponse();
    }

    // 获取当前登录的用户处于某状态下的所有融资申请
	@Authorization
	@RequestMapping(value = "getFinancingByUserAndStatus", method = { RequestMethod.POST })
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
        return JSONUtils.successResponse(JSONUtils.arrayToJSONs(fins));
	}

    // 获取某用户的所有融资申请
    @Authorization
    @RequestMapping(value = "getFinancingByEmail", method = { RequestMethod.POST })
    public String getFinancingByEmail(@CurrentUser User user, @RequestBody String request)
    {
        if (user.db.role != User.Roles.Admin) {
            return JSONUtils.failResponse("只有管理员才能获取");
        }
        var req = new JSONObject(request);
        var user2 = userService.getUserByEmail(req.getString("email").toUpperCase());
        if (user2 == null) {
            return JSONUtils.failResponse("此用户不存在");
        }

        Financing[] fins;
        switch (user2.db.role)
        {
            case Supplier:
                fins = financingService.getFinancingBySid(user2.db.id);
                break;
            case CoreBusiness:
                fins = financingService.getFinancingByCid(user2.db.id);
                break;
            case MoneyGiver:
                fins = financingService.getFinancingByMid(user2.db.id);
                break;
            default:
                return JSONUtils.failResponse("管理员没有融资申请");
        }
        return JSONUtils.successResponse(JSONUtils.arrayToJSONs(fins));
    }
}
