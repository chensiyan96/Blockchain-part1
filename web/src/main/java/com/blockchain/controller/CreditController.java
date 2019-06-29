package com.blockchain.controller;

import com.blockchain.model.User;
import com.blockchain.service.CreditService;
import com.blockchain.service.UserService;
import com.blockchain.utils.Authorization;
import com.blockchain.utils.CurrentUser;
import com.blockchain.utils.JSONUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "api/credit")
public class CreditController
{
    @Autowired
    private UserService userService;
    @Autowired
    private CreditService creditService;

    @Authorization
    @RequestMapping(value = "apply", method = { RequestMethod.POST })
    public String apply(@CurrentUser User user, @RequestBody String request)
    {
        if (user.db.role != User.Roles.Supplier) {
            return JSONUtils.failResponse("只有供应商才能申请限额");
        }
        var req = new JSONObject(request);
        var credit = creditService.getCreditById(user.db.id);
        credit.db.applied = req.getBigDecimal("money");
        creditService.updateCreditInfo(credit);
        return JSONUtils.successResponse();
    }

    @Authorization
    @RequestMapping(value = "approve", method = { RequestMethod.POST })
    public String approve(@CurrentUser User user, @RequestBody String request)
    {
        if (user.db.role != User.Roles.Admin) {
            return JSONUtils.failResponse("只有管理员才能设置限额");
        }
        var req = new JSONObject(request);
        var user2 = userService.getUserByID(req.getLong("id"));
        if (user2 == null) {
            return JSONUtils.failResponse("该用户不存在");
        }
        var credit = creditService.getCreditById(user2.db.id);
        credit.db.approved = req.getBigDecimal("money");
        creditService.updateCreditInfo(credit);
        return JSONUtils.successResponse();
    }

    @Authorization
    @RequestMapping(value = "rank", method = { RequestMethod.POST })
    public String rank(@CurrentUser User user, @RequestBody String request)
    {
        if (user.db.role != User.Roles.Admin) {
            return JSONUtils.failResponse("只有管理员才能设置评级");
        }
        var req = new JSONObject(request);
        var user2 = userService.getUserByID(req.getLong("id"));
        if (user2 == null) {
            return JSONUtils.failResponse("该用户不存在");
        }
        var credit = creditService.getCreditById(user2.db.id);
        credit.db.rank = req.getString("rank");
        creditService.updateCreditInfo(credit);
        return JSONUtils.successResponse();
    }

    // 获取当前用户的授信记录
    @Authorization
    @RequestMapping(value = "getRecordCredit", method = { RequestMethod.GET })
    public String getRecordCredit(@CurrentUser User user)
    {
        return JSONUtils.successResponse(JSONUtils.arrayToJSONs(creditService.getRecordCredit(user.db.id)));
    }
}
