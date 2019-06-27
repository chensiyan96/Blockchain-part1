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

    // 添加准入规则
    @Authorization
    @RequestMapping(value = "insertAccessRule", method = { RequestMethod.POST })
    public String insertAccessRule(@CurrentUser User user, @RequestBody String request)
    {
        if (user.db.role != User.Roles.Admin) {
            return JSONUtils.failResponse("只有管理员才能添加准入规则");
        }
        var req = new JSONObject(request);
        var id = accessRuleService.insertAccessRule(req.getString("content"));
        if (id == 0) {
            return JSONUtils.failResponse("添加失败");
        }
        return JSONUtils.successResponse(id);
    }

    // 修改准入规则
    @Authorization
    @RequestMapping(value = "updateAccessRule", method = { RequestMethod.POST })
    public String updateAccessRule(@CurrentUser User user, @RequestBody String request)
    {
        if (user.db.role != User.Roles.Admin) {
            return JSONUtils.failResponse("只有管理员才能修改准入规则");
        }
        var req = new JSONObject(request);
        var id = req.getLong("id");
        if (accessRuleService.getAccessRuleById(id) == null) {
            return JSONUtils.failResponse("不存在该规则");
        }
        var content = req.getString("content");
        accessRuleService.updateAccessRule(id, content);
        return JSONUtils.successResponse();
    }

    // 删除准入规则
    @Authorization
    @RequestMapping(value = "deleteAccessRule", method = { RequestMethod.POST })
    public String deleteAccessRule(@CurrentUser User user, @RequestBody String request)
    {
        if (user.db.role != User.Roles.Admin) {
            return JSONUtils.failResponse("只有管理员才能删除准入规则");
        }
        var req = new JSONObject(request);
        var id = req.getLong("id");
        if (accessRuleService.getAccessRuleById(id) == null) {
            return JSONUtils.failResponse("不存在该规则");
        }
        accessRuleService.deleteAccessRule(id);
        return JSONUtils.successResponse();
    }

    // 获取所有准入规则
    @RequestMapping(value = "getAllAccessRules", method = { RequestMethod.GET })
    public String getAllAccessRules()
    {
        return JSONUtils.successResponse(JSONUtils.arrayToJSONs(accessRuleService.getAllAccessRules()));
    }
}
