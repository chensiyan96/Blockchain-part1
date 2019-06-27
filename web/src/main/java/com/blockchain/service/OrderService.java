package com.blockchain.service;

import com.blockchain.dao.OrderMapper;
import com.blockchain.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService
{
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserService userService;

    public long insertOrder(Order order)
    {
        if (orderMapper.insertOrder(order.db)) {
            return order.db.id = orderMapper.getlastInsertId();
        }
        return 0;
    }

    public Order getOrderByID(long id)
    {
        var db = orderMapper.getOrderById(id);
        if (db == null) {
            return null;
        }
        var order = new Order(db);
        order.setSupplier(userService.getUserByID(order.db.sid));
        order.setCoreBusiness(userService.getUserByID(order.db.cid));
        return order;
    }

    public void updateOrderStatus(Order order)
    {
        orderMapper.updateOrderState(order.db.id, order.db.status);
    }

    public Order[] getOrderBySidAndStatus(long sid, byte status)
    {
        return constructOrderArray(orderMapper.getOrderBySidAndStatus(sid, status));
    }

    public Order[] getOrderByCidAndStatus(long cid, byte status)
    {
        return constructOrderArray(orderMapper.getOrderByCidAndStatus(cid, status));
    }

    private Order[] constructOrderArray(Order.DataBase[] dbs)
    {
        var orders = new Order[dbs.length];
        for (int i = 0; i < dbs.length; i++) {
            orders[i] = new Order(dbs[i]);
            orders[i].setSupplier(userService.getUserByID(orders[i].db.sid));
            orders[i].setCoreBusiness(userService.getUserByID(orders[i].db.cid));
        }
        return orders;
    }
}
