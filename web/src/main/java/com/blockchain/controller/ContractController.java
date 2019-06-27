package com.blockchain.controller;

import com.blockchain.model.Contract;
import com.blockchain.model.User;
import com.blockchain.service.ContractService;
import com.blockchain.service.UserService;
import com.blockchain.utils.Authorization;
import com.blockchain.utils.CurrentUser;
import com.blockchain.utils.JSONUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@CrossOrigin
@RestController
@RequestMapping(value = "api/contract")
public class ContractController
{
    @Autowired
    private UserService userService;
    @Autowired
    private ContractService contractService;

    // 创建一个合同
    @Authorization
    @RequestMapping(value = "createContract", method = { RequestMethod.POST })
    public String createContract(@CurrentUser User user, @RequestBody String request)
    {
        // 1.检查用户身份
        if (user.db.role != User.Roles.CoreBusiness && user.db.role != User.Roles.Supplier) {
            return JSONUtils.failResponse("必须由核心企业或供应商提交合同");
        }

        // 2.检查申请的另外一方是否存在
        var req = new JSONObject(request);
        var email = req.getString("another_email").toUpperCase();
        var another_user = userService.getUserByEmail(email);
        if (another_user == null) {
            return JSONUtils.failResponse("合同的另一方不存在");
        }

        // 3.创建contract对象
        var contract = new Contract();
        if (user.db.role == User.Roles.CoreBusiness)
        {
            if (another_user.db.role != User.Roles.Supplier) {
                return JSONUtils.failResponse("合同的另一方身份不正确");
            }
            contract.setCoreBusiness(user);
            contract.setSupplier(another_user);
        }
        else
        {
            if (another_user.db.role != User.Roles.CoreBusiness) {
                return JSONUtils.failResponse("合同的另一方身份不正确");
            }
            contract.setSupplier(user);
            contract.setCoreBusiness(another_user);
        }
        contract.db.content = req.getString("content");
        contract.db.createTime = new Timestamp(System.currentTimeMillis());

        // 4.合同入链并返回成功提示
        // todo
        return JSONUtils.successResponse(contract.db.id);
    }
    
    // 获取当前登录的用户签署过的所有合同
    @Authorization
    @RequestMapping(value = "getContractByUser", method = { RequestMethod.GET })
    public String getContractByUser(@CurrentUser User user)
    {
        Contract[] contracts;
        switch (user.db.role)
        {
            case Supplier:
                contracts = contractService.getContractBySid(user.db.id);
                break;
            case CoreBusiness:
                contracts = contractService.getContractByCid(user.db.id);
                break;
            case MoneyGiver:
                return JSONUtils.failResponse("资金方没有合同");
            default:
                return JSONUtils.failResponse("管理员没有合同");
        }
        return JSONUtils.successResponse(JSONUtils.arrayToJSONs(contracts));
    }
}
