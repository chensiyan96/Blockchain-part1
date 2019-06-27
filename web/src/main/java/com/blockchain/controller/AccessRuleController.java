package com.blockchain.controller;

import com.blockchain.model.User;
import com.blockchain.service.AccessRuleService;
import com.blockchain.utils.Authorization;
import com.blockchain.utils.CurrentUser;
import com.blockchain.utils.JSONUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "api/access")
public class AccessRuleController
{
    @Autowired
    private AccessRuleService accessRuleService;

    // 设置准入规则
    @Authorization
    @RequestMapping(value = "setAccessRule", method = { RequestMethod.POST })
    public String setAccessRule(@CurrentUser User user, @RequestBody String request)
    {
        // 1.检查用户身份
        if (user.db.role != User.Roles.Admin) {
            return JSONUtils.failResponse("只有管理员才能设置准入规则");
        }

        // 2.设置准入规则并返回成功提示
        var req = new JSONObject(request);
        accessRuleService.setAccessRule(req.getString("rule"));
        return JSONUtils.successResponse();
    }

    // 获取准入规则
    @RequestMapping(value = "getAccessRule", method = { RequestMethod.GET })
    public String getAccessRule()
    {
        return JSONUtils.successResponse(accessRuleService.getAccessRule());
    }
}
