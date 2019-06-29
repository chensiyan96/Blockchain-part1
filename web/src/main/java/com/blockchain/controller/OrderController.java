package com.blockchain.controller;

import com.blockchain.model.Order;
import com.blockchain.model.User;
import com.blockchain.service.AccountService;
import com.blockchain.service.OrderService;
import com.blockchain.service.UserService;
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
@RequestMapping(value = "api/order")
public class OrderController
{
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountService accountService;

    // 创建一个订单
    @Authorization
    @RequestMapping(value = "createOrder", method = { RequestMethod.POST })
    public String createOrder(@CurrentUser User uc, @RequestBody String request)
    {
        // 1.检查用户身份
        if (uc.db.role != User.Roles.CoreBusiness) {
            return JSONUtils.failResponse("必须由核心企业提交订单");
        }

        // 2.检查申请的另外一方是否存在和身份是否正确
        var req = new JSONObject(request);
        User us = userService.getUserByEmail(req.getString("Supplier_email").toUpperCase());
        if (us == null) {
            return JSONUtils.failResponse("供应商email不存在");
        }
        if (us.db.role != User.Roles.Supplier) {
            return JSONUtils.failResponse("供应商email身份不正确");
        }

        // 3.检查金额
        var money = req.getBigDecimal("money");
        if (money.compareTo(BigDecimal.ZERO) <= 0) {
            return JSONUtils.failResponse("金额必须大于0");
        }

        // 4.创建order对象
        var order = new Order();
        order.setSupplier(us);
        order.setCoreBusiness(uc);
        order.db.money = money;
        order.db.number = req.getLong("number");
        order.db.createTime = new Timestamp(System.currentTimeMillis());
        order.db.days = req.getInt("days");
        order.db.status = 0;

        // 5.在数据库中添加字段
        if (orderService.insertOrder(order) == 0) {
            return JSONUtils.failResponse("创建失败");
        }

        // 6.返回成功提示
        return JSONUtils.successResponse(order.db.id);
    }

    // 订单付款
    @Authorization
    @RequestMapping(value = "pay", method = { RequestMethod.POST })
    public String pay(@CurrentUser User user, @RequestBody String request)
    {
        // 1.检查用户身份
        if (user.db.role != User.Roles.CoreBusiness) {
            return JSONUtils.failResponse("必须由核心企业付款");
        }

        // 2.检查融资申请状态
        var req = new JSONObject(request);
        var order = orderService.getOrderByID(req.getLong("id"));
        if (order == null || order.db.cid != user.db.id) {
            return JSONUtils.failResponse("您不存在该申请");
        }
        if (order.db.status != 0) {
            return JSONUtils.failResponse("已经付款了");
        }

        // 3.付款
        var s = accountService.transferMoney(userService.getUserByID(order.db.sid), userService.getUserByID(order.db.cid), order.db.money);
        if (s != null) {
            return JSONUtils.failResponse(s);
        }

        // 4.更新订单状态
        order.db.status = 1;
        orderService.updateOrderStatus(order);

        // 5.返回成功提示
        return JSONUtils.successResponse(order.db.money);
    }

    // 获取当前登录的用户处于[参数status]状态下的所有订单
    @Authorization
    @RequestMapping(value = "getOrderByUserAndStatus", method = { RequestMethod.POST })
    public String getOrderByUserAndStatus(@CurrentUser User user, @RequestBody String request)
    {
        var req = new JSONObject(request);
        var status = (byte) req.getInt("status");
        Order[] orders;
        switch (user.db.role)
        {
            case Supplier:
                orders = orderService.getOrderBySidAndStatus(user.db.id, status);
                break;
            case CoreBusiness:
                orders = orderService.getOrderByCidAndStatus(user.db.id, status);
                break;
            case MoneyGiver:
                return JSONUtils.failResponse("资金方没有订单");
            default:
                return JSONUtils.failResponse("管理员没有订单");
        }
        return JSONUtils.successResponse(JSONUtils.arrayToJSONs(orders));
    }

    // 获取某用户的所有融资申请
    @Authorization
    @RequestMapping(value = "getOrderByEmail", method = { RequestMethod.POST })
    public String getOrderByEmail(@CurrentUser User user, @RequestBody String request)
    {
        if (user.db.role != User.Roles.Admin) {
            return JSONUtils.failResponse("只有管理员才能获取");
        }
        var req = new JSONObject(request);
        var user2 = userService.getUserByEmail(req.getString("email").toUpperCase());
        if (user2 == null) {
            return JSONUtils.failResponse("此用户不存在");
        }

        Order[] fins;
        switch (user2.db.role)
        {
            case Supplier:
                fins = orderService.getOrderBySid(user2.db.id);
                break;
            case CoreBusiness:
                fins = orderService.getOrderByCid(user2.db.id);
                break;
            case MoneyGiver:
                return JSONUtils.failResponse("资金方没有订单");
            default:
                return JSONUtils.failResponse("管理员没有融资申请");
        }
        return JSONUtils.successResponse(JSONUtils.arrayToJSONs(fins));
    }
}
